package org.mrmachine.springcloud.msvc_users.repositories;

import org.mrmachine.springcloud.msvc_users.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {
	boolean existsByEmail(@Param("email") String email);
}
