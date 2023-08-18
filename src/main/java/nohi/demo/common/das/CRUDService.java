package nohi.demo.common.das;

import java.io.Serializable;
import java.util.List;

/**
 * @author NOHI
 * @program: nohi-dd-miniprogram-server
 * @description:
 * @create 2021-01-05 21:32
 **/
public interface CRUDService<E, K extends Serializable> {
    /**
     * 保存实体数据
     * @param entity 实体数据
     * @return 实体数据
     */
    E save(E entity);

    /**
     * 保存实体数据集合
     * @param entities 实体数据集合
     * @return 实体数据
     */
    List<E> saveAll(List<E> entities);

    /**
     * 删除数据
     * @param id 主键
     */
    void delete(K id);

    /**
     * 根据 ID 查找单条数据
     * @param id 主键
     * @return 实体数据
     */
    E findOne(K id);

    /**
     * 查询所有数据
     * @return 实体数据集合
     */
    List<E> findAll();

    /**
     * 统计数量
     * @return 数量
     */
    Long count();

}
