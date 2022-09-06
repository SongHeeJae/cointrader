package kukekyakya.cointrader.client.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;
import lombok.ToString;

/**
 * Ticker
 *
 * @author heejae.song
 * @since 2022. 09. 03.
 */
@Data
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Ticker {
	private String market;
	private String tradeDate;
	private String tradeTime;
	private String tradeDateKst;
	private String tradeTimeKst;
	private Long tradeTimestamp;
	private Double opening_price;
	private Double highPrice;
	private Double lowPrice;
	private Double tradePrice;
	private Double prevClosingPrice;
	private String change; // EVEN : 보합, RISE : 상승, FALL : 하락
	private Double changePrice;
	private Double changeRate;
	private Double signedChangePrice;
	private Double signedChangeRate;
	private Double tradeVolume;
	private Double accTradePrice;
	private Double accTradePrice24h;
	private Double accTradeVolume;
	private Double accTradeVolume24h;
	private Double highest52WeekPrice;
	private String highest52WeekDate; // yyyy-MM-dd
	private Double lowest52WeekPrice;
	private String lowest52WeekDate; // yyyy-MM-dd
	private Long timestamp;
}
