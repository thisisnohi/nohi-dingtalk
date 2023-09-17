<script setup>
import { ref, onMounted } from 'vue'
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
    default: 'lists image media table wordcount save preview'
    // 插件需要import进来
    // default: 'wordcount visualchars visualblocks toc textpattern template tabfocus spellchecker searchreplace save quickbars print preview paste pagebreak noneditable nonbreaking media insertdatetime importcss imagetools image hr help fullscreen fullpage directionality codesample code charmap link code table lists advlist anchor autolink autoresize autosave'
    // default: 'searchreplace fontsizeselect bold italic underline strikethrough alignleft aligncenter alignright outdent indent blockquote undo redo removeformat subscript superscript code codesampl'
  },
  toolbar: {
    type: [String, Array],
    default:
        'advlist anchor undo redo |  formatselect | bold italic underline strikethrough forecolor backcolor | alignleft aligncenter alignright alignjustify | bullist numlist outdent indent blockquote | lists image media table | codesample code removeformat save preview'
    // default:
    //   "formats undo redo paste print fontsizeselect fontselect template fullpage|wordcount ltr rtl visualchars visualblocks toc spellchecker searchreplace|save preview pagebreak nonbreaking|media image|outdent indent aligncenter alignleft alignright alignjustify lineheight  underline quicklink h2 h3 blockquote numlist bullist table removeformat forecolor backcolor bold italic  strikethrough hr charmap link insertdatetime|subscript superscript cut codesample code |anchor preview fullscreen|help",
    // 'bold italic underline h1 h2 blockquote codesample numlist bullist link image | removeformat fullscreen'
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
  // setup: (edit) => {
  //   let _this = this
  //   editor.ui.registry.addButton("importbtn", {
  //     text: "导入word",
  //     // icon:'', // 目前使用文字按钮，如果需要图标展示，根据文档中自定义图标中的内容进行配置
  //     onAction: function () {
  //       //触发上传组件
  //       _this.$refs['fileRefs'].$refs['upload-inner'].handleClick()
  //     },
  //   });
  // }
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
