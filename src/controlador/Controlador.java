/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import modelo.Cliente;
import modelo.Modelo;
import vista.Vista;
import modelo.Action;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.format.DateTimeFormatter;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Controlador principal que coordina la interacción entre el modelo y la vista.
 * Implementa el patrón MVC como capa de controlador, gestionando los eventos
 * de la interfaz de usuario y actualizando el modelo y vista correspondientemente.
 * 
 * @author mi pc
 */
public class Controlador implements ActionListener {
    private Modelo modelo;
    private Vista vista;
    private DateTimeFormatter formatter;
    /**
     * Constructor que inicializa el controlador y configura los componentes.
     * 
     * @param modelo Instancia del modelo del sistema
     * @param vista Instancia de la vista del sistema
     */
    public Controlador(Modelo modelo, Vista vista) {
        this.modelo = modelo;
        this.vista = vista;
        this.formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        // Configurar componentes de la interfaz
        vista.getTxtId().setEditable(false);

        // Configurar listeners de botones
        this.vista.getBtnRegistrar().addActionListener(e -> registrar());
        this.vista.getBtnAtender().addActionListener(e -> atender());
        this.vista.getBtnEliminar().addActionListener(e -> eliminar());
        this.vista.getBtnBuscar().addActionListener(e -> buscar());
        this.vista.getBtnConsultarHistorial().addActionListener(e -> consultarH());
        this.vista.getBtnDeshacer().addActionListener(e -> undo());
        this.vista.getBtnGenerarEstadisticas().addActionListener(e -> generarE());

        // Inicializar componentes de la interfaz
        configurarCombobox();
        configurarTablas();

        // Cargar datos iniciales
        actualizarTablaClientesEnEspera();
        actualizarTablaClientesAtendidos();
        actualizarIdPrimerCliente();
    }

    /**
     * Registra un nuevo cliente en el sistema.
     * Valida los datos de entrada y actualiza la interfaz.
     */
    public void registrar() {
        try {
            String nombre = vista.getTxtNombre().getText().trim();
            if (nombre.isEmpty()) {
                JOptionPane.showMessageDialog(vista, "El nombre no puede estar vacío", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Cliente.TipoSolicitud tipoSolicitud = obtenerTipoSolicitudSeleccionado();
            Cliente.Prioridad prioridad = obtenerPrioridadSeleccionada();

            Cliente nuevoCliente = modelo.agregarCliente(nombre, tipoSolicitud, prioridad);
            if (nuevoCliente != null) {
                actualizarTablaClientesEnEspera();
                actualizarIdPrimerCliente();
                limpiarCamposRegistro();
                JOptionPane.showMessageDialog(vista,
                        "Cliente registrado exitosamente\nID: " + nuevoCliente.getId(),
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vista, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Atiende al siguiente cliente según el sistema de prioridades.
     * Utiliza el sistema híbrido de puntos para determinar el orden de atención.
     */
    private void atender() {
        try {
            if (!modelo.getGestionClientes().hayClientesEnEspera()) {
                JOptionPane.showMessageDialog(vista,
                        "No hay clientes en espera para atender",
                        "Información",
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            Cliente clienteAtendido = modelo.atenderCliente();
            if (clienteAtendido != null) {
                actualizarTablaClientesEnEspera();
                actualizarIdPrimerCliente();
                actualizarTablaClientesAtendidos();

                // Mensaje informativo sobre el sistema de prioridades
                String mensajeSistema = "";
                if (clienteAtendido.getPrioridad() == Cliente.Prioridad.URGENTE) {
                    mensajeSistema = "\n Prioridad URGENTE + posición en cola";
                } else {
                    mensajeSistema = "\n Prioridad NORMAL + posición en cola";
                }

                JOptionPane.showMessageDialog(vista,
                        "CLIENTE ATENDIDO\n"
                        + "Nombre: " + clienteAtendido.getNombre() + "\n"
                        + "ID: " + clienteAtendido.getId() + "\n"
                        + "Tipo: " + clienteAtendido.getTipoSolicitud() 
                        + mensajeSistema,
                        "Cliente Atendido",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vista, "Error al atender cliente: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Elimina un cliente seleccionado de la cola de espera.
     * Requiere que el usuario seleccione un cliente de la tabla.
     */
    private void eliminar() {
        try {
            // Obtener la fila seleccionada en la tabla de clientes en espera
            int filaSeleccionada = vista.getTblClientesEnEspera().getSelectedRow();

            if (filaSeleccionada == -1) {
                JOptionPane.showMessageDialog(vista,
                        "Por favor, seleccione un cliente de la tabla para eliminar",
                        "Selección Requerida",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Obtener el ID del cliente seleccionado (columna 0 es ID)
            String idSeleccionado = (String) vista.getTblClientesEnEspera().getValueAt(filaSeleccionada, 0);

            // Eliminar el cliente seleccionado directamente
            Cliente clienteEliminado = modelo.eliminarClientePorId(idSeleccionado);

            if (clienteEliminado != null) {
                // Actualizar las vistas
                actualizarTablaClientesEnEspera();
                actualizarIdPrimerCliente();

                // Mensaje simple de confirmación
                JOptionPane.showMessageDialog(vista,
                        "Cliente eliminado: " + clienteEliminado.getNombre(),
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(vista,
                        "Error: No se pudo eliminar el cliente seleccionado",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vista,
                    "Error al eliminar cliente: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    /**
     * Busca un cliente en el historial por su ID.
     * Muestra los resultados en un cuadro de diálogo.
     */
    private void buscar() {
        try {
            String id = vista.getTxtBuscarId().getText().trim();
            if (id.isEmpty()) {
                JOptionPane.showMessageDialog(vista, "Ingrese un ID para buscar", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Cliente cliente = modelo.buscarCliente(id);
            if (cliente != null) {
                JOptionPane.showMessageDialog(vista,
                        "=== CLIENTE ENCONTRADO ===\n" + cliente.toString(),
                        "Resultado de Búsqueda",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(vista,
                        "Cliente con ID " + id + " no encontrado",
                        "Búsqueda",
                        JOptionPane.WARNING_MESSAGE);
            }
            vista.getTxtBuscarId().setText("");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vista, "Error en búsqueda: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Consulta y muestra el historial completo de acciones realizadas.
     */
    private void consultarH() {
        try {
            String historial = modelo.obtenerHistorialAcciones();
            vista.getTxtHistorialAcciones().setText(historial);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vista, "Error al consultar historial: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Deshace la última acción realizada en el sistema.
     * Solicita confirmación al usuario antes de ejecutar.
     */
    private void undo() {
        try {
            if (!modelo.hayAccionesParaDeshacer()) {
                JOptionPane.showMessageDialog(vista,
                        "No hay acciones para deshacer",
                        "Información",
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            Action ultimaAccion = modelo.obtenerUltimaAccion();
            if (ultimaAccion == null) {
                JOptionPane.showMessageDialog(vista,
                        "No se pudo obtener la última acción",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Mostrar confirmación al usuario
            int confirmacion = JOptionPane.showConfirmDialog(vista,
                    "¿Está seguro de que desea deshacer la siguiente acción?\n\n"
                    + "Acción: " + ultimaAccion.getTipo() + "\n"
                    + "Cliente: " + ultimaAccion.getCliente().getNombre() + " (ID: " + ultimaAccion.getCliente().getId() + ")\n"
                    + "Fecha: " + ultimaAccion.getFechaHora().format(formatter),
                    "Confirmar Deshacer",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);

            if (confirmacion == JOptionPane.YES_OPTION) {
                boolean exito = modelo.deshacerUltimaAccion();

                if (exito) {
                    // ACTUALIZAR TODAS LAS TABLAS Y VISTAS
                    actualizarTablaClientesEnEspera();
                    actualizarIdPrimerCliente();
                    actualizarTablaClientesAtendidos();

                    // Si hay área de texto de historial, actualizarla también
                    if (vista.getTxtHistorialAcciones() != null) {
                        consultarH(); // Actualizar el historial de acciones
                    }

                    // Si hay área de texto de estadísticas, actualizarla
                    if (vista.getTxtEstadisticas() != null) {
                        generarE(); // Actualizar estadísticas
                    }

                    JOptionPane.showMessageDialog(vista,
                            "Acción deshecha exitosamente",
                            "Éxito",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(vista,
                            "Error al deshacer la acción",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vista,
                    "Error al deshacer: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    /**
     * Genera y muestra las estadísticas del sistema.
     */
    private void generarE() {
        try {
            String estadisticas = modelo.generarEstadisticas();
            vista.getTxtEstadisticas().setText(estadisticas);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vista, "Error al generar estadísticas: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Configura los ComboBox de la interfaz con las opciones disponibles.
     */
    private void configurarCombobox() {
        //combobox de tipo de solicitud
        vista.getCmbTipoSolicitud().addItem("SOPORTE");
        vista.getCmbTipoSolicitud().addItem("MANTENIMIENTO");
        vista.getCmbTipoSolicitud().addItem("RECLAMO");

        //combobox de prioridad
        vista.getCmbPrioridad().addItem("NORMAL");
        vista.getCmbPrioridad().addItem("URGENTE");
    }

    /**
     * Configura los modelos de tabla para clientes en espera y atendidos.
     */
    private void configurarTablas() {
        // Modelo para tabla de clientes en espera
        String[] columnasEspera = {"ID", "Nombre", "Tipo Solicitud", "Prioridad", "Hora Ingreso"};
        DefaultTableModel modelEspera = new DefaultTableModel(columnasEspera, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Hacer la tabla no editable
            }
        };
        vista.getTblClientesEnEspera().setModel(modelEspera);

        // Modelo para tabla de clientes atendidos
        String[] columnasAtendidos = {"ID", "Nombre", "Tipo Solicitud", "Prioridad", "Hora Ingreso", "Hora Atención", "Tiempo Espera (min)"};
        DefaultTableModel modelAtendidos = new DefaultTableModel(columnasAtendidos, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Hacer la tabla no editable
            }
        };
        vista.getTblClientesAtendidos().setModel(modelAtendidos);
    }

    // ==================== MÉTODOS AUXILIARES ====================

    /**
     * Actualiza la tabla de clientes en espera con los datos actuales del modelo.
     */
    private void actualizarTablaClientesEnEspera() {
        DefaultTableModel model = (DefaultTableModel) vista.getTblClientesEnEspera().getModel();
        model.setRowCount(0); // Limpiar tabla

        List<Cliente> clientesEnEspera = modelo.getGestionClientes().getClientesEnEspera();
        for (Cliente cliente : clientesEnEspera) {
            Object[] fila = {
                cliente.getId(),
                cliente.getNombre(),
                cliente.getTipoSolicitud(),
                cliente.getPrioridad(),
                cliente.getFechaLlegada().format(formatter)
            };
            model.addRow(fila);
        }

        // Forzar repintado de la tabla
        vista.getTblClientesEnEspera().repaint();
    }

    /**
     * Actualiza la tabla de clientes atendidos con los datos actuales del historial.
     */
    private void actualizarTablaClientesAtendidos() {
        DefaultTableModel model = (DefaultTableModel) vista.getTblClientesAtendidos().getModel();
        model.setRowCount(0); // Limpiar tabla

        List<Cliente> clientesAtendidos = modelo.getHistorial().getClientesAtendidos();
        for (Cliente cliente : clientesAtendidos) {
            Object[] fila = {
                cliente.getId(),
                cliente.getNombre(),
                cliente.getTipoSolicitud(),
                cliente.getPrioridad(),
                cliente.getFechaLlegada().format(formatter),
                cliente.getFechaAtencion() != null ? cliente.getFechaAtencion().format(formatter) : "N/A",
                cliente.calcularTiempoAtencion() > 0 ? cliente.calcularTiempoAtencion() + " min" : "N/A"
            };
            model.addRow(fila);
        }

        // Forzar repintado de la tabla
        vista.getTblClientesAtendidos().repaint();
    }

    /**
     * Limpia los campos del formulario de registro.
     */
    private void limpiarCamposRegistro() {
        vista.getTxtNombre().setText("");
        vista.getCmbTipoSolicitud().setSelectedIndex(0);
        vista.getCmbPrioridad().setSelectedIndex(0);
    }

    /**
     * Obtiene el tipo de solicitud seleccionado en el ComboBox.
     * 
     * @return TipoSolicitud seleccionado
     */
    private Cliente.TipoSolicitud obtenerTipoSolicitudSeleccionado() {
        String seleccion = (String) vista.getCmbTipoSolicitud().getSelectedItem();
        return Cliente.TipoSolicitud.valueOf(seleccion);
    }

    /**
     * Obtiene la prioridad seleccionada en el ComboBox.
     * 
     * @return Prioridad seleccionada
     */
    private Cliente.Prioridad obtenerPrioridadSeleccionada() {
        String seleccion = (String) vista.getCmbPrioridad().getSelectedItem();
        return Cliente.Prioridad.valueOf(seleccion);
    }

    /**
     * Actualiza el campo de ID con el primer cliente en la cola de espera.
     */
    private void actualizarIdPrimerCliente() {
        Cliente primerCliente = modelo.getGestionClientes().obtenerSiguienteCliente();
        if (primerCliente != null) {
            vista.getTxtId().setText(primerCliente.getId());
        } else {
            vista.getTxtId().setText("No hay clientes en espera");
        }
    }

    /**
     * Método requerido por la interfaz ActionListener.
     * No se utiliza directamente ya que se usan lambdas para los listeners.
     * 
     * @param e Evento de acción
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
