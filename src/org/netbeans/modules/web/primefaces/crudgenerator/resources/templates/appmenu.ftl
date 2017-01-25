<#if comment>

  TEMPLATE DESCRIPTION:

  This is XHTML template for 'JSF Pages From Entity Beans' action. Templating
  is performed using FreeMaker (http://freemarker.org/) - see its documentation
  for full syntax. Variables available for templating are:

    bundle - name of the bundle variable set in faces-config.xml (type: String)
    entityName - name of entity being modified (type: String)
    managedBean - name of managed choosen in UI (type: String)
    managedBeanProperty - name of managed bean property choosen in UI (type: String)
    item - name of property used for dataTable iteration (type: String)
    comment - always set to "false" (type: Boolean)
    entityDescriptors - list of beans describing individual entities. Bean has following properties:
        label - field label (type: String)
        name - field property name (type: String)
        dateTimeFormat - date/time/datetime formatting (type: String)
        blob - does field represents a large block of text? (type: boolean)
        primaryKey - is field a primary key field? (type: boolean)
        relationshipOne - does field represent one to one or many to one relationship (type: boolean)
        relationshipMany - does field represent one to many relationship (type: boolean)
        id - field id name (type: String)
        required - is field optional and nullable or it is not? (type: boolean)
        valuesGetter - if item is of type 1:1 or 1:many relationship then use this
            getter to populate <h:selectOneMenu> or <h:selectManyMenu>
    primeFacesVersion - Version of the PrimeFaces library in use (type: Version)
    servletMapping - Prefix mapping of the JSF servlet inside web.xml (type: String)
    jsfFolder - URL portion that holds the jsf pages, if any (type: String)

  This template is accessible via top level menu Tools->Templates and can
  be found in category PrimeFaces CRUD Generator->PrimeFaces Pages from Entity Classes.

</#if>
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"
      xmlns:ui="http://java.sun.com/jsf/facelets"
      xmlns:h="http://java.sun.com/jsf/html"
      xmlns:f="http://java.sun.com/jsf/core"
      xmlns:p="http://primefaces.org/ui">

    <ui:composition>
        <h:form id="menuForm">
            <p:menubar>
    <#if (primeFacesVersion.compareTo("3.4") >= 0)>
                <p:menuitem value="${r"#{"}${bundle}.Home${r"}"}" outcome="${r"/"}${appIndex}" icon="ui-icon-home"/>
    <#else>
                <p:menuitem value="${r"#{"}${bundle}.Home${r"}"}" url="${servletMapping}${r"/"}${appIndex}.xhtml" icon="ui-icon-home"/>
    </#if>
                <p:submenu label="${r"#{"}${bundle}.Maintenance${r"}"}">
<#list entities as entity>
    <#if (primeFacesVersion.compareTo("3.4") >= 0)>
                    <p:menuitem value="${r"#{"}${bundle}.${entity.entityClassName}Heading${r"}"}..." outcome="${jsfFolder}${r"/"}${entity.entityClassName?uncap_first}${r"/index"}" />
    <#else>
                    <p:menuitem value="${r"#{"}${bundle}.${entity.entityClassName}Heading${r"}"}..." url="${servletMapping}${jsfFolder}${r"/"}${entity.entityClassName?uncap_first}${r"/index.xhtml"}" />
    </#if>
</#list>
                </p:submenu>
    <#if (doMobile && primeFacesVersion.compareTo("5.0") >= 0)>
                <p:menuitem value="${r"#{"}${bundle}.Mobile${r"}"}" outcome="${jsfMobileFolder}${r"/"}${appIndex}" icon="ui-icon-signal"/>
    </#if>
            </p:menubar>
        </h:form>
    </ui:composition>

</html>