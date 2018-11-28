package uet.oop.bomberman;

import Sound.Sound;
import uet.oop.bomberman.graphics.Screen;
import uet.oop.bomberman.gui.Frame;
import uet.oop.bomberman.input.KeyName;
import uet.oop.bomberman.input.Keyboard;
import uet.oop.bomberman.output.Player;

import javax.sound.sampled.Clip;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

/**
 * Tạo vòng lặp cho game, lưu trữ một vài tham số cấu hình toàn cục,
 * Gọi phương thức render(), update() cho tất cả các entity
 */
public class Game extends Canvas {

	public static final int TILES_SIZE = 16,
							WIDTH = TILES_SIZE * (31 / 2),
							HEIGHT = 13 * TILES_SIZE;

	public static int SCALE = 3;

	public static final String TITLE = "BombermanGame";
	
	private static final int BOMBRATE =2;
	private static final int BOMBRADIUS = 1;
	private static final double BOMBERSPEED = 1.0;
	
	public static final int TIME = 200;
	public static final int POINTS = 0;
	
	protected static int SCREENDELAY = 3;
	protected  static  int SCREENWIN =4;
	protected static int bombRate = BOMBRATE;
	protected static int bombRadius = BOMBRADIUS;
	protected static double bomberSpeed = BOMBERSPEED;
	
	public Player player;
	protected int _screenDelay = SCREENDELAY;
	public Sound soundBackground;
	private Keyboard _input;
	private boolean _running = false;
	private boolean _paused = true;
	
	private Board _board;
	private Screen screen;
	private Frame _frame;
	
	private BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
	private int[] pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
	
	public Game(Frame frame) {
		_frame = frame;
		_frame.setTitle(TITLE);
		
		screen = new Screen(WIDTH, HEIGHT);
		_input = new Keyboard();
		
		_board = new Board(this, _input, screen);
		addKeyListener(_input);
		soundBackground = new Sound("soundBackground.wav");
		player = _board.player;
	}

	private void renderGame() {
		BufferStrategy bs = getBufferStrategy();
		if(bs == null) {
			createBufferStrategy(3);
			return;
		}
		
		screen.clear();
		
		_board.render(screen);
		
		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = screen._pixels[i];
		}
		
		Graphics g = bs.getDrawGraphics();
		
		g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
		_board.renderMessages(g);
		
		g.dispose();
		bs.show();
	}
	
	private void renderScreen() {
		BufferStrategy bs = getBufferStrategy();
		if(bs == null) {
			createBufferStrategy(3);
			return;
		}
		
		screen.clear();
		
		Graphics g = bs.getDrawGraphics();
		
		_board.drawScreen(g);

		g.dispose();
		bs.show();
	}
	private void update() {
		_input.update();
		_board.update();

	}
	int muc =1;
	public void start(Frame _frame)  {
		NhapTen();
		_running = true;
		long  lastTime = System.nanoTime();
		long timer = System.currentTimeMillis();
		final double ns = 1000000000.0 / 60.0; //nanosecond, 60 frames per second
		double delta = 0;
		int frames = 0;
		int updates = 0;
		requestFocus();
		_input.setEnter();
		while(_running) {

			batESC();
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while(delta >= 1) {
				update();
				updates++;
				delta--;
			}
			
			if(_paused) {
				soundBackground.stop();
				if(_screenDelay <= 0) {
					_board.setShow(-1);
					_paused = false;
					soundBackground.stop();
				}
					
				renderScreen();
			} else {
				soundBackground.play();
				soundBackground.setLoop(); // set background lặp
				renderGame();
			}
				
			
			frames++;
			if(System.currentTimeMillis() - timer > 1000) {
				_frame.setTime(_board.subtractTime());
				_frame.setPoints(_board.getPoints());
				timer += 1000;
				_frame.setTitle(TITLE + " | " + updates + " rate, " + frames + " fps");
				updates = 0;
				frames = 0;
				
				if(_board.getShow() == 2)
					--_screenDelay;
			}
		}
	}

	private void batESC() {
		muc = 1 ;
		long  lastTime = System.nanoTime();
		final double ns = 1000000000.0 /60; //nanosecond, 60 frames per second
		double delta = 0;
		long now;
		_input.setMuc(0);
		_input.setEnter();

		while (_input.getEsc() ) {

			now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while(delta >= 1) {
				_input.update();
				delta--;
			}
			updatemuc();
			_board.setShow(3);
			renderScreen();
			if (_input.Enter)
			{
				switch (muc)
				{
					case 0 : {
						_input.setEsc();
						_input.setEnter();
						return;
					}
					case 1 :
					{
						_board.loadLevel(1);
						_frame.setPoints(0);
						_frame.setTime(200);
						_input.setEsc();
						_input.setEnter();
						return;
					}
					case 2 :
					{
						_input.setEnter();
						 LoadDS();
						_input.setEnter();
						_input.setBack();
						_input.update();
						break;
					}
					case 3:
					{
						System.exit(0);
					}
				}

			}

		}
	}

	private void LoadDS() {
		_board.setShow(5);
		long  lastTime = System.nanoTime();
		final double ns = 1000000000.0 /60; //nanosecond, 60 frames per second
		double delta = 0;
		long now;

		_input.Enter = false;
		while (!_input.BACK)
		{
				now = System.nanoTime();
				delta += (now - lastTime) / ns;
				lastTime = now;
				while(delta >= 1) {
					_input.update();
					delta--;

				}


			renderScreen();
		}
	}
	public  void NhapTen()
	{

		String t = "";
		long  lastTime = System.nanoTime();
		final double ns = 1000000000.0 /60; //nanosecond, 60 frames per second
		double delta = 0;
		long now;
		while (!_input.Enter )
		{
			now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while(delta >= 1) {
				_input.update();
				delta--;
			}
			if (_input.getName()!= null && _input.getName()!="")
			{
				t+= _input.getName();
				//System.out.println("ádasda");
				_input.setName("");
			}
			if (_input.BACK)
			{
				if (t.length()>0)
				t = t.substring(0, t.length()-1);
				_input.setBack();
			}
			renderName(t);
		}
		_input.setEnter();
		player.setName(t);
	}

	private void renderName(String t) {
		BufferStrategy bs = getBufferStrategy();
		if(bs == null) {
			createBufferStrategy(3);
			return;
		}

		screen.clear();

		Graphics g = bs.getDrawGraphics();

		_board._screen.drawName(g,t);

		g.dispose();
		bs.show();
	}
	private void updatemuc() {
		muc = _input.updatemuc();
	}

	public static double getBomberSpeed() {
		return bomberSpeed;
	}
	
	public static int getBombRate() {
		return bombRate;
	}
	
	public static int getBombRadius() {
		return bombRadius;
	}
	
	public static void addBomberSpeed(double i) {
		bomberSpeed += i;
	}
	
	public static void addBombRadius(int i) {
		bombRadius += i;
	}
	
	public static void addBombRate(int i) {
		bombRate += i;
	}

	public void resetScreenDelay() {
		_screenDelay = SCREENDELAY;
	}

	public Board getBoard() {
		return _board;
	}

	public boolean isPaused() {
		return _paused;
	}
	
	public void pause() {
		_paused = true;
	}
	
}
