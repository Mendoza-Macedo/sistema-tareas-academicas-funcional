package vista;

import modelo.Curso;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class DialogoCurso extends JDialog {
    private final JTextField campoNombre;
    private final JTextField campoDocente;
    private boolean confirmado;

    public DialogoCurso(Window owner, String titulo, Curso curso) {
        super(owner, titulo, ModalityType.APPLICATION_MODAL);
        campoNombre = new JTextField(24);
        campoDocente = new JTextField(24);

        if (curso != null) {
            campoNombre.setText(curso.getNombre());
            campoDocente.setText(curso.getDocente());
        }

        setLayout(new BorderLayout(10, 10));
        add(crearFormulario(), BorderLayout.CENTER);
        add(crearBotones(), BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(owner);
    }

    private JPanel crearFormulario() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(14, 14, 10, 14));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Nombre del curso:"), gbc);
        gbc.gridx = 1;
        panel.add(campoNombre, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Docente:"), gbc);
        gbc.gridx = 1;
        panel.add(campoDocente, gbc);

        return panel;
    }

    private JPanel crearBotones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton cancelar = new JButton("Cancelar");
        JButton guardar = new JButton("Guardar");
        cancelar.addActionListener(e -> setVisible(false));
        guardar.addActionListener(e -> {
            confirmado = true;
            setVisible(false);
        });
        panel.add(cancelar);
        panel.add(guardar);
        return panel;
    }

    public boolean isConfirmado() {
        return confirmado;
    }

    public String getNombreCurso() {
        return campoNombre.getText();
    }

    public String getDocente() {
        return campoDocente.getText();
    }
}
