package Vista;

import java.awt.EventQueue;
import java.awt.GridBagConstraints;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import Controlador.ControladorPeer;

import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.regex.Pattern;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Peer extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private JTextField textField;
	private JTextField textField_2;
	private Pattern pattern;
	private Pattern pattern_2;
	
	private ControladorPeer controladorPeer = new ControladorPeer();
	
	private static final String IPADDRESS_PATTERN = 
			"^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
			"([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
			"([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
			"([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
	
	private static final String PORT_PATTERN = 
			"^([0-9]{1,4}|[1-5][0-9]{4}|6[0-4][0-9]{3}|65[0-4][0-9]{2}|655[0-2][0-9]|6553[0-5])$";
	private JTextField textField_1;
	private JButton btnIniciar;
	
	public Peer() {
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 400, 400);
		
		pattern = Pattern.compile(IPADDRESS_PATTERN);
		pattern_2 = Pattern.compile(PORT_PATTERN);
	    
		GridBagLayout gbl_c = new GridBagLayout();
		gbl_c.columnWidths = new int[]{0, 0, 0, 30, 0, 0, 0, 30, 0, 0};
		gbl_c.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_c.columnWeights = new double[]{1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, Double.MIN_VALUE};
		gbl_c.rowWeights = new double[]{1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		getContentPane().setLayout(gbl_c);
		
		btnIniciar = new JButton("Iniciar");
		btnIniciar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				iniciar(textField.getText(), Integer.parseInt(textField_1.getText()));
			}
		});
		GridBagConstraints gbc_btnIniciar = new GridBagConstraints();
		gbc_btnIniciar.gridwidth = 3;
		gbc_btnIniciar.insets = new Insets(0, 0, 5, 5);
		gbc_btnIniciar.gridx = 3;
		gbc_btnIniciar.gridy = 1;
		getContentPane().add(btnIniciar, gbc_btnIniciar);
		
		JLabel lblIp = new JLabel("IP");
		GridBagConstraints gbc_lblIp = new GridBagConstraints();
		gbc_lblIp.anchor = GridBagConstraints.EAST;
		gbc_lblIp.insets = new Insets(0, 0, 5, 5);
		gbc_lblIp.gridx = 1;
		gbc_lblIp.gridy = 3;
		getContentPane().add(lblIp, gbc_lblIp);
		
		textField = new JTextField();
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.gridwidth = 5;
		gbc_textField.insets = new Insets(0, 0, 5, 5);
		gbc_textField.gridx = 3;
		gbc_textField.gridy = 3;
		getContentPane().add(textField, gbc_textField);
		textField.setColumns(20);
		
		JLabel lblPuerto = new JLabel("Puerto");
		GridBagConstraints gbc_lblPuerto = new GridBagConstraints();
		gbc_lblPuerto.insets = new Insets(0, 0, 5, 5);
		gbc_lblPuerto.gridx = 1;
		gbc_lblPuerto.gridy = 5;
		getContentPane().add(lblPuerto, gbc_lblPuerto);
		
		textField_1 = new JTextField();
		textField_1.setText("6969");
		textField_1.setColumns(20);
		GridBagConstraints gbc_textField_1 = new GridBagConstraints();
		gbc_textField_1.gridwidth = 5;
		gbc_textField_1.insets = new Insets(0, 0, 5, 5);
		gbc_textField_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_1.gridx = 3;
		gbc_textField_1.gridy = 5;
		getContentPane().add(textField_1, gbc_textField_1);
		
		JLabel lblId = new JLabel("ID");
		GridBagConstraints gbc_lblId = new GridBagConstraints();
		gbc_lblId.insets = new Insets(0, 0, 5, 5);
		gbc_lblId.gridx = 1;
		gbc_lblId.gridy = 7;
		getContentPane().add(lblId, gbc_lblId);
		
		textField_2 = new JTextField();
		GridBagConstraints gbc_textField_2 = new GridBagConstraints();
		gbc_textField_2.gridwidth = 5;
		gbc_textField_2.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_2.insets = new Insets(0, 0, 5, 5);
		gbc_textField_2.gridx = 3;
		gbc_textField_2.gridy = 7;
		getContentPane().add(textField_2, gbc_textField_2);
		textField_2.setColumns(20);
		
		textField.setText("228.4.4.4");
		textField_2.setText("1");
		
		
		setResizable(false);
		setLocationRelativeTo(null);
	}
	
	public void iniciar(String IP, int puerto) {
		controladorPeer.iniciar(IP, puerto);
	}
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Peer frame = new Peer();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
