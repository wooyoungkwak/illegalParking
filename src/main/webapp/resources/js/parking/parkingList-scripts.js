$(function () {

    let parkingSeq ;

    function search(pageNumber) {
        if (pageNumber === undefined) {
            $('#pageNumber').val("1");
        } else {
            $('#pageNumber').val(pageNumber);
        }
        location.href = _contextPath + "/parkingList?" + $('form').serialize();
    }

    function getData(){
        let arr = $('#data').serializeArray();
        let data = {};
        $(arr).each(function(index, obj){
            data[obj.name] = obj.value;
        });
        return data;
    }

    function initialize() {
        $('#orderBy a').on('click', function () {
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

        $('#paginationSize').on("change", function () {
            $('#pageSize').val($(this).val());
            search();
        });

        // 주차장 정보 표시
        $('#tableList tbody tr').on('click', function () {
            let parkingSeqStr = $(this).children("td:eq(0)").text();
            parkingSeq = Number.parseInt(parkingSeqStr);

            let result = $.JJAjaxAsync({
                url: _contextPath + '/get',
                data: {
                    parkingSeq: parkingSeq
                }
            });

            $.each(result, function (key, value) {
                $('#' + key).val(value);
                if (key == "rdnmadr" ) {
                    $('#rdnmadr').val(result.lnmadr);
                }
            });

            $('#parkingTable').hide();
            $('#parkingAdd').show();
        });

        $('#modify').on('click', function (){

            let data = getData();
            data.parkingSeq = parkingSeq;

            if ( confirm("등록 하시겠습니까?") ) {
                $.JJAjaxSync({
                    url: _contextPath + '/set',
                    data: data,
                    success: function (){
                        if ( confirm(" 계속 등록 하시겠습니까? " ) ) {
                            location.href = location.href;
                        } else {
                            location.href = _contextPath + '/parkingList';
                        }
                    } ,
                    err: function (code){
                        alert("등록 실패 하였습니다. (에러코드 : " + code + ")");
                    }
                });
            } else {
                log(getData());
            }
        });

        $('#close').on('click', function () {
            $('#parkingTable').show();
            $('#parkingAdd').hide();
        });

        $('#parkingAdd').hide();

    }

    initialize();

});