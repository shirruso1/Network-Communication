package bgu.spl.net.impl.BGRSServer;

import bgu.spl.net.Database;
import bgu.spl.net.api.BGRSEncoderDecoder;
import bgu.spl.net.api.BGRSProtocol;
import bgu.spl.net.api.Message;
import bgu.spl.net.srv.Server;
import bgu.spl.net.srv.ThreadPerClientServer;

public class TPCMain {
    public static void main (String []args){
        Database databaseInstance=Database.getInstance();
        databaseInstance.initialize("././././././Courses.txt");
        Server <Message> sever =new ThreadPerClientServer<>(Integer.decode(args[0]).intValue(),()->new BGRSProtocol(),()->new BGRSEncoderDecoder());
        sever.serve();
    }
}
