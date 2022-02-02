package me.sokdak.miningstatproxy.filter;

import java.io.IOException;
import java.util.List;
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
  private static final String AUTH_KEY_HEADER_NAME = "X-Auth-Key";

  private final SecurityFilterProperties securityFilterProperties;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    if (!isExcludedPath(request)) {
      String authKeyHeader = request.getHeader(AUTH_KEY_HEADER_NAME);
      if (!authKeyHeader.equalsIgnoreCase(securityFilterProperties.getApiKey()))
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "api key does not match");
    }
    else {
      log.info(">> bypassing filter; excluded path {}", request.getServletPath());
    }
    filterChain.doFilter(request, response);
  }

  private boolean isExcludedPath(HttpServletRequest request) {
    String path = request.getServletPath();
    List<String> excludePaths = List.of("/", "/index.html", "/script.js", "/style.css");
    return excludePaths.stream().anyMatch(path::equalsIgnoreCase);
  }
}
