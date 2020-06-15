package application;

public class DataInTable {
    private String state;
    private String priceWithoutTax;
    private String margin;
    private String logisticCost;

    public DataInTable(String state, String priceWithoutTax, String margin, String logisticCost) {
        this.state = state;
        this.priceWithoutTax = priceWithoutTax;
        this.margin = margin;
        this.logisticCost = logisticCost;
    }

    public String getState() {
        return state;
    }

    public String getMargin() {
        return margin;
    }

    public String getPriceWithoutTax() {
        return priceWithoutTax;
    }

    public String getLogisticCost() {
        return logisticCost;
    }
}
