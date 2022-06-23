package views;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;

public class JTableEditable extends JTable {

	private boolean editableColumns[];	
	
	@Override
	public void setValueAt(Object newValue, int row, int column) {
		Object oldValue = getValueAt(row, column);
		
		if(oldValue instanceof String)
		{
		if(Informacion.validarFecha((String)oldValue) && !Informacion.validarFecha((String)newValue))
				{newValue = oldValue;}
		}

//	    System.out.println("Escribiendo el valor: " + newValue + "F:" + row + "C:" + column);

	    super.setValueAt(newValue, row, column);
    }
	
	
	

	public JTableEditable() {
		this.editableColumns = new boolean[super.dataModel.getColumnCount()];
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		return this.editableColumns[column];
//		return column!=0 && column!= 6;
	};

	public void setColumnasEditables(boolean columnas[]) {
		this.editableColumns = columnas;
	}
	
}
