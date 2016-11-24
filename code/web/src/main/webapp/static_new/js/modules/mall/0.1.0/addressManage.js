define(function (require, exports, module) {
    addressManage = {};
    require('jquery');
    require('doT');
    Common = require('common');

    addressManage.init = function () {
        getAddressList();
        setProvince();
    }

    $(document).ready(function(){
        $('body').on('click','#addAddress',function(){//新增地址
            $("#user_input,#province,#city,#district,#address_input,#telephone_input").val("");
            $('#addresseDiv').toggle();
        });

        $('body').on('click','.td-center span',function(e){//设置默认地址
            var url = '/mall/order/address/updateDefault.do';
            var id = $(this).parent().attr('id');
            Common.getPostData(url,{addressId:id},function(resp){
                if(resp.code == '200'){
                    getAddressList();
                }
            });
        });

        $('body').on('click','#submit',function(){//新增/修改地址
            var addressId = $(this).attr('addressId');
            addAddress(addressId);
        });

        $('body').on('click','.del',function(){
            $('.bg').show();
            $('.confirm').show().attr('addressId',$(this).attr('addressId'));
        });

        $('body').on('click','.yes',function(){
            var url = '/mall/order/address/delete.do';
            var id = $('.confirm').attr('addressId');
            Common.getData(url,{id:id},function(resp){
                $('.confirm').hide();
                $('.bg').hide();
                getAddressList();
            });
        });

        $('body').on('click','.no',function(){
            $('.confirm').hide();
            $('.bg').hide();
        });

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
    });

    function getAddressList() {
        var url = "/mall/order/address/list.do?dd=" + Math.random();
        Common.getData(url, {}, function (rep) {
            Common.render({
                tmpl: $('#addressScript'),
                data: rep,
                context: '#addressTable',
                overwrite:1
            });
        });
        $('table tr .td-center').each(function(){
            if($(this).attr('isDefault') == 1){
                console.log(1);
                $(this).find('button').show();
                $(this).find('span').hide();
            }
        });
    }

    addAddress = function (addressId) {
        var address = {};
        address.user = jQuery("#user_input").val();
        address.province = $("#province").val();
        address.city = $("#city").val();
        address.district = $("#district").val();
        address.address = jQuery("#address_input").val();
        address.tel = jQuery("#telephone_input").val();
        var re = /^(13[0-9]|15[0-9]|18[0-9])\d{8}$/;
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

        if(addressId){//修改地址
            var url = '/mall/order/address/update.do';
            address.addressId = addressId;
            Common.getPostData(url, address, function (rep) {
                getAddressList();
                $("#user_input,#address_input,#telephone_input").val("");
                $('#addresseDiv').hide();
            });
        }else{//新增地址
            var url = "/mall/order/address/add.do";
            Common.getPostData(url, address, function (rep) {
                Common.render({
                    tmpl: $('#addressScript'),
                    data: rep,
                    context: '#addressCtx',
                    "overwrite": 1
                });
                $("#user_input,#address_input,#telephone_input").val("");
                $('#addresseDiv').hide();
                getAddressList();
            });
        }

    }

    editAddress = function(id,userName,province,city,district,address,tel) {
        $('#submit').attr('addressId',id);
        $("#user_input").val(userName);
        $("#province").val(province).trigger('change');
        $("#city").val(city).trigger('change');
        $("#district").val(district);
        $("#address_input").val(address);
        $("#telephone_input").val(tel);

        $('#addresseDiv').show();
    }

    /**
     * 省市区
     * */
    setProvince = function () {
        var url = 'http://apis.map.qq.com/ws/district/v1/list';
        var param = {key: 'OZPBZ-FBMAU-6NSVD-4G2XT-SJ2F5-D5BHH', output: 'jsonp'};
        $.ajax({
            url: url,
            type: 'get',
            data: param,
            dataType: 'JSONP',
            success: function (data) {
                Common.render({
                    tmpl: $('#proviceTempJS'),
                    data: data.result[0],
                    context: '#province'
                });
            }
        });
    }

    setCity = function (provinceId) {
        if (provinceId) {
            var url = 'http://apis.map.qq.com/ws/district/v1/getchildren';
            var param = {id: provinceId, key: 'OZPBZ-FBMAU-6NSVD-4G2XT-SJ2F5-D5BHH', output: 'jsonp'};
            $.ajax({
                url: url,
                type: 'get',
                data: param,
                dataType: 'JSONP',
                success: function (data) {
                    Common.render({
                        tmpl: $('#cityTempJS'),
                        data: data.result[0],
                        context: '#city'
                    });
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
                type: 'get',
                data: param,
                dataType: 'JSONP',
                success: function (data) {
                    Common.render({
                        tmpl: $('#districtTempJS'),
                        data: data.result[0],
                        context: '#district'
                    });
                }
            });
        }
    }


    module.exports = addressManage;
});
