package bgu.spl.net.api;

import bgu.spl.net.Database;

public class AdminRegMessage implements Message {

    private short opcode;
    private String username;
    private String password;

    public AdminRegMessage(short opcode,String username,String password){
        this.opcode=opcode;
        this.username=username;
        this.password=password;
    }

    public Message execute (BGRSProtocol protocol){
        Database database= Database.getInstance();
        if(protocol.getIsLogin() || !database.addAdmin(username,password)){
            return new ErrorMessage(database.ERR_OPCODE,opcode);
        }

        return new AckMessage(database.ACK_OPCODE,opcode,"");
    }

    public short getOpcode(){return opcode;}
}