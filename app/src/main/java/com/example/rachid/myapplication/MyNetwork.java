package com.example.rachid.myapplication;

import android.app.Activity;

/**
 * Created by Rachid on 18/04/2016.
 */
public abstract class MyNetwork {

    private static Activity activity;
    private static MyMeteor myMeteor;

    //
    public static String signupUser(User mUser) {

        if (mUser != null) {

            // TODO: Accede al servidor y guarda en la DB este nuevo usuario, devuelve el ID que se le ha dado en la DB del servidor.
            // ---------------------------------------------------------------------------------------
            MyMeteor.connect();
            String id = MyMeteor.addUser(mUser);
            MyMeteor.disconnect();
            // ---------------------------------------------------------------------------------------

            return id;
        }

        return null;
    }

    //
    public static User loginUser(String mEmail, String mPassword) {

        User user = null;

        // TODO: Accede al servidor y solicita que se le de el usuario con el email y password pasados como parametros.
        // ---------------------------------------------------------------------------------------
        // ---------------------------------------------------------------------------------------

        return user;
    }

    //
    public static boolean updateUser(User mUser) {

        if (mUser != null) {

            // TODO: Accede al servidor y actualiza en la DB el usuario pasado como parametro
            // ---------------------------------------------------------------------------------------
            // ---------------------------------------------------------------------------------------

            return true;
        }

        return false;
    }
}
