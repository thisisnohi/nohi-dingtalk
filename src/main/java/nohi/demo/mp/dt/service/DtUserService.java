package nohi.demo.mp.dt.service;

import com.dingtalk.api.response.OapiV2UserListResponse;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.taobao.api.ApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nohi.demo.common.das.JpaCRUDService;
import nohi.demo.common.das.JpaDAO;
import nohi.demo.common.tx.BaseResponse;
import nohi.demo.mp.consts.CommonConsts;
import nohi.demo.mp.dt.dao.jpa.DtDeptDao;
import nohi.demo.mp.dt.dao.jpa.DtUserDao;
import nohi.demo.mp.dt.dao.jpa.DtUserDeptRelDao;
import nohi.demo.mp.dt.entity.jpa.DtDept;
import nohi.demo.mp.dt.entity.jpa.DtUser;
import nohi.demo.mp.dt.entity.jpa.DtUserDeptRel;
import nohi.demo.mp.service.mp.MpHandler;
import nohi.demo.mp.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p>
 * 人员表 服务实现类
 * </p>
 *
 * @author nohi
 * @date 2021-01-05
 */
@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DtUserService extends JpaCRUDService<DtUser, String> {
    private final DtUserDao dtUserDao;
    private final DtDeptDao dtDeptDao;
    private final DtUserDeptRelDao dtUserDeptRelDao;

    @Autowired
    private MpHandler mpHandler;

    @Override
    public JpaDAO<DtUser, String> getDAO(){
        return dtUserDao;
    }

    public List<DtUser> listDepts(DtUser info) {
        Sort sort =  Sort.by(Sort.Direction.ASC, "dtUserid");
        return this.findByExample(info, sort);
    }

    /**
     * 刷新用户
     * @param enNo
     * @return
     */
    public BaseResponse refresh(String enNo) throws ApiException {
        // 获取部门列表
        List<DtDept> dtdeptList = dtDeptDao.findAll();

        List<BaseResponse> optRespList = Lists.newArrayList();
        AtomicInteger error = new AtomicInteger();
        Set<String> errorSet = Sets.newHashSet();
        // 按部门同步
        dtdeptList.forEach( dept -> {
            BaseResponse response = this.refreshByDept(dept.getDtDeptName(), Long.valueOf(dept.getDtDeptId()));
            optRespList.add(response);
            if (!BaseResponse.ResCode.SUC.equals(response.getResCode())) {
                error.getAndIncrement();
                errorSet.add(response.getResMsg());
            }
        });

        int total = dtdeptList.size();
        String msg = "部门总数[" + total + "]";
        if (error.get() > 0) {
            msg += "失败数[" + error.get() + "]" + Joiner.on(",").join(errorSet);
            return BaseResponse.error(msg);
        }
        return BaseResponse.suc(msg);
    }

    /**
     * 按部门刷新用户
     * @param deptName
     * @param depIt
     * @return
     */
    public BaseResponse refreshByDept(String deptName, Long depIt) {
        String title = String.format("刷新部门[%s-%s]用户", depIt, deptName);
        try {
            List<OapiV2UserListResponse.ListUserResponse> userList =  mpHandler.getMpService().getDeptsAllUser(Long.valueOf(depIt));
            log.debug("部门[{}]钉钉用户:{}", deptName, JsonUtils.toJson(userList));
            if (null == userList || userList.isEmpty()) {
                return BaseResponse.newCode("01", "没有用户列表");
            }
            // 循环用户列表
            userList.forEach(user -> {
                this.synUserInfo(String.valueOf(depIt), deptName, user);
            });
        } catch (ApiException e) {
            log.error("{}异常", title, e.getMessage(), e);
            return BaseResponse.error(e.getMessage());
        }
        return BaseResponse.suc(title + "成功");
    }

    /**
     * 同步用户信息
     */
    public void synUserInfo(String deptId, String deptName, OapiV2UserListResponse.ListUserResponse user) {
        String title = String.format("刷新部门[%s-%s]用户[%s-%s]", deptId, deptName, user.getUserid(), user.getName());
        // 查询用户信息
        String id = null;
        DtUser dtUser = dtUserDao.getOneByDtUserid(user.getUserid());
        if (null == dtUser || StringUtils.isBlank(dtUser.getId())) {
            dtUser = new DtUser();
        } else {
            id = dtUser.getId();
        }
        dtUser.setDtUserid(user.getUserid());
        dtUser.setDtUsername(user.getName());
        dtUser.setDtUnionid(user.getUnionid());
        dtUser.setDtActive(user.getActive() ? CommonConsts.YesOrNo.YES.getKey() : CommonConsts.YesOrNo.NO.getKey());
        dtUser.setGetPriInd(CommonConsts.YesOrNo.YES.getKey());

        dtUser = dtUserDao.save(dtUser);
        log.debug("{}, dtUser.id:{}", title, dtUser.getId());
        // 同步部门信息
        DtUserDeptRel rel = dtUserDeptRelDao.findOneByDtUseridAndDtDeptId(dtUser.getDtUserid(), deptId);
        if (null == rel || StringUtils.isBlank(rel.getId())) {
            rel = new DtUserDeptRel();
            rel.setDtDeptId(deptId);
            rel.setDtUserid(dtUser.getDtUserid());
            // 保存员人部门关系
            dtUserDeptRelDao.save(rel);
        }
    }

    /**
     * 根据部门Id，查询用户列表
     * @param dtDeptId
     * @return
     */
    public List<DtUserDeptRel> findUserListByDeptId(String dtDeptId) {
        return dtUserDeptRelDao.findListByDtDeptId(dtDeptId);
    }
}
