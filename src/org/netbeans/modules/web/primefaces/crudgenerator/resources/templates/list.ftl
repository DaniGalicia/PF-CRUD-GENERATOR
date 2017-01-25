<#if comment>

  TEMPLATE DESCRIPTION:

  This is XHTML template for 'JSF Pages From Entity Beans' action. Templating
  is performed using FreeMaker (http://freemarker.org/) - see its documentation
  for full syntax. Variables available for templating are:

    bundle - name of the bundle variable set in faces-config.xml (type: String)
    entityName - name of entity being modified (type: String)
    managedBean - name of managed choosen in UI (type: String)
    managedBeanProperty - name of managed bean property choosen in UI (type: String)
    managedBeanLazyProperty - name of managed bean property for lazy loading choosen in UI (type: String)
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
    defaultDataTableRows - will be used for DataTable Paging
    defaultDataTableRowsPerPageTemplate - will be used for DataTable Paging
    primeFacesVersion - Version of the PrimeFaces library in use (type: Version)
    primeFacesVersion - Version of the PrimeFaces library in use (type: Version)
    searchLabels - Comma-seperated list of field name artifacts to search for labels (type: String)
                   Use in conjunction with getRelationsLabelName.
    doCreate - Provide CREATE functionality (type: boolean)
    doRead   - Provide READ functionality   (type: boolean)
    doUpdate - Provide UPDATE functionality (type: boolean)
    doDelete - Provide DELETE functionality (type: boolean)
    doSort   - Provide SORT functionality   (type: boolean)
    doFilter - Provide FILTER functionality (type: boolean)
    doContextMenus - Use context menus instead of regular buttons (type: boolean)
    doRelationshipNavigation - Navigate to / display child/parent data (type: boolean)
    hasRelationships - Whether this entity has any relationships to navigate to (type: boolean)
    hasParentRelationships - Whether this entity has parent relationships to navigate to (type: boolean)
    hasChildRelationships - Whether this entity has child relationships to navigate to (type: boolean)
    relationshipEntityDescriptors - List of child/parent entities (like entityDescriptors)
    growlMessages - Indicates whether to utilize Growl widget or traditional messages (type: Boolean)
    growlLife - Default display life time in ms for Growl widget (type: Integer)
    preferLazyLoading - Prefer Lazy Loading instead of traditional loading (type: boolean)

  This template is accessible via top level menu Tools->Templates and can
  be found in category PrimeFaces CRUD Generator->PrimeFaces Pages from Entity Classes.

</#if>
<@templateMacros?interpret/>
<#assign columnCounter = 0/>
<#assign createButton  = "createButton"/>
<#assign readButton    = "viewButton"/>
<#assign updateButton  = "editButton"/>
<#assign deleteButton  = "deleteButton"/>
<#if doContextMenus>
<#assign contextMenu   = entityName + "DataTableContextMenu"/>
</#if>
<#assign ajaxUpdateIds = "">
<#assign ajaxUpdateIdsExceptContextMenu = "">
<#if doContextMenus>
<#if doCreate><#assign ajaxUpdateIds = ajaxUpdateIds + " @form:" + createButton/></#if>
<#if doRead><#assign   ajaxUpdateIds = ajaxUpdateIds + " @form:" + readButton/></#if>
<#if doUpdate><#assign ajaxUpdateIds = ajaxUpdateIds + " @form:" + updateButton/></#if>
<#if doDelete><#assign ajaxUpdateIds = ajaxUpdateIds + " @form:" + deleteButton/></#if>
<#assign ajaxUpdateIdsExceptContextMenu = ajaxUpdateIds?trim/>
<#if doRead><#assign   ajaxUpdateIds = ajaxUpdateIds + " @form:" + contextMenu/></#if>
<#else>
<#if doCreate><#assign ajaxUpdateIds = ajaxUpdateIds + " " + createButton/></#if>
<#if doRead><#assign   ajaxUpdateIds = ajaxUpdateIds + " " + readButton/></#if>
<#if doUpdate><#assign ajaxUpdateIds = ajaxUpdateIds + " " + updateButton/></#if>
<#if doDelete><#assign ajaxUpdateIds = ajaxUpdateIds + " " + deleteButton/></#if>
</#if>
<#assign ajaxUpdateIds = ajaxUpdateIds?trim>
<#assign ajaxUpdateIds = ajaxUpdateIds?replace(" ",",")>
<#if growlMessages>
  <#assign messageUpdate = ":growl">
<#else>
  <#assign messageUpdate = ":" + entityName + "ListForm:messagePanel">
</#if>
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<ui:composition xmlns="http://www.w3.org/1999/xhtml"
                xmlns:ui="http://java.sun.com/jsf/facelets"
                xmlns:h="http://java.sun.com/jsf/html"
                xmlns:f="http://java.sun.com/jsf/core"
                xmlns:p="http://primefaces.org/ui">

        <h:form id="${entityName}ListForm">

            <p:panel header="${r"#{"}${bundle}.List${entityName}Title${r"}"}">
<#if doContextMenus>
                <p:contextMenu id="${contextMenu}" for="datalist">  
<#if doCreate>
                    <p:menuitem value="${r"#{"}${bundle}.Create${r"}"}" onclick="document.getElementById('${entityName}ListForm:${createButton}').click();" icon="ui-icon-plus"/>
</#if>
<#if doRead>
                    <p:menuitem value="${r"#{"}${bundle}.View${r"}"}" onclick="document.getElementById('${entityName}ListForm:${readButton}').click();" icon="ui-icon-search"/>
</#if>
<#if doUpdate>
                    <p:menuitem value="${r"#{"}${bundle}.Edit${r"}"}" onclick="document.getElementById('${entityName}ListForm:${updateButton}').click();" icon="ui-icon-pencil"/>
</#if>
<#if doDelete>
                    <p:menuitem value="${r"#{"}${bundle}.Delete${r"}"}" onclick="document.getElementById('${entityName}ListForm:${deleteButton}').click();" icon="ui-icon-trash"/>
</#if>
<#if doRelationshipNavigation && hasRelationships && doRead>
                    <p:separator/>
<#list relationshipEntityDescriptors as relationshipEntityDescriptor>
<#if relationshipEntityDescriptor.relationshipOne>
                    <p:menuitem value="${r"#{"}${bundle}.${entityName}MenuItem_${relationshipEntityDescriptor.id?replace(".","_")}${r"}"}" icon="ui-icon-search"  actionListener="${r"#{"}${managedBean}.prepare${relationshipEntityDescriptor.id?cap_first}${r"}"}" update=":${relationshipEntityDescriptor.relationClassName}ViewForm" oncomplete="<@pf>${relationshipEntityDescriptor.relationClassName}ViewDialog</@pf>.show()" disabled="${r"#{"}empty ${managedBean}.selected.${relationshipEntityDescriptor.id}${r"}"}"/>  
</#if>
<#if relationshipEntityDescriptor.relationshipMany>
                    <p:menuitem value="${r"#{"}${bundle}.${entityName}MenuItem_${relationshipEntityDescriptor.id?replace(".","_")}${r"}"}" icon="ui-icon-search"  action="${r"#{"}${managedBean}.navigate${relationshipEntityDescriptor.id?cap_first}${r"}"}" disabled="${r"#{"}empty ${managedBean}.selected.${relationshipEntityDescriptor.id}${r"}"}" ajax="false"/>  
</#if>
</#list>
</#if>
                </p:contextMenu>
</#if>  
<#if !growlMessages>
                <h:panelGroup id="messagePanel">
                    <p:messages id="listMessages" rendered="${r"#{"}!${managedBean}.validationFailed${r"}"}"/>
                </h:panelGroup>

</#if>
                <p:dataTable id="datalist"
<#if preferLazyLoading>
                             value="${r"#{"}${managedBeanLazyProperty}${r"}"}"
                             lazy="true"
<#else>
                             value="${r"#{"}${managedBeanProperty}${r"}"}"
                             lazy="false"
<#if entityIdField?? && entityIdField != "">
                             rowKey="${r"#{"}${item}.${entityIdField}${r"}"}"
</#if>
</#if>
                             var="${item}"
<#if defaultDataTableRows?? && defaultDataTableRows != "">
                             paginator="true"
                             rows="${defaultDataTableRows}"
                             rowsPerPageTemplate="${defaultDataTableRowsPerPageTemplate}"
</#if>
                             selectionMode="single"
                             selection="${r"#{"}${managedBean}${r".selected}"}">
<#if preferLazyLoading>
<!--
         To disable Lazy Loading, please copy/replace the following lines above,
         and be sure to replace the word HASH with the # symbol:

         value="${r"HASH{"}${managedBeanProperty}${r"}"}"
         lazy="false"
<#if entityIdField?? && entityIdField != "">
         rowKey="${r"HASH{"}${item}.${entityIdField}${r"}"}"
</#if>
-->
<#else>
<!--
         To enable Lazy Loading, please copy/replace the following lines above,
         and be sure to replace the word HASH with the # symbol:

         value="${r"HASH{"}${managedBeanLazyProperty}${r"}"}"
         lazy="true"
<#if entityIdField?? && entityIdField != "">
         rowKey="${r"HASH{"}${item}.${entityIdField}${r"}"}"  <<< Remove this line
</#if>
-->
</#if>

<#if ajaxUpdateIds?? && ajaxUpdateIds != "">

                    <p:ajax event="rowSelect"   update="${ajaxUpdateIds}"<#if doRelationshipNavigation && hasParentRelationships && doRead> listener="${r"#{"}${managedBean}.resetParents${r"}"}"</#if>/>
                    <p:ajax event="rowUnselect" update="${ajaxUpdateIds}"<#if doRelationshipNavigation && hasParentRelationships && doRead> listener="${r"#{"}${managedBean}.resetParents${r"}"}"</#if>/>
<#if doContextMenus>
                    <p:ajax event="contextMenu" update="${ajaxUpdateIdsExceptContextMenu}"<#if doRelationshipNavigation && hasParentRelationships && doRead> listener="${r"#{"}${managedBean}.resetParents${r"}"}"</#if>/>
</#if>
<#if doContextMenus && doRead>
                    <p:ajax event="rowDblselect" onsuccess="document.getElementById('${entityName}ListForm:${readButton}').click();"/>
</#if>
</#if>
<#list entityDescriptors as entityDescriptor>
  <#-- Skip this field if we are dealing with many:many -->
  <#if !entityDescriptor.relationshipMany && !entityDescriptor.versionField>
    <#if entityDescriptor.relationshipOne || entityDescriptor.relationshipMany>
        <#if entityDescriptor.getRelationsLabelName(searchLabels)??>
          <#assign relationLabelName = entityDescriptor.getRelationsLabelName(searchLabels)>
        <#else>
              <#assign relationLabelName = "">
        </#if>
    </#if>
<#assign columnCounter = columnCounter + 1/>
<#if (maxTableCols != 0 && columnCounter > maxTableCols)><!--</#if>
<#if entityDescriptor.relationshipOne>
                <#if relationLabelName?? && relationLabelName != "">
                    <p:column<#if doSort> sortBy="${r"#{"}${entityDescriptor.name}.${relationLabelName}${r"}"}"</#if><#if doFilter> filterBy="${r"#{"}${entityDescriptor.name}.${relationLabelName}${r"}"}"</#if>>
                <#else>
                    <#-- Disable sorting if we don't have a foreign field to sort by. -->
                    <p:column>
                </#if>
<#else>
                    <p:column<#if doSort> sortBy="${r"#{"}${entityDescriptor.name}${r"}"}"</#if><#if doFilter> filterBy="${r"#{"}${entityDescriptor.name}${r"}"}"</#if>>
</#if>
                        <f:facet name="header">
                            <h:outputText value="${r"#{"}${bundle}.List${entityName}Title_${entityDescriptor.id?replace(".","_")}${r"}"}"/>
                        </f:facet>
    <#if entityDescriptor.dateTimeFormat?? && entityDescriptor.dateTimeFormat != "">
                        <h:outputText value="${r"#{"}${entityDescriptor.name}${r"}"}">
                            <f:convertDateTime pattern="${entityDescriptor.dateTimeFormat}" />
                        </h:outputText>
    <#elseif entityDescriptor.returnType?contains("boolean") || entityDescriptor.returnType?contains("Boolean")>
                        <h:selectBooleanCheckbox id="${entityDescriptor.id?replace(".","_")}" value="${r"#{"}${entityDescriptor.name}${r"}"}" title="${r"#{"}${bundle}.Edit${entityName}Title_${entityDescriptor.id?replace(".","_")}${r"}"}" <#if entityDescriptor.required>required="true" requiredMessage="${r"#{"}${bundle}.Edit${entityName}RequiredMessage_${entityDescriptor.id?replace(".","_")}${r"}"}"</#if> disabled="true"/>
    <#elseif entityDescriptor.relationshipOne>
            <#if relationLabelName?? && relationLabelName != "">
                        <h:outputText value="${r"#{"}${entityDescriptor.name}.${relationLabelName}${r"}"}"/>
            <#else>
                        <h:outputText value="${r"#{"}${entityDescriptor.name}${r"}"}">
<#if (jsfVersion.compareTo("2.2") < 0) && cdiEnabled?? && cdiEnabled>
                            <f:converter binding="${r"#{"}${entityDescriptor.valuesConverter}${r"}"}"/>
<#else>
                            <f:converter converterId="${entityDescriptor.valuesConverter}"/>
</#if>
                        </h:outputText>
            </#if>
    <#else>
                        <h:outputText value="${r"#{"}${entityDescriptor.name}${r"}"}"/>
    </#if>
                    </p:column>
<#if (maxTableCols != 0 && columnCounter > maxTableCols)>--></#if>
  </#if>
</#list>
<#if !doContextMenus>
                    <f:facet name="footer">
                      <div class="footer-section">
<#else>

                </p:dataTable>

</#if>
<#if doCreate>
                        <p:commandButton id="${createButton}"<#if doContextMenus>                            </#if> icon="ui-icon-plus"   value="${r"#{"}${bundle}.Create${r"}"}" actionListener="${r"#{"}${managedBean}.${r"prepareCreate}"}" update=":${entityName}CreateForm" oncomplete="<@pf>${entityName}CreateDialog</@pf>.show()"/>
</#if>
<#if doRead>
                        <p:commandButton id="${readButton}"  <#if doContextMenus> style="visibility: hidden;"</#if> icon="ui-icon-search" value="${r"#{"}${bundle}.View${r"}"}" update=":${entityName}ViewForm" oncomplete="<@pf>${entityName}ViewDialog</@pf>.show()" disabled="${r"#{empty "}${managedBean}${r".selected}"}"/>
</#if>
<#if doUpdate>
                        <p:commandButton id="${updateButton}"  <#if doContextMenus> style="visibility: hidden;"</#if> icon="ui-icon-pencil" value="${r"#{"}${bundle}.Edit${r"}"}" update=":${entityName}EditForm" oncomplete="<@pf>${entityName}EditDialog</@pf>.show()" disabled="${r"#{empty "}${managedBean}${r".selected}"}"/>
</#if>
<#if doDelete>
    <#if (primeFacesVersion.compareTo("4.0") >= 0 && doConfirmationDialogs) >
                        <p:commandButton id="${deleteButton}"<#if doContextMenus> style="visibility: hidden;"</#if> icon="ui-icon-trash"  value="${r"#{"}${bundle}.Delete${r"}"}" actionListener="${r"#{"}${managedBean}${r".delete}"}" update="${messageUpdate},datalist" disabled="${r"#{empty "}${managedBean}${r".selected}"}">
                            <p:confirm header="${r"#{"}${bundle}.ConfirmationHeader${r"}"}" message="${r"#{"}${bundle}.ConfirmDeleteMessage${r"}"}" icon="ui-icon-alert"/>
                        </p:commandButton>
    <#else>
                        <p:commandButton id="${deleteButton}"<#if doContextMenus> style="visibility: hidden;"</#if> icon="ui-icon-trash"  value="${r"#{"}${bundle}.Delete${r"}"}" actionListener="${r"#{"}${managedBean}${r".delete}"}" update="${messageUpdate},datalist" disabled="${r"#{empty "}${managedBean}${r".selected}"}"/>
    </#if>
</#if>
<#if !doContextMenus>
                      </div>
                    </f:facet>

                </p:dataTable>
</#if>

            </p:panel>
<#if (primeFacesVersion.compareTo("4.0") >= 0 && doConfirmationDialogs) >
            <ui:include src="${confirmDialogPage}"/>
</#if>
        </h:form>

</ui:composition>