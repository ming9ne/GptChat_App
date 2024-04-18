package com.chatgpt.config;

import com.chatgpt.model.UserInfo;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Collection;

public class UserDetailsImpl implements UserDetails{
	private static final long serialVersionUID = 1L;
	private UserInfo userInfo;
	private PasswordEncoder passwordEncoder;
	
	public UserDetailsImpl(UserInfo userInfo, PasswordEncoder passwordEncoder) {
		this.userInfo = userInfo;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		ArrayList<GrantedAuthority> auth = new ArrayList<GrantedAuthority>();
		auth.add(new SimpleGrantedAuthority(userInfo.getRole()));
		System.out.println("######## UserDetailsImpl-getAuthorities Called : [" + userInfo.getRole() + "]");
		return auth;
	}

	@Override
	public String getPassword() {

		return passwordEncoder.encode(userInfo.getPassword());
	}

	@Override
	public String getUsername() {
		System.out.println("######## UserDetailsImpl-getUsername Called : " + userInfo.getUserId());
		return userInfo.getUserId();
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

}
