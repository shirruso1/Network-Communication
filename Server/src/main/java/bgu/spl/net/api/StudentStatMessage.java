package bgu.spl.net.api;

import bgu.spl.net.Database;

public class StudentStatMessage implements Message{
    private short opcode;
    private String studentUsername;

    public StudentStatMessage(short opcode,String studentUsername){
        this.opcode=opcode;
        this.studentUsername=studentUsername;
    }
    public Message execute (BGRSProtocol protocol){
        Database database = Database.getInstance();
        String output = database.studentStatusCheck(protocol.getUsername(), protocol.getIsLogin(),studentUsername,protocol.getUserType());
        if (output == null)
            return new ErrorMessage(database.ERR_OPCODE, opcode);
        return new AckMessage(database.ACK_OPCODE, opcode, output);
    }

    public short getOpcode(){return opcode;}
}
