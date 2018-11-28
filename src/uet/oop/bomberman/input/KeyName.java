package uet.oop.bomberman.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyName implements KeyListener {
    public String getT() {
        return t;
    }

    public void setT(String t) {
        this.t = t;
    }

    String t ;

    public  boolean enter = false;
    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER)
            enter = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        String temp = "";
        if (e.getKeyCode()!= KeyEvent.VK_ENTER)
        {
            temp = e.getKeyChar()+"";
        }
        else
        {
            enter = true;
        }
        t = temp;

    }
}
