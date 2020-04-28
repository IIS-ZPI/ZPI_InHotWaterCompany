package application;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import org.controlsfx.control.textfield.TextFields;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    private List<State> states = new ArrayList<>();
    private List<ProductInfo> productInfoList = new ArrayList<>();
    private List<String> productListAutoCompletion = new ArrayList<>();
    private List<String> stateListAutoCompletion = new ArrayList<>();

    @FXML
    private Text warning;
    @FXML
    private TextField searchProduct;
    @FXML
    private TextField searchState;
    @FXML
    private TextField wholesalePriceTextField;
    @FXML
    private TextField marginTextField;
    @FXML
    private TextField priceWithoutTaxTextField;

    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        states.add(new State("Alabama", 4, new Category(4, 4, 0, 4, 4, 4)));
        states.add(new State("Alaska", 0, new Category(0, 0, 0, 0, 0, 0)));
        states.add(new State("Arizona", 5.6, new Category(0, 5.6, 0, 5.6, 5.6, 5.6)));
        states.add(new State("Arkansas", 6.5, new Category(0.125, 6.5, 0, 6.5, 6.5, 0)));
        states.add(new State("California", 7.25, new Category(0, 7.25, 0, 7.25, 7.25, 0)));
        states.add(new State("Colorado", 2.9, new Category(0, 2.9, 0, 2.9, 2.9, 2.9)));
        states.add(new State("Connecticut", 6.35, new Category(0, 6.35, 0, 0, 6.35, 1)));
        states.add(new State("Delaware", 0, new Category(0, 0, 0, 0, 0, 0)));
        states.forEach(x -> stateListAutoCompletion.add(x.getState()));
        productInfoList.add(new ProductInfo("apple", "groceries", 0.24));
        productInfoList.add(new ProductInfo("orange", "groceries", 0.35));
        productInfoList.add(new ProductInfo("pineapple", "groceries", 0.78));
        productInfoList.add(new ProductInfo("Oxycodone", "Non-prescription-drug", 16.99));
        productInfoList.add(new ProductInfo("Fentanyl", "Non-prescription-drug", 13.58));
        productInfoList.forEach(x -> productListAutoCompletion.add(x.getProduct()));
        TextFields.bindAutoCompletion(searchProduct, productListAutoCompletion);
        TextFields.bindAutoCompletion(searchState, stateListAutoCompletion);
    }

    public void onClickButton() {
        String priceWholeString = wholesalePriceTextField.getText();
        String stateString = searchState.getText();
        String productString = searchProduct.getText();
        if (priceWholeString.isEmpty() || stateString.isEmpty() || productString.isEmpty())
            warning.setText("Enter all data");
        else {
            if (validatePrice(priceWholeString)) {
                double WholesalePrice = Double.parseDouble(priceWholeString);
                ProductInfo productInfo = findProduct(productString);
                if (productInfo == null)
                    warning.setText("Product not found");
                else {
                    State state = findState(stateString);
                    if (state == null)
                        warning.setText("State not found");
                    else {
                        warning.setText("");
                        double PriceWithoutTax = calculateWithoutTax(WholesalePrice, productInfo, state);
                        priceWithoutTaxTextField.setText(new DecimalFormat("##.##").format(PriceWithoutTax));
                        Double margin = calculateMargin(PriceWithoutTax, productInfo);
                        marginTextField.setText(new DecimalFormat("##.##").format(margin));
                    }
                }
            } else {
                warning.setText("Incorrect price");
            }
        }
    }

    private double calculateWithoutTax(double WholesalePrice, ProductInfo productInfo, State state) {
        return WholesalePrice / ((100.0 + getTaxCategory(productInfo, state)) / 100);
    }

    private double calculateMargin(double priceWithoutTax, ProductInfo productInfo) {
        return priceWithoutTax - productInfo.getWholesalePrice();
    }

    private boolean validatePrice(String price) {
        if (price.equals("."))
            return false;
        boolean isDotted = false;
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
        }
        return true;
    }

    private State findState(String state) {
        for (State s : states) {
            if (s.getState().equals(state))
                return s;
        }
        return null;
    }

    private ProductInfo findProduct(String product) {
        for (ProductInfo productInfo : productInfoList) {
            if (productInfo.getProduct().equals(product))
                return productInfo;
        }
        return null;
    }

    private double getTaxCategory(ProductInfo productInfo, State state) {
        double category = -1;
        switch (productInfo.getCategory()) {
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
        }
        return category;
    }
}