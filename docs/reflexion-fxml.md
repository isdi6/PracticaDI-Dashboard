# Reflexion sobre FXML y Scene Builder

1. ¿Que problema resuelve FXML en una aplicacion JavaFX?
Resuelve el problema de mezclar el diseño visual con el codigo de la logica en Java. En la version programatica tuvimos que meter un monton de lineas para crear botones, definir anchos, añadir espaciados y meter paneles dentro de otros paneles, todo revuelto con los metodos de guardar o borrar. Con FXML el diseño va en un archivo aparte y el archivo Java se queda solo con el controlador y los metodos que hacen cosas.

2. ¿Que informacion contiene un archivo .fxml?
Tiene texto en formato XML donde cada etiqueta representa una parte de la ventana (como VBox, Label, Button, TableView, etc). Tambien pone como estan colocados unos dentro de otros, los textos que llevan puestos, las clases de css que usan, y arriba indica a que clase controladora de Java esta enlazado con fx:controller.

3. ¿Que relacion existe entre FXML y las clases JavaFX?
La relacion es directa, cada etiqueta que pones en el archivo .fxml equivale a crear un objeto de esa clase en Java. Por ejemplo si pones <Button text="Guardar"/> es lo mismo que poner new Button("Guardar") en Java. Y los atributos que le pones a la etiqueta llaman a los metodos setter correspondientes en segundo plano.

4. ¿Que funcion cumple FXMLLoader?
El FXMLLoader es la clase que se encarga de leer el archivo .fxml cuando arranca el programa, lo va leyendo y va creando los objetos reales de JavaFX en memoria para que se puedan mostrar en pantalla. Tambien se encarga de instanciar el controlador y pasarle las referencias de los botones y campos.

5. ¿Que utilidad tienen fx:id y la anotacion @FXML?
El fx:id es el identificador o nombre que le das a un boton o caja de texto en el archivo XML para saber cual es. Y en el controlador de Java pones @FXML encima de la variable que tenga el mismo nombre para que JavaFX sepa que tiene que inyectarle ese componente ahi sin tener que poner las variables como publicas.

6. ¿Que papel tiene el controlador?
El controlador es donde va el codigo de verdad. Cuando el usuario hace clic en un boton o selecciona una fila de la tabla, el evento salta al controlador y ejecuta el metodo que tengamos programado para actualizar los datos, comprobar los campos o llamar a otros metodos.

7. ¿Que aporta Scene Builder al proceso de diseño?
Aporta poder diseñar la ventana de forma grafica con el raton arrastrando botones, paneles y tablas, en vez de tener que escribir el codigo XML a mano o adivinar las coordenadas. Es mucho mas rapido para probar si un boton queda mejor a la izquierda o a la derecha y te permite cambiar colores y margenes viendolo al momento.

8. ¿Que ha mejorado y que no ha mejorado en vuestra aplicacion despues de la refactorización?
Lo que ha mejorado: que el codigo Java principal ya no tiene 500 lineas de crear layouts y meter hijos, sino que queda todo mucho mas ordenado en el controlador. Ademas cambiar cualquier cosa del aspecto visual es mucho mas rapido desde Scene Builder.
Lo que no ha mejorado: que si te equivocas con una letra en el fx:id del XML o en el nombre del metodo en el onAction, el compilador de Java no te saca error rojo en Eclipse o VS Code, sino que la aplicacion te peta directamente al arrancar diciendo que no encuentra el recurso o que la variable es null, y a veces cuesta encontrar donde estaba la errata.
