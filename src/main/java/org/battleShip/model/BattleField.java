package org.battleShip.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BattleField {
    private AI ai;
    private static final String GREEN = "\033[0;32m";
    private static final String RED = "\033[0;31m";
    public static final String YELLOW = "\033[0;33m";
    public static final String RESET = "\033[0m";
    private static final String BLUE = "\033[0;34m";

    public void registrationStrike(Coordinate coordinate) {

        List<Coordinate> destroySections;
        Boolean missStrike = true;

        for (Ship ship : ai.getAlienShips()) {
            if (ship.getCoordinates().contains(coordinate)) {
                destroySections = ship.getDestroySections();
                destroySections.add(coordinate);
                ship.setDestroySections(destroySections);
                System.out.println(GREEN + "Damaged! " + coordinate + RESET);

                ai.setPreviousDamagedStrike(coordinate);

                missStrike = false;

                ai.calcShipAxis(ship);

                if (ship.getCoordinates().size() == ship.getDestroySections().size()) {
                    System.out.println(GREEN + "Destroyed! " + "ShipType: " + ship.getShipType() + " " + coordinate + RESET);
                    ship.setDestroyed(true);
                    ai.setPreviousDamagedStrike(null);

                    ai.calcMissFieldsAfterDestroyed(ship);
                }
            }
        }

        if (missStrike) {
            System.out.println(RED + "Miss! " + coordinate + RESET);
        }
    }

    public void printBattleField() {
        for (int y = 0; y < 11; y++) {
            if (y == 0) {
                System.out.println("X  A B C D E F G H I J");
            } else {
                String line = y == 10 ? String.valueOf(y) : y + " ";

                for (char x = 'A'; x < 'K'; x++) {
                    Coordinate coordinate = new Coordinate(x, y);

                    String mark = " ";
                    for (Ship ship : ai.getAlienShips()) {
                        if (ship.getCoordinates().contains(coordinate))
                            mark = BLUE + "O" + RESET;
                        if (ship.getDestroySections() != null && ship.getDestroySections().contains(coordinate))
                            mark = RED + "X" + RESET;
                    }

                    if (ai.getMissStrikes().contains(coordinate))
                        mark = "*";
                    line = line + " " + mark;
                }
                System.out.println(line);
            }
        }
    }

}
