package client;

import protocol.Message;

import java.io.IOException;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

class GameThread implements Runnable {
    private String toDo;
    private int countdown = 10;
    public GameThread(String toDo) {
        this.toDo = toDo;
    }

    @Override
    public void run() {
        
        if(this.toDo == "timer") {

            new Timer().schedule(new TimerTask(){

            @Override
            public void run() {
                countdown=countdown - 1;
                System.out.println(countdown);
                Client.launcher.getFrame().gamePanel.setTimeRemaining(countdown);
                }   
            },0, 1000);
        }
    }
}