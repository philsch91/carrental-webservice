package at.fhcampuswien.sde.carrentalwebservice.data;

import at.fhcampuswien.sde.carrentalwebservice.model.Car;

import java.util.List;
import java.util.Vector;

public class CarFactory {

    public CarFactory() { }

    public List<Car> buildCars() {
        List<Car> carList = new Vector<Car>();

        Car car1 = new Car();
        car1.setType("Car1");
        car1.setLatitude(48.208998);
        car1.setLongitude(16.373483);

        carList.add(car1);

        Car car2 = new Car();
        car2.setType("Car2");
        car2.setLatitude(48.217627);
        car2.setLongitude(16.395179);

        carList.add(car2);

        Car car3 = new Car();
        car3.setType("Car3");
        car3.setLatitude(48.158457);
        car3.setLongitude(16.382779);

        carList.add(car3);

        Car car4 = new Car();
        car4.setType("Car1");
        car4.setLatitude(48.186979);
        car4.setLongitude(16.313139);

        carList.add(car4);

        Car car5 = new Car();
        car5.setType("Car2");
        car5.setLatitude(48.216768);
        car5.setLongitude(16.312439);

        carList.add(car5);

        Car car6 = new Car();
        car6.setType("Car3");
        car6.setLatitude(48.211625);
        car6.setLongitude(16.357607);

        carList.add(car6);

        Car car7 = new Car();
        car7.setType("Car1");
        car7.setLatitude(48.231808);
        car7.setLongitude(16.414041);

        carList.add(car7);

        Car car8 = new Car();
        car8.setType("Car2");
        car8.setLatitude(48.209568);
        car8.setLongitude(16.343960);

        carList.add(car8);

        Car car9 = new Car();
        car9.setType("Car3");
        car9.setLatitude(48.202584);
        car9.setLongitude(16.368626);

        carList.add(car9);

        Car car10 = new Car();
        car10.setType("Car1");
        car10.setLatitude(48.208173);
        car10.setLongitude(16.366311);

        carList.add(car10);

        return carList;
    }
}
