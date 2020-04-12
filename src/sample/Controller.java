package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Controller {
    List<State> states = new ArrayList<State>();
    ObservableList<String> observableListState = FXCollections.observableArrayList();
    ObservableList<String> observableListCategory = FXCollections.observableArrayList();

    @FXML
    private ChoiceBox choiceBoxState;
    @FXML
    private ChoiceBox choiceBoxCategory;
    @FXML
    private TextArea priceOfTax, priceAfterTax;
    @FXML
    private TextField inputPrice;
    @FXML
    private Text warning;

    public Controller() {
        states.add(new State("Alabama", 4, new Category(4, 4, 0, 4, 4, 4)));
        states.add(new State("Alaska", 0, new Category(0, 0, 0, 0, 0, 0)));
        states.add(new State("Arizona", 5.6, new Category(0, 5.6, 0, 5.6, 5.6, 5.6)));
        states.add(new State("Arkansas", 6.5, new Category(0.125, 6.5, 0, 6.5, 6.5, 0)));
        states.add(new State("California", 7.25, new Category(0, 7.25, 0, 7.25, 7.25, 0)));
        states.add(new State("Colorado", 2.9, new Category(0, 2.9, 0, 2.9, 2.9, 2.9)));
        states.add(new State("Connecticut", 6.35, new Category(0, 6.35, 0, 0, 6.35, 1)));
        states.add(new State("Delaware", 0, new Category(0, 0, 0, 0, 0, 0)));
        states.forEach(x -> observableListState.add(x.getState()));
    }

    @FXML
    private void initialize() {
        observableListCategory.addAll("Groceries", "Prepared food", "Prescription drug", "Non-prescription drug", "Clothing", "Intangibles");
        choiceBoxState.setItems(observableListState);
        choiceBoxCategory.setItems(observableListCategory);
    }

    public void onClickButton() {

        String choiceBoxStateValue = (String) choiceBoxState.getValue();
        String choiceBoxCategoryValue = (String) choiceBoxCategory.getValue();

        if (choiceBoxStateValue != null && choiceBoxCategoryValue != null) {
            if (inputPrice.getText().isEmpty()) {
                warning.setText("Enter price !");
            } else if (!validatePrice(inputPrice.getText())) {
                warning.setText("Incorrect price");
            } else {
                warning.setText("");
                double price = Double.parseDouble(inputPrice.getText());
                State state = findState(choiceBoxStateValue);
                double resultOfTax = -1;
                double resultAfterTax = -1;
                switch (choiceBoxCategoryValue) {
                    case "Groceries":
                        resultOfTax = state.calcualteOfTax(price, state.getCategory().getGroceries());
                        resultAfterTax = state.calcualteAfterTax(price, state.getCategory().getGroceries());
                        break;
                    case "Prepared food":
                        resultOfTax = state.calcualteOfTax(price, state.getCategory().getPreparedFood());
                        resultAfterTax = state.calcualteAfterTax(price, state.getCategory().getPreparedFood());
                        break;
                    case "Prescription drug":
                        resultOfTax = state.calcualteOfTax(price, state.getCategory().getPrescriptionDrug());
                        resultAfterTax = state.calcualteAfterTax(price, state.getCategory().getPrescriptionDrug());
                        break;
                    case "Non-prescription drug":
                        resultOfTax = state.calcualteOfTax(price, state.getCategory().getNonPrescriptionDrug());
                        resultAfterTax = state.calcualteAfterTax(price, state.getCategory().getNonPrescriptionDrug());
                        break;
                    case "Clothing":
                        resultOfTax = state.calcualteOfTax(price, state.getCategory().getClothing());
                        resultAfterTax = state.calcualteAfterTax(price, state.getCategory().getClothing());
                        break;
                    case "Intangibles":
                        resultOfTax = state.calcualteOfTax(price, state.getCategory().getIntangibles());
                        resultAfterTax = state.calcualteAfterTax(price, state.getCategory().getIntangibles());
                        break;
                }
                priceOfTax.setText(new DecimalFormat("##.##").format(resultOfTax));
                priceAfterTax.setText(new DecimalFormat("##.##").format(resultAfterTax));
            }
        } else
            warning.setText("Choose State and Category");
    }

    private boolean validatePrice(String price) {
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

    private State findState(String choiceBoxValue) {
        State state = null;
        for (State s : states) {
            if (s.getState().equals(choiceBoxValue))
                state = s;
        }
        return state;
    }

}
