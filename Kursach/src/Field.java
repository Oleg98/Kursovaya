import javafx.scene.canvas.GraphicsContext;

import java.util.List;
import java.util.Random;
import java.util.Stack;

public class Field {
    private int width;
    private int height;
    private Cell[][] cells;
    private boolean initialized;

    public class WonGameException extends Exception {
        public WonGameException() {
            super("You won!");
        }
    }

    public class LooseGameException extends Exception {
        public LooseGameException() {
            super("You loose!");
        }
    }

    public Field(int width, int height) {
        this.width = width;
        this.height = height;
        this.initialized = false;
        cells = new Cell[width][height];
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Cell getCell(int x, int y) {
        if (isPositionValid(x, y)) {
            return cells[x][y];
        }
        else {
            return null;
        }
    }

    public void open(int x, int y) throws Exception {
        if (!isPositionValid(x, y)) {
            return;
        }

        if (!initialized) {
            init(x, y);
        }

        Cell currentCell = getCell(x, y);
        if (currentCell.isBomb()) {
            for (int i = 0; i < width; ++i) {
                for (int j = 0; j < height; ++j) {
                    if (cells[i][j].isBomb()) {
                        cells[i][j].open();
                    }
                }
            }

            throw new LooseGameException();
        }
        else {
            boolean[][] visited = new boolean[width][height];
            for (int i = 0; i < width; ++i) {
                for (int j = 0; j < height; ++j) {
                    visited[i][j] = false;
                }
            }

            Stack<Cell> stack = new Stack<>();
            stack.push(currentCell);
            while (!stack.isEmpty()) {
                currentCell = stack.pop();

                if (!visited[currentCell.getX()][currentCell.getY()]) {
                    currentCell.open();

                    if (currentCell.getBombsAround() == 0) {
                        List<Cell> neighbours = currentCell.getNeighbours();
                        for (Cell cell : neighbours) {
                            if (!cell.isBomb()) {
                                stack.push(cell);
                            }
                        }
                    }

                    visited[currentCell.getX()][currentCell.getY()] = true;
                }
            }

            boolean allOpened = true;
            for (int i = 0; i < width && allOpened; ++i) {
                for (int j = 0; j < height; ++j) {
                    Cell cell = cells[i][j];
                    if (!cell.isBomb() && !cell.isOpened()) {
                        allOpened = false;
                        break;
                    }
                }
            }

            if (allOpened) {
                throw new WonGameException();
            }
        }
    }

    public void restart() {
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                cells[x][y] = new Cell(this, x, y, false);
            }
        }
        initialized = false;
    }

    private void init(int startX, int startY) {
        Random random = new Random();
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                int value = random.nextInt(100);
                if (value <=10 && x != startX && y != startY) {
                    cells[x][y] = new Cell(this, x, y, true);
                }
            }
        }

        initialized = true;
    }

    private boolean isPositionValid(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }
}
