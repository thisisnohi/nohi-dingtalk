package nohi.demo.mp.dt.dao.jpa;

import nohi.demo.mp.dt.entity.jpa.DtKqDetail;
import nohi.demo.common.das.JpaDAO;

import javax.transaction.Transactional;
import java.util.List;

/**
 * <p>
 * 考勤打卡详情 Mapper 接口
 * </p>
 *
 * @author nohi
 * @date 2021-01-14
 */
public interface DtKqDetailDao extends JpaDAO<DtKqDetail, String> {
    @Transactional
    Integer deleteByIdIn(List<String> idList);
}
