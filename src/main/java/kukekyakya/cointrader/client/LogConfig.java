package kukekyakya.cointrader.client;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import feign.Logger;

/**
 * LogConfig
 *
 * @author heejae.song
 * @since 2022. 09. 05.
 */
@Configuration
public class LogConfig {
	@Bean
	Logger.Level feignLoggerLevel() {
		return Logger.Level.FULL;
	}
}
