$(function (){

    function search(num){
        num = ( num == undefined ? 1 : num );
        log("num = ", num);
    }

    // 신고 목록 초기화
    function initialize() {
        //
        $('#pagination').find("li").on('click', function (){
            let index;
            if ( $(this).text() === "<") {
                index = $(this).parent().children('.active').index();
                return;
            } else  if ($(this).text() === ">" ) {
                index = $(this).parent().children('.active').index();
                return;
            }

            $(this).parent().children().removeClass("active");
            $(this).addClass("active");
            search(22);
        });

        //
        $('#reportList').find("tr").on('click', function (){
            log ( $(this).children("td:first-child").text() );
        });
    }

    initialize();
});