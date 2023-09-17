<script setup>
import { ref } from 'vue'
import Tinymce from "@/views/main/components/tinymce/Tinymce.vue";
// import "mammoth/mammoth.browser.js";
// import Mammoth from "mammoth";

const disabled = ref(true)

/*tinymce操作*/
const refTinymce = ref(null);
const setTinyContent = () => {
  if (refTinymce?.value) {
    refTinymce.value.setContent("tinymce设置的内容");
  }
};
// const { elMessage } = useElement();
const getTinyContent = () => {
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
// const beforeUpload = (file, fileList) => {
//   const self = this;
//   var reader = new FileReader();
//   reader.onloadend = function (event) {
//     let arrayBuffer = reader.result;
//     //将word 转换成html
//     const loading = self.$loading({
//       lock: true,
//       text: '文件解析中....',
//       spinner: 'el-icon-loading',
//       background: 'rgba(0, 0, 0, 0.3)'
//     });
//     Mammoth.convertToHtml({ arrayBuffer: arrayBuffer }).then(function (
//         resultObject
//     ) {
//       // 重要！不适用setTimeout,无法使用
//       setTimeout(() => {
//         //获取原来编辑器的内容
//         let content = tinymce.activeEditor.getContent()+resultObject.value
//         tinymce.activeEditor.setContent(content);
//         loading.close();
//       }, 100);
//     });
//   };
//   reader.readAsArrayBuffer(file);
//   return false;
// }

</script>
<template>
  <div class="scroll-y">
    <div class="mb-1">
      <el-button @click="setTinyContent">设置文本消息</el-button>
      <el-button @click="getTinyContent">获取文本消息</el-button>
      <el-button @click="clearTinyContent">清空内容</el-button>
    </div>
    <div style="text-align: right; margin-bottom: 20px">
<!--      <el-upload-->
<!--          class="uploadBtn"-->
<!--          style="display:none"-->
<!--          ref="fileRefs"-->
<!--          action=""-->
<!--          :before-upload="beforeUpload"-->
<!--          :show-file-list="false"-->
<!--          :disabled="disabled"-->
<!--      >-->
<!--        <el-button :disabled="disabled">导入</el-button>-->
<!--      </el-upload>-->
    </div>
    <Tinymce ref="refTinymce"  />
  </div>
</template>
