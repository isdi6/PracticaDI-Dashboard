# Decisiones de diseño de la practica

Aqui explicamos las decisiones que tomamos para la interfaz segun lo que pide la rubrica del profesor:

1. Pantalla dividida en dos partes (tabla a la izquierda y formulario a la derecha):
Al principio pensamos en abrir una ventana emergente tipo dialogo o cambiar de escena cada vez que quisieras editar una incidencia, pero era un lio con los Stage y quedaba incomodo. Decidimos meter un HBox grande en el centro con la tabla a un lado y el formulario al otro. Asi pinchas en la tabla y ves los datos al momento a la derecha sin tener que cambiar de pantalla.

2. Cuatro contadores en la barra inferior:
Pusimos cuatro recuadros abajo en el pie de la ventana que marcan el total de incidencias, cuantas estan abiertas, cuantas en proceso y cuantas resueltas. Lo dejamos en la barra inferior tal como habiamos dibujado en el wireframe para tener siempre a la vista el resumen global sin estorbar a la tabla ni a los filtros de arriba, y porque cuando entras a un programa de estos lo primero que quieres ver es cuanto trabajo hay pendiente sin tener que contar las filas a ojo. Cuando guardas o borras una incidencia llamamos a un metodo que vuelve a contar los estados para que el numero se actualice.

3. Buscador por texto y botones de filtro rapido:
En la parte de arriba pusimos un TextField para buscar y al lado botones con [Todos], [Abiertas], [En proceso] y [Resueltas]. Esto nos dio bastantes dolores de cabeza porque al principio al filtrar se nos borraban los elementos de la tabla original, pero luego usamos el FilteredList para que solo oculte lo que no coincide. Se puede buscar a la vez por texto y por estado.

4. Usar texto ademas del color para los estados y prioridades:
En los estados le pusimos fondos de colores (verde resuelta, naranja abierta, etc.) y en la prioridad pusimos rojo para la alta. Pero como el profesor dijo en clase que no se puede usar unicamente el color porque si alguien no distingue bien los colores no sabe que significa, nos aseguramos de que siempre ponga la palabra bien clara dentro de la celda.

5. Estilos en un archivo CSS aparte (style.css):
Para no llenar el codigo de Java con setStyle y cadenas de texto larguisimas que luego no hay quien las entienda, creamos el archivo style.css en la carpeta resources. Le fuimos poniendo styleClass a los botones, a las tarjetas y a los campos de texto. Asi si queremos cambiar el color azul o el borde redondeado lo tocamos en el css directamente y no tocamos la logica de Java.
