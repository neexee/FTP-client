package ru.nsu.ccfit.fs.file;

/**
 * User: violetta
 * Date: 12/21/11
 * Time: 11:02 PM
 */
public class RegularFile extends AbstractFile{

    public RegularFile(String name, String date, String permissions){
        this.date = date;
        this.name = name;
        this.permissions = permissions;
    }
    @Override
    public int getType() {
        return REGULARTYPE;
    }


}
