package vista;

import modelo.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Date;
import java.util.List;

public class DialogoActividad extends JDialog {
    private final JTextField campoTitulo;
    private final JTextArea campoDescripcion;
    private final JComboBox<CursoItem> comboCurso;
    private final JComboBox<TipoActividad> comboTipo;
    private final JComboBox<Prioridad> comboPrioridad;
    private final JComboBox<EstadoActividad> comboEstado;
    private final JSpinner spinnerFecha;
    private final JCheckBox checkTieneFecha;
    private boolean confirmado;

    public DialogoActividad(Window owner, String titulo, List<Curso> cursos, ActividadAcademica actividad) {
        super(owner, titulo, ModalityType.APPLICATION_MODAL);
        campoTitulo = new JTextField(28);
        campoDescripcion = new JTextArea(4, 28);
        comboCurso = new JComboBox<>();
        comboTipo = new JComboBox<>(TipoActividad.values());
        comboPrioridad = new JComboBox<>(Prioridad.values());
        comboEstado = new JComboBox<>(EstadoActividad.values());
        spinnerFecha = new JSpinner(new SpinnerDateModel());
        spinnerFecha.setEditor(new JSpinner.DateEditor(spinnerFecha, "yyyy-MM-dd"));
        checkTieneFecha = new JCheckBox("Asignar fecha");
        checkTieneFecha.addActionListener(e -> spinnerFecha.setEnabled(checkTieneFecha.isSelected()));

        for (Curso curso : cursos) {
            comboCurso.addItem(new CursoItem(curso.getId(), curso.getNombre()));
        }

        if (actividad != null) {
            campoTitulo.setText(actividad.getTitulo());
            campoDescripcion.setText(actividad.getDescripcion());
            seleccionarCurso(actividad.getCursoId());
            comboTipo.setSelectedItem(actividad.getTipoActividad());
            comboPrioridad.setSelectedItem(actividad.getPrioridad());
            comboEstado.setSelectedItem(actividad.getEstado());
            checkTieneFecha.setSelected(actividad.tieneFecha());
            if (actividad.tieneFecha()) {
                spinnerFecha.setValue(java.sql.Date.valueOf(actividad.getFechaEntrega()));
            } else {
                spinnerFecha.setValue(new Date());
            }
        } else {
            comboEstado.setSelectedItem(EstadoActividad.INICIO);
            checkTieneFecha.setSelected(true);
            spinnerFecha.setValue(new Date());
        }
        spinnerFecha.setEnabled(checkTieneFecha.isSelected());

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

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Tipo:"), gbc);
        gbc.gridx = 1;
        panel.add(comboTipo, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Titulo:"), gbc);
        gbc.gridx = 1;
        panel.add(campoTitulo, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Descripcion:"), gbc);
        gbc.gridx = 1;
        panel.add(new JScrollPane(campoDescripcion), gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Curso:"), gbc);
        gbc.gridx = 1;
        panel.add(comboCurso, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(checkTieneFecha, gbc);
        gbc.gridx = 1;
        panel.add(spinnerFecha, gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        panel.add(new JLabel("Prioridad:"), gbc);
        gbc.gridx = 1;
        panel.add(comboPrioridad, gbc);

        gbc.gridx = 0; gbc.gridy = 6;
        panel.add(new JLabel("Estado:"), gbc);
        gbc.gridx = 1;
        panel.add(comboEstado, gbc);
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

    private void seleccionarCurso(String cursoId) {
        for (int i = 0; i < comboCurso.getItemCount(); i++) {
            CursoItem item = comboCurso.getItemAt(i);
            if (item.id().equals(cursoId)) {
                comboCurso.setSelectedIndex(i);
                break;
            }
        }
    }

    public boolean isConfirmado() {
        return confirmado;
    }

    public TipoActividad getTipoActividad() {
        return (TipoActividad) comboTipo.getSelectedItem();
    }

    public String getTituloActividad() {
        return campoTitulo.getText();
    }

    public String getDescripcion() {
        return campoDescripcion.getText();
    }

    public String getCursoId() {
        CursoItem item = (CursoItem) comboCurso.getSelectedItem();
        return item == null ? "" : item.id();
    }

    public java.time.LocalDate getFecha() {
        if (!checkTieneFecha.isSelected()) {
            return null;
        }
        java.util.Date fecha = (java.util.Date) spinnerFecha.getValue();
        return new java.sql.Date(fecha.getTime()).toLocalDate();
    }

    public Prioridad getPrioridad() {
        return (Prioridad) comboPrioridad.getSelectedItem();
    }

    public EstadoActividad getEstado() {
        return (EstadoActividad) comboEstado.getSelectedItem();
    }

    private record CursoItem(String id, String nombre) {
        @Override
        public String toString() {
            return nombre;
        }
    }
}
