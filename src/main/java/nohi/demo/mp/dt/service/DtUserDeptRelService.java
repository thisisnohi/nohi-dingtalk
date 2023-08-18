package nohi.demo.mp.dt.service;

import nohi.demo.mp.dt.dao.jpa.DtUserDeptRelDao;
import nohi.demo.mp.dt.entity.jpa.DtUserDeptRel;
import nohi.demo.common.das.JpaDAO;
import nohi.demo.common.das.JpaCRUDService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 部门人员关联表 服务实现类
 * </p>
 *
 * @author nohi
 * @date 2021-01-05
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DtUserDeptRelService extends JpaCRUDService<DtUserDeptRel, String> {
    private final DtUserDeptRelDao dtUserDeptRelDao;

    @Override
    public JpaDAO<DtUserDeptRel, String> getDAO(){
        return dtUserDeptRelDao;
    }
}
