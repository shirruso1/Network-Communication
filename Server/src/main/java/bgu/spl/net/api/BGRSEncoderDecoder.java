package bgu.spl.net.api;


import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class BGRSEncoderDecoder implements  MessageEncoderDecoder<Message>  {
    private byte[] bytes = new byte[1 << 10]; //start with 1k
    private int len = 0;
    private short opcode;
    private int counter;
    private final short ADMINREG_OPCODE=1;
    private final short STUDENTREG_OPCODE=2;
    private final short LOGIN_OPCODE=3;
    private final short LOGOUT_OPCODE=4;
    private final short COURSEREG_OPCODE=5;
    private final short KDAMCHECK_OPCODE=6;
    private final short COURSESTAT_OPCODE=7;
    private final short STUDENTSTAT_OPCODE=8;
    private final short ISREGISTERED_OPCODE=9;
    private final short UNREGISTER_OPCODE=10;
    private final short MYCOURSES_OPCODE=11;
    private final short ACK_OPCODE=12;
    private final short ERR_OPCODE=13;
    private final int [] countersArray={0,2,2,2,0,2,2,2,1,2,2,0};


    public Message decodeNextByte(byte nextByte) {
        pushByte(nextByte);
        if (len == 2) {
            opcode = bytesToShort(0);
            counter = countersArray[opcode];
        }
        if (opcode == ADMINREG_OPCODE) {
            if (nextByte == '\0')
                counter--;
            if (counter == 0) {
            	short opcodeTemp=opcode;
                String[] output = bytesToStrings(countersArray[opcode]);
                return new AdminRegMessage(opcodeTemp, output[0], output[1]);
            }
        }
        if (opcode == STUDENTREG_OPCODE) {
            if (nextByte == '\0')
                counter--;
            if (counter == 0) {
            	short opcodeTemp=opcode;
                String[] output = bytesToStrings(countersArray[opcode]);
                return new StudentRegMessage(opcodeTemp, output[0], output[1]);
            }
        }
        if (opcode == LOGIN_OPCODE) {
            if (nextByte == '\0')
                counter--;
            if (counter == 0) {
            	short opcodeTemp=opcode;
                String[] output = bytesToStrings(countersArray[opcode]);
                return new LoginMessage(opcodeTemp, output[0], output[1]);
            }
        }
        if (opcode == LOGOUT_OPCODE) {
        	short opcodeTemp=opcode;
            len = 0;
            opcode=-1;
            return new LogoutMessage(opcodeTemp);
        }
        if (opcode == COURSEREG_OPCODE) {
            if (len >= 3)
                counter--;
            if (counter == 0) {
                short courseNumber = bytesToShort(2);
                short opcodeTemp=opcode;
                opcode=-1;
                len=0;
                return new CourseRegMessage(opcodeTemp, courseNumber);
            }
        }
        if (opcode == KDAMCHECK_OPCODE) {
            if (len >= 3)
                counter--;
            if (counter == 0) {
                short courseNumber = bytesToShort(2);
                short opcodeTemp=opcode;
                opcode=-1;
                len=0;
                return new KdamCheckMessage(opcodeTemp, courseNumber);
            }
        }
        if (opcode == COURSESTAT_OPCODE) {
            if (len >= 3)
                counter--;
            if (counter == 0) {
                short courseNumber = bytesToShort(2);
                short opcodeTemp=opcode;
                opcode=-1;
                len=0;
                return new CourseStatMessage(opcodeTemp, courseNumber);
            }
        }
        if (opcode == STUDENTSTAT_OPCODE) {
            if (nextByte == '\0') {
                counter--;
                short opcodeTemp=opcode;
                String[] output = bytesToStrings(countersArray[opcode]);
                return new StudentStatMessage(opcodeTemp, output[0]);
            }
        }
        if (opcode == ISREGISTERED_OPCODE) {
            if (len >= 3)
                counter--;
            if (counter == 0) {
                short courseNumber = bytesToShort(2);
                short opcodeTemp=opcode;
                opcode=-1;
                len=0;
                return new isRegisteredMessage(opcodeTemp, courseNumber);
            }
        }
        if (opcode == UNREGISTER_OPCODE) {
            if (len >= 3)
                counter--;
            if (counter == 0) {
                short courseNumber = bytesToShort(2);
                short opcodeTemp=opcode;
                opcode=-1;
                len=0;
                return new UnregisterMessage(opcodeTemp, courseNumber);
            }
        }
        if(opcode==MYCOURSES_OPCODE) {
        	short opcodeTemp=opcode;
        	opcode=-1;
        	len=0;
            return new MyCoursesMessage(opcodeTemp);
        }
        return null;
    }

    public byte[] encode(Message message){
        if(message.getOpcode()==ERR_OPCODE){
            byte[] opcodeToBytes=shortToBytes(ERR_OPCODE);
            byte[] messageOpcodeToBytes=shortToBytes(((ErrorMessage)message).getMessageOpcode());
            byte[] result=new byte[opcodeToBytes.length+messageOpcodeToBytes.length];
            System.arraycopy(opcodeToBytes,0,result,0,opcodeToBytes.length);
            System.arraycopy(messageOpcodeToBytes,0,result,opcodeToBytes.length,messageOpcodeToBytes.length);
            return result;
        }
        byte[] opcodeToBytes=shortToBytes(ACK_OPCODE);
        byte[] messageOpcodeToBytes=shortToBytes(((AckMessage)message).getMessageOpcode());

        String optionalString=((AckMessage)message).getOptionalResponseMessage();
        if(optionalString.length()>0)
        	optionalString='\n'+optionalString;
        byte[] optionalResponseMessageToBytes=(optionalString+"\0").getBytes();
        byte[] result=new byte[opcodeToBytes.length+messageOpcodeToBytes.length+optionalResponseMessageToBytes.length];
        System.arraycopy(opcodeToBytes,0,result,0,opcodeToBytes.length);
        System.arraycopy(messageOpcodeToBytes,0,result,opcodeToBytes.length,messageOpcodeToBytes.length);
        System.arraycopy(optionalResponseMessageToBytes,0,result,messageOpcodeToBytes.length+opcodeToBytes.length,optionalResponseMessageToBytes.length);

        return result;
    }


    private void pushByte(byte nextByte) {
        if (len >= bytes.length) {
            bytes = Arrays.copyOf(bytes, len * 2);
        }

        bytes[len++] = nextByte;
    }

    private short bytesToShort(int index)
    {
        short result = (short)((bytes[index] & 0xff) << 8);
        result += (short)(bytes[index+1] & 0xff);
        return result;
    }

    public byte[] shortToBytes(short num)
    {
        byte[] bytesArr = new byte[2];
        bytesArr[0] = (byte)((num >> 8) & 0xFF);
        bytesArr[1] = (byte)(num & 0xFF);
        return bytesArr;
    }

    private String[] bytesToStrings (int numOfZeros){
        String result = new String(bytes, 2, len, StandardCharsets.UTF_8);
        int indexZero= result.indexOf('\0');
        String []output=new String[numOfZeros];
        int start=0;
        int end=indexZero;
        for(int i=0; i<numOfZeros;i++){
            output[i]=result.substring(start,end);
            start=indexZero+1;
            end=result.length()-1;
        }
        len = 0;
        opcode=-1;
        return output;
    }
}
