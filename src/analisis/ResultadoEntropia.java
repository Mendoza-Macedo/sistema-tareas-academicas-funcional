package analisis;

public class ResultadoEntropia {
    private final String nivel;
    private final int puntaje;
    private final String mensaje;

    public ResultadoEntropia(String nivel, int puntaje, String mensaje) {
        this.nivel = nivel;
        this.puntaje = puntaje;
        this.mensaje = mensaje;
    }

    public String getNivel() {
        return nivel;
    }

    public int getPuntaje() {
        return puntaje;
    }

    public String getMensaje() {
        return mensaje;
    }
}
