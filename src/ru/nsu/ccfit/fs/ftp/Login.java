package ru.nsu.ccfit.fs.ftp;

import java.io.Serializable;

public class Login implements Serializable{
   String user;
   String password;

   String baseDir;

   String serverName;
   int port;

  public Login(String user, String password, String host, int port){
    this.user= user;
    this.password = password;
    this.baseDir = baseDir;
    this.serverName = host;
    this.port = port;

   }
   public String getServerName(){
       return serverName;
   }
   public int getPort(){
       return port;
   }
   public String getUser(){
       return user;
   }
   public String getPassword(){
       return password;
   }
   public String getBaseDir(){
       return baseDir;
   }
}
