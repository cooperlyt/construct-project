package cc.coopersoft.construct.project.services;

import cc.coopersoft.construct.project.model.Project;
import cc.coopersoft.construct.project.model.ProjectReg;
import cc.coopersoft.construct.project.repository.RegRepository;
import cc.coopersoft.construct.project.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {

    private final RegRepository regRepository;

    private final ProjectRepository projectRepository;

    @Autowired
    public ProjectService(RegRepository regRepository,
                          ProjectRepository projectRepository) {
        this.regRepository = regRepository;
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


    public Optional<Project> project(String code){
        //TODO implements
        return null;
    }

    public Optional<ProjectReg> reg(String id){
        //TODO implements
        return null;
    }

    public List<ProjectReg> joinProjects(long code){
        return regRepository.findDistinctByCorpsCorpCodeAndStatusInOrderByCreateTimeDesc(code, EnumSet.of(BusinessStatus.running,BusinessStatus.valid));
    }

    public void enableProject(boolean enable){
        //TODO implements
    }


    public Project patchCreate(ProjectReg reg){

        //TODO implements
        return null;
    }

    public Project pathModify(ProjectReg reg){
        //TODO implements
        return null;
    }

}
