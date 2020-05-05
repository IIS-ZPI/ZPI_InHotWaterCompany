package db;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import application.ProductInfo;

public class SQLiteDatabase implements Database {
	private final Connection connection;
	
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
		throw new DatabaseException("Not implemented yet");
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
