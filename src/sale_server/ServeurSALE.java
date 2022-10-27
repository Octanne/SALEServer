package sale_server;

import sale_server.reponse.AbstractReponse;
import sale_server.reponse.UnreconizedReponse;
import sale_server.reponse.tare.EnergieListeReponse;
import sale_server.requete.AbstractRequete;
import sale_server.requete.pone.AjoutEnergieRequete;
import sale_server.requete.pone.SuppressionEnergieRequete;
import sale_server.requete.tare.EnergieAchatRequete;
import sale_server.requete.tare.EnergieListeRequete;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class ServeurSALE extends Thread{

    private final int portEcoute;
    private DatagramSocket socket;
    private boolean running, isActive;

    public ServeurSALE(int portEcoute) {
        super("ServeurSALE");
        this.portEcoute = portEcoute;
        running = false;
        isActive = false;
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
            DatagramPacket msgRecu = null;
            try {
                byte[] tampon = new byte[8164];
                msgRecu = new DatagramPacket(tampon, tampon.length);
                socket.receive(msgRecu);
            } catch(IOException e) {
                System.err.println("Erreur lors de la réception du message : " + e);
                continue;
            }

            try {
                // Récupération de l'objet requete
                ByteArrayInputStream bais = new ByteArrayInputStream(msgRecu.getData());
                ObjectInputStream ois = new ObjectInputStream(bais);
                Object requeteO = ois.readObject();
                System.out.println("Requete recue : " + requeteO.getClass().getSimpleName());

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
                            DatagramPacket msg = new DatagramPacket(donnees, donnees.length, msgRecu.getAddress(), portEcoute);
                            socket.send(msg);
                            System.out.println("Réponse envoyée : " + reponse.getClass().getSimpleName() +
                                    " (" + donnees.length + " octets)" + " à " + msgRecu.getAddress() + ":" + msgRecu.getPort());
                        } catch(IOException e) {
                            System.err.println("Erreur lors de la sérialisation : " + e);
                            bad_requete("Erreur lors de la sérialisation : " + e, msgRecu);
                        }
                    } else {
                        System.err.println("Requete non reconnue");
                        bad_requete("Requete non reconnue", msgRecu);
                    }
                } else {
                    System.err.println("Requete inconnue");
                    bad_requete("Requete inconnue", msgRecu);
                }
            } catch(ClassNotFoundException e) {
                System.err.println("Objet reçu non reconnu : " + e);
                bad_requete("Objet reçu non reconnu : " + e, msgRecu);
            } catch(IOException e) {
                System.err.println("Erreur lors de la récupération de l'objet : " + e);
                bad_requete("Erreur lors de la récupération de l'objet : " + e, msgRecu);
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

    private void bad_requete(String messageStatus, DatagramPacket msgRecu) {
        UnreconizedReponse rep = new UnreconizedReponse(messageStatus);

        // Transformation en tableau d'octets
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(rep);

            // Création et envoi du segment UDP
            byte[] donnees = baos.toByteArray();
            DatagramPacket msg = new DatagramPacket(donnees, donnees.length, msgRecu.getAddress(), portEcoute);
            socket.send(msg);
            System.out.println("Réponse envoyée : " + rep.getClass().getSimpleName() +
                    " (" + donnees.length + " octets)" + " à " + msgRecu.getAddress() + ":" + msgRecu.getPort());
        } catch(IOException e) {
            System.err.println("Erreur lors de la sérialisation : " + e);
        }
    }

    private AbstractReponse repondre(AjoutEnergieRequete requete) {

        return null;
    }

    private AbstractReponse repondre(SuppressionEnergieRequete requete) {

        return null;
    }

    private AbstractReponse repondre(EnergieAchatRequete requete) {

        return null;
    }

    private AbstractReponse repondre(EnergieListeRequete requete) {

        return null;
    }

}
