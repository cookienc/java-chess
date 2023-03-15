package chess.model.piece;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import chess.model.position.Distance;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class PawnTest {

    @ParameterizedTest(name = "Pawn이 {0} 팀일 때 ({1}, {2})만큼 이동하려고 하면 {3}을 반환한다")
    @CsvSource({
            "BLACK,0,-1,true", "BLACK,0,-2,true", "BLACK,0,1,false", "BLACK,0,2,false",
            "BLACK,1,0,false", "BLACK,2,0,false",
            "WHITE,0,-1,false", "WHITE,0,-2,false", "WHITE,0,1,true", "WHITE,0,2,true",
            "WHITE,1,0,false", "WHITE,2,0,false"
    })
    void movable_givenDistance_thenReturnIfMovable(
            final PieceColor pieceColor,
            final int rank,
            final int file,
            final boolean result
    ) {
        final Pawn pawn = new Pawn(pieceColor);
        final Distance distance = new Distance(rank, file);

        final boolean movable = pawn.movable(distance);

        assertThat(movable).isEqualTo(result);
    }
}
