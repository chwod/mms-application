Ext.require([ 'Ext.Editor', 'Ext.form.Panel', 'Ext.form.field.ComboBox',
		'Ext.form.field.Date', 'Ext.grid.*', 'Ext.data.*', 'Ext.panel.*', 'Ext.util.*',
		'Ext.layout.container.Border' ]);

Ext.onReady(function() {
	var changepasswordWindow = new Ext.Panel(
			{
				title : '修改密码',
				renderTo : 'container',
				width : 300,
				minWidth : 200,
				layout : 'fit',
				resizable : false,
				modal : true,
				defaultFocus : 'oldpassword',
				closeAction : 'hide',
				items : [{
					xtype : 'form',
					layout : {
						type : 'vbox',
						align : 'stretch'
					},
					border : false,
					bodyPadding : 10,
					fieldDefaults : {
						msgTarget : 'side',
						labelAlign : 'left',
						labelWidth : 100,
						selectOnFocus: true,
						allowBlank : false,
						labelStyle : 'font-weight:bold'
					},
					items : [{
						xtype: 'component',
						padding: '0 0 10',
						html: '<font color=red>初始密码在第一次登录后必需修改！</font>'
							},
							{
								xtype : 'textfield',
								fieldLabel : '原密码',
								inputType : 'password',
								name : 'oldpassword',
							},
							{
								xtype : 'textfield',
								fieldLabel : '新密码',
								inputType : 'password',
								name : 'newpassword'
							},
							{
								xtype : 'textfield',
								fieldLabel : '重复新密码',
								inputType : 'password',
								name : 'newpassword2'
							} ],
					buttons : [
							{
								text : '取消',
								handler : function() {
									Ext.Ajax.request({
										url : 'logout',
										success : function(response, opts) {
											window.location.href = "/?_t="
													+ new Date().toLocaleString();
										}
									});
								}
							},
							{
								text : '提交',
								handler : function() {
									var form = changepasswordWindow
											.down('[xtype=form]');
									if (form.isValid()) {
										var p = changepasswordWindow.down('[name=oldpassword]');
										var p1 = changepasswordWindow
												.down('[name=newpassword]');
										var p2 = changepasswordWindow
												.down('[name=newpassword2]');
										if (p1.getValue() != p2.getValue()) {
											Ext.Msg.alert("错误", "两次输入的新密码不对");
											return;
										}
										if (p.getValue() == p1.getValue()) {
											Ext.Msg.alert("错误", "新密码不能于原密码相同！");
											return;
										}
										var mask = new Ext.LoadMask({
											msg : '请稍等...',
											target : changepasswordWindow
										});
										mask.show();
										form.submit({
											url : 'changepassword',
											method : 'post',
											failure : function(form, action) {
												mask.hide();
												Ext.Msg.alert("验证失败", action && action.result && action.result.message ? action.result.message : '网络超时');
											},
											success : function(form, action) {
												Ext.Msg.alert("成功", "密码修改完成，请重新登录！", function(){
													Ext.Ajax.request({
														url : 'logout',
														success : function(response, opts) {
															window.location.href = "/?_t="
																+ new Date().toLocaleString();
														}
													});
												});
											}
										});
									}
								}
							} ]
				}]
			}).center();
	
	changepasswordWindow.getEl().on({
		keyup: function(e){
			if(e.getKey() == Ext.event.Event.ENTER){
				e.preventDefault();
				var form = changepasswordWindow
						.down('[xtype=form]');
				if (form.isValid()) {
					var p = changepasswordWindow.down('[name=oldpassword]');
					var p1 = changepasswordWindow
							.down('[name=newpassword]');
					var p2 = changepasswordWindow
							.down('[name=newpassword2]');
					if (p1.getValue() != p2.getValue()) {
						Ext.Msg.alert("错误", "两次输入的新密码不同");
						return;
					}
					if (p.getValue() == p1.getValue()) {
						Ext.Msg.alert("错误", "新密码不能与原密码相同！");
						return;
					}
					var mask = new Ext.LoadMask({
						msg : '请稍等...',
						target : changepasswordWindow
					});
					mask.show();
					form.submit({
						url : 'changepassword',
						method : 'post',
						failure : function(form, action) {
							mask.hide();
							Ext.Msg.alert("验证失败", action && action.result && action.result.message ? action.result.message : '网络超时');
						},
						success : function(form, action) {
							Ext.Msg.alert("成功", "密码修改完成，请重新登录！", function(){
								Ext.Ajax.request({
									url : 'logout',
									success : function(response, opts) {
										window.location.href = "/?_t="
											+ new Date().toLocaleString();
									}
								});
							});
						}
					});
				}
			}
		}
	});
	
	changepasswordWindow.down('[name=oldpassword]').focus();
	
	});