package tictactoe.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GameBoardTest {

    @Test
    void shouldCreateEmptyBoard() {
        GameBoard board = new GameBoard();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                assertTrue(board.isCellEmpty(i, j));
            }
        }
    }

    @Test
    void shouldGetAndSetValue() {
        GameBoard board = new GameBoard();

        assertEquals(0, board.getValue(0, 0));
        board.setValue(0, 0, 1);
        assertEquals(1, board.getValue(0, 0));
        assertFalse(board.isCellEmpty(0, 0));
    }

    @Test
    void shouldCreateBoardFromArray() {
        int[][] arr = {
                {1, 0, 0},
                {-1, 1, 0},
                {1,0, -1}
        };

        GameBoard board = new GameBoard(arr);

        assertEquals(1, board.getValue(0, 0));
        assertEquals(-1, board.getValue(1, 0));
        assertEquals(1, board.getValue(1, 1));
    }

    @Test
    void shouldThrowExceptionWhenOutOfBounds() {
        GameBoard board = new GameBoard();

        assertThrows(IllegalStateException.class, () -> board.setValue(3, 0 ,1));
        assertThrows(IllegalStateException.class, () -> board.setValue(0, 4, 1));
        assertThrows(IllegalStateException.class, () -> board.setValue(-1, 0, 0));
    }

    @Test
    void shouldThrowExceptionWhenCellIsOccupied() {
        GameBoard board = new GameBoard();
        board.setValue(0, 0, 1);

        assertThrows(IllegalStateException.class, () -> board.setValue(0,0, 0));
    }

    @Test
    void shouldThrowExceptionForInvalidBoardSize() {
        int[][] invalidArr = {
                {1, 0},
                {0, -1}
        };

        assertThrows(IllegalStateException.class, () -> new GameBoard(invalidArr));
    }

    @Test
    void shouldThrowExceptionWithWrongBoardInConstructor() {
        int[][] board = new int[5][3];
        int[][] board2 = new int[3][0];
        int[][] board3 = new int[3][4];

        assertThrows(IllegalStateException.class, () -> new GameBoard(board));
        assertThrows(IllegalStateException.class, () -> new GameBoard(board2));
        assertThrows(IllegalStateException.class, () -> new GameBoard(board3));
    }
}