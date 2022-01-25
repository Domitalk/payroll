package payroll;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

// data returned by each method will be straight in response body
@RestController
class EmployeeController {

    // constructor, starts with a EmployeeRepository instance
    private final EmployeeRepository repository;

    private final EmployeeModelAssembler assembler;

    EmployeeController(EmployeeRepository repository, EmployeeModelAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

    // Aggregate root
    // tag::get-aggregate-root[]
//    @GetMapping("/employees")
//    List<Employee> all() {
//        return repository.findAll();
//    }
    // end::get-aggregate-root[]

//    @GetMapping("/employees")
//    // CollectionModel is a HATEOAS container for more than one EntityModel
//    CollectionModel<EntityModel<Employee>> all() {
//        // This isn't a simple collection of employees, but a collection of employee resources
//        // so first, we make resources
//        List<EntityModel<Employee>> employees = repository.findAll().stream()
//                .map(employee -> EntityModel.of(employee,
//                        linkTo(methodOn(EmployeeController.class).one(employee.getId())).withSelfRel(),
//                        linkTo(methodOn(EmployeeController.class).all()).withRel("employees")))
//                .collect(Collectors.toList());
//
//        return CollectionModel.of(employees, linkTo(methodOn(EmployeeController.class).all()).withSelfRel());
//    }

    // AGAIN - USE ASSEMBLER INSTEAD TO MAKE PROCESS BETTER
    // Here we have a method that is going to send back a collection of models
    // instead we make a list of EntityModels using the assembler and put them in a collection, then return
    @GetMapping("/employees")
    CollectionModel<EntityModel<Employee>> all() {

        List<EntityModel<Employee>> employees = repository.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(employees, linkTo(methodOn(EmployeeController.class).all()).withSelfRel());

    }

    // The top level self with a link to show that the thing is a collection resource itself
    // _embedded represents HAL collections
//    {
//        "_embedded": {
//        "employeeList": [
//        {
//            "id": 1,
//                "name": "Bilbo Baggins",
//                "role": "burglar",
//                "_links": {
//            "self": {
//                "href": "http://localhost:8080/employees/1"
//            },
//            "employees": {
//                "href": "http://localhost:8080/employees"
//            }
//        }
//        },
//        {
//            "id": 2,
//                "name": "Frodo Baggins",
//                "role": "thief",
//                "_links": {
//            "self": {
//                "href": "http://localhost:8080/employees/2"
//            },
//            "employees": {
//                "href": "http://localhost:8080/employees"
//            }
//        }
//        }
//    ]
//    },
//        "_links": {
//        "self": {
//            "href": "http://localhost:8080/employees"
//        }
//    }
//    }

//
//    @PostMapping("/employees")
//    Employee newEmployee(@RequestBody Employee newEmployee) {
//        return repository.save(newEmployee);
//    }
    @PostMapping("/employees")
    ResponseEntity<?> newEmployee(@RequestBody Employee newEmployee) {

        EntityModel<Employee> entityModel = assembler.toModel(repository.save(newEmployee));

        return ResponseEntity //
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()) //
                .body(entityModel);
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
//    @GetMapping("/employees/{id}")
//    EntityModel<Employee> one(@PathVariable Long id) {
//
//        Employee employee = repository.findById(id) //
//                .orElseThrow(() -> new EmployeeNotFoundException(id));
//
//        // instead of Employee object from repository it returns a EntityModel of an Employee object
//        // EntityModel is a container from Spring HATEOAS that includes data + links instead of just data
//        return EntityModel.of(employee, //
//                // linkTo(methodOn(EmployeeController.class).one(id)).withSelfRel() asks that Spring HATEOAS build a link to the EmployeeController 's one() method, and flag it as a self link.
//                linkTo(methodOn(EmployeeController.class).one(id)).withSelfRel(),
//                // linkTo(methodOn(EmployeeController.class).all()).withRel("employees") asks Spring HATEOAS to build a link to the aggregate root, all(), and call it "employees".
//                linkTo(methodOn(EmployeeController.class).all()).withRel("employees"));
//                // building "links" with URI + relationship are what empowers the web
//
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

//    }

    // integrating assembler that was built out so that we can get models of Employee instead of creating Entity every time
    @GetMapping("/employees/{id}")
    EntityModel<Employee> one(@PathVariable Long id) {

        Employee employee = repository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));

        return assembler.toModel(employee);
    }
//
//    @PutMapping("/employees/{id}")
//    Employee replaceEmployee(@RequestBody Employee newEmployee, @PathVariable Long id) {
//        return repository.findById(id)
//                .map(employee -> {
//                    employee.setName(newEmployee.getName());
//                    employee.setRole(newEmployee.getRole());
//                    return repository.save(employee);
//                })
//                .orElseGet(() -> {
//                    newEmployee.setId(id);
//                    return repository.save(newEmployee);
//                });
//    }
    @PutMapping("/employees/{id}")
    ResponseEntity<?> replaceEmployee(@RequestBody Employee newEmployee, @PathVariable Long id) {

        Employee updatedEmployee = repository.findById(id) //
                .map(employee -> {
                    employee.setName(newEmployee.getName());
                    employee.setRole(newEmployee.getRole());
                    return repository.save(employee);
                }) //
                .orElseGet(() -> {
                    newEmployee.setId(id);
                    return repository.save(newEmployee);
                });

        EntityModel<Employee> entityModel = assembler.toModel(updatedEmployee);

        return ResponseEntity //
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()) //
                .body(entityModel);
    }
//
//    @DeleteMapping("/employees/{id}")
//    void deleteEmployee(@PathVariable Long id) {
//        repository.deleteById(id);
//    }
    @DeleteMapping("/employees/{id}")
    ResponseEntity<?> deleteEmployee(@PathVariable Long id) {

        repository.deleteById(id);

        return ResponseEntity.noContent().build();
    }

}
