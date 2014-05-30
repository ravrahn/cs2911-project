import java.util.Arrays;

/**
 * A Matrix Class
 * 
 * @author Gabriel
 */
public class Matrix {
	/**
	 * Constructs an empty matrix of given dimensions
	 * 
	 * @param rows
	 *            The number of rows
	 * @param columns
	 *            The number of columns
	 */
	public Matrix(int rows, int columns) {
		this.rows = rows;
		this.columns = columns;

		this.data = new double[rows][columns];
	}

	/**
	 * Constructs a matrix from an array of given dimensions
	 * 
	 * @param data
	 *            The data
	 * @param rows
	 *            The number of rows
	 * @param columns
	 *            The number of columns
	 */
	public Matrix(double[][] data, int rows, int columns) {
		this.rows = rows;
		this.columns = columns;

		this.data = new double[rows][columns];
		for (int i = 0; i < rows; i++) {
			this.data[i] = Arrays.copyOf(data[i], columns);
		}
	}

	/**
	 * Constructs a copy of a matrix
	 * 
	 * @param m
	 *            The matrix
	 */
	public Matrix(Matrix m) {
		if (m == null) {
			throw new IllegalArgumentException();
		}
		rows = m.getRows();
		columns = m.getColumns();

		data = new double[rows][columns];
		double[][] tempData = m.getData();
		for (int i = 0; i < rows; i++) {
			data[i] = Arrays.copyOf(tempData[i], columns);
		}
	}

	/**
	 * Adds a matrix to this matrix
	 * 
	 * @param m
	 *            The other matrix to be added
	 * @return The result of the addition
	 */
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

	/**
	 * Subtracts a matrix from this matrix
	 * 
	 * @param m
	 *            The other matrix to be subtracted
	 * @return The result of the subtraction
	 */
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

	/**
	 * Multiplies this matrix with another matrix
	 * 
	 * @param m
	 *            The other matrix to be multiplied
	 * @return The result of the multiplication
	 */
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
		if (!(o instanceof Matrix))
			return false;
		Matrix m = (Matrix) o;
		if (rows != m.getRows() || columns != m.getColumns()) {
			return false;
		}

		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				if (data[i][j] != m.getData(i, j))
					return false;
			}
		}

		return true;
	}

	/**
	 * Getter for the number of rows
	 * 
	 * @return the number of rows
	 */
	public int getRows() {
		return rows;
	}

	/**
	 * Getter for the number of columns
	 * 
	 * @return the number of columns
	 */
	public int getColumns() {
		return columns;
	}

	/**
	 * Getter for the data array
	 * 
	 * @return the data array
	 */
	public double[][] getData() {
		return data;
	}

	/**
	 * Getter for an element of the matrix
	 * 
	 * @param i
	 *            The row
	 * @param j
	 *            The column
	 * @return the element at the i'th row and j'th column of the matrix
	 */
	public double getData(int i, int j) {
		return data[i][j];
	}

	/**
	 * Setter for an element of the matrix
	 * 
	 * @param value
	 *            The new value
	 * @param i
	 *            The row
	 * @param j
	 *            The column
	 */
	public void setData(double value, int i, int j) {
		data[i][j] = value;
	}

	protected int rows;
	protected int columns;
	protected double[][] data;
}
