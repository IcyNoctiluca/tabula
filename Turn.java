import java.util.*;

public class Turn implements TurnInterface
{
    private List<MoveInterface> movesList;

    public Turn(){
        this.movesList = new ArrayList<MoveInterface>();
    }

    public void addMove(MoveInterface move) throws IllegalTurnException{
        if(movesList.size() >= 4){
            throw new IllegalTurnException("There are already 4 moves in the turn");
        }
        else{
            movesList.add(move);
        }
    }

    public List<MoveInterface> getMoves(){
        return movesList;
    }
}
