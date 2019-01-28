/**
 * 账户管理初始化
 */
var Account = {
  id: "AccountTable",	//表格id
  seItem: null,		//选中的条目
  seItems: null, //批量删除的数组
  table: null,
  layerIndex: -1
};

/**
 * 初始化表格的列
 */
Account.initColumn = function () {
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
Account.check = function () {
  var selected = $('#' + this.id).bootstrapTable('getSelections');
  if (selected.length != 1) {
    Feng.info("请先选中表格中的某一记录！");
    return false;
  } else {
    Account.seItem = selected[0];
    return true;
  }
};

Account.checkAll = function () {
  var selected = $('#' + this.id).bootstrapTable('getSelections');
  if (selected.length == 0) {
    Feng.info("请先选中表格中的记录！");
    return false;
  } else {

    Account.seItems = new Array();
    for (var i = 0; i < selected.length; i++) {
      Account.seItems.push(selected[i].id);
    }

    return true;
  }
};

/**
 * 点击添加账户
 */
Account.openAddAccount = function () {
  var index = layer.open({
    type: 2,
    title: '添加账户',
    area: ['800px', '450px'], //宽高
    fix: false, //不固定
    maxmin: true,
    content: Feng.ctxPath + '/account/account_add'
  });
  this.layerIndex = index;
};

/**
 * 打开查看账户详情
 */
Account.openAccountDetail = function () {
  if (this.check()) {
    var index = layer.open({
      type: 2,
      title: '账户详情',
      area: ['800px', '450px'], //宽高
      fix: false, //不固定
      maxmin: true,
      content: Feng.ctxPath + '/account/account_update/' + Account.seItem.id
    });
    this.layerIndex = index;
  }
};

/**
 * 删除账户
 */
Account.delete = function () {
  if (this.checkAll()) {

    var operation = function () {
      var ajax = new $ax(Feng.ctxPath + "/account/logic_delete", function () {
        Feng.success("删除成功!");
        Account.table.refresh();
      }, function (data) {
        Feng.error("删除失败!" + data.responseJSON.message + "!");
      });
      ajax.setData(Account.seItems);
      ajax.setContentType("application/json")
      ajax.start();
    };

    Feng.confirm("是否刪除该账户?", operation);
  }
};

/**
 * 锁定
 */
Account.freeze = function () {
  if (this.checkAll()) {

    var operation = function () {
      var ajax = new $ax(Feng.ctxPath + "/account/freeze", function () {
        Feng.success("设置成功!");
        Account.table.refresh();
      }, function (data) {
        Feng.error("设置失败!" + data.responseJSON.message + "!");
      });
      ajax.setData(Account.seItems);
      ajax.setContentType("application/json")
      ajax.start();
    };

    Feng.confirm("是否将该账户锁定?", operation);
  }
};

/**
 * 解锁
 */
Account.unfreeze = function () {
  if (this.checkAll()) {

    var operation = function () {
      var ajax = new $ax(Feng.ctxPath + "/account/unfreeze", function () {
        Feng.success("解锁成功!");
        Account.table.refresh();
      }, function (data) {
        Feng.error("解锁失败!" + data.responseJSON.message + "!");
      });
      ajax.setData(Account.seItems);
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
Account.formParams = function () {
  var queryData = {};
  queryData['acctType'] = $("#acctType").val();
  queryData['name'] = $("#name").val();
  queryData['userNo'] = $("#userNo").val();
  return queryData;
}

/**
 * 查询账户列表
 */
Account.search = function () {
  Account.table.refresh({query: Account.formParams()});
};

/**
 *  重置账户列表
 */
Account.resetSearch = function () {
  $("#acctType").val("");
  $("#name").val("");
  $("#userNo").val("");

  Account.search();
}

$(function () {
  var defaultColunms = Account.initColumn();
  var table = new BSTable(Account.id, "/account/list", defaultColunms,800);
  table.setPaginationType("server");
  table.setQueryParams(Account.formParams());
  table.init();
  Account.table = table;
});
