package kukekyakya.cointrader.client.upbit.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;
import lombok.ToString;

/**
 * Market
 *
 * @author heejae.song
 * @since 2022. 08. 27.
 */
@Data
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Market {
	private String market;
	private String koreanName;
	private String englishName;
	private String marketWarning;
}
