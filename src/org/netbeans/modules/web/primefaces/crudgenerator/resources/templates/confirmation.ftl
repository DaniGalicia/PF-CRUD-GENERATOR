<#if comment>

  TEMPLATE DESCRIPTION:

  This is XHTML main application template for 'PrimeFaces Pages from Entity Classes'.
  Templating is performed using FreeMaker (http://freemarker.org/) - see its documentation
  for full syntax. Variables available for templating are:

    bundle - name of the bundle variable set in faces-config.xml (type: String)
    comment - always set to "false" (type: Boolean)
    primeFacesVersion - Version of the PrimeFaces library in use (type: Version)
    servletMapping - Prefix mapping of the JSF servlet inside web.xml (type: String)
    growlMessages - Indicates whether to utilize Growl widget or traditional messages (type: Boolean)
    growlLife - Default display life time in ms for Growl widget (type: Integer)
    tooltipMessages - Indicates whether messages are presented as tooltips (entity pages only) (type: Boolean)

  This template is accessible via top level menu Tools->Templates and can
  be found in category PrimeFaces CRUD Generator->PrimeFaces Pages from Entity Classes.

</#if>
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<ui:composition xmlns="http://www.w3.org/1999/xhtml"
                xmlns:ui="http://java.sun.com/jsf/facelets"
                xmlns:p="http://primefaces.org/ui">

    <p:confirmDialog global="true">  
        <p:commandButton value="${r"#{"}${bundle}.Yes${r"}"}" type="button" styleClass="ui-confirmdialog-yes" icon="ui-icon-check"/>  
        <p:commandButton value="${r"#{"}${bundle}.No${r"}"}" type="button" styleClass="ui-confirmdialog-no" icon="ui-icon-close"/>       
    </p:confirmDialog>
    
</ui:composition>