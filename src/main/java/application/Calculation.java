package application;

import application.foreignTransport.ImportCountry;
import application.product.ProductInfo;

import java.io.IOException;

import static utils.ExchangeRate.getFor;

public class Calculation {

    private Calculation() {
        super();
    }

    static double calculateWithoutTax(double price, ProductInfo productInfo, State state) {
        return price / ((100.0 + getTaxFromCategory(productInfo, state)) / 100);
    }

    static double calculateMarginInState(double priceWithoutTax, ProductInfo productInfo, double logisticCosts) {
        return priceWithoutTax - productInfo.getWholesalePrice() - logisticCosts;
    }

    static double calculateMarginInOtherCountry(double price, ProductInfo productInfo, ImportCountry importCountry) throws IOException {
        double importTariff = 0;
        double transportFee = importCountry.getImportCosts()
                                           .getTransportFee();
        if (isConsumables(productInfo)) {
            importTariff = productInfo.getWholesalePrice() * (importCountry.getImportCosts()
                                                                           .getConsumablesImportTariff() / 100);
        } else {
            importTariff = productInfo.getWholesalePrice() * (importCountry.getImportCosts()
                                                                           .getOthersImportTariff() / 100);
        }
        double finalMargin = price - productInfo.getWholesalePrice() - importTariff - transportFee;
        return changeToActualCurrency(finalMargin, importCountry.getCurrencyCode());
    }

    static double changeToActualCurrency(double price, String currencyCode) throws IOException {
        return price * getFor(currencyCode);
    }

    static public boolean isConsumables(ProductInfo productInfo) {

        switch (productInfo.getCategory()) {
            case GROCERIES:
            case PREPARED_FOOD:
            case PRESCRIPTION_DRUG:
            case NONPRESCRIPTION_DRUG:
                return true;
            default:
                return false;
        }
    }

    static double getTaxFromCategory(ProductInfo productInfo, State state) {
        double category = -1;
        switch (productInfo.getCategory()) {
            case GROCERIES:
                category = state.getCategory()
                                .getGroceries();
                break;
            case PREPARED_FOOD:
                category = state.getCategory()
                                .getPreparedFood();
                break;
            case PRESCRIPTION_DRUG:
                category = state.getCategory()
                                .getPrescriptionDrug();
                break;
            case NONPRESCRIPTION_DRUG:
                category = state.getCategory()
                                .getNonPrescriptionDrug();
                break;
            case CLOTHING:
                category = state.getCategory()
                                .getClothing();
                break;
            case INTANGIBLES:
                category = state.getCategory()
                                .getIntangibles();
                break;
            default:
                break;
        }
        return category;
    }
}
