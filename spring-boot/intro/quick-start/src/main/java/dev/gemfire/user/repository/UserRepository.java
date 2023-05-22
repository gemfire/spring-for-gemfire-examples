package dev.gemfire.user.repository;

import dev.gemfire.user.model.User;
import org.springframework.data.gemfire.repository.GemfireRepository;

//interface UserRepository extends CrudRepository<User, String> { }
public interface UserRepository extends GemfireRepository<User, String> {
}
