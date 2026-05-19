package servicio;

import funcional.ActividadFunciones;
import modelo.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class ServicioActividades {
    private final ServicioCursos servicioCursos;

    public ServicioActividades(ServicioCursos servicioCursos) {
        this.servicioCursos = servicioCursos;
    }

    public ActividadAcademica registrarActividad(TipoActividad tipo, String cursoId, String titulo, String descripcion,
                                                 LocalDate fechaEntrega, Prioridad prioridad) {
        validarDatos(cursoId, titulo, fechaEntrega, prioridad);
        ActividadAcademica actividad = crearActividad(tipo, titulo, descripcion, fechaEntrega, prioridad, cursoId);
        servicioCursos.buscarCurso(cursoId).getActividades().add(actividad);
        return actividad;
    }

    public void editarActividad(String actividadId, TipoActividad tipo, String cursoId, String titulo, String descripcion,
                                LocalDate fechaEntrega, Prioridad prioridad, EstadoActividad estado) {
        validarDatos(cursoId, titulo, fechaEntrega, prioridad);
        ActividadAcademica actual = buscarActividad(actividadId);
        Curso cursoOriginal = buscarCursoPorActividad(actividadId);

        if (!actual.getTipoActividad().equals(tipo)) {
            ActividadAcademica nueva = crearActividad(tipo, titulo, descripcion, fechaEntrega, prioridad, cursoId);
            nueva.setEstado(estado);
            cursoOriginal.getActividades().remove(actual);
            servicioCursos.buscarCurso(cursoId).getActividades().add(nueva);
            return;
        }

        if (!cursoOriginal.getId().equals(cursoId)) {
            cursoOriginal.getActividades().remove(actual);
            servicioCursos.buscarCurso(cursoId).getActividades().add(actual);
            actual.setCursoId(cursoId);
        }

        actual.setTitulo(titulo.trim());
        actual.setDescripcion(descripcion == null ? "" : descripcion.trim());
        actual.setFechaEntrega(fechaEntrega);
        actual.setPrioridad(prioridad);
        actual.setEstado(estado);
    }

    public void eliminarActividad(String actividadId) {
        Curso curso = buscarCursoPorActividad(actividadId);
        curso.getActividades().removeIf(a -> a.getId().equals(actividadId));
    }

    public void marcarComoCompletada(String actividadId) {
        buscarActividad(actividadId).setEstado(EstadoActividad.FINALIZADO);
    }

    public void cambiarEstado(String actividadId, EstadoActividad estadoNuevo) {
        buscarActividad(actividadId).setEstado(estadoNuevo);
    }

    public List<ActividadAcademica> listarActividades() {
        return ActividadFunciones.actividadesDe(servicioCursos.listarCursos());
    }

    public List<ActividadAcademica> filtrar(String cursoId, Prioridad prioridad) {
        return ActividadFunciones.filtrar(listarActividades(), cursoId, prioridad);
    }

    public ActividadAcademica buscarActividad(String actividadId) {
        return listarActividades().stream()
                .filter(a -> a.getId().equals(actividadId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Actividad no encontrada."));
    }

    public Curso buscarCursoPorActividad(String actividadId) {
        return servicioCursos.listarCursos().stream()
                .filter(c -> c.getActividades().stream().anyMatch(a -> a.getId().equals(actividadId)))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Curso de la actividad no encontrado."));
    }

    private ActividadAcademica crearActividad(TipoActividad tipo, String titulo, String descripcion,
                                              LocalDate fechaEntrega, Prioridad prioridad, String cursoId) {
        String id = UUID.randomUUID().toString();
        if (tipo == TipoActividad.EVALUACION) {
            return new EvaluacionAcademica(id, titulo.trim(), descripcion == null ? "" : descripcion.trim(),
                    fechaEntrega, prioridad, EstadoActividad.INICIO, LocalDate.now(), cursoId);
        }
        return new TareaAcademica(id, titulo.trim(), descripcion == null ? "" : descripcion.trim(),
                fechaEntrega, prioridad, EstadoActividad.INICIO, LocalDate.now(), cursoId);
    }

    private void validarDatos(String cursoId, String titulo, LocalDate fechaEntrega, Prioridad prioridad) {
        if (cursoId == null || cursoId.isBlank()) {
            throw new IllegalArgumentException("Debe seleccionar un curso.");
        }
        if (titulo == null || titulo.isBlank()) {
            throw new IllegalArgumentException("El titulo es obligatorio.");
        }
        if (prioridad == null) {
            throw new IllegalArgumentException("La prioridad es obligatoria.");
        }
    }
}
