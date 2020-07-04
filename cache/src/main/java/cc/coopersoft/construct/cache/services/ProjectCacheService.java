package cc.coopersoft.construct.cache.services;

import cc.coopersoft.common.construct.project.Project;
import cc.coopersoft.construct.cache.repository.CacheDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class ProjectCacheService extends DataCacheService<Project.Default,Long> {


    private final WebClient webClient;

    @Autowired
    protected ProjectCacheService(CacheDataRepository<Project.Default, Long> repository, WebClient webClient) {
        super(repository);
        this.webClient = webClient;
    }

    @Override
    protected Mono<Project.Default> getData(Long key) {
        return webClient
                .get()
                .uri("http://construct-project/publish/project/{code}",key)
                .retrieve()
                .bodyToMono(Project.Default.class);
    }
}
