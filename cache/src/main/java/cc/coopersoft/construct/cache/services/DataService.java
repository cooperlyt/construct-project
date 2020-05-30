package cc.coopersoft.construct.cache.services;

import cc.coopersoft.common.construct.corp.Corp;
import cc.coopersoft.common.construct.project.JoinCorp;
import cc.coopersoft.common.construct.project.JoinCorpInfo;
import cc.coopersoft.common.construct.project.Project;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DataService {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProjectAndCorp{
        private Project.Default project;
        private List<Corp.Default> corps;
    }

    private final CorpCacheService corpCacheService;

    private final ProjectCacheService projectCacheService;

    @Autowired
    public DataService(CorpCacheService corpCacheService, ProjectCacheService projectCacheService) {
        this.corpCacheService = corpCacheService;
        this.projectCacheService = projectCacheService;
    }


    public ProjectAndCorp projectAndCorp(Long code){
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
