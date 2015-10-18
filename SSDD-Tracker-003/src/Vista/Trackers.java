package Vista;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class Trackers extends JPanel {

	private static final long serialVersionUID = 1L;
	private JTable table;
	private DefaultTableModel dtm;
	
	public Trackers(JPanel panel_1) {
		panel_1.setLayout(null);
		
		JLabel lblInformacinDeLostrackers = new JLabel("Informaci\u00F3n de los Trackers:");
		lblInformacinDeLostrackers.setBounds(60, 34, 139, 14);
		panel_1.add(lblInformacinDeLostrackers);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(60, 71, 450, 220);
		panel_1.add(scrollPane);
		
		table = new JTable();
		
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
		dtm.addColumn("Último KA");
				
		table.setModel(dtm);
		
		scrollPane.setViewportView(table);
	}
}
