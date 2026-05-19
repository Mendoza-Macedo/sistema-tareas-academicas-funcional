package aplicacion;

import servicio.SistemaAcademico;
import vista.VentanaPrincipal;

import javax.swing.*;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {
        SistemaAcademico sistema = new SistemaAcademico(Path.of("data", "datos_academicos.json"));
        if (args.length > 0 && "--prueba".equals(args[0])) {
            sistema.pruebaSinInterfaz();
            return;
        }

        SwingUtilities.invokeLater(() -> {
            String advertencia = sistema.cargar();
            VentanaPrincipal ventana = new VentanaPrincipal(sistema);
            ventana.setLocationRelativeTo(null);
            ventana.setVisible(true);
            if (advertencia != null) {
                ventana.mostrarAdvertencia(advertencia);
            }
        });
    }
}
