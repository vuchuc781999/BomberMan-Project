package uet.oop.bomberman.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Tiếp nhận và xử lý các sự kiện nhập từ bàn phím
 */
public class Keyboard implements KeyListener {
	
	private boolean[] keys = new boolean[120]; //120 is enough to this game
	public boolean up, down, left, right, space, esc, Enter, BACK;
	private String name;
	private int muc = 1;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
	
	public void update() {
		up = keys[KeyEvent.VK_UP] || keys[KeyEvent.VK_W];
		down = keys[KeyEvent.VK_DOWN] || keys[KeyEvent.VK_S];
		left = keys[KeyEvent.VK_LEFT] || keys[KeyEvent.VK_A];
		right = keys[KeyEvent.VK_RIGHT] || keys[KeyEvent.VK_D];
		space = keys[KeyEvent.VK_SPACE] || keys[KeyEvent.VK_X];
        esc = keys[KeyEvent.VK_ESCAPE];
        Enter = keys[KeyEvent.VK_ENTER];
        BACK = keys[KeyEvent.VK_BACK_SPACE];
	}

	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyPressed(KeyEvent e) {
		keys[e.getKeyCode()] = true;
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() != KeyEvent.VK_ESCAPE && e.getKeyCode()!= KeyEvent.VK_ENTER &&e.getKeyCode() !=KeyEvent.VK_BACK_SPACE )
        {
            keys[e.getKeyCode()] = false;
            name = "";
            name = String.valueOf(e.getKeyChar());
        }
        if (e.getKeyCode() == KeyEvent.VK_UP)
        {
            muc--;
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN)
        {
            muc ++;
        }
        if (muc< 0) muc = 3;
        muc = muc % 4;
		
	}

    public void setMuc(int muc) {
        this.muc = muc;
    }

    public void setEsc()
    {
        keys[KeyEvent.VK_ESCAPE] = false;
    }

    public void setEnter()
    {
        keys[KeyEvent.VK_ENTER]= false;
    }

    public boolean getEsc()
    {
        return esc;
    }

    public int updatemuc() {
        return muc;
    }

    public void setESC2() {
        esc = true;
    }

    public void setBack() {
        keys[KeyEvent.VK_BACK_SPACE]= false;
    }
}
