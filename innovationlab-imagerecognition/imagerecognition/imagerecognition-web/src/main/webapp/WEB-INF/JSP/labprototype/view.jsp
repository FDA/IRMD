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
<title>View Prototypes</title>

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
<script src="${bminjs}"></script>

</head>

<body>
<%@ include file="../common/header.jsp" %>
<!----Header Part End/----> 
<!----Container Part Starts/---->
<div class="main-container"> 
  <!----Filter Section Start/---->
  <div class="container create-profile-wrapper"> 
  	<c:if test="${message !=null}">
	  	<div class="bs-example">
		    <div class="alert alert-success text-center fade in">
		        <strong>${message}!</strong>
		    </div>
	  	</div>
 	</c:if>
 	<c:if test="${errormessage !=null}">
	  	<div class="bs-example">
		    <div class="alert alert-danger text-center fade in">
		        <strong>${errormessage}</strong>
		    </div>
	  	</div>
 	 </c:if>
    <!----Search Filter Start/---->
    <div class="white-wrapper">
      <h1>View LAB Prototype</h1>
      <div class="row">
		<div class="col-sm-12 ">
     	<div class="fixed-table-container">
     		<table class="sorting-table table table-striped table-bordered" cellSpacing="0" id="mainTable" cellSpacing="0">
            <thead>
             <tr>
			   <th data-field="username" data-halign="center" data-align="left" data-sortable="true">Name</th>
			   <th data-field="firstname" data-halign="center" data-align="left" data-sortable="true">Title</th>
			   <th data-field="lastname" data-halign="center" data-align="left" data-sortable="true">Description</th>
			   <th data-field="status" data-halign="center" data-align="left" data-sortable="true">Edit</th>
			   <th data-field="lastlogin" data-halign="center" data-align="left" data-sortable="true">Delete</th>
             </tr>
            </thead>
             <tbody>
                <c:forEach items="${labPrototypes}" var="labPrototype">
                	<tr>
	                   <td>${labPrototype.name}</td>
	                   <td>${labPrototype.title}</td>
	                   <td>${labPrototype.description}</td>
					   <td><a href="<c:url value='/admin/labprototype/edit-${labPrototype.id}' />">edit</a></td>
					   <td><a href="<c:url value='/admin/labprototype/delete-${labPrototype.id}' />">delete</a></td>
                	</tr>
                </c:forEach>
             </tbody>
          </table>	
         </div>
     	</div>
      </div>      
    </div>
    <!----Sorting Order Filter Start/---->
  </div>
</div>
<!----Container Part End/----> 
</body>
</html>