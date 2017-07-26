package com.github.kory33.blokus.game;

import com.github.kory33.blokus.game.cell.PlayerColor;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * A class representing a Blokus game session.
 */
public class BlokusGame {
    @NotNull private final BlokusGameData gameData;
    @NotNull private BlokusGamePhases currentGamePhase;

    private final BlokusBoardMesh redPlacementMesh;
    private final BlokusBoardMesh bluePlacementMesh;

    private final IBlokusPlayer redPlayer;
    private final IBlokusPlayer bluePlayer;

    public BlokusGame(IBlokusPlayer red, IBlokusPlayer blue) {
        this.gameData = new BlokusGameData();

        this.redPlacementMesh = new BlokusBoardMesh(PlayerColor.RED);
        this.bluePlacementMesh = new BlokusBoardMesh(PlayerColor.BLUE);
        this.redPlayer = red;
        this.bluePlayer = blue;

        this.currentGamePhase = BlokusGamePhases.RESUME_RED_PLAYER;
        this.redPlayer.assignColor(PlayerColor.RED);
        this.bluePlayer.assignColor(PlayerColor.BLUE);
    }

    /**
     * Let next player choose a placement and play the chosen placement.
     * Upon calling this method, <code>BlokusGamePhases</code> member is updated,
     * hence a change in return value of {@link #getCurrentGamePhase()}.
     * <p>
     * When the game has been already terminated, this method does not do anything.
     * </p>
     */
    public void playSinglePlacement() {
        if (this.currentGamePhase.isGameFinished()) {
            return;
        }

        IBlokusPlayer nextPlayer;
        BlokusBoardMesh playerBoardMesh;
        if (this.currentGamePhase == BlokusGamePhases.RESUME_RED_PLAYER) {
            nextPlayer = this.redPlayer;
            playerBoardMesh = this.redPlacementMesh;
        } else {
            nextPlayer = this.bluePlayer;
            playerBoardMesh = this.bluePlacementMesh;
        }

        Set<BlokusPlacement> possiblePlacements = playerBoardMesh.getPossiblePlacements(this.gameData);
        BlokusPlacement playerPlacement = nextPlayer.chooseBestPlacementFrom(possiblePlacements, this.gameData);
        this.makePlacement(playerPlacement);
    }

    private void makePlacement(BlokusPlacement placement) {
        PlayerColor placementColor = placement.getPlacementColor();
        if (this.currentGamePhase.getNextPlayerColor() != placementColor) {
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
        this.gameData.updateGameData(placement);

        // update the game status and placement exploration mesh
        if (opponentBoardMesh.getPossiblePlacements(this.gameData).isEmpty()) {
            if (playerBoardMesh.getPossiblePlacements(this.gameData).isEmpty()) {
                this.currentGamePhase = BlokusGamePhases.GAME_FINISH;
            }
            // keep the game status same and exit.
            return;
        }

        // invert the player color
        this.currentGamePhase = BlokusGamePhases.getResumeStatus(placementColor.getOpponentColor());
    }

    @NotNull
    public BlokusGamePhases getCurrentGamePhase() {
        return currentGamePhase;
    }
}
