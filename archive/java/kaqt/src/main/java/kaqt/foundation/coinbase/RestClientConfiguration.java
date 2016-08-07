package kaqt.foundation.coinbase;

public class RestClientConfiguration {
	private String apiKey;
	private String apiKeySecret;
	private String passphrase;
	
	public RestClientConfiguration(String apiKey, String apiKeySecret,String passphrase) {
		this.apiKey = apiKey;
		this.apiKeySecret = apiKeySecret;
		this.passphrase = passphrase;
	}

	public String getApiKay() {
		return apiKey;
	}

	public String getApiKeySecret() {
		return apiKeySecret;
	}

	public String getPassphrase() {
		return passphrase;
	}
}
