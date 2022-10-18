$.search = function (pageNumber) {
    if (pageNumber === undefined) {
        $('#pageNumber').val("1");
    } else {
        $('#pageNumber').val(pageNumber);
    }
    location.href = _contextPath + "/noticeList?" + $('form').serialize();
}

