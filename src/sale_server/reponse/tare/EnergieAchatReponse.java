package sale_server.reponse.tare;

import sale_server.data_type.statut.StatutAchat;

import java.util.HashMap;
import java.util.UUID;

public class EnergieAchatReponse extends TareReponse {

    private final HashMap<UUID, StatutAchat> resultAchatEnergie;

    public EnergieAchatReponse(HashMap<UUID, StatutAchat> resultAchatEnergie) {
        this.resultAchatEnergie = resultAchatEnergie;
    }

    public HashMap<UUID, StatutAchat> getResultAchatEnergie() {
        return resultAchatEnergie;
    }

    public void putResultAchatEnergie(UUID idEnergie, StatutAchat result) {
        resultAchatEnergie.put(idEnergie, result);
    }

}
