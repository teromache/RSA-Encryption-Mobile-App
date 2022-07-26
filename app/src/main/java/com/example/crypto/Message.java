package com.example.crypto;

import java.io.Serializable;

public class Message implements Serializable {

    private int id;
    private String hash_md5;
    private String plaintext;
    private String public_key;
    private String private_key;
    private String encrypted_message;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHash_md5() {
        return hash_md5;
    }

    public void setHash_md5(String hash_md5) {
        this.hash_md5 = hash_md5;
    }

    public String getPlaintext() {
        return plaintext;
    }

    public void setPlaintext(String plaintext) {
        this.plaintext = plaintext;
    }

    public String getPublic_key() {
        return public_key;
    }

    public void setPublic_key(String public_key) {
        this.public_key = public_key;
    }

    public String getPrivate_key() {
        return private_key;
    }

    public void setPrivate_key(String private_key) {
        this.private_key = private_key;
    }

    public String getEncrypted_message() {
        return encrypted_message;
    }

    public void setEncrypted_message(String encrypted_message) {
        this.encrypted_message = encrypted_message;
    }
}
