package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import org.controlsfx.control.textfield.TextFields;

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
    private TextField marginTextField;
    @FXML
    private TextField priceWithoutTaxTextField;
    @FXML
    private TextField wholesalePriceTextField;
    @FXML
    private TextField logisticCostsTextField;
    @FXML
    private TableView<MarginForAllState> tableView;
    @FXML
    private TableColumn<MarginForAllState, String> stateColumn;
    @FXML
    private TableColumn<MarginForAllState, Double> marginColumn;

    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        stateList.add(new State("Alabama", 4, new CategoryTax(4, 4, 0, 4, 4, 4)));
        stateList.add(new State("Alaska", 0, new CategoryTax(0, 0, 0, 0, 0, 0)));
        stateList.add(new State("Arizona", 5.6, new CategoryTax(0, 5.6, 0, 5.6, 5.6, 5.6)));
        stateList.add(new State("Arkansas", 6.5, new CategoryTax(0.125, 6.5, 0, 6.5, 6.5, 0)));
        stateList.add(new State("California", 7.25, new CategoryTax(0, 7.25, 0, 7.25, 7.25, 0)));
        stateList.add(new State("Colorado", 2.9, new CategoryTax(0, 2.9, 0, 2.9, 2.9, 2.9)));
        stateList.add(new State("Connecticut", 6.35, new CategoryTax(0, 6.35, 0, 0, 6.35, 1)));
        stateList.add(new State("Delaware", 0, new CategoryTax(0, 0, 0, 0, 0, 0)));
        stateList.forEach(x -> stateListAutoCompletion.add(x.getState()));
        productInfoList.add(new ProductInfo("apple", "groceries", 0.24));
        productInfoList.add(new ProductInfo("orange", "groceries", 0.35));
        productInfoList.add(new ProductInfo("pineapple", "groceries", 0.78));
        productInfoList.add(new ProductInfo("Oxycodone", "Non-prescription-drug", 16.99));
        productInfoList.add(new ProductInfo("Fentanyl", "Non-prescription-drug", 13.58));
        productInfoList.forEach(x -> productListAutoCompletion.add(x.getProduct()));
        TextFields.bindAutoCompletion(searchProductTextField, productListAutoCompletion);
        TextFields.bindAutoCompletion(searchStateTextField, stateListAutoCompletion);

        stateColumn.setCellValueFactory(new PropertyValueFactory<>("state"));
        marginColumn.setCellValueFactory(new PropertyValueFactory<>("margin"));
    }

    public void onClickCheckButton() {
        String priceString = priceTextField.getText();
        String stateString = searchStateTextField.getText();
        String productString = searchProductTextField.getText();
        String logisticCostsString = logisticCostsTextField.getText();
        if (priceString.isEmpty() || stateString.isEmpty() || productString.isEmpty() || logisticCostsString.isEmpty()) {
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
                        if (!validatePrice(logisticCostsString)) {
                            warning.setText("Incorrect Logistic cost");
                            clearAllOutputField();
                        } else {
                            warning.setText("");
                            double priceWithoutTax = calculateWithoutTax(price, productInfo, state);
                            double logisticCosts = Double.parseDouble(logisticCostsString);
                            priceWithoutTaxTextField.setText(new DecimalFormat("##.##").format(priceWithoutTax));
                            Double margin = calculateMargin(priceWithoutTax, productInfo, logisticCosts);
                            marginTextField.setText(new DecimalFormat("##.##").format(margin));

                            tableView.setItems(getMarginForAllstateList(stateList, productInfo, price));
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

    private State findState(String state) {
        for (State s : stateList) {
            if (s.getState().toLowerCase().equals(state.toLowerCase()))
                return s;
        }
        return null;
    }

    private ProductInfo findProduct(String product) {
        for (ProductInfo productInfo : productInfoList) {
            if (productInfo.getProduct().toLowerCase().equals(product.toLowerCase()))
                return productInfo;
        }
        return null;
    }

    double getTaxFromCategory(ProductInfo productInfo, State state) {
        double category = -1;
        switch (productInfo.getCategoryString()) {
            case "groceries":
                category = state.getCategory().getGroceries();
                break;
            case "preparedFood":
                category = state.getCategory().getPreparedFood();
                break;
            case "prescription-drug":
                category = state.getCategory().getPrescriptionDrug();
                break;
            case "Non-prescription-drug":
                category = state.getCategory().getNonPrescriptionDrug();
                break;
            case "clothing":
                category = state.getCategory().getClothing();
                break;
            case "intangibles":
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

    public ObservableList<MarginForAllState> getMarginForAllstateList(List<State> stateList, ProductInfo productInfo, double price) {
        ObservableList<MarginForAllState> marginForAllStateObservableList = FXCollections.observableArrayList();

        for (State s : stateList) {
            double priceWithoutTax = calculateWithoutTax(price, productInfo, s);
            double margin = calculateMargin(priceWithoutTax, productInfo, 0);
            marginForAllStateObservableList.add(new MarginForAllState(s.getState(), new DecimalFormat("##.##").format(margin)));
        }

        return marginForAllStateObservableList;
    }

    void clearAllOutputField() {
        marginTextField.setText("");
        priceWithoutTaxTextField.setText("");
        wholesalePriceTextField.setText("");
        tableView.setItems(null);
    }
}