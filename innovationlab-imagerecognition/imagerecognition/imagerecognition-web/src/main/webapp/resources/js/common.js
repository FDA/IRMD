function initiateUploadEvent(eventName){	
	$("#"+eventName).click();
	$("#"+eventName).change(function(e) {
        var fileName = e.target.files[0].name;
        $("#"+eventName+"Label").html(fileName);
    });
}

function validate(formName) {	  
	  var vflag = true;
	  $("#errorContentDiv").html("");
	  $('#'+formName+' input[type="text"]').each(function() {
	      if( $(this).val().length === 0 ) {
	    	  vflag = false;
	          $("#errorContentDiv").removeClass("hide");
	          $("#errorContentDiv").append("<label class='error-label'> Field "+$(this).attr("originalname")+" cannot be blank.</label></br>")
	      }
	  });  	  
	  return vflag;
}
