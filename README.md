Practica 1 - Dashboard de Incidencias
Asignatura: Desarrollo de Interfaces
Curso: 2º DAM

Descripcion:
Esta es la aplicacion para la practica de gestion de incidencias de una empresa. La hemos hecho con JavaFX y Maven usando Java 17/21.
No tiene base de datos porque la practica decia que habia que meter los datos en memoria en un ArrayList/ObservableList al arrancar.

Como hacerla funcionar:
Hay que abrir la consola en esta carpeta y poner:

mvn clean compile
mvn javafx:run

Estructura de carpetas que pide el profesor:
- src: todo el codigo java y el css dentro de resources
- pom.xml: dependencias de javafx y plugin para ejecutar
- README.md: este archivo
- docs/:
  - wireframe.png: captura del diseño que hicimos antes de programar
  - decisiones-diseno.md: explicacion de por que pusimos los botones y tablas asi
  - reflexion-fxml.md: preguntas sobre fxml y scene builder
  - IA.md: registro de las cosas que le preguntamos a la IA
- capturas/:
  - version-programatica.png: captura de la app hecha por codigo
  - version-final.png: captura de la app terminada

Que hace el programa:
- Tiene 4 contadores abajo en la barra inferior con el total, abiertas, en proceso y resueltas.
- Una tabla con las incidencias cargadas por defecto.
- Un buscador donde escribes y te va filtrando al momento.
- Botones para filtrar solo por abiertas, en proceso o resueltas.
- Si pulsas en una fila de la tabla salen los datos a la derecha para verlos o modificarlos y darle a Guardar.
- Boton de Nueva Incidencia para limpiar los campos y añadir una nueva a la lista.
- Si cambias el estado de una incidencia los contadores de abajo cambian de numero.
