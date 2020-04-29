package cc.coopersoft.construct.cache.services;

import cc.coopersoft.construct.cache.repository.CacheDataRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class DataCacheService<T,E> {

    protected abstract T getData(E key);

    private final CacheDataRepository<T,E> repository;

    protected DataCacheService(CacheDataRepository<T, E> repository) {
        this.repository = repository;
    }

    private T checkCache(E key){
        try{
            return repository.find(key);
        }catch (Exception ex){
            log.error("Error encountered while trying to retrieve data {} check Cache.Exception {}", key, ex);
            return null;
        }
    }

    private void cache(T data){
        try{
            repository.save(data);
        }catch (Exception ex){
            log.error("Unable to  cache data {}. exception {}", data, ex);
        }
    }


    public T get(E key){
        T data = checkCache(key);
        if (data != null){
            log.debug(" successfully retrieved a data {} from the cache: {}", key, data);
            return data;
        }

        log.debug("Unable to locate data from the cache: {}",key);

        data = getData(key);


        if (data != null){
            cache(data);
        }

        return data;
    }
}
