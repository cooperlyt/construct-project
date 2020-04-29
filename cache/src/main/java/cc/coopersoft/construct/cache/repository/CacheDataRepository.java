package cc.coopersoft.construct.cache.repository;

public interface CacheDataRepository<T,E> {

    void update(T data);
    void save(T data);
    void delete(E id);

    T find(E id);

}
