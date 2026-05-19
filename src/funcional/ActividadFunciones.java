package funcional;

import modelo.ActividadAcademica;
import modelo.Curso;
import modelo.EstadoActividad;
import modelo.Prioridad;

import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public final class ActividadFunciones {
    private ActividadFunciones() {
    }

    public static List<ActividadAcademica> actividadesDe(List<Curso> cursos) {
        return cursos.stream()
                .flatMap(curso -> curso.getActividades().stream())
                .sorted(Comparator.comparing(ActividadAcademica::getFechaEntrega, Comparator.nullsLast(Comparator.naturalOrder()))
                        .thenComparing(ActividadAcademica::getTitulo))
                .toList();
    }

    public static List<ActividadAcademica> filtrar(List<ActividadAcademica> actividades, String cursoId, Prioridad prioridad) {
        Function<List<ActividadAcademica>, List<ActividadAcademica>> flujo =
                porCurso(cursoId).andThen(porPrioridad(prioridad));
        return flujo.apply(actividades);
    }

    public static Function<List<ActividadAcademica>, List<ActividadAcademica>> porCurso(String cursoId) {
        Predicate<ActividadAcademica> criterio = actividad ->
                cursoId == null || cursoId.isBlank() || actividad.getCursoId().equals(cursoId);
        return actividades -> actividades.stream()
                .filter(criterio)
                .toList();
    }

    public static Function<List<ActividadAcademica>, List<ActividadAcademica>> porPrioridad(Prioridad prioridad) {
        Predicate<ActividadAcademica> criterio = actividad ->
                prioridad == null || actividad.getPrioridad() == prioridad;
        return actividades -> actividades.stream()
                .filter(criterio)
                .toList();
    }

    public static Map<EstadoActividad, Long> contarPorEstado(List<ActividadAcademica> actividades) {
        Map<EstadoActividad, Long> conteo = actividades.stream()
                .collect(Collectors.groupingBy(ActividadAcademica::getEstado,
                        () -> new EnumMap<>(EstadoActividad.class),
                        Collectors.counting()));
        for (EstadoActividad estado : EstadoActividad.values()) {
            conteo.putIfAbsent(estado, 0L);
        }
        return Map.copyOf(conteo);
    }

    public static long contarVencidas(List<ActividadAcademica> actividades) {
        return actividades.stream()
                .filter(ActividadAcademica::estaVencida)
                .count();
    }
}
