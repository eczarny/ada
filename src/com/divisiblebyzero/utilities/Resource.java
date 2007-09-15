package com.divisiblebyzero.utilities;

//
//  utilities.Resource.java
//  Ada Chess
//
//  Created by Eric Czarny on April 6, 2006.
//  Copyright 2007 Divisible by Zero. All rights reserved.
//

import java.awt.*;
import java.net.*;
import javax.sound.sampled.*;

public class Resource {
	public static Clip getClip(String resource) {
		try {
			Clip clip = AudioSystem.getClip();
			
			clip.open(AudioSystem.getAudioInputStream(new Resource().getResourceURL(resource)));
			
			return clip;
		} catch (Exception e) {
			return null;
		}
	}
	
	public static Image getImage(String resource) {
		return Toolkit.getDefaultToolkit().getImage(new Resource().getResourceURL(resource));
	}
	
	private URL getResourceURL(String resource) {
		return this.getClass().getResource(resource);
	}
}
