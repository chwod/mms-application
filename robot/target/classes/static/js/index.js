$(function() {
	$('<script>').prop('src', '/js/bootstrap.min.js').appendTo($('head'));

	$('div.talk-box > div.col > textarea').val('').focus();
	
	$(window).resize(
			function() {
				$('div.talk-list').height(
						window.innerHeight - $('div.talk-top').height()
								- $('div.talk-space').height()
								- $('div.talk-box').height()
								- $('div.talk-space').height()
								- $('div.talk-send').height()
								- $('div.talk-buttom').height());
			}).resize();
	
	$('div.talk-box textarea').keydown(function(event){
		
		$(this).val($(this).val().replace(/\n+/,''));
		$(this).val($(this).val().replace(/\s+/,' '));
		
		if(event.which == 13){
			
			event.preventDefault();
			
			$(this).val($.trim($(this).val()));
			
			if($(this).val().length > 0){
				
				showTalkItem(true, $(this).val());
				
				$.post('/',{q : $(this).val().substring(0,50)},function(data){
					showTalkItem(false, data.result, data.time);
				});
				
				$(this).val('').focus();
			}
		}
	});
	
	$('div.talk-box textarea').keyup(function(){
		$(this).val($(this).val().replace(/\n+/g,''));
		$(this).val($(this).val().replace(/\s+/g,' '));
	});
	
	var showTalkItem = function(isMe, data, time, muchMoreFlag){
		$('<div/>').addClass('talk-item')
			.addClass(isMe ? 'talk-me' : 'talk-robot')
			.append($('<div/>').addClass('talk-time').text(time ? time : new Date().Format("yyyy-MM-dd hh:mm:ss")))
			.append($('<div/>').addClass('taok-content')
						.append(isMe ? data.substring(0,50) : data)
						.append(isMe && data.length > 50 ? '...' : '')
						.append(isMe && data.length > 50 ? $('<font/>').prop('color','red').append($('<br/>')).append('(你输入的内容超过系统限制大小，超过的部分已经忽略)') : null))
			.appendTo($('div.talk-list > div.col'));
		$('div.talk-list').scrollTop(Number.MAX_SAFE_INTEGER);
	}
	
	Date.prototype.Format = function (fmt) { //author: meizz 
	    var o = {
	        "M+": this.getMonth() + 1, //月份 
	        "d+": this.getDate(), //日 
	        "h+": this.getHours(), //小时 
	        "m+": this.getMinutes(), //分 
	        "s+": this.getSeconds(), //秒 
	        "q+": Math.floor((this.getMonth() + 3) / 3), //季度 
	        "S": this.getMilliseconds() //毫秒 
	    };
	    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
	    for (var k in o)
	    if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
	    return fmt;
	}
	
});
