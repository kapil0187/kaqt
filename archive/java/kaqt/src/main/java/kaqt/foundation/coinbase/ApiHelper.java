package kaqt.foundation.coinbase;

import kaqt.foundation.Utility;
import kaqt.foundation.Utility.HttpRequestMethodTypeEnum;

public class ApiHelper {
	
	public static String getAccountUrl(String account) {
		return EndPointRelatedConstants.ACCOUNTS_ENDPOINT + "/" + account;
	}

	public static String getAccountLedgerUrl(String account) {
		return getAccountUrl(account) + EndPointRelatedConstants.ACCOUNT_HISTORY_ENDPOINT_SUFFIX;
	}

	public static String getAccountHoldsUrl(String account) {
		return getAccountUrl(account) + EndPointRelatedConstants.ACCOUNT_HOLDS_ENDPOINT_SUFFIX;
	}

	public static String getOrderUrl(String orderId) {
		return EndPointRelatedConstants.ORDERS_ENDPOINT + "/" + orderId;
	}

	public static String generateRequestMessage(HttpRequestMethodTypeEnum type,
			String url, String timestamp, String body) {
		return timestamp + Utility.HttpRequestMethodType.get(type) + url
				+ (type == HttpRequestMethodTypeEnum.POST ? body : "");
	}
}
