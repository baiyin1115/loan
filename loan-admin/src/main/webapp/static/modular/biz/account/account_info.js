/**
 * 初始化账户详情对话框
 */
var AccountDlg = {
  accountInfoData: {},
  // zTreeInstance: null,
  validateFields: {
    acctType: {validators: {notEmpty: {message: '账户类型'}}},
    balanceType: {validators: {notEmpty: {message: '余额性质'}}},
    name: {validators: {notEmpty: {message: '账户姓名'}}},
    status: {validators: {notEmpty: {message: '账户状态'}}}
  }
};

/**
 * 清除数据
 */
AccountDlg.clearData = function () {
  this.accountInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
AccountDlg.set = function (key, val) {
  this.accountInfoData[key] = (typeof value == "undefined") ? $("#" + key).val()
      : value;
  //alert($("#" + key).val())
  return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
AccountDlg.get = function (key) {
  return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
AccountDlg.close = function () {
  parent.layer.close(window.parent.Account.layerIndex);
}

/**
 * 收集数据
 */
AccountDlg.collectData = function () {
  this.set('id').set('userNo').set('name').set('availableBalance').set('freezeBalance').set('acctType').set('balanceType')
  .set('status').set('createBy').set('modifiedBy').set('createAt').set('updateAt').set('remark')   ;
}

/**
 * 验证数据是否为空
 */
AccountDlg.validate = function () {

  $('#accountInfoForm').data("bootstrapValidator").resetForm();
  $('#accountInfoForm').bootstrapValidator('validate');
  return $("#accountInfoForm").data('bootstrapValidator').isValid();
}

/**
 * 提交添加账户
 */
AccountDlg.addSubmit = function () {

  this.clearData();
  this.collectData();

  if (!this.validate()) {
    return;
  }

  //提交信息
  var ajax = new $ax(Feng.ctxPath + "/account/add", function (data) {
    Feng.success("添加成功!");
    window.parent.Account.table.refresh();
    AccountDlg.close();
  }, function (data) {
    Feng.error("添加失败!" + data.responseJSON.message + "!");
  });
  ajax.set(this.accountInfoData);
  ajax.setContentType("application/json")
  ajax.start();
}

/**
 * 提交修改
 */
AccountDlg.editSubmit = function () {

  this.clearData();
  this.collectData();

  if (!this.validate()) {
    return;
  }

  //提交信息
  var ajax = new $ax(Feng.ctxPath + "/account/update", function (data) {
    Feng.success("修改成功!");
    window.parent.Account.table.refresh();
    AccountDlg.close();
  }, function (data) {
    Feng.error("修改失败!" + data.responseJSON.message + "!");
  });
  ajax.set(this.accountInfoData);
  ajax.setContentType("application/json")
  ajax.start();
}

$(function () {
  Feng.initValidator("accountInfoForm", AccountDlg.validateFields);

  //初始化
  $("#acctType").val($("#acctTypeValue").val());
  $("#balanceType").val($("#balanceTypeValue").val());
  if($("#statusValue") == null || $("#statusValue").val() == ""){
    $("#status").val(1); //正常
  }
  else{
    $("#status").val($("#statusValue").val());
  }
});
