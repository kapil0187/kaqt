package kaqt.foundation;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import java.util.Base64;
import java.util.Hashtable;
import java.util.Map;

public class Utility {
	
	public static enum HttpRequestMethodTypeEnum {
		GET,
		POST,
		DELETE,
		PUT
	}
	
	public static Hashtable<HttpRequestMethodTypeEnum, String> HttpRequestMethodType = new Hashtable<HttpRequestMethodTypeEnum, String>() {
		private static final long serialVersionUID = 3537797092923784374L;
		{
			put(HttpRequestMethodTypeEnum.GET, "GET");
			put(HttpRequestMethodTypeEnum.POST, "POST");
			put(HttpRequestMethodTypeEnum.DELETE, "DELETE");
			put(HttpRequestMethodTypeEnum.PUT, "PUT");
		}
	};

	public static String generateHmacSHA256Signature(String message, String key)
			throws GeneralSecurityException, UnsupportedEncodingException {
		byte[] secretDecoded = Base64.getDecoder().decode(key);
        SecretKeySpec secretKey = new SecretKeySpec(secretDecoded, "HmacSHA256");
		Mac mac = Mac.getInstance("HmacSHA256");
		mac.init(secretKey);
		return Base64.getEncoder().encodeToString(mac.doFinal(message.getBytes()));
	}
	
	
	
}
