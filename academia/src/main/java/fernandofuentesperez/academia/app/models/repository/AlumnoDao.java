package fernandofuentesperez.academia.app.models.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import fernandofuentesperez.academia.app.models.entities.Alumno;

public interface AlumnoDao extends PagingAndSortingRepository<Alumno, Long>{

}
