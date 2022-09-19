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
<%@ attribute name="title" type="java.lang.String" required="true" %>
<%--<label for="${id}" class="form-label">${title}</label>--%>
<div class="input-group">
  <input id="${id}" class="form-control" type="search" placeholder="Search" aria-label="Search">
  <button class="btn btn-outline-success" type="submit">Search</button>
</div>