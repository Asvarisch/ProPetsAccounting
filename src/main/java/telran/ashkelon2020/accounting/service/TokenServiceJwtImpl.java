package telran.ashkelon2020.accounting.service;

import java.time.ZonedDateTime;
import java.util.Base64;
import java.util.Date;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import telran.ashkelon2020.accounting.dto.UserInfoDto;
import telran.ashkelon2020.accounting.dto.exceptions.TokenExpiredException;
import telran.ashkelon2020.accounting.dto.exceptions.TokenValidateException;
import telran.ashkelon2020.accounting.model.UserAccount;

@Service
@Order(10)
public class TokenServiceJwtImpl implements TokenService {

	@Value("${secret.value}")
	private String secret;

	@Value("${expiration.value}")
	private int jwtExpiration;

	@Autowired
	SecretKey secretKey;

	@Override
	public String createToken(UserAccount userAccount) {
		return Jwts.builder()
				.setId(userAccount.getEmail())
				.setExpiration(Date.from(ZonedDateTime.now().plusDays(jwtExpiration).toInstant()))
				.signWith(SignatureAlgorithm.HS256, secretKey)
				.compact();
	}
	
	@Override
	public UserInfoDto validateToken(String token) {
		Jws<Claims> jws;
		try {
			jws = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
		} catch (Exception e) {
			throw new TokenValidateException();
		} 
		Claims claims = jws.getBody();
		Date date = claims.getExpiration();
		if (date.before((Date.from(ZonedDateTime.now().toInstant())))) {
			throw new TokenExpiredException();
		}
		claims.setExpiration(Date.from(ZonedDateTime.now().plusDays(jwtExpiration).toInstant()));
		token = Jwts.builder()
				.setClaims(claims)
				.signWith(SignatureAlgorithm.HS256, secretKey)
				.compact();
		return new UserInfoDto((String)claims.getId(), token);
	}

	@Bean
	public SecretKey secretKey() {
		return new SecretKeySpec(Base64.getUrlEncoder().encode(secret.getBytes()), "AES");
	}

}
