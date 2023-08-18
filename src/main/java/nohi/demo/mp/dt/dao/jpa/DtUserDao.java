package nohi.demo.mp.dt.dao.jpa;

import nohi.demo.common.das.JpaDAO;
import nohi.demo.mp.dt.entity.jpa.DtUser;

/**
 * <p>
 * 人员表 Mapper 接口
 * </p>
 *
 * @author nohi
 * @date 2021-01-05
 */
public interface DtUserDao extends JpaDAO<DtUser, String> {

    DtUser getOneByDtUserid(String dtUserid);

}
