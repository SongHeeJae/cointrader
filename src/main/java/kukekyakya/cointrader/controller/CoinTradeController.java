package kukekyakya.cointrader.controller;

import java.util.concurrent.Executors;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kukekyakya.cointrader.service.CoinTradeService;
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
	private final CoinTradeService coinTradeService;

	@PostMapping("/trade/start")
	public void start() {
		new Thread(() -> coinTradeService.start()).start();
	}

	@PostMapping("/trade/end")
	public void end() {
		coinTradeService.end();
	}
}
