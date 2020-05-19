package application;

import javafx.collections.ObservableList;
import org.junit.Test;
import utils.CategoryResolver;

import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class ControllerTests {

    Controller controller = new Controller();

    ProductInfo apple = new ProductInfo("apple", ProductCategory.GROCERIES, 5.0);
    CategoryTax categories = new CategoryTax(1, 1, 1, 1, 1, 1);
    State alaska = new State("Alaska", 0.5, categories);

    @Test
    public void calculateWithoutTax_ReturnedSpecifiedValue() {
        double userPrice = 10;
        double groceriesTax = 1;

        double priceWithoutTax = controller.calculateWithoutTax(userPrice, apple, alaska);
        double expectedPriceWithouTax = userPrice / ((100 + groceriesTax) / 100);

        assertThat(priceWithoutTax, equalTo(expectedPriceWithouTax));
    }

    @Test
    public void calculateMargin_ReturnedSpecifiedValue() {
        double priceWithoutTax = 1.98;
        double logisticCosts = 1;
        double margin = controller.calculateMargin(priceWithoutTax, apple, logisticCosts);
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
    public void getMarginForAllstateList_ReturnedListWithThreeElements() {

        ArrayList<State> states = new ArrayList<>();
        states.add(new State("Alabama", 4, new CategoryTax(4, 4, 0, 4, 4, 4)));
        states.add(new State("California", 7.25, new CategoryTax(0, 7.25, 0, 7.25, 7.25, 0)));
        states.add(new State("Delaware", 0, new CategoryTax(0, 0, 0, 0, 0, 0)));

        ObservableList<MarginForAllState> observableList = controller.getMarginForAllstateList(states, apple, 5);

        assertThat(observableList.size(), equalTo(3));
    }

    @Test
    public void getMarginForAllstateList_ReturnedSpecifiedList() {
        ArrayList<State> states = new ArrayList<>();
        states.add(new State("Alabama", 4, new CategoryTax(4, 4, 0, 4, 4, 4)));
        states.add(new State("California", 7.25, new CategoryTax(0, 7.25, 0, 7.25, 7.25, 0)));
        states.add(new State("Delaware", 0, new CategoryTax(0, 0, 0, 0, 0, 0)));

        ProductInfo orange = new ProductInfo("orange", ProductCategory.GROCERIES, 0.24);

        ObservableList<MarginForAllState> observableList = controller.getMarginForAllstateList(states, orange, 5);

        assertThat(observableList.get(0).getMargin(), equalTo("4,57"));
        assertThat(observableList.get(1).getMargin(), equalTo("4,76"));
        assertThat(observableList.get(2).getMargin(), equalTo("4,76"));
    }
}
