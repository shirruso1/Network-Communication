//
// Created by spl211 on 30/12/2020.
//

#include "../include/serverIO.h"

serverIO::serverIO(std::mutex &mutex) :_mutex(mutex) {}
void serverIO::run(ConnectionHandler &connectionHandler,keyboardIO &keyboard) {

    while (true) {
        std::string answer;
        char bytesArray[2];
        if (!connectionHandler.getBytes(bytesArray, 2)) {
            std::cout << "Disconnected. Exiting...\n" << std::endl;
            break;
        }
        short opcode = bytesToShort(bytesArray, 0);
        short messageOpcode = -1;
        //if the returned message is ERROR message
        if (opcode == 13) {
            if (!connectionHandler.getBytes(bytesArray, 2)) {
                std::cout << "Disconnected. Exiting...\n" << std::endl;
                break;
            }
            messageOpcode = bytesToShort(bytesArray, 0);
            answer = "ERROR " + std::to_string(messageOpcode);
        }
            //if the returned messasge is ACK message
        else {
            if (!connectionHandler.getBytes(bytesArray, 2)) {
                std::cout << "Disconnected. Exiting...\n" << std::endl;
                break;
            }
            messageOpcode = bytesToShort(bytesArray, 0);
            std::string attachment;
            if (!connectionHandler.getLine(attachment)) {
                std::cout << "Disconnected. Exiting...\n" << std::endl;
                break;
            }
            answer = "ACK " + std::to_string(messageOpcode) + attachment;
        }


        std::cout << answer << std::endl;
        //if the ACK is for LOGOUT 
        if (messageOpcode == 4) {
            if (opcode == 12) {
                connectionHandler.close();
                keyboard.setAtomics(true, true);
                break;
            } else {
                keyboard.setAtomics(false, true);
            }
        }
    }

}

short serverIO::bytesToShort(char (&bytesArr)[2],int index) {
    short result = (short)((bytesArr[index] & 0xff) << 8);
    result += (short)(bytesArr[index+1] & 0xff);
    return result;
}
