package modelo;

import java.time.LocalDate;

public abstract class ActividadAcademica {
    private String id;
    private String titulo;
    private String descripcion;
    private LocalDate fechaEntrega;
    private Prioridad prioridad;
    private EstadoActividad estado;
    private LocalDate fechaRegistro;
    private String cursoId;

    public ActividadAcademica(String id, String titulo, String descripcion, LocalDate fechaEntrega,
                              Prioridad prioridad, EstadoActividad estado, LocalDate fechaRegistro,
                              String cursoId) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fechaEntrega = fechaEntrega;
        this.prioridad = prioridad;
        this.estado = estado;
        this.fechaRegistro = fechaRegistro;
        this.cursoId = cursoId;
    }

    public String getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDate getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(LocalDate fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public Prioridad getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(Prioridad prioridad) {
        this.prioridad = prioridad;
    }

    public EstadoActividad getEstado() {
        return estado;
    }

    public void setEstado(EstadoActividad estado) {
        this.estado = estado;
    }

    public LocalDate getFechaRegistro() {
        return fechaRegistro;
    }

    public String getCursoId() {
        return cursoId;
    }

    public void setCursoId(String cursoId) {
        this.cursoId = cursoId;
    }

    public boolean estaPendiente() {
        return estado == EstadoActividad.INICIO;
    }

    public boolean tieneFecha() {
        return fechaEntrega != null;
    }

    public boolean estaVencida() {
        return tieneFecha()
                && estado != EstadoActividad.FINALIZADO
                && fechaEntrega.isBefore(LocalDate.now());
    }

    public abstract TipoActividad getTipoActividad();

    public abstract String getResumenVisual();
}
