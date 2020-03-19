package cc.coopersoft.construct.project.services;

import cc.coopersoft.construct.project.model.JoinCorp;
import cc.coopersoft.construct.project.model.Project;
import cc.coopersoft.construct.project.model.ProjectBusiness;
import cc.coopersoft.construct.project.repository.BusinessRepository;
import cc.coopersoft.construct.project.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {

    private final BusinessRepository businessRepository;

    private final ProjectRepository projectRepository;

    @Autowired
    public ProjectService(BusinessRepository businessRepository,
                          ProjectRepository projectRepository) {
        this.businessRepository = businessRepository;
        this.projectRepository = projectRepository;
    }


    public Page<Project> projects(boolean valid,
                                  Optional<Integer> page,
                                  Optional<String> key,
                                  Optional<String> sort,
                                  Optional<String> dir){
        //TODO implements
        return null;
    }


    public Page<ProjectBusiness> businesses(Optional<Integer> page,
                                            Optional<String> key,
                                            Optional<String> sort,
                                            Optional<String> dir){

        //TODO implements
        return null;
    }


    public Optional<Project> project(String code){
        //TODO implements
        return null;
    }

    public Optional<ProjectBusiness> business(String id){
        //TODO implements
        return null;
    }

    public List<JoinCorp> joinProjects(String code){
        //TODO implements
        return null;
    }

    public void enableProject(boolean enable){
        //TODO implements
    }


    public Project patchCreate(ProjectBusiness business){

        //TODO implements
        return null;
    }

    public Project pathModify(ProjectBusiness business){
        //TODO implements
        return null;
    }

}
