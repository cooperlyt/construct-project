package cc.coopersoft.trust.gateway.repository;

public interface CacheDataRepository<T,E> {

    void update(E id, T data);
    void save(E id, T data);
    void delete(E id);

    T find(E id);

}
