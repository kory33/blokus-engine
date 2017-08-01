package com.github.kory33.blokus.game;

import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * A class representing a Blokus game session.
 */
public class BlokusGameConductor {
    @NotNull private final BlokusGame gameInstance;
    private final IBlokusPlayer redPlayer;
    private final IBlokusPlayer bluePlayer;

    public BlokusGameConductor(IBlokusPlayer red, IBlokusPlayer blue) {
        this.gameInstance = new BlokusGame();
        this.redPlayer = red;
        this.bluePlayer = blue;
    }

    /**
     * Let next player choose a placement and play the chosen placement.
     * <p>
     * When the game has been already terminated, this method does nothing.
     * </p>
     */
    public void playSinglePlacement() {
        if (this.gameInstance.isGameFinished()) {
            return;
        }

        IBlokusPlayer nextPlayer;
        if (this.gameInstance.getPhase() == BlokusGamePhases.RESUME_RED_PLAYER) {
            nextPlayer = this.redPlayer;
        } else {
            nextPlayer = this.bluePlayer;
        }

        Set<ColoredBlokusPlacement> possiblePlacements = this.gameInstance.getPossiblePlacements();
        ColoredBlokusPlacement playerPlacement = nextPlayer.chooseBestPlacementFrom(possiblePlacements, this.gameInstance.getGameData());
        this.gameInstance.makePlacement(playerPlacement);
    }

    @NotNull
    public BlokusGame getGameInstance() {
        return this.gameInstance;
    }
}
