package chiffrer.rsa;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.file.Files;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class ChiffreurRSA {

    private final PublicKey myPublicKey;
    private final PrivateKey myPrivateKey;
    private PublicKey distantePublicKey;
    private final String passePhrase;

    private ChiffreurRSA(PublicKey myPublicKey, PrivateKey myPrivateKey, PublicKey distantePublicKey, String passePhrase) {
        this.myPublicKey = myPublicKey;
        this.myPrivateKey = myPrivateKey;
        this.distantePublicKey = distantePublicKey;
        this.passePhrase = passePhrase;
    }

    public boolean distantePublicKeyIsSet() {
        return distantePublicKey != null;
    }

    private static KeyPair getKeyPairSave(String ownRSAKeyPathFolder) throws NoKeySaveExeption {
        PublicKey clePublique;
        try {
            File fichier = new File(ownRSAKeyPathFolder + "/key.pub");
            byte[] donnees = Files.readAllBytes(fichier.toPath());
            X509EncodedKeySpec spec = new X509EncodedKeySpec(donnees);
            KeyFactory usine = KeyFactory.getInstance("RSA");
            clePublique = usine.generatePublic(spec);
        } catch(IOException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new NoKeySaveExeption();
        }
        PrivateKey clePrivee;
        try {
            File fichier = new File(ownRSAKeyPathFolder + "/key");
            byte[] donnees = Files.readAllBytes(fichier.toPath());
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(donnees);
            KeyFactory usine = KeyFactory.getInstance("RSA");
            clePrivee = usine.generatePrivate(spec);
        } catch(IOException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new NoKeySaveExeption();
        }

        return new KeyPair(clePublique, clePrivee);
    }

    private static KeyPair getKeyPairGenerateAndSave(String ownRSAKeyPathFolder)  {
        File folder = new File(ownRSAKeyPathFolder);
        if (!folder.exists()) {
            boolean createSuc = folder.mkdir();
            if (!createSuc) {
                throw new RuntimeException("Can't create folder for RSA key");
            }
        }

        KeyPairGenerator keyGen;
        try {
            keyGen = KeyPairGenerator.getInstance("RSA");
        } catch(NoSuchAlgorithmException e) {
            throw new RuntimeException("Algorithme RSA inconnu : " + e);
        }
        keyGen.initialize(2048);
        KeyPair pair = keyGen.generateKeyPair();

        try {
            File fichier = new File(ownRSAKeyPathFolder + "/key.pub");
            fichier.createNewFile();
            FileOutputStream fos = new FileOutputStream(fichier);
            fos.write(pair.getPublic().getEncoded());
            fos.close();
        } catch(IOException e) {
            throw new RuntimeException("Erreur lors de la lecture de la clé : " + e);
        }

        try {
            File fichier = new File(ownRSAKeyPathFolder + "/key");
            fichier.createNewFile();
            FileOutputStream fos = new FileOutputStream(fichier);
            fos.write(pair.getPrivate().getEncoded());
            fos.close();
        } catch(IOException e) {
            throw new RuntimeException("Erreur lors de la lecture de la clé : " + e);
        }

        return pair;
    }

    public static ChiffreurRSA createChiffreurRSA(String ownRSAKeyPathFolder, String symetriquePassePhrase) {
        KeyPair keyPair;
        try {
            keyPair = getKeyPairSave(ownRSAKeyPathFolder);
        } catch (NoKeySaveExeption e) {
            keyPair = getKeyPairGenerateAndSave(ownRSAKeyPathFolder);
        }

        return new ChiffreurRSA(keyPair.getPublic(), keyPair.getPrivate(), null, symetriquePassePhrase);
    }

    public MessageChiffreRSA genMessageToSendPublicKey() {
        try {
            // Transforme l'objet en tableau de bytes
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutput out;
            out = new ObjectOutputStream(bos);
            out.writeObject(myPublicKey);
            byte[] message = bos.toByteArray();
            out.close();

            // Chiffrer le message
            SecretKeySpec specification = new SecretKeySpec(passePhrase.getBytes(), "AES");
            try {
                Cipher chiffreur = Cipher.getInstance("AES");
                chiffreur.init(Cipher.ENCRYPT_MODE, specification);
                return new MessageChiffreRSA(passePhrase, chiffreur.doFinal(message));
            } catch(NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException |
                    IllegalBlockSizeException | BadPaddingException e) {
                throw new IOException("Erreur lors du chiffrement : " + e);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void regDistantePublicKeyFromMessage(MessageChiffreRSA messageChiffreRSA) {
        if (messageChiffreRSA.isInitConnection()) {
            byte[] messageDechiffre;
            SecretKeySpec specification = new SecretKeySpec(passePhrase.getBytes(), "AES");
            try {
                Cipher dechiffreur = Cipher.getInstance("AES");
                dechiffreur.init(Cipher.DECRYPT_MODE, specification);
                messageDechiffre = dechiffreur.doFinal(messageChiffreRSA.getMessageChiffre());
            } catch(NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException |
                    IllegalBlockSizeException | BadPaddingException e) {
                throw new RuntimeException("Erreur lors du déchiffrement : " + e);
            }
            try {
                ByteArrayInputStream bis = new ByteArrayInputStream(messageDechiffre);
                ObjectInput in;
                in = new ObjectInputStream(bis);
                Object obj = in.readObject();

                if (obj instanceof PublicKey) {
                    distantePublicKey = (PublicKey) obj;
                } else {
                    throw new RuntimeException("Object is not a PublicKey");
                }

                bis.close();
                in.close();
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public MessageChiffreRSA chiffrer(Object objectToChiffre) throws NoDistanteKeyExeption {
        if (distantePublicKeyIsSet()) {
            try {
                // Créer le bytes[] depuis l'objet à envoyer
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ObjectOutput out = new ObjectOutputStream(bos);
                out.writeObject(objectToChiffre);
                byte[] message = bos.toByteArray();
                out.close();

                // Chiffrer le message
                Cipher chiffreur = Cipher.getInstance("RSA");
                chiffreur.init(Cipher.ENCRYPT_MODE, distantePublicKey);
                return new MessageChiffreRSA(chiffreur.doFinal(message));
            } catch(NoSuchAlgorithmException | NoSuchPaddingException |
                    InvalidKeyException | IllegalBlockSizeException |
                    BadPaddingException | IOException e) {
                throw new RuntimeException("Erreur lors du chiffrement : " + e);
            }
        } else {
            throw new NoDistanteKeyExeption();
        }
    }

    public Object dechiffrer(MessageChiffreRSA messageChiffreRSA) throws ClassNotFoundException {
        try {
            // Déchiffrer le message
            Cipher dechiffreur = Cipher.getInstance("RSA");
            dechiffreur.init(Cipher.DECRYPT_MODE, myPrivateKey);
            byte[] messageDechiffre = dechiffreur.doFinal(messageChiffreRSA.getMessageChiffre());

            // Créer l'objet depuis le bytes[]
            ByteArrayInputStream bis = new ByteArrayInputStream(messageDechiffre);
            ObjectInput in = new ObjectInputStream(bis);
            Object objectDechiffre = in.readObject();
            in.close();

            return objectDechiffre;
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException |
                 BadPaddingException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static class NoKeySaveExeption extends Exception {

    }

    public static class NoDistanteKeyExeption extends Exception {

    }
}