package bgu.spl.net.api;


import bgu.spl.net.Database;

public class LoginMessage implements Message {
	private short opcode;
	private String username;
	private String password;

	public LoginMessage(short opcode,String username,String password){
		this.opcode=opcode;
		this.username=username;
		this.password=password;
	}

	public Message execute(BGRSProtocol protocol){
		Database database=Database.getInstance();
		//if the user haven't registered or gave wrong password
		if(protocol.getIsLogin()||!database.checkLogin(username,password)) {
			return new ErrorMessage(database.ERR_OPCODE,opcode);
		}
		protocol.setUsername(username);
		protocol.setPassword(password);
		protocol.setShouldTerminate(false);
		protocol.setUserType(database.getUserType(username));
		protocol.setLogin(true);

		return new AckMessage(database.ACK_OPCODE,opcode,"");
	}

	public short getOpcode(){return opcode;}
}
