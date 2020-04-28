package application;

public class State {
    private String state;
    private double baseTax;
    private Category category;

    public State(String state, double baseTax, Category category) {
        this.state = state;
        this.baseTax = baseTax;
        this.category = category;
    }


    public Category getCategory() {
        return category;
    }

    public String getState() {
        return state;
    }

    public double getBaseTax() {
        return baseTax;
    }
}