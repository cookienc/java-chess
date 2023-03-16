package chess.model.move;

import static chess.model.board.PositionFixture.F3;
import static chess.model.board.PositionFixture.G5;
import static chess.model.board.PositionFixture.H1;
import static chess.model.board.PositionFixture.H2;
import static chess.model.board.PositionFixture.H3;
import static chess.model.board.PositionFixture.H4;
import static chess.model.board.PositionFixture.H5;
import static chess.model.piece.PieceColor.WHITE;
import static chess.model.piece.PieceType.ROOK;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import chess.model.board.Board;
import chess.model.board.PawnBoard;
import chess.model.board.Square;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BoardRookMoveTest {

    private Board board;

    @BeforeEach
    void init() {
        board = Board.create();
    }

    @Test
    @DisplayName("이동 중에 기물이 존재하는 칸을 만나면 예외가 발생한다.")
    void move_givenInvalidSourceAndTarget_thenFail() {
        // when, then
        assertThatThrownBy(() -> board.move(H1, H3, WHITE))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 경로로 이동할 수 없습니다");
    }

    @Test
    @DisplayName("룩이 움직일 수 있는 방향으로 움직이면 성공한다.")
    void move_whenRookValidTarget_thenSuccess() {
        // given
        final Board board = PawnBoard.create();

        // when
        board.move(H1, H2, WHITE);

        // then
        final List<Square> squares = board.getSquares();
        final int sourceIndex = H1.convertToIndex();
        final int targetIndex = H2.convertToIndex();

        assertAll(
                () -> assertThat(squares.get(targetIndex).isEmpty()).isFalse(),
                () -> assertThat(squares.get(sourceIndex).isEmpty()).isTrue(),
                () -> assertThat(squares.get(targetIndex).pick().getType()).isEqualTo(ROOK)
        );
    }

    @Test
    @DisplayName("룩이 움직일 수 없는 방향으로 움직이면 예외가 발생한다.")
    void rook_givenInvalidDirection_thenFail() {
        // given
        final Board board = PawnBoard.create();

        // when, then
        assertThatThrownBy(() -> board.move(H1, F3, WHITE))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 기물은 지정한 방향으로 움직일 수 없습니다.");
    }

    @Test
    @DisplayName("룩의 도착지가 적 기물이 있는 칸이라면 성공한다.")
    void move_whenRookValidEnemyTarget_thenSuccess() {
        // given
        final Board board = PawnBoard.create();
        board.move(H4, G5, WHITE);

        // when
        board.move(H1, H5, WHITE);

        // then
        final List<Square> squares = board.getSquares();
        final int sourceIndex = H1.convertToIndex();
        final int targetIndex = H5.convertToIndex();

        assertAll(
                () -> assertThat(squares.get(targetIndex).isEmpty()).isFalse(),
                () -> assertThat(squares.get(sourceIndex).isEmpty()).isTrue(),
                () -> assertThat(squares.get(targetIndex).pick().getType()).isEqualTo(ROOK)
        );
    }

    @Test
    @DisplayName("룩의 도착지가 자신의 기물이 있는 칸이라면 예외가 발생한다.")
    void move_whenRookInValidTarget_thenFail() {
        // when
        assertThatThrownBy(() -> board.move(H1, H2, WHITE))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 경로로 이동할 수 없습니다");
    }
}
