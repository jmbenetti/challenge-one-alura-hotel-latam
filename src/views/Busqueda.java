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
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import java.awt.Color;
import java.awt.SystemColor;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.awt.event.ActionEvent;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JDesktopPane;
import java.awt.Toolkit;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Busqueda extends JFrame {

	private JPanel contentPane;
	private JTextField txtBuscar;
	private JTable tbHuespedes;
	private JTable tbReservas;
	private JTabbedPane panel;

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

		JButton btnEditar = new JButton("");
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

		tbHuespedes = new JTable();
		tbHuespedes.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				System.out.println(verHuespedElegido());
				System.out.println(verReservaEnHuespedes());
			}
		});
		tbHuespedes.setFont(new Font("Arial", Font.PLAIN, 14));
		panel.addTab("Huéspedes", new ImageIcon(Busqueda.class.getResource("/imagenes/persona.png")),
				new JScrollPane(tbHuespedes), null);

		tbReservas = new JTable();
		tbReservas.setFont(new Font("Arial", Font.PLAIN, 14));
		panel.addTab("Reservas", new ImageIcon(Busqueda.class.getResource("/imagenes/calendario.png")),
				new JScrollPane(tbReservas), null);

		JButton btnEliminar = new JButton("");
		btnEliminar.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				//Huéspedes
				if (panel.getSelectedIndex() == 0) {
					if(verHuespedElegido() >= 0)
					{
						Object[] opciones = { "Sí", "No" };
						int eleccion = JOptionPane.showOptionDialog(rootPane,
								"¿Está seguro de que desea eliminar el húesped " + verHuespedElegido()
										+ " y su reserva asociada?",
								"Atención", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, opciones, "No");
						if (eleccion == JOptionPane.YES_OPTION) {
							borrarHuesped(verHuespedElegido());
							borrarReserva(verReservaEnHuespedes());
							actualizarTabla();
						}
					}
				}
				else
				{
					if(verReservaElegida() >= 0)
					{
						Object[] opciones = { "Sí", "No" };
						int eleccion = JOptionPane.showOptionDialog(rootPane,
								"¿Está seguro de que desea eliminar la reserva " + verReservaElegida()
										+ " y al huésped que la realizó?",
								"Atención", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, opciones, "No");
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

		JButton btnCancelar = new JButton("");
		btnCancelar.setIcon(new ImageIcon(Busqueda.class.getResource("/imagenes/cancelar.png")));
		btnCancelar.setBackground(SystemColor.menu);
		btnCancelar.setBounds(713, 416, 54, 41);
		contentPane.add(btnCancelar);

		JLabel lblNewLabel_2 = new JLabel("");
		lblNewLabel_2.setIcon(new ImageIcon(Busqueda.class.getResource("/imagenes/Ha-100px.png")));
		lblNewLabel_2.setBounds(25, 10, 104, 107);
		contentPane.add(lblNewLabel_2);
		setResizable(false);
		mostrarHuespedes("");
		mostrarReservas("");
	}

	private void mostrarHuespedes(String szBusqueda) {
		String szValor[] = new String[7];
		String szColumna[] = { "id", "Nombre", "Apellido", "Nacimiento", "Nacionalidad", "Teléfono", "Reserva" };
		DefaultTableModel miTableModel = new DefaultTableModel(szColumna, 0);
		String szConsulta = "SELECT * FROM huespedes WHERE 1";
		if (!szBusqueda.isBlank()) {
			szConsulta += " AND " + szBusqueda;
		}
		ConexionBD miConexionBD = new ConexionBD();
		ResultSet miResultSet = null;
		try {
			miResultSet = miConexionBD.consultar(szConsulta);
			while (miResultSet.next()) {
				szValor[0] = miResultSet.getString("id");
				szValor[1] = miResultSet.getString("nombre");
				szValor[2] = miResultSet.getString("apellido");
				szValor[3] = miResultSet.getString("nacimiento");
				szValor[4] = miResultSet.getString("nacionalidad");
				szValor[5] = miResultSet.getString("telefono");
				szValor[6] = miResultSet.getString("reserva");
				miTableModel.addRow(szValor);
			}
		} catch (Exception e) {
			System.out.println("Error al leer de la base de datos.");
		}
		tbHuespedes.setModel(miTableModel);
	}

	private void mostrarReservas(String szBusqueda) {
		String szValor[] = new String[5];
		String szColumna[] = { "id", "Entrada", "Salida", "Valor", "Forma de pago" };
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
				szValor[1] = miResultSet.getString("fechaentrada");
				szValor[2] = miResultSet.getString("fechasalida");
				szValor[3] = miResultSet.getString("valor");
				szValor[4] = miResultSet.getString("formapago");
				miTableModel.addRow(szValor);
			}
		} catch (Exception e) {
			System.out.println("Error al leer de la base de datos.");
		}
		tbReservas.setModel(miTableModel);
	}

	private int verHuespedElegido() {
		int nFila = tbHuespedes.getSelectedRow();
		int nHuesped = -1;
		if(nFila>=0)
		{
			nHuesped = Integer.parseInt(tbHuespedes.getModel().getValueAt(nFila, 0).toString());
		}
		return nHuesped;
	}

	private int verReservaEnHuespedes() {
		int nFila = tbHuespedes.getSelectedRow();
		int nReserva = Integer.parseInt(tbHuespedes.getModel().getValueAt(nFila, 6).toString());
		return nReserva;
	}
	
	private int verReservaElegida()
	{
		int nFila = tbReservas.getSelectedRow();
		int nReserva = -1;
		if(nFila>=0)
		{
			nReserva = Integer.parseInt(tbReservas.getModel().getValueAt(nFila, 0).toString());
		}
		return nReserva;
	}

	private void borrarHuesped(int nHuesped) {
		ConexionBD miConexionBD = new ConexionBD();
		miConexionBD.actualizar("DELETE FROM huespedes WHERE id=" + nHuesped);
	}
	
	private void borrarHuespedPorReserva(int nReserva)
	{
		ConexionBD miConexionBD = new ConexionBD();
		miConexionBD.actualizar("DELETE FROM huespedes WHERE reserva=" + nReserva);
	}

	private void borrarReserva(int nReserva) {
		ConexionBD miConexionBD = new ConexionBD();
		miConexionBD.actualizar("DELETE FROM reservas WHERE id=" + nReserva);
	}

	private void actualizarTabla() {
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

}
