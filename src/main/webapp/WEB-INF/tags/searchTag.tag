<%--
  Created by IntelliJ IDEA.
  User: user
  Date: 2022-09-19
  Time: 오후 3:23
  To change this template use File | Settings | File Templates.
--%>
<%@ tag language="java" pageEncoding="UTF-8" body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="id" type="java.lang.String" required="true" %>
<%@ attribute name="title" type="java.lang.String" required="false" %>
<%--<label for="${id}" class="form-label">${title}</label>--%>
<div class="input-group">
  <input id="${id}" class="form-control" name="${id}" placeholder="검색 문자를 입력하세요." >
  <a class="btn btn-outline-primary" id="search">검색</a>
</div>