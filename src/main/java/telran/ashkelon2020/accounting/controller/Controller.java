package telran.ashkelon2020.accounting.controller;

import static telran.ashkelon2020.accounting.configuration.Constants.TOKEN_HEADER;

import java.security.Principal;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import telran.ashkelon2020.accounting.dto.RolesResponseDto;
import telran.ashkelon2020.accounting.dto.UserRegisterDto;
import telran.ashkelon2020.accounting.dto.UserResponseDto;
import telran.ashkelon2020.accounting.dto.UserUpdateDto;
import telran.ashkelon2020.accounting.service.UserService;

@RestController
@RequestMapping("/account/en/v1")
public class Controller {

	@Autowired
	UserService accountService;

	// REGISTER USER
	@PostMapping("/registration")
	public ResponseEntity<UserResponseDto> register(@RequestBody UserRegisterDto userRegisterDto) {
		return accountService.addUser(userRegisterDto);
	}

	// LOGIN USER
	@PostMapping("/login")
	public UserResponseDto login(Principal principal) {
		return accountService.login(principal.getName());
	}

	// USER INFORMATION - check info about user by id
	@GetMapping("/{login}/info")
	public ResponseEntity<UserResponseDto> getUserInfo(@PathVariable String login, @RequestHeader(TOKEN_HEADER) String token) {
		return accountService.getUserInfo(login, token);
	}


	// EDIT USER PROFILE
	@PutMapping("/{login}")
	public UserResponseDto editeUser(@PathVariable String login, @RequestBody UserUpdateDto userUpdateDto,
			Principal principal) {
		return accountService.editUser(login, userUpdateDto, principal);
	}

	// REMOVE USER
	@DeleteMapping("/{login}")
	public UserResponseDto removeUser(@PathVariable String login) {
		return accountService.removeUser(login);
	}

	// ADD USER ROLE
	@PutMapping("/{login}/role/{role}")
	public RolesResponseDto addRole(@PathVariable String login, @PathVariable String role) {
		return accountService.changeRolesList(login, role, true);
	}

	// DELETE USER ROLE
	@DeleteMapping("/{login}/role/{role}")
	public RolesResponseDto removeRole(@PathVariable String login, @PathVariable String role) {
		return accountService.changeRolesList(login, role, false);
	}

	// BLOCK/UNBLOCK UsER ACCOUNT
	@PutMapping("/{login}/block/{status}")
	public boolean blockUserAccount(@PathVariable String login, @PathVariable boolean status) {
		return accountService.blockUserAccount(login, status);
	}

	// ADD USER FAVORITE
	@PutMapping("/{login}/favorite/{id}")
	public void addUserFavorite(@PathVariable String login, @PathVariable String id) {
		accountService.addUserFavoritePost(login, id);
	}

	// ADD USER ACTIVITY (his own post)
	@PutMapping("/{login}/activity/{id}")
	public void addUserActivity(@PathVariable String login, @PathVariable String id) {
		accountService.addUserActivity(login, id);
	}

	// REMOVE USER FAVORITE
	@DeleteMapping("/{login}/favorite/{id}")
	public void removeUserFavorite(@PathVariable String login, @PathVariable String id) {
		accountService.removeUserFavoritePost(login, id);
	}

	// REMOVE USER ACTIVITY (his own post)
	@DeleteMapping("/{login}/activity/{id}")
	public void removeUserActivity(@PathVariable String login, @PathVariable String id) {
		accountService.removeUserActivity(login, id);
	}

	// GeT USER DATA
	@GetMapping("/{login}")
	public Set<String> getUserData(@PathVariable String login, @RequestParam("dataType") boolean dataType) {
		return accountService.getUserData(login, dataType);
	}

	// TOKEN VALIDATION
	@GetMapping("/token/validation")
	public ResponseEntity<String> tokenValidation(@RequestHeader(TOKEN_HEADER) String token) {
		return accountService.tokenValidation(token);
	}

}
