package telran.ashkelon2020.accounting.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Singular;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserResponseDto {
	String email;
	String name;
	String avatar;
	String phone;
	@Singular
	List<String> roles;
	
	@JsonIgnore
	String token;
}
