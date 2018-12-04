/*
 * Created by Adam Wilborn on 2018.06.18
 * Copyright © 2018 Adam Wilborn. All rights reserved.
 */
package edu.vt.FacadeBeans;
import edu.vt.EntityBeans.UserBlog;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

// @Stateless annotation implies that the conversational state with the client shall NOT be maintained.
@Stateless

public class UserBlogFacade extends AbstractFacade<UserBlog> {

    /*
    Annotating 'private EntityManager entityManager;' with @PersistenceContext
    implies that the EntityManager instance pointed to by 'entityManager' is 
    associated with the 'Videos-WilbornPU' persistence context.
    
    The persistence context is a set of entity (UserBlog) instances in which for any
    persistent entity (UserBlog) identity, there is a unique entity (UserBlog) instance. 
    
    Within the persistence context, the entity (UserBlog) instances and their life cycle are
    managed. The EntityManager API is used to create and remove persistent entity (UserBlog)
    instances, to find entities by their primary key, and to query over entities (NationalParks).
     */
    @PersistenceContext(unitName = "NationalParksPU")
    
    // 'entityManager' holds the object reference to the instantiated EntityManager object.
    private EntityManager entityManager;

    // @Override annotation indicates that the super class AbstractFacade's getEntityManager() method is overridden.
    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }

    /*
    This constructor method invokes the parent abstract class AbstractFacade.java's 
    constructor method, which in turn initializes its entityClass instance variable
    with the UserBlog class object reference returned by the UserBlog.class. 
    */
    public UserBlogFacade() {
        super(UserBlog.class); 
        // Invokes super's AbstractFacade constructor method by passing
        // UserBlog.class, which is the object reference of the UserBlog class.
    }
    
    public List<UserBlog> findUserBlogsByUserPrimaryKey(Integer primaryKey) {
        /*
        The following @NamedQuery definition is given in UserBlog.java entity class file:
        @NamedQuery(name = "UserBlog.findUserBlogsByUserId", query = "SELECT u FROM UserBlog u WHERE u.userId.id = :userId")
        
        The following statement obtaines the results from the named database query.
         */
        List<UserBlog> userBlogs = entityManager.createNamedQuery("UserBlog.findUserBlogsByUserId")
                .setParameter("userId", primaryKey)
                .getResultList();

        return userBlogs;
    }
    
    /*
    See https://docs.oracle.com/javaee/7/api/ for documentation of Java EE 7 API packages, classes, and methods.
    
    Read https://docs.oracle.com/javaee/6/tutorial/doc/bnbtl.html#bnbtm to learn about Simple Queries,
    specifically the ones with the LIKE expression.
    
    The LIKE Expression
        SELECT p FROM Player p WHERE p.name LIKE ’Mich%’
    
        Data retrieved: All players whose names begin with “Mich”
    
        Description: The LIKE expression uses wildcard characters to search for strings that match the wildcard pattern.
        In this case, the query uses the LIKE expression and the % wildcard to find all players whose names begin with
        the string “Mich” For example, “Michael” and “Michelle” both match the wildcard pattern.

    ================================
    EntityManager Method createQuery
    ================================
    Query createQuery(String qlString)
        Create an instance of Query for executing a Java Persistence query language statement.
    Parameter:
        qlString - a Java Persistence query string, e.g., "SELECT c FROM UserBlog c WHERE c.name LIKE :searchString"
    Returns:
        the object reference of the newly created Query object

    =========================
    Query Method setParameter
    =========================
    Query setParameter(String name, Object value)
        Bind an argument value to a named parameter
    Parameters:
        name - parameter name (In our case, "searchString")
        value - parameter value (In our case, the searchString parameter that contains the search string the user entered for searching)
    Returns:
        the same object reference of the newly created Query object

    ==========================
    Query Method getResultList
    ==========================
    List getResultList()
        Execute a SELECT query and return the query results as an untyped List
    Returns:
        the object reference of the newly created List containing the search results

    -----------------------------
    Search Category: COMPANY NAME
    -----------------------------
     */
    /**
     * Searches NationalParksDB for companies where company name contains the searchString entered by the user.
     *
     * @param searchString contains the search string the user entered for searching company names
     * @return A list of UserBlog object references as the search results
     */
    // This method is called in the getSearchItems() method in UserBlogController.java
    public List<UserBlog> parkQuery(String searchString) {
        // Place the % wildcard before and after the search string to search for it anywhere in the company name 
        searchString = "%" + searchString + "%";
        // Conduct the search in a case-insensitive manner and return the results in a list.
        return getEntityManager().createQuery("SELECT c FROM UserBlog c WHERE c.park LIKE :searchString").setParameter("searchString", searchString).getResultList();
    }

    /*
    --------------------------------------
    Search Category: STOCK SYMBOL (TICKER)
    --------------------------------------
     */
    /**
     * Searches NationalParksDB for companies where company's stock ticker name contains the searchString entered by the user.
     *
     * @param searchString contains the search string the user entered for searching stock tickers
     * @return A list of UserBlog object references as the search results
     */
    // This method is called in the getSearchItems() method in UserBlogController.java
    public List<UserBlog> descriptionQuery(String searchString) {
        // Place the % wildcard before and after the search string to search for it anywhere in the stock ticker name 
        searchString = "%" + searchString + "%";
        // Conduct the search in a case-insensitive manner and return the results in a list.
        return getEntityManager().createQuery("SELECT c FROM UserBlog c WHERE c.description LIKE :searchString").setParameter("searchString", searchString).getResultList();
    }

    /*
    -------------------------------------
    Search Category: BUSINESS SECTOR NAME
    -------------------------------------
     */
    /**
     * Searches NationalParksDB for companies where business Sector name contains the searchString entered by the user.
     *
     * @param searchString contains the search string the user entered for searching business Sector names
     * @return A list of UserBlog object references as the search results
     */
    // This method is called in the getSearchItems() method in UserBlogController.java
    public List<UserBlog> ratingQuery(String searchString) {
        // Place the % wildcard before and after the search string to search for it anywhere in the business Sector name 
        searchString = "%" + searchString + "%";
        // Conduct the search in a case-insensitive manner and return the results in a list.
        return getEntityManager().createQuery("SELECT c FROM UserBlog c WHERE c.rating LIKE :searchString").setParameter("searchString", searchString).getResultList();
    }

    /*
    --------------------
    Search Category: ALL
    --------------------
     */
    /**
     * Searches NationalParksDB for companies where company name, ticker, and sector name contains the searchString entered by the user.
     *
     * @param searchString contains the search string the user entered for searching company name, ticker, and sector name
     * @return A list of UserBlog object references as the search results
     */
    // This method is called in the getSearchItems() method in UserBlogController.java
    public List<UserBlog> allQuery(String searchString) {
        // Place the % wildcard before and after the search string to search for it anywhere in company name, ticker, or sector name 
        searchString = "%" + searchString + "%";
        // Conduct the search in a case-insensitive manner and return the results in a list.
        return getEntityManager().createQuery("SELECT c FROM UserBlog c WHERE c.park LIKE :searchString OR c.description LIKE :searchString OR c.rating LIKE :searchString").setParameter("searchString", searchString).getResultList();
    }
}
