package kukekyakya.cointrader.client.binance.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Asset
 *
 * @author heejae.song
 * @since 2022. 09. 07.
 */
@Data
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class BinanceBalance {
	private String accountAlias;
	private String asset;
	private String balance;
	private String crossWalletBalance;
	private String crossUnPnl;
	private String availableBalance;
	private String maxWithdrawAmount;
	private Boolean marginAvailable;
	private Long updateTime;
}
