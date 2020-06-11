package cc.coopersoft.construct.corp.services;

import cc.coopersoft.construct.corp.model.CorpEmployee;
import cc.coopersoft.construct.corp.repository.CorpEmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeServices {

    private final CorpEmployeeRepository corpEmployeeRepository;

    public EmployeeServices(CorpEmployeeRepository corpEmployeeRepository) {
        this.corpEmployeeRepository = corpEmployeeRepository;
    }

    public List<CorpEmployee> employeeList(long corpCode){
        return corpEmployeeRepository.findByCorpCodeOrderByCode(corpCode);
    }

    public CorpEmployee addEmployee(long code,  String password){

    }
}
