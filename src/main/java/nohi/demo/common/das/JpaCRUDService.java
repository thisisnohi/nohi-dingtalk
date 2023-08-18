package nohi.demo.common.das;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

/**
 * @author NOHI
 * @program: nohi-dd-miniprogram-server
 * @description:
 * @create 2021-01-05 21:34
 **/
public abstract class JpaCRUDService<E, K extends Serializable> implements CRUDService<E, K> {

    /**
     * 获取当前实体 JPA DAO
     * @return 当前实体 JPA DAO
     */
    public abstract JpaDAO<E, K> getDAO();

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = Exception.class)
    public E save(E entity) {
        return getDAO().save(entity);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = Exception.class)
    public List<E> saveAll(List<E> entities) {
        return getDAO().saveAll(entities);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = Exception.class)
    public void delete(K id) {
        getDAO().deleteById(id);
    }

    @Override
    public E findOne(K id) {
        return getDAO().findById(id).orElse(null);
    }


    @Override
    public List<E> findAll() {
        return getDAO().findAll();
    }


    @Override
    public Long count() {
        return getDAO().count();
    }

    public List<E> findByExample(E entity) {
        Example<E> example = Example.of(entity);
        return getDAO().findAll(example);
    }

    public List<E> findByExample(E entity, Sort sort) {
        Example<E> example = Example.of(entity);
        return getDAO().findAll(example, sort);
    }

}
