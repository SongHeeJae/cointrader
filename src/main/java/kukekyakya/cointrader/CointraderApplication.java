package kukekyakya.cointrader;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class CointraderApplication {

	public static void main(String[] args) {
		SpringApplication.run(CointraderApplication.class, args);
	}

}
