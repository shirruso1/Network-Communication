package bgu.spl.net.api;

public interface Message {

    public  Message execute (BGRSProtocol protocol);

    public short getOpcode();
}
