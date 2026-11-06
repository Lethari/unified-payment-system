package domain;

public class Customer {
    private final int id;
    private final String name;
    private final String email;

    public Customer(int id, String name, String email) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Customer name is required");
        }
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Customer email is required");
        }

        this.id = id;
        this.name = name;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}
