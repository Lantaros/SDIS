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
                        t.cancel();
                        t.purge();
                        Client.advanceTurn();
                        return;
                    }
                };
            };t.schedule(tt, 0, 1000);
        }

        if(this.toDo.equals("timer_up")) {
            
            Timer ti = new Timer();
            TimerTask tti = new TimerTask() {
            int time = 4;            
                @Override
                public void run() {
                    time = time - 1;                   
                    if(Client.cancel) {
                    	
                        Client.cancel = false;
                        Client.confirmTimerUP = 0;
                        ti.cancel();
                        ti.purge();
                        return;
                    }
                    if(time == 0) {
                        //3seconds passed  
                    	//Client.handleNextTurn();
                        //Client.confirmMsg.add(0);
                    	Client.confirmTimerUP = 0;
                        ti.cancel();
                        ti.purge();
                        return;
                    }
                    
                };
            };ti.schedule(tti, 0, 1000);
        }

        if(this.toDo.equals("next_turn")){
             Client.handleNextTurn();
        }

        if(this.toDo.equals("block_3seconds")) {
            
            Timer ti = new Timer();
            TimerTask tti = new TimerTask() {
            int time = 4;
            
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
            int time = 4;            
                @Override
                public void run() {
                    time = time - 1;                   
                    if(Client.cancelLetter) {
                        Client.cancelLetter = false;
                        ti.cancel();
                        ti.purge();
                        return;
                    }
                    if(time == 0) {
                        //3seconds passed  
                    	Client.cancelLetter = true;
                        Client.removeClient();
                        //Client.confirmMsg.add(0);
                        ti.cancel();
                        ti.purge();
                        return;
                    }
                    
                };
            };ti.schedule(tti, 0, 1000);
        }
        
        if(this.toDo.equals("unblock_turn")) {
            Timer ti = new Timer();
            TimerTask tti = new TimerTask() {
            int time = 4;            
                @Override
                public void run() {
                    time = time - 1;                   
                    if(Client.cancelTurn) {
                        Client.cancelTurn = false;
                        ti.cancel();
                        ti.purge();
                        return;
                    }
                    if(time == 0) {
                        //3seconds passed   
                    	Client.cancelTurn = true;
                        Client.removeClientTurn();
                        ti.cancel();
                        ti.purge();
                        return;
                    }
                    
                };
            };ti.schedule(tti, 0, 1000);
        }
        
        if(this.toDo.equals("unblock_word")) {
            Timer ti = new Timer();
            TimerTask tti = new TimerTask() {
            int time = 4;            
                @Override
                public void run() {
                    time = time - 1;                   
                    if(Client.cancelWord) {
                        Client.cancelWord = false;
                        ti.cancel();
                        ti.purge();
                        return;
                    }
                    if(time == 0) {
                        //3seconds passed  
                    	Client.cancelWord = true;
                        Client.removeClientWord();
                        
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