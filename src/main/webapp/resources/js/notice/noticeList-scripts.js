$.search = function (pageNumber) {
    if (pageNumber === undefined) {
        $('#pageNumber').val("1");
    } else {
        $('#pageNumber').val(pageNumber);
    }
    location.href = _contextPath + "/noticeList?" + $('form').serialize();
};

$.closeNoticeSet = function () {
    $('#noticeMain').show();
    $('#noticeSet').hide();
};

$.openNoticeSet = function () {
    $('#noticeMain').hide();
    $('#noticeSet').show();
};

$.closeNoticeView = function () {
    $('#noticeMain').show();
    $('#noticeView').hide();
};

$.openNoticeView = function () {
    $('#noticeMain').hide();
    $('#noticeView').show();
};

$.initializeNoticeView = function (opt) {
    $('#noticeSeqView').val(opt.noticeSeq);
    $('#subjectView').val(opt.subject);
    $('#noticeTypeView').val(opt.noticeType);
    $('#noticeTypeValueView').val(opt.noticeTypeValue);
    $('#regDtView').val(opt.regDt);
    $('#contentView').val(opt.content);
}

$.changeNoticeModify = function () {
    let data = {
        noticeSeq: $('#noticeSeqView').val(),
        subject: $('#subjectView').val(),
        noticeType: $('#noticeTypeView').val(),
        content: $('#contentView').val(),
    };

    $.initializeNoticeSet(data);

    $('#noticeSet').show();
    $('#noticeView').hide();
};

