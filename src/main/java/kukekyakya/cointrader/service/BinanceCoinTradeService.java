package kukekyakya.cointrader.service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import kukekyakya.cointrader.client.binance.BinanceClientService;
import kukekyakya.cointrader.client.binance.CandleInterval;
import kukekyakya.cointrader.client.binance.MarginType;
import kukekyakya.cointrader.client.binance.OrderSide;
import kukekyakya.cointrader.client.binance.OrderType;
import kukekyakya.cointrader.client.binance.PositionSide;
import kukekyakya.cointrader.client.binance.response.BinanceBalance;
import kukekyakya.cointrader.client.binance.response.BinanceCandle;
import kukekyakya.cointrader.client.binance.response.BinanceOrder;
import kukekyakya.cointrader.client.binance.response.BinancePosition;
import kukekyakya.cointrader.client.binance.response.BinanceSimpleTicker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * BinanceCoinTradeService
 *
 * @author heejae.song
 * @since 2022. 09. 07.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BinanceCoinTradeService {
	private final BinanceClientService binanceClientService;

	private static final CandleInterval CANDLE_INTERVAL = CandleInterval.MINUTE_15;
	private static final int COUNT = 1; // {CANDLE_INTERVAL}봉 COUNT개 조회

	private static final double DIFF_PERCENT = 0.02; // 봉의 시가, 현재 종가 기준으로 이 정도 이상 차이나는 경우
	private static final double SELL_PERCENT_P = 0.01; // 양(이득)일 떄 판매 퍼센트 설정
	private static final double SELL_PERCENT_M = 0.01; // 음(손해)일 떄 판매 퍼센트 설정
	private static final long MINUS_CANDLE_COUNT = 12; // COUNT 보다 작아야한다.

	private static final double START_BUY_MONEY = 10; // 10 USDT
	private static final double MINIMUM_PRICE = 0; // 구매하고자 하는 코인의 최소 개당 금액

	private static final MarginType MARGIN_TYPE = MarginType.ISOLATED;
	private static final Integer LEVERAGE = 10;
	private static final PositionSide POSITION_SIDE = PositionSide.SHORT;
	private static final Boolean IS_SHORT = Boolean.TRUE;

	private static final Map<String, LocalDateTime> BUY_LOCK_DATE_TIME = new ConcurrentHashMap<>();

	private static final AtomicBoolean FLAG = new AtomicBoolean(false);

	private static final String MY_CURRENCY = "USDT";

	@Async
	public void start() {
		if(FLAG.get()) {
			return;
		}

		log.info("coin trade start");

		FLAG.set(true);

		binanceClientService.changePositionMode(false);

		WinRate winRate = WinRate.init(10);

		while(FLAG.get()) {
			Set<String> alreadyBuyCoin = new HashSet<>();

			List<BinanceBalance> balances = binanceClientService.readUserBalances();

			double myMoney = 0;
			for (BinanceBalance binanceBalance : balances) {
				String assetName = binanceBalance.getAsset();
				double balance = Double.valueOf(binanceBalance.getBalance());
				if (assetName.equalsIgnoreCase(MY_CURRENCY)) {
					myMoney += balance;
				}
			}

			// 포지션 조회
			List<BinancePosition> positions = binanceClientService.readAllPositions();
			for (BinancePosition position : positions) {
				String symbol = position.getSymbol();
				double entryPrice = Double.valueOf(position.getEntryPrice());
				double markPrice = Double.valueOf(position.getMarkPrice());
				double percent = calPercent(entryPrice, markPrice) * LEVERAGE;
				if(entryPrice <= 0) continue;

				myMoney -= Double.valueOf(position.getIsolatedWallet());

				// if (Math.abs(percent) >= SELL_PERCENT_P) {
				// if(true) {
				// 	log.info("판매 완료 symbol = {}, percent = {}", symbol, String.format("%.4f", percent));
					// binanceClientService.sellOrder(symbol, IS_SHORT ? OrderSide.BUY : OrderSide.SELL, OrderType.TAKE_PROFIT_MARKET);
					// BUY_LOCK_DATE_TIME.put(symbol, LocalDateTime.now().plusMinutes(CANDLE_INTERVAL.getMinutes() + 1));
				// } else {
					alreadyBuyCoin.add(symbol);
				// }
			}

			log.info("현재 잔액 : {}", myMoney);

			if(myMoney <= START_BUY_MONEY) {
				sleep(500);
				continue;
			}

			List<BinanceSimpleTicker> tickers = binanceClientService.readAllTickers();

			for (BinanceSimpleTicker ticker : tickers) {
				String symbol = ticker.getSymbol();
				if (!symbol.substring(symbol.length() - 4).equalsIgnoreCase(MY_CURRENCY)) {
					continue;
				}

				Double price = Double.valueOf(ticker.getPrice());
				if (price <= MINIMUM_PRICE)
					continue;

				List<BinanceCandle> binanceCandles = binanceClientService.readCandles(symbol, CANDLE_INTERVAL, COUNT);

				BinanceCandle newestCandle = binanceCandles.get(binanceCandles.size() - 1); // 마지막 캔들이 최신
				BinanceCandle oldestCandle = binanceCandles.get(0);
				double percent = calPercent(Double.valueOf(oldestCandle.getOpenPrice()),
					Double.valueOf(newestCandle.getClosePrice()));

				if (percent >= DIFF_PERCENT) {
					LocalDateTime lockDateTime = BUY_LOCK_DATE_TIME.get(symbol);
					if (lockDateTime != null) {
						if (lockDateTime.isBefore(LocalDateTime.now())) {
							BUY_LOCK_DATE_TIME.remove(symbol);
						} else {
							continue;
						}
					}

					if (alreadyBuyCoin.contains(symbol)) continue;

					double stopPriceWhenUp = IS_SHORT ? price * (1 - SELL_PERCENT_P) : price * (1 + SELL_PERCENT_P);
					double stopPriceWhenDown = IS_SHORT ? price * (1 + SELL_PERCENT_M) : price * (1 - SELL_PERCENT_M);

					log.info("구매 완료 symbol = {}, percent = {}", symbol, String.format("%.4f", percent));

					binanceClientService.cancelOrder(symbol);

					binanceClientService.changeMarginType(symbol, MARGIN_TYPE);
					binanceClientService.changeLeverage(symbol, LEVERAGE);

					BinanceOrder buyOrderResponse = binanceClientService.buyOrder(symbol,
						IS_SHORT ? OrderSide.SELL : OrderSide.BUY, OrderType.MARKET,
						START_BUY_MONEY * LEVERAGE / price);
					if(buyOrderResponse != null) {
						binanceClientService.sellOrder(symbol, IS_SHORT ? OrderSide.BUY : OrderSide.SELL,
							OrderType.TAKE_PROFIT_MARKET, stopPriceWhenUp, "%.8f");
						binanceClientService.sellOrder(symbol, IS_SHORT ? OrderSide.BUY : OrderSide.SELL,
							OrderType.STOP_MARKET, stopPriceWhenDown, "%.8f");
					}

					BUY_LOCK_DATE_TIME.put(symbol, LocalDateTime.now().plusMinutes(CANDLE_INTERVAL.getMinutes() * 2 + 1));
				}
			}
		}

		log.info("coin trade end");
	}

	private double calPercent(double start, double end) {
		return end > start ? (end / start) - 1 : -((start / end) - 1);
	}

	private void sleep(long milliseconds) {
		try {
			TimeUnit.MILLISECONDS.sleep(milliseconds);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void end() {
		FLAG.set(false);
	}

}
