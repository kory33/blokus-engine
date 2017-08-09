### Rules of Blokus Game

## Prerequisites

* A "board" is defined and noted as `B`
* A board has "cells", and they are represented as `B\[i,j] (1 <= i, j <= 12)`
* First player's "beginning cell" is B[3, 3] and the latter player's is B[10, 10]

## Rules
* A player's "area" is defined as the cells on which the player has made a "placement"
* A placement consists of several cells that are connected on their sides.
* A "turn" is completed by a player's placement
  - If no placement is available to the player, its turn gets skipped.
* Upon making a placement, a player has to ensure that
  - All the cells in a placement must be connected each other by their sides.
  - In the first placement, the player has to include its beginning cell.
  - From the second placement, the player has to include one of cells that "touches"
    the player area by their vertices.
  - All the cells in the placement must not touch any of the player area cells by their side.
  - All the cells in the placement must not be the player's or the adversary's area.
* A "size" of a placement is defined as the number of cells in a placement.
* In a single game session, a player can make:
  - 10 placements with size of 3
  - 5 placements with size of 4
  - 2 placements with size of 5
* If both the players cannot make a placement, the game terminates.
* The player who has obtained a larger area at the end of the game wins.
  - If both the players have equally sized area, the latter player wins.
  