$(function () {

    // 데이터 가져오기
    function getData(id) {
        let arr = $('#' + id).serializeArray();
        let data = {};
        $(arr).each(function (index, obj) {
            data[obj.name] = obj.value;
        });
        return data;
    }

    // 검색
    function search(pageNumber) {
        if (pageNumber === undefined) {
            $('#pageNumber').val("1");
        } else {
            $('#pageNumber').val(pageNumber);
        }
        location.href = _contextPath + "/groupList?" + $('form').serialize();
    }

    // 검색 입력 방식 선택
    function searchSelect(filterColumn) {
        if (filterColumn === 'LOCATION') {
            $('#searchStrGroup').hide();
            $('#searchStr2Group').show();
        } else {
            $('#searchStrGroup').show();
            $('#searchStr2Group').hide();
        }
    }

    // groupSetTag 설정
    function initializeGroupSetTag(opt) {
        $('#groupSeq').val(opt.groupSeq);
        $('#locationType').val(opt.locationType);
        $('#name').val(opt.name);

        let createTr = function (data) {
            let html = `<tr>`;
            html += `<td>${data.pointType}</td>`;
            html += `<td>${data.limitValue}</td>`;
            html += `<td>${data.value}</td>`;
            html += `<td>${data.useValue}</td>`;
            html += `<td>${data.residualValue}</td>`;
            html += `<td>${data.StartDate}</td>`;
            html += `<td>${data.StopDate}</td>`;
            html += `<td>${data.finish}</td>`;
            html += `</tr>`;
            return html;
        }

        let trs = '';
        $.each(opt.point, function (index, data) {
            trs += createTr(data);
        });

        $('#pointTable tbody').find('tr').remove();
        $('#pointTable tbody').append(trs);
    }

    //
    function initializeGroupEventTag(point){
        let createTr = function (data) {
            let html = `<tr>`;
            html += `<td><input type="hidden" value="${data.pointSeq}">${data.pointType}</td>`;
            html += `<td>${data.limitValue}</td>`;
            html += `<td>${data.value}</td>`;
            html += `<td>${data.useValue}</td>`;
            html += `<td>${data.residualValue}</td>`;
            html += `<td>${data.StartDate}</td>`;
            html += `<td>${data.StopDate}</td>`;
            html += `<td>${data.finish}</td>`;
            html += `</tr>`;
            return html;
        }
        let tr = createTr(point);
        $('#pointTable tbody').append(tr);
    }

    function initialize() {

        $('#groupSet').hide();
        $('#modalGroupAdd').hide();
        $('#modalGroupEvent').hide();
        $('#searchStr2Group').hide();

        $('#searchStr').next().on('click', function (event) {
            search();
        });

        $('#searchStr2').next().on('click', function (event) {
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

        $('#pageSize').on("change", function () {
            $('#pageNumber').val(1);
            search();
        });

        // 신고 등록 표시
        $('#userTable tbody tr').on('click', function () {

            let groupSeqStr = $(this).children("td:eq(0)").find('input').val();

            let locationType = $(this).children("td:eq(0)").text().trim();
            let name = $(this).children("td:eq(1)").text();

            let groupSeq = Number.parseInt(groupSeqStr);

            let result = $.JJAjaxAsync({
                url: _contextPath + '/point/get',
                data: {
                    groupSeq: groupSeq
                }
            });

            if (result.success) {
                let point = result.data;
                initializeGroupSetTag({
                    groupSeq: groupSeq,
                    point: point,
                    locationType: locationType,
                    name: name
                });
            } else {
                alert(result.msg);
                return;
            }

            $('#groupMain').hide();
            $('#groupSet').show();
        });

        $('#filterColumn').find('select[name="filterColumn"]').on('change', function () {
            searchSelect($(this).val());
        });

        $('#createGroupAdd').on('click', function () {
            let data = getData("groupAddForm");

            if (data.name === '') {
                alert("그룹명을 입력하세요.");
                return;
            }

            if (confirm("등록 하시겠습니까?")) {
                let result = $.JJAjaxAsync({
                    url: _contextPath + '/group/set',
                    data: data
                });

                if (result.success) {
                    search();
                    $(this).hide();
                }
            }
        });

        $('#createGroupEvent').on('click', function () {
            let data = getData("groupEventForm");
            data.groupSeq = $('#groupSeq').val();

            if (data.isPointLimit == 'on') {
                data.isPointLimit = true;
            } else {
                data.isPointLimit = false;
            }

            if (data.isTimeLimit == 'on') {
                data.isTimeLimit = true;
            } else {
                data.isTimeLimit = false;
            }

            if (confirm("등록 하시겠습니까?")) {
                let result = $.JJAjaxAsync({
                    url: _contextPath + '/point/set',
                    data: data
                });

                if (result.success) {
                    let point = result.data;
                    initializeGroupEventTag(point);
                } else {
                    alert("등록 실패 하였습니다. " + result.msg);
                }
            }
        });

        // 그룹 추가 팝업창 열기
        $('#openGroupAdd').on('click', function () {
            $('#modalGroupAdd').show();
            $('body').css({
                'overflow': 'hidden'
            });
        });

        // 이벤트 추가 팝업창 열기
        $('#openEventAdd').on('click', function () {
            $('#modalGroupEvent').show();
            $('body').css({
                'overflow': 'hidden'
            });
        });

        // 그룹 상세 보기 닫기
        $('#closeGroupSet').on('click', function () {
            $('#groupMain').show();
            $('#groupSet').hide();
        });

    }

    initialize();

});