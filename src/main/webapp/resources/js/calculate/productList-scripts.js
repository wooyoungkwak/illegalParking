$(function (){

    function search(pageNumber) {
        if (pageNumber === undefined) {
            $('#pageNumber').val("1");
        } else {
            $('#pageNumber').val(pageNumber);
        }
        location.href = _contextPath  + "/productList?" + $('form').serialize();
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

            search(pageNumber);
        });

        $('#pageSize').on("change", function (){
            $('#pageNumber').val(1);
            search();
        })

        $('#brand').on('change', function () {
            switch ($(this).val()) {
                case "STARBUGS":
                    $('#brandImg').attr('src', "https://image.istarbucks.co.kr/common/img/main/rewards-logo.png");
                    break;
                case "BASKINROBBINS":
                    $('#brandImg').attr('src', $.getContextPath() + "/../fileUpload/image/beskinrobbinslogo.jpg");
                    break;
            }
        });

        // 주차장 정보 표시
        $('#productTable tr').on('click', function () {

            let productSeqStr = $(this).children("td:eq(0)").text();
            productSeq = Number.parseInt(productSeqStr);

            let result = $.JJAjaxAsync({
                url: _contextPath + '/product/get',
                data: {
                    productSeq: productSeq
                }
            });

            log(result);

            // $.each(result, function (key, value) {
            //     $('#' + key).val(value);
            // });

            $('#productTable').hide();
            $('#productAddTag').show();
        });

        $('#close').on('click', function (){
            $('#productTable').show();
            $('#productAddTag').hide();
        });

        $('#productAddTag').hide();
    }

    initialize();

});