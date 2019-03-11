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
    rate: {validators: {notEmpty: {message: '利率'}}},
    ddDate: {validators: {notEmpty: {message: '计息日'}}}
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
    ddDate: {validators: {notEmpty: {message: '计息日'}}},
    cycleInterval: {validators: {notEmpty: {message: '周期间隔'}}}
  },
  validateDelayFields: {
    currentExtensionNo: {validators: {notEmpty: {message: '延期期数'}}},
    extensionRate: {validators: {notEmpty: {message: '延期利率'}}}
  },
  validateDivestmentFields: {
    divestmentAmt: {validators: {notEmpty: {message: '撤资本金'}}},
    divestmentInterest: {validators: {notEmpty: {message: '撤资费用'}}},
    divestmentWavAmt: {validators: {notEmpty: {message: '收益调整金额'}}}
  }
};

//todo