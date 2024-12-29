package com.chess.engine.board;

import com.chess.engine.Alliance;
import com.chess.engine.pieces.*;

public class ChessBoardFileParser {
        public static Board createBoardFromFile(String fileContent) {
                final Board.Builder builder = new Board.Builder();

                // Split the content by spaces
                String[] pieces = fileContent.trim().split(" ");

                for (String piece : pieces) {
                        if (piece.isEmpty()) {
                                // Skip empty strings
                                continue;
                        }

                        if (piece.equals("empty")) {
                                // Skip the keyword "empty"
                                continue;
                        }

                        try {
                                // If the piece is a number, it's likely an empty tile ID (e.g., "12")
                                if (piece.matches("\\d+")) {
                                        // Skip standalone numbers (empty tile placeholders)
                                        continue;
                                }

                                // First character is piece type (R, N, B, Q, K, P)
                                char pieceType = piece.charAt(0);

                                // Second character is alliance (W, B)
                                char alliance = piece.charAt(1);

                                // Remaining substring is the tile ID
                                int tileId = Integer.parseInt(piece.substring(2));

                                // Create the appropriate piece
                                Piece newPiece = null;
                                Alliance pieceAlliance = (alliance == 'W') ? Alliance.WHITE : Alliance.BLACK;

                                switch (pieceType) {
                                        case 'R': newPiece = new Rook(tileId, pieceAlliance); break;
                                        case 'N': newPiece = new Knight(tileId, pieceAlliance); break;
                                        case 'B': newPiece = new Bishop(tileId, pieceAlliance); break;
                                        case 'Q': newPiece = new Queen(tileId, pieceAlliance); break;
                                        case 'K': newPiece = new King(tileId, pieceAlliance, true, true); break;
                                        case 'P': newPiece = new Pawn(tileId, pieceAlliance); break;
                                        default:
                                                System.err.printf("Unknown piece type: %s at %s%n", pieceType, tileId);
                                }

                                if (newPiece != null) {
                                        builder.setPiece(newPiece);
                                }
                        } catch (NumberFormatException e) {
                                System.err.printf("Invalid tile ID in piece definition: '%s'%n", piece);
                        } catch (StringIndexOutOfBoundsException e) {
                                System.err.printf("Malformed piece entry: '%s'%n", piece);
                        }
                }

                // Set the move maker to white by default
                builder.setMoveMaker(Alliance.WHITE);

                return builder.build();
        }


}