package kukekyakya.cointrader.binance.client;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * PositionSide
 *
 * @author heejae.song
 * @since 2022. 09. 07.
 */
@Getter
@RequiredArgsConstructor
public enum PositionSide {
	BOTH("BOTH"),
	LONG("LONG"),
	SHORT("SHORT");

	private final String value;
}
