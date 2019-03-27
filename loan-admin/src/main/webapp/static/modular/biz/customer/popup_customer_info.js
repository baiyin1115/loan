/**
 * 初始化对话框
 */
var PopupCustomerDlg = {
  custId: "PopupCustomerTable",	//表格id
  custTable: null,
  windowParent: null
};


$(function () {

  //客户
  var defaultCustColunms = PopupCustomerDlg.initCustColumn();
  var table = new BSTable(PopupCustomerDlg.custId, "/customer/list", defaultCustColunms);
  table.setPaginationType("server");
  table.setQueryParams(PopupCustomerDlg.custFormParams());
  table.init();
  PopupCustomerDlg.custTable = table;

});

//客户--------------------------------------------------------------------------------------

/**
 * 初始化表格的列
 */
PopupCustomerDlg.initCustColumn = function () {
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
PopupCustomerDlg.custFormParams = function () {
  var queryData = {};
  queryData['certNo'] = $("#certNo").val();
  queryData['name'] = $("#name").val();
  queryData['mobile'] = $("#mobile").val();
  queryData['type'] = $("#type").val();
  return queryData;
};

/**
 * 查询客户列表
 */
PopupCustomerDlg.custSearch = function () {
  PopupCustomerDlg.custTable.refresh({query: PopupCustomerDlg.custFormParams()});
};

/**
 *  重置客户列表
 */
PopupCustomerDlg.resetCustSearch = function () {
  $("#certNo").val("");
  $("#name").val("");
  $("#mobile").val("");

  PopupCustomerDlg.custSearch();
};

PopupCustomerDlg.setCustomer = function () {
  var selected = $('#' + this.custId).bootstrapTable('getSelections');
  if (selected.length != 1) {
    Feng.info("请先选中表格中的某一记录！");
    return false;
  } else {
    var seItem = selected[0];

    window.parent.$("#custName").attr("value", seItem.name);
    window.parent.$("#custNo").attr("value", seItem.id);
    PopupCustomerDlg.close();
  }
};

PopupCustomerDlg.close = function () {
  if(window.parent.LoanDlg != undefined){
    parent.layer.close(window.parent.LoanDlg.custLayerIndex);
  }

  if(window.parent.Loan != undefined){
    parent.layer.close(window.parent.Loan.custLayerIndex);
  }

  if(window.parent.Invest != undefined){
    parent.layer.close(window.parent.Invest.custLayerIndex);
  }

  if(window.parent.InvestDlg != undefined){
    parent.layer.close(window.parent.InvestDlg.custLayerIndex);
  }
}


