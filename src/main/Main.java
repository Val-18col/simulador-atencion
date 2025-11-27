/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package main;

import controlador.Controlador;
import vista.Vista;
import modelo.Modelo;

/**
 * Clase main encargada de inicializar las clases modelo, controlador y de
 * mostrar la vista.
 *
 * @author mi pc
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // Crear el modelo (lógica de negocio)
        Modelo modelo = new Modelo();

        // Crear la vista (interfaz de usuario)
        Vista vista = new Vista();

        // Crear el controlador (coordinador MVC)
        Controlador controlador = new Controlador(modelo, vista);

        // Hacer visible la ventana principal
        vista.setVisible(true);
    }
}
