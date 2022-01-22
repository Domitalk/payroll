package payroll;

import org.springframework.data.jpa.repository.JpaRepository;

// The extends will bring in all the JpaRepository functionality. IE: Rails x ActiveRecord
// This will specify the DB domain type as Employee and id type as Long
interface EmployeeRepository extends JpaRepository<Employee, Long> {

}
