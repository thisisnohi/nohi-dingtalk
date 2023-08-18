package nohi.demo.mp.dt.service;

import com.dingtalk.api.response.OapiDepartmentListResponse;
import com.google.common.collect.Lists;
import com.taobao.api.ApiException;
import lombok.extern.slf4j.Slf4j;
import nohi.demo.common.tx.BaseResponse;
import nohi.demo.mp.dt.dao.jpa.DtDeptDao;
import nohi.demo.mp.dt.entity.jpa.DtDept;
import nohi.demo.common.das.JpaDAO;
import nohi.demo.common.das.JpaCRUDService;
import lombok.RequiredArgsConstructor;
import nohi.demo.mp.service.mp.MpHandler;
import nohi.demo.mp.utils.IdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 组织部门 服务实现类
 * </p>
 *
 * @author nohi
 * @date 2021-01-05
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class DtDeptService extends JpaCRUDService<DtDept, String> {
    private final DtDeptDao dtDeptDao;

    @Autowired
    private MpHandler mpHandler;

    @Override
    public JpaDAO<DtDept, String> getDAO(){
        return dtDeptDao;
    }


    public List<DtDept> listDepts(DtDept info) {
        Sort sort =  Sort.by(Sort.Direction.ASC, "dtDeptId");
        return this.findByExample(info, sort);
    }

    /**
     * 刷新部门
     * @param enNo
     * @return
     */
    public BaseResponse refreshDepts(String enNo) throws ApiException {
        // 获取部门列表
        List<OapiDepartmentListResponse.Department> departmentList = null;
        try {
            departmentList = mpHandler.getMpService().getDepts();
            log.debug("departmentList {}", null == departmentList ? "is null" : departmentList.size());
        } catch (ApiException e) {
            log.error("获取钉钉部门列表异常:{}", e.getMessage(), e);
            throw e;
        }
        if (null == departmentList || departmentList.isEmpty()) {
            return BaseResponse.error("获取钉钉部门数据为空!");
        }
        List<DtDept> depts = Lists.newArrayList();
        // 循环更新部门数据
        departmentList.forEach( item -> {
            // 查询id对应数据
            DtDept info = dtDeptDao.findOneByDtDeptId("" + item.getId());
            // 如果存在则更新、没有则新增
            String id = null;
            if (null != info) {
                id = info.getId();
            } else {
                info = new DtDept();
                id = IdUtils.uuid32();
            }
            info.setId(id);
            info.setDtDeptId(item.getId() + "");
            info.setDtDeptName(item.getName());
            info.setDtParDeptId(item.getParentid() + "");
            depts.add(info);

        });
        // 保存
        dtDeptDao.saveAll(depts);
        return BaseResponse.suc("操作成功");
    }
}
