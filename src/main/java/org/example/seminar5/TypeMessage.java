package org.example.seminar5;

public enum TypeMessage {
    MESSAGE ("message"),
    BROADCAST_MESSAGE ("broadcastMessage");

    private final String type;

    private TypeMessage(String type) {
        this.type = type;
    }
}
