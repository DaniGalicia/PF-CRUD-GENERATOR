<#if comment>

  TEMPLATE DESCRIPTION:

  This is XHTML mobile application template for 'PrimeFaces Pages from Entity Classes'.
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
<#setting number_format="0">
<?xml version='1.0' encoding='UTF-8' ?> 
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"
      xmlns:ui="http://java.sun.com/jsf/facelets"
      xmlns:h="http://java.sun.com/jsf/html"
      xmlns:f="http://java.sun.com/jsf/core"
      xmlns:p="http://primefaces.org/ui"
      xmlns:pm="http://primefaces.org/mobile">
    
    <f:view renderKitId="PRIMEFACES_MOBILE">
        <ui:insert name="metadata"/>
    </f:view>

    <h:head>
        <title><ui:insert name="title">Default Title</ui:insert></title>
        <h:outputStylesheet library="css" name="${styleFile}"/>
        <h:outputScript library="scripts" name="${scriptFile}"/>
        <h:outputScript target="head">
            jQuery(document).ready(function() {
                jQuery(document).ready(function() {
                    fixPFMDialogs();
                });
            });
        </h:outputScript>
    </h:head>

    <h:body>

<#if growlMessages>
        <p:growl id="growl"<#if (growlLife > 0)> life="${growlLife}"</#if><#if tooltipMessages> globalOnly="true"</#if>/>

</#if>
        <ui:insert name="body"/>
        <ui:insert name="footer"/>

    </h:body>
    
</html>
