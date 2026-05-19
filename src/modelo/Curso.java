package modelo;

import java.util.ArrayList;
import java.util.List;

public class Curso {
    private String id;
    private String nombre;
    private String docente;
    private final List<ActividadAcademica> actividades;

    public Curso(String id, String nombre, String docente) {
        this(id, nombre, docente, new ArrayList<>());
    }

    public Curso(String id, String nombre, String docente, List<ActividadAcademica> actividades) {
        this.id = id;
        this.nombre = nombre;
        this.docente = docente;
        this.actividades = new ArrayList<>(actividades);
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDocente() {
        return docente;
    }

    public void setDocente(String docente) {
        this.docente = docente;
    }

    public List<ActividadAcademica> getActividades() {
        return actividades;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
