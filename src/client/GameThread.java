package client;


import java.util.Timer;
import java.util.TimerTask;

class GameThread implements Runnable {

    private int roomID;
    private String toDo;
    private int countdown = 11;
    private Timer t;
    private TimerTask tt;
    public GameThread(String toDo) {
        this.toDo = toDo;
    }

    @Override
    public void run() {
        
        if(this.toDo.equals("timer")) {

            t = new Timer();
            tt = new TimerTask() {

                @Override
                public void run() {
                    countdown=countdown - 1;
                    Client.launcher.getFrame().gamePanel.setTimeRemaining(countdown);

                    if(countdown == 0) {
                        Client.advanceTurn();
                        t.cancel();
                        t.purge();
                        return;
                    }
                };
            };t.schedule(tt, 0, 1000);
        }

        if(this.toDo.equals("timer_up")) {
            Client.handleNextTurn(roomID);
        }

        if(this.toDo.equals("next_turn")){
             Client.handleNextTurn(roomID);
        }
    }

    public void resetTimer() {
        t.cancel();
        t.purge();
        Client.resetTimer = false;
    }

    public int getCountdown() {
        return this.countdown;
    }

    public int getRoomID() {
        return roomID;
    }

    public void setRoomID(int roomID) {
        this.roomID = roomID;
    }
}