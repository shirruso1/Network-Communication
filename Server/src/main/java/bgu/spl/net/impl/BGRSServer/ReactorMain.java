package bgu.spl.net.impl.BGRSServer;
import bgu.spl.net.Database;
import bgu.spl.net.api.BGRSEncoderDecoder;
import bgu.spl.net.api.BGRSProtocol;
import bgu.spl.net.api.Message;
import bgu.spl.net.srv.Reactor;
import bgu.spl.net.srv.Server;

public class ReactorMain {
    public static void main (String []args){
        Database databaseInstance=Database.getInstance();
        databaseInstance.initialize("././././././Courses.txt");
        Server<Message> server =new Reactor<>(Integer.decode(args[1]).intValue(),Integer.decode(args[0]).intValue(),()->new BGRSProtocol(),()->new BGRSEncoderDecoder());
        server.serve();
    }
}
