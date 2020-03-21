package cc.coopersoft.construct.project.services;

import cc.coopersoft.construct.project.model.JoinCorp;
import cc.coopersoft.construct.project.model.Project;
import cc.coopersoft.construct.project.model.ProjectInfo;
import cc.coopersoft.construct.project.repository.BusinessRepository;
import cc.coopersoft.construct.project.repository.JoinCorpRepository;
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

    private final JoinCorpRepository joinCorpRepository;

    @Autowired
    public ProjectService(BusinessRepository businessRepository,
                          ProjectRepository projectRepository,
                          JoinCorpRepository joinCorpRepository) {
        this.businessRepository = businessRepository;
        this.projectRepository = projectRepository;
        this.joinCorpRepository = joinCorpRepository;
    }


    public Page<Project> projects(boolean valid,
                                  Optional<Integer> page,
                                  Optional<String> key,
                                  Optional<String> sort,
                                  Optional<String> dir){
        //TODO implements
        return null;
    }


    public Page<ProjectInfo> businesses(Optional<Integer> page,
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

    public Optional<ProjectInfo> business(String id){
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


    public Project patchCreate(ProjectInfo business){

        //TODO implements
        return null;
    }

    public Project pathModify(ProjectInfo business){
        //TODO implements
        return null;
    }

}
