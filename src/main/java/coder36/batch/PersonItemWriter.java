package coder36.batch;


import java.util.List;

import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;

import coder36.batch.entities.Person;

@Named
public class PersonItemWriter implements ItemWriter<PersonLine> {

	@PersistenceContext
	private EntityManager em;
	
	@Value("#{jobParameters['fileName']}")
	private String filename;
		
	public void write(List<? extends PersonLine> items) throws Exception {
		for( PersonLine p: items ) {	

			// don't create duplicates
			List<Person> l = em.createQuery( "select p from Person p where p.surname=? and p.dob=?" )
							   .setParameter(1, p.getSurname() )
							   .setParameter(2, p.getDob() ).getResultList();
			
			if( l.size() != 0 ) return;

			Person pers = new Person();
			pers.setDob( p.getDob() );
			pers.setFirstname(p.getFirstname());
			pers.setSurname(p.getSurname());
			
			em.persist(pers);		
			if ( pers.getSurname().equals( "cram" ) ) {
				throw new RuntimeException();
			}
		}	
	}
		
}
