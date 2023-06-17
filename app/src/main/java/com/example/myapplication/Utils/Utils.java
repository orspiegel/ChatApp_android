package com.example.myapplication.Utils;

import com.example.myapplication.ROOM_p.Contact;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;

public class Utils {
    public static String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuilder sb = new StringBuilder();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }

    public static boolean containsID(Collection<Contact> c, String id) {
        for(Contact o : c) {
            if(o != null && o.getLoggedInID().equals(id)) {
                return true;
            }
        }
        return false;
    }



}
