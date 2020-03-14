package at.fhcampuswien.sde.carrentalwebservice.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;

    protected Car(){}

    public Car(long id, String name){
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return this.id;
    }

    public void setId(Long id){
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }

    @Override
    public String toString() {
        return String.format("Car[id=%d, name='%s']",id, name);
    }
}
