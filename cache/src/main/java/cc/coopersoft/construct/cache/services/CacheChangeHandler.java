package cc.coopersoft.construct.cache.services;


import cc.coopersoft.common.construct.corp.Corp;
import cc.coopersoft.common.construct.project.Project;
import cc.coopersoft.construct.cache.CacheChangeChannel;
import cc.coopersoft.construct.cache.repository.CacheDataRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

@EnableBinding(CacheChangeChannel.class)
@Slf4j
public class CacheChangeHandler {

    private final CacheDataRepository<Corp.Default,Long> corpRepository;

    private final CacheDataRepository<Project.Default,Long> projectRepository;

    @Autowired
    public CacheChangeHandler(
            CacheDataRepository<Corp.Default,Long> corpRepository,
            CacheDataRepository<Project.Default,Long>   projectRepository) {
        this.corpRepository = corpRepository;
        this.projectRepository = projectRepository;
    }

    @StreamListener(CacheChangeChannel.corpChannel)
    public void corpChange(Long code){
        log.debug("remove corp {} cache for change ",code);
        corpRepository.delete(code);
    }

    @StreamListener(CacheChangeChannel.projectChannel)
    public void projectChange(Long code){
        log.debug("remove project {} cache for change ",code);
        projectRepository.delete(code);
    }

}
