Ext.require(['Ext.data.*', 'Ext.grid.*']);

Ext.define('mailserver', {
    extend: 'Ext.data.Model',
    fields: [{
        name: 'id',
        type: 'int'
    }, 
    'displayName',
    'serverType',
    'connType',
    'auth',
    'domainName',
    'smtpHost',
    'smtpPort',
    'ior',
    'ssl',
    'tls',
    'defaultSenderAddress',
    'defaultSenderTitle',
    'defaultSenderUserName',
    'mailFile',
    'limitCycle',
    'limitCount',
    'status',
    'createTime']
    
});

Ext.onReady(function(){

    var msstore = Ext.create('Ext.data.JsonStore', {
        autoLoad: true,
        model: 'mailserver',
        proxy: {
            type: 'ajax',
            url: '/mscList'
        },
        listeners: {
        	endupdate: function(){
        		setgridbutton();
        	}
        }
    });
    
    var msTemplatestore = Ext.create('Ext.data.JsonStore', {
        fields: ['key', 'val'],
        autoLoad: true,
        proxy: {
        	type:	'ajax',
        	url:	'mstList'
        }
    });
    
    var mstypestore = Ext.create('Ext.data.Store', {
		fields: ['value', 'name'],
		data: [
		        {value: '0', name: '标准通用邮件服务器'},
		        {value: '1', name: 'IBM Lotus Domino'},
		        {value: '2', name: 'Microsoft Exchange'}
		]
	});
   
    var regularWindow = new Ext.window.Window(
    		{
    			title : '邮件发送限制',
    			minWidth : 200,
    			layout : 'fit',
    			resizable : false,
    			modal : true,
    			closeAction : 'hide',
    			items : [ {
    				xtype : 'form',
    				layout : {
    					type : 'vbox',
    					align : 'stretch'
    				},
    				timeout: 60,
    				border : false,
    				bodyPadding : 10,
    				fieldDefaults : {
    					msgTarget : 'under',
    					labelAlign : 'top',
    					labelWidth : 100,
    					labelStyle : 'font-weight:bold'
    				},
    				items : [
    						{
    							xtype : 'fieldcontainer',
    							layout : 'hbox',
    							items : [
    									{
    										xtype : 'numberfield',
    										fieldLabel : '时间周期(分钟)',
    										inputType : 'cycle',
    										minValue : 0,
    										afterLabelTextTpl : [ '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>' ],
    										name : 'cycle',
    										allowBlank : false
    									},
    									{
    										xtype : 'component',
    										html : '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'
    									},
    									{
    										xtype : 'numberfield',
    										fieldLabel : '总数量',
    										inputType : 'count',
    										minValue : 0,
    							        	hideTrigger: true,
    						                keyNavEnabled: false,
    						                mouseWheelEnabled: false,
    										afterLabelTextTpl : [ '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>' ],
    										name : 'count',
    										allowBlank : false
    									} ]
    						}, {
    							xtype : 'component',
    							anchor : '100%',
    							html : [ '<p>说明：在时间周期内最多可发送的总数量，设置为0表示不限制。</p>' ]
    						} ],
    				buttons : [ {
    					text : '取消',
    					handler : function() {
    						regularWindow.down('[xtype=form]').reset();
    						regularWindow.hide();
    					}
    				}, {
    					text : '提交',
    					handler : function() {
    						if(grid.getSelection().length > 0){
    							var form = regularWindow.down('[xtype=form]');
    							if (form.isValid()) {
    								var mask = new Ext.LoadMask({
    									msg : '请稍等...',
    									target : regularWindow
    								});
    								mask.show();
    								form.submit({
    									url : 'regular',
    									method : 'post',
    									params: {
    										id: grid.getSelection()[0].get('id')
    									},
    									failure : function(form, action) {
    										mask.destroy();
    										Ext.Msg.alert("验证失败", action && action.result && action.result.message ? action.result.message : '网络超时');
    									},
    									success : function(form, action) {
    										msstore.reload();
    										mask.destroy();
    										form.reset();
    										regularWindow.hide();
    									}
    								});
    							}
    						}
    					}
    				} ]
    			} ]
    		});
    
	var newWin = Ext.create('Ext.window.Window',
			{
				title : '系统邮件配置',
				minWidth : 240,
				layout : 'fit',
				resizable : false,
				modal : true,
				iconCls: 'icon-new',
				closeAction : 'hide',
				animateTarget: 'grid-add-button',
				animateShadow: true,
				items : [ {
					xtype : 'form',
					layout : {
						type : 'vbox',
						align : 'center'
					},
					border : false,
					bodyPadding : 10,
					fieldDefaults : {
						msgTarget : 'under',
						labelAlign : 'top',
						labelStyle : 'font-weight:bold'
					},
					items : [{
			            defaultType:'textfield',
			            defaults: {
			                allowBlank: false,
			                selectOnFocus: true,
			                hidden: true,
			                disabled: true,
			                msgTarget: 'side',
			                labelAlign: 'right',
			            },
			            items:[{
			            	xtype: 'hiddenfield',
			            	name: 'id',
			            	allowBlank: true,
			            	disabled: false,
			            }, {
			             	fieldLabel:'显示名称',
			            	name: 'displayName',
			            	width: 400,
			            	hidden: false,
			            	disabled: false
			            }, {
			            	xtype: 'fieldcontainer',
			            	hidden: false,
			            	disabled: false,
			            	layout: {
			            		type: 'hbox',
			            		defaultMargins: {top: 0, right: 5, bottom: 0, left: 0}
			            	},
			            	defaultType:'fieldcontainer',
			            	defaults: {
			            		hidden: false,
				            	disabled: false,
				            	allowBlank: false,
				                selectOnFocus: true,
				                msgTarget: 'side',
				                labelAlign: 'right',
			            		layout: {
				            		type: 'vbox',
				            		defaultMargins: {top: 0, right: 5, bottom: 0, left: 0}
				            	},
			            	},
			            	items: [{
			            		xtype: 'fieldcontainer',
			            		defaultType:'textfield',
				            	hidden: false,
				            	disabled: false,
				            	layout: {
				            		type: 'vbox',
				            		defaultMargins: {top: 0, right: 5, bottom: 0, left: 0}
				            	},
				            	defaults: {
				            		hidden: false,
					            	disabled: false,
					            	allowBlank: false,
					                selectOnFocus: true,
					                msgTarget: 'side',
					                labelAlign: 'right',
				            		layout: {
					            		type: 'hbox',
					            		defaultMargins: {top: 0, right: 5, bottom: 0, left: 0}
					            	},
				            	},
				            	items: [{
					            	xtype: 'radiogroup',
					            	itemId: 'setup-type-container',
					            	fieldLabel:'配置方式',
					            	layout: 'hbox',
					            	hidden: false,
					            	disabled: false,
					            	items: [
					            		{
					            			id: 'radio1', 
					            			boxLabel: '尝试从数据库中直接导入', 
					            			name: 'setuptype',
					            			hidden: true, 
					            			disabled: true, 
					            			inputValue: 1, 
					            			listeners:{change: function(obj){
					            			if(obj.getValue()){
					            	    		var target = newWin.down('[name=testflag]');
					            				target.setHidden(true);
					            				target = newWin.down('[name=serverType]');
					            				target.setHidden(true);
					            				target.disable();
					            				target.clearInvalid();
					            				target = newWin.down("[flag=connType]");
					            				target.setHidden(true);
					            				target = newWin.down('[name=msid]');
					            				target.setHidden(true);
					            				target.disable();
					            				target.clearInvalid();
					            				target = newWin.down('[name=smtpHost]');
					            				target.setHidden(true);
					            				target.disable();
					            				target.clearInvalid();
					            				target = newWin.down('[name=ior]');
					            				target.setHidden(true);
					            				target.disable();
					            				target.clearInvalid();
					            				target = newWin.down('[name=smtpPort]');
					            				target.setHidden(true);
					            				target.disable();
					            				target.clearInvalid();
					            				target = newWin.down('[name=defaultSenderAddress]');
					            				target.setHidden(true);
					            				target.disable();
					            				target.clearInvalid();
					            				target = newWin.down('[name=defaultSenderTitle]');
					            				target.setHidden(true);
					            				target.disable();
					            				target.clearInvalid();
					            				target = newWin.down('[name=domainName]');
					            				target.setHidden(true);
					            				target.disable();
					            				target.clearInvalid();
					            				target = newWin.down('[name=auth]');
					            				target.setHidden(true);
					            				target.disable();
					            				target.clearInvalid();
					            				target = newWin.down('[name=defaultSenderUserName]');
					            				target.setHidden(true);
					            				target.disable();
					            				target.clearInvalid();
					            				target = newWin.down('[name=defaultSenderPassword]');
					            				target.setHidden(true);
					            				target.disable();
					            				target.clearInvalid();
					            				target = newWin.down('[name=mailFile]');
					            				target.setHidden(true);
					            				target.disable();
					            				target.clearInvalid();
					            				target = newWin.down('[name=ssl]');
					            				target.setHidden(true);
					            				target.disable();
					            				target.clearInvalid();
					            				target = newWin.down('[name=tls]');
					            				target.setHidden(true);
					            				target.disable();
					            				target.clearInvalid();
					            			}
					            		}}},
					            		{
					            			id: 'radio2', 
					            			boxLabel: '从模版导入',
					            			name: 'setuptype', 
					            			inputValue: 2, 
					            			disabled: false, 
					            			listeners:{change: function(obj){
					            			if(obj.getValue()){
					            	    		var target = newWin.down('[name=testflag]');
					            				target.setHidden(true);
					            				target = newWin.down('[name=serverType]');
					            				target.setHidden(true);
					            				target.disable();
					            				target.clearInvalid();
					            				target = newWin.down("[flag=connType]");
					            				target.setHidden(true);
					            				target = newWin.down('[name=msid]');
					            				target.setHidden(false);
					            				target.enable();
					            				target.clearInvalid();
					            				target = newWin.down('[name=ior]');
					            				target.setHidden(true);
					            				target.disable();
					            				target.clearInvalid();
					            				target = newWin.down('[name=testmail]');
					            				target.setHidden(true);
					            				target.disable();
					            				target.clearInvalid();
					            				target = newWin.down('[name=smtpHost]');
					            				target.setHidden(true);
					            				target.disable();
					            				target.clearInvalid();
					            				target = newWin.down('[name=smtpPort]');
					            				target.setHidden(true);
					            				target.disable();
					            				target.clearInvalid();
					            				target = newWin.down('[name=defaultSenderAddress]');
					            				target.setHidden(false);
					            				target.enable();
					            				target.clearInvalid();
					            				target = newWin.down('[name=defaultSenderTitle]');
					            				target.setHidden(false);
					            				target.enable();
					            				target.clearInvalid();
					            				target = newWin.down('[name=domainName]');
					            				target.setHidden(true);
					            				target.disable();
					            				target.clearInvalid();
					            				target = newWin.down('[name=auth]');
					            				target.setHidden(false);
					            				target.enable();
					            				target.clearInvalid();
					            				target.fireEvent('change');
					            				target = newWin.down('[name=testmail]');
					            				target.setHidden(false);
					            				target.enable();
					            				target.clearInvalid();
					            				target = newWin.down('[name=mailFile]');
					            				target.setHidden(true);
					            				target.disable();
					            				target.clearInvalid();
					            				target = newWin.down('[name=ssl]');
					            				target.setHidden(true);
					            				target.disable();
					            				target.clearInvalid();
					            				target = newWin.down('[name=tls]');
					            				target.setHidden(true);
					            				target.disable();
					            				target.clearInvalid();
					            			}
					            		}}},
					            		{
					            			id: 'radio3', 
					            			boxLabel: '自定义设定', 
					            			name: 'setuptype', 
					            			inputValue: 3, 
					            			padding: '0 10',
					            			checked: true, 
					            			listeners:{change: function(obj){
					            			if(newWin.down('[id=radio3]').getValue()){
					            	    		var target = newWin.down('[name=testflag]');
					            				target.setHidden(true);
					            				target = newWin.down('[name=msid]');
					            				target.setHidden(true);
					            				target.disable();
					            				target.clearInvalid();
					            	    		target = newWin.down('[name=smtpHost]');
					            				target.setHidden(false);
					            				target.enable();
					            				target.clearInvalid();
					            				target = newWin.down('[name=serverType]');
					            				target.setHidden(false);
					            				target.enable();
					            				target.clearInvalid();
					            				target.fireEvent('select');
					            				target = newWin.down('[name=auth]');
					            				target.setHidden(false);
					            				target.enable();
					            				target.clearInvalid();
					            				target.fireEvent('change');
					            				target = newWin.down('[name=testmail]');
					            				target.setHidden(false);
					            				target.enable();
					            				target.clearInvalid();
					            			}
					            		}}}
					            	]
					            }, {
					            	fieldLabel: '同时验证信息的有效性',
					            	xtype:'checkbox',
					            	name: 'testflag',
					            	editable: false,
					            	disabled: false,
					            	checked: true,
					            	listeners: {
					            		change: function(){
					            			var target = this.up('form').down('[name=testmail]');
					            			target.clearInvalid();
					            			if(this.getValue()){
					            				target.setHidden(false);
					            				target.enable();
					            			}else{
					            				target.setHidden(true);
					            				target.disable();
					            			}
					            		}
					            	}
					            },{
					            	fieldLabel: '支持的邮件服务器列表',
					            	xtype: 'combo',
					            	store: msTemplatestore,
					            	queryMode: 'local',
					                displayField: 'key',
					                valueField: 'val',
					                editable: false,
					            	name: 'msid'
					            }, {
						            fieldLabel: '邮件服务器类型',
						            xtype: 'combo',
						            store: mstypestore,
					            	queryMode: 'local',
					                displayField: 'name',
					                valueField: 'value',
					                editable: false,
					                value: 0,
					            	name: 'serverType',
					            	listeners: {
					            		select: function(combo,record){
					            			var value = newWin.down('[name=serverType]').getValue();
					            			if(value == 0){
					            				var target = newWin.down("[flag=connType]");
					            				target.clearInvalid();
					            				target.setHidden(true);
					            				newWin.down('[name=ssl]').fireEvent('change');
					            				target = newWin.down('[name=smtpHost]');
					            				target.setHidden(false);
					            				target.enable();
					            				target.clearInvalid();
					            				target = newWin.down('[name=ssl]');
					            				target.setHidden(false);
					            				target.enable();
					            				target.clearInvalid();
					            				target = newWin.down('[name=tls]');
					            				target.setHidden(false);
					            				target.enable();
					            				target.clearInvalid();
					            				target = newWin.down('[name=ior]');
					            				target.setHidden(true);
					            				target.disable();
					            				target.clearInvalid();
					            				target = newWin.down('[name=mailFile]');
					            				target.setHidden(true);
					            				target.disable();
					            				target.clearInvalid();
					            				target = newWin.down('[name=defaultSenderAddress]');
					            				target.setHidden(false);
					            				target.enable();
					            				target.clearInvalid();
					            				target = newWin.down('[name=defaultSenderTitle]');
					            				target.setHidden(false);
					            				target.enable();
					            				target.clearInvalid();
					            				target = newWin.down('[name=domainName]');
					            				target.setHidden(true);
					            				target.disable();
					            				target.clearInvalid();
					            				target = newWin.down('[name=smtpPort]');
					            				target.setHidden(false);
					            				target.enable();
					            				target.clearInvalid();
					            			}
					            			if(value == 1){
					            				var target = newWin.down("[flag=connType]");
					            				target.clearInvalid();
					            				target.setHidden(false);
					            				newWin.down('[name=smtpPort]').setValue(63148);
					            				target = newWin.down('[name=mailFile]');
					            				target.setHidden(false);
					            				target.enable();
					            				target.clearInvalid();
					            				target = newWin.down('[name=defaultSenderAddress]');
					            				target.setHidden(true);
					            				target.disable();
					            				target.clearInvalid();
					            				target = newWin.down('[name=defaultSenderTitle]');
					            				target.setHidden(true);
					            				target.disable();
					            				target.clearInvalid();
					            				target = newWin.down('[name=ssl]');
					            				target.setHidden(true);
					            				target.disable();
					            				target.clearInvalid();
					            				target = newWin.down('[name=tls]');
					            				target.setHidden(true);
					            				target.disable();
					            				target.clearInvalid();
					            				target = newWin.down('[name=domainName]');
					            				target.setHidden(true);
					            				target.disable();
					            				target.clearInvalid();
					            				target = newWin.down('[name=smtpPort]');
					            				target.setHidden(true);
					            				target.disable();
					            				target.clearInvalid();
					            				newWin.down('[id=radio4]').fireEvent('change');
					            				newWin.down('[id=radio5]').fireEvent('change');
					            			}
					            			if(value == 2){
					            				var target = newWin.down("[flag=connType]");
					            				target.clearInvalid();
					            				target.setHidden(true);
					            				newWin.down('[name=ssl]').fireEvent('change');
					            				target = newWin.down('[name=smtpHost]');
					            				target.setHidden(false);
					            				target.enable();
					            				target.clearInvalid();
					            				target = newWin.down('[name=ssl]');
					            				target.setHidden(false);
					            				target.enable();
					            				target.clearInvalid();
					            				target = newWin.down('[name=tls]');
					            				target.setHidden(false);
					            				target.enable();
					            				target.clearInvalid();
					            				target = newWin.down('[name=ior]');
					            				target.setHidden(true);
					            				target.disable();
					            				target.clearInvalid();
					            				target = newWin.down('[name=mailFile]');
					            				target.setHidden(true);
					            				target.disable();
					            				target.clearInvalid();
					            				target = newWin.down('[name=defaultSenderAddress]');
					            				target.setHidden(false);
					            				target.enable();
					            				target.clearInvalid();
					            				target = newWin.down('[name=defaultSenderTitle]');
					            				target.setHidden(false);
					            				target.enable();
					            				target.clearInvalid();
					            				target = newWin.down('[name=domainName]');
					            				target.setHidden(false);
					            				target.enable();
					            				target.clearInvalid();
					            				target = newWin.down('[name=smtpPort]');
					            				target.setHidden(false);
					            				target.enable();
					            				target.clearInvalid();
					            			}
					            		}
					            	}
					            }, {
					            	xtype: 'radiogroup',
					            	fieldLabel:'连接方式',
					            	itemId: 'mail-conntype-container',
					            	flag: 'connType',
					            	layout: 'vbox',
					            	disabled: false,
					            	items: [
					            		{id: 'radio4', boxLabel: '指定服务器地址', name: 'connType', inputValue: 0, checked: true, listeners:{change: function(obj){
					            			if(newWin.down('[id=radio4]').getValue()){
					            				var target = newWin.down('[name=smtpHost]');
					            				target.setHidden(false);
					            				target.enable();
					            				target.clearInvalid();
					            				target = newWin.down('[name=ior]');
					            				target.setHidden(true);
					            				target.disable();
					            				target.clearInvalid();
					            				target = newWin.down('[name=smtpPort]');
					            				target.setHidden(false);
					            				target.enable();
					            				target.clearInvalid();
					            			}
					            		}}},
					            		{id: 'radio5', boxLabel: '通过IOR字符串', name: 'connType', inputValue: 1, listeners:{change: function(obj){
					            			if(newWin.down('[id=radio5]').getValue()){
					            				var target = newWin.down('[name=smtpHost]');
					            				target.setHidden(true);
					            				target.disable();
					            				target.clearInvalid();
					            				target = newWin.down('[name=ior]');
					            				target.setHidden(false);
					            				target.enable();
					            				target.clearInvalid();
					            				var target = newWin.down('[name=smtpPort]');
					            				target.setHidden(true);
					            				target.disable();
					            				target.clearInvalid();
					            			}
					            		}}}
					            	]
					            }, {
					            	fieldLabel: '邮件服务器地址',
					            	name: 'smtpHost'
					            }, {
					            	xtype: 'textarea',
					            	fieldLabel: 'IOR',
					            	name: 'ior'
					            }, {
					            	fieldLabel: '端口号',
					            	xtype: 'numberfield',
					            	name: 'smtpPort',
					            	value: 25,
					            	hideTrigger: true,
					                keyNavEnabled: false,
					                mouseWheelEnabled: false
					            }, {
					            	fieldLabel: 'SSL',
					            	xtype:'checkbox',
					            	name: 'ssl',
					            	editable: true,
					            	disabled: false
					            }, {
					            	fieldLabel: 'TLS',
					            	xtype:'checkbox',
					            	name: 'tls',
					            	editable: true,
					            	disabled: false
					            }, {
					            	fieldLabel:'用于发送的电子邮件地址',
					             	name: 'defaultSenderAddress',
					             	vtype: 'email',
					             	listeners: [{
					             		change: function(){
					             			var value = this.getValue();
					             			newWin.down('[name=testmail]').setValue(value);
					             			var strs = new Array();
					             			strs = value.split("@");
					             			if(strs.length == 2){
					             				newWin.down('[name=defaultSenderTitle]').setValue(strs[0]);
					             				newWin.down('[name=defaultSenderUserName]').setValue(strs[0]);
					             				newWin.down('[name=domainName]').setValue(strs[1]);
					             			}
					             		}
					             	}]
					            }]
				            }, {
			            		items: [{
				            		xtype: 'fieldcontainer',
				            		defaultType:'textfield',
					            	hidden: false,
					            	disabled: false,
					            	padding: '0 10',
					            	layout: {
					            		type: 'vbox',
					            		defaultMargins: {top: 0, right: 5, bottom: 0, left: 0}
					            	},
					            	defaults: {
					            		hidden: false,
						            	disabled: false,
						            	allowBlank: false,
						                selectOnFocus: true,
						                msgTarget: 'side',
						                labelAlign: 'right',
					            		layout: {
						            		type: 'hbox',
						            		defaultMargins: {top: 0, right: 5, bottom: 0, left: 0}
						            	},
					            	},
					            	items: [{
						            	fieldLabel:'用以发送的邮件发件人显示名称',
						             	name: 'defaultSenderTitle',
						             	allowBlank: true
						            }, {
						            	fieldLabel:'域',
						             	name: 'domainName'
						            }, {
						            	fieldLabel: '邮件服务器要求身份验证',
						            	xtype:'checkbox',
						            	name: 'auth',
						            	editable: true,
						            	disabled: false,
						            	checked: true,
						            	listeners: {
						            		change: function(){
						            			if(this.getValue()){
						            				var target = this.up('form').down('[name=defaultSenderUserName]');
						            				target.clearInvalid();
						            				target.setHidden(false);
						            				target.enable();
						            				target = this.up('form').down('[name=defaultSenderPassword]');
						            				target.clearInvalid();
						            				target.setHidden(false);
						            				target.enable();
						            			}else{
						            				var target = this.up('form').down('[name=defaultSenderUserName]');
						            				target.clearInvalid();
						            				target.setHidden(true);
						            				target.disable();
						            				target = this.up('form').down('[name=defaultSenderPassword]');
						            				target.clearInvalid();
						            				target.setHidden(true);
						            				target.disable();
						            			}
						            		}
						            	}
						            }, {
						             	fieldLabel:'用户名',
						            	name: 'defaultSenderUserName',
						            	listeners: [{
						            		blur: function(obj){
						            			var target = newWin.down('[name=mailFile]');
						            			target.setValue('mail\\\\' + obj.getValue() + ".nsf");
						            		}
						            	}]
						            }, {
						            	fieldLabel:'密码',
						            	name: 'defaultSenderPassword',
						            	inputType: 'password'
						            }, {
						            	fieldLabel:'邮件文件在服务器上的路径',
						             	name: 'mailFile',
						             	value: 'mail\\\\'
						            }, {
						            	fieldLabel: '测试邮件地址',
						            	name: 'testmail',
						            	vtype: 'email',
						            	hidden: false,
						            	disabled: false
						            }]
					            }]
			            	}]
			            }]
			        }],
					buttons : [{
						text : '取消',
						handler : function() {
							newWin.down('[xtype=form]').reset();
							newWin.hide();
						}
					},{
			        	text: '重置',
			        	handler: function(){
			        		newWinReset();
			        	}
			        }, {
			        	text: '提交',
			        	handler: function(){
			        		var form = newWin.down('form').getForm();
			        		if(form.isValid()){
			        			var mask = new Ext.LoadMask({
			        			    msg    : '请稍等...',
			        			    target : newWin
			        			});
			        			mask.show();
			        			form.submit({
			        				url:	'mailsetup',
			        				method:	'post',
			        				failure: function(form, action){
			        					mask.destroy();
			        					Ext.Msg.alert("验证失败", action && action.result && action.result.message ? action.result.message : '网络超时');
			        				},
			        				success: function(form, action){
			        					newWinReset();
										newWin.hide();
										mask.destroy();
										msstore.reload();
			        				}
			        			} );
			        		}
			        	}
			        }],
			        listeners: {
			        	afterrender: function(){
			        		newWin.getEl().on({
			        			keyup: function(e){
			        				if(e.getKey() == Ext.event.Event.ENTER){
			        					e.preventDefault();
			        	        		var form = newWin.down('form').getForm();
			        	        		if(form.isValid()){
			        	        			var mask = new Ext.LoadMask({
			        	        			    msg    : '请稍等...',
			        	        			    target : newWin
			        	        			});
			        	        			mask.show();
			        	        			form.submit({
			        	        				url:	'mailsetup',
			        	        				method:	'post',
			        	        				failure: function(form, action){
			        	        					mask.destroy();
			        	        					Ext.Msg.alert(
			        	        							"验证失败", action && action.result && action.result.message ? action.result.message : '网络超时');
			        	        				},
			        	        				success: function(form, action){
			        	        					newWinReset();
			        								newWin.hide();
			        								mask.destroy();
			        								msstore.reload();
			        	        				}
			        	        			} );
			        	        		}
			        				}
			        			}
			        		});
			        	}
			        }
				}]
			});
	
	var newWinReset = function(){
		var updateflag = newWin.down('form').down('[name=id]').getValue().length > 0 ? true : false;
		newWin.down('[xtype=form]').reset();
		newWin.down('[id=radio3]').fireEvent('change');
		newWin.down('form').down('[itemId=setup-type-container]').setHidden(false);
		//set data if update
		if(updateflag && grid.getSelection().length > 0 && grid.getSelection()[0]){
			var record = grid.getSelection()[0];
			newWin.down('form').down('[name=id]').setValue(record.get('id'));
			newWin.down('form').down('[name=displayName]').setValue(record.get('displayName'));
			newWin.down('form').down('[itemId=setup-type-container]').setHidden(true);
			var serverType = record.get('serverType');
			newWin.down('form').down('[name=serverType]').setValue(serverType);
			newWin.down('form').down('[name=serverType]').fireEvent('select');
			if(serverType == 0){
				newWin.down('form').down('[name=smtpHost]').setValue(record.get('smtpHost'));
				newWin.down('form').down('[name=smtpPort]').setValue(record.get('smtpPort'));
				newWin.down('form').down('[name=ssl]').setValue(record.get('ssl'));
				newWin.down('form').down('[name=tls]').setValue(record.get('tls'));
				newWin.down('form').down('[name=defaultSenderAddress]').setValue(record.get('defaultSenderAddress'));
				newWin.down('form').down('[name=defaultSenderTitle]').setValue(record.get('defaultSenderTitle'));
			}
			if(serverType == 1){
				var connType = record.get('connType');
				newWin.down('form').down('[itemId=mail-conntype-container]').down('#radio4').setValue(connType == 0 ? true : false);
				newWin.down('form').down('[itemId=mail-conntype-container]').down('#radio5').setValue(connType == 1 ? true : false);
				newWin.down('form').down('[itemId=mail-conntype-container]').down('#radio4').fireEvent('change');
				newWin.down('form').down('[itemId=mail-conntype-container]').down('#radio5').fireEvent('change');
				if(connType == 0){
					newWin.down('form').down('[name=smtpHost]').setValue(record.get('smtpHost'));
					newWin.down('form').down('[name=smtpPort]').setValue(record.get('smtpPort'));
				}
				if(connType == 1){
					newWin.down('form').down('[name=ior]').setValue(record.get('ior'));
				}
			}
			if(serverType == 2){
				newWin.down('form').down('[name=smtpHost]').setValue(record.get('smtpHost'));
				newWin.down('form').down('[name=smtpPort]').setValue(record.get('smtpPort'));
				newWin.down('form').down('[name=ssl]').setValue(record.get('ssl'));
				newWin.down('form').down('[name=tls]').setValue(record.get('tls'));
				newWin.down('form').down('[name=defaultSenderAddress]').setValue(record.get('defaultSenderAddress'));
				newWin.down('form').down('[name=defaultSenderTitle]').setValue(record.get('defaultSenderTitle'));
				newWin.down('form').down('[name=domainName]').setValue(record.get('domainName'));
			}
			newWin.down('form').down('[name=auth]').setValue(record.get('auth'));
			newWin.down('form').down('[name=auth]').fireEvent('change');
			if(newWin.down('form').down('[name=auth]')){
				newWin.down('form').down('[name=defaultSenderUserName]').setValue(record.get('defaultSenderUserName'));
				if(serverType == 1){
					newWin.down('form').down('[name=mailFile]').setValue(record.get('mailFile'));
				}
			}
			newWin.down('form').down('[name=testmail]').setValue(record.get('defaultSenderAddress'));
		}
	};
	
    var grid = Ext.create('Ext.grid.Panel', {
        renderTo: document.body,
        frame:true,
        autoWidth: true,
        viewConfig: {
            forceFit: true
        },
        frame: true,
        title: '邮件服务器列表',
        store: msstore,
        iconCls: 'icon-title',
        columns: [{
            text: 'ID',
            width: 50,
            sortable: true,
            dataIndex: 'id'
        }, {
            text: '显示名称',
            flex: 2,
            sortable: true,
            dataIndex: 'displayName'
        }, {
            text: '服务器类型',
            width: 120,
            sortable: true,
            dataIndex: 'serverType',
            renderer: function(v){
            	return mstypestore.findRecord('value',v).get('name');
            }
        }, {
            text: 'SMTP地址',
            flex: 1,
            sortable: true,
            dataIndex: 'smtpHost'
        }, {
            text: 'SMTP端口',
            width: 120,
            sortable: true,
            dataIndex: 'smtpPort'
        }, {
            text: '需要认证',
            width: 120,
            sortable: true,
            dataIndex: 'auth',
            renderer: function(v){
            	return v == true ? '<div class="grid-status-div grid-status-yes"></div>' : '<div class="grid-status-div">-</div>';
            }
        }, {
            text: 'SSL',
            sortable: true,
            dataIndex: 'ssl',
            renderer: function(v){
            	return v == true ? '<div class="grid-status-div grid-status-yes"></div>' : '<div class="grid-status-div">-</div>';
            }
        }, {
            text: 'TLS',
            sortable: true,
            dataIndex: 'tls',
            renderer: function(v){
            	return v == true ? '<div class="grid-status-div grid-status-yes"></div>' : '<div class="grid-status-div">-</div>';
            }
        }, {
            text: '默认发件地址',
            width: 200,
            sortable: true,
            dataIndex: 'defaultSenderAddress'
        }, {
            text: '默认发件人显示名称',
            width: 120,
            sortable: true,
            dataIndex: 'defaultSenderTitle'
        }, {
            text: '创建时间',
            width: 200,
            sortable: true,
            dataIndex: 'createTime'
        }],
        dockedItems: [{
            xtype: 'toolbar',
            items: [{
            	id: 'grid-add-button',
                text: '添加邮件服务器信息',
                iconCls: 'icon-add',
                handler: function(){
                	var position = ((Ext.getBody().getWidth()/2) - 500);
                	newWin.showAt(position < 0 ? 0 : position , 0);
                	newWin.down('form').down('[name=id]').setValue('');
                	newWinReset();
                	newWin.down('[id=radio3]').fireEvent('change');
                }
            }, '-', {
            	text: '编辑',
            	itemId: 'grid-mail-edit-button',
            	iconCls: 'icon-edit',
            	disabled: true,
            	handler: function(){
            		if(grid.getSelection().length > 0){
            			var position = ((Ext.getBody().getWidth()/2) - 500);
            			newWin.showAt(position < 0 ? 0 : position , 0);
            			newWin.down('form').down('[name=id]').setValue(grid.getSelection()[0].get('id'));
            			newWinReset();
            		}
            	}
            }, {
            	text: '发送限制',
            	itemId: 'grid-mail-regular-button',
            	iconCls: 'icon-regular',
            	disabled: true,
            	handler: function(){
            		if(grid.getSelection().length > 0){
            			regularWindow.show();
            			regularWindow.down('[name=cycle]').setValue(grid.getSelection()[0].get('limitCycle'));
            			regularWindow.down('[name=count]').setValue(grid.getSelection()[0].get('limitCount'));
            		}
            	}
            },'-', {
                text: '删除',
                itemId: 'grid-mail-delete-button',
                iconCls: 'icon-delete',
                disabled: true,
                handler: function(){
                	if(grid.getSelection().length > 0){
                		Ext.Msg.show({
    						title: '提示', 
    						message: '请确保所有的计划任务(包括未执行的计划任务在内)都不再使用此配置信息，否则无法删除',
    						buttons: Ext.Msg.OKCANCEL,
    						icon: Ext.Msg.QUESTION,
    						buttonText : {
								ok : '确定',
								cancel : '取消'
							},
    						fn: function(btn){
            					if(btn === 'ok'){
            						var mask = new Ext.LoadMask({
                            			msg    : '请稍等...',
                            			target : grid
                            		});
                            		mask.show();
            						Ext.Ajax.request({
            							url: 'remove',
            							params: {id: grid.getSelection()[0].get('id')},
            							success: function(response){
            								mask.destroy();
            								var result = Ext.decode(response.responseText);
            								if(result){
            									if(result.success){
            										msstore.reload();
            									} else {
            										Ext.Msg.alert("验证失败", result && result.message ? result.message : '网络超时');
            									}
            								}
            							},
            			    			failure: function(){
            			    				mask.destroy();
            			    			}
            						});
            					}
    						}
					});
                	}
                }
            }, '-', {
            	text: '刷新',
            	iconCls: 'icon-refresh',
            	handler: function(){
            		msstore.reload();
            	}
            }]
        }],
        listeners: {
        	rowdblclick: function(obj, record){
        		if(grid.getSelection().length > 0){
        			var position = ((Ext.getBody().getWidth()/2) - 500);
        			newWin.showAt(position < 0 ? 0 : position , 0);
        			newWin.down('form').down('[name=id]').setValue(grid.getSelection()[0].get('id'));
        			newWinReset();
        		}
        	}
        }
    });
    
    grid.getSelectionModel().on('selectionchange', function(selModel, selections){
    	setgridbutton();
    });
    
    var setgridbutton = function(){
    	grid.down('[itemId=grid-mail-edit-button]').setDisabled(grid.getSelection().length === 0);
        grid.down('[itemId=grid-mail-regular-button]').setDisabled(grid.getSelection().length === 0);
        grid.down('[itemId=grid-mail-delete-button]').setDisabled(grid.getSelection().length === 0);
    }
    
});