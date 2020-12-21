<!DOCTYPE html>
<html>
<head>
    <title>用户</title>
    <#include "../../include/head-file.ftl">
</head>
<body>
<div id="form" v-cloak>
    <el-header class="ms-header ms-tr" height="50px">
        <el-button type="primary" icon="iconfont icon-baocun" size="mini" @click="save()" :loading="saveDisabled">保存
        </el-button>
        <el-button size="mini" plain onclick="javascript:history.go(-1)"><i class="iconfont icon-fanhui"></i>返回</el-button>
    </el-header>
    <el-scrollbar style="height: calc(100vh - 50px)">
        <el-form ref="form" :model="form" :rules="rules" label-width="100px" size="mini">
        <el-main class="ms-container ms-margin-bottom-zero">
            <el-row>
                <el-col :span="2"><span class="ms-border-font ms-default-line-height">账户信息</span></el-col>
            </el-row>
            <el-row
                    gutter="0"
                    justify="start" align="top">
                <el-col span="12">
                    <el-form-item label="用户名" prop="peopleName">
                        <el-input v-model="form.peopleName"
                                  :disabled="false"
                                  :style="{width:  '100%'}"
                                  :clearable="true"
                                  placeholder="请输入用户名">
                        </el-input>
                    </el-form-item>
                </el-col>
                <el-col span="12">
                    <el-form-item label="密码" prop="peoplePassword">
                        <el-input v-model="form.peoplePassword"
                                  type="password"
                                  autocomplete='new-password'
                                  :disabled="false"
                                  :style="{width:  '100%'}"
                                  :clearable="true"
                                  placeholder="请输入密码">
                        </el-input>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row
                    gutter="0"
                    justify="start" align="top">
                <el-col span="12">
                    <el-form-item label="手机号码" prop="peoplePhone">
                        <el-input v-model="form.peoplePhone"
                                  :disabled="false"
                                  :style="{width:  '100%'}"
                                  :clearable="true"
                                  placeholder="请输入手机号码">
                        </el-input>
                    </el-form-item>
                </el-col>
                <el-col span="12">
                    <el-form-item  label="手机绑定" prop="peoplePhoneCheck">
                        <el-switch v-model="form.peoplePhoneCheck"
                                   :active-value="1"
                                   :inactive-value="0"
                                   :disabled="false">
                        </el-switch>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row
                    gutter="0"
                    justify="start" align="top">
                <el-col span="12">
                    <el-form-item label="邮箱" prop="peopleMail">
                        <el-input v-model="form.peopleMail"
                                  :disabled="false"
                                  :style="{width:  '100%'}"
                                  :clearable="true"
                                  placeholder="请输入邮箱">
                        </el-input>
                    </el-form-item>
                </el-col>
                <el-col span="12">
                    <el-form-item  label="邮箱绑定" prop="peopleMailCheck">
                        <el-switch v-model="form.peopleMailCheck"
                                   :active-value="1"
                                   :inactive-value="0"
                                   :disabled="false">
                        </el-switch>
                    </el-form-item>
                </el-col>
            </el-row>
        </el-main>
        <el-footer class="ms-footer ms-margin-bottom-zero">
            <el-row>
                <el-col :span="2"><span class="ms-border-font ms-default-line-height">用户信息</span></el-col>
            </el-row>
                <el-row
                        gutter="0"
                        justify="start" align="top">
                    <el-col span="12">
                        <el-form-item label="昵称" prop="puNickname">
                            <el-input v-model="form.puNickname"
                                      :disabled="false"
                                      :style="{width:  '100%'}"
                                      :clearable="true"
                                      placeholder="请输入昵称">
                            </el-input>
                        </el-form-item>
                    </el-col>
                    <el-col span="12">
                        <el-form-item label="性别" prop="puSex">
                            <el-select v-model="form.puSex"
                                       :style="{width: '100%'}"
                                       :disabled="false"
                                       :multiple="false" :clearable="true"
                                       placeholder="请选择性别">
                                <el-option v-for='item in puSexOptions' :key="item.value" :value="item.value"
                                           :label="item.label"
                                           :label="false?item.label:item.value"></el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row
                        gutter="0"
                        justify="start" align="top">
                    <el-col span="12">
                        <el-form-item label="真实姓名" prop="puRealName">
                            <el-input v-model="form.puRealName"
                                      :disabled="false"
                                      :style="{width:  '100%'}"
                                      :clearable="true"
                                      placeholder="请输入真实姓名">
                            </el-input>
                        </el-form-item>
                    </el-col>
                    <el-col span="12">
                        <el-form-item label="身份证" prop="puCard">
                            <el-input v-model="form.puCard"
                                      :disabled="false"
                                      :style="{width:  '100%'}"
                                      :clearable="true"
                                      placeholder="请输入身份证">
                            </el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row
                        gutter="0"
                        justify="start" align="top">
                    <el-col span="12">
                        <el-form-item  label="生日" prop="puBirthday">
                            <el-date-picker
                                    v-model="form.puBirthday"
                                    placeholder="请选择用户生日"
                                    :readonly="false"
                                    :disabled="false"
                                    :editable="true"
                                    :clearable="true"
                                    format="yyyy-MM-dd"
                                    value-format="yyyy-MM-dd"
                                    :style="{width:'100%'}"
                                    type="date">
                            </el-date-picker>
                        </el-form-item>
                    </el-col>
                    <el-col span="12">
                        <el-form-item  label="用户状态" prop="peopleState">
                            <el-switch v-model="form.peopleState"
                                       :active-value="1"
                                       :inactive-value="0"
                                       :disabled="false">
                            </el-switch>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row
                        gutter="0"
                        justify="start" align="top">
                    <el-col span="12">
                        <el-form-item  label="用户等级" prop="puLevel">
                            <template slot='label'>用户等级
                                <el-popover placement="top-start" title="提示" trigger="hover">
                                    类型不满足可以在自定义字典菜单中新增,字段类型为“用户等级类型”
                                    <i class="el-icon-question" slot="reference"></i>
                                </el-popover>
                            </template>
                            <el-select v-model="form.puLevel"
                                       @change="puLevelSelectChange"
                                       :style="{width: '100%'}"
                                       :filterable="false"
                                       :disabled="false"
                                       :multiple="false" :clearable="true"
                                       placeholder="请选择用户等级">
                                <el-option v-for='item in puLevelOptions' :key="item.dictValue" :value="item.dictValue"
                                           :label="item.dictLabel"></el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-form-item label="头像" prop="puIcon">
                    <el-upload
                            :file-list="form.puIcon"
                            :action="ms.base+'/file/upload.do'"
                            :on-remove="puIconhandleRemove"
                            :style="{width:''}"
                            :limit="1"
                            :disabled="false"
                            :data="{uploadPath:'/people/user','isRename':true,'appId':true}"
                            :on-success="puIconBasicPicSuccess"
                            :on-exceed="puIconhandleExceed"
                            accept="image/*"
                            list-type="picture-card">
                        <i class="el-icon-plus"></i>
                        <div slot="tip" class="el-upload__tip">最多上传1张头像</div>
                    </el-upload>
                </el-form-item>
                <el-form-item  label="城市选择" prop="provinceId">
                    <el-row gutter="10" justify="start" align="top">
                        <el-col span="6">
                            <el-select v-model="form.provinceId"
                                       :style="{width: '100%'}"
                                       :disabled="false"
                                       :multiple="false" :clearable="true"
                                       @change="form.cityId='';form.cityName='';form.countyId='';form.countyName='';form.provinceName=cityData(provinces,$event).name"
                                       placeholder="请选择省份">
                                <el-option v-for='(item,index) in provinces' :key="index" :value="item.id"
                                           :data-label='item.name' :label="item.name"></el-option>
                            </el-select>
                        </el-col>
                        <el-col span="8">

                            <el-select v-model="form.cityId"
                                       :style="{width: '100%'}"
                                       :disabled="false"
                                       :multiple="false" :clearable="true"
                                       @change="form.countyId='';form.countyName='';form.cityName=cityData(cityData(provinces,form.provinceId).childrensList,$event).name"
                                       placeholder="请选择城市">
                                <el-option v-for='(item,index) in cityData(provinces,form.provinceId).childrensList' :key="index" :value="item.id"
                                           :data-label='item.name' :label="item.name"></el-option>
                            </el-select>
                        </el-col>
                        <el-col span="8">
                            <el-select v-model="form.countyId"
                                       :style="{width: '100%'}"
                                       :disabled="false"
                                       :multiple="false" :clearable="true"
                                       @change="form.countyName=cityData(cityData(cityData(provinces,form.provinceId).childrensList,form.cityId).childrensList,$event).name"
                                       placeholder="请选择区域">
                                <el-option v-for='(item,index) in cityData(cityData(provinces,form.provinceId).childrensList,form.cityId).childrensList' :key="index" :value="item.id"
                                           :data-label='item.name' :label="item.name"></el-option>
                            </el-select>
                        </el-col>
                    </el-row>
                </el-form-item>
                <el-form-item label="地址" prop="puAddress">
                    <el-input
                            type="textarea" :rows="5"
                            :disabled="false"
                            v-model="form.puAddress"
                            :style="{width: '100%'}"
                            placeholder="请输入地址">
                    </el-input>
                </el-form-item>
            </el-footer>
    </el-form>
    </el-scrollbar>
</div>
</body>
</html>
<script>
    var form = new Vue({
        el: '#form',
        data: function () {
            return {
                saveDisabled: false,
                provinces: [],
                //表单数据
                form: {
                    // 用户名
                    peopleName: '',
                    // 密码
                    peoplePassword: '',
                    // 昵称
                    puNickname: '',
                    // 性别
                    puSex: '',
                    //用户生日
                    puBirthday: '',
                    // 真实姓名
                    puRealName: '',
                    // 身份证
                    puCard: '',
                    // 手机号码
                    peoplePhone: '',
                    // 邮箱
                    peopleMail: '',
                    // 手机验证
                    peoplePhoneCheck: '',
                    // 邮箱验证
                    peopleMailCheck: '',
                    // 用户状态
                    peopleState: '',
                    // 头像
                    puIcon: '',
                    // 城市选择
                    provinceId: '',
                    // 省
                    provinceName: '',
                    // 城市
                    cityName: '',
                    // 区
                    countyName: '',
                    // 城市id
                    cityId: '',
                    // 区id
                    countyId: '',
                    // 地址
                    puAddress: '',
                    //用户等级
                    puLevel: null,
                    //用户等级名称
                    puLevelName: null
                },
                puLevelOptions: [],
                puSexOptions: [{
                    "value": 1,
                    "label": "男"
                }, {
                    "value": 2,
                    "label": "女"
                }],
                rules: {
                    // 用户名
                    peopleName: [{
                        "required": true,
                        "message": "用户名必须填写"
                    }, {
                        "pattern": "^[^[!@#$%^&*()_+-/~?！@#￥%…&*（）——+—？》《：“‘’]+$",
                        "message": "用户名格式不匹配"
                    }, {
                        "min": 1,
                        "max": 50,
                        "message": "用户名长度必须为1-50"
                    }],
                    // 密码
                    peoplePassword: [{
                        "min": 1,
                        "max": 50,
                        "message": "密码长度必须为1-50"
                    }],
                    // 昵称
                    puNickname: [{
                        "required": true,
                        "message": "昵称必须填写"
                    }, {
                        "min": 1,
                        "max": 50,
                        "message": "昵称长度必须为1-50"
                    }],
                    // 真实姓名
                    puRealName: [{
                        "min": 1,
                        "max": 50,
                        "message": "真实姓名长度必须为1-50"
                    }],
                    // 身份证
                    puCard: [{
                        "pattern": "^[X0-9]+$",
                        "message": "身份证格式不匹配"
                    }, {
                        "min": 1,
                        "max": 50,
                        "message": "身份证长度必须为1-50"
                    }],
                    // 手机号码
                    peoplePhone: [{
                        "pattern": /^[1][0-9]{10}$/,
                        "message": "手机号码格式不匹配"
                    }],
                    // 邮箱
                    peopleMail: [{
                        "pattern": "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$",
                        "message": "邮箱格式不匹配"
                    }, {
                        "min": 1,
                        "max": 50,
                        "message": "邮箱长度必须为1-50"
                    }]
                }
            };
        },
        watch: {},
        computed: {
            cityData: function (data, id) {
                return function (data, id) {
                    var city = [];

                    for (var i in data) {
                        if (data[i].id == id) {
                            city = data[i];
                            break;
                        }
                    }

                    return city;
                };
            }
        },
        methods: {
            //对象拷贝 用来去除多余额数据
            objectCopy: function (src, o) {
                for (key in src) {
                    if (o[key] != undefined) {
                        src[key] = o[key];
                    }
                }
            },
            save: function () {
                var that = this;
                var url = ms.manager + "/people/peopleUser/save.do";

                if (that.form.puPeopleId > 0) {
                    url = ms.manager + "/people/peopleUser/update.do";
                }

                this.$refs.form.validate(function (valid) {
                    if (valid) {
                        that.saveDisabled = true;
                        var data = JSON.parse(JSON.stringify(that.form));
                        ms.http.post(url, data).then(function (data) {
                            if (data.result) {
                                that.$notify({
                                    title: '成功',
                                    message: '保存成功',
                                    type: 'success'
                                });
                                that.saveDisabled = false;
                                location.href = ms.manager + "/people/peopleUser/index.do";
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
            //获取当前用户
            get: function (id) {
                var that = this;
                ms.http.get(ms.manager + "/people/peopleUser/get.do", {
                    "puPeopleId": id
                }).then(function (data) {
                    if (data.result) {
                        that.objectCopy(that.form, data.data);
                        that.form.peopleDateTime = '';
                    }
                }).catch(function (err) {
                    console.log(err);
                });
            },
            puLevelSelectChange: function (level) {
                var dict = this.puLevelOptions.find(function (item) {
                    return item.dictValue == level;
                });
                this.form.puLevelName = dict.dictLabel;
            },
            getCityData: function () {
                var that = this;
                ms.http.get(ms.base + "/basic/city/query.do", {
                    pageSize: 9999
                }).then(function (data) {
                    that.provinces = data;
                });
            },
            //获取puLevel数据源
            puLevelOptionsGet: function () {
                var that = this;
                ms.http.get(ms.base + '/mdiy/dict/list.do', {
                    dictType: '用户等级类型',
                    pageSize: 99999
                }).then(function (res) {
                    that.puLevelOptions = res.rows;
                }).catch(function (err) {
                    console.log(err);
                });
            },
            //上传超过限制
            puIconhandleExceed: function (files, fileList) {
                this.$notify({
                    title: '当前最多上传1张头像',
                    type: 'warning'
                });
            },
            //puIcon文件上传完成回调
            puIconBasicPicSuccess: function (response, file, fileList) {
                this.form.puIcon = response; // this.form.puIcon.push({url: file.url, path: response, uid: file.uid});
            },
            puIconhandleRemove: function (file, files) {
                var index = -1;
                index = this.form.puIcon.findIndex(function (text) {
                    return text == file;
                });

                if (index != -1) {
                    this.form.puIcon.splice(index, 1);
                }
            }
        },
        created: function () {
            this.puLevelOptionsGet();
            this.getCityData();
            this.form.puPeopleId = ms.util.getParameter("puPeopleId");

            if (this.form.puPeopleId) {
                this.get(this.form.puPeopleId);
            } else {
                this.rules.peoplePassword.push({
                    "required": true,
                    "message": "密码必须填写"
                });
            }
        }
    });
</script>
<style>
    #form .ms-container {
        margin: 12px;
        height: calc(100% - 24px);
        padding: 14px;
        background: #fff;
    }
    #form .ms-footer {
        margin: 0px 12px 12px 12px;
        height: 100% !important;
        padding: 14px;
        background: #fff;
    }
</style>
