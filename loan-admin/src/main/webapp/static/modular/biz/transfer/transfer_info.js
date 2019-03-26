/**
 * 初始化详情对话框
 */
var TransferInfoDlg = {
  transferInfoData: {},
  typeInstance: null,
  inAcctTreeInstance: null,
  outAcctTreeInstance: null,
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
  this.set('id').set('orgNo').set('type').set('acctNo').set('externalAcct').set('amt')
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

  //----------------------------------------------------------------------------
  //TODO ---账户要以列表的形式展示
  //账户树
  var tree = new $ZTree("inAcctTree",
      "/account/selectCompanyAcctTreeList");
  tree.bindOnClick(TransferInfoDlg.onClickInAcct);
  tree.init();
  TransferInfoDlg.inAcctTreeInstance = tree;

  var tree = new $ZTree("outAcctTree",
      "/account/selectCompanyAcctTreeList");
  tree.bindOnClick(TransferInfoDlg.onClickOutAcct);
  tree.init();
  TransferInfoDlg.outAcctTreeInstance = tree;

});


//账户部分---------------------------------------------------------------------------------
/**
 * 显示父级菜单选择的树--入账账户
 */
TransferInfoDlg.showInAcctSelectTree = function () {
  Feng.showInputTree("inAcctName", "inAcctTreeDiv", 15, 34);
};

/**
 * 点击父级编号input框时
 */
TransferInfoDlg.onClickInAcct = function (e, treeId, treeNode) {
  $("#inAcctName").attr("value",
      TransferInfoDlg.inAcctTreeInstance.getSelectedVal());
  $("#inAcctNo").attr("value", treeNode.id);
};

/**
 * 显示父级菜单选择的树--出账账户
 */
TransferInfoDlg.showOutAcctSelectTree = function () {
  Feng.showInputTree("outAcctName", "outAcctTreeDiv", 15, 34);
};

/**
 * 点击父级编号input框时
 */
TransferInfoDlg.onClickInAcct = function (e, treeId, treeNode) {
  $("#outAcctName").attr("value",
      TransferInfoDlg.outAcctTreeInstance.getSelectedVal());
  $("#outAcctNo").attr("value", treeNode.id);
};
