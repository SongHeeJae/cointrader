package kukekyakya.cointrader.client.binance;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;
import org.springframework.stereotype.Service;

import kukekyakya.cointrader.client.binance.response.BinanceBalance;
import kukekyakya.cointrader.client.binance.response.BinanceCandle;
import kukekyakya.cointrader.client.binance.response.BinanceOrder;
import kukekyakya.cointrader.client.binance.response.BinancePosition;
import kukekyakya.cointrader.client.binance.response.BinanceSimpleTicker;
import kukekyakya.cointrader.client.binance.response.ChangeLeverageResponse;
import kukekyakya.cointrader.client.binance.response.ChangeMarginResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * BinanceClientService
 *
 * @author heejae.song
 * @since 2022. 09. 07.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BinanceClientService {
	private final BinanceClient binanceClient;

	private static final String API_KEY = "";
	private static final String SECRET_KEY = "";

	public List<BinanceCandle> readCandles(String symbol, CandleInterval interval, Integer limit) {
		try {
			List<List<Object>> result = binanceClient.readCandles(API_KEY, symbol,
				interval.getValue(), limit);

			return result.stream()
				.map(list -> BinanceCandle.builder()
					.openTime((Long)list.get(0))
					.openPrice((String)list.get(1))
					.highPrice((String)list.get(2))
					.lowPrice((String)list.get(3))
					.closePrice((String)list.get(4))
					.volume((String)list.get(5))
					.closeTime((Long)list.get(6))
					.quoteAssetVolume((String)list.get(7))
					.numberOfTrades((Integer)list.get(8))
					.takerBuyBaseAssetVolume((String)list.get(9))
					.takerBuyQuoteAssetVolume((String)list.get(10))
					.ignoreField((String)list.get(11))
					.build())
				.collect(Collectors.toList());
		} catch (Exception e) {
			e.printStackTrace();
			return List.of();
		}
	}

	public List<BinanceBalance> readUserBalances() {
		String timestamp = String.valueOf(Timestamp.valueOf(LocalDateTime.now()).getTime());
		List<QueryParam> params = List.of(new QueryParam("timestamp", timestamp));
		String queryString = getQueryString(params);
		try {
			return binanceClient.readAllUserBalances(API_KEY, timestamp, getSignature(queryString));
		} catch (Exception e) {
			e.printStackTrace();
			return List.of();
		}
	}

	public List<BinanceSimpleTicker> readAllTickers() {
		try {
			return binanceClient.readAllTickers(API_KEY);
		} catch (Exception e) {
			e.printStackTrace();
			return List.of();
		}
	}

	public BinanceSimpleTicker readTicker(String symbol) {
		try {
			return binanceClient.readTicker(API_KEY, symbol);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<BinancePosition> readAllPositions() {
		String timestamp = String.valueOf(Timestamp.valueOf(LocalDateTime.now()).getTime());
		List<QueryParam> params = List.of(new QueryParam("timestamp", timestamp));

		String queryString = getQueryString(params);

		try {
			return binanceClient.readAllPositions(
				API_KEY,
				timestamp,
				getSignature(queryString)
			);
		} catch (Exception e) {
			e.printStackTrace();
			return List.of();
		}
	}

	public BinancePosition readPosition(String symbol) {
		String timestamp = String.valueOf(Timestamp.valueOf(LocalDateTime.now()).getTime());
		List<QueryParam> params = List.of(new QueryParam("symbol", symbol), new QueryParam("timestamp", timestamp));
		String queryString = getQueryString(params);
		try {
			return binanceClient.readPosition(
				API_KEY,
				symbol,
				timestamp,
				getSignature(queryString)
			);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public BinanceOrder buyOrder(String symbol, OrderSide side, OrderType type, Double quantity) {
		quantity = Double.valueOf(quantity.intValue());
		String timestamp = String.valueOf(Timestamp.valueOf(LocalDateTime.now()).getTime());
		List<QueryParam> params = List.of(
			new QueryParam("symbol", symbol),
			new QueryParam("side", side.getValue()),
			new QueryParam("positionSide", PositionSide.BOTH.getValue()),
			new QueryParam("type", type.getValue()),
			new QueryParam("quantity", String.valueOf(quantity)),
			new QueryParam("timestamp", timestamp));

		String queryString = getQueryString(params);

		try {
			return binanceClient.buyOrder(
				API_KEY,
				symbol,
				side.getValue(),
				PositionSide.BOTH.getValue(),
				type.getValue(),
				quantity,
				timestamp,
				getSignature(queryString)
			);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public BinanceOrder cancelOrder(String symbol) {
		String timestamp = String.valueOf(Timestamp.valueOf(LocalDateTime.now()).getTime());
		List<QueryParam> params = List.of(
			new QueryParam("symbol", symbol),
			new QueryParam("timestamp", timestamp));

		String queryString = getQueryString(params);

		try {
			return binanceClient.cancelOrder(
				API_KEY,
				symbol,
				timestamp,
				getSignature(queryString)
			);
		} catch (Exception e) {
			return null;
		}
	}

	// close position
	public BinanceOrder sellOrder(String symbol, OrderSide side, OrderType type, Double stopPrice, String tickSize) {
		String timestamp = String.valueOf(Timestamp.valueOf(LocalDateTime.now()).getTime());
		List<QueryParam> params = List.of(
			new QueryParam("symbol", symbol),
			new QueryParam("side", side.getValue()),
			new QueryParam("positionSide", PositionSide.BOTH.getValue()),
			new QueryParam("type", type.getValue()),
			new QueryParam("closePosition", "true"),
			new QueryParam("stopPrice", String.format(tickSize, stopPrice)),
			new QueryParam("timestamp", timestamp));

		String queryString = getQueryString(params);

		try {
			return binanceClient.sellOrder(
				API_KEY,
				symbol,
				side.getValue(),
				PositionSide.BOTH.getValue(),
				type.getValue(),
				"true",
				String.format(tickSize, stopPrice),
				timestamp,
				getSignature(queryString)
			);
		} catch (Exception e) {
			try {
				TimeUnit.MILLISECONDS.sleep(30);
			} catch (InterruptedException ex) { }
			log.info("판매 주문 실패 symbol = {}, side = {}, type = {}, stopPrice = {}, tickSize = {}", symbol, side.getValue(),
				type.getValue(), stopPrice, tickSize);

			boolean precisionError = e.getMessage().indexOf("1111") != -1;
			String nextTickSize = precisionError ? nextTickSize(tickSize) : tickSize;

			if(OrderSide.BUY == side) { // short
				if(OrderType.TAKE_PROFIT_MARKET == type) { // TAKE_PROFIT_MARKET
					return sellOrder(symbol, side, type, precisionError ? stopPrice : stopPrice * 0.9999, nextTickSize);
				} else { // STOP_MARKET
					return sellOrder(symbol, side, type, precisionError ? stopPrice : stopPrice * 1.0001, nextTickSize);
				}
			} else { // long
				if(OrderType.TAKE_PROFIT_MARKET == type) { // TAKE_PROFIT_MARKET
					return sellOrder(symbol, side, type, precisionError ? stopPrice : stopPrice * 1.0001, nextTickSize);
				} else { // STOP_MARKET
					return sellOrder(symbol, side, type, precisionError ? stopPrice : stopPrice * 0.9999, nextTickSize);
				}
			}

		}
	}

	private String nextTickSize(String tickSize) {
		if("%.8f".equalsIgnoreCase(tickSize)) {
			return "%.7f";
		} else if("%.7f".equalsIgnoreCase(tickSize)) {
			return "%.6f";
		} else if("%.6f".equalsIgnoreCase(tickSize)) {
			return "%.5f";
		} else if("%.5f".equalsIgnoreCase(tickSize)) {
			return "%.4f";
		} else if("%.4f".equalsIgnoreCase(tickSize)) {
			return "%.3f";
		} else if("%.3f".equalsIgnoreCase(tickSize)) {
			return "%.2f";
		} else if("%.2f".equalsIgnoreCase(tickSize)) {
			return "%.1f";
		} else {
			return "%.0f";
		}
	}

	public ChangeLeverageResponse changeLeverage(String symbol, Integer leverage) {
		try {
			String timestamp = String.valueOf(Timestamp.valueOf(LocalDateTime.now()).getTime());
			List<QueryParam> params = List.of(
				new QueryParam("symbol", symbol),
				new QueryParam("leverage", String.valueOf(leverage)),
				new QueryParam("timestamp", timestamp));

			String queryString = getQueryString(params);
			return binanceClient.changeLeverage(
				API_KEY,
				symbol,
				leverage,
				timestamp,
				getSignature(queryString));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public ChangeMarginResponse changeMarginType(String symbol, MarginType marginType) {
		try {
			String timestamp = String.valueOf(Timestamp.valueOf(LocalDateTime.now()).getTime());
			List<QueryParam> params = List.of(
				new QueryParam("symbol", symbol),
				new QueryParam("marginType", marginType.getValue()),
				new QueryParam("timestamp", timestamp));

			String queryString = getQueryString(params);
			return binanceClient.changeMarginType(
				API_KEY,
				symbol,
				marginType.getValue(),
				timestamp,
				getSignature(queryString));
		} catch (Exception e) {
			// e.printStackTrace();
			return null;
		}
	}

	public void changePositionMode(boolean dualSidePosition) {
		try {
			String timestamp = String.valueOf(Timestamp.valueOf(LocalDateTime.now()).getTime());
			List<QueryParam> params = List.of(
				new QueryParam("dualSidePosition", dualSidePosition ? "true" : "false"),
				new QueryParam("timestamp", timestamp));

			String queryString = getQueryString(params);
			binanceClient.changePositionMode(
				API_KEY,
				dualSidePosition ? "true" : "false",
				timestamp,
				getSignature(queryString));
		} catch (Exception e) {
			// empty
		}
	}

	private String getSignature(String queryString) {
		try {
			Mac hmacSHA256 = Mac.getInstance("HmacSHA256");
			SecretKeySpec secretKeySpec = new SecretKeySpec(SECRET_KEY.getBytes(), "HmacSHA256");
			hmacSHA256.init(secretKeySpec);
			return new String(Hex.encodeHex(hmacSHA256.doFinal(queryString.getBytes())));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		}
		return "";
	}

	private String getQueryString(List<QueryParam> params) {
		return params.stream()
			.map(param -> param.getKey() + "=" + param.getValue())
			.collect(Collectors.joining("&"));
	}

	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	private static class QueryParam {
		private String key;
		private String value;
	}
}
