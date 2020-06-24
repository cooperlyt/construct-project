package cc.coopersoft.construct.corp.services;

import cc.coopersoft.construct.corp.model.Corp;
import cc.coopersoft.construct.corp.model.CorpEmployee;
import cc.coopersoft.construct.corp.repository.CorpEmployeeRepository;
import cc.coopersoft.construct.corp.repository.CorpRepository;
import com.github.wujun234.uid.UidGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServices {

    private final CorpEmployeeRepository corpEmployeeRepository;

    private final RemoteServices remoteServices;

    private final CorpRepository corpRepository;

    @Resource
    private UidGenerator defaultUidGenerator;

    public EmployeeServices(CorpEmployeeRepository corpEmployeeRepository, RemoteServices remoteServices, CorpRepository corpRepository) {
        this.corpEmployeeRepository = corpEmployeeRepository;
        this.remoteServices = remoteServices;
        this.corpRepository = corpRepository;
    }

    public List<CorpEmployee> employeeList(long corpCode){
        return corpEmployeeRepository.findByCorpCodeOrderById(corpCode);
    }

    @Transactional
    public CorpEmployee addEmployee(long code, CorpEmployee employee ){
        Corp corp = corpRepository.findById(code).orElseThrow();
        employee.setUsername(corp.getInfo().getGroupId());
        employee.setManager(true);
        employee.setUsername(remoteServices.addUser(code,employee));

        employee.setId(defaultUidGenerator.getUID());
        employee.setCorp(corp);
        employee.setValid(true);
        employee.setDataTime(new Date());
        return corpEmployeeRepository.save(employee);
    }

    public Optional<CorpEmployee> employee(long code){
        return corpEmployeeRepository.findById(code);
    }
}
