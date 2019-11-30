


jQuery(document).ready(function(){
	$.post('/version', function(v){
		$('#vgversion').text(v);
	});
	
	
	$('#logoutDiv').click(function(){
		$.get('/logout', function(){
			window.location.href = "/?_t=" + new Date();
		});
	});
	
	$('#changepasswordDiv').click(function(){
		dialog.dialog('open');
	});
	
	var messagedialog = $('div#changepassword-dialog-message').dialog({
		autoOpen: false,
		model: true,
		buttons: {
			"确定": function(){
				messagedialog.dialog('close');
			}
		},
		close: function(){
			$.get('/logout', function(){
				window.location.href = "/?_t=" + new Date();
			});
		}
	});
	
	var dialog = $("#changepassword-dialog-form").dialog({
		autoOpen : false,
		modal : true,
		width: 320,
		buttons : {
			"确定" : function() {
				if(dialog.find('input[name=oldpassword]').val().length === 0 || 
						dialog.find('input[name=newpassword]').val().length === 0 ||
						dialog.find('input[name=renewpassword]').val().length === 0 ){
					dialog.find('p.validateTips').text('所有项都是必填项');
					dialog.find('p.validateTips').addClass('ui-state-highlight');
					setTimeout(function() {
						dialog.find('p.validateTips').removeClass("ui-state-highlight", 1500);
					}, 500);
					return;
				}
				
				if(dialog.find('input[name=newpassword]').val() != dialog.find('input[name=renewpassword]').val()) {
					dialog.find('p.validateTips').text('两次输入的新密码不相同');
					dialog.find('p.validateTips').addClass('ui-state-highlight');
					setTimeout(function() {
						dialog.find('p.validateTips').removeClass("ui-state-highlight", 1500);
					}, 500);
					return;
				}
				
				if(dialog.find('input[name=newpassword]').val() == dialog.find('input[name=oldpassword]').val()) {
					dialog.find('p.validateTips').text('新密码不能与旧密码相同');
					dialog.find('p.validateTips').addClass('ui-state-highlight');
					setTimeout(function() {
						dialog.find('p.validateTips').removeClass("ui-state-highlight", 1500);
					}, 500);
					return;
				}
				
				$.post('changepassword',
					{
						oldpassword: dialog.find('input[name=oldpassword]').val(),
						newpassword: dialog.find('input[name=newpassword]').val()
					},	
					function(result){
						if(result.success){
							messagedialog.dialog('open');
						} else {
							dialog.find('p.validateTips').text(result.message);
							dialog.find('p.validateTips').addClass('ui-state-highlight');
							setTimeout(function() {
								dialog.find('p.validateTips').removeClass("ui-state-highlight", 1500);
							}, 500);
						}
				},'json');
				
			},
			"取消" : function() {
				dialog.dialog("close");
			}
		},
		close : function() {
			dialog.find('p.validateTips').text("");
			dialog.find('input').val('');
		}
	});
	
	dialog.find("form").on("submit", function(event) {
		event.preventDefault();
		if(dialog.find('input[name=oldpassword]').val().length === 0 || 
				dialog.find('input[name=newpassword]').val().length === 0 ||
				dialog.find('input[name=renewpassword]').val().length === 0 ){
			dialog.find('p.validateTips').text('所有项都是必填项');
			dialog.find('p.validateTips').addClass('ui-state-highlight');
			setTimeout(function() {
				dialog.find('p.validateTips').removeClass("ui-state-highlight", 1500);
			}, 500);
			return;
		}
		
		if(dialog.find('input[name=newpassword]').val() != dialog.find('input[name=renewpassword]').val()) {
			dialog.find('p.validateTips').text('两次输入的新密码不相同');
			dialog.find('p.validateTips').addClass('ui-state-highlight');
			setTimeout(function() {
				dialog.find('p.validateTips').removeClass("ui-state-highlight", 1500);
			}, 500);
			return;
		}
		
		if(dialog.find('input[name=newpassword]').val() == dialog.find('input[name=oldpassword]').val()) {
			dialog.find('p.validateTips').text('新密码不能与旧密码相同');
			dialog.find('p.validateTips').addClass('ui-state-highlight');
			setTimeout(function() {
				dialog.find('p.validateTips').removeClass("ui-state-highlight", 1500);
			}, 500);
			return;
		}
		
		$.post('changepassword', 
				{
					oldpassword: dialog.find('input[name=oldpassword]').val(),
					newpassword: dialog.find('input[name=newpassword]').val()
				},	
				function(result){
					if(result.success){
						messagedialog.dialog('open');
					} else {
						dialog.find('p.validateTips').text(result.message);
						dialog.find('p.validateTips').addClass('ui-state-highlight');
						setTimeout(function() {
							dialog.find('p.validateTips').removeClass("ui-state-highlight", 1500);
						}, 500);
					}
			},'json');
	});
});