package org.mrmachine.springcloud.msvc_users.services.impl;

import java.util.List;
import java.util.Optional;

import org.mrmachine.springcloud.msvc_users.clients.CourseClientRest;
import org.mrmachine.springcloud.msvc_users.entities.User;
import org.mrmachine.springcloud.msvc_users.repositories.UserRepository;
import org.mrmachine.springcloud.msvc_users.services.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class UserServiceImpl implements UserService {
	
	private final UserRepository repository;
	private CourseClientRest client;

    UserServiceImpl(UserRepository repository, CourseClientRest client) {
        this.repository = repository;
        this.client = client;
    }

	@Override
	public List<User> findAll() {
		System.out.println("list of users");
		return this.repository.findAll();
	}

	@Override
	public Optional<User> findById(Long id) {
		return this.repository.findById(id);
	}

	@Transactional
	@Override
	public Optional<User> save(User u) {
		return Optional.of(this.repository.save(u));
	}

	@Transactional
	@Override
	public void delete(Long id) {
		this.repository.deleteById(id);
		this.client.deleteUserFromCourse(id);
	}

	@Override
	public boolean existsByEmail(String email) {
		return this.repository.existsByEmail(email);
	}

	@Override
	public List<User> findAllById(Iterable<Long> ids) {
		return this.repository.findAllById(ids);
	}

}
