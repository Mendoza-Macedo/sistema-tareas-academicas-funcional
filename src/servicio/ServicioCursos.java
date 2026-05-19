package servicio;

import modelo.Curso;
import modelo.Estudiante;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public class ServicioCursos {
    private final Estudiante estudiante;

    public ServicioCursos(Estudiante estudiante) {
        this.estudiante = estudiante;
    }

    public Curso registrarCurso(String nombre, String docente) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del curso es obligatorio.");
        }
        Curso curso = new Curso(UUID.randomUUID().toString(), nombre.trim(), docente == null ? "" : docente.trim());
        estudiante.getCursos().add(curso);
        return curso;
    }

    public void editarCurso(String id, String nombre, String docente) {
        Curso curso = buscarCurso(id);
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del curso es obligatorio.");
        }
        curso.setNombre(nombre.trim());
        curso.setDocente(docente == null ? "" : docente.trim());
    }

    public void eliminarCurso(String id, boolean forzar) {
        Curso curso = buscarCurso(id);
        if (!curso.getActividades().isEmpty() && !forzar) {
            throw new IllegalArgumentException("El curso tiene actividades asociadas.");
        }
        estudiante.getCursos().remove(curso);
    }

    public List<Curso> listarCursos() {
        List<Curso> cursos = new ArrayList<>(estudiante.getCursos());
        cursos.sort(Comparator.comparing(Curso::getNombre, String.CASE_INSENSITIVE_ORDER));
        return cursos;
    }

    public Curso buscarCurso(String id) {
        return estudiante.getCursos().stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Curso no encontrado."));
    }
}
