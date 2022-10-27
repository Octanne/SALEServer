package sale_server.identifier;

public interface Identifier {

    int getSerialId();
    void changePrivateKey(String newPrivateKey);

    String getPrivateKey();

    boolean checkID(Identifier poneID);
}
