/**
 * 初始化客户详情对话框
 */
var CustomerInfoDlg = {
  customerInfoData: {},
  // zTreeInstance: null,
  validateFields: {
    certNo: {validators: {notEmpty: {message: '证件号码'}}},
    certType: {validators: {notEmpty: {message: '证件类型'}}},
    name: {validators: {notEmpty: {message: '客户姓名'}}},
    sex: {validators: {notEmpty: {message: '性别'}}},
    mobile: {validators: {notEmpty: {message: '手机号'}}},
    type: {validators: {notEmpty: {message: '客户类型'}}},
    status: {validators: {notEmpty: {message: '客户状态'}}}
  }
};

/**
 * 清除数据
 */
CustomerInfoDlg.clearData = function () {
  this.customerInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
CustomerInfoDlg.set = function (key, val) {
  this.customerInfoData[key] = (typeof value == "undefined") ? $("#" + key).val()
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
CustomerInfoDlg.get = function (key) {
  return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
CustomerInfoDlg.close = function () {
  parent.layer.close(window.parent.Customer.layerIndex);
}

/**
 * 收集数据
 */
CustomerInfoDlg.collectData = function () {
  this.set('id').set('certNo').set('certType').set('name').set('sex').set('mobile').set('phone').set('email')
  .set('type').set('status').set('createBy').set('modifiedBy').set('createAt').set('updateAt').set('remark')   ;
}

/**
 * 验证数据是否为空
 */
CustomerInfoDlg.validate = function () {

  $('#customerInfoForm').data("bootstrapValidator").resetForm();
  $('#customerInfoForm').bootstrapValidator('validate');
  return $("#customerInfoForm").data('bootstrapValidator').isValid();
}

/**
 * 提交添加客户
 */
CustomerInfoDlg.addSubmit = function () {

  this.clearData();
  this.collectData();

  if (!this.validate()) {
    return;
  }

  //提交信息
  var ajax = new $ax(Feng.ctxPath + "/customer/add", function (data) {
    Feng.success("添加成功!");
    window.parent.Customer.table.refresh();
    CustomerInfoDlg.close();
  }, function (data) {
    Feng.error("添加失败!" + data.responseJSON.message + "!");
  });
  ajax.set(this.customerInfoData);
  ajax.setContentType("application/json")
  ajax.start();
}

/**
 * 提交修改
 */
CustomerInfoDlg.editSubmit = function () {

  this.clearData();
  this.collectData();

  if (!this.validate()) {
    return;
  }

  //提交信息
  var ajax = new $ax(Feng.ctxPath + "/customer/update", function (data) {
    Feng.success("修改成功!");
    window.parent.Customer.table.refresh();
    CustomerInfoDlg.close();
  }, function (data) {
    Feng.error("修改失败!" + data.responseJSON.message + "!");
  });
  ajax.set(this.customerInfoData);
  ajax.setContentType("application/json")
  ajax.start();
}

$(function () {
  Feng.initValidator("customerInfoForm", CustomerInfoDlg.validateFields);

  //初始化
  $("#sex").val($("#sexValue").val());
  $("#certType").val($("#certTypeValue").val());
  $("#type").val($("#typeValue").val());
  if($("#statusValue") == null || $("#statusValue").val() == ""){
    $("#status").val(1); //正常
  }
  else{
    $("#status").val($("#statusValue").val());
  }
});
