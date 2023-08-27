package nohi.demo.mp.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "dtKq", description = "dt考勤")
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

    @Operation(method = "getKq", summary = "刷新部门")
    @GetMapping("getKq")
    public BaseResponse getKqAll(KqQueryReq req) {
        return dtKqInfoService.getKqAll(req);
    }

    @Operation(method = "synUserKq", summary = "刷新员工考勤信息")
    @GetMapping("synUserKq")
    public BaseResponse synUserKq(KqQueryReq req) {
        return dtKqInfoService.synUserKq(req);
    }
    @Operation(method = "synUserKqDetail", summary = "刷新员工考勤详情信息")
    @GetMapping("synUserKqDetail")
    public BaseResponse synUserKqDetail(KqQueryReq req) {
        return dtKqInfoService.synUserKqDetail(req);
    }

    @Operation(method = "findUserKq", summary = "查询考勤信息")
    @PostMapping("findUserKq")
    public BaseResponse findUserKq(@RequestBody KqQueryReq req) {
        return dtKqInfoService.findUserKq(req);
    }
}
