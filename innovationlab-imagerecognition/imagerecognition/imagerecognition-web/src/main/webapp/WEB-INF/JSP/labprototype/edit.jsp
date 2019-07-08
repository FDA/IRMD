<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<html lang="en">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<!----Favicon Starts/---->
<!----Favicon End/---->
<meta name="msapplication-TileColor" content="#ffffff">
<meta name="msapplication-TileImage" content="/ms-icon-144x144.png">
<meta name="theme-color" content="#ffffff">
<title>Edit Lab ProtoType</title>

<spring:url value="/resources/css/bootstrap.min.css" var="bminCSS" />
<spring:url value="/resources/css/base.css" var="baseCSS" />
<spring:url value="/resources/css/style.css" var="styleCSS" />

<link href="${bminCSS}" rel="stylesheet" />
<link href="${baseCSS}" rel="stylesheet" />
<link href="${styleCSS}" rel="stylesheet" />
	
<link rel="stylesheet" href="//code.jquery.com/ui/1.11.4/themes/smoothness/jquery-ui.css">
<script src="//code.jquery.com/jquery-1.10.2.js"></script>
<script src="//code.jquery.com/ui/1.11.4/jquery-ui.js"></script>
	
<spring:url value="/resources/js/bootstrap.min.js" var="bminjs" />
<spring:url value="/resources/js/common.js" var="common" />

<script src="${bminjs}"></script>
<script src="${common}"></script> 
</head>

<body>
<%@ include file="../common/header.jsp" %>
<!----Header Part End/----> 
<!----Container Part Starts/---->
<div class="main-container"> 
  
  <!----Filter Section Start/---->
  <div class="container create-profile-wrapper">
  <div id="errorContentDiv" class="alert alert-danger text-center fade in hide"></div></br> 
  <c:if test="${errormessage !=null}">
	  	<div class="bs-example">
		    <div class="alert alert-danger text-center fade in">
		        <strong>Opps! Something wrong happened. ${errormessage}</strong>
		    </div>
	  	</div>
 	 </c:if>
    <!----Search Filter Start/---->
    <div class="white-wrapper">
      <h1>Edit Lab ProtoType</h1>
	  <form:form method="POST" id="protoForm" action="update" modelAttribute="labPrototype" enctype="multipart/form-data" onsubmit="return validate('protoForm');">
	  <form:input type="hidden" path="id"/>
      <div class="row">
		<%@ include file="form.jsp" %>        
        <div class="col-sm-12 text-center">
      	<input type="submit" value="Update"/>
      </div>
      </div>
    </div>
	</form:form>
    <!----Sorting Order Filter Start/---->
  </div>
</div>
<!----Container Part End/----> 
</body>
</html>