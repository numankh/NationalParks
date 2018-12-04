package edu.vt.controllers;

import edu.vt.EntityBeans.PublicFile;
import edu.vt.EntityBeans.User;
import edu.vt.EntityBeans.UserFile;
import edu.vt.controllers.util.JsfUtil;
import edu.vt.controllers.util.JsfUtil.PersistAction;
import edu.vt.FacadeBeans.PublicFileFacade;
import edu.vt.globals.Constants;
import edu.vt.globals.Methods;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

@Named("publicFileController")
@SessionScoped
public class PublicFileController implements Serializable {

    @EJB
    private edu.vt.FacadeBeans.PublicFileFacade ejbFacade;
    private List<PublicFile> items = null;
    private List<PublicFile> parkItems = null;
    private PublicFile selected;
    
    @Inject
    UserFileController userFileController;
    
    @Inject
    ParkController parkController;
    
    /*
    cleanedFileNameHashMap<KEY, VALUE>
        KEY   = Integer fileId
        VALUE = String cleanedFileNameForSelected
     */
    HashMap<Integer, String> cleanedFileNameHashMap = null;

    public PublicFileController() {
    }

    public PublicFile getSelected() {
        return selected;
    }

    public void setSelected(PublicFile selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private PublicFileFacade getFacade() {
        return ejbFacade;
    }

    public PublicFile prepareCreate() {
        selected = new PublicFile();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("PublicFileCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("PublicFileUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("PublicFileDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<PublicFile> getItems() {
        if (items == null) {
            items = getFacade().findAll();
            
            // Instantiate a new hash map object
            cleanedFileNameHashMap = new HashMap<>();

            /*
            cleanedFileNameHashMap<KEY, VALUE>
                KEY   = Integer fileId
                VALUE = String cleanedFileNameForSelected
             */
            // Java 8 looping over a list with lambda
            items.forEach(userFile -> {

                // Obtain the filename stored in CloudStorage/FileStorage as 'userId_filename'
                String storedFileName = userFile.getFilename();

                // Remove the "userId_" (e.g., "4_") prefix in the stored filename
                String cleanedFileName = storedFileName.substring(storedFileName.indexOf("_") + 1);

                // Obtain the file database Primary Key id
                Integer fileId = userFile.getId();

                // Create an entry in the hash map as a key-value pair
                cleanedFileNameHashMap.put(fileId, cleanedFileName);
            });
        }
        return items;
    }

    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction != PersistAction.DELETE) {
                    getFacade().edit(selected);
                } else {
                    getFacade().remove(selected);
                }
                JsfUtil.addSuccessMessage(successMessage);
            } catch (EJBException ex) {
                String msg = "";
                Throwable cause = ex.getCause();
                if (cause != null) {
                    msg = cause.getLocalizedMessage();
                }
                if (msg.length() > 0) {
                    JsfUtil.addErrorMessage(msg);
                } else {
                    JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
                }
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            }
        }
    }

    public PublicFile getPublicFile(java.lang.Integer id) {
        return getFacade().find(id);
    }

    public List<PublicFile> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<PublicFile> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = PublicFile.class)
    public static class PublicFileControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            PublicFileController controller = (PublicFileController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "publicFileController");
            return controller.getPublicFile(getKey(value));
        }

        java.lang.Integer getKey(String value) {
            java.lang.Integer key;
            key = Integer.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Integer value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof PublicFile) {
                PublicFile o = (PublicFile) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), PublicFile.class.getName()});
                return null;
            }
        }

    }
    
    /*
    =====================================
    Return Cleaned Filename given File Id
    =====================================
     */
    public String cleanedFilenameForFileId(Integer fileId) {
        /*
        cleanedFileNameHashMap<KEY, VALUE>
            KEY   = Integer fileId
            VALUE = String cleanedFileNameForSelected
         */

        // Obtain the cleaned filename for the given fileId
        String cleanedFileName = cleanedFileNameHashMap.get(fileId);

        return cleanedFileName;
    }
    
    public List<PublicFile> getFilesForPark() {
        parkItems = getFacade().findByParkCode(parkController.getSelected().getParkCode());
        return parkItems;
    }
    
    public boolean filesForParkExist() {
        List<PublicFile> tempList = getFacade().findByParkCode(parkController.getSelected().getParkCode());
        return (tempList.size() > 0);
    }
    
    /**
     *
     * @param fileId database primary key value for a user file
     * @return the file if it is an image file; otherwise return a blank image
     */
    public String imageFile(Integer fileId) {

        // Obtain the object reference of the UserFile whose primary key = fileId
        PublicFile publicFile = getFacade().getPublicFile(fileId);

        // Obtain the userFile's filename as it is stored in the NationalParks DB database
        String imageFileName = publicFile.getFilename();

        // Extract the file extension from the filename
        String fileExtension = imageFileName.substring(imageFileName.lastIndexOf(".") + 1);

        // Convert file extension to uppercase
        String fileExtensionInCaps = fileExtension.toUpperCase();

        switch (fileExtensionInCaps) {
            case "JPG":
            case "JPEG":
            case "PNG":
            case "GIF":
                // File is an acceptable image type. Return the image file's relative path.
                return Constants.FILES_RELATIVE_PATH + imageFileName;
            case "MP4":
            case "MOV":
            case "OGG":
            case "WEBM":
                return "/resources/images/videoFile.png";
            default:
                return "/resources/images/viewFile.png";
        }
    }
    
    public void deleteByFileName(String fileName) {
        /*
        'user', the object reference of the signed-in user, was put into the SessionMap
        in the initializeSessionMap() method in LoginManager upon user's sign in.
         */
        User signedInUser = (User) Methods.sessionMap().get("user");

        // Obtain the database primary key of the signedInUser object
        Integer primaryKey = signedInUser.getId();

        // Obtain only those files from the database that belong to the signed-in user
        List<PublicFile> filesToDelete = getFacade().findPublicFilesByUserPrimaryKey(primaryKey);
        
        for(int i = 0; i < filesToDelete.size(); i++) {
            if (filesToDelete.get(i).getFilename().equals(fileName)) {
                getFacade().remove(filesToDelete.get(i));
            }
        }
        
    }

}
