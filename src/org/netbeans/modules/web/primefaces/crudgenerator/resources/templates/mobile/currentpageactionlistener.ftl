/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ${packageName};

import java.io.Serializable;
<#if cdiEnabled?? && cdiEnabled>
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.inject.Inject;
</#if>
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;
<#if !(cdiEnabled?? && cdiEnabled)>
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
</#if>

<#if cdiEnabled?? && cdiEnabled>
@Named(value = "currentPageActionListener")
<#else>
@ManagedBean(name = "currentPageActionListener")
</#if>
@RequestScoped
public class CurrentPageActionListener implements Serializable, ActionListener {

<#if cdiEnabled?? && cdiEnabled>
    @Inject
<#else>
    @ManagedProperty(value = "${r"#{"}mobilePageController${r"}"}")
</#if>
    private MobilePageController mobilePageController;
<#if !(cdiEnabled?? && cdiEnabled)>

    /* Setter method for managed property mobilePageController */
    public void setMobilePageController(MobilePageController mobilePageController) {
        this.mobilePageController = mobilePageController;
    }
</#if>
    /**
     * Creates a new instance of CurrentPageActionListener
     */
    public CurrentPageActionListener() {
    }

    @Override
    public void processAction(ActionEvent event) throws AbortProcessingException {
        this.mobilePageController.currentPageListener(event);
    }

}