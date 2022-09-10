package kukekyakya.cointrader.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import kukekyakya.cointrader.service.BinanceCoinTradeService;
import kukekyakya.cointrader.service.UpbitCoinTradeService;
import lombok.RequiredArgsConstructor;

/**
 * CoinTradeController
 *
 * @author heejae.song
 * @since 2022. 08. 27.
 */
@RestController
@RequiredArgsConstructor
public class CoinTradeController {
	private final BinanceCoinTradeService binanceCoinTradeService;

	@PostMapping("/trade/start")
	public void start() {
		new Thread(() -> binanceCoinTradeService.start()).start();
	}

	@PostMapping("/trade/end")
	public void end() {
		binanceCoinTradeService.end();
	}
}
