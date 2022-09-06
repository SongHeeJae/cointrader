package kukekyakya.cointrader.service;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import lombok.extern.slf4j.Slf4j;

/**
 * TokenService
 *
 * @author heejae.song
 * @since 2022. 08. 27.
 */
@Slf4j
@Service
public class TokenService {
	private static final String ACCESS_KEY = "";
	private static final String SECRET_KEY = "";
	private static final String TOKEN_TYPE = "Bearer ";

	public String gen() {
		return TOKEN_TYPE + JWT.create()
			.withClaim("access_key", ACCESS_KEY)
			.withClaim("nonce", UUID.randomUUID().toString())
			.sign(Algorithm.HMAC256(SECRET_KEY));
	}

	public String gen(Map<String, String> params) {
		return TOKEN_TYPE + JWT.create()
			.withClaim("access_key", ACCESS_KEY)
			.withClaim("nonce", UUID.randomUUID().toString())
			.withClaim("query_hash", getQueryHash(params))
			.withClaim("query_hash_alg", "SHA512")
			.sign(Algorithm.HMAC256(SECRET_KEY));
	}

	private String getQueryHash(Map<String, String> params) {
		List<String> queryElements = new ArrayList<>();
		for(Map.Entry<String, String> entity : params.entrySet()) {
			queryElements.add(entity.getKey() + "=" + entity.getValue());
		}

		String queryString = String.join("&", queryElements.toArray(new String[0]));

		try {
			MessageDigest md = MessageDigest.getInstance("SHA-512");
			md.update(queryString.getBytes("utf8"));
			return String.format("%0128x", new BigInteger(1, md.digest()));
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
			// empty
		}
		throw new RuntimeException("get query hash error.");
	}
}
