package nohi.demo.mp.dt.service;

import nohi.demo.common.das.JpaCRUDService;
import nohi.demo.mp.dt.dao.jpa.DtAprLeaveDao;
import nohi.demo.mp.dt.entity.jpa.DtAprLeave;
import nohi.demo.common.das.JpaDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 请假信息 服务实现类
 * </p>
 *
 * @author nohi
 * @date 2021-01-21
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DtAprLeaveService extends JpaCRUDService<DtAprLeave, String> {
    private final DtAprLeaveDao dtAprLeaveDao;

    @Override
    public JpaDAO<DtAprLeave, String> getDAO(){
        return dtAprLeaveDao;
    }
}
