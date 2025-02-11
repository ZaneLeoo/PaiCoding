// ============= 后端请求的地址 =============
// 后端接口地址

// 基础接口地址
export const BASE_URL = 'http://localhost:8080'
// 注册
export const REGISTER_URL = BASE_URL + '/login/register'
// 用户名密码登录
export const LOGIN_USER_NAME_URL = '/login/username'
// 退出登录
export const LOGOUT_URL = BASE_URL + '/logout'

// 查看指定文章
export const ARTICLE_URL = BASE_URL + '/article/api/get'
export const ARTICLE_STATUS_URL = BASE_URL + '/article/api/status'

// 获取文章对应的专栏信息
export const ARTICLE_COLUMN_RELATION_URL = 'column/api/article'

// 文章点赞、收藏
export const OPERATE_URL = BASE_URL + '/user/api/operate'

// 评论点赞
export const COMMENT_LIKE_URL = 'comment/api/favor'

// 提交评论
export const COMMENT_SUBMIT_URL = BASE_URL + '/comment/api/add'

// ############# 首页相关的请求 #############
// 获取指定category下的文章列表
export const CATEGORY_ARTICLE_LIST_URL = 'article/api/articles/category'

// ############# 文章分类相关的请求 #############
// 获取分类列表

// ############# 文章标签相关的请求 #############
// 获取文章标签列表(未被标记为删除的)
export const TAG_LIST_URL = BASE_URL + '/api/tag/all'
// 获取指定category下的 tags
export const ARTICLE_TAGS_URL = '/api/tag/list/category'

// ############# 文章相关的请求 #############
// 上传图片
export const FILE_UPLOAD_URL = BASE_URL + '/image/upload'
// 上传/更新文章
export const ARTICLE_UPLOAD_URL = BASE_URL + '/article/api/save'
// 更新（编辑）文章时获取的文章详情
export const ARTICLE_EDIT_URL = '/article/api/update'
// 删除文章
export const ARTICLE_DELETE_URL = '/article/api/delete'

// ############# 用户相关的请求 #############
// 获取用户信息
export const USER_INFO_URL = '/user/api/home'
// 获取用户文章列表
export const USER_ARTICLE_LIST_URL = '/user/api/articles'
// 获取用户浏览记录列表
export const USER_HISTORY_LIST_URL = '/user/api/history'
// 获取用户收藏列表
export const USER_STAR_LIST_URL = '/user/api/star'
// 获取用户关注的用户的列表
export const USER_FOLLOW_LIST_URL = '/user/api/follows'
// 获取用户粉丝列表
export const USER_FANS_LIST_URL = '/user/api/fans'
// 保存用户信息
export const USER_INFO_SAVE_URL = '/user/api/saveUserInfo'
// 关注/取关用户
export const USER_FOLLOW_URL = '/user/api/saveUserRelation'

// ############# 消息通知相关的请求 #############
// 获取未读通知
export const UNREAD_NOTICE_URL = '/notice/api/messages'

// ============= 全局信息获取的地址 =============
// 这里主要是有些页面在刷新时并不需要重新请求数据，所以在这里获取全局信息
export const GLOBAL_INFO_URL = '/api/global/info'

// ============= 前端跳转的地址 =============
export const WRITE_ARTICLE_URL = '/article/edit'

// ============= 额外的后端工具服务地址 =============
// excel处理地址
export const EXCEL_PROCESS_URL = '/tools/transfer'
