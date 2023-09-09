package nohi.demo.mp.dt.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import nohi.demo.mp.dt.entity.jpa.DtDept;
import nohi.demo.mp.dt.entity.jpa.DtUser;
import nohi.demo.mp.dto.mp.dept.DeptInfo;
import nohi.demo.mp.dto.mp.dept.DeptQuery;
import nohi.demo.mp.dto.mp.user.UserQuery;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 组织部门 Mapper 接口
 * </p>
 *
 * @author nohi
 * @date 2021-01-05
 */
public interface DtUserMapper extends BaseMapper<DtUser> {
    List<DtUser> userList(Page<?> page, UserQuery info);
}
