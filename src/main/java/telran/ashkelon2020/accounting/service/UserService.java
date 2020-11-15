package telran.ashkelon2020.accounting.service;

import java.security.Principal;
import java.util.Set;

import org.springframework.http.ResponseEntity;

import telran.ashkelon2020.accounting.dto.RolesResponseDto;
import telran.ashkelon2020.accounting.dto.UserRegisterDto;
import telran.ashkelon2020.accounting.dto.UserResponseDto;
import telran.ashkelon2020.accounting.dto.UserUpdateDto;

public interface UserService {

	ResponseEntity<UserResponseDto> addUser(UserRegisterDto userRegisterDto);
	
	UserResponseDto login(String login);

	ResponseEntity<UserResponseDto> getUserInfo(String login, String token);
	
	UserResponseDto editUser(String login, UserUpdateDto userUpdateDto, Principal principal);
	
	UserResponseDto removeUser(String login);
	
	RolesResponseDto changeRolesList(String login, String role, boolean isAddRole);
	
	boolean blockUserAccount(String login, boolean status);
	
	void addUserFavoritePost(String login, String id);
	
	void addUserActivity(String login, String id);

	void removeUserFavoritePost(String login, String id);

	void removeUserActivity(String login, String id);

	Set<String> getUserData(String login, boolean dataType);
	
	ResponseEntity<String> tokenValidation(String token);

	
	
}
