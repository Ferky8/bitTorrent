package Vista;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Controlador.ControladorConfiguracion;
import Controlador.ControladorDetallesPeer;
import Controlador.ControladorDetallesTracker;

import javax.swing.JTabbedPane;

public class TrackerMain extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TrackerMain frame = new TrackerMain();
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
	public TrackerMain() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 400);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		contentPane.add(tabbedPane, BorderLayout.CENTER);
		
		ControladorConfiguracion configuracion = new ControladorConfiguracion();
		ControladorDetallesTracker detallesTracker =  new ControladorDetallesTracker();
		ControladorDetallesPeer detallesPeer = new ControladorDetallesPeer();
		
		Component tab = new Configuracion(configuracion, detallesPeer);
		Component tab2 = new Trackers(detallesTracker);
		Component tab3 = new Peers(detallesPeer);
		
		tabbedPane.addTab("Configuración", null, tab, null);
				
		tabbedPane.addTab("Trackers", null, tab2, null);
		
		tabbedPane.addTab("Peers", null, tab3, null);
		
		setResizable(false);
		setLocationRelativeTo(null);
	}
}
