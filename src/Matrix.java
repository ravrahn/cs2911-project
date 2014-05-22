import java.util.Arrays;

/**
 * A Matrix Class
 * 
 * @author Gabriel
 */
public class Matrix {
	public Matrix(int rows, int columns) {
		this.rows = rows;
		this.columns = columns;
		
		this.data = new double[rows][columns];
	}
	
	public Matrix(double[][] data, int rows, int columns) {
		this.rows = rows;
		this.columns = columns;
		
		this.data = new double[rows][columns];
		for (int i = 0; i < rows; i++) {
			this.data[i] = Arrays.copyOf(data[i], columns);
		}
	}
	
	public Matrix(Matrix m) {
		rows = m.getRows();
		columns = m.getColumns();
		
		data = new double[rows][columns];
		double[][] tempData = m.getData();
		for (int i = 0; i < rows; i++) {
			data[i] = Arrays.copyOf(tempData[i], columns);
		}
	}
	
	public Matrix add(Matrix m) {
		if (m == null) {
			return null;
		}
		if (rows != m.getRows() || columns != m.getColumns()) {
			throw new IllegalArgumentException("Matrix of different dimensions");
		}

		Matrix newMatrix = new Matrix(rows, columns);
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				newMatrix.setData(data[i][j] + m.getData(i, j), i, j);
			}
		}
		
		return newMatrix;
	}
	
	public Matrix subtract(Matrix m) {
		if (m == null) {
			return null;
		}
		if (rows != m.getRows() || columns != m.getColumns()) {
			throw new IllegalArgumentException("Matrix of different dimensions");
		}

		Matrix newMatrix = new Matrix(rows, columns);
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				newMatrix.setData(data[i][j] - m.getData(i, j), i, j);
			}
		}
		
		return newMatrix;
	}
	
	public Matrix multiply(Matrix m) {
		if (m == null) {
			return null;
		}
		if (columns != m.getRows()) {
			throw new IllegalArgumentException("Matrices cannot be multiplied");
		}
		
		int newRows = rows;
		int newColumns = m.getColumns();
		int numSumElements = columns;
		Matrix newMatrix = new Matrix(newRows, newColumns);
		for (int i = 0; i < newRows; i++) {
			for (int j = 0; j < newColumns; j++) {
				double sum = 0;
				for (int k = 0; k < numSumElements; k++) {
					sum += data[i][k] * m.getData(k, j);
				}
				newMatrix.setData(sum, i, j);
			}
		}
		
		return newMatrix;
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Matrix)) return false;
		Matrix m = (Matrix) o;
		if (rows != m.getRows() || columns != m.getColumns()) {
			return false;
		}
		
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				if (data[i][j] != m.getData(i, j)) return false;
			}
		}
		
		return true;
	}
	
	public int getRows() {
		return rows;
	}
	
	public int getColumns() {
		return columns;
	}
	
	public double[][] getData() {
		return data;
	}
	
	public double getData(int i, int j) {
		return data[i][j];
	}
	
	public void setData(double value, int i, int j) {
		data[i][j] = value;
	}
	
	protected int rows;
	protected int columns;
	protected double[][] data;
}
