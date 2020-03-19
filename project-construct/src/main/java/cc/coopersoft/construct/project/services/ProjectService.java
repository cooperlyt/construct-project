package cc.coopersoft.construct.project.services;

import cc.coopersoft.construct.project.repository.BusinessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {

    private final BusinessRepository businessRepository;

    private final ProjectService projectService;

    @Autowired
    public ProjectService(BusinessRepository businessRepository, ProjectService projectService) {
        this.businessRepository = businessRepository;
        this.projectService = projectService;
    }


}
