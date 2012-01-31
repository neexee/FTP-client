package ru.nsu.ccfit.fs.activity;

import ru.nsu.ccfit.fs.file.AbstractFile;
import ru.nsu.ccfit.fs.ftp.Ftp;
import ru.nsu.ccfit.fs.ftp.Login;

import java.io.IOException;
import java.util.ArrayList;

public class LocalTest {
    public static void main(String[] args) throws IOException {
        Login login= new Login("ftp", "","freebsd.nsu.ru",21);
        Ftp client = new Ftp(login);
        //OutputStreamWriter writer = new OutputStreamWriter(System.out, "ASCII");
        //System.out.print("/n/n/n\r\r\r\n\n\n");
        ArrayList list = new ArrayList<AbstractFile>();
        try{
           client.connect();
           client.login();

           list = client.ls();

           client.cd("pub");
           System.out.println(list);
           list = client.ls();
           System.out.println(list);
        }
        catch (IOException a){
            a.printStackTrace();

        }
    }
}
