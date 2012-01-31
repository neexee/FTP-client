package ru.nsu.ccfit.fs.ftp;


import android.widget.Toast;
import ru.nsu.ccfit.fs.file.AbstractFile;
import ru.nsu.ccfit.fs.file.Directory;
import ru.nsu.ccfit.fs.file.RegularFile;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

import static   ru.nsu.ccfit.fs.ftp.FtpMessageCode.*;

public class Ftp {
    public static final int LS =0;
    public static final int CD =1;
    Login login;
    String currentDir = "/";
    boolean PASSIVE_MODE_ENABLED = false;

    Socket commands;
    Socket data;
    BufferedReader commandReader;
    PrintStream commandWriter;
    BufferedReader dataReader;
    PrintStream dataWriter;

    public Ftp(Login login)
    {
          this.login = login;

    }

    public void connect()  throws  IOException
    {
        commands = new Socket( login.getServerName(), login.getPort()) ;
        commandReader = new BufferedReader( new InputStreamReader( commands.getInputStream(), "ASCII"));
        commandWriter = new PrintStream(commands.getOutputStream(), true);

        String answer1 = getAnswer();
        ServerAnswer answer = parseAnswer(answer1);
         if (CONNECTED.getCode()!= answer.getCode())
        {
              //а может, это и не ftp вовсе на этом порту?
             //забанили, не тот порт, етц
            if((answer.getCode()%10 == 5) && (answer.getCode()%10 == 4)) {
                 //команда не может быть исполнена

            }

        }
        else {
           //OKAY?
         }



    }
    public void login() throws IOException
    {

             command("USER", login.getUser());
             ServerAnswer a = parseAnswer(getAnswer());

             switch (a.getCode())
             {
                 case ANON_USER_LOGIN_OK : ;
                 //any detaited checks
                 default:    command("PASS",login.getPassword() );

             }

        getAnswer();


    }
    public ArrayList<AbstractFile> ls() throws IOException {

        enterPassiveMode();

        command("LIST", "");
        getAnswer();
        String data = getData();
        System.out.println(data);
        ArrayList<AbstractFile> list = new ArrayList<AbstractFile>();
        Scanner reader = new Scanner(new StringReader(data));

        list.add(new Directory("..", "", ""));
        while (reader.hasNext())
        {
             String parse =   reader.nextLine();
             list.add(parseLs(parse));
        }

        System.out.print(list.toString());
        
        return list;



    }


    private AbstractFile parseLs(String fileInfo)
    {
         String date = "";
         while(fileInfo.charAt(0)==' ')
         {
              fileInfo= fileInfo.substring(1);
         }

         fileInfo.trim();
         String parts[] =fileInfo.split("\\p{Blank}");
         String perms = parts[0];
         String name = parts[parts.length -1];                 //have a bug

         if(perms.charAt(0) == 'd')
         {
             perms= perms.substring(1);
             System.out.println("CREATED"+ name + " " + date + " " + perms );
             return new Directory(name,date, perms);
         }
         else
         {
            System.out.println("CREATED"+ name + " " + date + " " + perms );
            return new RegularFile(name, date, perms);
         }
     
     
      
    }
    public void rmFile(String filename) throws IOException
    {
        command("DELE", filename);
        getAnswer();

    }

    public void rmDir(String dirname) throws IOException
    {
        command("RMD", dirname);
        getAnswer();
    }

    public String cd(String dirName) throws IOException
    {
        if(dirName.equals(".."))
        {
            command("CDUP", "");
            String currentDirparse[] = currentDir.split("/");
            currentDir="/";
            for(int i=0;i < currentDir.length()-1; i++)
            {
               currentDir+=currentDirparse[i] + "/";
            }
        }

        else
        {
            command("CWD", dirName);
            getAnswer();
            currentDir += dirName+ "/";
        }
        return dirName;
    }

    public void mkdir(String dirName) throws IOException
    {
        command("MKD",dirName);
        getAnswer();


    }

    public String fileInfo(String filename) throws IOException
    {
        String info = new String();
        command("MDTM", filename);                 //Time modification.
        info = getAnswer();
        command("SIZE", filename);
        info += getAnswer();
        return info;
    }
    private void command(String command, String  arg )
    {
          String _command = command +" "+ arg + "\r\n" ;
          System.out.print(_command);
          commandWriter.print(_command);
          commandWriter.flush();

    }

    private String getData()  throws IOException
    {
        System.out.println("FETCHING DATA");
        String buf = new String("");

        /* Костыль для медленного соединения*/
        buf += dataReader.readLine() ;
        if (dataReader.ready())
            buf += "\n ";


        while (dataReader.ready())
        {
            buf+= dataReader.readLine();

             if (dataReader.ready())
             {
                    buf += "\n";
             }
        }

        return buf;
    }
    private String getAnswer() throws IOException
    {
        
        String buf = "";
        System.out.println("FETCHING COMMAND ANSWER");
        buf += commandReader.readLine() ;
        if (commandReader.ready())
             buf += "\n ";
        /*Костыль для медленного соединения*/
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        while (commandReader.ready())
        {
                  buf += commandReader.readLine() ;
                  if (commandReader.ready())
                    buf += "\n";
        }

        System.out.println(buf);

        return buf;
    }
    private ServerAnswer parseAnswer(String answer)
    {
         ServerAnswer desu = null;
        try{

            desu = new ServerAnswer
                    (Integer.decode(answer.substring(0,3)).intValue(), answer.substring(3, answer.length()));
        }
        catch(NumberFormatException e){

           return new ServerAnswer(110, answer);

        }
        catch( StringIndexOutOfBoundsException e)
        {
          return new ServerAnswer(110, answer);
        }
        return desu;
    }

    private String getLastLine(String str)
    {
         Scanner scanner = new Scanner(str);
         String lastLine = new String("");

         while (scanner.hasNextLine())
          {
             lastLine = scanner.nextLine();
          }
         return lastLine;
    }

    private void enterPassiveMode() throws IOException
    {
       if(PASSIVE_MODE_ENABLED)
       {
          dataReader.close();
          dataWriter.close();
          data.close();
       }
        command("PASV", "");
        String answer = getAnswer();

        answer = getLastLine(answer);
        Scanner scanner = new Scanner(answer);
        scanner.findInLine("\\(");
        String passive = scanner.nextLine();
        String passives[] = passive.split(",");

        String ip = new String();

        for (int i = 0; i < 3; i++)
            ip += passives[i] + ".";

        ip += passives[3];

        Integer port1 = Integer.parseInt(passives[4], 10);
        String withoutBrake[] = passives[5].split("\\)");
        Integer port2 = Integer.parseInt(withoutBrake[0], 10);
        System.out.println("PASSIVE MODE: FETCHING DATA FROM IP " + ip + " PORT " + port1 +"*256 + " +port2);

        data = new Socket(ip, port1 * 256 + port2);
        dataReader = new BufferedReader(new InputStreamReader(data.getInputStream(), "ASCII"));
        dataWriter = new PrintStream(data.getOutputStream(), true);
        PASSIVE_MODE_ENABLED = true;
    }

    public void stop() throws IOException
    {
       if(PASSIVE_MODE_ENABLED)
       {
           dataReader.close();
           dataWriter.close();
           data.close();
       }

        commandReader.close();
        commandWriter.close();
        commands.close();
    }

    public String getCurrentDir()
    {
        return currentDir;
    }
}

