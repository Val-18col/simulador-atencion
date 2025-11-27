/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Representa una acción realizada en el sistema de atención al cliente.
 * Cada acción registra el tipo de operación, el cliente involucrado y la fecha/hora.
 * Se utiliza para implementar la funcionalidad de deshacer (undo).
 * 
 * @author mi pc
 */
public class Action {
     /**
     * Tipos de acciones que pueden realizarse en el sistema.
     */
    public enum TipoAccion {
        /** Registro de un nuevo cliente en el sistema */
        REGISTRAR,
        /** Eliminación de un cliente de la cola de espera */
        ELIMINAR,
        /** Atención de un cliente (pasa de cola a historial) */
        ATENDER
    }

    private TipoAccion tipo;
    private Cliente cliente;
    private LocalDateTime fechaHora;

    /**
     * Constructor para crear una nueva acción.
     * 
     * @param tipo Tipo de acción realizada
     * @param cliente Cliente involucrado en la acción
     */
    public Action(TipoAccion tipo, Cliente cliente) {
        this.tipo = tipo;
        this.cliente = cliente;
        this.fechaHora = LocalDateTime.now();
    }

    /**
     * Obtiene el tipo de acción.
     * 
     * @return TipoAccion que representa la acción realizada
     */
    public TipoAccion getTipo() {
        return tipo;
    }

    /**
     * Obtiene el cliente asociado a la acción.
     * 
     * @return Cliente involucrado en la acción
     */
    public Cliente getCliente() {
        return cliente;
    }

    /**
     * Obtiene la fecha y hora en que se realizó la acción.
     * 
     * @return LocalDateTime con la fecha/hora de la acción
     */
    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    /**
     * Representación en String de la acción.
     * 
     * @return String con formato: "TIPO - Cliente: NOMBRE (ID: ID) - FECHA"
     */
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return String.format("%s - Cliente: %s (ID: %s) - %s",
                tipo, cliente.getNombre(), cliente.getId(), fechaHora.format(formatter));
    }
}
