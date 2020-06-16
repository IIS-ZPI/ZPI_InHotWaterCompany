package application;

import application.foreignTransport.ImportCosts;
import application.foreignTransport.ImportCountry;
import application.product.ProductCategory;
import application.product.ProductInfo;
import db.DatabaseException;
import db.SQLiteDatabase;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class SQLTests {

    private final ProductInfo apple = new ProductInfo("apple", ProductCategory.GROCERIES, 5.0);
    private final ImportCountry poland = new ImportCountry("Poland", "97425", "PLN", new ImportCosts(2, 2, 2));
    private SQLiteDatabase sqLiteDatabase;

    @Before
    public void init() throws DatabaseException {
        sqLiteDatabase = new SQLiteDatabase("ExampleDatabase.db");
        sqLiteDatabase.initialize("src/test/java/application/ExampleDatabase.sql");
    }

    @Test
    public void fetchAllProductsShouldReturnListWithOneElement() throws DatabaseException {
        sqLiteDatabase.insertProduct(apple);
        List<ProductInfo> productInfoList = sqLiteDatabase.fetchAllProducts();

        assertThat(productInfoList.size(), equalTo(1));
    }

    @Test
    public void fetchAllImportCountriesReturnListWithOneElement() throws DatabaseException {
        sqLiteDatabase.insertImportCountry(poland);
        List<ImportCountry> productInfoList = sqLiteDatabase.fetchAllImportCountries();

        assertThat(productInfoList.size(), equalTo(1));
    }

    @Test
    public void fetchAllStatesReturnListWithOneElement() throws DatabaseException {
        List<State> productInfoList = sqLiteDatabase.fetchAllStates();

        assertThat(productInfoList.size(), equalTo(3));
    }

    @Test
    public void fetchAllCategoryNamesReturnListWithOneElement() throws DatabaseException {
        List<String> productInfoList = sqLiteDatabase.fetchAllCategoryNames();

        assertThat(productInfoList.size(), equalTo(6));
    }

    @Test
    public void updateLogisticCostShouldChangeLogisticCostsInSpecifiedState() throws DatabaseException {
        sqLiteDatabase.updateLogisticCost("Alabama", 5.55);
        List<State> productInfoList = sqLiteDatabase.fetchAllStates();

        assertThat(productInfoList.get(0).getLogisticCosts(), equalTo(5.55));
    }

    @Test
    public void updateImportCostShouldChangeImportCostsInSpecifiedState() throws DatabaseException {
        sqLiteDatabase.insertImportCountry(poland);
        sqLiteDatabase.updateImportCosts("Poland", new ImportCosts(9, 9, 9));
        List<ImportCountry> productInfoList = sqLiteDatabase.fetchAllImportCountries();

        ImportCosts importCosts = productInfoList.get(0).getImportCosts();
        assertThat(importCosts.getTransportFee(), equalTo(9d));
        assertThat(importCosts.getConsumablesImportTariff(), equalTo(9d));
        assertThat(importCosts.getOthersImportTariff(), equalTo(9d));

    }

    @Test
    public void fetchAllImportCountriesShouldReturnInsertedCountry() throws DatabaseException {
        sqLiteDatabase.insertImportCountry(poland);
        List<ImportCountry> productInfoList = sqLiteDatabase.fetchAllImportCountries();

        boolean polandIsPresent = false;
        for (ImportCountry importCountry : productInfoList) {
            if (importCountry.getName().equals("Poland"))
                polandIsPresent = true;
        }

        assertThat(polandIsPresent, equalTo(true));
    }

}
