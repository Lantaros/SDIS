all:
	mkdir -p bin
	javac -d bin src/client/*.java src/game/*.java src/protocol/*.java  src/server/*.java src/gui/*.java
client:
	cd bin &&\
	java -Djavax.net.ssl.keyStore=../client.keys -Djavax.net.ssl.keyStorePassword=123456 -Djavax.net.ssl.trustStore=../truststore -Djavax.net.ssl.trustStorePassword=123456 client.Client 127.0.0.1 3030
server:
	cd bin &&\
	java -Djavax.net.ssl.keyStore=../server.keys -Djavax.net.ssl.keyStorePassword=123456 -Djavax.net.ssl.trustStore=../truststore -Djavax.net.ssl.trustStorePassword=123456 server.Server 3030
