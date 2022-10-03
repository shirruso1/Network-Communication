//
// Created by spl211 on 30/12/2020.
//
#include <iostream>
#include <mutex>
#include <boost/regex.hpp>
#include <boost/lexical_cast.hpp>
#include <thread>
#include <stdlib.h>
#include "../include/ConnectionHandler.h"
#include "../include/keyboardIO.h"
#include "../include/serverIO.h"
#include <atomic>


using namespace std;

int main (int argc, char *argv[]) {

  if (argc < 3) {
        std::cerr << "Usage: " << argv[0] << " host port" << std::endl << std::endl;
        return -1;
    }
    std::string host = argv[1];
    short port = atoi(argv[2]);

    ConnectionHandler _connectionHandler(host, port);
    if (!_connectionHandler.connect()) {
        std::cerr << "Cannot connect to " << host << ":" << port << std::endl;
        return 1;
    }

    std::mutex mutex;
    keyboardIO listeningKeyboard;
    serverIO listeningSocket(mutex);
    std::thread thread(&serverIO::run,&listeningSocket,std::ref(_connectionHandler),std::ref(listeningKeyboard));
    listeningKeyboard.run(_connectionHandler);
    thread.detach();
    return 0;

}

