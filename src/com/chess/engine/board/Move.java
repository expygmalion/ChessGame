package com.chess.engine.board;
import com.chess.engine.pieces.Pawn;
import com.chess.engine.pieces.Piece;

import static com.chess.engine.board.Board.*;


public abstract class Move {
    final Board board;
    final Piece movedPiece;
    final int destinationCoordinate;
    public static final Move NULL_MOVE = new NullMove();

    public Move(final Board board, final Piece movedPiece, final int destinationCoordinate) {
        this.board = board;
        this.movedPiece = movedPiece;
        this.destinationCoordinate = destinationCoordinate;
    }

    @Override
    public int hashCode(){
        final int prime = 31;
        int result = 1;
        result = prime * result + this.destinationCoordinate;
        result = prime * result + this.movedPiece.hashCode();
        return result;
    }

    @Override
    public boolean equals(final Object other) {
        if(this == other){
            return true;
        }
        if(!(other instanceof Move)){
            return false;
        }
        final Move otherMove = (Move) other;
        return getDestinationCoordinate() == otherMove.getDestinationCoordinate()
                && getMovedPiece() == otherMove.getMovedPiece();
    }

    public int getDestinationCoordinate() {
        return destinationCoordinate;
    }

    public Piece getMovedPiece() {
        return this.movedPiece;
    }
    public boolean isAttack (){
        return true;
    }
    public boolean isCastlingMove(){
        return false;
    }
    public Piece getAttackedPiece(){
        return null;
    }



    //Added Rawan
    // Message_Taj: To improve redundancy, I pulled this upwards.
    public Board execute() {
        final Builder builder = new Builder();
        for (final Piece piece : this.board.currentPlayer().getActivePieces()) {

            //TODO HASHCODES
            if (!this.movedPiece.equals(piece)) {
                builder.setPiece(piece);
            }
        }
        for (final Piece piece : this.board.currentPlayer().getopponent().getActivePieces()) {
            builder.setPiece(piece);
        }
        builder.setPiece(this.movedPiece.movePiece(this));
        builder.setMoveMaker(this.board.currentPlayer().getopponent().getAlliance()); // Why the error?
        return builder.build();
    }

    public static final class MajorMove extends Move {

        public MajorMove(final Board board, final Piece movedPiece,
                         final int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }


    }
    public static class AttackMove extends Move {
        final Piece attackedPiece;

        public AttackMove(final Board board, final Piece movedPiece,
                          final int destinationCoordinate, final Piece attackedPiece) {
            super(board, movedPiece, destinationCoordinate);
            this.attackedPiece = attackedPiece;
        }
        // Added Ola
        @Override
        public Board execute() {
            return null;
        }
        @Override
        public Piece getAttackedPiece() {
            return this.attackedPiece;
        }
        @Override
        public boolean isAttack(){
            return true;
        }
        @Override
        public int hashCode(){
            return this.attackedPiece.hashCode() + super.hashCode();
        }
        @Override
        public boolean equals(final Object other) {
            if(this == other){
                return true;
            }
            if (!(other instanceof  AttackMove)) {
                return false;
            }
            final AttackMove otherAttackMove = (AttackMove) other;
            return super.equals(otherAttackMove) && getAttackedPiece().equals(otherAttackMove.getAttackedPiece());
        }
        // End Add
    }


    // Added Rawan
    public static final class PawnMove extends Move {

        public PawnMove(final Board board, final Piece movedPiece,
                        final int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }
    }

    public static class PawnAttackMove extends AttackMove {

        public PawnAttackMove(final Board board, final Piece movedPiece,
                              final int destinationCoordinate, Piece attackedPiece) {
            super(board, movedPiece, destinationCoordinate, attackedPiece);
        }
    } // End Add

    // Added Mishkat
    public static final class PawnEnPassantAttackMove extends PawnAttackMove {

        public PawnEnPassantAttackMove(final Board board,
                                       final Piece movedPiece,
                                       final int destinationCoordinate,
                                       Piece attackedPiece) {
            super(board, movedPiece, destinationCoordinate, attackedPiece);
        }
    }// End Add

    // Added Mishkat
    public static final class PawnJump extends Move {

        public PawnJump(final Board board, final Piece movedPiece,
                        final int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }
        // Added Ola
        @Override
        public Board execute() {
            final Builder builder = new Builder();
            for (final Piece piece : this.board.currentPlayer().getActivePieces()) {
                if (!this.movedPiece.equals(piece)) {
                    builder.setPiece(piece);
                }
            }
            for (final Piece piece : this.board.currentPlayer().getopponent().getActivePieces()) {
                builder.setPiece(piece);
            }
            final Pawn movedPawn = (Pawn) this.movedPiece.movePiece(this);
            builder.setPiece(movedPawn);
            builder.setEnPassantPawn(movedPawn);
            builder.setMoveMaker(this.board.currentPlayer().getopponent().getAlliance());
            return builder.build();
        } // End Add
    } // End Add

    // Added Taj
    static abstract class CastleMove extends Move {

        public CastleMove(final Board board, final Piece movedPiece,
                          final int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }
    } // End Add

    // Added Taj
    public static final class KingSideCastleMove extends CastleMove {

        public KingSideCastleMove(final Board board, final Piece movedPiece,
                                  final int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }
    } // End Add

    // Added Rawan
    public static final class QueenSideCastleMove extends CastleMove {

        public QueenSideCastleMove(final Board board, final Piece movedPiece,
                                   final int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }
    } // End Add

    // Added Rawan
    public static final class NullMove extends Move {

        public NullMove() {
            super(null, null, -1);
            }
        @Override
        public Board execute() {
            throw new RuntimeException("Cannot Instantiate, Null Move Not Made");
        } // End Rawan
    }

    //TODO these extended move classes will help us print into PGN format


    // Added Rawan
    public static class MoveFactory {

        private MoveFactory() {
            throw new RuntimeException("Not instantiatable!");
        }
        public static Move createMove(final Board board,
                                      final int currentCoordinate,
                                      final int destinationCoordinate) {
            for (final Move move : board.getAllLegalMoves()) {
                if (move.getCurrentCoordinate() == currentCoordinate &&
                        move.getDestinationCoordinate() == destinationCoordinate) {
                    return move;
                }
            }
            return NULL_MOVE;
        }
    } // End Add
    // Added Rawan
    private int getCurrentCoordinate() {
        return this.getMovedPiece().getPiecePosition();
    } // End Add
}

