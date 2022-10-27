package sale_server.reponse.tare;

import sale_server.data_type.PoneOffreEnergie;

public class EnergieListeReponse extends TareReponse {

    private final PoneOffreEnergie[] listeEnergie;

    public EnergieListeReponse(PoneOffreEnergie[] listeEnergie) {
        this.listeEnergie = listeEnergie;
    }

    public PoneOffreEnergie[] getListeEnergie() {
        return listeEnergie;
    }

}
