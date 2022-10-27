package sale_server.requete.pone;

import sale_server.identifier.PoneID;

import java.util.UUID;

public class SuppressionEnergieRequete extends PoneRequete {

    private final UUID[] idOffres;

    public SuppressionEnergieRequete(PoneID poneID, UUID[] idOffres) {
        super(poneID);
        this.idOffres = idOffres;
    }

    public SuppressionEnergieRequete(PoneID poneID, UUID[] idOffres, int portReponse) {
        super(poneID, portReponse);
        this.idOffres = idOffres;
    }

    public UUID[] getIdOffres() {
        return idOffres;
    }
}
