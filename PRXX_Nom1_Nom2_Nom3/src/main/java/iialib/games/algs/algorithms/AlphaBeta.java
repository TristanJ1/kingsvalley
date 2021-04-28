package iialib.games.algs.algorithms;

import java.util.ArrayList;

import games.kingsvalley.KVRole;
import iialib.games.algs.GameAlgorithm;
import iialib.games.algs.IHeuristic;
import iialib.games.model.IBoard;
import iialib.games.model.IMove;
import iialib.games.model.IRole;
import iialib.games.model.Player;

public class AlphaBeta<Move extends IMove,Role extends IRole,Board extends IBoard<Move,Role,Board>> implements GameAlgorithm<Move,Role,Board> {

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

	public AlphaBeta(Role playerMaxRole, Role playerMinRole, IHeuristic<Board, Role> h) {
		this.playerMaxRole = playerMaxRole;
		this.playerMinRole = playerMinRole;
		this.h = h;
	}

	public AlphaBeta(Role playerMaxRole, Role playerMinRole, IHeuristic<Board, Role> h, int depthMax) {
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
		int alpha = IHeuristic.MIN_VALUE;
		int beta = IHeuristic.MAX_VALUE;

		ArrayList<Move> allMoves = board.possibleMoves(playerMaxRole);
		System.out.println("    * " + allMoves.size() + " possible moves");
		bestMove = (allMoves.size() == 0 ? null : allMoves.get(0));
		
		for (Move move : allMoves) {  
			newVal = minMax(board.play(move, playerMaxRole), 1,alpha,beta);
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
	private int maxMin(Board board, int depth, int alpha, int beta) {
		if (depth == depthMax || board.isGameOver()) {
			nbLeaves++;
			return h.eval(board, playerMaxRole);
		} else {
			nbNodes++;
			ArrayList<Move> allMoves = board.possibleMoves(playerMaxRole);
			int newVal;
			for (Move move : allMoves) {
				newVal = minMax(board.play(move,playerMaxRole), depth+1,alpha,beta);
				if (newVal == IHeuristic.MIN_VALUE) {
					newVal -= depth;
				}
				
				if (newVal > alpha) alpha = newVal;
				if (alpha >= beta) return beta;
			}
			
			return alpha;
		}
	}

	private int minMax(Board board, int depth, int alpha, int beta) {
		if (depth == depthMax || board.isGameOver()) {
			nbLeaves++;
			return h.eval(board, playerMinRole);
		} else {
			nbNodes++;
			ArrayList<Move> allMoves = board.possibleMoves(playerMinRole);
			int newVal;
			for (Move move : allMoves) {
				newVal =  maxMin(board.play(move,playerMinRole), depth+1,alpha,beta);
				if (newVal == IHeuristic.MIN_VALUE) {
					newVal += depth;
				}
				
				if (newVal < beta) beta = newVal;
				if (alpha >= beta) return alpha;
			}

			return beta;
		}
	}

}