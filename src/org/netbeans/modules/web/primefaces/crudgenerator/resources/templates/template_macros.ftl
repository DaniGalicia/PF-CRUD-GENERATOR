<#macro pf><#if (primeFacesVersion.compareTo("5.0") < 0)><#nested><#else>PF('<#nested>')</#if></#macro>