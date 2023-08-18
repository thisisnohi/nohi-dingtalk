package nohi.demo.mp.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import nohi.demo.common.tx.BaseResponse;
import nohi.demo.mp.dt.entity.jpa.DtKqInfo;
import nohi.demo.mp.dt.service.DtKqInfoService;
import nohi.demo.mp.dto.mp.KqQueryReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author NOHI
 * @program: nohi-dd-miniprogram-server
 * @description:
 * @create 2021-01-03 20:59
 **/
@Api(value = "dtKq", tags = "dtKq", description = "dt考勤")
@RestController
@RequestMapping(value = "dtKq")
@Slf4j
public class DtKqInfoController {

    @Autowired
    private DtKqInfoService dtKqInfoService;

    @GetMapping("getAll")
    public Object getAll() {
        return dtKqInfoService.findAll();
    }

    @PostMapping("list")
    public List list(@RequestBody DtKqInfo info) {
        return dtKqInfoService.list(info);
    }

    @ApiOperation(value = "刷新部门", notes = "")
    @GetMapping("getKq")
    public BaseResponse getKqAll(KqQueryReq req) {
        return dtKqInfoService.getKqAll(req);
    }

    @ApiOperation(value = "刷新员工考勤信息", notes = "")
    @GetMapping("synUserKq")
    public BaseResponse synUserKq(KqQueryReq req) {
        return dtKqInfoService.synUserKq(req);
    }
    @ApiOperation(value = "刷新员工考勤详情信息", notes = "")
    @GetMapping("synUserKqDetail")
    public BaseResponse synUserKqDetail(KqQueryReq req) {
        return dtKqInfoService.synUserKqDetail(req);
    }

    @ApiOperation(value = "查询考勤信息", notes = "")
    @PostMapping("findUserKq")
    public BaseResponse findUserKq(@RequestBody KqQueryReq req) {
        return dtKqInfoService.findUserKq(req);
    }
}
