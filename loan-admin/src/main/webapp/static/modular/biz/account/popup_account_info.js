/**
 * 初始化账户详情对话框
 */
var PopupAccountDlg = {
  accountId: "PopupAccountTable",	//表格id
  accountTable: null,
  windowParent: null
};

$(function () {

  //客户
  var defaultAccountColunms = PopupAccountDlg.initAccountColumn();
  var table = new BSTable(PopupAccountDlg.accountId, "/account/popup_list",
      defaultAccountColunms);
  table.setPaginationType("server");
  table.setQueryParams(PopupAccountDlg.accountFormParams());
  table.init();
  PopupAccountDlg.accountTable = table;

  $("#acctType").val($("#acctTypeValue").val());

});

/**
 * 初始化表格的列
 */
PopupAccountDlg.initAccountColumn = function () {
  return [
    {field: 'selectItem', radio: true},
    {title:'账户',field:'id',align:'center',valign:'middle',sortable:true},
    {title:'客户编号',field:'custNo',align:'center',valign:'middle',sortable:true},
    {title:'证件号码',field:'custCertNo',align:'center',valign:'middle',sortable:true},
    {title:'客户姓名',field:'custName',align:'center',valign:'middle',sortable:true},
    {title:'手机号',field:'custMobile',align:'center',valign:'middle',sortable:true},
    {title:'账户名称',field:'name',align:'center',valign:'middle',sortable:true},
    {title:'可用余额',field:'availableBalance',align:'center',valign:'middle',sortable:true},
    {title:'冻结余额',field:'freezeBalance',align:'center',valign:'middle',sortable:true},
    {title:'账户类型',field:'acctTypeName',align:'center',valign:'middle',sortable:true},
    {title:'余额性质',field:'balanceTypeName',align:'center',valign:'middle',sortable:true},
    {title:'账户状态',field:'statusName',align:'center',valign:'middle',sortable:true},
    {title:'客户类型',field:'custTypeName',align:'center',valign:'middle',sortable:true},
    {title:'客户状态',field:'custStatusName',align:'center',valign:'middle',sortable:true}
  ];
};

/**
 * 查询表单提交参数对象
 * @returns {{}}
 */
PopupAccountDlg.accountFormParams = function () {
  var queryData = {};
  queryData['custCertNo'] = $("#custCertNo").val();
  queryData['custName'] = $("#custName").val();
  queryData['custMobile'] = $("#custMobile").val();
  queryData['acctType'] = $("#acctType").val();
  return queryData;
};

/**
 * 查询客户列表
 */
PopupAccountDlg.accountSearch = function () {
  PopupAccountDlg.accountTable.refresh(
      {query: PopupAccountDlg.accountFormParams()});
};

/**
 *  重置客户列表
 */
PopupAccountDlg.resetAccountSearch = function () {
  $("#custCertNo").val("");
  $("#custName").val("");
  $("#custMobile").val("");
  $("#acctType").val("");

  PopupAccountDlg.accountSearch();
};

PopupAccountDlg.setAccount = function () {
  var selected = $('#' + this.accountId).bootstrapTable('getSelections');
  if (selected.length != 1) {
    Feng.info("请先选中表格中的某一记录！");
    return false;
  } else {
    var seItem = selected[0];

    if (window.parent.Transfer != undefined) {
      if (window.parent.Transfer.accountType == "in") {
        window.parent.$("#inAcctName").attr("value", seItem.id+"_"+seItem.name);
        window.parent.$("#inAcctNo").attr("value", seItem.id);
      }else{
        window.parent.$("#outAcctName").attr("value", seItem.id+"_"+seItem.name);
        window.parent.$("#outAcctNo").attr("value", seItem.id);
      }
    }

    if (window.parent.TransferInfoDlg != undefined) {
      if (window.parent.TransferInfoDlg.accountType == "in") {
        window.parent.$("#inAcctName").attr("value", seItem.id+"_"+seItem.name);
        window.parent.$("#inAcctNo").attr("value", seItem.id);
      }else{
        window.parent.$("#outAcctName").attr("value", seItem.id+"_"+seItem.name);
        window.parent.$("#outAcctNo").attr("value", seItem.id);
      }
    }

    PopupAccountDlg.close();
  }
};

PopupAccountDlg.close = function () {

  if (window.parent.Transfer != undefined) {
    parent.layer.close(window.parent.Transfer.accountLayerIndex);
  }

  if (window.parent.TransferInfoDlg != undefined) {
    parent.layer.close(window.parent.TransferInfoDlg.accountLayerIndex);
  }
}


