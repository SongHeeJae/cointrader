package kukekyakya.cointrader.client.upbit.request;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * OrderRequest
 *
 * @author heejae.song
 * @since 2022. 09. 04.
 */
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderRequest {
	private String market;
	private String side;
	private String volume;
	private String price;
	private String ord_type;
}
