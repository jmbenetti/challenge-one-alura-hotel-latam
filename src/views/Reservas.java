package views;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.SystemColor;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.ImageIcon;
import java.awt.Color;
import javax.swing.JTextField;
import com.toedter.calendar.JDateChooser;
import java.awt.Font;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JPopupMenu;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Toolkit;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

import java.sql.ResultSet;

public class Reservas extends JFrame {

	private JPanel contentPane;
	private JTextField txtValor;
	private float fValorNoche = 3000;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Reservas frame = new Reservas();
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
	public Reservas() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(Reservas.class.getResource("/imagenes/calendario.png")));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 910, 540);
		setResizable(false);
		contentPane = new JPanel();
		contentPane.setBackground(SystemColor.control);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		setResizable(false);
		setLocationRelativeTo(null);

		JPanel panel = new JPanel();
		panel.setBackground(new Color(245, 245, 245));
		panel.setBounds(0, 0, 900, 502);
		contentPane.add(panel);
		panel.setLayout(null);

		JDateChooser txtFechaE = new JDateChooser();

		txtFechaE.setBounds(88, 166, 235, 33);
		panel.add(txtFechaE);

		JLabel lblNewLabel_1 = new JLabel("Fecha de Check In");
		lblNewLabel_1.setBounds(88, 142, 133, 14);
		lblNewLabel_1.setFont(new Font("Arial", Font.PLAIN, 14));
		panel.add(lblNewLabel_1);

		JLabel lblNewLabel_1_1 = new JLabel("Fecha de Check Out");
		lblNewLabel_1_1.setBounds(88, 210, 133, 14);
		lblNewLabel_1_1.setFont(new Font("Arial", Font.PLAIN, 14));
		panel.add(lblNewLabel_1_1);

		JDateChooser txtFechaS = new JDateChooser();
		txtFechaS.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				if ("date".equals(evt.getPropertyName())) {
//	                System.out.println("Salida: " + (Date) evt.getNewValue());
//	               System.out.println(contarNoches(txtFechaE.getDate(), txtFechaS.getDate()));
					long nContado = contarNoches(txtFechaE.getDate(), txtFechaS.getDate());
					float nTotal = nContado * fValorNoche;
					txtValor.setText(String.format("%.2f", nTotal));
				}
			}
		});

		txtFechaE.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				if ("date".equals(evt.getPropertyName())) {
					// Revisar días de reserva
//	                System.out.println("Entrada: " + (Date) evt.getNewValue());
//					System.out.println(contarNoches(txtFechaE.getDate(), txtFechaS.getDate()));
					long nContado = contarNoches(txtFechaE.getDate(), txtFechaS.getDate());
					float nTotal = nContado * fValorNoche;
					txtValor.setText(String.format("%.2f", nTotal));
				}
			}
		});

		txtFechaS.setBounds(88, 234, 235, 33);
		txtFechaS.getCalendarButton().setBackground(Color.WHITE);
		panel.add(txtFechaS);

		txtValor = new JTextField();
		txtValor.setBounds(88, 303, 235, 33);
		txtValor.setEnabled(false);
		txtValor.setBackground(Color.WHITE);
		txtValor.setDisabledTextColor(Color.BLACK);
		panel.add(txtValor);
		txtValor.setColumns(10);

		JLabel lblNewLabel_1_1_1 = new JLabel("Valor de la Reserva");
		lblNewLabel_1_1_1.setBounds(88, 278, 133, 14);
		lblNewLabel_1_1_1.setFont(new Font("Arial", Font.PLAIN, 14));
		panel.add(lblNewLabel_1_1_1);

		JComboBox txtFormaPago = new JComboBox();
		txtFormaPago.setBounds(88, 373, 235, 33);
		txtFormaPago.setFont(new Font("Arial", Font.PLAIN, 14));
		txtFormaPago.setModel(new DefaultComboBoxModel(
				new String[] { "Tarjeta de Crédito", "Tarjeta de Débito", "Dinero en efectivo" }));
		panel.add(txtFormaPago);

		JLabel lblNewLabel_1_1_1_1 = new JLabel("Forma de pago");
		lblNewLabel_1_1_1_1.setBounds(88, 347, 133, 24);
		lblNewLabel_1_1_1_1.setFont(new Font("Arial", Font.PLAIN, 14));
		panel.add(lblNewLabel_1_1_1_1);

		JLabel lblNewLabel_4 = new JLabel("Sistema de Reservas");
		lblNewLabel_4.setBounds(108, 93, 199, 42);
		lblNewLabel_4.setForeground(new Color(65, 105, 225));
		lblNewLabel_4.setFont(new Font("Arial", Font.BOLD, 20));
		panel.add(lblNewLabel_4);

		JButton btnReservar = new JButton("Continuar");
		btnReservar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Revisar condiciones para continuar
				String szErrores = "";
				long nNoches = 0;
				long nReservaCreada = -1; 

				Date fechaEntrada = txtFechaE.getDate();
				if (fechaEntrada == null) {
					szErrores += "Falta la fecha de entrada. ";
				} else {
					String szFechaEntrada = DateFormat.getDateInstance().format(fechaEntrada);
					System.out.println(fechaEntrada);
				}

				Date fechaSalida = txtFechaS.getDate();
				if (fechaSalida == null) {
					szErrores += "Falta la fecha de salida. ";
				} else {
					nNoches = contarNoches(fechaEntrada, fechaSalida);
					if (nNoches == 0) {
						szErrores += "Revise las fechas de su reserva. ";
					}

				}

				String szFormaPago = txtFormaPago.getSelectedItem().toString();
//				System.out.println(szFormaPago);

//				System.out.println(szErrores);
				//Obtengo la fecha actual sin hora
				Date fechaActual = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
				if(fechaEntrada.before(fechaActual))
				{szErrores += "La fecha de entrada no puede ser anterior al día de hoy. ";}
				

				if (szErrores.isBlank()) {
					// Crear reserva en base de datos
					nReservaCreada = nuevaReserva(fechaEntrada, fechaSalida, nNoches * fValorNoche, szFormaPago);
//					System.out.println(nReservaCreada);
					// ----
					JOptionPane.showMessageDialog(null, "Se creó la reserva " + nReservaCreada + ". Ingrese los datos del cliente a continuación.");
					RegistroHuesped huesped = new RegistroHuesped(nReservaCreada);
					huesped.setVisible(true);
					dispose();
				} else {
					JOptionPane.showMessageDialog(null, szErrores);
				}
			}
		});
		btnReservar.setForeground(Color.WHITE);
		btnReservar.setBounds(183, 436, 140, 33);
		btnReservar.setIcon(new ImageIcon(Reservas.class.getResource("/imagenes/calendario.png")));
		btnReservar.setBackground(new Color(65, 105, 225));
		btnReservar.setFont(new Font("Arial", Font.PLAIN, 14));
		panel.add(btnReservar);

		JPanel panel_1 = new JPanel();
		panel_1.setBackground(Color.WHITE);
		panel_1.setBounds(399, 0, 491, 502);
		panel.add(panel_1);
		panel_1.setLayout(null);

		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setBounds(0, -16, 500, 539);
		panel_1.add(lblNewLabel);
		lblNewLabel.setBackground(Color.WHITE);
		lblNewLabel.setIcon(new ImageIcon(Reservas.class.getResource("/imagenes/reservas-img-2.png")));

		JLabel lblNewLabel_2 = new JLabel("");
		lblNewLabel_2.setIcon(new ImageIcon(Reservas.class.getResource("/imagenes/Ha-100px.png")));
		lblNewLabel_2.setBounds(15, 6, 104, 107);
		panel.add(lblNewLabel_2);

		JLabel lblDiario = new JLabel("Valor por noche: $" + String.format("%.2f", fValorNoche));
		lblDiario.setBounds(129, 39, 194, 24);
		panel.add(lblDiario);
	}

	private static void addPopup(Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}

			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}

			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}

	private long contarNoches(Date fechaEntrada, Date fechaSalida) {
		long nNoches = 0;
		if (fechaEntrada == null) {
			nNoches = 0;
		} else {
			String szFechaEntrada = DateFormat.getDateInstance().format(fechaEntrada);
//			System.out.println(fechaEntrada);
		}
		if (fechaSalida == null) {
//			szErrores += "Falta la fecha de salida. ";
			nNoches = 0;
		} else {
			String szFechaSalida = DateFormat.getDateInstance().format(fechaSalida);
//			System.out.println(fechaSalida);
			LocalDateTime lEntrada = LocalDateTime.ofInstant(fechaEntrada.toInstant(), ZoneId.systemDefault());
			LocalDateTime lSalida = LocalDateTime.ofInstant(fechaSalida.toInstant(), ZoneId.systemDefault());
			nNoches = ChronoUnit.DAYS.between(lEntrada, lSalida);
			if (nNoches < 0) {
				nNoches = 0;
			}
			System.out.println("Noches de reserva: " + nNoches);
		}
		return nNoches;
	}

	private long nuevaReserva(Date fechaEntrada, Date fechaSalida, float fValor, String szFormaPago)
	{
		String pattern = "yyyy-MM-dd";
		SimpleDateFormat formatter = new SimpleDateFormat(pattern);
		String szFechaEntrada = formatter.format(fechaEntrada);
		String szFechaSalida = formatter.format(fechaSalida);
		
		long nInsertado = -1;
		String szInsert = "INSERT INTO `alurahotel`.`reservas` (`fechaentrada`, `fechasalida`, `valor`, `formapago`) VALUES ('" + szFechaEntrada + "', '"+ szFechaSalida + "', '"+ fValor + "', '" + szFormaPago + "');";
		
		ConexionBD miConexion = new ConexionBD();
		try {
			miConexion.actualizar(szInsert);
			ResultSet miResultSet = miConexion.consultar("SELECT LAST_INSERT_ID();");
			while (miResultSet.next()) {
				nInsertado = (miResultSet.getLong(1));
			}
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		System.out.println("Se creó la reserva " + nInsertado);
		return nInsertado;
	}
}
