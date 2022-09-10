package kukekyakya.cointrader.service.wallet;

import kukekyakya.cointrader.client.upbit.response.Market;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Coin
 *
 * @author heejae.song
 * @since 2022. 08. 27.
 */
@Data
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class BuyCoin {
	private Market market;
	private double buyPrice; // 구매한 금액
	private double buyTradePrice; // 구매했을 때 시장가
	private int tryCount; // 시도 횟수
}
