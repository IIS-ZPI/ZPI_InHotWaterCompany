package application.product;

public class CategoryTax {
    private double groceries, preparedFood, prescriptionDrug, nonPrescriptionDrug, clothing, intangibles;

    public CategoryTax(double groceries, double preparedFood, double prescriptionDrug, double nonPrescriptionDrug, double clothing, double intangibles) {
        this.groceries = groceries;
        this.preparedFood = preparedFood;
        this.prescriptionDrug = prescriptionDrug;
        this.nonPrescriptionDrug = nonPrescriptionDrug;
        this.clothing = clothing;
        this.intangibles = intangibles;
    }

    public double getGroceries() {
        return groceries;
    }

    public double getPreparedFood() {
        return preparedFood;
    }

    public double getPrescriptionDrug() {
        return prescriptionDrug;
    }

    public double getNonPrescriptionDrug() {
        return nonPrescriptionDrug;
    }

    public double getClothing() {
        return clothing;
    }

    public double getIntangibles() {
        return intangibles;
    }
}