<template>
  <a-card style="width: 900px; height: auto; margin: auto; margin-top: 20px">
    <a-layout class="main">
      <a-layout-header class="header">
        <div class="header-A">
          <!-- title -->
          <a-input
            v-model:value="title"
            :bordered="false"
            placeholder="Article Title..."
            style="font-size: 25px; width: 2000px"
          />
          <!-- cover -->
          <a-upload
            v-model:file-list="fileList"
            name="upload"
            list-type="picture-card"
            accpet="image/*"
            maxCount="1"
            class="avatar-uploader"
            :show-upload-list="false"
            :action="FILE_UPLOAD_URL"
            :before-upload="beforeUpload"
            @change="coverUpload"
          >
            <img v-if="imageUrl" :src="imageUrl" alt="avatar" class="uploaded-image" />
            <div v-else>
              <loading-outlined v-if="loading"></loading-outlined>
              <plus-outlined v-else></plus-outlined>
              <div class="ant-upload-text">Cover</div>
            </div>
          </a-upload>
        </div>
        <a-card style="width: 100%">
          <!-- category -->
          <span class="category">Categories:</span>
          <a-space :size="[0, 8]" wrap>
            <a-checkable-tag
              v-for="(tag, index) in tagsData.values"
              :key="tag"
              v-model:checked="selectTags[index]"
              @change="(checked) => handleChange(tag, checked)"
            >
              {{ tag }}
            </a-checkable-tag>
          </a-space>
        </a-card>
      </a-layout-header>
      <a-card style="width: 100%">
        <a-layout-content class="content">
          <a-typography-title :level="4">Content</a-typography-title>
          <!-- CKEditor 编辑区 -->
          <div class="main-container">
            <div
              class="editor-container editor-container_classic-editor"
              ref="editorContainerElement"
            >
              <div class="editor-container__editor">
                <div ref="editorElement">
                  <ckeditor
                    v-if="editor && config"
                    :modelValue="config.initialData"
                    :editor="editor"
                    :config="config"
                    @ready="onEditorReady"
                  />
                </div>
              </div>
            </div>
          </div>
          <!-- Summary -->
          <a-input
            v-model:value="summary"
            :bordered="false"
            placeholder="Article Summary..."
            style="font-size: 25px"
          />
        </a-layout-content>
        <a-alert
          v-if="visible"
          :message="errorMessage"
          type="error"
          closable
          :after-close="handleClose"
        />
      </a-card>
      <!-- 按钮功能区 -->
      <a-layout-footer class="footer">
        <div class="footer-buttons">
          <a-button type="primary" size="large">
            <template #icon>
              <ArrowLeftOutlined />
            </template>
            Back
          </a-button>
          <div class="right-buttons">
            <a-button type="primary" size="large" @click="save">
              <template #icon>
                <EditOutlined />
              </template>
              Save
            </a-button>
            <a-button type="primary" size="large" @click="publish">
              <template #icon>
                <NotificationOutlined />
              </template>
              Publish
            </a-button>
          </div>
        </div>
      </a-layout-footer>
    </a-layout>
  </a-card>
  <a-modal :open="isPublish" :footer="null">
    <a-result
      :title="
        isSave
          ? '恭喜你!保存草稿成功🚀🚀距离作家又近了一步😎😎✌️!'
          : '恭喜你!发布成功🚀🚀距离作家又近了一步😎😎✌️!'
      "
    >
      <template #icon>
        <SmileTwoTone />
      </template>
      <template #extra>
        <a-button type="primary" @click="toArticlePage">See Article🚀</a-button>
      </template>
    </a-result>
  </a-modal>
</template>

<script setup>
import { NotificationOutlined, ArrowLeftOutlined, EditOutlined } from '@ant-design/icons-vue'
import { reactive, computed, ref, onMounted } from 'vue'
import { FILE_UPLOAD_URL, ARTICLE_UPLOAD_URL, TAG_LIST_URL } from '@/http/URL'
import { doPost, doGet } from '@/http/BackendRequests.js'
import { useRouter } from 'vue-router'
const router = useRouter()
// 数据定义
const title = ref('')
const summary = ref('')
const tagsData = reactive({ values: [] })
const selectTags = reactive([false, false, false, false, false, false, false, false])
const selectTagData = reactive([])
const imageUrl = ref('')
const articleId = ref('')
const visible = ref(false)
const isPublish = ref(false)
const errorMessage = ref('')
const isLayoutReady = ref(false)
const isSave = ref(false)

//编辑器实例
const editorInstance = ref(null)
const onEditorReady = (newEditor) => {
  editorInstance.value = newEditor
  console.log('编辑器已就绪:', editorInstance.value)
}

// 显示错误
const showError = (message) => {
  visible.value = true
  errorMessage.value = message
}
const handleClose = () => {
  visible.value = false
}

// 路由跳转
const toArticlePage = () => {
  console.log(articleId.value)
  router.push(`/article/page/${articleId.value}`)
}

// 数据获取和初始化
onMounted(() => {
  isLayoutReady.value = true
  fetchTags()
})

// 标签获取
const fetchTags = () => {
  doGet(TAG_LIST_URL).then((response) => {
    const names = response.data.result.map((item) => item.name)
    tagsData.values = names
  })
}

// 标签选择
const handleChange = (tag, checked) => {
  console.log(tag)
  if (checked) {
    selectTagData.push(tag)
  } else {
    const index = selectTagData.indexOf(tag)
    if (index !== -1) {
      selectTagData.splice(index, 1)
    }
  }
}

// 封面上传
const beforeUpload = (file) => {
  const isSmallEnough = file.size / 1024 / 1024 < 3 // 最大5MB
  if (!isSmallEnough) {
    this.$message.error('文件大小不能超过 3MB!')
  }
  return isSmallEnough
}
const coverUpload = (info) => {
  if (info.file.status === 'done') {
    // 上传成功
    // 后端返回的 URL 可以在 info.file.response 中找到
    imageUrl.value = info.file.response.url // 假设后端返回的字段是 'url'
  }
}

// 发布文章
const publish = () => {
  if (!validateInputs()) return
  const editorData = editorInstance.value ? editorInstance.value.getData() : ''
  doPost(ARTICLE_UPLOAD_URL, {
    title: title.value,
    summary: summary.value,
    tags: selectTagData,
    content: editorData,
    cover: imageUrl.value,
    status: 1,
    actionType: 'POST'
  }).then((response) => {
    if (response.data.status.code === 200) {
      isPublish.value = true
      articleId.value = response.data.result
    }
  })
}

// 保存文章
const save = () => {
  if (!validateInputs()) return
  const editorData = editorInstance.value ? editorInstance.value.getData() : ''
  doPost(ARTICLE_UPLOAD_URL, {
    title: title.value,
    summary: summary.value,
    tags: selectTagData,
    content: editorData,
    cover: imageUrl.value,
    status: 0,
    actionType: 'POST'
  }).then((response) => {
    if (response.data.status.code === 200) {
      isPublish.value = true
      isSave.value = true
      articleId.value = response.data.result
    }
  })
}

// 输入验证
const validateInputs = () => {
  if (!title.value || title.value.length < 6) {
    showError('请输入标题(长度 > 6)')
    return false
  }
  if (!summary.value || summary.value.length < 6) {
    showError('请输入总结(长度 > 6)')
    return false
  }
  if (!editorInstance.value || !editorInstance.value.getData()) {
    showError('内容不能为空')
    return false
  }
  if (!selectTagData.length) {
    showError('请选择至少一个标签')
    return false
  }
  return true
}

// CKEditor config
const LICENSE_KEY =
  'eyJhbGciOiJFUzI1NiJ9.eyJleHAiOjE3NDAwOTU5OTksImp0aSI6ImY0N2NhMjJlLWNhNmItNGU4MS04NGI5LTQyYjdkYzk2YWEyNCIsInVzYWdlRW5kcG9pbnQiOiJodHRwczovL3Byb3h5LWV2ZW50LmNrZWRpdG9yLmNvbSIsImRpc3RyaWJ1dGlvbkNoYW5uZWwiOlsiY2xvdWQiLCJkcnVwYWwiLCJzaCJdLCJ3aGl0ZUxhYmVsIjp0cnVlLCJsaWNlbnNlVHlwZSI6InRyaWFsIiwiZmVhdHVyZXMiOlsiKiJdLCJ2YyI6IjYzZDFhNWE1In0.eN7mj5EkCIp7j1O15Yyz-sEX5W9zB0uHZqvvRyJHk7y9Pm5LR74s9VNzj3HUVOTfjVsa6rjPOXYVZvXy47-tRw'

import { Ckeditor } from '@ckeditor/ckeditor5-vue'
import 'ckeditor5/ckeditor5.css'
import translations from 'ckeditor5/translations/zh.js'
import {
  ClassicEditor,
  Autoformat,
  AutoImage,
  Autosave,
  Bold,
  Code,
  Essentials,
  FindAndReplace,
  Heading,
  ImageBlock,
  ImageInline,
  ImageInsert,
  ImageInsertViaUrl,
  ImageResize,
  ImageStyle,
  ImageTextAlternative,
  ImageToolbar,
  ImageUpload,
  Indent,
  IndentBlock,
  Italic,
  Link,
  List,
  ListProperties,
  Paragraph,
  PasteFromOffice,
  SimpleUploadAdapter,
  TextTransformation,
  Underline
} from 'ckeditor5'
const editor = ClassicEditor
const config = computed(() => {
  if (!isLayoutReady.value) {
    return null
  }
  return {
    extraPlugins: [SimpleUploadAdapter], // 正确配置 SimpleUploadAdapter
    simpleUpload: {
      uploadUrl: FILE_UPLOAD_URL
    },
    toolbar: {
      items: [
        'findAndReplace',
        '|',
        'heading',
        '|',
        'bold',
        'italic',
        'underline',
        'code',
        '|',
        'link',
        'insertImage',
        '|',
        'bulletedList',
        'numberedList',
        'outdent',
        'indent'
      ],
      shouldNotGroupWhenFull: false
    },
    plugins: [
      Autoformat,
      AutoImage,
      Autosave,
      Bold,
      Code,
      Essentials,
      FindAndReplace,
      Heading,
      ImageBlock,
      ImageInline,
      ImageInsert,
      ImageInsertViaUrl,
      ImageResize,
      ImageStyle,
      ImageTextAlternative,
      ImageToolbar,
      ImageUpload,
      Indent,
      IndentBlock,
      Italic,
      Link,
      List,
      ListProperties,
      Paragraph,
      PasteFromOffice,
      SimpleUploadAdapter,
      TextTransformation,
      Underline
    ],
    heading: {
      options: [
        {
          model: 'paragraph',
          title: 'Paragraph',
          class: 'ck-heading_paragraph'
        },
        {
          model: 'heading1',
          view: 'h1',
          title: 'Heading 1',
          class: 'ck-heading_heading1'
        },
        {
          model: 'heading2',
          view: 'h2',
          title: 'Heading 2',
          class: 'ck-heading_heading2'
        },
        {
          model: 'heading3',
          view: 'h3',
          title: 'Heading 3',
          class: 'ck-heading_heading3'
        },
        {
          model: 'heading4',
          view: 'h4',
          title: 'Heading 4',
          class: 'ck-heading_heading4'
        },
        {
          model: 'heading5',
          view: 'h5',
          title: 'Heading 5',
          class: 'ck-heading_heading5'
        },
        {
          model: 'heading6',
          view: 'h6',
          title: 'Heading 6',
          class: 'ck-heading_heading6'
        }
      ]
    },
    language: 'zh',
    translations: [translations],
    image: {
      toolbar: [
        'imageTextAlternative',
        '|',
        'imageStyle:inline',
        'imageStyle:wrapText',
        'imageStyle:breakText',
        '|',
        'resizeImage'
      ]
    },
    licenseKey: LICENSE_KEY,
    link: {
      addTargetToExternalLinks: true,
      defaultProtocol: 'https://',
      decorators: {
        toggleDownloadable: {
          mode: 'manual',
          label: 'Downloadable',
          attributes: {
            download: 'file'
          }
        }
      }
    },
    list: {
      properties: {
        styles: true,
        startIndex: true,
        reversed: true
      }
    },
    placeholder: 'Start your show!🚀'
  }
})
</script>

<style scoped>
.header {
  background-color: white;
  padding: 0; /* 去掉默认的内边距 */
  height: auto; /* 如果有必要，设置高度 */
}
.content {
  background-color: white;
}
.footer {
  background-color: white;
}
.category {
  margin-right: 8px;
  font-size: 17px;
  font-weight: bold;
}
.main {
  display: flex;
  gap: 10px;
  background-color: white;
}
.main-container {
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
}

.editor-container {
  width: 100%;
  height: 100%;
  overflow: hidden;
}

.footer-buttons {
  display: flex;
  justify-content: space-between; /* 使按钮左右对齐 */
  padding: 10px; /* 添加一些内边距 */
}

.right-buttons {
  display: flex;
  gap: 8px; /* 设置按钮之间的间距 */
}

.ant-input::placeholder {
  font-size: 25px;
}

.header-A {
  display: flex;
}
.uploaded-image {
  max-width: 100%; /* 限制宽度为100%，以适应父容器 */
  max-height: 300px; /* 设置最大高度 */
  object-fit: cover; /* 保持图片的比例，裁切多余的部分 */
}
</style>
