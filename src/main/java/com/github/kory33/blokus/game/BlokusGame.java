package com.github.kory33.blokus.game;

import com.github.kory33.blokus.game.cell.PlayerColor;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * A class representing a Blokus game session.
 */
public class BlokusGame {
    @NotNull private final BlokusBoard board;
    private final BlokusBoardMesh redPlacementMesh;
    private final BlokusBoardMesh bluePlacementMesh;

    @NotNull private BlokusGameStatus currentGameStatus;

    private final IBlokusPlayer redPlayer;
    private final IBlokusPlayer bluePlayer;

    public BlokusGame(IBlokusPlayer red, IBlokusPlayer blue) {
        this.board = new BlokusBoard();
        this.redPlacementMesh = new BlokusBoardMesh(PlayerColor.RED);
        this.bluePlacementMesh = new BlokusBoardMesh(PlayerColor.BLUE);
        this.redPlayer = red;
        this.bluePlayer = blue;

        this.currentGameStatus = BlokusGameStatus.RESUME_RED_PLAYER;
        this.redPlayer.assignColor(PlayerColor.RED);
        this.bluePlayer.assignColor(PlayerColor.BLUE);
    }

    /**
     * Let next player choose a placement and play the chosen placement.
     * Upon calling this method, <code>BlokusGameStatus</code> member is updated,
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
        if (this.currentGameStatus == BlokusGameStatus.RESUME_RED_PLAYER) {
            nextPlayer = this.redPlayer;
            playerBoardMesh = this.redPlacementMesh;
        } else {
            nextPlayer = this.bluePlayer;
            playerBoardMesh = this.bluePlacementMesh;
        }

        Set<BlokusPlacement> possiblePlacements = playerBoardMesh.getPossiblePlacements();
        BlokusPlacement playerPlacement = nextPlayer.chooseBestPlacementFrom(possiblePlacements, this.board);
        this.makePlacement(playerPlacement);
    }

    private void makePlacement(BlokusPlacement placement) {
        PlayerColor placementColor = placement.getPlacementColor();
        if (this.currentGameStatus.getNextPlayerColor() != placementColor) {
            throw new IllegalArgumentException("Illegally-colored placement given!");
        }

        BlokusBoardMesh playerBoardMesh;
        BlokusBoardMesh opponentBoardMesh;
        if (placementColor == PlayerColor.RED) {
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
        if (opponentBoardMesh.getPossiblePlacements().isEmpty()) {
            if (playerBoardMesh.getPossiblePlacements().isEmpty()) {
                this.currentGameStatus = BlokusGameStatus.GAME_FINISH;
            }
            // keep the game status same and exit.
            return;
        }

        // invert the player color
        this.currentGameStatus = BlokusGameStatus.getResumeStatus(placementColor.getOpponentColor());
    }

    @NotNull
    public BlokusBoard getBoard() {
        return board;
    }

    @NotNull
    public BlokusGameStatus getCurrentGameStatus() {
        return currentGameStatus;
    }
}
