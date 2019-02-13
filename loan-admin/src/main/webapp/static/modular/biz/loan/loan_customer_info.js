/**
 * 初始化账户详情对话框
 */
var LoanCustomerDlg = {
  custId: "LoanCustomerTable",	//表格id
  custTable: null,
};


$(function () {

  //客户
  var defaultCustColunms = LoanCustomerDlg.initCustColumn();
  var table = new BSTable(LoanCustomerDlg.custId, "/customer/list", defaultCustColunms);
  table.setPaginationType("server");
  table.setQueryParams(LoanCustomerDlg.custFormParams());
  table.init();
  LoanCustomerDlg.custTable = table;

});

//客户--------------------------------------------------------------------------------------

/**
 * 初始化表格的列
 */
LoanCustomerDlg.initCustColumn = function () {
  return [
    {field: 'selectItem', radio: true},
    {title:'客户编号',field:'id',align:'center',valign:'middle',sortable:true},
    {title:'证件号码',field:'certNo',align:'center',valign:'middle',sortable:true},
    {title:'证件类型',field:'certTypeName',align:'center',valign:'middle',sortable:true},
    {title:'客户姓名',field:'name',align:'center',valign:'middle',sortable:true},
    {title:'客户类型',field:'typeName',align:'center',valign:'middle',sortable:true},
    {title:'客户状态',field:'statusName',align:'center',valign:'middle',sortable:true},
  ];
};

/**
 * 查询表单提交参数对象
 * @returns {{}}
 */
LoanCustomerDlg.custFormParams = function () {
  var queryData = {};
  queryData['certNo'] = $("#certNo").val();
  queryData['name'] = $("#name").val();
  queryData['mobile'] = $("#mobile").val();
  return queryData;
};

/**
 * 查询客户列表
 */
LoanCustomerDlg.custSearch = function () {
  LoanCustomerDlg.custTable.refresh({query: LoanCustomerDlg.custFormParams()});
};

/**
 *  重置客户列表
 */
LoanCustomerDlg.resetCustSearch = function () {
  $("#certNo").val("");
  $("#name").val("");
  $("#mobile").val("");

  LoanCustomerDlg.custSearch();
};

LoanCustomerDlg.setCustomer = function () {
  var selected = $('#' + this.custId).bootstrapTable('getSelections');
  if (selected.length != 1) {
    Feng.info("请先选中表格中的某一记录！");
    return false;
  } else {
    var seItem = selected[0];

    window.parent.$("#custName").attr("value", seItem.name);
    window.parent.$("#custNo").attr("value", seItem.id);
    LoanCustomerDlg.close();
  }
};

LoanCustomerDlg.close = function () {
  if(window.parent.LoanDlg != undefined){
    parent.layer.close(window.parent.LoanDlg.custLayerIndex);
  }

  if(window.parent.Loan != undefined){
    parent.layer.close(window.parent.Loan.custLayerIndex);
  }
}


