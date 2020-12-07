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

//@Service
//@Order(5)
public class CorsFilter implements Filter {

  @Override
  public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
      throws IOException, ServletException {
    HttpServletRequest request = (HttpServletRequest) req;
    HttpServletResponse response = (HttpServletResponse) res;
    response.setHeader("Access-Control-Allow-Origin", "*");
    response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE, HEAD");
    response.setHeader("Access-Control-Max-Age", "3600");
    response.setHeader("Access-Control-Allow-Headers",
        "Content-Type, Authorization, X-Token, X-ServiceName, X-Login");
    response.addHeader("Access-Control-Expose-Headers", "*");
    if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
      response.setStatus(HttpServletResponse.SC_ACCEPTED);
      return;
    }
    chain.doFilter(request, response);
  }
}



//package telran.ashkelon2020.accounting.security.filters;
//
//import java.io.IOException;
//
//import javax.servlet.Filter;
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.ServletRequest;
//import javax.servlet.ServletResponse;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.springframework.core.annotation.Order;
//import org.springframework.stereotype.Service;
//
//@Service
//@Order(5)
//public class CorsFilter implements Filter {
//
//	@Override
//	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
//			throws IOException, ServletException {
//		HttpServletRequest request = (HttpServletRequest) req;
//		HttpServletResponse response = (HttpServletResponse) resp;
//
//		response.setHeader("Access-Control-Allow-Origin", "*");
////    response.setHeader("Access-Control-Allow-Credentials", "true");
//		response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
//		response.setHeader("Access-Control-Max-Age", "3600");
//		response.setHeader("Access-Control-Allow-Headers", "Authorization, content-type, X-Token");
//		response.addHeader("Access-Control-Expose-Headers", "X-Token");
//		if ("OPTIONS".equals(request.getMethod())) {
//			response.setStatus(HttpServletResponse.SC_ACCEPTED);
//		} else {
//			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
//		}
//
//		chain.doFilter(request, response);
//
//	}
//}