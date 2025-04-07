package org.battleShip.model;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class AITest {

    @InjectMocks
    private AI ai;

    @Mock
    private Random random;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGenerateShip_Type1() {
        when(random.nextInt(10)).thenReturn(5);

        Ship ship = ai.generateShip((byte) 1);
        assertEquals(1, ship.getCoordinates().size());
        assertEquals('A', ship.getCoordinates().get(0).getX());
        assertEquals(5, ship.getCoordinates().get(0).getY());
    }

    @Test
    public void testGenerateShip_Type2() {
        when(random.nextInt(10)).thenReturn(5, 6);

        Ship ship = ai.generateShip((byte) 2);
        assertEquals(2, ship.getCoordinates().size());
        assertEquals('A', ship.getCoordinates().get(0).getX());
        assertEquals(5, ship.getCoordinates().get(0).getY());
        assertEquals('A', ship.getCoordinates().get(1).getX());
        assertEquals(6, ship.getCoordinates().get(1).getY());
    }

    @Test
    public void testGenerateShip_Type3() {
        when(random.nextInt(10)).thenReturn(5, 6, 7);

        Ship ship = ai.generateShip((byte) 3);
        assertEquals(3, ship.getCoordinates().size());
        assertEquals('A', ship.getCoordinates().get(0).getX());
        assertEquals(5, ship.getCoordinates().get(0).getY());
        assertEquals('A', ship.getCoordinates().get(1).getX());
        assertEquals(6, ship.getCoordinates().get(1).getY());
        assertEquals('A', ship.getCoordinates().get(2).getX());
        assertEquals(7, ship.getCoordinates().get(2).getY());
    }

    @Test
    public void testGenerateShipCoordinates() {
        ai.generateShipCoordinates();
        assertEquals(10, ai.alienShips.size());
        assertEquals(0, ai.missStrikes.size()); // Assuming no miss fields are added initially
    }

    @Test
    public void testCalcMissFields() {
        List<Coordinate> coordinates = new ArrayList<>();
        coordinates.add(new Coordinate('A', 5));
        coordinates.add(new Coordinate('A', 6));

        ai.calcMissFields(coordinates);

        Set<Coordinate> expectedMissStrikes = Set.of(
                new Coordinate('A', 4), new Coordinate('A', 7),
                new Coordinate('B', 4), new Coordinate('B', 5), new Coordinate('B', 6), new Coordinate('B', 7)
        );

        assertEquals(expectedMissStrikes, ai.missStrikes);
    }

    @Test
    public void testGenerateShip_WithMaxTries() {
        when(random.nextInt(10)).thenThrow(new RuntimeException())
                .thenReturn(5);

        Ship ship = ai.generateShip((byte) 1);
        assertEquals(1, ship.getCoordinates().size());
        assertEquals('A', ship.getCoordinates().get(0).getX());
        assertEquals(5, ship.getCoordinates().get(0).getY());
    }

    @Test
    public void testGenerateShip_WithMaxTriesExceeded() {
        when(random.nextInt(10)).thenThrow(new RuntimeException());

        assertThrows(RuntimeException.class, () -> {
            ai.generateShip((byte) 1);
        });
    }
}



//package org.battleShip.model;
//
//import org.junit.jupiter.api.Test;
//
//class AITest {
//
//    @Test
//    void calcNextX() {
//        AI ai = new AI();
//
//        ai.generateShipCoordinates();
//
//        BattleField battleField = new BattleField(ai);
//        battleField.printBattleField();


//        Ship destroyedShip = new Ship(
//              null, // Axis.Y,
//              null, // (byte) 3,
//                new ArrayList<>() {{
//                    add(new Coordinates('A', 1));
//                    add(new Coordinates('B', 1));
////                    add(new Coordinates('D', 3));
//                }},
//                new ArrayList<>() {{
//                    add(new Coordinates('A', 1));
//                    add(new Coordinates('B', 1));
////                    add(new Coordinates('D', 3));
//                }},
//                true
//        );


//        ai.setAlienShips(Collections.singleton(destroyedShip));
//        ai.calcMissFieldsAfterDestroyed(destroyedShip);

//        ai.checkDamageSections(new Coordinates('A', 1));
//        BattleField battleField = new BattleField(ai);

//        battleField.printBattleField();
//        System.out.println(shipType4First.getShipAxis());
//       System.out.println(ai.calcNextX('А'));
//       System.out.println(ai.calcNextX('А'));
//       System.out.println(ai.calcNextX('А'));
//       System.out.println(ai.calcNextX('А'));

    }
}