package telran.ashkelon2020.accounting.dto.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class IncorrectPhoneException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7310702299823712967L;
	
	public IncorrectPhoneException() {
		super("Sorry your phone number is incorrect");
	}

}
