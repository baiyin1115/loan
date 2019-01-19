/**
 * 产品管理初始化
 */
var Product = {
    id: "ProductTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
Product.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
        {title: 'id', field: 'id', align: 'center', valign: 'middle',width:'50px'},
        {title: '公司', field: 'orgNo', align: 'center', valign: 'middle', sortable: true},
        {title: '产品名称', field: 'productName', align: 'center', valign: 'middle', sortable: true},
        {title: '产品利率', field: 'rate', align: 'center', valign: 'middle', sortable: true},
        {title: '服务费比例', field: 'serviceFeeScale', align: 'center', valign: 'middle', sortable: true},
        {title: '服务费收取方式', field: 'serviceFeeType', align: 'center', valign: 'middle', sortable: true},
        {title: '罚息利率', field: 'penRate', align: 'center', valign: 'middle', sortable: true},
        {title: '是否罚息', field: 'isPen', align: 'center', valign: 'middle', sortable: true},
        {title: '罚息基数', field: 'penNumber', align: 'center', valign: 'middle', sortable: true},
        {title: '还款方式', field: 'repayType', align: 'center', valign: 'middle', sortable: true},
        {title: '贷款类型', field: 'loanType', align: 'center', valign: 'middle', sortable: true},
        {title: '周期间隔', field: 'cycleInterval', align: 'center', valign: 'middle', sortable: true}];
};

/**
 * 检查是否选中
 */
Product.check = function () {
    var selected = $('#' + this.id).bootstrapTreeTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        Product.seItem = selected[0];
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
            area: ['800px', '420px'], //宽高
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
    if (this.check()) {

        var operation = function(){
            var ajax = new $ax(Feng.ctxPath + "/product/delete", function () {
                Feng.success("删除成功!");
                Product.table.refresh();
            }, function (data) {
                Feng.error("删除失败!" + data.responseJSON.message + "!");
            });
            ajax.set("productId",Product.seItem.id);
            ajax.start();
        };

        Feng.confirm("是否刪除该产品?", operation);
    }
};

/**
 * 查询产品列表
 */
Product.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    Product.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = Product.initColumn();
    var table = new BSTreeTable(Product.id, "/product/list", defaultColunms);
    table.setExpandColumn(2);
    table.setIdField("id");
    table.setCodeField("id");
    table.setParentCodeField("pid");
    table.setExpandAll(true);
    table.init();
    Product.table = table;
});
