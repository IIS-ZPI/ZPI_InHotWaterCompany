package application;

import application.foreignTransport.ImportCountry;
import application.product.ProductInfo;

import java.util.List;

public class Finder {

    private Finder() {
        super();
    }

    static State findState(String state, List<State> stateList) {
        for (State s : stateList) {
            if (s.getUsaState()
                 .equalsIgnoreCase(state.toLowerCase())) {
                return s;
            }
        }
        return null;
    }

    static ProductInfo findProduct(String product, List<ProductInfo> productInfoList) {
        for (ProductInfo productInfo : productInfoList) {
            if (productInfo.getProduct()
                           .equalsIgnoreCase(product.toLowerCase())) {
                return productInfo;
            }
        }
        return null;
    }

    static ImportCountry findCountry(String country, List<ImportCountry> importCountryList) {
        for (ImportCountry c : importCountryList) {
            if (c.getName()
                 .equalsIgnoreCase(country)) {
                return c;
            }
        }
        return null;
    }
}
