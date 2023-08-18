package nohi.demo.mp.dt.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nohi.demo.common.das.JpaCRUDService;
import nohi.demo.common.das.JpaDAO;
import nohi.demo.mp.dt.dao.jpa.COrgDao;
import nohi.demo.mp.dt.entity.jpa.COrg;
import nohi.demo.mp.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 机构表 服务实现类
 * </p>
 *
 * @author nohi
 * @date 2021-01-17
 */
@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class COrgService extends JpaCRUDService<COrg, String> {
    private final COrgDao cOrgDao;

    @Override
    public JpaDAO<COrg, String> getDAO(){
        return cOrgDao;
    }

    public List<COrg> list(COrg info) {
        log.debug("查询条件:{}", JsonUtils.toJson(info));

        Sort sort =  Sort.by(Sort.Direction.ASC, "orgNo");
        COrg query = new COrg();
        if (StringUtils.isNotBlank(info.getOrgNo())) {
            query.setOrgNo(info.getOrgNo());
        }
        if (StringUtils.isNotBlank(info.getOrgName())) {
            query.setOrgName(info.getOrgName());
        }
        if (StringUtils.isNotBlank(info.getParOrgNo())) {
            query.setParOrgNo(info.getParOrgNo());
        }
        if (StringUtils.isNotBlank(info.getOrgLvl())) {
            query.setOrgLvl(info.getOrgLvl());
        }
        if (StringUtils.isNotBlank(info.getProv())) {
            query.setProv(info.getProv());
        }
        if (StringUtils.isNotBlank(info.getCity())) {
            query.setCity(info.getCity());
        }
        if (StringUtils.isNotBlank(info.getRegion())) {
            query.setRegion(info.getRegion());
        }
        return this.findByExample(query, sort);
    }
}
