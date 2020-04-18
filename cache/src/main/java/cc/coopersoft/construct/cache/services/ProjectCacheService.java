package cc.coopersoft.construct.cache.services;

import cc.coopersoft.common.construct.project.Project;
import cc.coopersoft.construct.cache.repository.CacheDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Service;

@Service
public class ProjectCacheService extends DataCacheService<Project.Default,Long> {

    private final OAuth2RestTemplate oAuth2RestTemplate;

    @Autowired
    protected ProjectCacheService(OAuth2RestTemplate oAuth2RestTemplate,
                                  CacheDataRepository<Project.Default, Long> repository) {
        super(repository);
        this.oAuth2RestTemplate = oAuth2RestTemplate;
    }

    @Override
    protected Project.Default getData(Long key) {
        ResponseEntity<Project.Default> restExchange = oAuth2RestTemplate.exchange(
                "http://construct-project/view/project/{code}",
                HttpMethod.GET, null,Project.Default.class,key);

        return restExchange.getBody();
    }
}
