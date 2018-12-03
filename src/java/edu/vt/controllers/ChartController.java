/*
 * Created by Osman Balci on 2018.06.20
 * Copyright © 2018 Osman Balci. All rights reserved. 
 */
package edu.vt.controllers;

import edu.vt.EntityBeans.PublicBlog;
import edu.vt.EntityBeans.User;
import edu.vt.FacadeBeans.PublicBlogFacade;
import edu.vt.FacadeBeans.UserFacade;
import edu.vt.controllers.util.JsfUtil;
import edu.vt.controllers.util.JsfUtil.PersistAction;
import edu.vt.globals.Methods;

import java.io.Serializable;
import java.util.ArrayList;
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
import javax.swing.text.Document;
import javax.swing.text.Element;
import static org.primefaces.component.button.Button.PropertyKeys.fragment;

import org.primefaces.json.JSONArray;
import org.primefaces.json.JSONObject;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.MeterGaugeChartModel;
import org.primefaces.model.chart.PieChartModel;

@Named("chartController")
@SessionScoped

public class ChartController implements Serializable {

   private PieChartModel pieChart;
    private PublicBlogFacade facade;

    
    public Integer getRate(List<PublicBlog> list) {
        
        if (list.isEmpty()) {
            return 0;
        }

        int total = 0;
       
 
        
 
        
        
        for (int y = 0; y < list.size(); y++) {
            String star = list.get(y).getRating();
            
        int rate =Integer.parseInt(star.substring(0,1));
        total+=rate;
        
        
        
        }
       
       return Integer.parseInt(""+Math.round(((double)total)/((double)list.size())));
    }


   

   
}
