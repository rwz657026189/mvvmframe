# mvvmframe
一个基于retrofit2、Rxjava2、databinding采用mvvm架构的框架，封装常用的ui控件，列表加载，适合快速高效开发，已在多款商业级项目中运用。请务必熟悉databinding的基本操作。
- 特色
   - 采用mvvm架构，不需要写adapter，通过databinding绑定数据
   - 内置多种BindingAdapter（ViewBindingAdapter、ImageBindingAdapter、TextBindingAdapter等），极大扩展了基本控件支持能力
   - 强大的列表支持，简单配置即可实现复杂列表，更不需要关心loading、error、empty等视图展示效果，下拉刷新，上拉加载更多，分页加载都不需要额外实现
   - 支持多选列表，只需要继承BaseCheckedViewModule，已实现单选、多选、全选、部分不可选等业务逻辑
   - 动态支持系统webView或腾讯X5内核webView，填了很多坑（选择本地图片、混合协议、视频加载、部分页面加载不出、不能唤醒支付宝、微信等等）
   - 内置常用ui控件
      - CommMenuPop：只需要简单的数据即可实现漂亮的PopupWindow效果
      - CommBottomDialog：通用底部弹出dialog
      - LoadingDialog：加载中占位视图，适合单一请求，页面通用
      - MsgDialog：通信的消息Dialog
      - EditDialog: 带输入框的Dialog
      - IconTextView：带左右icon, 下划线，右侧文字，适合用于“我的”、“设置”等页面item
      - SingleCenterTextView：自带居中效果的图文控件，跟TextView用法一致，但总能保证图片文字居中展示
      - CodeTextView：带倒计时的TextView控件
      - StateButton：带按下、不可点击状态的TextView、居中、不可点击仍然响应点击事件（为了提示用户输入异常等情况）
      
      
- 主要三方框架：（部分非必须）
   - gson
   - retrofit2：网络请求
   - rxjava2：
   - rxbinding
   - rxpermissions
   - glide
   - glide-transformations：glide特效库
   - smartRefreshLayout：下拉刷新，上拉加载更多列表控件
   - banner
   - EventBus
   - Stetho：Facebook推出调试神器，可查看数据库
   - FlycoTabLayout：可滑动切换的tab控件
   - walle：美团打包神器，及其轻量
   - LitePal：轻量级数据库
   - SwipeDelMenuLayout：侧滑删除
   - leakcanary：检测内存泄漏
   - SwipeBackLayout：侧滑关闭页面（修复快速滑动的一个bug）
 
