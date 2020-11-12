package com.ea3;

public class Main {

    public static void main(final String[] args) throws Exception {

        new Main().init();
    }

    private void init() {

        final Person seba = new Person();
        final Person gonza = new Person();
        //the woman in the middle
        final Person luciaMaiteVazquez = new Person();


        //persons generate and print their keys
        seba.generateKeys();
        System.out.println("Im Seba and my public Key is: " + seba.getPublicKey().toString());
        gonza.generateKeys();
        System.out.println("Im Gonza and my public Key is: " + gonza.getPublicKey().toString());
        //our hated impostor also generates her own key
        luciaMaiteVazquez.generateKeys();
        System.out.println("Im Lucia and my public Key is: " + luciaMaiteVazquez.getPublicKey().toString());

        //seba sends his key to gonza and gonza to seba. Lucia also gets seba's key somehow
        seba.receivePublicKeyFrom(gonza);
        gonza.receivePublicKeyFrom(seba);
        luciaMaiteVazquez.receivePublicKeyFrom((seba));

        //each one generates their own random key
        seba.generateCommonSecretKey();
        System.out.println("Im seba and my secret key is " +  seba.getPrivateKey());
        gonza.generateCommonSecretKey();
        System.out.println("Im Gonza and my secret key is " +  gonza.getPrivateKey());
        luciaMaiteVazquez.generateCommonSecretKey();
        System.out.println("Im Lucia and my secret key is " +  luciaMaiteVazquez.getPrivateKey());

        seba.encryptAndSendMessage("Hey Gonza, hope that damn Lucia aint spying on us", gonza);
        //now she receives the message sent by seba to gonza
        seba.encryptAndSendMessage("Hey Gonza, hope that damn Lucia aint spying on us", luciaMaiteVazquez);

        //gonza tells us the decrypted message he got from seba
        gonza.whisperTheSecretMessage();

        //but of course she can't decrypt it
        System.out.println("I am Lucia and I cannot decrypt the message: ");
        luciaMaiteVazquez.whisperTheSecretMessage();
    }
}
