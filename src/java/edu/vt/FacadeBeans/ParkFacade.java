/*
 * Created by Adam Wilborn on 2018.11.20  * 
 * Copyright © 2018 Adam Wilborn. All rights reserved. * 
 */
package edu.vt.FacadeBeans;

import edu.vt.EntityBeans.NationalParks;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class ParkFacade extends AbstractFacade<NationalParks>{

    @PersistenceContext(unitName = "NationalParksPU")
    private EntityManager em;
    
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    public ParkFacade() {
        super(NationalParks.class);
    }

    public List<NationalParks> nameQuery(String searchString) {
        searchString = "%" + searchString + "%";
        return getEntityManager().createQuery("SELECT c FROM NationalParks c WHERE c.fullName LIKE :searchString").setParameter("searchString", searchString).getResultList();
    }

    public List<NationalParks> stateQuery(String searchString) {
        searchString = "%" + searchString + "%";
        return getEntityManager().createQuery("SELECT c FROM NationalParks c WHERE c.states LIKE :searchString").setParameter("searchString", searchString).getResultList();
    }

    public List<NationalParks> allQuery(String searchString) {
        searchString = "%" + searchString + "%";
        return getEntityManager().createQuery("SELECT c FROM NationalParks c WHERE c.fullName LIKE :searchString OR c.states LIKE :searchString").setParameter("searchString", searchString).getResultList();
    }
}
