// 신고 접수 건 파이 차트
$.drawPieChart = function(opt) {
    let options = {
        animationEnabled: true,
        // title: {
        //     text: "신고 건수"
        // },
        data: [{
            type: "doughnut",
            innerRadius: "40%",
            showInLegend: true,
            legendText: "{label}",
            indexLabel: "{label}",
            dataPoints: [
                {label: "대기", y: opt.completeCount},
                {label: "미처리", y: opt.exceptionCount},
                {label: "처리", y: opt.penaltyCount}
            ]
        }]
    };

    function draw() {
        $("#chartContainer").css("height", "300px").css("width", "500px");
        $("#chartContainer").CanvasJSChart(options);
    };

    draw();
}

// 관리 그룹 추가 이름 설정
$.setUserGroupNames = function (locationType) {
    function getNamesSelectHtml(names) {
        let html = '';
        for (let i = 0; i < names.length; i++) {
            html += '<option value="' + names[i] + '">' + names[i] + '</option>';
        }
        return html;
    }

    let result = $.JJAjaxAsync({
        url: _contextPath + '/userGroup/group/name/get',
        data: {
            locationType: locationType
        }
    });
    log(result);
    return;
    if (result.success) {
        let names = result.data;
        let html = getNamesSelectHtml(names);
        $('#name').append(html);
    }
}

// 관리 그룹 리스트 추가
$.addUserGroupList = function (data) {

    let createHtml = function (data) {
        let html = '';
        html +='<li class="nav-item">';
        html +='    <input type="hidden" value="' + data.userGroupSeq  +  '">'
        html +='    <a class="nav-link" href="#">' + data.groupName + '<i class="text-danger fa fa-times"></i></a>'
        html +='</li>'

        return html;
    }

    $('#addUserGroupNav').append(createHtml(data));
}

// 관리 그룹 리스트 이벤트 연결
$.bindUserGroupNavEvent = function (){
    $('#addUserGroupNav a').on('click', function (){
        if ( confirm("삭제 하시겠습니까") ) {

            let userGroupSeq = $(this).parent().find('input').val();
            let result = $.JJAjaxAsync({
                url: _contextPath + "/userGroup/remove",
                data: {
                    userGroupSeq: userGroupSeq
                }
            });

            if (result.success) {
                $(this).parent().remove();
            } else {
                alert("삭제를 실패 하였습니다.");
            }
        }
    });
}