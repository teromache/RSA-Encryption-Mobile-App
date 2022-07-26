package com.example.crypto;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class SenderActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText hash,plaintext,privatekey,publickey,encrypt_message,decrypt_message;
    //private TextView output;//,publickey;//privatekey,encrypt_message;
    //private StringBuffer md5;
    //String test = "";

    private String pk = "";
    private String prk = "";
    PublicKey publicKey;
    PrivateKey privateKey;
    byte[] cipherText = null;
    byte[] gg = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sender);

        Button button1 = findViewById(R.id.button1);
        Button button2 = findViewById(R.id.button2);
        Button button3 = findViewById(R.id.button3);
        //Button button4 = findViewById(R.id.button4);
        Button button5 = findViewById(R.id.button5);

        plaintext = findViewById(R.id.plaintext);
        hash = findViewById(R.id.hash);
        encrypt_message = findViewById(R.id.encrypt_message);
        decrypt_message = findViewById(R.id.decrypt_message);
        publickey = findViewById(R.id.public_key);
        privatekey = findViewById(R.id.private_key);

        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        // button4.setOnClickListener(this);
        button5.setOnClickListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //case1 hash md5
            case R.id.button1:
                MessageDigest digest = null;
                try {
                    digest = MessageDigest.getInstance("MD5");
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                digest.update(plaintext.getText().toString().getBytes());
                byte messageDigest[] = digest.digest();

                StringBuffer hexString = new StringBuffer();
                for (int i = 0; i < messageDigest.length; i++) {
                    hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
                    //md5 = hexString;
                    //test = md5.toString();
                    hash.setText(hexString);
                }
                break;

            //case2 get public and private key
            case R.id.button2:
                try {
                    KeyPairGenerator keypairGen = KeyPairGenerator.getInstance("RSA");
                    SecureRandom secureRandom = new SecureRandom();
                    keypairGen.initialize(1024, secureRandom);

                    KeyPair keyPair = keypairGen.generateKeyPair();

                    //RSAPublicKey pkey = (RSAPublicKey)keyPair.getPublic();

                    publicKey = keyPair.getPublic();
                    privateKey = keyPair.getPrivate();

                    pk = Base64.getEncoder().encodeToString(publicKey.getEncoded());
                    publickey.setText(pk);

                    prk = Base64.getEncoder().encodeToString(privateKey.getEncoded());
                    privatekey.setText(prk);

                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                break;

            //case3 encryption
            case R.id.button3:
                try {
                    String md5_message = hash.getText().toString();
                    String pt = plaintext.getText().toString();
                    Cipher cipher = Cipher.getInstance("RSA");
                    cipher.init(Cipher.ENCRYPT_MODE, privateKey);
                    //String pt = plaintext.getText().toString();
                    cipherText = cipher.doFinal(md5_message.getBytes());
                    String encrypt = Base64.getEncoder().encodeToString(cipherText);
                    encrypt_message.setText(encrypt);

                    //Cipher decryptionCipher = Cipher.getInstance("RSA");
                    //decryptionCipher.init(Cipher.DECRYPT_MODE,publicKey);
                    // byte[] decryptedMessage =
                    // decryptionCipher.doFinal(cipherText);
                    // String decryption = new String(decryptedMessage);
                    // decrypt_message.setText(decryption);

                    CryptoDB cdb = new CryptoDB(getApplicationContext());
                    Message msg = new Message();
                    msg.setPlaintext(plaintext.getText().toString());
                    msg.setHash_md5(md5_message);
                    msg.setPublic_key(pk);
                    msg.setPrivate_key(prk);
                    msg.setEncrypted_message(encrypt);
                    cdb.create(msg);

                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (NoSuchPaddingException e) {
                    e.printStackTrace();
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                } catch (BadPaddingException e) {
                    e.printStackTrace();
                } catch (IllegalBlockSizeException e) {
                    e.printStackTrace();
                }
                break;

                    //case 5 go to receicer page
                    case R.id.button5:
                        Intent intent = new Intent(SenderActivity.this, ReceiverActivity.class);
                        startActivity(intent);
                        break;
                }
        }
}