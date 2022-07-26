package com.example.crypto;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class CryptoDB extends SQLiteOpenHelper {

    private Context context;
    private static String dbName = "messageDB";
    private static String tableName = "message_data";
    public static String idColumn = "id";
    public static String columnHashMD5 = "HashMD5";
    private static String columnPlainText = "plaintext";
    private static String columnPublicKey= "public_key";
    private static String columnPrivateKey= "private_key";
    private static String columnEncrypt= "encrypt_message";

    public CryptoDB(Context context){
        super(context, dbName, null,7);

    }

    public CryptoDB(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public void onCreate(SQLiteDatabase sqLiteDatabase){
        sqLiteDatabase.execSQL("create table " + tableName + "(" +
                idColumn + " integer primary key autoincrement, " +
                columnPlainText + " text," +
                columnHashMD5 + " text, " +
                columnPublicKey + " text," +
                columnPrivateKey + " text, " +
                columnEncrypt + " text " +
                ")");

    }

    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1){
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + tableName);
        onCreate(sqLiteDatabase);
    }

    public boolean create (Message msg)
    {
        boolean result = true;

        try{
            SQLiteDatabase sqLiteDatabase = getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(columnPlainText, msg.getPlaintext());
            contentValues.put(columnHashMD5, msg.getHash_md5());
            contentValues.put(columnPublicKey, msg.getPublic_key());
            contentValues.put(columnPrivateKey, msg.getPrivate_key());
            contentValues.put(columnEncrypt, msg.getEncrypted_message());
            result = sqLiteDatabase.insert(tableName, null, contentValues) > 0;

        } catch (Exception e)
        {
            result = false;
        }
        return result;
    }

    public Cursor readData() {
        //String query = "SELECT * FROM " + tableName2;
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("select * from message_data", null);
        return cursor;
    }

}
