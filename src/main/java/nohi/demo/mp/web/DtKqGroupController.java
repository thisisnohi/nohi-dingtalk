package nohi.demo.mp.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import nohi.demo.common.tx.BaseResponse;
import nohi.demo.mp.dt.entity.jpa.DtKqGroup;
import nohi.demo.mp.dt.entity.jpa.DtUser;
import nohi.demo.mp.dt.service.DtKqGroupService;
import nohi.demo.mp.dt.service.DtUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author NOHI
 * @program: nohi-dd-miniprogram-server
 * @description:
 * @create 2021-01-03 20:59
 **/
@Api(value = "dtGroup", tags = "dtGroup", description = "dt考勤组")
@RestController
@RequestMapping(value = "dtGroup")
@Slf4j
public class DtKqGroupController {

    @Autowired
    private DtKqGroupService dtKqGroupService;

    @GetMapping("getAll")
    public Object getAll() {
        return dtKqGroupService.findAll();
    }

    @PostMapping("list")
    public List list(@RequestBody DtKqGroup info) {
        return dtKqGroupService.listDepts(info);
    }

    @ApiOperation(value = "刷新考勤组", notes = "")
    @GetMapping("refresh")
    public BaseResponse refresh() {
        try {
            return dtKqGroupService.refresh();
        } catch (Exception e) {
            log.error("刷新考勤组异常:{}", e.getMessage(), e);
            return BaseResponse.error("刷新考勤组异常:" + e.getMessage());
        }
    }


}
