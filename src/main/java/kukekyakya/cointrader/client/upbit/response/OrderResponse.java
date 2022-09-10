package kukekyakya.cointrader.client.upbit.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;
import lombok.ToString;

/**
 * OrderResponse
 *
 * @author heejae.song
 * @since 2022. 09. 03.
 */
@Data
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OrderResponse {
	private String uuid;
	private String side;
	private String ordType;
	private String price;
	private String state;
	private String market;
	private String createdAt;
	private String volume;
	private String remainingVolume;
	private String reservedFee;
	private String remainingFee;
	private String paidFee;
	private String locked;
	private String executedVolume;
	private Integer tradesCount;
}
