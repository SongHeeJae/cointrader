package kukekyakya.cointrader.upbit.client;

/**
 * UpbitClient
 *
 * @author heejae.song
 * @since 2022. 08. 27.
 */
// @FeignClient(
// 	name = "UpbitClient",
// 	url = "https://api.upbit.com",
// 	configuration = LogConfig.class
// )
public interface UpbitClient {

	// @GetMapping("/v1/market/all?isDetails=true")
	// List<Market> readAllMarkets(@RequestHeader("Authorization") String authorization);
	//
	// @GetMapping("/v1/candles/minutes/{unit}")
	// List<MinuteCandle> readAllMinuteCandle(@RequestHeader("Authorization") String authorization,
	// 	@PathVariable Integer unit, @RequestParam String market, @RequestParam Integer count);
	//
	// @GetMapping("/v1/accounts")
	// List<Account> readAllAccounts(@RequestHeader("Authorization") String authorization);
	//
	// @PostMapping(value = "/v1/orders")
	// @Headers("content-type: application/json; charset=utf-8")
	// OrderResponse order(@RequestHeader("Authorization") String authorization,
	// 	@RequestParam(value = "market", required = false) String market,
	// 	@RequestParam(value = "side", required = false) String side,
	// 	@RequestParam(value = "volume", required = false) String volume,
	// 	@RequestParam(value = "price", required = false) String price,
	// 	@RequestParam(value = "ord_type", required = false) String ordType);
	//
	// @GetMapping("/v1/ticker") // 여러건은 반점 구분
	// List<Ticker> readTickers(@RequestHeader("Authorization") String authorization,
	// 	@RequestParam("markets") String market);
}
