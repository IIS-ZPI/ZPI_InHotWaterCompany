package application;

import application.foreignTransport.ImportCosts;
import application.foreignTransport.ImportCountry;
import application.product.CategoryTax;
import application.product.ProductCategory;
import application.product.ProductInfo;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static application.Finder.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class FinderTests {

    private application.product.CategoryTax properTaxes = new CategoryTax(1, 1, 1, 1, 1, 1);
    private State alaska = new State("Alaska", 1, properTaxes, 1);
    private State arizona = new State("Arizona", 1, properTaxes, 2);
    private ProductInfo apple = new ProductInfo("apple", ProductCategory.GROCERIES, 5.0);
    private ImportCountry poland = new ImportCountry("Poland", "97425", "PLN", new ImportCosts(2, 2, 2));

    @Test
    public void findStateShouldFindPassedState() {
        List<State> states = new ArrayList<>();
        states.add(alaska);
        states.add(arizona);
        String stateAlaska = "Alaska";
        assertThat(findState(stateAlaska, states), equalTo(alaska));
    }

    @Test
    public void findStateShouldFindPassedStateWithSmallLetters() {
        List<State> states = new ArrayList<>();
        states.add(alaska);
        states.add(arizona);
        String stateAlaska = "alaska";
        assertThat(findState(stateAlaska, states), equalTo(alaska));
    }

    @Test
    public void findStateShouldFindPassedStateWithDifferentLetters() {
        List<State> states = new ArrayList<>();
        states.add(alaska);
        states.add(arizona);
        String stateAlaska = "aLaSka";
        assertThat(findState(stateAlaska, states), equalTo(alaska));
    }

    @Test
    public void findProductShouldFindPassedProduct() {
        List<ProductInfo> products = new ArrayList<>();
        products.add(apple);
        String productApple = "aPPle";
        assertThat(findProduct(productApple, products), equalTo(apple));
    }

    @Test
    public void findProductShouldNotFindProduct() {
        List<ProductInfo> products = new ArrayList<>();

        String productApple = "Apple";
        assertThat(findProduct(productApple, products), equalTo(null));
    }

    @Test
    public void findCountryShouldNotFindCountry() {
        List<ImportCountry> countries = new ArrayList<>();

        String countryPoland = "PolAnD";
        assertThat(findCountry(countryPoland, countries), equalTo(null));
    }

    @Test
    public void findCountryShouldFindProperCountry() {
        List<ImportCountry> countries = new ArrayList<>();
        countries.add(poland);
        String countryPoland = "PolAnD";
        assertThat(findCountry(countryPoland, countries), equalTo(poland));
    }

}
