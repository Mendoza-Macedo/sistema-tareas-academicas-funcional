package prueba;

import analisis.ResultadoEntropia;
import funcional.ActividadFunciones;
import funcional.EntropiaFuncional;
import modelo.*;
import servicio.SistemaAcademico;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;

public class PruebaBasica {
    public static void main(String[] args) throws Exception {
        Path carpeta = Files.createTempDirectory("sistema-academico");
        Path archivo = carpeta.resolve("datos.json");
        SistemaAcademico sistema = new SistemaAcademico(archivo);

        Curso curso = sistema.registrarCurso("Lenguajes de Programacion", "Juarez");
        sistema.registrarActividad(TipoActividad.TAREA, curso.getId(), "Pseudocodigo", "Terminar el pseudocodigo", LocalDate.now().plusDays(1), Prioridad.MEDIA);
        sistema.registrarActividad(TipoActividad.EVALUACION, curso.getId(), "Avance final", "Preparar entrega", null, Prioridad.ALTA);

        if (sistema.listarCursos().size() != 1) {
            throw new IllegalStateException("Se esperaba 1 curso");
        }
        if (sistema.filtrarActividades("", null).size() != 2) {
            throw new IllegalStateException("Se esperaban 2 actividades");
        }
        if (ActividadFunciones.filtrar(sistema.filtrarActividades("", null), curso.getId(), Prioridad.MEDIA).size() != 1) {
            throw new IllegalStateException("El filtro funcional no produjo el resultado esperado");
        }
        ResultadoEntropia entropia = EntropiaFuncional.evaluar(sistema.filtrarActividades("", null));
        if (entropia.getPuntaje() <= 0) {
            throw new IllegalStateException("La entropia funcional debe producir un puntaje positivo");
        }

        sistema.cargar();
        System.out.println("Prueba basica OK");
    }
}
