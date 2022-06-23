package views;

import java.awt.BorderLayout;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import com.toedter.calendar.JDateChooser;
import com.toedter.calendar.JDateChooserCellEditor;

import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import java.awt.Color;
import java.awt.SystemColor;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.awt.event.ActionEvent;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JDesktopPane;
import java.awt.Toolkit;
import javax.swing.event.ChangeListener;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

public class Busqueda extends JFrame {

	private JPanel contentPane;
	private JTextField txtBuscar;
	private JTableEditable tbHuespedes;
	private JTableEditable tbReservas;
	private JTabbedPane panel;
	private JButton btnEditar;
	private JButton btnCancelar;
	private int nColumnaEditada;
	private int nFilaEditada;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Busqueda frame = new Busqueda();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Busqueda() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(Busqueda.class.getResource("/imagenes/lupa2.png")));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 910, 516);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		setLocationRelativeTo(null);

		txtBuscar = new JTextField();
		txtBuscar.setBounds(647, 85, 158, 31);
		contentPane.add(txtBuscar);
		txtBuscar.setColumns(10);

		JButton btnBuscar = new JButton("");
		btnBuscar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				actualizarTabla();
			}
		});
		btnBuscar.setBackground(Color.WHITE);
		btnBuscar.setIcon(new ImageIcon(Busqueda.class.getResource("/imagenes/lupa2.png")));
		btnBuscar.setBounds(815, 75, 54, 41);
		contentPane.add(btnBuscar);

		btnEditar = new JButton("");

		btnCancelar = new JButton("");
		btnEditar.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// Huéspedes
				JTable tbEditada = null;
				if (panel.getSelectedIndex() == 0) {
					tbEditada = tbHuespedes;
				}
				if (panel.getSelectedIndex() == 1) {
					tbEditada = tbReservas;
				}

				int nFila = tbEditada.getSelectedRow();
				int nColumna = tbEditada.getSelectedColumn();
				nFilaEditada = nFila;
				nColumnaEditada = nColumna;
				verCambioCelda();
				if (nFila >= 0 && nColumna >= 0) {
					habilitarEdicion(true);
					tbEditada.requestFocus();
					if (tbEditada.editCellAt(nFila, nColumna)) {
						btnEditar.setEnabled(false);
						btnCancelar.setEnabled(true);
					} else {
						habilitarEdicion(false);
					}
				} else {
					JOptionPane.showMessageDialog(null, "Seleccione una celda antes de pulsar editar");
				}
			}
		});
		btnEditar.setIcon(new ImageIcon(Busqueda.class.getResource("/imagenes/editar-texto.png")));
		btnEditar.setBackground(SystemColor.menu);
		btnEditar.setBounds(587, 416, 54, 41);
		contentPane.add(btnEditar);

		JLabel lblNewLabel_4 = new JLabel("Sistema de Búsqueda");
		lblNewLabel_4.setForeground(new Color(12, 138, 199));
		lblNewLabel_4.setFont(new Font("Arial", Font.BOLD, 24));
		lblNewLabel_4.setBounds(155, 42, 258, 42);
		contentPane.add(lblNewLabel_4);

		JButton btnSalir = new JButton("");
		btnSalir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MenuUsuario usuario = new MenuUsuario();
				usuario.setVisible(true);
				dispose();
			}
		});
		btnSalir.setIcon(new ImageIcon(Busqueda.class.getResource("/imagenes/cerrar-sesion 32-px.png")));
		btnSalir.setForeground(Color.WHITE);
		btnSalir.setBackground(Color.WHITE);
		btnSalir.setBounds(815, 416, 54, 41);
		contentPane.add(btnSalir);

		panel = new JTabbedPane(JTabbedPane.TOP);
		panel.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				txtBuscar.setText("");
				actualizarTabla();
			}
		});
		panel.setBounds(10, 127, 874, 265);
		contentPane.add(panel);

		tbHuespedes = new JTableEditable();

		tbHuespedes.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_LEFT
						|| e.getKeyCode() == KeyEvent.VK_KP_UP || e.getKeyCode() == KeyEvent.VK_DOWN) {
					verCambioCelda();
				}

				if (e.getKeyCode() == KeyEvent.VK_ENTER) {

					// Si estoy editando...
					if (btnCancelar.isEnabled()) {
						terminarEdicion();

					}
				}
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					cancelarEdicion();
				}
			}
		});
		tbHuespedes.setRowSelectionAllowed(false);
		tbHuespedes.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				verCambioCelda();
			}
		});

		tbHuespedes.setFont(new Font("Arial", Font.PLAIN, 14));
		panel.addTab("Huéspedes", new ImageIcon(Busqueda.class.getResource("/imagenes/persona.png")),
				new JScrollPane(tbHuespedes), null);

		tbReservas = new JTableEditable();

		tbReservas.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_LEFT
						|| e.getKeyCode() == KeyEvent.VK_KP_UP || e.getKeyCode() == KeyEvent.VK_DOWN) {
					verCambioCelda();
				}

				if (e.getKeyCode() == KeyEvent.VK_ENTER) {

					// Si estoy editando...
					if (btnCancelar.isEnabled()) {
						terminarEdicion();
					}
				}
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					cancelarEdicion();
				}
			}
		});

		tbReservas.setRowSelectionAllowed(false);

		tbReservas.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				verCambioCelda();
			}
		});

		tbHuespedes.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
		tbReservas.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);

		habilitarEdicion(false);

		tbReservas.setFont(new Font("Arial", Font.PLAIN, 14));
		panel.addTab("Reservas", new ImageIcon(Busqueda.class.getResource("/imagenes/calendario.png")),
				new JScrollPane(tbReservas), null);

		JButton btnEliminar = new JButton("");
		btnEliminar.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// Huéspedes
				if (panel.getSelectedIndex() == 0) {
					if (verHuespedElegido() >= 0) {
						Object[] opciones = { "Sí", "No" };
						int eleccion = JOptionPane.showOptionDialog(rootPane,
								"¿Está seguro de que desea eliminar el húesped " + verHuespedElegido()
										+ " y su reserva asociada?",
								"Atención", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, opciones,
								"No");
						if (eleccion == JOptionPane.YES_OPTION) {
							borrarHuesped(verHuespedElegido());
							borrarReserva(verReservaEnHuespedes());
							actualizarTabla();
						}
					}
				} else {
					if (verReservaElegida() >= 0) {
						Object[] opciones = { "Sí", "No" };
						int eleccion = JOptionPane.showOptionDialog(rootPane,
								"¿Está seguro de que desea eliminar la reserva " + verReservaElegida()
										+ " y al huésped que la realizó?",
								"Atención", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, opciones,
								"No");
						if (eleccion == JOptionPane.YES_OPTION) {
							borrarHuespedPorReserva(verReservaElegida());
							borrarReserva(verReservaElegida());
							actualizarTabla();
						}
					}

				}
			}
		});
		btnEliminar.setIcon(new ImageIcon(Busqueda.class.getResource("/imagenes/deletar.png")));
		btnEliminar.setBackground(SystemColor.menu);
		btnEliminar.setBounds(651, 416, 54, 41);
		contentPane.add(btnEliminar);

		btnCancelar.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				cancelarEdicion();
			}
		});
		btnCancelar.setEnabled(false);
		btnCancelar.setIcon(new ImageIcon(Busqueda.class.getResource("/imagenes/cancelar.png")));
		btnCancelar.setBackground(SystemColor.menu);
		btnCancelar.setBounds(713, 416, 54, 41);
		contentPane.add(btnCancelar);

		JLabel lblNewLabel_2 = new JLabel("");
		lblNewLabel_2.setIcon(new ImageIcon(Busqueda.class.getResource("/imagenes/Ha-100px.png")));
		lblNewLabel_2.setBounds(25, 10, 104, 107);
		contentPane.add(lblNewLabel_2);
		setResizable(false);
		actualizarTabla();
	}

	private void mostrarHuespedes(String szBusqueda) {
		String szValor[] = new String[7];
		Object objValor[] = new Object[7];
		String szColumna[] = { "id", "Nombre", "Apellido", "Nacimiento", "Nacionalidad", "Teléfono", "Reserva" };
		DefaultTableModel miTableModel = new DefaultTableModel(szColumna, 0);
//		EditableTableModel miTableModel = new EditableTableModel(szColumna, 0);
		String szConsulta = "SELECT * FROM huespedes WHERE 1";
		if (!szBusqueda.isBlank()) {
			szConsulta += " AND " + szBusqueda;
		}
		ConexionBD miConexionBD = new ConexionBD();
		ResultSet miResultSet = null;
		try {
			miResultSet = miConexionBD.consultar(szConsulta);
			while (miResultSet.next()) {

				objValor[0] = miResultSet.getString("id");
				objValor[1] = miResultSet.getString("nombre");
				objValor[2] = miResultSet.getString("apellido");
//				objValor[3] = miResultSet.getDate("nacimiento");
				objValor[3] = new SimpleDateFormat("dd/MM/yyyy").format((Date) miResultSet.getDate("nacimiento"));
				objValor[4] = miResultSet.getString("nacionalidad");
				objValor[5] = miResultSet.getString("telefono");
				objValor[6] = miResultSet.getString("reserva");
				miTableModel.addRow(objValor);

			}
		} catch (Exception e) {
			System.out.println("Error al leer de la base de datos.");
		}
		tbHuespedes.setModel(miTableModel);
		JComboBox cbNacionalidades = new JComboBox(Informacion.nacionalidades);

		tbHuespedes.getColumnModel().getColumn(4).setCellEditor(new DefaultCellEditor(cbNacionalidades));
		//
		TableColumn column3 = tbHuespedes.getColumnModel().getColumn(3);
		column3.setCellRenderer(new DateCellRenderer());
	}

	private void mostrarReservas(String szBusqueda) {
		String szValor[] = new String[5];
		String szColumna[] = { "id", "Entrada", "Salida", "Valor", "Forma de pago" };
//		EditableTableModel miTableModel = new EditableTableModel(szColumna, 0);
		DefaultTableModel miTableModel = new DefaultTableModel(szColumna, 0);
		String szConsulta = "SELECT * FROM reservas WHERE 1";
		if (!szBusqueda.isBlank()) {
			szConsulta += " AND " + szBusqueda;
		}
		ConexionBD miConexionBD = new ConexionBD();
		ResultSet miResultSet = null;
		try {
			miResultSet = miConexionBD.consultar(szConsulta);
			while (miResultSet.next()) {
				szValor[0] = miResultSet.getString("id");
//				szValor[1] = miResultSet.getString("fechaentrada");
				szValor[1] = new SimpleDateFormat("dd/MM/yyyy").format((Date) miResultSet.getDate("fechaentrada"));
//				szValor[2] = miResultSet.getString("fechasalida");
				szValor[2] = new SimpleDateFormat("dd/MM/yyyy").format((Date) miResultSet.getDate("fechasalida"));
				// Saco los decimales al valor
				szValor[3] = String.valueOf(miResultSet.getString("valor")).split("\\.")[0];
				szValor[4] = miResultSet.getString("formapago");
				miTableModel.addRow(szValor);
			}
		} catch (Exception e) {
			System.out.println("Error al leer de la base de datos.");
		}
		tbReservas.setModel(miTableModel);
		JComboBox cbFormas = new JComboBox(Informacion.formasdepago);
		tbReservas.getColumnModel().getColumn(4).setCellEditor(new DefaultCellEditor(cbFormas));
		TableColumn column1 = tbReservas.getColumnModel().getColumn(1);
		column1.setCellRenderer(new DateCellRenderer());
		TableColumn column2 = tbReservas.getColumnModel().getColumn(2);
		column2.setCellRenderer(new DateCellRenderer());
		TableColumn column3 = tbReservas.getColumnModel().getColumn(3);
		column3.setCellRenderer(new IntCellRenderer());
	}

	private int verHuespedElegido() {
		int nFila = tbHuespedes.getSelectedRow();
		int nHuesped = -1;
		if (nFila >= 0) {
			nHuesped = Integer.parseInt(tbHuespedes.getModel().getValueAt(nFila, 0).toString());
		}
		return nHuesped;
	}

	private int verReservaEnHuespedes() {
		int nFila = tbHuespedes.getSelectedRow();
		int nReserva = Integer.parseInt(tbHuespedes.getModel().getValueAt(nFila, 6).toString());
		return nReserva;
	}

	private int verReservaElegida() {
		int nFila = tbReservas.getSelectedRow();
		int nReserva = -1;
		if (nFila >= 0) {
			nReserva = Integer.parseInt(tbReservas.getModel().getValueAt(nFila, 0).toString());
		}
		return nReserva;
	}

	private void borrarHuesped(int nHuesped) {
		ConexionBD miConexionBD = new ConexionBD();
		miConexionBD.actualizar("DELETE FROM huespedes WHERE id=" + nHuesped);
	}

	private void borrarHuespedPorReserva(int nReserva) {
		ConexionBD miConexionBD = new ConexionBD();
		miConexionBD.actualizar("DELETE FROM huespedes WHERE reserva=" + nReserva);
	}

	private void borrarReserva(int nReserva) {
		ConexionBD miConexionBD = new ConexionBD();
		miConexionBD.actualizar("DELETE FROM reservas WHERE id=" + nReserva);
	}

	private void actualizarTabla() {
		nColumnaEditada = -1;
		nFilaEditada = -1;
		if (panel.getSelectedIndex() == 0) {
			// Huespedes
			if (txtBuscar.getText().isBlank()) {
				mostrarHuespedes("");
			} else {
				mostrarHuespedes("apellido LIKE '%" + txtBuscar.getText() + "%'");
			}
		} else {
			// reservas
			if (txtBuscar.getText().isBlank()) {
				mostrarReservas("");
			} else {
				mostrarReservas("id=" + txtBuscar.getText());
			}
		}
	}

	private void habilitarEdicion(boolean bHabilitar) {
		// id, nombre, apellido, nacimiento, nacionalidad, teléfono, reserva
		boolean[] bColumnasEditablesHuespedes = { false, bHabilitar, bHabilitar, bHabilitar, bHabilitar, bHabilitar,
				false };
		// id, fechadeentrada, fechadesalida, valor, formadepago
		boolean[] bColumnasEditablesReservas = { false, bHabilitar, bHabilitar, bHabilitar, bHabilitar };
		tbHuespedes.setColumnasEditables(bColumnasEditablesHuespedes);
		tbReservas.setColumnasEditables(bColumnasEditablesReservas);
	}

	private void guardarDatosHuesped(int nFila, int nColumna) {
		String[] szColumna = { "id", "nombre", "apellido", "nacimiento", "nacionalidad", "telefono", "reserva" };

		String szValor = (String) tbHuespedes.getValueAt(nFila, nColumna);
		// Si es columna de fecha, le doy formato de SQL
		if (nColumna == 3) {
			szValor = Informacion.fechaSql(szValor);
		}
		int nId = Integer.parseInt((String) tbHuespedes.getValueAt(nFila, 0));

		String szUpdate = "UPDATE huespedes SET " + szColumna[nColumna] + "='" + szValor + "' WHERE id=" + nId;
		// System.out.println(szUpdate);
		ConexionBD miConexionBD = new ConexionBD();
		if (miConexionBD.actualizar(szUpdate)) {
			JOptionPane.showMessageDialog(null, "Datos actualizados con éxito");
		} else {
			JOptionPane.showMessageDialog(null, "Error al intentar guardar los datos");
		}
		actualizarTabla();
	}

	private void guardarDatosReserva(int nFila, int nColumna) {
		String[] szColumna = { "id", "fechaentrada", "fechasalida", "valor", "formapago" };

		String szValor = (String) tbReservas.getValueAt(nFila, nColumna);
		// Si es columna de fecha, le doy formato de SQL
		if (nColumna == 1 || nColumna == 2) {
			szValor = Informacion.fechaSql(szValor);
		}
		int nId = Integer.parseInt((String) tbReservas.getValueAt(nFila, 0));

		String szUpdate = "UPDATE reservas SET " + szColumna[nColumna] + "='" + szValor + "' WHERE id=" + nId;
		
		ConexionBD miConexionBD = new ConexionBD();
		if (miConexionBD.actualizar(szUpdate)) {
			JOptionPane.showMessageDialog(null, "Datos actualizados con éxito");
		} else {
			JOptionPane.showMessageDialog(null, "Error al intentar guardar los datos");
		}
		actualizarTabla();
	}

	private void cancelarEdicion() {
		habilitarEdicion(false);
		btnEditar.setEnabled(true);
		btnCancelar.setEnabled(false);
		actualizarTabla();
	}

	private void terminarEdicion() {
		habilitarEdicion(false);
		btnEditar.setEnabled(true);
		btnCancelar.setEnabled(false);
		// Si estoy en el panel huéspedes
		if (panel.getSelectedIndex() == 0) {

			guardarDatosHuesped(nFilaEditada, nColumnaEditada);
		} else {
			// reservas
//			System.out.println("Guardar reservas");
//			System.out.println("Fila: " + nFilaEditada + " columna " + nColumnaEditada);
			guardarDatosReserva(nFilaEditada, nColumnaEditada);
		}

	}

	private void verCambioCelda() {
		// Si estoy editando, me fijo si cambié de celda
		System.out.println("Revisando celda");
		System.out.println("Editando fila " + nFilaEditada + " columna " + nColumnaEditada);
		if (btnCancelar.isEnabled()) {

			// Huéspedes
			if (panel.getSelectedIndex() == 0) {
				if (tbHuespedes.getSelectedColumn() != nColumnaEditada
						|| tbHuespedes.getSelectedRow() != nFilaEditada) {
					// Si estoy en un combo, guardo al cambiar de celda
					if (nColumnaEditada == 4) {
						terminarEdicion();
					} else {
						// Si cambié, cancelo la edición
						cancelarEdicion();
					}
				}
			} else {
				if (tbReservas.getSelectedColumn() != nColumnaEditada || tbReservas.getSelectedRow() != nFilaEditada) {
					// Si estoy en un combo, guardo al cambiar de celda
					if (nColumnaEditada == 4) {
						terminarEdicion();
					} else {
						// Si cambié, cancelo la edición
						cancelarEdicion();
					}
				}
			}
		}
	}
}
