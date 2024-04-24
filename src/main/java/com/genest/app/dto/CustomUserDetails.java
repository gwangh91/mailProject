package com.genest.app.dto;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.genest.app.entity.UserJoin;

public class CustomUserDetails implements UserDetails {

	private static final long serialVersionUID = 1L;

	private final UserJoin userJoin;

	public CustomUserDetails(UserJoin userJoin) {
		this.userJoin = userJoin;
	}

	public CustomUserDetails() {
		this.userJoin = new UserJoin();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {

		Collection<GrantedAuthority> collection = new ArrayList<>();

		collection.add(new GrantedAuthority() {
			private static final long serialVersionUID = 1L;

			@Override
			public String getAuthority() {
				return userJoin.getRole();
			}
		});

		return collection;
	}

	@Override
	public String getPassword() {
		return userJoin.getPassword();
	}

	@Override
	public String getUsername() {
		return userJoin.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}