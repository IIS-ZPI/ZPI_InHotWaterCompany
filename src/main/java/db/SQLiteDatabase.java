package db;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import application.CategoryTax;
import application.ProductCategory;
import application.ProductInfo;
import application.State;

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
		try (RandomAccessFile file = new RandomAccessFile(pathToSql, "r");
			 Statement statement = connection.createStatement()) {
			
			byte[] buffer = new byte[(int) file.length()];
			file.readFully(buffer);
			
			statement.executeUpdate(new String(buffer));
			
		} catch (IOException | SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	@Override
	public void insertProduct(ProductInfo productInfo) throws DatabaseException {
		String sql = "INSERT INTO product(name, category, price) VALUES (?, ?, ?)";
			
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setString(1, productInfo.getProduct());
			preparedStatement.setInt(2, productInfo.getCategory().ordinal() + 1);
			preparedStatement.setDouble(3, productInfo.getWholesalePrice());
			preparedStatement.executeUpdate();
			
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	@Override
	public List<State> fetchAllStates() throws DatabaseException {
		String sql = "SELECT name, base_tax, groceries_tax, prepared_food_tax, prescription_drug_tax, nonprescription_drug_tax, clothing_tax, intangibles_tax FROM state";
		List<State> list = new ArrayList<State>();
		
		try (Statement statement = connection.createStatement();
			 ResultSet resultSet = statement.executeQuery(sql)) {
			
			while (resultSet.next()) {
				String name = resultSet.getString(1);
				double baseTax = resultSet.getDouble(2);
				CategoryTax categoryTax = new CategoryTax(resultSet.getDouble(3),
														  resultSet.getDouble(4),
														  resultSet.getDouble(5),
														  resultSet.getDouble(6),
														  resultSet.getDouble(7),
														  resultSet.getDouble(8));
				
				list.add(new State(name, baseTax, categoryTax));
			}
			
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		
		return list;
	}
	
	@Override
	public List<ProductInfo> fetchAllProducts() throws DatabaseException {
		String sql = "SELECT name, category, price FROM product";
		List<ProductInfo> list = new ArrayList<ProductInfo>();
		
		try (Statement statement = connection.createStatement();
			 ResultSet resultSet = statement.executeQuery(sql)) {
			
			while (resultSet.next()) {
				String name = resultSet.getString(1);
				ProductCategory category = ProductCategory.values()[resultSet.getInt(2) - 1];
				double price = resultSet.getDouble(3);
				
				list.add(new ProductInfo(name, category, price));
			}
			
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		
		return list;
	}
	
	@Override
	public List<String> fetchAllCategoryNames() throws DatabaseException {
		String sql = "SELECT name FROM category";
		List<String> list = new ArrayList<String>();
		
		try (Statement statement = connection.createStatement();
			 ResultSet resultSet = statement.executeQuery(sql)) {
			
			while (resultSet.next()) {
				list.add(resultSet.getString(1));
			}
			
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		
		return list;
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
			throw new DatabaseException(e.getMessage());
		}
	}
}
