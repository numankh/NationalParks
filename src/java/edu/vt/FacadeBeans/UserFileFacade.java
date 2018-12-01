/*
 * Created by Brannon Angers on 2018.12.01  * 
 * Copyright © 2018 Brannon Angers. All rights reserved. * 
 */
package edu.vt.FacadeBeans;

import edu.vt.EntityBeans.UserFile;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class UserFileFacade extends AbstractFacade<UserFile> {

    @PersistenceContext(unitName = "NationalParksPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UserFileFacade() {
        super(UserFile.class);
    }
    
    public UserFile getUserFile(int id) {
        return em.find(UserFile.class, id);
    }
    
    /*
    ======================================================
    The following methods are added to the generated code.
    ======================================================
     */
    /**
     *
     * @param primaryKey is the Primary Key of the user entity in the database
     * @return a list of object references of userFiles that belong to the user whose DB Primary Key = primaryKey
     */
    public List<UserFile> findUserFilesByUserPrimaryKey(Integer primaryKey) {
        /*
        The following @NamedQuery definition is given in UserFile.java entity class file:
        @NamedQuery(name = "UserFile.findUserFilesByUserId", query = "SELECT u FROM UserFile u WHERE u.userId.id = :userId")
        
        The following statement obtaines the results from the named database query.
         */
        List<UserFile> userFiles = em.createNamedQuery("UserFile.findUserFilesByUserId")
                .setParameter("userId", primaryKey)
                .getResultList();
        return userFiles;
    }
    
    /**
     *
     * @param file_name
     * @return a list of object references of userFiles with the name file_name
     */
    public List<UserFile> findByFilename(String file_name) {
        /*
        The following @NamedQuery definition is given in UserFile.java entity class file:
        @NamedQuery(name = "UserFile.findByFilename", query = "SELECT u FROM UserFile u WHERE u.filename = :filename")
        
        The following statement obtaines the results from the named database query.
         */
        List<UserFile> files = em.createNamedQuery("UserFile.findByFilename")
                .setParameter("filename", file_name)
                .getResultList();
        return files;
    }
    
}
