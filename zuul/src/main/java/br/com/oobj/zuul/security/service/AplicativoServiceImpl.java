package br.com.oobj.zuul.security.service;

import java.util.Arrays;
import java.util.Optional;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import br.com.oobj.zuul.security.model.Aplicativo;
import br.com.oobj.zuul.security.repository.AplicativoRepository;

//@Service
public class AplicativoServiceImpl implements UserDetailsService {
	
	private final AplicativoRepository aplicativoRepository;

//	@Autowired
	public AplicativoServiceImpl(AplicativoRepository aplicativoRepository) {
		this.aplicativoRepository = aplicativoRepository;
	}

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) {
		Optional<Aplicativo> optional = aplicativoRepository.findByToken(username);
		if (optional.isPresent()) {
			Aplicativo aplicativo = optional.get();
			return new User(aplicativo.getToken(), aplicativo.getSecret(), Arrays.asList(() -> "USER"));
		} else {
			throw new UsernameNotFoundException("application not found: " + username);
		}
	}
	
}
