package funcional;

import analisis.ResultadoEntropia;
import modelo.ActividadAcademica;
import modelo.EstadoActividad;
import modelo.Prioridad;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class EntropiaFuncional {
    private EntropiaFuncional() {
    }

    public record IndicadoresEntropia(
            int total,
            int inicio,
            int proceso,
            int vencidas,
            int altaPendiente,
            int completadas,
            int cercanas,
            int sinFecha,
            int maxConcentracion
    ) {
    }

    public static ResultadoEntropia evaluar(List<ActividadAcademica> actividades) {
        IndicadoresEntropia indicadores = medir(actividades, LocalDate.now());
        int puntaje = puntaje(indicadores);
        return interpretar(puntaje);
    }

    public static IndicadoresEntropia medir(List<ActividadAcademica> actividades, LocalDate hoy) {
        Map<LocalDate, Long> porFecha = actividades.stream()
                .filter(ActividadAcademica::tieneFecha)
                .collect(Collectors.groupingBy(ActividadAcademica::getFechaEntrega, Collectors.counting()));

        int total = actividades.size();
        int inicio = contar(actividades, actividad -> actividad.getEstado() == EstadoActividad.INICIO);
        int proceso = contar(actividades, actividad -> actividad.getEstado() == EstadoActividad.PROCESO);
        int vencidas = contar(actividades, ActividadAcademica::estaVencida);
        int altaPendiente = contar(actividades, actividad ->
                actividad.getPrioridad() == Prioridad.ALTA && actividad.getEstado() != EstadoActividad.FINALIZADO);
        int completadas = contar(actividades, actividad -> actividad.getEstado() == EstadoActividad.FINALIZADO);
        int cercanas = contar(actividades, actividad ->
                actividad.tieneFecha()
                        && !actividad.getFechaEntrega().isBefore(hoy)
                        && !actividad.getFechaEntrega().isAfter(hoy.plusDays(3)));
        int sinFecha = contar(actividades, actividad -> !actividad.tieneFecha());
        int maxConcentracion = porFecha.values().stream()
                .mapToInt(Long::intValue)
                .max()
                .orElse(0);

        return new IndicadoresEntropia(total, inicio, proceso, vencidas, altaPendiente, completadas,
                cercanas, sinFecha, maxConcentracion);
    }

    public static int puntaje(IndicadoresEntropia indicadores) {
        int puntajeBase = indicadores.inicio()
                + indicadores.proceso()
                + indicadores.vencidas() * 3
                + indicadores.altaPendiente() * 2;

        int penalizacionSinFecha = indicadores.sinFecha() >= 5 ? 1 : 0;
        int penalizacionCercanas = indicadores.cercanas() >= 4 ? 2 : 0;
        int penalizacionConcentracion = indicadores.maxConcentracion() >= 3 ? 2 : 0;
        int penalizacionAvance = indicadores.total() >= 4
                && indicadores.completadas() * 1.0 / indicadores.total() < 0.40 ? 2 : 0;

        return puntajeBase
                + penalizacionSinFecha
                + penalizacionCercanas
                + penalizacionConcentracion
                + penalizacionAvance;
    }

    public static ResultadoEntropia interpretar(int puntaje) {
        if (puntaje <= 4) {
            return new ResultadoEntropia("baja", puntaje, "La organizacion academica es adecuada y el desorden general es reducido.");
        }
        if (puntaje <= 10) {
            return new ResultadoEntropia("media", puntaje, "Existe acumulacion moderada de actividades y conviene reorganizar prioridades y tiempos.");
        }
        return new ResultadoEntropia("alta", puntaje, "Se detecta alto desorden academico, acumulacion de trabajo y fuerte incertidumbre en la planificacion.");
    }

    private static int contar(List<ActividadAcademica> actividades, java.util.function.Predicate<ActividadAcademica> criterio) {
        return (int) actividades.stream()
                .filter(criterio)
                .count();
    }
}
