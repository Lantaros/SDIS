package client;


import java.util.Timer;
import java.util.TimerTask;

class GameThread implements Runnable {

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
            Client.handleNextTurn();
        }

        if(this.toDo.equals("next_turn")){
             Client.handleNextTurn();
        }

        if(this.toDo.equals("block_3seconds")) {
            
            Timer ti = new Timer();
            TimerTask tti = new TimerTask() {
            int time = 3;
            
                @Override
                public void run() {
                    time = time - 1;                   

                    if(time == 0) {
                        Client.launcher.getFrame().gamePanel.setButtonWord(true);
                        ti.cancel();
                        ti.purge();
                        return;
                    }
                };
            };ti.schedule(tti, 0, 1000);
        }

        if(this.toDo.equals("unblock_letter")) {
            Timer ti = new Timer();
            TimerTask tti = new TimerTask() {
            int time = 3;            
                @Override
                public void run() {
                    time = time - 1;                   
                    if(Client.cancel) {
                        System.out.println("OIOI");
                        Client.cancel = false;
                        ti.cancel();
                        ti.purge();
                        return;
                    }
                    if(time == 0) {
                        //3seconds passed 
                        System.out.println("OIOI2");                       
                        Client.removeClient();
                        
                        ti.cancel();
                        ti.purge();
                        return;
                    }
                    
                };
            };ti.schedule(tti, 0, 1000);
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

}