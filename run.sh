#!/bin/bash

#rmiregistry
echo "Launching RMI Registry"
xterm -e "cd bin && rmiregistry " 2> /dev/null & 
sleep 1

#Peers
xterm -e "cd bin && java protocol.Peer 1.0 1 peer1 228.0.0.1 6789 228.0.0.2 6789 228.0.0.3 6789 1024 " 2> /dev/null &  
xterm -e "cd bin && java protocol.Peer 1.0 2 peer2 228.0.0.1 6789 228.0.0.2 6789 228.0.0.3 6789 1024 " 2> /dev/null & 
        
echo "Launched peers..." 
wait
