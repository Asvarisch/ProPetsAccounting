package telran.ashkelon2020.accounting.service;

import java.util.Base64;

import telran.ashkelon2020.accounting.dto.UserInfoDto;
import telran.ashkelon2020.accounting.model.UserAccount;

public interface TokenService {
	
	String createToken(UserAccount userAccount);
	
	UserInfoDto validateToken(String token);
	
	default String[] validateTokenBase64(String token) {
		token = token.split(" ")[1];
		String[] credentials = new String(Base64.getDecoder().decode(token)).split(":");
		return credentials;
	}

}
