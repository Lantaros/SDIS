all:
	mkdir -p bin
	javac -d bin src/*.java
client:
	cd bin
	java Client 127.0.0.1 3030 O
	cd ..
server:
	cd bin &&\
	java Server 3030
	cd ..

