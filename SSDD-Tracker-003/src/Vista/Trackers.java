package Vista;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import Controlador.ControladorDetallesTracker;
import Entidad.Tracker;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.Calendar;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ConcurrentHashMap;

public class Trackers extends JPanel implements Observer {

	private static final long serialVersionUID = 1L;
	
	private JTable table;
	private DefaultTableModel dtm;
	
	public Trackers(ControladorDetallesTracker detallesTracker) {		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 450, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 14, 0, 220, 34, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 1.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{1.0, 0.0, 1.0, 0.0, 1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JLabel lblInformacinDeLostrackers = new JLabel("Informaci\u00F3n de los Trackers:");
		GridBagConstraints gbc_lblInformacinDeLostrackers = new GridBagConstraints();
		gbc_lblInformacinDeLostrackers.anchor = GridBagConstraints.NORTHWEST;
		gbc_lblInformacinDeLostrackers.insets = new Insets(0, 0, 5, 5);
		gbc_lblInformacinDeLostrackers.gridx = 1;
		gbc_lblInformacinDeLostrackers.gridy = 1;
		add(lblInformacinDeLostrackers, gbc_lblInformacinDeLostrackers);
		
		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 1;
		gbc_scrollPane.gridy = 3;
		add(scrollPane, gbc_scrollPane);
		
		table = new JTable();
		
		scrollPane.setViewportView(table);
		
		dtm = new DefaultTableModel() {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				//All cells can`t be edited
				return false;
			}
		};
		dtm.addColumn("Id");
		dtm.addColumn("Master");
		dtm.addColumn("�ltimo KA");
		
		table.setModel(dtm);
		detallesTracker.anadirObserver(this);
	}
	
	@Override
	public void update(Observable o, Object trackers) {
		dtm = new DefaultTableModel() {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				//All cells can`t be edited
				return false;
			}
		};
		dtm.addColumn("Id");
		dtm.addColumn("Master");
		dtm.addColumn("�ltimo KA");
		
		ConcurrentHashMap<Integer, Tracker> concurrentHashMap = (ConcurrentHashMap<Integer, Tracker>) trackers;
		for (int key : concurrentHashMap.keySet()) {
    		Tracker tracker = concurrentHashMap.get(key);
    		
    		if(tracker.getId() != 0) {
    			
    			Date ahora = new Date();
        		long resta = ahora.getTime() - tracker.getUltimoKA().getTime();
        		
        		Date fechaResta = new Date(resta);
        		Calendar calendar = Calendar.getInstance();
        		calendar.setTime(fechaResta);
        		int seconds = calendar.get(Calendar.SECOND);
        		int miliseconds = calendar.get(Calendar.MILLISECOND);
        		
        		Object[] fila = new Object[3];
    			fila[0]= tracker.getId();
    			if(tracker.esMaster()) {
    				fila[1]= "Master";
    			}else {
    				fila[1]= "Slave";
    			}
    			fila[2]= seconds+":"+miliseconds;
    			
    			dtm.addRow(fila);
    		}
		}
		
		table.setModel(dtm);
	}
}
