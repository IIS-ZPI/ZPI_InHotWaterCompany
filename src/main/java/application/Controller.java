package application;

import application.foreignTransport.ImportCountry;
import application.product.ProductInfo;
import db.DatabaseException;
import db.SQLiteDatabase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import org.controlsfx.control.textfield.TextFields;
import utils.CSV;
import utils.CSVLoader;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static application.Calculation.*;

public class Controller implements Initializable {

    private static final String CLEAN_TEXTFIELD = "";
    private static final String BASE_CURRENCY = " USD";
    public static final String MONEY_LOSS_WAS_NOTED_IN = "Money loss was noted in ";
    private List<State> stateList = new ArrayList<>();
    private List<ProductInfo> productInfoList = new ArrayList<>();
    private List<String> productListAutoCompletion = new ArrayList<>();
    private List<String> stateListAutoCompletion = new ArrayList<>();
    private List<ImportCountry> importCountryList = new ArrayList<>();
    private List<String> countryListAutoCompletion = new ArrayList<>();
    @FXML
    private Text warning;
    @FXML
    private TextField searchProductTextField;
    @FXML
    private TextField searchStateTextField;
    @FXML
    private TextField priceTextField;
    @FXML
    private TextField wholesalePriceTextField;
    @FXML
    private TableView<DataInTable> tableView;
    @FXML
    private TableColumn<DataInTable, String> stateColumn;
    @FXML
    private TableColumn<DataInTable, String> marginColumn;
    @FXML
    private TableColumn<DataInTable, String> priceWithoutTaxColumn;
    @FXML
    private TableColumn<DataInTable, String> logisticCostColumn;
    @FXML
    private TextField searchCountryTextField;
    @FXML
    private CheckBox sendAbroadCheckBox;
    @FXML
    private TextField currencyTextField;
    @FXML
    private Text connectionText;

    @FXML
    public void initialize(URL location, ResourceBundle resources) {

        try {
            String path = "/tmp/tax_app.db";
            boolean databaseExists = new File(path).exists();
            SQLiteDatabase database = new SQLiteDatabase(path);

            if (!databaseExists) {
                database.initialize("src/main/resources/initial.sql");
                CSVLoader.loadProducts(new CSV("src/main/resources/products.csv"), database);
                CSVLoader.loadLogisticCosts(new CSV("src/main/resources/US_States_Common_Costs.csv"), database);
                CSVLoader.loadInternationalTransportationCosts(new CSV("src/main/resources/International Transportation Costs.csv"), database);
            }

            stateList.addAll(database.fetchAllStates());
            stateList.forEach(x -> stateListAutoCompletion.add(x.getUsaState()));

            productInfoList.addAll(database.fetchAllProducts());
            productInfoList.forEach(x -> productListAutoCompletion.add(x.getProduct()));
            importCountryList.addAll(database.fetchAllImportCountries());
            importCountryList.forEach(x -> countryListAutoCompletion.add(x.getName()));
        } catch (DatabaseException | IOException e) {
            e.printStackTrace();
        }
        TextFields.bindAutoCompletion(searchProductTextField, productListAutoCompletion);
        TextFields.bindAutoCompletion(searchStateTextField, stateListAutoCompletion);
        TextFields.bindAutoCompletion(searchCountryTextField, countryListAutoCompletion);

        stateColumn.setCellValueFactory(new PropertyValueFactory<>("state"));
        priceWithoutTaxColumn.setCellValueFactory(new PropertyValueFactory<>("priceWithoutTax"));
        marginColumn.setCellValueFactory(new PropertyValueFactory<>("margin"));
        logisticCostColumn.setCellValueFactory(new PropertyValueFactory<>("logisticCost"));

        searchProductTextField.textProperty().addListener((Observable, oldValue, newValue) -> {
            ProductInfo productInfo = Finder.findProduct(newValue, productInfoList);
            if (productInfo != null) {
                wholesalePriceTextField.setStyle("-fx-text-fill: black");
                wholesalePriceTextField.setText(String.valueOf(productInfo.getWholesalePrice()));
            } else {
                wholesalePriceTextField.setStyle("-fx-text-fill: red");
                wholesalePriceTextField.setText("Not found");
            }
        });

        searchCountryTextField.setDisable(true);
        currencyTextField.setText(BASE_CURRENCY);
        try {
            URL url = new URL("http://www.google.com");
            URLConnection connection = url.openConnection();
            connection.connect();
            connectionText.setText("Internet is connected");
            sendAbroadCheckBox.setDisable(false);
        } catch (IOException e) {
            connectionText.setText("Internet is not connected. Checking margin for other country are disabled.");
            sendAbroadCheckBox.setDisable(true);
        }
    }

    @FXML
    void onInputTextChangedDependOnProduct() {
        searchProductTextField.getText();
    }

    public void checkBoxesOnAction() {
        if (sendAbroadCheckBox.isSelected()) {
            searchStateTextField.setDisable(true);
            searchCountryTextField.setDisable(false);
            searchStateTextField.setText(CLEAN_TEXTFIELD);
        } else {
            searchStateTextField.setDisable(false);
            searchCountryTextField.setDisable(true);
            searchCountryTextField.setText(CLEAN_TEXTFIELD);
        }
    }

    public void onClickCheckButton() throws IOException {
        String priceString = priceTextField.getText();
        String stateString = searchStateTextField.getText();
        String productString = searchProductTextField.getText();
        String countryString = searchCountryTextField.getText();
        if (priceString.isEmpty() || productString.isEmpty() || (stateString.isEmpty() && countryString.isEmpty())) {
            warning.setText("Enter all data");
            clearAllOutputField();
        } else {
            if (validatePrice(priceString)) {
                double price = Double.parseDouble(priceString);

                ProductInfo productInfo = Finder.findProduct(productString, productInfoList);
                if (productInfo == null) {
                    warning.setText("Product not found");
                    clearAllOutputField();
                } else {
                    if (sendAbroadCheckBox.isSelected()) {
                        ImportCountry country = Finder.findCountry(countryString, importCountryList);
                        if (country == null) {
                            warning.setText("Country not found");
                            clearAllOutputField();
                        } else {
                            warning.setText(CLEAN_TEXTFIELD);
                            tableView.setItems(getDataForAllCountryList(country, importCountryList, productInfo, price));
                            customiseTable(marginColumn);
                        }
                    } else {
                        State state = Finder.findState(stateString, stateList);
                        if (state == null) {
                            warning.setText("State not found");
                            clearAllOutputField();
                        } else {
                            warning.setText(CLEAN_TEXTFIELD);
                            tableView.setItems(getDataForAllStateList(state, stateList, productInfo, price));
                            customiseTable(marginColumn);
                        }
                    }
                }
            } else {
                warning.setText("Incorrect price");
                clearAllOutputField();
            }
        }
    }

    public ObservableList<DataInTable> getDataForAllCountryList(ImportCountry country, List<ImportCountry> countryList, ProductInfo productInfo, double price) throws IOException {
        ObservableList<DataInTable> dataInTableObservableList = FXCollections.observableArrayList();
        int numberOfCountryWhereYouLostMoney = 0;
        double margin = calculateMarginInOtherCountry(price, productInfo, country);
        String marginString = formatPrice(margin).equals("-0") ? "0" : formatPrice(margin);
        if (Double.parseDouble(marginString) < 0) {
            numberOfCountryWhereYouLostMoney++;
        }
        double transportFee = country.getImportCosts().getTransportFee();
        dataInTableObservableList.add(new DataInTable(country.getName(), "-", marginString + " " + country.getCurrencyCode(), formatPrice(changeToActualCurrency(transportFee, country.getCurrencyCode())) + " " + country.getCurrencyCode()));
        for (ImportCountry c : countryList) {
            if (!c.equals(country)) {
                margin = calculateMarginInOtherCountry(price, productInfo, c);
                marginString = formatPrice(margin).equals("-0") ? "0" : formatPrice(margin);
                if (Double.parseDouble(marginString) < 0) {
                    numberOfCountryWhereYouLostMoney++;
                }
                transportFee = c.getImportCosts().getTransportFee();
                dataInTableObservableList.add(new DataInTable(c.getName(), "-", marginString + " " + c.getCurrencyCode(), formatPrice(changeToActualCurrency(transportFee, c.getCurrencyCode())) + " " + c.getCurrencyCode()));
            }
        }
        if (numberOfCountryWhereYouLostMoney > 0) {
            if (numberOfCountryWhereYouLostMoney == 1)
                warning.setText(MONEY_LOSS_WAS_NOTED_IN + numberOfCountryWhereYouLostMoney + " country");
            else
                warning.setText(MONEY_LOSS_WAS_NOTED_IN + numberOfCountryWhereYouLostMoney + " countries");
        }
        return dataInTableObservableList;
    }

    public ObservableList<DataInTable> getDataForAllStateList(State state, List<State> stateList, ProductInfo productInfo, double price) {
        ObservableList<DataInTable> dataInTableObservableList = FXCollections.observableArrayList();
        int numberOfStateWhereYouLostMoney = 0;

        double priceWithoutTax = calculateWithoutTax(price, productInfo, state);
        double margin = calculateMarginInState(priceWithoutTax, productInfo, state.getLogisticCosts());
        String marginString = formatPrice(margin).equals("-0") ? "0" : formatPrice(margin);
        if (Double.parseDouble(marginString) < 0) {
            numberOfStateWhereYouLostMoney++;
        }
        dataInTableObservableList.add(new DataInTable(state.getUsaState(), formatPrice(priceWithoutTax) + BASE_CURRENCY, marginString + BASE_CURRENCY, state.getLogisticCosts() + BASE_CURRENCY));

        for (State s : stateList) {
            if (!s.equals(state)) {
                priceWithoutTax = calculateWithoutTax(price, productInfo, s);
                margin = calculateMarginInState(priceWithoutTax, productInfo, s.getLogisticCosts());
                marginString = formatPrice(margin).equals("-0") ? "0" : formatPrice(margin);
                if (Double.parseDouble(marginString) < 0) {
                    numberOfStateWhereYouLostMoney++;
                }
                dataInTableObservableList.add(new DataInTable(s.getUsaState(), formatPrice(priceWithoutTax) + BASE_CURRENCY, marginString + BASE_CURRENCY, s.getLogisticCosts() + BASE_CURRENCY));
            }
        }
        if (numberOfStateWhereYouLostMoney > 0) {
            if (numberOfStateWhereYouLostMoney == 1)
                warning.setText(MONEY_LOSS_WAS_NOTED_IN + numberOfStateWhereYouLostMoney + " state");
            else
                warning.setText(MONEY_LOSS_WAS_NOTED_IN + numberOfStateWhereYouLostMoney + " states");
        }
        return dataInTableObservableList;
    }

    void clearAllOutputField() {
        tableView.setItems(null);
    }

    void customiseTable(TableColumn<DataInTable, String> col) {
        col.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? CLEAN_TEXTFIELD : getItem());
                TableRow<DataInTable> currentRow = getTableRow();

                if (!isEmpty()) {
                    if (item.charAt(0) == '-') {
                        currentRow.setStyle("-fx-background-color:lightcoral");
                    } else if (item.length() == 1 && item.charAt(0) == '0') {
                        currentRow.setStyle("-fx-background-color:yellow");
                    } else {
                        currentRow.setStyle("-fx-background-color:lightgreen");
                    }
                }
            }
        });
    }

    String formatPrice(double price) {
        return new DecimalFormat("##.##").format(price).replaceAll(",", ".");
    }

    boolean validatePrice(String price) {
        if (price.isEmpty())
            return false;

        if (price.equals("."))
            return false;

        boolean isDotted = false;
        int numberAfterDot = 0;
        for (int i = 0; i < price.length(); i++) {
            if (!Character.isDigit(price.charAt(i))) {
                if (price.charAt(i) == '.') {
                    if (isDotted)
                        return false;
                    else
                        isDotted = true;
                } else
                    return false;
            }
            if (isDotted && Character.isDigit(price.charAt(i)))
                numberAfterDot++;

            if (numberAfterDot > 2)
                return false;
        }
        return true;
    }
}
