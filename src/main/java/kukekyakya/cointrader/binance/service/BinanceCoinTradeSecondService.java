package kukekyakya.cointrader.binance.service;

import static kukekyakya.cointrader.binance.BinaneConstants.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import kukekyakya.cointrader.binance.client.BinanceClientService;
import kukekyakya.cointrader.binance.client.CandleInterval;
import kukekyakya.cointrader.binance.client.OrderSide;
import kukekyakya.cointrader.binance.client.OrderType;
import kukekyakya.cointrader.binance.client.response.BinanceBalance;
import kukekyakya.cointrader.binance.client.response.BinanceCandle;
import kukekyakya.cointrader.binance.client.response.BinanceOrder;
import kukekyakya.cointrader.binance.client.response.BinancePosition;
import kukekyakya.cointrader.binance.client.response.BinanceSimpleTicker;
import kukekyakya.cointrader.utils.WinRate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * BinanceCoinTradeSecondService
 *
 * @author heejae.song
 * @since 2022. 09. 10.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BinanceCoinTradeSecondService {
	private final BinanceClientService binanceClientService;

	private static final AtomicBoolean FLAG = new AtomicBoolean(false);

	@Async
	public void start() {
		if(FLAG.get()) {
			return;
		}

		log.info("coin trade start");

		FLAG.set(true);

		binanceClientService.changePositionMode(false);

		WinRate winRate = WinRate.init(2);

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
				double percent = calPercent(entryPrice, markPrice) * LEVERAGE.get();
				if(entryPrice <= 0) continue;

				myMoney -= Double.valueOf(position.getIsolatedWallet());

				alreadyBuyCoin.add(symbol);

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

				List<BinanceCandle> binanceCandles = binanceClientService.readCandles(symbol, CANDLE_INTERVAL, 10);
				if(binanceCandles.size() < 10) continue;

				BinanceCandle newestCandle = binanceCandles.get(9); // 마지막 캔들이 최신
				BinanceCandle middleCandle = binanceCandles.get(8);
				BinanceCandle oldestCandle = binanceCandles.get(7);
				BinanceCandle maxOldestCandle = binanceCandles.get(6);
				BinanceCandle maxMaxOldestCandle = binanceCandles.get(5);

				double oldestPercent = calPercent(Double.valueOf(oldestCandle.getOpenPrice()),
					Double.valueOf(oldestCandle.getClosePrice()));
				double middlePercent = calPercent(Double.valueOf(middleCandle.getOpenPrice()),
					Double.valueOf(middleCandle.getClosePrice()));
				double newestPercent = calPercent(Double.valueOf(newestCandle.getOpenPrice()),
					Double.valueOf(newestCandle.getClosePrice()));

				double maxPrice = Double.valueOf(binanceCandles.get(0).getClosePrice());
				for(int i=1; i<7; i++) {
					maxPrice = Math.max(maxPrice, Double.valueOf(binanceCandles.get(i).getClosePrice()));
				}

				// 급락 후 급등하는 종목 방지. 최근 10개 봉의 최고가가 최신가보다 크면, continue;
				// if(maxPrice >= Double.valueOf(newestCandle.getClosePrice())) {
				// 	continue;
				// }

				// double highPercent = calPercent(Double.valueOf(newestCandle.getOpenPrice()),
				// 	Double.valueOf(newestCandle.getHighPrice()));
				// double lowPercent = calPercent(Double.valueOf(newestCandle.getOpenPrice()),
				// 	Double.valueOf(newestCandle.getLowPrice()));
				// double closePercent = calPercent(Double.valueOf(newestCandle.getOpenPrice()),
				// 	Double.valueOf(newestCandle.getClosePrice()));

				double newestCloseAndHighDiffPercent = calPercent(Double.valueOf(newestCandle.getClosePrice()),
					Double.valueOf(newestCandle.getHighPrice()));
				double newestLowAndOpenDiffPercent = calPercent(Double.valueOf(newestCandle.getLowPrice()),
					Double.valueOf(newestCandle.getOpenPrice()));

				double middleCloseAndHighDiffPercent = calPercent(Double.valueOf(middleCandle.getClosePrice()),
					Double.valueOf(middleCandle.getHighPrice()));

				List<BinanceCandle> minuteCandles = binanceClientService.readCandles(symbol, CandleInterval.MINUTE_1, 1);

				// 윗꼬리 있고, 아랫꼬리 없는 상태.
				if (
					(calPercent(Double.valueOf(middleCandle.getLowPrice()), Double.valueOf(newestCandle.getClosePrice())) >= DIFF_PERCENT
						// && calPercent(Double.valueOf(middleCandle.getHighPrice()), Double.valueOf(newestCandle.getClosePrice())) <= -0.003 // 윗 꼬리 갭
						// && lowAndOpenDiffPercent <= 0.002 // 아랫 꼬리 갭. 윗 꼬리 이후, 아랫 꼬리 남기면서 다시 올라오는 종목 방지
						// && (minuteCandles.get(0).isMinusCandle())

						// && newestCandle.isPlusCandle()
						// && middleCandle.isPlusCandle()
						// && oldestCandle.isPlusCandle()
						// && (maxOldestCandle.isPlusCandle() || maxMaxOldestCandle.isPlusCandle())
					)

					// ||

					// (newestPercent >= DIFF_PERCENT
					// 	// && calPercent(Double.valueOf(newestCandle.getHighPrice()), Double.valueOf(newestCandle.getClosePrice())) >= 0.003
					// 	// && (minuteCandles.get(0).isMinusCandle())
					// 	&& middleCandle.isPlusCandle()
					// 	&& oldestCandle.isPlusCandle()
						// && (maxOldestCandle.isPlusCandle() || maxMaxOldestCandle.isPlusCandle())
					// )
				) {
					LocalDateTime lockDateTime = BUY_LOCK_DATE_TIME.get(symbol);
					if (lockDateTime != null) {
						if (lockDateTime.isBefore(LocalDateTime.now())) {
							BUY_LOCK_DATE_TIME.remove(symbol);
						} else {
							continue;
						}
					}

					if (alreadyBuyCoin.contains(symbol)) continue;

					double stopPriceWhenUp = IS_SHORT.get() ? price * (1 - SELL_PERCENT_P) : price * (1 + SELL_PERCENT_P);
					double stopPriceWhenDown = IS_SHORT.get() ? price * (1 + SELL_PERCENT_M) : price * (1 - SELL_PERCENT_M);

					log.info("구매 완료 symbol = {}, oldestPercent = {}, closeAndHighDiffPercent = {}, lowAndOpenDiffPercent = {}",
						symbol, String.format("%.4f", middlePercent),
						String.format("%.4f", newestCloseAndHighDiffPercent), String.format("%.4f", newestLowAndOpenDiffPercent));

					// binanceClientService.cancelOrder(symbol);

					// binanceClientService.changeMarginType(symbol, MARGIN_TYPE);
					binanceClientService.changeLeverage(symbol, LEVERAGE.get());

					BinanceOrder buyOrderResponse = binanceClientService.buyOrder(symbol,
						IS_SHORT.get() ? OrderSide.SELL : OrderSide.BUY, OrderType.MARKET,
						START_BUY_MONEY * LEVERAGE.get() / price);
					if(buyOrderResponse != null) {
						binanceClientService.sellOrder(symbol, IS_SHORT.get() ? OrderSide.BUY : OrderSide.SELL,
							OrderType.TAKE_PROFIT_MARKET, stopPriceWhenUp, "%.8f");
						binanceClientService.sellOrder(symbol, IS_SHORT.get() ? OrderSide.BUY : OrderSide.SELL,
							OrderType.STOP_MARKET, stopPriceWhenDown, "%.8f");
					}

					BUY_LOCK_DATE_TIME.put(symbol, LocalDateTime.now().plusMinutes(CANDLE_INTERVAL.getMinutes() * 2 + 1));
				}
			}
		}

		log.info("coin trade end");
	}

	public void changePosition(boolean isShort) {
		IS_SHORT.set(isShort);
	}

	public void changeLeverage(int leverage) {
		LEVERAGE.set(leverage);
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
