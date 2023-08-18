package nohi.demo.mp.dt.service;

import lombok.RequiredArgsConstructor;
import nohi.demo.common.das.JpaCRUDService;
import nohi.demo.common.das.JpaDAO;
import nohi.demo.mp.dt.dao.jpa.DtKqDetailDao;
import nohi.demo.mp.dt.entity.jpa.DtKqDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 考勤打卡详情 服务实现类
 * </p>
 *
 * @author nohi
 * @date 2021-01-14
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DtKqDetailService extends JpaCRUDService<DtKqDetail, String> {
    private final DtKqDetailDao dtKqDetailDao;

    @Override
    public JpaDAO<DtKqDetail, String> getDAO(){
        return dtKqDetailDao;
    }
}
