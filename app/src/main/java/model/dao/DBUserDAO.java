package model.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import model.Message;
import model.UserAcc;
import model.db.DatabaseHelper;

public class DBUserDAO implements IUserDAO {

    private DatabaseHelper mDb;
    private Context context;

    public DBUserDAO(Context context) {
        this.context = context;
        this.mDb = DatabaseHelper.getInstance(context);
    }

    @Override
    public long addUser(UserAcc user) {
        SQLiteDatabase db = mDb.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.USERNAME, user.getUserName());
        values.put(DatabaseHelper.EMAIL, user.getEmail().toString());
        values.put(DatabaseHelper.PASSWORD, user.getPassword());
        values.put(DatabaseHelper.FIRST_NAME, user.getFirstName());
        values.put(DatabaseHelper.LAST_NAME, user.getLastName());
        values.put(DatabaseHelper.CITY, user.getCity());
        values.put(DatabaseHelper.ADDRESS, user.getAddress());
        values.put(DatabaseHelper.TELEPHONE, user.getPhoneNumber());

        long userId = db.insert(DatabaseHelper.USERS, null, values);
        db.close();
        return userId;
    }

    @Override
    public UserAcc getUser(String username) {
        SQLiteDatabase db = mDb.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + DatabaseHelper.USERS
                + "WHERE " + DatabaseHelper.USERNAME + " = \"" + username + "\"";

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();
        long userId = c.getLong(c.getColumnIndex(DatabaseHelper.USER_ID));
        String uname = c.getString(c.getColumnIndex(DatabaseHelper.USERNAME));
        String password = c.getString(c.getColumnIndex(DatabaseHelper.PASSWORD));
        String email = c.getString(c.getColumnIndex(DatabaseHelper.EMAIL));
        String fname = c.getString(c.getColumnIndex(DatabaseHelper.FIRST_NAME));
        String lname = c.getString(c.getColumnIndex(DatabaseHelper.LAST_NAME));
        String city = c.getString(c.getColumnIndex(DatabaseHelper.CITY));
        String address = c.getString(c.getColumnIndex(DatabaseHelper.ADDRESS));
        String phone = c.getString(c.getColumnIndex(DatabaseHelper.TELEPHONE));

        UserAcc user = new UserAcc(uname, password, email, fname, lname, city, address, phone);
        user.setUserId(userId);
        c.close();
        db.close();
        return user;
    }

    @Override
    public UserAcc getUser(long id) {
        SQLiteDatabase db = mDb.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + DatabaseHelper.USERS
                + " WHERE " + DatabaseHelper.USER_ID + " = " + id;

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        String uname = c.getString(c.getColumnIndex(DatabaseHelper.USERNAME));
        String password = c.getString(c.getColumnIndex(DatabaseHelper.PASSWORD));
        String email = c.getString(c.getColumnIndex(DatabaseHelper.EMAIL));
        String fname = c.getString(c.getColumnIndex(DatabaseHelper.FIRST_NAME));
        String lname = c.getString(c.getColumnIndex(DatabaseHelper.LAST_NAME));
        String city = c.getString(c.getColumnIndex(DatabaseHelper.CITY));
        String address = c.getString(c.getColumnIndex(DatabaseHelper.ADDRESS));
        String phone = c.getString(c.getColumnIndex(DatabaseHelper.TELEPHONE));

        UserAcc user = new UserAcc(email, password, uname, fname, lname, phone, city, address);
        user.setUserId(id);
        c.close();
        db.close();
        return user;
    }

    @Override
    public List<UserAcc> getAllUsers() {
        ArrayList<UserAcc> users = new ArrayList<UserAcc>();
        String query = "SELECT * FROM " + DatabaseHelper.USERS;

        SQLiteDatabase db = mDb.getReadableDatabase();
        Cursor c = db.rawQuery(query, null);

        if (c.moveToFirst()) {
            do {
                String uname = c.getString(c.getColumnIndex(DatabaseHelper.USERNAME));
                String password = c.getString(c.getColumnIndex(DatabaseHelper.PASSWORD));
                String email = c.getString(c.getColumnIndex(DatabaseHelper.EMAIL));
                String fname = c.getString(c.getColumnIndex(DatabaseHelper.FIRST_NAME));
                String lname = c.getString(c.getColumnIndex(DatabaseHelper.LAST_NAME));
                String city = c.getString(c.getColumnIndex(DatabaseHelper.CITY));
                String address = c.getString(c.getColumnIndex(DatabaseHelper.ADDRESS));
                String phone = c.getString(c.getColumnIndex(DatabaseHelper.TELEPHONE));

                UserAcc user = new UserAcc(uname, password, email, fname, lname, city, address, phone);
                users.add(user);
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return users;
    }

    @Override
    public void deleteUser(UserAcc user) {
        SQLiteDatabase db = mDb.getWritableDatabase();
        db.delete(DatabaseHelper.USERS, DatabaseHelper.USERNAME + " = ?",
                new String[]{user.getUserName()});

        db.close();
    }

    @Override
    public long updateUser(UserAcc user) {
        SQLiteDatabase db = mDb.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.USERNAME, user.getUserName());
        values.put(DatabaseHelper.EMAIL, user.getEmail().toString());
        values.put(DatabaseHelper.PASSWORD, user.getPassword());
        values.put(DatabaseHelper.FIRST_NAME, user.getFirstName());
        values.put(DatabaseHelper.LAST_NAME, user.getLastName());
        values.put(DatabaseHelper.CITY, user.getCity());
        values.put(DatabaseHelper.ADDRESS, user.getAddress());
        values.put(DatabaseHelper.TELEPHONE, user.getPhoneNumber());

        long userId = db.update(DatabaseHelper.USERS, values, DatabaseHelper.USERNAME + " = ? ", new String[]{user.getUserName()});
        db.close();
        return userId;
    }

    @Override
    public long updateUser(long userId, String fname, String lname, String phone, String city, String address) {
        SQLiteDatabase db = mDb.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.FIRST_NAME, fname);
        values.put(DatabaseHelper.LAST_NAME, lname);
        values.put(DatabaseHelper.CITY, city);
        values.put(DatabaseHelper.ADDRESS, address);
        values.put(DatabaseHelper.TELEPHONE, phone);

        long result = db.update(DatabaseHelper.USERS, values, DatabaseHelper.USER_ID + " = ? ", new String[]{String.valueOf(userId)});
        db.close();
        return result;
    }

    @Override
    public boolean checkUsername(String username) {
        SQLiteDatabase db = mDb.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + DatabaseHelper.USERS
                + " WHERE " + DatabaseHelper.USERNAME + " = \"" + username + "\"";

        Cursor c = db.rawQuery(selectQuery, null);


        if (c != null && c.moveToFirst()) {
            db.close();
            c.close();
            return true;
        } else {
            db.close();
            c.close();
            return false;
        }
    }

    @Override
    public boolean checkUserEmail(String email) {
        SQLiteDatabase db = mDb.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + DatabaseHelper.USERS
                + " WHERE " + DatabaseHelper.EMAIL + " = \"" + email + "\"";

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null && c.moveToFirst()) {
            db.close();
            return true;
        } else {
            db.close();
            return false;
        }
    }

    @Override
    public UserAcc checkLogin(String email, String password) {
        SQLiteDatabase db = mDb.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + DatabaseHelper.USERS
                + " WHERE " + DatabaseHelper.EMAIL + " = \"" + email
                + "\" AND " + DatabaseHelper.PASSWORD + " = \"" + password + "\"";

        Cursor c = db.rawQuery(selectQuery, null);


        UserAcc user = null;

        if (c.moveToFirst()) {
            long id = c.getLong(c.getColumnIndex(DatabaseHelper.USER_ID));
            String uname = c.getString(c.getColumnIndex(DatabaseHelper.USERNAME));
            String upassword = c.getString(c.getColumnIndex(DatabaseHelper.PASSWORD));
            String uemail = c.getString(c.getColumnIndex(DatabaseHelper.EMAIL));
            String fname = c.getString(c.getColumnIndex(DatabaseHelper.FIRST_NAME));
            String lname = c.getString(c.getColumnIndex(DatabaseHelper.LAST_NAME));
            String city = c.getString(c.getColumnIndex(DatabaseHelper.CITY));
            String address = c.getString(c.getColumnIndex(DatabaseHelper.ADDRESS));
            String phone = c.getString(c.getColumnIndex(DatabaseHelper.TELEPHONE));

            user = new UserAcc(uemail, upassword, uname, fname, lname, phone, city, address);
            user.setUserId(id);
        }

        c.close();
        db.close();
        return user;
    }

    public boolean checkPassword(long userID, String password) {
        SQLiteDatabase db = mDb.getReadableDatabase();
        String query = "SELECT " + DatabaseHelper.PASSWORD + " FROM " + DatabaseHelper.USERS + " WHERE " + DatabaseHelper.USER_ID + " = " + userID;
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        String pass = c.getString(c.getColumnIndex(DatabaseHelper.PASSWORD));
        c.close();
        return password.equals(pass);

    }

    public long updateEmail(long userId, String email) {
        SQLiteDatabase db = mDb.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.EMAIL, email);

        long result = db.update(DatabaseHelper.USERS, values, DatabaseHelper.USER_ID + " = ?", new String[]{(String.valueOf(userId))});
        return result;
    }

    public long updatePassword(long userId, String password) {
        SQLiteDatabase db = mDb.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.PASSWORD, password);

        long result = db.update(DatabaseHelper.USERS, values, DatabaseHelper.USER_ID + " = ?", new String[]{(String.valueOf(userId))});
        return result;
    }

    @Override
    public long sendMessage(Message msg) {
        SQLiteDatabase db = mDb.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.SENDER_ID, msg.getSenderId());
        values.put(DatabaseHelper.RECEIVER_ID, msg.getReceiverId());
        values.put(DatabaseHelper.TITLE, msg.getHeading());
        values.put(DatabaseHelper.CONTENT, msg.getText());
        values.put(DatabaseHelper.DATE, String.valueOf(msg.getDate()));

        long id = db.insert(DatabaseHelper.MESSAGES, null, values);
        return id;
    }

    @Override
    public ArrayList<Message> getSentMessages(long userId) {
        SQLiteDatabase db = mDb.getReadableDatabase();
        String query = "SELECT * FROM " + DatabaseHelper.MESSAGES
                + " WHERE " + DatabaseHelper.SENDER_ID + " = " + userId;
        Cursor c = db.rawQuery(query, null);
        ArrayList<Message> messages = new ArrayList<Message>();

        if (c.moveToFirst()) {
            do {
                long messageId = c.getLong(c.getColumnIndex(DatabaseHelper.MESSAGE_ID));
                long receiverId = c.getLong(c.getColumnIndex(DatabaseHelper.RECEIVER_ID));
                String title = c.getString(c.getColumnIndex(DatabaseHelper.TITLE));
                String content = c.getString(c.getColumnIndex(DatabaseHelper.CONTENT));
                String date = c.getString(c.getColumnIndex(DatabaseHelper.DATE));
                Message msg = new Message(messageId, userId, receiverId, title, content);
                messages.add(msg);
            }
            while (c.moveToNext());
        }

        db.close();
        return messages;
    }

    @Override
    public ArrayList<Message> getReceivedMessages(long userId) {
        SQLiteDatabase db = mDb.getReadableDatabase();
        String query = "SELECT * FROM " + DatabaseHelper.MESSAGES
                + " WHERE " + DatabaseHelper.RECEIVER_ID + " = " + userId;
        Cursor c = db.rawQuery(query, null);
        ArrayList<Message> messages = new ArrayList<Message>();

        if (c.moveToFirst()) {
            do {
                long messageId = c.getLong(c.getColumnIndex(DatabaseHelper.MESSAGE_ID));
                long senderId = c.getLong(c.getColumnIndex(DatabaseHelper.SENDER_ID));
                String title = c.getString(c.getColumnIndex(DatabaseHelper.TITLE));
                String content = c.getString(c.getColumnIndex(DatabaseHelper.CONTENT));
                String date = c.getString(c.getColumnIndex(DatabaseHelper.DATE));
                Message msg = new Message(messageId, senderId, userId, title, content);
                messages.add(msg);
            }
            while (c.moveToNext());
        }

        db.close();
        return messages;
    }

    @Override
    public Message getMessage(long id) {
        SQLiteDatabase db = mDb.getReadableDatabase();
        String query = "SELECT * FROM " + DatabaseHelper.MESSAGES
                + " WHERE " + DatabaseHelper.MESSAGE_ID + " = " + id;
        Message m = null;
        Cursor c = db.rawQuery(query, null);
        if (c.moveToFirst()) {
            long senderId = c.getLong(c.getColumnIndex(DatabaseHelper.SENDER_ID));
            long receiverId = c.getLong(c.getColumnIndex(DatabaseHelper.RECEIVER_ID));
            String title = c.getString(c.getColumnIndex(DatabaseHelper.TITLE));
            String content = c.getString(c.getColumnIndex(DatabaseHelper.CONTENT));
            String date = c.getString(c.getColumnIndex(DatabaseHelper.DATE));

            m = new Message(id, senderId, receiverId, title, content);
        }
        c.close();
        db.close();
        return m;
    }
}
