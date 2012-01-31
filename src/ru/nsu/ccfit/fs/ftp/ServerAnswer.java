package ru.nsu.ccfit.fs.ftp;

public class ServerAnswer {
    int code;
    String message;
   public ServerAnswer(Integer code, String message){
        this.code = code;
        this.message = message;
    }
    public int getCode(){
     return code;
    }
    public String getMessage(){
        return message;
    }
}
