package cc.coopersoft.construct.corp.services;

import cc.coopersoft.construct.corp.model.Corp;
import cc.coopersoft.construct.corp.model.CorpEmployee;
import cc.coopersoft.construct.corp.repository.CorpEmployeeRepository;
import cc.coopersoft.construct.corp.repository.CorpRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class EmployeeServices {

    private final CorpEmployeeRepository corpEmployeeRepository;

    private final RemoteServices remoteServices;

    private final CorpRepository corpRepository;

    public EmployeeServices(CorpEmployeeRepository corpEmployeeRepository, RemoteServices remoteServices, CorpRepository corpRepository) {
        this.corpEmployeeRepository = corpEmployeeRepository;
        this.remoteServices = remoteServices;
        this.corpRepository = corpRepository;
    }

    public List<CorpEmployee> employeeList(long corpCode){
        return corpEmployeeRepository.findByCorpCodeOrderByCode(corpCode);
    }

    @Transactional
    public CorpEmployee addEmployee(long code, CorpEmployee employee ){
        employee.setUsername(String.valueOf(code));
        employee.setManager(true);
        employee.setUsername(remoteServices.addUser(code,employee));
        Corp corp = corpRepository.findById(code).orElseThrow();
        employee.setCorp(corp);
        employee.setValid(true);
        employee.setDataTime(new Date());
        return corpEmployeeRepository.save(employee);
    }
}
