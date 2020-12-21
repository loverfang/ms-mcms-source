<el-dialog v-cloak id="form" title="标签" :visible.sync="dialogVisible" width="50%">
    <el-form ref="form" :model="form" :rules="rules" label-width="100px" size="mini">
        <el-form-item label="标签名称" prop="tagName">
            <el-input v-model="form.tagName"
                      :disabled="false"
                      :style="{width:  '100%'}"
                      :clearable="true"
                      placeholder="请输入标签名称">
            </el-input>
        </el-form-item>
        <el-form-item label="标签类型" prop="tagType">
            <template slot='label'>标签类型
                <el-popover placement="top-start" title="提示" trigger="hover" content="类型不满足可以在自定义字典菜单中新增,字段类型为“标签类型”">
                    <i class="el-icon-question" slot="reference"></i>
                </el-popover>
            </template>
            <el-select v-model="form.tagType"
                       :style="{width: ''}"
                       :filterable="false"
                       :disabled="false"
                       :multiple="false" :clearable="true"
                       placeholder="请选择标签类型">
                <el-option v-for='item in tagTypeOptions' :key="item.dictValue" :value="item.dictValue"
                           :label="item.dictLabel"></el-option>
            </el-select>
        </el-form-item>
        <el-form-item label="描述" prop="tagDescription">
            <el-input
                    type="textarea" :rows="5"
                    :disabled="false"
                    v-model="form.tagDescription"
                    :style="{width: '100%'}"
                    placeholder="请输入描述">
            </el-input>
        </el-form-item>
    </el-form>
    <div slot="footer">
        <el-button size="mini" @click="dialogVisible = false">取 消</el-button>
        <el-button size="mini" type="primary" @click="save()" :loading="saveDisabled">保存</el-button>
    </div>
</el-dialog>
<script>
    var form = new Vue({
        el: '#form',
        data: function() {
            return {
                saveDisabled: false,
                dialogVisible: false,
                //表单数据
                form: {
                    // 标签名称
                    tagName: '',
                    // 标签类型
                    tagType: '',
                    // 描述
                    tagDescription: ''
                },
                tagTypeOptions: null,
                rules: {
                    // 标签名称
                    tagName: [{
                        "required": true,
                        "message": "标签名称必须填写"
                    }],
                    // 标签类型
                    tagType: [{
                        "required": true,
                        "message": "标签类型必须选择"
                    }]
                }
            };
        },
        watch: {
            dialogVisible: function(v) {
                if (!v) {
                    this.$refs.form.resetFields();
                    this.form.id = 0;
                }
            }
        },
        computed: {},
        methods: {
            open: function(id) {
                if (id) {
                    this.get(id);
                }

                this.$nextTick(function () {
                    this.dialogVisible = true;
                });
            },
            save: function() {
                var that = this;
                var url = ms.manager + "/mdiy/tag/save.do";

                if (that.form.id > 0) {
                    url = ms.manager + "/mdiy/tag/update.do";
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
                                location.href = ms.manager + "/mdiy/tag/index.do";
                            } else {
                                that.$notify({
                                    title: '失败',
                                    message: data.msg,
                                    type: 'warning'
                                });
                                that.saveDisabled = false;
                            }

                            that.dialogVisible = false;
                        });
                    } else {
                        return false;
                    }
                });
            },
            //获取tagType数据源
            tagTypeOptionsGet: function() {
                var that = this;
                ms.http.get(ms.base + '/mdiy/dict/list.do', {
                    dictType: '标签类型',
                    pageSize: 99999
                }).then(function (data) {
                    that.tagTypeOptions = data.rows;
                }).catch(function (err) {
                    console.log(err);
                });
            },
            //获取当前标签
            get: function(id) {
                var that = this;
                ms.http.get(ms.manager + "/mdiy/tag/get.do", {
                    "id": id
                }).then(function (data) {
                    if (data.data.id) {
                        that.form = data.data;
                        that.form.tagType += "";
                    }
                }).catch(function (err) {
                    console.log(err);
                });
            }
        },
        created: function () {
            this.tagTypeOptionsGet();
        }
    });
</script>
