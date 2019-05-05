package fernandofuentesperez.academy.app.models.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import fernandofuentesperez.academy.app.models.entities.Client;

public interface ClientService {

	public List<Client> findAll();
	
	public Page<Client> findAll(Pageable pageable);
	
	public void save(Client client);
	
	public Client findOne(Long id);
	
	public void delete(Long id);
	
}
