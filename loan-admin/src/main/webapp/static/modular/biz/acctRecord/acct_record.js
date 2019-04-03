/**
 * 转账管理初始化
 */
var AcctRecord = {
  id: "AcctRecordTable",	//表格id
  seItem: null,		//选中的条目
  seItems: null, //批量删除的数组
  table: null,
  accountLayerIndex: null,
  layerIndex: -1
};

/**
 * 初始化表格的列
 */
AcctRecord.initColumn = function () {
  var sign = '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;';
  return [
    {field: 'selectItem', radio: true},
    {title:'组号 ',field:'groupNo',align:'center',valign:'middle',sortable:true},
    {title:'流水编号 ',field:'id',align:'center',valign:'middle',sortable:true},
    {title:'状态',field:'statusName',align:'center',valign:'middle',sortable:true},
    {title:sign+'公司'+sign,field:'orgName',align:'center',valign:'middle',sortable:true},
    {title:'凭证编号',field:'voucherNo',align:'center',valign:'middle',sortable:true},
    {title:sign+'业务类型'+sign,field:'typeName',align:'center',valign:'middle',sortable:true},
    {title:sign+'资金类型'+sign,field:'amtTypeName',align:'center',valign:'middle',sortable:true},
    {title:'金额',field:'amtFormat',align:'center',valign:'middle',sortable:true},
    {title:sign+'账户'+sign,field:'acctName',align:'center',valign:'middle',sortable:true},
    {title:'业务日期',field:'acctDateFormat',align:'center',valign:'middle',sortable:true},
    {title:'发生方向',field:'balDir',align:'center',valign:'middle',sortable:true},
    {title:'操作员',field:'createByName',align:'center',valign:'middle',sortable:true},
    {title:'修改操作员',field:'modifiedByName',align:'center',valign:'middle',sortable:true},
    {title:sign+'创建时间'+sign,field:'createAt',align:'center',valign:'middle',sortable:true},
    {title:sign+'更新时间'+sign,field:'updateAt',align:'center',valign:'middle',sortable:true},
    {title:sign+'备注'+sign,field:'remark',align:'center',valign:'middle',sortable:true}
  ];
};

/**
 * 查询表单提交参数对象
 * @returns {{}}
 */
AcctRecord.formParams = function () {
  var queryData = {};
  queryData['orgNo'] = $("#orgNo").val();
  queryData['type'] = $("#type").val();
  queryData['amtType'] = $("#amtType").val();
  queryData['acctNo'] = $("#acctNo").val();
  queryData['queryBeginDate'] = $("#queryBeginDate").val();
  queryData['queryEndDate'] = $("#queryEndDate").val();
  return queryData;
};

/**
 * 查询列表
 */
AcctRecord.search = function () {
  AcctRecord.table.refresh({query: AcctRecord.formParams()});
};

/**
 *  重置列表
 */
AcctRecord.resetSearch = function () {
  $("#orgNo").val("");
  $("#type").val("");
  $("#amtType").val("");
  $("#acctNo").val("");
  $("#queryBeginDate").val("");
  $("#queryEndDate").val("");

  AcctRecord.search();
};

//初始化函数---------------------------------------------------------------------------------------
$(function () {
  var defaultColunms = AcctRecord.initColumn();
  var table = new BSTable(AcctRecord.id, "/acctRecord/list", defaultColunms,700,15,[15, 50, 100]);
  table.setPaginationType("server");
  table.setQueryParams(AcctRecord.formParams());
  table.init();
  AcctRecord.table = table;
});

//账户--------------------------------------------------------------------------------------
/**
 * 打开账户
 */
AcctRecord.openInAccountList = function () {
  var index = layer.open({
    type: 2,
    title: '选择账户',
    area: ['1100px', '600px'], //宽高
    fix: false, //不固定
    maxmin: true,
    content: Feng.ctxPath + '/account/popup_account_list/-1'
  });
  this.accountLayerIndex = index;
};