package nohi.demo.mp.dt.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dingtalk.api.response.OapiDepartmentListResponse;
import com.google.common.collect.Lists;
import com.taobao.api.ApiException;
import lombok.extern.slf4j.Slf4j;
import nohi.demo.common.tx.BasePage;
import nohi.demo.common.tx.BaseResponse;
import nohi.demo.common.tx.PageUtils;
import nohi.demo.mp.dt.dao.jpa.DtDeptDao;
import nohi.demo.mp.dt.dao.mapper.DtDeptMapper;
import nohi.demo.mp.dt.entity.jpa.DtDept;
import nohi.demo.common.das.JpaDAO;
import nohi.demo.common.das.JpaCRUDService;
import lombok.RequiredArgsConstructor;
import nohi.demo.mp.dto.mp.dept.DeptInfo;
import nohi.demo.mp.dto.mp.dept.DeptQuery;
import nohi.demo.mp.dto.mp.dept.DeptTree;
import nohi.demo.mp.service.mp.MpHandler;
import nohi.demo.mp.utils.IdUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
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
    private final DtDeptMapper dtDeptMapper;
    private final MpHandler mpHandler;


    @Override
    public JpaDAO<DtDept, String> getDAO(){
        return dtDeptDao;
    }

    /**
     * 分页查询
     * @param basePage 分页信息
     * @param info  查询条件
     * @return 结果列表
     */
    public List<DeptInfo> dtDeptList(BasePage basePage, DeptQuery info) {
        // 不进行 count sql 优化，解决 MP 无法自动优化 SQL 问题，这时候你需要自己查询 count 部分
        // page.setOptimizeCountSql(false);
        // 当 total 为小于 0 或者设置 setSearchCount(false) 分页插件不会进行 count 查询
        // 要点!! 分页返回的对象与传入的对象是同一个
        Page<DeptInfo> page = PageUtils.buildPage(basePage, DeptInfo.class);
        List<DeptInfo> list = dtDeptMapper.dtDeptList(page, info);
        PageUtils.buildPage(basePage, page);
        return list;
    }

    /**
     * 部门树
     * @param info
     * @return
     */
    public List<DeptTree> deptTree(DeptQuery info) {
        log.info("请求参数:{}", JSONObject.toJSONString(info));
        if (null == info) {
            info = new DeptQuery();
        }
        Page page = Page.of(0, 1000);
        page.setSearchCount(false);

        List<DeptInfo> pageDate = dtDeptMapper.dtDeptList(page, info);;
        return buildTree("", pageDate);
    }

    public List<DeptTree> buildTree(String parId, List<DeptInfo> list){
        List<DeptTree> tree = Lists.newArrayList();

        // 根节点
        if (StringUtils.isBlank(parId)) {
            for (DeptInfo item : list) {
                if (StringUtils.isBlank(item.getDtParDeptId())) {
                    DeptTree deptTree = new DeptTree();
                    BeanUtils.copyProperties(item, deptTree);
                    tree.add(deptTree);

                    // 添加子部门
                    deptTree.setChildren(buildTree(deptTree.getDtDeptId(), list));
                }
            }
        } else {
            for (DeptInfo item : list) {
                // 匹配子部门
                if (parId.equals(item.getDtParDeptId())) {
                    DeptTree deptTree = new DeptTree();
                    BeanUtils.copyProperties(item, deptTree);
                    tree.add(deptTree);
                    // 添加子部门
                    deptTree.setChildren(buildTree(deptTree.getDtDeptId(), list));
                }
            }
        }
        return tree;
    }

    public List<DtDept> listDepts(DtDept info) {
        Sort sort =  Sort.by(Sort.Direction.ASC, "dtDeptId");
        return this.findByExample(info, sort);
    }/**/

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
