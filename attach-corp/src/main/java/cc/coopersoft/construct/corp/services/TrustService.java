package cc.coopersoft.construct.corp.services;

import cc.coopersoft.construct.corp.model.Corp;
import cc.coopersoft.construct.corp.model.CorpEmployee;
import cc.coopersoft.construct.corp.repository.CorpEmployeeRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TrustService {

    private final CorpEmployeeRepository corpEmployeeRepository;

    public TrustService(CorpEmployeeRepository corpEmployeeRepository) {
        this.corpEmployeeRepository = corpEmployeeRepository;
    }


    public List<Corp> listCorp(String username){
        return corpEmployeeRepository.findByValidIsTrueAndCorpEnableIsTrueAndUsername(username).stream().map(CorpEmployee::getCorp).collect(Collectors.toList());
    }

}
