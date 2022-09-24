
package Connect_4;

import java.util.*;

public class AIMinMax {
    private static final int Empty = 0;
    private static final int Player1 = 1;
    public static final int Player2 = 2;
    private static final int COL = 7;
    private static final int ROW = 6;
    private static final int DEPTH = 3;
    private static int[][] board;

    public static void board() {
        board = new int[ROW][COL];
        int rows = board.length;
        int cols = board[0].length;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                board[i][j] = Empty;
            }
        }
    }


    public static int getFirstRow(int[][] B, int C) {
        int row = B.length;
        int count = 0;
        for (int i = 0; i < row; i++) {
            if (B[i][C] == Empty)
                break;
            else
                count += 1;
        }
        return count;
    }


    public static List<Integer> getAllCols(int[][] B) {
        List<Integer> colums = new ArrayList<>();
        int row = B.length;
        int col = B[0].length;
        for (int i = 0; i < col; i++) {
            if (B[row - 1][i] == Empty)
                colums.add(i);
        }
        return colums;
    }


    public static boolean Terminal(int[][] B) {
        return winningPosition(B, Player2) || winningPosition(B, Player1) || getAllCols(B).isEmpty();
    }


    public static int[][] dropPiece(int[][] B, int C, int piece) {
        int row = B.length;
        int col = B[0].length;
        int[][] temp = new int[row][col];
        for (int i = 0; i < row; i++) 
        {
            for(int j = 0; j < col; j++)
            {
                temp[i][j] = B[i][j];
            }
        }
        for (int i = 0; i < row; i++)
        {
            if (temp[i][C] == Empty) 
            {
                temp[i][C] = piece;
                break;
            }
        }
        return temp;
    }


    public static boolean winningPosition(int[][] board, int piece) {
        
        for (int i = 0; i < ROW; i++)
        {
            for (int j = 0; j < COL - 3; j++)
            {
                if (board[i][j] == piece && board[i][j + 1] == piece && board[i][j + 2] == piece && board[i][j + 3] == piece)
                    return true;
            }
        }

        
        for (int i = 0; i < ROW - 3; i++)
        {
            for (int j = 0; j < COL; j++)
            {
                if (board[i][j] == piece && board[i + 1][j] == piece && board[i + 2][j] == piece && board[i + 3][j] == piece)
                    return true;
            }
        }

        for (int i = 0; i < ROW - 3; i++)
        {
            for (int j = 0; j < COL - 3; j++)
            {
                if (board[i][j] == piece && board[i + 1][j + 1] == piece && board[i + 2][j + 2] == piece && board[i + 3][j + 3] == piece)
                    return true;
            }
        }

        for (int i = 3; i < ROW; i++)
        {
            for (int j = 0; j < COL - 3; j++)
            {
                if (board[i][j] == piece && board[i - 1][j + 1] == piece && board[i - 2][j + 2] == piece && board[i - 3][j + 3] == piece)
                    return true;
            }
        }
        return false;
    }


    public static double Score(int[][] board) {
        int score = 0;

        
        int center = 0;
        for (int i = 0; i < COL; i++) 
        {
            if (board[0][3] == Player1)
                center++;
        }
        score =score + center * 3;

        
        for (int i = 0; i < ROW; i++) 
        {
            for (int j = 0; j < COL - 3; j++) 
            {
                score =score + Window(board, i, j, Line.Horizontal);
            }
        }

        
        for (int i = 0; i < ROW - 3; i++) 
        {
            for (int j = 0; j < COL; j++) 
            {
                    score =score + Window(board, i, j, Line.Vertical);
            }
        }

        
        for (int i = 0; i < ROW - 3; i++) 
        {
            for (int j = 0; j < COL - 3; j++) 
            {
                score =score + Window(board, i, j, Line.Diagonal1);
                score =score + Window(board, i, j, Line.Diagonal2);
            }
        }
        return score;
    }


    enum Line {
        Horizontal, Vertical, Diagonal1, Diagonal2
    }


    public static int Window(int[][] board, int row, int column, Line line) {
        int winScore = 0;
        int empty = 0;
        int piece = 0;
        int oppPiece = 0;
        if (null != line) switch (line) {
            case Horizontal:
                for (int i = 0; i < 4; i++) 
                {
            switch (board[row][column + i]) 
            {
                case Player1:
                    piece++;
                    break;
                case Player2:
                    oppPiece++;
                    break;
                default:
                    empty++;
                    break;
            }
                }   break;
            case Vertical:
                for (int i = 0; i < 4; i++) 
                {
            switch (board[row + i][column]) 
            {
                case Player1:
                    piece++;
                    break;
                case Player2:
                    oppPiece++;
                    break;
                default:
                    empty++;
                    break;
            }
                }   break;
            case Diagonal1:
                for (int i = 0; i < 4; i++) 
                {
            switch (board[row + i][column + i]) 
            {
                case Player1:
                    piece++;
                    break;
                case Player2:
                    oppPiece++;
                    break;
                default:
                    empty++;
                    break;
            }
                }   break;
            case Diagonal2:
                for (int i = 0; i < 4; i++) 
                {
            switch (board[row + 3 - i][column + i])
            {
                case Player1:
                    piece++;
                    break;
                case Player2:
                    oppPiece++;
                    break;
                default:
                    empty++;
                    break;
            }
                }   break;
            default:
                break;
        }

        if (piece == 4)
            winScore = winScore + 100;
        else if (piece == 3 && empty == 1)
            winScore = winScore + 5;
        else if (piece == 2 && empty == 2)
            winScore = winScore + 2;

        if (oppPiece == 3 && empty == 1)
            winScore = winScore - 4;

        return winScore;
    }


    public static void BoardUpdate(int column, int playerMove) {
        int rows = board.length;
        for (int i = 0; i < rows; i++) 
        {
            if (board[i][column] == Empty) 
            {
                board[i][column] = playerMove;
                break;
            }
        }
    }


    public static int AIMove() {
        double next = MinMax(board, DEPTH, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, true).get(1);
        BoardUpdate((int) next, Player1);
        return (int) next;
    }


    public static List<Double> MinMax(int[][] B, int depth, double a, double b, boolean maximum) {
        List<Double> ret = new ArrayList<>();
        if (Terminal(B)) {
            if (winningPosition(B, Player1))
                ret.add(1000000.0);
            else if (winningPosition(B, Player2))
                ret.add(-1000000.0);
            else
                ret.add(0.0);
        } 
        else if (depth == 0) 
        {
            double x = Score(B);
            ret.add(x);
        } 
        else 
        {
            double value;
            int nextMove = 0;
            List<Integer> columns = getAllCols(B);

            if (maximum) {
                
                value = Double.NEGATIVE_INFINITY;
                for (int c : columns) 
                {
                    int[][] temp = dropPiece(B, c, Player1);
                    double newValue = MinMax(temp, depth - 1, a, b, false).get(0);
                    if (newValue > value) {
                        value = newValue;
                        nextMove = c;
                    }
                    if (value > a)
                        a = value;
                    if (a >= b)
                        break;
                }
            } 
            else 
            {
               
                value = Double.POSITIVE_INFINITY;
                for (int c : columns) 
                {
                    int[][] temp_Board = dropPiece(B, c, Player2);
                    double newValue = MinMax(temp_Board, depth - 1, a, b, true).get(0);
                    if (newValue < value) 
                    {
                        value = newValue;
                        nextMove = c;
                    }
                    if (value < b)
                        b = value;
                    if (a >= b)
                        break;
                }
            }
            ret.add(value);
            ret.add((double) nextMove);
        }
        return ret;
    }


    public static void main(String[] args) {
        board();
			
    }

}
