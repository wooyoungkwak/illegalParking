$(document).ready(() => {
    $('#inputGroupFileAddon03').on('click', function () {
        $('#inputGroupFile04').parent().fileUpload({
            contextPath: location.host + "/files/parking"
        });

        // console.log(" contextPath = ", location.host + "/files/parking");
    });
});