package bgu.spl.net.api;

public class BGRSProtocol implements MessagingProtocol<Message> {
    private String username=null;
    private String password=null;
    private boolean shouldTerminate;
    private boolean isLogin=false;
    String userType;

    public Message process(Message msg){
        return msg.execute(this);
    }


    public boolean shouldTerminate(){return shouldTerminate;}

    //setters
    public void setUsername(String username){this.username=username;}
    public void setPassword (String password){this.password=password;}
    public void setShouldTerminate (boolean shouldTerminate){this.shouldTerminate=shouldTerminate;}
    public void setLogin(boolean isLogin){this.isLogin=isLogin;}
    public void setUserType (String userType) {this.userType=userType;}
    //getters
    public boolean getIsLogin(){return this.isLogin;}
    public String getUsername(){return this.username;}
    public String getPassword(){return this.password;}
    public String getUserType() {return this.userType;}


}

