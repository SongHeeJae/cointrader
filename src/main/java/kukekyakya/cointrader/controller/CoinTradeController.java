package kukekyakya.cointrader.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import kukekyakya.cointrader.binance.client.PositionSide;
import kukekyakya.cointrader.binance.service.BinanceCoinTradeSecondService;
import kukekyakya.cointrader.binance.service.BinanceCoinTradeService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * CoinTradeController
 *
 * @author heejae.song
 * @since 2022. 08. 27.
 */
@RestController
@RequiredArgsConstructor
public class CoinTradeController {
	private final BinanceCoinTradeSecondService binanceCoinTradeService;

	@PostMapping("/trade/start")
	public void start() {
		new Thread(() -> binanceCoinTradeService.start()).start();
	}

	@PostMapping("/trade/end")
	public void end() {
		binanceCoinTradeService.end();
	}

	@PostMapping("/trade/change-position")
	public void changePosition(@RequestBody ChangePositionRequest request) {
		PositionSide position = request.getPosition();
		if(position == PositionSide.LONG) {
			binanceCoinTradeService.changePosition(false);
		} else {
			binanceCoinTradeService.changePosition(true);
		}
	}

	@PostMapping("/trade/change-leverage")
	public void changeLeverage(@RequestBody ChangeLeverageRequest request) {
		binanceCoinTradeService.changeLeverage(request.leverage);
	}

	@Getter
	@Setter
	private static class ChangePositionRequest {
		private PositionSide position;
	}

	@Getter
	@Setter
	private static class ChangeLeverageRequest {
		private int leverage;
	}
}
