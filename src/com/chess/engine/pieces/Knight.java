package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


//TODO Taj Creates the Class and provides structure and functionality

public class Knight extends Piece {
      //XYZ CANDIDATE_MOVE_COORDINATES to KNIGHT_MOVE_OFFSETS
    private final static int[] KNIGHT_MOVE_OFFSETS = {-17, -15, -10, -6, 6, 10, 15, 17};
    public Knight(final int piecePosition, final Alliance pieceAlliance) {
        super(PieceType.KNIGHT, piecePosition, pieceAlliance); //Omer added a new parameter (piecetype)


    }

    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {


        final List<Move> legalMoves = new ArrayList<>();

        for (final int currentCandidateOffset : KNIGHT_MOVE_OFFSETS) {
           //XYZ moveDestination TO  moveDestination

            final int moveDestination = this.piecePosition + currentCandidateOffset;
            if (BoardUtils.isValidTileCoordinate(moveDestination)){
                if (  isFirstColumnExclusion  (this.piecePosition, currentCandidateOffset)
                        || isSecondColumnExclusion  (this.piecePosition, currentCandidateOffset)
                        || isSeventhColumnExclusion (this.piecePosition, currentCandidateOffset)
                        || isEighthColumnExclusion  (this.piecePosition, currentCandidateOffset)   ) {
                    continue;
                }

                final Tile candidateDestinationTile = board.getTile(moveDestination);
                if(!candidateDestinationTile.isTileFilled()){
                    legalMoves.add(new Move.MajorMove(board, this, moveDestination));

                } else {
                    final Piece pieceAtDestination = candidateDestinationTile.getPiece();
                    final Alliance pieceAlliance = pieceAtDestination.getPieceAlliance();
                    if (this.pieceAlliance != pieceAlliance) {
                        legalMoves.add(new Move.AttackMove(board, this, moveDestination, pieceAtDestination));
                    }
                }
            }
        }
        return ImmutableList.copyOf(legalMoves);
    }
    // Added Rawan
    @Override
    public Piece movePiece(Move move) {
        return new Knight( move.getTargetPosition(), move.getMovedPiece().getPieceAlliance());
    } // End Add

    private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset) {
        return
                BoardUtils.FIRST_COLUMN[currentPosition]
                        &&
                        (  candidateOffset == -17
                                || candidateOffset == -10
                                || candidateOffset ==   6
                                || candidateOffset ==   15)  ;

    }
    private static boolean isEighthColumnExclusion(final int currentPosition, final int candidateOffset) {
        return
                BoardUtils.EIGHTH_COLUMN[currentPosition]
                        &&
                        (  candidateOffset == -15
                                || candidateOffset == -6
                                || candidateOffset ==   10
                                || candidateOffset ==   17  );

    }

    private static boolean isSecondColumnExclusion(final int currentPosition, final int candidateOffset) {
        return
                BoardUtils.SECOND_COLUMN[currentPosition]
                        &&
                        (   (candidateOffset == -10 )
                                || (candidateOffset ==   6)   );
    }
    private static boolean isSeventhColumnExclusion(final int currentPosition, final int candidateOffset) {
        return
                BoardUtils.SEVENTH_COLUMN[currentPosition]
                        &&
                        (   (candidateOffset == -6 )
                                || (candidateOffset ==   10)   );
    }

    // Added Ahmed
    @Override
    public String toString(){
        return PieceType.KNIGHT.toString();
    } // End Add
}
