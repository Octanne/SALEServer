package chiffrer.rsa;

import java.io.*;

public class MessageChiffreRSA implements Serializable {
    private final byte[] messageChiffre;
    private final String passePhrase;
    private final boolean initConnection;

    public MessageChiffreRSA(byte[] messageChiffre) {
        this.initConnection = false;
        this.messageChiffre = messageChiffre;
        this.passePhrase = null;
    }
    public MessageChiffreRSA(String passePhrase, byte[] publicKeyChiffre) {
        this.initConnection = true;
        this.passePhrase = passePhrase;
        this.messageChiffre = publicKeyChiffre;
    }

    public byte[] getMessageChiffre() {
        return messageChiffre;
    }

    public String getPassePhrase() {
        return passePhrase;
    }

    public boolean isInitConnection() {
        return initConnection;
    }
}