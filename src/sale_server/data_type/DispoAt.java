package sale_server.data_type;

import java.io.Serializable;
import java.util.GregorianCalendar;

public class DispoAt implements Serializable {
    private final int jour;
    private final int mois;
    private final int annee;
    private final int heure;
    private final int minute;

    public DispoAt(int jour, int mois, int annee, int heure, int minute) {
        this.jour = jour;
        this.mois = mois;
        this.annee = annee;
        this.heure = heure;
        this.minute = minute;
    }

    public GregorianCalendar getCalendar() {
        return new GregorianCalendar(annee, mois, jour, heure, minute);
    }
}
