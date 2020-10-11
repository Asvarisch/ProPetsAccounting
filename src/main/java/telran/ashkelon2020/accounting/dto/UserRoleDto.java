package telran.ashkelon2020.accounting.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserRoleDto {
	String email;
	List<String> roles;
	boolean isBlocked;
	
	@JsonIgnore
	String token;

	public UserRoleDto(String email, List<String> roles, String token) {
		this.email = email;
		this.roles = roles;
		this.token = token;
	}
	
	

}
