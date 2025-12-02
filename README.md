SIMULADOR DE ATENCIÓN AL CLIENTE - TechClassUC
Descripción del Proyecto
Sistema de simulación de atención al cliente desarrollado en Java con interfaz gráfica Swing. Implementa un sistema híbrido de prioridades que combina urgencia y tiempo de espera, gestión completa de clientes, historial de atendidos y funcionalidad UNDO completa.

Características Principales
1. Sistema inteligente de prioridades - Algoritmo de puntos híbrido (Urgente=8pts, Normal=6pts + bonus por posición)
2. Funcionalidad UNDO completa - Reversión de acciones (Registrar, Atender, Eliminar)
3. Estadísticas en tiempo real - Métricas automáticas de atención
4. Historial detallado - Trazabilidad completa de todas las acciones
5. Interfaz gráfica intuitiva - Organizada en pestañas con Java Swing
6. Arquitectura MVC - Separación clara de responsabilidades

Estructuras de Datos Implementadas
Cola (Queue) - Implementada en la clase GestionClientes para la gestión de clientes en espera con sistema de prioridad híbrido.
Lista (List) - Implementada en la clase Historial para el registro y consulta de clientes atendidos.
Pila (Stack) - Implementada en la clase RegistroAcciones para el sistema de reversión de acciones (UNDO).

Tecnologías Utilizadas
Java 8+ - Lenguaje principal
Java Swing - Interfaz gráfica
Patrón MVC - Arquitectura del sistema
Git - Control de versiones

Estructura del Proyecto
text
src/
├── main/
│   ├── modelo/
│   │   ├── Cliente.java          # Entidad principal del sistema
│   │   ├── GestionClientes.java  # Gestión de cola con prioridad híbrida
│   │   ├── Historial.java        # Historial de clientes atendidos
│   │   ├── RegistroAcciones.java # Pila para sistema UNDO
│   │   ├── Action.java           # Modelo de acciones reversibles
│   │   └── Modelo.java           # Coordinador principal del modelo
│   ├── vista/
│   │   └── Vista.java            # Interfaz gráfica Swing
│   ├── controlador/
│   │   └── Controlador.java      # Controlador MVC
│   └── main/
│       └── Main.java             # Punto de entrada

Cómo Ejecutar
Requisitos Previos
Java JDK 8 o superior

IDE compatible con Java (NetBeans, IntelliJ, Eclipse)

Pasos de Ejecución
Clonar el repositorio:
bash
git clone https://github.com/Val-18col/simulador-atencion.git
Abrir proyecto en tu IDE favorito
Ejecutar la clase Main.java
¡Listo! La interfaz gráfica se mostrará automáticamente

Funcionalidades por Pestaña
1. Gestión de Clientes
Registro de nuevos clientes (Nombre, Tipo, Prioridad)
Tabla en tiempo real de clientes en espera
Sistema de atención con prioridad inteligente
Eliminación selectiva de clientes

2. Lista de Atendidos
Historial completo de clientes atendidos
Búsqueda por ID
Tiempos de espera calculados automáticamente

3. Historial de Atención
Consulta de todas las acciones realizadas
Generación de estadísticas del sistema
Botón UNDO global para reversión de acciones

Algoritmo de Prioridad Híbrida
El sistema utiliza un algoritmo de puntuación único:

Puntos Totales = Puntos Base + Bonus por Posición

Puntos Base:
- Urgente = 8 puntos
- Normal = 6 puntos

Bonus por Posición:
- Posición 1: 4 puntos
- Posición 2: 3 puntos
- Posición 3: 2 puntos
- Posición 4: 1 punto
- Posición 5+: 0 puntos

Estadísticas Generadas
Cantidad de clientes en espera
Total de clientes atendidos
Promedio de tiempo de atención
Número de acciones registradas

Autores
Valery Hernandez - GitHub: Val-18col
Andres Manjarres - Desarrollo y documentación

Licencia
Este proyecto es de uso educativo y académico. Desarrollado como parte del programa TechClassUC.

⭐ ¡Si este proyecto te resulta útil, considera darle una estrella en GitHub!
