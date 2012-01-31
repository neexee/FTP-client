package ru.nsu.ccfit.fs.file;

import java.io.Serializable;

/**
 * User: violetta
 * Date: 12/21/11
 * Time: 10:54 PM
 */
public abstract class AbstractFile implements Serializable{

    public static final int DIRTYPE =1;
    public static final int REGULARTYPE =0;

    String name;
    String date;
    String permissions;

    public abstract int getType();

    public String getName() {
        return name;
    }

    public String getDate() {
        return  date;
    }

    public  String getPermissions() {
        return permissions;
    }
    public String toString(){
     return name + " " + date + " " + permissions;
    }

}
