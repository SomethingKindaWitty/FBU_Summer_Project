package me.caelumterrae.fbunewsapp.database;


import android.app.Instrumentation;
import android.arch.persistence.room.Room;

import org.junit.After;
import org.junit.Before;
import android.support.test.InstrumentationRegistry;

import java.util.List;

import me.caelumterrae.fbunewsapp.model.User;

public class LocalUserDataSource {

    private UserDatabase mDatabase;
   // private LocalUserDataSource mDataSource;
    private UserDao userDao;

    @Before
    public void initDb() throws Exception {
        // using an in-memory database because the information stored here disappears when the
        // process is killed
        mDatabase = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(),
                UserDatabase.class).build();
        userDao = mDatabase.userDao();
    }

    @After
    public void closeDb() throws Exception {
        mDatabase.close();
    }

    public User getUser(String name, String password){
        try {
            return userDao.findByName(name, password);
        }catch (NullPointerException e){
            return null;
        }
    }

    public List<User> getUsers() {
        try {
            return userDao.getAll();
        }catch (NullPointerException e){
            return null;
        }
    }


}
