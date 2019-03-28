/**
 * 初始化详情对话框
 */
var TransferInfoDlg = {
  transferInfoData: {},
  typeInstance: null,
  accountLayerIndex: null,
  accountType: null,
  validateFields: {
    orgNo: {validators: {notEmpty: {message: '公司编号'}}},
    type: {validators: {notEmpty: {message: '用途'}}},
    amt: {validators: {notEmpty: {message: '金额'}}},
  }
};


/**
 * 清除数据
 */
TransferInfoDlg.clearData = function () {
  this.transferInfoData = {};
};

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
TransferInfoDlg.set = function (key, val) {

  if ($("#" + key).attr("id") == "amt") {
    this.transferInfoData[key] = (typeof value == "undefined") ? Feng.parseMoney(
        $("#" + key).val()) : value;
  } else {
    this.transferInfoData[key] = (typeof value == "undefined") ? $("#" + key).val()
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
TransferInfoDlg.get = function (key) {
  return $("#" + key).val();
};

/**
 * 关闭此对话框
 */
TransferInfoDlg.close = function () {
  parent.layer.close(window.parent.Transfer.layerIndex);
};

/**
 * 收集数据
 */
TransferInfoDlg.collectData = function () {
  this.set('id').set('orgNo').set('type').set('inAcctNo').set('outAcctNo').set('amt')
  .set('acctDate').set('status').set('createBy').set('modifiedBy').set('createAt').set('updateAt').set('remark')
  ;
};

/**
 * 验证数据是否为空
 */
TransferInfoDlg.validate = function (ob) {

  ob.data("bootstrapValidator").resetForm();
  ob.bootstrapValidator('validate');
  return ob.data('bootstrapValidator').isValid();
};

/**
 * 提交
 */
TransferInfoDlg.addSubmit = function () {

  this.clearData();
  this.collectData();

  if (!this.validate($('#transferInfoForm'))) {
    return;
  }

  //提交信息
  var ajax = new $ax(Feng.ctxPath + "/transfer/add", function (data) {
    Feng.success("添加成功!");
    window.parent.Transfer.table.refresh();
    TransferInfoDlg.close();
  }, function (data) {
    Feng.error("添加失败!" + data.responseJSON.message + "!");
  });
  ajax.set(this.transferInfoData);
  ajax.setContentType("application/json")
  ajax.start();
};

/**
 * 提交修改
 */
TransferInfoDlg.editSubmit = function () {

  this.clearData();
  this.collectData();

  if (!this.validate($('#transferInfoForm'))) {
    return;
  }

  //提交信息
  var ajax = new $ax(Feng.ctxPath + "/transfer/update", function (data) {
    Feng.success("修改成功!");
    window.parent.Transfer.table.refresh();
    TransferInfoDlg.close();
  }, function (data) {
    Feng.error("修改失败!" + data.responseJSON.message + "!");
  });
  ajax.set(this.transferInfoData);
  ajax.setContentType("application/json")
  ajax.start();
};

/**
 * 初始化
 */
$(function () {

  Feng.initValidator("transferInfoForm", TransferInfoDlg.validateFields);

  //初始化
  $("#orgNo").val($("#orgNoValue").val());
  $("#type").val($("#typeValue").val());

  //格式化
  $("#amt").val(Feng.formatMoney($("#amt").val(), 2));
  //绑定格式化事件
  $('#amt').bind('blur', function () {
    Feng.formatAmt($('#amt'));
  });

});


//账户--------------------------------------------------------------------------------------
/**
 * 打开入账账户
 */
TransferInfoDlg.openInAccountList = function () {
  var index = layer.open({
    type: 2,
    title: '选择账户',
    area: ['1000px', '750px'], //宽高
    fix: true, //不固定
    maxmin: true,
    content: Feng.ctxPath + '/account/popup_account_list/-1'
  });
  this.accountLayerIndex = index;
  this.accountType = "in";
};

/**
 * 打开出账账户
 */
TransferInfoDlg.openOutAccountList = function () {
  var index = layer.open({
    type: 2,
    title: '选择账户',
    area: ['1000px', '750px'], //宽高
    fix: false, //不固定
    maxmin: true,
    content: Feng.ctxPath + '/account/popup_account_list/-1'
  });
  this.accountLayerIndex = index;
  this.accountType = "out";
};
