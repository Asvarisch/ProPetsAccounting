package telran.ashkelon2020.accounting.dto.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class IncorrectRegistrationException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3282040666910087504L;
	
	public IncorrectRegistrationException() {
		super("Sorry your credentials are incorrect");
	}

}
