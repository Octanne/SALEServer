package sale_server.identifier;

public class TareID {

    private final int serialId;
    private String privateKey;

    public TareID(int serialId, String privateKey) {
        this.serialId = serialId;
        this.privateKey = privateKey;
    }

    public int getSerialId() {
        return serialId;
    }

    public void changePrivateKey(String newPrivateKey) {
        this.privateKey = newPrivateKey;
    }

    public boolean checkID(TareID tareID) {
        return serialId == tareID.serialId && privateKey.equals(tareID.privateKey);
    }

}
