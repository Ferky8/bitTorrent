package Vista;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class Peers extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private JTable table2;
	private DefaultTableModel dtm2;
	
	public Peers(JPanel panel_2) {

		panel_2.setLayout(null);
		
		JLabel lblPeersActivos = new JLabel("Peers activos:");
		lblPeersActivos.setBounds(60, 34, 139, 14);
		panel_2.add(lblPeersActivos);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(60, 71, 450, 220);
		panel_2.add(scrollPane_1);
		
		table2 = new JTable();
		
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
				
		table2.setModel(dtm2);
		
		scrollPane_1.setViewportView(table2);
		
		panel_2.setVisible(true);
	}
}
