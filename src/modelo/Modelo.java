/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 * Clase principal del modelo que coordina todas las funcionalidades del
 * sistema. Implementa el patrón MVC como capa de modelo, integrando la gestión
 * de clientes, historial y registro de acciones.
 *
 * @author mi pc
 */
public class Modelo {

    private GestionClientes gestionClientes;
    private Historial historial;
    private RegistroAcciones registroAcciones;

    /**
     * Constructor que inicializa todos los componentes del modelo.
     */
    public Modelo() {
        this.gestionClientes = new GestionClientes();
        this.historial = new Historial();
        this.registroAcciones = new RegistroAcciones();
    }

    /**
     * Obtiene la instancia de gestión de clientes.
     *
     * @return GestionClientes instancia actual
     */
    public GestionClientes getGestionClientes() {
        return gestionClientes;
    }

    /**
     * Obtiene la instancia del historial.
     *
     * @return Historial instancia actual
     */
    public Historial getHistorial() {
        return historial;
    }

    /**
     * Obtiene la instancia del registro de acciones.
     *
     * @return RegistroAcciones instancia actual
     */
    public RegistroAcciones getRegistroAcciones() {
        return registroAcciones;
    }

    /**
     * Agrega un nuevo cliente al sistema y registra la acción.
     *
     * @param nombre Nombre del cliente
     * @param tipoSolicitud Tipo de solicitud del cliente
     * @param prioridad Prioridad del cliente
     * @return Cliente agregado, o null si no se pudo agregar
     */
    public Cliente agregarCliente(String nombre, Cliente.TipoSolicitud tipoSolicitud, Cliente.Prioridad prioridad) {
        Cliente nuevoCliente = gestionClientes.agregarCliente(nombre, tipoSolicitud, prioridad);
        if (nuevoCliente != null) {
            registroAcciones.registrarAction(Action.TipoAccion.REGISTRAR, nuevoCliente);
        }
        return nuevoCliente;
    }

    /**
     * Elimina un cliente de la cola por su ID y registra la acción.
     *
     * @param id ID del cliente a eliminar
     * @return Cliente eliminado, o null si no se encontró
     */
    public Cliente eliminarClientePorId(String id) {
        Cliente clienteEliminado = gestionClientes.eliminarClientePorIdCola(id);
        if (clienteEliminado != null) {
            registroAcciones.registrarAction(Action.TipoAccion.ELIMINAR, clienteEliminado);
        }
        return clienteEliminado;
    }

    /**
     * Atiende al siguiente cliente según el sistema de prioridades y registra
     * la acción.
     *
     * @return Cliente atendido, o null si no hay clientes en espera
     */
    public Cliente atenderCliente() {
        Cliente clienteAtendido = gestionClientes.atenderCliente();
        if (clienteAtendido != null) {
            historial.agregarAtendido(clienteAtendido);
            registroAcciones.registrarAction(Action.TipoAccion.ATENDER, clienteAtendido);
        }
        return clienteAtendido;
    }

    /**
     * Busca un cliente en el historial por su ID.
     *
     * @param id ID del cliente a buscar
     * @return Cliente encontrado, o null si no existe
     */
    public Cliente buscarCliente(String id) {
        return historial.buscarCliente(id);
    }

    /**
     * Genera estadísticas completas del sistema.
     *
     * @return String con las estadísticas formateadas
     */
    public String generarEstadisticas() {
        StringBuilder estadistica = new StringBuilder();
        estadistica.append("=== ESTADÍSTICAS DEL SISTEMA ===\n\n");
        estadistica.append("Clientes en espera: ").append(gestionClientes.getCantidadClientesEnEspera()).append("\n");
        estadistica.append("Clientes atendidos: ").append(historial.getCantidadAtendidos()).append("\n");
        estadistica.append("Promedio tiempo atención: ").append(String.format("%.2f", historial.getPromedioTiempoAtencion())).append(" minutos\n");
        estadistica.append("Acciones registradas: ").append(registroAcciones.getCantidadActions()).append("\n");
        return estadistica.toString();
    }

    /**
     * Obtiene el historial completo de acciones.
     *
     * @return String con el historial de acciones formateado
     */
    public String obtenerHistorialAcciones() {
        return registroAcciones.getActionsComoTexto();
    }

    /**
     * Deshace la última acción realizada en el sistema.
     *
     * @return true si se deshizo exitosamente, false en caso contrario
     */
    public boolean deshacerUltimaAccion() {
        Action ultimaAccion = registroAcciones.deshacerUltimaAction();
        if (ultimaAccion == null) {
            return false;
        }

        Cliente cliente = ultimaAccion.getCliente();
        Action.TipoAccion tipo = ultimaAccion.getTipo();

        switch (tipo) {
            case REGISTRAR:
                return deshacerAgregar(cliente);

            case ELIMINAR:
                return deshacerEliminar(cliente);

            case ATENDER:
                return deshacerAtender(cliente);

            default:
                return false;
        }
    }

    /**
     * Deshace una acción de agregar cliente.
     *
     * @param cliente Cliente a remover
     * @return true si se deshizo exitosamente
     */
    private boolean deshacerAgregar(Cliente cliente) {
        Cliente clienteEliminado = gestionClientes.eliminarClientePorIdCola(cliente.getId());
        return clienteEliminado != null;
    }

    /**
     * Deshace una acción de eliminar cliente.
     *
     * @param cliente Cliente a restaurar
     * @return true si se deshizo exitosamente
     */
    private boolean deshacerEliminar(Cliente cliente) {
        return gestionClientes.agregarClienteExistente(cliente);
    }

    /**
     * Deshace una acción de atender cliente.
     *
     * @param cliente Cliente a devolver a la cola
     * @return true si se deshizo exitosamente
     */
    private boolean deshacerAtender(Cliente cliente) {
        Cliente clienteRemovido = historial.removerClientePorIdList(cliente.getId());
        if (clienteRemovido != null) {
            clienteRemovido.setFechaAtencion(null);
            return gestionClientes.agregarClienteAlInicio(clienteRemovido);
        }
        return false;
    }

    /**
     * Obtiene la última acción sin deshacerla.
     *
     * @return Última acción realizada, o null si no hay acciones
     */
    public Action obtenerUltimaAccion() {
        return registroAcciones.obtenerUltimaAction();
    }

    /**
     * Verifica si hay acciones disponibles para deshacer.
     *
     * @return true si hay acciones para deshacer, false en caso contrario
     */
    public boolean hayAccionesParaDeshacer() {
        return registroAcciones.hayActions();
    }
}
