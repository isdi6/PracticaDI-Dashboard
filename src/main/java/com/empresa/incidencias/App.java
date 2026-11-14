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
import javafx.scene.layout.*;package com.empresa.incidencias;

import java.util.ArrayList;
import java.util.List;

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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Clase principal de la aplicación de escritorio JavaFX para la gestión de incidencias.
 * Extiende de Application e implementa la interfaz gráfica completa (CRUD, filtros, estadísticas y buscador).
 */
public class App extends Application {

    // Lista observable maestra que contiene todas las incidencias cargadas en memoria. 
    // Al ser ObservableList, cualquier cambio en ella refresca automáticamente los componentes vinculados.
    private final ObservableList<Incidencia> masterData = FXCollections.observableArrayList();
    
    // Lista filtrada que permite aplicar criterios de búsqueda y filtros de estado sin alterar los datos originales.
    private FilteredList<Incidencia> filteredData;
    
    // Componentes principales de la UI
    private TableView<Incidencia> tableView;
    private TextField searchField;
    private String currentStatusFilter = "Todos";

    // Controles del formulario lateral derecho (para crear o editar incidencias)
    private Label detailTitleLabel;
    private TextField titleField;
    private TextArea descArea;
    private ComboBox<String> statusCombo;
    private ComboBox<String> priorityCombo;
    private Button deleteBtn;

    // Etiquetas dinámicas para los contadores del pie de página (footer)
    private Label totalCountLabel;
    private Label abiertasCountLabel;
    private Label enProcesoCountLabel;
    private Label resueltasCountLabel;
    private Label statusMessageLabel;

    // Colección para almacenar los botones de filtro por estado y gestionar sus estilos activos/inactivos
    private final List<Button> filterButtons = new ArrayList<>();

    // Variables de control de estado del formulario
    private Incidencia selectedIncidencia = null;
    private boolean isNewMode = false;

    /**
     * Método de inicio obligatorio en JavaFX. Configura la jerarquía de contenedores, la escena y la ventana principal.
     */
    @Override
    public void start(Stage stage) {
        // 1. Cargamos los datos de prueba iniciales en la lista maestra
        initData();

        // 2. Layout principal de tipo BorderPane que organiza la interfaz por zonas (Top, Center, Bottom)
        BorderPane mainLayout = new BorderPane();
        mainLayout.getStyleClass().add("root-container");

        // 3. Zona superior: Contiene la cabecera con el título, la barra de búsqueda y los botones de filtro
        VBox topBox = new VBox(16);
        topBox.getChildren().addAll(createHeader(), createFilterBar());
        mainLayout.setTop(topBox);

        // 4. Zona central: Distribuye horizontalmente la tabla (izquierda) y el formulario de detalles (derecha)
        HBox centerBox = new HBox(20);
        centerBox.setPadding(new Insets(16, 0, 16, 0));
        VBox.setVgrow(centerBox, Priority.ALWAYS);

        VBox leftCard = createTableCard();
        VBox rightCard = createDetailCard();

        // Configuramos las proporciones de ancho para que la tabla expanda y el panel derecho sea fijo
        HBox.setHgrow(leftCard, Priority.ALWAYS);
        rightCard.setPrefWidth(340);
        rightCard.setMinWidth(300);
        rightCard.setMaxWidth(380);

        centerBox.getChildren().addAll(leftCard, rightCard);
        mainLayout.setCenter(centerBox);

        // 5. Zona inferior: Barra de estado con los contadores globales de incidencias
        HBox footerBar = createFooter();
        
        BorderPane rootWrapper = new BorderPane();
        rootWrapper.setCenter(mainLayout);
        rootWrapper.setBottom(footerBar);

        // 6. Configuración de la Escena (Scene) y aplicación de la hoja de estilos CSS externa
        Scene scene = new Scene(rootWrapper, 1060, 680);
        String css = getClass().getResource("/com/empresa/incidencias/style.css").toExternalForm();
        scene.getStylesheets().add(css);

        // 7. Configuración final del Stage (Ventana)
        stage.setTitle("Panel de Control [Ismael]");
        stage.setScene(scene);
        stage.setMinWidth(960);
        stage.setMinHeight(600);
        stage.show();

        // Seleccionamos un elemento por defecto al arrancar si la tabla tiene elementos
        if (tableView.getItems().size() > 1) {
            tableView.getSelectionModel().select(1);
        } else if (!tableView.getItems().isEmpty()) {
            tableView.getSelectionModel().select(0);
        }
        
        // Actualizamos por primera vez las estadísticas numéricas del pie de página
        updateStats();
    }

    /**
     * Inicializa la lista masterData con 24 registros de prueba simulando incidencias reales de una empresa.
     */
    private void initData() {
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
            new Incidencia(24, "Reindexación base de datos", "Optimización de consultas lentas en el servidor SQL.", "Resuelta", "Media"),
            new Incidencia(25, "Mi primera prueba", "Se ha actualizado la base de datos de firmas a la última versión.", "Resuelta", "Media")
        );

        // Envolvemos la lista maestra en una FilteredList inicializada sin restricciones (p -> true)
        filteredData = new FilteredList<>(masterData, p -> true);
    }

    /**
     * Construye la barra superior con el título de la aplicación, el buscador de texto en tiempo real y el botón de nueva incidencia.
     */
    private HBox createHeader() {
        HBox header = new HBox(20);
        header.setAlignment(Pos.CENTER_LEFT);

        Label title = new Label("Gestión de Incidencias");
        title.getStyleClass().add("header-title");

        Region spacer1 = new Region();
        HBox.setHgrow(spacer1, Priority.ALWAYS);

        // Campo de texto para la búsqueda interactiva con un Listener que reacciona a cada pulsación de tecla
        searchField = new TextField();
        searchField.setPromptText("🔍  Buscar por ID, título, descripción...");
        searchField.getStyleClass().add("search-field");
        searchField.setPrefWidth(320);
        searchField.textProperty().addListener((obs, oldVal, newVal) -> applyFilter());

        // Botón para activar el modo de creación de una nueva incidencia
        Button newBtn = new Button("+ Crear Ticket");
        newBtn.getStyleClass().add("primary-button");
        newBtn.setOnAction(e -> enterNewMode());

        header.getChildren().addAll(title, spacer1, searchField, newBtn);
        return header;
    }

    /**
     * Crea la barra de filtros por estado y el botón global para limpiar los filtros aplicados.
     */
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

            // Evento al hacer clic en un botón de filtro: actualiza el estado actual y recalcula los filtros
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

    /**
     * Actualiza de forma dinámica las clases CSS de los botones de filtro para resaltar visualmente el activo.
     */
    private void updateFilterButtonsUI(Button activeBtn) {
        for (Button btn : filterButtons) {
            btn.getStyleClass().removeAll("filter-btn-active", "filter-btn-inactive");
            if (btn == activeBtn) {
                btn.getStyleClass().add("filter-btn-active");
            } else {
                btn.getStyleClass().add("filter-btn-inactive");
            }
        }
    }

    /**
     * Restablece los filtros de búsqueda y estado a sus valores iniciales por defecto.
     */
    private void resetFilters() {
        searchField.setText("");
        currentStatusFilter = "Todos";
        if (!filterButtons.isEmpty()) {
            updateFilterButtonsUI(filterButtons.get(0));
        }
        applyFilter();
    }

    /**
     * Aplica el predicado combinado de búsqueda por texto y filtrado por estado sobre la filteredData.
     */
    private void applyFilter() {
        String searchText = searchField.getText() == null ? "" : searchField.getText().trim().toLowerCase();

        filteredData.setPredicate(incidencia -> {
            // 1. Filtrado por estado seleccionado en los botones superiores
            if (!currentStatusFilter.equals("Todos")) {
                String filterNormalized = currentStatusFilter.equals("Abiertas") ? "Abierta" :
                                         currentStatusFilter.equals("Resueltas") ? "Resuelta" : currentStatusFilter;
                if (!incidencia.getEstado().equalsIgnoreCase(filterNormalized)) {
                    return false;
                }
            }

            // 2. Filtrado por coincidencia de texto en ID, título o descripción
            if (!searchText.isEmpty()) {
                boolean matchesId = incidencia.getIdFormatted().contains(searchText) || 
                                    String.valueOf(incidencia.getId()).contains(searchText);
                boolean matchesTitle = incidencia.getTitulo() != null && incidencia.getTitulo().toLowerCase().contains(searchText);
                boolean matchesDesc = incidencia.getDescripcion() != null && incidencia.getDescripcion().toLowerCase().contains(searchText);
                return matchesId || matchesTitle || matchesDesc;
            }

            return true;
        });

        updateStats();
    }

    /**
     * Construye el contenedor de la tarjeta izquierda que engloba el componente TableView y sus columnas.
     */
    private VBox createTableCard() {
        VBox card = new VBox(12);
        card.getStyleClass().add("card-container");

        Label cardTitle = new Label("Tabla de Incidencias");
        cardTitle.getStyleClass().add("card-title");

        tableView = new TableView<>();
        tableView.getStyleClass().add("custom-table");
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        VBox.setVgrow(tableView, Priority.ALWAYS);

        // Columna 1: ID formateado con ceros a la izquierda
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

        // Columna 2: Título de la incidencia
        TableColumn<Incidencia, String> colTitulo = new TableColumn<>("Título");
        colTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colTitulo.setMinWidth(150);

        // Columna 3: Estado con celdas personalizadas en formato "Badge" (etiquetas de colores)
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

        // Columna 4: Prioridad con celdas personalizadas en formato "Badge"
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

        // Vinculamos la SortedList a la tabla para permitir ordenación por columnas sin romper los filtros
        SortedList<Incidencia> sortedList = new SortedList<>(filteredData);
        sortedList.comparatorProperty().bind(tableView.comparatorProperty());
        tableView.setItems(sortedList);

        // Listener de selección en la tabla: carga automáticamente los datos de la fila seleccionada al formulario derecho
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

    /**
     * Construye la tarjeta derecha con el formulario de detalles, campos de edición y botones de acción (Guardar/Eliminar).
     */
    private VBox createDetailCard() {
        VBox card = new VBox(14);
        card.getStyleClass().add("card-container");

        detailTitleLabel = new Label("Detalle de Incidencia");
        detailTitleLabel.getStyleClass().add("card-title");

        // Campo de texto para el Título
        VBox titleBox = new VBox(6);
        Label titleLbl = new Label("Título");
        titleLbl.getStyleClass().add("form-label");
        titleField = new TextField();
        titleField.getStyleClass().add("custom-text-field");
        titleBox.getChildren().addAll(titleLbl, titleField);

        // Área de texto para la Descripción
        VBox descBox = new VBox(6);
        Label descLbl = new Label("Descripción");
        descLbl.getStyleClass().add("form-label");
        descArea = new TextArea();
        descArea.getStyleClass().add("custom-text-area");
        descArea.setWrapText(true);
        descArea.setPrefRowCount(4);
        descBox.getChildren().addAll(descLbl, descArea);

        // Contenedor horizontal para los ComboBox de Estado y Prioridad
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

        // Botones de acción inferiores (Eliminar y Guardar cambios)
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

    /**
     * Rellena los campos del formulario derecho con los datos de la incidencia seleccionada en la tabla.
     */
    private void populateForm(Incidencia inc) {
        detailTitleLabel.setText("Detalle de Incidencia #" + inc.getIdFormatted());
        titleField.setText(inc.getTitulo());
        descArea.setText(inc.getDescripcion());
        statusCombo.setValue(inc.getEstado());
        priorityCombo.setValue(inc.getPrioridad());
        deleteBtn.setDisable(false);
    }

    /**
     * Prepara el formulario para el modo de inserción de una nueva incidencia limpiando los campos.
     */
    private void enterNewMode() {
        isNewMode = true;
        selectedIncidencia = null;
        tableView.getSelectionModel().clearSelection();

        detailTitleLabel.setText("Nueva Incidencia");
        titleField.setText("");
        descArea.setText("");
        statusCombo.setValue("Abierta");
        priorityCombo.setValue("Media");
        deleteBtn.setDisable(true);
        titleField.requestFocus();
    }

    /**
     * Gestiona la lógica de guardado: crea una nueva incidencia si está en modo alta o actualiza la seleccionada si está en edición.
     */
    private void saveChanges() {
    String title = titleField.getText().trim();
    String desc = descArea.getText().trim();
    String status = statusCombo.getValue();
    String priority = priorityCombo.getValue();

    // Validación básica de campos obligatorios
    if (title.isEmpty()) {
        showAlert("Validación", "Por favor introduce un título para la incidencia.");
        return;
    }

    // 1. Traducimos el operador ternario del estado a un if/else normal
    String estadoFinal = "Abierta"; // Valor por defecto si es nulo
    if (status != null) {
        estadoFinal = status;
    }

    // 2. Traducimos el operador ternario de la prioridad a un if/else normal
    String prioridadFinal = "Media"; // Valor por defecto si es nulo
    if (priority != null) {
        prioridadFinal = priority;
    }

    if (isNewMode) {
        int nextId = masterData.stream().mapToInt(Incidencia::getId).max().orElse(0) + 1;
        
        // Ahora usamos las variables limpias que acabamos de definir arriba
        Incidencia newInc = new Incidencia(nextId, title, desc, estadoFinal, prioridadFinal);
        
        masterData.add(newInc);
        tableView.getSelectionModel().select(newInc);
        statusMessageLabel.setText("✓ Incidencia #" + newInc.getIdFormatted() + " creada correctamente");
    } else if (selectedIncidencia != null) {
        selectedIncidencia.setTitulo(title);
        selectedIncidencia.setDescripcion(desc);
        selectedIncidencia.setEstado(estadoFinal);
        selectedIncidencia.setPrioridad(prioridadFinal);
        tableView.refresh();
        statusMessageLabel.setText("✓ Cambios guardados para la incidencia #" + selectedIncidencia.getIdFormatted());
    }

    updateStats();
}

    /**
     * Elimina la incidencia seleccionada actualmente de la lista maestra.
     */
    private void deleteSelected() {
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

    /**
     * Construye la barra inferior (Footer) con los contadores estadísticos en tiempo real.
     */
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

    /**
     * Método auxiliar para crear etiquetas descriptivas en las estadísticas del footer.
     */
    private Label createStatCaption(String text) {
        Label lbl = new Label(text);
        lbl.getStyleClass().add("stat-item");
        return lbl;
    }

    /**
     * Utiliza Streams de Java para contar y actualizar en tiempo real el número de incidencias según su estado.
     */
    private void updateStats() {
        long total = masterData.size();
        long abiertas = masterData.stream().filter(i -> "Abierta".equalsIgnoreCase(i.getEstado())).count();
        long enProceso = masterData.stream().filter(i -> "En proceso".equalsIgnoreCase(i.getEstado())).count();
        long resueltas = masterData.stream().filter(i -> "Resuelta".equalsIgnoreCase(i.getEstado())).count();

        if (totalCountLabel != null) totalCountLabel.setText(String.valueOf(total));
        if (abiertasCountLabel != null) abiertasCountLabel.setText(String.valueOf(abiertas));
        if (enProcesoCountLabel != null) enProcesoCountLabel.setText(String.valueOf(enProceso));
        if (resueltasCountLabel != null) resueltasCountLabel.setText(String.valueOf(resueltas));
    }

    /**
     * Muestra una ventana emergente de alerta (Alert) para notificar validaciones al usuario.
     */
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Método principal main que lanza la ejecución de la aplicación JavaFX.
     */
    public static void main(String[] args) {
        launch(args);
    }
}

import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase principal de la aplicación de escritorio JavaFX para la gestión de incidencias.
 * Extiende de Application e implementa la interfaz gráfica completa (CRUD, filtros, estadísticas y buscador).
 */
public class App extends Application {

    // Lista observable maestra que contiene todas las incidencias cargadas en memoria. 
    // Al ser ObservableList, cualquier cambio en ella refresca automáticamente los componentes vinculados.
    private final ObservableList<Incidencia> masterData = FXCollections.observableArrayList();
    
    // Lista filtrada que permite aplicar criterios de búsqueda y filtros de estado sin alterar los datos originales.
    private FilteredList<Incidencia> filteredData;
    
    // Componentes principales de la UI
    private TableView<Incidencia> tableView;
    private TextField searchField;
    private String currentStatusFilter = "Todos";

    // Controles del formulario lateral derecho (para crear o editar incidencias)
    private Label detailTitleLabel;
    private TextField titleField;
    private TextArea descArea;
    private ComboBox<String> statusCombo;
    private ComboBox<String> priorityCombo;
    private Button deleteBtn;

    // Etiquetas dinámicas para los contadores del pie de página (footer)
    private Label totalCountLabel;
    private Label abiertasCountLabel;
    private Label enProcesoCountLabel;
    private Label resueltasCountLabel;
    private Label statusMessageLabel;

    // Colección para almacenar los botones de filtro por estado y gestionar sus estilos activos/inactivos
    private final List<Button> filterButtons = new ArrayList<>();

    // Variables de control de estado del formulario
    private Incidencia selectedIncidencia = null;
    private boolean isNewMode = false;

    /**
     * Método de inicio obligatorio en JavaFX. Configura la jerarquía de contenedores, la escena y la ventana principal.
     */
    @Override
    public void start(Stage stage) {
        // 1. Cargamos los datos de prueba iniciales en la lista maestra
        initData();

        // 2. Layout principal de tipo BorderPane que organiza la interfaz por zonas (Top, Center, Bottom)
        BorderPane mainLayout = new BorderPane();
        mainLayout.getStyleClass().add("root-container");

        // 3. Zona superior: Contiene la cabecera con el título, la barra de búsqueda y los botones de filtro
        VBox topBox = new VBox(16);
        topBox.getChildren().addAll(createHeader(), createFilterBar());
        mainLayout.setTop(topBox);

        // 4. Zona central: Distribuye horizontalmente la tabla (izquierda) y el formulario de detalles (derecha)
        HBox centerBox = new HBox(20);
        centerBox.setPadding(new Insets(16, 0, 16, 0));
        VBox.setVgrow(centerBox, Priority.ALWAYS);

        VBox leftCard = createTableCard();
        VBox rightCard = createDetailCard();

        // Configuramos las proporciones de ancho para que la tabla expanda y el panel derecho sea fijo
        HBox.setHgrow(leftCard, Priority.ALWAYS);
        rightCard.setPrefWidth(340);
        rightCard.setMinWidth(300);
        rightCard.setMaxWidth(380);

        centerBox.getChildren().addAll(leftCard, rightCard);
        mainLayout.setCenter(centerBox);

        // 5. Zona inferior: Barra de estado con los contadores globales de incidencias
        HBox footerBar = createFooter();
        
        BorderPane rootWrapper = new BorderPane();
        rootWrapper.setCenter(mainLayout);
        rootWrapper.setBottom(footerBar);

        // 6. Configuración de la Escena (Scene) y aplicación de la hoja de estilos CSS externa
        Scene scene = new Scene(rootWrapper, 1060, 680);
        String css = getClass().getResource("/com/empresa/incidencias/style.css").toExternalForm();
        scene.getStylesheets().add(css);

        // 7. Configuración final del Stage (Ventana)
        stage.setTitle("Gestión de Incidencias");
        stage.setScene(scene);
        stage.setMinWidth(960);
        stage.setMinHeight(600);
        stage.show();

        // Seleccionamos un elemento por defecto al arrancar si la tabla tiene elementos
        if (tableView.getItems().size() > 1) {
            tableView.getSelectionModel().select(1);
        } else if (!tableView.getItems().isEmpty()) {
            tableView.getSelectionModel().select(0);
        }
        
        // Actualizamos por primera vez las estadísticas numéricas del pie de página
        updateStats();
    }

    /**
     * Inicializa la lista masterData con 24 registros de prueba simulando incidencias reales de una empresa.
     */
    private void initData() {
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

        // Envolvemos la lista maestra en una FilteredList inicializada sin restricciones (p -> true)
        filteredData = new FilteredList<>(masterData, p -> true);
    }

    /**
     * Construye la barra superior con el título de la aplicación, el buscador de texto en tiempo real y el botón de nueva incidencia.
     */
    private HBox createHeader() {
        HBox header = new HBox(20);
        header.setAlignment(Pos.CENTER_LEFT);

        Label title = new Label("Gestión de Incidencias");
        title.getStyleClass().add("header-title");

        Region spacer1 = new Region();
        HBox.setHgrow(spacer1, Priority.ALWAYS);

        // Campo de texto para la búsqueda interactiva con un Listener que reacciona a cada pulsación de tecla
        searchField = new TextField();
        searchField.setPromptText("🔍  Buscar por ID, título, descripción...");
        searchField.getStyleClass().add("search-field");
        searchField.setPrefWidth(320);
        searchField.textProperty().addListener((obs, oldVal, newVal) -> applyFilter());

        // Botón para activar el modo de creación de una nueva incidencia
        Button newBtn = new Button("+ Nueva Incidencia");
        newBtn.getStyleClass().add("primary-button");
        newBtn.setOnAction(e -> enterNewMode());

        header.getChildren().addAll(title, spacer1, searchField, newBtn);
        return header;
    }

    /**
     * Crea la barra de filtros por estado y el botón global para limpiar los filtros aplicados.
     */
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

            // Evento al hacer clic en un botón de filtro: actualiza el estado actual y recalcula los filtros
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

    /**
     * Actualiza de forma dinámica las clases CSS de los botones de filtro para resaltar visualmente el activo.
     */
    private void updateFilterButtonsUI(Button activeBtn) {
        for (Button btn : filterButtons) {
            btn.getStyleClass().removeAll("filter-btn-active", "filter-btn-inactive");
            if (btn == activeBtn) {
                btn.getStyleClass().add("filter-btn-active");
            } else {
                btn.getStyleClass().add("filter-btn-inactive");
            }
        }
    }

    /**
     * Restablece los filtros de búsqueda y estado a sus valores iniciales por defecto.
     */
    private void resetFilters() {
        searchField.setText("");
        currentStatusFilter = "Todos";
        if (!filterButtons.isEmpty()) {
            updateFilterButtonsUI(filterButtons.get(0));
        }
        applyFilter();
    }

    /**
     * Aplica el predicado combinado de búsqueda por texto y filtrado por estado sobre la filteredData.
     */
    private void applyFilter() {
        String searchText = searchField.getText() == null ? "" : searchField.getText().trim().toLowerCase();

        filteredData.setPredicate(incidencia -> {
            // 1. Filtrado por estado seleccionado en los botones superiores
            if (!currentStatusFilter.equals("Todos")) {
                String filterNormalized = currentStatusFilter.equals("Abiertas") ? "Abierta" :
                                         currentStatusFilter.equals("Resueltas") ? "Resuelta" : currentStatusFilter;
                if (!incidencia.getEstado().equalsIgnoreCase(filterNormalized)) {
                    return false;
                }
            }

            // 2. Filtrado por coincidencia de texto en ID, título o descripción
            if (!searchText.isEmpty()) {
                boolean matchesId = incidencia.getIdFormatted().contains(searchText) || 
                                    String.valueOf(incidencia.getId()).contains(searchText);
                boolean matchesTitle = incidencia.getTitulo() != null && incidencia.getTitulo().toLowerCase().contains(searchText);
                boolean matchesDesc = incidencia.getDescripcion() != null && incidencia.getDescripcion().toLowerCase().contains(searchText);
                return matchesId || matchesTitle || matchesDesc;
            }

            return true;
        });

        updateStats();
    }

    /**
     * Construye el contenedor de la tarjeta izquierda que engloba el componente TableView y sus columnas.
     */
    private VBox createTableCard() {
        VBox card = new VBox(12);
        card.getStyleClass().add("card-container");

        Label cardTitle = new Label("Tabla de Incidencias");
        cardTitle.getStyleClass().add("card-title");

        tableView = new TableView<>();
        tableView.getStyleClass().add("custom-table");
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        VBox.setVgrow(tableView, Priority.ALWAYS);

        // Columna 1: ID formateado con ceros a la izquierda
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

        // Columna 2: Título de la incidencia
        TableColumn<Incidencia, String> colTitulo = new TableColumn<>("Título");
        colTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colTitulo.setMinWidth(150);

        // Columna 3: Estado con celdas personalizadas en formato "Badge" (etiquetas de colores)
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

        // Columna 4: Prioridad con celdas personalizadas en formato "Badge"
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

        // Vinculamos la SortedList a la tabla para permitir ordenación por columnas sin romper los filtros
        SortedList<Incidencia> sortedList = new SortedList<>(filteredData);
        sortedList.comparatorProperty().bind(tableView.comparatorProperty());
        tableView.setItems(sortedList);

        // Listener de selección en la tabla: carga automáticamente los datos de la fila seleccionada al formulario derecho
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

    /**
     * Construye la tarjeta derecha con el formulario de detalles, campos de edición y botones de acción (Guardar/Eliminar).
     */
    private VBox createDetailCard() {
        VBox card = new VBox(14);
        card.getStyleClass().add("card-container");

        detailTitleLabel = new Label("Detalle de Incidencia");
        detailTitleLabel.getStyleClass().add("card-title");

        // Campo de texto para el Título
        VBox titleBox = new VBox(6);
        Label titleLbl = new Label("Título");
        titleLbl.getStyleClass().add("form-label");
        titleField = new TextField();
        titleField.getStyleClass().add("custom-text-field");
        titleBox.getChildren().addAll(titleLbl, titleField);

        // Área de texto para la Descripción
        VBox descBox = new VBox(6);
        Label descLbl = new Label("Descripción");
        descLbl.getStyleClass().add("form-label");
        descArea = new TextArea();
        descArea.getStyleClass().add("custom-text-area");
        descArea.setWrapText(true);
        descArea.setPrefRowCount(4);
        descBox.getChildren().addAll(descLbl, descArea);

        // Contenedor horizontal para los ComboBox de Estado y Prioridad
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

        // Botones de acción inferiores (Eliminar y Guardar cambios)
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

    /**
     * Rellena los campos del formulario derecho con los datos de la incidencia seleccionada en la tabla.
     */
    private void populateForm(Incidencia inc) {
        detailTitleLabel.setText("Detalle de Incidencia #" + inc.getIdFormatted());
        titleField.setText(inc.getTitulo());
        descArea.setText(inc.getDescripcion());
        statusCombo.setValue(inc.getEstado());
        priorityCombo.setValue(inc.getPrioridad());
        deleteBtn.setDisable(false);
    }

    /**
     * Prepara el formulario para el modo de inserción de una nueva incidencia limpiando los campos.
     */
    private void enterNewMode() {
        isNewMode = true;
        selectedIncidencia = null;
        tableView.getSelectionModel().clearSelection();

        detailTitleLabel.setText("Nueva Incidencia");
        titleField.setText("");
        descArea.setText("");
        statusCombo.setValue("Abierta");
        priorityCombo.setValue("Media");
        deleteBtn.setDisable(true);
        titleField.requestFocus();
    }

    /**
     * Gestiona la lógica de guardado: crea una nueva incidencia si está en modo alta o actualiza la seleccionada si está en edición.
     */
    private void saveChanges() {
        String title = titleField.getText().trim();
        String desc = descArea.getText().trim();
        String status = statusCombo.getValue();
        String priority = priorityCombo.getValue();

        // Validación básica de campos obligatorios
        if (title.isEmpty()) {
            showAlert("Validación", "Por favor introduce un título para la incidencia.");
            return;
        }

        if (isNewMode) {
            int nextId = masterData.stream().mapToInt(Incidencia::getId).max().orElse(0) + 1;
            Incidencia newInc = new Incidencia(nextId, title, desc, status != null ? status : "Abierta", priority != null ? priority : "Media");
            masterData.add(newInc);
            tableView.getSelectionModel().select(newInc);
            statusMessageLabel.setText("✓ Incidencia #" + newInc.getIdFormatted() + " creada correctamente");
        } else if (selectedIncidencia != null) {
            selectedIncidencia.setTitulo(title);
            selectedIncidencia.setDescripcion(desc);
            if (status != null) selectedIncidencia.setEstado(status);
            if (priority != null) selectedIncidencia.setPrioridad(priority);
            tableView.refresh();
            statusMessageLabel.setText("✓ Cambios guardados para la incidencia #" + selectedIncidencia.getIdFormatted());
        }

        updateStats();
    }

    /**
     * Elimina la incidencia seleccionada actualmente de la lista maestra.
     */
    private void deleteSelected() {
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

    /**
     * Construye la barra inferior (Footer) con los contadores estadísticos en tiempo real.
     */
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

    /**
     * Método auxiliar para crear etiquetas descriptivas en las estadísticas del footer.
     */
    private Label createStatCaption(String text) {
        Label lbl = new Label(text);
        lbl.getStyleClass().add("stat-item");
        return lbl;
    }

    /**
     * Utiliza Streams de Java para contar y actualizar en tiempo real el número de incidencias según su estado.
     */
    private void updateStats() {
        long total = masterData.size();
        long abiertas = masterData.stream().filter(i -> "Abierta".equalsIgnoreCase(i.getEstado())).count();
        long enProceso = masterData.stream().filter(i -> "En proceso".equalsIgnoreCase(i.getEstado())).count();
        long resueltas = masterData.stream().filter(i -> "Resuelta".equalsIgnoreCase(i.getEstado())).count();

        if (totalCountLabel != null) totalCountLabel.setText(String.valueOf(total));
        if (abiertasCountLabel != null) abiertasCountLabel.setText(String.valueOf(abiertas));
        if (enProcesoCountLabel != null) enProcesoCountLabel.setText(String.valueOf(enProceso));
        if (resueltasCountLabel != null) resueltasCountLabel.setText(String.valueOf(resueltas));
    }

    /**
     * Muestra una ventana emergente de alerta (Alert) para notificar validaciones al usuario.
     */
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Método principal main que lanza la ejecución de la aplicación JavaFX.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
