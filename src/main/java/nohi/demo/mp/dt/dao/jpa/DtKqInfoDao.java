package nohi.demo.mp.dt.dao.jpa;

import nohi.demo.mp.dt.entity.jpa.DtKqInfo;
import nohi.demo.common.das.JpaDAO;

import javax.transaction.Transactional;
import java.util.List;

/**
 * <p>
 * 考勤数据 Mapper 接口
 * </p>
 *
 * @author nohi
 * @date 2021-01-14
 */
public interface DtKqInfoDao extends JpaDAO<DtKqInfo, String> {

    @Transactional
    Integer deleteByIdIn(List<String> idList);
}
