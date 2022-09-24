package Connect_4;

import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import static javafx.application.Application.launch;

public class GUIGameBoard extends Application {
    public static final int Tile = 100;
    public static final int COL = 7;
    public static final int ROW = 6;

    public Disc[][] grid = new Disc[COL][ROW];
    public Button button1 = new Button("Player Start");
    public Button button2 = new Button("Computer Start");
    public Button button3 = new Button("Repeat");

    public Pane discRoot = new Pane();
    public Pane Pane = new Pane();
    public Text Message = new Text();

    public Parent createContent() {
        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);
        root.setSpacing(20);
        root.setPadding(new Insets(20));


        
        button1.setStyle("-fx-font-weight: bold;-fx-font-size: 20");
        button1.setTextFill(Color.DARKBLUE);
        button1.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                startNewGame();


            }
        });

        
        button2.setStyle("-fx-font-weight: bold;-fx-font-size: 20");
        button2.setTextFill(Color.DARKRED);
        button2.setVisible(true);
        button2.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                startNewGame();
                placeDisc(new Disc(false), AIMinMax.AIMove(),false);
            }
        });
        
        button3.setStyle("-fx-font-weight: bold;-fx-font-size: 20");
        button3.setVisible(true);
        button3.setTextFill(Color.FORESTGREEN);
        button3.setVisible(false);
        button3.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                button1.setVisible(true);
                button2.setVisible(true);
                button3.setVisible(false);

            }
        });


        Message.setFont(Font.font(18));
        Message.setStyle("-fx-font-weight: bold");


        HBox hBoxButtons = new HBox();
        hBoxButtons.setAlignment(Pos.CENTER_LEFT);
        hBoxButtons.setSpacing(60);
        hBoxButtons.getChildren().addAll(button1, button3, button2);
        hBoxButtons.setAlignment(Pos.CENTER);

        root.getChildren().add(hBoxButtons);
        root.getChildren().add(Message);
        Shape gridShape = makeGrid();

        Pane.getChildren().add(discRoot);
        Pane.getChildren().add(gridShape);
        Pane.getChildren().addAll(makeColumns());
        freezeConnect4Board();
        root.getChildren().add(Pane);
        return root;
    }

    public void freezeConnect4Board() {
        Pane.setDisable(true);
    }

    public void unfreezeConnect4Board() {
        Pane.setDisable(false);

    }

    public void startNewGame() {
        button3.setVisible(true);
        button2.setVisible(false);
        button1.setVisible(false);
        Message.setVisible(false);
        grid = new Disc[COL][ROW];
        discRoot.getChildren().clear();
        AIMinMax.board();
        unfreezeConnect4Board();
    }

    public Shape makeGrid() {
        Shape shape = new Rectangle((COL + 1) * Tile, (ROW + 1) * Tile);

        for (int y = 0; y < ROW; y++) {
            for (int x = 0; x < COL; x++) {
                Circle circle = new Circle(Tile / 2);
                circle.setCenterX(Tile / 2);
                circle.setCenterY(Tile / 2);
                circle.setTranslateX(x * (Tile + 5) + Tile / 4);
                circle.setTranslateY(y * (Tile + 5) + Tile / 4);

                shape = Shape.subtract(shape, circle);
            }
        }
        shape.setFill(Color.DARKSLATEGREY);
      

        return shape;
    }

    public List<Rectangle> makeColumns() {
        List<Rectangle> list = new ArrayList<>();

        for (int x = 0; x < COL; x++) {
            Rectangle rect = new Rectangle(Tile, (ROW + 1) * Tile);
            rect.setTranslateX(x * (Tile + 5) + Tile / 4);
            rect.setFill(Color.TRANSPARENT);

            rect.setOnMouseEntered(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    rect.setFill(Color.TRANSPARENT);

                }
            });
            

            final int column = x;
            rect.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {


                    
                    placeDisc(new Disc(true), column,true);
                    AIMinMax.BoardUpdate(column, AIMinMax.Player2);

                }
            });

            list.add(rect);
        }

        return list;
    }

    public void placeDisc(Disc disc, int column,boolean playerTurn) {
        freezeConnect4Board();
        boolean redMove = disc.blue;
        int row = ROW - 1;
        do {
            if (!getDisc(column, row).isPresent())
                break;

            row--;
        } while (row >= 0);

        if (row < 0)
            return;

        grid[column][row] = disc;
        discRoot.getChildren().add(disc);
        disc.setTranslateX(column * (Tile + 5) + Tile / 4);

        final int currentRow = row;

        TranslateTransition animation = new TranslateTransition(Duration.seconds(1), disc);
        animation.setToY(row * (Tile + 5) + Tile / 4);
        animation.setOnFinished(e -> {
            if (gameEnd(column, currentRow, redMove)) {
                game(redMove);
                return;
            } else if (playerTurn) {

                
                placeDisc(new Disc(false), AIMinMax.AIMove(),false);
                

            }
            unfreezeConnect4Board();
        });
        animation.play();


    }

    public boolean gameEnd(int column, int row, boolean blueMove) {
        List<Point2D> vertical = IntStream.rangeClosed(row - 3, row + 3)
                .mapToObj(r -> new Point2D(column, r))
                .collect(Collectors.toList());

        List<Point2D> horizontal = IntStream.rangeClosed(column - 3, column + 3)
                .mapToObj(c -> new Point2D(c, row))
                .collect(Collectors.toList());

        Point2D topLeft = new Point2D(column - 3, row - 3);
        List<Point2D> diagonal1 = IntStream.rangeClosed(0, 6)
                .mapToObj(i -> topLeft.add(i, i))
                .collect(Collectors.toList());

        Point2D botLeft = new Point2D(column - 3, row + 3);
        List<Point2D> diagonal2 = IntStream.rangeClosed(0, 6)
                .mapToObj(i -> botLeft.add(i, -i))
                .collect(Collectors.toList());

        return Range(vertical, blueMove) || Range(horizontal, blueMove)
                || Range(diagonal1, blueMove) || Range(diagonal2, blueMove);
    }

    public boolean Range(List<Point2D> points, boolean blueMove) {
        int chain = 0;

        for (Point2D p : points) {
            int column = (int) p.getX();
            int row = (int) p.getY();

            Disc disc = getDisc(column, row).orElse(new Disc(!blueMove));
            if (disc.blue == blueMove) {
                chain++;
                if (chain == 4) {
                    return true;
                }
            } else {
                chain = 0;
            }
        }

        return false;
    }

    public void game(boolean blueMove) {
        String winningMessage = "The Winner is the " + (blueMove ? "Blue" : "Yellow") + " player";
        Message.setText(winningMessage);
        Message.setVisible(true);
        if (blueMove)
            Message.setFill(Color.DARKGREEN);
        else
            Message.setFill(Color.BLACK);
        freezeConnect4Board();
    }

    public Optional<Disc> getDisc(int column, int row) {
        if (column < 0 || column >= COL
                || row < 0 || row >= ROW)
            return Optional.empty();

        return Optional.ofNullable(grid[column][row]);
    }

    private static class Disc extends Circle {
        private final boolean blue;

        public Disc(boolean blue) {
            super(Tile / 2, blue ? Color.BLUE : Color.YELLOW);
            this.blue = blue;

            setCenterX(Tile / 2);
            setCenterY(Tile / 2);
        }

    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setScene(new Scene(createContent()));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
