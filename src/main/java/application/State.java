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

    public double calcualteOfTax(double price, double category) {
        if (category == 0)
            return 0;

        return price * (category / 100);
    }

    public double calcualteAfterTax(double price, double category) {
        if (category == 0)
            return price;

        return price * (1 + (category / 100));
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