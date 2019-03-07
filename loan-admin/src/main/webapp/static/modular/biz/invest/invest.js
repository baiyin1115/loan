/**
 * 产品管理初始化
 */
var Product = {
  id: "ProductTable",	//表格id
  seItem: null,		//选中的条目
  seItems: null, //批量删除的数组
  table: null,
  layerIndex: -1
};

/**
 * 初始化表格的列
 */
Product.initColumn = function () {
  return [
    {field: 'selectItem', radio: false},
    {
      title: 'id',
      field: 'id',
      // visible: false,
      align: 'center',
      valign: 'middle'
    },
    {
      title: '公司',
      field: 'orgNoName',
      align: 'center',
      valign: 'middle',
      sortable: true
    },
    {
      title: '产品名称',
      field: 'productName',
      align: 'center',
      valign: 'middle',
      sortable: true
    },
    {
      title: '产品利率',
      field: 'rateFormat',
      align: 'center',
      valign: 'middle',
      sortable: true
    },
    {
      title: '服务费比例',
      field: 'serviceFeeScaleFormat',
      align: 'center',
      valign: 'middle',
      sortable: true
    },
    {
      title: '服务费收取方式',
      field: 'serviceFeeTypeName',
      align: 'center',
      valign: 'middle',
      sortable: true
    },
    {
      title: '罚息利率',
      field: 'penRateFormat',
      align: 'center',
      valign: 'middle',
      sortable: true
    },
    {
      title: '是否罚息',
      field: 'isPenName',
      align: 'center',
      valign: 'middle',
      sortable: true
    },
    {
      title: '罚息基数',
      field: 'penNumberName',
      align: 'center',
      valign: 'middle',
      sortable: true
    },
    {
      title: '还款方式',
      field: 'repayTypeName',
      align: 'center',
      valign: 'middle',
      sortable: true
    },
    {
      title: '贷款类型',
      field: 'loanTypeName',
      align: 'center',
      valign: 'middle',
      sortable: true
    },
    {
      title: '周期间隔',
      field: 'cycleIntervalFormat',
      align: 'center',
      valign: 'middle',
      sortable: true
    }];
};

/**
 * 检查是否选中
 */
Product.check = function () {
  var selected = $('#' + this.id).bootstrapTable('getSelections');
  if (selected.length != 1) {
    Feng.info("请先选中表格中的某一记录！");
    return false;
  } else {
    Product.seItem = selected[0];
    return true;
  }
};

Product.checkAll = function () {
  var selected = $('#' + this.id).bootstrapTable('getSelections');
  if (selected.length == 0) {
    Feng.info("请先选中表格中的记录！");
    return false;
  } else {

    Product.seItems = new Array();
    for (var i = 0; i < selected.length; i++) {
      Product.seItems.push(selected[i].id);
    }

    return true;
  }
};

/**
 * 点击添加产品
 */
Product.openAddProduct = function () {
  var index = layer.open({
    type: 2,
    title: '添加产品',
    area: ['800px', '600px'], //宽高
    fix: false, //不固定
    maxmin: true,
    content: Feng.ctxPath + '/product/product_add'
  });
  this.layerIndex = index;
};

/**
 * 打开查看产品详情
 */
Product.openProductDetail = function () {
  if (this.check()) {
    var index = layer.open({
      type: 2,
      title: '产品详情',
      area: ['800px', '600px'], //宽高
      fix: false, //不固定
      maxmin: true,
      content: Feng.ctxPath + '/product/product_update/' + Product.seItem.id
    });
    this.layerIndex = index;
  }
};

/**
 * 删除产品
 */
Product.delete = function () {
  if (this.checkAll()) {

    var operation = function () {
      var ajax = new $ax(Feng.ctxPath + "/product/delete", function () {
        Feng.success("删除成功!");
        Product.table.refresh();
      }, function (data) {
        Feng.error("删除失败!" + data.responseJSON.message + "!");
      });
      ajax.setData(Product.seItems);
      ajax.setContentType("application/json")
      ajax.start();
    };

    Feng.confirm("是否刪除该产品?", operation);
  }
};

/**
 * 查询表单提交参数对象
 * @returns {{}}
 */
Product.formParams = function () {
  var queryData = {};
  queryData['condition'] = $("#condition").val();
  return queryData;
}

/**
 * 查询产品列表
 */
Product.search = function () {
  Product.table.refresh({query: Product.formParams()});
};

$(function () {
  var defaultColunms = Product.initColumn();
  var table = new BSTable(Product.id, "/product/list", defaultColunms);
  table.setPaginationType("server");
  table.setQueryParams(Product.formParams());
  table.init();
  Product.table = table;
});
