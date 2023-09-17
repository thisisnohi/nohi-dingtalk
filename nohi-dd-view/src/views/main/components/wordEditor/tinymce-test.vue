<script setup>
import { computed, ref } from 'vue'
import Tinymce from "@/views/main/components/tinymce/Tinymce.vue";
import "mammoth/mammoth.browser.js";
import Mammoth from "mammoth";
import { asBlob } from "html-docx-js-typescript";
import { saveAs } from "file-saver";


const tinymceContent = ref(null);
/*tinymce操作*/
const refTinymce = ref(null);
const setTinyContent = () => {
  if (refTinymce?.value) {
    refTinymce.value.setContent("tinymce设置的内容");
  }
};
// const { elMessage } = useElement();
const getTinyContent = () => {
  console.info(tinymceContent)

  if (refTinymce?.value) {
    let content = refTinymce.value.getContent();
    console.info(content)
    // elMessage(content);
  }
};
const clearTinyContent = () => {
  if (refTinymce?.value) {
    refTinymce.value.setContent("");
  }
};
const beforeUpload = (file, fileList) => {
  const self = this;
  var reader = new FileReader();
  reader.onloadend = function(event) {
    let arrayBuffer = reader.result;
    //将word 转换成html
    // const loading = self.$loading({
    //   lock: true,
    //   text: '文件解析中....',
    //   spinner: 'el-icon-loading',
    //   background: 'rgba(0, 0, 0, 0.3)'
    // });
    Mammoth.convertToHtml({ arrayBuffer: arrayBuffer }).then(function(
        resultObject
    ) {
      // 重要！不适用setTimeout,无法使用
      setTimeout(() => {
        //获取原来编辑器的内容
        let content = refTinymce.value.getContent() + resultObject.value
        refTinymce.value.setContent(content);
        // loading.close();
      }, 100);
    });
  };
  reader.readAsArrayBuffer(file);
  return false;
}
const saveDocx = () => {
  if (refTinymce?.value) {
    console.info(refTinymce.value.getContent())
    asBlob(refTinymce.value.getContent()).then((data) => {
      saveAs(data, "实施方案.docx");
    });
  }

}

const print = ref({})
print.value = {
  id: "printArea",
  popTitle: "11111111111111", // 打印配置页上方的标题
  extraHead: "222222222222", // 最上方的头部文字，附加在head标签上的额外标签，使用逗号分割
  preview: false, // 是否启动预览模式，默认是false
  previewTitle: "预览的标题", // 打印预览的标题
  previewPrintBtnLabel: "预览结束，开始打印", // 打印预览的标题下方的按钮文本，点击可进入打印
  zIndex: 20002, // 预览窗口的z-index，默认是20002，最好比默认值更高
  previewBeforeOpenCallback() {
    console.log("正在加载预览窗口！");
    console.log(that.msg, this);
  }, // 预览窗口打开之前的callback
  previewOpenCallback() {
    console.log("已经加载完预览窗口，预览打开了！");
  }, // 预览窗口打开时的callback
  beforeOpenCallback() {
    console.log("开始打印之前！");
  }, // 开始打印之前的callback
  openCallback() {
    console.log("执行打印了！");
  }, // 调用打印时的callback
  closeCallback() {
    console.log("关闭了打印工具！");
  }, // 关闭打印的callback(无法区分确认or取消)
  clickMounted() {
    console.log("点击v-print绑定的按钮了！");
  },
  standard: "",
  extarCss: "",
}

// 打印内容
const printValue = computed(() => {
  if (refTinymce?.value) {
    return refTinymce.value.getContent()
  }
  return ''
})

</script>
<style>
.uploadBtn{
  margin-left: 10px;
  margin-right: 10px;
}
</style>
<style media="printContent">
/* 去掉页脚页眉 */
@page {
  size: auto;
  margin: 0mm auto;
}
</style>
<template>
  <div class="layout-container">
    <div class="layout-container-table">
      <div class="mb-1">
        <el-button @click="setTinyContent">设置文本消息</el-button>
        <el-button @click="getTinyContent">获取文本消息</el-button>
        <el-button @click="clearTinyContent">清空内容</el-button>
        <el-upload
            class="uploadBtn"
            style="display: inline-block;"
            ref="fileRefs"
            action=""
            :before-upload="beforeUpload"
            :show-file-list="false"
        >
          <el-button>导入</el-button>
        </el-upload>
        <el-button @click="saveDocx"> 生成HTML文件 </el-button>
        <el-button type="primary" v-print="print"> 文件打印 </el-button>
      </div>
      <Tinymce ref="refTinymce"  />
      <h3>预览</h3>
      <div
          id="printArea"
          v-html="printValue"
          style="text-align: left; padding: 48px;"
      ></div>
    </div>
  </div>
</template>
