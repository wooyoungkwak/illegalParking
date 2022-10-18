$.search = function (pageNumber) {
    if (pageNumber === undefined) {
        $('#pageNumber').val("1");
    } else {
        $('#pageNumber').val(pageNumber);
    }
    location.href = _contextPath + "/noticeList?" + $('form').serialize();
}

$.closeNoticeSet = function () {
    $('noticeMain').show();
    $('$noticeSet').hide();
}

$.openNoticeSet = function () {
    $('noticeMain').hide();
    $('$noticeSet').show();
}