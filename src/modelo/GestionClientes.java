/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.ArrayList;
import java.util.List;

/**
 * Gestiona la cola de clientes en espera utilizando un sistema de prioridades híbrido.
 * Implementa una cola FIFO con prioridad donde los clientes urgentes tienen ventaja
 * pero los normales en primeras posiciones también pueden ser atendidos.
 * 
 * @author Valery
 * @author Andres
 */
public class GestionClientes {
private Queue<Cliente> colaClientes;

    /**
     * Constructor que inicializa la cola de clientes.
     */
    public GestionClientes() {
        this.colaClientes = new ArrayDeque<>();
    }

    /**
     * Agrega un nuevo cliente a la cola de espera.
     * 
     * @param nombre Nombre del cliente
     * @param tipoSolicitud Tipo de solicitud del cliente
     * @param prioridad Prioridad del cliente
     * @return Cliente agregado, o null si no se pudo agregar
     */
    public Cliente agregarCliente(String nombre, Cliente.TipoSolicitud tipoSolicitud, Cliente.Prioridad prioridad) {
        Cliente nuevoCliente = new Cliente(nombre, tipoSolicitud, prioridad);
        boolean agregado = colaClientes.offer(nuevoCliente);
        if (agregado) {
            return nuevoCliente;
        }
        return null;
    }

    /**
     * Atiende al cliente con mayor prioridad según el sistema híbrido.
     * Sistema de puntos: Urgente=8, Normal=6 + bonus por posición (4,3,2,1,0...)
     * 
     * @return Cliente atendido, o null si no hay clientes en espera
     */
    public Cliente atenderCliente() {
        if (colaClientes.isEmpty()) {
            return null;
        }

        // Sistema de prioridad híbrido - Opción 4
        Cliente clienteConMayorPrioridad = null;
        int maxPuntos = -1;
        int posicion = 0;

        for (Cliente cliente : colaClientes) {
            // Puntos base: Urgente=8, Normal=6
            int puntosBase = (cliente.getPrioridad() == Cliente.Prioridad.URGENTE) ? 8 : 6;

            // Bonus por posición: 4,3,2,1,0... (máximo 4 puntos para primera posición)
            int bonusPosicion = Math.max(0, 4 - posicion);

            int puntosTotales = puntosBase + bonusPosicion;

            if (puntosTotales > maxPuntos) {
                maxPuntos = puntosTotales;
                clienteConMayorPrioridad = cliente;
            }
            posicion++;
        }

        // Remover y retornar el cliente con mayor prioridad
        if (clienteConMayorPrioridad != null) {
            colaClientes.remove(clienteConMayorPrioridad);
            clienteConMayorPrioridad.setFechaAtencion(java.time.LocalDateTime.now());

            System.out.println("Sistema de prioridades - Atendiendo: "
                    + clienteConMayorPrioridad.getNombre()
                    + " (" + clienteConMayorPrioridad.getPrioridad()
                    + ") - " + maxPuntos + " puntos totales");
        }

        return clienteConMayorPrioridad;
    }

    /**
     * Agrega un cliente específico al principio de la cola.
     * Utilizado para la funcionalidad de deshacer.
     * 
     * @param cliente Cliente a agregar al inicio
     * @return true si se agregó exitosamente, false en caso contrario
     */
    public boolean agregarClienteAlInicio(Cliente cliente) {
        if (cliente == null) {
            return false;
        }
        // Convertimos la cola a lista, agregamos al inicio y reconstruimos
        List<Cliente> listaClientes = new ArrayList<>(colaClientes);

        listaClientes.add(0, cliente);
        colaClientes.clear();
        colaClientes.addAll(listaClientes);

        return true;
    }

    /**
     * Agrega un cliente existente al final de la cola.
     * Utilizado para deshacer eliminaciones.
     * 
     * @param cliente Cliente a agregar
     * @return true si se agregó exitosamente, false en caso contrario
     */
    public boolean agregarClienteExistente(Cliente cliente) {
        if (cliente == null) {
            return false;
        }
        return colaClientes.offer(cliente);
    }

    /**
     * Verifica si un cliente específico está en la cola.
     * 
     * @param cliente Cliente a buscar
     * @return true si el cliente está en la cola, false en caso contrario
     */
    public boolean contieneCliente(Cliente cliente) {
        return colaClientes.contains(cliente);
    }

    /**
     * Elimina un cliente de la cola por su ID.
     * 
     * @param id ID del cliente a eliminar
     * @return Cliente eliminado, o null si no se encontró
     */
    public Cliente eliminarClientePorIdCola(String id) {
        java.util.Iterator<Cliente> iterator = colaClientes.iterator();
        while (iterator.hasNext()) {
            Cliente cliente = iterator.next();
            if (cliente.getId().equals(id)) {
                iterator.remove();
                return cliente;
            }
        }
        return null;
    }

    /**
     * Verifica si existe un cliente en la cola por su ID.
     * 
     * @param id ID del cliente a buscar
     * @return true si el cliente existe, false en caso contrario
     */
    public boolean existeClientePorId(String id) {
        for (Cliente cliente : colaClientes) {
            if (cliente.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Obtiene el siguiente cliente a atender sin removerlo de la cola.
     * 
     * @return Cliente en la primera posición, o null si la cola está vacía
     */
    public Cliente obtenerSiguienteCliente() {
        return colaClientes.peek();
    }

    /**
     * Obtiene una lista con todos los clientes en espera.
     * 
     * @return Lista de clientes en espera
     */
    public List<Cliente> getClientesEnEspera() {
        return new ArrayList<>(colaClientes);
    }

    /**
     * Obtiene la cantidad de clientes en espera.
     * 
     * @return Número de clientes en la cola de espera
     */
    public int getCantidadClientesEnEspera() {
        return colaClientes.size();
    }

    /**
     * Verifica si hay clientes en espera.
     * 
     * @return true si hay clientes en espera, false en caso contrario
     */
    public boolean hayClientesEnEspera() {
        return !colaClientes.isEmpty();
    }
}
