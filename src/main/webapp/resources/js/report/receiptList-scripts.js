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
        location.href = _contextPath + "/receiptList?" + $('form').serialize();
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

    function initializeReceiptSetTag(report) {
        $.each(report, function (key, value) {
            if (key.indexOf('receiptStateType') > -1) {
                if (value === 'OCCUR') {
                    $('#' + key).text("신고발생");
                } else if (value === 'FORGET')  {
                    $('#' + key).text("신고누락");
                } else if (value === 'EXCEPTION') {
                    $('#' + key).text("신고제외");
                }
            } else if (key.indexOf("firstFileName") > -1) {
                $('#' + key).attr('src', encodeURI(_contextPath + "/../fileUpload/image/" + value));
            } else if (key === 'firstIllegalType') {
                if (value === 'ILLEGAL') $('#' + key).text("불법주정차");
                else if (value === 'FIVE_MINUTE') $('#' + key).text("5분주정차");
            } else if (key === 'secondIllegalType') {
                if (value === 'ILLEGAL') $('#' + key).text("불법주정차");
                else if (value === 'FIVE_MINUTE') $('#' + key).text("5분주정차");
            } else if( key === 'regDt' || key === 'firstRegDt' || key === 'secondRegDt' ) {
                $('#' + key).text(value.replace('T', ' '));
            } else if ( key === 'comments') {
                let  html = '';
                for ( let i =0; value.length > i; i++) {
                    html = `<div class="col-12"><i class="fas fa-comments"></i>  ${value[i]}</div>`;
                }
                $('#' + key).append(html);
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
        $('#reportTable tbody tr').on('click', function () {

            let reportSeqStr = $(this).children("td:eq(0)").find('input').val();
            let carNum = $(this).children("td:eq(1)").text();
            let receiptSeq = Number.parseInt(reportSeqStr);

            let result = $.JJAjaxAsync({
                url: _contextPath + '/receipt/get',
                data: {
                    receiptSeq: receiptSeq
                }
            });

            if (result.success) {
                let receipt = result.data;
                initializeReceiptSetTag(receipt);
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

        $('#close').on('click', function () {
            $('#reportMain').show();
            $('#reportSet').hide();
        });

        $('#reportSet').hide();

    }

    initialize();

});