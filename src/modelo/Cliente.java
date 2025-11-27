/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


/**
 * Representa un cliente en el sistema de atención. Contiene toda la información
 * del cliente incluyendo datos personales, tipo de solicitud, prioridad y
 * tiempos de atención.
 *
 * @author Valery Hernandez
 * @author Andres Manjarres
 */
public class Cliente {

    /**
     * Contador estático para generar IDs únicos automáticamente
     */
    private static int contadorId = 0;

    private String id;
    private String nombre;
    private TipoSolicitud tipoSolicitud;
    private Prioridad prioridad;
    private LocalDateTime fechaLlegada;
    private LocalDateTime fechaAtencion;

    /**
     * Tipos de solicitud que puede tener un cliente.
     */
    public enum TipoSolicitud {
        /**
         * Solicitud de soporte técnico
         */
        SOPORTE,
        /**
         * Solicitud de mantenimiento
         */
        MANTENIMIENTO,
        /**
         * Solicitud de reclamo o queja
         */
        RECLAMO
    }

    /**
     * Niveles de prioridad para la atención de clientes.
     */
    public enum Prioridad {
        /**
         * Prioridad normal - atención estándar
         */
        NORMAL,
        /**
         * Prioridad urgente - atención prioritaria
         */
        URGENTE
    }

    /**
     * Constructor para crear un nuevo cliente.
     *
     * @param nombre Nombre completo del cliente
     * @param tipoSolicitud Tipo de solicitud del cliente
     * @param prioridad Nivel de prioridad del cliente
     */
    public Cliente(String nombre, TipoSolicitud tipoSolicitud, Prioridad prioridad) {
        contadorId++;
        this.id = String.valueOf(contadorId);
        this.nombre = nombre;
        this.tipoSolicitud = tipoSolicitud;
        this.prioridad = prioridad;
        this.fechaLlegada = LocalDateTime.now();
        this.fechaAtencion = null;
    }

    /**
     * Obtiene el ID único del cliente.
     *
     * @return String con el ID del cliente
     */
    public String getId() {
        return id;
    }

    /**
     * Obtiene el nombre del cliente.
     *
     * @return String con el nombre del cliente
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del cliente.
     *
     * @param nombre Nuevo nombre del cliente
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene la fecha y hora de atención del cliente.
     *
     * @return LocalDateTime con la fecha/hora de atención, o null si no ha sido
     * atendido
     */
    public LocalDateTime getFechaAtencion() {
        return fechaAtencion;
    }

    /**
     * Establece la fecha y hora de atención del cliente.
     *
     * @param fechaAtencion Fecha y hora en que fue atendido el cliente
     */
    public void setFechaAtencion(LocalDateTime fechaAtencion) {
        this.fechaAtencion = fechaAtencion;
    }

    /**
     * Obtiene el tipo de solicitud del cliente.
     *
     * @return TipoSolicitud del cliente
     */
    public TipoSolicitud getTipoSolicitud() {
        return tipoSolicitud;
    }

    /**
     * Establece el tipo de solicitud del cliente.
     *
     * @param tipoSolicitud Nuevo tipo de solicitud
     */
    public void setTipoSolicitud(TipoSolicitud tipoSolicitud) {
        this.tipoSolicitud = tipoSolicitud;
    }

    /**
     * Obtiene la prioridad del cliente.
     *
     * @return Prioridad del cliente
     */
    public Prioridad getPrioridad() {
        return prioridad;
    }

    /**
     * Establece la prioridad del cliente.
     *
     * @param prioridad Nueva prioridad del cliente
     */
    public void setPrioridad(Prioridad prioridad) {
        this.prioridad = prioridad;
    }

    /**
     * Obtiene la fecha y hora de llegada del cliente.
     *
     * @return LocalDateTime con la fecha/hora de llegada
     */
    public LocalDateTime getFechaLlegada() {
        return fechaLlegada;
    }

    /**
     * Establece la fecha y hora de llegada del cliente.
     *
     * @param fechaLlegada Nueva fecha/hora de llegada
     */
    public void setFechaLlegada(LocalDateTime fechaLlegada) {
        this.fechaLlegada = fechaLlegada;
    }

    /**
     * Obtiene el valor actual del contador de IDs.
     *
     * @return int con el valor actual del contador
     */
    public static int getContadorId() {
        return contadorId;
    }

    /**
     * Establece el valor del contador de IDs. Útil para reiniciar el sistema o
     * cargar datos.
     *
     * @param contadorId Nuevo valor del contador
     */
    public static void setContadorId(int contadorId) {
        Cliente.contadorId = contadorId;
    }

    /**
     * Calcula el tiempo de atención en minutos.
     *
     * @return long con el tiempo de espera en minutos, o -1 si no ha sido
     * atendido
     */
    public long calcularTiempoAtencion() {
        if (fechaAtencion != null && fechaLlegada != null) {
            return java.time.Duration.between(fechaLlegada, fechaAtencion).toMinutes();
        }
        return -1;
    }

    /**
     * Representación en String del cliente.
     *
     * @return String con toda la información del cliente formateada
     */
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String info = String.format("ID: %s | Nombre: %s | Tipo: %s | Prioridad: %s | Ingreso: %s",
                id, nombre, tipoSolicitud, prioridad, fechaLlegada.format(formatter));
        if (fechaAtencion != null) {
            info += String.format(" | Atendido: %s | Tiempo Espera: %d min",
                    fechaAtencion.format(formatter), calcularTiempoAtencion());
        } else {
            info += " | Estado: En espera";
        }
        return info;
    }
}
