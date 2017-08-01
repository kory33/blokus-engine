package com.github.kory33.blokus.game;

import com.github.kory33.blokus.game.color.PlayerColor;
import com.github.kory33.blokus.game.data.BlokusGameData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

public class BlokusGame {
    @NotNull private final BlokusGameData gameData;
    @NotNull private BlokusGamePhases currentGamePhase;

    private final BlokusBoardMesh redPlacementMesh;
    private final BlokusBoardMesh bluePlacementMesh;

    public BlokusGame() {
        this.gameData = new BlokusGameData();

        this.redPlacementMesh = new BlokusBoardMesh(PlayerColor.RED);
        this.bluePlacementMesh = new BlokusBoardMesh(PlayerColor.BLUE);
        this.currentGamePhase = BlokusGamePhases.getResumeStatus(BlokusConstant.FIRST_PLAYER);
    }

    private BlokusGame(BlokusGame game) {
        this.gameData = game.getGameData();
        this.currentGamePhase = game.getPhase();

        this.redPlacementMesh = game.redPlacementMesh.getCopy();
        this.bluePlacementMesh = game.bluePlacementMesh.getCopy();
    }

    public void makePlacement(ColoredBlokusPlacement placement) {
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

    public Set<ColoredBlokusPlacement> getPossiblePlacements() {
        if (this.isGameFinished()) {
            return new HashSet<>();
        }

        BlokusBoardMesh playerBoardMesh;
        if (this.currentGamePhase == BlokusGamePhases.RESUME_RED_PLAYER) {
            playerBoardMesh = this.redPlacementMesh;
        } else {
            playerBoardMesh = this.bluePlacementMesh;
        }

        return playerBoardMesh.getPossiblePlacements(this.gameData);
    }

    @NotNull
    public BlokusGamePhases getPhase() {
        return this.currentGamePhase;
    }

    public boolean isGameFinished() {
        return currentGamePhase.isGameFinished();
    }

    @NotNull
    public BlokusGameData getGameData() {
        return gameData.getCopy();
    }

    @Nullable
    public PlayerColor getWinnerColor() {
        if (!this.isGameFinished()) {
            return null;
        }
        return this.gameData.getPlacementCounts().getWinningColor();
    }

    public BlokusGame getCopy() {
        return new BlokusGame(this);
    }
}
