package iialib.games.algs.algorithms;

import java.util.ArrayList;

import games.kingsvalley.KVRole;
import iialib.games.algs.GameAlgorithm;
import iialib.games.algs.IHeuristic;
import iialib.games.model.IBoard;
import iialib.games.model.IMove;
import iialib.games.model.IRole;
import iialib.games.model.Player;

public class MiniMax<Move extends IMove,Role extends IRole,Board extends IBoard<Move,Role,Board>> implements GameAlgorithm<Move,Role,Board> {

	// Constants
	private final static int DEPTH_MAX_DEFAULT = 8;

	// Attributes
	private final Role playerMaxRole;
	private final Role playerMinRole;
	private int depthMax = DEPTH_MAX_DEFAULT;
	private IHeuristic<Board, Role> h;
	private int nbNodes;
	private int nbLeaves;

	// --------- Constructors ---------

	public MiniMax(Role playerMaxRole, Role playerMinRole, IHeuristic<Board, Role> h) {
		this.playerMaxRole = playerMaxRole;
		this.playerMinRole = playerMinRole;
		this.h = h;
	}

	public MiniMax(Role playerMaxRole, Role playerMinRole, IHeuristic<Board, Role> h, int depthMax) {
		this(playerMaxRole, playerMinRole, h);
		this.depthMax = depthMax;
	}

	/*
	 * IAlgo METHODS =============
	 */

	@Override
	public Move bestMove(Board board, Role playerRole) {
		System.out.println("[MiniMax]");
		System.out.println("playerMaxRole: " + playerMaxRole);
		System.out.println("playerMinRole: " + playerMinRole);
		System.out.println("playerRole: " + playerRole);

		this.nbNodes = 1; // root node
		this.nbLeaves = 0;
		int max = IHeuristic.MIN_VALUE;
		int newVal;
		Move bestMove;

		// Compute all possible moves for playerMaxRole
		ArrayList<Move> allMoves = board.possibleMoves(playerMaxRole);
		System.out.println("    * " + allMoves.size() + " possible moves");
		bestMove = (allMoves.size() == 0 ? null : allMoves.get(0));
		
		for (Move move : allMoves) {  
			
			newVal = minMax(board.play(move, playerMaxRole), 1);
			
			if (newVal > max) {
				max = newVal;
				bestMove = move;
			}
		}
		
		System.out.println("    * " + nbNodes + " nodes explored");
		System.out.println("    * " + nbLeaves + " leaves evaluated");
		System.out.println("Best value is: " + max);
		
		return bestMove;
	}

	/*
	 * PUBLIC METHODS ==============
	 */

	public String toString() {
		return "MiniMax(ProfMax=" + depthMax + ")";
	}

	/*
	 * PRIVATE METHODS ===============
	 */
	private int maxMin(Board board, int depth) {
		if (depth == depthMax || board.isGameOver()) {
			nbLeaves++;
			// Evaluate board with h while playerMaxRole is about to play
			return h.eval(board, playerMaxRole);
		} else {
			nbNodes++;
			int max = IHeuristic.MIN_VALUE;
			// Compute all possible moves for playerMaxRole
			ArrayList<Move> allMoves = board.possibleMoves(playerMaxRole);
			int newVal;
			
			for (Move move : allMoves) {
				newVal = minMax(board.play(move,playerMaxRole), depth+1);
				if (newVal == IHeuristic.MIN_VALUE) {
					// It is considered that playerMax will favor
					// the least deep case of playerMax-victory
					newVal -= depth;
				}
				
				max = Math.max(max, newVal);
			}
			
			return max;
		}
	}

	private int minMax(Board board, int depth) {
		if (depth == depthMax || board.isGameOver()) {
			nbLeaves++;
			// Evaluate board with h while playerMinRole is about to play
			return h.eval(board, playerMinRole);
		} else {
			nbNodes++;
			int min = IHeuristic.MAX_VALUE;
			// Compute all possible moves for playerMinRole
			ArrayList<Move> allMoves = board.possibleMoves(playerMinRole);
			int newVal;
			
			for (Move move : allMoves) {
				newVal =  maxMin(board.play(move,playerMinRole), depth+1);
				if (newVal == IHeuristic.MIN_VALUE) {
					// It is considered that playerMin will favor
					// the least deep case of playerMax-defeat
					newVal += depth;
				}
				
				min = Math.min(min, newVal);
			}

			return min;
		}
	}

}
