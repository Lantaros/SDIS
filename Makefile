#!bin compile backup application
all:
	javac -d bin/ src/*.java 

run:
	java bin/TestClient.class 
