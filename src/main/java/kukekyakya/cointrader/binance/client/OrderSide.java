package kukekyakya.cointrader.binance.client;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * OrderSide
 *
 * @author heejae.song
 * @since 2022. 09. 07.
 */
@Getter
@RequiredArgsConstructor
public enum OrderSide {
	BUY("BUY"),
	SELL("SELL");

	private final String value;
}
