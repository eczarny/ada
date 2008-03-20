package com.divisiblebyzero.utilities;

//
//  utilities.Localization.java
//  Ada Chess
//
//  Created by Eric Czarny on November 18, 2007.
//  Copyright 2008 Divisible by Zero. All rights reserved.
//

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

public class Localization {
	private static Logger logger = Logger.getLogger(Localization.class);
	private static Localization instance = null;
	
	private ResourceBundle bundle;
	private Locale locale;
	
	private static Locale[] supportedLocales = {
		Locale.ENGLISH,
		Locale.US
	};
	
	public static Localization getInstance() throws MissingResourceException {
		if (instance == null) {
			synchronized (Localization.class) {
				Locale locale = Locale.getDefault();
				
				if (!Localization.isLocaleSupported(locale)) {
					logger.error("The default locale, " + locale + ", is unsupported.");
					
					locale = Locale.US;
				}
				
				instance = new Localization("LocalizedStrings", locale);
			}
		}
		
		return instance;
	}
	
	private Localization(String bundle, Locale locale) throws MissingResourceException {
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
