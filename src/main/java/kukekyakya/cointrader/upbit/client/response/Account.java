package kukekyakya.cointrader.upbit.client.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;
import lombok.ToString;

/**
 * Account
 *
 * @author heejae.song
 * @since 2022. 09. 03.
 */
@Data
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Account {
	private String currency; // 화폐를 의미하는 영문 대문자 코드
	private String balance; // 주문가능 금액/수량
	private String locked; // 주문 중 묶여있는 금액/수량
	private String avgBuyPrice; // 매수평균가
	private Boolean avgBuyPriceModified; // 매수평균가 수정 여부
	private String unitCurrency; // 평단가 기준 화폐
}
