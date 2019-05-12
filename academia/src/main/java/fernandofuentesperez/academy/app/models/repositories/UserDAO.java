package fernandofuentesperez.academy.app.models.repositories;

import org.springframework.data.repository.CrudRepository;

import fernandofuentesperez.academy.app.models.entities.User;

public interface UserDAO extends CrudRepository<User, Long> {

	public User findByUserName(String userName);
}
