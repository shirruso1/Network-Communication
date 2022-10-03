package bgu.spl.net.api;

public class AckMessage implements Message{

    private short opcode;
    private short messageOpcode;
    private String optionalResponseMessage;

    public AckMessage (short opcode,short messageOpcode,String optionalResponseMessage){
        this.opcode=opcode;
        this.messageOpcode=messageOpcode;
        this.optionalResponseMessage=optionalResponseMessage;
    }

    public Message execute(BGRSProtocol protocol){return null;}

    //getters
    public short getOpcode(){return opcode;}
    public short getMessageOpcode(){return messageOpcode;}
    public String getOptionalResponseMessage(){return optionalResponseMessage;}
}
