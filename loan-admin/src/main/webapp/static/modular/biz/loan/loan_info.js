/**
 * 初始化账户详情对话框
 */
var LoanDlg = {
  loanInfoData: {},
  productTreeInstance: null,
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
LoanDlg.clearData = function () {
  this.loanInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
LoanDlg.set = function (key, val) {
  this.loanInfoData[key] = (typeof value == "undefined") ? $("#" + key).val()
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
LoanDlg.get = function (key) {
  return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
LoanDlg.close = function () {
  parent.layer.close(window.parent.Loan.layerIndex);
}

/**
 * 收集数据
 */
LoanDlg.collectData = function () {
  this.set('id').set('userNo').set('name').set('availableBalance').set('freezeBalance').set('acctType').set('balanceType')
  .set('status').set('createBy').set('modifiedBy').set('createAt').set('updateAt').set('remark')   ;
}

/**
 * 验证数据是否为空
 */
LoanDlg.validate = function () {

  $('#loanInfoForm').data("bootstrapValidator").resetForm();
  $('#loanInfoForm').bootstrapValidator('validate');
  return $("#loanInfoForm").data('bootstrapValidator').isValid();
}

/**
 * 提交添加账户
 */
LoanDlg.addSubmit = function () {

  this.clearData();
  this.collectData();

  if (!this.validate()) {
    return;
  }

  //提交信息
  var ajax = new $ax(Feng.ctxPath + "/loan/add", function (data) {
    Feng.success("添加成功!");
    window.parent.Loan.table.refresh();
    LoanDlg.close();
  }, function (data) {
    Feng.error("添加失败!" + data.responseJSON.message + "!");
  });
  ajax.set(this.loanInfoData);
  ajax.setContentType("application/json")
  ajax.start();
}

/**
 * 提交修改
 */
LoanDlg.editSubmit = function () {

  this.clearData();
  this.collectData();

  if (!this.validate()) {
    return;
  }

  //提交信息
  var ajax = new $ax(Feng.ctxPath + "/loan/update", function (data) {
    Feng.success("修改成功!");
    window.parent.Loan.table.refresh();
    LoanDlg.close();
  }, function (data) {
    Feng.error("修改失败!" + data.responseJSON.message + "!");
  });
  ajax.set(this.loanInfoData);
  ajax.setContentType("application/json")
  ajax.start();
}

$(function () {
  Feng.initValidator("loanInfoForm", LoanDlg.validateFields);

  //初始化
  $("#acctType").val($("#acctTypeValue").val());
  $("#balanceType").val($("#balanceTypeValue").val());
  if($("#statusValue") == null || $("#statusValue").val() == ""){
    $("#status").val(1); //正常
  }
  else{
    $("#status").val($("#statusValue").val());
  }

  //产品树
  var tree = new $ZTree("productTree", "/menu/selectMenuTreeList");
  tree.bindOnClick(LoanDlg.onClickProduct);
  tree.init();
  LoanDlg.productTreeInstance = tree;

});

/**
 * 点击父级编号input框时
 */
LoanDlg.onClickProduct = function (e, treeId, treeNode) {
  $("#productName").attr("value", LoanDlg.productTreeInstance.getSelectedVal());
  $("#product").attr("value", treeNode.id);
};


/**
 * 显示父级菜单选择的树
 */
LoanDlg.showMenuSelectTree = function () {
  Feng.showInputTree("productName", "productTreeDiv", 15, 34);
};
