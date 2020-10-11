package telran.ashkelon2020.accounting.security.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import telran.ashkelon2020.accounting.dao.UserRepository;
import telran.ashkelon2020.accounting.dto.exceptions.UserNotFoundException;
import telran.ashkelon2020.accounting.model.UserAccount;

@Service
@Order(40)
public class UserOrAdminValidationFilter implements Filter{
	
	@Autowired
	UserRepository repository;

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		String path = request.getServletPath();
		String method = request.getMethod();
		if (checkEndpoint(path, method)) {
			String user = request.getUserPrincipal().getName();
			String login = path.split("/")[4];
			UserAccount userAccount = repository.findById(user).orElseThrow(() -> new UserNotFoundException(user));
			if (!(user.equals(login) || userAccount.getRoles().contains("ADMINISTRATOR"))) {
				response.sendError(403);
				return;
			}
		}
		chain.doFilter(request, response);
		
	}
	
	private boolean checkEndpoint(String path, String method) {
		boolean res = path.matches("/account/en/v1/\\w+@\\w+.\\w+/?") && "DELETE".equalsIgnoreCase(method); // delete user profile
		return res;
	}

}
