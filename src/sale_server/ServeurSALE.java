package sale_server;

import sale_server.data_type.*;
import sale_server.data_type.statut.StatutAchat;
import sale_server.data_type.statut.StatutAjout;
import sale_server.data_type.statut.StatutSuppression;
import sale_server.identifier.Identifier;
import sale_server.identifier.PoneID;
import sale_server.identifier.TareID;
import sale_server.reponse.AbstractReponse;
import sale_server.reponse.UnreconizedReponse;
import sale_server.reponse.pone.AjoutEnergieReponse;
import sale_server.reponse.pone.SuppressionEnergieReponse;
import sale_server.reponse.tare.EnergieAchatReponse;
import sale_server.reponse.tare.EnergieListeReponse;
import sale_server.requete.AbstractRequete;
import sale_server.requete.pone.AjoutEnergieRequete;
import sale_server.requete.pone.SuppressionEnergieRequete;
import sale_server.requete.tare.EnergieAchatRequete;
import sale_server.requete.tare.EnergieListeRequete;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ServeurSALE extends Thread{

    private final int portEcoute;
    private DatagramSocket socket;
    private boolean running, isActive;

    // Stockage temporaire des IDS
    private final List<Identifier> ids_storage;

    public ServeurSALE(int portEcoute) {
        super("ServeurSALE");
        this.portEcoute = portEcoute;
        running = false;
        isActive = false;

        ids_storage = new ArrayList<>();
        // create fake ids
        ids_storage.add(new TareID(45884, "tare1"));
        ids_storage.add(new TareID(12541, "tare2"));
        ids_storage.add(new TareID(355442, "tare3"));
        // make some Pone
        ids_storage.add(new PoneID(1558, "pone1"));
        ids_storage.add(new PoneID(25546, "pone2"));
        ids_storage.add(new PoneID(22568, "pone3"));
    }

    public void start() {
        startServer();
        super.start();
    }

    public void startServer() {
        try {
            socket = new DatagramSocket(portEcoute);
            running = true;
            isActive = true;
        } catch (SocketException e) {
            e.printStackTrace();
            running = false;
            isActive = false;
        }
    }

    public void run() {
        while (running) {
            if (!isActive) continue;

            // Lecture du message du client
            DatagramPacket msgRecu;
            try {
                byte[] tampon = new byte[8164];
                msgRecu = new DatagramPacket(tampon, tampon.length);
                socket.receive(msgRecu);
            } catch(IOException e) {
                System.err.println("[SRV] Erreur lors de la réception du message : " + e);
                continue;
            }

            try {
                // Récupération de l'objet requete
                ByteArrayInputStream bais = new ByteArrayInputStream(msgRecu.getData());
                ObjectInputStream ois = new ObjectInputStream(bais);
                Object requeteO = ois.readObject();
                System.out.println("[SRV] Requete recue : " + requeteO.getClass().getSimpleName());

                // Traitement de la requete
                if (requeteO instanceof AbstractRequete) {
                    AbstractReponse reponse = null;
                    if (requeteO instanceof AjoutEnergieRequete) {
                        reponse = repondre((AjoutEnergieRequete) requeteO);
                    } else if (requeteO instanceof SuppressionEnergieRequete) {
                        reponse = repondre((SuppressionEnergieRequete) requeteO);
                    } else if (requeteO instanceof EnergieAchatRequete) {
                        reponse = repondre((EnergieAchatRequete) requeteO);
                    } else if (requeteO instanceof EnergieListeRequete) {
                        reponse = repondre((EnergieListeRequete) requeteO);
                    }

                    if (reponse != null) {
                        // Transformation en tableau d'octets
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        try {
                            ObjectOutputStream oos = new ObjectOutputStream(baos);
                            oos.writeObject(reponse);

                            // Création et envoi du segment UDP
                            byte[] donnees = baos.toByteArray();
                            DatagramPacket msg = new DatagramPacket(donnees, donnees.length, msgRecu.getAddress(),
                                    ((AbstractRequete) requeteO).getPortReponse());
                            socket.send(msg);
                            System.out.println("[SRV] Réponse envoyée : " + reponse.getClass().getSimpleName() +
                                    " (" + donnees.length + " octets)" + " à " + msgRecu.getAddress() + ":"
                                    + ((AbstractRequete) requeteO).getPortReponse());
                        } catch(IOException e) {
                            System.err.println("[SRV] Erreur lors de la sérialisation : " + e);
                            bad_requete("Erreur lors de la sérialisation : " + e,
                                    msgRecu.getAddress(), ((AbstractRequete) requeteO).getPortReponse());
                        }
                    } else {
                        System.err.println("[SRV] La requete n'est pas reconnue");
                        bad_requete("Requete non reconnue", msgRecu.getAddress(), ((AbstractRequete) requeteO).getPortReponse());
                    }
                } else {
                    System.err.println("[SRV] La charge utile n'est pas une requete");
                }
            } catch(ClassNotFoundException e) {
                System.err.println("[SRV] Objet reçu non reconnu : " + e);
            } catch(IOException e) {
                System.err.println("[SRV] Erreur lors de la récupération de l'objet : " + e);
            }
        }
    }

    public void stopServer() {
        running = false;
        socket.close();
        socket = null;
    }

    public void killServer() {
        isActive = false;
        socket.close();
        socket = null;
    }

    private void bad_requete(String messageStatus, InetAddress adresse, int port) {
        UnreconizedReponse rep = new UnreconizedReponse(messageStatus);

        // Transformation en tableau d'octets
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(rep);

            // Création et envoi du segment UDP
            byte[] donnees = baos.toByteArray();
            DatagramPacket msg = new DatagramPacket(donnees, donnees.length, adresse, port);
            socket.send(msg);
            System.out.println("[SRV] Réponse envoyée : " + rep.getClass().getSimpleName() +
                    " (" + donnees.length + " octets)" + " à " + adresse + ":" + port);
        } catch(IOException e) {
            System.err.println("[SRV] Erreur lors de la sérialisation : " + e);
        }
    }

    private AbstractReponse repondre(AjoutEnergieRequete requete) {
        if (checkAuthentification(requete)) {
            AjoutEnergieReponse reponse = new AjoutEnergieReponse(new HashMap<>());

            for (PoneOffreEnergie newOffre : requete.getNewOffres()) {
                if (Main.marcheEnergie.getEnergieFromUUID(newOffre.getOffreID()) != null) {
                    reponse.putResultAjoutEnergie(newOffre.getOffreID(), StatutAjout.ID_ALREADY_EXISTS);
                } else {
                    Main.marcheEnergie.addOffer(newOffre);
                    reponse.putResultAjoutEnergie(newOffre.getOffreID(), StatutAjout.SUCCESS);
                }
            }

            return reponse;
        } else {
            return new UnreconizedReponse("Authentification incorrecte");
        }
    }

    private AbstractReponse repondre(SuppressionEnergieRequete requete) {
        if (checkAuthentification(requete)) {
            SuppressionEnergieReponse reponse = new SuppressionEnergieReponse(new HashMap<>());

            for (UUID idOffre : requete.getIdOffres()) {
                PoneOffreEnergie offre = Main.marcheEnergie.getEnergieFromUUID(idOffre);

                if (offre != null) {
                    reponse.putResultSuppressionEnergie(idOffre, StatutSuppression.SUCCESS);
                    Main.marcheEnergie.removeOffer(offre);
                } else {
                    reponse.putResultSuppressionEnergie(idOffre, StatutSuppression.NOT_FOUND);
                }
            }

            return reponse;
        } else {
            return new UnreconizedReponse("Authentification incorrecte");
        }
    }

    private AbstractReponse repondre(EnergieAchatRequete requete) {
        if (checkAuthentification(requete)) {
            EnergieAchatReponse reponse = new EnergieAchatReponse(new HashMap<>());
            for (UUID idOffre : requete.getIDOffres()) {
                PoneOffreEnergie offre = Main.marcheEnergie.getEnergieFromUUID(idOffre);

                if (offre != null) {
                    if (offre.isSold()) {
                        reponse.putResultAchatEnergie(idOffre, StatutAchat.NOT_AVAILABLE);
                    } else {
                        reponse.putResultAchatEnergie(idOffre, StatutAchat.SUCCESS);
                        offre.soldTo(requete.getAuthID());
                    }
                } else {
                    reponse.putResultAchatEnergie(idOffre, StatutAchat.NOT_FOUND);
                }
            }

            return reponse;
        } else {
            return new UnreconizedReponse("Authentification incorrecte");
        }
    }

    private AbstractReponse repondre(EnergieListeRequete requete) {
        List<PoneOffreEnergie> liste = new ArrayList<>();

        if (checkAuthentification(requete)) {
            for (PoneOffreEnergie poneOffreEnergie : Main.marcheEnergie.getOffreEnergies()) {
                if (Main.marcheEnergie.filtrerEnergie(requete,poneOffreEnergie) &&
                        (requete.getDispoAt().isEmpty() || Main.marcheEnergie.filtrerDisponibleAt(requete,poneOffreEnergie))) {
                    liste.add(poneOffreEnergie);
                }
            }

            // creation d'une var reponse de type : EnergieListeReponse
            return new EnergieListeReponse(liste.toArray(new PoneOffreEnergie[0]));
        } else {
            return new UnreconizedReponse("Authentification échouée");
        }
    }

    public boolean checkAuthentification(AbstractRequete requete) {
        for (Identifier id : ids_storage) {
            if (id.checkID(requete.getAuthID())) {
                if (id instanceof TareID && requete.getAuthID() instanceof TareID) return true;
                else return id instanceof PoneID && requete.getAuthID() instanceof PoneID;
            }
        }

        return false;
    }

}
