package com.example.myapplication.Repository;
import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import com.example.myapplication.DB.UsersDB;
import com.example.myapplication.Dao.UserDao;
import com.example.myapplication.Entites.User;
import java.util.List;

public class UserRepository {
    private UserDao userDao;
    private LiveData<List<User>> allUsers;

    public UserRepository(Application application) {
        UsersDB usersDB = UsersDB.getInstance(application);
        userDao = usersDB.userDao();
        allUsers = userDao.getAllUsers();
    }
    public void insert(User user) {
        new InsertUserAsyncTask(userDao).execute(user);
    }
    public void update(User user) {
        new UpdateUserAsyncTask(userDao).execute(user);
    }
    public void delete(User user) {
        new DeleteUserAsyncTask(userDao).execute(user);
    }
    public void deleteAllUsers() {
        new DeleteAllUserAsyncTask(userDao).execute();
    }
    public LiveData<List<User>> getAllUsers() {
        return allUsers;
    }
    private static class InsertUserAsyncTask extends AsyncTask<User, Void, Void> {
        private UserDao userDao;
        private InsertUserAsyncTask(UserDao userDao) {
            this.userDao = userDao;
        }
        @Override
        protected Void doInBackground(User... users) {
            userDao.insert(users[0]);
            return null;
        }
    }

    private static class DeleteAllUserAsyncTask extends AsyncTask<Void, Void, Void> {
        private UserDao userDao;
        private DeleteAllUserAsyncTask(UserDao userDao) {
            this.userDao = userDao;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            userDao.deleteAllUsers();
            return null;
        }
    }

    private static class UpdateUserAsyncTask extends AsyncTask<User, Void, Void> {
        private UserDao userDao;
        private UpdateUserAsyncTask(UserDao userDao) {
            this.userDao = userDao;
        }
        @Override
        protected Void doInBackground(User... users) {
            userDao.update(users[0]);
            return null;
        }
    }

    private static class DeleteUserAsyncTask extends AsyncTask<User, Void, Void> {
        private UserDao userDao;
        private DeleteUserAsyncTask(UserDao userDao) {
            this.userDao = userDao;
        }
        @Override
        protected Void doInBackground(User... users) {
            userDao.delete(users[0]);
            return null;
        }
    }
}
