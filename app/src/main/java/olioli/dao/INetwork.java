package olioli.dao;

import android.content.Context;

import java.util.List;
import java.util.Map;

import olioli.dto.Users;

/**
 * Created by Ohardwick on 10/27/14.
 */
public interface INetwork {

    public boolean connect();
    //public void connectToMySQL(String user, String password, String ip, String port, String catalog);
    public Context getApplicationContext();

    public List<Users> collectUsers();

    public Boolean update();
}
