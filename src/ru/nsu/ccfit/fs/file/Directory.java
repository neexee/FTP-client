package ru.nsu.ccfit.fs.file;

/**
 * User: violetta
 * Date: 12/21/11
 * Time: 10:56 PM
 */
public class Directory extends AbstractFile{

    public Directory(String name, String date, String permissions){
        this.date = date;
        this.name = name;
        this.permissions = permissions;
    }
    @Override
    public int getType() {
        return DIRTYPE;
    }

}
