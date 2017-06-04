import java.util.*;
import java.io.*;

public class Game implements GameInterface
{
    private Map<Colour,PlayerInterface> playerColour;
    private Board board;
    private Dice dice;
    private int whosMove;

    public Game()
    {
        this.playerColour = new HashMap<>();
        this.board = new Board();
        this.dice = new Dice();
        this.whosMove = 0;
        playerColour.put(Colour.values()[0], new HumanConsolePlayer());
        playerColour.put(Colour.values()[1], new ComputerPlayer());
    }

    public static void main(String[] args){
        System.out.println("Welcome to Liam's Big Tabula Game!\nFrom here you can:");
        Game g = new Game();
        while(true){
            System.out.print("1 -set the players (human or computer), defaulting to one human and one computer player\n2 -load a game from file\n3 -continue a game that has been paused or loaded\n4 -save the game state to a file\n5 -start a new game\n6 -exit the program\n");
            Scanner key = new Scanner(System.in);
            String menuNoStr = key.nextLine();
            int menuNo = 0;
            try{
                menuNo = Integer.parseInt(menuNoStr);
            }
            catch(Exception e){
                System.out.println(e.getMessage() + "\nPlease input a number");
            }
            if(menuNo == 1){
                g.playerColour = new HashMap<>();
                int colour = 0;
                System.out.println("There are two players");
                boolean didFail = false;
                do{
                    System.out.println("To set " + Colour.values()[colour] + " player to human, enter 1\nTo set to a computer, enter 2");
                    String playerSetStr = key.nextLine();
                    int playerToSet = 0;
                    try{
                        playerToSet = Integer.parseInt(playerSetStr);
                        if(playerToSet == 1){
                            g.setPlayer(Colour.values()[colour], new HumanConsolePlayer());
                        }
                        else if(playerToSet == 2){
                            g.setPlayer(Colour.values()[colour], new ComputerPlayer());
                        }
                        if(colour == 0){
                            colour = 1;
                        }
                        else if(colour == 1){
                            colour = 0;
                        }
                        didFail = false;
                    }
                    catch(Exception e){
                        didFail = true;
                        System.out.println(e.getMessage());
                    }
                }
                while((g.playerColour.size() != 2) || (didFail));
            }
            if(menuNo == 2){
                System.out.println("To load a game, enter the name of a saved game:");
                String filename = key.nextLine();
                try{
                    g.loadGame(filename);
                }
                catch(Exception e){
                    System.out.println(e.getMessage());
                }
            }
            if(menuNo == 3){
                try{
                    Colour winner = g.play();
                    if(winner != null){
                        System.out.println(winner + " is the winner!\nCongratulations " + winner + "!");
                    }
                }
                catch(PlayerNotDefinedException e){
                    System.out.println(e.getMessage());
                }
            }
            if(menuNo == 4){
                System.out.println("Enter a name for the game to be saved as:");
                String filename = key.nextLine();
                try{
                    g.saveGame(filename);
                }
                catch(Exception e){
                    System.out.println(e.getMessage());
                }
            }
            if(menuNo == 5){
                g.board = new Board();
                try{
                    Colour winner = g.play();
                    if(winner != null){
                        System.out.println(winner + " is the winner!\nCongratulations " + winner + "!");
                    }
                }
                catch(PlayerNotDefinedException e){
                    System.out.println(e.getMessage());
                }
            }
            if(menuNo == 6){
                return;
            }
            else{}
        }
    }

    public void setPlayer(Colour colour, PlayerInterface player){
        playerColour.put(colour, player);
    }

    public Colour getCurrentPlayer(){
        return Colour.values()[whosMove];
    }

    public Colour play() throws PlayerNotDefinedException{
        if(playerColour.size() != 2){
            throw new PlayerNotDefinedException("There are not two players set");
        }
        do{
            System.out.println(board.toString());
            dice.clear();
            dice.roll();
            PlayerInterface p;
            Colour c = Colour.values()[0];
            TurnInterface turnsToMake = new Turn();
            Set s = playerColour.entrySet();
            Iterator i = s.iterator();
            while(i.hasNext()){
                Map.Entry m = (Map.Entry) i.next();
                c = (Colour) m.getKey();
                if(getCurrentPlayer().equals(c)){
                    p = (PlayerInterface) m.getValue();
                    try{               
                        System.out.println("It is " + c + "'s go to take a turn");
                        System.out.print("Dice values are:\t");
                        for(int dieVal: dice.getValues()){
                            System.out.print(dieVal + "\t");
                        }
                        System.out.print("\n");
                        turnsToMake = p.getTurn(c, board.clone(), dice.getValues());
                    }
                    catch(Exception e){
                        //game should be paused
                        System.out.println(e);
                        return null;
                    }
                }
                else{
                    c = c.otherColour();
                }
            }
            try{
                board.takeTurn(c, turnsToMake, dice.getValues());
            }
            catch(Exception e){
                System.out.println(e.getMessage());
                if(board.winner() != null){
                    return board.winner();
                }
            }
            if(whosMove == 0){
                whosMove = 1;
            }
            else if(whosMove == 1){
                whosMove = 0;
            }
        }
        while(board.winner() == null);
        return board.winner();
    }

    public void saveGame(String filename) throws IOException{
        try{
            FileOutputStream fileOut = new FileOutputStream(filename + ".ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(board);
            out.close();
            fileOut.close();
        }
        catch(Exception e){
            throw new IOException(e.getMessage());
        }
    }

    public void loadGame(String filename) throws IOException{
        Board boardLoaded;
        try{
            FileInputStream fileIn = new FileInputStream(filename + ".ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            boardLoaded = (Board) in.readObject();
            in.close();
            fileIn.close();
            this.board = boardLoaded;
        }
        catch(Exception e){
            throw new IOException(e.getMessage());
        }
    }
}
