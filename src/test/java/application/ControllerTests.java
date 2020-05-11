package application;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class ControllerTests {

    Controller controller = new Controller();

    ProductInfo apple = new ProductInfo("apple", "groceries", 5.0);
    //ProductInfo apple = new ProductInfo("apple", ProductCategory.GROCERIES, 5.0);
    Category categories = new Category(1, 1, 1, 1, 1, 1);
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

        double margin = controller.calculateMargin(priceWithoutTax, apple);
        double expectedMargin = priceWithoutTax - apple.getWholesalePrice();

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
    public void getTaxCategory_ReturnedSpecifiedValue(){
        double expectedTaxForAppleInAlaska = alaska.getCategory().getGroceries();
        double taxForAppleInAlaska = controller.getTaxFromCategory(apple, alaska);

        assertThat(taxForAppleInAlaska, equalTo(expectedTaxForAppleInAlaska));
    }


}
