package kukekyakya.cointrader.client.binance;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * MarginType
 *
 * @author heejae.song
 * @since 2022. 09. 07.
 */
@Getter
@RequiredArgsConstructor
public enum MarginType {
	ISOLATED("ISOLATED"),
	CROSSED("CROSSED");

	private final String value;
}
