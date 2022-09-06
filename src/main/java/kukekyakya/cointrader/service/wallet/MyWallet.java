package kukekyakya.cointrader.service.wallet;

import java.util.HashMap;
import java.util.Map;

import kukekyakya.cointrader.client.response.Market;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/**
 * MyWallet
 *
 * @author heejae.song
 * @since 2022. 08. 27.
 */
@Data
@Slf4j
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class MyWallet {
	private int money = 1000000;
	private Map<String, BuyCoin> myCoins = new HashMap<>();

	public void buyCoin(Market market, double buyPrice, double buyTradePrice, int tryCount) {
		myCoins.put(market.getMarket(), new BuyCoin(market, buyPrice, buyTradePrice, tryCount));

		this.money -= buyPrice;
		log.info("코인 구매 - 이름 : {}, 구매 금액 : {}, 잔액 : {}", market.getKoreanName(), buyPrice * tryCount, this.money);
	}

	public void sellCoin(Market market, double curTradePrice) {
		BuyCoin coin = myCoins.get(market.getMarket());
		if(coin == null) return;

		myCoins.remove(market.getMarket());

		double diffTradePrice = curTradePrice - coin.getBuyTradePrice();
		double percent = diffTradePrice > 0 ?
			(curTradePrice / coin.getBuyTradePrice()) - 1 :
			-((coin.getBuyTradePrice() / curTradePrice) - 1);
		double addedMoney = coin.getBuyPrice() + (coin.getBuyPrice() * percent);

		this.money += addedMoney;

		log.info("코인 판매 - 이름 : {}, 현재 시장가 : {}, 구매 시장가 : {}, 구매금액 : {}, 판매금액 : {}, 등락률 : {}, 잔액 : {}",
			market.getKoreanName(), curTradePrice, coin.getBuyTradePrice(), coin.getBuyPrice(), addedMoney, percent, this.money);
	}

	public boolean isAlreadyBuy(String market) {
		return myCoins.containsKey(market);
	}
}
