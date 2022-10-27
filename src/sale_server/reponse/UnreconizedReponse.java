package sale_server.reponse;

import java.io.Serializable;

public class UnreconizedReponse extends AbstractReponse {

    public UnreconizedReponse(String msg) {
        super("Requete non reconnue : " + msg, false);
    }

}
