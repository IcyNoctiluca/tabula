import java.util.*;

public class Die implements DieInterface
{
    private static Random generator;
    private int dieFaceValue;
    private boolean didItRoll;

    public Die(){
        this.didItRoll = false;
        this.generator = new Random();
    }

    public boolean hasRolled(){
        return didItRoll;
    }

    public void roll(){
        dieFaceValue = generator.nextInt(DieInterface.NUMBER_OF_SIDES_ON_DIE) + 1;
        didItRoll = true;
    }

    public int getValue() throws NotRolledYetException{
        if(hasRolled()){
            return dieFaceValue;
        }
        else{
            throw new NotRolledYetException("Die hasn't been rolled");
        }
    }

    public void setValue(int value){
        if(0 < value && value < 7){
            dieFaceValue = value;
            didItRoll = true;
        }
        else{
            didItRoll = false;
        }
    }

    public void clear(){
        didItRoll = false;
        dieFaceValue = 0;
        generator = new Random();
    }

    public void setSeed(long seed){
        generator = new Random(seed);
    }
}
