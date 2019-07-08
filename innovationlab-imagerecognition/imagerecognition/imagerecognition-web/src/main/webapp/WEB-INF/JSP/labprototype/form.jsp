		<input class="hidden" type="file" id="iconfile" name="iconfile" />
		<input class="hidden" type="file" id="mediafile" name="mediafile" />
		
		<div class="col-sm-2 text-right">
			<label>ProtoType Name: </label>
		</div>
        <div class="col-sm-4">
          <div class="form-group">
            <form:input path="name" originalname="Name"/>
          </div>
        </div>
		<div class="col-sm-2 text-right">
			<label>ProtoType Title: </label>
		</div>
        <div class="col-sm-4">
          <div class="form-group">
		   <form:input path="title" originalname="Title" />
          </div>
        </div>
        
        
        <div class="col-sm-2 text-right">
			<label>ProtoType Description:  </label>
		</div>
		<div class="col-sm-10">
          <div class="form-group">
            <form:textarea  class="iltextares" path="description" originalname="Description" />
          </div>
        </div>
        
		<div class="col-sm-2 text-right">
			<label>Icon File: </label>
		</div>
		<div class="col-sm-4">
          <div class="form-group">
            <input type="button" value="Upload" class="csbtn btn-primary" onClick="initiateUploadEvent('iconfile')">
            <label id="iconfileLabel">${labPrototype.iconfilename}</label>
          </div>
         </div>
        <div class="col-sm-2 text-right">
			<label>Media File: </label>
		</div>
		<div class="col-sm-4">
          <div class="form-group">
         	<input type="button" value="Upload" class="csbtn btn-primary" onClick="initiateUploadEvent('mediafile')">
         	<label id="mediafileLabel">${labPrototype.mediafilename}</label>
          </div>
        </div>
		