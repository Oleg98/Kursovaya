import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.List;

public class Cell {
    private Field field;
    private int x;
    private int y;
    private boolean bomb;
    private boolean opened;
    private boolean flagged;

    Cell(Field field, int x, int y, boolean bomb) {
        this.field = field;
        this.x = x;
        this.y = y;
        this.bomb = bomb;
        this.opened = false;
        this.flagged = false;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isBomb() {
        return bomb;
    }

    public void open() {
        this.opened = true;
    }

    public boolean isOpened() {
        return opened;
    }

    public void toggleFlag() {
        flagged = !flagged;
    }

    public boolean hasFlag() {
        return flagged;
    }

    public int getBombsAround() {
        List<Cell> neighbours = getNeighbours();
        int bombCount = 0;
        for (Cell cell : neighbours) {
            if (cell.isBomb()) {
                ++bombCount;
            }
        }
        return bombCount;
    }

    public List<Cell> getNeighbours() {
        List<Cell> result = new ArrayList<>();
        for (int i = -1; i <= 1; ++i) {
            for (int j = -1; j <= 1; ++j) {
                if (i != 0 || j != 0) {
                    Cell cell = field.getCell(x + i, y + j);
                    if (cell != null) {
                        result.add(cell);
                    }
                }
            }
        }
        return result;
    }
}
