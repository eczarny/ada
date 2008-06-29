package com.divisiblebyzero.network;

//
//  network.Host.java
//  Ada Chess
//
//  Created by Eric Czarny on April 29, 2006.
//  Copyright 2008 Divisible by Zero. All rights reserved.
//

import java.net.ServerSocket;
import java.net.Socket;

import org.apache.log4j.Logger;

import com.divisiblebyzero.ada.common.Ada;

public class Host extends Thread {
    private ServerSocket socket;
    private byte[] buffer;
    private Ada ada;
    private Notifier notifier;
    
    private static Logger logger = Logger.getLogger(Host.class);
    
    public Host(int port, Notifier notifier, Ada ada) throws Exception {
        this.buffer = new byte[5];
        
        try {
            this.socket = new ServerSocket(port);
            
            this.ada = ada;
            this.notifier = notifier;
            
            logger.info("Host created, attempting to start server thread...");
            
            this.start();
        } catch (Exception e) {
            logger.error("Unable to start server (Exception: " + e + ".");
            
            throw new Exception("Failed binding to port " + port + ", " + e.getMessage());
        }
    }
    
    public void run() {
        Message message =  new Message();
        
        logger.info("Server started, waiting for client connections...");
        
        try {
            Socket connection = this.socket.accept();
            
            read(connection, this.buffer);
            
            message.parseBytes(this.buffer);
            
            logger.debug("Network message received by host: " + message + ".");
            
            /* Setup requested, send acceptance, and enter Host loop. */
            if (message.isConnect()) {
                logger.info("Network connection between host and client established.");
                
                /* Let Ada know we're connected. */
                this.notifier.isConnected(true);
                
                write(connection, (new Message(Message.Commands.ACCEPT).toBytes()));
                
                while (true) {
                    read(connection, this.buffer);
                    
                    /* Parse the buffer, creating a new Message. */
                    message.parseBytes(this.buffer);
                    
                    logger.debug("Network message received by host: " + message + ".");
                    
                    /* If the Host receives a disconnect, tear down connection. */
                    if (message.isDisconnect()) {
                        write(connection, (new Message(Message.Commands.ACCEPT).toBytes()));
                        
                        break;
                    }
                    
                    /* Give Ada the new message. */
                    this.notifier.setMessage(message);
                    
                    /* Let Ada know we have an incoming message. */
                    this.notifier.isIncoming(true);
                    
                    /* Let the Table and Board know we are updating. */
                    this.ada.getTable().networkUpdateNotification();
                    
                    while (!this.notifier.isOutgoing()) {
                        /* Waiting for an outgoing message... */
                    }
                    
                    write(connection, this.notifier.getMessage().toBytes());
                    
                    this.notifier.isOutgoing(false);
                }
            }
            
            this.socket.close();
        } catch (Exception e) {
            logger.error("Connection with client lost!");
        }
    }
    
    private static void read(Socket connection, byte buffer[]) throws Exception {
        connection.getInputStream().read(buffer);
    }
    
    private static void write(Socket connection, byte buffer[]) throws Exception {
        connection.getOutputStream().write(buffer);
    }
}
