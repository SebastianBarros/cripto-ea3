package com.ea3;

import javax.crypto.Cipher;
import javax.crypto.KeyAgreement;
import javax.crypto.spec.SecretKeySpec;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;



public class Person {

    private PrivateKey privateKey;
    private PublicKey  publicKey;
    private PublicKey  receivedPublicKey;
    private byte[]     secretKey;
    private String     secretMessage;

    /*
    We initialize a cipher and use it to encrypt the given message. Of course we use the message in bytes.
    After that, the encrypted message is sent to the corresponding person.
     */

    public void encryptAndSendMessage(final String message, final Person person) {

        try {
            final SecretKeySpec keySpec = new SecretKeySpec(secretKey, "DES");
            final Cipher        cipher  = Cipher.getInstance("DES/ECB/PKCS5Padding");

            cipher.init(Cipher.ENCRYPT_MODE, keySpec);

            final byte[] encryptedMessage = cipher.doFinal(message.getBytes());

            person.receiveAndDecryptMessage(encryptedMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
    With the recived key, we initialize the Diffie-Hellman algorithm and store the secret key.
     */
    public void generateCommonSecretKey() {

        try {
            final KeyAgreement keyAgreement = KeyAgreement.getInstance("DH");
            keyAgreement.init(privateKey);
            keyAgreement.doPhase(receivedPublicKey, true);

            secretKey = shortenSecretKey(keyAgreement.generateSecret());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /*
        Generates both the private and public key with a keysize of 1024.
        This is done using the instance for Diffie-Hellman of the KeyPairGenerator class
        The numbers used for generating the key will be secured random as the method initialize explains
     */
    public void generateKeys() {

        try {
            final KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("DH");
            keyPairGenerator.initialize(1024);

            final KeyPair keyPair = keyPairGenerator.generateKeyPair();

            privateKey = keyPair.getPrivate();
            publicKey  = keyPair.getPublic();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public PublicKey getPublicKey() {

        return publicKey;
    }

    public String getPrivateKey() {
        return privateKey.toString();
    }

    /*
    Since we receive an encrypted byte array, the only thing left to do is to re-encrypt (that is decrypt with diffie-hellman) it and now we have it!
     */

    public void receiveAndDecryptMessage(final byte[] message) {

        try {

            final SecretKeySpec keySpec = new SecretKeySpec(secretKey, "DES");
            final Cipher        cipher  = Cipher.getInstance("DES/ECB/PKCS5Padding");

            cipher.init(Cipher.DECRYPT_MODE, keySpec);

            secretMessage = new String(cipher.doFinal(message));
        } catch (Exception e) {
            secretMessage = new String(message);
        }
    }

    /*
    We get and store the public key of the person we want to send messages with
     */
    public void receivePublicKeyFrom(final Person person) {

        receivedPublicKey = person.getPublicKey();
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    public void whisperTheSecretMessage() {

        System.out.println(secretMessage);
    }

    /**
     * 1024 bit symmetric key size is so big for DES so we must shorten the key size. You can get first 8 longKey of the
     * byte array or can use a key factory
     *
     * @param   longKey
     *
     * @return
     */
    private byte[] shortenSecretKey(final byte[] longKey) {

        try {

            // Use 8 bytes (64 bits) for DES, 6 bytes (48 bits) for Blowfish
            final byte[] shortenedKey = new byte[8];

            System.arraycopy(longKey, 0, shortenedKey, 0, shortenedKey.length);

            return shortenedKey;

            // Below lines can be more secure
            // final SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            // final DESKeySpec       desSpec    = new DESKeySpec(longKey);
            //
            // return keyFactory.generateSecret(desSpec).getEncoded();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
