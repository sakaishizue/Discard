package com.example.demo.service.impl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Authentication;
import com.example.demo.entity.LoginUser;
import com.example.demo.repository.AuthenticationMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LoginUserDetailsServiceImpl implements UserDetailsService {
	
	private final AuthenticationMapper authenticationMapper;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Authentication authentication = authenticationMapper.selectByUsername(username);
		if (authentication != null) {
			return new LoginUser(authentication.getUsername(),authentication.getPassword(),
					authentication.getDisplayname());
		} else {
			throw new UsernameNotFoundException(username + " => 指定のユーザー名なし");
		}
	}

//	private List<GrantedAuthority> getAuthorityList(Role role){
//		List<GrantedAuthority> authorities = new ArrayList<>();
//		authorities.add(new SimpleGrantedAuthority(role.name()));
//		if (role == Role.ADMIN) {
//			authorities.add(new SimpleGrantedAuthority(Role.USER.toString()));
//		}
//		return authorities;
//	}
}
