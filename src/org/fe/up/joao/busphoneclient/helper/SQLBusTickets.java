package org.fe.up.joao.busphoneclient.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLBusTickets extends SQLiteOpenHelper {
	
	private static final int DATABASE_VERSION = 2;
	private static final String DATABASE_NAME = "busphone";
    public static final String TABLE_TICKETS = "tickets";
	public static final String FIELD_TICKET_ID = "id";
	public static final String FIELD_TICKET_UUID = "uuid";
	public static final String FIELD_TICKET_TYPE = "ticket_type";
    private static final String TICKETS_TABLE_CREATE =
                "CREATE TABLE " + TABLE_TICKETS + " (" +
                FIELD_TICKET_ID + " TEXT, " +
                FIELD_TICKET_UUID + " TEXT, " +
                FIELD_TICKET_TYPE + " TEXT);";
    
    SQLiteDatabase db;
	
	
	public SQLBusTickets(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
//		db = getWritableDatabase();
    }
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		 db.execSQL(TICKETS_TABLE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_TICKETS);
        onCreate(db);
	}

}
