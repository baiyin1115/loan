/**
 * 融资管理初始化
 */
var Invest = {
  id: "InvestTable",	//表格id
  seItem: null,		//选中的条目
  seItems: null, //批量删除的数组
  table: null,
  custLayerIndex: null,
  layerIndex: -1
};

/**
 * 初始化表格的列
 */
Invest.initColumn = function () {
  var sign = '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;';
  return [
    {field: 'selectItem', radio: false},
    {title:'融资编号 ',field:'id',align:'center',valign:'middle',sortable:true},
    {title:'状态',field:'statusName',align:'center',valign:'middle',sortable:true},
    {title:sign+'公司'+sign,field:'orgName',align:'center',valign:'middle',sortable:true},
    {title:sign+'融资类型'+sign,field:'investTypeName',align:'center',valign:'middle',sortable:true},
    {title:'本金',field:'prinFormat',align:'center',valign:'middle',sortable:true},
    {title:'利率',field:'rateFormat',align:'center',valign:'middle',sortable:true},
    {title:'期数',field:'termNo',align:'center',valign:'middle',sortable:true},
    {title:'应收利息累计',field:'totSchdInterestFormat',align:'center',valign:'middle',sortable:true},
    {title:'计提利息累计',field:'totAccruedInterestFormat',align:'center',valign:'middle',sortable:true},
    {title:'已提本金累计',field:'totPaidPrinFormat',align:'center',valign:'middle',sortable:true},
    {title:'已提利息累计',field:'totPaidInterestFormat',align:'center',valign:'middle',sortable:true},
    {title:'收益调整金额累计',field:'totWavAmtFormat',align:'center',valign:'middle',sortable:true},
    {title:'客户名称',field:'custName',align:'center',valign:'middle',sortable:true},
    {title:sign+'入账账户'+sign,field:'inAcctName',align:'center',valign:'middle',sortable:true},
    {title:'投资人出款账户',field:'externalAcct',align:'center',valign:'middle',sortable:true},
    {title:'业务日期',field:'acctDateFormat',align:'center',valign:'middle',sortable:true},
    {title:'开始日期',field:'beginDateFormat',align:'center',valign:'middle',sortable:true},
    {title:'结束日期',field:'endDateFormat',align:'center',valign:'middle',sortable:true},
    {title:'周期间隔',field:'cycleInterval',align:'center',valign:'middle',sortable:true},
    {title:'计息日',field:'ddDate',align:'center',valign:'middle',sortable:true},
    {title:'延期期数',field:'extensionNo',align:'center',valign:'middle',sortable:true},
    {title:'延期利率',field:'extensionRateFormat',align:'center',valign:'middle',sortable:true},
    {title:'操作员',field:'createByName',align:'center',valign:'middle',sortable:true},
    {title:'修改操作员',field:'modifiedByName',align:'center',valign:'middle',sortable:true},
    {title:sign+'创建时间'+sign,field:'createAt',align:'center',valign:'middle',sortable:true},
    {title:sign+'更新时间'+sign,field:'updateAt',align:'center',valign:'middle',sortable:true},
    {title:sign+'备注'+sign,field:'remark',align:'center',valign:'middle',sortable:true}
  ];
};

/**
 * 检查是否选中
 */
Invest.check = function () {
  var selected = $('#' + this.id).bootstrapTable('getSelections');
  if (selected.length != 1) {
    Feng.info("请先选中表格中的某一记录！");
    return false;
  } else {
    Invest.seItem = selected[0];
    return true;
  }
};

Invest.checkAll = function () {
  var selected = $('#' + this.id).bootstrapTable('getSelections');
  if (selected.length == 0) {
    Feng.info("请先选中表格中的记录！");
    return false;
  } else {

    Invest.seItems = new Array();
    for (var i = 0; i < selected.length; i++) {
      Invest.seItems.push(selected[i].id);
    }

    return true;
  }
};

/**
 * 点击添加融资凭证
 */
Invest.openAddInvest = function () {
  var index = layer.open({
    type: 2,
    title: '登记融资凭证',
    area: ['1100px', '700px'], //宽高
    fix: false, //不固定
    maxmin: true,
    content: Feng.ctxPath + '/invest/invest_add'
  });
  this.layerIndex = index;
};

/**
 * 打开查看融资凭证详情
 */
Invest.openInvestDetail = function () {
  if (this.check()) {
    var index = layer.open({
      type: 2,
      title: '融资凭证详情',
      area: ['1100px', '700px'], //宽高
      fix: false, //不固定
      maxmin: true,
      content: Feng.ctxPath + '/invest/to_invest_update/' + Invest.seItem.id
    });
    this.layerIndex = index;
  }
};

/**
 * 打开确认界面
 */
Invest.openConfirmInvest = function () {
  if (this.check()) {
    var index = layer.open({
      type: 2,
      title: '确认',
      area: ['1280px', '750px'], //宽高
      fix: false, //不固定
      maxmin: true,
      content: Feng.ctxPath + '/invest/to_invest_confirm/' + Invest.seItem.id
    });
    this.layerIndex = index;
  }
};

/**
 * 打开延期界面
 */
Invest.openDelayInvest = function () {
  if (this.check()) {
    var index = layer.open({
      type: 2,
      title: '延期',
      area: ['1280px', '750px'], //宽高
      fix: false, //不固定
      maxmin: true,
      content: Feng.ctxPath + '/invest/to_invest_delay/' + Invest.seItem.id
    });
    this.layerIndex = index;
  }
};

/**
 * 打开撤资界面
 */
Invest.openDivestment = function () {
  if (this.check()) {
    var index = layer.open({
      type: 2,
      title: '撤资',
      area: ['1280px', '750px'], //宽高
      fix: false, //不固定
      maxmin: true,
      content: Feng.ctxPath + '/invest/to_invest_divestment/' + Invest.seItem.id
    });
    this.layerIndex = index;
  }
};

/**
 * 删除融资凭证
 */
Invest.delete = function () {
  if (this.checkAll()) {

    var operation = function () {
      var ajax = new $ax(Feng.ctxPath + "/invest/delete", function () {
        Feng.success("删除成功!");
        Invest.refreshInvestPlan();
        Invest.table.refresh();
      }, function (data) {
        Feng.error("删除失败!" + data.responseJSON.message + "!");
      });
      ajax.setData(Invest.seItems);
      ajax.setContentType("application/json")
      ajax.start();
    };

    Feng.confirm("是否刪除该融资凭证?", operation);
  }
};


/**
 * to结转
 */
Invest.settlement = function () {

  if (this.checkAll()) {
    var ajax = new $ax(Feng.ctxPath + "/invest/to_settlement", function (data) {
      //alert(data.message);
      Invest.to_settlement(data.message);
    }, function (data) {
      Feng.error("结转失败!" + data.responseJSON.message + "!");
    });
    ajax.setData(Invest.seItems);
    ajax.setContentType("application/json")
    ajax.start();
  }
};

/**
 *结转融资凭证
 */
Invest.to_settlement = function (message) {
  if (this.checkAll()) {

    var operation = function () {
      var ajax = new $ax(Feng.ctxPath + "/invest/settlement", function () {
        Feng.success("结转成功!");
        Invest.refreshInvestPlan();
        Invest.table.refresh();
      }, function (data) {
        Feng.error("结转失败!" + data.responseJSON.message + "!");
      });
      ajax.setData(Invest.seItems);
      ajax.setContentType("application/json")
      ajax.start();
    };

    Feng.confirm(message, operation);
  }
};

/**
 * 查询表单提交参数对象
 * @returns {{}}
 */
Invest.formParams = function () {
  var queryData = {};
  queryData['orgNo'] = $("#orgNo").val();
  queryData['custNo'] = $("#custNo").val();
  return queryData;
};

/**
 * 查询融资凭证列表
 */
Invest.search = function () {
  Invest.table.refresh({query: Invest.formParams()});
};

/**
 *  重置融资凭证列表
 */
Invest.resetSearch = function () {
  $("#orgNo").val("");
  $("#custNo").val("");

  Invest.search();
};

/**
 * 刷新回款计划
 */
Invest.refreshInvestPlan = function () {

  var selected = $('#' + this.id).bootstrapTable('getSelections');
  if (selected.length >= 1) {
    Invest.seItem = selected[0];
  }else {
    Invest.seItem = null;
  }
  InvestPlan.table.refresh({query: InvestPlan.formParams()});
};

//初始化函数---------------------------------------------------------------------------------------
$(function () {
  var defaultColunms = Invest.initColumn();
  var table = new BSTable(Invest.id, "/invest/list", defaultColunms,500,9,[9, 50, 100]);
  table.setPaginationType("server");
  table.setQueryParams(Invest.formParams());
  table.init();
  Invest.table = table;

  var defaultColunms = InvestPlan.initColumn();
  var table = new BSTable(InvestPlan.id, "/invest/invest_plan_list", defaultColunms,500,9,[9, 50, 100]);
  table.setPaginationType("server");
  table.setQueryParams(InvestPlan.formParams());
  table.init();
  InvestPlan.table = table;
});

//客户--------------------------------------------------------------------------------------
/**
 * 打开查看客户
 */
Invest.openCustList = function () {
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

//回款计划部分-------------------------------------------------------------------------------------
/**
 * 回款计划管理初始化
 */
var InvestPlan = {
  id: "InvestPlanTable",	//表格id
  seItem: null,		//选中的条目
  seItems: null, //批量删除的数组
  table: null,
  layerIndex: -1
};

/**
 * 初始化表格的列
 */
InvestPlan.initColumn = function () {
  var sign = '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;';
  return [
    {field: 'selectItem', radio: false},
    {title:'计划编号 ',field:'id',align:'center',valign:'middle',sortable:true},
    {title:'融资编号',field:'investNo',align:'center',valign:'middle',sortable:true},
    {title:'回款状态',field:'statusName',align:'center',valign:'middle',sortable:true},
    {title:'本期计息本金',field:'ddPrinFormat',align:'center',valign:'middle',sortable:true},
    {title:'本期利息',field:'chdInterestFormat',align:'center',valign:'middle',sortable:true},
    {title:'本期已提利息',field:'paidInterestFormat',align:'center',valign:'middle',sortable:true},
    {title:'期数',field:'termNo',align:'center',valign:'middle',sortable:true},
    {title:'业务日期',field:'acctDateFormat',align:'center',valign:'middle',sortable:true},
    {title:'计息日期',field:'ddDateFormat',align:'center',valign:'middle',sortable:true},
    {title:'利率',field:'rateFormat',align:'center',valign:'middle',sortable:true},
    {title:'本期开始日期',field:'beginDateFormat',align:'center',valign:'middle',sortable:true},
    {title:'本期结束日期',field:'endDateFormat',align:'center',valign:'middle',sortable:true},
    {title:'计息天数',field:'ddNum',align:'center',valign:'middle',sortable:true},
    {title:sign+'公司'+sign,field:'orgName',align:'center',valign:'middle',sortable:true},
    {title:'客户名称',field:'custName',align:'center',valign:'middle',sortable:true},
    {title:'操作员',field:'createByName',align:'center',valign:'middle',sortable:true},
    {title:'修改操作员',field:'modifiedByName',align:'center',valign:'middle',sortable:true},
    {title:sign+'创建时间'+sign,field:'createAt',align:'center',valign:'middle',sortable:true},
    {title:sign+'更新时间'+sign,field:'updateAt',align:'center',valign:'middle',sortable:true},
    {title:sign+'备注'+sign,field:'remark',align:'center',valign:'middle',sortable:true}
  ];
};

/**
 * 检查是否选中
 */
InvestPlan.check = function () {
  var selected = $('#' + this.id).bootstrapTable('getSelections');
  if (selected.length != 1) {
    Feng.info("请先选中表格中的某一记录！");
    return false;
  } else {
    InvestPlan.seItem = selected[0];
    return true;
  }
};

InvestPlan.checkAll = function () {
  var selected = $('#' + this.id).bootstrapTable('getSelections');
  if (selected.length == 0) {
    Feng.info("请先选中表格中的记录！");
    return false;
  } else {

    InvestPlan.seItems = new Array();
    for (var i = 0; i < selected.length; i++) {
      InvestPlan.seItems.push(selected[i].id);
    }

    return true;
  }
};

/**
 * 查询表单提交参数对象
 * @returns {{}}
 */
InvestPlan.formParams = function () {
  var queryData = {};
  if(Invest.seItem != null){
    queryData['investNo'] = Invest.seItem.id;
  }else{
    queryData['investNo'] = null;
  }
  return queryData;
};

/**
 * 计提
 */
InvestPlan.openAccrual = function () {
  if (this.checkAll()) {

    var operation = function () {
      var ajax = new $ax(Feng.ctxPath + "/invest/accrual", function () {
        Feng.success("计提成功!");
        InvestPlan.table.refresh();
        Invest.search();
      }, function (data) {
        Feng.error("计提失败!" + data.responseJSON.message + "!");
      });
      ajax.setData(InvestPlan.seItems);
      ajax.setContentType("application/json")
      ajax.start();
    };

    Feng.confirm("是否计提?", operation);
  }
};