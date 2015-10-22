package Vista;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import Controlador.ControladorDetallesPeer;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

public class Peers extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private ControladorDetallesPeer detallesPeer;
	
	private JTable table2;
	private DefaultTableModel dtm2;
	
	public Peers() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 450, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 14, 0, 220, 34, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 1.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{1.0, 0.0, 1.0, 0.0, 1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JLabel lblPeersActivos = new JLabel("Peers activos:");
		GridBagConstraints gbc_lblPeersActivos = new GridBagConstraints();
		gbc_lblPeersActivos.anchor = GridBagConstraints.NORTHWEST;
		gbc_lblPeersActivos.insets = new Insets(0, 0, 5, 5);
		gbc_lblPeersActivos.gridx = 1;
		gbc_lblPeersActivos.gridy = 1;
		add(lblPeersActivos, gbc_lblPeersActivos);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		GridBagConstraints gbc_scrollPane_1 = new GridBagConstraints();
		gbc_scrollPane_1.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPane_1.fill = GridBagConstraints.BOTH;
		gbc_scrollPane_1.gridx = 1;
		gbc_scrollPane_1.gridy = 3;
		add(scrollPane_1, gbc_scrollPane_1);
		
		table2 = new JTable();
		
		scrollPane_1.setViewportView(table2);
		
		dtm2 = new DefaultTableModel() {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				//All cells can`t be edited
				return false;
			}
		};
		dtm2.addColumn("Id");
		dtm2.addColumn("IP");
		dtm2.addColumn("Puerto");
		dtm2.addColumn("Torrent");
		dtm2.addColumn("Descarga");
		dtm2.addColumn("Subida");
		
		for(int i=0; i < 10; i++) {
			Object[] fila = new Object[6];
			
			fila[0] = (int)(Math.random() * ((1000) + 1));
			fila[1] = new String(Integer.toString(1 + (int)(Math.random() * ((254) + 1)))+"."+Integer.toString(1 + (int)(Math.random() * ((254) + 1)))+"."+Integer.toString(1 + (int)(Math.random() * ((254) + 1)))+"."+Integer.toString(1 + (int)(Math.random() * ((254) + 1))));
			fila[2] = 2000;
			fila[3] = 1 + (int)(Math.random() * ((9999) + 1));
			fila[4] = (int)(Math.random() * ((99999999) + 1));
			fila[5] = (int)(Math.random() * ((99999999) + 1));
			dtm2.addRow(fila);
		}
		
		table2.setModel(dtm2);
		table2.getColumn("Id").setPreferredWidth(36);
		table2.getColumn("Puerto").setPreferredWidth(24);
	}
	
	public void cargarDatos() {
		detallesPeer.cargarDatos();
	}
}
