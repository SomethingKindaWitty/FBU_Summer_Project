package me.caelumterrae.fbunewsapp.database;


import android.app.Instrumentation;
import android.arch.persistence.room.Room;

import org.junit.After;
import org.junit.Before;
import org.parceler.Parcel;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import java.util.List;

import me.caelumterrae.fbunewsapp.model.User;

//@Parcel
public final class LocalUserDataSource{

//    private static UserDatabase mDatabase;
////    private static LocalUserDataSource mDataSource;
//    private final UserDao mUserDao;
//
////    public static synchronized LocalUserDataSource getInstance(Context context){
////        if (mDataSource == null){
////            mDataSource = new LocalUserDataSource();
////        }
////        return mDataSource;
////    }
//
//    @Before
//    public void initDb() throws Exception {
//         mDatabase = UserDatabase.getInstance();
//         mUserDao = mDatabase.userDao();
//    }
////
//    @After
//    public void closeDb() throws Exception {
//        mDatabase.close();
//    }
//
//    public LocalUserDataSource(UserDao userDao) {
//        mUserDao = userDao;
//    }
//
//    public User getUser(String name, String password){
//        try {
//            return mUserDao.findByName(name, password);
//        }catch (NullPointerException e){
//            return null;
//        }
//    }
//
//    public List<User> getUsers() {
//        try {
//            return mUserDao.getAll();
//        }catch (NullPointerException e){
//            return null;
//        }
//    }
//
//    public void addUser(User user){
//        mUserDao.insertUser(user);
//    }
//
//    public User getUser(int id){
//        return mUserDao.findByID(id);
//    }

}
