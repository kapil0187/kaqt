package kaqt.foundation.coinbase;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.time.Instant;

import kaqt.foundation.Utility;
import kaqt.foundation.Utility.HttpRequestMethodTypeEnum;

import org.apache.http.HttpHeaders;
import org.apache.http.HttpRequest;

public class RestAuthentication {
	private RestClientConfiguration config;
	
	public RestAuthentication(RestClientConfiguration config) {
		this.config = config;
	}
	
	public void addAutheticationHeaders(HttpRequest request,
			HttpRequestMethodTypeEnum type, String endpoint, String body)
			throws UnsupportedEncodingException, GeneralSecurityException {
		String timestamp = Instant.now().getEpochSecond() + "";
		String message = ApiHelper.generateRequestMessage(type, endpoint,
				timestamp, body);

		String signature = Utility.generateHmacSHA256Signature(message,
				config.getApiKeySecret());
		
		request.addHeader(HeaderRelatedConstants.ACCEPT, HeaderRelatedConstants.CONTENT_TYPE);
		request.addHeader(HeaderRelatedConstants.CB_ACCESS_KEY, config.getApiKay());
		request.addHeader(HeaderRelatedConstants.CB_ACCESS_SIGNATURE, signature);
		request.addHeader(HeaderRelatedConstants.CB_ACCESS_TIMESTAMP, timestamp);
		request.addHeader(HeaderRelatedConstants.CB_ACCESS_PASSPHRASE, config.getPassphrase());
	}
	
}
