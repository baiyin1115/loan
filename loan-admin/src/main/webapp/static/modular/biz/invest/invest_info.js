/**
 * 初始化产品详情对话框
 */
var ProductInfoDlg = {
  productInfoData: {},
  // zTreeInstance: null,
  validateFields: {
    productName: {
      validators: {
        notEmpty: {
          message: '产品名称不能为空'
        }
      }
    },
    rate: {
      validators: {
        notEmpty: {
          message: '产品利率不能为空'
        }
      }
    },
    cycleInterval: {
      validators: {
        notEmpty: {
          message: '周期间隔不能为空'
        }
      }
    }
  }
};

/**
 * 清除数据
 */
ProductInfoDlg.clearData = function () {
  this.productInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
ProductInfoDlg.set = function (key, val) {
  this.productInfoData[key] = (typeof value == "undefined") ? $("#" + key).val()
      : value;
  //alert($("#" + key).val())
  return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
ProductInfoDlg.get = function (key) {
  return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
ProductInfoDlg.close = function () {
  parent.layer.close(window.parent.Product.layerIndex);
}

/**
 * 收集数据
 */
ProductInfoDlg.collectData = function () {
  this.set('id').set('orgNo').set('productName').set('rate').set('serviceFeeScale').set('serviceFeeType').set(
      'penRate').set('isPen').set('penNumber').set('repayType').set('loanType').set('cycleInterval');
}

/**
 * 验证数据是否为空
 */
ProductInfoDlg.validate = function () {

  $('#productInfoForm').data("bootstrapValidator").resetForm();
  $('#productInfoForm').bootstrapValidator('validate');
  return $("#productInfoForm").data('bootstrapValidator').isValid();
}

/**
 * 提交添加产品
 */
ProductInfoDlg.addSubmit = function () {

  this.clearData();
  this.collectData();

  if (!this.validate()) {
    return;
  }

  //提交信息
  var ajax = new $ax(Feng.ctxPath + "/product/add", function (data) {
    Feng.success("添加成功!");
    window.parent.Product.table.refresh();
    ProductInfoDlg.close();
  }, function (data) {
    Feng.error("添加失败!" + data.responseJSON.message + "!");
  });
  ajax.set(this.productInfoData);
  ajax.setContentType("application/json")
  ajax.start();
}

/**
 * 提交修改
 */
ProductInfoDlg.editSubmit = function () {

  this.clearData();
  this.collectData();

  if (!this.validate()) {
    return;
  }

  //提交信息
  var ajax = new $ax(Feng.ctxPath + "/product/update", function (data) {
    Feng.success("修改成功!");
    window.parent.Product.table.refresh();
    ProductInfoDlg.close();
  }, function (data) {
    Feng.error("修改失败!" + data.responseJSON.message + "!");
  });
  ajax.set(this.productInfoData);
  ajax.setContentType("application/json")
  ajax.start();
}

$(function () {
  Feng.initValidator("productInfoForm", ProductInfoDlg.validateFields);

  //初始化
  $("#orgNo").val($("#orgNoValue").val());
  $("#serviceFeeType").val($("#serviceFeeTypeValue").val());
  $("#isPen").val($("#isPenValue").val());
  $("#penNumber").val($("#penNumberValue").val());
  $("#repayType").val($("#repayTypeValue").val());
  $("#loanType").val($("#loanTypeValue").val());

});
