
/*
 * Copyright 2011 Damien Bourdette
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

var SERVICE_URL = '/glass/jobs/arguments';

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
	$.getJSON(SERVICE_URL, data, function(args) {
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
		$.getJSON(SERVICE_URL, {"className": this.classname}, function(args) {
			_this.arguments = args;
			var htmlBuilder="";
			$(args).each(function(index, argument) {
                htmlBuilder += "<tr>";
				if(argument.required) {
					htmlBuilder +='<td style="text-align: center;"><span style="font-weight:bold;" >'+argument.name+'*</span></td>';
				}
				else {
					htmlBuilder += '<td style="text-align: center;">'+argument.name+"</td>";
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
                htmlBuilder+="</tr>";
			});

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
