package bgu.spl.net.api;

import bgu.spl.net.Database;

public class isRegisteredMessage implements Message {
    private short opcode;
    private short courseNumber;

    public isRegisteredMessage(short opcode, short courseNumber){
        this.opcode=opcode;
        this.courseNumber=courseNumber;
    }
    public Message execute (BGRSProtocol protocol){
        Database database = Database.getInstance();
        String output = database.isRegisteredCourse(protocol.getUsername(), protocol.getIsLogin(),courseNumber,protocol.getUserType());
        if (output == null)
            return new ErrorMessage(database.ERR_OPCODE, opcode);
        return new AckMessage(database.ACK_OPCODE, opcode, output);
    }

    public short getOpcode(){return opcode;}
}
