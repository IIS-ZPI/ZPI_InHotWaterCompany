package application;

import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class FXTests extends ApplicationTest {

    Text warningText;
    TextField searchProductTextField;
    TextField searchStateTextField;
    TextField wholesaleTextField;
    TextField marginTextField;
    TextField priceWithoutTaxTextField;

    @Override
    public void start(Stage stage) throws Exception {
        Main main = new Main();
        ((Application) main).start(stage);
        warningText = find("#warning");
        searchProductTextField = find("#searchProduct");
        searchStateTextField = find("#searchState");
        wholesaleTextField = find("#wholesalePriceTextField");
        marginTextField = find("#marginTextField");
        priceWithoutTaxTextField = find("#priceWithoutTaxTextField");
    }

    @Test
    public void warningText_EmptyProductStateWholesale_EnterAllData() {
        clickOn("#checkButton");
        
        assertThat(warningText.getText(), equalTo("Enter all data"));
    }

    @Test
    public void warningText_OnlyProductName_EnterAllData() {
        searchProductTextField.setText("ExampleData");

        clickOn("#checkButton");

        assertThat(warningText.getText(), equalTo("Enter all data"));
    }

    @Test
    public void warningText_OnlyStateName_EnterAllData() {
        searchStateTextField.setText("ExampleData");

        clickOn("#checkButton");

        assertThat(warningText.getText(), equalTo("Enter all data"));
    }

    @Test
    public void warningText_OnlyWholesale_EnterAllData() {
        wholesaleTextField.setText("ExampleData");

        clickOn("#checkButton");

        assertThat(warningText.getText(), equalTo("Enter all data"));
    }

    @Test
    public void warningText_wholesaleIsString_IncorrectPrice() {
        searchProductTextField.setText("ExampleData");
        searchStateTextField.setText("ExampleData");
        wholesaleTextField.setText("ExampleData");

        clickOn("#checkButton");

        assertThat(warningText.getText(), equalTo("Incorrect price"));
    }

    @Test
    public void warningText_wholesaleIsNegative_IncorrectPrice() {
        searchProductTextField.setText("ExampleData");
        searchStateTextField.setText("ExampleData");
        wholesaleTextField.setText("-10");

        clickOn("#checkButton");

        assertThat(warningText.getText(), equalTo("Incorrect price"));
    }

    @Test
    public void warningText_ProductNotInDataBase_ProductNotFound() {
        searchProductTextField.setText("ExampleData");
        searchStateTextField.setText("ExampleData");
        wholesaleTextField.setText("10");

        clickOn("#checkButton");

        assertThat(warningText.getText(), equalTo("Product not found"));
    }

    @Test
    public void warningText_StateNotInDataBase_StateNotFound() {
        searchProductTextField.setText("Oxycodone");
        searchStateTextField.setText("ExampleData");
        wholesaleTextField.setText("10");

        clickOn("#checkButton");

        assertThat(warningText.getText(), equalTo("State not found"));
    }

    @Test
    public void marginTextField_isEditable_False() {
        assertThat(marginTextField.isEditable(), equalTo(false));
    }

    @Test
    public void marginTextField_isEmptyOnStart_True() {
        assertThat(marginTextField.getText(), equalTo(""));
    }

    @Test
    public void marginTextField_isNotEmpty_True() {
        searchProductTextField.setText("Oxycodone");
        searchStateTextField.setText("Alaska");
        wholesaleTextField.setText("10");

        clickOn("#checkButton");

        assertThat(marginTextField.getText(), Matchers.not(Matchers.emptyString()));
    }

    @Test
    public void priceWithoutTaxTextField_isEditable_False() {
        assertThat(priceWithoutTaxTextField.isEditable(), equalTo(false));
    }

    @Test
    public void priceWithoutTaxTextField_isEmptyOnStart_True() {
        assertThat(priceWithoutTaxTextField.getText(), equalTo(""));
    }

    @Test
    public void priceWithoutTaxTextField_isNotEmpty_True() {
        searchProductTextField.setText("Oxycodone");
        searchStateTextField.setText("Alaska");
        wholesaleTextField.setText("10");

        clickOn("#checkButton");

        assertThat(priceWithoutTaxTextField.getText(), Matchers.not(Matchers.emptyString()));
    }

    public <T extends Node> T find(final String query) {
        return lookup(query).query();
    }
}
