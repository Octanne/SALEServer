package sale_server.requete.tare;

import sale_server.identifier.TareID;
import sale_server.requete.AbstractRequete;

public abstract class TareRequete extends AbstractRequete {

    private final TareID tareID;

    public TareRequete(TareID tareID, int portReponse) {
        super(portReponse);
        this.tareID = tareID;
    }

    public TareRequete(TareID tareID) {
        super();
        this.tareID = tareID;
    }

    public TareID getAuthID() {
        return tareID;
    }
}
