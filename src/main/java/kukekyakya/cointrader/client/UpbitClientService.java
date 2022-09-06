package kukekyakya.cointrader.client;

import static java.util.function.Function.*;
import static java.util.stream.Collectors.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import kukekyakya.cointrader.client.response.Account;
import kukekyakya.cointrader.client.response.Market;
import kukekyakya.cointrader.client.response.MinuteCandle;
import kukekyakya.cointrader.client.response.OrderResponse;
import kukekyakya.cointrader.client.response.Ticker;
import kukekyakya.cointrader.service.TokenService;
import lombok.RequiredArgsConstructor;

/**
 * UpbitClientService
 *
 * @author heejae.song
 * @since 2022. 08. 27.
 */
@Service
@RequiredArgsConstructor
public class UpbitClientService {
	private final UpbitClient upbitClient;
	private final TokenService tokenService;

	public List<Market> readAllMarkets() {
		String token = tokenService.gen();
		return upbitClient.readAllMarkets(token);
	}

	public List<MinuteCandle> readAllMinuteCandles(Integer unit, String market, Integer count) {
		String token = tokenService.gen();
		return upbitClient.readAllMinuteCandle(token, unit, market, count);
	}

	public List<Account> readAllAccounts() {
		String token = tokenService.gen();
		return upbitClient.readAllAccounts(token);
	}

	public Map<String, Account> readAllAccountsToMap() {
		return readAllAccounts().stream()
			.collect(toMap(Account::getCurrency, identity()));
	}

	public List<Ticker> readTickers(String market) {
		Map<String, String> params = new HashMap<>();
		params.put("markets", market);

		String token = tokenService.gen(params);
		return upbitClient.readTickers(token, market);
	}

	public OrderResponse buy(String market, String price) {
		Map<String, String> params = new HashMap<>();
		params.put("market", market);
		params.put("side", OrderParam.BUY.getSide());
		params.put("price", price);
		params.put("ord_type", OrderParam.BUY.getOrdType());

		String token = tokenService.gen(params);
		return upbitClient.order(token, market, OrderParam.BUY.getSide(),
			null, price, OrderParam.BUY.getOrdType());
	}

	public OrderResponse sell(String market, String volume) {
		Map<String, String> params = new HashMap<>();
		params.put("market", market);
		params.put("side", OrderParam.SELL.getSide());
		params.put("volume", volume);
		params.put("ord_type", OrderParam.SELL.getOrdType());

		String token = tokenService.gen(params);

		// OrderRequest request = OrderRequest.builder()
		// 	.market(market)
		// 	.side(OrderParam.SELL.getSide())
		// 	.volume(volume)
		// 	.ord_type(OrderParam.SELL.getOrdType())
		// 	.build();

		try {
			CloseableHttpClient client = HttpClientBuilder.create().build();
			HttpPost request = new HttpPost("https://api.upbit.com/v1/orders");
			request.setHeader("Content-Type", "application/json");
			request.addHeader("Authorization", token);
			request.setEntity(new StringEntity(new ObjectMapper().writeValueAsString(params)));

			CloseableHttpResponse response = client.execute(request);
			HttpEntity entity = response.getEntity();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
