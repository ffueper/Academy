package fernandofuentesperez.academy.app.models.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;

import fernandofuentesperez.academy.app.models.entities.Client;

public interface ClientDAO extends PagingAndSortingRepository<Client, Long>{

	Client findOneByUserName(String userName);
	
}
