package kukekyakya.cointrader.binance.client;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * OrderType
 *
 * @author heejae.song
 * @since 2022. 09. 07.
 */
@Getter
@RequiredArgsConstructor
public enum OrderType {
	LIMIT("LIMIT"),
	MARKET("MARKET"),
	STOP("STOP"),
	STOP_MARKET("STOP_MARKET"),
	TAKE_PROFIT("TAKE_PROFIT"),
	TAKE_PROFIT_MARKET("TAKE_PROFIT_MARKET"),
	TRAILING_STOP_MARKET("TRAILING_STOP_MARKET");

	private final String value;
}
