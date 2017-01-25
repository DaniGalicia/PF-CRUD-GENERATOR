/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.netbeans.modules.web.primefaces.crudgenerator.util;

/**
 *
 * @author kuw
 */
public final class StringHelper {

    public static String firstLower(String string) {
        boolean makeFirstLower = string.length() < 2 || (!Character.isUpperCase(string.charAt(1)));
        return makeFirstLower ? string.substring(0, 1).toLowerCase() + string.substring(1) : string;
    }

    public static String firstUpper(String string) {
        return string.length() > 1 ? string.substring(0, 1).toUpperCase() + string.substring(1) : string.toUpperCase();
    }

    public static String removeBeanMethodPrefix(String methodName) {
        if (methodName.startsWith("get")) {  //NOI18N
            methodName = methodName.replaceFirst("get", "");
        }
        if (methodName.startsWith("set")) {  //NOI18N
            methodName = methodName.replaceFirst("set", "");
        }
        if (methodName.startsWith("is")) {  //NOI18N
            methodName = methodName.replaceFirst("is", "");
        }
        return methodName;
    }

    public static boolean isGetterMethod(String methodName) {
        return methodName.startsWith("get") || methodName.startsWith("is");
    }

    public static String toNatural(String input) {
        String natural = "";
        Character lastChar = null;
        for (Character curChar : input.toCharArray()) {
            if (lastChar == null) {
                // First character
                lastChar = Character.toUpperCase(curChar);
                natural = natural + lastChar;

            } else {
                if (Character.isLowerCase(lastChar) && 
                        (Character.isUpperCase(curChar)) || Character.isDigit(curChar)) {
                    natural = natural + " " + curChar;
                } else {
                    natural = natural + curChar;
                }
                lastChar = curChar;
            }

        }
        return natural;
    }
    
}
