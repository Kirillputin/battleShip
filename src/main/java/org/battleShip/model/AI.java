package org.battleShip.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.battleShip.enums.Axis;
import org.battleShip.enums.Sign;

import java.util.*;
import java.util.stream.IntStream;

@Data
@NoArgsConstructor
public class AI {
    private Coordinate previousDamagedStrike;
    private Coordinate strike;
    private Set<Ship> alienShips = new HashSet<>();
    private Set<Coordinate> missStrikes = new HashSet<>();
    private Random random = new Random();
    final private String validChars = "ABCDEFGHIJ";
    final private List<Integer> validY = IntStream.range(1, 11).boxed().toList();

    public void generateShipCoordinates() {
        Ship shipType4 = generateShip((byte) 4);
        alienShips.add(shipType4);
        calcMissFields(shipType4.getCoordinates());

        Ship shipType3_1 = generateShip((byte) 3);
        alienShips.add(shipType3_1);
        calcMissFields(shipType3_1.getCoordinates());

        Ship shipType3_2 = generateShip((byte) 3);
        alienShips.add(shipType3_2);
        calcMissFields(shipType3_2.getCoordinates());

        Ship shipType2_1 = generateShip((byte) 2);
        alienShips.add(shipType2_1);
        calcMissFields(shipType2_1.getCoordinates());

        Ship shipType2_2 = generateShip((byte) 2);
        alienShips.add(shipType2_2);
        calcMissFields(shipType2_2.getCoordinates());

        Ship shipType2_3 = generateShip((byte) 2);
        alienShips.add(shipType2_3);
        calcMissFields(shipType2_3.getCoordinates());

        Ship shipType1_1 = generateShip((byte) 1);
        alienShips.add(shipType1_1);
        calcMissFields(shipType1_1.getCoordinates());

        Ship shipType1_2 = generateShip((byte) 1);
        alienShips.add(shipType1_2);
        calcMissFields(shipType1_2.getCoordinates());

        Ship shipType1_3 = generateShip((byte) 1);
        alienShips.add(shipType1_3);
        calcMissFields(shipType1_3.getCoordinates());

        Ship shipType1_4 = generateShip((byte) 1);
        alienShips.add(shipType1_4);
        calcMissFields(shipType1_4.getCoordinates());

        missStrikes = new HashSet<>();
    }

    private Ship generateShip(byte type) {

        int count = 0;
        int maxTries = 3;

        while (true) {

            try {

                Ship ship = new Ship(null, type, new ArrayList<>(), new ArrayList<>());
                Coordinate nextCoordinate = null;

                if (type == 1) {
                    ship = new Ship(null, type, new ArrayList<>(), new ArrayList<>());

                    nextCoordinate = getCalcCoordinates(null, ship);
                    ship.addCoordinates(nextCoordinate);

                } else {
                    for (int i = 1; i < type; i++) {
                        if (i == 1) {
                            nextCoordinate = getCalcCoordinates(null, ship);
                            ship.addCoordinates(nextCoordinate);
                        }

                        if (i == 2) {
                            ship.setShipAxis(calcShipAxis(ship.getCoordinates()));
                        }

                        nextCoordinate = getCalcCoordinates(nextCoordinate, ship);
                        ship.addCoordinates(nextCoordinate);
                    }
                }
                return ship;

            } catch (RuntimeException e) {
                if (++count == maxTries) throw e;
            }
        }
    }

    private Coordinate getCalcCoordinates(Coordinate previousCoordinate, Ship ship) {

        long infinityExceptionIndex = 0;

        if (previousCoordinate == null) {
            //рандомно
            //вычислить координаты
            Coordinate randomCoordinate = getRandomCoordinates();
            // проверки на повторения
            ////проверить на промахи     ////проверить на попадания
            while (checkMissStrikes(randomCoordinate) || checkCloseSections(ship, randomCoordinate)) {
                infinityExceptionIndex++;
                randomCoordinate = getRandomCoordinates();
                if (infinityExceptionIndex == 100) {
                    throw new RuntimeException("Не могу вычислить следующую координату " + randomCoordinate + " для " + ship);
                }
            }

            return randomCoordinate;
        } else {
            // по соседней клетке
            Coordinate nextCoordinate = getNextCoordinates(previousCoordinate, ship);

            while (checkMissStrikes(nextCoordinate) || checkCloseSections(ship, nextCoordinate)) {
                infinityExceptionIndex++;
                nextCoordinate = getNextCoordinates(previousCoordinate, ship);
                if (infinityExceptionIndex == 100) {
                    throw new RuntimeException("Не могу вычислить следующую координату " + nextCoordinate + " для " + ship);
                }
            }

            return nextCoordinate;
        }
    }


    public Coordinate getCalcStrike() {

        if (previousDamagedStrike == null) {
            //стреляем рандомно
            //вычислить координаты
            Coordinate randomCoordinate = getRandomCoordinates();
            // проверки на повторения
            ////проверить на промахи     ////проверить на попадания
            while (checkMissStrikes(randomCoordinate) || checkDamageSections(randomCoordinate))
                randomCoordinate = getRandomCoordinates();

            // Проверка на попадание
            if (checkDamage(randomCoordinate))
                return randomCoordinate;
            else

                missStrikes.add(randomCoordinate);
            return randomCoordinate;
        } else {
            //стреляем по соседней клетке
            Coordinate nextCoordinate = getNextCoordinates();

            while (checkMissStrikes(nextCoordinate) || checkDamageSections(nextCoordinate))
                nextCoordinate = getNextCoordinates();

            if (checkDamage(nextCoordinate))
                return nextCoordinate;
            else
                missStrikes.add(nextCoordinate);
            return nextCoordinate;
        }
    }

    public void calcShipAxis(Ship ship) {
        List<Coordinate> destroySections = ship.getDestroySections()
                .stream()
                .sorted(Comparator.comparing(Coordinate::getX)
                        .thenComparing(Coordinate::getY)).toList();

        Coordinate prevCoordinate = null;

        for (Coordinate coordinate : destroySections) {
            if (prevCoordinate == null) {
                prevCoordinate = coordinate;

            } else {
                if (prevCoordinate.getX() + 1 == coordinate.getX()) {
                    ship.setShipAxis(Axis.X);
                }

                if (prevCoordinate.getY() + 1 == coordinate.getY()) {
                    ship.setShipAxis(Axis.Y);
                }
            }
        }
    }


    public Axis calcShipAxis(List<Coordinate> sections) {

        sections = sections.stream()
                .sorted(Comparator.comparing(Coordinate::getX)
                        .thenComparing(Coordinate::getY)).toList();

        Coordinate prevCoordinate = null;

        for (Coordinate coordinate : sections) {
            if (prevCoordinate == null) {
                prevCoordinate = coordinate;

            } else {
                if (prevCoordinate.getX() + 1 == coordinate.getX()) {
                    return Axis.X;
                }

                if (prevCoordinate.getY() + 1 == coordinate.getY()) {
                    return Axis.Y;
                }
            }
        }
        return null;
    }

    private Coordinate getNextCoordinates(Coordinate previousCoordinate, Ship ship) {
        Axis axis = null;
        Coordinate closeSection = null;

        List<Coordinate> closeSections = ship.getCoordinates()
                .stream()
                .sorted(Comparator.comparing(Coordinate::getX)
                        .thenComparing(Coordinate::getY)).toList();

        if (closeSections.contains(previousCoordinate)) {
            axis = ship.getShipAxis();
            closeSection = random.nextBoolean() ? closeSections.get(0) : closeSections.get(closeSections.size() - 1);
        }

        if (axis == null) {
            axis = random.nextBoolean() ? Axis.X : Axis.Y;
        }

        if (closeSection == null) {
            closeSection = previousCoordinate;
        }

        return switch (axis) {
            case X -> new Coordinate(calcNextX(closeSection.getX()), closeSection.getY());
            case Y -> new Coordinate(closeSection.getX(), calcNextY(closeSection.getY()));
        };
    }

    private Coordinate getNextCoordinates() {
        Axis axis = null;
        Coordinate destroySection = null;

        for (Ship alienShip : alienShips) {
            List<Coordinate> destroySections = alienShip.getDestroySections()
                    .stream()
                    .sorted(Comparator.comparing(Coordinate::getX)
                            .thenComparing(Coordinate::getY)).toList();

            if (destroySections.contains(previousDamagedStrike)) {
                axis = alienShip.getShipAxis();
                destroySection = random.nextBoolean() ? destroySections.get(0) : destroySections.get(destroySections.size() - 1);
            }
        }

        if (axis == null) {
            axis = random.nextBoolean() ? Axis.X : Axis.Y;
        }

        if (destroySection == null) {
            destroySection = previousDamagedStrike;
        }

        return switch (axis) {
            case X -> new Coordinate(calcNextX(destroySection.getX()), destroySection.getY());
            case Y -> new Coordinate(destroySection.getX(), calcNextY(destroySection.getY()));
        };
    }

//    private int calcNextY(int y) {
//        Sign sign = random.nextBoolean() ? Sign.PLUS : Sign.MINUS;
//
//        int nextY = y;
//
//        switch (sign) {
//            case PLUS -> {
//                nextY++;
//                if (checkValidateY(nextY)) {
//                    return nextY;
//                } else {
//                    nextY = y;
//                    nextY--;
//                    return nextY;
//                }
//            }
//            case MINUS -> {
//                nextY--;
//                if (checkValidateY(nextY)) {
//                    return nextY;
//                } else {
//                    nextY = y;
//                    nextY++;
//                    return nextY;
//                }
//            }
//        }
//        return 0;
//    }

    private int calcNextY(int y) {
        boolean isPlus = random.nextBoolean();

        if (isPlus) {
            int nextY = y + 1;
            if (checkValidateY(nextY)) {
                return nextY;
            }
        } else {
            int nextY = y - 1;
            if (checkValidateY(nextY)) {
                return nextY;
            }
        }

        return isPlus ? y - 1 : y + 1;
    }

    private char calcNextX(char x) {
        // вычислить рандомно "+" или "-"
        Sign sign = random.nextBoolean() ? Sign.PLUS : Sign.MINUS;

        char nextX = x;

        switch (sign) {
            case PLUS -> {
                nextX++;
                if (checkValidateX(nextX)) {
                    return nextX;
                } else {
                    nextX = x;
                    nextX--;
                    return nextX;
                }
            }
            case MINUS -> {
                nextX--;
                if (checkValidateX(nextX)) {
                    return nextX;
                } else {
                    nextX = x;
                    nextX++;
                    return nextX;
                }
            }
        }
        return 0;
    }

    private boolean checkValidateX(char x) {
        return validChars.contains(String.valueOf(x));
    }

    private boolean checkValidateY(int y) {
        return validY.contains(y);
    }

    public boolean checkDamageSections(Coordinate coordinate) {
        return alienShips.stream().anyMatch(ship -> ship.getDestroySections().contains(coordinate));
    }

    public boolean checkCloseSections(Ship ship, Coordinate coordinate) {
        return ship.getCoordinates().contains(coordinate) || alienShips.stream().anyMatch(existShip -> existShip.getCoordinates().contains(coordinate));
    }

    private boolean checkMissStrikes(Coordinate coordinate) {
        return missStrikes.contains(coordinate);
    }

    public void calcMissFieldsAfterDestroyed(Ship destroyedShip) {
        Coordinate currentCoordinate;

        for (Coordinate destroyedCoordinate : destroyedShip.getDestroySections()) {

            currentCoordinate = new Coordinate((char) (destroyedCoordinate.getX() - 1), destroyedCoordinate.getY() - 1);
            if (!destroyedShip.getDestroySections().contains(currentCoordinate))
                missStrikes.add(currentCoordinate);

            currentCoordinate = new Coordinate((char) (destroyedCoordinate.getX() - 1), destroyedCoordinate.getY() + 1);
            if (!destroyedShip.getDestroySections().contains(currentCoordinate))
                missStrikes.add(currentCoordinate);

            currentCoordinate = new Coordinate((char) (destroyedCoordinate.getX() - 1), destroyedCoordinate.getY());
            if (!destroyedShip.getDestroySections().contains(currentCoordinate))
                missStrikes.add(currentCoordinate);

            currentCoordinate = new Coordinate((destroyedCoordinate.getX()), destroyedCoordinate.getY() - 1);
            if (!destroyedShip.getDestroySections().contains(currentCoordinate))
                missStrikes.add(currentCoordinate);

            currentCoordinate = new Coordinate((destroyedCoordinate.getX()), destroyedCoordinate.getY() + 1);
            if (!destroyedShip.getDestroySections().contains(currentCoordinate))
                missStrikes.add(currentCoordinate);

            currentCoordinate = new Coordinate((char) (destroyedCoordinate.getX() + 1), destroyedCoordinate.getY() - 1);
            if (!destroyedShip.getDestroySections().contains(currentCoordinate))
                missStrikes.add(currentCoordinate);

            currentCoordinate = new Coordinate((char) (destroyedCoordinate.getX() + 1), destroyedCoordinate.getY() + 1);
            if (!destroyedShip.getDestroySections().contains(currentCoordinate))
                missStrikes.add(currentCoordinate);

            currentCoordinate = new Coordinate((char) (destroyedCoordinate.getX() + 1), destroyedCoordinate.getY());
            if (!destroyedShip.getDestroySections().contains(currentCoordinate))
                missStrikes.add(currentCoordinate);
        }
    }

    public void calcMissFields(List<Coordinate> sections) {
        Coordinate currentCoordinate;

        for (Coordinate section : sections) {

            currentCoordinate = new Coordinate((char) (section.getX() - 1), section.getY() - 1);
            if (!sections.contains(currentCoordinate))
                missStrikes.add(currentCoordinate);

            currentCoordinate = new Coordinate((char) (section.getX() - 1), section.getY() + 1);
            if (!sections.contains(currentCoordinate))
                missStrikes.add(currentCoordinate);

            currentCoordinate = new Coordinate((char) (section.getX() - 1), section.getY());
            if (!sections.contains(currentCoordinate))
                missStrikes.add(currentCoordinate);

            currentCoordinate = new Coordinate((section.getX()), section.getY() - 1);
            if (!sections.contains(currentCoordinate))
                missStrikes.add(currentCoordinate);

            currentCoordinate = new Coordinate((section.getX()), section.getY() + 1);
            if (!sections.contains(currentCoordinate))
                missStrikes.add(currentCoordinate);

            currentCoordinate = new Coordinate((char) (section.getX() + 1), section.getY() - 1);
            if (!sections.contains(currentCoordinate))
                missStrikes.add(currentCoordinate);

            currentCoordinate = new Coordinate((char) (section.getX() + 1), section.getY() + 1);
            if (!sections.contains(currentCoordinate))
                missStrikes.add(currentCoordinate);

            currentCoordinate = new Coordinate((char) (section.getX() + 1), section.getY());
            if (!sections.contains(currentCoordinate))
                missStrikes.add(currentCoordinate);
        }
    }


    private Coordinate getRandomCoordinates() {
        return new Coordinate(calcRandomX(), calcRandomY());
    }

    private char calcRandomX() {
        int randomInt = random.nextInt(10) + 1;
        int index = 0;

        for (char x = 'A'; x < 'K'; x++) {
            index++;
            if (index == randomInt)
                return x;
        }
        return 0;
    }

    private int calcRandomY() {
        return random.nextInt(10) + 1;
    }

    private boolean checkDamage(Coordinate coordinate) {
        return alienShips.stream().anyMatch(ship -> ship.getCoordinates().contains(coordinate));
    }

    //    private Ship generateType4() {
//        Ship shipType4 = new Ship(null, (byte) 4, new ArrayList<>(), new ArrayList<>());
//
//        Coordinate firstCoordinate = getCalcCoordinates(null, shipType4);
//        shipType4.addCoordinates(firstCoordinate);
//
//        Coordinate secondCoordinate = getCalcCoordinates(firstCoordinate, shipType4);
//        shipType4.addCoordinates(secondCoordinate);
//
//        shipType4.setShipAxis(calcShipAxis(shipType4.getCoordinates()));
//
//        Coordinate thirdCoordinate = getCalcCoordinates(secondCoordinate, shipType4);
//        shipType4.addCoordinates(thirdCoordinate);
//
//        Coordinate fourthCoordinate = getCalcCoordinates(thirdCoordinate, shipType4);
//        shipType4.addCoordinates(fourthCoordinate);
//
//        return shipType4;
//    }
//
//    private Ship generateType3() {
//
//        int count = 0;
//        int maxTries = 3;
//
//        while (true) {
//
//            try {
//                Ship shipType3 = new Ship(null, (byte) 3, new ArrayList<>(), new ArrayList<>());
//
//                Coordinate firstCoordinate = getCalcCoordinates(null, shipType3);
//                shipType3.addCoordinates(firstCoordinate);
//
//                Coordinate secondCoordinate = getCalcCoordinates(firstCoordinate, shipType3);
//                shipType3.addCoordinates(secondCoordinate);
//
//                shipType3.setShipAxis(calcShipAxis(shipType3.getCoordinates()));
//
//                Coordinate thirdCoordinate = getCalcCoordinates(secondCoordinate, shipType3);
//                shipType3.addCoordinates(thirdCoordinate);
//
//                return shipType3;
//
//            } catch (RuntimeException e) {
//                if (++count == maxTries) throw e;
//            }
//        }
//
//    }
//
//    private Ship generateType2() {
//
//        int count = 0;
//        int maxTries = 3;
//
//        while (true) {
//
//            try {
//
//                Ship shipType2 = new Ship(null, (byte) 2, new ArrayList<>(), new ArrayList<>());
//
//                Coordinate firstCoordinate = getCalcCoordinates(null, shipType2);
//                shipType2.addCoordinates(firstCoordinate);
//
//                Coordinate secondCoordinate = getCalcCoordinates(firstCoordinate, shipType2);
//                shipType2.addCoordinates(secondCoordinate);
//
//                return shipType2;
//
//            } catch (RuntimeException e) {
//                if (++count == maxTries) throw e;
//            }
//        }
//    }
//
//    private Ship generateType1() {
//
//        int count = 0;
//        int maxTries = 3;
//
//        while (true) {
//
//            try {
//
//                Ship shipType1 = new Ship(null, (byte) 1, new ArrayList<>(), new ArrayList<>());
//
//                Coordinate firstCoordinate = getCalcCoordinates(null, shipType1);
//                shipType1.addCoordinates(firstCoordinate);
//
//                return shipType1;
//
//            } catch (RuntimeException e) {
//                if (++count == maxTries) throw e;
//            }
//        }
//    }


}
