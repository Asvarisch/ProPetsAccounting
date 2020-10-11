package telran.ashkelon2020.accounting.dto;

public class AuthenticationResponseDto {
	
	private final String jwt;
	
	public AuthenticationResponseDto(String jwt) {
		this.jwt = jwt;
	}

	public String getJwt() {
		return jwt;
	}

}
