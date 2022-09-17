package kukekyakya.cointrader.upbit.client;

import kukekyakya.cointrader.upbit.service.TokenService;
import lombok.RequiredArgsConstructor;

/**
 * UpbitClientService
 *
 * @author heejae.song
 * @since 2022. 08. 27.
 */
// @Service
@RequiredArgsConstructor
public class UpbitClientService {
	private final UpbitClient upbitClient;
	private final TokenService tokenService;

	// public List<Market> readAllMarkets() {
	// 	try {
	// 		String token = tokenService.gen();
	// 		return upbitClient.readAllMarkets(token);
	// 	} catch (Exception e) {
	// 		e.printStackTrace();
	// 	}
	// 	return List.of();
	// }
	//
	// public List<MinuteCandle> readAllMinuteCandles(Integer unit, String market, Integer count) {
	// 	try {
	// 		String token = tokenService.gen();
	// 		return upbitClient.readAllMinuteCandle(token, unit, market, count);
	// 	} catch (Exception e) {
	// 		e.printStackTrace();
	// 	}
	// 	return List.of();
	// }
	//
	// public List<Account> readAllAccounts() {
	// 	try {
	// 		String token = tokenService.gen();
	// 		return upbitClient.readAllAccounts(token);
	// 	} catch (Exception e) {
	// 		e.printStackTrace();
	// 	}
	// 	return List.of();
	// }
	//
	// public Map<String, Account> readAllAccountsToMap() {
	// 	return readAllAccounts().stream()
	// 		.collect(toMap(Account::getCurrency, identity()));
	// }
	//
	// public List<Ticker> readTickers(String market) {
	// 	try {
	// 		Map<String, String> params = new HashMap<>();
	// 		params.put("markets", market);
	//
	// 		String token = tokenService.gen(params);
	// 		return upbitClient.readTickers(token, market);
	// 	} catch (Exception e) {
	// 		e.printStackTrace();
	// 	}
	// 	return List.of();
	// }
	//
	// public OrderResponse buy(String market, String price) {
	// 	try {
	// 		Map<String, String> params = new HashMap<>();
	// 		params.put("market", market);
	// 		params.put("side", OrderParam.BUY.getSide());
	// 		params.put("price", price);
	// 		params.put("ord_type", OrderParam.BUY.getOrdType());
	//
	// 		String token = tokenService.gen(params);
	// 		return upbitClient.order(token, market, OrderParam.BUY.getSide(),
	// 			null, price, OrderParam.BUY.getOrdType());
	// 	} catch (Exception e) {
	// 		e.printStackTrace();
	// 		return null;
	// 	}
	// }
	//
	// public OrderResponse sell(String market, String volume) {
	// 	Map<String, String> params = new HashMap<>();
	// 	params.put("market", market);
	// 	params.put("side", OrderParam.SELL.getSide());
	// 	params.put("volume", volume);
	// 	params.put("ord_type", OrderParam.SELL.getOrdType());
	//
	// 	String token = tokenService.gen(params);
	//
	//
	// 	try {
	// 		CloseableHttpClient client = HttpClientBuilder.create().build();
	// 		HttpPost request = new HttpPost("https://api.upbit.com/v1/orders");
	// 		request.setHeader("Content-Type", "application/json");
	// 		request.addHeader("Authorization", token);
	// 		request.setEntity(new StringEntity(new ObjectMapper().writeValueAsString(params)));
	//
	// 		CloseableHttpResponse response = client.execute(request);
	// 		HttpEntity entity = response.getEntity();
	// 	} catch (Exception e) {
	// 		e.printStackTrace();
	// 	}
	// 	return null;
	// }
}
