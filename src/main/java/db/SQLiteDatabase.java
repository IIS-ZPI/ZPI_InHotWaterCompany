package db;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import application.ProductInfo;

public class SQLiteDatabase implements Database {
	private final Connection connection;
	
	/**
	 * Connects to exiting SQLite database specified by path.
	 * If database doesn't exists, creates a new database file and connects to it
	 * 
	 * @param path - path to database file
	 * @throws DatabaseException if something gone wrong
	 */
	public SQLiteDatabase(String path) throws DatabaseException {
		try {
			connection = DriverManager.getConnection("jdbc:sqlite:" + path);
			
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	@Override
	public void initialize(String pathToSql) throws DatabaseException {
		try {
			RandomAccessFile file = new RandomAccessFile(pathToSql, "r");
			byte[] buffer = new byte[(int) file.length()];
			
			file.readFully(buffer);
			file.close();
			
			Statement statement = connection.createStatement();
			statement.executeUpdate(new String(buffer));
			
		} catch (IOException | SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	@Override
	public void insertProduct(ProductInfo productInfo) throws DatabaseException {
		try {
			String sql = "INSERT INTO product(name, category, price) VALUES (?, ?, ?)";
			
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, productInfo.getProduct());
			preparedStatement.setInt(2, productInfo.getCategory().ordinal() + 1);
			preparedStatement.setDouble(3, productInfo.getWholesalePrice());
			preparedStatement.executeUpdate();
			
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	@Override
	public Connection getConnection() {
		return connection;
	}

	@Override
	public void close() throws DatabaseException {
		try {
			connection.close();
			
		} catch (SQLException e) {
			new DatabaseException(e.getMessage());
		}
	}
}
