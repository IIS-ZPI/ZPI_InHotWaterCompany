package application.foreignTransport;

public class ImportCountry {
	private final String name;
	private final String code;
	private final String currencyCode;
	private final ImportCosts importCosts;
	
	public ImportCountry(String name, String code, String currencyCode, ImportCosts importCosts) {
		this.name = name;
		this.code = code;
		this.currencyCode = currencyCode;
		this.importCosts = importCosts;
	}
	
	public String getName() {
		return name;
	}
	
	public String getCode() {
		return code;
	}
	
	public String getCurrencyCode() {
		return currencyCode;
	}
	
	public ImportCosts getImportCosts() {
		return importCosts;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		
		builder.append(name).append(", ");
		builder.append(code).append(", ");
		builder.append(currencyCode).append(", ");
		builder.append(importCosts.toString());
		
		return builder.toString();
	}
}
