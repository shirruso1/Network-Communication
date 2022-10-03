//
// Created by spl211 on 30/12/2020.
//

#ifndef CLIENT_SERVERIO_H
#define CLIENT_SERVERIO_H
#include <mutex>
#include <stdlib.h>
#include <atomic>
#include "./ConnectionHandler.h"
#include "../include/keyboardIO.h"
using namespace std;

class serverIO {
private:
    std::mutex & _mutex;

public:
    serverIO(std::mutex & mutex);
    void run(ConnectionHandler &connectionHandler,keyboardIO &keyboard);
    short bytesToShort(char (&bytesArr)[2],int index);
};


#endif //CLIENT_SERVERIO_H
