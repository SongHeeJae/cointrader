package kukekyakya.cointrader.binance.client.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.ToString;

/**
 * BinanceAccount
 *
 * @author heejae.song
 * @since 2022. 09. 07.
 */
@Data
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BinanceAccount {
	private Long makerCommission;
	private Long takerCommission;
	private Long buyerCommission;
	private Long sellerCommission;
	private Boolean canTrade;
	private Boolean canWithdraw;
	private Boolean canDeposit;
	private Boolean brokered;
	private Long updateTime;
	private String accountType;
	private List<BinanceAccountBalance> balances = List.of();
	private List<String> permissions = List.of();
}
