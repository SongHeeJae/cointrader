package kukekyakya.cointrader.binance;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import kukekyakya.cointrader.binance.client.CandleInterval;
import kukekyakya.cointrader.binance.client.MarginType;
import kukekyakya.cointrader.binance.client.PositionSide;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * BinaneConstants
 *
 * @author heejae.song
 * @since 2022. 09. 10.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class BinaneConstants {
	public static final String API_KEY = "";
	public static final String SECRET_KEY = "";

	public static final CandleInterval CANDLE_INTERVAL = CandleInterval.MINUTE_15;
	public static final int COUNT = 10; // {CANDLE_INTERVAL}봉 COUNT개 조회

	public static final double DIFF_PERCENT = 0.015; // 봉의 시가, 현재 종가 기준으로 이 정도 이상 차이나는 경우
	public static final double SELL_PERCENT_P = 0.02; // 양(이득)일 때 판매 퍼센트 설정
	public static final double SELL_PERCENT_M = 0.012; // 음(손해)일 때 판매 퍼센트 설정
	public static final long MINUS_CANDLE_COUNT = 12; // COUNT 보다 작아야한다.

	public static final double START_BUY_MONEY = 300; // USDT
	public static final double MINIMUM_PRICE = 0; // 구매하고자 하는 코인의 최소 개당 금액

	public static final MarginType MARGIN_TYPE = MarginType.ISOLATED;
	public static final AtomicInteger LEVERAGE = new AtomicInteger(10);
	public static final PositionSide POSITION_SIDE = PositionSide.LONG;

	public static final Map<String, LocalDateTime> BUY_LOCK_DATE_TIME = new ConcurrentHashMap<>();

	public static final String MY_CURRENCY = "USDT";

	public static final AtomicBoolean IS_SHORT = new AtomicBoolean(false);
}
