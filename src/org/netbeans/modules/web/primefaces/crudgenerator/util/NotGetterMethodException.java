/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.netbeans.modules.web.primefaces.crudgenerator.util;

/**
 *
 * @author Kay Wrobel
 */
public class NotGetterMethodException extends Exception {

    /**
     * Creates a new instance of <code>NotGetterMethodException</code> without
     * detail message.
     */
    public NotGetterMethodException() {
        super("Bean method is not a valid getter method. It's name should begin with \"get\" or \"is\".");
    }

    /**
     * Constructs an instance of <code>NotGetterMethodException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public NotGetterMethodException(String msg) {
        super(msg);
    }
}
