//
// Created by spl211 on 30/12/2020.
//

using namespace std;
#include <boost/lexical_cast.hpp>
#include "../include/keyboardIO.h"

keyboardIO::keyboardIO():shouldTerminate(false),waitLogout(false){}
void keyboardIO::run(ConnectionHandler &connectionHandler) {

    while (!shouldTerminate) {
        const short bufsize = 1024;
        char buf[bufsize];
        std::cin.getline(buf, bufsize);


        std::string line(buf);
        vector <string> inputFromKeyboard;
        //split the string where there is space
        std::size_t index = line.find_first_of(' ');
        while (index != string::npos) {
            inputFromKeyboard.push_back(line.substr(0, index));
            index++;
            line = line.substr(index);
            index = line.find_first_of(' ');
        }
        inputFromKeyboard.push_back(line);

        char bytesToSend[bufsize];
        int length=0;
        if (inputFromKeyboard[0] == "ADMINREG") {
            shortToBytes(1,bytesToSend,0);
            length =stringEncoder(inputFromKeyboard,bytesToSend,length);

        }
        else if (inputFromKeyboard[0] =="STUDENTREG"){
            shortToBytes(2,bytesToSend,0);
            length =stringEncoder(inputFromKeyboard,bytesToSend,length);
        }
        else if(inputFromKeyboard[0]=="LOGIN"){
            shortToBytes(3,bytesToSend,0);
            length =stringEncoder(inputFromKeyboard,bytesToSend,length);
        }
        else if(inputFromKeyboard[0]=="LOGOUT"){
            shortToBytes(4,bytesToSend,0);
            length=2;
        }
        else if(inputFromKeyboard[0]=="COURSEREG"){
            shortToBytes(5,bytesToSend,0);
            length=shortEncoder(inputFromKeyboard,bytesToSend);
        }
        else if(inputFromKeyboard[0]=="KDAMCHECK"){
            shortToBytes(6,bytesToSend,0);
            length=shortEncoder(inputFromKeyboard,bytesToSend);
        }
        else if(inputFromKeyboard[0]=="COURSESTAT") {
            shortToBytes(7, bytesToSend, 0);
            length=shortEncoder(inputFromKeyboard,bytesToSend);
        }
        else if(inputFromKeyboard[0]=="STUDENTSTAT"){
            shortToBytes(8, bytesToSend, 0);
            const char* studentUsername=inputFromKeyboard[1].c_str();
            length=copyToArray(studentUsername,bytesToSend,2);
        }
        else if(inputFromKeyboard[0]=="ISREGISTERED"){
            shortToBytes(9, bytesToSend, 0);
            length=shortEncoder(inputFromKeyboard,bytesToSend);
        }
        else if(inputFromKeyboard[0]=="UNREGISTER"){

            shortToBytes(10, bytesToSend, 0);
            length=shortEncoder(inputFromKeyboard,bytesToSend);
        }
        else if(inputFromKeyboard[0]=="MYCOURSES"){
            shortToBytes(11,bytesToSend,0);
            length=2;
        }
        if (!connectionHandler.sendBytes(bytesToSend,length)) {
            std::cout << "Disconnected. Exiting...\n" << std::endl;
            break;
        }
        if (inputFromKeyboard[0]=="LOGOUT"){
            while(!waitLogout){}
            bool before=true, after=false;
            this->waitLogout.compare_exchange_weak(before,after,memory_order_release,memory_order_relaxed);
        }
    }
}
void keyboardIO::shortToBytes(short num, char (&bytesArr)[1024],int index) {
    bytesArr[index] = ((num >> 8) & 0xFF);
    bytesArr[index+1] = (num & 0xFF);


}
int keyboardIO::copyToArray(const char *first, char (&second)[1024], int index) {
    int counter=0;
    while(first[counter]!='\0'){
        second[index]=first[counter];
        counter++;
        index++;
    }
    second[index]='\0';
    index++;
    return index;
}
int keyboardIO::stringEncoder(vector<string> inputFromKeyboard, char (&bytesToSend)[1024], int length) {
    const char* username=inputFromKeyboard[1].c_str();
    const char* password=inputFromKeyboard[2].c_str();
    length=copyToArray(username,bytesToSend,2);
    length=copyToArray(password,bytesToSend,length);
    return length;
}

int keyboardIO::shortEncoder(vector<string> inputFromKeyboard, char (&bytesToSend)[1024]) {
    short courseNumber= boost::lexical_cast<short>(inputFromKeyboard[1]);
    shortToBytes(courseNumber,bytesToSend,2);
    return 4;
}
void keyboardIO::setAtomics(bool _shouldTerminate, bool _waitLogout) {
    bool before=false;
    this->shouldTerminate.compare_exchange_weak(before,_shouldTerminate,memory_order_release,memory_order_relaxed);
    this->waitLogout.compare_exchange_weak(before,_waitLogout,memory_order_release,memory_order_relaxed);
}
