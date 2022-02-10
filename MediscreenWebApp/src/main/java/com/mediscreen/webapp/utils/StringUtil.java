package com.mediscreen.webapp.utils;

public final class StringUtil {

    /* Constructeur privé */
    private StringUtil() {
        //vide
    }

    public static String nettoyerTags(String note) {
        return note.replaceAll("\\<.*?\\>", "");
    }

}
