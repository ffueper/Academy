package fernandofuentesperez.academy.app.models.repositories;

import org.springframework.data.repository.CrudRepository;

import fernandofuentesperez.academy.app.models.entities.Role;

public interface RoleDAO extends CrudRepository<Role, Long> {

}
