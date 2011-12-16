package com.divisiblebyzero.network;

import java.io.Serializable;

public class Notifier implements Serializable {
    private static final long serialVersionUID = -6714255866242683456L;
    
    private boolean incoming, outgoing;
    private boolean connected;
    private Message message;
    
    public Notifier() {
        this.incoming  = false;
        this.outgoing  = false;
        this.connected = false;
        this.message   = new Message();
    }
    
    public boolean isIncoming() {
        return this.incoming;
    }
    
    public void isIncoming(boolean incoming) {
        this.incoming = incoming;
    }
    
    public boolean isOutgoing() {
        return this.outgoing;
    }
    
    public void isOutgoing(boolean outgoing) {
        this.outgoing = outgoing;
    }
    
    public boolean isConnected() {
        return this.connected;
    }
    
    public void isConnected(boolean connected) {
        this.connected = connected;
    }
    
    public Message getMessage() {
        return this.message;
    }
    
    public void setMessage(Message message) {
        this.message = message;
    }
}
