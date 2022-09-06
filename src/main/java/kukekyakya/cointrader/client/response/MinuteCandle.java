package kukekyakya.cointrader.client.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;
import lombok.ToString;

/**
 * MinuteCandle
 *
 * @author heejae.song
 * @since 2022. 08. 27.
 */
@Data
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MinuteCandle {
	private String market;
	private String candleDateTimeUtc;
	private String candleDateTimeKst;
	private double openingPrice;
	private double highPrice;
	private double lowPrice;
	private double tradePrice;
	private long timestamp;
	private double candleAccTradePrice;
	private double candleAccTradeVolume;
	private int unit;

	public boolean isPlusCandle() {
		return openingPrice < tradePrice;
	}

	public boolean isMinusCandle() {
		return !isPlusCandle();
	}
}
