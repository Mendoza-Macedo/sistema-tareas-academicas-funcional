package modelo;

import java.time.LocalDate;

public class EvaluacionAcademica extends ActividadAcademica {
    public EvaluacionAcademica(String id, String titulo, String descripcion, LocalDate fechaEntrega,
                               Prioridad prioridad, EstadoActividad estado, LocalDate fechaRegistro,
                               String cursoId) {
        super(id, titulo, descripcion, fechaEntrega, prioridad, estado, fechaRegistro, cursoId);
    }

    @Override
    public TipoActividad getTipoActividad() {
        return TipoActividad.EVALUACION;
    }

    @Override
    public String getResumenVisual() {
        return "Actividad de entrega, evaluacion o control importante";
    }
}
