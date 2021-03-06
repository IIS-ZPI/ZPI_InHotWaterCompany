package application;

import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class FXTests extends ApplicationTest {

    Text warningText;
    TextField searchProductTextField;
    TextField searchStateTextField;
    TextField priceTextField;
    TextField wholesalePriceTextField;
    TextField searchCountryTextField;
    TextField minimalPriceTextField;

    @Override
    public void start(Stage stage) throws Exception {
        Main main = new Main();
        ((Application) main).start(stage);
        warningText = find("#warning");
        searchProductTextField = find("#searchProductTextField");
        searchStateTextField = find("#searchStateTextField");
        priceTextField = find("#priceTextField");
        wholesalePriceTextField = find("#wholesalePriceTextField");
        searchCountryTextField = find("#searchCountryTextField");
        minimalPriceTextField = find("#minimalPriceTextField");
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
        priceTextField.setText("ExampleData");

        clickOn("#checkButton");

        assertThat(warningText.getText(), equalTo("Enter all data"));
    }

    @Test
    public void warningText_wholesaleIsString_IncorrectPrice() {
        searchProductTextField.setText("ExampleData");
        searchStateTextField.setText("ExampleData");
        priceTextField.setText("ExampleData");

        clickOn("#checkButton");

        assertThat(warningText.getText(), equalTo("Incorrect price"));
    }

    @Test
    public void warningText_wholesaleIsNegative_IncorrectPrice() {
        searchProductTextField.setText("ExampleData");
        searchStateTextField.setText("ExampleData");
        priceTextField.setText("-10");

        clickOn("#checkButton");

        assertThat(warningText.getText(), equalTo("Incorrect price"));
    }

    @Test
    public void warningText_ProductNotInDataBase_ProductNotFound() {
        searchProductTextField.setText("ExampleData");
        searchStateTextField.setText("ExampleData");
        priceTextField.setText("10");

        clickOn("#checkButton");

        assertThat(warningText.getText(), equalTo("Product not found"));
    }

    @Test
    public void warningText_StateNotInDataBase_StateNotFound() {
        searchProductTextField.setText("Oxycodone");
        searchStateTextField.setText("ExampleData");
        priceTextField.setText("10");

        clickOn("#checkButton");

        assertThat(warningText.getText(), equalTo("State not found"));
    }

    @Test
    public void wholesalePriceTextField_ExampleData_NotFound() {
        searchProductTextField.setText("ExampleData");

        clickOn("#checkButton");

        assertThat(wholesalePriceTextField.getText(), equalTo("Not found"));
    }

    @Test
    public void wholesalePriceTextField_isEditable_False() {
        assertThat(wholesalePriceTextField.isEditable(), equalTo(false));
    }

    @Test
    public void searchCountryTextField_isDisable_True() {
        assertThat(searchCountryTextField.isDisable(), equalTo(true));
    }

    @Test
    public void searchCountryTextField_isDisable_False() {
        clickOn("#sendAbroadCheckBox");
        assertThat(wholesalePriceTextField.isDisable(), equalTo(false));
    }

    @Test
    public void searchStateTextField_isEditable_True() {
        clickOn("#sendAbroadCheckBox");
        assertThat(searchStateTextField.isDisable(), equalTo(true));
    }

    @Test
    public void minimalPriceTextField_isDisable_True() {
        clickOn("#sendAbroadCheckBox");
        assertThat(minimalPriceTextField.isDisable(), equalTo(true));
    }

    public <T extends Node> T find(final String query) {
        return lookup(query).query();
    }
}
