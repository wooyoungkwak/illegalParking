$(function (){

    function search(num){
        num = ( num == undefined ? 1 : num );
        log("num = ", num);
    }

    function initialize() {
        //
        $('#pagination').find("li").on('click', function (){
            $(this).parent().children().removeClass("active");
            $(this).addClass("active");
            search(1);
        });
    }

    initialize();

});