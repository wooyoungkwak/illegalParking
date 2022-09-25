// 동기 json to json 통신
$.JJAjaxAsync = function (opt) {
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

// 비동기 json to json 통신
$.JJAjaxSync = function (opt) {
    if ( opt === undefined) {
        opt.success("");
        return;
    }

    $.ajax({
        url: opt.url,
        type: 'post',
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
        error: function (jqXHR, statusCode, errorThrown) {
            opt.error(statusCode);
        },
        success: function (data, statusCode, jqXHR) {
            opt.success(data);
        }
    });
}

// 파일 업로드
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
            alert(" 실패 하였습니다. 상태 코드 : " + jqXHR.status);
        },
        success: function (data, statusCode, jqXHR) {
            console.log("jqXHR.status = ",jqXHR.status);
            console.log("data = ", JSON.stringify(data));
            alert(" 업로드 되었습니다. ");
        }
    });
}

