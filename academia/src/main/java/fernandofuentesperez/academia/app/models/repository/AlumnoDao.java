package fernandofuentesperez.academia.app.models.repository;

import org.springframework.data.repository.CrudRepository;

import fernandofuentesperez.academia.app.models.entities.Alumno;

public interface AlumnoDao extends CrudRepository<Alumno, Long>{

}
