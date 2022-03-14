package br.com.letscode.token.service;


import br.com.letscode.exception.BusinessException;
import br.com.letscode.token.entity.User;
import br.com.letscode.token.repository.UserRepository;
import br.com.letscode.token.web.AppUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) {
		User user = userRepository.findByLogin(username).orElseThrow(() -> new BusinessException("User not found with username: ".concat(username)));
		return new AppUserDetails(user);
	}

}