package bgu.spl.net.api;

import bgu.spl.net.Database;

public class KdamCheckMessage implements Message {
    private short opcode;
    private short courseNumber;

    public KdamCheckMessage(short opcode, short courseNumber){
        this.courseNumber=courseNumber;
        this.opcode=opcode;
    }
    public Message execute (BGRSProtocol protocol){
        Database database=Database.getInstance();
        String output= database.kdamCourseCheck(protocol.getUsername(), protocol.getIsLogin(),courseNumber,protocol.getUserType());
        if(output==null)
            return new ErrorMessage(database.ERR_OPCODE,opcode);
        return new AckMessage(database.ACK_OPCODE,opcode,output);
    }

    public short getOpcode(){return opcode;}
}
