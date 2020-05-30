package application;

public class State {
    private String state;
    private double baseTax;
    private CategoryTax category;
    private double logisticCosts;

    @Deprecated
    public State(String state, double baseTax, CategoryTax category) {
    	this(state, baseTax, category, 0.0);
    }
    
    public State(String state, double baseTax, CategoryTax category, double logisticCosts) {
        this.state = state;
        this.baseTax = baseTax;
        this.category = category;
        this.logisticCosts = logisticCosts;

    public State(String state, double baseTax, CategoryTax category) {
        this.state = state;
        this.baseTax = baseTax;
        this.category = category;
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