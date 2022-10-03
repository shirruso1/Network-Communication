package bgu.spl.net.api;


import bgu.spl.net.Database;

public class CourseRegMessage implements Message{
    short opcode;
    short courseNumber;
    public CourseRegMessage(short opcode,short courseNumber){
        this.opcode=opcode;
        this.courseNumber=courseNumber;
    }
    public Message execute(BGRSProtocol protocol){
        Database database=Database.getInstance();
        if(!protocol.getIsLogin() ||protocol.getUserType().equals("Admin")||
        		!database.regCourse(protocol.getUsername(),courseNumber))
            return new ErrorMessage(database.ERR_OPCODE,opcode);
        return new AckMessage(database.ACK_OPCODE,opcode,"");
    }

    public short getOpcode(){return opcode;}

}
