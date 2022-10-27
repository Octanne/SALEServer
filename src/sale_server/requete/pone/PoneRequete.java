package sale_server.requete.pone;

import sale_server.identifier.PoneID;
import sale_server.requete.AbstractRequete;

public abstract class PoneRequete extends AbstractRequete {

    private final PoneID poneID;

    public PoneRequete(PoneID poneID) {
        this.poneID = poneID;
    }

    public PoneID getPoneID() {
        return poneID;
    }

}
