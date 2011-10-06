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

var SERVICE_URL = '/glass/jsapi/jobs/description';
var LOGS_SERVICE_URL = '/glass/jsapi/logs';

/**
 * Proposes a job name.
 */
function proposeJobName(clazz) {
    if (clazz == '' && clazz.indexOf('.') == -1) {
        $('#nameProposal').text("");

        return;
    }

    var name = clazz.substring(clazz.lastIndexOf('.') + 1, clazz.length);

    $('#nameProposal').text("maybe " + name + " ?");
}

/**
 * Displays description and job arguments for given class
 */
onJobSelected = function() {
    var clazz = $("#clazz").val();

    proposeJobName(clazz);

    if (clazz == null) {
        $("#arguments").empty();

        return;
    }

    $.getJSON(SERVICE_URL, {"className": clazz}, function(job) {
        $("#description").text(job.description);
        $("#disallowConcurrentExecution").text(job.disallowConcurrentExecution);
        $("#persistJobDataAfterExecution").text(job.persistJobDataAfterExecution);

        var htmlBuilder = "";
        $(job.arguments).each(function(index, argument) {
            htmlBuilder += "<tr>";
            if (argument.required) {
                htmlBuilder += '<td style="text-align: center;"><span style="font-weight:bold;" >' + argument.name + '*</span></td>';
            }
            else {
                htmlBuilder += '<td style="text-align: center;">' + argument.name + "</td>";
            }
            htmlBuilder += "<td>" + argument.description + "</td>";
            htmlBuilder += "<td>";
            $(argument.sampleValues).each(function(i, sample) {
                if (i > 0) {
                    htmlBuilder += "<br>";
                }
                htmlBuilder += sample;
            });
            htmlBuilder += "</td>"
            htmlBuilder += "</tr>";
        });

        $("#arguments").html(htmlBuilder);
    });
}

showLogs = function(executionId) {
    $.getJSON(LOGS_SERVICE_URL, {"executionId": executionId}, function(logs) {
        $("#logs-" + executionId + "-link").hide();
        $("#logs-" + executionId).show(500);

        var htmlBuilder = "";

        $(logs).each(function(index, log) {
            htmlBuilder += log.formattedDate;
            htmlBuilder += " ";
            htmlBuilder += "<span class=\"" + log.level + "\">" + log.level + "</span>";
            htmlBuilder += " ";
            htmlBuilder += log.message;

            if (log.stackTrace != null) {
                htmlBuilder += " ";
                htmlBuilder += "<a href=\"#\" onclick=\"viewStackTrace(" + executionId + ")\">view stacktrace</a>";
                htmlBuilder += "<span id=\"logs-" + executionId + "-stacktrace\" style=\"display:none;\">" + log.formattedStackTrace + "</span>";
            }

            htmlBuilder += "<br>";
        });

        $("#logs-" + executionId).html(htmlBuilder);
    });
}

viewStackTrace = function(executionId) {
    var stackTrace = $("#logs-" + executionId + "-stacktrace").html();

    var popup = window.open('','stacktrace-' + executionId, 'width=800,height=800');

    popup.document.documentElement.innerHTML = stackTrace;
}

