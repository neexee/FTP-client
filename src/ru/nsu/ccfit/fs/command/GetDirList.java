package ru.nsu.ccfit.fs.command;

import ru.nsu.ccfit.fs.file.AbstractFile;
import ru.nsu.ccfit.fs.ftp.Ftp;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * User: violetta
 * Date: 12/23/11
 * Time: 10:44 PM
 */
public class GetDirList extends Command{
    @Override
    public Serializable run(Ftp ftp, String arg) throws IOException {
        ArrayList<AbstractFile> list = ftp.ls();
        return list;
    }
}
