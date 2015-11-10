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
	private Trackers trackerDetailPanel;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private Pattern pattern;
	private Pattern pattern_2;
	
	private static final String IPADDRESS_PATTERN = 
			"^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
			"([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
			"([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
			"([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
	
	private static final String PORT_PATTERN = 
			"^([0-9]{1,4}|[1-5][0-9]{4}|6[0-4][0-9]{3}|65[0-4][0-9]{2}|655[0-2][0-9]|6553[0-5])$";
	private JTextField textField_3;
	
	public Configuracion(Trackers trackerDetailPanel) {
		this.trackerDetailPanel = trackerDetailPanel;
		
		pattern = Pattern.compile(IPADDRESS_PATTERN);
		pattern_2 = Pattern.compile(PORT_PATTERN);
	    
		GridBagLayout gbl_c = new GridBagLayout();
		gbl_c.columnWidths = new int[]{0, 0, 0, 0, 0, 30, 0, 0, 0, 30, 0, 0};
		gbl_c.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
		gbl_c.columnWeights = new double[]{1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, Double.MIN_VALUE};
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
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.gridwidth = 5;
		gbc_textField.insets = new Insets(0, 0, 5, 5);
		gbc_textField.gridx = 5;
		gbc_textField.gridy = 1;
		add(textField, gbc_textField);
		textField.setColumns(20);
		
		JButton btnIniciarparar = new JButton("Iniciar/Parar");
		btnIniciarparar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//Si estan mal la IP y el puerto
				if (!(pattern.matcher(textField.getText()).matches()) && !(pattern_2.matcher(textField_1.getText()).matches())
						&& !(pattern_2.matcher(textField_3.getText()).matches())) {
					JOptionPane.showMessageDialog(null, "IP y puerto invalidos", "ERROR", JOptionPane.ERROR_MESSAGE);
				//Si esta mal solo la IP
				} else if (!(pattern.matcher(textField.getText()).matches())){
					JOptionPane.showMessageDialog(null, "IP invalida", "ERROR", JOptionPane.ERROR_MESSAGE);
				//Si esta mal solo el puerto t-t
				} else if (!(pattern_2.matcher(textField_1.getText()).matches())){
					JOptionPane.showMessageDialog(null, "Puerto T-T invalido", "ERROR", JOptionPane.ERROR_MESSAGE);
					//Si esta mal solo el puerto t-p
				} else if (!(pattern_2.matcher(textField_3.getText()).matches())){
					JOptionPane.showMessageDialog(null, "Puerto T-P invalido", "ERROR", JOptionPane.ERROR_MESSAGE);
				//Si esta todo bien
				} else {
					iniciar(textField.getText(), Integer.parseInt(textField_1.getText()), Integer.parseInt(textField_2.getText()));
				}
			}
		});
		GridBagConstraints gbc_btnIniciarparar = new GridBagConstraints();
		gbc_btnIniciarparar.insets = new Insets(0, 0, 5, 5);
		gbc_btnIniciarparar.gridx = 1;
		gbc_btnIniciarparar.gridy = 2;
		add(btnIniciarparar, gbc_btnIniciarparar);
		
		JLabel lblPuertoTt = new JLabel("Puerto T-T");
		GridBagConstraints gbc_lblPuertoTt = new GridBagConstraints();
		gbc_lblPuertoTt.insets = new Insets(0, 0, 5, 5);
		gbc_lblPuertoTt.gridx = 3;
		gbc_lblPuertoTt.gridy = 3;
		add(lblPuertoTt, gbc_lblPuertoTt);
		
		textField_1 = new JTextField();
		GridBagConstraints gbc_textField_1 = new GridBagConstraints();
		gbc_textField_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_1.insets = new Insets(0, 0, 5, 5);
		gbc_textField_1.gridx = 5;
		gbc_textField_1.gridy = 3;
		add(textField_1, gbc_textField_1);
		textField_1.setColumns(20);
		
		JButton btnDesconectar = new JButton("Desconectar");
		btnDesconectar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int opcion = JOptionPane.showConfirmDialog(null, "¿Seguro que deseas desconectar?", "ATENCIÓN", JOptionPane.YES_NO_OPTION);
				if(opcion == 0) {
					desconectar();
				}
			}
		});
		
		JLabel lblPuertoTp = new JLabel("Puerto T-P");
		GridBagConstraints gbc_lblPuertoTp = new GridBagConstraints();
		gbc_lblPuertoTp.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblPuertoTp.insets = new Insets(0, 0, 5, 5);
		gbc_lblPuertoTp.gridx = 7;
		gbc_lblPuertoTp.gridy = 3;
		add(lblPuertoTp, gbc_lblPuertoTp);
		
		textField_3 = new JTextField();
		GridBagConstraints gbc_textField_3 = new GridBagConstraints();
		gbc_textField_3.insets = new Insets(0, 0, 5, 5);
		gbc_textField_3.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_3.gridx = 9;
		gbc_textField_3.gridy = 3;
		add(textField_3, gbc_textField_3);
		textField_3.setColumns(10);
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
		gbc_textField_2.gridwidth = 5;
		gbc_textField_2.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_2.insets = new Insets(0, 0, 5, 5);
		gbc_textField_2.gridx = 5;
		gbc_textField_2.gridy = 5;
		add(textField_2, gbc_textField_2);
		textField_2.setColumns(20);
		
		textField.setText("228.4.4.4");
		textField_1.setText("9000");
		textField_2.setText("1");
		textField_3.setText("1234");
		
	}
	
	public void iniciar(String IP, int puerto, int ID) {
		configuracion = new ControladorConfiguracion();
		configuracion.iniciar(IP, puerto, ID);
		configuracion.anadirObserver(trackerDetailPanel);
	}
	
	public void parar(int ID) {
		configuracion.parar(ID);
	}
	
	public void desconectar() {
		configuracion.desconectar();
	}
}
