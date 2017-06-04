import java.util.*;

public class Move implements MoveInterface
{
    private int locNumber;
    private int diceVal;

    public Move(){
        this.locNumber = 0;
        this.diceVal = 0;
    }

    public void setSourceLocation(int locationNumber) throws NoSuchLocationException{
        if(0 <= locationNumber && locationNumber <= 24){
            this.locNumber = locationNumber;
        }
        else{
            throw new NoSuchLocationException("There is no such location as " + locationNumber);
        }
    }

    public int getSourceLocation(){
        return locNumber;
    }

    public void setDiceValue(int diceValue) throws IllegalMoveException{
        if(0 <= diceValue && diceValue <= 6){
            this.diceVal = diceValue;
        }
        else{
            throw new IllegalMoveException("Invalid dice value");
        }
    }

    public int getDiceValue(){
        return diceVal;
    }
}
