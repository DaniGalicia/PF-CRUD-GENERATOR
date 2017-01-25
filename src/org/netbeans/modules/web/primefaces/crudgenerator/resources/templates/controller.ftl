<#if comment>

  TEMPLATE DESCRIPTION:

  This is Java template for 'JSF Pages From Entity Beans' controller class. Templating
  is performed using FreeMaker (http://freemarker.org/) - see its documentation
  for full syntax. Variables available for templating are:

    cdiEnabled - whether the project is CDI-Enabled (type: boolean)
    injectAbstractEJB - whether CDI-EJB Injection should happen in AbstractController (type: boolean)
    controllerClassName - controller class name (type: String)
    controllerPackageName - controller package name (type: String)
    entityClassName - entity class name without package (type: String)
    importEntityFullClassName - whether to import entityFullClassName or not
    entityFullClassName - fully qualified entity class name (type: String)
    ejbClassName - EJB class name (type: String)
    importEjbFullClassName - whether to import ejbFullClassName or not
    ejbFullClassName - fully qualified EJB class name (type: String)
    managedBeanName - name of managed bean (type: String)
    cdiExtensionVersion - CDI Extension Version (type: Version)
    viewAccessScopedFullClassName - CDI Fully Qualified Class Name of ViewAccessScoped annotation (type: String)
    keyEmbedded - is entity primary key is an embeddable class (type: Boolean)
    keyType - fully qualified class name of entity primary key
    keyBody - body of Controller.Converter.getKey() method
    keyStringBody - body of Controller.Converter.getStringKey() method
    keyGetter - entity getter method returning primary key instance
    keySetter - entity setter method to set primary key instance
    embeddedIdFields - contains information about embedded primary Ids
    doRelationshipNavigation - Whether to perform navigation to related entities (type: boolean)
    hasRelationships - Whether this entity has any relationships to navigate to (type: boolean)
    hasParentRelationships - Whether this entity has parent relationships to navigate to (type: boolean)
    hasChildRelationships - Whether this entity has child relationships to navigate to (type: boolean)
    doMobile - Whether the app also has mobile pages (type: boolean)
    relationShipEntityDescriptors - list of beans describing individual entities. Bean has following properties:
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
        relationClassName - class name for the related Entity (type: String)
        relationQualifiedClassName - fully qualified class name for the related Entity (type: String)
        getRelationsLabelName(String) - field name of the foreign entity field matching String (type: String)
        returnType - fully qualified data type of the field
        id - field id name (type: String)
        required - is field optional and nullable or it is not? (type: boolean)
        valuesListGetter - if item is of type 1:many or many:many relationship then use this
            getter to populate <h:selectOneMenu> or <h:selectManyMenu>
        valuesConverter - if item is of type 1:many or many:many relationship then use this
            for the converter binding of <h:selectOneMenu> or <h:selectManyMenu>
        versionField - is the field a Version field (type: boolean)

  This template is accessible via top level menu Tools->Templates and can
  be found in category PrimeFaces CRUD Generator->PrimeFaces Pages from Entity Classes.

</#if>
package ${controllerPackageName};

<#if doMobile>
import ${controllerUtilPackageName}.MobilePageController;
</#if>
<#if importEntityFullClassName?? && importEntityFullClassName>
import ${entityFullClassName};
</#if>
<#if !cdiEnabled?? || cdiEnabled == false || (cdiEnabled?? && cdiEnabled && injectAbstractEJB == false)>
<#if importEjbFullClassName?? && importEjbFullClassName>
import ${ejbFullClassName};
</#if>
</#if>
<#if managedBeanName??>
<#if cdiEnabled?? && cdiEnabled>
import javax.inject.Named;
<#if cdiExtensionVersion?? && viewAccessScopedFullClassName??>
import ${viewAccessScopedFullClassName};
<#elseif (jsfVersion.compareTo("2.2") >= 0)>
import javax.faces.view.ViewScoped;
<#else>
import javax.enterprise.context.SessionScoped;
</#if>
<#else>
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
<#if doRelationshipNavigation && (hasParentRelationships || doMobile) && (!cdiEnabled?? || cdiEnabled == false)>
import javax.faces.bean.ManagedProperty;
</#if>
import javax.faces.bean.ViewScoped;
</#if>
</#if>
<#if doRelationshipNavigation && hasChildRelationships && !(cdiEnabled?? && cdiEnabled && cdiExtensionVersion??)>
import javax.faces.context.FacesContext;
</#if>
<#if doRelationshipNavigation && hasParentRelationships>
import javax.faces.event.ActionEvent;
</#if>
<#if !cdiEnabled?? || cdiEnabled == false || (cdiEnabled?? && cdiEnabled && injectAbstractEJB == false)>
import javax.annotation.PostConstruct;
</#if>
<#if (cdiEnabled?? && cdiEnabled) && ((doRelationshipNavigation && hasRelationships) || injectAbstractEJB == false)>
import javax.inject.Inject;
</#if>
<#if managedBeanName??>
<#if cdiEnabled?? && cdiEnabled>
@Named(value="${managedBeanName}")
<#if cdiExtensionVersion??>
@ViewAccessScoped
<#elseif (jsfVersion.compareTo("2.2") >= 0)>
@ViewScoped
<#else>
@SessionScoped
</#if>
<#else>
@ManagedBean(name = "${managedBeanName}")
@ViewScoped
</#if>
</#if>
public class ${controllerClassName} extends ${abstractControllerClassName}<${entityClassName}> {

<#if !cdiEnabled?? || cdiEnabled == false || (cdiEnabled?? && cdiEnabled && injectAbstractEJB == false)>
<#if !cdiEnabled?? || cdiEnabled == false>
    @EJB
</#if>
<#if (cdiEnabled?? && cdiEnabled && injectAbstractEJB == false)>
    @Inject
</#if>
    private ${ejbClassName} ejbFacade;
</#if>
<#if doRelationshipNavigation && hasRelationships>
<#list relationshipEntityDescriptors as relationshipEntityDescriptor>
<#if (cdiEnabled?? && cdiEnabled && cdiExtensionVersion??) || relationshipEntityDescriptor.relationshipOne>
<#if (cdiEnabled?? && cdiEnabled) >
    @Inject
<#else>
    @ManagedProperty(value = "${r"#{"}${relationshipEntityDescriptor.relationClassName?uncap_first}Controller${r"}"}")
</#if>
    private ${relationshipEntityDescriptor.relationClassName}Controller ${relationshipEntityDescriptor.id?uncap_first}Controller;
</#if>
</#list>
</#if>
<#if doRelationshipNavigation && doMobile>
<#if cdiEnabled?? && cdiEnabled == true>
    @Inject
<#else>
    @ManagedProperty(value = "${r"#{"}mobilePageController${r"}"}")
</#if>
    private MobilePageController mobilePageController;
</#if>
<#if doRelationshipNavigation && hasParentRelationships && (!cdiEnabled?? || cdiEnabled == false)>
<#list relationshipEntityDescriptors as relationshipEntityDescriptor>
<#if relationshipEntityDescriptor.relationshipOne>

    /* Setter method for managed property ${relationshipEntityDescriptor.id}Controller */
    public void set${relationshipEntityDescriptor.id?cap_first}Controller(${relationshipEntityDescriptor.relationClassName}Controller ${relationshipEntityDescriptor.id?uncap_first}Controller) {
        this.${relationshipEntityDescriptor.id?uncap_first}Controller = ${relationshipEntityDescriptor.id?uncap_first}Controller;
    }
</#if>
</#list>
</#if>
<#if doRelationshipNavigation && doMobile && !(cdiEnabled?? && cdiEnabled)>

    /* Setter method for managed property mobilePageController */
    public void setMobilePageController(MobilePageController mobilePageController) {
        this.mobilePageController = mobilePageController;
    }
</#if>
<#if !cdiEnabled?? || cdiEnabled == false || (cdiEnabled?? && cdiEnabled && injectAbstractEJB == false)>

    /**
     * Initialize the concrete ${entityClassName} controller bean.
     * The AbstractController requires the EJB Facade object for most operations.
     */
    @PostConstruct
    @Override
    public void init() {
        super.setFacade(ejbFacade);
    }
</#if>

    public ${controllerClassName}() {
        // Inform the Abstract parent controller of the concrete ${entityClassName?cap_first} Entity
        super(${entityClassName}.class);
    }

<#if embeddedIdFields??>
    @Override
    protected void setEmbeddableKeys() {
  <#list embeddedIdFields as fields>
        this.getSelected().${keyGetter}().${fields.getEmbeddedSetter()}(this.getSelected().${fields.getCodeToPopulate()});
  </#list>
    }
</#if>        

<#if keyEmbedded>
    @Override
    protected void initializeEmbeddableKey() {
            this.getSelected().${keySetter}(new ${keyType}());
    }

</#if>
<#if doRelationshipNavigation && hasParentRelationships>
    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
<#list relationshipEntityDescriptors as relationshipEntityDescriptor>
<#if relationshipEntityDescriptor.relationshipOne>
        ${relationshipEntityDescriptor.id?uncap_first}Controller.setSelected(null);
</#if>
</#list>
    }
</#if>

<#if doRelationshipNavigation && hasRelationships>
<#list relationshipEntityDescriptors as relationshipEntityDescriptor>
<#if relationshipEntityDescriptor.relationshipOne>
    /**
     * Sets the "selected" attribute of the ${relationshipEntityDescriptor.relationClassName?cap_first} controller
     * in order to display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepare${relationshipEntityDescriptor.id?cap_first}(ActionEvent event) {
        if (this.getSelected() != null && ${relationshipEntityDescriptor.id?uncap_first}Controller.getSelected() == null) {
            ${relationshipEntityDescriptor.id?uncap_first}Controller.setSelected(this.getSelected().get${relationshipEntityDescriptor.id?cap_first}());
        }
    }
</#if>
<#if relationshipEntityDescriptor.relationshipMany>
    /**
<#if (cdiEnabled?? && cdiEnabled && cdiExtensionVersion??)>
    * Passes collection of ${relationshipEntityDescriptor.relationClassName?cap_first} entities that are retrieved from ${entityClassName}?cap_first
<#else>
    * Sets the "items" attribute with a collection of ${relationshipEntityDescriptor.relationClassName?cap_first} entities that are retrieved from ${entityClassName}?cap_first
</#if>
     * and returns the navigation outcome.
     *
     * @return  navigation outcome for ${relationshipEntityDescriptor.relationClassName?cap_first} page
     */
    public String navigate${relationshipEntityDescriptor.id?cap_first}() {
        if (this.getSelected() != null) {
<#if (cdiEnabled?? && cdiEnabled && cdiExtensionVersion??)>
            ${relationshipEntityDescriptor.id?uncap_first}Controller.setItems(this.getSelected().get${relationshipEntityDescriptor.id?cap_first}());
            ${relationshipEntityDescriptor.id?uncap_first}Controller.setLazyItems(this.getSelected().get${relationshipEntityDescriptor.id?cap_first}());
<#else>
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("${relationshipEntityDescriptor.relationClassName?cap_first}_items", this.getSelected().get${relationshipEntityDescriptor.id?cap_first}());
</#if>
        }
<#if (cdiEnabled?? && cdiEnabled && cdiExtensionVersion??)>
        return <#if doMobile>this.mobilePageController.getMobilePagesPrefix() + </#if>"${jsfFolder}${r"/"}${relationshipEntityDescriptor.relationClassName?uncap_first}${r"/index?faces-redirect=true"}";
<#else>
        return <#if doMobile>this.mobilePageController.getMobilePagesPrefix() + </#if>"${jsfFolder}${r"/"}${relationshipEntityDescriptor.relationClassName?uncap_first}${r"/index"}";
</#if>
    }

</#if>
</#list>
</#if>
}
