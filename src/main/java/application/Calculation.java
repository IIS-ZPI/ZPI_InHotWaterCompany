package application;

import application.foreignTransport.ImportCountry;
import application.product.ProductInfo;

public class Calculation {
    static double calculateWithoutTax(double price, ProductInfo productInfo, State state) {
        return price / ((100.0 + getTaxFromCategory(productInfo, state)) / 100);
    }

    static double calculateMarginInState(double priceWithoutTax, ProductInfo productInfo, double logisticCosts) {
        return priceWithoutTax - productInfo.getWholesalePrice() - logisticCosts;
    }

    static double calculateMarginInOtherCountry(double price, ProductInfo productInfo, ImportCountry importCountry) {
        double finalMargin = 0;
        double transportFee = changeToActualCurrency(importCountry.getImportCosts().getTransportFee(), importCountry.getCurrencyCode());
        if (isConsumables(productInfo)) {
            finalMargin = price - productInfo.getWholesalePrice() - (productInfo.getWholesalePrice() * (importCountry.getImportCosts().getConsumablesImportTariff() / 100))
                    - transportFee;
        } else {
            finalMargin = price - productInfo.getWholesalePrice() - (productInfo.getWholesalePrice() * (importCountry.getImportCosts().getOthersImportTariff() / 100))
                    - transportFee;
        }
        return finalMargin;
    }

    static double changeToActualCurrency(double price, String currencyCode) {// TO DO
        return price;
    }

    static public boolean isConsumables(ProductInfo productInfo) {

        switch (productInfo.getCategory()) {
            case GROCERIES:
            case PREPARED_FOOD:
            case PRESCRIPTION_DRUG:
            case NONPRESCRIPTION_DRUG:
                return true;
        }
        return false;
    }

    static double getTaxFromCategory(ProductInfo productInfo, State state) {
        double category = -1;
        switch (productInfo.getCategory()) {
            case GROCERIES:
                category = state.getCategory().getGroceries();
                break;
            case PREPARED_FOOD:
                category = state.getCategory().getPreparedFood();
                break;
            case PRESCRIPTION_DRUG:
                category = state.getCategory().getPrescriptionDrug();
                break;
            case NONPRESCRIPTION_DRUG:
                category = state.getCategory().getNonPrescriptionDrug();
                break;
            case CLOTHING:
                category = state.getCategory().getClothing();
                break;
            case INTANGIBLES:
                category = state.getCategory().getIntangibles();
                break;
            default:
                break;
        }
        return category;
    }
}
