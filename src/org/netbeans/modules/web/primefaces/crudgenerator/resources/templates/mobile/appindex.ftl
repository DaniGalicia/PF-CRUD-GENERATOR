<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<ui:composition xmlns:ui="http://java.sun.com/jsf/facelets"
      xmlns:h="http://java.sun.com/jsf/html"
      xmlns:f="http://java.sun.com/jsf/core"
      xmlns:p="http://primefaces.org/ui"
      xmlns:pm="http://primefaces.org/mobile"
      template="${mobileTemplatePage}">
        <ui:define name="title">
            <h:outputText value="${r"#{"}${bundle}.AppName${r"}"}"></h:outputText>
        </ui:define>
        <ui:define name="body">
            <p:panel header="${r"#{"}${bundle}.Welcome${r"}"}">
                <ui:include src="${appMobileMenu}"/>
            </p:panel>

        </ui:define>

</ui:composition>