package kukekyakya.cointrader.binance.client.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * BinanceCandle
 *
 * @author heejae.song
 * @since 2022. 09. 07.
 */
@Data
@Builder
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class BinanceCandle {
	private Long openTime;
	private String openPrice;
	private String highPrice;
	private String lowPrice;
	private String closePrice;
	private String volume;
	private Long closeTime;
	private String quoteAssetVolume;
	private Integer numberOfTrades;
	private String takerBuyBaseAssetVolume;
	private String takerBuyQuoteAssetVolume;
	private String ignoreField;

	public Double getClosePrice() {
		return Double.valueOf(closePrice);
	}

	public Double getHighPrice() {
		return Double.valueOf(highPrice);
	}

	public Double getLowPrice() {
		return Double.valueOf(lowPrice);
	}

	public Double getOpenPrice() {
		return Double.valueOf(openPrice);
	}

	public boolean isPlusCandle() {
		return Double.valueOf(openPrice) < Double.valueOf(closePrice);
	}

	public boolean isMinusCandle() {
		return !isPlusCandle();
	}
}
