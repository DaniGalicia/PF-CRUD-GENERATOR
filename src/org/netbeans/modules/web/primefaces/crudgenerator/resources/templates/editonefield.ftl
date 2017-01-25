        <#if (primeFacesVersion.compareTo("3.3") < 0 || (crud != "Create" && (entityDescriptor.primaryKey || entityDescriptor.embeddedKey)))>
                        <h:outputLabel value="${r"#{"}${bundle}.${crud}${entityName}Label_${entityDescriptor.id?replace(".","_")}${r"}"}" for="${entityDescriptor.id?replace(".","_")}" />
        <#else>
                        <p:outputLabel value="${r"#{"}${bundle}.${crud}${entityName}Label_${entityDescriptor.id?replace(".","_")}${r"}"}" for="${entityDescriptor.id?replace(".","_")}" />
        </#if>
        <#if tooltipMessages>
                      <h:panelGroup>
        </#if>
        <#if crud != "Create" && (entityDescriptor.primaryKey || entityDescriptor.embeddedKey)>
                        <h:outputText id="${entityDescriptor.id?replace(".","_")}" value="${r"#{"}${entityDescriptor.name}${r"}"}" />
        <#elseif entityDescriptor.dateTimeFormat?? && entityDescriptor.dateTimeFormat != "">
                        <p:calendar id="${entityDescriptor.id?replace(".","_")}" pattern="${entityDescriptor.dateTimeFormat}" value="${r"#{"}${entityDescriptor.name}${r"}"}" title="${r"#{"}${bundle}.${crud}${entityName}Title_${entityDescriptor.id?replace(".","_")}${r"}"}" <#if entityDescriptor.required>required="true" requiredMessage="${r"#{"}${bundle}.${crud}${entityName}RequiredMessage_${entityDescriptor.id?replace(".","_")}${r"}"}"</#if> showOn="button"/>
        <#elseif entityDescriptor.returnType?contains("boolean") || entityDescriptor.returnType?contains("Boolean")>
          <#if (primeFacesVersion.compareTo("3") >= 0)>
                        <p:selectBooleanCheckbox id="${entityDescriptor.id?replace(".","_")}" value="${r"#{"}${entityDescriptor.name}${r"}"}" <#if entityDescriptor.required>required="true" requiredMessage="${r"#{"}${bundle}.${crud}${entityName}RequiredMessage_${entityDescriptor.id?replace(".","_")}${r"}"}"</#if>/>
          <#else>
                        <h:selectBooleanCheckbox id="${entityDescriptor.id?replace(".","_")}" value="${r"#{"}${entityDescriptor.name}${r"}"}" title="${r"#{"}${bundle}.${crud}${entityName}Title_${entityDescriptor.id?replace(".","_")}${r"}"}" <#if entityDescriptor.required>required="true" requiredMessage="${r"#{"}${bundle}.${crud}${entityName}RequiredMessage_${entityDescriptor.id?replace(".","_")}${r"}"}"</#if>/>
          </#if>
        <#elseif entityDescriptor.blob || (entityDescriptor.maxSize?? && entityDescriptor.maxSize > textThreshold) >
                        <p:inputTextarea rows="4" cols="30" id="${entityDescriptor.id?replace(".","_")}" value="${r"#{"}${entityDescriptor.name}${r"}"}" title="${r"#{"}${bundle}.${crud}${entityName}Title_${entityDescriptor.id?replace(".","_")}${r"}"}" <#if entityDescriptor.required>required="true" requiredMessage="${r"#{"}${bundle}.${crud}${entityName}RequiredMessage_${entityDescriptor.id?replace(".","_")}${r"}"}"</#if>/>
        <#elseif entityDescriptor.relationshipOne>
                        <p:selectOneMenu id="${entityDescriptor.id?replace(".","_")}" value="${r"#{"}${entityDescriptor.name}${r"}"}" <#if entityDescriptor.required>required="true" requiredMessage="${r"#{"}${bundle}.${crud}${entityName}RequiredMessage_${entityDescriptor.id?replace(".","_")}${r"}"}"</#if><#if (jsfVersion.compareTo("2.2") >= 0)> converter="${entityDescriptor.valuesConverter}"</#if>>
                            <f:selectItem itemLabel="${r"#{"}${bundle}.SelectOneMessage${r"}"}"/>
                            <f:selectItems value="${r"#{"}${entityDescriptor.valuesListGetter}${r"}"}"
                                           var="${entityDescriptor.id?replace(".","_")}Item"
                                           itemValue="${r"#{"}${entityDescriptor.id?replace(".","_")}Item${r"}"}"
            <#if relationLabelName != "">
                                           itemLabel="${r"#{"}${entityDescriptor.id?replace(".","_")}Item.${relationLabelName}${r".toString()}"}"
            </#if>
                            />
                            <#if (jsfVersion.compareTo("2.2") < 0)><f:converter binding="${r"#{"}${entityDescriptor.valuesConverter}${r"}"}"/></#if>
                        </p:selectOneMenu>
        <#elseif entityDescriptor.relationshipMany>
          <#if entityDescriptor.relationshipOwner>
                        <p:selectManyMenu id="${entityDescriptor.id?replace(".","_")}" value="${r"#{"}${entityDescriptor.name}${r"}"}" <#if entityDescriptor.required>required="true" requiredMessage="${r"#{"}${bundle}.${crud}${entityName}RequiredMessage_${entityDescriptor.id?replace(".","_")}${r"}"}"</#if><#if (jsfVersion.compareTo("2.2") >= 0)> converter="${entityDescriptor.valuesConverter}"</#if>>
                            <f:selectItems value="${r"#{"}${entityDescriptor.valuesListGetter}${r"}"}"
                                           var="${entityDescriptor.id?replace(".","_")}Item"
                                           itemValue="${r"#{"}${entityDescriptor.id?replace(".","_")}Item${r"}"}"
            <#if relationLabelName != "">
                                           itemLabel="${r"#{"}${entityDescriptor.id?replace(".","_")}Item.${relationLabelName}${r".toString()}"}"
            </#if>
                            />
                            <#if (jsfVersion.compareTo("2.2") < 0)><f:converter binding="${r"#{"}${entityDescriptor.valuesConverter}${r"}"}"/></#if>
                        </p:selectManyMenu>
          </#if>
        <#else>
                        <p:inputText id="${entityDescriptor.id?replace(".","_")}" value="${r"#{"}${entityDescriptor.name}${r"}"}" title="${r"#{"}${bundle}.${crud}${entityName}Title_${entityDescriptor.id?replace(".","_")}${r"}"}"<#if entityDescriptor.required> required="true" requiredMessage="${r"#{"}${bundle}.${crud}${entityName}RequiredMessage_${entityDescriptor.id?replace(".","_")}${r"}"}"</#if><#if entityDescriptor.maxSize??> size="${entityDescriptor.maxSize}" maxlength="${entityDescriptor.maxSize}"</#if>/>
        </#if>
        <#if tooltipMessages>
                        <p:tooltip for="${entityDescriptor.id?replace(".","_")}" value="${r"#{"}${managedBean}.getComponentMessages('${entityDescriptor.id?replace(".","_")}', ${bundle}.${crud}${entityName}HelpText_${entityDescriptor.id?replace(".","_")})${r"}"}"/>
                      </h:panelGroup>
        </#if>