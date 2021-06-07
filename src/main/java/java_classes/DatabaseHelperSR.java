package java_classes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelperSR extends SQLiteOpenHelper {
    //Initialise Database Name and Table Name
    private static final String DATABASE_NAME = "database_sr";
    private static final String TABLE_NAME = "table_sr";

    public DatabaseHelperSR(Context context){
        super(context,DATABASE_NAME,null,1);
    }
    @Override
    public void onCreate(SQLiteDatabase data) {
        String createTable = "create table if not exists " + TABLE_NAME +
                "(id INTEGER PRIMARY KEY, txt TEXT)";
        data.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase data, int previous, int next) {
        data.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(data);
    }

    public boolean addText(String input){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("txt",input);
        sqLiteDatabase.insert(TABLE_NAME,null,contentValues);
        return true;
    }

    public boolean clearTable(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(TABLE_NAME,null,null);
        return true;
    }
    public ArrayList getAllText(){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        ArrayList<String> StringArray = new ArrayList<String>();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from "+TABLE_NAME,null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            StringArray.add(cursor.getString(cursor.getColumnIndex("txt")));
            cursor.moveToNext();
        }
        return StringArray;
    }
}
