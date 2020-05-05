package utils;

import application.ProductCategory;

public abstract class CategoryResolver {
	public static ProductCategory getEnum(String category) {
		switch (category) {
			case "groceries":				return ProductCategory.GROCERIES;
			case "Prepared-food":			return ProductCategory.PREPARED_FOOD;
			case "Prescription-drug":		return ProductCategory.PRESCRIPTION_DRUG;
			case "Non-prescription-drug":	return ProductCategory.NONPRESCRIPTION_DRUG;
			case "Clothing":				return ProductCategory.CLOTHING;
			case "Intangibles":				return ProductCategory.INTANGIBLES;
			default:						return ProductCategory.UNDEFINED;
		}
	}
	
	public static String getString(ProductCategory category) {
		switch (category) {
			case GROCERIES:				return "groceries";
			case PREPARED_FOOD:			return "Prepared-food";
			case PRESCRIPTION_DRUG:		return "Prescription-drug";
			case NONPRESCRIPTION_DRUG:	return "Non-prescription-drug";
			case CLOTHING:				return "Clothing";
			case INTANGIBLES:			return "Intangibles";
			default:					return "Undefined";
		}
	}
}
