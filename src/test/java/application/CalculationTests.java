package application;

import application.foreignTransport.ImportCosts;
import application.foreignTransport.ImportCountry;
import application.product.CategoryTax;
import application.product.ProductCategory;
import application.product.ProductInfo;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static application.Calculation.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CalculationTests {

    private ProductInfo apple = new ProductInfo("apple", ProductCategory.GROCERIES, 5.0);
    private CategoryTax properTaxes = new CategoryTax(1, 1, 1, 1, 1, 1);
    private State alaska = new State("Alaska", 1, properTaxes, 1);
    private State arizona = new State("Arizona", 1, properTaxes, 2);
    private ImportCountry poland = new ImportCountry("Poland", "1234", "PLN", new ImportCosts(1, 1, 1));

    @Test
    public void calculateWithoutTaxShouldReturnProperValue() {
        double priceOfApple = 10.0;

        double actualPrice = ((int) (calculateWithoutTax(priceOfApple, apple, alaska) * 100)) / 100.0d;
        double expectedPrice = 9.90;

        assertThat(actualPrice, equalTo(expectedPrice));
    }

    @Test
    public void calculateMarginInStateShouldReturnProperValue() {
        double priceOfAppleWithoutTax = 10.0;
        double logisticCosts = 2;

        double actualMargin = calculateMarginInState(priceOfAppleWithoutTax, apple, logisticCosts);
        double expectedMargin = 3;

        assertThat(actualMargin, equalTo(expectedMargin));
    }

    @Test
    public void calculateMarginInOtherCountryShouldReturnProperValue() throws IOException {
        double priceOfApple = 10.0;

        double actualMargin = ((int) (calculateMarginInOtherCountry(priceOfApple, apple, poland) * 100)) / 100.0d;
        double expectedMargin = 15.52;

        assertThat(actualMargin, equalTo(expectedMargin));
    }

    @Test
    public void getMinimalSellPriceShouldReturnProperValue() {

        List<State> states = new ArrayList<>();
        states.add(alaska);
        states.add(arizona);

        double actualMinimalSellPrice = ((int) (getMinimalSellPrice(apple, states) * 100)) / 100.0d;
        double expectedMinimalSellPrice = 7.07;

        assertThat(actualMinimalSellPrice, equalTo(expectedMinimalSellPrice));
    }

    @Test
    public void isConsumablesShouldReturnProperValue0() {
        boolean actualState = isConsumables(apple);

        assertThat(actualState, equalTo(true));
    }

    @Test
    public void isConsumablesShouldReturnProperValue1() {
        ProductInfo tv = mock(ProductInfo.class);
        when(tv.getCategory()).thenReturn(ProductCategory.UNDEFINED);
        boolean actualState = isConsumables(tv);

        assertThat(actualState, equalTo(false));
    }

    @Test
    public void getTaxFromCategoryShouldReturnProperValue0() {
        double tax = getTaxFromCategory(apple, alaska);
        double expectedTax = 1.0;
        assertThat(tax, equalTo(expectedTax));
    }

    @Test
    public void getTaxFromCategoryShouldReturnProperValue1() {
        ProductInfo tv = mock(ProductInfo.class);
        when(tv.getCategory()).thenReturn(ProductCategory.UNDEFINED);

        double actualTax = getTaxFromCategory(tv, alaska);
        double expectedTax = -1.0;

        assertThat(actualTax, equalTo(expectedTax));
    }
}
