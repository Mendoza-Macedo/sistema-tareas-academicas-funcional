package vista;

import analisis.ResultadoEntropia;
import modelo.*;
import servicio.SistemaAcademico;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class VentanaPrincipal extends JFrame {
    private final SistemaAcademico sistema;
    private final JComboBox<ItemCurso> comboCurso;
    private final JComboBox<String> comboPrioridad;
    private final JPanel panelInicio;
    private final JPanel panelProceso;
    private final JPanel panelFinalizado;
    private final JLabel lblTotal;
    private final JLabel lblInicio;
    private final JLabel lblProceso;
    private final JLabel lblVencidas;
    private final JLabel lblFinalizadas;
    private final JLabel lblEntropia;
    private final JLabel lblPuntaje;
    private final JTextArea areaMensaje;
    private String actividadSeleccionadaId;

    public VentanaPrincipal(SistemaAcademico sistema) {
        super("Sistema de Gestion de Tareas Academicas");
        this.sistema = sistema;
        this.comboCurso = new JComboBox<>();
        this.comboPrioridad = new JComboBox<>(new String[]{"Todas", "BAJA", "MEDIA", "ALTA"});
        this.panelInicio = crearPanelColumna();
        this.panelProceso = crearPanelColumna();
        this.panelFinalizado = crearPanelColumna();
        this.lblTotal = new JLabel("0");
        this.lblInicio = new JLabel("0");
        this.lblProceso = new JLabel("0");
        this.lblVencidas = new JLabel("0");
        this.lblFinalizadas = new JLabel("0");
        this.lblEntropia = new JLabel("-");
        this.lblPuntaje = new JLabel("0");
        this.areaMensaje = new JTextArea(4, 18);

        configurarVentana();
        refrescarTodo();
    }

    private void configurarVentana() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1320, 760));
        getContentPane().setBackground(new Color(241, 243, 248));
        setLayout(new BorderLayout(12, 12));
        add(crearCabecera(), BorderLayout.NORTH);
        add(crearCentro(), BorderLayout.CENTER);
        add(crearPanelResumen(), BorderLayout.EAST);
    }

    private JComponent crearCabecera() {
        JPanel cabecera = new JPanel(new BorderLayout(10, 10));
        cabecera.setBackground(new Color(53, 84, 125));
        cabecera.setBorder(new EmptyBorder(16, 18, 16, 18));

        JPanel izquierda = new JPanel();
        izquierda.setOpaque(false);
        izquierda.setLayout(new BoxLayout(izquierda, BoxLayout.Y_AXIS));
        JLabel titulo = new JLabel("Sistema de tareas academicas");
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 26));
        izquierda.add(titulo);

        JPanel derecha = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        derecha.setOpaque(false);
        derecha.add(crearBoton("Cursos", e -> mostrarMenuCursos((Component) e.getSource())));
        derecha.add(crearBoton("Acciones", e -> mostrarMenuAcciones((Component) e.getSource())));

        JPanel filtros = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 6));
        filtros.setOpaque(false);
        estilizarCombo(comboCurso, 180);
        estilizarCombo(comboPrioridad, 130);
        filtros.add(crearTextoBlanco("Curso:"));
        filtros.add(comboCurso);
        filtros.add(crearTextoBlanco("Prioridad:"));
        filtros.add(comboPrioridad);

        JButton limpiar = new JButton("Limpiar filtros");
        limpiar.addActionListener(e -> {
            comboCurso.setSelectedIndex(0);
            comboPrioridad.setSelectedIndex(0);
            refrescarTablero();
        });
        filtros.add(limpiar);

        comboCurso.addActionListener(e -> refrescarTablero());
        comboPrioridad.addActionListener(e -> refrescarTablero());

        JPanel contenedor = new JPanel(new BorderLayout(0, 10));
        contenedor.setOpaque(false);
        contenedor.add(izquierda, BorderLayout.WEST);
        contenedor.add(derecha, BorderLayout.EAST);
        contenedor.add(filtros, BorderLayout.SOUTH);
        cabecera.add(contenedor, BorderLayout.CENTER);
        return cabecera;
    }

    private JPanel crearCentro() {
        JPanel tablero = new JPanel(new GridLayout(1, 3, 14, 14));
        tablero.setBackground(new Color(221, 210, 189));
        tablero.setBorder(new EmptyBorder(16, 16, 16, 16));

        tablero.add(crearColumna("Inicio", "Actividades por iniciar", panelInicio, new Color(255, 249, 196)));
        tablero.add(crearColumna("Proceso", "Actividades en avance", panelProceso, new Color(222, 236, 255)));
        tablero.add(crearColumna("Finalizado", "Actividades cerradas", panelFinalizado, new Color(219, 245, 222)));

        JScrollPane scroll = new JScrollPane(tablero);
        scroll.setBorder(new EmptyBorder(0, 12, 12, 0));
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        return tablero;
    }

    private JPanel crearColumna(String titulo, String subtitulo, JPanel panelInterno, Color colorEncabezado) {
        JPanel columna = new JPanel(new BorderLayout(8, 8));
        columna.setBackground(new Color(251, 248, 242));
        columna.setBorder(BorderFactory.createLineBorder(new Color(176, 163, 137)));

        JPanel encabezado = new JPanel();
        encabezado.setLayout(new BoxLayout(encabezado, BoxLayout.Y_AXIS));
        encabezado.setBackground(colorEncabezado);
        encabezado.setBorder(new EmptyBorder(10, 12, 10, 12));
        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 18));
        JLabel lblSub = new JLabel(subtitulo);
        lblSub.setFont(new Font("SansSerif", Font.PLAIN, 12));
        encabezado.add(lblTitulo);
        encabezado.add(Box.createVerticalStrut(2));
        encabezado.add(lblSub);

        JScrollPane scroll = new JScrollPane(panelInterno);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(new Color(251, 248, 242));

        columna.add(encabezado, BorderLayout.NORTH);
        columna.add(scroll, BorderLayout.CENTER);
        return columna;
    }

    private JPanel crearPanelColumna() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(251, 248, 242));
        panel.setBorder(new EmptyBorder(6, 6, 6, 6));
        return panel;
    }

    private JComponent crearPanelResumen() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(241, 243, 248));
        panel.setBorder(new EmptyBorder(0, 0, 12, 12));
        panel.setPreferredSize(new Dimension(300, 0));

        panel.add(crearTarjetaResumen("Total", lblTotal, new Color(238, 243, 255)));
        panel.add(Box.createVerticalStrut(10));
        panel.add(crearTarjetaResumen("Inicio", lblInicio, new Color(255, 249, 213)));
        panel.add(Box.createVerticalStrut(10));
        panel.add(crearTarjetaResumen("Proceso", lblProceso, new Color(223, 236, 255)));
        panel.add(Box.createVerticalStrut(10));
        panel.add(crearTarjetaResumen("Vencidas", lblVencidas, new Color(255, 227, 227)));
        panel.add(Box.createVerticalStrut(10));
        panel.add(crearTarjetaResumen("Finalizadas", lblFinalizadas, new Color(222, 245, 223)));
        panel.add(Box.createVerticalStrut(12));

        JPanel entropia = new JPanel(new BorderLayout(10, 10));
        entropia.setBackground(Color.WHITE);
        entropia.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(214, 220, 233)),
                new EmptyBorder(12, 12, 12, 12)
        ));

        JPanel top = new JPanel(new GridLayout(2, 2, 8, 8));
        top.setOpaque(false);
        top.add(new JLabel("Nivel:"));
        top.add(lblEntropia);
        top.add(new JLabel("Puntaje:"));
        top.add(lblPuntaje);

        areaMensaje.setLineWrap(true);
        areaMensaje.setWrapStyleWord(true);
        areaMensaje.setEditable(false);
        areaMensaje.setOpaque(false);
        areaMensaje.setFocusable(false);

        entropia.add(top, BorderLayout.NORTH);
        entropia.add(areaMensaje, BorderLayout.CENTER);
        panel.add(entropia);
        panel.add(Box.createVerticalGlue());
        return panel;
    }

    private JPanel crearTarjetaResumen(String titulo, JLabel valor, Color fondo) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(fondo);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(214, 220, 233)),
                new EmptyBorder(12, 14, 12, 14)
        ));
        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 14));
        valor.setFont(new Font("SansSerif", Font.BOLD, 26));
        valor.setHorizontalAlignment(SwingConstants.CENTER);
        card.add(lblTitulo, BorderLayout.NORTH);
        card.add(valor, BorderLayout.CENTER);
        return card;
    }

    private void mostrarMenuCursos(Component origen) {
        JPopupMenu menu = new JPopupMenu();
        JMenuItem nuevo = new JMenuItem("Nuevo curso");
        JMenuItem editar = new JMenuItem("Editar curso");
        JMenuItem eliminar = new JMenuItem("Eliminar curso");

        nuevo.addActionListener(e -> crearCurso());
        editar.addActionListener(e -> editarCurso());
        eliminar.addActionListener(e -> eliminarCurso());

        menu.add(nuevo);
        menu.add(editar);
        menu.add(eliminar);
        menu.show(origen, 0, origen.getHeight());
    }

    private void mostrarMenuAcciones(Component origen) {
        JPopupMenu menu = new JPopupMenu();
        JMenuItem nueva = new JMenuItem("Nueva actividad");
        JMenuItem editar = new JMenuItem("Editar actividad");
        JMenuItem eliminar = new JMenuItem("Eliminar actividad");
        JMenuItem proceso = new JMenuItem("Pasar a proceso");
        JMenuItem finalizar = new JMenuItem("Finalizar actividad");

        nueva.addActionListener(e -> crearActividad());
        editar.addActionListener(e -> editarActividad());
        eliminar.addActionListener(e -> eliminarActividad());
        proceso.addActionListener(e -> pasarAProceso());
        finalizar.addActionListener(e -> completarActividad());

        menu.add(nueva);
        menu.addSeparator();
        menu.add(editar);
        menu.add(eliminar);
        menu.add(proceso);
        menu.add(finalizar);
        menu.show(origen, 0, origen.getHeight());
    }

    private void crearCurso() {
        DialogoCurso dialogo = new DialogoCurso(this, "Nuevo curso", null);
        dialogo.setVisible(true);
        if (!dialogo.isConfirmado()) {
            return;
        }
        ejecutarSeguro(() -> {
            sistema.registrarCurso(dialogo.getNombreCurso(), dialogo.getDocente());
            refrescarTodo();
        });
    }

    private void editarCurso() {
        Curso curso = getCursoActivo();
        if (curso == null) {
            mostrarInfo("Selecciona primero un curso en el filtro.");
            return;
        }
        DialogoCurso dialogo = new DialogoCurso(this, "Editar curso", curso);
        dialogo.setVisible(true);
        if (!dialogo.isConfirmado()) {
            return;
        }
        ejecutarSeguro(() -> {
            sistema.editarCurso(curso.getId(), dialogo.getNombreCurso(), dialogo.getDocente());
            refrescarTodo();
        });
    }

    private void eliminarCurso() {
        Curso curso = getCursoActivo();
        if (curso == null) {
            mostrarInfo("Selecciona primero un curso en el filtro.");
            return;
        }
        boolean confirmar = JOptionPane.showConfirmDialog(
                this,
                !curso.getActividades().isEmpty()
                        ? "El curso tiene actividades asociadas. Si continua, se eliminaran. Desea seguir?"
                        : "Desea eliminar el curso seleccionado?",
                "Confirmar",
                JOptionPane.YES_NO_OPTION
        ) == JOptionPane.YES_OPTION;
        if (!confirmar) {
            return;
        }
        ejecutarSeguro(() -> {
            sistema.eliminarCurso(curso.getId(), true);
            actividadSeleccionadaId = null;
            refrescarTodo();
        });
    }

    private void crearActividad() {
        if (sistema.listarCursos().isEmpty()) {
            mostrarInfo("Primero debes registrar al menos un curso.");
            return;
        }
        DialogoActividad dialogo = new DialogoActividad(this, "Nueva actividad", sistema.listarCursos(), null);
        dialogo.setVisible(true);
        if (!dialogo.isConfirmado()) {
            return;
        }
        ejecutarSeguro(() -> {
            ActividadAcademica actividad = sistema.registrarActividad(
                    dialogo.getTipoActividad(),
                    dialogo.getCursoId(),
                    dialogo.getTituloActividad(),
                    dialogo.getDescripcion(),
                    dialogo.getFecha(),
                    dialogo.getPrioridad()
            );
            actividadSeleccionadaId = actividad.getId();
            refrescarTodo();
        });
    }

    private void editarActividad() {
        ActividadAcademica actividad = getActividadSeleccionada();
        if (actividad == null) {
            mostrarInfo("Selecciona una tarjeta del tablero.");
            return;
        }
        DialogoActividad dialogo = new DialogoActividad(this, "Editar actividad", sistema.listarCursos(), actividad);
        dialogo.setVisible(true);
        if (!dialogo.isConfirmado()) {
            return;
        }
        ejecutarSeguro(() -> {
            sistema.editarActividad(
                    actividad.getId(),
                    dialogo.getTipoActividad(),
                    dialogo.getCursoId(),
                    dialogo.getTituloActividad(),
                    dialogo.getDescripcion(),
                    dialogo.getFecha(),
                    dialogo.getPrioridad(),
                    dialogo.getEstado()
            );
            refrescarTodo();
        });
    }

    private void eliminarActividad() {
        ActividadAcademica actividad = getActividadSeleccionada();
        if (actividad == null) {
            mostrarInfo("Selecciona una tarjeta del tablero.");
            return;
        }
        boolean confirmar = JOptionPane.showConfirmDialog(this, "Desea eliminar la actividad seleccionada?", "Confirmar", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
        if (!confirmar) {
            return;
        }
        ejecutarSeguro(() -> {
            sistema.eliminarActividad(actividad.getId());
            actividadSeleccionadaId = null;
            refrescarTodo();
        });
    }

    private void pasarAProceso() {
        ActividadAcademica actividad = getActividadSeleccionada();
        if (actividad == null) {
            mostrarInfo("Selecciona una tarjeta del tablero.");
            return;
        }
        ejecutarSeguro(() -> {
            sistema.cambiarEstadoActividad(actividad.getId(), EstadoActividad.PROCESO);
            refrescarTodo();
        });
    }

    private void completarActividad() {
        ActividadAcademica actividad = getActividadSeleccionada();
        if (actividad == null) {
            mostrarInfo("Selecciona una tarjeta del tablero.");
            return;
        }
        ejecutarSeguro(() -> {
            sistema.marcarActividadCompletada(actividad.getId());
            refrescarTodo();
        });
    }

    private void refrescarTodo() {
        cargarCursos();
        refrescarTablero();
        refrescarResumen();
    }

    private void cargarCursos() {
        comboCurso.removeAllItems();
        comboCurso.addItem(new ItemCurso("", "Todos"));
        for (Curso curso : sistema.listarCursos()) {
            comboCurso.addItem(new ItemCurso(curso.getId(), curso.getNombre()));
        }
    }

    private void refrescarTablero() {
        panelInicio.removeAll();
        panelProceso.removeAll();
        panelFinalizado.removeAll();

        String cursoId = getCursoFiltro();
        Prioridad prioridad = getPrioridadFiltro();

        for (ActividadAcademica actividad : sistema.filtrarActividades(cursoId, prioridad)) {
            Curso curso = sistema.listarCursos().stream()
                    .filter(c -> c.getId().equals(actividad.getCursoId()))
                    .findFirst()
                    .orElse(null);
            JPanel tarjeta = crearTarjetaActividad(actividad, curso);
            if (actividad.getEstado() == EstadoActividad.FINALIZADO) {
                panelFinalizado.add(tarjeta);
                panelFinalizado.add(Box.createVerticalStrut(8));
            } else if (actividad.getEstado() == EstadoActividad.PROCESO) {
                panelProceso.add(tarjeta);
                panelProceso.add(Box.createVerticalStrut(8));
            } else {
                panelInicio.add(tarjeta);
                panelInicio.add(Box.createVerticalStrut(8));
            }
        }

        agregarTextoVacio(panelInicio, "No hay actividades en inicio.");
        agregarTextoVacio(panelProceso, "No hay actividades en proceso.");
        agregarTextoVacio(panelFinalizado, "No hay actividades finalizadas.");

        panelInicio.revalidate();
        panelProceso.revalidate();
        panelFinalizado.revalidate();
        panelInicio.repaint();
        panelProceso.repaint();
        panelFinalizado.repaint();
    }

    private JPanel crearTarjetaActividad(ActividadAcademica actividad, Curso curso) {
        Color fondo = actividad.getEstado() == EstadoActividad.FINALIZADO
                ? new Color(216, 245, 219)
                : actividad.getEstado() == EstadoActividad.PROCESO
                ? new Color(222, 236, 255)
                : new Color(255, 248, 188);

        JPanel card = new JPanel(new BorderLayout(6, 6));
        card.setBackground(fondo);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 210));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                        actividad.estaVencida() ? new Color(196, 71, 71)
                                : actividad.getId().equals(actividadSeleccionadaId) ? new Color(53, 84, 125)
                                : new Color(193, 180, 144),
                        actividad.getId().equals(actividadSeleccionadaId) ? 3 : 1
                ),
                new EmptyBorder(10, 10, 10, 10)
        ));

        String tituloTexto = actividad.getTitulo();
        if (actividad.estaVencida()) {
            tituloTexto += " (Vencida)";
        }
        JLabel titulo = new JLabel("<html><body style='width:190px'><b>" + tituloTexto + "</b></body></html>");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 15));

        JTextArea detalle = new JTextArea();
        detalle.setEditable(false);
        detalle.setOpaque(false);
        detalle.setLineWrap(true);
        detalle.setWrapStyleWord(true);
        detalle.setFont(new Font("SansSerif", Font.PLAIN, 12));
        detalle.setText(
                "Tipo: " + actividad.getTipoActividad() + "\n" +
                "Curso: " + (curso == null ? "Sin curso" : curso.getNombre()) + "\n" +
                "Fecha: " + (actividad.tieneFecha() ? actividad.getFechaEntrega() : "Sin fecha") + "\n" +
                "Prioridad: " + actividad.getPrioridad() + "\n" +
                actividad.getResumenVisual()
        );

        JTextArea descripcion = new JTextArea(recortarTexto(actividad.getDescripcion()));
        descripcion.setEditable(false);
        descripcion.setOpaque(false);
        descripcion.setLineWrap(true);
        descripcion.setWrapStyleWord(true);
        descripcion.setFont(new Font("SansSerif", Font.ITALIC, 11));

        card.add(titulo, BorderLayout.NORTH);
        card.add(detalle, BorderLayout.CENTER);
        card.add(descripcion, BorderLayout.SOUTH);

        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                actividadSeleccionadaId = actividad.getId();
                refrescarTablero();
                if (e.getClickCount() == 2) {
                    editarActividad();
                }
            }
        });

        return card;
    }

    private String recortarTexto(String texto) {
        if (texto == null || texto.isBlank()) {
            return "Sin descripcion";
        }
        String limpio = texto.trim();
        return limpio.length() > 90 ? limpio.substring(0, 87) + "..." : limpio;
    }

    private void agregarTextoVacio(JPanel panel, String mensaje) {
        if (panel.getComponentCount() == 0) {
            JLabel label = new JLabel(mensaje);
            label.setForeground(new Color(112, 104, 92));
            label.setBorder(new EmptyBorder(14, 8, 8, 8));
            panel.add(label);
        }
    }

    private void refrescarResumen() {
        SistemaAcademico.ResumenSistema resumen = sistema.obtenerResumen();
        lblTotal.setText(String.valueOf(resumen.getTotal()));
        lblInicio.setText(String.valueOf(resumen.getInicio()));
        lblProceso.setText(String.valueOf(resumen.getProceso()));
        lblVencidas.setText(String.valueOf(resumen.getVencidas()));
        lblFinalizadas.setText(String.valueOf(resumen.getFinalizadas()));

        ResultadoEntropia entropia = resumen.getEntropia();
        lblEntropia.setText(entropia.getNivel().toUpperCase());
        lblPuntaje.setText(String.valueOf(entropia.getPuntaje()));
        areaMensaje.setText(entropia.getMensaje());
    }

    public void mostrarAdvertencia(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Advertencia", JOptionPane.WARNING_MESSAGE);
    }

    private void ejecutarSeguro(Runnable accion) {
        try {
            accion.run();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarInfo(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Informacion", JOptionPane.INFORMATION_MESSAGE);
    }

    private ActividadAcademica getActividadSeleccionada() {
        if (actividadSeleccionadaId == null) {
            return null;
        }
        try {
            return sistema.buscarActividad(actividadSeleccionadaId);
        } catch (Exception ex) {
            actividadSeleccionadaId = null;
            return null;
        }
    }

    private Curso getCursoActivo() {
        String id = getCursoFiltro();
        if (id.isBlank()) {
            return null;
        }
        return sistema.listarCursos().stream().filter(c -> c.getId().equals(id)).findFirst().orElse(null);
    }

    private String getCursoFiltro() {
        ItemCurso item = (ItemCurso) comboCurso.getSelectedItem();
        return item == null ? "" : item.id();
    }

    private Prioridad getPrioridadFiltro() {
        String valor = (String) comboPrioridad.getSelectedItem();
        if (valor == null || valor.equals("Todas")) {
            return null;
        }
        return Prioridad.valueOf(valor);
    }

    private JButton crearBoton(String texto, java.awt.event.ActionListener accion) {
        JButton boton = new JButton(texto);
        boton.addActionListener(accion);
        boton.setFocusPainted(false);
        return boton;
    }

    private JLabel crearTextoBlanco(String texto) {
        JLabel label = new JLabel(texto);
        label.setForeground(Color.WHITE);
        return label;
    }

    private void estilizarCombo(JComboBox<?> combo, int ancho) {
        combo.setPreferredSize(new Dimension(ancho, 28));
    }

    private record ItemCurso(String id, String nombre) {
        @Override
        public String toString() {
            return nombre;
        }
    }
}
