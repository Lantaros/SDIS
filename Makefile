#!bin compile backup application
all:
	javac -d bin src/listeners/*.java src/protocol/*.java

registry:
	cd bin
	rmiregistry &
	cd ..

runAll:
	cd bin; \
	java protocol.Peer 1.0 1 peer1 228.0.0.1 6789 228.0.0.2 6789 228.0.0.3 6789 1024
	java protocol.Peer 1.0 2 peer2 228.0.0.1 6789 228.0.0.2 6789 228.0.0.3 6789 1024
	java protocol.Peer 1.0 3 peer3 228.0.0.1 6789 228.0.0.2 6789 228.0.0.3 6789 1024
	java protocol.Peer 1.0 4 peer4 228.0.0.1 6789 228.0.0.2 6789 228.0.0.3 6789 1024
	java protocol.TestClient peer1 BACKUP ../ficheiro_a_transmitir
	cd ..
