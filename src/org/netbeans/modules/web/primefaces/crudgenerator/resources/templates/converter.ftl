<#if comment>

  TEMPLATE DESCRIPTION:

  This is Java template for 'JSF Pages From Entity Beans' controller class. Templating
  is performed using FreeMaker (http://freemarker.org/) - see its documentation
  for full syntax. Variables available for templating are:

    converterClassName - converter class name (type: String)
    converterPackageName - controller package name (type: String)
    entityClassName - entity class name without package (type: String)
    entityFullClassName - fully qualified entity class name (type: String)
    ejbClassName - EJB class name (type: String)
    ejbFullClassName - fully qualified EJB class name (type: String)
    managedBeanName - name of managed bean (type: String)
    cdiExtensionVersion - CDI Extension Version (type: Version)
    keyEmbedded - is entity primary key is an embeddable class (type: Boolean)
    keyType - fully qualified class name of entity primary key
    keyBody - body of Controller.Converter.getKey() method
    keyStringBody - body of Controller.Converter.getStringKey() method
    keyGetter - entity getter method returning primaty key instance
    keySetter - entity setter method to set primary key instance

  This template is accessible via top level menu Tools->Templates and can
  be found in category PrimeFaces CRUD Generator->PrimeFaces Pages from Entity Classes.

</#if>
package ${converterPackageName};

import ${entityFullClassName};
import ${ejbFullClassName};
import ${controllerPackageName}.util.JsfUtil;
import java.util.logging.Level;
import java.util.logging.Logger;
<#if (jsfVersion.compareTo("2.2") >= 0)>
import javax.faces.convert.FacesConverter;
<#elseif cdiEnabled?? && cdiEnabled == true>
import javax.inject.Named;
<#else>
import javax.faces.bean.ManagedBean;
</#if>
<#if ejbClassName??>
<#if cdiEnabled?? && cdiEnabled == true>
<#if (jsfVersion.compareTo("2.2") >= 0)>
import javax.enterprise.inject.spi.CDI;
<#else>
import javax.inject.Inject;
</#if>
<#elseif ejbClassName??>
import javax.ejb.EJB;
</#if>
</#if>
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

<#if (jsfVersion.compareTo("2.2") >= 0)>
@FacesConverter(value = "${converterClassName?uncap_first}")
<#elseif cdiEnabled?? && cdiEnabled == true>
@Named(value = "${converterClassName?uncap_first}")
<#else>
@ManagedBean(name = "${converterClassName?uncap_first}")
</#if>
public class ${converterClassName?cap_first} implements Converter {

<#-- IMPORTANT: Under Java EE 6 and older CDI implementations that came prior to JSF 2.2, it was
  -- possible to actually inject and EJB object here. This functionality was non-standard and was
  -- later removed with JSF 2.2 and newer CDI implementations. For those newer CDI implementations,
  -- please see the usage of the CDI utility class below in the getEjbFacade method.
  -->
<#if ejbClassName??>
<#if !(cdiEnabled?? && cdiEnabled == true)>
    @EJB
<#elseif (cdiEnabled?? && cdiEnabled == true && jsfVersion.compareTo("2.2") < 0)>
    @Inject
</#if>
    private ${ejbClassName} ejbFacade;
</#if>

<#if keyEmbedded>
    private static final String SEPARATOR = "#";
    private static final String SEPARATOR_ESCAPED = "\\#";
</#if>

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
        if (value == null || value.length() == 0 || JsfUtil.isDummySelectItem(component, value)) {
            return null;
        }
<#if ejbClassName??>
        return this.getEjbFacade().find(getKey(value));
<#elseif jpaControllerClassName??>
        ${abstractControllerClassName}<${entityClassName}> controller = (${abstractControllerClassName}<${entityClassName}>)facesContext.getApplication().getELResolver().
                getValue(facesContext.getELContext(), null, "${managedBeanName}");
        return controller.getJpaController().find${entityClassName}(getKey(value));
</#if>
    }

    ${keyType} getKey(String value) {
        ${keyType} key;
${keyBody}
        return key;
    }

    String getStringKey(${keyType} value) {
        StringBuffer sb = new StringBuffer();
${keyStringBody}
        return sb.toString();
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
        if (object == null || 
            (object instanceof String && ((String) object).length() == 0)) {
            return null;
        }
        if (object instanceof ${entityClassName}) {
            ${entityClassName} o = (${entityClassName}) object;
            return getStringKey(o.${keyGetter}());
        } else {
            <#-- 2013-02-04 Kay Wrobel: Do not throw exception, but Log the event
                 so the app can continue to run. This also allows individual <selectItem>
                 tags with empty values inside <selectOneMenu> tags to prompt for
                 e.g. "Please select one", which passes a String object to this method!
            throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: "+${entityClassName}.class.getName());
            -->
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), ${entityClassName}.class.getName()});
            return null;
        }
    }
    
<#if ejbClassName??>
    private ${ejbClassName} getEjbFacade() {
<#if (cdiEnabled?? && cdiEnabled == true  && jsfVersion.compareTo("2.2") >= 0)>
        this.ejbFacade = CDI.current().select(${ejbClassName}.class).get();
</#if>
        return this.ejbFacade;
    }
</#if>
}
