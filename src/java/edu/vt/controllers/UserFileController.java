/*
 * Created by Osman Balci on 2018.06.16
 * Copyright © 2018 Osman Balci. All rights reserved.
 */
package edu.vt.controllers;

import edu.vt.EntityBeans.User;
import edu.vt.EntityBeans.UserFile;
import edu.vt.FacadeBeans.UserFacade;
import edu.vt.controllers.util.JsfUtil;
import edu.vt.controllers.util.JsfUtil.PersistAction;
import edu.vt.FacadeBeans.UserFileFacade;
import edu.vt.globals.Constants;
import edu.vt.globals.Methods;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
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
import javax.faces.component.UIOutput;
import javax.faces.event.AjaxBehaviorEvent;

@Named("userFileController")
@SessionScoped

public class UserFileController implements Serializable {

    /*
    ===============================
    Instance Variables (Properties)
    ===============================

    The instance variable 'userFacade' is annotated with the @EJB annotation.
    The @EJB annotation directs the EJB Container (of the GlassFish AS) to inject (store) the object reference
    of the UserFacade object, after it is instantiated at runtime, into the instance variable 'userFacade'.
     */
    @EJB
    private UserFacade userFacade;

    /*
    The instance variable 'userFileFacade' is annotated with the @EJB annotation.
    The @EJB annotation directs the EJB Container (of the GlassFish AS) to inject (store) the object reference 
    of the UserFileFacade object, after it is instantiated at runtime, into the instance variable 'userFileFacade'.
     */
    @EJB
    private UserFileFacade userFileFacade;

    // 'selected' contains the object reference of the selected User File object
    private UserFile selected;

    // 'items' is a List containing the object references of User File objects
    private List<UserFile> items = null;

    /*
    cleanedFileNameHashMap<KEY, VALUE>
        KEY   = Integer fileId
        VALUE = String cleanedFileNameForSelected
     */
    HashMap<Integer, String> cleanedFileNameHashMap = null;

    // Selected row number in p:dataTable in ListUserFiles.xhtml
    private String selectedRowNumber = "0";
    
    String parkCode;

    /*
    ==================
    Constructor Method
    ==================
     */
    public UserFileController() {
    }

    /*
    =========================
    Getter and Setter Methods
    =========================
     */
    public UserFacade getUserFacade() {
        return userFacade;
    }

    public void setUserFacade(UserFacade userFacade) {
        this.userFacade = userFacade;
    }

    public UserFileFacade getUserFileFacade() {
        return userFileFacade;
    }

    public void setUserFileFacade(UserFileFacade userFileFacade) {
        this.userFileFacade = userFileFacade;
    }

    public UserFile getSelected() {
        return selected;
    }

    public void setSelected(UserFile selected) {
        this.selected = selected;
    }

    public String getSelectedRowNumber() {
        return selectedRowNumber;
    }

    public void setSelectedRowNumber(String selectedRowNumber) {
        this.selectedRowNumber = selectedRowNumber;
    }

    /*
    ***************************************************************
    Return the List of User Files that Belong to the Signed-In User
    ***************************************************************
     */
    public List<UserFile> getItems() {

        if (items == null) {
            /*
            'user', the object reference of the signed-in user, was put into the SessionMap
            in the initializeSessionMap() method in LoginManager upon user's sign in.
             */
            User signedInUser = (User) Methods.sessionMap().get("user");

            // Obtain the database primary key of the signedInUser object
            Integer primaryKey = signedInUser.getId();

            // Obtain only those files from the database that belong to the signed-in user
            items = getUserFileFacade().findUserFilesByUserPrimaryKey(primaryKey);

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

    /*
    ================
    Instance Methods
    ================

    *********************************
    Prepare to Create a New User File
    *********************************
     */
    public UserFile prepareCreate() {
        // Instantiate a new UserFile object and store its object reference into instance variable 'selected'
        // The UserFile class is defined in UserFile.java
        selected = new UserFile();
        return selected;
    }

    /*
    **********************
    Create a New User File
    **********************
     */
    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("UserFileCreated"));

        /*
        JsfUtil.isValidationFailed() returns TRUE if the validationFailed() method has been called
        for the current request. Return of FALSE means that the create operation was successful and 
        we can reset items to null so that it will be recreated with the newly created user file.
         */
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    // We do not allow update of a user file.
    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("UserFileUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("UserFileDeleted"));

        /*
        JsfUtil.isValidationFailed() returns TRUE if the validationFailed() method has been called
        for the current request. Return of FALSE means that the destroy operation was successful and 
        we can reset items to null so that it will be recreated without the destroyed user file.
         */
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    /*
     ****************************************************************************
     *   Perform CREATE, EDIT (UPDATE), and DELETE Operations in the Database   *
     ****************************************************************************
     */
    /**
     * @param persistAction refers to CREATE, UPDATE (Edit) or DELETE action
     * @param successMessage displayed to inform the user about the result
     */
    private void persist(PersistAction persistAction, String successMessage) {

        if (selected != null) {
            try {
                if (persistAction != PersistAction.DELETE) {
                    /*
                     -------------------------------------------------
                     Perform CREATE or EDIT operation in the database.
                     -------------------------------------------------
                     The edit(selected) method performs the SAVE (STORE) operation of the "selected"
                     object in the database regardless of whether the object is a newly
                     created object (CREATE) or an edited (updated) object (EDIT or UPDATE).
                    
                     UserFileFacade inherits the edit(selected) method from the AbstractFacade class.
                     */
                    getUserFileFacade().edit(selected);
                } else {
                    /*
                     -----------------------------------------
                     Perform DELETE operation in the database.
                     -----------------------------------------
                     The remove method performs the DELETE operation in the database.
                    
                     UserFileFacade inherits the remove(selected) method from the AbstractFacade class.
                     */
                    getUserFileFacade().remove(selected);
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
                    JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccurred"));
                }
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccurred"));
            }
        }
    }

    /*
    ***************************************
    |   NetBeans Auto Generated Methods   |
    ***************************************
     */
    public UserFile getUserFile(java.lang.Integer id) {
        return getUserFileFacade().find(id);
    }

    public List<UserFile> getItemsAvailableSelectMany() {
        return getUserFileFacade().findAll();
    }

    public List<UserFile> getItemsAvailableSelectOne() {
        return getUserFileFacade().findAll();
    }

    @FacesConverter(forClass = UserFile.class)
    public static class UserFileControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            UserFileController controller = (UserFileController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "userFileController");
            return controller.getUserFile(getKey(value));
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
            if (object instanceof UserFile) {
                UserFile o = (UserFile) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), UserFile.class.getName()});
                return null;
            }
        }

    }

    /*
    =========================
    Delete Selected User File
    =========================
     */
    public String deleteSelectedUserFile() {

        UserFile userFileToDelete = selected;

        /*
        We need to preserve the messages since we will redirect to show a
        different JSF page after successful deletion of the user file.
         */
        Methods.preserveMessages();

        if (userFileToDelete == null) {
            Methods.showMessage("Fatal Error", "No File Selected!", "You do not have a file to delete!");
            return "";
        } else {
            try {
                // Delete the file from CloudStorage/FileStorage
                Files.deleteIfExists(Paths.get(userFileToDelete.getFilePath()));

                // Delete the user file record from the database
                getUserFileFacade().remove(userFileToDelete);
                // UserFileFacade inherits the remove() method from AbstractFacade

                Methods.showMessage("Information", "Success!", "Selected File is Successfully Deleted!");

                // See method below
                refreshFileList();

                return "/userFile/ListUserFiles?faces-redirect=true";

            } catch (IOException ex) {
                Methods.showMessage("Fatal Error", "Something went wrong while deleting the user file!",
                        "See: " + ex.getMessage());
                return "";
            }
        }
    }

    /*
    ========================
    Refresh User's File List
    ========================
     */
    public void refreshFileList() {
        /*
        By setting the items to null, we force the getItems
        method above to retrieve all of the user's files again.
         */
        selected = null; // Remove selection
        items = null;    // Invalidate list of items to trigger re-query.
    }

    /*
    ===============================
    Return Image File given File Id
    ===============================
     */
    /**
     *
     * @param fileId database primary key value for a user file
     * @return the file if it is an image file; otherwise return a blank image
     */
    public String imageFile(Integer fileId) {

        // Obtain the object reference of the UserFile whose primary key = fileId
        UserFile userFile = getUserFileFacade().getUserFile(fileId);

        // Obtain the userFile's filename as it is stored in the CloudDrive DB database
        String imageFileName = userFile.getFilename();

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

    /*
    =========================================
    Return Cleaned Filename for Selected File
    =========================================
     */
    // This method is called from UserFiles.xhtml by passing the fileId as a parameter.
    public String cleanedFileNameForSelected() {

        Integer fileId = selected.getId();
        /*
        cleanedFileNameHashMap<KEY, VALUE>
            KEY   = Integer fileId
            VALUE = String cleanedFileNameForSelected
         */

        // Obtain the cleaned filename for the given fileId
        String cleanedFileName = cleanedFileNameHashMap.get(fileId);

        return cleanedFileName;
    }

    /*
    ====================================
    Return Selected File's Relative Path
    ====================================
     */
    public String selectedFileRelativePath() {
        return Constants.FILES_RELATIVE_PATH + selected.getFilename();
    }

    /*
    =============================================
    Return True if Selected File is an Image File
    =============================================
     */
    public boolean isImage() {

        switch (extensionOfSelectedFileInCaps()) {
            case "JPG":
            case "JPEG":
            case "PNG":
            case "GIF":
                // Selected file is an acceptable image file
                return true;
            default:
                return false;
        }
    }

    /*
    ========================================
    Return True if Selected File is Viewable
    ========================================
     */
    public boolean isViewable() {

        switch (extensionOfSelectedFileInCaps()) {
            case "CSS":
            case "CSV":
            case "HTML":
            case "JAVA":
            case "PDF":
            case "SQL":
            case "TXT":
                // Selected file is viewable
                return true;
            default:
                return false;
        }
    }

    /*
    ============================================================
    The HTML5 <video> tag can play the following video files: 
    MP4, OGG, WEBM, or Apple's MOV which is compatible with MP4. 
    ============================================================
     */
    public boolean isVideo() {

        String fileExtension = extensionOfSelectedFileInCaps();

        return (fileExtension.equals("MP4") || fileExtension.equals("MOV") || fileExtension.equals("OGG") || fileExtension.equals("WEBM"));
    }

    /*
    ========================================================
    Return Extension of the Selected File in Capital Letters
    ========================================================
     */
    public String extensionOfSelectedFileInCaps() {

        // Obtain the selected filename
        String userFileName = selected.getFilename();

        // Extract the file extension from the filename
        String fileExtension = userFileName.substring(userFileName.lastIndexOf(".") + 1);

        // Convert file extension to be in capital letters
        String fileExtensionInCaps = fileExtension.toUpperCase();

        return fileExtensionInCaps;
    }

    public String getParkCode() {
        return parkCode;
    }

    public void setParkCode(String parkCode) {
        this.parkCode = parkCode;
    }
    
    public void parkChange(AjaxBehaviorEvent vce) {
        String code = (String) ((UIOutput) vce.getSource()).getValue();
        this.parkCode = code;
    }
    
    
}