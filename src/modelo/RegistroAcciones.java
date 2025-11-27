/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

/**
 * Gestiona el registro de todas las acciones realizadas en el sistema.
 * Implementa una pila (LIFO) para almacenar acciones y permitir la funcionalidad de deshacer.
 * Proporciona métodos para registrar, consultar y revertir acciones.
 * 
 * @author mi pc
 */
public class RegistroAcciones {
private Stack<Action> pilaAcciones;
    /**
     * Constructor que inicializa la pila de acciones.
     */
    public RegistroAcciones() {
        this.pilaAcciones = new Stack<>();
    }

    /**
     * Registra una nueva acción en la pila.
     * 
     * @param tipo Tipo de acción a registrar
     * @param cliente Cliente involucrado en la acción
     */
    public void registrarAction(Action.TipoAccion tipo, Cliente cliente) {
        Action nuevaAccion = new Action(tipo, cliente);
        pilaAcciones.push(nuevaAccion);
        System.out.println("Acción registrada: " + nuevaAccion);
    }

    /**
     * Obtiene la cantidad de acciones registradas.
     * 
     * @return Número total de acciones en la pila
     */
    public int getCantidadActions() {
        return pilaAcciones.size();
    }

    /**
     * Verifica si hay acciones registradas.
     * 
     * @return true si hay acciones, false si la pila está vacía
     */
    public boolean hayActions() {
        return !pilaAcciones.isEmpty();
    }

    /**
     * Obtiene todas las acciones como texto formateado.
     * Las acciones se muestran en orden inverso (más recientes primero).
     * 
     * @return String con el historial completo de acciones
     */
    public String getActionsComoTexto() {
        if (pilaAcciones.isEmpty()) {
            return "No hay acciones registradas.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("=== HISTORIAL DE ACCIONES (más recientes primero) ===\n\n");

        // Recorrer la pila en orden inverso (más reciente primero)
        Stack<Action> pilaTemp = (Stack<Action>) pilaAcciones.clone();
        List<Action> listaInversa = new ArrayList<>();
        while (!pilaTemp.isEmpty()) {
            listaInversa.add(pilaTemp.pop());
        }

        for (int i = 0; i < listaInversa.size(); i++) {
            Action action = listaInversa.get(i);
            sb.append((i + 1)).append(". ").append(action.toString()).append("\n");
        }

        return sb.toString();
    }

    /**
     * Deshace la última acción registrada (elimina y retorna de la pila).
     * 
     * @return Última acción realizada, o null si no hay acciones
     */
    public Action deshacerUltimaAction() {
        if (!pilaAcciones.isEmpty()) {
            return pilaAcciones.pop();
        }
        return null;
    }

    /**
     * Obtiene la última acción sin eliminarla de la pila.
     * 
     * @return Última acción realizada, o null si no hay acciones
     */
    public Action obtenerUltimaAction() {
        if (!pilaAcciones.isEmpty()) {
            return pilaAcciones.peek();
        }
        return null;
    }

    /**
     * Obtiene una lista con todas las acciones registradas.
     * 
     * @return Lista de todas las acciones en orden de la pila
     */
    public List<Action> getTodasLasActions() {
        return new ArrayList<>(pilaAcciones);
    }
}
