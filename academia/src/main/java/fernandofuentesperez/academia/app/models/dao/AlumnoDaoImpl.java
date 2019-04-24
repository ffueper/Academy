package fernandofuentesperez.academia.app.models.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import fernandofuentesperez.academia.app.models.entities.Alumno;

@Repository("alumnoDaoJPA")
public class AlumnoDaoImpl implements AlumnoDao {

	@PersistenceContext
	EntityManager em;
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
	@Override
	public List<Alumno> findAll() {
		
		return em.createQuery("from Alumno").getResultList();
	}

	@Transactional
	@Override
	public void save(Alumno alumno) {
		em.persist(alumno);
		
	}

}
