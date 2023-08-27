package nohi.demo.mp.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import nohi.demo.common.tx.BaseResponse;
import nohi.demo.mp.dt.service.DtVocationStatusService;
import nohi.demo.mp.dt.service.DtVocationTypeService;
import nohi.demo.mp.dto.mp.KqQueryReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author NOHI
 * @program: nohi-dd-miniprogram-server
 * @description:
 * @create 2021-01-03 20:59
 **/
@Tag(name = "dtVocation", description = "假期相关操作")
@RestController
@RequestMapping(value = "dtVocation")
@Slf4j
public class DtVocationController {

    @Autowired
    private DtVocationTypeService service;

    @Autowired
    private DtVocationStatusService dtVocationStatusService;

    @Operation(method = "getTypeAll", summary = "查询假期类型信息")
    @GetMapping("getTypeAll")
    public Object getTypeAll() {
        return service.findAll();
    }

    @Operation(method = "getVocationAll", summary = "查询假请信息")
    @GetMapping("getVocationAll")
    public Object getVocationAll() {
        return dtVocationStatusService.findAll();
    }

    @Operation(method = "refreshVocationType", summary = "刷新假期类型数据")
    @GetMapping("refreshVocationType")
    public BaseResponse refreshVocationType() {
        try {
            return service.refresh();
        } catch (Exception e) {
            log.error("刷新部门异常:{}", e.getMessage(), e);
            return BaseResponse.error("刷新部门异常:" + e.getMessage());
        }
    }

    @Operation(method = "refreshVocationStatus", summary = "刷新员工假请数据")
    @GetMapping("refreshVocationStatus")
    public BaseResponse refreshVocationStatus(KqQueryReq req) {
        return dtVocationStatusService.refreshVocationStatus(req);
    }

    @Operation(method = "findUserVocation", summary = "查询请假信息")
    @PostMapping("findUserVocation")
    public BaseResponse findUserVocation(@RequestBody KqQueryReq req) {
        return dtVocationStatusService.findUserVocation(req);
    }
}
