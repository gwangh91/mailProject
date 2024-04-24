package com.genest.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.genest.app.jwt.JWTFilter;
import com.genest.app.jwt.JWTUtil;
import com.genest.app.jwt.LoginFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	// AuthenticationManagerが因子として受け取るAuthenticationConfiguraionオブジェクト作成者注入
	private final AuthenticationConfiguration authenticationConfiguration;

	// JWTUtil注入
	private final JWTUtil jWTUtil;

	public SecurityConfig(AuthenticationConfiguration authenticationConfiguration, JWTUtil jWTUtil) {
		this.authenticationConfiguration = authenticationConfiguration;
		this.jWTUtil = jWTUtil;
	}

	// AuthenticationManager Bean 登録
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {

		return configuration.getAuthenticationManager();
	}

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {

		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		// csrf disable
		http.csrf((auth) -> auth.disable());

		// Fromログイン方式disable
		http.formLogin((auth) -> auth.disable());

		// http basic認証方式disable
		http.httpBasic((auth) -> auth.disable());

		// 経路別の認可作業
		http.authorizeHttpRequests((auth) -> auth
				.requestMatchers("/", "loging", "/login-form", "/join-form", "/confirmMailForm", "/sendMail",
						"/join-registration", "/join-check-email", "css/loginFormStyles.css", "images/Logo.png",
						"js/joinFormScript.js", "js/joinPopupScript.js", "css/mailFormStyles.css",
						"js/mailFormScript.js", "js/mailResultScript.js")
				.permitAll().requestMatchers("/mailForm").hasRole("USER").anyRequest().authenticated());

		http.addFilterBefore(new JWTFilter(jWTUtil), LoginFilter.class);

		// AuthenticationManager()、JWTUtilの伝達
		http.addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration), jWTUtil),
				UsernamePasswordAuthenticationFilter.class);

		// セッション設定
		http.sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		return http.build();
	}
}