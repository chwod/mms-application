Ext.require(['Ext.data.*', 'Ext.grid.*']);

Ext.define('shortMessageConfiguration', {
    extend: 'Ext.data.Model',
    fields: [{
        name: 'id',
        type: 'int'
    }, 
    'displayName',
    'smTunnel',
    'smAccessNumber',
    'componyName',
    'applicationId',
    'applicationName',
    'extendCode',
    'serviceAddress',
    'serviceType',
    'limitCycle',
    'limitCount',
    'status',
    'createTime',
    'updateTime']
});

Ext.onReady(function(){

    var smstore = Ext.create('Ext.data.JsonStore', {
        autoLoad: true,
        model: 'shortMessageConfiguration',
        proxy: {
            type: 'ajax',
            url: '/smcList'
        },
        listeners: {
        	endupdate: function(){
        		setgridbutton();
        	}
        }
    });
    
    var smtstore = Ext.create('Ext.data.JsonStore', {
        fields: ['key', 'val'],
        autoLoad: true,
        proxy: {
        	type:	'ajax',
        	url:	'smtList'
        }
    });
    
    var ststore = Ext.create('Ext.data.Store', {
		fields: ['value', 'name'],
		data: [
		        {value: '1', name: 'Web Service'},
		]
	});
    
    var regularWindow = new Ext.window.Window(
    		{
    			title : '短信发送限制',
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
    										smstore.reload();
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
				title : '短信通道配置',
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
			                hidden: false,
			                disabled: false,
			                msgTarget: 'side',
			                labelAlign: 'right',
			            },
			            items:[{
			            	xtype: 'hiddenfield',
			            	name: 'id',
			            	hidden: true,
			            	allowBlank: true
			            }, {
			             	fieldLabel:'显示名称',
			            	name: 'displayName'
			            }, {
			             	fieldLabel:'集团／公司名称',
			            	name: 'componyName',
			            }, {
			            	fieldLabel: '短信通道类型',
			            	xtype: 'combo',
			            	store: smtstore,
			            	queryMode: 'local',
			                displayField: 'key',
			                valueField: 'val',
			                editable: false,
			            	name: 'smTunnel'
			            }, {
			             	fieldLabel:'接入号',
			            	name: 'smAccessNumber',
			            	allowBlank: true
			            }, {
			             	fieldLabel:'应用名称',
			             	allowBlank: true,
			            	name: 'applicationName'
			            }, {
			             	fieldLabel:'应用ID',
			            	name: 'applicationId'
			            }, {
			             	fieldLabel:'应用密码',
			             	inputType: 'password',
			            	name: 'applicationPassword'
			            }, {
			             	fieldLabel:'扩展号',
			            	name: 'extendCode',
			            	allowBlank: true,
			            	maxLength: 4,
			            	regex: /\d{0,4}/
			            }, {
			            	fieldLabel: '接口方式',
			            	xtype: 'radiogroup',
			            	itemId: 'setup-type-container',
			            	layout: 'hbox',
			            	items: [{
		            			id: 'radio-ws', 
		            			boxLabel: 'Web Service', 
		            			name: 'serviceType',
		            			inputValue: 'W',
		            			checked: true
			            	}]
			            }, {
			             	fieldLabel:'接口地址',
			            	name: 'serviceAddress'
			            }, {
			             	fieldLabel:'测试手机号',
			            	name: 'testPhoneNumber',
			            	regex: /1\d{10}/,
			            	maxLength: 11,
			            	minLength: 11,
			            	regexText: '仅支持以1开头11位中国大路手机号'
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
			        				url:	'smtsetup',
			        				method:	'post',
			        				failure: function(form, action){
			        					mask.destroy();
			        					Ext.Msg.alert("验证失败", action && action.result && action.result.message ? action.result.message : '网络超时');
			        				},
			        				success: function(form, action){
			        					newWinReset();
										newWin.hide();
										mask.destroy();
										smstore.reload();
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
			        	        				url:	'smtsetup',
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
			        								smstore.reload();
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
		//set data if update
		if(updateflag && grid.getSelection().length > 0 && grid.getSelection()[0]){
			var record = grid.getSelection()[0];
			newWin.down('form').down('[name=id]').setValue(record.get('id'));
			newWin.down('form').down('[name=displayName]').setValue(record.get('displayName'));
			newWin.down('form').down('[name=componyName]').setValue(record.get('componyName'));
			newWin.down('form').down('[name=smTunnel]').setValue(record.get('smTunnel'));
			newWin.down('form').down('[name=smAccessNumber]').setValue(record.get('smAccessNumber'));
			newWin.down('form').down('[name=applicationName]').setValue(record.get('applicationName'));
			newWin.down('form').down('[name=applicationId]').setValue(record.get('applicationId'));
			newWin.down('form').down('[name=extendCode]').setValue(record.get('extendCode'));
			newWin.down('form').down('[name=serviceType]').setValue(record.get('serviceType'));
			newWin.down('form').down('[name=serviceAddress]').setValue(record.get('serviceAddress'));
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
        title: '短信通道列表',
        store: smstore,
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
            text: '集团／公司名称',
            width: 120,
            sortable: true,
            dataIndex: 'componyName'
        }, {
        	text: '接入号',
        	width: 120,
        	sortable: true,
        	dataIndex: 'smAccessNumber'
        }, {
            text: '应用名称',
            flex: 1,
            sortable: true,
            dataIndex: 'applicationName'
        }, {
            text: '应用账号',
            width: 120,
            sortable: true,
            dataIndex: 'applicationId'
        }, {
            text: '扩展号',
            width: 120,
            sortable: true,
            dataIndex: 'extendCode'
        }, {
            text: '接口方式',
            sortable: true,
            dataIndex: 'serviceType',
            renderer: function(v){
            	return v == 'W' ? 'Web Service' : '';
            }
        },{
            text: '最后更新时间',
            width: 200,
            sortable: true,
            dataIndex: 'updateTime'
        },{
            text: '创建时间',
            width: 200,
            sortable: true,
            dataIndex: 'createTime'
        }],
        dockedItems: [{
            xtype: 'toolbar',
            items: [{
            	id: 'grid-add-button',
                text: '新建短信通道',
                iconCls: 'icon-add',
                handler: function(){
                	var position = ((Ext.getBody().getWidth()/2) - 500);
        			newWin.showAt(position < 0 ? 0 : position , 0);
                	newWin.down('form').down('[name=id]').setValue('');
                	newWinReset();
                }
            }, '-', {
            	text: '编辑',
            	itemId: 'grid-sm-edit-button',
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
            	itemId: 'grid-sm-regular-button',
            	iconCls: 'icon-regular',
            	disabled: true,
            	handler: function(){
            		if(grid.getSelection().length > 0){
            			regularWindow.show();
            			regularWindow.down('[name=cycle]').setValue(grid.getSelection()[0].get('limitCycle'));
            			regularWindow.down('[name=count]').setValue(grid.getSelection()[0].get('limitCount'));
            		}
            	}
            }, '-', {
                text: '删除',
                itemId: 'grid-sm-delete-button',
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
            										smstore.reload();
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
            		smstore.reload();
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
    	grid.down('[itemId=grid-sm-edit-button]').setDisabled(grid.getSelection().length === 0);
    	grid.down('[itemId=grid-sm-regular-button]').setDisabled(grid.getSelection().length === 0);
        grid.down('[itemId=grid-sm-delete-button]').setDisabled(grid.getSelection().length === 0);
    }
    
});