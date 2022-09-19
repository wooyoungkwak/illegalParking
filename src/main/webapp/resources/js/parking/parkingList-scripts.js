$(function () {

    function search(pageNumber) {
        if ( pageNumber === undefined) {
            pageNumber = 1;
        }

        let paramObject = {
            pageNumber : pageNumber,
            searchStr : "asdf",
            sortStr : "desc",
            filter : "loocation"
        }

        location.href = _contextPath + $.param(paramObject);
    }

    function initialize() {

        $('#pagination').find("li").on('click', function () {
            let ul = $(this).parent();
            let totalSize = ul.children("li").length;
            if (totalSize <= 3) {
                return;
            }
            let pageNumber = Number.parseInt(ul.children('.active').text());
            if ($(this).text() === "<") {
                if ( pageNumber == 1) return;
                pageNumber = pageNumber - 1;
            } else if ($(this).text() === ">") {
                let myLocation = $(this).index();
                let activeLocation = ul.children('.active').index();
                if ( activeLocation == (myLocation-1) ) {
                    return;
                }
                pageNumber = pageNumber + 1;
            }

            search(pageNumber);
        });
    }

    initialize();

});