$.JJAjax = function (opt) {
    let result = '';

    if ( opt === undefined) {
        return result;
    }

    $.ajax({
        url: opt.url,
        type: 'post',
        async: false,
        contentType : 'application/json; charset=UTF-8',
        data: JSON.stringify(opt.data),
        dataType: "json",
        beforeSend: function (xhr, options) {
            xhr.setRequestHeader('AJAX', true);
        },
        xhr: function () {
            let myXhr = $.ajaxSettings.xhr();
            return myXhr;
        },
        error: function (jqXHR, statusCode, errorThrown) {},
        success: function (data, statusCode, jqXHR) {
            result = data;
        }
    });

    return result;
}

window.addEventListener('DOMContentLoaded', event => {

    // Toggle the side navigation
    const sidebarToggle = document.body.querySelector('#sidebarToggle');
    if (sidebarToggle) {
        // Uncomment Below to persist sidebar toggle between refreshes
        // if (localStorage.getItem('sb|sidebar-toggle') === 'true') {
        //     document.body.classList.toggle('sb-sidenav-toggled');
        // }
        sidebarToggle.addEventListener('click', event => {
            event.preventDefault();
            document.body.classList.toggle('sb-sidenav-toggled');
            localStorage.setItem('sb|sidebar-toggle', document.body.classList.contains('sb-sidenav-toggled'));
        });
    }

});

$(document).ready(function () {
   $('#back').on('click', function (){
       window.history.back();
   })
});

$.fn.fileUpload = function (opt) {

    let formData = new FormData();

    if ( opt.description !== undefined ){
        formData.append("description", opt.description);
    }

    $(this).find(':file').each(function (){
        let key = $(this).attr("name");
        if ( key == undefined) {
            return;
        }
        $.each($(this)[0].files, function(index, file){
            formData.append(key, file);
        });
    });

    $.ajax({
        url: opt.contextPath,
        type: 'post',
        processData: false,
        contentType: false,
        data: formData,
        dataType: "json",
        beforeSend: function (xhr, options) {
            xhr.setRequestHeader('AJAX', true);
        },
        xhr: function () {
            let myXhr = $.ajaxSettings.xhr();
            return myXhr;
        },
        error: function (jqXHR, statusCode, errorThrown) {
            console.log("====================== err =========================");
            console.log(jqXHR.status);
            console.log(statusCode, errorThrown);
            console.log(errorThrown);
            console.log("====================== err =========================");
        },
        success: function (data, statusCode, jqXHR) {
            console.log("jqXHR.status = ",jqXHR.status);
            console.log("data = ", JSON.stringify(data));
        }
    });
}