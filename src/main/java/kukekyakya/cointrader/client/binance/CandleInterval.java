package kukekyakya.cointrader.client.binance;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * CandleInterval
 *
 * @author heejae.song
 * @since 2022. 09. 07.
 */
@Getter
@RequiredArgsConstructor
public enum CandleInterval {
	MINUTE_1("1m", 1),
	MINUTE_3("3m", 3),
	MINUTE_5("5m", 5),
	MINUTE_15("15m", 15),
	MINUTE_30("30m", 30),
	HOUR_1("1h", 60),
	HOUR_2("2h", 120),
	HOUR_4("4h", 240),
	HOUR_6("6h", 360),
	HOUR_8("8h", 480),
	HOUR_12("12h", 720),
	DAY_1("1d", 60 * 24),
	DAY_3("3d", 60 * 24 * 3),
	WEEK_1("1w", 60 * 24 * 7),
	MONTH_1("1M", 60 * 24 * 30);

	private final String value;
	private final Integer minutes;
}
