/*******************************************************************************
 * MIT License
 * 
 * Copyright (c) 2019 Infostretch Corporation
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:

 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 * 
 * For any inquiry or need additional information, please contact
 * support-qaf@infostretch.com
 ******************************************************************************/
// Error Buckets with patterns
var errorBuckets = {
	"ServerSideFailure" : /JSONObject\[\".*\"\] not found|Unexpected end of file from server|A JSONArray text must start with/i,
	"ConfigurationFailure" : /:: Not Found|java\.util\.NoSuchElementException|org\.testng\.internal\.MethodInvocationHelper\.invokeDataProvider|StepInvocationException/i,
	"DriverFailure" : /unknown error: result was not received in (\d)+ seconds|An unknown server-side error|Driver may have died/i,
	"ElementFailure" : /Element is not clickable|element not visible|invalid element state|Element is not currently intractable|Cannot read property .* of null/i,
	"UIFailure" : /Timed out after (\d)+ seconds|No frame element found|no such element|Unable to locate element|selenium\.NoSuchElementException/i,
	"CheckPointFailure" : /^\\s*$|AssertionError/i
// "AutomationFailure" : /.*/

};

var listTemplate = '<li class="active" dir="${dir}" title="${name}" id="${li_id}">'
		+ '<a href="#${anchor_id}" title="${name}" id="job_${anchor_id}">'
		+ '<span class="jobid ${jobid}">${jobid}</span>'
		+ '<div class="details">'
		+ '<p>${name}</p>'
		+ '<span>${msToFormatedDateStr(startTime, "dd-MM-yy HH:MM")}</span>'
		+ '</div>' + '</a>' + '</li>';

var overviewTemplate = '<div class="title_bg">${name}</div>'
		+ '<div class="cont_data">'
		+ '<div id="overview-sec">'
		+ '<div class="top_area">'
		+ '<div class="left_area" id="pichart" style="width: 430px; height: 217px;"></div>'
		+ '<div class="right_area">'
		+ '<div class="colum colum_one_mrgin">'
		+ '<ul>'
		+ '<li class="title text-center">&nbsp;</li>'
		+ '<li class="title text-center"><a dir="all" class="link" href="javascript:void(0)" onclick="loadListAll();">View all results</a></li>'
		+ '<li class="result text-center">Passed</li>'
		+ '<li class=""><div class="green_bg" style="margin:0 auto;">${pass}</div></li>'
		+ '</ul>'
		+ '</div>'
		+ '<div class="colum colum_two_mrgin">'
		+ '<ul>'
		+ '<li class="title text-center">Duration</li>'
		+ '<li class="number text-center" title="[${msToFormatedDateStr(startTime, \"dd-MM-yy HH:MM\")} - ${msToFormatedDateStr(endTime, \"dd-MM-yy HH:MM\")}]">${getDuration(endTime-startTime)}</li>'
		+ '<li class="result text-center">Skipped</li>'
		+ '<li class=""><div class="yellow_bg" style="margin:0 auto;">${skip}</div></li>'
		+ '</ul>'
		+ '</div>'
		+ '<div class="colum colum_three_mrgin">'
		+ '<ul>'
		+ '<li class="title text-center">Total</li>'
		+ '<li class="number text-center">${total}</li>'
		+ '<li class="result text-center">Failed</li>'
		+ '<li class=""><div class="red_bg" style="margin:0 auto;">${fail}</div></li>'
		+ '</ul>' + '</div>' + '</div>' + '</div>'
		+ '<div class="clear"></div>' + '<div class="bottom_area">'
		+ '<table border="0" cellspacing="0" cellpadding="0" id="test_report">'
		+ '<thead>' + '<tr>' + '<th scope="col" width=20%>Test</th>'
		+ '<th scope="col" width=10%>Duration</th>'
		+ '<th scope="col" width=10%>Passed</th>'
		+ '<th scope="col" width=10%>Skipped</th>'
		+ '<th scope="col" width=10%>Failed</th>'
		+ '<th scope="col" width=10%>Total</th>'
		+ '<th scope="col" width=10%>Pass Rate</th>' + '</tr>' + '</thead>'
		+ '<tbody id="tests">' + '</tbody>' + '</table>' + '</div>'
		+ '<!-- end overview-->' + '</div>' + '</div>';

var testOverviewTemplate = '<tr>'
		+ '<td class="test_col"><a href="${window.location.href}#${name}" id="result_${name}" onclick="loaResult(\'${name}\');" class="reportlink">${name}</a></td>'
		+ '<td title="[${msToFormatedDateStr(startTime, \'dd-MM-yy HH:MM\')} - ${msToFormatedDateStr(endTime, \'dd-MM-yy HH:MM\')}]">${getDuration(endTime-startTime)}</td>'
		+ '<td>${pass}</td>'
		+ '<td>${skip}</td>'
		+ '<td>${fail}</td>'
		+ '<td>${total}</td>'
		+ '<td>'
		+ '<div class="passrate_col"><span style="width:${calcPassRate(pass,fail,skip)}%;">${calcPassRate(pass,fail,skip)}%</span></div>'
		+ '</td>' + '</tr>';

var methodHeaderTemplate = '<div class="mehod ${result} ${type}" id="${result}_cont">'
		+ '<div class="mehodheader" onclick="mehodheaderClick(this);">'
		+ '<span class="statusicon ${result}"> &nbsp;<span class="status" style="display:none">${result}</span></span>'
		+ '<b class="ui-icon-text">{{if (typeof metaData != \'undefined\') }} {{each(i,v) metaData}} {{if (i == \'name\')}}${v} {{/if}}{{/each}} {{else}} ${name} {{/if}}</b> {{if ((typeof args != \'undefined\') && args.length>0 && isMap(args[0]))}}'
		+ ' <span class="mehod-args"> ${args[0][\'tcId\']} ${args[0][\'recId\']}</span> {{/if}}'
		+ '<div style="float: right; ">'
		+ '{{if typeof retryCount != \'undefined\' && retryCount>0}}'
		+ '<span class=\'rerunCount\' title="Retried Failed Execution">${retryCount}</span>{{/if}}'
		+ '<span class="duration">${getDuration(duration)}</span>'
		+ '<span class="ui-icon time_icon oexecution" style="float: right;" title="Duration">${startTime}</span>'
		+ '</div>'
		+ '</div>'
		+ '<div class="details data_cont" data-file="${datafile}" style="display:none; position: relative;"></div>'
		+ '<div class="meta-info tab-content" style="display:none;padding:0px;width:100%;">'
		+ '<table cellspacing="1" cellpadding="0" border="0" style="display:block;">'
		+ '<tbody>'
		+ '{{if ((typeof args != \'undefined\') && args.length>0)}}'
		+ '<tr>'
		+ '<th>Test Data:</th>'
		+ '<td colspan="3">${parseArray(args)}</td>'
		+ '</tr>'
		+ '{{/if}} {{if (typeof metaData != \'undefined\') }} {{each(i,v) getMetaDataToDisplay(metaData)}} {{if (v!= \'undefined\' && v.length>0)}}'
		+ '<tr>'
		+ '<th>${i.capitalizeFirstLetter()}:</th>'
		+ '<td>'
		+ '{{html displayMetaData(v)}}'
		+ '</td>'
		+ '</tr>'
		+ '{{/if}} {{/each}} {{/if}} {{if dependsOn.length>0}}'
		+ '<tr>'
		+ '<th>Depends On:</th>'
		+ '<td colspan="3">{{each dependsOn}}'
		+ '<span class="group">${$value}</span> {{/each}}'
		+ '</td>'
		+ '</tr>'
		+ '{{/if}}'
		+ '<tr>'
		+ '<th>Start Time:</th>'
		+ '<td>${msToDateStr( startTime)}</td>'
		+ '</tr>'
		+ '<tr>'
		+ '<th>End&nbsp;Time:</th>'
		+ '<td>${msToDateStr(startTime+duration)}</td>'
		+ '</tr>'
		+ '<tr id="totalTime" style="display:none;">'
		+ '<th>Actual&nbsp;Time:</th>'
		+ '<td></td>'
		+ '</tr>'
		+ '<tr>'
		+ '<th valign="top">Check Points:</th>'
		+ '<td class="check-points-details" style="word-wrap: break-word;">'
		+ '</td>' + '</tr>' + '</tbody>' + '</table>' + '</div>' + '</div>';

var methodDetailsTemplate = '<span class="toolbar">'
		+ '{{if errorTrace}}'
		+ '<span class="ui-icon ui-icon-alert error-trace action" style="float: right; margin-right: .3em;" title="Error trace"></span> {{/if}} {{if seleniumLog.length>0}}'
		+ '<span class="ui-icon ui-icon-document selenium-log action" style="float: right; margin-right: .3em;" title="Command log"></span> {{/if}} {{if (checkPoints.length>0 && checkPoints[0].duration)}}'
		+ '<span class="ui-icon ui-icon-chart step-analysis action" style="float: right; margin-right: .3em;" title="Step Time Analysis"></span> {{/if}}'
		+ '<span class="ui-icon ui-icon-clipboard meta-info-check-points  action" style="float: right; margin-right: .3em;" title="Check Points"></span>'
		+ '</span>'
		+ '<div class="detailsContainer">'
		+ '<div class="check-points tab-content" style="display:none;word-wrap: break-word;">'
		+ '{{tmpl(checkPoints) "checkpointTemplate"}}'
		+ '</div>'
		+ '<div class="selenium-log tab-content " style="display:none;padding-top:5px;">'
		+ '<table width="100%">'
		+ '<tbody>'
		+ '{{tmpl(seleniumLog) "seleniumLogTemplate"}}</tbody>'
		+ '</table>'
		+ '</div>'
		+ '{{if errorTrace}}'
		+ '<div class="error-trace tab-content" style="display:none;margin-top:15px;">'
		+ '<pre class="prettyprint linenums">{{html errorTrace}}</pre>'
		+ '</div>'
		+ '{{/if}} {{if (checkPoints.length>0 && checkPoints[0].duration)}}'
		+ '<div class="step-analysis tab-content" style="position:relative;margin-left:20px;margin-top:20px;margin-bottom:40px;margin-right:20px;height:300px;display:none;" data="${getStepTimes(checkPoints)}" </div>'
		+ '{{/if}}' + '</div>';

var checkpointTemplate = '<pre class="prettyprint" style="border: none !important;">'
		+ '<div class="checkpoint ${getContainerClass(type)}" style="border:none;">'
		+ '<div {{if subCheckPoints}}onclick="$(this).closest(\'.checkpoint\').children(\'.subcheckpoints\').toggle();$(this).children(\'span\').toggleClass(\'ui-icon-triangle-1-e ui-icon-triangle-1-s\');" {{/if}}>'
		+ '<span class="ui-icon {{if subCheckPoints.length > 0}} ui-icon-triangle-1-e {{else}} ${getIcon(type)} {{/if}}" style="float:left;margin-top:0.0em;margin-left:5px;" title="${type}"></span>'
		+ '<span style="vertical-align:top;margin-left:25px;display:block;word-wrap: break-word;">{{html escapHtml(message)}}'
		+ '{{if screenshot}}<a class="screenshot" href="${screenshot}" style="width:auto;margin-top:0.0em;vertical-align:middle;" title="Screenshot"></a>'
		+ '{{/if}}'
		+ '{{if duration}}'
		+ '[{{if threshold}}'
		+ '{{if (threshold>0) && (threshold*1000<duration)}}<span class="step-threshold" style="color:#FF9900" title="threshold: ${threshold}s&#13;exceeded: ${duration/1000.0 - threshold}s">${duration/1000.0}s</span>{{else}}'
		+ '<span class="step-threshold" title="threshold: ${threshold}s&#13;outstanding: ${threshold - duration/1000.0}s">${duration/1000.0}s</span> {{/if}} {{else}}${duration/1000.0}s {{/if}}] {{/if}}'
		+ '</span>'
		+ '</div>'
		+ '{{if subCheckPoints}}'
		+ '<div style="display:none;" class="subcheckpoints">'
		+ '{{tmpl(subCheckPoints) "checkpointTemplate"}}'
		+ '</div>'
		+ '{{/if}}' + '</div>' + '</pre>';

var errorAnalysisTemplate = '<div id="error_analysis_chart" style="width:25%; max-width:350px; height: 300px;" class="fleft"></div>'
		+ '<div style="min-width: 450px;width: 71%; height: 300px;" class="fleft">'
		+ '{{each(i, item) $data}}'
		+ '<div class="collapse collapsible" style="background-color: #D2D2C2;margin-top:5px; width:100%; float:left;">'
		+ '<div class="accordien_arrow" style="background-color: transparent;float: left;border:5px solid transparent;"></div>'
		+ '<div class="key" style="padding:6px;">${i}'
		+ '<div class="value_count">${item.length}</div>'
		+ '</div>'
		+ '</div>'
		+ '<div>'
		+ '<ul style="list-style-type: square; padding-left:15px;" class="ui-state-active ui-widget-content">'
		+ '{{each item}} {{html dspTCLink($value)}} {{/each}}'
		+ '</ul>'
		+ '</div>' + '{{/each}}' + '</div>' + '<div style="clear:left;"></div>';

var seleniumLogTemplate =

'{{if subLogs.length>0}}'
		+ '<tr valign="top">'
		+ '<td colspan="4">'
		+ '<div class="selenium-log" style="padding-top:5px;font-size:13px">'
		+ '<table width="100%">'
		+ '<thead onclick="$(this).closest(\'table\').children(\'tbody\').toggle();" style="border:1px solid #000;">'
		+ '<tr valign="top">'
		+ '<th align="left" width="20%"><pre class="prettyprint">{{html trunck(commandName)}}</pre></th>'
		+ '<th align="left" width="37%"><pre class="prettyprint">{{html trunck(args)}}</pre></th>'
		+ '<th align="left" width="37%"><pre class="prettyprint">{{html trunck(result)}}</pre></th>'
		+ '<td width="4%"><pre class="prettyprint">${getTotalDuration(subLogs)}</pre></td>'
		+ '</tr>'
		+ '</thead>'
		+ '<tbody style="display:none;">'
		+ '{{tmpl(subLogs) "seleniumLogTemplate"}}</tbody>'
		+ '</table>'
		+ '</div>'
		+ '</td>'
		+ '{{else}}'
		+ '{{if commandName}}'
		+ '<tr valign="top">'
		+ '<td width="19%"><pre class="prettyprint">${commandName}</pre></td>'
		+ '<td width="37%"><pre class="prettyprint">${args}</pre></td>'
		+ '<td width="37%"><pre class="prettyprint">${formatedRes(result)}</pre></td>'
		+ '{{else}}'
		+ '<tr onclick="showDialog(this)" valign="top">'
		+ '<td width="19%">${commandName}</td>'
		+ '<td width="37%"><pre class="prettyprint lang-html">${args}</pre></td>'
		+ '<td width="37%"><pre class="prettyprint lang-html">${formatedRes(result)}</pre></td>'
		+ '{{/if}}'

		+ '{{if duration}}'
		+ '<td width="4%"><pre class="prettyprint">${duration>0?(duration/1000):duration}</pre></td>'
		+ '{{/if}} {{/if}}' + '</tr>';

var envInfoTemplate = '<ol>'
		+ '{{each(i, item) $data}} {{if typeof item== \'object\'}}'
		+ '<li><span class="key">${i}</span>:${JSON.stringify(item)}</li>'
		+ '{{else}} {{if item.toString().length > 0}}'
		+ '<li><span class="key">${i}</span>:{{html item}}</li>'
		+ '{{/if}} {{/if}} {{/each}}' + '</ol>';

$.template("listTemplate", listTemplate);
$.template("overviewTemplate", overviewTemplate);
$.template("testOverviewTemplate", testOverviewTemplate);
$.template("methodHeaderTemplate", methodHeaderTemplate);
$.template("methodDetailsTemplate", methodDetailsTemplate);
$.template("checkpointTemplate", checkpointTemplate);
$.template("errorAnalysisTemplate", errorAnalysisTemplate);
$.template("seleniumLogTemplate", seleniumLogTemplate);
$.template("envInfoTemplate", envInfoTemplate);

function getUrlVar(key) {

	var result = new RegExp(key + "=([^&]*)", "i").exec(window.location.search);

	return (result && unescape(result[1] + "/test-results")) || "test-results";

}
var resultRootDir = getUrlVar("job");// "test-results";
var curResultDir = "";
var layout;
var treports;
var refreshInterval = 10000;
var interuptLoading = false;
var inprogress = false;
var chainedcls, chainedtests;
var length = 0;
var split;
var url;

// add key only in upper case
var metaKeysToExclude = [ 'NAME', 'SIGN', 'LINENO', 'RESULTFILENAME' ];

var timer = $.timer(function() {
	$('#reportlist').html('');
	loadList(true);

}, refreshInterval, false);

var pageLayout;

$(document).ready(function() {

	$("#reportlist_scroll").mCustomScrollbar({
		scrollButtons : {
			enable : true
		},
		theme : "dark-thin",
		autoDraggerLength : false,
		advanced : {
			updateOnContentResize : true,
			updateOnBrowserResize : false
		}
	});
	// create page layout
	pageLayout = $('body').layout({
		// west__onresize : initPaneScrollbar,
		defaults : {
			padding : 0,
			margin : 0
		},
		north : {
			size : 80,
			spacing_open : 0,
			closable : false,
			resizable : false
		},
		west : {
			size : 250,
			spacing_closed : 22,
			togglerLength_closed : 140,
			togglerAlign_closed : "center",
			togglerContent_closed : "R<BR>e<BR>p<BR>o<BR>r<BR>t<BR>s",
			togglerTip_closed : "Open & Pin Reports",
			sliderTip : "Slide Open Reports",
			slideTrigger_open : "mouseover"
		}
	});
	loadList();

	$(".environment_info .title_bg").click(function(event) {
		$(this).toggleClass('expanded');
		$(this).toggleClass('collapse');
	});

	$('#refreshBtn').click(function() {
		timer.toggle();
		$("#refreshBtn").toggleClass('nav-toggle');
		$("#refreshBtn").toggleClass('nav-toggle-active');
	});
	$("#stop").button({
		text : false,
		icons : {
			primary : "ui-icon-stop"
		}
	});
	$('#stop').click(function() {
		$.ajaxQueue.stop();
		$('#loading-info').hide(1000);
		setChecked('#oexpand', false);
		setChecked('#ocollapse', false);
	});
	$('#loading-info').hide();

	$('#error_analysis_header').click(function() {
		displayErrorBucket();
	});

});

function getResultRootDir() {
	return resultRootDir;
}

function progress(percent, $element) {
	var progressBarWidth = percent * $element.width() / 100;
	$element.animate({
		width : progressBarWidth
	}, 500).html(percent + "%&nbsp;");
}

function loadList() {
	loadList(false);
}

/**
 * left panel : list of all reports
 */
function loadList(loadmethods) {
	return $.getJSON(getResultRootDir() + "/meta-info.json", function(data) {
		treports = data;
		var size = treports.reports.length;
		$.each(treports.reports, function(i, item) {
			item['li_id'] = getIdFromDir(item.dir);
			item['jobid'] = size--;
			if ($.isNumeric(item['li_id'])) {
				item['jobid'] = item['jobid'] + " #" + item['li_id'];
				item['anchor_id'] = item['li_id'];
			} else {
				item['anchor_id'] = item.startTime;
			}
			$.tmpl(listTemplate, item).appendTo("#reportlist");
		});
		$('#reportlist li').click(function() {
			$('#details').html('');
			if (($(this).hasClass('selected'))) {
				return 0;
			}
			window.location.href = $(this).find("a").attr('href');
			var curSelceteReport = $('#reportlist li.selected');
			if (curSelceteReport) {
				$(curSelceteReport).removeClass("selected");
				$(curSelceteReport).addClass("active");
			}
			$(this).addClass("selected");
			$(this).removeClass("active");
			curResultDir = removePrefixOfResultRootDir($(this).attr("dir"));
			loadOverview(removePrefixOfResultRootDir($(this).attr("dir")));
			showOverview($("#overview-tab"));
		});

		if (loadmethods) {
			$('#details').html('');

			var curSelceteReport = $('#reportlist li.selected');
			if (curSelceteReport) {
				$(curSelceteReport).removeClass("selected");

				$(curSelceteReport).addClass("active");
			}
			$("#reportlist li:first").addClass("selected");
			$("#reportlist li:first").removeClass("active");
			curResultDir = $("#reportlist li:first").attr("dir");
			loadAllMethods($("#reportlist li:first").attr("dir"));
		} else {
			selectReport();
		}
	});

}

function selectReport() {

	var url = window.location.href;
	split = url.split("#");
	length = split.length;
	if (length > 1) {
		if ($('#' + split[1]).length) {
			$('#' + split[1]).trigger('click');
		} else if ($('#job_' + split[1]).parent('li').length) {
			$('#job_' + split[1]).parent('li').trigger('click');
		} else {
			$("#reportlist li:first").trigger('click');
		}
	} else {
		$("#reportlist li:first").trigger('click');
	}

}

function getIdFromDir(dir) {
	var index = dir.indexOf("test-results") + "test-results/".lenght;
	var endindex = dir.indexOf("json") - 1;

	return dir.substring(index, endindex);
}

function removePrefixOfResultRootDir(dir) {
	// var index = dir.indexOf(resultRootDir);
	// return dir.substr(index, dir.length);
	var index = dir.indexOf("test-results");
	return dir.substr(index, dir.length).replace("test-results", resultRootDir);
}

function loadListAll() {
	return $.getJSON(getResultRootDir() + "/meta-info.json", function(data) {

		$('#details').html('');
		var curSelceteReport = $('#reportlist li.selected');

		if (curSelceteReport) {
			$(curSelceteReport).removeClass("selected");
			$(curSelceteReport).addClass("active");
		}
		$(curSelceteReport).addClass("selected");
		$(curSelceteReport).removeClass("active");
		loadAllMethods($(curSelceteReport).attr("dir"));
		window.location.href = window.location.href + "#*";

	});

}
function loaResult(dir) {
	tmp = curResultDir + "/" + dir;
	loadMethods(curResultDir, dir);
	currSuite = dir;
	resetFilterAndOrder();
	$("#report_details").show();
	$("#overview-tab-content").hide();
	$("ul.tabs li").removeClass("active");
	$(".data_cont").hide();

	pageLayout.close('west');
}

function loadOverview(dir) {

	$.getJSON(dir + "/meta-info.json", function(data) {
		$("#overview-tab-content").show();

		$("#overview-tab-content").html('');
		$.tmpl(overviewTemplate, data).appendTo("#overview-tab-content");

		$.each(data.tests, function(i, item) {
			$.getJSON(dir + "/" + item + "/overview.json", function(data1) {
				data1["name"] = item;
				$.tmpl(testOverviewTemplate, data1).appendTo("#tests");
			});
		});

		if (length > 2) {
			setTimeout(function() {
				if (split[2] == "*") {
					loadListAll();
				} else {
					$('#result_' + split[2]).click();
				}
			}, 10);
			length = 0;
		}
		drawPIChart(data);
		url = window.location.href;

	});
}

function loadAllMethods(dir) {
	$("#overview-tab-content").hide();
	$("#method-results").html('');
	dir = removePrefixOfResultRootDir(dir);
	chained1 = $.getJSON(dir + "/meta-info.json", function(data) {

		$.each(data.tests, function(i, item) {
			loaResult(item);
		});
	});

	chained1.done(function() {
		chainedtests.done(function() {
			chainedcls.done(function() {
				doSortE(true);
			});
		});
	});

}

function drawPIChart(report) {
	var data = [ [ 'Fail', report.fail ], [ 'Pass', report.pass ],
			[ 'Skip', report.skip ] ];

	jQuery.jqplot("pichart", [ data ], {
		seriesDefaults : {
			// Make this a pie chart.
			renderer : jQuery.jqplot.PieRenderer,
			rendererOptions : {
				seriesColors : [ '#e63c20', '#23a347', '#f3b600' ],
				// RED GREEN YELLOW
				// Put data labels on the pie slices.
				// By default, labels show the percentage of the
				// slice.
				showDataLabels : true
			}
		},
		legend : {
			show : false,
			location : 'e'
		}
	});
}

function loadMethods(cresultRoot, testDir) {

	curResultDir = cresultRoot;
	var dir = cresultRoot + "/" + testDir;
	$("#method-results").show();

	chainedtests = $
			.getJSON(
					dir + "/overview.json",
					function(data) {
						$('.error_analysis')
								.toggle((data.fail + data.skip) > 0);

						$
								.each(
										data.classes,
										function(i, cls) {
											chainedcls = $
													.getJSON(
															dir
																	+ "/"
																	+ cls
																	+ "/meta-info.json",
															function(minfo) {
																$
																		.each(
																				minfo.methods,
																				function(
																						j,
																						mdata) {
																					if (mdata.metaData) {
																						var resultFileName = mdata.metaData.resultFileName;
																						if (typeof resultFileName == 'undefined') {
																							mdata.datafile = dir
																									+ "/"
																									+ cls
																									+ "/"
																									+ mdata.metaData.name
																									+ ".json";

																						} else {
																							mdata.datafile = dir
																									+ "/"
																									+ cls
																									+ "/"
																									+ resultFileName
																									+ ".json";
																						}
																					} else {
																						mdata.datafile = dir
																								+ "/"
																								+ cls
																								+ "/"
																								+ mdata.name
																								+ ".json";
																					}
																					$
																							.tmpl(
																									methodHeaderTemplate,
																									mdata)
																							.appendTo(
																									"#method-results");
																					if (split.length > 3
																							&& mdata.datafile
																									.indexOf(split[3]
																											+ ".json") > 0) {
																						setTimeout(
																								function() {
																									var ele = $("div[data-file*='"
																											+ split[3]
																											+ ".json'] ~ div");

																									var detailsContainer = $(
																											ele)
																											.parent();
																									mehodheaderClick(ele);
																									$(
																											'.ui-layout-center.content.ui-layout-pane')
																											.animate(
																													{
																														scrollTop : $(
																																detailsContainer)
																																.offset().top
																																- ($(
																																		".ui-layout-north")
																																		.height() + 30)
																													},
																													500);
																								},
																								100);
																					}
																				});
															});
										});
						$("#execution_env_info").html('');
						$("#isfw_build_info").html('');
						$("#desired_capabilities").html('');
						$("#actual-capabilities").html('');
						$("#run-parameters").html('');

						$.tmpl(envInfoTemplate,
								data.envInfo['execution-env-info']).appendTo(
								"#execution_env_info");
						$
								.tmpl(envInfoTemplate,
										data.envInfo['isfw-build-info'])
								.appendTo("#isfw_build_info");
						;
						$.tmpl(envInfoTemplate,
								data.envInfo['browser-desired-capabilities'])
								.appendTo("#desired_capabilities");
						$.tmpl(envInfoTemplate,
								data.envInfo['browser-actual-capabilities'])
								.appendTo("#actual-capabilities");
						$.tmpl(envInfoTemplate, data.envInfo['run-parameters'])
								.appendTo("#run-parameters");

					});
	resetFilterAndOrder();
	// window.location.href = url;

}

function getStepTimes(checkpoints) {
	var d = [];
	$.each(checkpoints, function(index, value) {
		var threshold = 0;
		if (value.threshold) {
			threshold = value.threshold;
		}
		d.push([ index, value.duration / 1000.0, threshold ]);
	});
	return JSON.stringify(d);
}

function createStepGraph(ele) {
	var data = JSON.parse($(ele).attr("data"));
	var s2 = [];
	var s3 = [];
	var ticks = [];
	$.each(data, function(index, value) {

		s2.push(value[1]);
		s3.push(value[2]);
		ticks.push(value[0] + 1);
	});
	var barOptions = {
		seriesDefaults : {
			renderer : $.jqplot.BarRenderer,
			pointLabels : {
				show : true
			}
		},
		series : [ {
			color : '#0000FF'
		}, {
			color : '#6dff49'
		} ],
		legend : {
			show : true,
			location : 's',
			renderer : $.jqplot.EnhancedLegendRenderer,
			labels : [ 'Duration', 'Threshold' ],
			placement : 'outside',
			marginTop : "60px",
			rendererOptions : {
				numberRows : '1',
				numberColumns : '3',
				seriesToggle : true
			}
		},
		axes : {
			// Use a category axis on the x axis and use our custom ticks.
			xaxis : {
				renderer : $.jqplot.CategoryAxisRenderer,
				labelRenderer : $.jqplot.CanvasAxisLabelRenderer,
				label : 'Step Index',
				ticks : ticks
			},
			// Pad the y axis just a little so bars can get close to, but
			// not touch, the grid boundaries. 1.2 is the default padding.
			yaxis : {
				pad : 1.05,
				min : 0,
				tickOptions : {
					formatString : '%d s'
				},
				label : 'Duration(s)',
				labelRenderer : $.jqplot.CanvasAxisLabelRenderer,
				labelOptions : {
					angle : -90
				},
			}
		},
		highlighter : {
			show : true,
			tooltipContentEditor : function(str, seriesIndex, pointIndex,
					jqPlot) {
				var stepText = $(ele).parent().find(
						'.tab-content.check-points > .checkpoint').eq(
						pointIndex).find('div > span').eq(1).text();
				stepText = stepText.substr(0, stepText.lastIndexOf('['));
				if (s3[pointIndex] != 0)
					stepText = stepText + '<br/>Threshold:' + s3[pointIndex]
				stepText = stepText + '<br/>Duration:' + s2[pointIndex];
				return stepText;
			}
		}
	};
	plot2 = $.jqplot($(ele).attr("id"), [ s2, s3 ], barOptions);

}

function getMetaDataToDisplay(metadata) {
	$.map(metadata, function(val, key) {
		if ($.inArray(key.toUpperCase(), metaKeysToExclude) >= 0) {
			delete metadata[key];
		}
	});
	return metadata;
}

function displayMetaData(value) {
	if (isString(value))
		return "<span class='group'>" + value + "</span>";
	var blkstr = [];
	$.each(value, function(idx, val) {
		var str = "<span class='group'>" + val + "</span>";
		blkstr.push(str);
	});
	return blkstr.valueOf();

}
function loadDetailsTemplate(data, container) {
	$.tmpl(methodDetailsTemplate, data).appendTo(container);

	$(container).find('pre.prettyprint').each(
			function() {
				var lang = '';
				if($(this).is('[class*=lang-]')){
					var lang = this.className.split('lang-')[1].split(' ')[0];
					console.log("class name" + lang);
				}
				$(this).html(
						PR.prettyPrintOne($(this).html(), lang, $(this).hasClass(
								'linenums')));
				$(this).addClass('prettyprinted');
			});
	applyUi(container);
	displayTotalTime(container, data);
}

function loadDetails(file, container) {
	return $.ajax({
		dataType : "json",
		url : file,
		async : false
	}).success(function(data) {
		loadDetailsTemplate(data, container);
	});
}

function toggleTab(ele, contentCss) {
	if (($(ele).hasClass("ui-state-active"))) {
		return 0;
	}

	container = $(ele).parent().parent();
	$(container).find('.tab-content:not(' + contentCss + ')').each(function() {
		var tab = $(this);
		tab.slideUp();
	});
	$(container).find('.action').each(function() {
		$(this).removeClass('ui-state-active');
		$(this).removeClass('ui-state-highlight');

	});
	$(container).find(".tab-content" + contentCss).show();
	$(container).find(".tab-content" + contentCss).slideDown();
	$(container).find('.action' + contentCss).each(function() {
		$(this).addClass('ui-state-active');
	});
}

function getIcon(type) {
	type = type.toLowerCase();

	if (type == 'pass')
		return 'ui-icon-circle-check';
	if (type == 'fail')
		return 'ui-icon-circle-close';
	if (type == 'skip')
		return 'ui-icon-cancel';
	if (type == 'warn')
		return 'ui-icon-notice';
	if (type == 'teststep')
		return 'ui-icon-pencil';
	if (type == 'teststeppass')
		return 'ui-icon-check';
	if (type == 'teststepfail')
		return 'ui-icon-closethick';

	return 'ui-icon-' + type;
}

function getHeaderIcon(type) {
	type = type.toLowerCase();

	if (type == 'pass')
		return 'ui-icon-check';
	if (type == 'fail')
		return 'ui-icon-closethick';
	if (type == 'skip')
		return 'ui-icon-cancel';

	return 'ui-icon-' + type;
}

function getHeaderState(type) {
	type = type.toLowerCase();

	if (type == 'pass')
		return 'ui-state-pass';
	if (type == 'fail')
		return 'ui-state-error';
	if (type == 'skip')
		return 'ui-state-alert';

	return '';
}

function getContainerClass(type) {
	type = type.toLowerCase();
	if (type == 'pass')
		return 'pass ui-state-pass';
	if (type == 'fail' || type == 'teststepfail')
		return type + ' ui-state-error';
	if (type == 'info' || type == 'teststep')
		return type + ' ui-state-highlight';
	if (type == 'teststeppass')
		return type + ' ui-state-pass';
	if (type == 'warn')
		return type + ' ui-state-warn';
	return ' ui-state-highlight';

}
function trunck(str) {
	if (str.toString().length > 100) {
		return '<span title="' + str + '">' + str.toString().substring(0, 30)
				+ '...</span>';
	}
	return str;
}

function escapHtml(str){
	return str.replace(/<(?!(a |\/a))/gi,"&lt;");
}

function formatedRes(res) {
	res = vkbeautify.xmlmin(res, true);

	try {
		var results = [];
		extractJSON(res, results);
		$(results).each(function(index, value) {
			res = res.replace(value, vkbeautify.json(value));
		});
	} catch (e) {
		console.log(e);
	}
	res = vkbeautify.xml(res);

	return res;
}

function extractJSON(str, results) {
	var firstOpen, firstClose, candidate;
	firstOpen = str.indexOf('{', firstOpen + 1);
	do {
		firstClose = str.lastIndexOf('}');
		// console.log('firstOpen: ' + firstOpen, 'firstClose: ' + firstClose);
		if (firstClose <= firstOpen) {
			return results;
		}
		do {
			candidate = str.substring(firstOpen, firstClose + 1);
			// console.log('candidate: ' + candidate);
			try {
				var res = JSON.parse(candidate);
				// console.log('...found');
				results.push(candidate);
				// return [res, firstOpen, firstClose + 1];
				return extractJSON(str.slice(firstClose + 1), results)
			} catch (e) {
				// console.log('...failed');
			}
			firstClose = str.substr(0, firstClose).lastIndexOf('}');
		} while (firstClose > firstOpen);
		firstOpen = str.indexOf('{', firstOpen + 1);
	} while (firstOpen != -1);
}

function showDialog(ele) {
	cmdDialog = $("#cmd-dialog");
	$('#request-details').html($(ele).find("td:nth(1)").html());
	$('#response-details').html($(ele).find("td:nth(2)").html());

	$(cmdDialog).dialog(
			{
				modal : true,
				resizable : true,
				draggable : true,
				width : '80%',
				height : '630',
				title : "Details",
				buttons : {
					'Close' : function() {
						$(this).dialog('close');
					},
					'Request' : function() {
						$('#request-details').show();
						$(cmdDialog).scrollTop("0");
					},
					'Response' : function() {
						$('#response-details').show();
						var top = $('#request-details').is(":visible") ? $(
								'#request-details').height() : "0";
						$(cmdDialog).scrollTop(top);
					}
				}
			});
}
function previewImage(uri) {

	// Get the HTML Elements
	imageDialog = $("#dialog");
	imageTag = $('#image');
	newWin = $('#newwin');

	// Split the URI so we can get the file name
	uriParts = uri.split("/");

	// append dir if not absolute path
	if (uri.indexOf('http') != 0 && uri.indexOf('\.') == 0)
		uri = curResultDir + "/" + uri;

	// Set the image src
	imageTag.attr('src', uri);
	newWin.attr('href', uri);

	// When the image has loaded, display the dialog
	imageTag.load(function() {

		$('#dialog').dialog({
			modal : true,
			resizable : true,
			draggable : true,
			width : '450px',
			title : uriParts[uriParts.length - 1]
		});
	});

}

function resetFilterAndOrder() {
	// reset default filter

	setChecked('#ffail', true);
	setChecked('#fpass', true);
	setChecked('#fskip', true);
	setChecked('#ftest', true);

	setChecked('#fconfig', false);

	// reset default order
	setChecked('#oexecution', false);
	setChecked('#ocollapse', true);
	setChecked('#opass', false);
	setChecked('#ofail', false);
	setChecked('#oskip', false);
	setChecked('#oexecution', false);
	setChecked('#oname', false);

	setChecked('#ocollapse', false);
	setChecked('#oexpand', false);

	$.ajaxQueue.clear();
	$('#loading-info').hide();
	// if (plot1) plot1.destroy();
	$("#error_analysis_header").removeClass("expanded");
	$("#error_analysis_header").addClass("collapse");

	$("#error_analysis_details").html('');
}

function isChecked(objCss) {
	return $(objCss).is(":checked");
}

function setChecked(objCss, bval) {
	return $(objCss).prop('checked', bval);
}

function applyUi(container) {
	var tc_name = $(container).parent().children('.mehodheader').find(
			'.ui-icon-text').text();
	var newName = $.trim(tc_name).replace(/\s/g, ".");

	$(container).removeClass('dataloading');

	$(container).children('.detailsContainer').prepend(
			$(container).parent().children('.meta-info'));
	$(container).children('.detailsContainer').children('.meta-info')
			.slideDown();
	$(container).children('.detailsContainer').children('.check-points')
			.slideDown();

	$(container).find('.meta-info-check-points.action').addClass(
			'ui-state-active');

	$(container).find('.action').each(function() {
		$(this).addClass('ui-state-hover');
		$(this).hover(function() {
			if (!($(this).hasClass("ui-state-active")))
				$(this).toggleClass('ui-state-highlight');
		});

	});

	$(container).find('.selenium-log.action').bind("click", function(event) {
		toggleTab($(this), '.selenium-log');
	});
	$(container).find('.error-trace.action').bind("click", function(event) {
		toggleTab($(this), '.error-trace');
	});
	$(container).find('.step-analysis.action').bind(
			"click",
			function(event) {
				toggleTab($(this), '.step-analysis');
				if (!$(this).parent().parent().find(
						'.tab-content.step-analysis').attr('id')) {
					$(this).parent().parent()
							.find('.tab-content.step-analysis').attr('id',
									'd_' + $.now());
					createStepGraph($(this).parent().parent().find(
							'.tab-content.step-analysis'));
				}
			});

	$(container).find('.meta-info-check-points.action').bind("click",
			function(event) {
				toggleTab($(this), '.meta-info');
				$('.check-points.tab-content').show();
			});

	$(container).find(".selenium-log table:odd").addClass('ui-state-default');
	$(container).find(".selenium-log table:even").addClass('ui-state-hover');

	$(container).find(".selenium-log tbody>tr:odd").addClass(
			'ui-state-highlight ui-widget-content');
	$(container).find(".selenium-log tbody>tr:even").addClass(
			'ui-state-active ui-widget-content');
	$(container).find(".selenium-log thead>tr").addClass('ui-widget-header');
	$('.screenshot').click(function(event) {

		event.preventDefault();
		previewImage($(this).attr('href'));

	});
	$(".screenshot").button({
		icons : {
			primary : "ui-icon-image"
		},
		text : false
	});
	$(container).children('.detailsContainer').find('.check-points-details')
			.prepend(
					$(container).children('.detailsContainer').children(
							'.check-points'));

}

function displayTotalTime(container, data) {

	var dur = getTotalDuration(data.checkPoints);
	if (dur > 0) {
		$(container).find("#totalTime").show();
		$(container).find("#totalTime").find('td').text((dur) + 's');
	}

}

function getTotalDuration(entries) {
	var dur = 0;
	$.each(entries, function(index, value) {
		if (value.duration)
			dur = dur + value.duration;
	});

	return dur > 0 ? dur / 1000.0 : 0;
}

function mehodheaderClick(ele) {
	details = $(ele).parent().children('.details');
	toggleTestDetails(details);
	setChecked('#ocollapse', false);
	setChecked('#oexpand', false);

	// testcase deep link
	var dataFile = $(details).attr('data-file');
	var tcLink = dataFile.slice(dataFile.lastIndexOf('/') + 1, dataFile
			.lastIndexOf('.'));
	var stIndex = dataFile.indexOf(curResultDir) + curResultDir.length + 1;
	if (stIndex > 0) {
		currSuite = dataFile.slice(stIndex).split("/")[0];
	}
	window.location.href = $("#reportlist li.selected a").attr('href') + "#"
			+ currSuite + "#" + tcLink;
}

function setActiveTab(tab) {
	$("ul.tabs li").removeClass("active");
	$(tab).addClass("active");
	$(".tab_content").hide();
}

function showTrendChart(tab) {
	interuptLoading = true;
	setActiveTab(tab);
	$("#trends-tab-content").show();
	$("#report_details").hide();
	$("#method-results").html("");
	$(".data_cont").show();
	if ($('#trends-chart').is(":empty")) {
		ajaxindicatorstart();
		drawTrendChart(treports.reports);
	}
	ajaxindicatorstop();
}

var plot1;

function getBucketFromErrorTrace(errorTrace) {

	errorTrace = errorTrace.replace(/(\r|\n)/g, '');
	var bucketToReturn = "AutomationFailure";
	$.each(errorBuckets, function(bucket, pattern) {

		if (pattern.test(errorTrace)) {
			bucketToReturn = bucket;
			return;
		}
	});
	return bucketToReturn;
}
// usage:
function populateErrorBucket() {
	var errorMap = {};
	var categoryCnt = 0;
	var chained;
	var per100 = $("#method-results>.fail:not([style*='display: none']), #method-results>.skip:not([style*='display: none'])").length;
	var progressCnt = 0;
	if (per100 == 0) {
		showErrorBucketProgress(progressCnt / per100);
		$("#error_analysis_details").html('');
	}
	$(
			"#method-results>.fail:not([style*='display: none']), #method-results>.skip:not([style*='display: none'])")
			.each(function() {
				var detailsContainer = $(this).find('.details');
				var dataFileUrl = $(detailsContainer).attr('data-file');
				var testObject = $(this);
				$.ajaxQueue.addRequest({
					url : dataFileUrl,
					dataType : 'json',
					success : function(data) {
						var errorTrace = data.errorTrace;
						if (!errorTrace || errorTrace.trim().length == 0) {
							errorTrace = "CheckPointFailure";
						}

						var error = errorTrace.split(':')[0];

						error = getBucketFromErrorTrace(errorTrace);

						if (!errorMap[error]) {
							errorMap[error] = [];
							categoryCnt++;
						}
						var testResult = [];
						testResult.push(data);
						testResult.push(testObject);
						errorMap[error].push(testResult);
						progressCnt++;
						showErrorBucketProgress(progressCnt / per100);
						// loadDetailsTemplate(data, detailsContainer);
					}
				});
			});
	$.ajaxQueue.run();
	$.ajaxQueue.done = function() {
		$('#error_analysis_details').html('');
		$.tmpl(errorAnalysisTemplate, errorMap).appendTo(
				$('#error_analysis_details'));

		$("#error_analysis_details .collapsible").click(function(event) {
			$(this).toggleClass('expanded');
			$(this).toggleClass('collapse');
		});
		// draw chart

		var data = [];
		var scolors = [];
		var f = Math.floor(255 / categoryCnt);

		categoryCnt = 0;
		$.map(errorMap, function(val, i) {
			data.push([ i, val.length ]);
			var g = categoryCnt * f;
			g = (g < 16 ? "00" : g);
			scolors.push("#FF" + g.toString(16) + "05");
			categoryCnt++;
		});
		scolors.sort(function() {
			return .5 - Math.random();
		});
		$.each($('#error_analysis_details .collapsible'), function(i) {
			$(this).css("background-color", scolors[i]);
		});
		jQuery.jqplot("error_analysis_chart", [ data ], {
			seriesDefaults : {
				// Make this a pie chart.
				renderer : $.jqplot.DonutRenderer,
				rendererOptions : {
					sliceMargin : 1,
					startAngle : -90,
					seriesColors : scolors,
					// Put data labels on the pie slices.
					// By default, labels show the percentage of the
					// slice.
					showDataLabels : true
				}
			}

		});

		// loadDetailsTemplate(data, detailsContainer);
		$.map(errorMap, function(val, i) {

			$(val).each(function(i) {
				var testResult = $(val)[i].pop();
				var data = $(val)[i].pop();
				var details = $(testResult).find('.details');
				if (details.is(":empty")) {
					loadDetailsTemplate(data, details);
				}
			});

		});
	};
}

function showErrorBucketProgress(per) {
	$('#error_analysis_details').html(Math.round(per * 100) + '%');
}

function displayErrorBucket() {
	if ($('#error_analysis_header').hasClass('expanded')) {
		populateErrorBucket();
	}
}

function removePkgName(cls) {
	return cls.indexOf('.') > 0 ? cls.substr(cls.lastIndexOf('.') + 1) : cls;
}
var i = 100;

function dspTCLink(obj) {
	var ele = $(obj[1]).find('.mehodheader .ui-icon-text');
	var nId = 'f' + (i++);
	$(ele).attr("id", nId);
	return "<li><a onclick='viewTest(\"" + nId
			+ "\")' href='javascript:void(0);'>" + $(ele).text() + "</a></li>";
}

function getTestName(obj, nid) {
	$(obj).find('.mehodheader .ui-icon-text').id = nid;
	return $(obj).find('.mehodheader .ui-icon-text').text();
}

function viewTest(id) {
	var ele = $("#" + id);
	$('.ui-layout-center.content.ui-layout-pane').animate({
		scrollTop : $(ele).offset().top - ($(".ui-layout-north").height() + 30)
	}, 500);

	var testResult = $(ele).parent().parent();
	$(testResult).children('.details').show();
	var errTraceTab = $(testResult).children().find('.error-trace');

	toggleTab(errTraceTab, '.error-trace');

}

function showOverview(tab) {
	interuptLoading = true;
	setActiveTab(tab);
	$("#overview-tab-content").show();
	$("#report_details").hide();
	$("#method-results").hide();

	$("#method-results").html("");
	$(".data_cont").show();

	window.location.href = $("#reportlist li.selected a").attr('href');
	pageLayout.open('west');
}

function drawTrendChart(reports) {
	var pass = [];
	var fail = [];
	var skip = [];
	var labelsX = [];
	var lstReports = [];
	var cnt = reports.length - 1;

	$.each(reports, function(i, report) {
		lstReports[cnt] = report.dir;
		cnt = cnt - 1;
	});

	$.each(lstReports, function(i, report) {
		chained = $.ajax({
			url : report + "/meta-info.json",
			dataType : 'json',
			async : false,
			success : function(details) {
				pass.push(details.pass);
				fail.push(details.fail);
				skip.push(details.skip);
			}
		});
		labelsX.push(i + 1);
	});

	chained.done(function() {
		var plot1b = $.jqplot('trends-chart', [ pass, fail, skip ], {
			stackSeries : true,
			seriesColors : [ '#23a347', '#e63c20', '#f3b600' ],
			seriesDefaults : {
				rendererOptions : {
					smooth : true
				},
				fill : true,
				fillAndStroke : true,
				fillAlpha : 0.75,
				shadow : false
			},
			axes : {
				xaxis : {
					min : 1,
					renderer : $.jqplot.CategoryAxisRenderer,
					label : 'Execution'
				},
				yaxis : {
					min : 0,
					labelRenderer : $.jqplot.CanvasAxisLabelRenderer,
					label : 'Number Of TestCases'
				}
			},

			highlighter : {
				show : true,
				showToolTip : true,
				tooltipContentEditor : function tooltipContentEditor(str,
						seriesIndex, pointIndex, plot) {
					var executionNumber = pointIndex;
					var labels = [ 'Pass', 'Fail', 'Skip' ];
					var total = plot.data[0][executionNumber]
							+ plot.data[1][executionNumber]
							+ plot.data[2][executionNumber]
					var stringToReturn = labels[0] + ": "
							+ plot.data[0][executionNumber] + "/" + total
							+ "<br/>" + labels[1] + ":&nbsp;&nbsp;&nbsp;"
							+ plot.data[1][executionNumber] + "/" + total
							+ "<br/>" + labels[2] + ":&nbsp&nbsp;"
							+ plot.data[2][executionNumber] + "/" + total;
					return stringToReturn;
				},

			},

			legend : {
				show : true,
				labels : [ 'Pass', 'Fail', 'Skip' ],
				renderer : $.jqplot.EnhancedLegendRenderer,
				location : 's',
				placement : 'outside',
				marginLeft : "-250px",
				// marginRight: "350px",
				marginTop : "30px",
				rendererOptions : {
					numberRows : '1',
					numberColumns : '3',
					seriesToggle : true
				}
			}
		});
		$('#trends-chart').bind(
				'jqplotDataClick',
				function(ev, seriesIndex, pointIndex, data) {
					var executionNumber = pointIndex + 1;

					if ($('#reportlist').find('li.selected').find('span.jobid')
							.text() != executionNumber) {
						$('#reportlist').children().find('.' + executionNumber)
								.click();
					} else {
						$('.fright').find('#overview-tab').click();
					}
				});

	});
}

function toggle(ele, childCss) {
	$(ele).children(childCss).toggle('slow');
}

function expandCollapseAll(expand) {
	if (!expand) {
		$.ajaxQueue.stop();
		$.ajaxQueue.clear();
		$('#loading-info').hide();
	}

	var tot = $('.mehod:not(:hidden) .details').length;

	if (expand) {
		$('#loading-info').show();
		if ($('.mehod:not(:hidden) .details:hidden').length < 1) {
			var len = $('.mehod:not(:hidden) .details').length;
			$('#progress').html('');
			$('#progress').append('<span>' + len + "/" + len + '</span>');
		}
	}

	$('.mehod:not(:hidden) .details').each(
			function(i) {
				if ($(this).is(":hidden") == expand) {
					var container = $(this);
					toggleTestDetails(container, true);
					if ($(this).is(":empty")) {
						$.ajaxQueue.addRequest({
							dataType : "json",
							url : $(container).attr("data-file"),
							success : function(data) {
								loadDetailsTemplate(data, container);
								$('#loading-info').show();
								$('#progress').html('');
								$('#progress').append(
										'<span>' + i + "/" + tot + "</span>");

								if (i == tot - 1) {
									$('#progress').html('');
									var j = i + 1;
									$('#progress').append(
											'<span>' + j + "/" + tot
													+ "</span>");
									$('#loading-info').hide(3000);
								}
							}
						});
					}
				}
			});
	if (expand) {
		$.ajaxQueue.run();
	}
	$('#loading-info').hide();
}

function toggleTestDetails(detailsContainer, isBulk) {
	if ($(detailsContainer).is(":hidden")) {
		showTestDetails(detailsContainer, isBulk);
	} else {
		$(detailsContainer).slideUp();
	}
	return true;

}

function showTestDetails(detailsContainer, isBulk) {
	$(detailsContainer).show();

	if ($(detailsContainer).is(":empty") && !isBulk) {
		$(detailsContainer).addClass('dataloading');

		return loadDetails($(detailsContainer).attr("data-file"),
				detailsContainer);
	}
	return true;
}

function wait(forTask, timeout) {
	setTimeout(forTask, timeout);
}

function doFilter(cssClass) {
	interuptLoading = true;
	var expr = '';

	expr = expr + (!isChecked('#fconfig') ? ':not(.config)' : '')
			+ (!isChecked('#ftest') ? ':not(.test)' : '');

	expr = expr + (!isChecked('#fpass') ? ':not(.pass)' : '')
			+ (!isChecked('#ffail') ? ':not(.fail)' : '')
			+ (!isChecked('#fskip') ? ':not(.skip)' : '');

	$('.mehod:has(' + expr + ')').show();
	$('.mehod:not(' + expr + ')').hide();

	setChecked('#ocollapse', false);
	setChecked('#oexpand', false);

	if (cssClass === 'fail' || cssClass === 'skip') {
		showErrorBucket();
	}
	var searchTerm = $('#inputSerach').val().trim();
	if (searchTerm.length > 0) {
		var expr = ".mehod:not(:has(.group:Contains('" + searchTerm + "')))";
		$(expr).hide();
	}
	setFilterResultCount();
}

function setFilterResultCount() {
	var tot = $(".mehod").length;
	if (tot > 0) {
		$('#filterResultCnt').text($(".mehod:not(:hidden)").length);
		$('#allResultCnt').text("/" + tot);
	} else {
		$('#filterResultCnt').text("");
		$('#allResultCnt').text("");
	}

}

jQuery.expr[':'].Contains = function(a, i, m) {

	var pattern = new RegExp(m[3], "ig");
	return pattern.test(jQuery(a).text());
};

function showErrorBucket() {
	if (!$("#ffail").is(":checked") && !$("#fskip").is(":checked")) {
		$('#error_analysis_header').hide();
	} else {
		$('#error_analysis_header').show();
	}
	$("#error_analysis_details").html('');
	populateErrorBucket();
}

function doSort(cssClass) {
	interuptLoading = true;
	$('.mehod').tsort(
			'.mehodheader .status',
			{
				sortFunction : function(a, b) {
					return a.s == b.s ? 0 : a.s == cssClass ? -1
							: b.s == cssClass ? 1 : 0;
				}
			});
}

function doSortE(isReverse) {
	interuptLoading = true;
	$('.mehod').tsort(
			'.mehodheader .oexecution',
			{
				sortFunction : function(a, b) {
					retVal = a.s == b.s ? 0
							: parseInt(a.s) > parseInt(b.s) ? (isReverse) ? -1
									: 1 : (isReverse) ? 1 : -1;
					return retVal;
				}
			});
}

function doSortN(isReverse) {
	$('.mehod').tsort('.mehodheader .ui-icon-text');
}
// utility functions

function parseArray(obj) {
	/*
	 * var blkstr = []; for (var i = 0, l = obj.length; i < l; i++) {
	 * blkstr.push(jsonToString(obj[i])); }
	 */return JSON.stringify(obj, null, ' ');
}

function ajaxindicatorstart(text) {
	$('#trends-chart-loading').show();
}

function ajaxindicatorstop() {
	$('#trends-chart-loading').hide();
}

function jsonToString(value) {
	if (isString(value))
		return value;
	var blkstr = [];
	$.each(value, function(idx2, val2) {
		var str = idx2 + ":" + jsonToString(val2);
		blkstr.push(str);
	});
	return blkstr.valueOf();
}

isString = function(o) {
	return o == null || typeof o == "string"
			|| (typeof o == "object" && o.constructor === String);
}

function isMap(o) {
    try {
        Map.prototype.has.call(o); // throws if o is not an object or has no [[MapData]]
        return true;
    } catch(e) {
        return false;
    }
}

function msToDateStr(ms) {
	var date = new Date(ms);
	return date;
}

function msToFormatedDateStr(ms) {
	var date = new Date(ms);
	return date.toLocaleDateString() + " " + date.toLocaleTimeString();// .customFormat(
}

function getDuration(ms) {
	if (ms < 0)
		return "N/A";
	secs = ms / 1000;
	var hours = Math.floor(secs / (60 * 60));
	var divisor_for_minutes = secs % (60 * 60);
	var minutes = Math.floor(divisor_for_minutes / 60);
	var divisor_for_seconds = divisor_for_minutes % 60;
	var seconds = Math.ceil(divisor_for_seconds);
	return hours + ":" + minutes + ":" + seconds;
}

function calcPassRate(pass, fail, skip) {
	return Math.round(pass / (pass + fail + skip) * 100);
}

String.prototype.capitalizeFirstLetter = function() {
	return this.charAt(0).toUpperCase() + this.slice(1);
};

/** * */
(function($) {
	var AjaxQueue = function(options) {
		this.options = options || {};
		var oldComplete = options.complete || function() {
		};
		var completeCallback = function(XMLHttpRequest, textStatus) {
			(function() {
				oldComplete(XMLHttpRequest, textStatus);
				if ($.ajaxQueue.getRequestCount() <= 0) {
					$.ajaxQueue.done();
					$.ajaxQueue.done = function() {
					};
				}
			})();
			$.ajaxQueue.currentRequest = null;
			$.ajaxQueue.startNextRequest();

		};
		this.options.complete = completeCallback;
	};

	AjaxQueue.prototype = {
		options : {},
		perform : function() {
			$.ajax(this.options);
		}
	}

	$.ajaxQueue = {
		queue : [],

		currentRequest : null,
		inprogrss : false,
		stopped : false,
		done : function() {
		},
		getRequestCount : function() {
			return $.ajaxQueue.queue.length;
		},
		stop : function() {
			$.ajaxQueue.stopped = true;

		},

		run : function() {
			$.ajaxQueue.stopped = false;
			$.ajaxQueue.startNextRequest();
		},

		clear : function() {
			$.ajaxQueue.stopped = false;
			var requests = $.ajaxQueue.queue.length;
			for (var i = 0; i < requests; i++) {
				$.ajaxQueue.queue.shift();
			}
			$.ajaxQueue.currentRequest = null;
		},

		addRequest : function(options) {
			var request = new AjaxQueue(options);
			$.ajaxQueue.queue.push(request);
			$.ajaxQueue.startNextRequest();
		},

		startNextRequest : function() {
			if ($.ajaxQueue.currentRequest) {
				return false;
			}
			var request = $.ajaxQueue.queue.shift();
			if (request && !$.ajaxQueue.stopped) {
				inprogrss = true;
				$.ajaxQueue.currentRequest = request;
				request.perform();
			} else {
				inprogrss = false;
			}
		}
	}

})(jQuery);
