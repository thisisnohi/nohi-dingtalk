package nohi.demo.mp.dt.dao.jpa;

import jakarta.transaction.Transactional;
import nohi.demo.common.das.JpaDAO;
import nohi.demo.mp.dt.entity.jpa.DtKqInfo;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

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

    @Query(value = " select du.DT_USERNAME, kq.* " +
            " from " +
            " ( " +
            "    select DT_USERID, WORK_DATE, min(USERCHECK_TIME) UP, max(USERCHECK_TIME) down " +
            "    from DT_KQ_INFO " +
            "    group by DT_USERID, WORK_DATE " +
            " ) KQ " +
            " left join DT_USER du on KQ.DT_USERID = du.DT_USERID" +
            " order by WORK_DATE,DT_USERNAME,  up ", nativeQuery = true)
    List<Map<String,String>>  findUserKq();
}
