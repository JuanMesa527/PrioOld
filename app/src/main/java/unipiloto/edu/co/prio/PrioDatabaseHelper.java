package unipiloto.edu.co.prio;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class PrioDatabaseHelper  extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "prio";
    public static final int DATABASE_VERSION = 1;


    public PrioDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Role (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "NAME TEXT NOT NULL UNIQUE," +
                "DESCRIPTION TEXT NOT NULL" + ")");
        db.execSQL("CREATE TABLE Category (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "NAME TEXT NOT NULL UNIQUE" + ")");
        db.execSQL("CREATE TABLE Locality (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "NAME TEXT NOT NULL UNIQUE," +
                "AREA REAL NOT NULL," +
                "POPULATION INTEGER NOT NULL" + ")");
        db.execSQL("CREATE TABLE User (" +
                "ID INTEGER PRIMARY KEY," +
                "FIRST_NAME TEXT NOT NULL," +
                "LAST_NAME TEXT NOT NULL," +
                "AGE INTEGER NOT NULL," +
                "EMAIL TEXT NOT NULL," +
                "PASSWORD TEXT NOT NULL," +
                "ROLE_ID INTEGER NOT NULL," +
                "LOCALITY_ID INTEGER NOT NULL," +
                "FOREIGN KEY (ROLE_ID) REFERENCES Role(ID)," +
                "FOREIGN KEY (LOCALITY_ID) REFERENCES Locality(ID)" + ")");
        db.execSQL("CREATE TABLE Proyect (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "NAME TEXT NOT NULL UNIQUE," +
                "DESCRIPTION TEXT NOT NULL," +
                "BUDGET REAL NOT NULL," +
                "START_DATE TEXT NOT NULL," +
                "END_DATE TEXT NOT NULL," +
                "CATEGORY_ID INTEGER NOT NULL," +
                "LOCALITY_ID INTEGER NOT NULL," +
                "FOREIGN KEY (CATEGORY_ID) REFERENCES Category(ID)," +
                "FOREIGN KEY (LOCALITY_ID) REFERENCES Locality(ID)" + ")");
        db.execSQL("CREATE TABLE Vote_type (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "NAME TEXT NOT NULL UNIQUE" + ")");
        db.execSQL("CREATE TABLE Vote (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "USER_ID INTEGER NOT NULL," +
                "PROYECT_ID INTEGER NOT NULL," +
                "VOTE_ID INTEGER NOT NULL," +
                "OPINION TEXT," +
                "FOREIGN KEY (USER_ID) REFERENCES User(ID)," +
                "FOREIGN KEY (PROYECT_ID) REFERENCES Proyect(ID)," +
                "FOREIGN KEY (VOTE_ID) REFERENCES Vote_type(ID)" + ")");

        db.execSQL("INSERT INTO Locality (NAME, AREA, POPULATION) VALUES ('Usaquen', 65.54, 503767)");
        db.execSQL("INSERT INTO Locality (NAME, AREA, POPULATION) VALUES ('Chapinero', 35.78, 139701)");
        db.execSQL("INSERT INTO Locality (NAME, AREA, POPULATION) VALUES ('Santa Fe', 44.82, 109195)");

        db.execSQL("INSERT INTO Role (NAME, DESCRIPTION) VALUES ('Ciudadano', 'Revisa las propuestas de proyectos de su zona de residencia')");
        db.execSQL("INSERT INTO Role (NAME, DESCRIPTION) VALUES ('Planeador', 'Encargado de gestionar las propuestas de proyectos de espacio público\n" +
                "agrupadas por zona de la ciudad (localidad)')");
        db.execSQL("INSERT INTO Role (NAME, DESCRIPTION) VALUES ('decisor', 'Consulta los resultados de las votaciones en las diversas zonas.\n" +
                "Analiza la información en un mapa según la ubicación de los votantes\n" +
                "Generar estadísticas como el impacto porcentual del proyecto en términos\n" +
                "del número de participantes y el número de habitantes.')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Role");
        db.execSQL("DROP TABLE IF EXISTS Category");
        db.execSQL("DROP TABLE IF EXISTS Locality");
        db.execSQL("DROP TABLE IF EXISTS User");
        db.execSQL("DROP TABLE IF EXISTS Proyect");
        db.execSQL("DROP TABLE IF EXISTS Vote_type");
        db.execSQL("DROP TABLE IF EXISTS Vote");
        onCreate(db);
    }

    public void initData(){
        SQLiteDatabase db = getWritableDatabase();
        onUpgrade(db, 1, 1);
    }

    public boolean insertUser(String firstName, String lastName, int age, String email, String password, int roleId, int localityId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("FIRST_NAME", firstName);
        contentValues.put("LAST_NAME", lastName);
        contentValues.put("AGE", age);
        contentValues.put("EMAIL", email);
        contentValues.put("PASSWORD", password);
        contentValues.put("ROLE_ID", roleId);
        contentValues.put("LOCALITY_ID", localityId);
        long result = db.insert("User", null, contentValues);
        if (result == -1) {
            return false;
        }
        return true;
    }

    public boolean getLogin(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM User WHERE EMAIL = "+"'"+email+"'"+" AND PASSWORD = "+"'"+password+"'", null);
        if (cursor.getCount() > 0) {
            return true;
        }
        return false;
    }

    public List<String> getAllLocalities() {
        List<String> localities = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT NAME FROM Locality", null);
        if (cursor.moveToFirst()) {
            do {
                localities.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return localities;
    }

    public int getLocalityId(String localityName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT ID FROM Locality WHERE NAME = "+"'"+localityName+"'", null);
        cursor.moveToFirst();
        int id = cursor.getInt(0);
        cursor.close();
        return id;
    }
}
