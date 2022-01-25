package payroll;

// this annotation makes the object ready for storage in a JPA-based data store
// Spring Data JPA repos are interfaces with methods for create, read, update and deleting records
//@Entity
//class Employee {
//    // these variables are attributes of Employee domain object
//    // id has some JPA annotations so its primary key and auto generated
//    private @Id @GeneratedValue Long id;
//    private String name;
//    private String role;
//
//    Employee() {}
//
//    Employee(String name, String role) {
//        this.name = name;
//        this.role = role;
//    }
//
//    // getter method Employee.getId()
//    public Long getId() {
//        return this.id;
//    }
//
//    // getter method Employee.getName()
//    public String getName() {
//        return this.name;
//    }
//
//    // getter method Employee.getRole()
//    public String getRole() {
//        return this.role;
//    }
//
//    // setter method Employee.setId()
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    // setter method Employee.setName()
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    // setter method Employee.setRole()
//    public void setRole(String role) {
//        this.role = role;
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o)
//            return true;
//        if (!(o instanceof Employee))
//            return false;
//        Employee employee = (Employee) o;
//        return Objects.equals(this.id, employee.id) && Objects.equals(this.name, employee.name) && Objects.equals(this.role, employee.role);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(this.id, this.name, this.role);
//    }
//
//    @Override
//    public String toString() {
//        return "Employee{" + "id=" + this.id + ", name='" + this.name + '\'' + ", role='" + this.role + '\'' + '}';
//    }
//
//}

// The whole point of REST is to make things resilient.
// when things change, it's easy to break
// with REST we can try to make sure that features can last longer without breaking by following some strict guidelines set in the beginning

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Objects;

@Entity
class Employee {

    private @Id @GeneratedValue Long id;
    private String firstName;
    private String lastName;
    private String role;

    Employee () {}
    Employee(String firstName, String lastName, String role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
    }

    public String getName() {
        return this.firstName + " " + this.lastName;
    }

    public void setName(String name) {
        String[] parts = name.split(" ");
        this.firstName = parts[0];
        this.lastName = parts[1];
    }

    public Long getId() {
        return this.id;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public String getRole() {
        return this.role;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o)
            return true;
        if (!(o instanceof Employee))
            return false;
        Employee employee = (Employee) o;
        return Objects.equals(this.id, employee.id)
                && Objects.equals(this.firstName, employee.firstName)
                && Objects.equals(this.lastName, employee.lastName)
                && Objects.equals(this.role, employee.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.firstName, this.lastName, this.role);
    }

    @Override
    public String toString() {
        return "Employee{" + "id=" + this.id + ", firstName='" + this.firstName + '\'' + ", lastName='" + this.lastName
                + '\'' + ", role='" + this.role + '\'' + '}';
    }
//    Field name has been replaced by firstName and lastName.
//    A "virtual" getter for the old name property, getName() is defined. It uses the firstName and lastName fields to produce a value.
//    A "virtual" setter for the old name property is also defined, setName(). It parses an incoming string and stores it into the proper fields.

// Basically, when we make a new Employee


}