package fernandofuentesperez.academia.app.models.services;

import java.util.List;

import fernandofuentesperez.academia.app.models.entities.Alumno;

public interface AlumnoService {

	public List<Alumno> findAll();
	
	public void save(Alumno alumno);
	
	public Alumno findOne(Long id);
	
	public void delete(Long id);
}
