package Vista;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import Controlador.ControladorConfiguracion;

import java.awt.event.ActionListener;
import java.util.regex.Pattern;
import java.awt.event.ActionEvent;

public class Configuracion extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private ControladorConfiguracion configuracion;
	
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private Pattern pattern;
	private Pattern pattern_2;
	
	private static final String IPADDRESS_PATTERNPEDO = 
			"^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
			"([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
			"([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
			"([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
	
	private static final String PORT_PATTERN = 
			"^([0-9]{1,4}|[1-5][0-9]{4}|6[0-4][0-9]{3}|65[0-4][0-9]{2}|655[0-2][0-9]|6553[0-5])$";
	
	public Configuracion() {
		pattern = Pattern.compile(IPADDRESS_PATTERNPEDO);
		pattern_2 = Pattern.compile(PORT_PATTERN);
	    
		GridBagLayout gbl_c = new GridBagLayout();
		gbl_c.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
		gbl_c.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
		gbl_c.columnWeights = new double[]{1.0, 0.0, 1.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		gbl_c.rowWeights = new double[]{1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		setLayout(gbl_c);
		
		JLabel lblIp = new JLabel("IP");
		GridBagConstraints gbc_lblIp = new GridBagConstraints();
		gbc_lblIp.anchor = GridBagConstraints.EAST;
		gbc_lblIp.insets = new Insets(0, 0, 5, 5);
		gbc_lblIp.gridx = 3;
		gbc_lblIp.gridy = 1;
		add(lblIp, gbc_lblIp);
		
		textField = new JTextField();
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.insets = new Insets(0, 0, 5, 5);
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.gridx = 5;
		gbc_textField.gridy = 1;
		add(textField, gbc_textField);
		textField.setColumns(20);
		
		JButton btnIniciarparar = new JButton("Iniciar/Parar");
		btnIniciarparar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//Si estan mal la IP y el puerto
				if (!(pattern.matcher(textField.getText()).matches()) && !(pattern_2.matcher(textField_1.getText()).matches())) {
					JOptionPane.showMessageDialog(null, "IP y puerto invalidos", "ERROR", JOptionPane.ERROR_MESSAGE);
				//Si esta mal solo la IP
				} else if (!(pattern.matcher(textField.getText()).matches())){
					JOptionPane.showMessageDialog(null, "IP invalida", "ERROR", JOptionPane.ERROR_MESSAGE);
				//Si esta mal solo el puerto
				}else if (!(pattern_2.matcher(textField_1.getText()).matches())){
					JOptionPane.showMessageDialog(null, "Puerto invalido", "ERROR", JOptionPane.ERROR_MESSAGE);
				//Si esta todo bien
				} else{
					
				}
			}
		});
		GridBagConstraints gbc_btnIniciarparar = new GridBagConstraints();
		gbc_btnIniciarparar.insets = new Insets(0, 0, 5, 5);
		gbc_btnIniciarparar.gridx = 1;
		gbc_btnIniciarparar.gridy = 2;
		add(btnIniciarparar, gbc_btnIniciarparar);
		
		JLabel lblPuerto = new JLabel("Puerto");
		GridBagConstraints gbc_lblPuerto = new GridBagConstraints();
		gbc_lblPuerto.insets = new Insets(0, 0, 5, 5);
		gbc_lblPuerto.gridx = 3;
		gbc_lblPuerto.gridy = 3;
		add(lblPuerto, gbc_lblPuerto);
		
		textField_1 = new JTextField();
		GridBagConstraints gbc_textField_1 = new GridBagConstraints();
		gbc_textField_1.insets = new Insets(0, 0, 5, 5);
		gbc_textField_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_1.gridx = 5;
		gbc_textField_1.gridy = 3;
		add(textField_1, gbc_textField_1);
		textField_1.setColumns(20);
		
		JButton btnDesconectar = new JButton("Desconectar");
		btnDesconectar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showConfirmDialog(null, "¿Seguro que deseas desconectar?", "ATENCIÓN", JOptionPane.YES_NO_OPTION);
			}
		});
		GridBagConstraints gbc_btnDesconectar = new GridBagConstraints();
		gbc_btnDesconectar.insets = new Insets(0, 0, 5, 5);
		gbc_btnDesconectar.gridx = 1;
		gbc_btnDesconectar.gridy = 4;
		add(btnDesconectar, gbc_btnDesconectar);
		
		JLabel lblId = new JLabel("ID");
		GridBagConstraints gbc_lblId = new GridBagConstraints();
		gbc_lblId.insets = new Insets(0, 0, 5, 5);
		gbc_lblId.gridx = 3;
		gbc_lblId.gridy = 5;
		add(lblId, gbc_lblId);
		
		textField_2 = new JTextField();
		GridBagConstraints gbc_textField_2 = new GridBagConstraints();
		gbc_textField_2.insets = new Insets(0, 0, 5, 5);
		gbc_textField_2.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_2.gridx = 5;
		gbc_textField_2.gridy = 5;
		add(textField_2, gbc_textField_2);
		textField_2.setColumns(20);
	}
	
	public void iniciar(String IP, int puerto, int ID) {
		configuracion.iniciar(IP, puerto, ID);
	}
	
	public void parar(int ID) {
		configuracion.parar(ID);
	}
	
	public void desconectar(int ID) {
		configuracion.desconectar(ID);
	}
}
