<#if comment>

  TEMPLATE DESCRIPTION:

  This is Java template for 'JSF Pages From Entity Beans' Lazy Loading implementation class.
  Its purpose is to provide a sorting mechanism for entity lists for the LazyEntityDataModel.
  Templating is performed using FreeMaker (http://freemarker.org/) - see its documentation
  for full syntax. Variables available for templating are:

    ejbFacadePackageName - package name of the for EJB facade classes (type: String)

  This template is accessible via top level menu Tools->Templates and can
  be found in category PrimeFaces CRUD Generator->PrimeFaces Pages from Entity Classes.

</#if>
package ${ejbFacadePackageName};

import java.util.Comparator;
import org.primefaces.model.SortOrder;

/**
 *
 * @param <T>
 */
public class LazyEntitySorter<T> implements Comparator<T> {

    private final String sortField;
    private final SortOrder sortOrder;

    /**
     *
     * @param sortField
     * @param sortOrder
     */
    public LazyEntitySorter(String sortField, SortOrder sortOrder) {
        this.sortField = sortField;
        this.sortOrder = sortOrder;
    }

    @Override
    public int compare(T entity1, T entity2) {
        try {
            Object value1 = EntityUtility.getFieldValue(entity1, sortField);
            Object value2 = EntityUtility.getFieldValue(entity2, sortField);

            int value = ((Comparable) value1).compareTo(value2);

            return SortOrder.ASCENDING.equals(sortOrder) ? value : -1 * value;
        } catch (SecurityException | IllegalArgumentException e) {
            throw new RuntimeException();
        }
    }

}
