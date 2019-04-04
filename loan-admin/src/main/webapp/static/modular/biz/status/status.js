/**
 * 初始化详情对话框
 */
var StatusDlg = {
  statusInfoData: {},
  typeInstance: null,
  acctTreeInstance: null,
  validateFields: {
    acctDate: {validators: {notEmpty: {message: '业务日期'}}},
    settlementFlag: {validators: {notEmpty: {message: '结转标志'}}},
  }
};


/**
 * 清除数据
 */
StatusDlg.clearData = function () {
  this.statusInfoData = {};
};

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
StatusDlg.set = function (key, val) {

  if ($("#" + key).attr("id") == "amt") {
    this.statusInfoData[key] = (typeof value == "undefined") ? Feng.parseMoney(
        $("#" + key).val()) : value;
  } else {
    this.statusInfoData[key] = (typeof value == "undefined") ? $("#" + key).val()
        : value;
  }

  return this;
};

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
StatusDlg.get = function (key) {
  return $("#" + key).val();
};

/**
 * 关闭此对话框
 */
StatusDlg.close = function () {
  parent.layer.close(window.parent.Inout.layerIndex);
};

/**
 * 收集数据
 */
StatusDlg.collectData = function () {
  this.set('id').set('acctDate').set('settlementFlag');
};

/**
 * 验证数据是否为空
 */
StatusDlg.validate = function (ob) {

  ob.data("bootstrapValidator").resetForm();
  ob.bootstrapValidator('validate');
  return ob.data('bootstrapValidator').isValid();
};

/**
 * 提交
 */
StatusDlg.addSubmit = function () {

  this.clearData();
  this.collectData();

  if (!this.validate($('#statusInfoForm'))) {
    return;
  }

  //提交信息
  var ajax = new $ax(Feng.ctxPath + "/status/update", function (data) {
    Feng.success("修改成功!");
    $("#settlementFlag").val(data.settlementFlag);
    $("#settlementFlagValue").val(data.settlementFlagValue);
    $("#acctDate").val(data.acctDate);
  }, function (data) {
    Feng.error("修改失败!" + data.responseJSON.message + "!");
  });
  ajax.set(this.statusInfoData);
  ajax.setContentType("application/json")
  ajax.start();
};

/**
 * 初始化
 */
$(function () {

  Feng.initValidator("statusInfoForm", StatusDlg.validateFields);

  //初始化
  $("#settlementFlag").val($("#settlementFlagValue").val());

});

