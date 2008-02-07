package com.divisiblebyzero.utilities;

//
//  utilities.Resource.java
//  Ada Chess
//
//  Created by Eric Czarny on April 6, 2006.
//  Copyright 2008 Divisible by Zero. All rights reserved.
//

import java.awt.Image;
import java.awt.Toolkit;
import java.net.URL;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import org.apache.log4j.Logger;

public class Resource {
	private static Logger logger = Logger.getLogger(Resource.class);
	
	public static Image getImage(String resource) {
		return Toolkit.getDefaultToolkit().getImage(resource);
	}
	
	public static void playAudioFile(String resource) {
		Resource.getClip(resource).start();
	}
	
	private static Clip getClip(String resource) {
		logger.info("Attempting to get the " + resource + " audio resource.");
		
		try {
			Clip clip = AudioSystem.getClip();
			
			clip.open(AudioSystem.getAudioInputStream(new Resource().getResourceURL(resource)));
			
			return clip;
		} catch (Exception e) {
			logger.error("Unable to locate the following audio resource: " + resource + ".");
		}
		
		return null;
	}
	
	private URL getResourceURL(String resource) {
		return this.getClass().getResource(resource);
	}
}
