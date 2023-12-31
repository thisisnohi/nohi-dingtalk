//package nohi.demo.mp.service.excel;
//
//import java.io.*;
//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//
//import nohi.doc.DocConsts;
//import nohi.doc.meta.DocBlockMeta;
//import nohi.doc.meta.DocColMeta;
//import nohi.doc.meta.DocSheetMeta;
//import nohi.doc.meta.DocTemplateMeta;
//import nohi.doc.service.IDocService;
//import nohi.doc.service.NOHIDocServices;
//import nohi.doc.service.XmlParase;
//import nohi.doc.utils.ExcelUtils;
//import nohi.doc.vo.DocVO;
//import nohi.ftp.service.FtpServer;
//import nohi.utils.Clazz;
//import nohi.utils.DocCommonUtils;
//import org.apache.log4j.Logger;
//import org.apache.poi.ss.usermodel.*;
//
//
///**
// * Excel服务类
// * @author NOHI
// * @create date: 2013-2-14
// */
//public class ExcelXlsxService implements IDocService {
//    private Logger log = Logger.getLogger(this.getClass());
//    private Workbook templatebook = null;//模板文件
//    private Sheet templateSheet = null; //模板文件Sheet
//
//    private boolean valueFailExit = true; //value解析导演是否退出
//
//    Workbook hwb = null;
//    String key = null;
//    DocTemplateMeta template; //模板文件
//
//    Object dataVO ;//导入数据
//
//    public DocVO exportDoc(DocVO doc) throws Exception {
//        //0 检验输入数据
//        if (null == doc) {
//            log.info("输入数据为空");
//            return doc;
//        }
//        log.debug("docType: " + doc.getDocType() + " ,docID:" + doc.getDocId());
//
//        //1 取得文档模板
//        template = NOHIDocServices.getTemplate(doc.getDocId());
//        if (null == template) {
//            log.info("文档模板为空,没有取得文档ID[" + doc.getDocId()+ "]对应的配置文件");
//            throw new Exception("文档模板为空,没有取得文档ID[" + doc.getDocId()+ "]对应的配置文件");
//        }
//
//        InputStream is = null;
//        dataVO = doc.getDataVO();
//
//        log.info( "template.getTemplate():" + template.getTemplate() );
//        //2,读取Excel模板
//        is = XmlParase.class.getClassLoader().getResourceAsStream(template.getTemplate());
//        try {
//            templatebook = WorkbookFactory.create(is);
//        } catch (IOException e) {
//            log.error(e.getMessage(),e);
//            throw new Exception("读取模板文件[" + template.getTemplate() + "]失败",e);
//        }
//
//        //创建新的EXCEL
//        hwb = templatebook;
//
//        //导出数据
//        try {
//            int templateSheetSize = 1;
//
//            //备份模板
//            templateSheetSize = templatebook.getNumberOfSheets();
//            log.debug("template.getTemplateSheetSize():" + template.getTemplateSheetSize());
//
//            for (int i = 1 ; i <= templateSheetSize ;i++) {
//                hwb.setSheetName(i-1, hwb.getSheetName(i-1) + "_bak");
//            }
//            exportToSheet(dataVO);
//
//            //删除备份的模板
//            for (int i = 1 ; i <= templateSheetSize ;i++) {
//                hwb.removeSheetAt(0);
//            }
//        } catch (Exception e) {
//            log.error(e.getMessage(),e);
//            throw new Exception("生成数据失败",e);
//        }
//
//
//        //生成文件名
//        if (null == doc.getDocName() || "".equals(doc.getDocName().trim())) {
//            String docVersion = doc.getDocVersion();
//            if (null == docVersion || "".equals(docVersion.trim())) {
//                docVersion = DocCommonUtils.getCurrentTimeStr("yyyyMMddHHmmssSSS");
//            }
//
//            StringBuffer filename = new StringBuffer();
//            filename.append(template.getId())
//                    .append("_")
//                    .append(template.getName())
//                    .append(docVersion);
//
//            if (template.getTemplate().endsWith( "xlsx" )) {
//                filename.append(".xlsx");
//            }else{
//                filename.append(".xls");
//            }
//
//            ;
//            doc.setDocName(filename.toString());
//        }
//
//        //保存文件
//        OutputStream os = null;
//        try {
//            if (DocConsts.NOHI_DOC_STORE_FILE_TYPE_LOCAL.equals(NOHIDocServices.getStoreFileType())) {
//                doc.setFilePath(getStoreFileName(doc));
//                os = new  FileOutputStream(doc.getFilePath());
//            }else {
//                os = storeFile2FTPserver(doc);
//            }
//            hwb.write(os);
//        } catch (Exception e) {
//            log.error(e.getMessage(),e);
//            throw new Exception("保存数据失败",e);
//        }finally{
//            if (is != null){
//                try {
//                    is.close();
//                } catch (IOException e) {
//                    log.error(e);
//                }
//            }
//
//            if (null != os) {
//                try {
//                    os.close();
//                } catch (IOException e) {
//                    log.error(e);
//                }
//            }
//            closeFtp();
//        }
//
//        return doc;
//    }
//
//    private void exportToSheet(Object data) throws Exception{
//        //解析每一个sheet
//        List<DocSheetMeta> sheetList = template.getSheetList();
//        int i = 0;
//        for(Iterator<DocSheetMeta> it = sheetList.iterator();it.hasNext();){
//            DocSheetMeta sheet = it.next();
//            key = sheet.getName();
//            String sheetName = sheet.getDynamicsheetname();
//            //设置sheet名称
//            if (null != sheetName && !"".equals(sheetName.trim())) {
//                sheetName = (String)Clazz.getValue(data, sheetName);
//            }else {
//                sheetName = key;
//            }
//
//            if (null == sheetName || "".equals(sheetName.trim())) {
//                if (DocConsts.EXCEL_SHEET_REPEAT.equals(repeatSheet)) {
//                    sheetName = sheetName + "_"  + i;
//                }else {
//                    sheetName = key;
//                }
//            }
//            i++;
//
//            log.debug("sheetName: " + sheetName);
//            //2,取得sheet
//            Sheet mHSSFSheet = 	hwb.createSheet(sheetName);
//            templateSheet = templatebook.getSheet(key + "_bak");
//
//            if (null == templateSheet) {
//                log.error("没有找到[" + key + "]对应的sheet");
//                throw new Exception("没有找到[" + key + "]对应的sheet");
//            }
//
//            //根据模板设置每一列的宽
//            for (int cellIndex = 0 ; cellIndex < ExcelUtils.getRow(templateSheet, 0).getLastCellNum() ; cellIndex ++) {
//                mHSSFSheet.setColumnWidth(cellIndex, templateSheet.getColumnWidth(cellIndex));
//            }
//
//            //解析每一个块
//            List<DocBlockMeta> blockList = sheet.getBlockList();
//            String lastRowIndex = "0"; //保存上一个块占用的最后一行index
//            for (DocBlockMeta block : blockList) {
//                //放入上一次修改行index
//                block.setLastModifyRowIndex(lastRowIndex);
//                String startRow = block.getRow();
//
//                //System.out.println("startrow:" + startRow + " ,lastRowIndex:" + lastRowIndex);
//                //拷贝所有操作行的数据、及格式
//                if (Integer.valueOf(startRow).compareTo(Integer.valueOf(lastRowIndex) + 1) > 0 || "0".equals(lastRowIndex)) {
//                    //copy模板中未用到的行
//                    int start = Integer.valueOf(lastRowIndex) + 1;
//                    if (Integer.valueOf(lastRowIndex) == 0) {
//                        start = 0;
//                    }
//                    for ( ; start < Integer.valueOf(startRow) ; start++) {
//                        ExcelUtils.setRowStyle(ExcelUtils.getRow(templateSheet,start), ExcelUtils.getRow(mHSSFSheet, start));
//                        block.setLastModifyRowIndex(startRow);
//                    }
//                }
//
//                //导入数据到块中
//                exportToBlock(data,block,mHSSFSheet);
//                //导入数据完成后，最后修改行index
//                if (null != block.getThisModifyRowIndex() ){
//                    lastRowIndex = block.getThisModifyRowIndex();
//                }
//            }
//            if (Integer.valueOf(lastRowIndex).compareTo(templateSheet.getLastRowNum()) < 0 ) {
//                for (int start = Integer.valueOf(lastRowIndex)+1 ; start <= templateSheet.getLastRowNum() ; start++) {
//                    ExcelUtils.setRowStyle(ExcelUtils.getRow(templateSheet,start), ExcelUtils.getRow(mHSSFSheet, start));
//                }
//            }
//        }
//    }
//
//    /**
//     * 导入数据到每一个块中
//     * @throws Exception
//     */
//    private void exportToBlock(Object dataVO,DocBlockMeta block,Sheet mHSSFSheet) throws Exception{
//        Row row = null;//行
//        Row rowStyle = null;//样式行
//        Cell cell = null;//单元格
//
//        String type = block.getType();
//        String startRow = block.getRow();
//        String rowpostion = block.getRowpostion();
//        String addrows = block.getAddrows();
//        String lastModifyRowIndex = block.getLastModifyRowIndex();
//
//        rowStyle = ExcelUtils.getRow(templateSheet, Integer.valueOf(block.getRow()));
//
//        if (null == lastModifyRowIndex || "".equals(lastModifyRowIndex.trim())) {
//            lastModifyRowIndex = "0";
//        }
//
//        if (null == addrows || "".equals(addrows.trim())) {
//            addrows = "1";
//        }
////		System.out.println("block.getRow():" + block.getRow() + ",startRow:" + startRow + ",lastModifyRowIndex:" + lastModifyRowIndex + ",addrows:" + addrows + ",rowpostion:" + rowpostion);
//        //相对位置: 计算开始位置
//        if ("relative".equalsIgnoreCase(rowpostion)) {
//            startRow = String.valueOf(Integer.valueOf(lastModifyRowIndex) + Integer.valueOf(addrows));
//        }
//        //System.out.println("startRow:" + startRow);
//        Map<String, DocColMeta> colsMap = block.getCols();
//
//        //字段属性
//        if (DocConsts.BLOCK_TYPE_FIELD.equals(type)) {
//            row = ExcelUtils.getRow(mHSSFSheet,Integer.valueOf(startRow));
//            block.setThisModifyRowIndex(startRow);
//
//            //设置样式
//            row = ExcelUtils.setRowStyle(rowStyle,row);
//
//            String key = null;
//            //遍历配置文件中块对应的所有列属性
//            for (Iterator<String> it = colsMap.keySet().iterator();it.hasNext();){
//                key = it.next();
//                DocColMeta col = colsMap.get(key);//每一列属性
//                String property = col.getProperty();//属性字段
////    			System.out.println("property:" + property);
//
//                //取得属性对应的字段值
//                Object rs = null;
//                try {
//                    rs = Clazz.getValue(dataVO, property);
//                } catch (Exception e) {
//                    log.error("取[" + property + "]属性值报错",e);
//                    throw new Exception("取[" + property + "]属性值报错",e);
//                }
//
//                cell = ExcelUtils.getCell(row,Integer.valueOf(col.getColumn()));
//
//                //设置单元格值
//                String dataType = col.getDataType();
//                String pattern = col.getPattern();
//                String codeType = col.getCodeType();
//
//                String value = Clazz.getFieldStrValue(rs, dataType, pattern, codeType);
////    			System.out.println("value:" + value);
//                //取得单元格，设置值
//                cell.setCellValue(null == value? "" : value);
//            }
//        }else if (DocConsts.BLOCK_TYPE_TABLE.equals(type)) {
//            //取得属性对应的字段值
//            List list = null;
//            try {
//                list = (List) Clazz.getValue(dataVO, block.getList());
//            } catch (Exception e) {
//                log.error("取[" + block.getList() + "]属性值报错",e);
//                throw new Exception("取[" + block.getList() + "]属性值报错",e);
//            }
//
//            int i = 0 ;
//            if (null == list || list.isEmpty()) {
//                return;
//            }
//            //遍历对象
//            for (Iterator itemIt = list.iterator();itemIt.hasNext();){
//                Object item = itemIt.next();
//                int rowIndex = Integer.valueOf(startRow) + i;
//                row = ExcelUtils.getRow(mHSSFSheet,rowIndex);
//
//                block.setThisModifyRowIndex(String.valueOf(rowIndex));
//
//                //设置样式
//                if ( i == 0) {
//                    ExcelUtils.setRowStyle(rowStyle,row);
//                }else {
//                    ExcelUtils.setRowStyleFromListFirstRow(ExcelUtils.getRow(mHSSFSheet,Integer.valueOf(startRow)),row);
//                }
//
//
//                i ++;
//
//                String key = null;
//                //遍历配置文件中块对应的所有列属性
//                for (Iterator<String> it = colsMap.keySet().iterator();it.hasNext();){
//                    key = it.next();
//                    DocColMeta col = colsMap.get(key);//每一列属性
//                    String property = col.getProperty();//属性字段
//
//                    //取得属性对应的字段值
//                    Object rs = null;
//                    try {
//                        rs = Clazz.getValue(item, property);
//                    } catch (Exception e) {
//                        log.error("取[" + property + "]属性值报错",e);
//                        throw new Exception("取[" + property + "]属性值报错",e);
//                    }
//                    cell = ExcelUtils.getCell(row,Integer.valueOf(col.getColumn()));
//
//                    String dataType = col.getDataType();
//                    String pattern = col.getPattern();
//                    String codeType = col.getCodeType();
//
//                    //取得单元格，设置值
//                    cell.setCellValue(Clazz.getFieldStrValue(rs, dataType, pattern, codeType));
//
////					try{
////						CellRangeAddress array = cell.getArrayFormulaRange();
////						System.out.println(array.formatAsString());// 看看对不对
////					}catch (Exception e){}
//                }
//            }
//        }
//
//        block.setThisModifyRowIndex(String.valueOf(mHSSFSheet.getLastRowNum()));
//    }
//
//    public Object importFromFile(DocVO doc, File inputFile) throws Exception {
//        //0 检验输入数据
//        if (null == doc || null == inputFile) {
//            log.info("输入数据为空");
//            throw new Exception("输入数据为空");
//        }
//        log.debug("docType: " + doc.getDocType() + " ,docID:" + doc.getDocId());
//
//        //1 取得文档模板
//        DocTemplateMeta template = NOHIDocServices.getTemplate(doc.getDocId());
//        if (null == template) {
//            log.info("文档模板为空,没有取得文档ID[" + doc.getDocId()+ "]对应的配置文件");
//            throw new Exception("文档模板为空,没有取得文档ID[" + doc.getDocId()+ "]对应的配置文件");
//        }
//
//
//        FileInputStream fi = null;
//
//        //2,读取Excel
//        try {
//            fi = new FileInputStream(inputFile);
//        } catch (IOException e) {
//            log.error(e.getMessage(),e);
//            throw new Exception("读取文件[" + inputFile.getName() + "]失败",e);
//        }
//        String prefiex = inputFile.getName();
//        if (prefiex.lastIndexOf( "." ) > 0) {
//            prefiex = prefiex.substring( prefiex.lastIndexOf( "." ) );
//        }
//
//        return paraseFromFileInputStream(fi, template);
//    }
//
//    /**
//     * 根据文档模板，输入流解析Excel
//     * @param fis
//     * @param template
//     * @return
//     * @throws Exception
//     */
//    private Object paraseFromFileInputStream(InputStream fis,DocTemplateMeta template) throws Exception{
//        Object dataVo = null;
//        try {
//            dataVo = Class.forName(template.getData()).newInstance();
//        } catch (Exception e) {
//            log.error("生成VO出错" + e.getMessage(),e);
//            throw new Exception("生成VO出错" + e.getMessage(),e);
//        }
//
//        Workbook hwb = null;
//
//        //1,读取Excel
//        try {
//            hwb =  WorkbookFactory.create(fis);
//        } catch (IOException e) {
//            log.error( e.getMessage(),e );
//            throw e;
//        }
//
//        //解析每一个sheet
////		Map<String, DocSheetMeta> sheets = template.getSheetMap();
//        List<DocSheetMeta> sheetList = template.getSheetList();
//        String key = null;
//        DocSheetMeta sheet;
//        for(Iterator<DocSheetMeta> it = sheetList.iterator();it.hasNext();){
//
//            sheet = it.next();
//            key = sheet.getName();
//            //2,取得sheet
//            Sheet mHSSFSheet = hwb.getSheet(key);
//
//            if (null == mHSSFSheet){
//                log.info("没有找到[" + key + "]对应的sheet");
//                continue;
//                //throw new Exception("没有找到[" + key + "]对应的sheet");
//            }
//            //解析每一个块
//            List<DocBlockMeta> blockList = sheet.getBlockList();
//            for (DocBlockMeta block : blockList) {
//                paraseBlock(dataVo,block,mHSSFSheet);//解析每一个块
//            }
//        }
//        return dataVo;
//    }
//
//    /**
//     * 解析每一个块
//     * @param dataVO
//     * @throws Exception
//     */
//    @SuppressWarnings("unchecked")
//    private void paraseBlock(Object dataVO, DocBlockMeta bolckMeta, Sheet mHSSFSheet) throws Exception{
//        Row row = null;//行
//        Cell cell = null;//列
//        int lastRow = mHSSFSheet.getLastRowNum(); //最后一行行数
//
//        //字段类型
//        if (DocConsts.BLOCK_TYPE_FIELD.equals(bolckMeta.getType())) {
//            //得到行数据
//            row = mHSSFSheet.getRow(Integer.valueOf(bolckMeta.getRow()));
//
//            Map<String, DocColMeta> docCols = bolckMeta.getCols();
//            String key = null;
//            String value = null;
//            DocColMeta col;
//
//            for(Iterator<String> it = docCols.keySet().iterator();it.hasNext();){
//                key = it.next();
//                col = docCols.get(key);
//
//                //取得单元格
//                cell = row.getCell(Integer.valueOf(col.getColumn()));
//                if (null == cell) {
//                    continue;
//                }
//                //判断是否合并单元格
//                boolean isMerge = ExcelUtils.isMergedRegion(cell.getSheet(),cell.getRowIndex(), cell.getColumnIndex());
//                //判断是否具有合并单元格
//                if(isMerge) {
//                    value = ExcelUtils.getMergedRegionValue( cell.getSheet(), cell.getRowIndex(), cell.getColumnIndex() );
//                }else {
//                    value = ExcelUtils.getCellValue(cell);//取值
//                }
//
//                try {
//                    ExcelUtils.setValue(dataVO, col, value);
//                } catch (Exception e) {
//                    log.error( e.getMessage(),e );
//                    if (valueFailExit) {
//                        throw e;
//                    }
//                }
//            }
//        }else if (DocConsts.BLOCK_TYPE_TABLE.equals(bolckMeta.getType())) {//列表类型
//            String endRowStr = bolckMeta.getEndRow();
//            Object item = null;
//
//            List list = new ArrayList();
//
//            if (null == endRowStr || "".equals(endRowStr)) {
//                endRowStr = "100";
//            }
//
//            Map<String, DocColMeta> docCols = bolckMeta.getCols();
//            String key = null;
//            String value = null;
//            DocColMeta col;
//
//            //列表是很多行的数据，遍历
//            for (int i = Integer.valueOf(bolckMeta.getRow()); i <= Integer.valueOf(endRowStr);i++) {
//                if (i > lastRow) {
//                    break;
//                }
//
//                //得到行数据
//                row = mHSSFSheet.getRow(i);
//                if (null == row) {
//                    continue;
//                }
//                //每次都得新建对象c
//                try {
//                    item = Class.forName(bolckMeta.getItemClass()).newInstance();
//                } catch (Exception e) {
//                    log.error("生成VO出错" + e.getMessage(),e);
//                    throw new Exception("生成VO出错" + e.getMessage(),e);
//                }
//                //解析每行数据
//                for(Iterator<String> it = docCols.keySet().iterator();it.hasNext();){
//                    key = it.next();
//                    col = docCols.get(key);
//                    cell = row.getCell(Integer.valueOf(col.getColumn()));
//                    if (null == cell) {
//                        continue;
//                    }
//                    //判断是否合并单元格
//                    boolean isMerge = ExcelUtils.isMergedRegion(cell.getSheet(),cell.getRowIndex(), cell.getColumnIndex());
//                    //判断是否具有合并单元格
//                    if(isMerge) {
//                        value = ExcelUtils.getMergedRegionValue( cell.getSheet(), cell.getRowIndex(), cell.getColumnIndex() );
//                    }else {
//                        value = ExcelUtils.getCellValue(cell);//取值
//                    }
//
//                    try {
//                        ExcelUtils.setValue(item, col, value);
//                    } catch (Exception e) {
//                        log.error(e.getMessage(),e);
//                        if (valueFailExit) {
//                            throw new Exception( "行[" + (i + 1) +"]列[" + (Integer.valueOf(col.getColumn()) + 1) + "],属性[" + col.getProperty() + "],值[" + value + "]" + ",类型[" + col.getDataType() + "],错误:" + e );
//                        }
//                    }
//                }
//                list.add(item);
//            }
//
//            //把List放入对象中
//            try {
//                ExcelUtils.setValue(dataVO,bolckMeta.getList(),List.class,list);
//            } catch (Exception e) {
//                log.error(e.getMessage(),e);
//                throw e;
//            }
//        }
//    }
//
//    public Object importFromInputStream(DocVO doc, InputStream is) throws Exception {
//        //0 检验输入数据
//        if (null == doc || null == is) {
//            log.error("输入数据为空");
//            throw new Exception("输入数据为空" );
//        }
//
//        log.debug("docType: " + doc.getDocType() + " ,docID:" + doc.getDocId());
//
//
//        //1 取得文档模板
//        DocTemplateMeta template = NOHIDocServices.getTemplate(doc.getDocId());
//        if (null == template) {
//            log.info("文档模板为空,没有取得文档ID[" + doc.getDocId()+ "]对应的配置文件");
//            throw new Exception("文档模板为空,没有取得文档ID[" + doc.getDocId()+ "]对应的配置文件");
//        }
//
//        return paraseFromFileInputStream(is, template);
//    }
//
//    /**
//     *
//     * @param doc
//     * @param is
//     * @param valueFailExit  解析字段值错误是否退出,默认false;
//     * @return
//     * @throws Exception
//     */
//    public Object importFromInputStream(DocVO doc, InputStream is,boolean valueFailExit) throws Exception {
//        this.valueFailExit = valueFailExit;
//        return importFromInputStream(doc, is);
//    }
//}
//
