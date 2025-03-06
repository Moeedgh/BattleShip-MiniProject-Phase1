import java.util.Random;
import java.util.Scanner;

/**
  The BattleShip class manages the gameplay of the Battleship game between two players.
  It includes methods to manage grids, turns, and check the game status.
 */
public class BattleShip {

    // Grid size for the game
    static final int GRID_SIZE = 10;

    // Player 1's main grid containing their ships
    static char[][] player1Grid = new char[GRID_SIZE][GRID_SIZE];

    // Player 2's main grid containing their ships
    static char[][] player2Grid = new char[GRID_SIZE][GRID_SIZE];

    // Player 1's tracking grid to show their hits and misses
    static char[][] player1TrackingGrid = new char[GRID_SIZE][GRID_SIZE];

    // Player 2's tracking grid to show their hits and misses
    static char[][] player2TrackingGrid = new char[GRID_SIZE][GRID_SIZE];

    // Scanner object for user input
    static Scanner scanner = new Scanner(System.in);

    /**
      The main method that runs the game loop.
      It initializes the grids for both players, places ships randomly, and manages turns.
      The game continues until one player's ships are completely sunk.
     */
    public static void main(String[] args) {
        // Initialize grids for both players
        initializeGrid(player1Grid);
        initializeGrid(player2Grid);
        initializeGrid(player1TrackingGrid);
        initializeGrid(player2TrackingGrid);

        // Place ships randomly on each player's grid
        placeShips(player1Grid);
        placeShips(player2Grid);
        // Variable to track whose turn it is
        boolean player1Turn = true;

        // Main game loop, runs until one player's ships are all sunk
        while (!isGameOver()) {
            if (player1Turn) {
                System.out.println("Player 1's turn:");
                printGrid(player1TrackingGrid);
                playerTurn(player2Grid, player1TrackingGrid);
            } else {
                System.out.println("Player 2's turn:");
                printGrid(player2TrackingGrid);
                playerTurn(player1Grid, player2TrackingGrid);
            }
            player1Turn = !player1Turn;
        }

        System.out.println("Game Over!");
    }

    /**
      Initializes the grid by filling it with water ('~').

      @param grid The grid to initialize.
     */
    static void initializeGrid(char[][] grid) {
        for (int i=0;i<GRID_SIZE;i++) {
            for(int j=0;j<GRID_SIZE;j++) {
                grid[i][j]='~';
            }
        }
    }

    /**
      Places ships randomly on the given grid.
      This method is called for both players to place their ships on their respective grids.

      @param grid The grid where ships need to be placed.
     */
    static void placeShips(char[][] grid) {
        int size=5;
        Random rand = new Random();
        while (size>1) {
            int row = rand.nextInt(10);
            int col = rand.nextInt(10);
            boolean horizontal = rand.nextBoolean();
            if(canPlaceShip(grid,row,col,size,horizontal)) {
                if(horizontal) {
                    for(int j=0;j<size;j++) {
                        grid[row][col+j]='X';
                    }
                    size--;
                }
                else {
                    for(int i=0;i<size;i++) {
                        grid[row+i][col]='X';
                    }
                    size--;
                }
            }
        }
    }

    /**
      Checks if a ship can be placed at the specified location on the grid.
      This includes checking the size of the ship, its direction (horizontal or vertical),
      and if there's enough space to place it.

      @param grid The grid where the ship is to be placed.
      @param row The starting row for the ship.
      @param col The starting column for the ship.
      @param size The size of the ship.
      @param horizontal The direction of the ship (horizontal or vertical).
      @return true if the ship can be placed at the specified location, false otherwise.
     */
    static boolean canPlaceShip(char[][] grid, int row, int col, int size, boolean horizontal) {
        if (horizontal) {
            for(int j=0;j<size;j++) {
                if(col+j==10)
                    return false;
                if (grid[row][col+j] == 'X') {
                    return false;
                }
            }
            for(int j=-1;j<size+1;j++) {
                for (int i=-1;i<2;i++){
                    if(i==0 && (j>=0 && j<size)) {
                        continue;
                    }
                    if((row+i>=0 && row+i<10 ) && (col+j>=0 && col+j<10)){
                        if (grid[row+i][col+j] == 'X') {
                            return false;
                        }
                    }
                }
            }
            return true;
        }
        else {
            for(int i=0;i<size;i++) {
                if (row+i==10)
                    return false;
                if(grid[row+i][col]=='X')
                    return false;
            }
            for(int i=-1;i<size+1;i++) {
                for (int j=-1;j<2;j++){
                    if(j==0 && (i>=0 && i<size))
                        continue;
                    if ((row+i>=0 && row+i<10 ) && (col+j>=0 && col+j<10)){
                        if (grid[row+i][col+j] == 'X')
                            return false;
                    }
                }
            }
            return true;
        }
    }

    /**
      Manages a player's turn, allowing them to attack the opponent's grid
      and updates their tracking grid with hits or misses.

      @param opponentGrid The opponent's grid to attack.
      @param trackingGrid The player's tracking grid to update.
     */
    static void playerTurn(char[][] opponentGrid, char[][] trackingGrid) {
        System.out.print("Enter target (for example B6):");
        String target = scanner.next();
        if(isValidInput(target)) {
            int col = target.codePointAt(0)-65;
            int row = target.codePointAt(1)-48;
            if (opponentGrid[row][col] == 'X'){
                trackingGrid[row][col]='X';
                System.out.println("Hit!");
            }
            else {
                trackingGrid[row][col]='O';
                System.out.println("Miss!");
            }
        }
    }

    /**
      Checks if the game is over by verifying if all ships are sunk.

      @return true if the game is over (all ships are sunk), false otherwise.
     */
    static boolean isGameOver() {
        if(allShipsSunk(player1TrackingGrid)){
            System.out.println("all player 2's ships sunk!");
            System.out.println("Player 1 won!");
            return true;
        }
        if(allShipsSunk(player2TrackingGrid)){
            System.out.println("all player 1's ships sunk!");
            System.out.println("Player 2 won!");
            return true;
        }
        return false;
    }

    /**
      Checks if all ships have been destroyed on a given grid.

      @param grid The grid to check for destroyed ships.
      @return true if all ships are sunk, false otherwise.
     */
    static boolean allShipsSunk(char[][] grid) {
        int count=0;
        for (int i=0;i<GRID_SIZE;i++) {
            for (int j=0;j<GRID_SIZE;j++) {
                if (grid[i][j] == 'X') {
                    count++;
                }
            }
        }
        if (count == 14)
            return true;
        else
            return false;
    }

    /**
      Validates if the user input is in the correct format (e.g., A5).

      @param input The input string to validate.
      @return true if the input is in the correct format, false otherwise.
     */
    static boolean isValidInput(String input) {
        if(input.length()==2){
            int word = input.codePointAt(0);
            int num= input.codePointAt(1);
            if((word>=65 && word<=74) && (num>=48 && num<=57))
                return true;
            else{
                System.out.println("Invalid input, try again.");
                return false;
            }
        }
        else{
            System.out.println("Invalid input, try again.");
            return false;
        }
    }

    /**
      Prints the current state of the player's tracking grid.
      This method displays the grid, showing hits, misses, and untried locations.

      @param grid The tracking grid to print.
     */
    static void printGrid(char[][] grid) {
        System.out.println("  A B C D E F G H I J");
        for (int i=0;i<GRID_SIZE;i++) {
            System.out.print(i+" ");
            for (int j=0;j<GRID_SIZE;j++) {
                if (j==9)
                    System.out.println(grid[i][j]);
                else
                    System.out.print(grid[i][j]+" ");
            }
        }
    }
}
