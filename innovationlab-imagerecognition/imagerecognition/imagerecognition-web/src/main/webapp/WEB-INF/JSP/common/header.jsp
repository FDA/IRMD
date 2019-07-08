<!----Header Part Starts/---->
<div class="container-fluid">
  <div class="header row"> 
    <!----Menu Section/---->
    <div class="header-menu">
      <div class="header-top">
		  <div class="logo-text pull-left"> <a href="index.html" title="Logo"><h1>Innovation Lab Admin Panel</h1></a></div>
        <div class="login-area pull-right col-sm-4 text-right">
          <ul>
            <c:if test="${pageContext.request.userPrincipal.name != null}">       
	        	<span class="welcome-text">Welcome :</span>
		         <li>
					<span class="username">
					<sec:authentication property="name" /></span></li> |
		         <li>
				<a href="<c:url value='/j_spring_security_logout'/>">
				<i class="glyphicon glyphicon-cog" aria-hidden="true"></i>Logout</a>
	        	</li>
 			</c:if>
          </ul>
        </div>
      </div>
      <div class="clearfix"></div>
      <!----Main Menu Section Start/---->
      <div class="header-main-menu">
       <nav class="navbar navbar-inverse">
          <div class="container-fluid"> 
            <!----Toggle Button Start/---->
            <div class="navbar-header"> <span class="menu-mobile visible-xs">Menu</span>
              <button aria-controls="navbar" aria-expanded="false" data-target="#navbar" data-toggle="collapse" class="navbar-toggle collapsed" type="button"> <span class="sr-only">Toggle navigation</span> <span class="icon-bar"></span> <span class="icon-bar"></span> <span class="icon-bar"></span> </button>
            </div>
            <!----Toggle Button End/----> 
            <!--/.nav-collapse Start-->
            <div class="navbar-collapse collapse" id="navbar">
              <ul class="nav navbar-nav">
                <li class="about-tab"><a aria-expanded="false" aria-haspopup="true" role="button" data-toggle="dropdown" class="dropdown-toggle" href="javascript:void(0)">Lab Event <span class="caret-arrow"></span></a>
                  <ul class="dropdown-menu">
                    <li><a href="<c:url value='/admin/labevent/addnew'/>">Create Event</a></li>
                    <li><a href="<c:url value='/admin/labevent/show'/>">View Events</a></li>
                  </ul>
                </li>
                <li class="dropdown contact-tab"> <a aria-expanded="false" aria-haspopup="true" role="button" data-toggle="dropdown" class="dropdown-toggle" href="javascript:void(0)">Lab ProtoType<span class="caret-arrow"></span></a>
                  <ul class="dropdown-menu">
                    <li><a href="<c:url value='/admin/labprototype/addnew'/>">Create ProtoType</a></li>
                    <li><a href="<c:url value='/admin/labprototype/show'/>">View ProtoType</a></li>
                  </ul>
                </li>
              </ul>
            </div>
            <!--/.nav-collapse End--> 
          </div>
        </nav>
      </div>
      <!----Main Menu Section End/----> 
    </div>
  </div>
</div>
<!----Header Part Ends/---->