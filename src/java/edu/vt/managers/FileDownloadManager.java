/*
 * Created by Osman Balci on 2018.06.16
 * Copyright © 2018 Osman Balci. All rights reserved.
 */
package edu.vt.managers;

// UserFile class's instance methods are accessed
import edu.vt.EntityBeans.UserFile;

// These two are needed for CDI @Inject injection
import edu.vt.controllers.UserFileController;
import javax.inject.Inject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Serializable;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.ServletContext;

// These two are needed for PrimeFaces file download
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

@Named(value = "fileDownloadManager")
@RequestScoped

public class FileDownloadManager implements Serializable {
    /*
    ===============================
    Instance Variables (Properties)
    ===============================
     */

    /*
    Using the @Inject annotation, the compiler is directed to store the object reference of the
    UserFileController CDI-managed bean into the instance variable userFileController at runtime.
    With this injection, the instance variables and instance methods of the UserFileController
    class can be accessed in this CDI-managed bean. The following imports are required for the injection:
    
        import com.mycompany.jsfclasses.UserFileController;
        import javax.inject.Inject;
     */
    @Inject
    private UserFileController userFileController;

    /*
    StreamedContent and DefaultStreamedContent classes are imported from
    org.primefaces.model.StreamedContent above.
     */
    private StreamedContent file;

    /*
    ==================
    Constructor Method
    ==================
     */
    public FileDownloadManager() {
    }

    /*
    =============
    Getter Method
    =============
     */
    public StreamedContent getFile() throws FileNotFoundException {

        UserFile fileToDownload = userFileController.getSelected();

        String nameOfFileToDownload = fileToDownload.getFilename();
        String absolutePathOfFileToDownload = fileToDownload.getFilePath();
        String contentMimeType = FacesContext.getCurrentInstance().getExternalContext().getMimeType(absolutePathOfFileToDownload);

        /*
        System.out.print prints under the GlassFish tab of the Output window.
        Print out intermediate values to effectively debug your application logic.
        
        System.out.print("*** Name of the file to download = " + nameOfFileToDownload + " ***\n");
        System.out.print("*** Path of the file to download = " + absolutePathOfFileToDownload + " ***\n");
        System.out.print("*** MIME Type of the file to download = " + contentMimeType + " ***");
         */
        InputStream stream = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().
                getContext()).getResourceAsStream(absolutePathOfFileToDownload);

        // Obtain the filename without the prefix "userId_" inserted to associate the file to a user
        String downloadedFileName = nameOfFileToDownload.substring(nameOfFileToDownload.indexOf("_") + 1);

        // FileInputStream must be used here since our files are stored in a directory external to our application
        file = new DefaultStreamedContent(new FileInputStream(absolutePathOfFileToDownload), contentMimeType, downloadedFileName);

        return file;
    }

}
