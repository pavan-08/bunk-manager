package com.bunkmanager.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.bunkmanager.entity.Attendance;
import com.bunkmanager.entity.Subjects;
import com.bunkmanager.entity.TimeTable;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Pavan on 2/24/2016.
 */
public class DBHelper {

    public static abstract class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME_ATTENDANCE = "attendance";
        public static final String TABLE_NAME_SUBJECTS = "subjects";
        public static final String TABLE_NAME_TIMETABLE = "time_table";
        public static final String COLUMN_NAME_SNAME = "name";
        public static final String COLUMN_NAME_LIMIT = "percent";
        public static final String COLUMN_NAME_LECTURE = "lecture";
        public static final String COLUMN_NAME_SUBJECT = "subject";
        public static final String COLUMN_NAME_TIMESTAMP = "timestamp";
        public static final String COLUMN_NAME_DAY = "day";
        public static final String COLUMN_NAME_STATUS = "status";
    }

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASENAME = "bm.db";
    public static final String SQL_CREATE_TABLE_ATTENDANCE = "CREATE TABLE " + FeedEntry.TABLE_NAME_ATTENDANCE + " ("
            + FeedEntry._ID + " INTEGER PRIMARY KEY, "
            + FeedEntry.COLUMN_NAME_LECTURE + " INTEGER, "
            + FeedEntry.COLUMN_NAME_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP, "
            + FeedEntry.COLUMN_NAME_STATUS + " INTEGER, "
            + FeedEntry.COLUMN_NAME_SUBJECT + " INTEGER, "
            + "FOREIGN KEY (" + FeedEntry.COLUMN_NAME_SUBJECT + ") REFERENCES " + FeedEntry.TABLE_NAME_SUBJECTS + "(" + FeedEntry._ID + "), "
            + "FOREIGN KEY (" + FeedEntry.COLUMN_NAME_LECTURE + ") REFERENCES " + FeedEntry.TABLE_NAME_TIMETABLE + "(" + FeedEntry._ID + "))";

    public static final String SQL_CREATE_TABLE_SUBJECTS = "CREATE TABLE " + FeedEntry.TABLE_NAME_SUBJECTS + " ("
            + FeedEntry._ID + " INTEGER PRIMARY KEY, "
            + FeedEntry.COLUMN_NAME_SNAME + " TEXT, "
            + FeedEntry.COLUMN_NAME_LIMIT + " INTEGER)";

    public static final String SQL_CREATE_TABLE_TIMETABLE = "CREATE TABLE " + FeedEntry.TABLE_NAME_TIMETABLE + " ("
            + FeedEntry._ID + " INTEGER PRIMARY KEY, "
            + FeedEntry.COLUMN_NAME_DAY + " TEXT, "
            + FeedEntry.COLUMN_NAME_SUBJECT + " INTEGER, "
            + "FOREIGN KEY (" + FeedEntry.COLUMN_NAME_SUBJECT + ") REFERENCES " + FeedEntry.TABLE_NAME_SUBJECTS + "(" + FeedEntry._ID + "))";

    public static final String SQL_DELETE_ATTENDANCE = "DROP TABLE IF EXISTS " + FeedEntry.TABLE_NAME_ATTENDANCE;
    public static final String SQL_DELETE_TIMETABLE = "DROP TABLE IF EXISTS " + FeedEntry.TABLE_NAME_TIMETABLE;
    public static final String SQL_DELETE_SUBJECTS = "DROP TABLE IF EXISTS " + FeedEntry.TABLE_NAME_SUBJECTS;
    private Context ref;
    public class DBHandler extends SQLiteOpenHelper {

        public DBHandler(Context context) {
            super(context, DATABASENAME, null, DATABASE_VERSION);
            ref= context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE_TABLE_SUBJECTS);
            db.execSQL(SQL_CREATE_TABLE_TIMETABLE);
            db.execSQL(SQL_CREATE_TABLE_ATTENDANCE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(SQL_DELETE_ATTENDANCE);
            db.execSQL(SQL_DELETE_TIMETABLE);
            db.execSQL(SQL_DELETE_SUBJECTS);
            onCreate(db);
        }

        @Override
        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onUpgrade(db, oldVersion, newVersion);
        }
    }

    private final Context context;
    private DBHandler dbHandler;
    private SQLiteDatabase sqLiteDatabase;

    public DBHelper(Context context) {
        this.context = context;

    }

    public DBHelper open() throws SQLException {
        this.dbHandler = new DBHandler(context);
        this.sqLiteDatabase = dbHandler.getWritableDatabase();
        return this;
    }

    public void close() {
        if(this.dbHandler != null)
            dbHandler.close();
    }

    public long addSubject(String name, int limit) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(FeedEntry.COLUMN_NAME_SNAME, name);
        initialValues.put(FeedEntry.COLUMN_NAME_LIMIT, limit);
        return sqLiteDatabase.insert(FeedEntry.TABLE_NAME_SUBJECTS, null, initialValues);
    }

    public ArrayList<Subjects> getSubjects() {
        ArrayList<Subjects> subjects = new ArrayList<Subjects>();

        Cursor mCursor = sqLiteDatabase.query(FeedEntry.TABLE_NAME_SUBJECTS, new String[]
                        {FeedEntry._ID, FeedEntry.COLUMN_NAME_SNAME, FeedEntry.COLUMN_NAME_LIMIT},
                null, null, null, null, null);
        if(mCursor != null && mCursor.getCount() != 0) {
            mCursor.moveToFirst();
            do {
                Subjects sub = new Subjects();
                sub.setId(mCursor.getInt(0));
                sub.setName(mCursor.getString(1));
                sub.setLimit(mCursor.getInt(2));
                //Toast.makeText(ref,mCursor.getString(1),Toast.LENGTH_SHORT).show();
                subjects.add(sub);
            } while (mCursor.moveToNext());
        }
        return subjects;
    }

    public Subjects getSubject(String name) {
        Subjects sub = new Subjects();
        Cursor mCursor = sqLiteDatabase.query(FeedEntry.TABLE_NAME_SUBJECTS,new String[]
                        {FeedEntry._ID, FeedEntry.COLUMN_NAME_SNAME, FeedEntry.COLUMN_NAME_LIMIT},
                FeedEntry.COLUMN_NAME_SNAME + "=" + "\"" + name + "\"",null,null,null,null);
        if(mCursor != null && mCursor.getCount() != 0) {
            mCursor.moveToFirst();
            do {
                sub.setId(mCursor.getInt(0));
                sub.setName(mCursor.getString(1));
                sub.setLimit(mCursor.getInt(2));
            } while (mCursor.moveToNext());
        }
        return sub;
    }

    public Subjects getSubjectByID(int id) {
        Subjects sub = new Subjects();
        Cursor mCursor = sqLiteDatabase.query(FeedEntry.TABLE_NAME_SUBJECTS,new String[]
                        {FeedEntry._ID, FeedEntry.COLUMN_NAME_SNAME, FeedEntry.COLUMN_NAME_LIMIT},
                FeedEntry._ID + "=" + id,null,null,null,null);
        if(mCursor != null && mCursor.getCount() != 0) {
            mCursor.moveToFirst();
            do {
                sub.setId(mCursor.getInt(0));
                sub.setName(mCursor.getString(1));
                sub.setLimit(mCursor.getInt(2));
            } while (mCursor.moveToNext());
        }
        return sub;
    }

    public int updateSubject(ContentValues contentValues, int id) {
        return sqLiteDatabase.update(FeedEntry.TABLE_NAME_SUBJECTS, contentValues, FeedEntry._ID + "=" + id, null);
    }

    public int deleteSubject(int id) {
        deleteAllAttendanceBySubject(id);
        deleteLectureBySubject(id);
        return sqLiteDatabase.delete(FeedEntry.TABLE_NAME_SUBJECTS, FeedEntry._ID + "=" + id, null);
    }

    public long addLecture(String day, int subject) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(FeedEntry.COLUMN_NAME_DAY, day);
        initialValues.put(FeedEntry.COLUMN_NAME_SUBJECT, subject);
        return sqLiteDatabase.insert(FeedEntry.TABLE_NAME_TIMETABLE, null, initialValues);
    }

    public ArrayList<TimeTable> getLectures(String day) {

        ArrayList<TimeTable> lectures = new ArrayList<TimeTable>();
        Cursor mCursor = sqLiteDatabase.query(FeedEntry.TABLE_NAME_TIMETABLE,new String[]
                        {FeedEntry._ID, FeedEntry.COLUMN_NAME_DAY, FeedEntry.COLUMN_NAME_SUBJECT},
                FeedEntry.COLUMN_NAME_DAY + "=\"" + day + "\"",null,null,null,null);

        if(mCursor != null && mCursor.getCount() != 0) {
            mCursor.moveToFirst();
            do {

                TimeTable lecture = new TimeTable();
                Subjects subject = new Subjects();
                lecture.setId(mCursor.getInt(0));
                lecture.setDay(mCursor.getString(1));
                String sql = "SELECT * FROM " + FeedEntry.TABLE_NAME_SUBJECTS + " WHERE " + FeedEntry._ID + "=" + mCursor.getInt(2);
                Cursor sub = sqLiteDatabase.rawQuery(sql, null);
                //System.out.println(mCursor.getInt(2) + " " + sub.getCount());
                if (sub != null && sub.getCount() != 0) {
                    sub.moveToFirst();
                    subject.setId(sub.getInt(0));
                    subject.setName(sub.getString(1));
                    subject.setLimit(sub.getInt(2));
                    lecture.setSubject(subject);
                    lectures.add(lecture);
                }
            } while (mCursor.moveToNext());
        }
        return lectures;
    }

    public TimeTable getLecture(int id) {
        TimeTable lecture = new TimeTable();
        Cursor mCursor = sqLiteDatabase.query(FeedEntry.TABLE_NAME_TIMETABLE,new String[]
                        {FeedEntry._ID, FeedEntry.COLUMN_NAME_DAY, FeedEntry.COLUMN_NAME_SUBJECT},
                FeedEntry._ID + "=" + id,null,null,null,null);
        if(mCursor != null && mCursor.getCount() != 0) {
            mCursor.moveToFirst();
            do {
                Subjects subject = new Subjects();
                lecture.setId(mCursor.getInt(0));
                lecture.setDay(mCursor.getString(1));
                String sql = "SELECT * FROM " + FeedEntry.TABLE_NAME_SUBJECTS + " WHERE " + FeedEntry._ID + "=" + mCursor.getInt(2);
                Cursor sub = sqLiteDatabase.rawQuery(sql, null);
                //System.out.println(mCursor.getInt(2) + " " + sub.getCount());
                if (sub != null && sub.getCount() != 0) {
                    sub.moveToFirst();
                    subject.setId(sub.getInt(0));
                    subject.setName(sub.getString(1));
                    subject.setLimit(sub.getInt(2));
                    lecture.setSubject(subject);
                }
            } while (mCursor.moveToNext());
        }
        return lecture;
    }

    public String getLectureCount(String day) {
        String sql = "SELECT COUNT(*) AS count FROM " + FeedEntry.TABLE_NAME_TIMETABLE
                + " WHERE " + FeedEntry.COLUMN_NAME_DAY + "= \"" + day + "\"";
        Cursor mCursor = sqLiteDatabase.rawQuery(sql, null);

        if(mCursor != null)
            mCursor.moveToFirst();
        return String.valueOf(mCursor.getInt(0));
    }

    public int updateLecture(ContentValues contentValues, int id) {
        return sqLiteDatabase.update(FeedEntry.TABLE_NAME_TIMETABLE, contentValues, FeedEntry._ID + "=" + id, null);
    }

    public int deleteLecture(int id) {
        return sqLiteDatabase.delete(FeedEntry.TABLE_NAME_TIMETABLE, FeedEntry._ID + "=" + id, null);
    }

    public int deleteLectureBySubject(int subject) {
        return sqLiteDatabase.delete(FeedEntry.TABLE_NAME_TIMETABLE, FeedEntry.COLUMN_NAME_SUBJECT + "=" + subject, null);
    }



    public long addAttendance(int lecture, int status, int subject) {
        ContentValues cv = new ContentValues();
        cv.put(FeedEntry.COLUMN_NAME_LECTURE,lecture);
        cv.put(FeedEntry.COLUMN_NAME_STATUS, status);
        cv.put(FeedEntry.COLUMN_NAME_SUBJECT, subject);
        return sqLiteDatabase.insert(FeedEntry.TABLE_NAME_ATTENDANCE, null, cv);
    }

    public int getAttendanceCount(int lecture, int status) {
        String sql = "SELECT COUNT(" + FeedEntry._ID + ") AS count FROM " + FeedEntry.TABLE_NAME_ATTENDANCE
                + " WHERE " + FeedEntry.COLUMN_NAME_LECTURE + "= \"" + lecture + "\" AND "
                + FeedEntry.COLUMN_NAME_STATUS + "= \"" + status + "\"";
        Cursor mCursor = sqLiteDatabase.rawQuery(sql,null);
        if(mCursor != null)
            mCursor.moveToFirst();
        return mCursor.getInt(0);
    }

    public void deleteAttendance(int lecture, int status) {
        int id;
        String getSql = "SELECT " + FeedEntry._ID + " FROM " + FeedEntry.TABLE_NAME_ATTENDANCE
                + " WHERE " + FeedEntry.COLUMN_NAME_LECTURE + "="
                + lecture + " AND " + FeedEntry.COLUMN_NAME_STATUS + "=" + status;
        Cursor mCursor = sqLiteDatabase.rawQuery(getSql, null);
        if(mCursor != null) {
            mCursor.moveToLast();
            id = mCursor.getInt(0);
            String delSql = "DELETE FROM " + FeedEntry.TABLE_NAME_ATTENDANCE + " WHERE " + FeedEntry._ID + "=" + id;
            sqLiteDatabase.execSQL(delSql);
        }
    }

    public void deleteAllAttendance(int lecture) {
        String sql = "DELETE FROM " + FeedEntry.TABLE_NAME_ATTENDANCE
                + " WHERE " + FeedEntry.COLUMN_NAME_LECTURE + "=" + lecture;
        sqLiteDatabase.execSQL(sql);
    }

    public void deleteAllAttendanceBySubject(int subject) {
        String sql = "DELETE FROM " + FeedEntry.TABLE_NAME_ATTENDANCE
                + " WHERE " + FeedEntry.COLUMN_NAME_SUBJECT + "=" + subject;
        sqLiteDatabase.execSQL(sql);
    }

    public String getSubjectAttendance(int subjectId, int status) {
        //System.out.println("sub: "+subjectId);
        String sql = "SELECT COUNT(*) AS count FROM " + FeedEntry.TABLE_NAME_ATTENDANCE
                + " WHERE " + FeedEntry.COLUMN_NAME_SUBJECT + "= " + subjectId + " AND "
                + FeedEntry.COLUMN_NAME_STATUS + "= " + status;
        Cursor mCursor = sqLiteDatabase.rawQuery(sql,null);
        //System.out.println(subjectId);
        if(mCursor != null)
            mCursor.moveToFirst();
        //System.out.println("status: " + status + " count: " +mCursor.getInt(0));
        return String.valueOf(mCursor.getInt(0));
    }

    public ArrayList<Attendance> getSubjectAttendanceList(int subjectID, int status) {
        ArrayList<Attendance> attendanceList = new ArrayList<Attendance>();
        String sql = "SELECT "+FeedEntry._ID + ", " +FeedEntry.COLUMN_NAME_LECTURE + ", date(datetime("
                + FeedEntry.COLUMN_NAME_TIMESTAMP + ", 'localtime')), "
                + FeedEntry.COLUMN_NAME_STATUS + ", " + FeedEntry.COLUMN_NAME_SUBJECT
                + " FROM " + FeedEntry.TABLE_NAME_ATTENDANCE
                + " WHERE " + FeedEntry.COLUMN_NAME_SUBJECT + "= " + subjectID + " AND "
                + FeedEntry.COLUMN_NAME_STATUS + "= " + status;
        Cursor mCursor = sqLiteDatabase.rawQuery(sql,null);
        if(mCursor != null && mCursor.getCount() != 0) {
            mCursor.moveToFirst();
            do {
                Attendance attendance = new Attendance();
                attendance.setId(mCursor.getInt(0));
                attendance.setLecture(getLecture(mCursor.getInt(1)));
                attendance.setTimestamp(mCursor.getString(2));
                attendance.setStatus(mCursor.getInt(3));
                attendance.setSubject(getSubjectByID(mCursor.getInt(4)));
                attendanceList.add(attendance);
            } while (mCursor.moveToNext());
        }
        return attendanceList;
    }


    public void deleteAllData() {
        String attendance = "DELETE FROM " + FeedEntry.TABLE_NAME_ATTENDANCE + " WHERE _id > 0";
        String lectures = "DELETE FROM " + FeedEntry.TABLE_NAME_TIMETABLE + " WHERE _id > 0";
        String subjects = "DELETE FROM " + FeedEntry.TABLE_NAME_SUBJECTS + " WHERE _id > 0";
        sqLiteDatabase.execSQL(attendance);
        sqLiteDatabase.execSQL(lectures);
        sqLiteDatabase.execSQL(subjects);
    }
}
