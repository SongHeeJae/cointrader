package kukekyakya.cointrader.binance.client.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.ToString;

/**
 * BinanceAccountBalance
 *
 * @author heejae.song
 * @since 2022. 09. 07.
 */
@Data
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BinanceAccountBalance {
	private String asset;
	private String free; // 잔고인듯? (잔액 X)
	private String locked;
}
