package com.example.productmngmt.security.jwt;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.productmngmt.exceptionhandler.ResponseMessage;
import com.example.productmngmt.service.impl.MyUserDetailsServiceImpl;
import com.example.productmngmt.util.CryptoUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.ExpiredJwtException;

@Component
public class JwtFilter extends OncePerRequestFilter {

	@Autowired
	MyUserDetailsServiceImpl myUserDetailsService;

	@Autowired
	JwtTokenProvider jwtTokenProvider;

	@Autowired
	CryptoUtil cryptoUtil;
	
	@Autowired
	JwtUtil jwtutil;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		final String authorizationHeader = request.getHeader("Authorization");
		String username = null;
		String jwt = null;

		try {
			if (authorizationHeader != null) {
				jwt = authorizationHeader;
				username = jwtTokenProvider.extractUsername(jwt);
			}

			if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				UserDetails userDetails = myUserDetailsService.loadUserByUsername(username);
				jwtutil.setToken(jwt);
				jwtTokenProvider.checkIfUserAccessTokenBlackListed(jwt);

				if (Boolean.TRUE.equals(jwtTokenProvider.validateToken(jwt, userDetails))) {
					UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
							userDetails, null, userDetails.getAuthorities());
					authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(authenticationToken);
				}
			}
			filterChain.doFilter(request, response);

		} catch (ExpiredJwtException e) {

			PrintWriter out = response.getWriter();
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			ObjectMapper mapper = new ObjectMapper();
			ResponseMessage responseMessage = new ResponseMessage(new Date(), HttpStatus.UNAUTHORIZED, "Token Expired");
			out.print(mapper.writeValueAsString(responseMessage));
			out.flush();
		} catch (AccessDeniedException e) {
			PrintWriter out = response.getWriter();
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			ObjectMapper mapper = new ObjectMapper();
			ResponseMessage responseMessage = new ResponseMessage(new Date(), HttpStatus.UNAUTHORIZED, "Token Invalid");
			out.print(mapper.writeValueAsString(responseMessage));
			out.flush();
		}

	}

}
