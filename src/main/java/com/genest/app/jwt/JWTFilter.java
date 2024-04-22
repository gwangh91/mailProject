package com.genest.app.jwt;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.genest.app.dto.CustomUserDetails;
import com.genest.app.entity.UserJoin;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JWTFilter extends OncePerRequestFilter {

	private final JWTUtil jwtUtil;

	public JWTFilter(JWTUtil jwtUtil) {
		this.jwtUtil = jwtUtil;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String authorization = request.getHeader("Authorization");

		if (authorization == null || !authorization.startsWith("Bearer ")) {

			filterChain.doFilter(request, response);

			return;
		}

		String token = authorization.split(" ")[1];

		if (jwtUtil.isExpired(token)) {
			filterChain.doFilter(request, response);

			return;
		}

		String email = jwtUtil.getEmail(token);
		String role = jwtUtil.getRole(token);

		UserJoin userJoin = new UserJoin();
		userJoin.setEmail(email);
		userJoin.setPassword("temppassword");
		userJoin.setRole(role);

		CustomUserDetails customUserDetails = new CustomUserDetails(userJoin);

		Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null,
				customUserDetails.getAuthorities());

		SecurityContextHolder.getContext().setAuthentication(authToken);

		filterChain.doFilter(request, response);
	}

}