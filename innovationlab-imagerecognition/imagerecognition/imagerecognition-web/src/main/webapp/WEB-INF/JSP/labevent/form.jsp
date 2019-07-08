		<div class="col-sm-2 text-right">
			<label>Event Title: </label>
		</div>
        <div class="col-sm-4">
          <div class="form-group">
            <form:input path="title" originalname="Title"/>
          </div>
        </div>
		<div class="col-sm-2 text-right">
			<label>Location: </label>
		</div>
        <div class="col-sm-4">
          <div class="form-group">
		   <form:input path="location" originalname="Location" />
          </div>
        </div>
        
        <div class="col-sm-2 text-right">
			<label>Start Time:  </label>
		</div>
		<div class="col-sm-4">
          <div class="form-group">
          	<div class='input-group date' id='startTime'>
	          	<c:choose>
				    <c:when test="${labEvent.startTime != null}">
				        <input type="text" name="startTime" class="form-control datepicker" value="<fmt:formatDate pattern="MM/dd/YYYY hh:mm a" value="${labEvent.startTime}"/>" originalname="Start Time">
				    </c:when>    
				    <c:otherwise>
				       <input type="text" name="startTime" class="form-control datepicker" value="" originalname="Start Time"/>
				    </c:otherwise>
				</c:choose>
				<span class="input-group-addon">
                        <span class="glyphicon glyphicon-calendar"></span>
                </span>
			</div>
          </div>
        </div>
		<div class="col-sm-2 text-right">
			<label>End Time: </label>
		</div>
		<div class="col-sm-4">
          <div class="form-group">
          	<div class='input-group date' id='endTime'>
	          	<c:choose>
					    <c:when test="${labEvent.endTime != null}">
					        <input type="text" name="endTime" class="form-control datepicker" value="<fmt:formatDate pattern="MM/dd/YYYY hh:mm a" type="date" value="${labEvent.endTime}"/>" originalname="End Time">
					    </c:when>    
					    <c:otherwise>
					       <input type="text" name="endTime" class="form-control datepicker" value="" originalname="End Time"/>
					    </c:otherwise>
				</c:choose>
				<span class="input-group-addon">
                        <span class="glyphicon glyphicon-calendar"></span>
                </span>
			</div>
          </div>
         </div>
         
        <div class="col-sm-2 text-right">
			<label>Event Description: </label>
		</div>
		<div class="col-sm-10">
          <div class="form-group">
            <form:textarea  class="iltextares" path="description" originalname="Event Description" />
          </div>
        </div>
        
		<div class="col-sm-2 text-right">
			<label>Registration Link: </label>
		</div>
		<div class="col-sm-4">
          <div class="form-group">
            <input type="url" name="registrationLink" originalname="Registration Link" />
          </div>
        </div>
		<div class="col-sm-2 text-right">
			<label>Item Type: </label>
		</div>
		<div class="col-sm-4">
          <div class="form-group">
            <form:input path="itemType" originalname="Item Type:" />
          </div>	
        </div>
        
		<div class="col-sm-2 text-right">
			<label>Path: </label>
		</div>
		<div class="col-sm-4">
          <div class="form-group">
            <form:input path="path" originalname="Path"/>
          </div>
        </div>
        <div class="col-sm-2 text-right">
			<label>Work Space: </label>
		</div>
        <div class="col-sm-4">
          <div class="form-group">
            <form:input path="workspace" originalname="Work Space" />
          </div>
        </div>
        
		<div class="col-sm-2 text-right">
			<label>All Day Event:  </label>
		</div>
		<div class="col-sm-4">
          <div class="form-group">
            <form:radiobutton path="allDayEvent" value="true"/><span class="radio-txt">Yes</span>
			<form:radiobutton path="allDayEvent" value="false"/><span class="radio-txt">No</span> 
          </div>
        </div>
		<div class="col-sm-2 text-right">
			<label>Registration Open: </label>
		</div>
		<div class="col-sm-4">
          <div class="form-group">
            <form:radiobutton path="registrationOpen" value="true"/><span class="radio-txt">Yes</span>
			<form:radiobutton path="registrationOpen" value="false"/><span class="radio-txt">No</span> 
          </div>
        </div>
        
    	 <div class="col-sm-2 text-right">
			<label>Recurrence: </label>
		</div>
		<div class="col-sm-4">
          <div class="form-group">
            <form:radiobutton path="recurrence" value="true"/><span class="radio-txt">Yes</span>
			<form:radiobutton path="recurrence" value="false"/><span class="radio-txt">No</span> 
          </div>
        </div>