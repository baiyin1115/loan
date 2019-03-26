/**
 * 初始化详情对话框
 */
var InoutInfoDlg = {
  inoutInfoData: {},
  typeInstance: null,
  acctTreeInstance: null,
  validateFields: {
    orgNo: {validators: {notEmpty: {message: '公司编号'}}},
    type: {validators: {notEmpty: {message: '用途'}}},
    amt: {validators: {notEmpty: {message: '金额'}}},
    acctNo: {validators: {notEmpty: {message: '账户'}}}
  }
};


/**
 * 清除数据
 */
InoutInfoDlg.clearData = function () {
  this.inoutInfoData = {};
};

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
InoutInfoDlg.set = function (key, val) {

  if ($("#" + key).attr("id") == "amt") {
    this.inoutInfoData[key] = (typeof value == "undefined") ? Feng.parseMoney(
        $("#" + key).val()) : value;
  } else {
    this.inoutInfoData[key] = (typeof value == "undefined") ? $("#" + key).val()
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
InoutInfoDlg.get = function (key) {
  return $("#" + key).val();
};

/**
 * 关闭此对话框
 */
InoutInfoDlg.close = function () {
  parent.layer.close(window.parent.Inout.layerIndex);
};

/**
 * 收集数据
 */
InoutInfoDlg.collectData = function () {
  this.set('id').set('orgNo').set('type').set('acctNo').set('externalAcct').set('amt')
  .set('acctDate').set('status').set('createBy').set('modifiedBy').set('createAt').set('updateAt').set('remark')
  ;
};

/**
 * 验证数据是否为空
 */
InoutInfoDlg.validate = function (ob) {

  ob.data("bootstrapValidator").resetForm();
  ob.bootstrapValidator('validate');
  return ob.data('bootstrapValidator').isValid();
};

/**
 * 提交
 */
InoutInfoDlg.addSubmit = function () {

  this.clearData();
  this.collectData();

  if (!this.validate($('#inoutInfoForm'))) {
    return;
  }

  //提交信息
  var ajax = new $ax(Feng.ctxPath + "/inout/add", function (data) {
    Feng.success("添加成功!");
    window.parent.Inout.table.refresh();
    InoutInfoDlg.close();
  }, function (data) {
    Feng.error("添加失败!" + data.responseJSON.message + "!");
  });
  ajax.set(this.inoutInfoData);
  ajax.setContentType("application/json")
  ajax.start();
};

/**
 * 提交修改
 */
InoutInfoDlg.editSubmit = function () {

  this.clearData();
  this.collectData();

  if (!this.validate($('#inoutInfoForm'))) {
    return;
  }

  //提交信息
  var ajax = new $ax(Feng.ctxPath + "/inout/update", function (data) {
    Feng.success("修改成功!");
    window.parent.Inout.table.refresh();
    InoutInfoDlg.close();
  }, function (data) {
    Feng.error("修改失败!" + data.responseJSON.message + "!");
  });
  ajax.set(this.inoutInfoData);
  ajax.setContentType("application/json")
  ajax.start();
};

/**
 * 初始化
 */
$(function () {

  Feng.initValidator("inoutInfoForm", InoutInfoDlg.validateFields);

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
  //账户树
  var tree = new $ZTree("acctTree",
      "/account/selectCompanyAcctTreeList");
  tree.bindOnClick(InoutInfoDlg.onClickAcct);
  tree.init();
  InoutInfoDlg.acctTreeInstance = tree;

});


//账户部分---------------------------------------------------------------------------------
/**
 * 显示父级菜单选择的树--入账账户
 */
InoutInfoDlg.showAcctSelectTree = function () {
  Feng.showInputTree("acctName", "acctTreeDiv", 15, 34);
};

/**
 * 点击父级编号input框时
 */
InoutInfoDlg.onClickAcct = function (e, treeId, treeNode) {
  $("#acctName").attr("value",
      InoutInfoDlg.acctTreeInstance.getSelectedVal());
  $("#acctNo").attr("value", treeNode.id);
};
