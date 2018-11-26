package uet.oop.bomberman.entities.tile.item;

import Sound.Sound;
import uet.oop.bomberman.Game;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.character.Bomber;
import uet.oop.bomberman.graphics.Sprite;

public class SpeedItem extends Item {

	public SpeedItem(int x, int y, Sprite sprite) {
		super(x, y, sprite);
	}

	@Override
	public boolean collide(Entity e) {
		// TODO: xử lý Bomber ăn Item

        if(e instanceof Bomber) {
            Game.addBomberSpeed(0.1);
            Sound soundItem = new Sound("item.wav");
            soundItem.play();
            remove();
            return true;
        }

		return false;
	}
}