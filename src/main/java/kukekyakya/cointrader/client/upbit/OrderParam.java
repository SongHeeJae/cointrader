package kukekyakya.cointrader.client.upbit;

import org.springframework.web.bind.annotation.GetMapping;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * OrderParam
 *
 * @author heejae.song
 * @since 2022. 09. 03.
 */
@Getter
@RequiredArgsConstructor
public enum OrderParam {
	BUY("bid", "price"),
	SELL("ask", "market");

	private final String side;
	private final String ordType;
}
