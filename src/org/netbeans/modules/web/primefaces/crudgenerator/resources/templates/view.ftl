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
        maxSize - returns the size of a field if annotated with @Size (type: Integer)
        primaryKey - is field a primary key field? (type: boolean)
        readOnly - is the field a read-only field? (type: boolean)
        relationshipOne - does field represent one to one or many to one relationship (type: boolean)
        relationshipMany - does field represent one to many relationship (type: boolean)
        relationshipOwner - does the field represent the owning side of a many:many relationship? (type: boolean)
        getRelationsLabelName(String) - field name of the foreign entity field matching String (type: String)
        returnType - fully qualified data type of the field
        id - field id name (type: String)
        required - is field optional and nullable or it is not? (type: boolean)
        valuesListGetter - if item is of type 1:many or many:many relationship then use this
            getter to populate <h:selectOneMenu> or <h:selectManyMenu>
        valuesConverter - if item is of type 1:many or many:many relationship then use this
            for the converter binding of <h:selectOneMenu> or <h:selectManyMenu>
        versionField - is the field a Version field (type: boolean)
    primeFacesVersion - Version of the PrimeFaces library in use (type: Version)
    servletMapping - Prefix mapping of the JSF servlet inside web.xml (type: String)
    searchLabels - Comma-seperated list of field name artifacts to search for labels (type: String)
                   Use in conjunction with getRelationsLabelName.
    growlMessages - Indicates whether to utilize Growl widget or traditional messages (type: Boolean)
    growlLife - Default display life time in ms for Growl widget (type: Integer)
    tooltipMessages - Indicates whether messages are presented as tooltips (entity pages only) (type: Boolean)
    textThreshold - the maximum length of a text field before switching to a text area (type: Integer)

  This template is accessible via top level menu Tools->Templates and can
  be found in category PrimeFaces CRUD Generator->PrimeFaces Pages from Entity Classes.

</#if>
<@templateMacros?interpret/>
<#assign crud = "View">
<#assign textThreshold = 255>
<#assign maxFields = 20>
<#assign fieldCount = 0>
<#assign firstField = true>
<#assign tabCount = 0>
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"
      xmlns:ui="http://java.sun.com/jsf/facelets"
      xmlns:h="http://java.sun.com/jsf/html"
      xmlns:f="http://java.sun.com/jsf/core"
      xmlns:p="http://primefaces.org/ui">

    <ui:composition>

        <p:dialog id="${entityName}ViewDlg" widgetVar="${entityName}ViewDialog" modal="true" resizable="false"  <#if (primeFacesVersion.compareTo("4.0") >= 0)>appendTo="@(body)"<#else>appendToBody="true"</#if> header="${r"#{"}${bundle}.View${entityName}Title${r"}"}"<#if (primeFacesVersion.compareTo("3.5") >= 0)> closeOnEscape="true"</#if>>

            <h:form id="${entityName}ViewForm">

                <h:panelGroup id="display">
<#if (entityDescriptors?size > maxFields)>
                    <p:tabView id="${entityName}TabView">
<#else>
                    <p:panelGrid columns="2" columnClasses="column" rendered="${r"#{"}${managedBeanProperty} != null${r"}"}">
</#if>
    <#list entityDescriptors as entityDescriptor>
     <#-- Skip this field if we are dealing with many:many and this entity is not the owner -->
     <#if !entityDescriptor.versionField &&
          !(entityDescriptor.relationshipMany && 
           !entityDescriptor.relationshipOwner)> 

        <#if entityDescriptor.relationshipOne || entityDescriptor.relationshipMany>
            <#if entityDescriptor.getRelationsLabelName(searchLabels)??>
              <#assign relationLabelName = entityDescriptor.getRelationsLabelName(searchLabels)>
            <#else>
              <#assign relationLabelName = "">
            </#if>
        </#if>
        <#assign fieldCount = fieldCount + 1>
        <#if (entityDescriptors?size > maxFields) && (fieldCount > maxFields || firstField)>
        <#assign tabCount = tabCount + 1>
        <#if (tabCount > 1)>
                            </p:panelGrid>
                        </p:tab>
        </#if>
                        <p:tab id="${entityName}Tab${tabCount}" title="${r"#{"}${bundle}.TabHeaderPrefix${r"}"} ${tabCount}">
                            <p:panelGrid columns="2" columnClasses="column" rendered="${r"#{"}${managedBeanProperty} != null${r"}"}">
        <#if (fieldCount > maxFields)>
        <#assign fieldCount = 0>
        </#if>
        </#if>
        <@viewOneFieldTemplate?interpret/>
     <#assign firstField = false>
     </#if>
    </#list>
<#if (entityDescriptors?size > maxFields)>
                            </p:panelGrid>
                        </p:tab>
                    </p:tabView>
<#else>
                    </p:panelGrid>
</#if>
                  <p:commandButton value="${r"#{"}${bundle}.Close${r"}"}" onclick="<@pf>${entityName}ViewDialog</@pf>.hide()"/>
                </h:panelGroup>

            </h:form>

        </p:dialog>

    </ui:composition>

</html>
