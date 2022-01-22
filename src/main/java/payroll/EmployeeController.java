package payroll;

import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

// data returned by each method will be straight in response body
@RestController
class EmployeeController {

    // constructor, starts with a EmployeeRepository instance
    private final EmployeeRepository repository;

    EmployeeController(EmployeeRepository repository) {
        this.repository = repository;
    }

    // Aggregate root
    // tag::get-aggregate-root[]
    @GetMapping("/employees")
    List<Employee> all() {
        return repository.findAll();
    }
    // end::get-aggregate-root[]

    @PostMapping("/employees")
    Employee newEmployee(@RequestBody Employee newEmployee) {
        return repository.save(newEmployee);
    }

    //Single item
//    @GetMapping("/employees/{id}")
//    Employee one(@PathVariable Long id) {
//        return repository.findById(id)
//                .orElseThrow(() -> new EmployeeNotFoundException(id));
//    }

    // HATEOAS integration
    // The above used simple PRC logic, now we send back hypertext ie: links
    //
    @GetMapping("/employees/{id}")
    EntityModel<Employee> one(@PathVariable Long id) {

        Employee employee = repository.findById(id) //
                .orElseThrow(() -> new EmployeeNotFoundException(id));

        // instead of Employee object from repository it returns a EntityModel of an Employee object
        // EntityModel is a container from Spring HATEOAS that includes data + links instead of just data
        return EntityModel.of(employee, //
                // linkTo(methodOn(EmployeeController.class).one(id)).withSelfRel() asks that Spring HATEOAS build a link to the EmployeeController 's one() method, and flag it as a self link.
                linkTo(methodOn(EmployeeController.class).one(id)).withSelfRel(),
                // linkTo(methodOn(EmployeeController.class).all()).withRel("employees") asks Spring HATEOAS to build a link to the aggregate root, all(), and call it "employees".
                linkTo(methodOn(EmployeeController.class).all()).withRel("employees"));
                // building "links" with URI + relationship are what empowers the web

// SAMPLE RESTful response to a single employee
//        {
//            "id": 1,
//            "name": "Bilbo Baggins",
//            "role": "burglar",
//            "_links": {
//                "self": {
//                    "href": "http://localhost:8080/employees/1"
//                },
//                "employees": {
//                    "href": "http://localhost:8080/employees"
//                }
//            }
//        }
    }

    @PutMapping("/employees/{id}")
    Employee replaceEmployee(@RequestBody Employee newEmployee, @PathVariable Long id) {
        return repository.findById(id)
                .map(employee -> {
                    employee.setName(newEmployee.getName());
                    employee.setRole(newEmployee.getRole());
                    return repository.save(employee);
                })
                .orElseGet(() -> {
                    newEmployee.setId(id);
                    return repository.save(newEmployee);
                });
    }

    @DeleteMapping("/employees/{id}")
    void deleteEmployee(@PathVariable Long id) {
        repository.deleteById(id);
    }

}
