

//借据部分-------------------------------------------------------------------------------------

/**
 * 贷款管理初始化
 */
var Loan = {
  id: "LoanTable",	//表格id
  seItem: null,		//选中的条目
  seItems: null, //批量删除的数组
  table: null,
  layerIndex: -1
};

/**
 * 初始化表格的列
 */
Loan.initColumn = function () {
  return [
    {field: 'selectItem', radio: false},
    {title:'借据编号 ',field:'id',align:'center',valign:'middle',sortable:true},
    {title:'公司',field:'orgNo',align:'center',valign:'middle',sortable:true},
    {title:'产品',field:'productNo',align:'center',valign:'middle',sortable:true},
    {title:'客户',field:'custNo',align:'center',valign:'middle',sortable:true},
    {title:'原始合同编号',field:'contrNo',align:'center',valign:'middle',sortable:true},
    {title:'贷款类型',field:'loanType',align:'center',valign:'middle',sortable:true},
    {title:'业务日期',field:'acctDate',align:'center',valign:'middle',sortable:true},
    {title:'借款开始日期',field:'beginDate',align:'center',valign:'middle',sortable:true},
    {title:'借款结束日期',field:'endDate',align:'center',valign:'middle',sortable:true},
    {title:'本金',field:'prin',align:'center',valign:'middle',sortable:true},
    {title:'利率',field:'rate',align:'center',valign:'middle',sortable:true},
    {title:'应收利息',field:'receiveInterest',align:'center',valign:'middle',sortable:true},
    {title:'产品还款方式',field:'repayType',align:'center',valign:'middle',sortable:true},
    {title:'期数',field:'termNo',align:'center',valign:'middle',sortable:true},
    {title:'放款日期',field:'lendingDate',align:'center',valign:'middle',sortable:true},
    {title:'放款金额',field:'lendingAmt',align:'center',valign:'middle',sortable:true},
    {title:'放款账户',field:'lendingAcct',align:'center',valign:'middle',sortable:true},
    {title:'收款账户',field:'externalAcct',align:'center',valign:'middle',sortable:true},
    {title:'服务费',field:'serviceFee',align:'center',valign:'middle',sortable:true},
    {title:'服务费收取方式',field:'serviceFeeType',align:'center',valign:'middle',sortable:true},
    {title:'约定还款日',field:'ddDate',align:'center',valign:'middle',sortable:true},
    {title:'是否罚息',field:'isPen',align:'center',valign:'middle',sortable:true},
    {title:'罚息利率',field:'penRate',align:'center',valign:'middle',sortable:true},
    {title:'罚息基数',field:'penNumber',align:'center',valign:'middle',sortable:true},
    {title:'展期期数',field:'extensionNo',align:'center',valign:'middle',sortable:true},
    {title:'展期利息',field:'extensionRate',align:'center',valign:'middle',sortable:true},
    {title:'应还本金',field:'schdPrin',align:'center',valign:'middle',sortable:true},
    {title:'应还利息',field:'schdInterest',align:'center',valign:'middle',sortable:true},
    {title:'应收服务费',field:'schdServFee',align:'center',valign:'middle',sortable:true},
    {title:'逾期罚息累计',field:'schdPen',align:'center',valign:'middle',sortable:true},
    {title:'已还本金累计',field:'totPaidPrin',align:'center',valign:'middle',sortable:true},
    {title:'已还利息累计',field:'totPaidInterest',align:'center',valign:'middle',sortable:true},
    {title:'已收服务费累计',field:'totPaidServFee',align:'center',valign:'middle',sortable:true},
    {title:'已还罚息累计',field:'totPaidPen',align:'center',valign:'middle',sortable:true},
    {title:'减免金额累计',field:'totWavAmt',align:'center',valign:'middle',sortable:true},
    {title:'借据状态',field:'status',align:'center',valign:'middle',sortable:true},
    {title:'操作员',field:'createBy',align:'center',valign:'middle',sortable:true},
    {title:'修改操作员',field:'modifiedBy',align:'center',valign:'middle',sortable:true},
    {title:'创建时间',field:'createAt',align:'center',valign:'middle',sortable:true},
    {title:'更新时间',field:'updateAt',align:'center',valign:'middle',sortable:true},
    {title:'备注',field:'remark',align:'center',valign:'middle',sortable:true},
  ];
};

/**
 * 检查是否选中
 */
Loan.check = function () {
  var selected = $('#' + this.id).bootstrapTable('getSelections');
  if (selected.length != 1) {
    Feng.info("请先选中表格中的某一记录！");
    return false;
  } else {
    Loan.seItem = selected[0];
    return true;
  }
};

Loan.checkAll = function () {
  var selected = $('#' + this.id).bootstrapTable('getSelections');
  if (selected.length == 0) {
    Feng.info("请先选中表格中的记录！");
    return false;
  } else {

    Loan.seItems = new Array();
    for (var i = 0; i < selected.length; i++) {
      Loan.seItems.push(selected[i].id);
    }

    return true;
  }
};

/**
 * 点击添加借据
 */
Loan.openAddLoan = function () {
  var index = layer.open({
    type: 2,
    title: '登记借据',
    area: ['1100px', '700px'], //宽高
    fix: false, //不固定
    maxmin: true,
    content: Feng.ctxPath + '/loan/loan_add'
  });
  this.layerIndex = index;
};

/**
 * 打开查看借据详情
 */
Loan.openLoanDetail = function () {
  if (this.check()) {
    var index = layer.open({
      type: 2,
      title: '借据详情',
      area: ['800px', '600px'], //宽高
      fix: false, //不固定
      maxmin: true,
      content: Feng.ctxPath + '/loan/loan_update/' + Loan.seItem.id
    });
    this.layerIndex = index;
  }
};

/**
 * 删除借据
 */
Loan.delete = function () {
  if (this.checkAll()) {

    var operation = function () {
      var ajax = new $ax(Feng.ctxPath + "/loan/delete", function () {
        Feng.success("删除成功!");
        Loan.table.refresh();
      }, function (data) {
        Feng.error("删除失败!" + data.responseJSON.message + "!");
      });
      ajax.setData(Loan.seItems);
      ajax.setContentType("application/json")
      ajax.start();
    };

    Feng.confirm("是否刪除该借据?", operation);
  }
};

/**
 * 锁定
 */
Loan.freeze = function () {
  if (this.checkAll()) {

    var operation = function () {
      var ajax = new $ax(Feng.ctxPath + "/loan/freeze", function () {
        Feng.success("设置成功!");
        Loan.table.refresh();
      }, function (data) {
        Feng.error("设置失败!" + data.responseJSON.message + "!");
      });
      ajax.setData(Loan.seItems);
      ajax.setContentType("application/json")
      ajax.start();
    };

    Feng.confirm("是否将该贷款锁定?", operation);
  }
};

/**
 * 解锁
 */
Loan.unfreeze = function () {
  if (this.checkAll()) {

    var operation = function () {
      var ajax = new $ax(Feng.ctxPath + "/loan/unfreeze", function () {
        Feng.success("解锁成功!");
        Loan.table.refresh();
      }, function (data) {
        Feng.error("解锁失败!" + data.responseJSON.message + "!");
      });
      ajax.setData(Loan.seItems);
      ajax.setContentType("application/json")
      ajax.start();
    };

    Feng.confirm("是否将该贷款解锁?", operation);
  }
};

/**
 * 查询表单提交参数对象
 * @returns {{}}
 */
Loan.formParams = function () {
  var queryData = {};
  queryData['orgNo'] = $("#orgNo").val();
  queryData['custNo'] = $("#custNo").val();
  queryData['contrNo'] = $("#contrNo").val();
  return queryData;
}

/**
 * 查询借据列表
 */
Loan.search = function () {
  Loan.table.refresh({query: Loan.formParams()});
};

/**
 *  重置借据列表
 */
Loan.resetSearch = function () {
  $("#orgNo").val("");
  $("#custNo").val("");
  $("#contrNo").val("");

  Loan.search();
}

//初始化函数---------------------------------------------------------------------------------------
$(function () {
  var defaultColunms = Loan.initColumn();
  var table = new BSTable(Loan.id, "/loan/list", defaultColunms,400);
  table.setPaginationType("server");
  table.setQueryParams(Loan.formParams());
  table.init();
  Loan.table = table;

  var defaultColunms = LoanPlan.initColumn();
  var table = new BSTable(LoanPlan.id, "/loan/loan_repay_plan_list", defaultColunms,400);
  table.setPaginationType("server");
  table.setQueryParams(LoanPlan.formParams());
  table.init();
  LoanPlan.table = table;
});



//还款计划部分-------------------------------------------------------------------------------------
/**
 * 还款计划管理初始化
 */
var LoanPlan = {
  id: "LoanPlanTable",	//表格id
  seItem: null,		//选中的条目
  seItems: null, //批量删除的数组
  table: null,
  layerIndex: -1
};

/**
 * 初始化表格的列
 */
LoanPlan.initColumn = function () {
  return [
    {field: 'selectItem', radio: false},
    {title:'计划编号 ',field:'id',align:'center',valign:'middle',sortable:true},
    {title:'借据编号',field:'loanNo',align:'center',valign:'middle',sortable:true},
    {title:'公司编号',field:'orgNo',align:'center',valign:'middle',sortable:true},
    {title:'产品编号',field:'productNo',align:'center',valign:'middle',sortable:true},
    {title:'客户编号',field:'custNo',align:'center',valign:'middle',sortable:true},
    {title:'业务日期',field:'acctDate',align:'center',valign:'middle',sortable:true},
    {title:'期数',field:'termNo',align:'center',valign:'middle',sortable:true},
    {title:'利率',field:'rate',align:'center',valign:'middle',sortable:true},
    {title:'本期开始日期',field:'beginDate',align:'center',valign:'middle',sortable:true},
    {title:'本期结束日期',field:'endDate',align:'center',valign:'middle',sortable:true},
    {title:'计息天数',field:'ddNum',align:'center',valign:'middle',sortable:true},
    {title:'还款日期',field:'ddDate',align:'center',valign:'middle',sortable:true},
    {title:'还款账户',field:'externalAcct',align:'center',valign:'middle',sortable:true},
    {title:'入账账户',field:'inAcctNo',align:'center',valign:'middle',sortable:true},
    {title:'本期应还本金',field:'ctdPrin',align:'center',valign:'middle',sortable:true},
    {title:'本期应还利息',field:'ctdBigint',align:'center',valign:'middle',sortable:true},
    {title:'本期应收服务费',field:'ctdServFee',align:'center',valign:'middle',sortable:true},
    {title:'本期应收罚息',field:'ctdPen',align:'center',valign:'middle',sortable:true},
    {title:'本期已还本金',field:'paidPrin',align:'center',valign:'middle',sortable:true},
    {title:'本期已还利息',field:'paidBigint',align:'center',valign:'middle',sortable:true},
    {title:'本期已收服务费',field:'paidServFee',align:'center',valign:'middle',sortable:true},
    {title:'本期已收罚息',field:'paidPen',align:'center',valign:'middle',sortable:true},
    {title:'本期减免',field:'wavAmt',align:'center',valign:'middle',sortable:true},
    {title:'还款状态',field:'status',align:'center',valign:'middle',sortable:true},
    {title:'操作员',field:'createBy',align:'center',valign:'middle',sortable:true},
    {title:'修改操作员',field:'modifiedBy',align:'center',valign:'middle',sortable:true},
    {title:'创建时间',field:'createAt',align:'center',valign:'middle',sortable:true},
    {title:'更新时间',field:'updateAt',align:'center',valign:'middle',sortable:true},
    {title:'备注',field:'remark',align:'center',valign:'middle',sortable:true},
  ];
};

/**
 * 检查是否选中
 */
LoanPlan.check = function () {
  var selected = $('#' + this.id).bootstrapTable('getSelections');
  if (selected.length != 1) {
    Feng.info("请先选中表格中的某一记录！");
    return false;
  } else {
    LoanPlan.seItem = selected[0];
    return true;
  }
};

LoanPlan.checkAll = function () {
  var selected = $('#' + this.id).bootstrapTable('getSelections');
  if (selected.length == 0) {
    Feng.info("请先选中表格中的记录！");
    return false;
  } else {

    LoanPlan.seItems = new Array();
    for (var i = 0; i < selected.length; i++) {
      LoanPlan.seItems.push(selected[i].id);
    }

    return true;
  }
};

/**
 * 打开查看还款计划详情
 */
LoanPlan.openLoanDetail = function () {
  if (this.check()) {
    var index = layer.open({
      type: 2,
      title: '贷款详情',
      area: ['800px', '480px'], //宽高
      fix: false, //不固定
      maxmin: true,
      content: Feng.ctxPath + '/loan/loan_update/' + LoanPlan.seItem.id
    });
    this.layerIndex = index;
  }
};

/**
 * 查询表单提交参数对象
 * @returns {{}}
 */
LoanPlan.formParams = function () {
  var queryData = {};
  queryData['acctType'] = $("#acctType").val();
  queryData['name'] = $("#name").val();
  queryData['userNo'] = $("#userNo").val();
  return queryData;
}

/**
 * 查询还款计划列表
 */
LoanPlan.search = function () {
  LoanPlan.table.refresh({query: LoanPlan.formParams()});
};

/**
 *  重置还款计划列表
 */
LoanPlan.resetSearch = function () {
  $("#acctType").val("");
  $("#name").val("");
  $("#userNo").val("");

  LoanPlan.search();
}
