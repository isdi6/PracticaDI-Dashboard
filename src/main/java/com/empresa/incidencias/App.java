package com.empresa.incidencias;

import com.empresa.incidencias.model.Incidencia;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class App extends Application {

    private final ObservableList<Incidencia> masterData = FXCollections.observableArrayList();
    private FilteredList<Incidencia> filteredData;
    
    private TableView<Incidencia> tableView;
    private TextField searchField;
    private String currentStatusFilter = "Todos";

    // Controles del formulario de la derecha
    private Label detailTitleLabel;
    private TextField titleField;
    private TextArea descArea;
    private ComboBox<String> statusCombo;
    private ComboBox<String> priorityCombo;
    private Button deleteBtn;

    // Etiquetas para los contadores de abajo (pie de pagina)
    private Label totalCountLabel;
    private Label abiertasCountLabel;
    private Label enProcesoCountLabel;
    private Label resueltasCountLabel;
    private Label statusMessageLabel;

    // Lista para guardar los botones de filtro por estado
    private final List<Button> filterButtons = new ArrayList<>();

    private Incidencia selectedIncidencia = null;
    private boolean isNewMode = false;

    @Override
    public void start(Stage stage) {
        // Cargamos los datos de prueba en la lista
        initData();

        // Layout principal con BorderPane
        BorderPane mainLayout = new BorderPane();
        mainLayout.getStyleClass().add("root-container");

        // Parte de arriba con titulo, buscador y botones de filtro
        VBox topBox = new VBox(16);
        topBox.getChildren().addAll(createHeader(), createFilterBar());
        mainLayout.setTop(topBox);

        // Parte central: a la izquierda la tabla y a la derecha el formulario
        HBox centerBox = new HBox(20);
        centerBox.setPadding(new Insets(16, 0, 16, 0));
        VBox.setVgrow(centerBox, Priority.ALWAYS);

        VBox leftCard = createTableCard();
        VBox rightCard = createDetailCard();

        // Le decimos a la tarjeta de la tabla que ocupe todo el espacio sobrante
        HBox.setHgrow(leftCard, Priority.ALWAYS);
        rightCard.setPrefWidth(340);
        rightCard.setMinWidth(300);
        rightCard.setMaxWidth(380);

        centerBox.getChildren().addAll(leftCard, rightCard);
        mainLayout.setCenter(centerBox);

        // Barra inferior con los contadores
        HBox footerBar = createFooter();
        
        BorderPane rootWrapper = new BorderPane();
        rootWrapper.setCenter(mainLayout);
        rootWrapper.setBottom(footerBar);

        // Creamos la escena y le cargamos el css
        Scene scene = new Scene(rootWrapper, 1060, 680);
        String css = getClass().getResource("/com/empresa/incidencias/style.css").toExternalForm();
        scene.getStylesheets().add(css);

        stage.setTitle("Gestión de Incidencias");
        stage.setScene(scene);
        stage.setMinWidth(960);
        stage.setMinHeight(600);
        stage.show();

        // Dejamos seleccionada la primera o segunda fila por defecto
        if (tableView.getItems().size() > 1) {
            tableView.getSelectionModel().select(1);
        } else if (!tableView.getItems().isEmpty()) {
            tableView.getSelectionModel().select(0);
        }
        // Actualizamos los contadores de abajo (pie de pagina)
        updateStats();
    }

    private void initData() {
        // esto lo hemos buscado en Gemini porque nos daba pereza inventarnos 24 incidencias a mano (Consulta 2 en docs/IA.md)
        // Total 24 tickets: 8 Abiertas, 6 En proceso, 10 Resueltas
        masterData.addAll(
            new Incidencia(1, "Falta de Tóner", "Impresora de la primera planta requiere recambio urgente.", "Abierta", "Alta"),
            new Incidencia(2, "Falta de Tóner", "Impresora del departamento de contabilidad sin tinta negra ...", "Abierta", "Alta"),
            new Incidencia(3, "Falta de Tóner", "Tóner sustituido en administración central.", "Resuelta", "Baja"),
            new Incidencia(4, "Falta de Inico contaçor", "Incidencia resuelta en el módulo del software de contabilidad.", "Resuelta", "Alta"),
            new Incidencia(5, "Falta de Inte merciaga", "Restablecida la integración de mensajería interna.", "Resuelta", "Baja"),
            new Incidencia(6, "Falta de Elegántica", "Plantillas visuales corregidas según el diseño institucional.", "Resuelta", "Baja"),
            new Incidencia(7, "Error de sincronización ERP", "Fallo al conectar con el servidor central de inventarios.", "Abierta", "Alta"),
            new Incidencia(8, "Problema con la VPN", "Los usuarios en remoto reportan desconexiones continuas.", "Abierta", "Alta"),
            new Incidencia(9, "Certificado digital caducado", "Renovación obligatoria de firmas electrónicas en nóminas.", "Abierta", "Media"),
            new Incidencia(10, "Bloqueo de contraseña", "Usuario bloqueó su cuenta tras tres intentos fallidos.", "Abierta", "Baja"),
            new Incidencia(11, "Backup semanal incompleto", "El disco externo se quedó sin espacio para el volcado.", "Abierta", "Alta"),
            new Incidencia(12, "Lentitud general en la red", "Saturación detectada en el switch de la sala de reuniones.", "Abierta", "Alta"),
            new Incidencia(13, "Configuración nuevo puesto", "Preparar nuevo equipo con Windows 11 y paquete ofimático.", "En proceso", "Media"),
            new Incidencia(14, "Migración cuentas de correo", "Traslado de buzones corporativos a la nube.", "En proceso", "Alta"),
            new Incidencia(15, "Revisión reglas de cortafuegos", "Auditoría de seguridad y cierre de puertos desusados.", "En proceso", "Baja"),
            new Incidencia(16, "Instalación software tributario", "Actualización a la última versión para liquidaciones.", "En proceso", "Media"),
            new Incidencia(17, "Sustitución router secundario", "Reemplazo preventivo por modelo con Wi-Fi 6.", "En proceso", "Baja"),
            new Incidencia(18, "Mantenimiento sala de servidores", "Comprobación de SAIs y temperatura ambiente.", "En proceso", "Media"),
            new Incidencia(19, "Recuperación fichero borrado", "Restauración de hoja de cálculo desde snapshot diario.", "Resuelta", "Media"),
            new Incidencia(20, "Reparación conector de red", "Clavija RJ45 rota en el despacho de dirección.", "Resuelta", "Baja"),
            new Incidencia(21, "Configurar escaneo a carpeta", "Acceso directo a carpeta SMB desde impresora multifunción.", "Resuelta", "Baja"),
            new Incidencia(22, "Teclado averiado", "Sustitución de periférico por teclado ergonómico.", "Resuelta", "Baja"),
            new Incidencia(23, "Reinicio switch departamental", "Reinicio programado finalizado con éxito.", "Resuelta", "Baja"),
            new Incidencia(24, "Reindexación base de datos", "Optimización de consultas lentas en el servidor SQL.", "Resuelta", "Media")
        );

        filteredData = new FilteredList<>(masterData, p -> true);
    }

    private HBox createHeader() {
        HBox header = new HBox(20);
        header.setAlignment(Pos.CENTER_LEFT);

        Label title = new Label("Gestión de Incidencias");
        title.getStyleClass().add("header-title");

        Region spacer1 = new Region();
        HBox.setHgrow(spacer1, Priority.ALWAYS);

        // Campo de busqueda rapida
        // esto lo sacamos de Gemini para filtrar mientras escribes en el buscador (Consulta 3 en docs/IA.md)
        searchField = new TextField();
        searchField.setPromptText("🔍  Buscar por ID, título, descripción...");
        searchField.getStyleClass().add("search-field");
        searchField.setPrefWidth(320);
        searchField.textProperty().addListener((obs, oldVal, newVal) -> applyFilter());

        // Boton para crear una nueva incidencia
        Button newBtn = new Button("+ Nueva Incidencia");
        newBtn.getStyleClass().add("primary-button");
        newBtn.setOnAction(e -> enterNewMode());

        header.getChildren().addAll(title, spacer1, searchField, newBtn);
        return header;
    }

    private HBox createFilterBar() {
        HBox filterBar = new HBox(10);
        filterBar.setAlignment(Pos.CENTER_LEFT);

        String[] statuses = {"Todos", "Abiertas", "En proceso", "Resueltas"};
        for (String status : statuses) {
            Button btn = new Button("[ " + status + " ]");
            btn.getStyleClass().add("filter-btn");
            if (status.equals("Todos")) {
                btn.getStyleClass().add("filter-btn-active");
            } else {
                btn.getStyleClass().add("filter-btn-inactive");
            }

            btn.setOnAction(e -> {
                currentStatusFilter = status;
                updateFilterButtonsUI(btn);
                applyFilter();
            });

            filterButtons.add(btn);
            filterBar.getChildren().add(btn);
        }

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button clearFiltersBtn = new Button("Limpiar filtros");
        clearFiltersBtn.getStyleClass().add("secondary-button");
        clearFiltersBtn.setOnAction(e -> resetFilters());

        filterBar.getChildren().addAll(spacer, clearFiltersBtn);
        return filterBar;
    }

    private void updateFilterButtonsUI(Button activeBtn) {
        // esto lo buscamos en Gemini porque no sabiamos como hacer para que los botones de filtro cambien de estilo al pinchar
        for (Button btn : filterButtons) {
            btn.getStyleClass().removeAll("filter-btn-active", "filter-btn-inactive");
            if (btn == activeBtn) {
                btn.getStyleClass().add("filter-btn-active");
            } else {
                btn.getStyleClass().add("filter-btn-inactive");
            }
        }
    }

    private void resetFilters() {
        // boton de limpiar filtros para dejarlo todo como al principio
        searchField.setText("");
        currentStatusFilter = "Todos";
        if (!filterButtons.isEmpty()) {
            updateFilterButtonsUI(filterButtons.get(0));
        }
        applyFilter();
    }

    private void applyFilter() {
        // esto nos ayudo Gemini a montarlo con un predicado compuesto para que no pete si esta vacio (Consulta 3 en docs/IA.md)
        String searchText = searchField.getText() == null ? "" : searchField.getText().trim().toLowerCase();

        filteredData.setPredicate(incidencia -> {
            // comprobamos el estado seleccionado
            if (!currentStatusFilter.equals("Todos")) {
                String filterNormalized = currentStatusFilter.equals("Abiertas") ? "Abierta" :
                                         currentStatusFilter.equals("Resueltas") ? "Resuelta" : currentStatusFilter;
                if (!incidencia.getEstado().equalsIgnoreCase(filterNormalized)) {
                    return false;
                }
            }

            // comprobamos si coincide el texto que escribe el usuario
            if (!searchText.isEmpty()) {
                boolean matchesId = incidencia.getIdFormatted().contains(searchText) || 
                                    String.valueOf(incidencia.getId()).contains(searchText);
                boolean matchesTitle = incidencia.getTitulo() != null && incidencia.getTitulo().toLowerCase().contains(searchText);
                boolean matchesDesc = incidencia.getDescripcion() != null && incidencia.getDescripcion().toLowerCase().contains(searchText);
                return matchesId || matchesTitle || matchesDesc;
            }

            return true;
        });

        // volvemos a contar los numeritos de abajo
        updateStats();
    }

    private VBox createTableCard() {
        VBox card = new VBox(12);
        card.getStyleClass().add("card-container");

        Label cardTitle = new Label("Tabla de Incidencias");
        cardTitle.getStyleClass().add("card-title");

        tableView = new TableView<>();
        tableView.getStyleClass().add("custom-table");
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        VBox.setVgrow(tableView, Priority.ALWAYS);

        // Columna del ID
        TableColumn<Incidencia, String> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(data -> Bindings.createStringBinding(data.getValue()::getIdFormatted));
        colId.setMinWidth(60);
        colId.setMaxWidth(80);
        colId.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(item + "  |");
                    setStyle("-fx-text-fill: #334155; -fx-font-weight: 500;");
                }
            }
        });

        // Columna del titulo
        TableColumn<Incidencia, String> colTitulo = new TableColumn<>("Título");
        colTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colTitulo.setMinWidth(150);

        // Columna del estado
        // esto nos dio error y le pedimos a Gemini que nos dijera como pintar las celdas con colores tipo badge (Consulta 4 en docs/IA.md)
        TableColumn<Incidencia, String> colEstado = new TableColumn<>("Estado");
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        colEstado.setMinWidth(110);
        colEstado.setMaxWidth(130);
        colEstado.setCellFactory(col -> new TableCell<>() {
            private final Label badge = new Label();
            {
                badge.getStyleClass().add("badge");
            }

            @Override
            protected void updateItem(String estado, boolean empty) {
                super.updateItem(estado, empty);
                if (empty || estado == null) {
                    setGraphic(null);
                } else {
                    badge.setText(estado);
                    badge.getStyleClass().removeAll("badge-red", "badge-gray", "badge-yellow");
                    if ("Abierta".equalsIgnoreCase(estado)) {
                        badge.getStyleClass().add("badge-red");
                    } else if ("Resuelta".equalsIgnoreCase(estado)) {
                        badge.getStyleClass().add("badge-gray");
                    } else {
                        badge.getStyleClass().add("badge-yellow");
                    }
                    setGraphic(badge);
                    setAlignment(Pos.CENTER_LEFT);
                }
            }
        });

        // Columna de prioridad
        TableColumn<Incidencia, String> colPrioridad = new TableColumn<>("Prioridad");
        colPrioridad.setCellValueFactory(new PropertyValueFactory<>("prioridad"));
        colPrioridad.setMinWidth(100);
        colPrioridad.setMaxWidth(120);
        colPrioridad.setCellFactory(col -> new TableCell<>() {
            private final Label badge = new Label();
            {
                badge.getStyleClass().add("badge");
            }

            @Override
            protected void updateItem(String prioridad, boolean empty) {
                super.updateItem(prioridad, empty);
                if (empty || prioridad == null) {
                    setGraphic(null);
                } else {
                    badge.setText(prioridad);
                    badge.getStyleClass().removeAll("badge-red", "badge-gray", "badge-yellow");
                    if ("Alta".equalsIgnoreCase(prioridad)) {
                        badge.getStyleClass().add("badge-red");
                    } else if ("Baja".equalsIgnoreCase(prioridad)) {
                        badge.getStyleClass().add("badge-gray");
                    } else {
                        badge.getStyleClass().add("badge-yellow");
                    }
                    setGraphic(badge);
                    setAlignment(Pos.CENTER_LEFT);
                }
            }
        });

        tableView.getColumns().addAll(List.of(colId, colTitulo, colEstado, colPrioridad));

        // esto de ordenar la tabla con el comparator lo sacamos de StackOverflow y Gemini
        SortedList<Incidencia> sortedList = new SortedList<>(filteredData);
        sortedList.comparatorProperty().bind(tableView.comparatorProperty());
        tableView.setItems(sortedList);

        // listener para cuando el usuario pincha en una fila cargar los datos a la derecha
        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                isNewMode = false;
                selectedIncidencia = newSel;
                populateForm(newSel);
            }
        });

        card.getChildren().addAll(cardTitle, tableView);
        return card;
    }

    private VBox createDetailCard() {
        VBox card = new VBox(14);
        card.getStyleClass().add("card-container");

        detailTitleLabel = new Label("Detalle de Incidencia");
        detailTitleLabel.getStyleClass().add("card-title");

        // Campo para el titulo
        VBox titleBox = new VBox(6);
        Label titleLbl = new Label("Título");
        titleLbl.getStyleClass().add("form-label");
        titleField = new TextField();
        titleField.getStyleClass().add("custom-text-field");
        titleBox.getChildren().addAll(titleLbl, titleField);

        // Area para la descripcion
        VBox descBox = new VBox(6);
        Label descLbl = new Label("Descripción");
        descLbl.getStyleClass().add("form-label");
        descArea = new TextArea();
        descArea.getStyleClass().add("custom-text-area");
        descArea.setWrapText(true);
        descArea.setPrefRowCount(4);
        descBox.getChildren().addAll(descLbl, descArea);

        // Desplegables para estado y prioridad
        HBox combosBox = new HBox(12);

        VBox estadoBox = new VBox(6);
        Label estadoLbl = new Label("Estado");
        estadoLbl.getStyleClass().add("form-label");
        statusCombo = new ComboBox<>(FXCollections.observableArrayList("Abierta", "En proceso", "Resuelta"));
        statusCombo.getStyleClass().add("custom-combo-box");
        statusCombo.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(estadoBox, Priority.ALWAYS);
        estadoBox.getChildren().addAll(estadoLbl, statusCombo);

        VBox priorityBox = new VBox(6);
        Label priorityLbl = new Label("Prioridad");
        priorityLbl.getStyleClass().add("form-label");
        priorityCombo = new ComboBox<>(FXCollections.observableArrayList("Baja", "Media", "Alta"));
        priorityCombo.getStyleClass().add("custom-combo-box");
        priorityCombo.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(priorityBox, Priority.ALWAYS);
        priorityBox.getChildren().addAll(priorityLbl, priorityCombo);

        combosBox.getChildren().addAll(estadoBox, priorityBox);

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        // Botones de accion para guardar y borrar
        HBox actionButtons = new HBox(10);
        actionButtons.setAlignment(Pos.CENTER_RIGHT);

        deleteBtn = new Button("Eliminar");
        deleteBtn.getStyleClass().add("secondary-button");
        deleteBtn.setOnAction(e -> deleteSelected());

        Button saveBtn = new Button("Guardar cambios");
        saveBtn.getStyleClass().add("primary-button");
        saveBtn.setOnAction(e -> saveChanges());

        HBox.setHgrow(deleteBtn, Priority.ALWAYS);
        HBox.setHgrow(saveBtn, Priority.ALWAYS);
        deleteBtn.setMaxWidth(Double.MAX_VALUE);
        saveBtn.setMaxWidth(Double.MAX_VALUE);

        actionButtons.getChildren().addAll(deleteBtn, saveBtn);

        card.getChildren().addAll(detailTitleLabel, titleBox, descBox, combosBox, spacer, actionButtons);
        return card;
    }

    private void populateForm(Incidencia inc) {
        detailTitleLabel.setText("Detalle de Incidencia #" + inc.getIdFormatted());
        titleField.setText(inc.getTitulo());
        descArea.setText(inc.getDescripcion());
        statusCombo.setValue(inc.getEstado());
        priorityCombo.setValue(inc.getPrioridad());
        deleteBtn.setDisable(false);
    }

    private void enterNewMode() {
        // modo para meter una incidencia nueva, limpiamos los campos
        isNewMode = true;
        selectedIncidencia = null;
        tableView.getSelectionModel().clearSelection();

        detailTitleLabel.setText("Nueva Incidencia");
        titleField.setText("");
        descArea.setText("");
        statusCombo.setValue("Abierta");
        priorityCombo.setValue("Media");
        deleteBtn.setDisable(true); // deshabilitamos el boton de borrar porque aun no existe
        titleField.requestFocus();
    }

    private void saveChanges() {
        // recogemos los datos del formulario a mano
        String title = titleField.getText().trim();
        String desc = descArea.getText().trim();
        String status = statusCombo.getValue();
        String priority = priorityCombo.getValue();

        // comprobamos que el titulo no este vacio para que no metan cosas raras
        if (title.isEmpty()) {
            showAlert("Validación", "Por favor introduce un título para la incidencia.");
            return;
        }

        if (isNewMode) {
            // esto lo buscamos en Gemini para sacar el id mas alto y sumarle uno (Consulta 5 en docs/IA.md)
            int nextId = masterData.stream().mapToInt(Incidencia::getId).max().orElse(0) + 1;
            Incidencia newInc = new Incidencia(nextId, title, desc, status != null ? status : "Abierta", priority != null ? priority : "Media");
            masterData.add(newInc);
            tableView.getSelectionModel().select(newInc);
            statusMessageLabel.setText("✓ Incidencia #" + newInc.getIdFormatted() + " creada correctamente");
        } else if (selectedIncidencia != null) {
            // modificamos la incidencia que estaba seleccionada
            selectedIncidencia.setTitulo(title);
            selectedIncidencia.setDescripcion(desc);
            if (status != null) selectedIncidencia.setEstado(status);
            if (priority != null) selectedIncidencia.setPrioridad(priority);
            tableView.refresh(); // refrescamos la tabla para que se vean los cambios
            statusMessageLabel.setText("✓ Cambios guardados para la incidencia #" + selectedIncidencia.getIdFormatted());
        }

        updateStats();
    }

    private void deleteSelected() {
        // si borras la incidencia la quitamos de la lista y seleccionamos la primera que quede para que no se quede en blanco
        if (selectedIncidencia != null) {
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

    private HBox createFooter() {
        HBox footer = new HBox(28);
        footer.getStyleClass().add("footer-container");
        footer.setAlignment(Pos.CENTER_LEFT);

        totalCountLabel = new Label("24");
        totalCountLabel.setStyle("-fx-text-fill: #2563eb; -fx-font-weight: bold; -fx-font-size: 19px;");

        abiertasCountLabel = new Label("8");
        abiertasCountLabel.setStyle("-fx-text-fill: #ef4444; -fx-font-weight: bold; -fx-font-size: 19px;");

        enProcesoCountLabel = new Label("6");
        enProcesoCountLabel.setStyle("-fx-text-fill: #0f172a; -fx-font-weight: bold; -fx-font-size: 19px;");

        resueltasCountLabel = new Label("10");
        resueltasCountLabel.setStyle("-fx-text-fill: #0f172a; -fx-font-weight: bold; -fx-font-size: 19px;");

        HBox totalBox = new HBox(4, createStatCaption("Total:"), totalCountLabel);
        HBox abiertasBox = new HBox(4, createStatCaption("Abiertas:"), abiertasCountLabel);
        HBox enProcesoBox = new HBox(4, createStatCaption("En proceso:"), enProcesoCountLabel);
        HBox resueltasBox = new HBox(4, createStatCaption("Resueltas:"), resueltasCountLabel);

        totalBox.setAlignment(Pos.BASELINE_LEFT);
        abiertasBox.setAlignment(Pos.BASELINE_LEFT);
        enProcesoBox.setAlignment(Pos.BASELINE_LEFT);
        resueltasBox.setAlignment(Pos.BASELINE_LEFT);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        statusMessageLabel = new Label("✓ Sistema listo y actualizado");
        statusMessageLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #1e293b; -fx-font-weight: 500;");

        footer.getChildren().addAll(totalBox, abiertasBox, enProcesoBox, resueltasBox, spacer, statusMessageLabel);
        return footer;
    }

    private Label createStatCaption(String text) {
        Label lbl = new Label(text);
        lbl.getStyleClass().add("stat-item");
        return lbl;
    }

    private void updateStats() {
        // esto lo hemos sacado de Gemini con streams para contar cuantas hay de cada tipo (Consulta 5 en docs/IA.md)
        long total = masterData.size();
        long abiertas = masterData.stream().filter(i -> "Abierta".equalsIgnoreCase(i.getEstado())).count();
        long enProceso = masterData.stream().filter(i -> "En proceso".equalsIgnoreCase(i.getEstado())).count();
        long resueltas = masterData.stream().filter(i -> "Resuelta".equalsIgnoreCase(i.getEstado())).count();

        // actualizamos las etiquetas con los valores nuevos
        if (totalCountLabel != null) totalCountLabel.setText(String.valueOf(total));
        if (abiertasCountLabel != null) abiertasCountLabel.setText(String.valueOf(abiertas));
        if (enProcesoCountLabel != null) enProcesoCountLabel.setText(String.valueOf(enProceso));
        if (resueltasCountLabel != null) resueltasCountLabel.setText(String.valueOf(resueltas));
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
