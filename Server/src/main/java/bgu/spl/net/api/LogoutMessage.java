package bgu.spl.net.api;

import bgu.spl.net.Database;

public class LogoutMessage implements Message {
    short opcode;

    public LogoutMessage(short opcode){this.opcode=opcode;}

    public Message execute(BGRSProtocol protocol){
        Database database=Database.getInstance();
        if(!protocol.getIsLogin())
            return new ErrorMessage(database.ERR_OPCODE,opcode);

        database.logout(protocol.getUsername());
        protocol.setShouldTerminate(true);
        return new AckMessage(database.ACK_OPCODE,opcode,"");
    }

    public short getOpcode(){return opcode;}

}
