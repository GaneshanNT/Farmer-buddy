package model.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import model.Offer;
import model.UserAcc;
import model.db.DatabaseHelper;

public class DBOfferDAO implements IOfferDAO {

    private DatabaseHelper mDb;
    private DBUserDAO userDAO;

    public DBOfferDAO(Context context) {
        this.mDb = DatabaseHelper.getInstance(context);
        this.userDAO = new DBUserDAO(context);
    }

    @Override
    public long addOffer(Offer offer, long userId, String category) {
        SQLiteDatabase db = mDb.getWritableDatabase();

        long categoryId = getCategory(category);

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.USER_ID, userId);
        values.put(DatabaseHelper.CATEGORY_ID, categoryId);
        values.put(DatabaseHelper.TITLE, offer.getName());
        values.put(DatabaseHelper.PRICE, offer.getPrice());
        values.put(DatabaseHelper.CONDITION, String.valueOf(offer.getCondition()));
        values.put(DatabaseHelper.DESCRIPTION, offer.getDescription());
        values.put(DatabaseHelper.CITY, offer.getCity());
        values.put(DatabaseHelper.IS_ACTIVE, String.valueOf(offer.isActive()));
        values.put(DatabaseHelper.DATE, String.valueOf(offer.getCreationDate()));

        long id = db.insert(DatabaseHelper.OFFERS, null, values);

        ArrayList<byte[]> images = offer.getImages();
        for (int i = 0; i < images.size(); i++) {
            ContentValues vals = new ContentValues();
            vals.put(DatabaseHelper.OFFER_ID, id);
            vals.put(DatabaseHelper.CONTENT, images.get(i));
            if (i != 0)
                vals.put(DatabaseHelper.IS_MAIN, false);
            else
                vals.put(DatabaseHelper.IS_MAIN, true);

            db.insert(DatabaseHelper.IMAGES, null, vals);
        }

        db.close();

        offer.setId(id);
        return id;
    }

    //get category by ID
    @Override
    public String getCategory(long id) {
        SQLiteDatabase db = mDb.getReadableDatabase();
        String selectQuery = "SELECT " + DatabaseHelper.NAME + " FROM " + DatabaseHelper.CATEGORIES
                + " WHERE " + DatabaseHelper.CATEGORY_ID + " = " + id;
        Cursor c = db.rawQuery(selectQuery, null);
        String name = "";
        if (c.moveToFirst()) {
            name = c.getString(c.getColumnIndex(DatabaseHelper.NAME));
        }
        c.close();
        db.close();
        return name;
    }

    // get category by name
    @Override
    public long getCategory(String name) {
        SQLiteDatabase db = mDb.getReadableDatabase();
        String selectQuery = "SELECT " + DatabaseHelper.CATEGORY_ID + " FROM " + DatabaseHelper.CATEGORIES
                + " WHERE " + DatabaseHelper.NAME + " = \"" + name + "\"";
        Cursor c = db.rawQuery(selectQuery, null);
        long id = 0;
        if (c.moveToFirst()) {
            id = c.getLong(c.getColumnIndex(DatabaseHelper.CATEGORY_ID));
        }
        c.close();
        return id;
    }

    // get offer by ID
    @Override
    public Offer getOffer(long id) {
        SQLiteDatabase db = mDb.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + DatabaseHelper.OFFERS
                + " WHERE " + DatabaseHelper.OFFER_ID + " = " + id;

        Cursor c = db.rawQuery(selectQuery, null);
        Offer offer = null;
        if (c.moveToFirst()) {
            long userId = c.getLong(c.getColumnIndex(DatabaseHelper.USER_ID));
            long catId = c.getLong(c.getColumnIndex(DatabaseHelper.CATEGORY_ID));
            String title = c.getString(c.getColumnIndex(DatabaseHelper.TITLE));
            double price = c.getDouble(c.getColumnIndex(DatabaseHelper.PRICE));
            String condition = c.getString(c.getColumnIndex(DatabaseHelper.CONDITION));
            String description = c.getString(c.getColumnIndex(DatabaseHelper.DESCRIPTION));
            String city = c.getString(c.getColumnIndex(DatabaseHelper.CITY));
            boolean active = Boolean.parseBoolean(c.getString(c.getColumnIndex(DatabaseHelper.IS_ACTIVE)));
            String date = c.getString(c.getColumnIndex(DatabaseHelper.DATE));

        /*    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/YYYY");
            Date creationDate = new Date();
            try {
                creationDate = sdf.parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }*/

            UserAcc user = userDAO.getUser(userId);
            String category = getCategory(catId);
            ArrayList<byte[]> images = getImages(id);

            offer = new Offer(user, title, description, price, condition, category, city, active, images, null);
            offer.setId(id);

        }
        c.close();
        db.close();
        return offer;
    }


    // get images by offer id
    @Override
    public ArrayList<byte[]> getImages(long offerId) {
        SQLiteDatabase db = mDb.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + DatabaseHelper.IMAGES
                + " WHERE " + DatabaseHelper.OFFER_ID + " = " + offerId;

        Cursor c = db.rawQuery(selectQuery, null);
        ArrayList<byte[]> images = new ArrayList<byte[]>();

        if (c.moveToFirst()) {
            do {
                byte[] content = c.getBlob(c.getColumnIndex(DatabaseHelper.CONTENT));

                images.add(content);
            } while (c.moveToNext());
        }
        c.close();
        return images;
    }

    @Override
    public ArrayList<Offer> getOffersByCategory(String category) {
        SQLiteDatabase db = mDb.getReadableDatabase();
        long catId = getCategory(category);
        String selectQuery = "SELECT * FROM " + DatabaseHelper.OFFERS
                + " WHERE " + DatabaseHelper.CATEGORY_ID + " = " + catId + " AND " + DatabaseHelper.IS_ACTIVE + " = \"true\"";
        Cursor c = db.rawQuery(selectQuery, null);
        ArrayList<Offer> offers = new ArrayList<Offer>();

        if (c.moveToFirst()) {
            do {
                long offerId = c.getLong(c.getColumnIndex(DatabaseHelper.OFFER_ID));
                long userId = c.getLong(c.getColumnIndex(DatabaseHelper.USER_ID));
                String title = c.getString(c.getColumnIndex(DatabaseHelper.TITLE));
                double price = c.getDouble(c.getColumnIndex(DatabaseHelper.PRICE));
                String condition = c.getString(c.getColumnIndex(DatabaseHelper.CONDITION));
                String description = c.getString(c.getColumnIndex(DatabaseHelper.DESCRIPTION));
                String city = c.getString(c.getColumnIndex(DatabaseHelper.CITY));
                boolean active = Boolean.parseBoolean(c.getString(c.getColumnIndex(DatabaseHelper.IS_ACTIVE)));
                String date = c.getString(c.getColumnIndex(DatabaseHelper.DATE));

                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                Date creationDate = new Date();
                try {
                    creationDate = sdf.parse(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                UserAcc user = userDAO.getUser(userId);
                ArrayList<byte[]> images = getImages(offerId);

                Offer offer = new Offer(user, title, description, price, condition, category, city, active, images, creationDate);
                offer.setId(offerId);
                offers.add(offer);
            }
            while (c.moveToNext());
        }

        c.close();
        db.close();
        return offers;
    }

    @Override
    public void deleteOffer(Offer offer) {

    }

    @Override
    public ArrayList<String> getAllCategories() {

        SQLiteDatabase db = mDb.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + DatabaseHelper.CATEGORIES;
        Cursor c = db.rawQuery(selectQuery, null);

        ArrayList<String> categories = new ArrayList<String>();

        if (c.moveToFirst()) {
            do {
                String name = c.getString(c.getColumnIndex(DatabaseHelper.NAME));
                categories.add(name);
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return categories;
    }

    // get offer by word in title
    public ArrayList<Offer> getOffers(String word) {
        ArrayList<Offer> offers = new ArrayList<Offer>();

        SQLiteDatabase db = mDb.getReadableDatabase();
        String query = "SELECT * FROM " + DatabaseHelper.OFFERS
                + " WHERE " + DatabaseHelper.TITLE + " LIKE \"%" + word + "%\" AND " + DatabaseHelper.IS_ACTIVE + " = \"true\"";
        Cursor c = db.rawQuery(query, null);

        if (c.moveToFirst()) {
            do {
                long offerId = c.getLong(c.getColumnIndex(DatabaseHelper.OFFER_ID));
                long userId = c.getLong(c.getColumnIndex(DatabaseHelper.USER_ID));
                long catId = c.getLong(c.getColumnIndex(DatabaseHelper.CATEGORY_ID));
                String title = c.getString(c.getColumnIndex(DatabaseHelper.TITLE));
                double price = c.getDouble(c.getColumnIndex(DatabaseHelper.PRICE));
                String condition = c.getString(c.getColumnIndex(DatabaseHelper.CONDITION));
                String description = c.getString(c.getColumnIndex(DatabaseHelper.DESCRIPTION));
                String city = c.getString(c.getColumnIndex(DatabaseHelper.CITY));
                boolean active = Boolean.parseBoolean(c.getString(c.getColumnIndex(DatabaseHelper.IS_ACTIVE)));
                String date = c.getString(c.getColumnIndex(DatabaseHelper.DATE));

                //SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                //Date creationDate = new Date();
                //try {
                //    creationDate = sdf.parse(date);
                //} catch (ParseException e) {
                //    e.printStackTrace();
                //}

                UserAcc user = userDAO.getUser(userId);
                String category = getCategory(catId);
                ArrayList<byte[]> images = getImages(offerId);

                Offer offer = new Offer(user, title, description, price, condition, category, city, active, images, null);
                offer.setId(offerId);
                offers.add(offer);
            }
            while (c.moveToNext());
        }

        c.close();
        db.close();
        return offers;
    }

    @Override
    public ArrayList<Offer> getOffersByUser(long userId) {
        ArrayList<Offer> offers = new ArrayList<Offer>();

        SQLiteDatabase db = mDb.getReadableDatabase();
        String query = "SELECT * FROM " + DatabaseHelper.OFFERS
                + " WHERE " + DatabaseHelper.USER_ID + " = " + userId + " AND " + DatabaseHelper.IS_ACTIVE + " = \"true\"";
        Cursor c = db.rawQuery(query, null);

        if (c.moveToFirst()) {
            do {
                long offerId = c.getLong(c.getColumnIndex(DatabaseHelper.OFFER_ID));
                long catId = c.getLong(c.getColumnIndex(DatabaseHelper.CATEGORY_ID));
                String title = c.getString(c.getColumnIndex(DatabaseHelper.TITLE));
                double price = c.getDouble(c.getColumnIndex(DatabaseHelper.PRICE));
                String condition = c.getString(c.getColumnIndex(DatabaseHelper.CONDITION));
                String description = c.getString(c.getColumnIndex(DatabaseHelper.DESCRIPTION));
                String city = c.getString(c.getColumnIndex(DatabaseHelper.CITY));
                boolean active = Boolean.parseBoolean(c.getString(c.getColumnIndex(DatabaseHelper.IS_ACTIVE)));
                String date = c.getString(c.getColumnIndex(DatabaseHelper.DATE));

//                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
//                Date creationDate = new Date();
//                try {
//                    creationDate = sdf.parse(date);
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }

                UserAcc user = userDAO.getUser(userId);
                String category = getCategory(catId);
                ArrayList<byte[]> images = getImages(offerId);

                Offer offer = new Offer(user, title, description, price, condition, category, city, active, images, null);
                offer.setId(offerId);
                offers.add(offer);
            }
            while (c.moveToNext());
        }

        c.close();
        db.close();
        return offers;
    }

    public ArrayList<Offer> getAllMyOffers(long userId) {
        ArrayList<Offer> offers = new ArrayList<Offer>();

        SQLiteDatabase db = mDb.getReadableDatabase();
        String query = "SELECT * FROM " + DatabaseHelper.OFFERS
                + " WHERE " + DatabaseHelper.USER_ID + " = " + userId;
        Cursor c = db.rawQuery(query, null);

        if (c.moveToFirst()) {
            do {
                long offerId = c.getLong(c.getColumnIndex(DatabaseHelper.OFFER_ID));
                long catId = c.getLong(c.getColumnIndex(DatabaseHelper.CATEGORY_ID));
                String title = c.getString(c.getColumnIndex(DatabaseHelper.TITLE));
                double price = c.getDouble(c.getColumnIndex(DatabaseHelper.PRICE));
                String condition = c.getString(c.getColumnIndex(DatabaseHelper.CONDITION));
                String description = c.getString(c.getColumnIndex(DatabaseHelper.DESCRIPTION));
                String city = c.getString(c.getColumnIndex(DatabaseHelper.CITY));
                boolean active = Boolean.parseBoolean(c.getString(c.getColumnIndex(DatabaseHelper.IS_ACTIVE)));
                String date = c.getString(c.getColumnIndex(DatabaseHelper.DATE));

                UserAcc user = userDAO.getUser(userId);
                String category = getCategory(catId);
                ArrayList<byte[]> images = getImages(offerId);

                Offer offer = new Offer(user, title, description, price, condition, category, city, active, images, null);
                offer.setId(offerId);
                offers.add(offer);
            }
            while (c.moveToNext());
        }

        c.close();
        db.close();
        return offers;
    }

    public int getOffersCount() {
        SQLiteDatabase db = mDb.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + DatabaseHelper.OFFERS;
        Cursor c = db.rawQuery(selectQuery, null);
        int count = c.getCount();
        c.close();
        return count;
    }

    @Override
    public long updateOffer(long offerId, Offer offer) {
        SQLiteDatabase db = mDb.getWritableDatabase();

        long categoryId = getCategory(offer.getCategory());

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.CATEGORY_ID, categoryId);
        values.put(DatabaseHelper.TITLE, offer.getName());
        values.put(DatabaseHelper.PRICE, offer.getPrice());
        values.put(DatabaseHelper.CONDITION, String.valueOf(offer.getCondition()));
        values.put(DatabaseHelper.DESCRIPTION, offer.getDescription());
        values.put(DatabaseHelper.CITY, offer.getCity());
        values.put(DatabaseHelper.IS_ACTIVE, String.valueOf(offer.isActive()));
        values.put(DatabaseHelper.DATE, String.valueOf(offer.getCreationDate()));

        long id = db.update(DatabaseHelper.OFFERS, values, DatabaseHelper.OFFER_ID + " = ? ", new String[]{String.valueOf(offerId)});

        ArrayList<byte[]> newImages = offer.getImages();
        ArrayList<byte[]> oldImages = getImages(offerId);

        for (int i = 0; i < oldImages.size(); i++) {
            boolean equals = false;
            for (int j = 0; j < newImages.size(); j++) {
                if (Arrays.equals(oldImages.get(i), newImages.get(j))) {
                    oldImages.remove(i);
                    newImages.remove(j);
                    equals = true;
                }
                if (equals)
                    break;
            }
        }

        for (int i = 0; i < oldImages.size(); i++) {
//            String query = " DELETE FROM " + mDb.IMAGES
//                    + " WHERE " + mDb.OFFER_ID + " = " + offerId + " AND " + mDb.CONTENT + " = " + oldImages.get(i)();
//            db.rawQuery(query, null);
            db.execSQL("DELETE FROM " + DatabaseHelper.IMAGES + " WHERE " + DatabaseHelper.OFFER_ID + " = ? AND " + DatabaseHelper.CONTENT + " = ?", new Object[]{offerId, oldImages.get(i)});

        }

        for (int i = 0; i < newImages.size(); i++) {
            ContentValues vals = new ContentValues();
            vals.put(DatabaseHelper.OFFER_ID, offerId);
            vals.put(DatabaseHelper.CONTENT, newImages.get(i));
            vals.put(DatabaseHelper.IS_MAIN, false);
            db.insert(DatabaseHelper.IMAGES, null, vals);
        }

        db.close();
        return id;
    }


}
