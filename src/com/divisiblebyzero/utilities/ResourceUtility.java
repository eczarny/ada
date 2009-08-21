package com.divisiblebyzero.utilities;

//
// utilities.Resource.java
// Ada Chess
//
// Created by Eric Czarny on April 6, 2006.
// Copyright 2009 Divisible by Zero. All rights reserved.
//

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.apache.log4j.Logger;

public class ResourceUtility {
    private static Logger logger = Logger.getLogger(ResourceUtility.class);
    
    public static Image getImage(String filename) throws IOException {
        return ImageIO.read(new File(filename));
    }
    
    public static void playAudioFile(String filename) {
        try {
            ResourceUtility.getClip(filename).start();
        } catch (LineUnavailableException e) {
            logger.error("The specified audio resource is already in use: " + filename, e);
        } catch (UnsupportedAudioFileException e) {
            logger.error("The specified audio resource is unsupported: " + filename, e);
        } catch (IOException e) {
            logger.error("The specified audio resource does not exist or cannot be opened: " + filename, e);
        }
    }
    
    private static Clip getClip(String filename) throws LineUnavailableException, UnsupportedAudioFileException, IOException {
        ResourceUtility resourceUtility = new ResourceUtility();
        Clip clip = AudioSystem.getClip();
        
        clip.open(AudioSystem.getAudioInputStream(resourceUtility.getResourceURL(filename)));
        
        return clip;
    }
    
    private URL getResourceURL(String resource) {
        return getClass().getClassLoader().getResource(resource);
    }
}
