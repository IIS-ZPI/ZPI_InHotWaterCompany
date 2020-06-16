package application;

import application.product.CategoryTax;

public class State {
    private String usaState;
    private double baseTax;
    private CategoryTax category;
    private double logisticCosts;

    public State(String state, double baseTax, CategoryTax category, double logisticCosts) {
        this.usaState = state;
        this.baseTax = baseTax;
        this.category = category;
        this.logisticCosts = logisticCosts;
    }

    public CategoryTax getCategory() {
        return category;
    }

    public String getUsaState() {
        return usaState;
    }

    public double getBaseTax() {
        return baseTax;
    }

    public double getLogisticCosts() {
        return logisticCosts;
    }
}
