package games.kingsvalley;

import java.util.ArrayList;

import iialib.games.model.IBoard;
import iialib.games.model.Score;

public class KVBoard implements IBoard<KVMove, KVRole, KVBoard> {
	
	public KVPiece[][] board;
	public int nbTurn = 0;
	
	public KVBoard() {
		KVPiece[][] b = new KVPiece[7][7];
		for(int i = 0; i< 7;i++) {
			if(i == 3) {
				b[0][i] = KVPiece.WHITEKING;
				b[6][i] = KVPiece.BLUEKING;
			}else {
				b[0][i] = KVPiece.BLUESOLDIER;
				b[6][i] = KVPiece.WHITESOLDIER;
			}
		}
		for(int i = 1; i < 6; i++) {
			for(int j = 0; j < 7; j++) {
				b[i][j] = KVPiece.EMPTY;
			}
		}
		this.board = b;		
	}
	
	public KVBoard(KVBoard kvB) {
		KVPiece[][] b = new KVPiece[7][7];
		for(int i = 0; i < 7; i++) {
			for(int j = 0; j < 7; j++) {
				b[i][j] = kvB.board[i][j];
			}
		}
		this.board = b;
		this.nbTurn = kvB.nbTurn;
	}
	
	public void printKVBoard() {
		for(int i = 0; i < 7; i++) {
			for(int j = 0; j < 7; j++) {
				switch(this.board[i][j]) {
					case BLUEKING:
						System.out.print("X");
						break;
					case WHITEKING:
						System.out.print("O");
						break;
					case BLUESOLDIER:
						System.out.print("x");
						break;
					case WHITESOLDIER:
						System.out.print("o");
						break;
					case EMPTY:{
						if(j==3 && i == 3) System.out.print("+");
						else System.out.print("-");
						break;
					}
				}
					
			}
			System.out.println();
		}
	}

	@Override
	public ArrayList<KVMove> possibleMoves(KVRole playerRole) {
		ArrayList<KVMove> ret = new ArrayList<KVMove>();
		KVPiece sKing;
		KVPiece sSoldier;
		if(playerRole == KVRole.WHITE) {
			sKing = KVPiece.WHITEKING;
			sSoldier = KVPiece.WHITESOLDIER;
		}else {
			sKing = KVPiece.BLUEKING;
			sSoldier = KVPiece.BLUESOLDIER;
		}
		for(int i = 0; i < this.board.length; i++) {
			for(int j = 0; j < this.board[0].length; j++) {
				KVPiece cibledStartingTile = this.board[i][j];
				if(cibledStartingTile == sKing || cibledStartingTile == sSoldier) { //on cible une case avec notre soldat/roi
					for(int k = 0; k < this.board.length; k++) {
						for(int l = 0; l < this.board[0].length; l++) {
							if(this.isValidMove(new KVMove(i,j,k,l), playerRole)) {
								ret.add(new KVMove(i,j,k,l));
							}
						}
					}
				}
			}
		}
		return ret;
	}

	@Override
	public KVBoard play(KVMove move, KVRole playerRole) {
		if(!this.isValidMove(move, playerRole)) {
			System.out.println("Played an invalid move");
			return null;
		}   
		KVBoard b = new KVBoard(this);
		KVPiece startTarget = b.board[move.x1][move.y1];
		b.board[move.x1][move.y1] = KVPiece.EMPTY;
		b.board[move.x2][move.y2] = startTarget;	
		return b;
	}

	@Override
	public boolean isValidMove(KVMove move, KVRole playerRole) {
		if(move.x1 == move.x2 && move.y1 == move.y2) return false; //on exclue le cas o� on ne bouge pas
		KVPiece sKing;
		KVPiece sSoldier;
		if(playerRole == KVRole.WHITE) {
			sKing = KVPiece.WHITEKING;
			sSoldier = KVPiece.WHITESOLDIER;
		}else {
			sKing = KVPiece.BLUEKING;
			sSoldier = KVPiece.BLUESOLDIER;
		}
		KVPiece cibledStartingTile = this.board[move.x1][move.y1];
		
		if(cibledStartingTile == sSoldier && move.x2 == 3 && move.y2 == 3) return false; //un soldat ne peut terminer au centre
		
		if(cibledStartingTile == sKing ) {
			if(this.isItFirstTurn() == true) return false;
		}
		
		if(cibledStartingTile == sKing || cibledStartingTile == sSoldier) { //on v�rifie que la case de d�part a une de nos pi�ces
			if(move.x1 == move.x2) { //m�me ligne
				if(move.y1 > move.y2) { //on va vers la gauche
					for(int i = move.y1-1; i >= move.y2; i--) {
						if(this.board[move.x1][i] != KVPiece.EMPTY) return false;
					}
					if(move.y2 == 0) {
						return true;
					}else{
						if(this.board[move.x2][move.y2 - 1] != KVPiece.EMPTY) {
							return true;
						}else {
							return false;
						}
					}
				}else { //on va vers la droite
					for(int i = move.y1+1; i <= move.y2; i++) {
						if(this.board[move.x1][i] != KVPiece.EMPTY) return false;
					}
					if(move.y2 == 6) {
						return true;
					}else{
						if(this.board[move.x2][move.y2 + 1] != KVPiece.EMPTY) {
							return true;
						}else {
							return false;
						}
					}
				}
			}else if(move.y1 == move.y2) { //meme colonne
				if(move.x1 > move.x2) { //on va vers le haut
					for(int i = move.x1-1; i >= move.x2; i--) {
						if(this.board[i][move.y1] != KVPiece.EMPTY) return false;
					}
					if(move.x2 == 0) {
						return true;
					}else{
						if(this.board[move.x2-1][move.y2] != KVPiece.EMPTY) {
							return true;
						}else {
							return false;
						}
					}
				}else { //on va vers le bas
					for(int i = move.x1+1; i <= move.x2; i++) {
						if(this.board[i][move.y1] != KVPiece.EMPTY) return false;
					}
					if(move.x2 == 6) {
						return true;
					}else{
						if(this.board[move.x2+1][move.y2] != KVPiece.EMPTY) {
							return true;
						}else {
							return false;
						}
					}
				}
			}else if(move.x1 - move.x2 == move.y1 - move.y2) { //m�me diagonale haut gauche -> bas droite
				if(move.x1 > move.x2) { //on monte
					for(int i = 1; i <= move.x1-move.x2; i++) {
						if(this.board[move.x1 - i][move.y1 - i] != KVPiece.EMPTY) return false;
					}
					if(move.y2 == 0 || move.x2 == 0) {
						return true;
					}else{
						if(this.board[move.x2-1][move.y2 - 1] != KVPiece.EMPTY) {
							return true;
						}else {
							return false;
						}
					}
				}else { //on descent
					for(int i = 1; i <= move.x2-move.x1; i++) {
						if(this.board[move.x1 + i][move.y1 + i] != KVPiece.EMPTY) return false;
					}
					if(move.y2 == 6 || move.x2 == 6) {
						return true;
					}else{
						if(this.board[move.x2+1][move.y2 + 1] != KVPiece.EMPTY) {
							return true;
						}else {
							return false;
						}
					}
				}
			}else if(move.x1 - move.x2 == move.y2 - move.y1) { //m�me diagonale bas gauche -> haut droite
				if(move.x1 > move.x2) { //on monte
					for(int i = 1; i <= move.x1-move.x2; i++) {
						if(this.board[move.x1 - i][move.y1 + i] != KVPiece.EMPTY) return false;
					}
					if(move.y2 == 6 || move.x2 == 0) {
						return true;
					}else{
						if(this.board[move.x2-1][move.y2 + 1] != KVPiece.EMPTY) {
							return true;
						}else {
							return false;
						}
					}
				}else { //on descent
					for(int i = 1; i <= move.x2-move.x1; i++) {
						if(this.board[move.x1 + i][move.y1 - i] != KVPiece.EMPTY) return false;
					}
					if(move.y2 == 0 || move.x2 == 6) {
						return true;
					}else{
						if(this.board[move.x2+1][move.y2 - 1] != KVPiece.EMPTY) {
							return true;
						}else {
							return false;
						}
					}
				}
			}else { //ni align� ni en diagonale
				return false;
			}
		}
		return false; // case de d�part pas notre pi�ce
	}
	
	public boolean isItFirstTurn() {
		for(int i = 0; i < this.board[0].length; i++) {
			if(i == 3 && (this.board[0][i] != KVPiece.WHITEKING || this.board[6][i] != KVPiece.BLUEKING)) return false;
			else if(i != 3 && (this.board[0][i] != KVPiece.BLUESOLDIER || this.board[6][i] != KVPiece.WHITESOLDIER)) return false;
		}
		return true;
	}
	
	public boolean isItBlueFirstTurn() {
		for(int i = 0; i < this.board[0].length; i++) {
			if(i == 3 && this.board[6][i] != KVPiece.BLUEKING) return false;
			else if(i != 3 && (this.board[0][i] != KVPiece.BLUESOLDIER)) return false;
		}
		return true;
	}

	@Override
	public boolean isGameOver() {
		KVPiece center = this.board[3][3];
		if(center == KVPiece.BLUEKING || center == KVPiece.WHITEKING) return true; //on v�rifie si un roi n'est pas au centre
		int xBK = 0;
		int yBK = 0;
		int xWK = 0;
		int yWK = 0;
		for(int i = 0; i < this.board.length; i++) { //on cherche les positions des rois
			for(int j = 0; j < this.board[0].length; j++) {
				if(this.board[i][j] == KVPiece.BLUEKING) {
					xBK = i;
					yBK = j;
				}else if(this.board[i][j] == KVPiece.WHITEKING) {
					xWK = i;
					yWK = j;
				}
			}
		}
		if(!this.canPieceMove(xBK,yBK)) return true;
		if(!this.canPieceMove(xWK,yWK)) return true;
		return false;
	}
	
	public boolean canPieceMove(int x, int y) {
		if(x == 0) {
			if(y == 0) { //corner haut gauche
				if(this.board[0][1] != KVPiece.EMPTY && this.board[1][1] != KVPiece.EMPTY && this.board[1][0] != KVPiece.EMPTY) return false;
			}else if(y == 6) { //corner haut droit
				if(this.board[0][5] != KVPiece.EMPTY && this.board[1][5] != KVPiece.EMPTY && this.board[1][6] != KVPiece.EMPTY) return false;
			}else {//bord haut
				if(this.board[0][y-1] != KVPiece.EMPTY && this.board[1][y-1] != KVPiece.EMPTY && this.board[1][y] != KVPiece.EMPTY &&
						this.board[1][y+1] != KVPiece.EMPTY && this.board[0][y+1] != KVPiece.EMPTY) return false;
			}
		}else if(x == 6) {
			if(y == 0) { //corner bas gauche
				if(this.board[5][0] != KVPiece.EMPTY && this.board[5][1] != KVPiece.EMPTY && this.board[6][1] != KVPiece.EMPTY) return false;
			}else if(y == 6) { //corner bas droit
				if(this.board[6][5] != KVPiece.EMPTY && this.board[5][5] != KVPiece.EMPTY && this.board[5][6] != KVPiece.EMPTY) return false;
			}else { //bord bas
				if(this.board[6][y-1] != KVPiece.EMPTY && this.board[5][y-1] != KVPiece.EMPTY && this.board[5][y] != KVPiece.EMPTY &&
						this.board[5][y+1] != KVPiece.EMPTY && this.board[6][y+1] != KVPiece.EMPTY) return false;
			}
		}else if(y == 0) { //bord gauche
			if(this.board[x-1][0] != KVPiece.EMPTY && this.board[x-1][1] != KVPiece.EMPTY && this.board[x][1] != KVPiece.EMPTY &&
					this.board[x+1][1] != KVPiece.EMPTY && this.board[x+1][0] != KVPiece.EMPTY) return false;
		}else if(y == 6) { //bord droit
			if(this.board[x-1][6] != KVPiece.EMPTY && this.board[x-1][5] != KVPiece.EMPTY && this.board[x][5] != KVPiece.EMPTY &&
					this.board[x+1][5] != KVPiece.EMPTY && this.board[x+1][6] != KVPiece.EMPTY) return false;
		}else { //pas sur une extr�mit�
			if(this.board[x-1][y-1] != KVPiece.EMPTY && this.board[x-1][y] != KVPiece.EMPTY && this.board[x-1][y+1] != KVPiece.EMPTY &&
					this.board[x][y+1] != KVPiece.EMPTY && this.board[x+1][y+1] != KVPiece.EMPTY && this.board[x+1][y] != KVPiece.EMPTY &&
					this.board[x+1][y-1] != KVPiece.EMPTY && this.board[x][y-1] != KVPiece.EMPTY) return false;
		}
		return true; //peut bouger
	}
	
	public int nbFreeSpace(int x, int y) {
		int res = 0;
		if(x == 0) {
			if(y == 0) { //corner haut gauche
				if(this.board[0][1] == KVPiece.EMPTY) res++; if(this.board[1][1] == KVPiece.EMPTY) res++; if(this.board[1][0] == KVPiece.EMPTY) res++;
				return res;
			}else if(y == 6) { //corner haut droit
				if(this.board[0][5] == KVPiece.EMPTY) res++; if(this.board[1][5] == KVPiece.EMPTY) res++; if(this.board[1][6] == KVPiece.EMPTY) res++;
				return res; 
			}else {//bord haut
				if(this.board[0][y-1] == KVPiece.EMPTY) res++; if(this.board[1][y-1] == KVPiece.EMPTY) res++; if(this.board[1][y] == KVPiece.EMPTY) res++; if(
						this.board[1][y+1] == KVPiece.EMPTY) res++; if(this.board[0][y+1] == KVPiece.EMPTY)res++; return res;
			}
		}else if(x == 6) {
			if(y == 0) { //corner bas gauche
				if(this.board[5][0] == KVPiece.EMPTY) res++; if(this.board[5][1] == KVPiece.EMPTY) res++; if(this.board[6][1] == KVPiece.EMPTY) res++; return res;
			}else if(y == 6) { //corner bas droit
				if(this.board[6][5] == KVPiece.EMPTY) res++; if(this.board[5][5] == KVPiece.EMPTY) res++; if(this.board[5][6] == KVPiece.EMPTY) res++; return res;
			}else { //bord bas
				if(this.board[6][y-1] == KVPiece.EMPTY) res++; if(this.board[5][y-1] == KVPiece.EMPTY) res++; if(this.board[5][y] == KVPiece.EMPTY) res++; if(
						this.board[5][y+1] == KVPiece.EMPTY) res++; if(this.board[6][y+1] == KVPiece.EMPTY) res++; return res;
			}
		}else if(y == 0) { //bord gauche
			if(this.board[x-1][0] == KVPiece.EMPTY) res++; if(this.board[x-1][1] == KVPiece.EMPTY) res++; if(this.board[x][1] == KVPiece.EMPTY) res++; if(
					this.board[x+1][1] == KVPiece.EMPTY) res++; if(this.board[x+1][0] == KVPiece.EMPTY) res++; return res;
		}else if(y == 6) { //bord droit
			if(this.board[x-1][6] == KVPiece.EMPTY) res++; if(this.board[x-1][5] == KVPiece.EMPTY) res++; if(this.board[x][5] == KVPiece.EMPTY) res++; if(
					this.board[x+1][5] == KVPiece.EMPTY) res++; if(this.board[x+1][6] == KVPiece.EMPTY) res++; return res;
		}else { //pas sur une extr�mit�
			if(this.board[x-1][y-1] == KVPiece.EMPTY) res++; if(this.board[x-1][y] == KVPiece.EMPTY) res++; if(this.board[x-1][y+1] == KVPiece.EMPTY) res++; if(
					this.board[x][y+1] == KVPiece.EMPTY) res++; if(this.board[x+1][y+1] == KVPiece.EMPTY) res++; if(this.board[x+1][y] == KVPiece.EMPTY) res++; if(
					this.board[x+1][y-1] == KVPiece.EMPTY) res++; if(this.board[x][y-1] == KVPiece.EMPTY) res++; return res;
		}
	}

	@Override
	public ArrayList<Score<KVRole>> getScores() {
        ArrayList<Score<KVRole>> scores = new ArrayList<Score<KVRole>>();
        int xBK = 0;
		int yBK = 0;
		int xWK = 0;
		int yWK = 0;
		for(int i = 0; i < 7; i++) {
			for(int j = 0; j < 7; j++) {
				if(this.board[i][j] == KVPiece.BLUEKING) {
					xBK = i;
					yBK = j;
				}else if(this.board[i][j] == KVPiece.WHITEKING) {
					xWK = i;
					yWK = j;
				}
			}
		}
		if(xBK == 3 && yBK == 3) {
			scores.add(new Score<KVRole>(KVRole.WHITE,Score.Status.LOOSE,0));
            scores.add(new Score<KVRole>(KVRole.BLUE,Score.Status.WIN,1));
		}else if(xWK == 3 && yWK == 3){
			scores.add(new Score<KVRole>(KVRole.WHITE,Score.Status.LOOSE,1));
            scores.add(new Score<KVRole>(KVRole.BLUE,Score.Status.WIN,0));
		}else if(canPieceMove(xBK, yBK) == false) {
			scores.add(new Score<KVRole>(KVRole.WHITE,Score.Status.LOOSE,1));
            scores.add(new Score<KVRole>(KVRole.BLUE,Score.Status.WIN,0));
		}else if(canPieceMove(xWK, yWK) == false) {
			scores.add(new Score<KVRole>(KVRole.WHITE,Score.Status.LOOSE,0));
            scores.add(new Score<KVRole>(KVRole.BLUE,Score.Status.WIN,1));
		}else {
			System.out.println("Game is not over");
			scores.add(new Score<KVRole>(KVRole.WHITE,Score.Status.LOOSE,0));
            scores.add(new Score<KVRole>(KVRole.BLUE,Score.Status.WIN,0));
		}
		return scores;
    }
	
	 public static void main (String[] args){
		 KVBoard b = new KVBoard();
		 b.printKVBoard();
		 System.out.println(b.possibleMoves(KVRole.BLUE).size());
		 ArrayList<KVMove> mva = b.possibleMoves(KVRole.BLUE);
		 for(int i = 0; i < mva.size(); i++) {
			 System.out.println(mva.get(i).toString());
		 }
		 KVMove m = new KVMove(0,0,5,0);
		 b = b.play(m, KVRole.BLUE);
		 b.printKVBoard();
		 System.out.println(b.possibleMoves(KVRole.WHITE).size());
		 ArrayList<KVMove> mvs = b.possibleMoves(KVRole.WHITE);
		 for(int i = 0; i < mvs.size(); i++) {
			 System.out.println(mvs.get(i).toString());
		 }
		 KVMove m2 = new KVMove(6,3,3,0);
		 b = b.play(m2, KVRole.WHITE);
		 b.printKVBoard();
		 System.out.println(b.possibleMoves(KVRole.BLUE).size());
			
	 }

}
