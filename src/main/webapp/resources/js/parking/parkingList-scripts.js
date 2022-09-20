$(function () {

    function search(pageNumber) {
        if (pageNumber === undefined) {
            $('#pageNumber').val("1");
        } else {
            $('#pageNumber').val(pageNumber);
        }
        location.href = _contextPath + "/parkingList?" + $('form').serialize();
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

        $('#tableList tr').on('click', function (){

            let parkingSeqStr = $(this).children("td:eq(0)").text();
            let parkingSeq = Number.parseInt(parkingSeqStr);

            let result = $.JJAjaxAsync({
                url: _contextPath,
                data: {
                    parkingSeq: parkingSeq
                }
            });

            log(result);
            $('#parkingTable').hide();
            $('#parkingDetail').show();
        });

        $('#close').on('click', function (){
            $('#parkingTable').show();
            $('#parkingDetail').hide();
        });

        $('#parkingDetail').hide();
    }

    initialize();

});