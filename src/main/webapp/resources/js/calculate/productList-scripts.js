$(function () {

    // =========== productAdd 전용 ==========

    // 데이터 가져오기
    $.getDataByProduct = function () {
        let arr = $('#data').serializeArray();
        let data = {};
        $(arr).each(function (index, obj) {
            data[obj.name] = obj.value;
        });
        data.userSeq = _userSeq;
        data.pointValue = Number(data.pointValue);
        return data;
    }

    // =========== productList 전용 ==========

    // 검색
    function search(pageNumber) {
        if (pageNumber === undefined) {
            $('#pageNumber').val("1");
        } else {
            $('#pageNumber').val(pageNumber);
        }
        location.href = _contextPath + "/productList?" + $('form').serialize();
    }

    // 검색 입력 방식 선택 함수
    $.searchSelect = function (filterColumn) {
        if ( filterColumn === 'brand') {
            $('#searchStrGroup').hide();
            $('#searchStr2Group').show();
        } else {
            $('#searchStrGroup').show();
            $('#searchStr2Group').hide();
        }
    }

    // =========== 공통 ==========

    // 초기화
    $.initialize = function () {

        $('#orderBy a').on('click', function () {
            search();
        });

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

        // 제품 테이블 항목 이벤트
        $('#productTable tbody tr').on('click', function () {

            let productSeqStr = $(this).children("td:eq(0)").text();
            productSeq = Number.parseInt(productSeqStr);

            let result = $.JJAjaxAsync({
                url: _contextPath + '/product/get',
                data: {
                    productSeq: productSeq
                }
            });

            if ( result.success ) {
                $.each(result.data, function (key, value) {
                    $('#' + key).val(value);
                });
            }

            $('#productSeq').val(productSeq);
            $('#productTable').hide();
            $('#productAddTag').show();
        });

        // 등록 이벤트
        $('#register').on('click', function () {
            $.JJAjaxSync({
                url: _contextPath + "/product/set",
                data: $.getDataByProduct(),
                success: function () {
                    if (confirm(" 등록 되었습니다. \n 계속 등록 하시겠습니까? ")) {
                        location.href = location.href;
                    } else {
                        location.href = _contextPath + '/productList';
                    }
                },
                error: function (code) {
                    alert("등록 실패 하였습니다. (에러코드 : " + code + ")");
                }
            });

        });

        // 수정 이벤트
        $('#modify').on('click', function () {
            // let data = getDataByProduct();
            // data.productSeq = Number($('#productSeq').val());
            //
            // $.JJAjaxSync({
            //     url: _contextPath + "/product/modify",
            //     data: data,
            //     success: function (ret) {
            //         log(ret);
            //
            //         if ( ret.success ) {
            //             alert(" 수정 되었습니다.");
            //             location.href = location.href;
            //         } else {
            //             alert("등록 실패 하였습니다.");
            //         }
            //     },
            //     error: function (code) {
            //         alert("등록 실패 하였습니다. (에러코드 : " + code + ")");
            //     }
            // });
        });

        // 닫기 이벤트
        $('#close').on('click', function () {
            $('#productTable').show();
            $('#productAddTag').hide();
        });

        //
        $('#filterColumn').find('select[name="filterColumn"]').on('change', function (){
            $.searchSelect($(this).val());
        });

        //
        $('#productAddTag').hide();


    }

    $.initialize();

});