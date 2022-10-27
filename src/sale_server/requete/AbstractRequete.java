package sale_server.requete;

import sale_server.identifier.Identifier;

import java.io.Serializable;

public abstract class AbstractRequete implements Serializable {

    // TODO Pour le projet pour chiffrer on géra via une encapsulation dans un nouvelle objet
    // TODO contenant un tableau de bytes correspond à l'objet après chiffrement et un champ clefPublique pour
    // TODO chiffré en RSA la réponse à la requete qui sera du coup dans le même principe.

    private final int portReponse;

    public AbstractRequete(int portReponse) {
        this.portReponse = portReponse;
    }

    public AbstractRequete() {
        this.portReponse = 3032;
    }

    public int getPortReponse() {
        return portReponse;
    }

    public abstract Identifier getAuthID();
}