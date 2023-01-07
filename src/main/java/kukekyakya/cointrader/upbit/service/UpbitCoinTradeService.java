package kukekyakya.cointrader.upbit.service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.scheduling.annotation.Async;

import kukekyakya.cointrader.upbit.client.UpbitClientService;
import kukekyakya.cointrader.upbit.client.response.Account;
import kukekyakya.cointrader.upbit.client.response.Market;
import kukekyakya.cointrader.upbit.client.response.MinuteCandle;
import kukekyakya.cointrader.upbit.client.response.Ticker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * CoinTradeService
 *
 * @author heejae.song
 * @since 2022. 08. 27.
 */
@Slf4j
// @Service
@RequiredArgsConstructor
public class UpbitCoinTradeService {
	private final UpbitClientService upbitClientService;

	private static final int MINUTE_UNIT = 5;
	private static final int COUNT = 1;
	private static final double DIFF_PERCENT = 0.01; // 봉의 시가, 현재 종가 기준으로 이 정도 이상 차이나는 경우
	private static final double SELL_PERCENT_P = 0.003; // 양일 떄 판매 퍼센트 설정
	private static final double SELL_PERCENT_M = 0.005; // 음일 떄 판매 퍼센트 설정
	private static final long MINUS_CANDLE_COUNT = 12; // COUNT 보다 작아야한다.

	private static final int START_BUY_MONEY = 10000;
	private static final double MINIMUM_PRICE = 5; // 구매하고자 하는 코인의 최소 개당 금액

	// private static final MyWallet MY_WALLET =

	private static final Map<String, LocalDateTime> BUY_LOCK_DATE_TIME = new ConcurrentHashMap<>();

	private static final AtomicBoolean FLAG = new AtomicBoolean(false);

	private static final String MY_CURRENCY = "KRW";

	@Async
	public void start() {
		if(FLAG.get()) {
			return;
		}

		// upbitClientService.sell("KRW-SAND", "7.0001");

		// if(true) return;

		log.info("coin trade start");

		FLAG.set(true);

		while(FLAG.get()) {
			Set<String> alreadyBuyCoin = new HashSet<>();

			List<Account> accounts = upbitClientService.readAllAccounts(); // 구매한 목록

			double myMoney = -1;
			for (Account account : accounts) {
				if(account.getCurrency().equals(MY_CURRENCY)) {
					myMoney = Double.valueOf(account.getBalance());
					break;
				}
			}

			log.info("현재 잔액 : {}", myMoney);

			// TODO 판매는 별도 스레드로 돌려도 될듯
			for (Account account : accounts){
				if(account.getCurrency().equals(MY_CURRENCY)) continue;

				String market = account.getUnitCurrency() + "-" + account.getCurrency();
				Ticker ticker = upbitClientService.readTickers(market).get(0);
				Double tradePrice = ticker.getTradePrice();

				double percent = calPercent(Double.valueOf(account.getAvgBuyPrice()), tradePrice);
				if((percent > 0 && percent >= SELL_PERCENT_P) || (percent < 0 && percent <= -SELL_PERCENT_M)) {
					log.info("판매 완료 market = {}, percent = {}", market, percent);
					upbitClientService.sell(market, account.getBalance());
					BUY_LOCK_DATE_TIME.put(market, LocalDateTime.now().plusMinutes(MINUTE_UNIT + 1));
				} else {
					alreadyBuyCoin.add(market);
				}

				sleep(130);
			}

			if(myMoney <= START_BUY_MONEY) { // 판매 금액을 다시 추가하진 않는다.
				sleep(100);
				continue;
			}

			List<Market> markets = upbitClientService.readAllMarkets();
			for (Market market : markets) {
				if (market.getMarket().indexOf("KRW") == -1)
					continue;

				sleep(100);
				List<MinuteCandle> minuteCandles = upbitClientService.readAllMinuteCandles(MINUTE_UNIT,
					market.getMarket(), COUNT);
				if (minuteCandles.size() < COUNT)
					continue;

				MinuteCandle newestCandle = minuteCandles.get(0);
				MinuteCandle oldestCandle = minuteCandles.get(minuteCandles.size() - 1);
				double percent = calPercent(oldestCandle.getOpeningPrice(), newestCandle.getTradePrice());
				long count = countMinusCandle(minuteCandles);

				// 구매
				if (minuteCandles.get(0).getTradePrice() <= MINIMUM_PRICE)
					continue;
				if (percent >= DIFF_PERCENT) { // TODO 거래량도 확인
					LocalDateTime lockDateTime = BUY_LOCK_DATE_TIME.get(market.getMarket());
					if(lockDateTime != null) {
						if(lockDateTime.isBefore(LocalDateTime.now())) {
							BUY_LOCK_DATE_TIME.remove(market.getMarket());
						} else {
							continue;
						}
					}

					if(alreadyBuyCoin.contains(market.getMarket())) continue;

					try {
						log.info("구매 완료 market = {}, percent = {}", market.getMarket(), percent);
						upbitClientService.buy(market.getMarket(), String.valueOf(START_BUY_MONEY));
					} catch (Exception e) {
						log.error("구매 실패 market = {}, percent = {}", market.getMarket(), percent, e);
					}
					sleep(30);
				}
			}

			sleep(100);
		}

		log.info("coin trade end");
	}

	private double calPercent(double start, double end) {
		return end > start ? (end / start) - 1 : -((start / end) - 1);
	}

	private long countMinusCandle(List<MinuteCandle> minuteCandles) {
		return minuteCandles.stream().filter(MinuteCandle::isMinusCandle).count();
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
