<#if comment>

  TEMPLATE DESCRIPTION:

  This is Java template for 'JSF Pages From Entity Beans' Lazy Loading implementation class.
  Templating is performed using FreeMaker (http://freemarker.org/) - see its documentation
  for full syntax. Variables available for templating are:

    ejbFacadePackageName - package name of the for EJB facade classes (type: String)

  This template is accessible via top level menu Tools->Templates and can
  be found in category PrimeFaces CRUD Generator->PrimeFaces Pages from Entity Classes.

</#if>
package ${ejbFacadePackageName};

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EntityUtility {

    public static Object getFieldValue(Object bean, String fieldName) {
        try {
            BeanInfo info = Introspector.getBeanInfo(bean.getClass());
            for (PropertyDescriptor pd : info.getPropertyDescriptors()) {
                if (pd.getName().equals(fieldName)) {
                    return pd.getReadMethod().invoke(bean);
                }
            }
        } catch (IntrospectionException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(EntityUtility.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}
