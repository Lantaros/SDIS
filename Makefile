all:
	mkdir -p bin
	javac -d bin src/client/*.java src/game/*.java src/protocol/*.java  src/server/*.java src/gui/*.java
client:
	cd bin
	java client.Client 127.0.0.1 3030 O
	cd ..
server:
	cd bin &&\
	java server.Server 3030
	cd ..

