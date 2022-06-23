package views;

import java.util.Vector;

import javax.swing.table.DefaultTableModel;

public class EditableTableModel extends DefaultTableModel {
	String[] columnTitles;

	Object[][] dataEntries;

	int rowCount;

	public EditableTableModel(String[] columnTitles, Object[][] dataEntries) {
		this.columnTitles = columnTitles;
		this.dataEntries = dataEntries;
	}

	public EditableTableModel() {
		this(0, 0);
	}

	public EditableTableModel(int rowCount, int columnCount) {
		this(newVector(columnCount), rowCount);
	}

	public EditableTableModel(Vector<?> columnNames, int rowCount) {
		setDataVector(newVector(rowCount), columnNames);
	}

	public EditableTableModel(Object[] columnNames, int rowCount) {
		this(convertToVector(columnNames), rowCount);
	}

	public void setDataVector(Object[][] dataVector, Object[] columnIdentifiers) {
		setDataVector(convertToVector(dataVector), convertToVector(columnIdentifiers));
	}

	protected static Vector<Object> convertToVector(Object[] anArray) {
		if (anArray == null) {
			return null;
		}
		Vector<Object> v = new Vector<>(anArray.length);
		for (Object o : anArray) {
			v.addElement(o);
		}
		return v;
	}

	private static <E> Vector<E> nonNullVector(Vector<E> v) {
		return (v != null) ? v : new Vector<>();
	}

	public void setDataVector(Vector<? extends Vector> dataVector, Vector<?> columnIdentifiers) {
		this.dataVector = nonNullVector((Vector<Vector>) dataVector);
		this.columnIdentifiers = nonNullVector(columnIdentifiers);
		justifyRows(0, getRowCount());
		fireTableStructureChanged();
	}

	private static <E> Vector<E> newVector(int size) {
		Vector<E> v = new Vector<>(size);
		v.setSize(size);
		return v;
	}

	private void justifyRows(int from, int to) {
		dataVector.setSize(getRowCount());

		for (int i = from; i < to; i++) {
			if (dataVector.elementAt(i) == null) {
				dataVector.setElementAt(new Vector<>(), i);
			}
			dataVector.elementAt(i).setSize(getColumnCount());
		}
	}

	public int getRowCount() {
		return dataVector.size();
	}

	public int getColumnCount() {
		return columnIdentifiers.size();
	}

	public Object getValueAt(int row, int column) {
		@SuppressWarnings("unchecked")
		Vector<Object> rowVector = dataVector.elementAt(row);
		return rowVector.elementAt(column);
	}

	public String getColumnName(int column) {
		Object id = null;
		if (column < columnIdentifiers.size() && (column >= 0)) {
			id = columnIdentifiers.elementAt(column);
		}
		return (id == null) ? super.getColumnName(column) : id.toString();
	}

	public Class getColumnClass(int column) {
		return getValueAt(0, column).getClass();
	}

	public boolean isCellEditable(int row, int column) {
		return true;
	}

	public void setValueAt(Object aValue, int row, int column) {
		  @SuppressWarnings("unchecked")
	        Vector<Object> rowVector = dataVector.elementAt(row);
	        rowVector.setElementAt(aValue, column);
	        fireTableCellUpdated(row, column);
	}

}
