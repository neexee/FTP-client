package ru.nsu.ccfit.fs.command;

import ru.nsu.ccfit.fs.ftp.Ftp;

import java.io.IOException;
import java.io.Serializable;

/**
 * User: violetta
 * Date: 12/23/11
 * Time: 10:48 PM
 */
public class ChangeDir extends Command{
    @Override
    public Serializable run(Ftp ftp, String arg) throws IOException
    {
        String answer = ftp.cd(arg);

        return answer;
    }
}
