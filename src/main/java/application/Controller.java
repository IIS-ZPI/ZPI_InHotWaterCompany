package application;

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
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    private List<State> stateList = new ArrayList<>();
    private List<ProductInfo> productInfoList = new ArrayList<>();
    private List<String> productListAutoCompletion = new ArrayList<>();
    private List<String> stateListAutoCompletion = new ArrayList<>();

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
    public void initialize(URL location, ResourceBundle resources) {
        try {
            String path = "/tmp/tax_app.db";    // change to relative, not platform dependent
            boolean databaseExists = new File(path).exists();
            SQLiteDatabase database = new SQLiteDatabase(path);

            if (!databaseExists) {
                database.initialize("src/main/resources/initial.sql");
                CSVLoader.load(new CSV("src/test/java/application/products.csv"), database);
            }

            stateList.addAll(database.fetchAllStates());
            stateList.forEach(x -> stateListAutoCompletion.add(x.getState()));

            productInfoList.addAll(database.fetchAllProducts());
            productInfoList.forEach(x -> productListAutoCompletion.add(x.getProduct()));
        } catch (DatabaseException | IOException e) {
            e.printStackTrace();
        }

        TextFields.bindAutoCompletion(searchProductTextField, productListAutoCompletion);
        TextFields.bindAutoCompletion(searchStateTextField, stateListAutoCompletion);

        stateColumn.setCellValueFactory(new PropertyValueFactory<>("state"));
        priceWithoutTaxColumn.setCellValueFactory(new PropertyValueFactory<>("priceWithoutTax"));
        marginColumn.setCellValueFactory(new PropertyValueFactory<>("margin"));
        logisticCostColumn.setCellValueFactory(new PropertyValueFactory<>("logisticCost"));
    }

    public void onClickCheckButton() {
        String priceString = priceTextField.getText();
        String stateString = searchStateTextField.getText();
        String productString = searchProductTextField.getText();
        if (priceString.isEmpty() || stateString.isEmpty() || productString.isEmpty()) {
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
                    State state = findState(stateString);
                    if (state == null) {
                        warning.setText("State not found");
                        clearAllOutputField();
                    } else {
                        warning.setText("");
                        tableView.setItems(getDataForAllStateList(state, stateList, productInfo, price));
                        customiseTable(state, stateColumn);
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

    double calculateMargin(double priceWithoutTax, ProductInfo productInfo, double logisticCosts) {
        return priceWithoutTax - productInfo.getWholesalePrice() - logisticCosts;
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

    public void onClickCheckProductWholesalePrice() {
        String productString = searchProductTextField.getText();
        ProductInfo productInfo = findProduct(productString);
        if (productInfo != null) {
            wholesalePriceTextField.setStyle("-fx-text-fill: black");
            wholesalePriceTextField.setText(String.valueOf(productInfo.getWholesalePrice()));
        } else {
            wholesalePriceTextField.setStyle("-fx-text-fill: red");
            wholesalePriceTextField.setText("Not found");
        }
    }

    public ObservableList<DataInTable> getDataForAllStateList(State state, List<State> stateList, ProductInfo productInfo, double price) {
        ObservableList<DataInTable> dataInTableObservableList = FXCollections.observableArrayList();
        double priceWithoutTax = calculateWithoutTax(price, productInfo, state);
        double margin = calculateMargin(priceWithoutTax, productInfo, state.getLogisticCosts());
        dataInTableObservableList.add(new DataInTable(state.getState(), formatPrice(priceWithoutTax), formatPrice(margin), String.valueOf(state.getLogisticCosts())));

        for (State s : stateList) {
            if (!s.equals(state)) {
                priceWithoutTax = calculateWithoutTax(price, productInfo, s);
                margin = calculateMargin(priceWithoutTax, productInfo, s.getLogisticCosts());
                dataInTableObservableList.add(new DataInTable(s.getState(), formatPrice(priceWithoutTax), formatPrice(margin), String.valueOf(s.getLogisticCosts())));
            }
        }
        return dataInTableObservableList;
    }

    void clearAllOutputField() {
        wholesalePriceTextField.setText("");
        tableView.setItems(null);
    }

    void customiseTable(State state, TableColumn<DataInTable, String> col) {
        col.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : getItem());
                TableRow<DataInTable> currentRow = getTableRow();

                if (!isEmpty()) {
                    if (item.equals(state.getState())) {
                        currentRow.setStyle("-fx-background-color:lightgreen");
                    } else {
                        currentRow.setStyle("-fx-background-color:white");
                        currentRow.setStyle("-fx-text-fill: black");
                    }
                }
            }
        });
    }

    String formatPrice(double price) {
        return new DecimalFormat("##.##").format(price);
    }
}
