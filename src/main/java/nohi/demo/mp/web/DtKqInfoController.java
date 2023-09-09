package nohi.demo.mp.web;

import com.alibaba.fastjson.JSONObject;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import nohi.demo.common.tx.BaseRequest;
import nohi.demo.common.tx.BaseResponse;
import nohi.demo.mp.dt.entity.jpa.DtKqInfo;
import nohi.demo.mp.dt.service.kaoqing.DtKqInfoService;
import nohi.demo.mp.dto.mp.KqQueryReq;
import nohi.demo.mp.dto.mp.kq.UserKqInfoDTO;
import nohi.demo.mp.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

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
    @PostMapping("getKq")
    public BaseResponse getKqAll(@RequestBody KqQueryReq req) {
        return dtKqInfoService.getKqAll(req);
    }

    @Operation(method = "synUserKq", summary = "刷新员工考勤信息")
    @PostMapping("synUserKq")
    public BaseResponse synUserKq(@RequestBody BaseRequest<KqQueryReq> req) {
        dtKqInfoService.synUserKq(req.getData());
        return dtKqInfoService.synUserKqDetail(req.getData());
    }

    @Operation(method = "synUserKqDetail", summary = "刷新员工考勤详情信息")
    @PostMapping("synUserKqDetail")
    public BaseResponse synUserKqDetail(@RequestBody BaseRequest<KqQueryReq> req) {
        return dtKqInfoService.synUserKqDetail(req.getData());
    }

    @Operation(method = "findUserKq", summary = "查询考勤信息")
    @PostMapping("findUserKq")
    public BaseResponse findUserKq(@RequestBody KqQueryReq req) {
        return dtKqInfoService.findUserKq(req);
    }

    @Operation(method = "dingTalkPageList", summary = "考勤数据列表，分页")
    @PostMapping("dingTalkPageList")
    public BaseResponse<List<UserKqInfoDTO>> dingTalkPageList(@RequestBody BaseRequest<KqQueryReq> info) {
        log.info("请求:{}", JSONObject.toJSONString(info));
        List<UserKqInfoDTO> list = dtKqInfoService.dingTalkPageList(info.getPage(), info.getData());
        BaseResponse<List<UserKqInfoDTO>> response = BaseResponse.suc("操作成功");
        response.setPage(info.getPage());
        response.setData(list);
        return response;
    }

    @Operation(method = "empWorkSheetDetail", summary = "查询考勤信息")
    @PostMapping("empWorkSheetDetail")
    public BaseResponse empWorkSheetDetail(@RequestBody BaseRequest<KqQueryReq> info) {
        List<Map> list = dtKqInfoService.empWorkSheetDetail(info.getData());
        BaseResponse<List<Map>> response = BaseResponse.suc("操作成功");
        response.setData(list);
        return response;
    }

    @Operation(method = "exportSheet", summary = "导出项目工时信息")
    @PostMapping("exportSheet")
    public void exportSheet(@RequestBody BaseRequest<KqQueryReq> request, HttpServletResponse response) {
        log.debug("req:{}", JsonUtils.toJson(request));
        try {
            String outputFile = dtKqInfoService.exportProjectSheet(request.getData());
            File file = new File(outputFile);
            //
            log.debug("filename:{}", file.getName());
            String fileName = new String(file.getName().getBytes("gbk"), "ISO-8859-1");
            log.debug("filename:{}", fileName);
            response.setContentType("application/force-download");
            response.addHeader("Content-Disposition", "attachment;fileName=" + new String(fileName.getBytes("utf-8"),"ISO8859-1"));

            byte[] b = new byte[1024 * 1024];
            int i;
            try (InputStream is = new FileInputStream(file);
                 ServletOutputStream out = response.getOutputStream()) {
                while ((i = is.read(b)) > 0) {
                    out.write(b, 0, i);
                }
                out.flush();
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        } catch(Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
