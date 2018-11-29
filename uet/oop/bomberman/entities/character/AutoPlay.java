package uet.oop.bomberman.entities.character;

import javafx.util.Pair;
import uet.oop.bomberman.Board;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

public class AutoPlay {
    private Bomber _bomber;
    private Board _board;
    private boolean _enable;

    private int[][] BFSResult;
    private boolean placeBomb;
    private boolean placed;
    private static final int CANTMOVETO = -1;
    private static final int NOTFOUND = -1;
    private static final int[] tempX = { 0,1,0,-1 };
    private static final int[] tempY = { -1,0,1,0 };

    private Pair<Integer, Integer> nearestBrick;
    private Pair<Integer, Integer> nearestEnemy;
    private Pair<Integer, Integer> safeLocation;
    private Pair<Integer, Integer> nearestItem;
    private Pair<Integer, Integer> portal;

    private static Random r = new Random();

    public AutoPlay(Board _board, Bomber _bomber, boolean _enable) {
        this._enable = _enable;
        this._board = _board;
        this._bomber = _bomber;
        //BFSResult = new int[Board._map.length][Board._map[0].length];
        placeBomb = false;
        nearestEnemy = new Pair<>(NOTFOUND, NOTFOUND);
        portal = new Pair<>(NOTFOUND, NOTFOUND);
    }

    public boolean is_enable() {
        return _enable;
    }

    public void set_enable(boolean _enable) {
        this._enable = _enable;
    }

    public int calculateDirection() {
        BFS();
        findSafeLocation();

        int _direction = -1;
        if (_board.detectNoEnemies()) {
            if (notFound(portal)) {
                _direction = destroyBrick();
            } else {
                _direction = endGame();
            }
        } else {
            if (notFound(nearestEnemy)) {
                if (notFound(nearestItem)) {
                    _direction = destroyBrick();
                } else {
                    _direction = eatItem();
                }
            } else {
                _direction = killEnemy();
            }
        }

        if (_direction != -1) {
            System.out.println(_direction);
            System.out.println();
            for (char[] i : Board._map) {             // print map
                for (char j : i) {
                    System.out.print("  " + j);
                }
                System.out.println();
                System.out.println();
            }
            System.out.println();
            System.out.println();
            for (int[] i : BFSResult) {
                for (int j : i) {
                    System.out.print((j>=0&&j<=9)?"  " + j:" " + j);
                }
                System.out.println();
                System.out.println();
            }
            System.out.println();
            System.out.println();
        }

        return _direction;
    }

    public boolean calculatePlaceBomb() {
        if (placeBomb) {
            placeBomb = false;
            return true;
        }

        return false;
    }

    private void BFS() {
        BFSResult = new int[Board._map.length][Board._map[0].length];
        nearestBrick = new Pair<>(NOTFOUND, NOTFOUND);
        nearestEnemy = new Pair<>(NOTFOUND, NOTFOUND);
        nearestItem = new Pair<>(NOTFOUND, NOTFOUND);
        placed = false;

        boolean[][] visited = new boolean[Board._map.length][Board._map[0].length];
        LinkedList<Pair<Integer, Integer>> queue = new LinkedList<>();

        int bomberXPos = _bomber.getXTile();
        int bomberYPos = _bomber.getYTile();

        visited[bomberYPos][bomberXPos] = true;
        queue.add(new Pair<>(bomberXPos, bomberYPos));
        BFSResult[bomberYPos][bomberXPos] = 0;

        while (queue.size() > 0) {
            Pair<Integer, Integer> s = queue.poll();

            for (int i = 0; i < 4; i++) {
                int x = s.getKey() + tempX[i];
                int y = s.getValue() + tempY[i];

                if (!visited[y][x]) {
                    visited[y][x] = true;
                    if (Board._map[y][x] == ' ') {
                        queue.add(new Pair<>(x, y));
                        BFSResult[y][x] = BFSResult[s.getValue()][s.getKey()] + 1;
                    } else if (Board._map[y][x] == '+') {
                        if (Board._map[_bomber.getYTile()][_bomber.getXTile()] == '+') {
                            queue.add(new Pair<>(x, y));
                        }
                        BFSResult[y][x] = BFSResult[s.getValue()][s.getKey()] + 1;
                        placed = true;
                    } else if (Board._map[y][x] == 'i') {
                        queue.add(new Pair<>(x, y));
                        BFSResult[y][x] = BFSResult[s.getValue()][s.getKey()] + 1;
                        if (notFound(nearestItem)) {
                            nearestItem = new Pair<>(x, y);
                        }
                    } else if (Board._map[y][x] == '*') {
                        BFSResult[y][x] = BFSResult[s.getValue()][s.getKey()] + 1;
                        if (notFound(nearestBrick)) {
                            nearestBrick = new Pair<>(x, y);
                        }
                    } else if (Board._map[y][x] == 'e') {
                        BFSResult[y][x] = BFSResult[s.getValue()][s.getKey()] + 1;
                        if (notFound(nearestEnemy)) {
                            nearestEnemy = new Pair<>(x, y);
                        }
                    } else if (Board._map[y][x] == 'x') {
                        if (_board.detectNoEnemies()) {
                            BFSResult[y][x] = BFSResult[s.getValue()][s.getKey()] + 1;
                        } else {
                            BFSResult[y][x] = CANTMOVETO;
                        }
                        if (notFound(portal)) {
                            portal = new Pair<>(x, y);
                        }
                    } else {
                        BFSResult[y][x] = CANTMOVETO;
                    }
                }
            }
        }
    }

    private void findSafeLocation() {
        int bomberXPos = _bomber.getXTile();
        int bomberYPos = _bomber.getYTile();

        if (Board._map[bomberYPos][bomberXPos] == 'p') {
            safeLocation = new Pair<>(bomberXPos, bomberYPos);
        } else {
            safeLocation = new Pair<>(NOTFOUND, NOTFOUND);
            int enemyDirection = directionTo(nearestEnemy);
            boolean _break = false;

            LinkedList<Pair<Integer, Integer>> queue = new LinkedList<>();

            queue.add(new Pair<>(_bomber.getXTile(), _bomber.getYTile()));

            while (queue.size() > 0) {
                Pair<Integer, Integer> s = queue.poll();

                for (int i = 0; i < 4; i++) {
                    int x = s.getKey() + tempX[i];
                    int y = s.getValue() + tempY[i];

                    if (Board._map[y][x] == ' ') {
                        Pair<Integer, Integer> temp = new Pair<>(x, y);
                        if (directionTo(temp) != enemyDirection) {
                            safeLocation = temp;
                            _break = true;
                            break;
                        }
                    }

                    if (BFSResult[y][x] == BFSResult[s.getValue()][s.getKey()] + 1) {
                        queue.add(new Pair<>(x, y));
                    }
                }

                if (_break) {
                    break;
                }
            }
        }
    }

    private int directionTo(Pair<Integer, Integer> coordinate) {
        int result = -1;
        if (!notFound(coordinate)) {
            if (BFSResult[coordinate.getValue()][coordinate.getKey()] != 0) {
                while (BFSResult[coordinate.getValue()][coordinate.getKey()] > 1) {
                    for (int i = 0; i < 4; i++) {
                        int x = coordinate.getKey() + tempX[i];
                        int y = coordinate.getValue() + tempY[i];

                        if (BFSResult[coordinate.getValue()][coordinate.getKey()] - 1 == BFSResult[y][x] && Board._map[y][x] != '*') {
                            coordinate = new Pair<>(x, y);
                            break;
                        }
                    }
                }

                for (int i = 0; i < 4; i++) {
                    int x = coordinate.getKey() + tempX[i];
                    int y = coordinate.getValue() + tempY[i];

                    if (BFSResult[y][x] == 0) {
                        result = (2 + (i % 2)* 2 - i);
                        break;
                    }
                }
            }
        }

        return result;
    }

    private int destroyBrick() {
        int direction = -1;
        if (!placed) {
            if (!notFound(nearestBrick)) {
                if (BFSResult[nearestBrick.getValue()][nearestBrick.getKey()] == 1) {
                    placeBomb = true;
                } else {
                    direction = directionTo(nearestBrick);
                }
            }
        } else {
            if (Board._map[_bomber.getYTile()][_bomber.getXTile()] == '+') {
                direction = moveToSafeLocation();
            } else {
                if (!notFound(nearestBrick)) {
                    if (BFSResult[nearestBrick.getValue()][nearestBrick.getKey()] > 1) {
                        direction = directionTo(nearestBrick);
                    }
                }
            }
        }

        return direction;
    }

    private int killEnemy() {
        int direction = -1;

        if (!placed) {
            if (!notFound(nearestEnemy)) {
                int temp = 4;

                for (int i = 0; i < 4; i++) {
                    int x = nearestEnemy.getKey() + tempX[i];
                    int y = nearestEnemy.getValue() + tempY[i];

                    if (BFSResult[y][x] == BFSResult[nearestEnemy.getValue()][nearestEnemy.getKey()] - 1) {
                        int a = _board.getCharcterAt(nearestEnemy.getKey(), nearestEnemy.getValue()).getDirection();
                        if(a != i) {
                            temp = 3;
                        }
                        break;
                    }
                }
                if (BFSResult[nearestEnemy.getValue()][nearestEnemy.getKey()] < temp) {
                    placeBomb = true;
                } else {
                    direction = directionTo(nearestEnemy);
                }
            }
        } else {
            if (Board._map[_bomber.getYTile()][_bomber.getXTile()] != '+') {
                if (BFSResult[nearestEnemy.getValue()][nearestEnemy.getKey()] < 3) {
                    ArrayList<Integer> directions = new ArrayList<>();
                    directions.add(0);
                    directions.add(1);
                    directions.add(2);
                    directions.add(3);
                    int temp = directionTo(nearestEnemy);
                    int temp1 = 0;
                    for (int i = 0; i < directions.size(); i++) {
                        if (directions.get(i) == temp || Board._map[_bomber.getYTile() + tempY[i + temp1]][_bomber.getXTile() + tempX[i + temp1]] != ' ') {
                            directions.remove(i);
                            i--;
                            temp1++;
                        }
                    }

                    if (directions.size() > 0) {
                        direction = directions.get(r.nextInt(directions.size()));
                    }
                } else {
                    direction = moveToSafeLocation();
                }
            } else {
                direction = moveToSafeLocation();
            }
        }

        return direction;
    }

    private int moveToSafeLocation() {
        return directionTo(safeLocation);
    }

    private int eatItem() {
        return directionTo(nearestItem);
    }

    private int endGame() {
        return directionTo(portal);
    }

    private boolean notFound(Pair<Integer, Integer> entity) {
        return entity.getKey() == NOTFOUND;
    }
}