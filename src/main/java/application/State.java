package application;

public class State {
    private String state;
    private double baseTax;
    private CategoryTax category;

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
}