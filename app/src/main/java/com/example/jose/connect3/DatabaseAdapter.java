package com.example.jose.connect3;
import java.util.ArrayList;
        import android.content.ContentValues;
        import android.content.Context;
        import android.database.Cursor;
        import android.database.SQLException;
        import android.database.sqlite.SQLiteDatabase; import android.database.sqlite.SQLiteOpenHelper; import android.database.sqlite.SQLiteQueryBuilder; import android.util.Log;
public class DatabaseAdapter {
    private static final String DEBUG_TAG = "DatabaseAdapter";
    private static final String DATABASE_NAME = "ccc.db";
    private static final int DATABASE_VERSION = 1;
    private DatabaseHelper helper;
    private SQLiteDatabase db;

    public DatabaseAdapter(Context context) {
        helper = new DatabaseHelper(context);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {
        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        public void onCreate(SQLiteDatabase db) {
            createTable(db);
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + UserTable.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + RoundTable.TABLE_NAME);
            Log.w(DEBUG_TAG, "Upgrading database from version " + oldVersion + " to " + newVersion + ". All data will be deleted");
            createTable(db);
        }

        private void createTable(SQLiteDatabase db) {
            String str1 = "CREATE TABLE " + UserTable.TABLE_NAME + " ("
                    + UserTable.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + UserTable.NAME + " TEXT UNIQUE, "
                    + UserTable.PASSWORD + " TEXT);";
            String str2 = "CREATE TABLE " + RoundTable.TABLE_NAME + " (" + RoundTable.ID
                    + " INTEGER REFERENCES " + UserTable.TABLE_NAME + ", " + RoundTable.PARTIDA + " INTEGER, "
                    + RoundTable.DURACION + " INTEGER, "
                    + RoundTable.NPIEZAS + " INTEGER, "
                    + RoundTable.FECHA + " DATE, PRIMARY KEY ("
                    + RoundTable.ID + ", " + RoundTable.PARTIDA + "));";
            try {
                db.execSQL(str1);
                db.execSQL(str2);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public DatabaseAdapter open() throws SQLException { db = helper.getWritableDatabase();
        return this;
    }
    public void close() {
        db.close();
    }
    public long insertUser(String username, String password) { ContentValues values = new ContentValues(); values.put(UserTable.NAME, username); values.put(UserTable.PASSWORD, password);
        return db.insert(UserTable.TABLE_NAME, null, values); }
    public boolean deleteUser(long id) {
        db.delete(RoundTable.TABLE_NAME, RoundTable.ID + "=" + id, null);
        return db.delete(UserTable.TABLE_NAME, UserTable.ID + "=" + id, null) > 0;
    }
    public Cursor getAllUsers() {
        return db.query(UserTable.TABLE_NAME,
                new String[] { UserTable.ID, UserTable.NAME, UserTable.PASSWORD }, null, null, null, null, null);
    }
    public boolean isRegistered(String username, String password) { Cursor cursor = db.query(UserTable.TABLE_NAME,
            new String[] { UserTable.NAME, UserTable.PASSWORD },
            UserTable.NAME + " = '" + username + "' AND " + UserTable.PASSWORD
                    + "= '" + password + "'", null, null, null, UserTable.NAME + " DESC");
        int count = cursor.getCount();
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        if (count == 0)
            return false;
        else
            return true;
    }
    public boolean insertData(String username, int duration, int n_tiles) {
        Cursor cursorId = db.query(UserTable.TABLE_NAME,
                new String[] { UserTable.ID, UserTable.NAME }, UserTable.NAME + " = '" + username + "'", null, null, null, UserTable.NAME + " DESC");
        cursorId.moveToFirst();
        int index = cursorId.getColumnIndex(UserTable.ID); int id = cursorId.getInt(index);
        try {
            Cursor cursorPartida = db.query(RoundTable.TABLE_NAME,
                    new String[] {"MAX(" + RoundTable.PARTIDA + ")" }, RoundTable.ID + " = '" + id + "'", null, RoundTable.ID, null, RoundTable.PARTIDA);
            int maxPartida = cursorPartida.moveToFirst() ? cursorPartida.getInt(0) : 0;
            ContentValues values = new ContentValues(); values.put(RoundTable.ID, id); values.put(RoundTable.PARTIDA, maxPartida + 1); values.put(RoundTable.DURACION, duration); values.put(RoundTable.NPIEZAS, n_tiles); db.insert(RoundTable.TABLE_NAME, null, values);

        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    public ArrayList<String> getAllRounds() { ArrayList<String> list = new ArrayList<String>();
        Cursor cursor = db.rawQuery("SELECT " + UserTable.NAME + "," + RoundTable.PARTIDA + ","
                + RoundTable.DURACION + ","
                + RoundTable.NPIEZAS + " "
                + "FROM " + RoundTable.TABLE_NAME + " AS r ,"
                + UserTable.TABLE_NAME + " AS u "
                + "WHERE r." + RoundTable.ID + "=u." + UserTable.ID , null);
        while (cursor.moveToNext()) {
            int iName = cursor.getColumnIndex(UserTable.NAME);
            int iPartida = cursor.getColumnIndex(RoundTable.PARTIDA); int iDuracion = cursor.getColumnIndex(RoundTable.DURACION); int iNPiezas = cursor.getColumnIndex(RoundTable.NPIEZAS);
            String str = cursor.getString(iName) + " "
                    + cursor.getInt(iPartida) + " "
                    + cursor.getInt(iDuracion) + " "
                    + cursor.getInt(iNPiezas);
            list.add(str);
        }
        return list;
    }
}
