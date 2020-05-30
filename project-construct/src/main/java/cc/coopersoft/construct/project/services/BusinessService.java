package cc.coopersoft.construct.project.services;

import cc.coopersoft.common.construct.corp.Corp;
import cc.coopersoft.common.construct.corp.CorpProperty;
import cc.coopersoft.common.construct.corp.CorpReg;
import cc.coopersoft.common.construct.corp.RegInfo;
import cc.coopersoft.common.construct.project.ProjectCorpSummary;
import cc.coopersoft.common.data.BusinessType;
import cc.coopersoft.common.data.OperationType;
import cc.coopersoft.common.data.RegSource;
import cc.coopersoft.common.data.RegStatus;
import cc.coopersoft.construct.project.model.*;
import cc.coopersoft.construct.project.repository.BuildRepository;
import cc.coopersoft.construct.project.repository.RegRepository;
import cc.coopersoft.construct.project.repository.ProjectRepository;
import com.github.wujun234.uid.UidGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

@Service
@Slf4j
public class BusinessService {



    private final RegRepository regRepository;

    private final ProjectRepository projectRepository;



    private final BuildRepository buildRepository;

    private final RemoteService remoteService;


    @Resource
    private UidGenerator cachedUidGenerator;


    @Autowired
    public BusinessService(RegRepository regRepository,
                           ProjectRepository projectRepository,
                           BuildRepository buildRepository, RemoteService remoteService) {
        this.regRepository = regRepository;
        this.projectRepository = projectRepository;
        this.buildRepository = buildRepository;
        this.remoteService = remoteService;
    }



    private void saveBuildRecord(BuildReg buildReg, long projectCode){
        for(BuildRegInfo build: buildReg.getBuilds()){
            switch (build.getOperation()){

                case DELETE:
                    buildRepository.deleteById(build.getCode());
                    break;
                case MODIFY:
                    Build buildRecord = buildRepository.findById(build.getCode()).orElseThrow();
                    //if (buildRecord.getInfo().getId().equals(build.getPrevious().getId())){
                        buildRecord.setInfo(build.getInfo());
                        buildRecord.setProjectCode(projectCode);
                        buildRecord.setRegTime(buildReg.getRegTime());
                        buildRepository.saveAndFlush(buildRecord);
                    //}

                    break;
                case CREATE:
                    Build newBuildRecord = new Build();
                    newBuildRecord.setCode(build.getCode());
                    newBuildRecord.setInfo(build.getInfo());
                    newBuildRecord.setRegTime(buildReg.getRegTime());
                    newBuildRecord.setEnable(true);
                    newBuildRecord.setProjectCode(projectCode);
                    buildRepository.saveAndFlush(newBuildRecord);
                    break;
            }
        }
    }

    @Transactional()
    public Project patchCreate(ProjectReg reg){

        reg = create(reg);
        reg = patch(reg);
        log.debug("save and flush reg.");
        regRepository.saveAndFlush(reg);
        log.debug("reg saved.");

        saveBuildRecord(reg.getBuild(),reg.getCode());

        Project project = new Project();
        project.setCode(reg.getCode());
        project.setEnable(true);
        project.setRegTime(new Date());
        project.setInfo(reg.getInfo());
        project.setCorp(reg.getCorp());
        project.setBuild(reg.getBuild());
        project.setDeveloper(getDeveloper(reg.getCorp()));

        log.debug("save project.");
        return projectRepository.save(project);
    }

    @Transactional()
    public Project pathModify(long code , ProjectReg reg){
        remoteService.notifyProjectChange(code);
        Project project = projectRepository.findById(code).orElseThrow();


        reg = modify(project, reg);
        reg = patch(reg);

        regRepository.saveAndFlush(reg);

        if (reg.isBuildMaster()){
            saveBuildRecord(reg.getBuild(),reg.getCode());
            project.setBuild(reg.getBuild());
        }

        if (reg.isInfoMaster()){
            project.setInfo(reg.getInfo());
        }
        if (reg.isCorpMaster()){
            project.setCorp(reg.getCorp());
            project.setDeveloper(getDeveloper(reg.getCorp()));
        }

        project.setRegTime(new Date());



        return projectRepository.save(project);
    }

    private ProjectReg patch(ProjectReg reg){
        reg.setApplyTime(new Date());
        reg.setRegTime(new Date());
        reg.setSource(RegSource.Patch);
        reg.setStatus(RegStatus.Register);

        if(reg.isBuildMaster()){
            reg.getBuild().setRegTime(new Date());
        }

        if (reg.isCorpMaster()) {
            reg.getCorp().setRegTime(new Date());
        }

        if (reg.isInfoMaster()) {
            reg.getInfo().setRegTime(new Date());
        }
        return reg;
    }

    private JoinCorp getDeveloper(JoinCorpReg reg){
        for(JoinCorp corp : reg.getCorps()){
            if (CorpProperty.Developer.equals(corp.getProperty())){
                return corp;
            }
        }
        throw new IllegalArgumentException("no Developer found.");
    }



    public boolean projectInReg(long code){
        return regRepository.existsByCodeAndStatus(code, RegStatus.Running);
    }

    private JoinCorpInfo getCorpInfo(JoinCorp joinCorp, long code){

        Corp.Default corp = remoteService.getCorp(code);

        if (corp == null || !corp.isEnable()){
            throw new IllegalArgumentException("corp is not found or not enable: " + code);
        }

        JoinCorpInfo result = new JoinCorpInfo();
        //result.setId(joinCorp.getId());
        result.setCorp(joinCorp);
        result.setName(corp.getInfo().getName());
        result.setGroupIdType(corp.getInfo().getGroupIdType());
        result.setGroupId(corp.getInfo().getGroupId());
        result.setOwnerId(corp.getInfo().getOwnerId());
        result.setOwnerIdType(corp.getInfo().getOwnerIdType());
        result.setOwnerName(corp.getInfo().getOwnerName());

        for(CorpReg<RegInfo> reg : corp.getRegs()){
            if (joinCorp.getProperty().equals(reg.getProperty())){
                result.setLevel(reg.getInfo().getLevel());
                return result;
            }
        }

        throw new IllegalArgumentException("corp is found ! but not have this property:[" + joinCorp.getProperty() + "]" + code);
    }

    private void initBuildReg(BusinessType businessType, BuildReg buildReg){
        int count = 0;
        BigDecimal onArea = BigDecimal.ZERO;
        BigDecimal underArea = BigDecimal.ZERO;
        for(BuildRegInfo buildRegInfo: buildReg.getBuilds()){
            if (!BusinessType.MODIFY.equals(businessType) || !OperationType.DELETE.equals(buildRegInfo.getOperation())){
                count++;
                if (buildRegInfo.getInfo().getOnArea() != null)
                    onArea = onArea.add(buildRegInfo.getInfo().getOnArea());
                if (buildRegInfo.getInfo().getUnderArea() != null){
                    underArea = underArea.add(buildRegInfo.getInfo().getUnderArea());
                }
            }
            buildRegInfo.setId(cachedUidGenerator.getUID());
            if (OperationType.MODIFY.equals(buildRegInfo.getOperation())){
                buildRegInfo.getInfo().setId(buildRegInfo.getId());
                buildRegInfo.setPrevious(buildRepository.findById(buildRegInfo.getCode()).orElseThrow().getInfo());
            }else if (OperationType.CREATE.equals(buildRegInfo.getOperation())){
                buildRegInfo.getInfo().setId(buildRegInfo.getId());
                buildRegInfo.setCode(buildRegInfo.getId());
            }
            if (OperationType.QUOTED.equals(buildRegInfo.getOperation())){
                buildRegInfo.setInfo(
                    buildRepository.findById(buildRegInfo.getCode()).orElseThrow().getInfo());
            }
        }
        buildReg.setCount(count);
        buildReg.setOnArea(onArea);
        buildReg.setUnderArea(underArea);
    }

    private ProjectReg create(ProjectReg reg){
        reg.setType(BusinessType.CREATE);
        reg.setCreateTime(new Date());
        reg.setCode(cachedUidGenerator.getUID());
        reg.setId(reg.getCode());


        reg.setCorpMaster(true);
        reg.getCorp().setId(reg.getId());


        reg.setBuildMaster(true);
        reg.getBuild().setId(reg.getId());
        initBuildReg(reg.getType(),reg.getBuild());

        log.debug("corp size:" + reg.getCorp().getCorps().size());
        for(JoinCorp corp: reg.getCorp().getCorps()){
            corp.setId(cachedUidGenerator.getUID());
            corp.setInfo(getCorpInfo(corp,corp.getCode()));
            corp.setReg(reg.getCorp());
        }
       // test(reg.getCorp().getCorps());
        ProjectCorpSummary.fillProjectCorpSummary(reg.getCorp(),reg.getCorp().getCorps());

        reg.setInfoMaster(true);
        reg.getInfo().setId(reg.getId());

        return reg;
    }

    private ProjectReg modify(Project project, ProjectReg reg){
        if (projectInReg(project.getCode())){
            throw new IllegalArgumentException("project in reg business: " + project.getCode());
        }

        reg.setType(BusinessType.MODIFY);
        reg.setCreateTime(new Date());
        reg.setCode(project.getCode());
        reg.setId(cachedUidGenerator.getUID());
        reg.setCorpMaster(reg.getCorp() != null);

        if (reg.isCorpMaster()){
            reg.getCorp().setId(reg.getId());
            reg.getCorp().setPrevious(project.getCorp());
            for(JoinCorp corp: reg.getCorp().getCorps()){
                corp.setId(cachedUidGenerator.getUID());
                corp.setInfo(getCorpInfo(corp,corp.getCode()));
                corp.setReg(reg.getCorp());
            }
            ProjectCorpSummary.fillProjectCorpSummary(reg.getCorp(),reg.getCorp().getCorps());
        }else{
            reg.setCorp(project.getCorp());
        }

        reg.setInfoMaster(reg.getInfo() != null);
        if (reg.isInfoMaster()){
            reg.getInfo().setId(reg.getId());
            reg.getInfo().setPrevious(project.getInfo());
        }else{
            reg.setInfo(project.getInfo());
        }

        reg.setBuildMaster(reg.getBuild() != null);
        if (reg.isBuildMaster()){
            reg.getBuild().setId(reg.getId());
            initBuildReg(reg.getType(),reg.getBuild());
        }else{
            reg.setBuild(project.getBuild());
        }

        return reg;
    }

}
