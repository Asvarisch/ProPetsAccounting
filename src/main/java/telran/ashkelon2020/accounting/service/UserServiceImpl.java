package telran.ashkelon2020.accounting.service;

import static telran.ashkelon2020.accounting.configuration.Constants.TOKEN_HEADER;

import java.security.Principal;
import java.util.Set;
import java.util.regex.Pattern;

import org.mindrot.jbcrypt.BCrypt;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import telran.ashkelon2020.accounting.dao.UserRepository;
import telran.ashkelon2020.accounting.dto.RolesResponseDto;
import telran.ashkelon2020.accounting.dto.UserRegisterDto;
import telran.ashkelon2020.accounting.dto.UserResponseDto;
import telran.ashkelon2020.accounting.dto.UserRoleDto;
import telran.ashkelon2020.accounting.dto.UserUpdateDto;
import telran.ashkelon2020.accounting.dto.exceptions.IncorrectPhoneException;
import telran.ashkelon2020.accounting.dto.exceptions.IncorrectRegistrationException;
import telran.ashkelon2020.accounting.dto.exceptions.UserExistsException;
import telran.ashkelon2020.accounting.dto.exceptions.UserNotFoundException;
import telran.ashkelon2020.accounting.model.UserAccount;

@Service
@ManagedResource
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository repository;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	TokenService tokenService;

//	@Value("${expdate.value}")
//	private long period;
//
	@Value("${default.role}")
	private String defaultUser;

//
//	@ManagedAttribute
//	public long getPeriod() {
//		return period;
//	}
//
//	@ManagedAttribute
//	public void setPeriod(long period) {
//		this.period = period;
//	}
//
	public String getDefaultUser() {
		return defaultUser;
	}

	public void setDefaultUser(String defaultUser) {
		this.defaultUser = defaultUser;
	}

	@Override
	@Transactional
	public ResponseEntity<UserResponseDto> addUser(UserRegisterDto userRegisterDto) {
		if (repository.existsById(userRegisterDto.getEmail())) {
			throw new UserExistsException(userRegisterDto.getEmail());
		}

		if (!validateEmail(userRegisterDto.getEmail()) || !validatePassword(userRegisterDto.getPassword())) {
			throw new IncorrectRegistrationException();
		}
		UserAccount userAccount = modelMapper.map(userRegisterDto, UserAccount.class);
		String hashPassword = BCrypt.hashpw(userRegisterDto.getPassword(), BCrypt.gensalt());
		userAccount.addRole(defaultUser.toUpperCase());
		userAccount.setPassword(hashPassword);
		repository.save(userAccount);
		UserResponseDto userResponseDto = modelMapper.map(userAccount, UserResponseDto.class);
		HttpHeaders headers = new HttpHeaders();
		headers.add(TOKEN_HEADER, tokenService.createToken(userAccount));
		return new ResponseEntity<UserResponseDto>(userResponseDto, headers, HttpStatus.OK);
	}

	private boolean validateEmail(String email) {
		String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?!-)(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
		Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		boolean res = pattern.matcher(email).matches();
		return res;
	}

	private boolean validatePassword(String password) {
		String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
		Pattern pattern = Pattern.compile(regex);
		boolean res = pattern.matcher(password).matches();
		return res;
		/*
		 * ^ # start-of-string (?=.*[0-9]) # a digit must occur at least once
		 * (?=.*[a-z]) # a lower case letter must occur at least once (?=.*[A-Z]) # an
		 * upper case letter must occur at least once (?=.*[@#$%^&+=]) # a special
		 * character must occur at least once (?=\S+$) # no whitespace allowed in the
		 * entire string .{8,} # anything, at least eight places though $ #
		 * end-of-string
		 */
	}

	@Override
	public UserResponseDto getUser(String login) {
		UserAccount userAccount = repository.findById(login).orElseThrow(() -> new UserNotFoundException(login));
		return modelMapper.map(userAccount, UserResponseDto.class);
	}

	@Override
	public UserResponseDto editUser(String login, UserUpdateDto userUpdateDto, Principal principal) {
		UserAccount userAccount = repository.findById(login).orElseThrow(() -> new UserNotFoundException(login));
		if (userUpdateDto.getName() != null) {
			userAccount.setName(userUpdateDto.getName());
		}
		if (userUpdateDto.getAvatar() != null) {
			userAccount.setAvatar(userUpdateDto.getAvatar());
		}
		if (userUpdateDto.getPhone() != null && validatePhone(userUpdateDto.getPhone())) {
			userAccount.setPhone(userUpdateDto.getPhone());
		}else {
			throw new IncorrectPhoneException();
		}
		repository.save(userAccount);
		return modelMapper.map(userAccount, UserResponseDto.class);
	}

	//FIXME
	private boolean validatePhone(String phone) { 
		String regex = "^((\\+|00)?972\\-?|0)(([23489]|[57]\\d)\\-?\\d{7})$";
		Pattern pattern = Pattern.compile(regex);
		boolean res = pattern.matcher(phone).matches();
		return res;
	}

	@Override
	public UserResponseDto removeUser(String login) {
		UserAccount userAccount = repository.findById(login).orElseThrow(() -> new UserNotFoundException(login));
		repository.deleteById(login);
		return modelMapper.map(userAccount, UserResponseDto.class);
	}

	@Override
	public RolesResponseDto changeRolesList(String login, String role, boolean isAddRole) {
		UserAccount userAccount = repository.findById(login)
				.orElseThrow(() -> new UserNotFoundException(login));
		boolean res;
		if (isAddRole) {
			res = userAccount.addRole(role.toUpperCase());
		} else {
			res = userAccount.removeRole(role.toUpperCase());
		}
		if (res) {
			repository.save(userAccount);
		}
		return modelMapper.map(userAccount, RolesResponseDto.class);
	}

	@Override
	public boolean blockUserAccount(String login, boolean status) {
		UserAccount userAccount = repository.findById(login).orElseThrow(() -> new UserNotFoundException(login));
		if (status) {
			userAccount.setBlocked(true);
			repository.save(userAccount);
			return true;
		} else {
			userAccount.setBlocked(false);
			repository.save(userAccount);
			return false;
		}
	}

	@Override
	public void addUserFavoritePost(String login, String id) {
		UserAccount userAccount = repository.findById(login).orElseThrow(() -> new UserNotFoundException(login));
		userAccount.addFavoritePost(id);
		repository.save(userAccount);
	}

	@Override
	public void addUserActivity(String login, String id) {
		UserAccount userAccount = repository.findById(login).orElseThrow(() -> new UserNotFoundException(login));
		userAccount.addUserPost(id);
		repository.save(userAccount);

	}

	@Override
	public void removeUserFavoritePost(String login, String id) {
		UserAccount userAccount = repository.findById(login).orElseThrow(() -> new UserNotFoundException(login));
		userAccount.removeFavoritePost(id);
		repository.save(userAccount);

	}

	@Override
	public void removeUserActivity(String login, String id) {
		UserAccount userAccount = repository.findById(login).orElseThrow(() -> new UserNotFoundException(login));
		userAccount.removeUserPost(id);
		repository.save(userAccount);

	}

	@Override
	public Set<String> getUserData(String login, boolean dataType) {
		UserAccount userAccount = repository.findById(login).orElseThrow(() -> new UserNotFoundException(login));
		if (dataType) {
			return userAccount.getActivity();
		} else {
			return userAccount.getFavorites();
		}

	}

	@Override
	public ResponseEntity<UserRoleDto> tokenValidate(String token) {
		UserRoleDto userRoleDto = tokenService.validateToken(token);
		HttpHeaders headers = new HttpHeaders();
		headers.add(TOKEN_HEADER, userRoleDto.getToken());
		return new ResponseEntity<UserRoleDto>(userRoleDto, headers, HttpStatus.OK);
	}

}
