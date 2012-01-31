package ru.nsu.ccfit.fs.command;

import ru.nsu.ccfit.fs.ftp.Ftp;

import java.io.IOException;
import java.io.Serializable;

/**
 * User: violetta
 * Date: 12/23/11
 * Time: 10:43 PM
 */
public abstract class Command implements Serializable{
    
    public abstract Serializable run(Ftp ftp, String arg) throws IOException;
}
