package persistencia;

import modelo.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PersistenciaArchivo {
    private final Path archivo;

    public PersistenciaArchivo(Path archivo) {
        this.archivo = archivo;
    }

    public void guardar(Estudiante estudiante) throws IOException {
        Files.createDirectories(archivo.getParent());
        Files.writeString(archivo, JsonBasico.convertirAJson(convertirMapa(estudiante)), StandardCharsets.UTF_8);
    }

    public Estudiante cargar() throws IOException {
        if (!Files.exists(archivo)) {
            return null;
        }
        Object objeto = JsonBasico.parsear(Files.readString(archivo, StandardCharsets.UTF_8));
        return convertirEstudiante(castMapa(objeto));
    }

    private Map<String, Object> convertirMapa(Estudiante estudiante) {
        Map<String, Object> mapa = new LinkedHashMap<>();
        mapa.put("nombre", estudiante.getNombre());
        mapa.put("codigo", estudiante.getCodigo());
        List<Object> cursos = new ArrayList<>();
        for (Curso curso : estudiante.getCursos()) {
            Map<String, Object> cursoMap = new LinkedHashMap<>();
            cursoMap.put("id", curso.getId());
            cursoMap.put("nombre", curso.getNombre());
            cursoMap.put("docente", curso.getDocente());
            List<Object> actividades = new ArrayList<>();
            for (ActividadAcademica actividad : curso.getActividades()) {
                Map<String, Object> actMap = new LinkedHashMap<>();
                actMap.put("id", actividad.getId());
                actMap.put("tipo", actividad.getTipoActividad().name());
                actMap.put("titulo", actividad.getTitulo());
                actMap.put("descripcion", actividad.getDescripcion());
                actMap.put("fechaEntrega", actividad.tieneFecha() ? actividad.getFechaEntrega().toString() : null);
                actMap.put("prioridad", actividad.getPrioridad().name());
                actMap.put("estado", actividad.getEstado().name());
                actMap.put("fechaRegistro", actividad.getFechaRegistro().toString());
                actMap.put("cursoId", actividad.getCursoId());
                actividades.add(actMap);
            }
            cursoMap.put("actividades", actividades);
            cursos.add(cursoMap);
        }
        mapa.put("cursos", cursos);
        return mapa;
    }

    private Estudiante convertirEstudiante(Map<String, Object> mapa) {
        String nombre = String.valueOf(mapa.getOrDefault("nombre", "Estudiante local"));
        String codigo = String.valueOf(mapa.getOrDefault("codigo", "000000"));
        List<Curso> cursos = new ArrayList<>();
        for (Object cursoObj : castLista(mapa.get("cursos"))) {
            Map<String, Object> cursoMap = castMapa(cursoObj);
            List<ActividadAcademica> actividades = new ArrayList<>();
            for (Object actividadObj : castLista(cursoMap.get("actividades"))) {
                Map<String, Object> actMap = castMapa(actividadObj);
                actividades.add(crearActividad(actMap));
            }
            cursos.add(new Curso(
                    String.valueOf(cursoMap.get("id")),
                    String.valueOf(cursoMap.get("nombre")),
                    String.valueOf(cursoMap.getOrDefault("docente", "")),
                    actividades
            ));
        }
        return new Estudiante(nombre, codigo, cursos);
    }

    private ActividadAcademica crearActividad(Map<String, Object> mapa) {
        String id = String.valueOf(mapa.get("id"));
        String titulo = String.valueOf(mapa.get("titulo"));
        String descripcion = String.valueOf(mapa.getOrDefault("descripcion", ""));
        LocalDate fechaEntrega = mapa.get("fechaEntrega") == null ? null : LocalDate.parse(String.valueOf(mapa.get("fechaEntrega")));
        Prioridad prioridad = Prioridad.valueOf(String.valueOf(mapa.get("prioridad")));
        EstadoActividad estado = EstadoActividad.valueOf(String.valueOf(mapa.get("estado")));
        LocalDate fechaRegistro = LocalDate.parse(String.valueOf(mapa.get("fechaRegistro")));
        String cursoId = String.valueOf(mapa.get("cursoId"));
        TipoActividad tipo = TipoActividad.valueOf(String.valueOf(mapa.get("tipo")));

        if (tipo == TipoActividad.EVALUACION) {
            return new EvaluacionAcademica(id, titulo, descripcion, fechaEntrega, prioridad, estado, fechaRegistro, cursoId);
        }
        return new TareaAcademica(id, titulo, descripcion, fechaEntrega, prioridad, estado, fechaRegistro, cursoId);
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> castMapa(Object valor) {
        return (Map<String, Object>) valor;
    }

    @SuppressWarnings("unchecked")
    private List<Object> castLista(Object valor) {
        if (valor == null) {
            return List.of();
        }
        return (List<Object>) valor;
    }
}
