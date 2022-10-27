package sale_server.requete.tare;

import sale_server.identifier.TareID;

import java.util.UUID;

public class EnergieAchatRequete extends TareRequete {

    private final UUID[] idOffres;

    public EnergieAchatRequete(TareID tareID, UUID[] idOffres) {
        super(tareID);
        this.idOffres = idOffres;
    }

    public EnergieAchatRequete(TareID tareID, UUID[] idOffres, int portReponse) {
        super(tareID, portReponse);
        this.idOffres = idOffres;
    }

    public UUID[] getIDOffres() {
        return idOffres;
    }
}
