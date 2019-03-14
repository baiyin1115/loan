/**
 * 初始化账户详情对话框
 */
var InvestDlg = {
  investInfoData: {},
  investPlanData: {},
  investTypeInstance: null,
  inAcctTreeInstance: null,
  custLayerIndex: null,
  validateFields: {
    orgNo: {validators: {notEmpty: {message: '公司编号'}}},
    investType: {validators: {notEmpty: {message: '产品类型'}}},
    custNo: {validators: {notEmpty: {message: '客户编号'}}},
    beginDate: {validators: {notEmpty: {message: '开始日期'}}},
    endDate: {validators: {notEmpty: {message: '结束日期'}}},
    prin: {validators: {notEmpty: {message: '本金'}}},
    inAcctNo: {validators: {notEmpty: {message: '入账账户'}}},
    investType: {validators: {notEmpty: {message: '贷款类型'}}},
    rate: {validators: {notEmpty: {message: '利率'}}}
    // ddDate: {validators: {notEmpty: {message: '计息日'}}}
  },
  validateConfirmFields: {
    orgNo: {validators: {notEmpty: {message: '公司编号'}}},
    investType: {validators: {notEmpty: {message: '产品类型'}}},
    custNo: {validators: {notEmpty: {message: '客户编号'}}},
    beginDate: {validators: {notEmpty: {message: '开始日期'}}},
    endDate: {validators: {notEmpty: {message: '结束日期'}}},
    prin: {validators: {notEmpty: {message: '本金'}}},
    inAcctNo: {validators: {notEmpty: {message: '入账账户'}}},
    investType: {validators: {notEmpty: {message: '贷款类型'}}},
    rate: {validators: {notEmpty: {message: '利率'}}},
    termNo: {validators: {notEmpty: {message: '期数'}}},
    // ddDate: {validators: {notEmpty: {message: '计息日'}}},
    cycleInterval: {validators: {notEmpty: {message: '周期间隔'}}}
  },
  validateDelayFields: {
    currentExtensionNo: {validators: {notEmpty: {message: '延期期数'}}},
    currentExtensionRate: {validators: {notEmpty: {message: '延期利率'}}}
  },
  validateDivestmentFields: {
    divestmentAmt: {validators: {notEmpty: {message: '撤资本金'}}},
    divestmentInterest: {validators: {notEmpty: {message: '撤资费用'}}},
    divestmentWavAmt: {validators: {notEmpty: {message: '收益调整金额'}}}
  }
};


/**
 * 清除数据
 */
InvestDlg.clearData = function () {
  this.investInfoData = {};
};

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
InvestDlg.set = function (key, val) {

  if ($("#" + key).attr("id") == "prin" ||
      $("#" + key).attr("id") == "totSchdInterest" ||
      $("#" + key).attr("id") == "totAccruedInterest" ||
      $("#" + key).attr("id") == "totPaidPrin" ||
      $("#" + key).attr("id") == "totPaidInterest" ||

      $("#" + key).attr("id") == "divestmentAmt" ||
      $("#" + key).attr("id") == "divestmentInterest" ||
      $("#" + key).attr("id") == "divestmentWavAmt"

  ) {
    this.investInfoData[key] = (typeof value == "undefined") ? Feng.parseMoney(
        $("#" + key).val()) : value;
  } else {
    this.investInfoData[key] = (typeof value == "undefined") ? $("#" + key).val()
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
InvestDlg.get = function (key) {
  return $("#" + key).val();
};

/**
 * 关闭此对话框
 */
InvestDlg.close = function () {
  parent.layer.close(window.parent.Invest.layerIndex);
  parent.layer.close(window.parent.InvestPlan.layerIndex);
};

/**
 * 收集数据
 */
InvestDlg.collectData = function () {
  this.set('id').set('orgNo').set('custNo').set('investType').set('inAcctNo').set('externalAcct').set('prin')
  .set('acctDate').set('beginDate').set('endDate').set('rate').set('termNo').set('cycleInterval')
  .set('status').set('ddDate').set('extensionNo').set('extensionRate').set('totSchdInterest')
  .set('totAccruedInterest').set('totPaidPrin').set('totPaidInterest').set('totWavAmt')
  .set('createBy').set('modifiedBy').set('createAt').set('updateAt').set('remark')

  .set("currentExtensionNo").set("currentExtensionRate")

  .set("calculateAmt").set("calculateInterest").set("divestmentAmt").set("divestmentInterest").set("divestmentWavAmt")
  ;

};

/**
 * 验证数据是否为空
 */
InvestDlg.validate = function (ob) {

  ob.data("bootstrapValidator").resetForm();
  ob.bootstrapValidator('validate');
  return ob.data('bootstrapValidator').isValid();
};

/**
 * 提交
 */
InvestDlg.addSubmit = function () {

  this.clearData();
  this.collectData();

  if (!this.validate($('#investInfoForm'))) {
    return;
  }

  //提交信息
  var ajax = new $ax(Feng.ctxPath + "/invest/add", function (data) {
    Feng.success("添加成功!");
    window.parent.Invest.table.refresh();
    InvestDlg.close();
  }, function (data) {
    Feng.error("添加失败!" + data.responseJSON.message + "!");
  });
  ajax.set(this.investInfoData);
  ajax.setContentType("application/json")
  ajax.start();
};

/**
 * 提交修改
 */
InvestDlg.editSubmit = function () {

  this.clearData();
  this.collectData();

  if (!this.validate($('#investInfoForm'))) {
    return;
  }

  //提交信息
  var ajax = new $ax(Feng.ctxPath + "/invest/update", function (data) {
    Feng.success("修改成功!");
    window.parent.Invest.table.refresh();
    InvestDlg.close();
  }, function (data) {
    Feng.error("修改失败!" + data.responseJSON.message + "!");
  });
  ajax.set(this.investInfoData);
  ajax.setContentType("application/json")
  ajax.start();
};

/**
 * 试算
 */
InvestDlg.calculate = function () {

  this.clearData();
  this.collectData();

  if (!this.validate($('#investInfoForm'))) {
    return;
  }

  //提交信息
  var ajax = new $ax(Feng.ctxPath + "/invest/calculate", function (data) {
    Feng.success("试算成功!");

    //设置数据信息
    $("#totSchdInterest").val(Feng.formatMoney(data.totSchdInterest, 2));
    $("#termNo").val(data.termNo);
    $("#ddDate").val(data.ddDate);
    $("#cycleInterval").val(data.cycleInterval);

  }, function (data) {
    Feng.error("试算失败!" + data.responseJSON.message + "!");
  });
  ajax.set(this.investInfoData);
  //alert(JSON.stringify(this.loanInfoData));
  ajax.setContentType("application/json")
  ajax.start();
};

/**
 * 确认
 */
InvestDlg.confirmSubmit = function () {

  this.clearData();
  this.collectData();

  if (!this.validate($('#investConfirmInfoForm'))) {
    return;
  }

  //提交信息
  var ajax = new $ax(Feng.ctxPath + "/invest/confirm", function (data) {
    Feng.success("确认成功!");
    window.parent.Invest.refreshInvestPlan();
    window.parent.Invest.table.refresh();
    InvestDlg.close();
  }, function (data) {
    Feng.error("确认失败!" + data.responseJSON.message + "!");
  });
  ajax.set(this.investInfoData);
  ajax.setContentType("application/json")
  ajax.start();
};

/**
 * 确认试算
 */
InvestDlg.confirmCalculate = function () {

  this.clearData();
  this.collectData();

  if (!this.validate($('#investConfirmInfoForm'))) {
    return;
  }

  //提交信息
  var ajax = new $ax(Feng.ctxPath + "/invest/calculate", function (data) {
    Feng.infoDetail("试算详情", data.resultMsg);

  }, function (data) {
    Feng.error("试算失败!" + data.responseJSON.message + "!");
  });
  ajax.set(this.investInfoData);
  //alert(JSON.stringify(this.loanInfoData));
  ajax.setContentType("application/json")
  ajax.start();
};

/**
 * 延期
 */
InvestDlg.delaySubmit = function () {

  this.clearData();
  this.collectData();

  if (!this.validate($('#investDelayInfoForm'))) {
    return;
  }

  //提交信息
  var ajax = new $ax(Feng.ctxPath + "/invest/delay", function (data) {
    Feng.success("延期成功!");
    window.parent.Invest.refreshInvestPlan();
    window.parent.Invest.table.refresh();
    InvestDlg.close();
  }, function (data) {
    Feng.error("延期失败!" + data.responseJSON.message + "!");
  });
  ajax.set(this.investInfoData);
  ajax.setContentType("application/json")
  ajax.start();
};

/**
 * 延期试算
 */
InvestDlg.delayCalculate = function () {

  this.clearData();
  this.collectData();

  if (!this.validate($('#investDelayInfoForm'))) {
    return;
  }

  //提交信息
  var ajax = new $ax(Feng.ctxPath + "/invest/delayCalculate", function (data) {
    Feng.infoDetail("试算详情", data.resultMsg);

  }, function (data) {
    Feng.error("试算失败!" + data.responseJSON.message + "!");
  });
  ajax.set(this.investInfoData);
  //alert(JSON.stringify(this.loanInfoData));
  ajax.setContentType("application/json")
  ajax.start();
};


/**
 * 初始化
 */
$(function () {

  Feng.initValidator("investInfoForm", InvestDlg.validateFields);
  Feng.initValidator("investConfirmInfoForm", InvestDlg.validateConfirmFields);
  Feng.initValidator("investDelayInfoForm", InvestDlg.validateDelayFields);

  //初始化
  $("#orgNo").val($("#orgNoValue").val());
  $("#investType").val($("#investTypeValue").val());

  //格式化
  $("#prin").val(Feng.formatMoney($("#prin").val(), 2));
  $("#totSchdInterest").val(Feng.formatMoney($("#totSchdInterest").val(), 2));
  $("#totAccruedInterest").val(Feng.formatMoney($("#totAccruedInterest").val(), 2));
  $("#totPaidPrin").val(Feng.formatMoney($("#totPaidPrin").val(), 2));
  $("#totPaidInterest").val(Feng.formatMoney($("#totPaidInterest").val(), 2));
  $("#totWavAmt").val(Feng.formatMoney($("#totWavAmt").val(), 2));

  //绑定格式化事件
  $('#prin').bind('blur', function () {
    Feng.formatAmt($('#prin'));
  });
  $('#totSchdInterest').bind('blur', function () {
    Feng.formatAmt($('#totSchdInterest'));
  });
  $('#totAccruedInterest').bind('blur', function () {
    Feng.formatAmt($('#totAccruedInterest'));
  });
  $('#totPaidPrin').bind('blur', function () {
    Feng.formatAmt($('#totPaidPrin'));
  });
  $('#totPaidInterest').bind('blur', function () {
    Feng.formatAmt($('#totPaidInterest'));
  });
  $('#totWavAmt').bind('blur', function () {
    Feng.formatAmt($('#totWavAmt'));
  });

  //----------------------------------------------------------------------------
  //放款账户树
  var tree = new $ZTree("inAcctTree",
      "/account/selectInAcctTreeList");
  tree.bindOnClick(InvestDlg.onClickInAcct);
  tree.init();
  InvestDlg.inAcctTreeInstance = tree;

});

//客户--------------------------------------------------------------------------------------
/**
 * 打开查看客户
 */
InvestDlg.openCustList = function () {
  var index = layer.open({
    type: 2,
    title: '选择客户',
    area: ['900px', '600px'], //宽高
    fix: false, //不固定
    maxmin: true,
    content: Feng.ctxPath + '/customer/popup_cust_list/2' //1:借款人账户,2:融资人账户
  });
  this.custLayerIndex = index;
};

//账户部分---------------------------------------------------------------------------------
/**
 * 显示父级菜单选择的树--入账账户
 */
InvestDlg.showInAcctSelectTree = function () {
  Feng.showInputTree("inAcctName", "inAcctTreeDiv", 15, 34);
};


/**
 * 点击父级编号input框时
 */
InvestDlg.onClickInAcct = function (e, treeId, treeNode) {
  $("#inAcctName").attr("value",
      InvestDlg.inAcctTreeInstance.getSelectedVal());
  $("#inAcctNo").attr("value", treeNode.id);
};
