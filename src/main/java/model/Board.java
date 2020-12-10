package model;

import model.enums.FieldStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Board {

    Coordinates limit;
    HashMap<Coordinates,Field> fields;
    List<Ship> ships;

    public Board(Coordinates boardSize){
        this.limit = boardSize;
        this.fields = new HashMap<>();
        for(int i=0; i< boardSize.getX(); i++){
            for(int j=0; j<boardSize.getY(); j++){
                Coordinates location = new Coordinates(i, j);
                fields.put(location, new Field(location));
            }
        }
        this.ships = new ArrayList<>();
    }

    public Coordinates getLimit() {
        return limit;
    }

    public void addShip(Ship newShip) {
        this.ships.add(newShip);
        for(var field : newShip.getShipElements()){
            field.setFieldStatus(FieldStatus.FIELD_SHIP_ACTIVE);
        }
    }

    public Ship getShipAtPosition(Coordinates position) {
        return ships
            .stream()
            .filter(s -> s.getShipElements().stream().map(Field::getPosition).anyMatch(p -> p.equals(position)))
            .findFirst()
            .orElse(null);
    }

    public void removeShip(Ship shipToBeRemoved) {
        for(var field : shipToBeRemoved.getShipElements()) {
            field.setFieldStatus(FieldStatus.FIELD_EMPTY);
        }
        this.ships.remove(shipToBeRemoved);
    }

    public Field getFieldOnPosition(Coordinates position){
        return this.fields.get(position);
    }

    public boolean areCoordsInRange(Coordinates position){
        return position.less(this.limit) && position.getX() >= 0 && position.getY() >= 0;
    }

    public List<Field> getFieldsAround(Field field){
        ArrayList<Field> result = new ArrayList<Field>();
        for(int x=-1; x<=1; x++){
            for(int y=-1; y<=1; y++){
                if(x != 0 || y!= 0){
                    Coordinates toCheck = field.getPosition().add(new Coordinates(x, y));
                    if(this.areCoordsInRange(toCheck)){
                        result.add(this.getFieldOnPosition(toCheck));
                    }

                }
            }
        }
        return result;
    }
}