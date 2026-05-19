package modelo;

import java.util.ArrayList;
import java.util.List;

public class Estudiante {
    private String nombre;
    private String codigo;
    private final List<Curso> cursos;

    public Estudiante(String nombre, String codigo) {
        this(nombre, codigo, new ArrayList<>());
    }

    public Estudiante(String nombre, String codigo, List<Curso> cursos) {
        this.nombre = nombre;
        this.codigo = codigo;
        this.cursos = new ArrayList<>(cursos);
    }

    public String getNombre() {
        return nombre;
    }

    public String getCodigo() {
        return codigo;
    }

    public List<Curso> getCursos() {
        return cursos;
    }
}
