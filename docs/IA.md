# Registro de Uso de Inteligencia Artificial (IA)

Registro oficial de consultas realizadas a herramientas de IA durante el desarrollo de la practica segun los apartados solicitados:

---

## Consulta 1: Organizacion de la pantalla con Maestro-Detalle
• Fecha: 21/10/2026
• Problema que queríamos resolver: No conseguiamos que la tabla ocupara el espacio que sobraba en la ventana y el formulario de la derecha se nos aplastaba o se salia de la pantalla al maximizar.
• Herramienta utilizada: Gemini
• Prompt o consulta realizada: "como puedo hacer en javafx para que en un hbox la parte de la izquierda ocupe todo el ancho y la parte de la derecha se quede con un tamaño fijo sin deformarse?"
• Resumen de la respuesta: Nos explico que habia que usar HBox.setHgrow(panelIzquierdo, Priority.ALWAYS) en el contenedor que quisieramos expandir, y en el de la derecha fijar un setPrefWidth y setMaxWidth.
• Qué parte utilizamos: La propiedad Priority.ALWAYS aplicada a la tarjeta de la tabla y los limites de ancho del formulario lateral.
• Qué modificamos: Ajustamos las medidas a 340 pixeles para que coincidiera con el tamaño que teniamos dibujado en el wireframe.
• Cómo validamos la respuesta: Ejecutamos la app con mvn javafx:run y probamos a estirar la ventana con el raton para comprobar que la tabla se adaptaba bien.
• Qué hemos aprendido: Para que sirve la propiedad Priority.ALWAYS dentro de los contenedores horizontales de JavaFX.

---

## Consulta 2: Generacion de datos de prueba en memoria
• Fecha: 21/10/2026
• Problema que queríamos resolver: Necesitabamos cargar 24 incidencias realistas en memoria al arrancar la aplicacion para que cuadraran exactamente con los numeros del diseño (8 abiertas, 6 en proceso y 10 resueltas).
• Herramienta utilizada: Gemini
• Prompt o consulta realizada: "generame 24 objetos Incidencia para java con id, titulo de averia de oficina, descripcion, estado (Abierta, En proceso, Resuelta) y prioridad (Alta, Media, Baja)"
• Resumen de la respuesta: Nos dio una lista de objetos new Incidencia(...) con problemas tipicos de oficina (toner de impresora, caida de red, certificados caducados, etc.).
• Qué parte utilizamos: Las 24 instancias añadidas al masterData dentro de initData().
• Qué modificamos: Ajustamos algunos titulos y comprobamos el reparto para que sumaran exactamente 8 abiertas, 6 en proceso y 10 resueltas.
• Cómo validamos la respuesta: Arrancamos el programa y comprobamos que la tabla mostraba todas las filas y los contadores iniciales marcaban 24 en total.
• Qué hemos aprendido: A poblar colecciones observables (ObservableList) directamente en codigo sin depender de base de datos.

---

## Consulta 3: Buscador en tiempo real y botones de filtro combinados
• Fecha: 22/10/2026
• Problema que queríamos resolver: Queriamos filtrar la tabla mientras el usuario escribe en el TextField pero que ademas respetara el filtro del boton de estado que estuviera pulsado (por ejemplo, buscar texto solo dentro de las "Abiertas") sin perder los datos originales.
• Herramienta utilizada: Gemini
• Prompt o consulta realizada: "tengo un TableView con una ObservableList de incidencias en java y quiero filtrarlo con un TextField y varios botones de estado a la vez sin perder los datos originales"
• Resumen de la respuesta: Nos dijo que habia que envolver la lista en un FilteredList y asignarle un predicado compuesto que compruebe tanto el texto como el estado, usando un listener en el textProperty del buscador.
• Qué parte utilizamos: La creacion del FilteredList y la funcion applyFilter() con el setPredicate.
• Qué modificamos: Añadimos toLowerCase() para que no importaran mayusculas ni minusculas y metimos comprobaciones para que no diera error de null si algun campo estaba vacio.
• Cómo validamos la respuesta: Escribimos palabras como "toner", "red" y "switch" mientras alternabamos entre los botones de "Abiertas" y "Todos".
• Qué hemos aprendido: Que FilteredList actua como un filtro visual sin modificar la coleccion original en memoria.

---

## Consulta 4: Colorear celdas con badges de estado y prioridad
• Fecha: 22/10/2026
• Problema que queríamos resolver: Queriamos que la columna de Estado tuviera fondo verde para resuelta, naranja para abierta y amarillo para en proceso, pero sin perder el texto visible para que fuera accesible.
• Herramienta utilizada: Gemini
• Prompt o consulta realizada: "como ponerle un fondo de color redondeado tipo badge a una celda de tableview en javafx segun el valor que tenga"
• Resumen de la respuesta: Nos recomendo usar un setCellFactory creando un TableCell personalizado que devuelva un Label con las clases de estilo correspondientes dentro del updateItem.
• Qué parte utilizamos: La estructura del TableCell y la llamada a updateItem(String item, boolean empty).
• Qué modificamos: Le vinculamos nuestras propias clases de estilo definidas en el archivo style.css (.badge-red, .badge-yellow, .badge-gray).
• Cómo validamos la respuesta: Comprobamos visualmente en la tabla que cada estado y prioridad tenia su color correcto junto a su texto legible.
• Qué hemos aprendido: Como funciona el ciclo de vida de las celdas en un TableView y como reutilizar controles dentro de ellas.

---

## Consulta 5: Calculo de nuevo ID automatico y actualizacion de contadores
• Fecha: 22/10/2026
• Problema que queríamos resolver: Al darle a guardar una nueva incidencia necesitabamos calcular el siguiente numero de ID libre y actualizar los contadores de arriba sin reiniciar la app.
• Herramienta utilizada: Gemini
• Prompt o consulta realizada: "como sacar el id mas alto de una lista de objetos en java con streams y como contar cuantos objetos cumplen una condicion"
• Resumen de la respuesta: Nos paso el codigo masterData.stream().mapToInt(Incidencia::getId).max().orElse(0) + 1 para el ID y masterData.stream().filter(...).count() para contar cada estado.
• Qué parte utilizamos: La linea del calculo del ID maximo y las cuatro lineas de conteo con streams.
• Qué modificamos: Lo metimos dentro de saveChanges() y dentro de la funcion updateStats() que actualiza las etiquetas del pie/cabecera.
• Cómo validamos la respuesta: Creamos varias incidencias nuevas y vimos que les asignaba el ID 25, 26, etc. y que el contador de Abiertas aumentaba al momento.
• Qué hemos aprendido: La sintaxis basica de la API Stream de Java para buscar maximos y hacer conteos rapidos.

---

## Consulta 6: Limpieza y estructura final de entrega
• Fecha: 26/10/2026
• Problema que queríamos resolver: Dejar el proyecto con la estructura exacta que pide la rubrica del profesor (docs/, capturas/, pom.xml, README.md) y solucionar un problema de compilacion con un pom duplicado dentro de src/.
• Herramienta utilizada: Gemini
• Prompt o consulta realizada: "como organizar las carpetas de este proyecto maven javafx para que cumpla con la plantilla de entrega con docs, capturas y readme sin archivos basura ni errores de build"
• Resumen de la respuesta: Nos indico que habia que eliminar el pom.xml duplicado que estaba dentro de src/, borrar los archivos temporales y situar los documentos en docs/ y las imagenes en capturas/.
• Qué parte utilizamos: La recomendacion de borrar los archivos duplicados y la colocacion de carpetas.
• Qué modificamos: Redactamos nosotros mismos las respuestas de las decisiones y de FXML con el lenguaje que hemos usado en clase.
• Cómo validamos la respuesta: Ejecutamos mvn clean compile comprobando que salia BUILD SUCCESS y revisamos el arbol de directorios.
• Qué hemos aprendido: A mantener limpia la estructura de un proyecto Maven sin mezclar archivos de configuracion en subcarpetas.
