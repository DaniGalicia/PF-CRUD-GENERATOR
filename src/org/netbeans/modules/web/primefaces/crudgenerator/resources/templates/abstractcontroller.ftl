<#if comment>

  TEMPLATE DESCRIPTION:

  This is Java template for 'JSF Pages From Entity Beans' controller class. Templating
  is performed using FreeMaker (http://freemarker.org/) - see its documentation
  for full syntax. Variables available for templating are:

    bundle - name of the bundle set in faces-config.xml (type: String)
    cdiEnabled - whether the project is CDI-Enabled (type: boolean)
    injectAbstractEJB - whether CDI-EJB Injection should happen in AbstractController (type: boolean)
    controllerClassName - controller class name (type: String)
    controllerPackageName - controller package name (type: String)
    entityClassName - entity class name without package (type: String)
    entityFullClassName - fully qualified entity class name (type: String)
    ejbClassName - EJB class name (type: String)
    ejbFullClassName - fully qualified EJB class name (type: String)
    managedBeanName - name of managed bean (type: String)
    keyEmbedded - is entity primary key is an embeddable class (type: Boolean)
    keyType - fully qualified class name of entity primary key
    keyBody - body of Controller.Converter.getKey() method
    keyStringBody - body of Controller.Converter.getStringKey() method
    keyGetter - entity getter method returning primaty key instance
    keySetter - entity setter method to set primary key instance
    doRelationshipNavigation - Whether to perform navigation to related entities (type: boolean)
    cdiExtensionVersion - The version of MyFaces CODI, if any (type: Version)

  This template is accessible via top level menu Tools->Templates and can
  be found in category PrimeFaces CRUD Generator->PrimeFaces Pages from Entity Classes.

</#if>
package ${controllerPackageName};

import ${ejbFacadeFullClassName};
import ${lazyEntityDataModelFullClassName};
import ${controllerPackageName}.util.JsfUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.event.ActionEvent;
<#if cdiEnabled?? && cdiEnabled  && injectAbstractEJB >
import javax.inject.Inject;
</#if>

import java.util.ResourceBundle;
import javax.ejb.EJBException;
<#if doRelationshipNavigation && !cdiExtensionVersion??>
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
</#if>
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

/**
 * Represents an abstract shell of to be used as JSF Controller to be used in
 * AJAX-enabled applications. No outcomes will be generated from its methods
 * since handling is designed to be done inside one page.
 * @param <T>   the concrete Entity type of the Controller bean to be created
 */
public abstract class ${abstractControllerClassName}<T> implements Serializable {

    private static final long serialVersionUID = 1L;

<#if cdiEnabled?? && cdiEnabled  && injectAbstractEJB >
    @Inject
</#if>
    private ${ejbFacadeClassName}<T> ejbFacade;
    private Class<T> itemClass;
    private T selected;
    private Collection<T> items;
    private ${lazyEntityDataModelClassName}<T> lazyItems;


    private enum PersistAction {
        CREATE,
        DELETE,
        UPDATE
    }

    public ${abstractControllerClassName}() {
    }

    public ${abstractControllerClassName}(Class<T> itemClass) {
        this.itemClass = itemClass;
    }

<#if !cdiEnabled?? || cdiEnabled == false || injectAbstractEJB == false>

    /**
     * Initialize the concrete controller bean.
     * This AbstractController requires the EJB Facade object for most operations,
     * and that task is performed by the concrete controller bean.
<#if doRelationshipNavigation>
     * <p>
     * In addition, each controller for an entity that has Many-To-One relationships,
     * needs to establish references to those entities' controllers in order to display
     * their information from a context menu.
</#if>
     */
    public abstract void init();

    /**
     * Retrieve the current EJB Facade object so that other beans in this package
     * can perform additional data layer tasks (e.g. additional queries)
     *
     * @return  the concrete EJB Facade associated with the concrete controller bean.
     */
    protected AbstractFacade<T> getFacade() {
        return ejbFacade;
    }

    /**
     * Sets the concrete EJB Facade object so that data layer actions can be
     * performed. This applies to all basic CRUD actions this controller performs.
     *
     * @param ejbFacade  the concrete EJB Facade to perform data layer actions with
     */
    protected void setFacade(AbstractFacade<T> ejbFacade) {
        this.ejbFacade = ejbFacade;
    }

</#if>
    /**
     * Retrieve the currently selected item.
     *
     * @return  the currently selected Entity
     */
    public T getSelected() {
        return selected;
    }

    /**
     * Pass in the currently selected item.
     *
     * @param selected  the Entity that should be set as selected
     */
    public void setSelected(T selected) {
        this.selected = selected;
    }

    /**
     * Sets any embeddable key fields if an Entity uses composite keys.
     * If the entity does not have composite keys, this method performs no actions
     * and exists purely to be overridden inside a concrete controller class.
     */
    protected void setEmbeddableKeys() {
        // Nothing to do if entity does not have any embeddable key.
    };

    /**
     * Sets the concrete embedded key of an Entity that uses composite keys.
     * This method will be overriden inside concrete controller classes and does
     * nothing if the specific entity has no composite keys.
     */
    protected void initializeEmbeddableKey() {
        // Nothing to do if entity does not have any embeddable key.
    }

    /**
     * Returns all items as a Collection object.
     *
     * @return  a collection of Entity items returned by the data layer
     */
    public Collection<T> getItems() {
        if (items == null) {
            items = this.ejbFacade.findAll();
        }
        return items;
    }

    /**
     * Pass in collection of items
     *
     * @param items a collection of Entity items
     */
    public void setItems(Collection<T> items) {
        this.items = items;
    }

    /**
     *
     * @return Entity-specific Lazy Data Model
     */
    public ${lazyEntityDataModelClassName}<T> getLazyItems() {
        if (lazyItems == null) {
            lazyItems = new ${lazyEntityDataModelClassName}<>(this.ejbFacade);
        }
        return lazyItems;
    }

    public void setLazyItems(${lazyEntityDataModelClassName}<T> lazyItems) {
        this.lazyItems = lazyItems;
    }

    public void setLazyItems(Collection<T> items) {
        if (items instanceof List) {
            lazyItems = new ${lazyEntityDataModelClassName}<>((List<T>) items);
        } else {
            lazyItems = new ${lazyEntityDataModelClassName}<>(new ArrayList<>(items));
        }
    }

    /**
     * Apply changes to an existing item to the data layer.
     *
     * @param event an event from the widget that wants to save an Entity to the data layer
     */
    public void save(ActionEvent event) {
        String msg = ResourceBundle.getBundle("${bundle}").getString(itemClass.getSimpleName() + "Updated");
        persist(PersistAction.UPDATE, msg);
    }

    /**
     * Store a new item in the data layer.
     *
     * @param event an event from the widget that wants to save a new Entity to the data layer
     */
    public void saveNew(ActionEvent event) {
        String msg = ResourceBundle.getBundle("${bundle}").getString(itemClass.getSimpleName() + "Created");
        persist(PersistAction.CREATE, msg);
        if (!isValidationFailed()) {
            items = null; // Invalidate list of items to trigger re-query.
            lazyItems = null; // Invalidate list of lazy items to trigger re-query.
        }
    }

    /**
     * Remove an existing item from the data layer.
     *
     * @param event an event from the widget that wants to delete an Entity from the data layer
     */
    public void delete(ActionEvent event) {
        String msg = ResourceBundle.getBundle("${bundle}").getString(itemClass.getSimpleName() + "Deleted");
        persist(PersistAction.DELETE, msg);
        if (!isValidationFailed()) {
            selected = null; // Remove selection
            items = null; // Invalidate list of items to trigger re-query.
            lazyItems = null; // Invalidate list of lazy items to trigger re-query.
        }
    }

    /**
     * Performs any data modification actions for an entity. The actions that
     * can be performed by this method are controlled by the {@link PersistAction}
     * enumeration and are either CREATE, EDIT or DELETE.
     *
     * @param persistAction     a specific action that should be performed on the current item
     * @param successMessage    a message that should be displayed when persisting the item succeeds
     */
    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            this.setEmbeddableKeys();
            try {
                if (persistAction != PersistAction.DELETE) {
                    this.ejbFacade.edit(selected);
                } else {
                    this.ejbFacade.remove(selected);
                }
                JsfUtil.addSuccessMessage(successMessage);
            } catch (EJBException ex) {
                Throwable cause = JsfUtil.getRootCause(ex.getCause());
                if (cause != null) {
                    if (cause instanceof ConstraintViolationException) {
                        ConstraintViolationException excp = (ConstraintViolationException) cause;
                        for (ConstraintViolation s : excp.getConstraintViolations()) {
                            JsfUtil.addErrorMessage(s.getMessage());
                        }
                    } else {
                        String msg = cause.getLocalizedMessage();
                        if (msg.length() > 0) {
                            JsfUtil.addErrorMessage(msg);
                        } else {
                            JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
                        }
                    }
                }
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("${bundle}").getString("PersistenceErrorOccured"));
            }
        }
    }

    /**
     * Creates a new instance of an underlying entity and assigns it to Selected
     * property.
     *
     * @param event an event from the widget that wants to create a new, unmanaged Entity for the data layer
     * @return      a new, unmanaged Entity
     */
    public T prepareCreate(ActionEvent event) {
        T newItem;
        try {
            newItem = itemClass.newInstance();
            this.selected = newItem;
            initializeEmbeddableKey();
            return newItem;
        } catch (InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Inform the user interface whether any validation error exist on a page.
     *
     * @return  a logical value whether form validation has passed or failed
     */
    public boolean isValidationFailed() {
        return JsfUtil.isValidationFailed();
    }

    /**
     * Retrieve all messages as a String to be displayed on the page.
     *
     * @param clientComponent   the component for which the message applies
     * @param defaultMessage    a default message to be shown
     * @return                  a concatenation of all messages
     */
    public String getComponentMessages(String clientComponent, String defaultMessage) {
        return JsfUtil.getComponentMessages(clientComponent, defaultMessage);
    }

<#if doRelationshipNavigation && !cdiExtensionVersion??>
    /**
     * Retrieve a collection of Entity items for a specific Controller from another JSF page via Request parameters.
     */
    @PostConstruct
    public void initParams() {
        Object paramItems = FacesContext.getCurrentInstance().getExternalContext().getRequestMap().get(itemClass.getSimpleName() + "_items");
        if (paramItems != null) {
            setItems((Collection<T>) paramItems);
            setLazyItems((Collection<T>) paramItems);
        }
    }

</#if>
}