//
// Created by spl211 on 30/12/2020.
//

#ifndef CLIENT_KEYBOARDIO_H
#define CLIENT_KEYBOARDIO_H
#include "./ConnectionHandler.h"
#include <atomic>

using namespace std;

class keyboardIO {
private:
    atomic<bool> shouldTerminate;
    atomic<bool> waitLogout;

public:
    keyboardIO();
    void run(ConnectionHandler &connectionHandler);
    void shortToBytes(short num, char (&bytesArr)[1024],int index);
    int copyToArray (const char* first, char (&second)[1024], int index);
    int stringEncoder(vector <string> inputFromKeyboard, char (&bytesToSend)[1024], int length);
    int shortEncoder(vector <string> inputFromKeyboard, char (&bytesToSend)[1024]);
    void setAtomics (bool _shouldTerminate, bool _waitLogout);

};


#endif //CLIENT_KEYBOARDIO_H
