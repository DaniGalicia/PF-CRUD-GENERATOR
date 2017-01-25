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

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.ExpressionTree;
import com.sun.source.tree.ModifiersTree;
import com.sun.source.tree.Tree;
import com.sun.source.tree.TypeParameterTree;
import com.sun.source.tree.VariableTree;
import com.sun.source.util.TreePath;
import java.awt.Image;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeListener;
import org.netbeans.api.j2ee.core.Profile;
import org.netbeans.api.java.project.JavaProjectConstants;
import org.netbeans.api.java.source.CompilationController;
import org.netbeans.api.java.source.JavaSource;
import org.netbeans.api.java.source.Task;
import org.netbeans.api.java.source.TreeMaker;
import org.netbeans.api.java.source.WorkingCopy;
import org.netbeans.api.progress.aggregate.AggregateProgressFactory;
import org.netbeans.api.progress.aggregate.AggregateProgressHandle;
import org.netbeans.api.progress.aggregate.ProgressContributor;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectUtils;
import org.netbeans.api.project.SourceGroup;
import org.netbeans.api.project.Sources;
import org.netbeans.api.queries.FileEncodingQuery;
import org.netbeans.modules.j2ee.common.J2eeProjectCapabilities;
import org.netbeans.modules.j2ee.core.api.support.java.GenerationUtils;
import org.netbeans.modules.j2ee.core.api.support.java.SourceUtils;
import org.netbeans.modules.j2ee.core.api.support.wizard.Wizards;
import org.netbeans.modules.j2ee.deployment.devmodules.spi.J2eeModuleProvider;
import org.netbeans.modules.j2ee.ejbcore.ejb.wizard.jpa.dao.AppServerValidationPanel;
import org.netbeans.modules.j2ee.ejbcore.ejb.wizard.jpa.dao.EjbFacadeWizardIterator;
import org.netbeans.modules.j2ee.persistence.dd.PersistenceUtils;
import org.netbeans.modules.j2ee.persistence.dd.common.PersistenceUnit;
import org.netbeans.modules.j2ee.persistence.provider.InvalidPersistenceXmlException;
import org.netbeans.modules.j2ee.persistence.provider.ProviderUtil;
import org.netbeans.modules.j2ee.persistence.wizard.PersistenceClientEntitySelection;
import org.netbeans.modules.j2ee.persistence.wizard.Util;
import org.netbeans.modules.j2ee.persistence.wizard.fromdb.ProgressPanel;
import org.netbeans.modules.j2ee.persistence.wizard.jpacontroller.JpaControllerIterator;
import org.netbeans.modules.j2ee.persistence.wizard.jpacontroller.ProgressReporter;
import org.netbeans.modules.j2ee.persistence.wizard.jpacontroller.ProgressReporterDelegate;
import org.netbeans.modules.j2ee.persistence.wizard.unit.PersistenceUnitWizardDescriptor;
import org.netbeans.modules.j2ee.persistence.wizard.unit.PersistenceUnitWizardPanel.TableGeneration;
import org.netbeans.modules.web.api.webmodule.ExtenderController;
import org.netbeans.modules.web.api.webmodule.WebModule;
import org.netbeans.modules.web.api.webmodule.WebProjectConstants;
import org.netbeans.modules.web.beans.CdiUtil;
import org.netbeans.modules.web.jsf.JSFFrameworkProvider;
import org.netbeans.modules.web.jsf.JSFUtils;
import org.netbeans.modules.web.jsf.api.ConfigurationUtils;
import org.netbeans.modules.web.jsf.api.facesmodel.Application;
import org.netbeans.modules.web.jsf.api.facesmodel.JSFConfigModel;
import org.netbeans.modules.web.jsf.api.facesmodel.NavigationHandler;
import org.netbeans.modules.web.jsf.api.facesmodel.ResourceBundle;
import org.netbeans.modules.web.jsf.palette.JSFPaletteUtilities;
import org.netbeans.modules.web.jsf.wizards.FacesConfigIterator;
import org.netbeans.modules.web.primefaces.crudgenerator.palette.items.FromEntityBase;
import org.netbeans.modules.web.primefaces.crudgenerator.util.CustomJpaControllerUtil;
import org.netbeans.modules.web.primefaces.crudgenerator.util.StringHelper;
import org.netbeans.modules.web.primefaces.crudgenerator.util.Version;
import org.netbeans.modules.web.spi.webmodule.WebModuleExtender;
import org.netbeans.spi.project.ui.templates.support.Templates;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.WizardDescriptor;
import org.openide.cookies.SaveCookie;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileSystem;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataFolder;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.loaders.TemplateWizard;
import org.openide.util.Exceptions;
import org.openide.util.HelpCtx;
import org.openide.util.ImageUtilities;
import org.openide.util.NbBundle;
import org.openide.util.RequestProcessor;

/**
 *
 * @author Pavel Buzek
 * @author Kay Wrobel
 */
// JAN2013 - Kay - Modified for supporting extra templates for PrimeFaces JSF pages.
public class PersistenceClientIterator implements TemplateWizard.Iterator {

    static final String TEMPLATE_EDITONE = "editonefield.ftl";
    static final String TEMPLATE_VIEWONE = "viewonefield.ftl";
    static final String TEMPLATE_MACROS = "template_macros.ftl";
    static final String TEMPLATE_FOLDER = "org/netbeans/modules/web/primefaces/crudgenerator/resources/templates/"; //NOI18N
    static final String TEMPLATE_MOBILE_FOLDER = "org/netbeans/modules/web/primefaces/crudgenerator/resources/templates/mobile/"; //NOI18N
    static final String PRIMEFACES_CRUD_STYLESHEET = "pfcrud.css"; //NOI18N
    static final String PRIMEFACES_CRUD_SCRIPT = "pfcrud.js"; //NOI18N
    //2013-01-08 Kay Wrobel: PrimeFaces additions
    static final String PRIMEFACES_TEMPLATE_PAGE = "template.xhtml"; //NOI18N
    static final String RESOURCE_FOLDER = "org/netbeans/modules/web/primefaces/crudgenerator/resources/"; //NOI18N
    static final String PRIMEFACES_APPMENU_PAGE = "appmenu.xhtml"; //NOI18N
    static final String FL_RESOURCE_FOLDER = "org/netbeans/modules/web/primefaces/crudgenerator/facelets/resources/templates/"; //NOI18N
    static final String PRIMEFACES_APPINDEX_PAGE = "index.xhtml"; //NOI18N
    static final int PROGRESS_STEP_COUNT = 8;
    // 2013-01-08 Kay Wrobel: removed private scope to allow package members access
    static final String WELCOME_JSF_FL_PAGE = "index.xhtml"; //NOI18N
    static final String TEMPLATE_JSF_FL_PAGE = "template.xhtml"; //NOI18N
    private int index;
    private transient WizardDescriptor.Panel[] panels;
    static final String[] UTIL_CLASS_NAMES = {"JsfCrudELResolver", "JsfUtil", "PagingInfo"}; //NOI18N
    static final String[] UTIL_CLASS_NAMES2 = {"JsfUtil"}; //NOI18N
    static final String UTIL_FOLDER_NAME = "util"; //NOI18N
    static final String[] UTIL_CLASS_NAMES_MOBILE = {"CurrentPageActionListener", "MobilePageController", "MobilePage"}; //NOI18N
    private static final String FACADE_SUFFIX = "Facade"; //NOI18N
    private static final String FACADE_ABSTRACT = "AbstractFacade"; //NOI18N
    private static final String ABSTRACT_CONTROLLER_CLASSNAME = "AbstractController";  //NOI18N
    private static final String LAZY_ENTITY_DATA_MODEL_CLASSNAME = "LazyEntityDataModel";  //NOI18N
    private static final String LAZY_ENTITY_SORTER_CLASSNAME = "LazyEntitySorter";  //NOI18N
    private static final String ENTITY_UTILITY_CLASSNAME = "EntityUtility";  //NOI18N
    private static final String CONTROLLER_SUFFIX = "Controller";  //NOI18N
    private static final String CONVERTER_SUFFIX = "Converter";  //NOI18N
    private static final String JAVA_EXT = "java"; //NOI18N
    public static final String JSF2_GENERATOR_PROPERTY = "jsf2Generator"; // "true" if set otherwise undefined
    private static final String CSS_FOLDER = "resources/css/";  //NOI18N
    private static final String SCRIPT_FOLDER = "resources/scripts/";  //NOI18N
    private static final String PRIMEFACES_CONFIRMATION_PAGE = "confirmation.xhtml"; //NOI18N
    private static final String PRIMEFACES_MOBILE_NAVIGATION_HANDLER = "org.primefaces.mobile.application.MobileNavigationHandler"; //NOI18N
    private transient WebModuleExtender wme;
    private transient ExtenderController ec;

    @Override
    public Set instantiate(TemplateWizard wizard) throws IOException {
        final List<String> entities = (List<String>) wizard.getProperty(WizardProperties.ENTITY_CLASS);
        final String jsfFolder = (String) wizard.getProperty(WizardProperties.JSF_FOLDER);
        final String jsfGenericIncludeFolder = (String) wizard.getProperty(WizardProperties.JSF_GI_FOLDER);
        final String jsfEntityIncludeFolder = (String) wizard.getProperty(WizardProperties.JSF_EI_FOLDER);
        final Project project = Templates.getProject(wizard);
        final FileObject javaPackageRoot = (FileObject) wizard.getProperty(WizardProperties.JAVA_PACKAGE_ROOT_FILE_OBJECT);
        final String jpaControllerPkg = (String) wizard.getProperty(WizardProperties.JPA_CLASSES_PACKAGE);
        final String controllerPkg = (String) wizard.getProperty(WizardProperties.JSF_CLASSES_PACKAGE);
        final String converterPkg = (String) wizard.getProperty(WizardProperties.JSF_CONVERTER_PACKAGE);
        SourceGroup[] sgs = ProjectUtils.getSources(project).getSourceGroups(JavaProjectConstants.SOURCES_TYPE_RESOURCES);
        final FileObject resourcePackageRoot = (sgs.length > 0) ? sgs[0].getRootFolder() : javaPackageRoot;
        //2013-01-13 Kay Wrobel: Added two new fields to control how DataTable pager looks like
        final String defaultDataTableRows = (String) wizard.getProperty(WizardProperties.DEFAULT_DATATABLE_ROWS);
        final String defaultDataTableRowsPerPageTemplate = (String) wizard.getProperty(WizardProperties.DEFAULT_DATATABLE_ROWSPERPAGETEMPLATE);
        //2013-01-13 Kay Wrobel: Added support for specific versions of PrimeFaces and MyFaces CODI
        final String primeFacesVersionString = (String) wizard.getProperty(WizardProperties.PRIMEFACES_VERSION);
        final String cdiExtensionVersionString = (String) wizard.getProperty(WizardProperties.CDIEXT_VERSION);
        final Version primeFacesVersion = primeFacesVersionString.isEmpty() ? null : new Version(primeFacesVersionString);
        final Version cdiExtensionVersion = cdiExtensionVersionString.isEmpty() ? null : new Version(cdiExtensionVersionString);
        final String searchLabelArtifacts = (String) wizard.getProperty(WizardProperties.SEARCH_LABEL_ARTIFACTS);
        //2013-02-09 Kay Wrobel
        Boolean createBoolean = (Boolean) wizard.getProperty(WizardProperties.CREATE_FUNCTION);
        final boolean doCreate = createBoolean == null ? true : createBoolean;
        Boolean readBoolean = (Boolean) wizard.getProperty(WizardProperties.READ_FUNCTION);
        final boolean doRead = readBoolean == null ? true : readBoolean;
        Boolean updateBoolean = (Boolean) wizard.getProperty(WizardProperties.UPDATE_FUNCTION);
        final boolean doUpdate = updateBoolean == null ? true : updateBoolean;
        Boolean deleteBoolean = (Boolean) wizard.getProperty(WizardProperties.DELETE_FUNCTION);
        final boolean doDelete = deleteBoolean == null ? true : deleteBoolean;
        Boolean sortBoolean = (Boolean) wizard.getProperty(WizardProperties.SORT_FUNCTION);
        final boolean doSort = sortBoolean == null ? true : sortBoolean;
        Boolean filterBoolean = (Boolean) wizard.getProperty(WizardProperties.FILTER_FUNCTION);
        final boolean doFilter = filterBoolean == null ? true : filterBoolean;
        Boolean preferLazyLoadingBoolean = (Boolean) wizard.getProperty(WizardProperties.PREFER_LAZY_LOADING);
        final boolean preferLazyLoading = preferLazyLoadingBoolean == null ? false : preferLazyLoadingBoolean;
        Boolean growlMessagesBoolean = (Boolean) wizard.getProperty(WizardProperties.GROWL_MESSAGES);
        final boolean growlMessages = growlMessagesBoolean == null ? true : growlMessagesBoolean;
        final int growlLife = ((Integer) wizard.getProperty(WizardProperties.GROWL_LIFE));
        //2013-10-04 Kay Wrobel
        final String jsfVersionString = (String) wizard.getProperty(WizardProperties.JSF_VERSION);
        final Version jsfVersion = jsfVersionString.isEmpty() ? null : new Version(jsfVersionString);
        Boolean tooltipMessagesBoolean = (Boolean) wizard.getProperty(WizardProperties.TOOLTIP_MESSAGES);
        final boolean tooltipMessages = tooltipMessagesBoolean == null ? false : tooltipMessagesBoolean;
        Boolean confirmationDialogsBoolean = (Boolean) wizard.getProperty(WizardProperties.CONFIRMATION_DIALOGS);
        final boolean confirmationDialogs = confirmationDialogsBoolean == null ? false : confirmationDialogsBoolean;
        Boolean relationshipNavigationBoolean = (Boolean) wizard.getProperty(WizardProperties.RELATIONSHIP_NAVIGATION);
        final boolean relationshipNavigation = relationshipNavigationBoolean == null ? false : relationshipNavigationBoolean;
        Boolean contextMenusBoolean = (Boolean) wizard.getProperty(WizardProperties.CONTEXT_MENUS);
        final boolean contextMenus = contextMenusBoolean == null ? false : contextMenusBoolean;
        String maxTableColsString = (String) wizard.getProperty(WizardProperties.MAX_DATATABLE_COLS);
        final int maxTableCols = Integer.parseInt(maxTableColsString);
        Boolean injectAbstractEJBBoolean = (Boolean) wizard.getProperty(WizardProperties.CDI_EJB_ABSTRACT_INJECTION);
        final boolean injectAbstractEJB = injectAbstractEJBBoolean == null ? false : injectAbstractEJBBoolean;
        //2014-05-07 Kay Wrobel
        final String viewAccessScopedFullClassName = (String) wizard.getProperty(WizardProperties.VIEW_ACCESS_SCOPED_FULL_CLASSNAME);
        //2015-01-19 Kay Wrobel: Mobile support
        final String jsfMobileFolder = (String) wizard.getProperty(WizardProperties.JSF_MOBILE_FOLDER);
        final String jsfMobileGenericIncludeFolder = (String) wizard.getProperty(WizardProperties.JSF_MOBILE_GI_FOLDER);
        final String jsfMobileEntityIncludeFolder = (String) wizard.getProperty(WizardProperties.JSF_MOBILE_EI_FOLDER);
        Boolean doMobileBoolean = (Boolean) wizard.getProperty(WizardProperties.DO_MOBILE);
        final boolean doMobile = doMobileBoolean == null ? false : doMobileBoolean;

        // add framework to project first:
        WebModule wm = WebModule.getWebModule(project.getProjectDirectory());
        JSFFrameworkProvider fp = new JSFFrameworkProvider();
        if (!fp.isInWebModule(wm)) {    //add jsf if not already present
            updateWebModuleExtender(project, wm, fp);
            wme.extend(wm);
        }

        Preferences preferences = ProjectUtils.getPreferences(project, ProjectUtils.class, true);
        final String preferredLanguage = preferences.get("jsf.language", "JSP");  //NOI18N

        final boolean jsf2Generator = "true".equals(wizard.getProperty(JSF2_GENERATOR_PROPERTY)) && "Facelets".equals(preferredLanguage);   //NOI18N
        final String bundleName = (String) wizard.getProperty(WizardProperties.LOCALIZATION_BUNDLE_NAME);

        boolean createPersistenceUnit = (Boolean) wizard.getProperty(org.netbeans.modules.j2ee.persistence.wizard.WizardProperties.CREATE_PERSISTENCE_UNIT);

        if (createPersistenceUnit) {
            PersistenceUnitWizardDescriptor puPanel = (PersistenceUnitWizardDescriptor) (panels[panels.length - 1] instanceof PersistenceUnitWizardDescriptor ? panels[panels.length - 1] : null);
            if (puPanel != null) {
                PersistenceUnit punit = Util.buildPersistenceUnitUsingData(project, puPanel.getPersistenceUnitName(), puPanel.getDBResourceSelection(), TableGeneration.NONE, puPanel.getSelectedProvider());
                ProviderUtil.setTableGeneration(punit, puPanel.getTableGeneration(), puPanel.getSelectedProvider());
                if (punit != null) {
                    Util.addPersistenceUnitToProject(project, punit);
                }
            }
        }

        final CustomJpaControllerUtil.CustomEmbeddedPkSupport embeddedPkSupport = new CustomJpaControllerUtil.CustomEmbeddedPkSupport();

        final String title = NbBundle.getMessage(PersistenceClientIterator.class, "TITLE_Progress_Jsf_Pages"); //NOI18N
        final ProgressContributor progressContributor = AggregateProgressFactory.createProgressContributor(title);
        final AggregateProgressHandle handle
                = AggregateProgressFactory.createHandle(title, new ProgressContributor[]{progressContributor}, null, null);
        final ProgressPanel progressPanel = new ProgressPanel();
        final JComponent progressComponent = AggregateProgressFactory.createProgressComponent(handle);

        final ProgressReporter reporter = new ProgressReporterDelegate(
                progressContributor, progressPanel);

        final Runnable r = new Runnable() {
            public void run() {
                final boolean genSessionBean = J2eeProjectCapabilities.forProject(project).isEjb31LiteSupported();
                try {
                    javaPackageRoot.getFileSystem().runAtomicAction(new FileSystem.AtomicAction() {
                        public void run() throws IOException {
                            handle.start();
                            int jpaProgressStepCount = genSessionBean ? EjbFacadeWizardIterator.getProgressStepCount(entities.size()) : JpaControllerIterator.getProgressStepCount(entities.size());
                            int progressStepCount = jpaProgressStepCount + getProgressStepCount();
                            progressStepCount += ((jsf2Generator ? 5 : PersistenceClientIterator.PROGRESS_STEP_COUNT) * entities.size());
                            progressContributor.start(progressStepCount);
                            FileObject jpaControllerPackageFileObject = FileUtil.createFolder(javaPackageRoot, jpaControllerPkg.replace('.', '/'));
                            if (genSessionBean) {
                                EjbFacadeWizardIterator.generateSessionBeans(progressContributor, progressPanel, entities, project, jpaControllerPkg, jpaControllerPackageFileObject, false, false, null, null, true);
                                //2015-07-06 Kay Wrobel: Add support for lazy-loading
                                addCustomFacadeMethods(progressContributor, progressPanel, project, jpaControllerPkg, jpaControllerPackageFileObject);
                                //TODO: also add the custom LazyDataModel class to facade package
                            } else {
//                                assert !jsf2Generator : "jsf2 generator works only with EJBs";
                                JpaControllerIterator.generateJpaControllers(reporter,
                                        entities, project, jpaControllerPkg,
                                        jpaControllerPackageFileObject,
                                        embeddedPkSupport, false);
                            }
                            FileObject jsfControllerPackageFileObject = FileUtil.createFolder(javaPackageRoot, controllerPkg.replace('.', '/'));
                            FileObject jsfConverterPackageFileObject = FileUtil.createFolder(javaPackageRoot, converterPkg.replace('.', '/'));
                            // 2013-01-17 Kay Wrobel: removed calls to regular JSF generators since this is a fork specifically for PrimeFaces
                            Sources srcs = ProjectUtils.getSources(project);
                            SourceGroup sgWeb[] = srcs.getSourceGroups(WebProjectConstants.TYPE_DOC_ROOT);
                            FileObject webRoot = sgWeb[0].getRootFolder();
                            generatePrimeFacesControllers(progressContributor, progressPanel, jsfControllerPackageFileObject, controllerPkg, jsfConverterPackageFileObject, converterPkg, jpaControllerPkg, entities, project, jsfFolder, jsfGenericIncludeFolder, jsfEntityIncludeFolder, jpaControllerPackageFileObject, embeddedPkSupport, genSessionBean, jpaProgressStepCount, webRoot, bundleName, javaPackageRoot, resourcePackageRoot, defaultDataTableRows, defaultDataTableRowsPerPageTemplate, primeFacesVersion, cdiExtensionVersion, jsfVersion, searchLabelArtifacts, doCreate, doRead, doUpdate, doDelete, doSort, doFilter, preferLazyLoading, growlMessages, growlLife, tooltipMessages, confirmationDialogs, relationshipNavigation, contextMenus, maxTableCols, injectAbstractEJB, viewAccessScopedFullClassName, doMobile, jsfMobileFolder, jsfMobileGenericIncludeFolder, jsfMobileEntityIncludeFolder);
                            PersistenceUtils.logUsage(PersistenceClientIterator.class, "USG_PERSISTENCE_JSF", new Object[]{entities.size(), preferredLanguage});
                            progressContributor.progress(progressStepCount);
                        }
                    });
                } catch (IOException ioe) {
                    Logger.getLogger(PersistenceClientIterator.class.getName()).log(Level.INFO, null, ioe);
                    NotifyDescriptor nd = new NotifyDescriptor.Message(ioe.getLocalizedMessage(), NotifyDescriptor.ERROR_MESSAGE);
                    DialogDisplayer.getDefault().notify(nd);
                } finally {
                    progressContributor.finish();
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            progressPanel.close();
                        }
                    });
                    handle.finish();
                }
            }
        };

        // Ugly hack ensuring the progress dialog opens after the wizard closes. Needed because:
        // 1) the wizard is not closed in the AWT event in which instantiate() is called.
        //    Instead it is closed in an event scheduled by SwingUtilities.invokeLater().
        // 2) when a modal dialog is created its owner is set to the foremost modal
        //    dialog already displayed (if any). Because of #1 the wizard will be
        //    closed when the progress dialog is already open, and since the wizard
        //    is the owner of the progress dialog, the progress dialog is closed too.
        // The order of the events in the event queue:
        // -  this event
        // -  the first invocation event of our runnable
        // -  the invocation event which closes the wizard
        // -  the second invocation event of our runnable
        SwingUtilities.invokeLater(new Runnable() {
            private boolean first = true;

            public void run() {
                if (!first) {
                    RequestProcessor.getDefault().post(r);
                    progressPanel.open(progressComponent, title);
                } else {
                    first = false;
                    SwingUtilities.invokeLater(this);
                }
            }
        });

        return Collections.singleton(DataFolder.findFolder(javaPackageRoot));
    }

    private static int getProgressStepCount() {
        int count = UTIL_CLASS_NAMES2.length;
        return count;
    }

    public static boolean doesSomeFileExistAlready(FileObject javaPackageRoot, FileObject webRoot,
            String jpaControllerPkg, String jsfControllerPkg, String jsfConverterPkg, String jsfFolder, String jsfGenericIncludeFolder, String jsfEntityIncludeFolder, List<String> entities,
            String bundleName) {
        for (String entity : entities) {
            String simpleControllerName = getFacadeFileName(entity);
            String pkg = jpaControllerPkg;
            if (pkg.length() > 0) {
                pkg += ".";
            }
            if (javaPackageRoot.getFileObject((pkg + simpleControllerName).replace('.', '/') + ".java") != null) {
                return true;
            }
            simpleControllerName = getControllerFileName(entity);
            pkg = jsfControllerPkg;
            if (pkg.length() > 0) {
                pkg += ".";
            }
            if (javaPackageRoot.getFileObject((pkg + simpleControllerName).replace('.', '/') + ".java") != null) {
                return true;
            }
            //2013-02-02 Kay Wrobel: Test for converters as well
            String simpleConverterName = getConverterFileName(entity);
            String converterPkg = jsfControllerPkg;
            if (converterPkg.length() > 0) {
                converterPkg += ".";
            }
            if (javaPackageRoot.getFileObject((converterPkg + simpleConverterName).replace('.', '/') + ".java") != null) {
                return true;
            }
            String fileName = getJsfFileName(entity, jsfFolder, "");
            // For regular JSF
            if (webRoot.getFileObject(fileName + "index.xhtml") != null) {
                return true;
            }
            fileName = getJsfFileName(entity, jsfEntityIncludeFolder, "");
            // For Generic Include JSFs
            if (webRoot.getFileObject(fileName + "appmenu.xhtml") != null
                    || webRoot.getFileObject(fileName + "template.xhtml") != null) {
                return true;
            }
            // For Entity Include JSFs
            if (webRoot.getFileObject(fileName + "View.xhtml") != null
                    || webRoot.getFileObject(fileName + "Edit.xhtml") != null
                    || webRoot.getFileObject(fileName + "List.xhtml") != null
                    || webRoot.getFileObject(fileName + "Create.xhtml") != null) {
                return true;
            }
        }
        bundleName = getBundleFileName(bundleName);
        return javaPackageRoot.getFileObject(bundleName) != null;
    }

    private static void addCustomFacadeMethods(ProgressContributor progressContributor, ProgressPanel progressPanel, Project project, String jpaControllerPackage, FileObject jpaControllerPackageFileObject) throws IOException {
        Task<CompilationController> waiter = null;
        final String afName = jpaControllerPackage + "." + FACADE_ABSTRACT; //NOI18N
        FileObject afFO = jpaControllerPackageFileObject.getFileObject(FACADE_ABSTRACT, "java");
        JavaSource source = JavaSource.forFileObject(afFO);

        if (source != null) {
            source.runModificationTask(new Task<WorkingCopy>() {
                @Override
                public void run(WorkingCopy workingCopy) throws Exception {
                    workingCopy.toPhase(JavaSource.Phase.RESOLVED);
                    ClassTree classTree = SourceUtils.getPublicTopLevelTree(workingCopy);
                    assert classTree != null;
                    CompilationUnitTree cut = workingCopy.getCompilationUnit();
                    TreeMaker maker = workingCopy.getTreeMaker();
                    GenerationUtils genUtils = GenerationUtils.newInstance(workingCopy);
                    TreePath classTreePath = workingCopy.getTrees().getPath(workingCopy.getCompilationUnit(), classTree);
                    TypeElement classElement = (TypeElement) workingCopy.getTrees().getElement(classTreePath);

                    for (Tree typeDecl : cut.getTypeDecls()) {
                        if (Tree.Kind.CLASS == typeDecl.getKind()) {
                            ClassTree clazz = (ClassTree) typeDecl;
                            List<Tree> members = new ArrayList<>();
                            
                            // First method: findRange with one sort field and sort order
                            ModifiersTree methodModifiers = genUtils.createModifiers(Modifier.PUBLIC);
                            Tree returnType = genUtils.createType("java.util.List<T>", classElement);

                            List<VariableTree> parameters = new ArrayList<>();
                            parameters.add(genUtils.createVariable("first", genUtils.createType("int", classElement)));
                            parameters.add(genUtils.createVariable("pageSize", genUtils.createType("int", classElement)));
                            parameters.add(genUtils.createVariable("sortField", genUtils.createType("String", classElement)));
                            parameters.add(genUtils.createVariable("sortOrder", genUtils.createType("String", classElement)));
                            parameters.add(genUtils.createVariable("filters", genUtils.createType("java.util.Map<String, Object>", classElement)));
                            String methodBody = "{"
                                    + "        javax.persistence.criteria.CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();\n"
                                    + "        javax.persistence.criteria.CriteriaQuery cq = cb.createQuery();\n"
                                    + "        javax.persistence.criteria.Root<T> entityRoot = cq.from(entityClass);\n"
                                    + "        cq.select(entityRoot);\n"
                                    + "\n"
                                    + "        List<javax.persistence.criteria.Predicate> predicates = getPredicates(cb, entityRoot, filters);\n"
                                    + "        if (predicates.size() > 0) {\n"
                                    + "            cq.where(predicates.toArray(new javax.persistence.criteria.Predicate[]{}));\n"
                                    + "        }\n"
                                    + "\n"
                                    + "        if (sortField != null && sortField.length() > 0) {\n"
                                    + "            if (entityRoot.get(sortField) != null) {\n"
                                    + "                if (sortOrder.startsWith(\"ASC\")) {\n"
                                    + "                    cq.orderBy(cb.asc(entityRoot.get(sortField)));\n"
                                    + "                }\n"
                                    + "                if (sortOrder.startsWith(\"DESC\")) {\n"
                                    + "                    cq.orderBy(cb.desc(entityRoot.get(sortField)));\n"
                                    + "                }\n"
                                    + "            }\n"
                                    + "        }\n"
                                    + "\n"
                                    + "        javax.persistence.Query q = getEntityManager().createQuery(cq);\n"
                                    + "        q.setMaxResults(pageSize);\n"
                                    + "        q.setFirstResult(first);\n"
                                    + "        return q.getResultList();\n"
                                    + "}";

                            members.add(maker.Method(methodModifiers,
                                    "findRange",
                                    returnType,
                                    Collections.<TypeParameterTree>emptyList(),
                                    parameters,
                                    Collections.<ExpressionTree>emptyList(),
                                    methodBody,
                                    null));

                            // Second method: findRange with a map of sort fields and sort orders
                            methodModifiers = genUtils.createModifiers(Modifier.PUBLIC);
                            returnType = genUtils.createType("java.util.List<T>", classElement);

                            parameters = new ArrayList<>();
                            parameters.add(genUtils.createVariable("first", genUtils.createType("int", classElement)));
                            parameters.add(genUtils.createVariable("pageSize", genUtils.createType("int", classElement)));
                            parameters.add(genUtils.createVariable("sortFields", genUtils.createType("java.util.Map<String, String>", classElement)));
                            parameters.add(genUtils.createVariable("filters", genUtils.createType("java.util.Map<String, Object>", classElement)));
                            methodBody = "{"
                                    + "        javax.persistence.criteria.CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();\n"
                                    + "        javax.persistence.criteria.CriteriaQuery cq = cb.createQuery();\n"
                                    + "        javax.persistence.criteria.Root<T> entityRoot = cq.from(entityClass);\n"
                                    + "        cq.select(entityRoot);\n"
                                    + "\n"
                                    + "        List<javax.persistence.criteria.Predicate> predicates = getPredicates(cb, entityRoot, filters);\n"
                                    + "        if (predicates.size() > 0) {\n"
                                    + "            cq.where(predicates.toArray(new javax.persistence.criteria.Predicate[]{}));\n"
                                    + "        }\n"
                                    + "\n"
                                    + "        if (sortFields != null && !sortFields.isEmpty()) {\n"
                                    + "            for (String sortField : sortFields.keySet()) {\n"
                                    + "                if (entityRoot.get(sortField) != null) {\n"
                                    + "                    String sortOrder = sortFields.get(sortField);\n"
                                    + "                    if (sortOrder.startsWith(\"ASC\")) {\n"
                                    + "                        cq.orderBy(cb.asc(entityRoot.get(sortField)));\n"
                                    + "                    }\n"
                                    + "                    if (sortOrder.startsWith(\"DESC\")) {\n"
                                    + "                        cq.orderBy(cb.desc(entityRoot.get(sortField)));\n"
                                    + "                    }\n"
                                    + "                }\n"
                                    + "            }\n"
                                    + "        }\n"
                                    + "\n"
                                    + "        javax.persistence.Query q = getEntityManager().createQuery(cq);\n"
                                    + "        q.setMaxResults(pageSize);\n"
                                    + "        q.setFirstResult(first);\n"
                                    + "        return q.getResultList();\n"
                                    + "}";

                            members.add(maker.Method(methodModifiers,
                                    "findRange",
                                    returnType,
                                    Collections.<TypeParameterTree>emptyList(),
                                    parameters,
                                    Collections.<ExpressionTree>emptyList(),
                                    methodBody,
                                    null));

                            // Third method: count records with given filters
                            methodModifiers = genUtils.createModifiers(Modifier.PUBLIC);
                            returnType = genUtils.createType("int", classElement);

                            parameters = new ArrayList<>();
                            parameters.add(genUtils.createVariable("filters", genUtils.createType("java.util.Map<String, Object>", classElement)));
                            methodBody = "{"
                                    + "        javax.persistence.criteria.CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();\n"
                                    + "        javax.persistence.criteria.CriteriaQuery cq = cb.createQuery();\n"
                                    + "        javax.persistence.criteria.Root<T> entityRoot = cq.from(entityClass);\n"
                                    + "        cq.select(cb.count(entityRoot));\n"
                                    + "\n"
                                    + "        List<javax.persistence.criteria.Predicate> predicates = getPredicates(cb, entityRoot, filters);\n"
                                    + "        if (predicates.size() > 0) {\n"
                                    + "            cq.where(predicates.toArray(new javax.persistence.criteria.Predicate[]{}));\n"
                                    + "        }\n"
                                    + "\n"
                                    + "        javax.persistence.Query q = getEntityManager().createQuery(cq);\n"
                                    + "        return ((Long) q.getSingleResult()).intValue();\n"
                                    + "}";

                            members.add(maker.Method(methodModifiers,
                                    "count",
                                    returnType,
                                    Collections.<TypeParameterTree>emptyList(),
                                    parameters,
                                    Collections.<ExpressionTree>emptyList(),
                                    methodBody,
                                    null));

                            // Fourth method: private method that returns Criteria Query Predicates
                            methodModifiers = genUtils.createModifiers(Modifier.PRIVATE);
                            returnType = genUtils.createType("java.util.List<javax.persistence.criteria.Predicate>", classElement);

                            parameters = new ArrayList<>();
                            parameters.add(genUtils.createVariable("cb", genUtils.createType("javax.persistence.criteria.CriteriaBuilder", classElement)));
                            parameters.add(genUtils.createVariable("entityRoot", genUtils.createType("javax.persistence.criteria.Root<T>", classElement)));
                            parameters.add(genUtils.createVariable("filters", genUtils.createType("java.util.Map<String, Object>", classElement)));
                            methodBody = "{\n" +
                                    "\n" +
                                    "        javax.persistence.metamodel.Metamodel entityModel = this.getEntityManager().getMetamodel();\n" +
                                    "        javax.persistence.metamodel.ManagedType<T> entityType = entityModel.managedType(entityClass);\n" +
                                    "        java.util.Set<javax.persistence.metamodel.EmbeddableType<?>> embeddables = entityModel.getEmbeddables();\n" +
                                    "        String fieldTypeName = null;\n" +
                                    "\n" +
                                    "        // Add predicates (WHERE clauses) based on filters map\n" +
                                    "        List<javax.persistence.criteria.Predicate> predicates = new java.util.ArrayList<>();\n" +
                                    "        for (String s : filters.keySet()) {\n" +
                                    "\n" +
                                    "            javax.persistence.criteria.Path<Object> pkFieldPath = null;\n" +
                                    "            if (s.contains(\".\")) {\n" +
                                    "                String embeddedIdField = s.split(\"\\\\.\")[0];\n" +
                                    "                String embeddedIdMember = s.split(\"\\\\.\")[1];\n" +
                                    "                pkFieldPath = entityRoot.get(embeddedIdField).get(embeddedIdMember);\n" +
                                    "                javax.persistence.metamodel.EmbeddableType<?> embeddableType = entityModel.embeddable(entityType.getAttribute(embeddedIdField).getJavaType());\n" +
                                    "                fieldTypeName = embeddableType.getAttribute(embeddedIdMember).getJavaType().getName();\n" +
                                    "            } else {\n" +
                                    "                pkFieldPath = entityRoot.get(s);\n" +
                                    "                fieldTypeName = entityType.getAttribute(s).getJavaType().getName();\n" +
                                    "            }\n" +
                                    "\n" +
                                    "            if (pkFieldPath != null && fieldTypeName != null) {\n" +
                                    "                if (fieldTypeName.contains(\"String\")) {\n" +
                                    "                    predicates.add(cb.like((javax.persistence.criteria.Expression) pkFieldPath, filters.get(s) + \"%\"));\n" +
                                    "                } else {\n" +
                                    "                    javax.persistence.criteria.Expression<?> filterExpression = getCastExpression((String) filters.get(s), fieldTypeName, cb);\n" +
                                    "                    if (filterExpression != null) {\n" +
                                    "                        predicates.add(cb.equal((javax.persistence.criteria.Expression<?>) pkFieldPath, filterExpression));\n" +
                                    "\n" +
                                    "                    } else {\n" +
                                    "                        predicates.add(cb.equal((javax.persistence.criteria.Expression<?>) pkFieldPath, filters.get(s)));\n" +
                                    "                    }\n" +
                                    "                }\n" +
                                    "            }\n" +
                                    "        }\n" +
                                    "        return predicates;\n" +
                                    "    }";

                            members.add(maker.Method(methodModifiers,
                                    "getPredicates",
                                    returnType,
                                    Collections.<TypeParameterTree>emptyList(),
                                    parameters,
                                    Collections.<ExpressionTree>emptyList(),
                                    methodBody,
                                    null));

                            // Fourth method: private method that returns Criteria Query Predicates
                            methodModifiers = genUtils.createModifiers(Modifier.PRIVATE);
                            returnType = genUtils.createType("javax.persistence.criteria.Expression<?>", classElement);

                            parameters = new ArrayList<>();
                            parameters.add(genUtils.createVariable("searchValue", genUtils.createType("String", classElement)));
                            parameters.add(genUtils.createVariable("typeName", genUtils.createType("String", classElement)));
                            parameters.add(genUtils.createVariable("cb", genUtils.createType("javax.persistence.criteria.CriteriaBuilder", classElement)));
                            methodBody = "{\n" +
                                    "        javax.persistence.criteria.Expression<?> expression = null;\n" +
                                    "        switch (typeName) {\n" +
                                    "            case \"short\":\n" +
                                    "                expression = cb.literal(Short.parseShort(searchValue));\n" +
                                    "                break;\n" +
                                    "            case \"byte\":\n" +
                                    "                expression = cb.literal(Byte.parseByte(searchValue));\n" +
                                    "                break;\n" +
                                    "            case \"int\":\n" +
                                    "                expression = cb.literal(Integer.parseInt(searchValue));\n" +
                                    "                break;\n" +
                                    "            case \"long\":\n" +
                                    "                expression = cb.literal(Long.parseLong(searchValue));\n" +
                                    "                break;\n" +
                                    "            case \"float\":\n" +
                                    "                expression = cb.literal(Float.parseFloat(searchValue));\n" +
                                    "                break;\n" +
                                    "            case \"double\":\n" +
                                    "                expression = cb.literal(Double.parseDouble(searchValue));\n" +
                                    "                break;\n" +
                                    "            case \"boolean\":\n" +
                                    "                expression = cb.literal(Boolean.parseBoolean(searchValue));\n" +
                                    "                break;\n" +
                                    "            default:\n" +
                                    "                break;\n" +
                                    "        }\n" +
                                    "        return expression;\n" +
                                    "    }";

                            members.add(maker.Method(methodModifiers,
                                    "getCastExpression",
                                    returnType,
                                    Collections.<TypeParameterTree>emptyList(),
                                    parameters,
                                    Collections.<ExpressionTree>emptyList(),
                                    methodBody,
                                    null));

                            // Add the new methods to the class
                            ClassTree modifiedClazz = null;
                            for (Tree member : members) {
                                if (modifiedClazz == null) {
                                    modifiedClazz = maker.addClassMember(clazz, member);
                                } else {
                                    modifiedClazz = maker.addClassMember(modifiedClazz, member);
                                }
                            }

                            if (modifiedClazz != null) {
                                workingCopy.rewrite(clazz, modifiedClazz);
                            }
                        }
                    }
                }
            }).commit();

            waiter = new Task<CompilationController>() {
                @Override
                public void run(CompilationController cc) throws Exception {
                    cc.toPhase(JavaSource.Phase.ELEMENTS_RESOLVED);
                }
            };

        }

    }
    
    private static void generatePrimeFacesControllers(
            ProgressContributor progressContributor,
            final ProgressPanel progressPanel,
            String jpaControllerPkg,
            Project project,
            FileObject jpaControllerPackageFileObject,
            int progressIndex) throws IOException {
        
    }

    //2013-01-08 Kay Wrobel:
    //NEW! Generate everything, but with PrimeFaces Widgets. Managed Beans
    //are also tailored for PrimeFaces, hence the complete duplication of
    //generateJsfControllers2 but with variances.
    private static void generatePrimeFacesControllers(
            ProgressContributor progressContributor,
            final ProgressPanel progressPanel,
            FileObject controllerTargetFolder,
            String controllerPkg,
            FileObject converterTargetFolder,
            String converterPkg,
            String jpaControllerPkg,
            List<String> entities,
            Project project,
            String jsfFolder,
            String jsfGenericIncludeFolder,
            String jsfEntityIncludeFolder,
            FileObject jpaControllerPackageFileObject,
            CustomJpaControllerUtil.CustomEmbeddedPkSupport embeddedPkSupport,
            boolean genSessionBean,
            int progressIndex,
            FileObject webRoot,
            String bundleName,
            FileObject javaPackageRoot,
            FileObject resourcePackageRoot,
            String defaultDataTableRows,
            String defaultDataTableRowsPerPageTemplate,
            Version primeFacesVersion,
            Version cdiExtensionVersion,
            Version jsfVersion,
            String searchLabelArtifacts,
            boolean doCreate,
            boolean doRead,
            boolean doUpdate,
            boolean doDelete,
            boolean doSort,
            boolean doFilter,
            boolean preferLazyLoading,
            boolean growlMessages,
            int growlLife,
            boolean tooltipMessages,
            boolean confirmationDialogs,
            boolean relationshipNavigation,
            boolean contextMenus,
            int maxTableCols,
            boolean injectAbstractEJB,
            String viewAccessScopedFullClassName,
            boolean doMobile,
            String jsfMobileFolder,
            String jsfMobileGenericIncludeFolder,
            String jsfMobileEntityIncludeFolder) throws IOException {
        String progressMsg;

        if (jsfGenericIncludeFolder.length() == 0) {
            jsfGenericIncludeFolder = "/";
        }
        if (jsfEntityIncludeFolder.length() == 0) {
            jsfEntityIncludeFolder = "/";
        }
        if (jsfMobileFolder.length() == 0) {
            jsfMobileFolder = "/mobile";
        }
        if (jsfMobileGenericIncludeFolder.length() == 0) {
            jsfMobileGenericIncludeFolder = "/mobile";
        }
        if (jsfMobileEntityIncludeFolder.length() == 0) {
            jsfMobileEntityIncludeFolder = "/mobile";
        }

        //copy util classes
        FileObject utilFolder = controllerTargetFolder.getFileObject(UTIL_FOLDER_NAME);
        if (utilFolder == null) {
            utilFolder = FileUtil.createFolder(controllerTargetFolder, UTIL_FOLDER_NAME);
        }
        String controllerUtilPkg = controllerPkg == null || controllerPkg.length() == 0 ? UTIL_FOLDER_NAME : controllerPkg + "." + UTIL_FOLDER_NAME;
        for (String UTIL_CLASS_NAMES21 : UTIL_CLASS_NAMES2) {
            if (utilFolder.getFileObject(UTIL_CLASS_NAMES21, JAVA_EXT) == null) {
                progressMsg = NbBundle.getMessage(org.netbeans.modules.web.jsf.wizards.PersistenceClientIterator.class, "MSG_Progress_Jsf_Now_Generating", UTIL_CLASS_NAMES21 + "." + JAVA_EXT); //NOI18N
                progressContributor.progress(progressMsg, progressIndex++);
                progressPanel.setText(progressMsg);
                FileObject tableTemplate = FileUtil.getConfigRoot().getFileObject(PersistenceClientSetupPanelVisual.PRIMEFACES_TEMPLATE_PATH + UTIL_CLASS_NAMES21 + ".ftl");
                FileObject target = FileUtil.createData(utilFolder, UTIL_CLASS_NAMES21 + "." + JAVA_EXT); //NOI18N
                HashMap<String, Object> params = new HashMap<>();
                params.put("packageName", controllerUtilPkg);
                params.put("comment", Boolean.FALSE); // NOI18N
                JSFPaletteUtilities.expandJSFTemplate(tableTemplate, params, target);
            } else {
                progressContributor.progress(progressIndex++);
            }
        }

        // Copy some mobile util classes as well if mobile and relationship navigation are done
        if (doMobile && relationshipNavigation) {
            for (String UTIL_CLASS_NAME_MOBILE : UTIL_CLASS_NAMES_MOBILE) {
                if (utilFolder.getFileObject(UTIL_CLASS_NAME_MOBILE, JAVA_EXT) == null) {
                    progressMsg = NbBundle.getMessage(org.netbeans.modules.web.jsf.wizards.PersistenceClientIterator.class, "MSG_Progress_Jsf_Now_Generating", UTIL_CLASS_NAME_MOBILE + "." + JAVA_EXT); //NOI18N
                    progressContributor.progress(progressMsg, progressIndex++);
                    progressPanel.setText(progressMsg);
                    FileObject tableTemplate = FileUtil.getConfigRoot().getFileObject(PersistenceClientSetupPanelVisual.PRIMEFACES_MOBILE_TEMPLATE_PATH + UTIL_CLASS_NAME_MOBILE.toLowerCase() + ".ftl");
                    FileObject target = FileUtil.createData(utilFolder, UTIL_CLASS_NAME_MOBILE + "." + JAVA_EXT); //NOI18N
                    HashMap<String, Object> params = new HashMap<>();
                    params.put("packageName", controllerUtilPkg);
                    params.put("comment", Boolean.FALSE); // NOI18N
                    params.put("cdiEnabled", isCdiEnabled(project));
                    if (jsfMobileFolder.length() > 0) {
                        if (jsfMobileFolder.startsWith("/")) {
                            params.put("jsfMobileFolder", jsfMobileFolder);
                        } else {
                            params.put("jsfMobileFolder", "/" + jsfMobileFolder);
                        }
                    } else {
                        params.put("jsfMobileFolder", "mobile");
                    }
                    JSFPaletteUtilities.expandJSFTemplate(tableTemplate, params, target);
                } else {
                    progressContributor.progress(progressIndex++);
                }
            }
        }

        //2013-02-11 Kay Wrobel: Retrieve the servlet mapping from the web.xml file
        WebModule wm = WebModule.getWebModule(project.getProjectDirectory());
        String servletMapping = getServletMapping(wm);

        //Create a EntityUtility class file
        FileObject entityHelperFileObject;
        entityHelperFileObject = jpaControllerPackageFileObject.getFileObject(ENTITY_UTILITY_CLASSNAME, JAVA_EXT);
        if (entityHelperFileObject == null) {
            entityHelperFileObject = jpaControllerPackageFileObject.createData(ENTITY_UTILITY_CLASSNAME, JAVA_EXT);
        }

        //Create a LazyEntityDataModel class file
        FileObject lazyEntityDataModelFileObject;
        lazyEntityDataModelFileObject = jpaControllerPackageFileObject.getFileObject(LAZY_ENTITY_DATA_MODEL_CLASSNAME, JAVA_EXT);
        if (lazyEntityDataModelFileObject == null) {
            lazyEntityDataModelFileObject = jpaControllerPackageFileObject.createData(LAZY_ENTITY_DATA_MODEL_CLASSNAME, JAVA_EXT);
        }

        //Create a LazyEntitySorter class file
        FileObject lazyEntitySorterFileObject;
        lazyEntitySorterFileObject = jpaControllerPackageFileObject.getFileObject(LAZY_ENTITY_SORTER_CLASSNAME, JAVA_EXT);
        if (lazyEntitySorterFileObject == null) {
            lazyEntitySorterFileObject = jpaControllerPackageFileObject.createData(LAZY_ENTITY_SORTER_CLASSNAME, JAVA_EXT);
        }

        //Create an abstract controller class file
        FileObject abstractControllerFileObject;
        abstractControllerFileObject = controllerTargetFolder.getFileObject(ABSTRACT_CONTROLLER_CLASSNAME, JAVA_EXT);
        if (abstractControllerFileObject == null) {
            abstractControllerFileObject = controllerTargetFolder.createData(ABSTRACT_CONTROLLER_CLASSNAME, JAVA_EXT);
        }

        //int[] nameAttemptIndices = new int[entities.size()];
        FileObject[] controllerFileObjects = new FileObject[entities.size()];
        for (int i = 0; i < controllerFileObjects.length; i++) {
            String simpleControllerName = getControllerFileName(entities.get(i));
            controllerFileObjects[i] = controllerTargetFolder.getFileObject(simpleControllerName, JAVA_EXT);
            if (controllerFileObjects[i] == null) {
                controllerFileObjects[i] = controllerTargetFolder.createData(simpleControllerName, JAVA_EXT);
            }
        }

        FileObject[] converterFileObjects = new FileObject[entities.size()];
        for (int i = 0; i < converterFileObjects.length; i++) {
            String simpleConverterName = getConverterFileName(entities.get(i));
            converterFileObjects[i] = converterTargetFolder.getFileObject(simpleConverterName, JAVA_EXT);
            if (converterFileObjects[i] == null) {
                converterFileObjects[i] = converterTargetFolder.createData(simpleConverterName, JAVA_EXT);
            }
        }

        // Generate main stylesheet file + folder
        Charset encoding = FileEncodingQuery.getEncoding(project.getProjectDirectory());
        if (webRoot.getFileObject(CSS_FOLDER + PRIMEFACES_CRUD_STYLESHEET) == null) {
            String content = JSFFrameworkProvider.readResource(PersistenceClientIterator.class.getClassLoader().getResourceAsStream(RESOURCE_FOLDER + PRIMEFACES_CRUD_STYLESHEET), "UTF-8"); //NOI18N
            FileObject target = FileUtil.createData(webRoot, CSS_FOLDER + PRIMEFACES_CRUD_STYLESHEET);
            JSFFrameworkProvider.createFile(target, content, encoding.name());
            progressMsg = NbBundle.getMessage(PersistenceClientIterator.class, "MSG_Progress_Jsf_Now_Generating", target.getNameExt()); //NOI18N
            progressContributor.progress(progressMsg, progressIndex++);
            progressPanel.setText(progressMsg);
        }

        // Generate main javascript file + folder
        if (webRoot.getFileObject(SCRIPT_FOLDER + PRIMEFACES_CRUD_SCRIPT) == null) {
            String content = JSFFrameworkProvider.readResource(PersistenceClientIterator.class.getClassLoader().getResourceAsStream(RESOURCE_FOLDER + PRIMEFACES_CRUD_SCRIPT), "UTF-8"); //NOI18N
            FileObject target = FileUtil.createData(webRoot, SCRIPT_FOLDER + PRIMEFACES_CRUD_SCRIPT);
            JSFFrameworkProvider.createFile(target, content, encoding.name());
            progressMsg = NbBundle.getMessage(PersistenceClientIterator.class, "MSG_Progress_Jsf_Now_Generating", target.getNameExt()); //NOI18N
            progressContributor.progress(progressMsg, progressIndex++);
            progressPanel.setText(progressMsg);
        }

        List<TemplateData> bundleData = new ArrayList<>();

        //2013-06-10 Kay Wrobel: Create a bundle variable name
        //This will be passed on to template engine!
        String bundleVar = bundleName.replaceFirst("/", "");
        bundleVar = (bundleVar.substring(0, 1).toLowerCase() + bundleVar.substring(1));

        // 2016-08-09 Kay Wrobel: Simplify code by using new static function (see below)
        jsfFolder = removeTrailingSlash(jsfFolder);
        jsfGenericIncludeFolder = removeTrailingSlash(jsfGenericIncludeFolder);
        jsfEntityIncludeFolder = removeTrailingSlash(jsfEntityIncludeFolder);
        jsfMobileFolder = removeTrailingSlash(jsfMobileFolder);
        jsfMobileGenericIncludeFolder = removeTrailingSlash(jsfMobileGenericIncludeFolder);
        jsfMobileEntityIncludeFolder = removeTrailingSlash(jsfMobileEntityIncludeFolder);

        // 2013-11-11 Kay Wrobel: 
        // To fix ticket #16, we have to avoid using <#include> directive inside
        // the templates because the template root is not universally know
        // when using NetBeans. For that reason, we will pull in the actual code
        // into a String variable and use the <@variable?interpret/> directive.
        // for dynamic code interpretation.
        String viewOneFieldTemplate = JSFFrameworkProvider.readResource(PersistenceClientIterator.class.getClassLoader().getResourceAsStream(TEMPLATE_FOLDER + TEMPLATE_VIEWONE), "UTF-8"); //NOI18N        FileObject viewOneFieldTemplate = tmpRoot.getFileObject(TEMPLATE_VIEWONE);
        String editOneFieldTemplate = JSFFrameworkProvider.readResource(PersistenceClientIterator.class.getClassLoader().getResourceAsStream(TEMPLATE_FOLDER + TEMPLATE_EDITONE), "UTF-8"); //NOI18N        FileObject editOneFieldTemplate = tmpRoot.getFileObject(TEMPLATE_EDITONE);
        String viewOneFieldMobileTemplate = JSFFrameworkProvider.readResource(PersistenceClientIterator.class.getClassLoader().getResourceAsStream(TEMPLATE_MOBILE_FOLDER + TEMPLATE_VIEWONE), "UTF-8"); //NOI18N        FileObject viewOneFieldTemplate = tmpRoot.getFileObject(TEMPLATE_VIEWONE);
        String editOneFieldMobileTemplate = JSFFrameworkProvider.readResource(PersistenceClientIterator.class.getClassLoader().getResourceAsStream(TEMPLATE_MOBILE_FOLDER + TEMPLATE_EDITONE), "UTF-8"); //NOI18N        FileObject editOneFieldTemplate = tmpRoot.getFileObject(TEMPLATE_EDITONE);
        String templateMacros = JSFFrameworkProvider.readResource(PersistenceClientIterator.class.getClassLoader().getResourceAsStream(TEMPLATE_FOLDER + TEMPLATE_MACROS), "UTF-8"); //NOI18N        FileObject editOneFieldTemplate = tmpRoot.getFileObject(TEMPLATE_MACROS);

        for (int i = 0; i < controllerFileObjects.length; i++) {
            String entityClass = entities.get(i);
            String simpleClassName = CustomJpaControllerUtil.simpleClassName(entityClass);
            String simpleJpaControllerName = simpleClassName + (genSessionBean ? FACADE_SUFFIX : "JpaController"); //NOI18N

            progressMsg = NbBundle.getMessage(PersistenceClientIterator.class, "MSG_Progress_Jsf_Now_Generating", simpleClassName + "." + JAVA_EXT); //NOI18N
            progressContributor.progress(progressMsg, progressIndex++);
            progressPanel.setText(progressMsg);

            FileObject configRoot = FileUtil.getConfigRoot();
            FileObject abstractTemplate = configRoot.getFileObject(PersistenceClientSetupPanelVisual.PRIMEFACES_ABSTRACTCONTROLLER_TEMPLATE);
            FileObject template = configRoot.getFileObject(PersistenceClientSetupPanelVisual.PRIMEFACES_CONTROLLER_TEMPLATE);
            FileObject converterTemplate = configRoot.getFileObject(PersistenceClientSetupPanelVisual.PRIMEFACES_CONVERTER_TEMPLATE);
            Map<String, Object> params = new HashMap<>();
            String controllerClassName = controllerFileObjects[i].getName();
            String converterClassName = converterFileObjects[i].getName();
            String managedBean = controllerClassName.substring(0, 1).toLowerCase() + controllerClassName.substring(1);
            //Find out if entity has relationships
            Map<String, Object> relationshipParams = FromEntityBase.createFieldParameters(webRoot, entityClass, managedBean, managedBean + ".selected", false, true, null);
            boolean hasRelationships = false; // SingleRelationships = either OneToMany or ManyToOne. We are skipping ManyToMany for now
            boolean hasParentRelationships = false; // ParentRelationships = only OneToMany, not ManyToOne or ManyToMany.
            boolean hasChildRelationships = false; // ChildRelationships = only ManyToOney, not OneToMany or ManyToMany.
            List<FromEntityBase.TemplateData> relationshipEntityDescriptors = new ArrayList<>();
            for (FromEntityBase.TemplateData relationshipEntityDescriptor : (List<FromEntityBase.TemplateData>) relationshipParams.get("entityDescriptors")) {

                if (relationshipEntityDescriptor.isRelationshipOne()) {
                    hasParentRelationships = true;
                }

                if (relationshipEntityDescriptor.isRelationshipMany()) {
                    hasChildRelationships = true;
                }

                if (hasParentRelationships || hasChildRelationships) {
                    hasRelationships = true;
                    relationshipEntityDescriptors.add(relationshipEntityDescriptor);
                }
            }
            relationshipParams = null; // Destroy

            // Some tables may have multiple relationships to the same parent or child entity
            // To avoid duplicate form includes on the index.ftl template, we have to determine
            // all the unique entities in the list to pass along just to the index.ftl.
            Map<String, FromEntityBase.TemplateData> uniqueRelationshipEntityDescriptorMap = new HashMap<>();
            for (FromEntityBase.TemplateData relationshipEntityDescriptor : relationshipEntityDescriptors) {
                if (!uniqueRelationshipEntityDescriptorMap.containsKey(relationshipEntityDescriptor.getRelationClassName())) {
                    uniqueRelationshipEntityDescriptorMap.put(relationshipEntityDescriptor.getRelationClassName(), relationshipEntityDescriptor);
                }
            }
            Collection<FromEntityBase.TemplateData> uniqueRelationshipEntityDescriptors = uniqueRelationshipEntityDescriptorMap.values();

            params.put("managedBeanName", managedBean);
            params.put("cdiEnabled", isCdiEnabled(project));
            params.put("injectAbstractEJB", injectAbstractEJB);
            params.put("controllerPackageName", controllerPkg);
            params.put("controllerUtilPackageName", controllerUtilPkg);
            params.put("controllerClassName", controllerClassName);
            params.put("abstractControllerClassName", ABSTRACT_CONTROLLER_CLASSNAME);
            params.put("entityFullClassName", entityClass);
            params.put("importEntityFullClassName", showImportStatement(controllerPkg, entityClass));
            params.put(genSessionBean ? "ejbFullClassName" : "jpaControllerFullClassName", jpaControllerPkg + "." + simpleJpaControllerName);
            params.put("importEjbFullClassName", showImportStatement(controllerPkg, jpaControllerPkg + "." + simpleJpaControllerName));
            params.put(genSessionBean ? "ejbClassName" : "jpaControllerClassName", simpleJpaControllerName);
            params.put("ejbFacadePackageName", jpaControllerPkg);
            if (genSessionBean) {
                params.put("ejbFacadeFullClassName", jpaControllerPkg + ".Abstract" + FACADE_SUFFIX);
                params.put("ejbFacadeClassName", "Abstract" + FACADE_SUFFIX);
            }
            params.put("lazyEntityDataModelFullClassName", jpaControllerPkg + "." + LAZY_ENTITY_DATA_MODEL_CLASSNAME);
            params.put("lazyEntityDataModelClassName", LAZY_ENTITY_DATA_MODEL_CLASSNAME);
            params.put("entityClassName", simpleClassName);
            params.put("comment", Boolean.FALSE); // NOI18N
            params.put("bundle", bundleName); // NOI18N
            boolean isInjected = Util.isContainerManaged(project);
            if (!genSessionBean && isInjected) {
                params.put("isInjected", true); //NOI18N
            }
            String persistenceUnitName = Util.getPersistenceUnitAsString(project, simpleClassName);
            if (persistenceUnitName != null) {
                params.put("persistenceUnitName", persistenceUnitName); //NOI18N
            }
            if (cdiExtensionVersion != null) {
                params.put("cdiExtensionVersion", cdiExtensionVersion); //NOI18N
            }
            if (viewAccessScopedFullClassName != null) {
                params.put("viewAccessScopedFullClassName", viewAccessScopedFullClassName); //NOI18N
            }
            params.put("jsfVersion", jsfVersion); //NOI18N
            params.put("doRelationshipNavigation", relationshipNavigation);
            params.put("hasRelationships", hasRelationships);
            params.put("hasParentRelationships", hasParentRelationships);
            params.put("hasChildRelationships", hasChildRelationships);
            params.put("relationshipEntityDescriptors", relationshipEntityDescriptors);
            params.put("doMobile", doMobile); // NOI18N
            if (jsfFolder.length() > 0) {
                if (jsfFolder.startsWith("/")) {
                    params.put("jsfFolder", jsfFolder);
                } else {
                    params.put("jsfFolder", "/" + jsfFolder);
                }
            } else {
                params.put("jsfFolder", "");
            }
            if (jsfMobileFolder.length() > 0) {
                if (jsfMobileFolder.startsWith("/")) {
                    params.put("jsfMobileFolder", jsfMobileFolder);
                } else {
                    params.put("jsfMobileFolder", "/" + jsfMobileFolder);
                }
            } else {
                params.put("jsfMobileFolder", "mobile");
            }
            FromEntityBase.createParamsForConverterTemplate(params, controllerTargetFolder, entityClass, embeddedPkSupport);

            //Generate LazyEntityDataModel, LazyEntitySorter and EntityUtility classes on first loop
            if (i == 0) {
                FileObject entityHelperTemplate = configRoot.getFileObject(PersistenceClientSetupPanelVisual.PRIMEFACES_ENTITY_UTILITY_TEMPLATE);
                JSFPaletteUtilities.expandJSFTemplate(entityHelperTemplate, params, entityHelperFileObject);
                FileObject lazyEntityDataModelTemplate = configRoot.getFileObject(PersistenceClientSetupPanelVisual.PRIMEFACES_LAZY_ENTITY_DATA_MODEL_TEMPLATE);
                JSFPaletteUtilities.expandJSFTemplate(lazyEntityDataModelTemplate, params, lazyEntityDataModelFileObject);
                FileObject lazyEntitySorterTemplate = configRoot.getFileObject(PersistenceClientSetupPanelVisual.PRIMEFACES_LAZY_ENTITY_SORTER_TEMPLATE);
                JSFPaletteUtilities.expandJSFTemplate(lazyEntitySorterTemplate, params, lazyEntitySorterFileObject);
            }

            //Generate abstract controller on first loop
            if (i == 0) {
                JSFPaletteUtilities.expandJSFTemplate(abstractTemplate, params, abstractControllerFileObject);
            }
            JSFPaletteUtilities.expandJSFTemplate(template, params, controllerFileObjects[i]);
            //2013-02-01 Kay Wrobel: Generate converter class
            params.put("converterPackageName", converterPkg);
            params.put("converterClassName", converterClassName);
            JSFPaletteUtilities.expandJSFTemplate(converterTemplate, params, converterFileObjects[i]);

            params = FromEntityBase.createFieldParameters(webRoot, entityClass, managedBean, managedBean + ".selected", false, true, null);
            bundleData.add(new TemplateData(simpleClassName, (List<FromEntityBase.TemplateData>) params.get("entityDescriptors")));
            params.put("controllerClassName", controllerClassName);
            params.put("converterClassName", converterClassName);
            params.put("servletMapping", servletMapping);
            params.put("primeFacesVersion", primeFacesVersion); //NOI18N
            params.put("jsfVersion", jsfVersion); //NOI18N
            params.put("searchLabels", searchLabelArtifacts); //Default property artifacts to look for
            params.put("cdiEnabled", isCdiEnabled(project));
            params.put("growlMessages", growlMessages);
            params.put("growlLife", growlLife);
            params.put("tooltipMessages", tooltipMessages);
            params.put("bundle", bundleVar); // NOI18N
            params.put("editOneFieldTemplate", editOneFieldTemplate);
            params.put("viewOneFieldTemplate", viewOneFieldTemplate);
            params.put("editOneFieldMobileTemplate", editOneFieldMobileTemplate);
            params.put("viewOneFieldMobileTemplate", viewOneFieldMobileTemplate);
            params.put("templateMacros", templateMacros);
            params.put("doConfirmationDialogs", confirmationDialogs);
            params.put("doRelationshipNavigation", relationshipNavigation);
            params.put("hasRelationships", hasRelationships);
            params.put("hasParentRelationships", hasParentRelationships);
            params.put("hasChildRelationships", hasChildRelationships);
            params.put("relationshipEntityDescriptors", relationshipEntityDescriptors);
            params.put("doContextMenus", contextMenus);

            if (doCreate) {
                expandSingleJSFTemplate("create.ftl", entityClass, jsfEntityIncludeFolder, webRoot, "Create", params, progressContributor, progressPanel, progressIndex++);
                if (doMobile) {
                    expandSingleJSFMobileTemplate("create.ftl", entityClass, jsfMobileEntityIncludeFolder, webRoot, "Create", params, progressContributor, progressPanel, progressIndex++);
                }

            }
            if (doUpdate) {
                expandSingleJSFTemplate("edit.ftl", entityClass, jsfEntityIncludeFolder, webRoot, "Edit", params, progressContributor, progressPanel, progressIndex++);
                if (doMobile) {
                    expandSingleJSFMobileTemplate("edit.ftl", entityClass, jsfMobileEntityIncludeFolder, webRoot, "Edit", params, progressContributor, progressPanel, progressIndex++);
                }
            }
            if (doRead) {
                expandSingleJSFTemplate("view.ftl", entityClass, jsfEntityIncludeFolder, webRoot, "View", params, progressContributor, progressPanel, progressIndex++);
                if (doMobile) {
                    expandSingleJSFMobileTemplate("view.ftl", entityClass, jsfMobileEntityIncludeFolder, webRoot, "View", params, progressContributor, progressPanel, progressIndex++);
                }
            }

            params = FromEntityBase.createFieldParameters(webRoot, entityClass, managedBean, managedBean + ".items", true, true, null);
            params.put("managedBeanLazyProperty", managedBean + ".lazyItems"); // NOI18N
            params.put("controllerClassName", controllerClassName);
            params.put("converterClassName", converterClassName);
            params.put("defaultDataTableRows", defaultDataTableRows);
            params.put("defaultDataTableRowsPerPageTemplate", defaultDataTableRowsPerPageTemplate);
            params.put("servletMapping", servletMapping);
            params.put("primeFacesVersion", primeFacesVersion); //NOI18N
            params.put("jsfVersion", jsfVersion); //NOI18N
            params.put("searchLabels", searchLabelArtifacts); //Default property artifacts to look for
            params.put("cdiEnabled", isCdiEnabled(project));
            params.put("doCreate", doCreate);
            params.put("doRead", doRead);
            params.put("doUpdate", doUpdate);
            params.put("doDelete", doDelete);
            params.put("doSort", doSort);
            params.put("doFilter", doFilter);
            params.put("growlMessages", growlMessages);
            params.put("growlLife", growlLife);
            params.put("tooltipMessages", tooltipMessages);
            params.put("bundle", bundleVar); // NOI18N
            params.put("doConfirmationDialogs", confirmationDialogs);
            params.put("confirmDialogPage", jsfGenericIncludeFolder + "/" + PRIMEFACES_CONFIRMATION_PAGE);
            params.put("doRelationshipNavigation", relationshipNavigation);
            params.put("hasRelationships", hasRelationships);
            params.put("hasParentRelationships", hasParentRelationships);
            params.put("hasChildRelationships", hasChildRelationships);
            params.put("relationshipEntityDescriptors", relationshipEntityDescriptors);
            params.put("uniqueRelationshipEntityDescriptors", uniqueRelationshipEntityDescriptors);
            params.put("doContextMenus", contextMenus);
            params.put("maxTableCols", maxTableCols);
            params.put("templateMacros", templateMacros);
            params.put("preferLazyLoading", preferLazyLoading);
            expandSingleJSFTemplate("list.ftl", entityClass, jsfEntityIncludeFolder, webRoot, "List", params, progressContributor, progressPanel, progressIndex++);
            if (doMobile) {
                if (jsfMobileFolder.length() > 0) {
                    if (jsfMobileFolder.startsWith("/")) {
                        params.put("jsfMobileFolder", jsfMobileFolder);
                    } else {
                        params.put("jsfMobileFolder", "/" + jsfMobileFolder);
                    }
                } else {
                    params.put("jsfMobileFolder", "mobile");
                }
                params.put("appIndex", PRIMEFACES_APPINDEX_PAGE.replace(".xhtml", ""));
                expandSingleJSFMobileTemplate("list.ftl", entityClass, jsfMobileEntityIncludeFolder, webRoot, "List", params, progressContributor, progressPanel, progressIndex++);
            }
            if (!"/".equals(jsfEntityIncludeFolder)) {
                params.put("entityIncludeFolder", jsfEntityIncludeFolder); // NOI18N
            } else {
                params.put("entityIncludeFolder", ""); // NOI18N
            }
            if (!"/mobile/".equals(jsfMobileEntityIncludeFolder)) {
                params.put("mobileEntityIncludeFolder", jsfMobileEntityIncludeFolder); // NOI18N
            } else {
                params.put("mobileEntityIncludeFolder", "/mobile"); // NOI18N
            }
            params.put("templatePage", jsfGenericIncludeFolder + "/" + PRIMEFACES_TEMPLATE_PAGE);
            params.put("mobileTemplatePage", jsfMobileGenericIncludeFolder + "/" + PRIMEFACES_TEMPLATE_PAGE);
            params.put("mobileConfirmDialogPage", jsfMobileGenericIncludeFolder + "/" + PRIMEFACES_CONFIRMATION_PAGE);
            expandSingleJSFTemplate("index.ftl", entityClass, jsfFolder, webRoot, "index", params, progressContributor, progressPanel, progressIndex++);
            if (doMobile) {
                expandSingleJSFMobileTemplate("index.ftl", entityClass, jsfMobileFolder, webRoot, "index", params, progressContributor, progressPanel, progressIndex++);
            }

        }

        progressMsg = NbBundle.getMessage(PersistenceClientIterator.class, "MSG_Progress_Jsf_Now_Generating", bundleName); //NOI18N
        progressContributor.progress(progressMsg, progressIndex++);
        progressPanel.setText(progressMsg);

        FileObject template = FileUtil.getConfigRoot().getFileObject(PersistenceClientSetupPanelVisual.PRIMEFACES_BUNDLE_TEMPLATE);
        Map<String, Object> params = new HashMap<>();
        params.put("entities", bundleData);
        params.put("comment", Boolean.FALSE);
        params.put("primeFacesVersion", primeFacesVersion); //NOI18N
        params.put("jsfVersion", jsfVersion); //NOI18N
        params.put("growlMessages", growlMessages);
        params.put("growlLife", growlLife);
        params.put("tooltipMessages", tooltipMessages);
        String bundleFileName = getBundleFileName(bundleName);
        FileObject target = resourcePackageRoot.getFileObject(bundleFileName);
        if (target == null) {
            target = FileUtil.createData(resourcePackageRoot, bundleFileName);
        }
        JSFPaletteUtilities.expandJSFTemplate(template, params, target);

        // Add PrimeFaces Application Menu to be included in main template
        // 2013-10-15 Kay Wrobel: Fix beginning slash in path information
        String jsfGenericIncludeFolder2 = jsfGenericIncludeFolder;
        if (jsfGenericIncludeFolder2.length() > 0) {
            if (jsfGenericIncludeFolder2.startsWith("/")) {
                jsfGenericIncludeFolder2 = jsfGenericIncludeFolder2.replaceFirst("/", "");
            }
        }

        template = FileUtil.getConfigRoot().getFileObject(PersistenceClientSetupPanelVisual.PRIMEFACES_APPMENU_TEMPLATE);
        target = webRoot.getFileObject(jsfGenericIncludeFolder2 + "/" + PRIMEFACES_APPMENU_PAGE);

        params.put("appIndex", PRIMEFACES_APPINDEX_PAGE.replace(".xhtml", ""));
        params.put("servletMapping", servletMapping);
        params.put("bundle", bundleVar); // NOI18N
        if (jsfFolder.length() > 0) {
            if (jsfFolder.startsWith("/")) {
                params.put("jsfFolder", jsfFolder);
            } else {
                params.put("jsfFolder", "/" + jsfFolder);
            }
        } else {
            params.put("jsfFolder", "");
        }
        params.put("doMobile", doMobile); // NOI18N
        if (doMobile) {
            if (jsfMobileFolder.length() > 0) {
                if (jsfMobileFolder.startsWith("/")) {
                    params.put("jsfMobileFolder", jsfMobileFolder);
                } else {
                    params.put("jsfMobileFolder", "/" + jsfMobileFolder);
                }
            } else {
                params.put("jsfMobileFolder", "mobile");
            }

        }
        if (target == null) {
            target = FileUtil.createData(webRoot, jsfGenericIncludeFolder2 + "/" + PRIMEFACES_APPMENU_PAGE);
        }
        JSFPaletteUtilities.expandJSFTemplate(template, params, target);

        // Add PrimeFaces Application Home Page to be overwritten as index.xhtml
        template = FileUtil.getConfigRoot().getFileObject(PersistenceClientSetupPanelVisual.PRIMEFACES_APPINDEX_TEMPLATE);
        target = webRoot.getFileObject(WELCOME_JSF_FL_PAGE);
        params.put("bundle", bundleVar); // NOI18N
        params.put("templatePage", jsfGenericIncludeFolder + "/" + PRIMEFACES_TEMPLATE_PAGE);
        if (target == null) {
            target = FileUtil.createData(webRoot, WELCOME_JSF_FL_PAGE);
        }
        JSFPaletteUtilities.expandJSFTemplate(template, params, target);

        // Add PrimeFaces Application Template Page to be overwritten
        template = FileUtil.getConfigRoot().getFileObject(PersistenceClientSetupPanelVisual.PRIMEFACES_TEMPLATE_TEMPLATE);
        target = webRoot.getFileObject(jsfGenericIncludeFolder + "/" + PRIMEFACES_TEMPLATE_PAGE);
        params.put("appMenu", jsfGenericIncludeFolder + "/" + PRIMEFACES_APPMENU_PAGE);
        params.put("styleFile", PRIMEFACES_CRUD_STYLESHEET);
        params.put("scriptFile", PRIMEFACES_CRUD_SCRIPT);
        params.put("bundle", bundleVar); // NOI18N
        if (target == null) {
            target = FileUtil.createData(webRoot, jsfGenericIncludeFolder + "/" + PRIMEFACES_TEMPLATE_PAGE);
        }
        JSFPaletteUtilities.expandJSFTemplate(template, params, target);

        if (doMobile) {
            String jsfMobileGenericIncludeFolder2 = jsfMobileGenericIncludeFolder;
            if (jsfMobileGenericIncludeFolder2.length() > 0) {
                if (jsfMobileGenericIncludeFolder2.startsWith("/")) {
                    jsfMobileGenericIncludeFolder2 = jsfMobileGenericIncludeFolder2.replaceFirst("/", "");
                }
            }

            String jsfMobileFolder2 = jsfMobileFolder;
            if (jsfMobileFolder2.length() > 0) {
                if (jsfMobileFolder2.startsWith("/")) {
                    jsfMobileFolder2 = jsfMobileFolder2.replaceFirst("/", "");
                }
            }

            template = FileUtil.getConfigRoot().getFileObject(PersistenceClientSetupPanelVisual.PRIMEFACES_MOBILE_APPMENU_TEMPLATE);
            target = webRoot.getFileObject(jsfMobileGenericIncludeFolder2 + "/" + PRIMEFACES_APPMENU_PAGE);

            params.put("appIndex", PRIMEFACES_APPINDEX_PAGE.replace(".xhtml", ""));
            params.put("servletMapping", servletMapping);
            params.put("bundle", bundleVar); // NOI18N
            if (jsfMobileFolder.length() > 0) {
                if (jsfMobileFolder.startsWith("/")) {
                    params.put("jsfMobileFolder", jsfMobileFolder);
                } else {
                    params.put("jsfMobileFolder", "/" + jsfMobileFolder);
                }
            } else {
                params.put("jsfMobileFolder", "mobile");
            }
            if (target == null) {
                target = FileUtil.createData(webRoot, jsfMobileGenericIncludeFolder2 + "/" + PRIMEFACES_APPMENU_PAGE);
            }
            JSFPaletteUtilities.expandJSFTemplate(template, params, target);

            // Add PrimeFaces Application Home Page to be overwritten as index.xhtml
            template = FileUtil.getConfigRoot().getFileObject(PersistenceClientSetupPanelVisual.PRIMEFACES_MOBILE_APPINDEX_TEMPLATE);
            target = webRoot.getFileObject(jsfMobileFolder2 + "/" + WELCOME_JSF_FL_PAGE);
            params.put("bundle", bundleVar); // NOI18N
            params.put("mobileTemplatePage", jsfMobileGenericIncludeFolder + "/" + PRIMEFACES_TEMPLATE_PAGE);
            params.put("appMobileMenu", jsfMobileGenericIncludeFolder + "/" + PRIMEFACES_APPMENU_PAGE);
            if (target == null) {
                target = FileUtil.createData(webRoot, jsfMobileFolder2 + "/" + WELCOME_JSF_FL_PAGE);
            }
            JSFPaletteUtilities.expandJSFTemplate(template, params, target);

            // Add PrimeFaces Mobile Application Template Page to be overwritten
            template = FileUtil.getConfigRoot().getFileObject(PersistenceClientSetupPanelVisual.PRIMEFACES_MOBILE_TEMPLATE_TEMPLATE);
            target = webRoot.getFileObject(jsfMobileGenericIncludeFolder + "/" + PRIMEFACES_TEMPLATE_PAGE);
            params.put("styleFile", PRIMEFACES_CRUD_STYLESHEET);
            params.put("scriptFile", PRIMEFACES_CRUD_SCRIPT);
            params.put("bundle", bundleVar); // NOI18N
            if (target == null) {
                target = FileUtil.createData(webRoot, jsfMobileGenericIncludeFolder + "/" + PRIMEFACES_TEMPLATE_PAGE);
            }
            JSFPaletteUtilities.expandJSFTemplate(template, params, target);
        }

        // Add PrimeFaces Confirmation Dialog Page Include File
        // We only support PF 4.0+ due to its new "global" mode, which
        // is utilized in all CRUD pages via the <p:confirm> tag on buttons
        if (primeFacesVersion.compareTo("4.0") >= 0 && confirmationDialogs) {
            template = FileUtil.getConfigRoot().getFileObject(PersistenceClientSetupPanelVisual.PRIMEFACES_CONFIRMATION_TEMPLATE);
            target = webRoot.getFileObject(jsfGenericIncludeFolder + "/" + PRIMEFACES_CONFIRMATION_PAGE);
            params.put("bundle", bundleVar); // NOI18N
            if (target == null) {
                target = FileUtil.createData(webRoot, jsfGenericIncludeFolder + "/" + PRIMEFACES_CONFIRMATION_PAGE);
            }
            JSFPaletteUtilities.expandJSFTemplate(template, params, target);
        }

        // Add PrimeFaces Confirmation Dialog Page Include File for Mobile
        // ConfirmDialogs were introduced with PF 5.1.13+.
        if (doMobile && confirmationDialogs && primeFacesVersion.compareTo("5.1.13") >= 0) {
            template = FileUtil.getConfigRoot().getFileObject(PersistenceClientSetupPanelVisual.PRIMEFACES_MOBILE_CONFIRMATION_TEMPLATE);
            target = webRoot.getFileObject(jsfMobileGenericIncludeFolder + "/" + PRIMEFACES_CONFIRMATION_PAGE);
            params.put("bundle", bundleVar); // NOI18N
            if (target == null) {
                target = FileUtil.createData(webRoot, jsfMobileGenericIncludeFolder + "/" + PRIMEFACES_CONFIRMATION_PAGE);
            }
            JSFPaletteUtilities.expandJSFTemplate(template, params, target);
        }

        FileObject[] configFiles = ConfigurationUtils.getFacesConfigFiles(wm);
        FileObject fo;
        if (configFiles.length == 0) {
            FileObject dest = wm.getWebInf();
            if (dest == null) {
                dest = webRoot.createFolder("WEB-INF");
            }
            fo = FacesConfigIterator.createFacesConfig(project, dest, "faces-config", false);
        } else {
            fo = configFiles[0];
        }
        JSFConfigModel model = ConfigurationUtils.getConfigModel(fo, true);
        ResourceBundle rb = model.getFactory().createResourceBundle();
        //rb.setVar("bundle");
        rb.setVar(bundleVar);
        rb.setBaseName(bundleName);
        ResourceBundle existing = findBundle(model, rb);

        // Create and identify existing PrimeFaces Mobile Navigation Handler
        NavigationHandler nh = model.getFactory().createNavigationHandler();
        nh.setFullyQualifiedClassType(PRIMEFACES_MOBILE_NAVIGATION_HANDLER);
        NavigationHandler existingNavigationHandler = findNavigationHandler(model, nh);

        model.startTransaction();
        try {
            Application app;
            if (model.getRootComponent().getApplications().isEmpty()) {
                app = model.getFactory().createApplication();
                model.getRootComponent().addApplication(app);
            } else {
                app = model.getRootComponent().getApplications().get(0);
            }

            if (existing == null) {
                app.addResourceBundle(rb);
            }

            // Add PrimeFaces Mobile Navigation Handler if it doesn't exist
            if (existingNavigationHandler == null) {
                app.addNavigationHandler(nh);

            }
        } finally {
            try {
                model.endTransaction();
                model.sync();
            } catch (IllegalStateException ex) {
                IOException io = new IOException("Could not create faces config", ex);
                throw Exceptions.attachLocalizedMessage(io,
                        NbBundle.getMessage(PersistenceClientIterator.class, "ERR_UpdateFacesConfig",
                                Exceptions.findLocalizedMessage(ex)));
            }
            saveFacesConfig(fo);
        }

    }

    private static boolean showImportStatement(String packageName, String fqn) {
        String simpleName = CustomJpaControllerUtil.simpleClassName(fqn);
        return !(packageName + "." + simpleName).equals(fqn); //NOI18N
    }

    protected static boolean isCdiEnabled(Project project) {

        // For Java EE 7 or higher assume CDI being enabled since CDI is now
        // enabled by default, no more beans.xml required.
        WebModule wm = WebModule.getWebModule(project.getProjectDirectory());
        if (wm.getJ2eeProfile().isAtLeast(Profile.JAVA_EE_7_WEB)) {
            return true;
        }

        CdiUtil cdiUtil = project.getLookup().lookup(CdiUtil.class);
        return (cdiUtil == null) ? false : cdiUtil.isCdiEnabled();
    }

    private static void saveFacesConfig(FileObject fo) {
        DataObject facesDO;
        try {
            facesDO = DataObject.find(fo);
            if (facesDO != null) {
                SaveCookie save = facesDO.getCookie(SaveCookie.class);
                if (save != null) {
                    save.save();
                }
            }
        } catch (DataObjectNotFoundException ex) {
            Exceptions.printStackTrace(ex);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    private static ResourceBundle findBundle(JSFConfigModel model, ResourceBundle rb) {
        for (Application app : model.getRootComponent().getApplications()) {
            for (ResourceBundle bundle : app.getResourceBundles()) {
                if (bundle.getVar().equals(rb.getVar())) {
                    return bundle;
                }
            }
        }
        return null;
    }

    private static String getBundleFileName(String bundleName) {
        if (bundleName.startsWith("/")) {
            bundleName = bundleName.substring(1);
        }
        if (!bundleName.endsWith(".properties")) {
            bundleName = bundleName + ".properties"; //.substring(0, bundleName.length()-11);
        }
        return bundleName;
    }

    private static NavigationHandler findNavigationHandler(JSFConfigModel model, NavigationHandler nh) {
        for (Application app : model.getRootComponent().getApplications()) {
            for (NavigationHandler handler : app.getNavigationHandler()) {
                if (handler.getFullyQualifiedClassType().equals(nh.getFullyQualifiedClassType())) {
                    return handler;
                }
            }
        }
        return null;
    }

    public static final class TemplateData {

        private final String entityClassName;
        private final List<FromEntityBase.TemplateData> entityDescriptors;

        public TemplateData(String entityClassName, List<FromEntityBase.TemplateData> entityDescriptors) {
            this.entityClassName = entityClassName;
            this.entityDescriptors = entityDescriptors;
        }

        public String getEntityClassName() {
            return entityClassName;
        }

        public String getEntityNaturalName() {
            return StringHelper.toNatural(entityClassName);
        }

        public List<FromEntityBase.TemplateData> getEntityDescriptors() {
            return entityDescriptors;
        }
    }

    private static String getControllerFileName(String entityClass) {
        String simpleClassName = CustomJpaControllerUtil.simpleClassName(entityClass);
        return simpleClassName + CONTROLLER_SUFFIX;
    }

    private static String getConverterFileName(String entityClass) {
        String simpleClassName = CustomJpaControllerUtil.simpleClassName(entityClass);
        return simpleClassName + CONVERTER_SUFFIX;
    }

    private static String getFacadeFileName(String entityClass) {
        String simpleClassName = CustomJpaControllerUtil.simpleClassName(entityClass);
        return simpleClassName + FACADE_SUFFIX;
    }

    private static String getJsfFileName(String entityClass, String jsfFolder, String name) {
        String simpleClassName = CustomJpaControllerUtil.simpleClassName(entityClass);
        String firstLower = simpleClassName.substring(0, 1).toLowerCase() + simpleClassName.substring(1);
        if (jsfFolder.endsWith("/")) {
            jsfFolder = jsfFolder.substring(0, jsfFolder.length() - 1);
        }
        if (jsfFolder.startsWith("/")) {
            jsfFolder = jsfFolder.substring(1);
        }
        if (jsfFolder.length() > 0) {
            return jsfFolder + "/" + firstLower + "/" + name;
        } else {
            return firstLower + "/" + name;
        }
    }

    private static void expandSingleJSFTemplate(String templateName, String entityClass,
            String jsfFolder, FileObject webRoot, String name, Map<String, Object> params,
            ProgressContributor progressContributor, ProgressPanel progressPanel, int progressIndex) throws IOException {

        //Call out specifying the default JSF Template Directory
        expandSingleJSFTemplate(templateName, PersistenceClientSetupPanelVisual.PRIMEFACES_TEMPLATE_PATH, entityClass,
                jsfFolder, webRoot, name, params,
                progressContributor, progressPanel, progressIndex);
    }

    private static void expandSingleJSFMobileTemplate(String templateName, String entityClass,
            String jsfFolder, FileObject webRoot, String name, Map<String, Object> params,
            ProgressContributor progressContributor, ProgressPanel progressPanel, int progressIndex) throws IOException {

        //Call out specifying the default JSF Template Directory
        expandSingleJSFTemplate(templateName, PersistenceClientSetupPanelVisual.PRIMEFACES_MOBILE_TEMPLATE_PATH, entityClass,
                jsfFolder, webRoot, name, params,
                progressContributor, progressPanel, progressIndex);
    }

    //2013-01-08 Kay Wrobel: NOT NEEDED ANYMORE! This was original done because
    //I had integrated everything in the JSF Plugin and needed a way to use
    //identically named templates from a different location.
//    private static void expandSinglePrimeFacesJSFTemplate(String templateName, String entityClass,
//            String jsfFolder, FileObject webRoot, String name, Map<String, Object> params,
//            ProgressContributor progressContributor, ProgressPanel progressPanel, int progressIndex) throws IOException {
//
//        //Call out specifying the default JSF Template Directory
//        expandSingleJSFTemplate(templateName, "/Templates/PrimeFacesCRUDGenerator/PrimeFaces_From_Entity_Wizard/PrimeFaces/", entityClass,
//                jsfFolder, webRoot, name, params,
//                progressContributor, progressPanel, progressIndex);
//    }
    private static void expandSingleJSFTemplate(String templateName, String templateDirectory, String entityClass,
            String jsfFolder, FileObject webRoot, String name, Map<String, Object> params,
            ProgressContributor progressContributor, ProgressPanel progressPanel, int progressIndex) throws IOException {
        FileObject template = FileUtil.getConfigRoot().getFileObject(templateDirectory + templateName);
        String fileName = getJsfFileName(entityClass, jsfFolder, name);
        String progressMsg = NbBundle.getMessage(PersistenceClientIterator.class, "MSG_Progress_Jsf_Now_Generating", fileName); //NOI18N
        progressContributor.progress(progressMsg, progressIndex);
        progressPanel.setText(progressMsg);

        FileObject jsfFile = webRoot.getFileObject(fileName + ".xhtml");
        if (jsfFile == null) {
            jsfFile = FileUtil.createData(webRoot, fileName + ".xhtml");
        }
        JSFPaletteUtilities.expandJSFTemplate(template, params, jsfFile);
    }

    /**
     * Convenience method to obtain the source root folder.
     *
     * @param project the Project object
     * @return the FileObject of the source root folder
     */
    private static FileObject getSourceRoot(Project project) {
        if (project == null) {
            return null;
        }

        // Search the ${src.dir} Source Package Folder first, use the first source group if failed.
        Sources src = ProjectUtils.getSources(project);
        SourceGroup[] grp = src.getSourceGroups(JavaProjectConstants.SOURCES_TYPE_JAVA);
        for (SourceGroup grp1 : grp) {
            if ("${src.dir}".equals(grp1.getName())) {
                // NOI18N
                return grp1.getRootFolder();
            }
        }
        if (grp.length != 0) {
            return grp[0].getRootFolder();
        }

        return null;
    }

    @Override
    public void initialize(TemplateWizard wizard) {
        index = 0;
        wme = null;
        // obtaining target folder
        Project project = Templates.getProject(wizard);
        DataFolder targetFolder = null;
        try {
            targetFolder = wizard.getTargetFolder();
        } catch (IOException ex) {
            targetFolder = DataFolder.findFolder(project.getProjectDirectory());
        }

        SourceGroup[] sourceGroups = ProjectUtils.getSources(project).getSourceGroups(JavaProjectConstants.SOURCES_TYPE_JAVA);
        HelpCtx helpCtx;

        WebModule wm = WebModule.getWebModule(project.getProjectDirectory());

        if (wm.getJ2eeProfile().equals(Profile.JAVA_EE_6_WEB)
                || wm.getJ2eeProfile().equals(Profile.JAVA_EE_6_FULL)
                || wm.getJ2eeProfile().equals(Profile.JAVA_EE_7_WEB)
                || wm.getJ2eeProfile().equals(Profile.JAVA_EE_7_FULL)
                || JSFUtils.isJSF20Plus(wm, true)) {    //NOI18N
            wizard.putProperty(JSF2_GENERATOR_PROPERTY, "true");
            helpCtx = new HelpCtx("persistence_entity_selection_javaee6");  //NOI18N
        } else {
            helpCtx = new HelpCtx("persistence_entity_selection_javaee5");  //NOI18N
        }

        wizard.putProperty(PersistenceClientEntitySelection.DISABLENOIDSELECTION, Boolean.TRUE);
        WizardDescriptor.Panel secondPanel = new AppServerValidationPanel(
                new PersistenceClientEntitySelection(NbBundle.getMessage(PersistenceClientIterator.class, "LBL_EntityClasses"),
                        helpCtx, wizard)); // NOI18N
        PersistenceClientSetupPanel thirdPanel = new PersistenceClientSetupPanel(project, wizard);

        JSFFrameworkProvider fp = new JSFFrameworkProvider();
        String[] names;
        ArrayList<WizardDescriptor.Panel> panelsList = new ArrayList<>();
        ArrayList<String> namesList = new ArrayList<>();
        panelsList.add(secondPanel);
        panelsList.add(thirdPanel);
        namesList.add(NbBundle.getMessage(PersistenceClientIterator.class, "LBL_EntityClasses"));
        namesList.add(NbBundle.getMessage(PersistenceClientIterator.class, "LBL_JSFPagesAndClasses"));

//2013-02-11 Kay Wrobel: Code commented out because JSFConfigurationWizardPanel is protected to package in JSF Support!
//We will at this point assume that JSF Framework has been added to the project!        
//        if (!fp.isInWebModule(wm)) {
//            updateWebModuleExtender(project, wm, fp);
//            JSFConfigurationWizardPanel jsfWizPanel = new JSFConfigurationWizardPanel(wme, ec);
//            thirdPanel.setFinishPanel(false);
//            panelsList.add(jsfWizPanel);
//            namesList.add(NbBundle.getMessage(PersistenceClientIterator.class, "LBL_JSF_Config_CRUD"));
//        }
        boolean noPuNeeded = true;
        try {
            noPuNeeded = ProviderUtil.persistenceExists(project) || !ProviderUtil.isValidServerInstanceOrNone(project);
        } catch (InvalidPersistenceXmlException ex) {
            Logger.getLogger(JpaControllerIterator.class.getName()).log(Level.FINE, "Invalid persistence.xml: {0}", ex.getPath()); //NOI18N
        }

        if (!noPuNeeded) {
            panelsList.add(new PersistenceUnitWizardDescriptor(project));
            namesList.add(NbBundle.getMessage(PersistenceClientIterator.class, "LBL_PersistenceUnitSetup"));
        }

        panels = panelsList.toArray(new WizardDescriptor.Panel[0]);
        names = namesList.toArray(new String[0]);

        wizard.putProperty("NewFileWizard_Title",
                NbBundle.getMessage(PersistenceClientIterator.class, "Templates/PrimeFacesCRUDGenerator/PrimeFacesFromDB"));
        Image pfLogo = ImageUtilities.loadImage("org/netbeans/modules/web/primefaces/crudgenerator/resources/primefaces-logo.png", true);
        wizard.putProperty(WizardDescriptor.PROP_IMAGE, pfLogo);
        Wizards.mergeSteps(wizard, panels, names);
    }

    private void updateWebModuleExtender(Project project, WebModule wm, JSFFrameworkProvider fp) {
        if (wme == null) {
            ec = ExtenderController.create();
            String j2eeLevel = wm.getJ2eePlatformVersion();
            ec.getProperties().setProperty("j2eeLevel", j2eeLevel);
            J2eeModuleProvider moduleProvider = (J2eeModuleProvider) project.getLookup().lookup(J2eeModuleProvider.class);
            if (moduleProvider != null) {
                String serverInstanceID = moduleProvider.getServerInstanceID();
                ec.getProperties().setProperty("serverInstanceID", serverInstanceID);
            }
            wme = fp.createWebModuleExtender(wm, ec);
        }
        wme.update();
    }

    private String[] createSteps(String[] before, WizardDescriptor.Panel[] panels) {
        int diff = 0;
        if (before == null) {
            before = new String[0];
        } else if (before.length > 0) {
            diff = ("...".equals(before[before.length - 1])) ? 1 : 0; // NOI18N
        }
        String[] res = new String[(before.length - diff) + panels.length];
        for (int i = 0; i < res.length; i++) {
            if (i < (before.length - diff)) {
                res[i] = before[i];
            } else {
                res[i] = panels[i - before.length + diff].getComponent().getName();
            }
        }
        return res;
    }

    @Override
    public void uninitialize(TemplateWizard wiz) {
        panels = null;
        wme = null;
    }

    @Override
    public WizardDescriptor.Panel current() {
        return panels[index];
    }

    @Override
    public String name() {
        return NbBundle.getMessage(PersistenceClientIterator.class, "LBL_WizardTitle_FromEntity");
    }

    @Override
    public boolean hasNext() {
        return index < panels.length - 1;
    }

    @Override
    public boolean hasPrevious() {
        return index > 0;
    }

    @Override
    public void nextPanel() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        index++;
    }

    @Override
    public void previousPanel() {
        if (!hasPrevious()) {
            throw new NoSuchElementException();
        }
        index--;
    }

    @Override
    public void addChangeListener(ChangeListener l) {
    }

    @Override
    public void removeChangeListener(ChangeListener l) {
    }

    private static String getServletMapping(WebModule wm) {
        String servletMapping = ConfigurationUtils.getFacesServletMapping(wm);
        int wildCardPos = servletMapping.indexOf("*");
        if (wildCardPos > 0 && servletMapping.charAt(wildCardPos - 1) == '/') {
            wildCardPos -= 1;
        }
        servletMapping = servletMapping.substring(0, wildCardPos);
        return servletMapping;
    }
    
    private static String removeTrailingSlash(String uri) {
        // URI must be greater than 1 in length. This excludes URI="/" from the test, which should not be fixed.
        // Otherwise URI would become empty string.
        if (uri.endsWith("/")) {
            return uri.substring(0, uri.lastIndexOf("/"));
        } else {
            return uri;
        }
}
}
