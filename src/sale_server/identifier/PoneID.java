package sale_server.identifier;

public class PoneID {

    private final int serialId;
    private String privateKey;

    public PoneID(int serialId, String privateKey) {
        this.serialId = serialId;
        this.privateKey = privateKey;
    }

    public int getSerialId() {
        return serialId;
    }

    public void changePrivateKey(String newPrivateKey) {
        this.privateKey = newPrivateKey;
    }

    public boolean checkID(PoneID poneID) {
        return serialId == poneID.serialId && privateKey.equals(poneID.privateKey);
    }
}
