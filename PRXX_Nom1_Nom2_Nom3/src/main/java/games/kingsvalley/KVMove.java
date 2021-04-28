package games.kingsvalley;

import iialib.games.model.IMove;

public class KVMove implements IMove {
	public final int x1;
	public final int y1;
    public final int x2;
    public final int y2;
    
    KVMove(int x1, int y1, int x2, int y2){
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    @Override
    public String toString() {
    	return numberToLetter(y1) + String.valueOf(x1+1) + "-" + numberToLetter(y2) + String.valueOf(x2+1);
    }
    
    public char numberToLetter(int x) {
		switch(x) {
			case 0: return 'A';
			case 1: return 'B';
			case 2: return 'C';
			case 3: return 'D';
			case 4: return 'E';
			case 5: return 'F';
			default : return 'G';
		}	
	}

}
