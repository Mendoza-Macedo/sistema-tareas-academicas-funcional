package servicio;

import analisis.ResultadoEntropia;
import analisis.ServicioEntropia;
import modelo.*;
import persistencia.PersistenciaArchivo;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class SistemaAcademico {
    private final PersistenciaArchivo persistenciaArchivo;
    private final ServicioEntropia servicioEntropia;
    private Estudiante estudiante;
    private ServicioCursos servicioCursos;
    private ServicioActividades servicioActividades;

    public SistemaAcademico(Path rutaArchivo) {
        this.persistenciaArchivo = new PersistenciaArchivo(rutaArchivo);
        this.servicioEntropia = new ServicioEntropia();
        this.estudiante = new Estudiante("Estudiante local", "000000");
        reiniciarServicios();
    }

    public String cargar() {
        try {
            Estudiante cargado = persistenciaArchivo.cargar();
            if (cargado != null) {
                estudiante = cargado;
                reiniciarServicios();
            }
            guardar();
            return null;
        } catch (Exception ex) {
            estudiante = new Estudiante("Estudiante local", "000000");
            reiniciarServicios();
            return "No se pudo cargar el archivo de datos. Se iniciara en blanco.\nDetalle: " + ex.getMessage();
        }
    }

    public void guardar() {
        try {
            persistenciaArchivo.guardar(estudiante);
        } catch (IOException ex) {
            throw new IllegalStateException("No se pudo guardar la informacion.");
        }
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public List<Curso> listarCursos() {
        return servicioCursos.listarCursos();
    }

    public Curso registrarCurso(String nombre, String docente) {
        Curso curso = servicioCursos.registrarCurso(nombre, docente);
        guardar();
        return curso;
    }

    public void editarCurso(String id, String nombre, String docente) {
        servicioCursos.editarCurso(id, nombre, docente);
        guardar();
    }

    public void eliminarCurso(String id, boolean forzar) {
        servicioCursos.eliminarCurso(id, forzar);
        guardar();
    }

    public ActividadAcademica registrarActividad(TipoActividad tipo, String cursoId, String titulo, String descripcion,
                                                 java.time.LocalDate fechaEntrega, Prioridad prioridad) {
        ActividadAcademica actividad = servicioActividades.registrarActividad(tipo, cursoId, titulo, descripcion, fechaEntrega, prioridad);
        guardar();
        return actividad;
    }

    public void editarActividad(String id, TipoActividad tipo, String cursoId, String titulo, String descripcion,
                                java.time.LocalDate fechaEntrega, Prioridad prioridad, EstadoActividad estado) {
        servicioActividades.editarActividad(id, tipo, cursoId, titulo, descripcion, fechaEntrega, prioridad, estado);
        guardar();
    }

    public void eliminarActividad(String id) {
        servicioActividades.eliminarActividad(id);
        guardar();
    }

    public void marcarActividadCompletada(String id) {
        servicioActividades.marcarComoCompletada(id);
        guardar();
    }

    public void cambiarEstadoActividad(String id, EstadoActividad estado) {
        servicioActividades.cambiarEstado(id, estado);
        guardar();
    }

    public List<ActividadAcademica> filtrarActividades(String cursoId, Prioridad prioridad) {
        return servicioActividades.filtrar(cursoId, prioridad);
    }

    public ActividadAcademica buscarActividad(String id) {
        return servicioActividades.buscarActividad(id);
    }

    public ResultadoEntropia obtenerEntropia() {
        return servicioEntropia.calcular(listarCursos());
    }

    public ResumenSistema obtenerResumen() {
        List<ActividadAcademica> actividades = servicioActividades.listarActividades();
        int total = actividades.size();
        int inicio = (int) actividades.stream().filter(a -> a.getEstado() == EstadoActividad.INICIO).count();
        int proceso = (int) actividades.stream().filter(a -> a.getEstado() == EstadoActividad.PROCESO).count();
        int vencidas = (int) actividades.stream().filter(ActividadAcademica::estaVencida).count();
        int finalizadas = (int) actividades.stream().filter(a -> a.getEstado() == EstadoActividad.FINALIZADO).count();
        return new ResumenSistema(total, inicio, proceso, vencidas, finalizadas, obtenerEntropia());
    }

    public void pruebaSinInterfaz() {
        cargar();
        ResultadoEntropia resultado = obtenerEntropia();
        System.out.println("Prueba sin interfaz completada");
        System.out.println("Cursos: " + listarCursos().size());
        System.out.println("Entropia: " + resultado.getNivel() + " (" + resultado.getPuntaje() + ")");
    }

    private void reiniciarServicios() {
        servicioCursos = new ServicioCursos(estudiante);
        servicioActividades = new ServicioActividades(servicioCursos);
    }

    public static class ResumenSistema {
        private final int total;
        private final int inicio;
        private final int proceso;
        private final int vencidas;
        private final int finalizadas;
        private final ResultadoEntropia entropia;

        public ResumenSistema(int total, int inicio, int proceso, int vencidas, int finalizadas, ResultadoEntropia entropia) {
            this.total = total;
            this.inicio = inicio;
            this.proceso = proceso;
            this.vencidas = vencidas;
            this.finalizadas = finalizadas;
            this.entropia = entropia;
        }

        public int getTotal() {
            return total;
        }

        public int getInicio() {
            return inicio;
        }

        public int getProceso() {
            return proceso;
        }

        public int getVencidas() {
            return vencidas;
        }

        public int getFinalizadas() {
            return finalizadas;
        }

        public ResultadoEntropia getEntropia() {
            return entropia;
        }
    }
}
