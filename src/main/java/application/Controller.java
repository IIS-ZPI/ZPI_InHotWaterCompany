package application;

import application.foreignTransport.ImportCountry;
import application.product.ProductInfo;
import db.DatabaseException;
import db.SQLiteDatabase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable {
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
    public void initialize(URL location, ResourceBundle resources) {

        try {
            String path = "src/main/resources/tax_app.db";
            boolean databaseExists = new File(path).exists();
            SQLiteDatabase database = new SQLiteDatabase(path);
            File file = new File(path);
            if(databaseExists)
                file.delete();

            if (!databaseExists) {
                database.initialize("src/main/resources/initial.sql");
                CSVLoader.loadProducts(new CSV("src/main/resources/products.csv"), database);
                CSVLoader.loadLogisticCosts(new CSV("src/main/resources/US_States_Common_Costs.csv"), database);
                CSVLoader.loadInternationalTransportationCosts(new CSV("src/main/resources/International Transportation Costs.csv"), database);
            }

            stateList.addAll(database.fetchAllStates());
            stateList.forEach(x -> stateListAutoCompletion.add(x.getState()));

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
            ProductInfo productInfo = findProduct(newValue);
            if (productInfo != null) {
                wholesalePriceTextField.setStyle("-fx-text-fill: black");
                wholesalePriceTextField.setText(String.valueOf(productInfo.getWholesalePrice()));
            } else {
                wholesalePriceTextField.setStyle("-fx-text-fill: red");
                wholesalePriceTextField.setText("Not found");
            }
        });
        searchCountryTextField.setEditable(false);
    }

    @FXML
    void onInputTextChangedDependOnProduct(ActionEvent event) {
        searchProductTextField.getText();
    }

    public void checkBoxesOnAction(ActionEvent event) {
        if (sendAbroadCheckBox.isSelected()) {
            searchStateTextField.setEditable(false);
            searchCountryTextField.setEditable(true);
            searchStateTextField.setText("");
        } else {
            searchStateTextField.setEditable(true);
            searchCountryTextField.setEditable(false);
            searchCountryTextField.setText("");
        }
    }

    public void onClickCheckButton() {
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

                ProductInfo productInfo = findProduct(productString);
                if (productInfo == null) {
                    warning.setText("Product not found");
                    clearAllOutputField();
                } else {
                    if (sendAbroadCheckBox.isSelected()) {
                        ImportCountry country = findCountry(countryString);
                        if (country == null) {
                            warning.setText("Country not found");
                            clearAllOutputField();
                        } else {
                            warning.setText("");
                            tableView.setItems(getDataForAllCountryList(country, importCountryList, productInfo, price));
                            customiseTable(marginColumn);
                        }
                    } else {
                        State state = findState(stateString);
                        if (state == null) {
                            warning.setText("State not found");
                            clearAllOutputField();
                        } else {
                            warning.setText("");
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

    double calculateWithoutTax(double price, ProductInfo productInfo, State state) {
        return price / ((100.0 + getTaxFromCategory(productInfo, state)) / 100);
    }

    double calculateMarginInState(double priceWithoutTax, ProductInfo productInfo, double logisticCosts) {
        return priceWithoutTax - productInfo.getWholesalePrice() - logisticCosts;
    }

    double calculateMarginInOtherCountry(double price, ProductInfo productInfo, ImportCountry importCountry) {
        double finalMargin = 0;
        double transportFee = changeToActualCurrency(importCountry.getImportCosts().getTransportFee(), importCountry.getCurrencyCode());
        if (isConsumables(productInfo)) {
            finalMargin = price - productInfo.getWholesalePrice() - (productInfo.getWholesalePrice() * (importCountry.getImportCosts().getConsumablesImportTariff() / 100))
                    - transportFee;
        } else {
            finalMargin = price - productInfo.getWholesalePrice() - (productInfo.getWholesalePrice() * (importCountry.getImportCosts().getOthersImportTariff() / 100))
                    - transportFee;
        }
        return finalMargin;
    }

    double changeToActualCurrency(double price, String currencyCode) {// TO DO
        return price;
    }

    public boolean isConsumables(ProductInfo productInfo) {

        switch (productInfo.getCategory()) {
            case GROCERIES:
            case PREPARED_FOOD:
            case PRESCRIPTION_DRUG:
            case NONPRESCRIPTION_DRUG:
                return true;
        }
        return false;
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

    State findState(String state) {
        for (State s : stateList) {
            if (s.getState().toLowerCase().equals(state.toLowerCase()))
                return s;
        }
        return null;
    }

    ProductInfo findProduct(String product) {
        for (ProductInfo productInfo : productInfoList) {
            if (productInfo.getProduct().toLowerCase().equals(product.toLowerCase()))
                return productInfo;
        }
        return null;
    }

    ImportCountry findCountry(String country) {
        for (ImportCountry c : importCountryList) {
            if (c.getName().equalsIgnoreCase(country))
                return c;
        }
        return null;
    }

    double getTaxFromCategory(ProductInfo productInfo, State state) {
        double category = -1;
        switch (productInfo.getCategory()) {
            case GROCERIES:
                category = state.getCategory().getGroceries();
                break;
            case PREPARED_FOOD:
                category = state.getCategory().getPreparedFood();
                break;
            case PRESCRIPTION_DRUG:
                category = state.getCategory().getPrescriptionDrug();
                break;
            case NONPRESCRIPTION_DRUG:
                category = state.getCategory().getNonPrescriptionDrug();
                break;
            case CLOTHING:
                category = state.getCategory().getClothing();
                break;
            case INTANGIBLES:
                category = state.getCategory().getIntangibles();
                break;
            default:
                break;
        }
        return category;
    }

    private ObservableList<DataInTable> getDataForAllCountryList(ImportCountry country, List<ImportCountry> countryList, ProductInfo productInfo, double price) {
        ObservableList<DataInTable> dataInTableObservableList = FXCollections.observableArrayList();
        int numberOfCountryWhereYouLostMoney = 0;
        double margin = calculateMarginInOtherCountry(price, productInfo, country);
        String marginString = formatPrice(margin).equals("-0") ? "0" : formatPrice(margin);
        if (Double.parseDouble(marginString) < 0) {
            numberOfCountryWhereYouLostMoney++;
        }
        double transportFee = country.getImportCosts().getTransportFee();
        dataInTableObservableList.add(new DataInTable(country.getName(), "-", marginString, String.valueOf(changeToActualCurrency(transportFee, country.getCurrencyCode()))));
        for (ImportCountry c : countryList) {
            if (!c.equals(country)) {
                margin = calculateMarginInOtherCountry(price, productInfo, c);
                marginString = formatPrice(margin).equals("-0") ? "0" : formatPrice(margin);
                if (Double.parseDouble(marginString) < 0) {
                    numberOfCountryWhereYouLostMoney++;
                }
                transportFee = c.getImportCosts().getTransportFee();
                dataInTableObservableList.add(new DataInTable(c.getName(), "-", marginString, String.valueOf(changeToActualCurrency(transportFee, c.getCurrencyCode()))));
            }
        }
        if (numberOfCountryWhereYouLostMoney > 0) {
            if (numberOfCountryWhereYouLostMoney == 1)
                warning.setText("Money loss was noted in " + numberOfCountryWhereYouLostMoney + " country");
            else
                warning.setText("Money loss was noted in " + numberOfCountryWhereYouLostMoney + " countries");
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
        dataInTableObservableList.add(new DataInTable(state.getState(), formatPrice(priceWithoutTax), marginString, String.valueOf(state.getLogisticCosts())));

        for (State s : stateList) {
            if (!s.equals(state)) {
                priceWithoutTax = calculateWithoutTax(price, productInfo, s);
                margin = calculateMarginInState(priceWithoutTax, productInfo, s.getLogisticCosts());
                marginString = formatPrice(margin).equals("-0") ? "0" : formatPrice(margin);
                if (Double.parseDouble(marginString) < 0) {
                    numberOfStateWhereYouLostMoney++;
                }
                dataInTableObservableList.add(new DataInTable(s.getState(), formatPrice(priceWithoutTax), marginString, String.valueOf(s.getLogisticCosts())));
            }
        }
        if (numberOfStateWhereYouLostMoney > 0) {
            if (numberOfStateWhereYouLostMoney == 1)
                warning.setText("Money loss was noted in " + numberOfStateWhereYouLostMoney + " state");
            else
                warning.setText("Money loss was noted in " + numberOfStateWhereYouLostMoney + " states");
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
                setText(empty ? "" : getItem());
                TableRow<DataInTable> currentRow = getTableRow();

                if (!isEmpty()) {
                    if (item.charAt(0) == '-') {
                        currentRow.setStyle("-fx-background-color:lightcoral");
                    } else if (item.length() == 1 && item.charAt(0) == '0') {
                        currentRow.setStyle("-fx-background-color:yellow");
                    } else {
                        currentRow.setStyle("-fx-background-color:lightgreen");
                        //currentRow.setStyle("-fx-text-fill: black");
                    }
                }
            }
        });
    }

    String formatPrice(double price) {
        return new DecimalFormat("##.##").format(price).replaceAll(",", ".");
    }
}
