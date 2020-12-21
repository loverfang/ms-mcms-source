<el-dialog id="form" v-cloak :title="form.name" :visible.sync="dialogVisible" width="80%">
    <el-main>
        <codemirror v-model="form.fileContent" :options="cmOption">
        </codemirror>
    </el-main>
    <div slot="footer">
        <el-button type="primary" icon="iconfont icon-baocun" size="mini" @click="save()">保存</el-button>
    </div>
</el-dialog>
<script>
    Vue.use(VueCodemirror);
    var form = new Vue({
        el: '#form',
        data: function () {
            return {
                saveDisabled: false,
                dialogVisible: false,
                //设置
                cmOption: {
                    tabSize: 4,
                    styleActiveLine: true,
                    lineNumbers: true,
                    line: true,
                    styleSelectedText: true,
                    lineWrapping: true,
                    mode: 'text/html',
                    matchBrackets: true,
                    showCursorWhenSelecting: true,
                    hintOptions: {
                        completeSingle: false
                    }
                },
                fileName: "",
                //表单数据
                form: {
                    // 文件名称
                    name: '',
                    fileName: '',
                    fileNamePrefix: '',
                    fileContent: ''
                },
                rules: {}
            };
        },
        watch: {},
        computed: {},
        methods: {
            open: function (path) {
                if (path) {
                    this.get(path);
                }

                this.$nextTick(function () {
                    this.dialogVisible = true;
                });
            },
            save: function () {
                var that = this;
                var url = ms.manager + "/template/writeFileContent.do";
                that.saveDisabled = true;
                that.form.oldFileName = that.form.fileNamePrefix + that.form.name;
                delete that.form.oldFileContent;
                that.$confirm('确定修改该文件吗？', '修改文件', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(function () {
                    ms.http.post(url, that.form).then(function (data) {
                        if (data.result) {
                            that.$notify({
                                title: '成功',
                                message: '修改成功',
                                type: 'success'
                            });
                            that.dialogVisible = false;
                            indexVue.list(indexVue.dataStack.pop().path);
                        } else {
                            that.$notify({
                                title: '失败',
                                message: data.msg,
                                type: 'warning'
                            });
                        }

                        that.saveDisabled = false;
                    });
                }).catch(function () {
                    that.$notify({
                        type: 'info',
                        message: '已取消修改'
                    });
                });
            },
            //获取当前模板管理
            get: function (fileName) {
                var that = this;
                ms.http.get(ms.manager + "/template/readFileContent.do", {
                    "fileName": fileName
                }).then(function (res) {
                    that.form = res.data;
                }).catch(function (err) {
                    console.log(err);
                });
            }
        },
        created: function () {}
    });
</script>
<style>
    .CodeMirror {
        border: 1px solid #eee;
        height: auto;
    }

    .CodeMirror-scroll {
        height: calc(100vh - 330px);
        overflow-y: hidden;
        overflow-x: auto;
    }
    .el-dialog{
        margin-top: 8vh!important;
    }
</style>
