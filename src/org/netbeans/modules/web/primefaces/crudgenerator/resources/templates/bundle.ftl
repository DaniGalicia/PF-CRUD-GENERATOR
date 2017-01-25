<#if comment>

  TEMPLATE DESCRIPTION:

  This is Bundle.properties template for 'JSF Pages From Entity Beans' action. Templating
  is performed using FreeMaker (http://freemarker.org/) - see its documentation
  for full syntax. Variables available for templating are:

    entities - list of beans with following properites:
        entityClassName - controller class name (type: String)
        entityDescriptors - list of beans describing individual entities. Bean has following properties:
            label - part of bundle key name for label (type: String)
            title - part of bundle key name for title (type: String)
            name - field property name (type: String)
            dateTimeFormat - date/time/datetime formatting (type: String)
            blob - does field represents a large block of text? (type: boolean)
            relationshipOne - does field represent one to one or many to one relationship (type: boolean)
            relationshipMany - does field represent one to many relationship (type: boolean)
            id - field id name (type: String)
            required - is field optional and nullable or it is not? (type: boolean)
            valuesGetter - if item is of type 1:1 or 1:many relationship then use this
                getter to populate <h:selectOneMenu> or <h:selectManyMenu>

  This template is accessible via top level menu Tools->Templates and can
  be found in category JavaServer Faces->JSF from Entity.

</#if>
PersistenceErrorOccured=A persistence error occurred.
Previous=Previous
Next=Next

AppName=Application Name
Back=Back
Cancel=Cancel
Create=Create
Delete=Delete
Edit=Edit
Close=Close
Save=Save
View=View
Yes=Yes
No=No
Maintenance=Maintenance
Mobile=Mobile
Menu=Menu
FullApp=Desktop Version
Home=Home
Welcome=Welcome
WelcomeMessage=Welcome to your PrimeFaces Application. Please explore by making a selection from the menu.
SelectOneMessage=Select One...
ConfirmationHeader=Confirmation
ConfirmDeleteMessage=Are you sure you want to proceed?
ConfirmEditMessage=Do you want to apply the changes?
ConfirmCreateMessage=Ready to create?
TabHeaderPrefix=Detail Page

<#list entities as entity>
<#list entity.entityDescriptors as entityDescriptor>
<#if entityDescriptor.relationshipOne || entityDescriptor.relationshipMany>
<#assign fieldName = entityDescriptor.label/>
<#assign fieldNameLength = fieldName?length/>
<#if fieldName?ends_with("Collection")><#assign fieldName = fieldName?substring(0,fieldNameLength - 10)/></#if>
<#if fieldName?ends_with("List")><#assign fieldName = fieldName?substring(0,fieldNameLength - 4)/></#if>
<#if fieldName?ends_with("Set")><#assign fieldName = fieldName?substring(0,fieldNameLength - 3)/></#if>
<#assign fieldName = fieldName?trim/>
<#if entityDescriptor.relationshipOne>
${entity.entityClassName}MenuItem_${entityDescriptor.id?replace(".","_")}=View ${fieldName}
<#else>
${entity.entityClassName}MenuItem_${entityDescriptor.id?replace(".","_")}=View ${fieldName}s...
</#if>
</#if>
</#list>
${entity.entityClassName}Heading=${entity.entityNaturalName}
${entity.entityClassName}Title=${entity.entityNaturalName} Maintenance
${entity.entityClassName}Created=${entity.entityNaturalName} was successfully created.
${entity.entityClassName}Updated=${entity.entityNaturalName} was successfully updated.
${entity.entityClassName}Deleted=${entity.entityNaturalName} was successfully deleted.
Create${entity.entityClassName}Title=Create New ${entity.entityNaturalName}
Create${entity.entityClassName}SaveLink=Save
Create${entity.entityClassName}ShowAllLink=Show All ${entity.entityNaturalName} Items
Create${entity.entityClassName}IndexLink=Index
    <#list entity.entityDescriptors as entityDescriptor>
Create${entity.entityClassName}Label_${entityDescriptor.id?replace(".","_")}=${entityDescriptor.label}:
<#if entityDescriptor.required>Create${entity.entityClassName}RequiredMessage_${entityDescriptor.id?replace(".","_")}=The ${entityDescriptor.label} field is required.
</#if>Create${entity.entityClassName}Title_${entityDescriptor.id?replace(".","_")}=${entityDescriptor.label}
Create${entity.entityClassName}HelpText_${entityDescriptor.id?replace(".","_")}=Please provide information for ${entityDescriptor.label}
    </#list>
Edit${entity.entityClassName}Title=Edit ${entity.entityNaturalName}
Edit${entity.entityClassName}SaveLink=Save
Edit${entity.entityClassName}ViewLink=View
Edit${entity.entityClassName}ShowAllLink=Show All ${entity.entityNaturalName} Items
Edit${entity.entityClassName}IndexLink=Index
    <#list entity.entityDescriptors as entityDescriptor>
Edit${entity.entityClassName}Label_${entityDescriptor.id?replace(".","_")}=${entityDescriptor.label}:
<#if entityDescriptor.required>Edit${entity.entityClassName}RequiredMessage_${entityDescriptor.id?replace(".","_")}=The ${entityDescriptor.label} field is required.
</#if>Edit${entity.entityClassName}Title_${entityDescriptor.id?replace(".","_")}=${entityDescriptor.label}
Edit${entity.entityClassName}HelpText_${entityDescriptor.id?replace(".","_")}=Please provide information for ${entityDescriptor.label}
    </#list>
View${entity.entityClassName}Title=View ${entity.entityNaturalName}
View${entity.entityClassName}DestroyLink=Destroy
View${entity.entityClassName}EditLink=Edit
View${entity.entityClassName}CreateLink=Create New ${entity.entityNaturalName}
View${entity.entityClassName}ShowAllLink=Show All ${entity.entityNaturalName} Items
View${entity.entityClassName}IndexLink=Index
    <#list entity.entityDescriptors as entityDescriptor>
View${entity.entityClassName}Label_${entityDescriptor.id?replace(".","_")}=${entityDescriptor.label}:
View${entity.entityClassName}Title_${entityDescriptor.id?replace(".","_")}=${entityDescriptor.label}
    </#list>
List${entity.entityClassName}Title=List ${entity.entityNaturalName}
List${entity.entityClassName}Empty=(No ${entity.entityNaturalName} Items Found)
List${entity.entityClassName}DestroyLink=Destroy
List${entity.entityClassName}EditLink=Edit
List${entity.entityClassName}ViewLink=View
List${entity.entityClassName}CreateLink=Create New ${entity.entityNaturalName}
List${entity.entityClassName}IndexLink=Index
    <#list entity.entityDescriptors as entityDescriptor>
List${entity.entityClassName}Title_${entityDescriptor.id?replace(".","_")}=${entityDescriptor.label}
    </#list>
</#list>
