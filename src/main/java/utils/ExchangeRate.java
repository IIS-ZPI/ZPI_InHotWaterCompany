package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

public final class ExchangeRate {
	private static JSONObject data = null;
	
	private ExchangeRate() {
		throw new IllegalStateException("Utility class");
	}
	
	private static void fetchData() throws IOException {
		URL url = new URL("https://api.exchangeratesapi.io/latest?base=USD");
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openConnection().getInputStream()));
		StringBuilder builder = new StringBuilder();
		
		String line = bufferedReader.readLine();
		
		while (line != null) {
			builder.append(line);
			line = bufferedReader.readLine();
		}
		
		bufferedReader.close();
		
		try {
			data = new JSONObject(builder.toString()).getJSONObject("rates");
			
		} catch (JSONException e) {
			throw new IOException("Incorrect response form server");
		}
	}
	
	/**
	 * Returns exchange rate for specified European country currency.
	 * Rates are quoted against the USD.
	 * At first call, data is downloaded from remote server and cached to
	 * avoid excessive network traffic and speed up next calls.
	 * Data source: https://exchangeratesapi.io/
	 * 
	 * @param currencyCode - ISO 4217 currency code
	 * @return rate against to USD
	 * @throws IOException if can't connect to remote server
	 * @throws IllegalArgumentException if specified currency isn't supported
	 */
	public static double getFor(String currencyCode) throws IOException {
		if (data == null) {
			fetchData();
		}
		
		if (!data.has(currencyCode)) {
			throw new IllegalArgumentException("Unsupported currency");
		}
		
		return data.getDouble(currencyCode);
	}
}
