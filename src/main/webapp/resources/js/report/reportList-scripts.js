$(function (){

    function getData(){
        let arr = $('#data').serializeArray();
        let data = {};
        $(arr).each(function(index, obj){
            data[obj.name] = obj.value;
        });
        return data;
    }

    function search(pageNumber) {
        if (pageNumber === undefined) {
            $('#pageNumber').val("1");
        } else {
            $('#pageNumber').val(pageNumber);
        }
        location.href = _contextPath  + "/reportList?" + $('form').serialize();
    }

    function initializeReportSetTagTitle(carNum){
        $('#reportSetTitle').text(carNum);
    }

    function initializeReportSetTagBtn(isComplete){
        if ( isComplete === undefined || isComplete === "대기") {
            $('#register').show();
            $('#modify').hide();
        }  else {
            $('#register').hide();
            $('#modify').show();
        }
    }

    function initialize() {

        $('#orderBy a').on('click', function (){
            search();
        });

        $('#search').on('click', function (event) {
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
                if ( pageNumber == 1) return;
                pageNumber = pageNumber - 1;

            } else if ($(this).text() === ">") {
                pageNumber = Number.parseInt(ul.children('.active').text());
                let myLocation = $(this).index();
                let activeLocation = ul.children('.active').index();
                if ( activeLocation == (myLocation-1) ) {
                    return;
                }
                pageNumber = pageNumber + 1;
            } else {
                pageNumber = Number.parseInt($(this).text());
            }

            search(pageNumber);
        });

        $('#pageSize').on("change", function (){
            $('#pageNumber').val(1);
            search();
        });

        // 신고 등록 표시
        $('#reportTable tr').on('click', function () {

            let reportSeqStr = $(this).children("td:eq(0)").text();
            let carNum = $(this).children("td:eq(3)").text();

            reportSeq = Number.parseInt(reportSeqStr);
            // let result = $.JJAjaxAsync({
            //     url: _contextPath + '/get',
            //     data: {
            //         reportSeq: reportSeq
            //     }
            // });
            //
            // $.each(result, function (key, value) {
            //     $('#' + key).val(value);
            // });

            initializeReportSetTagTitle(carNum);
            initializeReportSetTagBtn();

            $('#reportMain').hide();
            $('#reportSet').show();
        });


        $('#modify').on('click', function (){

            let data = getData();
            data.reportSeq = reportSeq;

            // if ( confirm("등록 하시겠습니까?") ) {
            //     $.JJAjaxSync({
            //         url: _contextPath + '/set',
            //         data: data,
            //         success: function (){
            //             alert("등록 되었습니다.");
            //         } ,
            //         err: function (code){
            //             alert("등록 실패 하였습니다. (에러코드 : " + code + ")");
            //         }
            //     });
            // } else {
            //     log(getData());
            // }
        });

        $('#close').on('click', function () {
            $('#reportMain').show();
            $('#reportSet').hide();
        });

        $('#reportSet').hide();

    }

    initialize();

});