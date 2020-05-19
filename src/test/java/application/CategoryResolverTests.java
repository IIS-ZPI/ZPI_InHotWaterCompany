package application;

import org.junit.Test;
import utils.CategoryResolver;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class CategoryResolverTests {

    @Test
    public void getEnum_EnumDoesNotExist_ReturnedUndefinedCategory() {
        ProductCategory productCategory = CategoryResolver.getEnum("ExampleData");
        assertThat(productCategory, equalTo(ProductCategory.UNDEFINED));
    }

    @Test
    public void getEnum_EnumExists_ReturnedSpecifiedCategory0() {
        ProductCategory productCategory = CategoryResolver.getEnum("groceries");
        assertThat(productCategory, equalTo(ProductCategory.GROCERIES));
    }

    @Test
    public void getEnum_EnumExists_ReturnedSpecifiedCategory1() {
        ProductCategory productCategory = CategoryResolver.getEnum("gRocEriEs");
        assertThat(productCategory, equalTo(ProductCategory.GROCERIES));
    }

    @Test
    public void getString_EnumExists_ReturnedSpecifiedCategory() {
        String productCategory = CategoryResolver.getString(ProductCategory.GROCERIES);
        assertThat(productCategory, equalTo("groceries"));
    }


}
