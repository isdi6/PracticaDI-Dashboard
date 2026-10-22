package com.empresa.incidencias.model;

import javafx.beans.property.*;

public class Incidencia {
    private final IntegerProperty id;
    private final StringProperty titulo;
    private final StringProperty descripcion;
    private final StringProperty estado;
    private final StringProperty prioridad;

    public Incidencia(int id, String titulo, String descripcion, String estado, String prioridad) {
        this.id = new SimpleIntegerProperty(id);
        this.titulo = new SimpleStringProperty(titulo);
        this.descripcion = new SimpleStringProperty(descripcion);
        this.estado = new SimpleStringProperty(estado);
        this.prioridad = new SimpleStringProperty(prioridad);
    }

    public int getId() { return id.get(); }
    public void setId(int id) { this.id.set(id); }
    public IntegerProperty idProperty() { return id; }
    public String getIdFormatted() { return String.format("%02d", id.get()); }

    public String getTitulo() { return titulo.get(); }
    public void setTitulo(String titulo) { this.titulo.set(titulo); }
    public StringProperty tituloProperty() { return titulo; }

    public String getDescripcion() { return descripcion.get(); }
    public void setDescripcion(String descripcion) { this.descripcion.set(descripcion); }
    public StringProperty descripcionProperty() { return descripcion; }

    public String getEstado() { return estado.get(); }
    public void setEstado(String estado) { this.estado.set(estado); }
    public StringProperty estadoProperty() { return estado; }

    public String getPrioridad() { return prioridad.get(); }
    public void setPrioridad(String prioridad) { this.prioridad.set(prioridad); }
    public StringProperty prioridadProperty() { return prioridad; }
}
