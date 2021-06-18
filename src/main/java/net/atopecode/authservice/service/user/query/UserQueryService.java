package net.atopecode.authservice.service.user.query;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import net.atopecode.authservice.model.user.IUserRepository;
import net.atopecode.authservice.model.user.User;
import net.atopecode.authservice.util.NormalizeString;

@Service
public class UserQueryService implements IUserQueryService{

	private IUserRepository userRepository;
	
	public UserQueryService(IUserRepository userRepositoryQuery) {
		this.userRepository = userRepositoryQuery;
	}
	
	@Override
	public Optional<User> findById(Long id) {
		Optional<User> user = Optional.empty();
		if(id != null) {
			user = userRepository.findById(id);
		}
		
		return user;
	}

	@Override
	public Optional<User> findByIdWithRoles(Long id) {
		Optional<User> user = Optional.empty();
		if(id != null) {
			user = userRepository.findByIdWithRoles(id);
		}
		
		return user;
	}

	@Override
	public Optional<User> findByName(String name) {
		Optional<User> user = Optional.empty();
		if(StringUtils.hasText(name)) {
			name = NormalizeString.normalize(name);
			user = userRepository.findByNormalizedName(name);
		}
		
		return user;
	}

	@Override
	public Optional<User> findByNameWithRoles(String name) {
		Optional<User> user = Optional.empty();
		if(StringUtils.hasText(name)) {
			name = NormalizeString.normalize(name);
			user = userRepository.findByNormalizedNameWithRoles(name);
		}
		
		return user;
	}

	@Override
	public Optional<User> findByEmail(String email) {
		Optional<User> user = Optional.empty();
		if(StringUtils.hasText(email)) {
			email = NormalizeString.normalize(email);
			user = userRepository.findByNormalizedEmail(email);
		}
		
		return user;
	}

	@Override
	public Optional<User> findByEmailWithRoles(String email) {
		Optional<User> user = Optional.empty();
		if(StringUtils.hasText(email)) {
			email = NormalizeString.normalize(email);
			user = userRepository.findByNormalizedEmailWithRoles(email);
		}
		
		return user;
	}

}
