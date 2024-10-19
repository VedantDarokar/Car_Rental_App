import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Car {
    private String carId;
    private String brand;
    private String model;
    private double basePricePerDay;
    private boolean isAvailable;

    public Car(String carId, String brand, String model, double basePricePerDay) {
        this.carId = carId;
        this.brand = brand;
        this.model = model;
        this.basePricePerDay = basePricePerDay;
        this.isAvailable = true;
    }

    public String getCarId() {
        return carId;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public double calculatePrice(int rentalDays) {
        return basePricePerDay * rentalDays;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void rent() {
        isAvailable = false;
    }

    public void returnCar() {
        isAvailable = true;
    }
}

class Customer {
    private String customerId;
    private String name;

    public Customer(String customerId, String name) {
        this.customerId = customerId;
        this.name = name;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getName() {
        return name;
    }
}

class Rental {
    private Car car;
    private Customer customer;
    private int days;

    public Rental(Car car, Customer customer, int days) {
        this.car = car;
        this.customer = customer;
        this.days = days;
    }

    public Car getCar() {
        return car;
    }

    public Customer getCustomer() {
        return customer;
    }
}

class CarRentalSystem {
    private List<Car> cars;
    private List<Customer> customers;
    private List<Rental> rentals;

    public CarRentalSystem() {
        cars = new ArrayList<>();
        customers = new ArrayList<>();
        rentals = new ArrayList<>();
    }

    public void addCar(Car car) {
        cars.add(car);
    }

    public void addCustomer(Customer customer) {
        customers.add(customer);
    }

    public void rentCar(String carId, Customer customer, int days) {
        Car selectedCar = findCarById(carId);

        if (selectedCar != null && selectedCar.isAvailable()) {
            selectedCar.rent();
            rentals.add(new Rental(selectedCar, customer, days));
            System.out.println("\nCar rented successfully.");
        } else {
            System.out.println("\nCar is not available for rent.");
        }
    }

    public void returnCar(String carId) {
        Car car = findCarById(carId);

        if (car != null && !car.isAvailable()) {
            car.returnCar();
            rentals.removeIf(rental -> rental.getCar().equals(car));
            System.out.println("Car returned successfully.");
        } else {
            System.out.println("Invalid car ID or car is not rented.");
        }
    }

    private Car findCarById(String carId) {
        return cars.stream().filter(car -> car.getCarId().equals(carId)).findFirst().orElse(null);
    }

    public void showMenu() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n===== Car Rental System =====");
            System.out.println("1. Rent a Car");
            System.out.println("2. Return a Car");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    handleRentCar(scanner);
                    break;
                case 2:
                    handleReturnCar(scanner);
                    break;
                case 3:
                    System.out.println("Thank you for using the Car Rental System!");
                    return;
                default:
                    System.out.println("Invalid choice, please try again.");
            }
        }
    }

    private void handleRentCar(Scanner scanner) {
        System.out.print("\nEnter your name: ");
        String customerName = scanner.nextLine();
        Customer customer = new Customer("CUS" + (customers.size() + 1), customerName);
        addCustomer(customer);

        System.out.println("\nAvailable Cars:");
        cars.stream().filter(Car::isAvailable).forEach(car ->
            System.out.println(car.getCarId() + " - " + car.getBrand() + " " + car.getModel())
        );

        System.out.print("\nEnter the car ID you want to rent: ");
        String carId = scanner.nextLine();

        System.out.print("Enter the number of rental days: ");
        int rentalDays = scanner.nextInt();
        scanner.nextLine();

        Car selectedCar = findCarById(carId);

        if (selectedCar != null && selectedCar.isAvailable()) {
            double totalPrice = selectedCar.calculatePrice(rentalDays);
            System.out.printf("Total Price: %.2f INR%n", totalPrice);
            System.out.print("\nConfirm rental (Y/N): ");
            String confirm = scanner.nextLine();

            if (confirm.equalsIgnoreCase("Y")) {
                rentCar(carId, customer, rentalDays);
            } else {
                System.out.println("\nRental canceled.");
            }
        } else {
            System.out.println("\nInvalid car selection or car not available.");
        }
    }

    private void handleReturnCar(Scanner scanner) {
        System.out.print("\nEnter the car ID you want to return: ");
        String carId = scanner.nextLine();
        returnCar(carId);
    }
}

public class CarRentalApp {
    public static void main(String[] args) {
        CarRentalSystem rentalSystem = new CarRentalSystem();

        rentalSystem.addCar(new Car("C001", "Toyota", "Camry", 2000));
        rentalSystem.addCar(new Car("C002", "Honda", "Elevate", 2500));
        rentalSystem.addCar(new Car("C003", "Mahindra", "Thar", 3000));

        rentalSystem.showMenu();
    }
}
