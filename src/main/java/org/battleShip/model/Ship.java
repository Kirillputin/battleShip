package org.battleShip.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.battleShip.enums.Axis;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ship {
    private Axis shipAxis;
    private Byte shipType;
    private List<Coordinate> coordinates = new ArrayList<>();
    private List<Coordinate> destroySections = new ArrayList<>();
    private Boolean destroyed = false;

    public Ship(Axis shipAxis, Byte shipType, List<Coordinate> coordinates, List<Coordinate> destroySections) {
        this.shipAxis = shipAxis;
        this.shipType = shipType;
        this.coordinates = coordinates;
        this.destroySections = destroySections;
    }

    public void addCoordinates(Coordinate coordinate) {
        this.coordinates.add(coordinate);
    }
}


