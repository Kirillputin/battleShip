package org.battleShip;

import org.battleShip.model.AI;
import org.battleShip.model.BattleField;

public class Main {
    public static void main(String[] args) {

//        Set<Ship> ships = new HashSet<>();
//        AI ai = new AI();
//
//        Ship shipType2First = new Ship(null,
//                (byte) 2,
//                new ArrayList<>() {{
//                    add(new Coordinate('A', 1));
//                    add(new Coordinate('B', 1));
//                }},
//                new ArrayList<>()
//        );
//
//        Ship shipType2Second = new Ship(null,
//                (byte) 2,
//                new ArrayList<>() {{
//                    add(new Coordinate('I', 9));
//                    add(new Coordinate('I', 10));
//                }},
//                new ArrayList<>()
//        );
//
//        Ship shipType2Third = new Ship(null,
//                (byte) 2,
//                new ArrayList<>() {{
//                    add(new Coordinate('J', 3));
//                    add(new Coordinate('J', 4));
//                }},
//                new ArrayList<>()
//        );
//
//        Ship shipType3First = new Ship(null,
//                (byte) 3,
//                new ArrayList<>() {{
//                    add(new Coordinate('D', 1));
//                    add(new Coordinate('D', 2));
//                    add(new Coordinate('D', 3));
//                }},
//                new ArrayList<>()
//        );
//
//        Ship shipType3Second = new Ship(null,
//                (byte) 3,
//                new ArrayList<>() {{
//                    add(new Coordinate('B', 5));
//                    add(new Coordinate('B', 6));
//                    add(new Coordinate('B', 7));
//                }},
//                new ArrayList<>()
//        );
//
//        Ship shipType4First = new Ship(null,
//                (byte) 4,
//                new ArrayList<>() {{
//                    add(new Coordinate('G', 1));
//                    add(new Coordinate('H', 1));
//                    add(new Coordinate('I', 1));
//                    add(new Coordinate('J', 1));
//                }},
//                new ArrayList<>()
//        );
//
//        Ship shipType1First = new Ship(null,
//                (byte) 1,
//                new ArrayList<>() {{
//                    add(new Coordinate('A', 10));
//                }},
//                new ArrayList<>()
//        );
//
//        Ship shipType1Second = new Ship(null,
//                (byte) 1,
//                new ArrayList<>() {{
//                    add(new Coordinate('D', 8));
//                }},
//                new ArrayList<>()
//        );
//
//        Ship shipType1Third = new Ship(null,
//                (byte) 1,
//                new ArrayList<>() {{
//                    add(new Coordinate('F', 8));
//                }},
//                new ArrayList<>()
//        );
//
//        Ship shipType1Fourth = new Ship(null,
//                (byte) 1,
//                new ArrayList<>() {{
//                    add(new Coordinate('G', 6));
//                }},
//                new ArrayList<>()
//        );
//
//        ships.add(shipType1First);
//        ships.add(shipType1Second);
//        ships.add(shipType1Third);
//        ships.add(shipType1Fourth);
//
//        ships.add(shipType2First);
//        ships.add(shipType2Second);
//        ships.add(shipType2Third);
//
//        ships.add(shipType3First);
//        ships.add(shipType3Second);
//
//        ships.add(shipType4First);
//
//        ai.setAlienShips(ships);
//        BattleField battleField = new BattleField(ai);

///////////////////////////////////GAME////////////////////////////////////////////////////////////
//        System.out.println("INITIATION FIELD");
//        battleField.printBattleField();
//
//        //FIRST FIRE!
//        System.out.println("\n/////////FIRST FIRE!///////////");
//        Coordinates firstDamagedStrike = new Coordinates('G', 6);
//        battleField.registrationStrike(firstDamagedStrike);
//        battleField.printBattleField();
//
//        //2 FIRE!
//        System.out.println("\n////////2 FIRE!///////////");
//        battleField.registrationStrike(ai.getCalcStrike());
//        battleField.printBattleField();
//
//        //3 FIRE!
//        System.out.println("\n////////3 FIRE!///////////");
//        battleField.registrationStrike(ai.getCalcStrike());
//        battleField.printBattleField();
//
//        //4 FIRE!
//        System.out.println("\n////////4 FIRE!///////////");
//        battleField.registrationStrike(ai.getCalcStrike());
//        battleField.printBattleField();
//
//        //5 FIRE!
//        System.out.println("\n////////5 FIRE!///////////");
//        battleField.registrationStrike(ai.getCalcStrike());
//        battleField.printBattleField();

        //AUTOMATIC GAME
        AI ai = new AI();
        ai.generateShipCoordinates();

        BattleField battleField = new BattleField(ai);

        System.out.println(BattleField.YELLOW + "\n//////// RANDOM FIELD  ///////////" + BattleField.RESET);
        battleField.printBattleField();

        boolean aliveShips = true;
        int step = 1;
        while (aliveShips) {
            System.out.println(BattleField.YELLOW + " \n //////// " + step++ + " FIRE! ///////////" + BattleField.RESET);
            battleField.registrationStrike(ai.getCalcStrike());
            battleField.printBattleField();
            aliveShips = ai.getAlienShips().stream().anyMatch(ship -> !ship.getDestroyed());
        }

        System.out.println(BattleField.YELLOW + "\n//////// GAME OVER! ///////////" + BattleField.RESET);
    }
}