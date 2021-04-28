package games.kingsvalley;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import iialib.games.algs.IHeuristic;
import iialib.games.algs.algorithms.*;

import iialib.games.algs.algorithms.MiniMax;
import iialib.games.model.IBoard;
import iialib.games.model.IChallenger;
import iialib.games.model.IMove;
import iialib.games.model.IRole;

public class MyChallenger implements IChallenger {
	
	private KVRole role;
	private KVRole otherRole;
	private KVBoard gameBoard;
	
	public MyChallenger() {
		this.gameBoard = new KVBoard(); 
	}

	@Override
	public String teamName() {
		return "Elliker - Jeromin - Sangare";
	}

	@Override
	public void setRole(String role) {
		if(role.contains("W")) {
			this.role = KVRole.WHITE;
			this.otherRole = KVRole.BLUE;
		}
		else {
			this.role = KVRole.BLUE;
			this.otherRole = KVRole.WHITE;
		}
	}

	@Override
	public void iPlay(String move) {
		this.gameBoard.nbTurn++;
		this.gameBoard = this.gameBoard.play(stringToKVMove(move), this.role);
	}

	@Override
	public void otherPlay(String move) {
		this.gameBoard = this.gameBoard.play(stringToKVMove(move), this.otherRole);
	}

	@Override
	public String bestMove() {
		
		for(KVMove m: this.gameBoard.possibleMoves(this.role)) {
			if(this.isWiningMove(m)) {
				return m.toString();
			}
			if(/*this.gameBoard.isItFirstTurn() */this.gameBoard.nbTurn == 0 && this.role == KVRole.WHITE) {
				return new KVMove(6, 1, 1, 1).toString(); //B7-B2
			}
			if(/*this.gameBoard.isItBlueFirstTurn() */this.gameBoard.nbTurn == 0 && this.role == KVRole.BLUE){
				if(this.gameBoard.isValidMove(new KVMove(0, 5, 5, 5), KVRole.BLUE)) {
					return new KVMove(0, 5, 5, 5).toString();  //F1-F6
				}else {
					return new KVMove(0, 1, 5, 1).toString();   //B1-B6
				}
			}
			
		}
		
		int depth = 4;
		if(this.gameBoard.nbTurn > 3 && this.gameBoard.nbTurn < 30) depth = 5;
		System.out.println("DEPTH: " + depth);
		System.out.println("nbTurn: " + this.gameBoard.nbTurn);
		
		AlphaBeta<KVMove, KVRole, KVBoard> alg;
		if(this.role == KVRole.WHITE) {
			alg = new AlphaBeta<KVMove, KVRole, KVBoard>(this.role,this.otherRole,KVHeuristics.hWhite , depth);
		} else {
			alg = new AlphaBeta<KVMove, KVRole, KVBoard>(this.role,this.otherRole,KVHeuristics.hBlue , depth);
		}
		return alg.bestMove(this.gameBoard, this.role).toString();
	}

	@Override
	public String victory() {
		return "VICTOIRE ^_^";
	}

	@Override
	public String defeat() {
		return "DEFAITE";
	}

	@Override
	public String tie() {
		return " <| EGALITE |>";
	}

	@Override
	public String getBoard() {
		String s = "    A B C D E F G\n" + 
				"  .................\n";
		for(int i = this.gameBoard.board.length-1; i>-1; i--) {
			s = s + String.valueOf(i+1);
			s = s + " : ";
			for(int j = 0; j < this.gameBoard.board[0].length; j++) {
				switch(this.gameBoard.board[i][j]) {
				case EMPTY: if(i == 3 && j == 3) {
								s = s + "+";
							}else {
								s = s + "-";
							}
							break;
				case WHITEKING: s = s + "O";  
								break;
				case BLUEKING: s = s + "X";  
							   break;
				case WHITESOLDIER: s = s + "o"; 
								   break;
				case BLUESOLDIER: s = s + "x"; 
								  break;
				default: s = s + " ";
				}
				s = s + " ";
			}
			s = s + ": ";
			s = s + String.valueOf(i+1);
			s = s + "\n";
		}
		s = s + "  .................\n";
		s = s + "    A B C D E F G";
		return s;
	}

	@Override
	public void setBoardFromFile(String fileName) {
		System.out.println("SETBOARDFROMFILE");
		File file = new File(fileName);
		Scanner sc = null;
		try {
			sc = new Scanner(file);
			sc.nextLine();
			sc.nextLine();
			int i;
			String s;
			for(int j=6;j>-1;j--) {
				i=0;
				s = sc.nextLine();
				for (int k=0;k<s.length();k++) {
					switch(s.charAt(k)) {
						case 'O': this.gameBoard.board[j][i] = KVPiece.WHITEKING;
								  i+=1;
								  break;
						case 'o': this.gameBoard.board[j][i] = KVPiece.WHITESOLDIER;
							      i+=1;
						          continue;
						case 'X': this.gameBoard.board[j][i] = KVPiece.BLUEKING;
					      		  i+=1;
					      		  break;
						case 'x': this.gameBoard.board[j][i] = KVPiece.BLUESOLDIER;
					      		  i+=1;
					      		  break;
						case '-': this.gameBoard.board[j][i] = KVPiece.EMPTY;
					      		  i+=1;
					      		  break;
						case '+': this.gameBoard.board[j][i] = KVPiece.EMPTY;
			      		  		  i+=1;
			      		  		  break;
			      		default: ;
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		sc.close();
	}

	@Override
	public Set<String> possibleMoves() {
		ArrayList<KVMove> moves = this.gameBoard.possibleMoves(this.role);
		Set<String> s = new HashSet<String>();
		for(KVMove move : moves) {
			s.add(move.toString());
		}
		return s;
	}
	
	public KVMove stringToKVMove(String s) {
		char[] c = s.toCharArray();
		int y1 = letterToNumber(c[0]);
		int x1 = Character.getNumericValue(c[1]) - 1;
		int y2 = letterToNumber(c[3]);
		int x2 = Character.getNumericValue(c[4]) - 1;	
		return new KVMove(x1,y1,x2,y2);
	}
	
	public int letterToNumber(char c) {
		switch(c) {
			case 'A': return 0;
			case 'B': return 1;
			case 'C': return 2;
			case 'D': return 3;
			case 'E': return 4;
			case 'F': return 5;
			default : return 6;
		}			
	}
	
	public boolean isWiningMove(KVMove m) {
		KVBoard b = this.gameBoard.play(m, this.role);
		KVPiece center = b.board[3][3];
		if((center == KVPiece.BLUEKING && this.role == KVRole.BLUE) || (center == KVPiece.WHITEKING && this.role == KVRole.WHITE)) return true;
		int xBK = 0;
		int yBK = 0;
		int xWK = 0;
		int yWK = 0;
		for(int i = 0; i < b.board.length; i++) { 
			for(int j = 0; j < b.board[0].length; j++) {
				if(b.board[i][j] == KVPiece.BLUEKING) {
					xBK = i;
					yBK = j;
				}else if(b.board[i][j] == KVPiece.WHITEKING) {
					xWK = i;
					yWK = j;
				}
			}
		}
		if(!b.canPieceMove(xBK,yBK) && this.role == KVRole.WHITE) return true;
		if(!b.canPieceMove(xWK,yWK) && this.role == KVRole.BLUE) return true;
		return false;
	}
	
	/*public static void main (String[] args){
		                                     // test pour getBoard()
		 MyChallenger m = new MyChallenger();
		 m.role = KVRole.BLUE;
		 System.out.println(m.getBoard()); 
		 System.out.println(m.possibleMoves().toString());  
		 
		MyChallenger m = new MyChallenger();  //  test pour setBoardFromFile()
		String file = "plateau.txt";
		m.gameBoard = new KVBoard();
		m.setBoardFromFile(file);
		System.out.println(m.getBoard());  
		
	 } */
	
}
