package sale_server.identifier;

import java.io.Serializable;

public class PoneID implements Serializable, Identifier {

    private final int serialId;
    private String privateKey;

    public PoneID(int serialId, String privateKey) {
        this.serialId = serialId;
        this.privateKey = privateKey;
    }

    public int getSerialId() {
        return serialId;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void changePrivateKey(String newPrivateKey) {
        this.privateKey = newPrivateKey;
    }

    public boolean checkID(Identifier poneID) {
        return serialId == poneID.getSerialId() && privateKey.equals(poneID.getPrivateKey());
    }
}
