    <meta charset="utf-8">
    <!--浏览器小图标-->
    <link rel="icon" href="//cdn.mingsoft.net/global/images/ms.ico" type="x-icon">
    <script type="text/javascript" src="${base}/static/plugins/vue/2.6.9/vue.min.js"></script>
    <script src="${base}/static/plugins/vue-i18n/8.18.2/vue-i18n.js"></script>
    <!-- 图标 -->
    <link rel="stylesheet" type="text/css" href="${base}/static/plugins/iconfont/1.0.0/iconfont.css" />
    <script src="${base}/static/plugins/iconfont/1.0.0/iconfont.js"></script>

    <!-- 引入样式 -->
    <link rel="stylesheet" href="${base}/static/plugins/element-ui/2.12.0/index.css">
    <!-- 引入组件库 -->
    <script src="${base}/static/plugins/element-ui/2.12.0/index.js"></script>
    <!--图片懒加载-->
    <script src="${base}/static/plugins/vue.lazyload/1.2.6/vue-lazyload.js"></script>
    <!--网络请求框架-->
    <script src="${base}/static/plugins/axios/0.18.0/axios.min.js"></script>
    <script src="${base}/static/plugins/qs/6.6.0/qs.min.js"></script>
    <!--金额转换-->
    <script src="${base}/static/plugins/accounting/0.4.1/accounting.js"></script>
    <!--铭飞-->
    <script src="${base}/static/plugins/ms/1.0.0/ms.js"></script>
    <script src="${base}/static/plugins/ms/1.0.0/ms.http.js"></script>
    <script src="${base}/static/plugins/ms/1.0.0/ms.util.js"></script>
    <script src="${base}/static/plugins/vue-ueditor-wrap/vue-ueditor-wrap.min.js"></script>
    <script src="${base}/static/plugins/ms/1.0.0/ms.dict.js"></script>

    <#--复制-->
    <script src="${base}/static/plugins/clipboard/clipboard.js"></script>
    <#-- 树形下拉-->
    <script src="${base}/static/plugins/tree-select/tree.js"></script>
    <!--通用样式-->
    <link rel="stylesheet" href="${base}/static/css/app.css"/>
    <#--主题-->
    <link rel="stylesheet" href="${base}/static/css/theme.css">
    <![if IE]>
    <script src="${base}/static/plugins/babel-polyfill/7.8.3/polyfill.min.js"></script>
    <script src="${base}/static/plugins/ms/1.0.0/compatible.js"></script>
    <![endif]>
    <#--    Vue拓展-->
    <script src="${base}/static/plugins/ms/1.0.0/ms.vue.expand.js"></script>



    <script>
        ms.base = "${base}";
        ms.manager = "${managerPath}";
        ms.web = ms.base;

        //ms.base = "http://192.168.0.54:90/";
        //ms.manager = "http://192.168.0.54:90/apis/ms/";
        //ms.web = "http://192.168.0.54:90/apis/";
        //图片懒加载
		  Vue.use(VueLazyload, {
		    error: ms.base + '/static/images/error.png',
		    loading: ms.base + '/static/images/loading.png',
		  })
        Vue.component('vue-ueditor-wrap', VueUeditorWrap);

    </script>
    <style>
        .ms-admin-menu .is-active {
            border: 0px !important;
        }
    </style>
    <#include '/component/ms-select.ftl'/>
    <#include '/component/ms-date-picker.ftl'/>
    <#include '/component/ms-upload.ftl'/>
    <#include '/component/ms-cascader.ftl'/>
    <#--金额输入框-->
    <#include '/component/ms-money-input.ftl'/>
    <#include '/component/ms-employee.ftl'/>
    <#include "/component/ms-search.ftl">



