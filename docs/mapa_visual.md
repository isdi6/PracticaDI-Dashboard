Si en el examen te ponen un código larguísimo, ignora el 90%. Tu vista solo tiene que buscar estas 4 palabras clave para saber cómo está construida la pantalla.

1. VBox (Vertical Box) = APILAR HACIA ARRIBA
-----------------------------------------------------------------------------
Piensa en un VBox como apilar ladrillos uno encima del otro para levantar un pilar.
Si ves esto en el código:
VBox caja = new VBox();
caja.getChildren().addAll(titulo, parrafo, boton);

Visualmente significa que en la pantalla verás:
[ Título ]
[ Párrafo ]
[ Botón ]
(Todo en una sola columna, de arriba a abajo).

2. HBox (Horizontal Box) = PONER EN FILA
-----------------------------------------------------------------------------
Piensa en un HBox como poner ladrillos en una misma hilada en el suelo, de izquierda a derecha.
Si ves esto en el código:
HBox fila = new HBox();
fila.getChildren().addAll(botonAceptar, botonCancelar);

Visualmente significa:
[ Botón Aceptar ] [ Botón Cancelar ]
(Uno al lado del otro).

3. GridPane = ALICATAR UNA PARED (Cuadrícula)
-----------------------------------------------------------------------------
Es una cuadrícula perfecta, como los azulejos de un baño o una tabla de Excel.
Tiene columnas (0, 1, 2) y filas (0, 1, 2).
Si ves esto:
GridPane grid = new GridPane();
grid.add(etiquetaNombre, 0, 0); // Columna 0, Fila 0
grid.add(campoTexto, 1, 0);     // Columna 1, Fila 0

Significa que están perfectamente alineados en celdas. Esto es lo que te van a pedir que uses en la Parte C para sacar el 10.

4. getStyleClass().add("nombre") = LA PINTURA (CSS)
-----------------------------------------------------------------------------
Si ves esto:
boton.getStyleClass().add("btn-peligro");

Significa que Java le ha puesto una pegatina al botón que dice "btn-peligro". 
Tu único trabajo aquí es ir al archivo .css y escribir:
.btn-peligro {
    -fx-background-color: red;
}
Así es como conectas el código Java (la estructura) con el CSS (la pintura).

-----------------------------------------------------------------------------
PARTE B: PREGUNTAS TIPO ENSAYO (Indicar dónde están las cosas)
-----------------------------------------------------------------------------
Si el profesor te pregunta: "¿Dónde hay Diseño Responsive y Flexbox en tu código y qué hace?", ve a tu App.java y copia/adapta esta respuesta:

>> RESPUESTA:
"El uso de Flexbox en mi código se encuentra en el método start() con la declaración 'HBox centerBox = new HBox(20);', que actúa como un contenedor flexible horizontal para la tabla y el formulario. 
La adaptabilidad responsive se encuentra unas líneas más abajo, en 'HBox.setHgrow(leftCard, Priority.ALWAYS);'. Su función es equivalente a 'flex-grow' en CSS: le dice a la tarjeta de la tabla que absorba todo el espacio sobrante si el usuario maximiza la ventana, mientras el formulario mantiene un ancho estable. Esto evita huecos en blanco y mantiene la proporción de la interfaz."

Si te preguntan: "¿Cómo está organizado el CSS en tu proyecto?":

>> RESPUESTA:
"En mi archivo style.css he optado por una organización basada en selectores de clase reutilizables (como '.card-container' o '.primary-button') en lugar de usar identificadores (IDs). Su función es crear un sistema de diseño modular, manteniendo la especificidad baja para que sea fácil sobrescribir estilos si es necesario en el futuro, cumpliendo las buenas prácticas de organización CSS."


-----------------------------------------------------------------------------
PARTE C: LA MEJORA PRÁCTICA 
-----------------------------------------------------------------------------
El profesor te pedirá que mejores algo del código. Aquí tienes la mejora perfecta que cubre la rúbrica ("uso de Grid") lista para aplicar al formulario de la derecha.

PASO 1: Busca en tu App.java (Ctrl+F) el texto: "VBox titleBox = new VBox(6);"
PASO 2: Vas a ver que hay bloques VBox para Título, Descripción, Estado y Prioridad. 
PASO 3: BORRA todo ese bloque (desde "VBox titleBox..." hasta "combosBox.getChildren().addAll(estadoBox, priorityBox);").
PASO 4: En ese espacio vacío, PEGA EXACTAMENTE ESTE CÓDIGO:

        // --- INICIO MEJORA GRID (PARTE C) ---
        GridPane formGrid = new GridPane();
        formGrid.setHgap(12); 
        formGrid.setVgap(16); 

        // Título
        Label titleLbl = new Label("Título");
        titleField = new TextField();
        GridPane.setHgrow(titleField, Priority.ALWAYS);
        
        // Descripción
        Label descLbl = new Label("Descripción");
        descArea = new TextArea();
        descArea.setPrefRowCount(3);
        GridPane.setHgrow(descArea, Priority.ALWAYS);

        // Añadir a la cuadrícula (Nodo, Columna, Fila)
        formGrid.add(titleLbl, 0, 0);      
        formGrid.add(titleField, 1, 0);
        formGrid.add(descLbl, 0, 1);       
        formGrid.add(descArea, 1, 1);
        // --- FIN MEJORA GRID ---

PASO 5: Cambia la última línea de ese método para que meta tu nuevo 'formGrid' en la tarjeta.
Cámbiala por esta: 
card.getChildren().addAll(detailTitleLabel, formGrid, spacer, actionButtons);

PASO 6: Entrega esta justificación al profesor en tu examen:

>> JUSTIFICACIÓN PARTE C:
"Para esta mejora práctica, he sustituido los contenedores anidados VBox/HBox del formulario por un GridPane. Esta decisión organizativa se debe a que un formulario requiere un layout bidimensional (etiquetas a la izquierda, campos a la derecha). El GridPane proporciona una estructura tabular más limpia y semántica, y al combinarlo con 'GridPane.setHgrow()', mantenemos intacto el diseño responsive, permitiendo que los campos de texto se estiren adecuadamente."

=============================================================================

>>AÑADIR UN BOTON NUEVO:
Añadir un botón nuevo a la pantalla (por ejemplo, en la parte de arriba o junto a los de Guardar/Eliminar)

Paso 1: Busca en el código dónde están los otros botones (haz Ctrl+F y busca el texto de un botón que ya exista, por ejemplo "Guardar cambios" o "+ Nueva Incidencia").

Paso 2: Pega este código justo debajo para crear tu botón:

Button miBoton = new Button("El Texto Que Te Pidan");
miBoton.getStyleClass().add("primary-button"); // Le pone el estilo azul bonito

// Si te piden que haga algo al pinchar:
miBoton.setOnAction(e -> {
    System.out.println("Botón pulsado"); // O lo que te pidan que haga
});

Paso 3: Busca la línea justo debajo que dice ....getChildren().addAll(..., ...) y añade miBoton al final de esa lista, dentro de los paréntesis.


>> PON ESTO UNO AL LADO DEL OTRO EN VEZ DE ARRIBA Y ABAJO:

Esto es lo que van a buscar para evaluarte el Flexbox.

VBox (Vertical): Apila las cosas de arriba a abajo.
HBox (Horizontal): Pone las cosas de izquierda a derecha.
Cómo arreglarlo: Si te dicen que el Título y la Descripción salen uno debajo del otro y quieren que salgan uno al lado del otro, solo tienes que buscar el VBox que los contiene y cambiar la palabra VBox por HBox.

// Si estaba así (Arriba y abajo):
VBox caja = new VBox(10); 
// Cámbialo a (Izquierda a derecha):
HBox caja = new HBox(10);

>>CAMBIAR EL COLOR/TAMAÑO/ESTILO DE ESTO:

Paso 1 (En Java): Busca el elemento que quieres pintar. Añádele esta línea justo debajo de donde se crea:

miElemento.getStyleClass().add("mi-estilo-examen");

Paso 2 (En el archivo .css): Vete al archivo style.css, vete a la última línea del todo, y añade tu clase CSS. (Recuerda que en JavaFX todo lleva -fx- delante).

.mi-estilo-examen {
    -fx-background-color: #ff0000; /* Fondo rojo, pon el que te pidan */
    -fx-text-fill: white;          /* Color del texto */
    -fx-font-size: 16px;           /* Tamaño de letra */
    -fx-font-weight: bold;         /* Negrita */
}

>>HAZ QUE EL ELEMENTO OCUPTE TODO EL ESPACIO SOBRANTE:

Si te dicen que al agrandar la ventana se queda un hueco en blanco feo y quieren que un elemento (una tabla, un cuadro de texto, etc.) se estire para rellenarlo.

Paso 1: Identifica si el contenedor padre es un HBox (horizontal) o VBox (vertical).

Paso 2: Pega esta línea mágica debajo del elemento que quieres que se estire:

// Si está dentro de un HBox (para que se estire a lo ancho):
HBox.setHgrow(nombreDelElemento, Priority.ALWAYS);

// Si está dentro de un VBox (para que se estire a lo alto):
VBox.setVgrow(nombreDelElemento, Priority.ALWAYS);


>>Propuesta 1: Añadir una confirmación antes de eliminar
Dónde ir (Ctrl+F): Busca private void deleteSelected()

Qué hacer: Sustituye todo ese método por este:

    private void deleteSelected() {
        if (selectedIncidencia != null) {
            // Creamos la ventana de confirmación
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Confirmar eliminación");
            confirm.setHeaderText(null);
            confirm.setContentText("¿Estás seguro de que deseas eliminar la incidencia #" + selectedIncidencia.getIdFormatted() + "?");

            // Solo borramos si el usuario pulsa ACEPTAR (OK)
            if (confirm.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
                String idFmt = selectedIncidencia.getIdFormatted();
                masterData.remove(selectedIncidencia);
                statusMessageLabel.setText("✓ Incidencia #" + idFmt + " eliminada");
                if (!masterData.isEmpty()) {
                    tableView.getSelectionModel().selectFirst();
                } else {
                    enterNewMode();
                }
                updateStats();
            }
        }
    }

>>Propuesta 2: Añadir un botón para recuperar una incidencia eliminada
Paso 1: Arriba del todo de App.java (junto a private Incidencia selectedIncidencia = null;), declara esta variable para guardar la última borrada:
    private Incidencia lastDeletedIncidencia = null;
    private Button restoreBtn;

Paso 2: En deleteSelected(), justo antes de masterData.remove(selectedIncidencia);, guarda la copia y activa el botón:
    lastDeletedIncidencia = selectedIncidencia;
    if (restoreBtn != null) restoreBtn.setDisable(false);

Paso 3: En createHeader() (o en createDetailCard() junto a los botones de acción), crea el botón y añádelo al getChildren().addAll(...):

    restoreBtn = new Button("Recuperar borrada");
    restoreBtn.getStyleClass().add("secondary-button");
    restoreBtn.setDisable(true); // Desactivado al principio
    restoreBtn.setOnAction(e -> {
        if (lastDeletedIncidencia != null) {
            masterData.add(lastDeletedIncidencia);
            tableView.getSelectionModel().select(lastDeletedIncidencia);
            statusMessageLabel.setText("✓ Incidencia #" + lastDeletedIncidencia.getIdFormatted() + " recuperada");
            lastDeletedIncidencia = null;
            restoreBtn.setDisable(true);
            updateStats();
        }
    });


>>Propuesta 16: Hacer obligatoria la descripción
Dónde ir (Ctrl+F): Busca private void saveChanges()
Qué hacer: Justo debajo del if (title.isEmpty()) { ... }, pega este segundo if:
        if (desc.isEmpty()) {
            showAlert("Validación", "Por favor introduce una descripción obligatoria para la incidencia.");
            return;
        }

>>Propuesta 17 y 18: Buscar sin importar tildes + Buscar también por prioridad y estado
Dónde ir (Ctrl+F): Busca private void applyFilter()
Qué hacer: Añade arriba del archivo import java.text.Normalizer; y reemplaza applyFilter() y añade el método auxiliar quitarTildes justo debajo:

    // Método auxiliar para quitar tildes (Propuesta 17)
    private String quitarTildes(String texto) {
        if (texto == null) return "";
        return java.text.Normalizer.normalize(texto, java.text.Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                .toLowerCase();
    }

    private void applyFilter() {
        String searchText = quitarTildes(searchField.getText() == null ? "" : searchField.getText().trim());

        filteredData.setPredicate(incidencia -> {
            if (!currentStatusFilter.equals("Todos")) {
                String filterNormalized = currentStatusFilter.equals("Abiertas") ? "Abierta" :
                                         currentStatusFilter.equals("Resueltas") ? "Resuelta" : currentStatusFilter;
                if (!incidencia.getEstado().equalsIgnoreCase(filterNormalized)) {
                    return false;
                }
            }

            if (!searchText.isEmpty()) {
                boolean matchesId = incidencia.getIdFormatted().contains(searchText) || 
                                    String.valueOf(incidencia.getId()).contains(searchText);
                boolean matchesTitle = quitarTildes(incidencia.getTitulo()).contains(searchText);
                boolean matchesDesc = quitarTildes(incidencia.getDescripcion()).contains(searchText);
                // Propuesta 18: Buscar también por estado y prioridad
                boolean matchesEstado = quitarTildes(incidencia.getEstado()).contains(searchText);
                boolean matchesPrioridad = quitarTildes(incidencia.getPrioridad()).contains(searchText);
                
                return matchesId || matchesTitle || matchesDesc || matchesEstado || matchesPrioridad;
            }
            return true;
        });
        updateStats();
    }

>>Propuesta 19 y 20: Actualizar filtros al guardar / Mostrar nuevas aunque haya filtro activo
Dónde ir (Ctrl+F): Busca private void saveChanges()
Qué hacer: Al final del método saveChanges(), justo donde está updateStats();:
Para la 19 (Actualizar filtro automáticamente): Añade applyFilter(); justo encima de updateStats();.
Para la 20 (Que la nueva incidencia se vea siempre aunque haya filtros): Dentro del bloque if (isNewMode) {, antes de tableView.getSelectionModel().select(newInc);, pon resetFilters(); para limpiar los filtros activos y que la nueva fila no quede oculta.

>>Propuesta 21: Mostrar estadísticas únicamente de los resultados filtrados
Dónde ir (Ctrl+F): Busca private void updateStats()
Qué hacer: Cambia todas las palabras masterData dentro de ese método por filteredData:

    private void updateStats() {
        long total = filteredData.size();
        long abiertas = filteredData.stream().filter(i -> "Abierta".equalsIgnoreCase(i.getEstado())).count();
        long enProceso = filteredData.stream().filter(i -> "En proceso".equalsIgnoreCase(i.getEstado())).count();
        long resueltas = filteredData.stream().filter(i -> "Resuelta".equalsIgnoreCase(i.getEstado())).count();

        if (totalCountLabel != null) totalCountLabel.setText(String.valueOf(total));
        if (abiertasCountLabel != null) abiertasCountLabel.setText(String.valueOf(abiertas));
        if (enProcesoCountLabel != null) enProcesoCountLabel.setText(String.valueOf(enProceso));
        if (resueltasCountLabel != null) resueltasCountLabel.setText(String.valueOf(resueltas));
    }

>>Propuesta 22: Evitar que se repitan los identificadores (Secuencia independiente)
Qué hacer: Si borras la incidencia 24 y creas una nueva, max().orElse(0) + 1 volvería a dar 24. Para evitarlo:
Arriba en App.java, pon: private int nextIdSequence = 25;
En saveChanges(), cambia la línea int nextId = masterData.stream()... por:
        int nextId = nextIdSequence++;


>>Propuesta 23 y 27: Corregir datos de prueba / Eliminar números manuales en el pie
Propuesta 23: Ve a initData() y corrige las erratas de las incidencias 4, 5 y 6 (cambia "Falta de Inico contaçor", "Falta de Inte merciaga" y "Falta de Elegántica" por "Error en inicio de contabilidad", "Fallo de mensajería interna", "Error en plantillas de diseño").
Propuesta 27: Ve a createFooter() y cambia "24", "8", "6", "10" por "0" en los cuatro new Label("0").

>>Propuesta 24: Evitar repetir el código que colorea las etiquetas (Badges)
Dónde ir (Ctrl+F): Busca colEstado.setCellFactory en createTableCard().

Qué hacer: Crea este método auxiliar fuera de createTableCard():

    private Callback<TableColumn<Incidencia, String>, TableCell<Incidencia, String>> crearCeldaBadge(String valorRojo, String valorGris) {
        return col -> new TableCell<>() {
            private final Label badge = new Label();
            { badge.getStyleClass().add("badge"); }

            @Override
            protected void updateItem(String valor, boolean empty) {
                super.updateItem(valor, empty);
                if (empty || valor == null) {
                    setGraphic(null);
                } else {
                    badge.setText(valor);
                    badge.getStyleClass().removeAll("badge-red", "badge-gray", "badge-yellow");
                    if (valorRojo.equalsIgnoreCase(valor)) {
                        badge.getStyleClass().add("badge-red");
                    } else if (valorGris.equalsIgnoreCase(valor)) {
                        badge.getStyleClass().add("badge-gray");
                    } else {
                        badge.getStyleClass().add("badge-yellow");
                    }
                    setGraphic(badge);
                    setAlignment(Pos.CENTER_LEFT);
                }
            }
        };
    }
(Añade arriba import javafx.util.Callback; si te lo pide). Luego, en createTableCard(), borra los dos bloques gigantes de colEstado.setCellFactory(...) y colPrioridad.setCellFactory(...) y pon solo estas 2 líneas:

        colEstado.setCellFactory(crearCeldaBadge("Abierta", "Resuelta"));
        colPrioridad.setCellFactory(crearCeldaBadge("Alta", "Baja"));

>>Propuesta 25: Adaptar la aplicación a diferentes tamaños de pantalla (Responsive)
Dónde ir (Ctrl+F): Busca rightCard.setPrefWidth(340); en start() y searchField.setPrefWidth(320); en createHeader().
Qué hacer:
En start(), borra rightCard.setMaxWidth(380); y pon HBox.setHgrow(rightCard, Priority.SOMETIMES); para que el formulario también pueda crecer en pantallas grandes. Baja stage.setMinWidth(960); a stage.setMinWidth(720);.
En createHeader(), añade HBox.setHgrow(searchField, Priority.SOMETIMES); para que el buscador sea flexible.

>>Propuesta 28, 29 y 30: Simplificaciones rápidas
Propuesta 28 (Cambiar botones de filtro por un ComboBox): En createFilterBar(), borra el bucle for (String status : statuses) { ... } y pon:

        ComboBox<String> filterCombo = new ComboBox<>(FXCollections.observableArrayList("Todos", "Abiertas", "En proceso", "Resueltas"));
        filterCombo.setValue("Todos");
        filterCombo.getStyleClass().add("custom-combo-box");
        filterCombo.setOnAction(e -> {
            currentStatusFilter = filterCombo.getValue();
            applyFilter();
        });
        filterBar.getChildren().add(filterCombo);

>>Propuesta 29 (Sustituir Eliminar por Archivar): 
En createDetailCard(), cambia new Button("Eliminar") por new Button("Archivar"). En deleteSelected(), en vez de masterData.remove(selectedIncidencia);, pon:

        selectedIncidencia.setEstado("Archivada");
        tableView.refresh();

>>Propuesta 30 (Eliminar estadísticas inferiores):
 En start(), borra HBox footerBar = createFooter(); y rootWrapper.setBottom(footerBar);.

 >>BLOQUE 2: Añadir nuevos campos (Propuestas 3, 4, 5, 6, 7, 8)
Todas estas propuestas siguen exactamente el mismo patrón de 4 pasos. Si te cae cualquiera de ellas (Técnico, Departamento, Categoría, Fecha o Comentarios), haz esto:
Paso 1: En Incidencia.java (carpeta model)
Añade el atributo, ponlo en el constructor o dale un valor por defecto, y crea su get y set. Ejemplo para Técnico (Propuesta 3) o Departamento (Propuesta 4):

    private String tecnico = "Sin asignar"; // (O departamento = "Informática", etc.)

    public String getTecnico() { return tecnico; }
    public void setTecnico(String tecnico) { this.tecnico = tecnico; }

(Si es Propuesta 6 o 7 - Fecha, usa private LocalDate fecha = LocalDate.now(); y añade import java.time.LocalDate;).

Paso 2: Declarar el control arriba en App.java
Si es Propuesta 3 (Técnico): private TextField tecnicoField;
Si es Propuesta 4 (Departamento): private ComboBox<String> deptoCombo;
Si es Propuesta 5 (Categoría): private ComboBox<String> categoriaCombo;
Si es Propuesta 7 (Fecha límite): private DatePicker fechaLimitePicker;
Si es Propuesta 8 (Comentarios): private TextArea comentariosArea;

Paso 3: Meterlo en el formulario (createDetailCard())
Pega el bloque correspondiente antes del Region spacer = new Region(); y añade la caja (tecnicoBox, deptoBox, etc.) dentro del card.getChildren().addAll(...) final:

        // EJEMPLO PARA TEXTO (Propuesta 3: Técnico)
        VBox tecnicoBox = new VBox(6);
        Label tecnicoLbl = new Label("Técnico responsable");
        tecnicoLbl.getStyleClass().add("form-label");
        tecnicoField = new TextField();
        tecnicoField.getStyleClass().add("custom-text-field");
        tecnicoBox.getChildren().addAll(tecnicoLbl, tecnicoField);

        // EJEMPLO PARA DESPLEGABLE (Propuesta 4: Departamento / Propuesta 5: Categoría)
        VBox deptoBox = new VBox(6);
        Label deptoLbl = new Label("Departamento");
        deptoLbl.getStyleClass().add("form-label");
        deptoCombo = new ComboBox<>(FXCollections.observableArrayList("Administración", "Informática", "Contabilidad", "RRHH"));
        deptoCombo.getStyleClass().add("custom-combo-box");
        deptoCombo.setMaxWidth(Double.MAX_VALUE);
        deptoBox.getChildren().addAll(deptoLbl, deptoCombo);

        // EJEMPLO PARA FECHA (Propuesta 7: Fecha límite)
        VBox fechaBox = new VBox(6);
        Label fechaLbl = new Label("Fecha límite");
        fechaLbl.getStyleClass().add("form-label");
        fechaLimitePicker = new DatePicker();
        fechaLimitePicker.setMaxWidth(Double.MAX_VALUE);
        fechaBox.getChildren().addAll(fechaLbl, fechaLimitePicker);

Paso 4: Conectarlo en populateForm, enterNewMode y saveChanges
En populateForm(Incidencia inc): tecnicoField.setText(inc.getTecnico()); (o deptoCombo.setValue(inc.getDepartamento());)
En enterNewMode(): tecnicoField.setText(""); (o deptoCombo.setValue("Informática");)
En saveChanges(): selectedIncidencia.setTecnico(tecnicoField.getText().trim());
(Si además te piden mostrarlo en la tabla como en la Propuesta 6, ve a createTableCard() y copia/pega las 3 líneas de colTitulo cambiando "Título" por "Fecha" y "titulo" por "fecha").

>>BLOQUE 3: Funcionalidades Extra (Filtros nuevos y Archivos - Propuestas 9, 10, 12, 13)
Propuesta 10: Añadir filtros por prioridad
Dónde ir: Arriba en App.java, añade private String currentPriorityFilter = "Todas";
En createFilterBar(): Añade un desplegable de prioridad al lado de los botones:

        ComboBox<String> prioFilter = new ComboBox<>(FXCollections.observableArrayList("Todas", "Alta", "Media", "Baja"));
        prioFilter.setValue("Todas");
        prioFilter.getStyleClass().add("custom-combo-box");
        prioFilter.setOnAction(e -> {
            currentPriorityFilter = prioFilter.getValue();
            applyFilter();
        });
        filterBar.getChildren().add(prioFilter);

En applyFilter(): Añade este if justo debajo del if (!currentStatusFilter.equals("Todos")):

        if (!currentPriorityFilter.equals("Todas")) {
            if (!incidencia.getPrioridad().equalsIgnoreCase(currentPriorityFilter)) {
                return false;
            }
        }


Propuesta 12 y 13: Exportar e Importar CSV (Aprovechando lo que ya sabes de Acceso a Datos)
Qué hacer: Añade import java.io.*; arriba del todo en App.java. Crea un botón en createHeader() llamado "Exportar CSV" y ponle esta acción:

        // PROPUESTA 12: EXPORTAR A CSV
        Button exportBtn = new Button("Exportar CSV");
        exportBtn.getStyleClass().add("secondary-button");
        exportBtn.setOnAction(e -> {
            try (PrintWriter pw = new PrintWriter(new FileWriter("incidencias.csv"))) {
                pw.println("ID;Titulo;Descripcion;Estado;Prioridad");
                for (Incidencia inc : masterData) {
                    pw.println(inc.getId() + ";" + inc.getTitulo() + ";" + inc.getDescripcion() + ";" + inc.getEstado() + ";" + inc.getPrioridad());
                }
                statusMessageLabel.setText("✓ Exportado a incidencias.csv");
            } catch (IOException ex) {
                showAlert("Error", "No se pudo exportar el CSV.");
            }
        });

Y si te piden Importar CSV (Propuesta 13):

        // PROPUESTA 13: IMPORTAR DESDE CSV
        Button importBtn = new Button("Importar CSV");
        importBtn.getStyleClass().add("secondary-button");
        importBtn.setOnAction(e -> {
            try (BufferedReader br = new BufferedReader(new FileReader("incidencias.csv"))) {
                String linea = br.readLine(); // Saltamos cabecera
                masterData.clear();
                while ((linea = br.readLine()) != null) {
                    String[] partes = linea.split(";");
                    if (partes.length >= 5) {
                        masterData.add(new Incidencia(Integer.parseInt(partes[0]), partes[1], partes[2], partes[3], partes[4]));
                    }
                }
                updateStats();
                statusMessageLabel.setText("✓ Datos importados de incidencias.csv");
            } catch (Exception ex) {
                showAlert("Error", "No se pudo importar el archivo CSV.");
            }
        });


>>Propuesta 9: Permitir adjuntar archivos (FileChooser)
Qué hacer: Añade import javafx.stage.FileChooser; y import java.io.File; arriba. En createDetailCard(), añade este botón:

        Button attachBtn = new Button("📎 Adjuntar archivo");
        attachBtn.getStyleClass().add("secondary-button");
        attachBtn.setOnAction(e -> {
            FileChooser fc = new FileChooser();
            fc.setTitle("Seleccionar archivo adjunto");
            File file = fc.showOpenDialog( null );
            if (file != null) {
                descArea.appendText("\n[Adjunto: " + file.getName() + "]");
                statusMessageLabel.setText("✓ Archivo adjuntado: " + file.getName());
            }
        });
(No olvides meter attachBtn en el card.getChildren().addAll(...) de createDetailCard()).

5. Mapa para saber dónde tocar cuando te pidan un cambio
Memoriza esta tabla. Te permitirá localizar rápidamente la parte del código que tienes que modificar.
Si te pide...	Debes mirar...
Añadir un botón	createHeader() o createDetailCard()
Añadir un campo	createDetailCard()
Guardar datos	saveChanges()
Recuperar datos	populateForm()
Crear incidencias	enterNewMode() y saveChanges()
Eliminar incidencias	deleteSelected()
Cambiar filtros	applyFilter()
Cambiar colores	createTableCard() y style.css
Modificar estadísticas	updateStats()
Cambiar datos iniciales	initData()
Cambiar tamaño de pantalla	start() y los contenedores
Ordenar la tabla	SortedList y tableView