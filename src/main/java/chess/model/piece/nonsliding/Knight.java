package chess.model.piece.nonsliding;

import static chess.model.position.Direction.NORTH_EAST_EAST;
import static chess.model.position.Direction.NORTH_NORTH_EAST;
import static chess.model.position.Direction.NORTH_NORTH_WEST;
import static chess.model.position.Direction.NORTH_WEST_WEST;
import static chess.model.position.Direction.SOUTH_EAST_EAST;
import static chess.model.position.Direction.SOUTH_SOUTH_EAST;
import static chess.model.position.Direction.SOUTH_SOUTH_WEST;
import static chess.model.position.Direction.SOUTH_WEST_WEST;

import chess.model.Color;
import chess.model.piece.PieceType;
import chess.model.position.Direction;
import chess.model.position.Distance;
import java.util.Set;

public class Knight extends NonSlidingPiece {

    private static final Set<Direction> directions = Set.of(
            NORTH_NORTH_EAST, NORTH_NORTH_WEST, SOUTH_SOUTH_EAST, SOUTH_SOUTH_WEST,
            NORTH_WEST_WEST, NORTH_EAST_EAST, SOUTH_WEST_WEST, SOUTH_EAST_EAST
    );
    private static final double SCORE = 2.5;

    public Knight(final Color color) {
        super(color, PieceType.KNIGHT);
    }

    @Override
    protected boolean isRightDistance(final Distance distance) {
        return true;
    }

    protected boolean isRightDirection(final Direction direction) {
        return directions.stream()
                .anyMatch(it -> it.match(direction.rank(), direction.file()));
    }

    @Override
    public boolean isKing() {
        return false;
    }

    @Override
    public double getScore() {
        return SCORE;
    }
}
