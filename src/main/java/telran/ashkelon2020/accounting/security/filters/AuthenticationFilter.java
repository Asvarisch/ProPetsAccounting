package telran.ashkelon2020.accounting.security.filters;

import static telran.ashkelon2020.accounting.configuration.Constants.TOKEN_HEADER;


import java.io.IOException;
import java.security.Principal;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import telran.ashkelon2020.accounting.dao.UserRepository;
import telran.ashkelon2020.accounting.dto.UserInfoDto;
import telran.ashkelon2020.accounting.model.UserAccount;
import telran.ashkelon2020.accounting.service.TokenService;

@Service
@Order(10)
public class AuthenticationFilter implements Filter {

	@Autowired
	UserRepository repository;

	@Autowired
	TokenService tokenService;

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		if (!checkEndpoint(request.getServletPath(), request.getMethod())) {
			try {
				String login;
				String token;
				if (checkLoginEndpoint(request.getServletPath())) {
					//Token Authorisation in Base 64(user+password) handling
					token = request.getHeader("Authorization");
					String[] credentials = tokenService.validateTokenBase64(token);
					UserAccount userAccount = repository.findById(credentials[0]).orElse(null);
					if (userAccount == null) {
						response.sendError(401);
						return;
					}
					if (!BCrypt.checkpw(credentials[1],userAccount.getPassword())) {
						response.sendError(403);
						return;
					}
					login = userAccount.getEmail();
					response.setHeader(TOKEN_HEADER, tokenService.createToken(userAccount));
					
				} else {
					//Token X-Token handling
					token = request.getHeader(TOKEN_HEADER);
					if (token != null) {
						UserInfoDto userInfoDto = tokenService.validateToken(token);
						login = userInfoDto.getEmail();
						response.setHeader(TOKEN_HEADER, userInfoDto.getToken());
					} else {
						response.sendError(403);
						return;
					}	
				}
				request = new WrapperRequest(request, login);
				
			} catch (Exception e) {
				e.printStackTrace();
				response.sendError(400);
				return;
			}
		}

		chain.doFilter(request, response);

	}

	

	private boolean checkEndpoint(String path, String method) {
		boolean res = path.matches("/account/en/v1/registration/?");
		res = res || (path.matches("/account/en/v1/token/validation/?") && "GET".equalsIgnoreCase(method));
		res = res || (path.matches("/account/en/v1/\\w+@\\w+.\\w+/info/?") && "GET".equalsIgnoreCase(method)); // user information - for other services to use for user authentication and further authorization 
		return res;
	}
	
	private boolean checkLoginEndpoint(String path) {
		return path.matches("/account/en/v1/login/?");
	}
	
	private class WrapperRequest extends HttpServletRequestWrapper {
		String user;

		public WrapperRequest(HttpServletRequest request, String user) {
			super(request);
			this.user = user;
		}

		@Override
		public Principal getUserPrincipal() {
			return new Principal() {

				@Override
				public String getName() {
					return user;
				}
			};
		}
	}

}
