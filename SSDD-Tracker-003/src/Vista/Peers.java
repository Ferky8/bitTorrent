package Vista;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

public class Peers extends JPanel {

	private static final long serialVersionUID = 1L;
	
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
		
		table2.setModel(dtm2);
	}
}
