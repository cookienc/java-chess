package chess.controller;

import chess.dao.MoveDao;
import chess.model.Scores;
import chess.model.board.state.GameState;
import chess.model.board.state.ProgressState;
import chess.view.InputView;
import chess.view.OutputView;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ChessController {

    private final InputView inputView;
    private final OutputView outputView;

    public ChessController(final InputView inputView, final OutputView outputView) {
        this.inputView = inputView;
        this.outputView = outputView;
    }

    public void start(final MoveDao moveDao) {
        inputView.printGameStartMessage();
        doGame(moveDao);
    }

    private void doGame(final MoveDao moveDao) {
        GameState state = retry(()  -> getGameState(moveDao));
        retry(this::progressGame, state);
    }

    private GameState getGameState(final MoveDao moveDao) {
        final GameCommand gameCommand = retry(inputView::readGameCommand);
        return getGameState(gameCommand, moveDao);
    }

    private GameState getGameState(final GameCommand gameCommand, final MoveDao moveDao) {
        GameState state = ProgressState.of(gameCommand, moveDao);
        printCreateGameMessage(state);
        printBoardStatus(state);
        return state;
    }

    private void printCreateGameMessage(final GameState state) {
        outputView.printIfHasGame(state.hasGame());
    }

    private <T> T retry(final Supplier<T> supplier) {
        while (true) {
            try {
                return supplier.get();
            } catch (IllegalArgumentException e) {
                outputView.printErrorMessage(e.getMessage());
            }
        }
    }

    private void progressGame(GameState state) {
        while (state.isNotEnd()) {
            final MoveRequest moveRequest = readGameCommand();

            state = applyCommandAndExecute(state, moveRequest);

            printGameProgressMessage(state.isGameEnd());
        }
    }

    private MoveRequest readGameCommand() {
        return inputView.readPlayCommand();
    }

    private static GameState applyCommandAndExecute(GameState state, final MoveRequest moveRequest) {
        state = state.changeState(moveRequest.getGameCommand());
        state.executeAndSave(moveRequest.getSource(), moveRequest.getTarget());
        return state;
    }

    private void printGameProgressMessage(final GameState state) {
        printScores(state);
        printBoardStatus(state);
    }

    private void printScores(final GameState state) {
        if (state.isStatus()) {
            final Scores scores = state.calculateScores();
            outputView.printScores(ScoreResponses.from(scores));
        }
    }

    private void printBoardStatus(final GameState state) {
        if (state.isNotEnd()) {
            outputView.printCurrentPlayer(state.findCurrentPlayer());
            outputView.printChessBoard(new BoardResponse(state.getBoard()));
        }
    }

    private <T> void retry(final Consumer<T> consumer, T input) {
        while (true) {
            try {
                consumer.accept(input);
                return;
            } catch (IllegalArgumentException e) {
                outputView.printErrorMessage(e.getMessage());
            }
        }
    }
}
