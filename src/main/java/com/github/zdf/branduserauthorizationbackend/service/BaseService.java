package com.github.zdf.branduserauthorizationbackend.service;

import java.util.Optional;

public interface BaseService<T, ID> {
    /**
     * 保存所有的实体
     *
     * @param val 实体集
     * @return 保存后的实体集，若ID为空则会添加ID
     */
    Iterable<T> saveAll(Iterable<T> val);

    /**
     * 删除val
     *
     * @param val 要删除的实体
     */
    void delete(T val);

    /**
     * 删除ID为id的数据
     *
     * @param id 数据Id
     */
    void deleteById(ID id);

    /**
     * 删除ID在ids集合中的所有实体
     *
     * @param ids 需要删除的实体的ID集合
     */
    void deleteAll(Iterable<T> ids);

    /**
     * 获取ID为id的数据
     *
     * @param id 数据ID
     * @return 若不存在，则返回null。否则返回数据对象
     */
    Optional<T> getById(ID id);

    /**
     * 获取所有ID为ids集合中的id的实体
     *
     * @param ids ID集合
     * @return 所有ID在ids集合的实体
     */
    Iterable<T> getAllById(Iterable<ID> ids);

    /**
     * 根据example中非null的字段，查询符合的所有实体
     *
     * @param example 查询条件
     * @return 符合example条件的所有实体
     */
    Iterable<T> getAllByExample(T example);

    /**
     * 查看是否存在
     *
     * @param id ID
     * @return 若存在则为true，否则为false
     */
    boolean existById(ID id);

    /**
     * 获取所有对象
     *
     * @return 所有对象
     */
    Iterable<T> getAll();

    /**
     * 查看id是否是entity的id相等
     *
     * @param id     ID
     * @param entity 实体
     * @return true则是，否则不是
     */
    boolean isIdOfEntity(ID id, T entity);
}
