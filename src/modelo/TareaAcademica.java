package modelo;

import java.time.LocalDate;

public class TareaAcademica extends ActividadAcademica {
    public TareaAcademica(String id, String titulo, String descripcion, LocalDate fechaEntrega,
                          Prioridad prioridad, EstadoActividad estado, LocalDate fechaRegistro,
                          String cursoId) {
        super(id, titulo, descripcion, fechaEntrega, prioridad, estado, fechaRegistro, cursoId);
    }

    @Override
    public TipoActividad getTipoActividad() {
        return TipoActividad.TAREA;
    }

    @Override
    public String getResumenVisual() {
        return "Actividad pensada para practica, avance o seguimiento";
    }
}
