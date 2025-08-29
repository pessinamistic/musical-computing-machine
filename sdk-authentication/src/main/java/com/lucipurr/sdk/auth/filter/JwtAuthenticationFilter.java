/* (C) Lucipurr 69@420 */
package com.lucipurr.sdk.auth.filter;

import com.lucipurr.sdk.auth.service.JwtService;
import com.lucipurr.sdk.auth.service.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private static final String AUTH_HEADER = "Authorization";
  private static final String BEARER_PREFIX = "Bearer ";

  private final JwtService jwtService;
  private final UserDetailsServiceImpl userDetailsService;

  public JwtAuthenticationFilter(JwtService jwtService, UserDetailsServiceImpl userDetailsService) {
    this.jwtService = jwtService;
    this.userDetailsService = userDetailsService;
  }

  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain)
      throws ServletException, IOException {
    final String authHeader = request.getHeader(AUTH_HEADER);

    if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
      filterChain.doFilter(request, response);
      return;
    }

    try {
      final String jwt = authHeader.substring(BEARER_PREFIX.length());
      final String username = jwtService.validateTokenAndGetUsername(jwt);

      if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        if (jwtService.isTokenValid(jwt, userDetails)) {
          UsernamePasswordAuthenticationToken authToken =
              new UsernamePasswordAuthenticationToken(
                  userDetails, null, userDetails.getAuthorities());
          authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
          SecurityContextHolder.getContext().setAuthentication(authToken);
        }
      }
    } catch (Exception e) {
      // Log the error if needed
      logger.error("Cannot set user authentication", e);
    }

    filterChain.doFilter(request, response);
  }

  //  @Override
  //  protected void doFilterInternal(
  //          HttpServletRequest request,
  //          HttpServletResponse response,
  //          FilterChain filterChain
  //  ) throws ServletException, IOException {
  //
  //    final String authHeader = request.getHeader("Authorization");
  //
  //    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
  //      filterChain.doFilter(request, response);
  //      return;
  //    }
  //
  //    try {
  //      final String jwt = authHeader.substring(7);
  //      final String userEmail = jwtService.extractUsername(jwt);
  //
  //      if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
  //        UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
  //
  //        if (jwtService.isTokenValid(jwt, userDetails)) {
  //          UsernamePasswordAuthenticationToken authToken =
  //                  new UsernamePasswordAuthenticationToken(
  //                          userDetails,
  //                          null,
  //                          userDetails.getAuthorities()
  //                  );
  //          authToken.setDetails(
  //                  new WebAuthenticationDetailsSource().buildDetails(request)
  //          );
  //          SecurityContextHolder.getContext().setAuthentication(authToken);
  //        }
  //      }
  //    } catch (Exception e) {
  //      // Custom error handling
  //      response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
  //      return;
  //    }
  //
  //    filterChain.doFilter(request, response);
  //  }
}
