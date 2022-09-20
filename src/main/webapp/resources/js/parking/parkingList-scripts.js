$(function () {

    function search() {
        let pageNumber = $('#pageNumber').val();
        if ( pageNumber === "") {
            $('#pageNumber').val("1");
        }
        location.href = _contextPath  + "/parkingList?" + $('form').serialize();
    }

    function initialize() {
        $('#orderBy a').on('click', function (){
            search();
        })

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

            $('#pageNumber').val(pageNumber);
            search();
        });

        $('#pageSize').on("change", function (){
            search();
        })
    }

    initialize();

});