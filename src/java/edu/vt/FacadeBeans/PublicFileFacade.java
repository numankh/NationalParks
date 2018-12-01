/*
 * Created by Brannon Angers on 2018.12.01  * 
 * Copyright © 2018 Brannon Angers. All rights reserved. * 
 */
package edu.vt.FacadeBeans;

import edu.vt.EntityBeans.PublicFile;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Brannon
 */
@Stateless
public class PublicFileFacade extends AbstractFacade<PublicFile> {

    @PersistenceContext(unitName = "NationalParksPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PublicFileFacade() {
        super(PublicFile.class);
    }
    
}
