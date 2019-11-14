package ufpr.gres.rnsgaii.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.apache.commons.io.Charsets;

import com.google.common.base.Preconditions;
import com.google.common.io.Files;

public class InstanceReaderUtils {

	protected FileReader reader;

	protected BufferedReader buffer;

	protected File filename;

	protected String fileContent;

	protected String[] lines;

	protected String separator = " ";

	protected int index = 0;

	/**
	 * Constructor
	 * 
	 * @param filename the file to read from
	 * @throws IOException if an I/O error occurs
	 */
	public InstanceReaderUtils(File filename) throws IOException {

		Preconditions.checkNotNull(filename, "The filename cannot be null");
		Preconditions.checkArgument(filename.exists(), "The file does not exist");
		Preconditions.checkArgument(filename.isFile(), "The filename cannot be a directory");

		// Read the file
		this.fileContent = Files.toString(filename, Charsets.UTF_8);

		// Separete the file into lines
		this.lines = this.fileContent.split("\n");
	}

	/**
	 * Return the number of lines in the read file;
	 * 
	 * @return the number of lines
	 */
	public int getNumberOfLines() {
		return this.lines.length;
	}

	/**
	 * Define the separator used when split the line
	 * 
	 * @param separator the character used to split the line
	 */
	public void setSeparator(String separator) {
		this.separator = separator;
	}

	/**
	 * Return the separator used for splitting a string
	 * 
	 * @return the separator
	 */
	public String getSeparator() {
		return this.separator;
	}

	/**
	 * Read an integer number
	 * 
	 * @return an integer value
	 */
	public int readIntegerValue() {
		return readIntegerArray()[0];
	}

	/**
	 * Read an integer array
	 * 
	 * @return an integer array
	 */
	public int[] readIntegerArray() {
		return readIntegerMatrix(1)[0];
	}

	/**
	 * Read an integer matrix
	 * 
	 * @param lines the number of lines that will be read
	 * @return an integer matrix
	 */
	public int[][] readIntegerMatrix(int lines) {
		return toIntegerMatrix(readLines(lines));
	}

	/**
	 * Read a double number
	 * 
	 * @return a double value
	 */
	public double readDoubleValue() {
		return readDoubleArray()[0];
	}

	/**
	 * Read a double matrix
	 * 
	 * @param lines the number of lines that will be read
	 * @return a double matrix
	 */
	public double[][] readDoubleMatrix(int lines) {
		return toDoubleMatrix(readLines(lines));
	}

	/**
	 * Read a double array
	 * 
	 * @return a double array
	 */
	public double[] readDoubleArray() {
		return readDoubleMatrix(1)[0];
	}

	/**
	 * Read a line of the file
	 * 
	 * @return A line read
	 */
	public String readLine() {

		if (index >= getNumberOfLines()) {
			return null;
		}

		return this.lines[index++].trim();
	}

	/**
	 * Read a string matrix
	 * 
	 * @param lines the number of lines read
	 * @return a string matrix
	 */
	public String[][] readLines(int lines) {

		Preconditions.checkArgument(lines >= 1, "The lines should be >= 1");

		String[][] result = new String[lines][];

		for (int i = 0; i < lines; i++) {

			String line = readLine();

			if (line != null) {
				result[i] = line.split(separator);
			} else {
				result[i] = new String[] {};
			}
		}

		// Verify the number of columns

		int columns = -1;

		for (int i = 0; i < lines; i++) {

			if (columns == -1) {
				columns = result[i].length;
			}

			if (result[i].length != columns) {
				throw new IllegalStateException("There is line with different number os columns");
			}
		}

		return result;
	}
	
	public static double[][] toDoubleMatrix(String[][] matrix) {

		Preconditions.checkNotNull(matrix, "The matrix cannot be null");
		
		double[][] result = new double[matrix.length][];

		for (int i = 0; i < matrix.length; i++) {
			
			result[i] = new double[matrix[i].length];

			for (int j = 0; j < matrix[i].length; j++) {				

				if (matrix[i][j] == null || matrix[i][j].isEmpty()) {
					result[i][j] = 0.0;
				} else {
					result[i][j] = Double.valueOf(matrix[i][j].trim());
				}
			}
		}

		return result;
	}
	
	/**
	 * Convert from String[][] to double[][]
	 * 
	 * @param matrix the matrix that should be converted
	 * @return a double matrix
	 */
	public static int[][] toIntegerMatrix(String[][] matrix) {

		Preconditions.checkNotNull(matrix, "The matrix cannot be null");
		
		int[][] result = new int[matrix.length][];

		for (int i = 0; i < matrix.length; i++) {
			
			result[i] = new int[matrix[i].length];

			for (int j = 0; j < matrix[i].length; j++) {				

				if (matrix[i][j] == null || matrix[i][j].isEmpty()) {
					result[i][j] = 0;
				} else {
					result[i][j] = Integer.valueOf(matrix[i][j].trim());
				}
			}
		}

		return result;
	}
}
