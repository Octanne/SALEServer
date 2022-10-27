package sale_server.identifier;

import java.io.Serializable;

public class TareID implements Serializable, Identifier {

    private final int serialId;
    private String privateKey;

    public TareID(int serialId, String privateKey) {
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

    public boolean checkID(Identifier tareID) {
        return serialId == tareID.getSerialId() && privateKey.equals(tareID.getPrivateKey());
    }

}
