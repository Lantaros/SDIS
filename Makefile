#!bin compile backup application
all:
	mkdir -p bin
	javac -d bin src/listeners/*.java src/protocol/*.java
