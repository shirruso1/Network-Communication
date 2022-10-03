package bgu.spl.net.api;

import bgu.spl.net.Database;

public class MyCoursesMessage implements Message {
    private short opcode;
    public MyCoursesMessage(short opcode){
        this.opcode=opcode;
    }
    public Message execute(BGRSProtocol protocol){
        Database database = Database.getInstance();
        String output = database.getStudentCourses(protocol.getUsername(), protocol.getIsLogin(),protocol.getUserType());
        if (output == null)
            return new ErrorMessage(database.ERR_OPCODE, opcode);
        return new AckMessage(database.ACK_OPCODE, opcode, output);
    }

    public short getOpcode(){return opcode;}
}
