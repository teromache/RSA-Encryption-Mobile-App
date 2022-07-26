package com.example.crypto;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.crypto.Message;
import com.example.crypto.CryptoDB;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;


import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;

public class ReceiverActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText hashMd5,plaintext,privatekey,publickey,encrypt_message,decrypt_message,comparison;
    private Message msg;
    private CryptoDB cdb;
    //String decryptedMsg = "";
    String pk = "";
    String prk = "";
    String pt = "";
    PublicKey publicKey = null;
    PrivateKey privateKey = null;
    String priKey = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receiver);

        Button button1 = findViewById(R.id.button1);
        Button button2 = findViewById(R.id.button2);


        //plaintext = findViewById(R.id.plaintext);
        hashMd5 = findViewById(R.id.hash);
        publickey = findViewById(R.id.public_key);
        //privatekey = findViewById(R.id.private_key);
        encrypt_message = findViewById(R.id.encrypt_message);
        decrypt_message = findViewById(R.id.decrypt_message);
        comparison = findViewById(R.id.test);

        button1.setOnClickListener(this);
        button2.setOnClickListener(this);

        cdb = new CryptoDB(this);
        Cursor cursor = cdb.readData();
        if(cursor.getCount()==0)
        {
            Toast.makeText(getApplicationContext(),"No data",Toast.LENGTH_SHORT).show();
        }
        else
            while(cursor.moveToNext())
            {
                //plaintext.setText(cursor.getString(1));
                pt = cursor.getString(1);
                hashMd5.setText(cursor.getString(2));
                publickey.setText(cursor.getString(3));
                priKey = cursor.getString(4);
                //privatekey.setText(cursor.getString(4));
                encrypt_message.setText(cursor.getString(5));
            }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            //case1 decrypt
            case R.id.button1:
                try {

                    //prk = privatekey.getText().toString();
                    byte[] decodeprk = Base64.getDecoder().decode(priKey);

                    KeyFactory keyFactory = KeyFactory.getInstance("RSA");
                    PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decodeprk);
                    privateKey = keyFactory.generatePrivate(keySpec);

                    String md5_message = hashMd5.getText().toString();
                    Cipher cipher = Cipher.getInstance("RSA");
                    cipher.init(Cipher.ENCRYPT_MODE, privateKey);
                    //String pt = plaintext.getText().toString();
                    byte[] encrypt = cipher.doFinal(md5_message.getBytes());

                    //process of changing string public key to PublicKey format
                    pk = publickey.getText().toString();
                    byte[] decodepk = Base64.getDecoder().decode(pk);
                    X509EncodedKeySpec keySpecs = new X509EncodedKeySpec(decodepk);
                    publicKey = keyFactory.generatePublic(keySpecs);
                    //end here

                    //decryption process
                    Cipher decryptionCipher = Cipher.getInstance("RSA");
                    decryptionCipher.init(Cipher.DECRYPT_MODE, publicKey);
                    byte[] decryptedMessage = decryptionCipher.doFinal(encrypt);
                    String decryption = new String(decryptedMessage);
                    decrypt_message.setText(decryption);
                    //end here


                } catch (NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException | InvalidKeyException e) {
                    e.printStackTrace();
                } catch (BadPaddingException e) {
                    e.printStackTrace();
                } catch (IllegalBlockSizeException e) {
                    e.printStackTrace();
                }
                break;

            //case 2 compare received hash with decrypt hash
            case R.id.button2:

                String hash = hashMd5.getText().toString();
                String dec = decrypt_message.getText().toString();

                if(hash.equalsIgnoreCase(dec))
                    comparison.setText("Correct");
                else
                    comparison.setText("Wrong");

                break;
        }
    }

}
