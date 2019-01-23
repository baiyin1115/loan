/**
 * 客户管理初始化
 */
var Customer = {
  id: "CustomerTable",	//表格id
  seItem: null,		//选中的条目
  seItems: null, //批量删除的数组
  table: null,
  layerIndex: -1
};

/**
 * 初始化表格的列
 */
Customer.initColumn = function () {
  return [
    {field: 'selectItem', radio: false},
    {title:'客户编号',field:'id',align:'center',valign:'middle',sortable:true},
    {title:'证件号码',field:'certNo',align:'center',valign:'middle',sortable:true},
    {title:'证件类型',field:'certTypeName',align:'center',valign:'middle',sortable:true},
    {title:'客户姓名',field:'name',align:'center',valign:'middle',sortable:true},
    {title:'性别',field:'sexName',align:'center',valign:'middle',sortable:true},
    {title:'手机号',field:'mobile',align:'center',valign:'middle',sortable:true},
    {title:'电话',field:'phone',align:'center',valign:'middle',sortable:true},
    {title:'电子邮箱',field:'email',align:'center',valign:'middle',sortable:true},
    {title:'客户类型',field:'typeName',align:'center',valign:'middle',sortable:true},
    {title:'客户状态',field:'statusName',align:'center',valign:'middle',sortable:true},
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
Customer.check = function () {
  var selected = $('#' + this.id).bootstrapTable('getSelections');
  if (selected.length != 1) {
    Feng.info("请先选中表格中的某一记录！");
    return false;
  } else {
    Customer.seItem = selected[0];
    return true;
  }
};

Customer.checkAll = function () {
  var selected = $('#' + this.id).bootstrapTable('getSelections');
  if (selected.length == 0) {
    Feng.info("请先选中表格中的记录！");
    return false;
  } else {

    Customer.seItems = new Array();
    for (var i = 0; i < selected.length; i++) {
      Customer.seItems.push(selected[i].id);
    }

    return true;
  }
};

/**
 * 点击添加客户
 */
Customer.openAddCustomer = function () {
  var index = layer.open({
    type: 2,
    title: '添加客户',
    area: ['800px', '600px'], //宽高
    fix: false, //不固定
    maxmin: true,
    content: Feng.ctxPath + '/customer/customer_add'
  });
  this.layerIndex = index;
};

/**
 * 打开查看客户详情
 */
Customer.openCustomerDetail = function () {
  if (this.check()) {
    var index = layer.open({
      type: 2,
      title: '客户详情',
      area: ['800px', '600px'], //宽高
      fix: false, //不固定
      maxmin: true,
      content: Feng.ctxPath + '/customer/customer_update/' + Customer.seItem.id
    });
    this.layerIndex = index;
  }
};

/**
 * 删除客户
 */
Customer.delete = function () {
  if (this.checkAll()) {

    var operation = function () {
      var ajax = new $ax(Feng.ctxPath + "/customer/logic_delete", function () {
        Feng.success("删除成功!");
        Customer.table.refresh();
      }, function (data) {
        Feng.error("删除失败!" + data.responseJSON.message + "!");
      });
      ajax.setData(Customer.seItems);
      ajax.setContentType("application/json")
      ajax.start();
    };

    Feng.confirm("是否刪除该客户?", operation);
  }
};

/**
 * 设置黑名单
 */
Customer.setBlackList = function () {
  if (this.checkAll()) {

    var operation = function () {
      var ajax = new $ax(Feng.ctxPath + "/customer/set_black_list", function () {
        Feng.success("设置成功!");
        Customer.table.refresh();
      }, function (data) {
        Feng.error("设置失败!" + data.responseJSON.message + "!");
      });
      ajax.setData(Customer.seItems);
      ajax.setContentType("application/json")
      ajax.start();
    };

    Feng.confirm("是否将该客户设置到黑名单里?", operation);
  }
};

/**
 * 取消黑名单
 */
Customer.cancelBlackList = function () {
  if (this.checkAll()) {

    var operation = function () {
      var ajax = new $ax(Feng.ctxPath + "/customer/cancel_black_list", function () {
        Feng.success("取消成功!");
        Customer.table.refresh();
      }, function (data) {
        Feng.error("取消失败!" + data.responseJSON.message + "!");
      });
      ajax.setData(Customer.seItems);
      ajax.setContentType("application/json")
      ajax.start();
    };

    Feng.confirm("是否将该客户从黑名单里移除?", operation);
  }
};

/**
 * 查询表单提交参数对象
 * @returns {{}}
 */
Customer.formParams = function () {
  var queryData = {};
  queryData['certNo'] = $("#certNo").val();
  queryData['name'] = $("#name").val();
  queryData['mobile'] = $("#mobile").val();
  return queryData;
}

/**
 * 查询客户列表
 */
Customer.search = function () {
  Customer.table.refresh({query: Customer.formParams()});
};

/**
 *  重置客户列表
 */
Customer.resetSearch = function () {
  $("#certNo").val("");
  $("#name").val("");
  $("#mobile").val("");

  Customer.search();
}

$(function () {
  var defaultColunms = Customer.initColumn();
  var table = new BSTable(Customer.id, "/customer/list", defaultColunms);
  table.setPaginationType("server");
  table.setQueryParams(Customer.formParams());
  table.init();
  Customer.table = table;
});
