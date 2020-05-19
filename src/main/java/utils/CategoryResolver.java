package utils;

import application.ProductCategory;

/**
 * Temporary class to provide backward compatibility for Controller class
 */
public final class CategoryResolver {
	private CategoryResolver() {
		throw new IllegalStateException("Utility class");
	}
	
	public static ProductCategory getEnum(String category) {
		switch (category) {
			case "groceries":				return ProductCategory.GROCERIES;
			case "preparedFood":			return ProductCategory.PREPARED_FOOD;
			case "prescription-drug":		return ProductCategory.PRESCRIPTION_DRUG;
			case "Non-prescription-drug":	return ProductCategory.NONPRESCRIPTION_DRUG;
			case "clothing":				return ProductCategory.CLOTHING;
			case "intangibles":				return ProductCategory.INTANGIBLES;
			default:						return ProductCategory.UNDEFINED;
		}
	}
	
	public static String getString(ProductCategory category) {
		switch (category) {
			case GROCERIES:				return "groceries";
			case PREPARED_FOOD:			return "preparedFood";
			case PRESCRIPTION_DRUG:		return "prescription-drug";
			case NONPRESCRIPTION_DRUG:	return "Non-prescription-drug";
			case CLOTHING:				return "clothing";
			case INTANGIBLES:			return "intangibles";
			default:					return "undefined";
		}
	}
}
