CFLAGS:=-c -Wall -Weffc++ -g -std=c++11 -Iinclude
LDFLAGS:=-lboost_system -lboost_thread -pthread

all: bin/BGRSclient

bin/BGRSclient: bin/Client.o bin/keyboardIO.o bin/serverIO.o bin/ConnectionHandler.o
	@echo 'Building target: Client'
	@echo 'Invoking: C++ Linker'
	g++ -Wall -Weffc++ -o bin/BGRSclient bin/Client.o bin/keyboardIO.o bin/serverIO.o bin/ConnectionHandler.o $(LDFLAGS)
	@echo 'Finished building target: Client'
	@echo ' '

bin/Client.o: src/Client.cpp
	g++ $(CFLAGS) -o bin/Client.o src/Client.cpp $(LDFLAGS)

bin/keyboardIO.o: src/keyboardIO.cpp
	g++ $(CFLAGS) -o bin/keyboardIO.o src/keyboardIO.cpp $(LDFLAGS)

bin/serverIO.o: src/serverIO.cpp
	g++ $(CFLAGS) -o bin/serverIO.o src/serverIO.cpp $(LDFLAGS)

bin/ConnectionHandler.o: src/ConnectionHandler.cpp
	g++ $(CFLAGS) -o bin/ConnectionHandler.o src/ConnectionHandler.cpp $(LDFLAGS)

clean:
	@echo 'cleaning'
	rm -f bin/*
