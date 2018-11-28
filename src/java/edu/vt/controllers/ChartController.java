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

@Named("chartController")
@SessionScoped

public class ChartController implements Serializable {

   private MeterGaugeChartModel meterGaugeChart;
    private PublicBlogFacade facade;

    
    public MeterGaugeChartModel getMeterGauge(List<PublicBlog> list) {
        
        if (list.isEmpty()) {
            
        }

        facade = new PublicBlogFacade();
        List<Number> intervals = new ArrayList<Number>() {
            {
                add(1);
                add(2);
                add(3);
                add(4);
                add(5);
            }
        };
        
        
        int sum = 0;
        for (int y = 0; y < list.size(); y++) {
            String star = list.get(y).getRating();
            
        sum+=Integer.parseInt(star.substring(0,1));
        
        
        }
        double average  = ((double)sum)/list.size();
meterGaugeChart = new MeterGaugeChartModel(1, intervals);
     meterGaugeChart = new MeterGaugeChartModel(140, intervals);
meterGaugeChart.setTitle("Custom Options");
meterGaugeChart.setSeriesColors("66cc66,93b75f,E7E658,cc6666");
meterGaugeChart.setGaugeLabel("km/h");
meterGaugeChart.setGaugeLabelPosition("bottom");
 meterGaugeChart.setShowTickLabels(false);
meterGaugeChart.setLabelHeightAdjust(110);
meterGaugeChart.setIntervalOuterRadius(130);

       return meterGaugeChart;
    }


   

   
}
