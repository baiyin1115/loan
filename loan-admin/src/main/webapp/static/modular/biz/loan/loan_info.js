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
    // extensionRate:{validators:{notEmpty:{message:'展期利率'}}},
    // status:{validators:{notEmpty:{message:'借据状态'}}}
  },
  validatePutFields: {
    // orgNo: {validators: {notEmpty: {message: '公司编号'}}},
    // productNo: {validators: {notEmpty: {message: '产品编号'}}},
    // custNo: {validators: {notEmpty: {message: '客户编号'}}},
    // contrNo:{validators:{notEmpty:{message:'原始合同编号'}}},
    // acctDate: {validators: {notEmpty: {message: '业务日期'}}},
    // beginDate: {validators: {notEmpty: {message: '借款开始日期'}}},
    // endDate: {validators: {notEmpty: {message: '借款结束日期'}}},
    // prin: {validators: {notEmpty: {message: '本金'}}},
    // serviceFee:{validators:{notEmpty:{message:'服务费'}}},
    // receiveBigint:{validators:{notEmpty:{message:'应收利息'}}},
    // termNo:{validators:{notEmpty:{message:'期数'}}},
    // externalAcct:{validators:{notEmpty:{message:'收款账户'}}},
    // loanType: {validators: {notEmpty: {message: '贷款类型'}}},
    // rate: {validators: {notEmpty: {message: '利率'}}},
    // serviceFeeScale: {validators: {notEmpty: {message: '服务费比例'}}},
    // serviceFeeType: {validators: {notEmpty: {message: '服务费收取方式'}}},
    // repayType: {validators: {notEmpty: {message: '还款方式'}}},
    //isPen:{validators:{notEmpty:{message:'是否罚息'}}},
    //penRate:{validators:{notEmpty:{message:'罚息利率'}}},
    //penNumber:{validators:{notEmpty:{message:'罚息基数'}}}
    // ddDate:{validators:{notEmpty:{message:'约定还款日'}}}
    // extensionNo:{validators:{notEmpty:{message:'展期期数'}}},
    // extensionRate:{validators:{notEmpty:{message:'展期利率'}}},
    // status:{validators:{notEmpty:{message:'借据状态'}}}
    lendingDate: {validators: {notEmpty: {message: '放款日期'}}},
    lendingAmt: {validators: {notEmpty: {message: '放款金额'}}},
    lendingAcct: {validators: {notEmpty: {message: '放款账户'}}}
  },
  validateDelayFields: {
    // orgNo: {validators: {notEmpty: {message: '公司编号'}}},
    // productNo: {validators: {notEmpty: {message: '产品编号'}}},
    // custNo: {validators: {notEmpty: {message: '客户编号'}}},
    // contrNo:{validators:{notEmpty:{message:'原始合同编号'}}},
    // acctDate: {validators: {notEmpty: {message: '业务日期'}}},
    // beginDate: {validators: {notEmpty: {message: '借款开始日期'}}},
    // endDate: {validators: {notEmpty: {message: '借款结束日期'}}},
    // prin: {validators: {notEmpty: {message: '本金'}}},
    // serviceFee:{validators:{notEmpty:{message:'服务费'}}},
    // receiveBigint:{validators:{notEmpty:{message:'应收利息'}}},
    // termNo:{validators:{notEmpty:{message:'期数'}}},
    // lendingDate:{validators:{notEmpty:{message:'放款日期'}}},
    // lendingAmt:{validators:{notEmpty:{message:'放款金额'}}},
    // lendingAcct: {validators: {notEmpty: {message: '放款账户'}}},
    // externalAcct:{validators:{notEmpty:{message:'收款账户'}}},
    // loanType: {validators: {notEmpty: {message: '贷款类型'}}},
    // rate: {validators: {notEmpty: {message: '利率'}}},
    // serviceFeeScale: {validators: {notEmpty: {message: '服务费比例'}}},
    // serviceFeeType: {validators: {notEmpty: {message: '服务费收取方式'}}},
    // repayType: {validators: {notEmpty: {message: '还款方式'}}},
    //isPen:{validators:{notEmpty:{message:'是否罚息'}}},
    //penRate:{validators:{notEmpty:{message:'罚息利率'}}},
    //penNumber:{validators:{notEmpty:{message:'罚息基数'}}}
    // ddDate:{validators:{notEmpty:{message:'约定还款日'}}}
    // status:{validators:{notEmpty:{message:'借据状态'}}}
    currentExtensionNo: {validators: {notEmpty: {message: '展期期数'}}},
    extensionRate: {validators: {notEmpty: {message: '展期利率'}}}
  },
  validatePrepayFields: {
    // orgNo: {validators: {notEmpty: {message: '公司编号'}}},
    // productNo: {validators: {notEmpty: {message: '产品编号'}}},
    // custNo: {validators: {notEmpty: {message: '客户编号'}}},
    // contrNo:{validators:{notEmpty:{message:'原始合同编号'}}},
    // acctDate: {validators: {notEmpty: {message: '业务日期'}}},
    // beginDate: {validators: {notEmpty: {message: '借款开始日期'}}},
    // endDate: {validators: {notEmpty: {message: '借款结束日期'}}},
    // prin: {validators: {notEmpty: {message: '本金'}}},
    // serviceFee:{validators:{notEmpty:{message:'服务费'}}},
    // receiveBigint:{validators:{notEmpty:{message:'应收利息'}}},
    // termNo:{validators:{notEmpty:{message:'期数'}}},
    // lendingDate:{validators:{notEmpty:{message:'放款日期'}}},
    // lendingAmt:{validators:{notEmpty:{message:'放款金额'}}},
    // lendingAcct: {validators: {notEmpty: {message: '放款账户'}}},
    // externalAcct:{validators:{notEmpty:{message:'收款账户'}}},
    // loanType: {validators: {notEmpty: {message: '贷款类型'}}},
    // rate: {validators: {notEmpty: {message: '利率'}}},
    // serviceFeeScale: {validators: {notEmpty: {message: '服务费比例'}}},
    // serviceFeeType: {validators: {notEmpty: {message: '服务费收取方式'}}},
    // repayType: {validators: {notEmpty: {message: '还款方式'}}},
    //isPen:{validators:{notEmpty:{message:'是否罚息'}}},
    //penRate:{validators:{notEmpty:{message:'罚息利率'}}},
    //penNumber:{validators:{notEmpty:{message:'罚息基数'}}}
    // ddDate:{validators:{notEmpty:{message:'约定还款日'}}}
    // status:{validators:{notEmpty:{message:'借据状态'}}}
    currentRepayPrin: {validators: {notEmpty: {message: '本金'}}},
    currentRepayFee: {validators: {notEmpty: {message: '费用'}}},
    currentRepayWav: {validators: {notEmpty: {message: '减免'}}}
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
      $("#" + key).attr("id") == "lendingAmt" ||
      $("#" + key).attr("id") == "schdPrin" ||
      $("#" + key).attr("id") == "schdInterest" ||
      $("#" + key).attr("id") == "schdServFee" ||
      $("#" + key).attr("id") == "currentRepayPrin" ||
      $("#" + key).attr("id") == "currentRepayFee" ||
      $("#" + key).attr("id") == "currentRepayWav"
  ) {
    this.loanInfoData[key] = (typeof value == "undefined") ? Feng.parseMoney(
        $("#" + key).val()) : value;
  } else {
    this.loanInfoData[key] = (typeof value == "undefined") ? $("#" + key).val()
        : value;
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
      'updateAt').set('remark').set("currentExtensionNo")

  .set("currentRepayPrin").set("currentRepayFee").set("currentRepayWav")
  .set("repayAmt").set("repayInterest").set("repayPen").set("repayServFee");

};

/**
 * 验证数据是否为空
 */
LoanDlg.validate = function (ob) {

  ob.data("bootstrapValidator").resetForm();
  ob.bootstrapValidator('validate');
  return ob.data('bootstrapValidator').isValid();
};

/**
 * 提交添加账户
 */
LoanDlg.addSubmit = function () {

  this.clearData();
  this.collectData();

  if (!this.validate($('#loanInfoForm'))) {
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

  if (!this.validate($('#loanInfoForm'))) {
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

/**
 * 试算
 */
LoanDlg.calculate = function () {

  this.clearData();
  this.collectData();

  if (!this.validate($('#loanInfoForm'))) {
    return;
  }

  //提交信息
  var ajax = new $ax(Feng.ctxPath + "/loan/calculate", function (data) {
    Feng.success("试算成功!");

    //设置数据信息
    $("#serviceFee").val(Feng.formatMoney(data.serviceFee, 2));
    $("#receiveInterest").val(Feng.formatMoney(data.receiveInterest, 2));
    $("#termNo").val(data.termNo);
    $("#lendingAmt").val(Feng.formatMoney(data.lendingAmt, 2));

  }, function (data) {
    Feng.error("试算失败!" + data.responseJSON.message + "!");
  });
  ajax.set(this.loanInfoData);
  //alert(JSON.stringify(this.loanInfoData));
  ajax.setContentType("application/json")
  ajax.start();
};

/**
 * 放款
 */
LoanDlg.putSubmit = function () {

  if (!this.validate($('#loanPutInfoForm'))) {
    return;
  }

  var operation = function () {

    LoanDlg.clearData();
    LoanDlg.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/loan/put", function (data) {
      Feng.success("放款成功!");
      window.parent.Loan.refreshLoanPlan();
      window.parent.Loan.table.refresh();
      LoanDlg.close();
    }, function (data) {
      Feng.error("放款失败!" + data.responseJSON.message + "!");
    });
    ajax.set(LoanDlg.loanInfoData);
    ajax.setContentType("application/json")
    ajax.start();
  };

  Feng.confirm("是否放款,放款后信息不可修改?", operation);

};

/**
 * 放款试算
 */
LoanDlg.putCalculate = function () {

  if (!this.validate($('#loanPutInfoForm'))) {
    return;
  }

  this.clearData();
  this.collectData();

  //提交信息
  var ajax = new $ax(Feng.ctxPath + "/loan/put_calculate", function (data) {
    Feng.infoDetail("试算详情", data.resultMsg);

  }, function (data) {
    Feng.error("试算失败!" + data.responseJSON.message + "!");
  });
  ajax.set(this.loanInfoData);
  //alert(JSON.stringify(this.loanInfoData));
  ajax.setContentType("application/json")
  ajax.start();
};

/**
 * 展期
 */
LoanDlg.delaySubmit = function () {

  if (!this.validate($('#loanDelayInfoForm'))) {
    return;
  }

  var operation = function () {

    LoanDlg.clearData();
    LoanDlg.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/loan/delay", function (data) {
      Feng.success("展期成功!");
      window.parent.Loan.refreshLoanPlan();
      window.parent.Loan.table.refresh();
      LoanDlg.close();
    }, function (data) {
      Feng.error("展期失败!" + data.responseJSON.message + "!");
    });
    ajax.set(LoanDlg.loanInfoData);
    ajax.setContentType("application/json")
    ajax.start();
  };

  Feng.confirm("是否展期?", operation);

};

/**
 * 展期试算
 */
LoanDlg.delayCalculate = function () {

  if (!this.validate($('#loanDelayInfoForm'))) {
    return;
  }

  this.clearData();
  this.collectData();

  //提交信息
  var ajax = new $ax(Feng.ctxPath + "/loan/delay_calculate", function (data) {
    Feng.infoDetail("试算详情", data.resultMsg);

  }, function (data) {
    Feng.error("试算失败!" + data.responseJSON.message + "!");
  });
  ajax.set(this.loanInfoData);
  //alert(JSON.stringify(this.loanInfoData));
  ajax.setContentType("application/json")
  ajax.start();
};

/**
 * 提前还款
 */
LoanDlg.prepaySubmit = function () {

  if (!this.validate($('#loanPrepayInfoForm'))) {
    return;
  }

  var operation = function () {

    LoanDlg.clearData();
    LoanDlg.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/loan/prepay", function (data) {
      Feng.success("提前还款成功!");
      window.parent.Loan.refreshLoanPlan();
      window.parent.Loan.table.refresh();
      LoanDlg.close();
    }, function (data) {
      Feng.error("提前还款失败!" + data.responseJSON.message + "!");
    });
    ajax.set(LoanDlg.loanInfoData);
    ajax.setContentType("application/json")
    ajax.start();
  };

  Feng.confirm("是否提前还款?", operation);

};

/**
 * 提前还款试算
 */
LoanDlg.prepayCalculate = function () {

  if (!this.validate($('#loanPrepayInfoForm'))) {
    return;
  }

  this.clearData();
  this.collectData();

  //提交信息
  var ajax = new $ax(Feng.ctxPath + "/loan/prepay_calculate", function (data) {
    Feng.infoDetail("试算详情", data.resultMsg);

  }, function (data) {
    Feng.error("试算失败!" + data.responseJSON.message + "!");
  });
  ajax.set(this.loanInfoData);
  //alert(JSON.stringify(this.loanInfoData));
  ajax.setContentType("application/json")
  ajax.start();
};

/**
 * 初始化
 */
$(function () {
  Feng.initValidator("loanInfoForm", LoanDlg.validateFields);
  Feng.initValidator("loanPutInfoForm", LoanDlg.validatePutFields);
  Feng.initValidator("loanDelayInfoForm", LoanDlg.validateDelayFields);
  Feng.initValidator("loanPrepayInfoForm", LoanDlg.validatePrepayFields);

  //初始化
  $("#orgNo").val($("#orgNoValue").val());
  $("#serviceFeeType").val($("#serviceFeeTypeValue").val());
  $("#isPen").val($("#isPenValue").val());
  $("#penNumber").val($("#penNumberValue").val());
  $("#repayType").val($("#repayTypeValue").val());
  $("#loanType").val($("#loanTypeValue").val());

  $("#prin").val(Feng.formatMoney($("#prin").val(), 2));
  $("#receiveInterest").val(Feng.formatMoney($("#receiveInterest").val(), 2));
  $("#serviceFee").val(Feng.formatMoney($("#serviceFee").val(), 2));
  $("#lendingAmt").val(Feng.formatMoney($("#lendingAmt").val(), 2));
  $("#schdPrin").val(Feng.formatMoney($("#schdPrin").val(), 2));
  $("#schdInterest").val(Feng.formatMoney($("#schdInterest").val(), 2));
  $("#schdServFee").val(Feng.formatMoney($("#schdServFee").val(), 2));
  $("#currentRepayPrin").val(Feng.formatMoney($("#currentRepayPrin").val(), 2));
  $("#currentRepayFee").val(Feng.formatMoney($("#currentRepayFee").val(), 2));
  $("#currentRepayWav").val(Feng.formatMoney($("#currentRepayWav").val(), 2));

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
  $('#prin').bind('blur', function () {
    Feng.formatAmt($('#prin'));
  });
  $('#serviceFee').bind('blur', function () {
    Feng.formatAmt($('#serviceFee'));
  });
  $('#receiveInterest').bind('blur', function () {
    Feng.formatAmt($('#receiveInterest'));
  });
  $('#lendingAmt').bind('blur', function () {
    Feng.formatAmt($('#lendingAmt'));
  });
  $('#schdPrin').bind('blur', function () {
    Feng.formatAmt($('#schdPrin'));
  });
  $('#schdInterest').bind('blur', function () {
    Feng.formatAmt($('#schdInterest'));
  });
  $('#schdServFee').bind('blur', function () {
    Feng.formatAmt($('#schdServFee'));
  });
  $('#currentRepayPrin').bind('blur', function () {
    Feng.formatAmt($('#currentRepayPrin'));
  });
  $('#currentRepayFee').bind('blur', function () {
    Feng.formatAmt($('#currentRepayFee'));
  });
  $('#currentRepayWav').bind('blur', function () {
    Feng.formatAmt($('#currentRepayWav'));
  });

  //alert(Feng.formatMoney(0.00,2));

});

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