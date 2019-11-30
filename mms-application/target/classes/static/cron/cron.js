var flag = false;

function setcron() {
	if (flag) {
		parent.setcron($("#cron").val());
	} else {
		$("#runTime").parent().parent().parent().css("background-color", "yellow");
	}
}
function closecron() {
	parent.closecron();
}

$(function() {
	$(".numberspinner").numberspinner({
		onChange : function() {
			$(this).closest("div.line").children().eq(0).click()
		}
	});
	var vals = $("input[name^='v_']");
	var cron = $("#cron");
	vals.change(function() {
		var item = [];
		vals.each(function() {
			item.push(this.value)
		});
		var currentIndex = 0;
		$(".tabs>li").each(function(i, item) {
			if ($(item).hasClass("tabs-selected")) {
				currentIndex = i;
				return false
			}
		});
		for (var i = currentIndex; i >= 1; i--) {
			if (item[i] != "*" && item[i - 1] == "*") {
				item[i - 1] = "0"
			}
		}
		if (item[currentIndex] == "*") {
			for (var i = currentIndex + 1; i < item.length; i++) {
				if (i == 5) {
					item[i] = "?"
				} else {
					item[i] = "*"
				}
			}
		}
		cron.val(item.join(" ")).change()
	});
	cron.change(function() {
		btnFan();
		$.ajax({
			type : "get",
			url : "cronValidation",
			dataType : "json",
			data : {
				"cron" : $("#cron").val()
			},
			success : function(data) {
				if (data.success == true) {
					flag = true;
					var date = data.date;
					var strHTML = "<ul>";
					for (var i = 0; i < date.length; i++) {
						strHTML += "<li>" + date[i] + "</li>"
					}
					strHTML += "</ul>";
					$("#runTime").parent().parent().parent().css("background-color", "");
					$("#runTime").html(strHTML)
				} else {
					flag = false;
					$("#runTime").html(data.message)
				}
			}
		})
	});
	var secondList = $(".secondList").children();
	$("#sencond_appoint").click(function() {
		if (this.checked) {
			if ($(secondList).filter(":checked").length == 0) {
				$(secondList.eq(0)).attr("checked", true)
			}
			secondList.eq(0).change()
		}
	});
	secondList.change(function() {
		var sencond_appoint = $("#sencond_appoint").prop("checked");
		if (sencond_appoint) {
			var vals = [];
			secondList.each(function() {
				if (this.checked) {
					vals.push(this.value)
				}
			});
			var val = "?";
			if (vals.length > 0 && vals.length < 59) {
				val = vals.join(",")
			} else {
				if (vals.length == 59) {
					val = "*"
				}
			}
			var item = $("input[name=v_second]");
			item.val(val);
			item.change()
		}
	});
	var minList = $(".minList").children();
	$("#min_appoint").click(function() {
		if (this.checked) {
			if ($(minList).filter(":checked").length == 0) {
				$(minList.eq(0)).attr("checked", true)
			}
			minList.eq(0).change()
		}
	});
	minList.change(function() {
		var min_appoint = $("#min_appoint").prop("checked");
		if (min_appoint) {
			var vals = [];
			minList.each(function() {
				if (this.checked) {
					vals.push(this.value)
				}
			});
			var val = "?";
			if (vals.length > 0 && vals.length < 59) {
				val = vals.join(",")
			} else {
				if (vals.length == 59) {
					val = "*"
				}
			}
			var item = $("input[name=v_min]");
			item.val(val);
			item.change()
		}
	});
	var hourList = $(".hourList").children();
	$("#hour_appoint").click(function() {
		if (this.checked) {
			if ($(hourList).filter(":checked").length == 0) {
				$(hourList.eq(0)).attr("checked", true)
			}
			hourList.eq(0).change()
		}
	});
	hourList.change(function() {
		var hour_appoint = $("#hour_appoint").prop("checked");
		if (hour_appoint) {
			var vals = [];
			hourList.each(function() {
				if (this.checked) {
					vals.push(this.value)
				}
			});
			var val = "?";
			if (vals.length > 0 && vals.length < 24) {
				val = vals.join(",")
			} else {
				if (vals.length == 24) {
					val = "*"
				}
			}
			var item = $("input[name=v_hour]");
			item.val(val);
			item.change()
		}
	});
	var dayList = $(".dayList").children();
	$("#day_appoint").click(function() {
		if (this.checked) {
			if ($(dayList).filter(":checked").length == 0) {
				$(dayList.eq(0)).attr("checked", true)
			}
			dayList.eq(0).change()
		}
	});
	dayList.change(function() {
		var day_appoint = $("#day_appoint").prop("checked");
		if (day_appoint) {
			var vals = [];
			dayList.each(function() {
				if (this.checked) {
					vals.push(this.value)
				}
			});
			var val = "?";
			if (vals.length > 0 && vals.length < 31) {
				val = vals.join(",")
			} else {
				if (vals.length == 31) {
					val = "*"
				}
			}
			var item = $("input[name=v_day]");
			item.val(val);
			item.change()
		}
	});
	var mouthList = $(".mouthList").children();
	$("#mouth_appoint").click(function() {
		if (this.checked) {
			if ($(mouthList).filter(":checked").length == 0) {
				$(mouthList.eq(0)).attr("checked", true)
			}
			mouthList.eq(0).change()
		}
	});
	mouthList.change(function() {
		var mouth_appoint = $("#mouth_appoint").prop("checked");
		if (mouth_appoint) {
			var vals = [];
			mouthList.each(function() {
				if (this.checked) {
					vals.push(this.value)
				}
			});
			var val = "?";
			if (vals.length > 0 && vals.length < 12) {
				val = vals.join(",")
			} else {
				if (vals.length == 12) {
					val = "*"
				}
			}
			var item = $("input[name=v_mouth]");
			item.val(val);
			item.change()
		}
	});
	var weekList = $(".weekList").children();
	$("#week_appoint").click(function() {
		if (this.checked) {
			if ($(weekList).filter(":checked").length == 0) {
				$(weekList.eq(0)).attr("checked", true)
			}
			weekList.eq(0).change()
		}
	});
	weekList.change(function() {
		var week_appoint = $("#week_appoint").prop("checked");
		if (week_appoint) {
			var vals = [];
			weekList.each(function() {
				if (this.checked) {
					vals.push(this.value)
				}
			});
			var val = "?";
			if (vals.length > 0 && vals.length < 7) {
				val = vals.join(",")
			} else {
				if (vals.length == 7) {
					val = "*"
				}
			}
			var item = $("input[name=v_week]");
			item.val(val);
			item.change()
		}
	})
});
(function($) {

	$(document).ready(function() {
		var cron = $.url().param('cron');
		if (cron) {
			$('#cron').val(cron);
			$('#cron').trigger('change');
			$('#btnFan').trigger('click');
		}
	});

})(jQuery);