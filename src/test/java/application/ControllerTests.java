package application;

import application.product.CategoryTax;
import application.product.ProductCategory;
import application.product.ProductInfo;
import javafx.collections.ObservableList;
import org.junit.Test;

import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class ControllerTests {

    private Controller controller = new Controller();

    private ProductInfo apple = new ProductInfo("apple", ProductCategory.GROCERIES, 5.0);
    private CategoryTax taxesInAlaska = new CategoryTax(1, 1, 1, 1, 1, 1);
    private State alaska = new State("Alaska", 0.5, taxesInAlaska, 2);
    private CategoryTax taxesInAlabama = new CategoryTax(4, 4, 0, 4, 4, 4);
    private State alabama = new State("Alabama", 4, taxesInAlabama, 3);

    @Test
    public void calculateWithoutTax_ReturnedSpecifiedValue() {
        double userPrice = 10;
        double groceriesTax = 1;

        double priceWithoutTax = controller.calculateWithoutTax(userPrice, apple, alaska);
        double expectedPriceWithoutTax = userPrice / ((100 + groceriesTax) / 100);

        assertThat(priceWithoutTax, equalTo(expectedPriceWithoutTax));
    }

    @Test
    public void calculateMargin_ReturnedSpecifiedValue() {
        double priceWithoutTax = 1.98;
        double logisticCosts = 1;
        double margin = controller.calculateMarginInState(priceWithoutTax, apple, logisticCosts);
        double expectedMargin = priceWithoutTax - apple.getWholesalePrice() - logisticCosts;

        assertThat(margin, equalTo(expectedMargin));
    }

    @Test
    public void validatePrice_ShouldReturnFalse0() {
        assertThat(controller.validatePrice(""), equalTo(false));
    }

    @Test
    public void validatePrice_ShouldReturnFalse1() {
        assertThat(controller.validatePrice("qwe"), equalTo(false));
    }

    @Test
    public void validatePrice_ShouldReturnFalse2() {
        assertThat(controller.validatePrice("123,12"), equalTo(false));
    }

    @Test
    public void validatePrice_ShouldReturnFalse3() {
        assertThat(controller.validatePrice("123.4565201452145"), equalTo(false));
    }

    @Test
    public void validatePrice_ShouldReturnFalse4() {
        assertThat(controller.validatePrice("-1.5"), equalTo(false));
    }

    @Test
    public void validatePrice_ShouldReturnTrue0() {
        assertThat(controller.validatePrice("123.45"), equalTo(true));
    }

    @Test
    public void validatePrice_ShouldReturnTrue1() {
        assertThat(controller.validatePrice("0"), equalTo(true));
    }

    @Test
    public void getTaxCategory_ReturnedSpecifiedValue() {
        double expectedTaxForAppleInAlaska = alaska.getCategory().getGroceries();
        double taxForAppleInAlaska = controller.getTaxFromCategory(apple, alaska);

        assertThat(taxForAppleInAlaska, equalTo(expectedTaxForAppleInAlaska));
    }

    @Test
    public void getDataForAllstateList_ReturnedListWithThreeElements() {
        ArrayList<State> states = new ArrayList<>();
        states.add(alaska);
        states.add(alabama);

        ObservableList<DataInTable> observableList = controller.getDataForAllStateList(states.get(0), states, apple, 10);

        assertThat(observableList.size(), equalTo(2));
    }

    @Test
    public void getDataForAllstateList_ReturnedSpecifiedElement() {
        ArrayList<State> states = new ArrayList<>();
        states.add(alaska);

        ObservableList<DataInTable> observableList = controller.getDataForAllStateList(states.get(0), states, apple, 10);
        DataInTable dataInTable = observableList.get(0);

        String expectedState = "Alaska";
        String expectedPriceWithoutTax = "9.9";
        String expectedMargin = "2.9";
        String expectedLogisticCost = "2.0";

        assertThat(dataInTable.getState(), equalTo(expectedState));
        assertThat(dataInTable.getPriceWithoutTax(), equalTo(expectedPriceWithoutTax));
        assertThat(dataInTable.getMargin(), equalTo(expectedMargin));
        assertThat(dataInTable.getLogisticCost(), equalTo(expectedLogisticCost));
    }
}
