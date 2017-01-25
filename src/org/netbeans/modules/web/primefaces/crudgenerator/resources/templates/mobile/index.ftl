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
    growlMessages - Indicates whether to utilize Growl widget or traditional messages (type: Boolean)
    growlLife - Default display life time in ms for Growl widget (type: Integer)
    uniqueRelationshipEntityDescriptor - list of unique parent/child entities. Bean has same properties as entityDescriptors.

  This template is accessible via top level menu Tools->Templates and can
  be found in category PrimeFaces CRUD Generator->PrimeFaces Pages from Entity Classes.

</#if>
<#setting number_format="0">
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<ui:composition xmlns:ui="http://java.sun.com/jsf/facelets"
      xmlns:h="http://java.sun.com/jsf/html"
      xmlns:f="http://java.sun.com/jsf/core"
      xmlns:p="http://primefaces.org/ui"
      xmlns:pm="http://primefaces.org/mobile"
      template="${mobileTemplatePage}">

        <ui:define name="title">
            <h:outputText value="${r"#{"}${bundle}.${entityName}Title${r"}"}"/>
        </ui:define>

        <ui:define name="body">

<#if (doConfirmationDialogs && primeFacesVersion.compareTo("5.1.13") >= 0) >
            <ui:include src="${mobileConfirmDialogPage}"/>
</#if>
            <ui:include src="${mobileEntityIncludeFolder}/${entityName?uncap_first}/List.xhtml"/>
<#if doUpdate>
            <ui:include src="${mobileEntityIncludeFolder}/${entityName?uncap_first}/Edit.xhtml"/>
</#if>
<#if doCreate>
            <ui:include src="${mobileEntityIncludeFolder}/${entityName?uncap_first}/Create.xhtml"/>
</#if>
<#if (doRelationshipNavigation && hasParentRelationships && doRead)>
<#list uniqueRelationshipEntityDescriptors as uniqueRelationshipEntityDescriptor>
<#if uniqueRelationshipEntityDescriptor.relationshipOne>
            <ui:include src="${mobileEntityIncludeFolder}/${uniqueRelationshipEntityDescriptor.relationClassName?uncap_first}/Edit.xhtml"/>
</#if>
</#list>
</#if>

        </ui:define>

</ui:composition>