import java.util.*;
import java.io.*;

public class Location implements LocationInterface, Serializable
{
    private String locationName;
    private boolean canHasMixed;
    private List<Colour> currentPieces;

    public Location(String name){
        this.locationName = name;
        currentPieces = new ArrayList<Colour>();
    }

    public String getName(){
        return locationName;
    }

    public void setName(String name){
        locationName = name;
    }

    public boolean isMixed(){
        return canHasMixed;
    }

    public void setMixed(boolean isMixed){
        canHasMixed = isMixed;
    }

    public boolean isEmpty(){
        if(currentPieces.size() == 0){
            return true;
        }
        else{
            return false;
        }
    }

    public int numberOfPieces(Colour colour){
        int count = 0;
        for(Colour c: currentPieces){
            if (c.equals(colour)){
                count++;
            }
        }
        return count;
    }

    public boolean canAddPiece(Colour colour){
        if(isMixed()){
            return true;
        }
        else if((isEmpty()) || (numberOfPieces(colour.otherColour()) == 1) || (numberOfPieces(colour) > 0)){
            return true;
        }
        else{
            return false;
        }
    }

    public Colour addPieceGetKnocked(Colour colour) throws IllegalMoveException{
        if(!canAddPiece(colour)){
            throw new IllegalMoveException("You cannot move a piece here");
        }
        else{
            currentPieces.add(colour);
            if((numberOfPieces(colour.otherColour()) == 1) && (!isMixed())){
                removePiece(colour.otherColour());
                return colour.otherColour();
            }
            else{
                return null;
            }
        }
    }

    public boolean canRemovePiece(Colour colour){
        if(isMixed()){
            return true;
        }
        else if(numberOfPieces(colour) == 1){
            return true;
        }
        else{
            return false;
        }
    }

    public void removePiece(Colour colour) throws IllegalMoveException{
        //if(canRemovePiece(colour)){
        //    currentPieces.remove(currentPieces.indexOf(colour));
        //}
        //else{throw new IllegalMoveException("Piece can't be removed");}
        try{
            currentPieces.remove(currentPieces.indexOf(colour));
        }
        catch(Exception e){
            throw new IllegalMoveException("Piece can't be removed");
        }
    }

    public boolean isValid(){
        if(canHasMixed){
            return true;
        }
        else{
            Colour cG = Colour.values()[0];
            int noGreen = numberOfPieces(cG);
            Colour cB = cG.otherColour();
            int noBlue = numberOfPieces(cB);
            if(((noBlue != 0) && (noGreen == 0)) || ((noBlue == 0) && (noGreen != 0))){
                return true;
            }
            else{
                return false;
            }
        }
    }
}
