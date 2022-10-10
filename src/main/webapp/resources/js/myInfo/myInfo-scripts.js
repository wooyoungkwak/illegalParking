$(function () {

    // 초기화
    function initialize() {
        let result = $.JJAjaxAsync({
            url: _contextPath + '/api/myInfo/get',
            data: {
                userSeq: _userSeq
            }
        });

        if (result.success) {
            $.each(result.data, function (key, value) {
                $('#' + key).val(value);
            });
        }

    }

    initialize();

});