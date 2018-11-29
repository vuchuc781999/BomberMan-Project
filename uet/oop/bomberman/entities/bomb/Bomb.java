package uet.oop.bomberman.entities.bomb;

import Sound.Sound;
import uet.oop.bomberman.Board;
import uet.oop.bomberman.Game;
import uet.oop.bomberman.entities.AnimatedEntitiy;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.character.Bomber;
import uet.oop.bomberman.entities.character.Character;
import uet.oop.bomberman.graphics.Screen;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.level.Coordinates;

public class Bomb extends AnimatedEntitiy {

	protected double _timeToExplode = 120; //2 seconds
	public int _timeAfter = 20;
	
	protected Board _board;
	protected Flame[] _flames;
	protected int[] _flameLengths;
	protected boolean _exploded = false;
	protected boolean _allowedToPassThru = true;
    public Sound soundExplosion;
	
	public Bomb(int x, int y, Board board) {
		_x = x;
		_y = y;
		_board = board;
		_sprite = Sprite.bomb;

		soundExplosion = new Sound("soundExplosion.wav");

		_flameLengths = new int[4];
		int maxRadius = Game.getBombRadius();

		board._map[y][x] = '+';
		for (int i = 0; i < 4; i++) {
		    int __x = x;
		    int __y = y;

            while(_flameLengths[i] < maxRadius) {
                if(i == 0) __y--;
                if(i == 1) __x++;
                if(i == 2) __y++;
                if(i == 3) __x--;

                Entity a = _board.getEntity(__x, __y, null);

                //if(a instanceof Character) ++radius;

                if(a.collide(this) == false && a instanceof Character == false)
                    break;

                ++_flameLengths[i];
                board._map[__y][__x] = '+';
            }
        }
	}
	
	@Override
	public void update() {
		if(_timeToExplode > 0) 
			_timeToExplode--;
		else {
			if(!_exploded) 
				explode();
			else
				updateFlames();
			
			if(_timeAfter > 0) 
				_timeAfter--;
			else {
                remove();

                _board._map[(int) _y][(int) _x] = ' ';
                for (int i = 0; i < 4; i++) {
                    int __x = (int) _x;
                    int __y = (int) _y;

                    for (int j = 0; j < _flameLengths[i]; j++) {
                        if(i == 0) __y--;
                        if(i == 1) __x++;
                        if(i == 2) __y++;
                        if(i == 3) __x--;

                        _board._map[__y][__x] = ' ';
                    }
                }
            }
		}
			
		animate();
	}
	
	@Override
	public void render(Screen screen) {
		if(_exploded) {
			_sprite =  Sprite.bomb_exploded2;
			renderFlames(screen);
		} else
			_sprite = Sprite.movingSprite(Sprite.bomb, Sprite.bomb_1, Sprite.bomb_2, _animate, 60);
		
		int xt = (int)_x << 4;
		int yt = (int)_y << 4;
		
		screen.renderEntity(xt, yt , this);
	}
	
	public void renderFlames(Screen screen) {
		for (int i = 0; i < _flames.length; i++) {
			_flames[i].render(screen);
		}
	}
	
	public void updateFlames() {
		for (int i = 0; i < _flames.length; i++) {
			_flames[i].update();
		}
	}

    /**
     * Xử lý Bomb nổ
     */
	protected void explode() {
		_exploded = true;
		soundExplosion.play();
		
		// TODO: xử lý khi Character đứng tại vị trí Bomb

        _allowedToPassThru = true;
        Character a = _board.getCharcterAt(_x, _y);
        if (a != null) {
            a.kill();
        }
		// TODO: tạo các Flame

        _flames = new Flame[4];

        for (int i = 0; i < _flames.length; i++) {
            _flames[i] = new Flame((int)_x, (int)_y, i, Game.getBombRadius(), _board);
        }
	}
	
	public FlameSegment flameAt(int x, int y) {
		if(!_exploded) return null;
		
		for (int i = 0; i < _flames.length; i++) {
			if(_flames[i] == null) return null;
			FlameSegment e = _flames[i].flameSegmentAt(x, y);
			if(e != null) return e;
		}
		
		return null;
	}

	@Override
	public boolean collide(Entity e) {
        // TODO: xử lý khi Bomber đi ra sau khi vừa đặt bom (_allowedToPassThru)
        // TODO: xử lý va chạm với Flame của Bomb khác

        if(e instanceof Bomber) {
            double diffX = e.getX() - Coordinates.tileToPixel(getX());
            double diffY = e.getY() - Coordinates.tileToPixel(getY());

            if(!(diffX >= -10 && diffX < 16 && diffY >= 1 && diffY <= 28)) { // sau khi ra khỏi bom vừa đặt thì không thể đi qua bom
                _allowedToPassThru = false;
            }

            return _allowedToPassThru;
        }

        if(e instanceof Flame) {
            _timeToExplode = 0;
            return true;
        }

        return false;
	}
}
