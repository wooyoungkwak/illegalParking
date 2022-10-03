$(function () {

    // 초기화
    function initialize() {
        let result = $.JJAjaxAsync({
            url: _contextPath + '/myInfo/get',
            data: {
                userSeq: _userSeq
            }
        });

        $.each(result, function (key, value) {
            $('#' + key).val(value);
        });

    }

    initialize();

});