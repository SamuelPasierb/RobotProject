package services;

import java.util.List;

// Imports
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import data.RobotValues;

public class DatabaseService {
    
    // Database variables
    private final EntityManagerFactory emf;
    private final EntityManager em; 

    /**
     * Service for maning database
     */
    public DatabaseService() {
        this.emf = Persistence.createEntityManagerFactory("lego");
        this.em = emf.createEntityManager();
    }

    /**
     * Saves {@link RobotValues} to the Database.
     * 
     * @param robot {@link RobotValues} to save
     */
    public final void save(RobotValues robot) {
        em.getTransaction().begin();
		em.persist(robot);
		em.getTransaction().commit();
    }
    
    /**
     * Loads the data from the Database
     * 
     * @param query {@code MySQL} query that you want to load
     * @return {@link List} of {@link RobotValues} from the Database
     */
    @SuppressWarnings("unchecked")
    public final List<RobotValues> load(String query) {
        return em.createQuery(query).getResultList();
    }

}
