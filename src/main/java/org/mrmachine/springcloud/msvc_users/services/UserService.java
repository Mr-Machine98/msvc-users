package org.mrmachine.springcloud.msvc_users.services;

import java.util.List;
import java.util.Optional;

import org.mrmachine.springcloud.msvc_users.entities.User;

public interface UserService {
	List<User> findAll();
	Optional<User> findById(Long id);
	Optional<User> save(User u);
	void delete(Long id);
	boolean existsByEmail(String email);
	List<User> findAllById(Iterable<Long> ids);
}
