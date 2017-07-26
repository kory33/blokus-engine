package com.github.kory33.blokus.game;

import com.github.kory33.blokus.game.cell.PlayerColor;

import java.util.Set;

/**
 * A class representing a Blokus game session.
 */
public class BlokusGame {
    private final BlokusBoard board;
    private final BlokusBoardMesh redPlacementMesh;
    private final BlokusBoardMesh bluePlacementMesh;

    private GameStatus currentGameStatus;

    private final IBlokusPlayer redPlayer;
    private final IBlokusPlayer bluePlayer;

    public BlokusGame(IBlokusPlayer red, IBlokusPlayer blue) {
        this.board = new BlokusBoard();
        this.redPlacementMesh = new BlokusBoardMesh(PlayerColor.RED);
        this.bluePlacementMesh = new BlokusBoardMesh(PlayerColor.BLUE);
        this.redPlayer = red;
        this.bluePlayer = blue;

        this.currentGameStatus = GameStatus.RESUME_RED_PLAYER;
        this.redPlayer.assignColor(PlayerColor.RED);
        this.bluePlayer.assignColor(PlayerColor.BLUE);
    }

    /**
     * Let next player choose a placement and play the chosen placement.
     * Upon calling this method, <code>GameStatus</code> member is updated,
     * hence a change in return value of {@link #getCurrentGameStatus()}.
     * <p>
     * When the game has been already terminated, this method does not do anything.
     * </p>
     */
    public void playSinglePlacement() {
        if (this.currentGameStatus.isGameFinished()) {
            return;
        }

        IBlokusPlayer nextPlayer;
        Set<BlokusPlacement> nextPossiblePlacement;
        BlokusBoardMesh playerBoardMesh;
        if (this.currentGameStatus == GameStatus.RESUME_RED_PLAYER) {
            nextPlayer = this.redPlayer;
            playerBoardMesh = this.redPlacementMesh;
        } else {
            nextPlayer = this.bluePlayer;
            playerBoardMesh = this.bluePlacementMesh;
        }

        this.makePlacement(nextPlayer.chooseBestPlacementFrom(playerBoardMesh.getPossiblePlacements(), this.board));
    }

    private void makePlacement(BlokusPlacement placement) {
        if (this.currentGameStatus.getNextPlayerColor() != placement.getPlacementColor()) {
            throw new IllegalArgumentException("Illegally-colored placement given!");
        }

        BlokusBoardMesh playerBoardMesh;
        BlokusBoardMesh opponentBoardMesh;
        if (placement.getPlacementColor() == PlayerColor.RED) {
            playerBoardMesh = this.redPlacementMesh;
            opponentBoardMesh = this.bluePlacementMesh;
        } else {
            playerBoardMesh = this.bluePlacementMesh;
            opponentBoardMesh = this.redPlacementMesh;
        }

        // update board and mesh status with the placement
        playerBoardMesh.makePlayerPlacement(placement);
        opponentBoardMesh.makeOpponentPlacement(placement);
        this.board.updateBoard(placement);

        // update the game status and placement exploration mesh
        // TODO generify code and reduce duplicate code
        if (placement.getPlacementColor() == PlayerColor.RED) {
            if (this.bluePlacementMesh.getPossiblePlacements().isEmpty()) {
                if (this.redPlacementMesh.getPossiblePlacements().isEmpty()) {
                    this.currentGameStatus = GameStatus.GAME_FINISH;
                }
                // keep the game status on RESUME_RED_PLAYER and exit.
                return;
            }
            this.currentGameStatus = GameStatus.RESUME_BLUE_PLAYER;
        } else {
           if (this.redPlacementMesh.getPossiblePlacements().isEmpty()) {
                if (this.bluePlacementMesh.getPossiblePlacements().isEmpty()) {
                    this.currentGameStatus = GameStatus.GAME_FINISH;
                }
                // keep the game status on RESUME_BLUE_PLAYER and exit.
                return;
            }
            this.currentGameStatus = GameStatus.RESUME_RED_PLAYER;
        }
    }

    public BlokusBoard getBoard() {
        return board;
    }

    public GameStatus getCurrentGameStatus() {
        return currentGameStatus;
    }
}
