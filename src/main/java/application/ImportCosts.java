package application;

public class ImportCosts {
	private final double transportFee;
	private final double consumablesImportTariff;
	private final double othersImportTariff;
	
	public ImportCosts(double transportFee, double consumablesImportTariff, double othersImportTariff) {
		this.transportFee = transportFee;
		this.consumablesImportTariff = consumablesImportTariff;
		this.othersImportTariff = othersImportTariff;
	}
	
	public double getTransportFee() {
		return transportFee;
	}
	
	public double getConsumablesImportTariff() {
		return consumablesImportTariff;
	}
	
	public double getOthersImportTariff() {
		return othersImportTariff;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		
		builder.append("$").append(transportFee).append(", ");
		builder.append(consumablesImportTariff).append("%, ");
		builder.append(othersImportTariff).append("%");
		
		return builder.toString();
	}
}
