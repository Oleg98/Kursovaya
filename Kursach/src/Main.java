import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Main extends Application {
    private int fieldWidth = 30;
    private int fieldHeight = 16;
    private int cellWidth = 32;
    private int cellHeight = 32;
    private Field field;
    private GraphicsContext context;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        field = new Field(fieldWidth, fieldHeight);
        field.restart();

        stage.setTitle("Sapper");
        Group root = new Group();
        Canvas canvas = new Canvas(fieldWidth * cellWidth, fieldHeight * cellHeight);
        context = canvas.getGraphicsContext2D();

        draw();
        stage.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                if (e.getButton() == MouseButton.PRIMARY) {
                    int x = (int)Math.floor(e.getSceneX() / (double)cellWidth);
                    int y = (int)Math.floor(e.getSceneY() / (double)cellHeight);

                    try {
                        field.open(x, y);
                        draw();
                    }
                    catch(Exception exc) {
                        draw();

                        Alert alert = new Alert(Alert.AlertType.INFORMATION);

                        alert.setTitle("End game");
                        alert.setHeaderText(null);
                        alert.setContentText(exc.getMessage());

                        alert.showAndWait();
                        field.restart();
                        draw();
                    }
                }
                else if (e.getButton() == MouseButton.SECONDARY) {
                    int x = (int)Math.floor(e.getSceneX() / (double)cellWidth);
                    int y = (int)Math.floor(e.getSceneY() / (double)cellHeight);

                    field.getCell(x, y).toggleFlag();
                    draw();
                }
            }
        });

        root.getChildren().add(canvas);
        stage.setScene(new Scene(root));
        stage.show();
    }

    private void draw() {
        context.setFill(Color.ORANGE);
        context.fillRect(0, 0, fieldWidth * cellWidth, fieldHeight * cellHeight);

        for (int x = 0; x < fieldWidth; ++x) {
            for (int y = 0; y < fieldHeight; ++y) {
                Cell cell = field.getCell(x, y);
                if (cell.isBomb() && cell.isOpened()) {
                    context.setFill(Color.RED);
                    context.fillOval(x * cellWidth + 5, y * cellHeight + 5, cellWidth - 10, cellHeight - 10);
                }
                else if (cell.isOpened()) {
                    context.setFill(Color.ORANGE);
                    context.fillRect(x * cellWidth + 1, y * cellHeight + 1, cellWidth - 2, cellHeight - 2);

                    if (cell.getBombsAround() != 0) {
                        context.setFill(Color.BLUE);
                        context.setFont(new Font("Tahoma", 13));
                        context.fillText(String.valueOf(cell.getBombsAround()), (x + 0.38) * cellWidth, (y + 0.75) * cellHeight);
                    }
                }
                else {
                    if (cell.hasFlag()) {
                        context.setFill(Color.GREEN);
                    }
                    else {
                        context.setFill(Color.WHEAT);
                    }
                    context.fillRect(x * cellWidth + 1, y * cellHeight + 1, cellWidth - 2, cellHeight - 2);
                }
            }
        }
    }
}