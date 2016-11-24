define('eaddress', ['jquery', 'doT', 'common'], function (require, exports, module) {

    var eaddress = {};
    require('jquery');
    require('doT');
    var common = require('common');

    var totalPrice = 0;//商品总价格（分）
    var totalExp = 0;//用户总积分
    var totalAvailableExp = 0;//用户总可用积分
    var availableExp = 0;//用户可用积分
    var expOff = 0;//积分实际抵用价格（分）
    var totalVoucherOff = 0;//优惠券可抵用总价（分）
    var voucherOff = 0;//优惠券实际抵用价格（分）
    var yunfei = 0;//运费


    //************************************************初始化***********************************************/

    eaddress.init = function (goodsList) {
        load();
        getExperience();
        getVouchers('unused');
        goodslist(goodsList);
        getCategories();
    }

    $(document).ready(function () {
        $('body').on('click', '.em-new', function () {//新增地址
            $("#user_input,#province,#city,#district,#address_input,#telephone_input").val("");
            $('#newAddressDiv').toggle();
        });

        $('body').on('click', '.addressDD', function () {
            $(this).addClass('dd-default').siblings().removeClass('dd-default');
            $(this).find('i').css('visibility', 'visible');
            $(this).siblings().find('i').css('visibility', 'hidden');
            $(this).find('.spa4 em').show();
            $(this).siblings().find('.spa4 em').hide();
            $(this).children('.spa5').each(function () {
                if ($(this).attr('isDefault') == '1') {
                    $(this).children('button').show();
                    $(this).children('.setDefault').hide();
                    $(this).parent().siblings('dd').find('.span5 button,em').hide();
                } else {
                    $(this).children('em').show();
                    $(this).parent().siblings('dd').find('.span5 button,em').hide();
                }
            });
            var id = $(this).attr('id');
            var province = $(this).attr('province');
            var url = "/mall/order/address/detail.do";
            common.getData(url, {id: id}, function (rep) {
                fillinInfo(rep);
            });

            //计算运费
            expressAmount(province);
        });

        $('.addressDD').first().trigger('click');

        $('body').on('click', '.addressDD .spa5 .setDefault', function (e) {//设置默认地址
            e.stopPropagation();

            var self = $(this);
            var url = '/mall/order/address/updateDefault.do';
            var id = self.parents('dd').attr('id');
            common.getPostData(url, {addressId: id}, function (resp) {
                if (resp.code == '200') {
                    load();
                    $('.addressDD').first().trigger('click');
                }
            });
        });

        //积分
        $('body').on('blur', '#usedExp', function () {//格式化个数
            var num = $('#usedExp').val().replace(/[^0-9]/ig, "") - 0;
            if (num > availableExp) {
                num = availableExp;
            }
            $('#usedExp').val(num);
            $('#orderusedExp').val(num);
            $('.expoff').text("￥-" + (num / 10).toFixed(2));
            expOff = num * 10;
            var price = (totalPrice - voucherOff - expOff) / 100 + yunfei;
            price = price.toFixed(2);
            $(".tprice_em").text("￥" + price);
        });

        //优惠券
        $('input:radio[name=voucher]').on('click', function () {
            $('#ordervoucherId').val($(this).val());
            var off = $(this).attr('off');
            voucherOff = off < totalVoucherOff ? off : totalVoucherOff;

            var temp = (totalPrice - voucherOff) / 10;
            availableExp = totalAvailableExp < temp ? totalAvailableExp : temp;
            expOff = expOff < availableExp ? expOff : availableExp;
            $('#availableExp').text(availableExp);
            $('#usedExp').val(expOff);
            $('.expoff').text("￥- " + (expOff / 10).toFixed(2));
            $('#orderusedExp').val(expOff);

            $('#voucheroff').text("￥-" + (voucherOff / 100).toFixed(2));
            var price = (totalPrice - voucherOff - expOff * 10) / 100;
            price += yunfei;
            price = price.toFixed(2);
            $(".tprice_em").text("￥" + price);
        });

        //选择家长
        $('input:radio[name=parent]').click(function () {
            $('#parentId').val($(this).val());
        });
        $("input[name='parent']").filter(':checked').trigger('click');

        $('#submit').click(function () {
            if ($('input[name="goodsList"]').length != 0) {
                var voucherId = $("input[name='voucher']:checked").val();
                var flag = false;
                if (voucherId != undefined) {
                    var requestData = {};
                    requestData.voucherId = voucherId;
                    common.getData('/mall/order/isVoucher.do', requestData, function (resp) {
                        if (resp.code == 200) {
                        } else {
                            alert("您所选择的优惠券已经被使用，请重新选择");
                            flag = true;
                        }
                    });
                }
                if (flag) {
                    return false;
                }


                if ($('.store-affirm-infof').length != 0) {
                    pushOrderToParent();
                } else {
                    buyNow();
                }
            } else {
                createOrder();
            }
        });
    });

    function getCategories() {
        common.getData("/mall/categories.do", {}, function (resp) {
            common.render({
                tmpl: '#listAcTml',
                data: resp.goodsCategories,
                context: '#listAc',
                overwrite: 1
            });

            $('.listData').each(function () {
                var parentId = $(this).attr('value');
                var that = this;
                common.getData("/mall/categories.do", {level: 2, parentId: parentId}, function (resp) {
                    common.render({
                        tmpl: '#listTml',
                        data: resp.goodsCategories,
                        context: that,
                        overwrite: 1
                    });
                });
            });

        })
    }

    load = function () {
        var url = "/mall/order/address/list.do?dd=" + Math.random();
        common.getData(url, {}, function (rep) {
            common.render({
                tmpl: $('#addressScript'),
                data: rep,
                context: '#addressCtx',
                overwrite: 1
            });
            fillinInfo(rep[0]);
        });
    }

    getExperience = function () {
        common.getData("/mall/order/experiences.do", {}, function (resp) {
            totalExp = resp.exp;
            $('#totalE').text(totalExp);
            $("#totalExp").text(totalExp);
        });
    }

    getVouchers = function (state) {
        common.getData("/mall/vouchers.do", {state: state}, function (resp) {
            var vouchers = resp.vouchers;
            $("#vouchercount").text(vouchers.length);
            if (vouchers.length > 0) {
                common.render({
                    tmpl: '#voucherTmpl',
                    data: vouchers,
                    context: '#voucherCtx',
                    overwrite: 1
                });
            } else {
                $("#vouchercount").hide();
            }
        });
    }

//******************************************商品信息*************************************************************/

    goodslist = function (goodsList) {
        if (goodsList != null) {
            common.render({
                tmpl: $('#goods_script'),
                data: goodsList,
                context: '#goodsList'
            });
            totalPrice = goodsList[0].sumPrice;
            $("#tprice_div").text("￥" + (totalPrice / 100).toFixed(2));
            $(".tprice_em").text("￥" + (totalPrice / 100).toFixed(2));
            off(goodsList);
        } else {
            var url = "/mall/cars/list.do?type=1&cc=" + Math.random();
            common.getData(url, {}, function (rep) {
                if (rep.list.length > 0) {
                    $('#goodscount').text(rep.list.length);
                    try {
                        jQuery("#tprice_div").text("￥" + rep.priceStr);
                        jQuery(".tprice_em").text("￥" + rep.priceStr);
                        totalPrice = rep.price;

                        common.render({
                            tmpl: $('#goods_script'),
                            data: rep.list,
                            context: '#goodsList'
                        });
                        off(rep.list);
                    } catch (x) {

                    }
                }
            });
        }
    }

    function off(goodsList) {//计算可用积分&优惠券
        var totalExpOff = 0;
        for (var i = 0; i < goodsList.length; i++) {
            totalExpOff = goodsList[i].experienceOff * goodsList[i].count + totalExpOff;
            totalVoucherOff = goodsList[i].voucherOff * goodsList[i].count + totalVoucherOff;
        }
        totalExpOff = totalExpOff / 10;
        totalAvailableExp = totalExpOff < totalExp ? totalExpOff : totalExp;
        availableExp = totalAvailableExp;
        $('#availableExp').text(availableExp);
    }


    //**********************************************提交订单********************************************************/
    createOrder = function () {
        var addressId = jQuery("#addressId").val();
        if (!addressId) {
            alert("请正确填写收件人信息");
            return;
        }


        var length = jQuery(".goods_List").length;
        if (length == 0) {
            alert("请选择商品");
            return;
        }
        var message = buildmessage();
        $('#message').val(message);
        jQuery("#orderForm").submit();
    }

    /**
     * 立即购买
     * */
    buyNow = function () {
        $('#orderForm').attr('action', '/mall/order/createNow.do');
        createOrder();
    }

    /**
     * 推送订单给家长
     * */
    pushOrderToParent = function () {
        $('#orderForm').attr('action', '/mall/order/pushOrder.do');
        createOrder();
    }

    function buildmessage() {
        var message = '';
        $('.goods_List').each(function (index, ele) {
            var goodsId = $(ele).find('.store-affirm-YL').find('input').val();
            var msg = $(ele).find('.store-affirm-LY').find('input').val();
            message += goodsId + '-' + msg + ',';
        })
        return message;
    }

    //************************************************地址***************************************************/
    fillinInfo = function (address) {
        if (address) {
            $("#addressId").val(address.id);
            $("#label_name").text(address.userName);
            $("#label_address").text(address.province + " " + address.city + " " + address.district + " " + address.address);
            $("#label_tel").text(address.telephone);
        }
    }

    addAddress = function () {
        var address = {};
        address.user = jQuery("#user_input").val();
        address.province = $("#province").val();
        address.city = $("#city").val();
        address.district = $("#district").val();
        address.address = jQuery("#address_input").val();
        address.tel = jQuery("#telephone_input").val();
        var re = /^(13[0-9]|15[0-9]|17[0-9]|18[0-9])\d{8}$/;
        if (!re.test(address.tel)) {
            alert("请正确输入手机号码");
            return;
        }
        if (!address.user) {
            alert("请输入收件人");
            return;
        }
        if (!address.address) {
            alert("请输入收件人地址");
            return;
        }
        if (!address.province || !address.city) {
            alert("请选择省市区");
            return;
        }


        var addressId = $('#addBtn').attr('addressId');
        if (addressId) {//修改地址
            var url = '/mall/order/address/update.do';
            address.addressId = addressId;
            common.getPostData(url, address, function (rep) {
                load();
                $('.addressDD').first().trigger('click');
                $('.addressDD').each(function () {
                    if ($(this).attr('id') == rep.id) {
                        $(this).trigger('click');
                    }
                });
                $("#user_input,#address_input,#telephone_input").val("");
                $('#newAddressDiv').hide();
            });
        } else {//新增地址
            var url = "/mall/order/address/add.do";
            common.getPostData(url, address, function (rep) {
                common.render({
                    tmpl: $('#addressScript'),
                    data: rep,
                    context: '#addressCtx',
                    "overwrite": 1
                });
                $("#user_input,#address_input,#telephone_input").val("");
                if (rep[0].isDefault == 1) {
                    fillinInfo(rep[1]);
                    $('#addressCtx dd').eq(1).click();
                } else {
                    fillinInfo(rep[0]);
                    $('#addressCtx dd').eq(0).click();
                }
                $('#newAddressDiv').hide();
            });
        }

    }

    editAddress = function (id, userName, province, city, district, address, tel) {
        $('#addBtn').attr('addressId', id);
        $("#user_input").val(userName);
        $("#province").val(province).trigger('change');
        $("#city").val(city).trigger('change');
        $("#district").val(district);
        $("#address_input").val(address);
        $("#telephone_input").val(tel);

        $('#newAddressDiv').show();
    }


    /**
     * 计算运费
     * */
    expressAmount = function (address) {
        var url = '/mall/order/expressAmount.do';
        var goodsList = [];
        $('#goodsList').children().each(function () {
            var gooodsId = $(this).find("input[name='goodsId']").val();
            var count = $(this).find(".store-affirm-SL").children().text();
            goodsList.push({goodsId: gooodsId, count: count});
        });
        var goodsExpTemp = {address: address, goodsList: goodsList};
        $.ajax({
            url: url,
            type: 'post',
            data: JSON.stringify(goodsExpTemp),
            dataType: 'json',
            contentType: 'application/json',
            success: function (data) {
                yunfei = Number(data);
                var price = (totalPrice - voucherOff - expOff) / 100;
                if (totalPrice >= 9900) {
                    yunfei = 0;
                }
                if (price < 0) {
                    price = 0;
                }
                price += yunfei;
                price = price.toFixed(2);
                $('#expAmount').text("￥" + yunfei.toFixed(2));
                $(".tprice_em").text("￥" + price);
            },
            error: function (xhr, e) {
                console.log(e);
            }
        });
    }

    /**
     * 省市区
     * */
    setProvince = function () {
        var url = 'http://apis.map.qq.com/ws/district/v1/list';
        var param = {key: 'OZPBZ-FBMAU-6NSVD-4G2XT-SJ2F5-D5BHH', output: 'jsonp'};
        $.ajax({
            url: url,
            async: false,
            type: 'get',
            data: param,
            dataType: 'JSONP',
            success: function (data) {
                common.render({tmpl: $('#proviceTempJS'), data: data.result[0], context: '#province'});
            }
        });
    }

    setCity = function (provinceId) {
        if (provinceId) {
            var url = 'http://apis.map.qq.com/ws/district/v1/getchildren';
            var param = {id: provinceId, key: 'OZPBZ-FBMAU-6NSVD-4G2XT-SJ2F5-D5BHH', output: 'jsonp'};
            $.ajax({
                url: url,
                async: false,
                type: 'get',
                data: param,
                dataType: 'JSONP',
                success: function (data) {
                    common.render({tmpl: $('#cityTempJS'), data: data.result[0], context: '#city'});
                    setDistrict(data.result[0][0].id);
                }
            });
        }
    }

    setDistrict = function (cityId) {
        if (cityId) {
            var url = 'http://apis.map.qq.com/ws/district/v1/getchildren';
            var param = {id: cityId, key: 'OZPBZ-FBMAU-6NSVD-4G2XT-SJ2F5-D5BHH', output: 'jsonp'};
            $.ajax({
                url: url,
                async: false,
                type: 'get',
                data: param,
                dataType: 'JSONP',
                success: function (data) {
                    common.render({tmpl: $('#districtTempJS'), data: data.result[0], context: '#district'});
                }
            });
        }
    }

    setProvince();
    $('#province').change(function () {
        var pid = $(this).find("option:selected").attr("pid");
        $('#city').empty();
        $('#district').empty();
        setCity(pid);
    });
    $('#city').change(function () {
        var cid = $(this).find("option:selected").attr('cid');
        $('#district').empty();
        setDistrict(cid);
    });


    module.exports = eaddress;
});
