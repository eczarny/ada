package com.divisiblebyzero.utilities;

//
// Localization.java
// Ada Chess
//
// Created by Eric Czarny on November 18, 2007.
// Copyright 2009 Divisible by Zero. All rights reserved.
//

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

public class LocalizationUtility {
    private static Logger logger = Logger.getLogger(LocalizationUtility.class);
    private static LocalizationUtility instance = null;
    
    private ResourceBundle bundle;
    private Locale locale;
    
    private static Locale[] supportedLocales = {
        Locale.ENGLISH,
        Locale.US
    };
    
    public static synchronized LocalizationUtility getInstance() throws MissingResourceException {
        if (instance == null) {
            Locale locale = Locale.getDefault();
            
            if (!LocalizationUtility.isLocaleSupported(locale)) {
                logger.error("The default locale, " + locale + ", is unsupported.");
                
                locale = Locale.US;
            }
            
            instance = new LocalizationUtility("LocalizedStrings", locale);
        }
        
        return instance;
    }
    
    private LocalizationUtility(String bundle, Locale locale) throws MissingResourceException {
        this.bundle = ResourceBundle.getBundle(bundle, locale);
        
        this.locale = locale;
    }
    
    public String getLocalizedString(String key) throws MissingResourceException {
        return this.bundle.getString(key);
    }
    
    public Locale getLocale() {
        return this.locale;
    }
    
    public static boolean isLocaleSupported(Locale locale) {
        boolean result = false;
        
        for (Locale availableLocale : supportedLocales) {
            if (availableLocale.equals(locale)) {
                result = true;
                
                break;
            }
        }
        
        return result;
    }
}
