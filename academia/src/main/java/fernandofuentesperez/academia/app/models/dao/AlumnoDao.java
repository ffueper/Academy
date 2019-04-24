package fernandofuentesperez.academia.app.models.dao;

import java.util.List;

import fernandofuentesperez.academia.app.models.entities.Alumno;

public interface AlumnoDao {

	public List<Alumno> findAll();
	
	public void save(Alumno alumno);
	
}
