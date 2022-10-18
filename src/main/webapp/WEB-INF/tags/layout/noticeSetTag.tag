<%--
  Created by IntelliJ IDEA.
  User: young
  Date: 2022-03-02
  Time: 오후 7:56
  To change this template use File | Settings | File Templates.
--%>
<%@ tag language="java" pageEncoding="UTF-8" %>
<%@ taglib uri="http://stripes.sourceforge.net/stripes.tld" prefix="stripes" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="layoutTags" tagdir="/WEB-INF/tags/layout" %>
<% String contextPath = request.getContextPath(); %>

<!-- content -->
<main id="noticeSet">
	<div class="container-fluid px-4">

		<h1 class="mt-4">공지사항</h1>
		<ol class="breadcrumb mb-4">
			<li class="breadcrumb-item active">${subTitle} > 공지사항</li>
		</ol>

		<div class="card mb-2 shadow-sm rounded">
			<div class="card-header">
				<div class="row">
					<div class="col-9">
						<i class="fas fa-table"></i> 관공서 상세 정보
					</div>
					<div class="col-3 d-flex justify-content-end">
						<a class="btn btn-close" id="noticeSetClose"></a>
					</div>
				</div>
			</div>

			<div class="card-body">
				<form id="noticeForm">

				</form>
			</div>

		</div>

	</div>
</main>

<script src="https://canvasjs.com/assets/script/jquery.canvasjs.min.js"></script>
<script type="application/javascript">
    $(function () {

        $('#noticeSetClose').on('click', function (){
            // 그룹 추가 태그 숨기기
            $('#noticeSet').hide();
        })

    });
</script>

