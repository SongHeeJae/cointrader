package kukekyakya.cointrader.binance.client;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * WorkingType
 *
 * @author heejae.song
 * @since 2022. 09. 07.
 */
@Getter
@RequiredArgsConstructor
public enum WorkingType { // stopPrice 트리거되는 타입
	MARK_PRICE("MARK_PRICE"),
	CONTRACT_PRICE("CONTRACT_PRICE"); // 디폴트

	private final String value;
}
