import java.util.*;

public class HumanConsolePlayer implements PlayerInterface
{
    private Scanner key;

    public HumanConsolePlayer(){
        this.key = new Scanner(System.in);
    }

    public TurnInterface getTurn(Colour colour, BoardInterface board, List<Integer> diceValues) throws PauseException{
        TurnInterface t = new Turn();
        int numberOfMoves = diceValues.size();
        System.out.println("Enter 'p' at any input to pause the game\nYou have " + numberOfMoves + " moves to make");
        for(int i = 0; i < numberOfMoves; i++){
            System.out.println("The possible moves you could make are:");
            for(MoveInterface m: board.possibleMoves(colour, diceValues)){
                System.out.println("Move counter at\t" + (m.getSourceLocation() == 0 ? "Start/Knocked-Off location" : m.getSourceLocation()) + "\tby " + m.getDiceValue() + "  places");
            }
            if(board.possibleMoves(colour, diceValues).size() == 0){
                return t;
            }
            String sourceNoStr;
            int sourceNo = 0;
            System.out.println("Enter source number of the " + (i == 0 ? "first" : "next") + " piece you would like to move (0 for Start/Knocked-Off location):");
            sourceNoStr = key.nextLine();
            if(sourceNoStr.equals("p")){
                throw new PauseException("You have paused the game");
            }
            try{
                sourceNo = Integer.parseInt(sourceNoStr);
            }
            catch(Exception e){
                System.out.println(e.getMessage() + ". Please try again");
                return getTurn(colour, board, diceValues);
            }
            String dieNoStr;
            int dieNo = 0;
            System.out.println("Enter the die number of places by which to move the piece");
            dieNoStr = key.nextLine();
            if(dieNoStr.equals("p")){
                throw new PauseException("You have paused the game");
            }
            try{
                dieNo = Integer.parseInt(dieNoStr);
            }catch(Exception e){
                System.out.println(e.getMessage() + ". Please try again");
                return getTurn(colour, board, diceValues);
            }
            try{
                Move m = new Move();
                m.setSourceLocation(sourceNo);
                m.setDiceValue(dieNo);
                t.addMove(m);
                board.makeMove(colour, m);
                diceValues.remove(diceValues.indexOf(dieNo));
            }
            catch(Exception e){
                System.out.println(e.getMessage() + ". Please try again");
                return getTurn(colour, board, diceValues);
            }
        }
        return t;
    }
}
