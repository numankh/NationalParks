/*
 * Created by Osman Balci on 2018.08.03
 * Copyright © 2018 Osman Balci. All rights reserved.
 */
package edu.vt.controllers;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

@Named(value = "sliderController")
@RequestScoped

public class SliderController {

    // Each String object in the List contains the image filename, e.g., photo1.png
    private List<String> sliderImages;

    /*
    The PostConstruct annotation is used on a method that needs to be executed after
    dependency injection is done to perform any initialization. The initialization 
    method init() is the first method invoked before this class is put into service. 
    */
    @PostConstruct
    public void init() {

        sliderImages = new ArrayList<>();

        for (int i = 1; i <= 12; i++) {
            sliderImages.add("photo" + i + ".png");
        }
    }

    /*
    =============
    Getter Method
    =============
     */
    public List<String> getSliderImages() {
        return sliderImages;
    }
    
    /*
    ===============
    Instance Method
    ===============
     */
    public String description(String image) {

        String imageDescription = "";

        switch (image) {
            case "photo1.png":
                imageDescription = "Popular cloud storage and synchronization services";
                break;
            case "photo2.png":
                imageDescription = "Google Drive is a file storage and synchronization service";
                break;
            case "photo3.png":
                imageDescription = "iCloud is Apple's cloud storage and cloud computing service";
                break;
            case "photo4.png":
                imageDescription = "OneDrive is Microsoft's file hosting service";
                break;
            case "photo5.png":
                imageDescription = "Amazon Drive is Amazon's cloud storage service";
                break;
            case "photo6.png":
                imageDescription = "Dropbox is a file hosting service operated by Dropbox, Inc.";
                break;
            case "photo7.png":
                imageDescription = "IBM provides flexible and scalable cloud storage";
                break;
            case "photo8.png":
                imageDescription = "Oracle provides file storage services for enterprise applications";
                break;
            case "photo9.png":
                imageDescription = "Box Drive is a file hosting service provided by Box, Inc.";
                break;
            case "photo10.png":
                imageDescription = "Cloud storage plays a critical role in cloud computing";
                break;
            case "photo11.png":
                imageDescription = "Security is a crucial quality requirement for cloud storage";
                break;
            case "photo12.png":
                imageDescription = "Amazon Web Services is a cloud services platform";
                break;
        }

        return imageDescription;
    }
}
