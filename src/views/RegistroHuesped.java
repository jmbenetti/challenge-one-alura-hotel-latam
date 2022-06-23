package views;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import java.awt.Color;
import com.toedter.calendar.JDateChooser;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.SystemColor;
import java.awt.event.ActionListener;
import java.util.Date;
import java.awt.event.ActionEvent;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;

public class RegistroHuesped extends JFrame {

	private JPanel contentPane;
	private JTextField txtNombre;
	private JTextField txtApellido;
	private JTextField txtTelefono;
	private JTextField txtNreserva;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RegistroHuesped frame = new RegistroHuesped(-1);
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
	public RegistroHuesped(long nReserva) {
		setIconImage(Toolkit.getDefaultToolkit().getImage(RegistroHuesped.class.getResource("/imagenes/persona.png")));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 910, 634);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		setLocationRelativeTo(null);

		txtNombre = new JTextField();
		txtNombre.setBackground(Color.WHITE);
		txtNombre.setColumns(10);
		txtNombre.setBounds(576, 150, 255, 33);
		contentPane.add(txtNombre);

		txtApellido = new JTextField();
		txtApellido.setColumns(10);
		txtApellido.setBackground(Color.WHITE);
		txtApellido.setBounds(576, 217, 255, 33);
		contentPane.add(txtApellido);

		JDateChooser txtFechaN = new JDateChooser();
		txtFechaN.setBounds(576, 281, 255, 33);
		contentPane.add(txtFechaN);

		JComboBox txtNacionalidad = new JComboBox();
		txtNacionalidad.setFont(new Font("Arial", Font.PLAIN, 14));
		txtNacionalidad.setModel(new DefaultComboBoxModel(Informacion.nacionalidades));
		txtNacionalidad.setBounds(576, 350, 255, 33);
		contentPane.add(txtNacionalidad);

		JLabel lblNewLabel_1 = new JLabel("Nombre");
		lblNewLabel_1.setFont(new Font("Arial", Font.PLAIN, 14));
		lblNewLabel_1.setBounds(578, 125, 253, 14);
		contentPane.add(lblNewLabel_1);

		JLabel lblNewLabel_1_1 = new JLabel("Apellido");
		lblNewLabel_1_1.setFont(new Font("Arial", Font.PLAIN, 14));
		lblNewLabel_1_1.setBounds(576, 194, 255, 14);
		contentPane.add(lblNewLabel_1_1);

		JLabel lblNewLabel_1_1_1 = new JLabel("Fecha de Nacimiento");
		lblNewLabel_1_1_1.setFont(new Font("Arial", Font.PLAIN, 14));
		lblNewLabel_1_1_1.setBounds(576, 256, 255, 14);
		contentPane.add(lblNewLabel_1_1_1);

		JLabel lblNewLabel_1_1_1_1 = new JLabel("Nacionalidad");
		lblNewLabel_1_1_1_1.setFont(new Font("Arial", Font.PLAIN, 14));
		lblNewLabel_1_1_1_1.setBounds(576, 325, 255, 14);
		contentPane.add(lblNewLabel_1_1_1_1);

		JLabel lblNewLabel = new JLabel("New label");
		lblNewLabel.setIcon(new ImageIcon(RegistroHuesped.class.getResource("/imagenes/registro.png")));
		lblNewLabel.setBounds(0, 0, 502, 556);
		contentPane.add(lblNewLabel);

		JButton btnCancelar = new JButton("");
		btnCancelar.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				txtNombre.setText("");
				txtApellido.setText("");
				txtFechaN.setCalendar(null);
				txtNacionalidad.setSelectedIndex(0);
				txtTelefono.setText("");
			}
		});
		btnCancelar.setIcon(new ImageIcon(RegistroHuesped.class.getResource("/imagenes/cancelar.png")));
		btnCancelar.setBackground(SystemColor.menu);
		btnCancelar.setBounds(764, 543, 54, 41);
		contentPane.add(btnCancelar);

		JButton btnGuardar = new JButton("");
		btnGuardar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String szErrores = "";

				// Revisar campos cargados
				String szNombre = txtNombre.getText();
				String szApellido = txtApellido.getText();
				Date fechaNacimiento = txtFechaN.getDate();
				String szNacionalidad = txtNacionalidad.getSelectedItem().toString();
				String szTelefono = txtTelefono.getText();
				long nReserva = -1;
				if (!txtNreserva.getText().isBlank()) {
					nReserva = Long.parseLong(txtNreserva.getText());
				}

				if (szNombre.isBlank()) {
					szErrores += "Debe ingresar un nombre. ";
				}
				if (szApellido.isBlank()) {
					szErrores += "Debe ingresar un apellido. ";
				}
				if (fechaNacimiento == null) {
					szErrores += "Falta la fecha de nacimiento. ";
				}
				if (szNacionalidad.isBlank()) {
					szErrores += "Debe seleccionar la nacionalidad. ";
				}
				if (szTelefono.isBlank()) {
					szErrores += "Falta el número de teléfono. ";
				}

				// --

				if (szErrores.isBlank()) {
					
					if(nuevoCliente(szNombre, szApellido, fechaNacimiento, szNacionalidad, szTelefono, nReserva))
					{
						Exito exito = new Exito();
						exito.setVisible(true);
						dispose();
					}
					else
					{
						JOptionPane.showMessageDialog(null, "No se pudo guardar el cliente.");
					}
					
				} else {
					JOptionPane.showMessageDialog(null, szErrores);
				}
			}
		});
		btnGuardar.setIcon(new ImageIcon(RegistroHuesped.class.getResource("/imagenes/disquete.png")));
		btnGuardar.setBackground(SystemColor.menu);
		btnGuardar.setBounds(700, 543, 54, 41);
		contentPane.add(btnGuardar);

		JButton btnSalir = new JButton("");
		btnSalir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (JOptionPane.showConfirmDialog(null,
						"Si sale ahora, se cancelará la reserva creada, ¿desea continuar?", "Atención",
						JOptionPane.YES_NO_OPTION) == 0) {
					borrarReservaCancelada();
					MenuUsuario usuario = new MenuUsuario();
					usuario.setVisible(true);
					dispose();
				}
			}
		});
		btnSalir.setIcon(new ImageIcon(RegistroHuesped.class.getResource("/imagenes/cerrar-sesion 32-px.png")));
		btnSalir.setBackground(SystemColor.menu);
		btnSalir.setBounds(828, 543, 54, 41);
		contentPane.add(btnSalir);

		JLabel lblNewLabel_1_2 = new JLabel("Teléfono");
		lblNewLabel_1_2.setFont(new Font("Arial", Font.PLAIN, 14));
		lblNewLabel_1_2.setBounds(578, 394, 253, 14);
		contentPane.add(lblNewLabel_1_2);

		txtTelefono = new JTextField();
		txtTelefono.setColumns(10);
		txtTelefono.setBackground(Color.WHITE);
		txtTelefono.setBounds(576, 419, 255, 33);
		contentPane.add(txtTelefono);

		JLabel lblNewLabel_2 = new JLabel("");
		lblNewLabel_2.setIcon(new ImageIcon(RegistroHuesped.class.getResource("/imagenes/Ha-100px.png")));
		lblNewLabel_2.setBounds(780, 11, 104, 107);
		contentPane.add(lblNewLabel_2);

		JLabel lblNewLabel_4 = new JLabel("Registro de Huésped");
		lblNewLabel_4.setForeground(new Color(12, 138, 199));
		lblNewLabel_4.setFont(new Font("Arial", Font.BOLD, 20));
		lblNewLabel_4.setBounds(576, 74, 198, 42);
		contentPane.add(lblNewLabel_4);

		JLabel lblNewLabel_1_2_1 = new JLabel("Número de Reserva");
		lblNewLabel_1_2_1.setFont(new Font("Arial", Font.PLAIN, 14));
		lblNewLabel_1_2_1.setBounds(578, 455, 253, 14);
		contentPane.add(lblNewLabel_1_2_1);

		txtNreserva = new JTextField();
		txtNreserva.setEnabled(false);
		txtNreserva.setColumns(10);
		txtNreserva.setBackground(Color.WHITE);
		txtNreserva.setBounds(576, 480, 255, 33);
		if (nReserva >= 0) {
			txtNreserva.setText(Long.toString(nReserva));
		}
		txtNreserva.setBackground(Color.WHITE);
		txtNreserva.setDisabledTextColor(Color.BLACK);
		contentPane.add(txtNreserva);
	}

	private void borrarReservaCancelada() {
		if (!txtNreserva.getText().isBlank()) {
			String szDelete = "DELETE FROM `alurahotel`.`reservas` WHERE id=" + txtNreserva.getText() + ";";
			ConexionBD miConexion = new ConexionBD();
			try {
				miConexion.actualizar(szDelete);
			} catch (Exception e) {
				System.out.println(e);
			}
		}
	}

	private boolean nuevoCliente(String szNombre, String szApellido, Date fechaNacimiento, String szNacionalidad,
			String szTelefono, long nReserva) {
		boolean bGuardado = false;
		String pattern = "yyyy-MM-dd";
		SimpleDateFormat formatter = new SimpleDateFormat(pattern);
		String szFechaNacimiento = formatter.format(fechaNacimiento);

		String szInsert = "INSERT INTO alurahotel.huespedes(nombre, apellido, nacimiento, nacionalidad, telefono, reserva) "
				+ "VALUES('" + szNombre + "', '" + szApellido + "', '" + szFechaNacimiento + "', '" + szNacionalidad
				+ "', '" + szTelefono + "', '"+ nReserva + "')";
		
		ConexionBD miConexion = new ConexionBD();
		try {
			miConexion.actualizar(szInsert);
			bGuardado = true;
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		System.out.println("Se guardaron los datos del cliente.");
		return bGuardado;
	}

}
