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

    @Override
    public void start(Stage stage) throws Exception {
        Main main = new Main();
        ((Application) main).start(stage);
    }

    @Test
    public void warningText_EmptyProductStateWholesale_EnterAllData() {
        Text text = find("#warning");
        clickOn("#checkButton");
        assertThat(text.getText(), equalTo("Enter all data"));
    }

    @Test
    public void warningText_OnlyProductName_EnterAllData() {
        Text text = find("#warning");
        TextField textField = find("#searchProduct");
        textField.setText("ExampleData");

        clickOn("#checkButton");
        assertThat(text.getText(), equalTo("Enter all data"));
    }

    @Test
    public void warningText_OnlyStateName_EnterAllData() {
        Text text = find("#warning");
        TextField textField = find("#searchState");
        textField.setText("ExampleData");

        clickOn("#checkButton");
        assertThat(text.getText(), equalTo("Enter all data"));
    }

    @Test
    public void warningText_OnlyWholesale_EnterAllData() {
        Text text = find("#warning");
        TextField textField = find("#wholesalePriceTextField");
        textField.setText("ExampleData");

        clickOn("#checkButton");
        assertThat(text.getText(), equalTo("Enter all data"));
    }

    @Test
    public void warningText_wholesaleIsString_IncorrectPrice() {
        Text text = find("#warning");

        TextField product = find("#searchProduct");
        product.setText("ExampleData");

        TextField state = find("#searchState");
        state.setText("ExampleData");

        TextField wholesale = find("#wholesalePriceTextField");
        wholesale.setText("ExampleData");

        clickOn("#checkButton");

        assertThat(text.getText(), equalTo("Incorrect price"));
    }

    @Test
    public void warningText_wholesaleIsNegative_IncorrectPrice() {
        Text text = find("#warning");

        TextField product = find("#searchProduct");
        product.setText("ExampleData");

        TextField state = find("#searchState");
        state.setText("ExampleData");

        TextField wholesale = find("#wholesalePriceTextField");
        wholesale.setText("-10");

        clickOn("#checkButton");

        assertThat(text.getText(), equalTo("Incorrect price"));
    }

    @Test
    public void warningText_ProductNotInDataBase_ProductNotFound() {
        Text text = find("#warning");

        TextField product = find("#searchProduct");
        product.setText("ExampleData");

        TextField state = find("#searchState");
        state.setText("ExampleData");

        TextField wholesale = find("#wholesalePriceTextField");
        wholesale.setText("10");

        clickOn("#checkButton");

        assertThat(text.getText(), equalTo("Product not found"));
    }

    @Test
    public void warningText_StateNotInDataBase_StateNotFound() {
        Text text = find("#warning");

        TextField product = find("#searchProduct");
        product.setText("Oxycodone");

        TextField state = find("#searchState");
        state.setText("ExampleData");

        TextField wholesale = find("#wholesalePriceTextField");
        wholesale.setText("10");

        clickOn("#checkButton");

        assertThat(text.getText(), equalTo("State not found"));
    }

    @Test
    public void marginTextField_isEditable_False() {
        TextField textField = find("#marginTextField");
        assertThat(textField.isEditable(), equalTo(false));
    }

    @Test
    public void marginTextField_isEmptyOnStart_True() {
        TextField textField = find("#marginTextField");
        assertThat(textField.getText(), equalTo(""));
    }

    @Test
    public void marginTextField_isNotEmpty_True() {
        TextField product = find("#searchProduct");
        product.setText("Oxycodone");

        TextField state = find("#searchState");
        state.setText("Alaska");

        TextField wholesale = find("#wholesalePriceTextField");
        wholesale.setText("10");

        clickOn("#checkButton");

        TextField textField = find("#marginTextField");

        assertThat(textField.getText(), Matchers.not(Matchers.emptyString()));
    }

    @Test
    public void priceWithoutTaxTextField_isEditable_False() {
        TextField textField = find("#priceWithoutTaxTextField");
        assertThat(textField.isEditable(), equalTo(false));
    }

    @Test
    public void priceWithoutTaxTextField_isEmptyOnStart_True() {
        TextField textField = find("#priceWithoutTaxTextField");
        assertThat(textField.getText(), equalTo(""));
    }

    @Test
    public void priceWithoutTaxTextField_isNotEmpty_True() {
        TextField product = find("#searchProduct");
        product.setText("Oxycodone");

        TextField state = find("#searchState");
        state.setText("Alaska");

        TextField wholesale = find("#wholesalePriceTextField");
        wholesale.setText("10");

        clickOn("#checkButton");

        TextField textField = find("#priceWithoutTaxTextField");

        assertThat(textField.getText(), Matchers.not(Matchers.emptyString()));
    }

    public <T extends Node> T find(final String query) {
        return lookup(query).query();
    }
}
