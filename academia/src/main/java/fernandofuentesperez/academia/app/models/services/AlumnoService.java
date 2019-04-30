package fernandofuentesperez.academia.app.models.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import fernandofuentesperez.academia.app.models.entities.Alumno;

public interface AlumnoService {

	public List<Alumno> findAll();
	
	public Page<Alumno> findAll(Pageable pageable);
	
	public void save(Alumno alumno);
	
	public Alumno findOne(Long id);
	
	public void delete(Long id);
}
