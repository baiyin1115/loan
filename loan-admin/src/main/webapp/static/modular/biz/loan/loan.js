/**
 * 账户管理初始化
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
    {title:'账户',field:'id',align:'center',valign:'middle',sortable:true},
    {title:'用户编号',field:'userNo',align:'center',valign:'middle',sortable:true},
    {title:'账户名称',field:'name',align:'center',valign:'middle',sortable:true},
    {title:'可用余额',field:'availableBalance',align:'center',valign:'middle',sortable:true},
    {title:'冻结余额',field:'freezeBalance',align:'center',valign:'middle',sortable:true},
    {title:'账户类型',field:'acctTypeName',align:'center',valign:'middle',sortable:true},
    {title:'余额性质',field:'balanceTypeName',align:'center',valign:'middle',sortable:true},
    {title:'账户状态',field:'statusName',align:'center',valign:'middle',sortable:true},
    {title:'操作员',field:'createByName',align:'center',valign:'middle',sortable:true},
    {title:'修改操作员',field:'modifiedByName',align:'center',valign:'middle',sortable:true},
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
 * 点击添加账户
 */
Loan.openAddLoan = function () {
  var index = layer.open({
    type: 2,
    title: '开立系统账户账户',
    area: ['800px', '480px'], //宽高
    fix: false, //不固定
    maxmin: true,
    content: Feng.ctxPath + '/loan/loan_add'
  });
  this.layerIndex = index;
};

/**
 * 打开查看账户详情
 */
Loan.openLoanDetail = function () {
  if (this.check()) {
    var index = layer.open({
      type: 2,
      title: '账户详情',
      area: ['800px', '480px'], //宽高
      fix: false, //不固定
      maxmin: true,
      content: Feng.ctxPath + '/loan/loan_update/' + Loan.seItem.id
    });
    this.layerIndex = index;
  }
};

/**
 * 删除账户
 */
Loan.delete = function () {
  if (this.checkAll()) {

    var operation = function () {
      var ajax = new $ax(Feng.ctxPath + "/loan/logic_delete", function () {
        Feng.success("删除成功!");
        Loan.table.refresh();
      }, function (data) {
        Feng.error("删除失败!" + data.responseJSON.message + "!");
      });
      ajax.setData(Loan.seItems);
      ajax.setContentType("application/json")
      ajax.start();
    };

    Feng.confirm("是否刪除该账户?", operation);
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

    Feng.confirm("是否将该账户锁定?", operation);
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

    Feng.confirm("是否将该账户解锁?", operation);
  }
};

/**
 * 查询表单提交参数对象
 * @returns {{}}
 */
Loan.formParams = function () {
  var queryData = {};
  queryData['acctType'] = $("#acctType").val();
  queryData['name'] = $("#name").val();
  queryData['userNo'] = $("#userNo").val();
  return queryData;
}

/**
 * 查询账户列表
 */
Loan.search = function () {
  Loan.table.refresh({query: Loan.formParams()});
};

/**
 *  重置账户列表
 */
Loan.resetSearch = function () {
  $("#acctType").val("");
  $("#name").val("");
  $("#userNo").val("");

  Loan.search();
}

$(function () {
  var defaultColunms = Loan.initColumn();
  var table = new BSTable(Loan.id, "/loan/list", defaultColunms);
  table.setPaginationType("server");
  table.setQueryParams(Loan.formParams());
  table.init();
  Loan.table = table;
});
