package miner;

public class Game {

    private Bomb bomb;
    private Flag flag;


    private GameState state;

    public Game(int cols, int rows, int bombs) {
        Ranges.setSize(new Coord(cols, rows));
        bomb = new Bomb(bombs);
        flag = new Flag();
    }

    public void start() {
        bomb.start();
        flag.start();
        state = GameState.PLAYED;
    }

    public Box getBox(Coord coord) {
        if (Box.OPENED == flag.get(coord))
            return bomb.get(coord);
        else
            return flag.get(coord);

    }


    public void pressLeftButton(Coord coord) {
        if (isGameOver()) return;
        openBox(coord);
        checkWinner();
    }

    public void pressRightButton(Coord coord) {
        if (GameState.PLAYED == state) {
            flag.toggleFlagedToBox(coord);
        } else start();

    }

    public GameState getState() {
        return state;
    }

    public int getTotalBombs() {
        return bomb.getTotalBombs();
    }

    public int getTotalFlaged() {
        return flag.getTotalFlaged();
    }

    private boolean isGameOver() {
        if (GameState.PLAYED != state) {
            start();
            return true;
        }
        return false;
    }

    private void checkWinner() {
        if (GameState.PLAYED == state)
            if (flag.getTotalClosed() == bomb.getTotalBombs()) {
                state = GameState.WINNER;
                flag.setFlagedToLastClosedBoxes();
            }
    }


    private void openBox(Coord coord) {
        switch (flag.get(coord)) {
            case OPENED:
                break;
            case FLAGED:
                break;
            case CLOSED:
                switch (bomb.get(coord)) {
                    case ZERO:
                        openBoxesAroundZero(coord);
                        break;
                    case BOMB:
                        openBombs(coord);
                        break;
                    default:
                        flag.setOpenedToBox(coord);
                        break;
                }
        }
    }

    private void openBombs(Coord bombedCoord) {
        flag.setBombedToBox(bombedCoord);
        for (Coord coord : Ranges.getAllCoords())
            if (bomb.get(coord) == Box.BOMB)
                flag.setOpenedToClosedBox(coord);
            else
                flag.setNoBombToFlagedBox(coord);
        state = GameState.BOMBED;
    }

    private void openBoxesAroundZero(Coord coord) {
        System.out.println(coord.x + " " + coord.y);
        flag.setOpenedToBox(coord);
        for (Coord around : Ranges.getCoordsAround(coord))
            openBox(around);
    }
}
