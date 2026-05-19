package analisis;

import funcional.ActividadFunciones;
import funcional.EntropiaFuncional;
import modelo.ActividadAcademica;
import modelo.Curso;

import java.util.List;

public class ServicioEntropia {
    public ResultadoEntropia calcular(List<Curso> cursos) {
        List<ActividadAcademica> actividades = ActividadFunciones.actividadesDe(cursos);
        return EntropiaFuncional.evaluar(actividades);
    }
}
