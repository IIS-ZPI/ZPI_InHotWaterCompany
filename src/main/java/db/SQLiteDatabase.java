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

import application.product.CategoryTax;
import application.foreignTransport.ImportCosts;
import application.foreignTransport.ImportCountry;
import application.product.ProductCategory;
import application.product.ProductInfo;
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
		String sql = "SELECT "
		           +     "state.name, "
		           +     "base_tax, "
		           +     "groceries_tax, "
		           +     "prepared_food_tax, "
		           +     "prescription_drug_tax, "
		           +     "nonprescription_drug_tax, "
		           +     "clothing_tax, "
		           +     "intangibles_tax, "
		           +     "transport_fee "
		           + "FROM state_tax "
		           + "JOIN state ON state_tax.state = state.id "
		           + "JOIN logistic_cost ON logistic_cost.state = state.id";
		
		List<State> list = new ArrayList<>();
		
		try (Statement statement = connection.createStatement();
			 ResultSet resultSet = statement.executeQuery(sql)) {
			
			while (resultSet.next()) {
				String name = resultSet.getString(1);
				double baseTax = resultSet.getDouble(2);
				CategoryTax categoryTax = new CategoryTax((resultSet.getDouble(3) < 0) ? baseTax : resultSet.getDouble(3),
				                                          (resultSet.getDouble(4) < 0) ? baseTax : resultSet.getDouble(4),
				                                          (resultSet.getDouble(5) < 0) ? baseTax : resultSet.getDouble(5),
				                                          (resultSet.getDouble(6) < 0) ? baseTax : resultSet.getDouble(6),
				                                          (resultSet.getDouble(7) < 0) ? baseTax : resultSet.getDouble(7),
				                                          (resultSet.getDouble(8) < 0) ? baseTax : resultSet.getDouble(8));
				
				double logisticCosts = resultSet.getDouble(9);
				
				list.add(new State(name, baseTax, categoryTax, logisticCosts));
			}
			
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		
		return list;
	}
	
	@Override
	public List<ProductInfo> fetchAllProducts() throws DatabaseException {
		String sql = "SELECT name, category, price FROM product";
		List<ProductInfo> list = new ArrayList<>();
		
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
		List<String> list = new ArrayList<>();
		
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
	public void updateLogisticCost(String stateName, double logisticCost) throws DatabaseException {
		String sql = "UPDATE logistic_cost "
		           + "SET transport_fee = ? "
		           + "WHERE state = ( "
		           +     "SELECT id FROM state WHERE name = ? "
		           + ")";
		
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setDouble(1, logisticCost);
			preparedStatement.setString(2, stateName);
			preparedStatement.executeUpdate();
			
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
	}
	
	@Override
	public void insertImportCountry(ImportCountry importCountry) throws DatabaseException {
		String sql = "INSERT INTO import_country(name, code, currency) VALUES (?, ?, ?)";
		
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setString(1, importCountry.getName());
			preparedStatement.setString(2, importCountry.getCode());
			preparedStatement.setString(3, importCountry.getCurrencyCode());
			preparedStatement.executeUpdate();
			
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		
		sql = "INSERT INTO import_cost(import_country, transport_fee, consumables_import_tariff, others_import_tariff) VALUES "
		    +     "(( SELECT id FROM import_country WHERE code = ? ), ?, ?, ?)";
		
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setString(1, importCountry.getCode());
			preparedStatement.setDouble(2, importCountry.getImportCosts().getTransportFee());
			preparedStatement.setDouble(3, importCountry.getImportCosts().getConsumablesImportTariff());
			preparedStatement.setDouble(4, importCountry.getImportCosts().getOthersImportTariff());
			preparedStatement.executeUpdate();
			
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
	}
	
	@Override
	public void updateImportCosts(String countryName, ImportCosts importCosts) throws DatabaseException {
		String sql = "UPDATE import_cost "
		           + "SET "
		           +     "transport_fee = ?, "
		           +     "consumables_import_tariff = ?, "
		           +     "others_import_tariff = ? "
		           + "WHERE import_country = ( "
		           +     "SELECT id FROM import_country WHERE name = ? "
		           + ")";
		
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setDouble(1, importCosts.getTransportFee());
			preparedStatement.setDouble(2, importCosts.getConsumablesImportTariff());
			preparedStatement.setDouble(3, importCosts.getOthersImportTariff());
			preparedStatement.setString(4, countryName);
			preparedStatement.executeUpdate();
			
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
	}
	
	@Override
	public List<ImportCountry> fetchAllImportCountries() throws DatabaseException {
		String sql = "SELECT "
		           +     "name, "
		           +     "code, "
		           +     "currency, "
		           +     "transport_fee, "
		           +     "consumables_import_tariff, "
		           +     "others_import_tariff "
		           + "FROM import_country "
		           + "JOIN import_cost on import_cost.import_country = import_country.id";
		
		List<ImportCountry> list = new ArrayList<>();
		
		try (Statement statement = connection.createStatement();
			 ResultSet resultSet = statement.executeQuery(sql)) {
			
			while (resultSet.next()) {
				String name = resultSet.getString(1);
				String code = resultSet.getString(2);
				String currencyCode = resultSet.getString(3);
				double transportFee = resultSet.getDouble(4);
				double consumablesImportTariff = resultSet.getDouble(5);
				double othersImportTariff = resultSet.getDouble(6);
				
				ImportCosts importCosts = new ImportCosts(transportFee, consumablesImportTariff, othersImportTariff);
				list.add(new ImportCountry(name, code, currencyCode, importCosts));
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