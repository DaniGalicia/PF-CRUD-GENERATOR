                        <h:outputText value="${r"#{"}${bundle}.${crud}${entityName}Label_${entityDescriptor.id?replace(".","_")}${r"}"}"/>
        <#if entityDescriptor.dateTimeFormat?? && entityDescriptor.dateTimeFormat != "">
                        <h:outputText value="${r"#{"}${entityDescriptor.name}${r"}"}" title="${r"#{"}${bundle}.${crud}${entityName}Title_${entityDescriptor.id?replace(".","_")}${r"}"}">
                            <f:convertDateTime pattern="${entityDescriptor.dateTimeFormat}" />
                        </h:outputText>
        <#elseif entityDescriptor.returnType?contains("boolean") || entityDescriptor.returnType?contains("Boolean")>
                        <h:selectBooleanCheckbox id="${entityDescriptor.id?replace(".","_")}" value="${r"#{"}${entityDescriptor.name}${r"}"}" title="${r"#{"}${bundle}.Edit${entityName}Title_${entityDescriptor.id?replace(".","_")}${r"}"}" <#if entityDescriptor.required>required="true" requiredMessage="${r"#{"}${bundle}.Edit${entityName}RequiredMessage_${entityDescriptor.id?replace(".","_")}${r"}"}"</#if> disabled="true"/>
        <#elseif entityDescriptor.relationshipOne>
            <#if relationLabelName?? && relationLabelName != "">
                        <h:outputText value="${r"#{"}${entityDescriptor.name}.${relationLabelName}${r"}"}"/>
            <#else>
                        <h:outputText value="${r"#{"}${entityDescriptor.name}${r"}"}">
<#if (jsfVersion.compareTo("2.2") < 0) && cdiEnabled?? && cdiEnabled == true>
                            <f:converter binding="${r"#{"}${entityDescriptor.valuesConverter}${r"}"}"/>
<#else>
                            <f:converter converterId="${entityDescriptor.valuesConverter}"/>
</#if>
                        </h:outputText>
            </#if>
        <#elseif entityDescriptor.relationshipMany>
          <#if entityDescriptor.relationshipOwner>
                        <h:selectManyMenu<#if (jsfVersion.compareTo("2.2") >= 0)> converter="${entityDescriptor.valuesConverter}"</#if>>
                            <f:selectItems value="${r"#{"}${entityDescriptor.name}${r"}"}"
                                           var="${entityDescriptor.id?replace(".","_")}Item"
                                           itemValue="${r"#{"}${entityDescriptor.id?replace(".","_")}Item${r"}"}"
            <#if relationLabelName != "">
                                           itemLabel="${r"#{"}${entityDescriptor.id?replace(".","_")}Item.${relationLabelName}${r".toString()}"}"
            </#if>
                            />
                            <#if (jsfVersion.compareTo("2.2") < 0)><f:converter binding="${r"#{"}${entityDescriptor.valuesConverter}${r"}"}"/></#if>
                        </h:selectManyMenu>
          </#if>
        <#else>
                        <h:outputText value="${r"#{"}${entityDescriptor.name}${r"}"}" title="${r"#{"}${bundle}.${crud}${entityName}Title_${entityDescriptor.id?replace(".","_")}${r"}"}"/>
        </#if>