/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.util.LinkedList;
import java.util.List;

/**
 * Gestiona el historial de clientes atendidos. Implementa una lista enlazada
 * para almacenar y consultar clientes que ya han sido atendidos. Proporciona
 * métodos para búsqueda, estadísticas y gestión del historial.
 *
 * @author Valery
 * @author Andres
 * @version 1.0
 * @see Cliente
 * @see GestionClientes
 */
public class Historial {

    private List<Cliente> clientesAtendidos;

    /**
     * Constructor que inicializa la lista de clientes atendidos.
     */
    public Historial() {
        this.clientesAtendidos = new LinkedList<>();
    }

    /**
     * Agrega un cliente a la lista de atendidos.
     *
     * @param cliente Cliente que ha sido atendido
     */
    public void agregarAtendido(Cliente cliente) {
        if (cliente != null) {
            clientesAtendidos.add(cliente);
        }
    }

    /**
     * Busca un cliente en el historial por su ID.
     *
     * @param id ID del cliente a buscar
     * @return Cliente encontrado, o null si no existe
     */
    public Cliente buscarCliente(String id) {
        for (Cliente cliente : clientesAtendidos) {
            if (cliente.getId().equals(id)) {
                return cliente;
            }
        }
        return null;
    }

    /**
     * Obtiene una copia de la lista de clientes atendidos.
     *
     * @return Lista de clientes atendidos
     */
    public List<Cliente> getClientesAtendidos() {
        return new LinkedList<>(clientesAtendidos);
    }

    /**
     * Obtiene la cantidad total de clientes atendidos.
     *
     * @return Número de clientes en el historial
     */
    public int getCantidadAtendidos() {
        return clientesAtendidos.size();
    }

    /**
     * Calcula el promedio de tiempo de atención en minutos.
     *
     * @return Promedio de tiempo de atención, o 0.0 si no hay clientes
     * atendidos
     */
    public double getPromedioTiempoAtencion() {
        if (clientesAtendidos.isEmpty()) {
            return 0.0;
        }

        long totalMinutos = 0;
        int contador = 0;

        for (Cliente cliente : clientesAtendidos) {
            long tiempo = cliente.calcularTiempoAtencion();
            if (tiempo > 0) {
                totalMinutos += tiempo;
                contador++;
            }
        }

        return contador > 0 ? (double) totalMinutos / contador : 0.0;
    }

    /**
     * Remueve un cliente específico del historial. Utilizado para la
     * funcionalidad de deshacer.
     *
     * @param cliente Cliente a remover
     * @return true si se removió exitosamente, false en caso contrario
     */
    public boolean removerClienteAtendido(Cliente cliente) {
        return clientesAtendidos.remove(cliente);
    }

    /**
     * Remueve un cliente del historial por su ID.
     *
     * @param id ID del cliente a remover
     * @return Cliente removido, o null si no se encontró
     */
    public Cliente removerClientePorIdList(String id) {
        for (Cliente cliente : clientesAtendidos) {
            if (cliente.getId().equals(id)) {
                clientesAtendidos.remove(cliente);
                return cliente;
            }
        }
        return null;
    }

    /**
     * Verifica si un cliente está en el historial.
     *
     * @param cliente Cliente a verificar
     * @return true si el cliente está en el historial, false en caso contrario
     */
    public boolean contieneCliente(Cliente cliente) {
        return clientesAtendidos.contains(cliente);
    }
}
