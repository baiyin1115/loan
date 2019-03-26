/**
 * 转账管理初始化
 */
var Transfer = {
  id: "TransferTable",	//表格id
  seItem: null,		//选中的条目
  seItems: null, //批量删除的数组
  table: null,
  custLayerIndex: null,
  layerIndex: -1
};

/**
 * 初始化表格的列
 */
Transfer.initColumn = function () {
  var sign = '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;';
  return [
    {field: 'selectItem', radio: false},
    {title:'转账编号 ',field:'id',align:'center',valign:'middle',sortable:true},
    {title:'状态',field:'statusName',align:'center',valign:'middle',sortable:true},
    {title:sign+'公司'+sign,field:'orgName',align:'center',valign:'middle',sortable:true},
    {title:sign+'用途'+sign,field:'typeName',align:'center',valign:'middle',sortable:true},
    {title:'金额',field:'amtFormat',align:'center',valign:'middle',sortable:true},
    {title:sign+'账户'+sign,field:'acctName',align:'center',valign:'middle',sortable:true},
    {title:'外部账户',field:'externalAcct',align:'center',valign:'middle',sortable:true},
    {title:'业务日期',field:'acctDateFormat',align:'center',valign:'middle',sortable:true},
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
Transfer.check = function () {
  var selected = $('#' + this.id).bootstrapTable('getSelections');
  if (selected.length != 1) {
    Feng.info("请先选中表格中的某一记录！");
    return false;
  } else {
    Transfer.seItem = selected[0];
    return true;
  }
};

Transfer.checkAll = function () {
  var selected = $('#' + this.id).bootstrapTable('getSelections');
  if (selected.length == 0) {
    Feng.info("请先选中表格中的记录！");
    return false;
  } else {

    Transfer.seItems = new Array();
    for (var i = 0; i < selected.length; i++) {
      Transfer.seItems.push(selected[i].id);
    }

    return true;
  }
};

/**
 * 点击添加转账凭证
 */
Transfer.openAdd = function () {
  var index = layer.open({
    type: 2,
    title: '登记转账凭证',
    area: ['800px', '450px'], //宽高
    fix: false, //不固定
    maxmin: true,
    content: Feng.ctxPath + '/transfer/transfer_add'
  });
  this.layerIndex = index;
};

/**
 * 打开查看转账凭证详情
 */
Transfer.openDetail = function () {
  if (this.check()) {
    var index = layer.open({
      type: 2,
      title: '转账凭证详情',
      area: ['800px', '450px'], //宽高
      fix: false, //不固定
      maxmin: true,
      content: Feng.ctxPath + '/transfer/to_transfer_update/' + Transfer.seItem.id
    });
    this.layerIndex = index;
  }
};

/**
 * 确认凭证
 */
Transfer.confirm = function () {

  if (this.checkAll()) {
    var ajax = new $ax(Feng.ctxPath + "/transfer/to_confirm", function (data) {
      //alert(data.message);
      Transfer.to_confirm(data.message);
    }, function (data) {
      Feng.error("确认失败!" + data.responseJSON.message + "!");
    });
    ajax.setData(Transfer.seItems);
    ajax.setContentType("application/json")
    ajax.start();
  }
};

/**
 *to确认
 */
Transfer.to_confirm = function (message) {
  if (this.checkAll()) {

    var operation = function () {
      var ajax = new $ax(Feng.ctxPath + "/transfer/confirm", function () {
        Feng.success("确认成功!");
        Transfer.table.refresh();
      }, function (data) {
        Feng.error("确认失败!" + data.responseJSON.message + "!");
      });
      ajax.setData(Transfer.seItems);
      ajax.setContentType("application/json")
      ajax.start();
    };

    Feng.confirm(message, operation);
  }
};


/**
 * 删除转账凭证
 */
Transfer.delete = function () {
  if (this.checkAll()) {

    var operation = function () {
      var ajax = new $ax(Feng.ctxPath + "/transfer/delete", function () {
        Feng.success("删除成功!");
        Transfer.table.refresh();
      }, function (data) {
        Feng.error("删除失败!" + data.responseJSON.message + "!");
      });
      ajax.setData(Transfer.seItems);
      ajax.setContentType("application/json")
      ajax.start();
    };

    Feng.confirm("是否刪除该转账凭证?", operation);
  }
};

/**
 * 撤销转账凭证
 */
Transfer.cancel = function () {
  if (this.checkAll()) {

    var operation = function () {
      var ajax = new $ax(Feng.ctxPath + "/transfer/cancel", function () {
        Feng.success("撤销成功!");
        Transfer.table.refresh();
      }, function (data) {
        Feng.error("撤销失败!" + data.responseJSON.message + "!");
      });
      ajax.setData(Transfer.seItems);
      ajax.setContentType("application/json")
      ajax.start();
    };

    Feng.confirm("是否撤销该转账凭证?", operation);
  }
};

/**
 * 查询表单提交参数对象
 * @returns {{}}
 */
Transfer.formParams = function () {
  var queryData = {};
  queryData['orgNo'] = $("#orgNo").val();
  queryData['type'] = $("#type").val();
  queryData['queryBeginDate'] = $("#queryBeginDate").val();
  queryData['queryEndDate'] = $("#queryEndDate").val();
  return queryData;
};

/**
 * 查询转账凭证列表
 */
Transfer.search = function () {
  Transfer.table.refresh({query: Transfer.formParams()});
};

/**
 *  重置转账凭证列表
 */
Transfer.resetSearch = function () {
  $("#orgNo").val("");
  $("#type").val("");
  $("#queryBeginDate").val("");
  $("#queryEndDate").val("");

  Transfer.search();
};

//初始化函数---------------------------------------------------------------------------------------
$(function () {
  var defaultColunms = Transfer.initColumn();
  var table = new BSTable(Transfer.id, "/transfer/list", defaultColunms);
  table.setPaginationType("server");
  table.setQueryParams(Transfer.formParams());
  table.init();
  Transfer.table = table;
});