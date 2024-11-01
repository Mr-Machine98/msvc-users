package org.mrmachine.springcloud.msvc_users.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "msvc-courses", url = "msvc-courses:8002")
public interface CourseClientRest {

	@DeleteMapping("/delete-user-from-couse/{id}")
	void deleteUserFromCourse(@PathVariable Long id);
}
