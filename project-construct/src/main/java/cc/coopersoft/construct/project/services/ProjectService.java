package cc.coopersoft.construct.project.services;

import cc.coopersoft.common.construct.corp.Corp;
import cc.coopersoft.common.construct.corp.CorpProperty;
import cc.coopersoft.common.construct.corp.CorpReg;
import cc.coopersoft.common.construct.corp.RegInfo;
import cc.coopersoft.common.data.RegSource;
import cc.coopersoft.common.data.RegStatus;
import cc.coopersoft.construct.project.model.*;
import cc.coopersoft.construct.project.repository.RegRepository;
import cc.coopersoft.construct.project.repository.ProjectRepository;
import com.github.wujun234.uid.UidGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ProjectService {

    private final RegRepository regRepository;

    private final ProjectRepository projectRepository;

    private final OAuth2RestTemplate oAuth2RestTemplate;

    @Resource
    private UidGenerator defaultUidGenerator;

    private final Source source;

    public void publishChangeMessage(long code){
        log.debug(" sending message corp {} change ", code);
        source.output().send(MessageBuilder.withPayload(code).build());
    }

    @Autowired
    public ProjectService(RegRepository regRepository,
                          ProjectRepository projectRepository,
                          OAuth2RestTemplate oAuth2RestTemplate,
                          Source source) {
        this.regRepository = regRepository;
        this.projectRepository = projectRepository;
        this.oAuth2RestTemplate = oAuth2RestTemplate;
        this.source = source;
    }


    public Page<Project> projects(Optional<Boolean> valid,
                                  Optional<Integer> page,
                                  Optional<String> key,
                                  Optional<String> sort,
                                  Optional<String> dir){
        //TODO implements
        return null;
    }

    public List<JoinCorp> joinProjects(long code){
        //TODO implements
        return null;
    }

    public Optional<Project> project(long code){
        return projectRepository.findById(code);
    }

    public void enableProject(long code, boolean enable){
        publishChangeMessage(code);
        Optional<Project> project = project(code);
        if (project.isEmpty()){
            throw new IllegalArgumentException("project is not found! :" + code);
        }
        project.get().setEnable(enable);
        projectRepository.save(project.get());
    }

    private ProjectReg patch(ProjectReg reg){
        reg.setApplyTime(new Date());
        reg.setRegTime(new Date());
        reg.setSource(RegSource.Patch);
        reg.setStatus(RegStatus.Register);

        if (reg.isCorpMaster()) {
            reg.getCorp().setRegTime(new Date());
            reg.getCorp().setRegistered(true);
        }

        if (reg.isInfoMaster()) {
            reg.getInfo().setRegTime(new Date());
            reg.getInfo().setRegistered(true);
        }
        return reg;
    }

    private JoinCorp getDeveloper(ProjectCorpReg reg){
        for(JoinCorp corp : reg.getCorps()){
            if (CorpProperty.Developer.equals(corp.getProperty())){
                return corp;
            }
        }
        throw new IllegalArgumentException("no Developer found.");
    }

    @Transactional()
    public Project patchCreate(ProjectReg reg){

        reg = create(reg);
        reg = patch(reg);

        Project  project = new Project();
        project.setCode(reg.getCode());
        project.setEnable(true);
        project.setDataTime(new Date());
        project.setInfo(reg.getInfo());
        project.setCorp(reg.getCorp());
        project.setDeveloper(getDeveloper(reg.getCorp()));

        regRepository.save(reg);

        return projectRepository.save(project);
    }

    @Transactional()
    public Project pathModify(long code , ProjectReg reg){
        publishChangeMessage(code);
        Optional<Project> project = project(code);
        if (project.isEmpty() || !project.get().isEnable()){
            throw new IllegalArgumentException("project is not found or not enable : " + code);
        }

        reg = modify(project.get(), reg);
        reg = patch(reg);

        if (reg.isInfoMaster()){
            project.get().setInfo(reg.getInfo());
        }
        if (reg.isCorpMaster()){
            project.get().setCorp(reg.getCorp());
            project.get().setDeveloper(getDeveloper(reg.getCorp()));
        }

        project.get().setDataTime(new Date());

        regRepository.save(reg);

        return projectRepository.save(project.get());
    }

    public boolean projectInReg(long code){
        return regRepository.existsByCodeAndStatus(code, RegStatus.Running);
    }

    private JoinCorpInfo getCorpInfo(CorpProperty property, long code){

        ResponseEntity<Corp.Default> restExchange = oAuth2RestTemplate.exchange(
                "http://construct-project-cache/data/corp/{code}",
                HttpMethod.GET, null,Corp.Default.class,code);

        Corp.Default corp = restExchange.getBody();

        if (corp == null || !corp.isEnable()){
            throw new IllegalArgumentException("corp is not found or not enable: " + code);
        }

        JoinCorpInfo result = new JoinCorpInfo();
        result.setName(corp.getInfo().getName());
        result.setGroupIdType(corp.getInfo().getGroupIdType());
        result.setGroupId(corp.getInfo().getGroupId());

        for(CorpReg<RegInfo> reg : corp.getRegs()){
            if (property.equals(reg.getProperty())){
                result.setLevel(reg.getInfo().getLevel());
                return result;
            }
        }

        throw new IllegalArgumentException("corp is found ! but not have this property:[" + property + "]" + code);
    }


    private ProjectReg create(ProjectReg reg){
        reg.setCreateTime(new Date());
        reg.setCode(defaultUidGenerator.getUID());
        reg.setId(reg.getCode());
        reg.setCorpMaster(true);
        reg.setInfoMaster(true);
        for(JoinCorp corp: reg.getCorp().getCorps()){
            corp.setId(defaultUidGenerator.getUID());
            corp.setInfo(getCorpInfo(corp.getProperty(),corp.getCode()));
        }
        reg.getInfo().getInfo().setId(reg.getId());
        return reg;
    }

    private ProjectReg modify(Project project, ProjectReg reg){
        if (projectInReg(project.getCode())){
            throw new IllegalArgumentException("project in reg business: " + project.getCode());
        }


        reg.setCreateTime(new Date());
        reg.setCode(project.getCode());
        reg.setId(defaultUidGenerator.getUID());
        reg.setCorpMaster(reg.getCorp() != null);

        if (reg.isCorpMaster()){
            reg.getCorp().setPrevious(project.getCorp());
            for(JoinCorp corp: reg.getCorp().getCorps()){
                corp.setId(defaultUidGenerator.getUID());
                corp.setInfo(getCorpInfo(corp.getProperty(),corp.getCode()));
            }
        }else{
            reg.setCorp(project.getCorp());
        }

        reg.setInfoMaster(reg.getInfo() != null);
        if (reg.isInfoMaster()){
            reg.getInfo().setPrevious(project.getInfo());
            reg.getInfo().getInfo().setId(defaultUidGenerator.getUID());
        }else{
            reg.setInfo(project.getInfo());
        }

        return reg;
    }

}
