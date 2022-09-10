package kukekyakya.cointrader.client.binance.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * BinanceOrder
 *
 * @author heejae.song
 * @since 2022. 09. 07.
 */
@Data
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class BinanceOrder {
	private String clientOrderId;
	private String cumQty;
	private String cumQuote;
	private String executedQty;
	private Long orderId;
	private String avgPrice;
	private String origQty;
	private String price;
	private Boolean reduceOnly;
	private String side;
	private String positionSide;
	private String status;
	private String stopPrice;
	private String closePosition;
	private String symbol;
	private String timeInForce;
	private String type;
	private String origType;
	private String activatePrice;
	private String priceRate;
	private Long updateTime;
	private String workingType;
	private Boolean priceProtect;
}
