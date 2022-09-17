package kukekyakya.cointrader.binance.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import kukekyakya.cointrader.config.LogConfig;
import kukekyakya.cointrader.binance.client.response.BinanceBalance;
import kukekyakya.cointrader.binance.client.response.BinanceOrder;
import kukekyakya.cointrader.binance.client.response.BinancePosition;
import kukekyakya.cointrader.binance.client.response.BinanceSimpleTicker;
import kukekyakya.cointrader.binance.client.response.ChangeLeverageResponse;
import kukekyakya.cointrader.binance.client.response.ChangeMarginResponse;

/**
 * BinanceClient
 *
 * @author heejae.song
 * @since 2022. 09. 07.
 */
@FeignClient(
	name = "BinanceClient",
	url = "https://fapi.binance.com",
	configuration = LogConfig.class
)
public interface BinanceClient {

	@GetMapping("/fapi/v1/klines")
	List<List<Object>> readCandles(
		@RequestHeader("X-MBX-APIKEY") String apiKey,
		@RequestParam("symbol") String symbol,
		@RequestParam("interval") String interval,
		@RequestParam("limit") Integer limit
	);

	@GetMapping("/fapi/v2/balance")
	List<BinanceBalance> readAllUserBalances(
		@RequestHeader("X-MBX-APIKEY") String apiKey,
		@RequestParam("timestamp") String timestamp,
		@RequestParam("signature") String signature);

	@GetMapping("/fapi/v1/ticker/price")
	List<BinanceSimpleTicker> readAllTickers(
		@RequestHeader("X-MBX-APIKEY") String apiKey);

	@GetMapping("/fapi/v1/ticker/price")
	BinanceSimpleTicker readTicker(
		@RequestHeader("X-MBX-APIKEY") String apiKey,
		@RequestParam("symbol") String symbol);

	@PostMapping("/fapi/v1/order")
	BinanceOrder buyOrder(
		@RequestHeader("X-MBX-APIKEY") String apiKey,
		@RequestParam("symbol") String symbol,
		@RequestParam("side") String side,
		@RequestParam("positionSide") String positionSide,
		@RequestParam("type") String type,
		@RequestParam("quantity") Double quantity,
		@RequestParam("timestamp") String timestamp,
		@RequestParam("signature") String signature);

	@PostMapping("/fapi/v1/order")
	BinanceOrder sellOrder(
		@RequestHeader("X-MBX-APIKEY") String apiKey,
		@RequestParam("symbol") String symbol,
		@RequestParam("side") String side,
		@RequestParam("positionSide") String positionSide,
		@RequestParam("type") String type,
		@RequestParam("closePosition") String closePosition,
		@RequestParam("stopPrice") String stopPrice,
		@RequestParam("timestamp") String timestamp,
		@RequestParam("signature") String signature);

	@DeleteMapping("/fapi/v1/order")
	BinanceOrder cancelOrder(
		@RequestHeader("X-MBX-APIKEY") String apiKey,
		@RequestParam("symbol") String symbol,
		@RequestParam("timestamp") String timestamp,
		@RequestParam("signature") String signature);

	@PostMapping("/fapi/v1/leverage")
	ChangeLeverageResponse changeLeverage(
		@RequestHeader("X-MBX-APIKEY") String apiKey,
		@RequestParam("symbol") String symbol,
		@RequestParam("leverage") Integer leverage,
		@RequestParam("timestamp") String timestamp,
		@RequestParam("signature") String signature
	);

	@PostMapping("/fapi/v1/marginType")
	ChangeMarginResponse changeMarginType(
		@RequestHeader("X-MBX-APIKEY") String apiKey,
		@RequestParam("symbol") String symbol,
		@RequestParam("marginType") String marginType,
		@RequestParam("timestamp") String timestamp,
		@RequestParam("signature") String signature
	);

	@PostMapping("/fapi/v1/positionSide/dual")
	void changePositionMode(
		@RequestHeader("X-MBX-APIKEY") String apiKey,
		@RequestParam("dualSidePosition") String dualSidePosition,
		@RequestParam("timestamp") String timestamp,
		@RequestParam("signature") String signature
	);

	@GetMapping("/fapi/v2/positionRisk")
	List<BinancePosition> readAllPositions(
		@RequestHeader("X-MBX-APIKEY") String apiKey,
		@RequestParam("timestamp") String timestamp,
		@RequestParam("signature") String signature
	);

	@GetMapping("/fapi/v2/positionRisk")
	BinancePosition readPosition(
		@RequestHeader("X-MBX-APIKEY") String apiKey,
		@RequestParam("symbol") String symbol,
		@RequestParam("timestamp") String timestamp,
		@RequestParam("signature") String signature
	);
}
