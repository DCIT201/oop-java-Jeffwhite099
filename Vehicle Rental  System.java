import java.util.*;

// 1. Abstract Class for Abstraction Principle
abstract class Vehicle {
    private String vehicleId;
    private String model;
    private double baseRentalRate;
    private boolean isAvailable;

    public Vehicle(String vehicleId, String model, double baseRentalRate) {
        this.vehicleId = vehicleId;
        this.model = model;
        this.baseRentalRate = baseRentalRate;
        this.isAvailable = true;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public String getModel() {
        return model;
    }

    public double getBaseRentalRate() {
        return baseRentalRate;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailability(boolean availability) {
        this.isAvailable = availability;
    }

    public abstract double calculateRentalCost(int days);

    public abstract boolean isAvailableForRental();
}

// 2. Specific Vehicle Classes
class Car extends Vehicle {
    private boolean hasGPS;

    public Car(String vehicleId, String model, double baseRentalRate, boolean hasGPS) {
        super(vehicleId, model, baseRentalRate);
        this.hasGPS = hasGPS;
    }

    public boolean hasGPS() {
        return hasGPS;
    }

    @Override
    public double calculateRentalCost(int days) {
        double cost = getBaseRentalRate() * days;
        if (hasGPS) {
            cost += 5 * days; // Additional fee for GPS
        }
        return cost;
    }

    @Override
    public boolean isAvailableForRental() {
        return isAvailable();
    }
}

class Motorcycle extends Vehicle {
    public Motorcycle(String vehicleId, String model, double baseRentalRate) {
        super(vehicleId, model, baseRentalRate);
    }

    @Override
    public double calculateRentalCost(int days) {
        return getBaseRentalRate() * days;
    }

    @Override
    public boolean isAvailableForRental() {
        return isAvailable();
    }
}

class Truck extends Vehicle {
    private double cargoCapacity;

    public Truck(String vehicleId, String model, double baseRentalRate, double cargoCapacity) {
        super(vehicleId, model, baseRentalRate);
        this.cargoCapacity = cargoCapacity;
    }

    public double getCargoCapacity() {
        return cargoCapacity;
    }

    @Override
    public double calculateRentalCost(int days) {
        return getBaseRentalRate() * days + (cargoCapacity * 0.5 * days); // Additional fee based on cargo capacity
    }

    @Override
    public boolean isAvailableForRental() {
        return isAvailable();
    }
}

// 3. Customer Class
class Customer {
    private String customerId;
    private String name;
    private List<Vehicle> rentalHistory;

    public Customer(String customerId, String name) {
        this.customerId = customerId;
        this.name = name;
        this.rentalHistory = new ArrayList<>();
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getName() {
        return name;
    }

    public void addRentalToHistory(Vehicle vehicle) {
        rentalHistory.add(vehicle);
    }

    public List<Vehicle> getRentalHistory() {
        return rentalHistory;
    }
}

// 4. Interface for Polymorphism
interface Rentable {
    void rent(Customer customer, int days);
    void returnVehicle();
}

// 5. Rental Agency Class
class RentalAgency {
    private List<Vehicle> fleet;
    private List<RentalTransaction> transactions;

    public RentalAgency() {
        this.fleet = new ArrayList<>();
        this.transactions = new ArrayList<>();
    }

    public void addVehicleToFleet(Vehicle vehicle) {
        fleet.add(vehicle);
    }

    public void processRental(Customer customer, String vehicleId, int days) {
        for (Vehicle vehicle : fleet) {
            if (vehicle.getVehicleId().equals(vehicleId) && vehicle.isAvailableForRental()) {
                vehicle.setAvailability(false);
                transactions.add(new RentalTransaction(customer, vehicle, days));
                customer.addRentalToHistory(vehicle);
                System.out.println("Rental processed successfully.");
                return;
            }
        }
        System.out.println("Vehicle not available for rental.");
    }

    public void generateReport() {
        System.out.println("Rental Transactions Report:");
        for (RentalTransaction transaction : transactions) {
            System.out.println(transaction);
        }
    }
}

// 6. RentalTransaction Class
class RentalTransaction {
    private Customer customer;
    private Vehicle vehicle;
    private int rentalDays;

    public RentalTransaction(Customer customer, Vehicle vehicle, int rentalDays) {
        this.customer = customer;
        this.vehicle = vehicle;
        this.rentalDays = rentalDays;
    }

    @Override
    public String toString() {
        return "Customer: " + customer.getName() + ", Vehicle: " + vehicle.getModel() + ", Days: " + rentalDays;
    }
}

public class Main {
    public static void main(String[] args) {
        RentalAgency agency = new RentalAgency();
        Customer customer1 = new Customer("C001", "Alice");

        Car car = new Car("V001", "Toyota Camry", 50, true);
        Motorcycle motorcycle = new Motorcycle("V002", "Honda CBR", 30);
        Truck truck = new Truck("V003", "Ford F-150", 70, 1000);

        agency.addVehicleToFleet(car);
        agency.addVehicleToFleet(motorcycle);
        agency.addVehicleToFleet(truck);

        agency.processRental(customer1, "V001", 3);
        agency.processRental(customer1, "V002", 2);

        agency.generateReport();
    }
}
