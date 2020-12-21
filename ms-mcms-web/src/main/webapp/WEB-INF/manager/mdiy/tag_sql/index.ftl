<!DOCTYPE html>
<html>
<head>
    <title>标签对应多个sql语句</title>
    <#include "../../include/head-file.ftl">
</head>
<body>
<div id="form" v-cloak>
    <el-header class="ms-header ms-tr" height="50px">
        <el-button type="primary" icon="iconfont icon-baocun" size="mini" @click="save()" :loading="saveDisabled">保存</el-button>
        <el-button size="mini" icon="iconfont icon-fanhui" plain onclick="javascript:history.go(-1)">返回</el-button>
    </el-header>
    <el-main class="ms-container">
        <el-scrollbar class="ms-scrollbar" style="height: 100%;">
        <el-form ref="form" :model="form" label-width="100px" size="mini">
            <el-form-item  label="标签名称" prop="tagName">
                <el-input v-model="tagName"
                          :disabled="true"
                          :style="{width:  '50%'}"
                          :clearable="true"
                          placeholder="">
                </el-input>
            </el-form-item>
            <el-form-item  label="自定义sql" prop="tagSql">
                <el-input
                        type="textarea" :rows="30"
                        :disabled="false"
                        v-model="form.tagSql"
                        :style="{width: '100%'}"
                        placeholder="请输入自定义sql支持ftl写法">
                </el-input>
            </el-form-item>
        </el-form>
        </el-scrollbar>
    </el-main>
</div>
</body>
</html>
<script>
    var form = new Vue({
        el: '#form',
        data: function() {
            return {
                saveDisabled: false,
                //表单数据
                form: {
                    // 自定义标签编号
                    tagId: '',
                    // 排序
                    sort: 0,
                    // 自定义sql支持ftl写法
                    tagSql: ''
                },
                rules: {}
            };
        },
        watch: {},
        computed: {},
        methods: {
            save: function() {
                var that = this;
                var url = ms.manager + "/mdiy/tagSql/save.do";

                if (that.form.id) {
                    url = ms.manager + "/mdiy/tagSql/update.do";
                }

                this.$refs.form.validate(function (valid) {
                    if (valid) {
                        that.saveDisabled = true;
                        var data = JSON.parse(JSON.stringify(that.form));
                        ms.http.post(url, data).then(function (data) {
                            if (data.data.id > 0) {
                                that.$notify({
                                    title: '成功',
                                    message: '保存成功',
                                    type: 'success'
                                });
                                that.saveDisabled = false;
                                window.history.back(-1); //刷新页面

                                self.location = document.referrer;
                            } else {
                                that.$notify({
                                    title: '失败',
                                    message: data.msg,
                                    type: 'warning'
                                });
                                that.saveDisabled = false;
                            }
                        });
                    } else {
                        return false;
                    }
                });
            },
            //获取当前标签对应多个sql语句
            get: function(tagId) {
                var that = this;
                ms.http.get(ms.manager + "/mdiy/tagSql/list.do", {
                    "tagId": tagId
                }).then(function (data) {
                    if (data.data.total > 0) {
                        that.form = data.data.rows[0];
                    }
                }).catch(function (err) {
                    console.log(err);
                });
            }
        },
        created: function() {
            this.tagName = ms.util.getParameter("tagName");
            this.form.tagId = ms.util.getParameter("tagId");

            if (this.form.tagId) {
                this.get(this.form.tagId);
            }
        }
    });
</script>
