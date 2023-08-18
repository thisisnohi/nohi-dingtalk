package nohi.demo.mp.dt.dao.jpa;

import nohi.demo.common.das.JpaDAO;
import nohi.demo.mp.dt.entity.jpa.DtVocationStatus;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  假期状态
 * </p>
 *
 * @author nohi
 * @date 2021-01-05
 */
public interface DtVocationStatusDao extends JpaDAO<DtVocationStatus, String> {
    @Query(value = " select  " +
            "   du.DT_USERNAME  " +
            " , vt.LEAVE_NAME  " +
            " , date_format(vs.START_TIME, '%Y-%m-%d') WORK_DATE  " +
            " , case vs.DURATION_UNIT  " +
            "       when 'percent_day' then DURATION_PERCENT / 100  " +
            "       when 'percent_hour' then DURATION_PERCENT / 100 / 8  " +
            "    end '请假(天)'  " +
            " , vs.id, vs.duration_unit, vs.duration_percent,  vs.start_time,vs.end_time, vs.userid, vs.leave_code  " +
            "from DT_VOCATION_STATUS  vs  " +
            "left join dt_vocation_type vt on vs.LEAVE_CODE = vt.LEAVE_CODE  " +
            "left join DT_USER du on vs.USERID = du.DT_USERID  " +
            "  " +
            "order by WORK_DATE, du.DT_USERNAME ", nativeQuery = true)
    List<Map<String,String>> findUserVocation();
}
