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

var SERVICE_URL = '/glass/jobs/description';

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

swapStacktrace = function(link, target) {
    if (link.text == "see stacktrace") {
        target.show(1000);
        $(link).text("hide stacktrace");
    } else {
        target.hide(1000);
        $(link).text("see stacktrace");
    }
}

