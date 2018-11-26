package uet.oop.bomberman.level;

import uet.oop.bomberman.Board;
import uet.oop.bomberman.Game;
import uet.oop.bomberman.entities.LayeredEntity;
import uet.oop.bomberman.entities.character.Bomber;
import uet.oop.bomberman.entities.character.enemy.Balloon;
import uet.oop.bomberman.entities.character.enemy.Oneal;
import uet.oop.bomberman.entities.tile.Grass;
import uet.oop.bomberman.entities.tile.Portal;
import uet.oop.bomberman.entities.tile.Wall;
import uet.oop.bomberman.entities.tile.destroyable.Brick;
import uet.oop.bomberman.entities.tile.item.BombItem;
import uet.oop.bomberman.entities.tile.item.FlameItem;
import uet.oop.bomberman.entities.tile.item.SpeedItem;
import uet.oop.bomberman.exceptions.LoadLevelException;
import uet.oop.bomberman.graphics.Screen;
import uet.oop.bomberman.graphics.Sprite;

import java.io.File;
import java.net.URL;
import java.util.Scanner;

public class FileLevelLoader extends LevelLoader {

	/**
	 * Ma trận chứa thông tin bản đồ, mỗi phần tử lưu giá trị kí tự đọc được
	 * từ ma trận bản đồ trong tệp cấu hình
	 */
	private static char[][] _map;
	
	public FileLevelLoader(Board board, int level) throws LoadLevelException {
		super(board, level);
	}
	
	@Override
	public void loadLevel(int level) throws LoadLevelException {
		// TODO: đọc dữ liệu từ tệp cấu hình /levels/Level{level}.txt
		// TODO: cập nhật các giá trị đọc được vào _width, _height, _level, _map

        String _path = "/levels/Level" + Integer.toString(level) + ".txt";
        System.out.println(_path);
        try {
            URL url = FileLevelLoader.class.getResource(_path);
            String _url = url.getPath();
            File mapData = new File(_url);
            Scanner sc = new Scanner(mapData);

           _level = sc.nextInt();  // đọc giá trị level
            _height = sc.nextInt(); // đọc số hàng
            _width = sc.nextInt();  // đọc số cột
            sc.nextLine();

            _map = new char[_height][_width];

            for (int i = 0; i < _height; i++) {
                String row = sc.nextLine();
                for (int j = 0; j < _width; j++) {
                    _map[i][j] = row.charAt(j);
                }
            }
        } catch (Exception e) {
            throw new LoadLevelException("Fail to load levelsssss" + _path, e);
        }
    }

	@Override
	public void createEntities() {
		// TODO: tạo các Entity của màn chơi
		// TODO: sau khi tạo xong, gọi _board.addEntity() để thêm Entity vào game

		// TODO: phần code mẫu ở dưới để hướng dẫn cách thêm các loại Entity vào game
		// TODO: hãy xóa nó khi hoàn thành chức năng load màn chơi từ tệp cấu hình

        for (int x = 0; x < _width; x++) {
            for (int y = 0; y < _height; y++) {
                switch (_map[y][x]) {
                    case '#':
                        _board.addEntity(x + y * _width, new Wall(x, y, Sprite.wall));
                        break;
                    case 'p':
                        _board.addCharacter( new Bomber(Coordinates.tileToPixel(x), Coordinates.tileToPixel(y) + Game.TILES_SIZE, _board) );
                        Screen.setOffset(0, 0);
                        _board.addEntity(x + y * _width, new Grass(x, y, Sprite.grass));
                        break;
                    case '1':
                        _board.addCharacter( new Balloon(Coordinates.tileToPixel(x), Coordinates.tileToPixel(y) + Game.TILES_SIZE, _board));
                        _board.addEntity(x + y * _width, new Grass(x, y, Sprite.grass));
                        break;
                    case '2':
                        _board.addCharacter( new Oneal(Coordinates.tileToPixel(x), Coordinates.tileToPixel(y) + Game.TILES_SIZE, _board));
                        _board.addEntity(x + y * _width, new Grass(x, y, Sprite.grass));
                        break;
                    case '*':
                        _board.addEntity(x + y * _width,
                                new LayeredEntity(x, y,
                                        new Grass(x, y, Sprite.grass),
                                        new Brick(x, y, Sprite.brick)
                                )
                        );
                        break;
                    case 'b':
                        _board.addEntity(x + y * _width,
                                new LayeredEntity(x, y,
                                        new Grass(x ,y, Sprite.grass),
                                        new BombItem(x, y, Sprite.powerup_bombs),
                                        new Brick(x, y, Sprite.brick)
                                )
                        );
                        break;
                    case 'f':
                        _board.addEntity(x + y * _width,
                                new LayeredEntity(x, y,
                                        new Grass(x ,y, Sprite.grass),
                                        new FlameItem(x, y, Sprite.powerup_flames),
                                        new Brick(x, y, Sprite.brick)
                                )
                        );
                        break;
                    case 's':
                        _board.addEntity(x + y * _width,
                                new LayeredEntity(x, y,
                                        new Grass(x ,y, Sprite.grass),
                                        new SpeedItem(x, y, Sprite.powerup_speed),
                                        new Brick(x, y, Sprite.brick)
                                )
                        );
                        break;
                    case 'x':
                        _board.addEntity(x + y * _width,
                                new LayeredEntity(x, y,
                                        new Grass(x ,y, Sprite.grass),
                                        new Portal(x, y, _board, Sprite.portal),
                                        new Brick(x, y, Sprite.brick)
                                )
                        );
                        break;
                    default:
                        _board.addEntity(x + y * _width, new Grass(x, y, Sprite.grass));
                        break;
                }
            }
        }
        /*
        // thêm Wall
		for (int x = 0; x < _width; x++) {
			for (int y = 0; y < _height; y++) {
				int pos = x + y * _width;
				Sprite sprite = _map[y][x] == '#' ? Sprite.wall : Sprite.grass;
				_board.addEntity(pos, new Grass(x, y, sprite));
			}
		}

		// thêm Bomber
		int xBomber = 1, yBomber = 1;
		_board.addCharacter( new Bomber(Coordinates.tileToPixel(xBomber), Coordinates.tileToPixel(yBomber) + Game.TILES_SIZE, _board) );
		Screen.setOffset(0, 0);
		_board.addEntity(xBomber + yBomber * _width, new Grass(xBomber, yBomber, Sprite.grass));

		// thêm Enemy
		int xE = 2, yE = 1;
		_board.addCharacter( new Balloon(Coordinates.tileToPixel(xE), Coordinates.tileToPixel(yE) + Game.TILES_SIZE, _board));
		_board.addEntity(xE + yE * _width, new Grass(xE, yE, Sprite.grass));

		// thêm Brick
		int xB = 3, yB = 1;
		_board.addEntity(xB + yB * _width,
				new LayeredEntity(xB, yB,
					new Grass(xB, yB, Sprite.grass),
					new Brick(xB, yB, Sprite.brick)
				)
		);

		// thêm Item kèm Brick che phủ ở trên
		int xI = 1, yI = 2;
		_board.addEntity(xI + yI * _width,
				new LayeredEntity(xI, yI,
					new Grass(xI ,yI, Sprite.grass),
					new SpeedItem(xI, yI, Sprite.powerup_flames),
					new Brick(xI, yI, Sprite.brick)
				)
		);
		*/
	}

}
