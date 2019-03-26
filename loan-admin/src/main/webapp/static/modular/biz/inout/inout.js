/**
 * 收支管理初始化
 */
var Inout = {
  id: "InOutTable",	//表格id
  seItem: null,		//选中的条目
  seItems: null, //批量删除的数组
  table: null,
  custLayerIndex: null,
  layerIndex: -1
};

/**
 * 初始化表格的列
 */
Inout.initColumn = function () {
  var sign = '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;';
  return [
    {field: 'selectItem', radio: false},
    {title:'收支编号 ',field:'id',align:'center',valign:'middle',sortable:true},
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
Inout.check = function () {
  var selected = $('#' + this.id).bootstrapTable('getSelections');
  if (selected.length != 1) {
    Feng.info("请先选中表格中的某一记录！");
    return false;
  } else {
    Inout.seItem = selected[0];
    return true;
  }
};

Inout.checkAll = function () {
  var selected = $('#' + this.id).bootstrapTable('getSelections');
  if (selected.length == 0) {
    Feng.info("请先选中表格中的记录！");
    return false;
  } else {

    Inout.seItems = new Array();
    for (var i = 0; i < selected.length; i++) {
      Inout.seItems.push(selected[i].id);
    }

    return true;
  }
};

/**
 * 点击添加收支凭证
 */
Inout.openAdd = function () {
  var index = layer.open({
    type: 2,
    title: '登记收支凭证',
    area: ['800px', '450px'], //宽高
    fix: false, //不固定
    maxmin: true,
    content: Feng.ctxPath + '/inout/inout_add'
  });
  this.layerIndex = index;
};

/**
 * 打开查看收支凭证详情
 */
Inout.openDetail = function () {
  if (this.check()) {
    var index = layer.open({
      type: 2,
      title: '收支凭证详情',
      area: ['800px', '450px'], //宽高
      fix: false, //不固定
      maxmin: true,
      content: Feng.ctxPath + '/inout/to_inout_update/' + Inout.seItem.id
    });
    this.layerIndex = index;
  }
};

/**
 * 确认凭证
 */
Inout.confirm = function () {

  if (this.checkAll()) {
    var ajax = new $ax(Feng.ctxPath + "/inout/to_confirm", function (data) {
      //alert(data.message);
      Inout.to_confirm(data.message);
    }, function (data) {
      Feng.error("确认失败!" + data.responseJSON.message + "!");
    });
    ajax.setData(Inout.seItems);
    ajax.setContentType("application/json")
    ajax.start();
  }
};

/**
 *to确认
 */
Inout.to_confirm = function (message) {
  if (this.checkAll()) {

    var operation = function () {
      var ajax = new $ax(Feng.ctxPath + "/inout/confirm", function () {
        Feng.success("确认成功!");
        Inout.table.refresh();
      }, function (data) {
        Feng.error("确认失败!" + data.responseJSON.message + "!");
      });
      ajax.setData(Inout.seItems);
      ajax.setContentType("application/json")
      ajax.start();
    };

    Feng.confirm(message, operation);
  }
};


/**
 * 删除收支凭证
 */
Inout.delete = function () {
  if (this.checkAll()) {

    var operation = function () {
      var ajax = new $ax(Feng.ctxPath + "/inout/delete", function () {
        Feng.success("删除成功!");
        Inout.table.refresh();
      }, function (data) {
        Feng.error("删除失败!" + data.responseJSON.message + "!");
      });
      ajax.setData(Inout.seItems);
      ajax.setContentType("application/json")
      ajax.start();
    };

    Feng.confirm("是否刪除该收支凭证?", operation);
  }
};

/**
 * 撤销收支凭证
 */
Inout.cancel = function () {
  if (this.checkAll()) {

    var operation = function () {
      var ajax = new $ax(Feng.ctxPath + "/inout/cancel", function () {
        Feng.success("撤销成功!");
        Inout.table.refresh();
      }, function (data) {
        Feng.error("撤销失败!" + data.responseJSON.message + "!");
      });
      ajax.setData(Inout.seItems);
      ajax.setContentType("application/json")
      ajax.start();
    };

    Feng.confirm("是否撤销该收支凭证?", operation);
  }
};

/**
 * 查询表单提交参数对象
 * @returns {{}}
 */
Inout.formParams = function () {
  var queryData = {};
  queryData['orgNo'] = $("#orgNo").val();
  queryData['type'] = $("#type").val();
  queryData['queryBeginDate'] = $("#queryBeginDate").val();
  queryData['queryEndDate'] = $("#queryEndDate").val();
  return queryData;
};

/**
 * 查询收支凭证列表
 */
Inout.search = function () {
  Inout.table.refresh({query: Inout.formParams()});
};

/**
 *  重置收支凭证列表
 */
Inout.resetSearch = function () {
  $("#orgNo").val("");
  $("#type").val("");
  $("#queryBeginDate").val("");
  $("#queryEndDate").val("");

  Inout.search();
};

//初始化函数---------------------------------------------------------------------------------------
$(function () {
  var defaultColunms = Inout.initColumn();
  var table = new BSTable(Inout.id, "/inout/list", defaultColunms);
  table.setPaginationType("server");
  table.setQueryParams(Inout.formParams());
  table.init();
  Inout.table = table;
});