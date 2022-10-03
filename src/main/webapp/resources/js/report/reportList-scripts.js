$(function () {

    // 신고 대상 등록 정보 ( 대상 / 설명 / 처리 )
    function getData() {
        let arr = $('#data').serializeArray();
        let data = {};
        $(arr).each(function (index, obj) {
            data[obj.name] = obj.value;
        });
        return data;
    }

    // 검색
    function search(pageNumber) {
        if (pageNumber === undefined) {
            $('#pageNumber').val("1");
        } else {
            $('#pageNumber').val(pageNumber);
        }
        location.href = _contextPath + "/reportList?" + $('form').serialize();
    }

    // 검색 입력 방식 선택
    function searchSelect(filterColumn) {
        if (filterColumn === 'RESULT') {
            $('#searchStrGroup').hide();
            $('#searchStr2Group').show();
        } else {
            $('#searchStrGroup').show();
            $('#searchStr2Group').hide();
        }
    }

    //
    function initializeReportSetTagTitle(carNum) {
        $('#reportSetTitle').text(carNum);
    }

    function initializeReportSetTag(report) {
        $.each(report, function (key, value) {
            if (key.indexOf("FileName") > -1) {
                $('#' + key).attr('src', encodeURI(_contextPath + "/../fileUpload/image/" + value));
            } else if (key === 'firstIllegalType') {
                if (value === 'ILLEGAL') $('#' + key).text("불법주정차");
                else if (value === 'FIVE_MINUTE') $('#' + key).text("5분주정차");
            } else if (key === 'secondIllegalType') {
                if (value === 'ILLEGAL') $('#' + key).text("불법주정차");
                else if (value === 'FIVE_MINUTE') $('#' + key).text("5분주정차");
            } else if (key === 'note') {
                $('#' + key).val(value === null ? "" : value);
            } else if (key === 'resultType') {
                if (value === undefined || value === 'WAIT') {
                    $('#register').show();
                } else {
                    $('#register').hide();
                    $('#setResultType').attr("disabled", true);
                    $('#note').attr("disabled", true);
                }
                $('#setResultType').val(value);
            } else {
                $('#' + key).text(value);
            }
        });

    }

    function initialize() {

        $('#orderBy a').on('click', function () {
            search();
        });

        $('#searchStr').next().on('click', function (event) {
            search();
        });

        $('#searchStr2').next().on('click', function (event) {
            search();
        });

        $('#pagination').find("li").on('click', function () {
            let ul = $(this).parent();
            let totalSize = ul.children("li").length;
            if (totalSize <= 3) {
                return;
            }
            let pageNumber;
            if ($(this).text() === "<") {
                pageNumber = Number.parseInt(ul.children('.active').text());
                if (pageNumber == 1) return;
                pageNumber = pageNumber - 1;

            } else if ($(this).text() === ">") {
                pageNumber = Number.parseInt(ul.children('.active').text());
                let myLocation = $(this).index();
                let activeLocation = ul.children('.active').index();
                if (activeLocation == (myLocation - 1)) {
                    return;
                }
                pageNumber = pageNumber + 1;
            } else {
                pageNumber = Number.parseInt($(this).text());
            }

            search(pageNumber);
        });

        $('#pageSize').on("change", function () {
            $('#pageNumber').val(1);
            search();
        });

        // 신고 등록 표시
        $('#reportTable tr').on('click', function () {

            let reportSeqStr = $(this).children("td:eq(0)").text();
            let carNum = $(this).children("td:eq(3)").text();

            let reportSeq = Number.parseInt(reportSeqStr);

            let result = $.JJAjaxAsync({
                url: _contextPath + '/get',
                data: {
                    reportSeq: reportSeq
                }
            });

            if (result.success) {
                let report = result.data;
                initializeReportSetTag(report);
            } else {
                alert("데이터 요청을 실패 하였습니다. ");
                return;
            }

            initializeReportSetTagTitle(carNum);

            $('#reportMain').hide();
            $('#reportSet').show();
        });

        $('#filterColumn').find('select[name="filterColumn"]').on('change', function () {
            searchSelect($(this).val());
        });

        $('#register').on('click', function () {

            let data = getData();
            if (data.setResultType === 'WAIT') {
                alert("결과를 선택 하세요.");
                return;
            }

            data.reportSeq = reportSeq;
            data.userSeq = _userSeq;

            if (confirm("등록 하시겠습니까?")) {
                $.JJAjaxSync({
                    url: _contextPath + '/set',
                    data: data,
                    success: function (data) {
                        if (data.success) {
                            alert("등록 되었습니다.");
                            search();
                        } else {
                            alert("등록 실패 하였습니다.");
                        }
                    },
                    err: function (code) {
                        alert("등록 실패 하였습니다. (에러코드 : " + code + ")");
                    }
                });
            }
        });

        $('#close').on('click', function () {
            $('#reportMain').show();
            $('#reportSet').hide();
        });

        $('#reportSet').hide();

    }

    initialize();

});