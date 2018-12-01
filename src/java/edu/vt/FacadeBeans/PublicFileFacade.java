/*
 * Created by Brannon Angers on 2018.12.01  * 
 * Copyright © 2018 Brannon Angers. All rights reserved. * 
 */
package edu.vt.FacadeBeans;

import edu.vt.EntityBeans.PublicFile;
import edu.vt.EntityBeans.UserFile;
import edu.vt.globals.Constants;
import java.util.List;
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
    
    /**
     *
     * @param park_code
     * @return a list of object references of userFiles with the name file_name
     */
    public List<PublicFile> findByParkCode(String park_code) {
        /*
        The following @NamedQuery definition is given in UserFile.java entity class file:
        @NamedQuery(name = "UserFile.findByFilename", query = "SELECT u FROM UserFile u WHERE u.filename = :filename")
        
        The following statement obtaines the results from the named database query.
         */
        List<PublicFile> files = em.createNamedQuery("PublicFile.findByParkCode")
                .setParameter("parkCode", park_code)
                .getResultList();
        return files;
    }
    
    /**
     *
     * @param file_name
     * @return a list of object references of userFiles with the name file_name
     */
    public List<PublicFile> findByFilename(String file_name) {
        /*
        The following @NamedQuery definition is given in UserFile.java entity class file:
        @NamedQuery(name = "UserFile.findByFilename", query = "SELECT u FROM UserFile u WHERE u.filename = :filename")
        
        The following statement obtaines the results from the named database query.
         */
        List<PublicFile> files = em.createNamedQuery("PublicFile.findByFilename")
                .setParameter("filename", file_name)
                .getResultList();
        return files;
    }
    
    public PublicFile getPublicFile(int id) {
        return em.find(PublicFile.class, id);
    }
    
    /**
     *
     * @param primaryKey is the Primary Key of the user entity in the database
     * @return a list of object references of userFiles that belong to the user whose DB Primary Key = primaryKey
     */
    public List<PublicFile> findPublicFilesByUserPrimaryKey(Integer primaryKey) {
        /*
        The following @NamedQuery definition is given in UserFile.java entity class file:
        @NamedQuery(name = "UserFile.findUserFilesByUserId", query = "SELECT u FROM UserFile u WHERE u.userId.id = :userId")
        
        The following statement obtaines the results from the named database query.
         */
        List<PublicFile> publicFiles = em.createNamedQuery("PublicFile.findPublicFilesByUserId")
                .setParameter("userId", primaryKey)
                .getResultList();
        return publicFiles;
    }
    
}
