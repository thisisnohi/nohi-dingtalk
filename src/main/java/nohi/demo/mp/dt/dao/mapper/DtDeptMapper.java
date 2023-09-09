package nohi.demo.mp.dt.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import nohi.demo.common.das.JpaDAO;
import nohi.demo.mp.dt.entity.jpa.DtDept;
import nohi.demo.mp.dto.mp.dept.DeptInfo;
import nohi.demo.mp.dto.mp.dept.DeptQuery;
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
@Mapper
public interface DtDeptMapper extends BaseMapper<DtDept> {

    List<DeptInfo> dtDeptList(Page<?> page, DeptQuery info);
}
