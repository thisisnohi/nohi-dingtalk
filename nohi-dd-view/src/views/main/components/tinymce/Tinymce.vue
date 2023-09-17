<script setup>
import { ref, onMounted } from 'vue'
import "mammoth/mammoth.browser.js";
import mammoth from "mammoth";

import tinymce from 'tinymce/tinymce' //tinymce默认hidden，不引入不显示
import Editor from '@tinymce/tinymce-vue'
import 'tinymce/themes/silver/theme' // 主题文件
import 'tinymce/icons/default'
import 'tinymce/models/dom'
// tinymce插件可按自己的需要进行导入
// 更多插件参考：https://www.tiny.cloud/docs/plugins/
import 'tinymce/plugins/image' // 插入上传图片插件
import 'tinymce/plugins/importcss' //图片工具
import 'tinymce/plugins/media' // 插入视频插件
import 'tinymce/plugins/table' // 插入表格插件
import 'tinymce/plugins/lists' // 列表插件
import 'tinymce/plugins/charmap' // 特殊字符
import 'tinymce/plugins/wordcount' // 字数统计插件
import 'tinymce/plugins/codesample' // 插入代码
import 'tinymce/plugins/code' // 查看源码
import 'tinymce/plugins/fullscreen' //全屏
import 'tinymce/plugins/link' //
import 'tinymce/plugins/preview' // 预览
import 'tinymce/plugins/template' //插入模板
import 'tinymce/plugins/save' // 保存
import 'tinymce/plugins/searchreplace' //查询替换
import 'tinymce/plugins/pagebreak' //分页
import 'tinymce/plugins/insertdatetime' //时间插入
import 'tinymce/plugins/accordion'
import 'tinymce/plugins/advlist'
import 'tinymce/plugins/anchor'
import 'tinymce/plugins/autolink'
import 'tinymce/plugins/autoresize'
import 'tinymce/plugins/autosave'
import 'tinymce/plugins/directionality'
import 'tinymce/plugins/emoticons'
import 'tinymce/plugins/help'
import 'tinymce/plugins/nonbreaking'
import 'tinymce/plugins/quickbars'
import 'tinymce/plugins/visualblocks'
import 'tinymce/plugins/visualchars'

const props = defineProps({
  value: {
    type: String,
    default: ''
  },
  disabled: {
    type: Boolean,
    default: false
  },

  plugins: {
    type: [String, Array],
    // default: 'lists image media table wordcount save preview'
    // 插件需要import进来
    // default: 'wordcount visualchars visualblocks toc textpattern template tabfocus spellchecker searchreplace save quickbars print preview paste pagebreak noneditable nonbreaking media insertdatetime importcss imagetools image hr help fullscreen fullpage directionality codesample code charmap link code table lists advlist anchor autolink autoresize autosave'
    // default: 'searchreplace fontsizeselect bold italic underline strikethrough alignleft aligncenter alignright outdent indent blockquote undo redo removeformat subscript superscript code codesampl'
    default : 'image table advlist fullscreen link lists preview searchreplace insertdatetime charmap'
  },
  toolbar: {
    type: [String, Array],
    default:
        // ' anchor undo redo |  formatselect | bold italic underline strikethrough forecolor backcolor | alignleft aligncenter alignright alignjustify | bullist numlist outdent indent blockquote | lists image media table | codesample code removeformat save preview'
    // default:
    //   "formats undo redo paste print fontsizeselect fontselect template fullpage|wordcount ltr rtl visualchars visualblocks toc spellchecker searchreplace|save preview pagebreak nonbreaking|media image|outdent indent aligncenter alignleft alignright alignjustify lineheight  underline quicklink h2 h3 blockquote numlist bullist table removeformat forecolor backcolor bold italic  strikethrough hr charmap link insertdatetime|subscript superscript cut codesample code |anchor preview fullscreen|help",
    // 'bold italic underline h1 h2 blockquote codesample numlist bullist link image | removeformat fullscreen'
    'fontselect | formatselect | fontsizeselect | forecolor backcolor | bold italic underline strikethrough | image | table | alignleft aligncenter alignright alignjustify | outdent indent | numlist bullist | preview  hr | undo redo | fullscreen searchreplace |print | customUploadBtn'
    // 'fontselect | formatselect | fontsizeselect | forecolor backcolor | bold italic underline strikethrough | image | table | alignleft aligncenter alignright alignjustify | outdent indent | numlist bullist | preview  hr | undo redo | fullscreen searchreplace |print | customUploadBtn'
  }
})

const init = {
  // width: 720,
  height: 400,
  language_url: './tinymce/langs/zh-Hans.js',
  language: 'zh-Hans',
  // 皮肤：这里引入的是public下的资源文件
  skin_url: './tinymce/skins/ui/oxide',
  // skin_url: 'tinymce/skins/ui/oxide-dark',//黑色系
  content_css: './tinymce/skins/content/default/content.css', //内容区域css样式
  // images_file_types: "jpg,svg,webp",
  // images_upload_url: "xxxxxxxxxxxxx",//系统默认配置的自动上传路径，需替换为真实路径测试
  plugins: props.plugins,
  toolbar: props.toolbar,
  // plugins: "image,table,advlist,fullscreen,link,lists,preview,searchreplace,insertdatetime,charmap",//image imagetools
  // toolbar: ['fontselect | formatselect | fontsizeselect | forecolor backcolor | bold italic underline strikethrough | image | table | alignleft aligncenter alignright alignjustify | outdent indent | numlist bullist | preview  hr | undo redo | fullscreen searchreplace |print | customUploadBtn'],
  branding: false,
  // 隐藏菜单栏
  //menubar: false,
  // 是否显示底部状态栏
  statusbar: true,
  // convert_urls: false,
  // 初始化完成
  init_instance_callback: (editor) => {
    console.log('初始化完成：', editor)
  },
  file_picker_callback: function(callback, value, meta) {
    // 打开选择文件的弹窗
    var input = document.createElement('input');
    input.type = 'file';
    input.accept = 'image/*';

    input.onchange = function () {
      var file = input.files[0];
      // 将文件转换成base64编码
      var reader = new FileReader();
      reader.onloadend = function () {
        var base64 = reader.result;
        // 将base64编码插入到编辑器的当前光标位置
        callback(base64, {
          alt: ''
        });
      };
      reader.readAsDataURL(file);
    };
    input.click();
  },
  setup: function (editor) {
    // 注册自定义按钮
    editor.ui.registry.addButton('customUploadBtn', {
      text: '上传Word',
      onAction: function () {
        var input = document.createElement('input');
        input.type = 'file';
        input.accept = '.doc,.docx';
        // 执行上传文件操作
        input.addEventListener("change", handleFileSelect, false);

        //获取上传文件base64数据
        function arrayBufferToBase64(arrayBuffer) {
          var binary = '';
          var bytes = new Uint8Array(arrayBuffer);
          var len = bytes.byteLength;
          for (var i = 0; i < len; i++) {
            binary += String.fromCharCode(bytes[i]);
          }
          return window.btoa(binary);
        }

        function handleFileSelect(event) {
          var file = event.target.files[0];
          //获取上传文件后缀，如果是docx格式，则使用mammoth来进行解析，
          //如果不是则访问后台，将文件传输流base64传递到后台
          //生成文件，然后用java解析doc，并返回到前台
          var extension = file.name.slice((file.name.lastIndexOf(".") - 1 >>> 0) + 2);
          if (extension === 'docx') {
            readFileInputEventAsArrayBuffer(event, function (arrayBuffer) {
              var base64Data = arrayBufferToBase64(arrayBuffer);
              console.log(base64Data);
              mammoth.convertToHtml({ arrayBuffer: arrayBuffer })
                  .then(displayResult, function (error) {
                    console.error(error);
                  });
            });
          } else if(extension === 'doc') {
            readFileInputEventAsArrayBuffer(event, function (arrayBuffer) {
              //base64文件流
              var base64Data = arrayBufferToBase64(arrayBuffer);
              var result ="后台请求";
              alert(result);
              console.log(base64Data);
            });
            //tinymce的set方法将内容添加到编辑器中
            tinymce.activeEditor.setContent(result);
          }
        }

        function displayResult(result) {
          //tinymce的set方法将内容添加到编辑器中
          tinymce.activeEditor.setContent(result.value);
        }

        function readFileInputEventAsArrayBuffer(event, callback) {
          var file = event.target.files[0];
          var reader = new FileReader();
          reader.onload = function (loadEvent) {
            var arrayBuffer = loadEvent.target.result;
            callback(arrayBuffer);
          };
          reader.readAsArrayBuffer(file);
        }

        // 触发点击事件，打开选择文件的对话框
        input.click();
      }
    });
  }
  // 此处为自定义图片上传处理函数
  // images_upload_handler: (blobInfo, progress) => {
  //   console.log("图片上传处理：", blobInfo.blob());
  //   const formData = new FormData();
  //   formData.append("file", blobInfo.blob());
  //   axiosReq({
  //     url: "xxxxxxxxxxx",//需替换为真实路径测试
  //     data: formData,
  //     method: "post",
  //     bfLoading: true,
  //     isUploadFile: true,
  //   }).then((res) => {
  //     console.log(res);
  //   });
  // },
}
let textContent = ref(props.value)

// 组件挂载完毕
onMounted(() => {
  tinymce.init({})
})

// 添加相关的事件,https://github.com/tinymce/tinymce-vueevents
const clear = () => {
  textContent = ''
}

const setContent = (value) => {
  console.info('setContent:' + value)
  textContent.value = value
}
const getContent = () => {
  return textContent.value
}
defineExpose({ setContent, getContent })
</script>
<template>
  <p>
    与框架自带tinymce有冲突，使用时只需要一种即可。
    测试时，刷新页面使用
  </p>
  <editor v-model="textContent" :init="init" :disabled="disabled"></editor>
</template>
