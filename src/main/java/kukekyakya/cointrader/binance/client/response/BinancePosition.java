package kukekyakya.cointrader.binance.client.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * BinancePosition
 *
 * @author heejae.song
 * @since 2022. 09. 08.
 */
@Data
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class BinancePosition {
	private String entryPrice;
	private String marginType;
	private String isAutoAddMargin;
	private String isolatedMargin;
	private String leverage;
	private String liquidationPrice;
	private String markPrice;
	private String maxNotionalValue;
	private String positionAmt;
	private String notional;
	private String isolatedWallet;
	private String symbol;
	private String unRealizedProfit;
	private String positionSide;
	private Long updateTime;
}
