
/* ************ */
/* JobArguments */
/* ************ */
function JobArguments() {
	this.arguments=null;
	this.classname=null;
}

function JobArguments(classname) {
	this.arguments=null;
	this.classname=classname;
}

/**
 * This function gets arguments list for a given class name.
 * See JobController Java code.
 */
JobArguments.prototype.findByClassname = function(classname) {
	console.log("getJobArguments("+classname+")");
	if(classname==="null") {
		this.classname=null;
		return;
	}
	this.classname=classname;
	this.arguments=null;
	
	var data = {};
	data["className"]=classname;

	_this=this;
	$.getJSON('/jobs/arguments', data, function(args) {
		_this.arguments = args;
		_this.log();
	});
}

/**
 *
 */
JobArguments.prototype.buildHtml = function(elementId) {
	if(!elementId) {
		return;
	}

	if(this.arguments==null && this.classname==null) {
		$("#"+elementId).empty();
		return;
	}
	
	if(this.arguments==null && this.classname!=null) {
		_this = this;
		$.getJSON('/jobs/arguments', {"className": this.classname}, function(args) {
			_this.arguments = args;
			var htmlBuilder="<tr>";
			$(args).each(function(index, argument) {
				if(argument.required) {
					htmlBuilder +='<td style="text-align: center;"><span style="font-weight:bold;" >'+argument.name+'*</span></td>';
				}
				else {
					htmlBuilder += "<td>"+argument.name+"</td>";
				}
				htmlBuilder += "<td>"+argument.description+"</td>";
				htmlBuilder += "<td>";
				$(argument.sampleValues).each(function(i,sample) {
					if(i>0) {
						htmlBuilder += "<br>";
					}
					htmlBuilder += sample;
				});
				htmlBuilder += "</td>"
			});
			htmlBuilder+="</tr>";
			$("#"+elementId).html(htmlBuilder);
		});
	
	}
}


/**
*
*/
JobArguments.prototype.log = function() {
	if(this.arguments) {
		$(this.arguments).each(function(index, argument) {
			console.log(index+".name:"+argument.name);
			console.log(index+".description:"+argument.description);
			console.log(index+".required:"+argument.required);
			console.log(index+".sampleValues:"+argument.sampleValues);
		});
	}
}
