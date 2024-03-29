package com.github.zdf.branduserauthorizationbackend.service.impl;

import com.github.zdf.branduserauthorizationbackend.service.BaseService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Example;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Objects;
import java.util.Optional;

public abstract class BaseServiceImpl<T, ID> implements BaseService<T, ID> {

    private MongoRepository<T, ID> repository;

    protected BaseServiceImpl(MongoRepository<T, ID> repository) {
        this.repository = repository;
    }

    @Override
    public boolean isIdOfEntity(ID id, T entity) {
        if (id == null || entity == null) {
            return false;
        }
        return Objects.equals(id, getId(entity));
    }

    /**
     * 获取实体的ID
     *
     * @param entity 实体
     * @return 实体的ID
     */
    public abstract ID getId(T entity);

    @Override
    @CacheEvict(key = "getTargetClass() + getMethodName()", allEntries = true)
    public Iterable<T> saveAll(Iterable<T> val) {
        return repository.saveAll(val);
    }

    @Override
    @CacheEvict(key = "getTargetClass() + getMethodName() + getTarget().getId(#val)")
    public void delete(T val) {
        repository.delete(val);
    }

    @Override
    @CacheEvict(key = "getTargetClass() + getMethodName() + #id")
    public void deleteById(ID id) {
        repository.deleteById(id);
    }

    @Override
    @CacheEvict(key = "getTargetClass() + getMethodName()", allEntries = true)
    public void deleteAll(Iterable<T> entities) {
        repository.deleteAll(entities);
    }

    @Override
    @Cacheable(key = "getTargetClass() + getMethodName() + #id", unless = "#result == null")
    public Optional<T> getById(ID id) {
        return repository.findById(id);
    }

    @Override
    public Iterable<T> getAllById(Iterable<ID> ids) {
        return repository.findAllById(ids);
    }

    @Override
    public Iterable<T> getAllByExample(T example) {
        return repository.findAll(Example.of(example));
    }

    @Override
    public boolean existById(ID id) {
        return repository.existsById(id);
    }

    @Override
    public Iterable<T> getAll() {
        return repository.findAll();
    }
}
