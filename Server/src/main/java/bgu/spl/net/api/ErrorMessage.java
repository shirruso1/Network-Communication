package bgu.spl.net.api;
public class ErrorMessage implements Message {
    private short opcode;
    private short messageOpcode;

    public ErrorMessage(short opcode,short messageOpcode){
        this.opcode=opcode;
        this.messageOpcode=messageOpcode;
    }

    public Message execute(BGRSProtocol protocol){return null;}

    //getters
    public short getOpcode(){return opcode;}
    public short getMessageOpcode(){return messageOpcode;}
}
