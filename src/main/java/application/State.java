package application;

public class State {
    private String state;
    private double baseTax;
    private CategoryTax category;
    private double logisticCosts;

    public State(String state, double baseTax, CategoryTax category, double logisticCosts) {
        this.state = state;
        this.baseTax = baseTax;
        this.category = category;
        this.logisticCosts = logisticCosts;
    }

    public CategoryTax getCategory() {
        return category;
    }

    public String getState() {
        return state;
    }

    public double getBaseTax() {
        return baseTax;
    }

    public double getLogisticCosts() {
        return logisticCosts;
    }
}