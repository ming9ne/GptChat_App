package com.chatgpt.config;

import com.chatgpt.model.UserInfo;
import com.chatgpt.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailServiceImpl implements UserDetailsService{

	private UserRepository userresp;
	private PasswordEncoder passwordEncoder;

	@Autowired
	public UserDetailServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userresp = userRepository;
		this.passwordEncoder = passwordEncoder;
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		Optional<UserInfo> useropt = userresp.findById(username);
		if ( useropt.isEmpty()) {
			throw new UsernameNotFoundException(username);
		}
			
		return new UserDetailsImpl(useropt.get(), passwordEncoder);
	}

}
