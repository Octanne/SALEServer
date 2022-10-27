package sale_server.requete.pone;

import sale_server.data_type.PoneOffreEnergie;
import sale_server.identifier.PoneID;

import java.util.UUID;

public class AjoutEnergieRequete extends PoneRequete {
    private final PoneOffreEnergie[] newOffres;

    public AjoutEnergieRequete(PoneID poneID, PoneOffreEnergie[] newOffres) {
        super(poneID);
        this.newOffres = newOffres;
    }

    public PoneOffreEnergie[] getNewOffres() {
        return newOffres;
    }
}
