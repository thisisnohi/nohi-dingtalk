package nohi.demo.mp.dt.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import nohi.demo.mp.dt.entity.jpa.DtKqInfo;
import nohi.demo.mp.dto.mp.KqQueryReq;
import nohi.demo.mp.dto.mp.dept.DeptInfo;
import nohi.demo.mp.dto.mp.kq.UserKqInfoDTO;
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
public interface DtKqInfoMapper extends BaseMapper<DtKqInfo> {

    List<UserKqInfoDTO> dingTalkList(Page<?> page, KqQueryReq info);
}
