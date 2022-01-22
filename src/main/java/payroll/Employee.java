package payroll;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Objects;

// this annotation makes the object ready for storage in a JPA-based data store
@Entity
class Employee {
    // these variables are attributes of Employee domain object
    // id has some JPA annotations so its primary key and auto generated
    private @Id @GeneratedValue Long id;
    private String name;
    private String role;

    Employee() {}

    Employee(String name, String role) {
        this.name = name;
        this.role = role;
    }

    // getter method Employee.getId()
    public Long getId() {
        return this.id;
    }

    // getter method Employee.getName()
    public String getName() {
        return this.name;
    }

    // getter method Employee.getRole()
    public String getRole() {
        return this.role;
    }

    // setter method Employee.setId()
    public void setId(Long id) {
        this.id = id;
    }

    // setter method Employee.setName()
    public void setName(String name) {
        this.name = name;
    }

    // setter method Employee.setRole()
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
        return Objects.equals(this.id, employee.id) && Objects.equals(this.name, employee.name) && Objects.equals(this.role, employee.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.name, this.role);
    }

    @Override
    public String toString() {
        return "Employee{" + "id=" + this.id + ", name='" + this.name + '\'' + ", role='" + this.role + '\'' + '}';
    }

}
