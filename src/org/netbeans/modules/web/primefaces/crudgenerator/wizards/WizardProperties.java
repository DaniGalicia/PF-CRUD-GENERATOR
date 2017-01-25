/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2010 Oracle and/or its affiliates. All rights reserved.
 *
 * Oracle and Java are registered trademarks of Oracle and/or its affiliates.
 * Other names may be trademarks of their respective owners.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common
 * Development and Distribution License("CDDL") (collectively, the
 * "License"). You may not use this file except in compliance with the
 * License. You can obtain a copy of the License at
 * http://www.netbeans.org/cddl-gplv2.html
 * or nbbuild/licenses/CDDL-GPL-2-CP. See the License for the
 * specific language governing permissions and limitations under the
 * License.  When distributing the software, include this License Header
 * Notice in each file and include the License file at
 * nbbuild/licenses/CDDL-GPL-2-CP.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the GPL Version 2 section of the License file that
 * accompanied this code. If applicable, add the following below the
 * License Header, with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 *
 * Contributor(s):
 *
 * The Original Software is NetBeans. The Initial Developer of the Original
 * Software is Sun Microsystems, Inc. Portions Copyright 1997-2006 Sun
 * Microsystems, Inc. All Rights Reserved.
 *
 * If you wish your version of this file to be governed by only the CDDL
 * or only the GPL Version 2, indicate your decision by adding
 * "[Contributor] elects to include this software in this distribution
 * under the [CDDL or GPL Version 2] license." If you do not indicate a
 * single choice of license, a recipient has the option to distribute
 * your version of this file under either the CDDL, the GPL Version 2 or
 * to extend the choice of license to its licensees as provided above.
 * However, if you add GPL Version 2 code and therefore, elected the GPL
 * Version 2 license, then the option applies only if the new code is
 * made subject to such option by the copyright holder.
 */
/*
 * Kay Wrobel elects to include this software in this distribution under the
 * GPL Version 2 license.
 */

package org.netbeans.modules.web.primefaces.crudgenerator.wizards;

public class WizardProperties {
    public static final String CONFIG_FILE = "ConfigFile"; // NOI18N
    public static final String DESCRIPTION = "Description"; // NOI18N
    public static final String NAME = "Name"; // NOI18N
    public static final String SCOPE = "Scope"; // NOI18N
    public static final String ENTITY_CLASS = "EntityClass"; //NOI18N
    public static final String JSF_FOLDER = "JSFFolder"; //NOI18N
    public static final String JSF_GI_FOLDER = "JSFGIFolder"; //NOI18N
    public static final String JSF_EI_FOLDER = "JSFEIFolder"; //NOI18N
    public static final String JSF_CLASSES_PACKAGE = "JsfClassesPackage"; //NOI18N
    public static final String JSF_CONVERTER_PACKAGE = "JsfConverterPackage"; //NOI18N
    //2013-10-04 Kay Wrobel
    public static final String JSF_VERSION = "JsfVersionNumber"; //NOI18N
    public static final String JPA_CLASSES_PACKAGE = "JpaClassesPackage"; //NOI18N
    public static final String JAVA_PACKAGE_ROOT_FILE_OBJECT = "JavaPackageRootFileObject"; //NOI18N
    public static final String AJAXIFY_JSF_CRUD = "AjaxifyJsfCrud"; //NOI18N
    public static final String LOCALIZATION_BUNDLE_NAME = "localizationBundleName"; //NOI18N
    //2013-01-08 Kay Wrobel
    public static final String SUPPORT_PRIMEFACES = "SupportPrimeFaces"; //NOI18N
    public static final String DEFAULT_DATATABLE_ROWS = "DefaultDataTableRows"; //NOI18N
    public static final String DEFAULT_DATATABLE_ROWSPERPAGETEMPLATE = "DefaultDataTableRowsPerPageTemplate"; //NOI18N
    //2013-01-23 Kay Wrobel
    public static final String PRIMEFACES_VERSION = "PrimeFacesVersion"; //NOI18N
    public static final String CDIEXT_VERSION = "cdiExtensionVersion"; //NOI18N
    //2014-05-06 Kay Wrobel
    public static final String CDIEXT_LIBRARY = "cdiExtensionLibrary"; //NOI18N
    //2013-01-25 Kay Wrobel
    public static final String SEARCH_LABEL_ARTIFACTS = "SearchLabelArtifacts"; //NOI18N
    //2013-02-09 Kay Wrobel: Add some switches for C/R/U/D
    public static final String CREATE_FUNCTION = "CreateFunction"; //NOI18N
    public static final String READ_FUNCTION = "ReadFunction"; //NOI18N
    public static final String UPDATE_FUNCTION = "UpdateFunction"; //NOI18N
    public static final String DELETE_FUNCTION = "DeleteFunction"; //NOI18N
    public static final String SORT_FUNCTION = "SortFunction"; //NOI18N
    public static final String FILTER_FUNCTION = "FilterFunction"; //NOI18N
    public static final String GROWL_MESSAGES = "GrowlMessages"; //NOI18N
    public static final String GROWL_LIFE = "GrowlLife"; //NOI18N
    public static final String TOOLTIP_MESSAGES = "TooltipMessages"; //NOI18N
    public static final String CONFIRMATION_DIALOGS = "ConfirmationDialogs"; //NOI18N
    public static final String RELATIONSHIP_NAVIGATION = "RelationshipNavigation"; //NOI18N
    public static final String CONTEXT_MENUS = "ContextMenus"; //NOI18N
    public static final String MAX_DATATABLE_COLS = "MaxDataTableColumns"; //NOI18N
    //2014-04-17 Kay Wrobel: Allow user to control where EJB injection happens
    public static final String CDI_EJB_ABSTRACT_INJECTION = "CDIEJBAbstractInjection"; //NOI18N
    //2014-05-07 Kay Wrobel: Full class name of the ViewAccessScoped annotation
    public static final String VIEW_ACCESS_SCOPED_FULL_CLASSNAME = "ViewAccessScopedFullClassName"; //NOI18N
    //2015-01-19 Kay Wrobel: Folders for PrimeFaces Mobile Pages
    public static final String JSF_MOBILE_FOLDER = "JSFMobileFolder"; //NOI18N
    public static final String JSF_MOBILE_GI_FOLDER = "JSFMobileGIFolder"; //NOI18N
    public static final String JSF_MOBILE_EI_FOLDER = "JSFMobileEIFolder"; //NOI18N
    public static final String DO_MOBILE = "DoMobile"; //NOI18N
    public static final String PREFER_LAZY_LOADING = "PreferLazyLoading"; //NOI18N
}
