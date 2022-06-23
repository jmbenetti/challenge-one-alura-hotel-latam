package views;

import java.awt.Component;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class DateCellRenderer extends DefaultTableCellRenderer {
	public DateCellRenderer() {
	}
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		this.setText("");
		if (value instanceof String) {
			if (Informacion.validarFecha((String) value)) {
				this.setText((String) value);
			}
		}
		if ((value != null) && (value instanceof Date || value instanceof java.sql.Date)) {
			String strDate = new SimpleDateFormat("dd/MM/yyyy").format((Date) value);
			this.setText(strDate);
		}
		return this;
	}
}
