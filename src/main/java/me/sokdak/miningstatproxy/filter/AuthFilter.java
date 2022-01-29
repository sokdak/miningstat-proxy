package me.sokdak.miningstatproxy.filter;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.sokdak.miningstatproxy.property.SecurityFilterProperties;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@WebFilter("/**")
@Order(1)
@RequiredArgsConstructor
public class AuthFilter extends OncePerRequestFilter {
  private final SecurityFilterProperties securityFilterProperties;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    if (!request.getRequestURI().contains(securityFilterProperties.getApiKey()))
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "api key does not match");
    filterChain.doFilter(request, response);
  }
}
