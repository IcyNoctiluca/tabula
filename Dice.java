import java.util.*;

public class Dice implements DiceInterface
{
    private List<DieInterface> pair;
    private List<Integer> values;

    public Dice(){
        values = new ArrayList<Integer>();
        pair = new ArrayList<DieInterface>();
        DieInterface die1 = new Die();
        DieInterface die2 = new Die();
        pair.add(die1);
        pair.add(die2);
    }

    public void clear(){
        for(DieInterface die: pair){
            die.clear();
        }
        values = new ArrayList<Integer>();
    }

    public List<DieInterface> getDice(){
        return pair;
    }

    public List<Integer> getValues() throws NotRolledYetException{
        if(haveRolled()){
          return values;
        }
        else{
          throw new NotRolledYetException("Dice haven't been rolled yet");
        }
    }

    public boolean haveRolled(){
      if((pair.get(0).hasRolled()) && (pair.get(1).hasRolled())){
        return true;
      }
      else{
        return false;
      }
    }

    public void roll(){
      for(DieInterface die: pair){
        die.roll();
        try{
          values.add(die.getValue());
        }
        catch(Exception e){
            System.out.println(e);
        }
      }
      if(values.get(0) == values.get(1)){
        values.add(values.get(0));
        values.add(values.get(0));
      }
    }

}
