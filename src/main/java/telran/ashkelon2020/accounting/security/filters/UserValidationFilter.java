package telran.ashkelon2020.accounting.security.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

@Service
@Order(20)
public class UserValidationFilter implements Filter {

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
			if (!user.equals(login)) {
				response.sendError(403);
				return;
			}
		}
		chain.doFilter(request, response);

	}

	private boolean checkEndpoint(String path, String method) {
		boolean res = path.matches("/account/en/v1/\\w+@\\w+.\\w+/?") && "PUT".equalsIgnoreCase(method); // edit user profile
		res = res || (path.matches("/account/en/v1/\\w+@\\w+.\\w+/favorite/\\w+/?") && ("PUT".equalsIgnoreCase(method) || "DELETE".equalsIgnoreCase(method))); // add and delete user favourites
		res = res || (path.matches("/account/en/v1/\\w+@\\w+.\\w+/activity/\\w+/?") && ("PUT".equalsIgnoreCase(method) || "DELETE".equalsIgnoreCase(method))); // add and delete user activity
		res = res || (path.matches("/account/en/v1/\\w+@\\w+.\\w+/?") && "GET".equalsIgnoreCase(method)); //get user data
		return res;
	}

}
