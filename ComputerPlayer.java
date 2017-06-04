import java.util.*;

public class ComputerPlayer implements PlayerInterface
{
    private Random generator;

    public ComputerPlayer(){
        this.generator = new Random();
    }

    public TurnInterface getTurn(Colour colour, BoardInterface board, List<Integer> diceValues) throws PauseException{
        TurnInterface t = new Turn();
        int numberOfMoves = diceValues.size();
        for(int i = 0; i < numberOfMoves; i++){
            List<MoveInterface> possMoves = new ArrayList<MoveInterface>();
            for(MoveInterface m: board.possibleMoves(colour, diceValues)){
                possMoves.add(m);
            }
            if(possMoves.size() == 0){
                return t;
            }
            try{
                int rand = generator.nextInt(possMoves.size());
                t.addMove(possMoves.get(rand));
                System.out.println(colour + "'s move was from " + possMoves.get(rand).getSourceLocation() + " by " + possMoves.get(rand).getDiceValue() + " places");
                board.makeMove(colour, possMoves.get(rand));
                diceValues.remove(diceValues.indexOf(possMoves.get(rand).getDiceValue()));
            }
            catch(Exception e){
                System.out.println(e.getMessage());
            }
        }
        return t;
    }
}
