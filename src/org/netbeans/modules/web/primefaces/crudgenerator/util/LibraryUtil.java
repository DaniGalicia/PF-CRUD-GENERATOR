/*
 * This software is distributied under the GPL Version 2 license. The license
 * text is accompanied with this module.
 */
package org.netbeans.modules.web.primefaces.crudgenerator.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.Attributes.Name;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.netbeans.api.java.classpath.ClassPath;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.filesystems.JarFileSystem;
import org.openide.util.Exceptions;

/**
 * Helper class for libraries. Key feature is to determine if a given library
 * name exists in the project class path and at what version level.
 *
 * @author Kay Wrobel
 */
public class LibraryUtil {

    private static final String MANIFEST_RESOURCE_NAME = "META-INF/MANIFEST.MF";
    private static final String MANIFEST_KEY_BUNDLE_NAME = "Bundle-Name";
    private static final String MANIFEST_KEY_BUNDLE_VERSION = "Bundle-Version";
    private static final Name BUNDLE_NAME = new Attributes.Name(MANIFEST_KEY_BUNDLE_NAME);
    private static final Name BUNDLE_VERSION = new Attributes.Name(MANIFEST_KEY_BUNDLE_VERSION);

    public static Version getVersion(ClassLoader classLoader, String libraryName) {
        Enumeration<URL> resources;
        String libraryVersion = "";
        // Retrieve all Manifest information that's part of the class loader.
        try {
            resources = classLoader.getResources(MANIFEST_RESOURCE_NAME);
        } catch (IOException ex) {
            Logger.getLogger(LibraryUtil.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

        while (resources.hasMoreElements()) {
            try {
                //Going to query for following manifest attributes
                Manifest manifest = new Manifest(resources.nextElement().openStream());
                Attributes mainAttributes = manifest.getMainAttributes();

                // Find library version by Bundle information (Eclipse/OSGi)
                if (mainAttributes.containsKey(BUNDLE_NAME) && mainAttributes.containsKey(BUNDLE_VERSION)) {
                    if (mainAttributes.getValue(BUNDLE_NAME).equals(libraryName)) {
                        libraryVersion = mainAttributes.getValue(BUNDLE_VERSION);
                        return new Version(libraryVersion);
                    }
                }

                // If unsuccessful, try by default Manifest Headers
                if (mainAttributes.containsKey(Name.IMPLEMENTATION_TITLE) && mainAttributes.containsKey(Name.IMPLEMENTATION_VERSION)) {
                    if (mainAttributes.getValue(Name.IMPLEMENTATION_TITLE).equals(libraryName)) {
                        libraryVersion = mainAttributes.getValue(Name.IMPLEMENTATION_VERSION);
                        return new Version(libraryVersion);
                    }
                }

            } catch (IOException ex) {
                Logger.getLogger(LibraryUtil.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
        return null;
    }

    public static String getLibrary(ClassPath classPath, String resource) {
        // Find as resource as a class
        String classNameAsPath = resource.replace('.', '/') + ".class";
        FileObject classResource = classPath.findResource(classNameAsPath);
        if (classResource == null) {
            // Find as resource as a package
            classNameAsPath = resource.replace('.', '/');
            classResource = classPath.findResource(classNameAsPath);

        }
        if (classResource != null) {
            FileObject archiveFile = FileUtil.getArchiveFile(classResource);
            if (archiveFile != null && FileUtil.isArchiveFile(archiveFile)) {
                File toFile = FileUtil.toFile(archiveFile);
                try {
                    JarFile jf = new JarFile(toFile);
                    Manifest manifest = jf.getManifest();
                    Attributes mainAttributes = manifest.getMainAttributes();

                    // Find library version by Bundle information (Eclipse/OSGi)
                    if (mainAttributes.containsKey(BUNDLE_NAME) && mainAttributes.containsKey(BUNDLE_VERSION)) {
                        if (!mainAttributes.getValue(BUNDLE_NAME).isEmpty()) {
                            return mainAttributes.getValue(BUNDLE_NAME);
                        }
                    }

                    // If unsuccessful, try by default Manifest Headers
                    if (mainAttributes.containsKey(Name.IMPLEMENTATION_TITLE) && mainAttributes.containsKey(Name.IMPLEMENTATION_VERSION)) {
                        if (!mainAttributes.getValue(Name.IMPLEMENTATION_TITLE).isEmpty()) {
                            return mainAttributes.getValue(Name.IMPLEMENTATION_TITLE);
                        }
                    }
                } catch (IOException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }

        }
        return null;
    }

}
