/**
 * 初始化账户详情对话框
 */
var LoanDlg = {
  loanInfoData: {},
  productTreeInstance: null,
  lendingAcctTreeInstance: null,
  custLayerIndex: null,
  validateFields: {
    orgNo: {validators: {notEmpty: {message: '公司编号'}}},
    productNo: {validators: {notEmpty: {message: '产品编号'}}},
    custNo: {validators: {notEmpty: {message: '客户编号'}}},
    // contrNo:{validators:{notEmpty:{message:'原始合同编号'}}},
    acctDate: {validators: {notEmpty: {message: '业务日期'}}},
    beginDate: {validators: {notEmpty: {message: '借款开始日期'}}},
    endDate: {validators: {notEmpty: {message: '借款结束日期'}}},
    prin: {validators: {notEmpty: {message: '本金'}}},
    // serviceFee:{validators:{notEmpty:{message:'服务费'}}},
    // receiveBigint:{validators:{notEmpty:{message:'应收利息'}}},
    // termNo:{validators:{notEmpty:{message:'期数'}}},
    // lendingDate:{validators:{notEmpty:{message:'放款日期'}}},
    // lendingAmt:{validators:{notEmpty:{message:'放款金额'}}},
    lendingAcct: {validators: {notEmpty: {message: '放款账户'}}},
    // externalAcct:{validators:{notEmpty:{message:'收款账户'}}},
    loanType: {validators: {notEmpty: {message: '贷款类型'}}},
    rate: {validators: {notEmpty: {message: '利率'}}},
    serviceFeeScale: {validators: {notEmpty: {message: '服务费比例'}}},
    serviceFeeType: {validators: {notEmpty: {message: '服务费收取方式'}}},
    repayType: {validators: {notEmpty: {message: '还款方式'}}}
    //isPen:{validators:{notEmpty:{message:'是否罚息'}}},
    //penRate:{validators:{notEmpty:{message:'罚息利率'}}},
    //penNumber:{validators:{notEmpty:{message:'罚息基数'}}}
    // ddDate:{validators:{notEmpty:{message:'约定还款日'}}},
    // extensionNo:{validators:{notEmpty:{message:'展期期数'}}},
    // extensionRate:{validators:{notEmpty:{message:'展期利息'}}},
    // status:{validators:{notEmpty:{message:'借据状态'}}}
  }
};

/**
 * 清除数据
 */
LoanDlg.clearData = function () {
  this.loanInfoData = {};
};

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
LoanDlg.set = function (key, val) {

  if ($("#" + key).attr("id") == "prin" ||
      $("#" + key).attr("id") == "serviceFee" ||
      $("#" + key).attr("id") == "receiveInterest" ||
      $("#" + key).attr("id") == "lendingAmt"
  ) {
    this.loanInfoData[key] = (typeof value == "undefined") ? Feng.parseMoney($("#" + key).val()) : value;
  } else {
    this.loanInfoData[key] = (typeof value == "undefined") ? $("#" + key).val() : value;
  }

  //alert($("#" + key).val())
  return this;
};

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
LoanDlg.get = function (key) {
  return $("#" + key).val();
};

/**
 * 关闭此对话框
 */
LoanDlg.close = function () {
  parent.layer.close(window.parent.Loan.layerIndex);
};

/**
 * 收集数据
 */
LoanDlg.collectData = function () {
  this.set('id').set('orgNo').set('productNo').set('custNo').set('contrNo').set(
      'acctDate').set('beginDate').set('endDate')
  .set('prin').set('serviceFee').set('receiveInterest').set('termNo').set(
      'lendingDate').set('lendingAmt').set('lendingAcct')
  .set('externalAcct').set('loanType').set('rate').set('serviceFeeScale').set(
      'serviceFeeType').set('repayType').set('isPen')
  .set('penRate').set('penNumber').set('ddDate').set('extensionNo').set(
      'extensionRate').set('schdPrin').set('schdInterest')
  .set('schdServFee').set('schdPen').set('totPaidPrin').set(
      'totPaidInterest').set('totPaidServFee').set('totPaidPen').set(
      'totWavAmt')
  .set('status').set('createBy').set('modifiedBy').set('createAt').set(
      'updateAt').set('remark');
};

/**
 * 验证数据是否为空
 */
LoanDlg.validate = function () {

  $('#loanInfoForm').data("bootstrapValidator").resetForm();
  $('#loanInfoForm').bootstrapValidator('validate');
  return $("#loanInfoForm").data('bootstrapValidator').isValid();
};

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
};

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
};

$(function () {
  Feng.initValidator("loanInfoForm", LoanDlg.validateFields);

  //初始化
  $("#acctType").val($("#acctTypeValue").val());
  $("#balanceType").val($("#balanceTypeValue").val());
  if ($("#statusValue") == null || $("#statusValue").val() == "") {
    $("#status").val(1); //正常
  }
  else {
    $("#status").val($("#statusValue").val());
  }

  //产品树
  var tree = new $ZTree("productTree", "/product/selectProductTreeList");
  tree.bindOnClick(LoanDlg.onClickProduct);
  tree.init();
  LoanDlg.productTreeInstance = tree;

  //放款账户树
  var tree = new $ZTree("lendingAcctTree",
      "/account/selectLendingAcctTreeList");
  tree.bindOnClick(LoanDlg.onClickLendingAcct);
  tree.init();
  LoanDlg.lendingAcctTreeInstance = tree;

  //绑定格式化事件
  $('#prin').bind('blur', function () {Feng.formatAmt($('#prin'));});
  $('#serviceFee').bind('blur', function () {Feng.formatAmt($('#serviceFee'));});
  $('#receiveInterest').bind('blur', function () {Feng.formatAmt($('#receiveInterest'));});
  $('#lendingAmt').bind('blur', function () {Feng.formatAmt($('#lendingAmt'));});

  //alert(Feng.formatMoney(0.00,2));

});

/**
 * 试算
 */
LoanDlg.calculate = function () {

  this.clearData();
  this.collectData();

  if (!this.validate()) {
    return;
  }

  //提交信息
  var ajax = new $ax(Feng.ctxPath + "/loan/calculate", function (data) {
    Feng.success("试算成功!");

    //设置数据信息
    $("#serviceFee").val(Feng.formatMoney(data.serviceFee,2));
    $("#receiveInterest").val(Feng.formatMoney(data.receiveInterest,2));
    $("#termNo").val(data.termNo);
    $("#lendingAmt").val(Feng.formatMoney(data.lendingAmt,2));

  }, function (data) {
    Feng.error("试算失败!" + data.responseJSON.message + "!");
  });
  ajax.set(this.loanInfoData);
  //alert(JSON.stringify(this.loanInfoData));
  ajax.setContentType("application/json")
  ajax.start();
};

//账户部分---------------------------------------------------------------------------------
/**
 * 显示父级菜单选择的树
 */
LoanDlg.showLendingAcctSelectTree = function () {
  Feng.showInputTree("lendingAcctName", "lendingAcctTreeDiv", 15, 34);
};

/**
 * 点击父级编号input框时
 */
LoanDlg.onClickLendingAcct = function (e, treeId, treeNode) {
  $("#lendingAcctName").attr("value",
      LoanDlg.lendingAcctTreeInstance.getSelectedVal());
  $("#lendingAcct").attr("value", treeNode.id);
};

//产品部分---------------------------------------------------------------------------------
/**
 * 点击父级编号input框时
 */
LoanDlg.onClickProduct = function (e, treeId, treeNode) {
  $("#productName").attr("value", LoanDlg.productTreeInstance.getSelectedVal());
  $("#productNo").attr("value", treeNode.id);

  LoanDlg.changeProduct(treeNode.id); //触发刷新
};

/**
 * 显示父级菜单选择的树
 */
LoanDlg.showProductSelectTree = function () {
  Feng.showInputTree("productName", "productTreeDiv", 15, 34);
};

/**
 * 显示父级菜单选择的树
 */
LoanDlg.changeProduct = function (id) {

  //提交信息
  var ajax = new $ax(Feng.ctxPath + "/product/detail/" + id, function (data) {

    $("#loanType").val(data.loanType);
    $("#rate").val(data.rate);
    $("#serviceFeeScale").val(data.serviceFeeScale);
    $("#serviceFeeType").val(data.serviceFeeType);
    $("#repayType").val(data.repayType);
    $("#isPen").val(data.isPen);
    $("#penRate").val(data.penRate);
    $("#penNumber").val(data.penNumber);

  }, function (data) {
    Feng.error("未查询到产品信息!" + data.responseJSON.message + "!");
  });
  ajax.start();

};

//客户--------------------------------------------------------------------------------------
/**
 * 打开查看客户
 */
LoanDlg.openCustList = function () {
  var index = layer.open({
    type: 2,
    title: '选择客户',
    area: ['900px', '600px'], //宽高
    fix: false, //不固定
    maxmin: true,
    content: Feng.ctxPath + '/loan/loan_cust_list'
  });
  this.custLayerIndex = index;
};