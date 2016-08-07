package kaqt.orderex.coinbase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.time.Instant;

import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import kaqt.foundation.Utility.HttpRequestMethodTypeEnum;
import kaqt.foundation.coinbase.ApiHelper;
import kaqt.foundation.coinbase.EndPointRelatedConstants;
import kaqt.foundation.coinbase.HeaderRelatedConstants;
import kaqt.foundation.coinbase.RestAuthentication;
import kaqt.foundation.coinbase.RestClientConfiguration;

public class AuthenticatedRestClient {

	private RestAuthentication auth;
	private HttpClient client;
	private Gson gson;
	private JsonParser parser;
	private String apiUrl;

	public AuthenticatedRestClient(RestClientConfiguration config, String apiUrl) {
		this.auth = new RestAuthentication(config);
		this.client = HttpClientBuilder.create().build();
		this.apiUrl = apiUrl;
		this.gson = new Gson();
		this.parser = new JsonParser();
	}

	public Account[] getAccounts() throws GeneralSecurityException,
			ClientProtocolException, IOException {
		HttpRequestMethodTypeEnum requestType = HttpRequestMethodTypeEnum.GET;
		String endpoint = EndPointRelatedConstants.ACCOUNTS_ENDPOINT;
		String request = ApiHelper.generateRequestMessage(requestType,
				endpoint, Instant.now().getEpochSecond() + "", "");
		HttpGet getRequest = new HttpGet(apiUrl + endpoint);
		auth.addAutheticationHeaders(getRequest, requestType, endpoint, request);
		HttpResponse response = client.execute(getRequest);
		return gson.fromJson(getResponseAsJson(response), Account[].class);
	}

	public OpenOrder[] getOpenOrders() throws GeneralSecurityException,
			ClientProtocolException, IOException {
		HttpRequestMethodTypeEnum requestType = HttpRequestMethodTypeEnum.GET;
		String endpoint = EndPointRelatedConstants.ORDERS_ENDPOINT;
		String request = ApiHelper.generateRequestMessage(requestType,
				endpoint, Instant.now().getEpochSecond() + "", "");
		HttpGet getRequest = new HttpGet(apiUrl + endpoint);
		auth.addAutheticationHeaders(getRequest, requestType, endpoint, request);
		HttpResponse response = client.execute(getRequest);
		return gson.fromJson(getResponseAsJson(response), OpenOrder[].class);
	}

	public Order getOrder(String orderId) throws GeneralSecurityException,
			ClientProtocolException, IOException {
		HttpRequestMethodTypeEnum requestType = HttpRequestMethodTypeEnum.GET;
		String endpoint = EndPointRelatedConstants.ORDERS_ENDPOINT + "/"
				+ orderId;
		String request = ApiHelper.generateRequestMessage(requestType,
				endpoint, Instant.now().getEpochSecond() + "", "");
		HttpGet getRequest = new HttpGet(apiUrl + endpoint);
		auth.addAutheticationHeaders(getRequest, requestType, endpoint, request);
		HttpResponse response = client.execute(getRequest);
		return gson.fromJson(getResponseAsJson(response), Order.class);
	}

	public Fill[] getFills() throws GeneralSecurityException,
			ClientProtocolException, IOException {
		HttpRequestMethodTypeEnum requestType = HttpRequestMethodTypeEnum.GET;
		String endpoint = EndPointRelatedConstants.FILLS_ENDPOINT;
		String request = ApiHelper.generateRequestMessage(requestType,
				endpoint, Instant.now().getEpochSecond() + "", "");
		HttpGet getRequest = new HttpGet(apiUrl + endpoint);
		auth.addAutheticationHeaders(getRequest, requestType, endpoint, request);
		HttpResponse response = client.execute(getRequest);
		return gson.fromJson(getResponseAsJson(response), Fill[].class);
	}

	public Account getAccount(String id) throws GeneralSecurityException,
			ClientProtocolException, IOException {
		HttpRequestMethodTypeEnum requestType = HttpRequestMethodTypeEnum.GET;
		String endpoint = EndPointRelatedConstants.ACCOUNTS_ENDPOINT + "/" + id;
		String request = ApiHelper.generateRequestMessage(requestType,
				endpoint, Instant.now().getEpochSecond() + "", "");
		HttpGet getRequest = new HttpGet(apiUrl + endpoint);
		auth.addAutheticationHeaders(getRequest, requestType, endpoint, request);
		HttpResponse response = client.execute(getRequest);
		return gson.fromJson(getResponseAsJson(response), Account.class);
	}

	public Hold getHolds(String id) throws GeneralSecurityException,
			ClientProtocolException, IOException {
		HttpRequestMethodTypeEnum requestType = HttpRequestMethodTypeEnum.GET;
		String endpoint = EndPointRelatedConstants.ACCOUNTS_ENDPOINT + "/" + id
				+ EndPointRelatedConstants.ACCOUNT_HOLDS_ENDPOINT_SUFFIX;
		String request = ApiHelper.generateRequestMessage(requestType,
				endpoint, Instant.now().getEpochSecond() + "", "");
		HttpGet getRequest = new HttpGet(apiUrl + endpoint);
		auth.addAutheticationHeaders(getRequest, requestType, endpoint, request);
		HttpResponse response = client.execute(getRequest);
		return gson.fromJson(getResponseAsJson(response), Hold.class);
	}

	public AccountHistory getAccountHistory(String id)
			throws GeneralSecurityException, ClientProtocolException,
			IOException {
		HttpRequestMethodTypeEnum requestType = HttpRequestMethodTypeEnum.GET;
		String endpoint = EndPointRelatedConstants.ACCOUNTS_ENDPOINT + "/" + id
				+ EndPointRelatedConstants.ACCOUNT_HISTORY_ENDPOINT_SUFFIX;
		String request = ApiHelper.generateRequestMessage(requestType,
				endpoint, Instant.now().getEpochSecond() + "", "");
		HttpGet getRequest = new HttpGet(apiUrl + endpoint);
		auth.addAutheticationHeaders(getRequest, requestType, endpoint, request);
		HttpResponse response = client.execute(getRequest);
		return gson.fromJson(getResponseAsJson(response), AccountHistory.class);
	}

	public Product[] getProducts() throws GeneralSecurityException,
			ClientProtocolException, IOException {
		HttpRequestMethodTypeEnum requestType = HttpRequestMethodTypeEnum.GET;
		String endpoint = EndPointRelatedConstants.PRODUCTS_ENDPOINT;
		String request = ApiHelper.generateRequestMessage(requestType,
				endpoint, Instant.now().getEpochSecond() + "", "");
		HttpGet getRequest = new HttpGet(apiUrl + endpoint);
		auth.addAutheticationHeaders(getRequest, requestType, endpoint, request);
		HttpResponse response = client.execute(getRequest);
		return gson.fromJson(getResponseAsJson(response), Product[].class);
	}

	public ProductStatistics getProductStatistics(String productId)
			throws GeneralSecurityException, ClientProtocolException,
			IOException {
		HttpRequestMethodTypeEnum requestType = HttpRequestMethodTypeEnum.GET;
		String endpoint = EndPointRelatedConstants.PRODUCTS_ENDPOINT + "/"
				+ productId
				+ EndPointRelatedConstants.PRODUCTS_STATISTICS_ENDPOINT_SUFFIX;
		String request = ApiHelper.generateRequestMessage(requestType,
				endpoint, Instant.now().getEpochSecond() + "", "");
		HttpGet getRequest = new HttpGet(apiUrl + endpoint);
		auth.addAutheticationHeaders(getRequest, requestType, endpoint, request);
		HttpResponse response = client.execute(getRequest);
		return gson.fromJson(getResponseAsJson(response),
				ProductStatistics.class);
	}

	public NewOrderResponse sendNewOrderRequest(NewOrderRequest order) throws IllegalStateException, IOException, GeneralSecurityException {
		String endpoint = EndPointRelatedConstants.ORDERS_ENDPOINT;
		HttpRequestMethodTypeEnum type = HttpRequestMethodTypeEnum.POST;
		String body = gson.toJson(order);
		HttpPost postRequest = new HttpPost(apiUrl + endpoint);
		auth.addAutheticationHeaders(postRequest, type, endpoint, body);
		postRequest.addHeader("content-type", "application/json");
		postRequest.setEntity(new StringEntity(body));
		HttpResponse response = client.execute(postRequest);
		return gson.fromJson(getResponseAsJson(response), NewOrderResponse.class);
	}

	public String deleteOpenOrder(String orderId)
			throws GeneralSecurityException, ClientProtocolException,
			IOException {
		HttpRequestMethodTypeEnum requestType = HttpRequestMethodTypeEnum.DELETE;
		String endpoint = EndPointRelatedConstants.ORDERS_ENDPOINT + "/" + orderId;
		String request = ApiHelper.generateRequestMessage(requestType,
				endpoint, Instant.now().getEpochSecond() + "", "");
		HttpDelete deleteRequest = new HttpDelete(apiUrl + endpoint);
		auth.addAutheticationHeaders(deleteRequest, requestType, endpoint,
				request);
		HttpResponse response = client.execute(deleteRequest);
		return this.getResponseAsJson(response).getAsString();
	}

	private JsonElement getResponseAsJson(HttpResponse response)
			throws IllegalStateException, IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(response
				.getEntity().getContent()));
		JsonElement root = parser.parse(br);
		return root;
	}

}
