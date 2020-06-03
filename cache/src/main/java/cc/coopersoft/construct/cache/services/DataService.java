package cc.coopersoft.construct.cache.services;

import cc.coopersoft.common.construct.corp.Corp;
import cc.coopersoft.common.construct.corp.CorpProperty;
import cc.coopersoft.common.construct.corp.CorpReg;
import cc.coopersoft.common.construct.corp.RegInfo;
import cc.coopersoft.common.construct.project.JoinCorp;
import cc.coopersoft.common.construct.project.JoinCorpInfo;
import cc.coopersoft.common.construct.project.Project;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class DataService {


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProjectAndCorp{
        private Project.Default project;
        private List<Corp.Default> corps;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class JoinCorpAndCorp{
        private JoinCorp<JoinCorpInfo> joinCorp;
        private Corp.Default corp;
    }

    private final CorpCacheService corpCacheService;

    private final ProjectCacheService projectCacheService;



    @Autowired
    public DataService(CorpCacheService corpCacheService, ProjectCacheService projectCacheService) {
        this.corpCacheService = corpCacheService;
        this.projectCacheService = projectCacheService;
    }



    public List<JoinCorpAndCorp> joinCorpAndCorp(long code){
        Project.Default project = projectCacheService.get(code);
        return project.getCorps().stream().map(corp -> JoinCorpAndCorp.builder().joinCorp(corp).corp(corpCacheService.get(corp.getCode())).build()).collect(Collectors.toList());
    }

    public ProjectAndCorp projectAndCorp(long code){
        Project.Default project = projectCacheService.get(code);
        if (project != null){
            Set<Long> corpCodes = new HashSet<>();
            ProjectAndCorp result = new ProjectAndCorp(project,new ArrayList<>(project.getCorps().size()));
            for(JoinCorp<JoinCorpInfo> corp: project.getCorps()){
                if (!corpCodes.contains(corp.getCode())){
                    result.getCorps().add(corpCacheService.get(corp.getCode()));
                    corpCodes.add(corp.getCode());
                }
            }
            return result;
        }
        return null;
    }
}
