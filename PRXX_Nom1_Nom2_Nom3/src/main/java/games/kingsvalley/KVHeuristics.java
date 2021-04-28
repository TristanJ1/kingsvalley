package games.kingsvalley;

import iialib.games.algs.IHeuristic;

public class KVHeuristics {
	
	public static IHeuristic<KVBoard,KVRole>  hWhite = (board,role) -> {
		float coeffCentreKingI = (float) 5 + board.nbTurn/10;
		float coeffFreeSpaceKingI = (float) 3 + board.nbTurn/10;
		
		float coeffCentreKingOther = (float) 5;
		float coeffFreeSpaceKingOther = (float) 3;
		float coeffCentreSoldierI = (float) 10/18 + board.nbTurn/10;
		float coeffFreeSpaceSoldierI = (float) 1/3 + board.nbTurn/10; 
		float coeffCentreSoldierOther = (float) 5/18;
		float coeffFreeSpaceSoldierOther = (float) 1/6;  
		float coeffDiag = (float) 3;
		
		float diag = 0;
        int centreI = 0;
        int centreOther = 0;
        int centreSoldierI = 0;
        int centreSoldierOther = 0;
        int nbFreeSpaceI = 0;
        int nbFreeSpaceOther = 0;
        int nbFreeSpaceSdI = 0;
        int nbFreeSpaceSdOther = 0;
        KVPiece center = board.board[3][3];
        int xBK = 0;
		int yBK = 0;
		int xWK = 0;
		int yWK = 0;
		for(int i = 0; i < board.board.length; i++) {
			for(int j = 0; j < board.board[0].length; j++) {
				if(board.board[i][j] == KVPiece.BLUEKING) {
					xBK = i;
					yBK = j;
					centreOther += 6-Math.abs(3-i) - Math.abs(3-j);
					nbFreeSpaceOther += board.nbFreeSpace(i,j);
				}else if(board.board[i][j] == KVPiece.WHITEKING) {
					xWK = i;
					yWK = j;
					centreI += 6-Math.abs(3-i) - Math.abs(3-j);
					nbFreeSpaceI += board.nbFreeSpace(i,j);
				}else if(board.board[i][j] == KVPiece.BLUESOLDIER) {
					if(i>0 && i<6 && j>0 && j<6 && (i==j || i==6-j)) diag--;
					
					
					centreSoldierOther += 6-Math.abs(3-i) - Math.abs(3-j);
					nbFreeSpaceSdOther += board.nbFreeSpace(i,j);
				}else if(board.board[i][j] == KVPiece.WHITESOLDIER) {
					if(i>0 && i<6 && j>0 && j<6 && (i==j || i==6-j)) diag++;
					centreSoldierI += 6-Math.abs(3-i) - Math.abs(3-j);
					nbFreeSpaceSdI += board.nbFreeSpace(i,j);
				}
			}
		}
     
		if(center == KVPiece.WHITEKING || !board.canPieceMove(xBK,yBK)) return IHeuristic.MAX_VALUE;
		if(center == KVPiece.BLUEKING  || !board.canPieceMove(xWK,yWK)) return IHeuristic.MIN_VALUE;
			

		
        return  (int)(coeffCentreKingI*centreI +
        		coeffCentreSoldierI*centreSoldierI +
        		coeffFreeSpaceKingI*nbFreeSpaceI + 
        		coeffFreeSpaceSoldierI*nbFreeSpaceSdI
        		 - 
        		coeffCentreKingOther*centreOther -
        		coeffCentreSoldierOther*centreSoldierOther -
        		coeffFreeSpaceKingOther*nbFreeSpaceOther -
        		coeffFreeSpaceSoldierOther*nbFreeSpaceSdOther + coeffDiag*diag);   
    };
    
	public static IHeuristic<KVBoard,KVRole>  hBlue = (board,role) -> {
		float coeffCentreKingI = (float) 5 + board.nbTurn/10;
		float coeffFreeSpaceKingI = (float) 3 + board.nbTurn/10;
		
		float coeffCentreKingOther = (float) 5;
		float coeffFreeSpaceKingOther = (float) 3 * 20;
		float coeffCentreSoldierI = (float) 10/18 + board.nbTurn/10;
		float coeffFreeSpaceSoldierI = (float) 1/3 + board.nbTurn/10; 
		float coeffCentreSoldierOther = (float) 5/18;
		float coeffFreeSpaceSoldierOther = (float) 1/6;
		float coeffDiag = (float) 3;
		
		float diag = 0;
		

        int centreI = 0;
        int centreOther = 0;
        int centreSoldierI = 0;
        int centreSoldierOther = 0;
        int nbFreeSpaceI = 0;
        int nbFreeSpaceOther = 0;
        int nbFreeSpaceSdI = 0;
        int nbFreeSpaceSdOther = 0;
        KVPiece center = board.board[3][3];
        int xBK = 0;
		int yBK = 0;
		int xWK = 0;
		int yWK = 0;
		
		for(int i = 0; i < board.board.length; i++) {
			for(int j = 0; j < board.board[0].length; j++) {
				if(board.board[i][j] == KVPiece.BLUEKING) {
					xBK = i;
					yBK = j;
					centreI += 6-Math.abs(3-i) - Math.abs(3-j);
					nbFreeSpaceI += board.nbFreeSpace(i,j);
				}else if(board.board[i][j] == KVPiece.WHITEKING) {
					xWK = i;
					yWK = j;
					centreOther += 6-Math.abs(3-i) - Math.abs(3-j);
					nbFreeSpaceOther += board.nbFreeSpace(i,j);
				}else if(board.board[i][j] == KVPiece.BLUESOLDIER) {
					if(i>0 && i<6 && j>0 && j<6 && (i==j || i==6-j)) diag++;
					centreSoldierI += 6-Math.abs(3-i) - Math.abs(3-j);
					nbFreeSpaceSdI += board.nbFreeSpace(i,j);
				}else if(board.board[i][j] == KVPiece.WHITESOLDIER) {
					if(i>0 && i<6 && j>0 && j<6 && (i==j || i==6-j)) diag--;
					centreSoldierOther += 6-Math.abs(3-i) - Math.abs(3-j);
					nbFreeSpaceSdOther += board.nbFreeSpace(i,j);
				}
			}
		}
     
		if(center == KVPiece.WHITEKING || !board.canPieceMove(xBK,yBK)) return IHeuristic.MIN_VALUE;
		if(center == KVPiece.BLUEKING  || !board.canPieceMove(xWK,yWK)) return IHeuristic.MAX_VALUE;
		
        return  (int)(coeffCentreKingI*centreI +
        		coeffCentreSoldierI*centreSoldierI +
        		coeffFreeSpaceKingI*nbFreeSpaceI + 
        		coeffFreeSpaceSoldierI*nbFreeSpaceSdI- 
        		coeffCentreKingOther*centreOther -
        		coeffCentreSoldierOther*centreSoldierOther -
        		coeffFreeSpaceKingOther*nbFreeSpaceOther -
        		coeffFreeSpaceSoldierOther*nbFreeSpaceSdOther + coeffDiag*diag);   
    };
    
}
