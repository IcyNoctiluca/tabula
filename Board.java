import java.util.*;
import java.io.*;

public class Board implements BoardInterface, Serializable
{
    private String boardName;
    private List<LocationInterface> boardPlaces;

    public Board(){
        boardPlaces = new ArrayList<LocationInterface>();
        Location start = new Location("Start");
        start.setMixed(true);
        boardPlaces.add(start);
        for(int i = 1; i <= BoardInterface.NUMBER_OF_LOCATIONS; i++){
            Location l = new Location(Integer.toString(i));
            l.setMixed(false);
            boardPlaces.add(l);
        }
        Location finish = new Location("Finish");
        finish.setMixed(true);
        boardPlaces.add(finish);
        Location knock = new Location("Knock");
        knock.setMixed(true);
        boardPlaces.add(knock);

        for(Colour c: Colour.values()){ //
            for(int i = 0; i < BoardInterface.PIECES_PER_PLAYER; i++){  //BoardInterface.PIECES_PER_PLAYER
                try{
                    getStartLocation().addPieceGetKnocked(c);
                }
                catch(Exception e){
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    public void setName(String name){
        this.boardName = name;
    }

    public LocationInterface getStartLocation(){
        return boardPlaces.get(0);
    }

    public LocationInterface getEndLocation(){
        return boardPlaces.get(boardPlaces.size() - 2);
    }

    public LocationInterface getKnockedLocation(){
        return boardPlaces.get(boardPlaces.size() - 1);
    }

    public LocationInterface getBoardLocation(int locationNumber) throws NoSuchLocationException{
        LocationInterface theLocation = null;
        for(LocationInterface l: boardPlaces){
            if((!l.isMixed()) && (l.getName().equals(Integer.toString(locationNumber)))){
                theLocation = l;
            }
        }
        if(theLocation != null){
            return theLocation;
        }
        else{
            throw new NoSuchLocationException("Cannot find location of number: " + locationNumber);
        }
    }

    public boolean canMakeMove(Colour colour, MoveInterface move){
        List<Integer> diceVal = new ArrayList<Integer>();
        diceVal.add(move.getDiceValue());
        for(MoveInterface m: possibleMoves(colour, diceVal)){
            if((m.getDiceValue() == move.getDiceValue()) && (m.getSourceLocation() == move.getSourceLocation())){
                return true;
            }
        }
        return false;
    }

    public void makeMove(Colour colour, MoveInterface move) throws IllegalMoveException{
        if(canMakeMove(colour, move)){
            LocationInterface startLoc;
            LocationInterface endLoc;
            int source = move.getSourceLocation();
            if(getKnockedLocation().numberOfPieces(colour) != 0){
                startLoc = getKnockedLocation();
                startLoc.removePiece(colour);
                }
            else{
                try{
                    startLoc = getBoardLocation(source);
                    startLoc.removePiece(colour);
                }
                catch(Exception e){
                    if(source == 0){
                        startLoc = getStartLocation();
                        startLoc.removePiece(colour);
                    }
                    else{
                        System.out.println(e.getMessage());
                    }
                }
            }
            try{
                endLoc = getBoardLocation(source + move.getDiceValue());
                Colour c = endLoc.addPieceGetKnocked(colour);
                if(c != null){
                    getKnockedLocation().addPieceGetKnocked(c);
                }
            }
            catch(Exception e){
                if(move.getDiceValue() + source > BoardInterface.NUMBER_OF_LOCATIONS){
                    //moved into finish location
                    endLoc = getEndLocation();
                    Colour c = getEndLocation().addPieceGetKnocked(colour);
                }
            }
        }
        else{
            throw new IllegalMoveException("Illegal move there!");
        }
    }

    public void takeTurn(Colour colour, TurnInterface turn, List<Integer> diceValues) throws IllegalTurnException{
        for(MoveInterface m: turn.getMoves()){
            if(!diceValues.contains(m.getDiceValue())){
                throw new IllegalTurnException("Dice values do not marry up :/\n Illegal turn!");
            }
            else{
                try{
                    makeMove(colour, m);
                }
                catch(Exception e){
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    public boolean isWinner(Colour colour){
        if(getEndLocation().numberOfPieces(colour) == BoardInterface.PIECES_PER_PLAYER){
            return true;
        }
        else{
            return false;
        }
    }

    public Colour winner(){
        Colour c = Colour.values()[0];
        if(isWinner(c)){
            return c;
        }
        else if(isWinner(c.otherColour())){
            return c.otherColour();
        }
        else{
            return null;
        }
    }

    public boolean isValid(){
        for(LocationInterface l: boardPlaces){
            if(!l.isValid()){
                return false;
            }
        }
        return true;
    }

    public Set<MoveInterface> possibleMoves(Colour colour, List<Integer> diceValues){
        Set<MoveInterface> setOfMoves;
        if(getKnockedLocation().numberOfPieces(colour) != 0){
            setOfMoves = new HashSet<MoveInterface>();
            for(Integer dieVal: diceValues){
                try{
                    if(getBoardLocation(dieVal).canAddPiece(colour)){
                        Move m = new Move();
                        m.setSourceLocation(0);
                        m.setDiceValue(dieVal);
                        setOfMoves.add(m);
                    }
                }
                catch(Exception e){
                    System.out.println(e.getMessage());
                }
            }
            return setOfMoves;
        }
        else if(getStartLocation().numberOfPieces(colour) != 0){
            setOfMoves = new HashSet<MoveInterface>();
            for(Integer dieVal: diceValues){
                try{
                    if(getBoardLocation(dieVal).canAddPiece(colour)){
                        Move m = new Move();
                        m.setSourceLocation(0);
                        m.setDiceValue(dieVal);
                        setOfMoves.add(m);
                    }
                }
                catch(Exception e){
                    System.out.println(e.getMessage());
                }
            }
            return setOfMoves;
        }
        else{
            setOfMoves = new HashSet<MoveInterface>();
            for(int i = 1; i <= BoardInterface.NUMBER_OF_LOCATIONS; i++){
                try{
                    if(getBoardLocation(i).numberOfPieces(colour) != 0){
                        for(Integer dieVal: diceValues){
                            try{
                                if(getBoardLocation(i + dieVal).canAddPiece(colour)){
                                    Move m = new Move();
                                    m.setSourceLocation(i);
                                    m.setDiceValue(dieVal);
                                    setOfMoves.add(m);
                                }
                            }
                            catch(Exception e){
                                if(i + dieVal > BoardInterface.NUMBER_OF_LOCATIONS){
                                    //moved into finish location
                                    Move m = new Move();
                                    m.setSourceLocation(i);
                                    m.setDiceValue(dieVal);
                                    setOfMoves.add(m);
                                }
                                else{
                                    System.out.println(e.getMessage());
                                }
                            }
                        }
                    }
                }
                catch(Exception e){
                    System.out.println(e.getMessage());
                }
            }
            return setOfMoves;
        }
    }

    public BoardInterface clone(){
        //return this;
        Board cloned;
        try{
            cloned = (Board)super.clone();
            return cloned;
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public String toString(){
        String boardString = "\t" + Colour.values()[0] + "\t" + Colour.values()[1];
        for(LocationInterface l: boardPlaces){
            boardString += "\n" + l.getName() + "\t";
            int numberPiecesG = l.numberOfPieces(Colour.values()[0]);
            int numberPiecesB = l.numberOfPieces(Colour.values()[1]);;
            boardString += (numberPiecesG == 0 ? " " : numberPiecesG) + "\t" + (numberPiecesB == 0 ? " " : numberPiecesB);
        }
        //System.out.println(boardString);
        return boardString;
    }
}
