/*
 * Created by Nick Eda on 2018.06.10
 * Copyright © 2018 Nick Eda. All rights reserved. 
 */
package edu.vt.FacadeBeans;

import edu.vt.EntityBeans.UserTrip;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

// @Stateless annotation implies that the conversational state with the client shall NOT be maintained.
@Stateless
public class UserTripFacade extends AbstractFacade<UserTrip> {

    /*
    Annotating 'private EntityManager em;' with '@PersistenceContext(unitName = "HealthTripPU")' implies that
    the EntityManager instance pointed to by 'em' is associated with the 'HealthTripPU' persistence context. 
    
    Here, Entity is the UserTrip object. The persistence context is a set of entity (UserTrip)
    instances in which for any persistent entity identity, there is a unique entity instance. 
    
    Within the persistence context, the entity instances and their life cycles are managed. The EntityManager API is used
    to create and remove persistent entity instances, to find entities by their primary key, and to query over entities.
     */
    @PersistenceContext(unitName = "NationalParksPU")
    private EntityManager em;  // 'em' holds the object reference to the instantiated EntityManager object.

    // @Override annotation indicates that the super class AbstractFacade's getEntityManager() method is overridden.
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    /*
     This constructor method invokes the parent abstract class AbstractFacade.java's 
     constructor method, which in turn initializes its entityClass instance variable
     with the UserTrip class object reference returned by the UserTrip.class. 
     */
    public UserTripFacade() {
        super(UserTrip.class);
    }

    public UserTrip getUserTrip(int id) {
        return em.find(UserTrip.class, id);
    }

    /*
    ====================================================
    The following method is added to the generated code.
    ====================================================
     */

    /**
     * Find all surveys that belong to a User whose database primary key is dbPrimaryKey
     * 
     * @param dbPrimaryKey is the Primary Key of the user entity in the database
     * @return a list of object references of userTrips that belong to the user whose database Primary Key = dbPrimaryKey
     */
    public List<UserTrip> findUserTripsByUserPrimaryKey(Integer dbPrimaryKey) {
        /*
        The following @NamedQuery is defined in UserTrip.java entity class file:
        @NamedQuery(name = "UserTrip.findTripsByUserPrimaryKey", 
            query = "SELECT u FROM UserTrip u WHERE u.userId.id = :primaryKey")
        
        The following statement obtaines the results from the named database query.
         */
        List<UserTrip> userTrips = em.createNamedQuery("UserTrip.findTripsByUserPrimaryKey")
                .setParameter("primaryKey", dbPrimaryKey)
                .getResultList();

        return userTrips;
    }

      
}
