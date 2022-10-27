package sale_server.requete.pone;

import sale_server.identifier.Identifier;
import sale_server.identifier.PoneID;
import sale_server.requete.AbstractRequete;

public abstract class PoneRequete extends AbstractRequete {

    private final PoneID poneID;

    public PoneRequete(PoneID poneID) {
        super();
        this.poneID = poneID;
    }

    public PoneRequete(PoneID poneID, int portReponse) {
        super(portReponse);
        this.poneID = poneID;
    }

    public Identifier getAuthID() {
        return poneID;
    }

}
