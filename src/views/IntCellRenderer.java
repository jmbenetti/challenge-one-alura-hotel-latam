package views;

import java.awt.Component;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class IntCellRenderer extends DefaultTableCellRenderer {
	public IntCellRenderer() {
	}
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		this.setText("");
		if (Informacion.esEntero((String)value)) {
				this.setText((String) value);
		}
		return this;
	}
}
