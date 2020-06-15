package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class ExchangeRate {
	private static HashMap<String, Double> data = new HashMap<>();
	
	private ExchangeRate() {
		throw new IllegalStateException("Utility class");
	}
	
	private static void fetchData() throws IOException {
		URL[] urls = {
			new URL("https://api.nbp.pl/api/exchangerates/tables/A/?format=json"),
			new URL("https://api.nbp.pl/api/exchangerates/tables/B/?format=json")
		};
		
		for (URL url : urls) {
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openConnection().getInputStream()));
			StringBuilder builder = new StringBuilder();
			
			String line = bufferedReader.readLine();
			
			while (line != null) {
				builder.append(line);
				line = bufferedReader.readLine();
			}
			
			bufferedReader.close();
			
			try {
				JSONArray rates = new JSONArray(builder.toString()).getJSONObject(0).getJSONArray("rates");
				
				for (int i = 0; i < rates.length(); i++) {
					JSONObject json = rates.getJSONObject(i);
					data.put(json.getString("code"), json.getDouble("mid"));
				}
				
			} catch (JSONException e) {
				throw new IOException("Incorrect response form server");
			}
		}
		
		data.put("PLN", 1.0);
		
		double usd = data.get("USD");
		
		for (Entry<String, Double> entry : data.entrySet()) {
			entry.setValue(usd / entry.getValue());
		}
	}
	
	/**
	 * Returns exchange rate for specified currency.
	 * Rates are quoted against the USD.
	 * At first call, data is downloaded from remote server and cached to
	 * avoid excessive network traffic and speed up next calls.
	 * Data source: https://api.nbp.pl/
	 * 
	 * @param currencyCode - ISO 4217 currency code
	 * @return rate against to USD
	 * @throws IOException if can't connect to remote server
	 * @throws IllegalArgumentException if specified currency isn't supported
	 */
	public static double getFor(String currencyCode) throws IOException {
		if (data.isEmpty()) {
			fetchData();
		}
		
		if (!data.containsKey(currencyCode)) {
			throw new IllegalArgumentException("Unsupported currency");
		}
		
		return data.get(currencyCode);
	}
}
