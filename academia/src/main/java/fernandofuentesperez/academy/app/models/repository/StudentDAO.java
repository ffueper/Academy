package fernandofuentesperez.academy.app.models.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import fernandofuentesperez.academy.app.models.entities.Student;

public interface StudentDAO extends PagingAndSortingRepository<Student, Long>{

}
