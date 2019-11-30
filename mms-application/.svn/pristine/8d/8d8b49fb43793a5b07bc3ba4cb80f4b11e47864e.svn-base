Ext.require(['Ext.data.*', 'Ext.grid.*']);

Ext.define('datasource', {
    extend: 'Ext.data.Model',
    fields: [{
        name: 'id',
        type: 'int'
    }, 
    'displayName',
    'sqlType',
    'host',
    'port',
    'dbName',
    'archName',
    'authType',
    'username',
    'status',
    'updateTime',
    'createTime']
    
});

Ext.onReady(function(){

    var dsstore = Ext.create('Ext.data.JsonStore', {
        autoLoad: true,
        autoSync: true,
        model: 'datasource',
        proxy: {
            type: 'ajax',
            url: '/dscList'
        },
        listeners: {
        	endupdate: function(){
        		setgridbutton();
        	}
        }
    });
    
    var dbTypeStore = Ext.create('Ext.data.Store', {
        fields: ['type', 'name', 'port'],
        data : [
            {"type":"1", "name":"ORACLE", "port":1521},
            {"type":"2", "name":"SQL SERVER", "port":1433}
        ]
    });
    
    var authTypeStore = Ext.create('Ext.data.Store', {
        fields: ['type', 'name'],
        data : [
            {"type":"1", "name":"Windows 身份验证"},
            {"type":"2", "name":"SQL Server 身份验证"}
        ]
    });
    
	var newWin = Ext.create('Ext.window.Window',
			{
				title : '新建数据源',
				minWidth : 200,
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
						align : 'stretch'
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
			                msgTarget: 'side',
			                labelAlign: 'right',
			                labelWidth: 150
			            },
			            items:[{
			            	xtype: 'hiddenfield',
			            	name: 'id',
			            	allowBlank: true,
			            }, {
			             	fieldLabel:'显示名称',
			            	name: 'displayName'
			            }, {
			            	xtype: 'combobox',
			            	fieldLabel:'数据库类型',
			             	name: 'sqlType',
			             	editable: false,
			             	store: dbTypeStore,
			                queryMode: 'local',
			                displayField: 'name',
			                valueField: 'type',
			                listeners: {
			                	select: function(){
			                		var record = dbTypeStore.findRecord('type',newWin.down('[name=sqlType]').getValue());
			                		newWin.down('[name=port]').setValue(record.get('port'));
			                		if(record.get('type') == 2){
			                			var target = newWin.down('[name=archName]');
			                			target.setHidden(false);
			                			target.enable();
			                			target.clearInvalid();
			                			target = newWin.down('[name=authType]');
			                			target.setHidden(false);
			                			target.enable();
			                			target.clearInvalid();
			                			displayFields();
			                		}else{
			                			var target = newWin.down('[name=archName]');
			                			target.setHidden(true);
			                			target.disable();
			                			target.clearInvalid();
			                			target = newWin.down('[name=authType]');
			                			target.setHidden(true);
			                			target.disable();
			                			target.clearInvalid();
			                			if(record.get('type') == 1){
			                				target = newWin.down('[name=username]');
			                    			target.setHidden(false);
			                    			target.enable();
			                    			target.clearInvalid();
			                    			target = newWin.down('[name=password]');
			                    			target.setHidden(false);
			                    			target.enable();
			                    			target.clearInvalid();
			                			}
			                		}
			                	}
			                }
			            }, {
			             	fieldLabel:'服务器地址',
			            	name: 'host'
			            }, {
			            	xtype: 'numberfield',
			            	fieldLabel:'端口号',
			            	name: 'port',
			            	minValue: 0,
			            	hideTrigger: true,
			                keyNavEnabled: false,
			                mouseWheelEnabled: false
			            }, {
			            	fieldLabel: '数据库名',
			            	name: 'dbName'
			            }, {
			            	fieldLabel: '数据库架构名',
			            	name: 'archName',
			            	value: 'dbo',
			            	hidden: true
			            }, {
			            	xtype: 'combobox',
			            	fieldLabel: '身份验证方式',
			            	store: authTypeStore,
			            	editable: false,
			            	name: 'authType',
			            	queryMode: 'local',
			            	displayField: 'name',
			            	valueField: 'type',
			            	hidden: true,
			            	listeners: {
			            		select: function(combo, record){
			            			displayFields();
			            		}
			            	}
			            }, {
			            	fieldLabel: '用户名',
			            	name: 'username',
			            	hidden: true
			            }, {
			            	fieldLabel: '密码',
			            	name: 'password',
			            	inputType: 'password',
			            	hidden: true
			            }]
			        }],
					buttons : [{
						text : '取消',
						handler : function() {
							newWin.hide();
							resetnewWin();
						}
					},{
			        	text: '重置',
			        	handler: function(){
			        		resetnewWin();
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
			        				url:	'dssetup',
			        				method:	'post',
			        				failure: function(form, action){
			        					mask.destroy();
			        					Ext.Msg.alert("验证失败", action && action.result && action.result.message ? action.result.message : '网络超时');
			        				},
			        				success: function(form, action){
			        					resetnewWin();
										newWin.hide();
										mask.destroy();
										dsstore.reload();
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
			        	        				url:	'dssetup',
			        	        				method:	'post',
			        	        				failure: function(form, action){
			        	        					mask.destroy();
			        	        					Ext.Msg.alert("验证失败", action && action.result && action.result.message ? action.result.message : '网络超时');
			        	        				},
			        	        				success: function(form, action){
			        	        					resetnewWin();
			        								newWin.hide();
			        								mask.destroy();
			        								dsstore.reload();
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
	
	var resetnewWin = function(){
		var updateflag = newWin.down('form').down('[name=id]').getValue().length > 0 ? true : false;
		newWin.down('[xtype=form]').reset();
		var target = newWin.down('form').down('[name=archName]');
		target.setHidden(true);
		target.disable();
		target.clearInvalid();
		target = newWin.down('form').down('[name=authType]');
		target.setHidden(true);
		target.disable();
		target.clearInvalid();
		target = newWin.down('form').down('[name=username]');
		target.setHidden(true);
		target.disable();
		target.clearInvalid();
		target = newWin.down('form').down('[name=password]');
		target.setHidden(true);
		target.disable();
		target.clearInvalid();
		
		if(updateflag && grid.getSelection().length > 0 && grid.getSelection()[0]){
			var record = grid.getSelection()[0];
			newWin.down('form').down('[name=id]').setValue(record.get('id'));
			newWin.down('form').down('[name=displayName]').setValue(record.get('displayName'));
			var sqlType = record.get('sqlType');
			newWin.down('form').down('[name=sqlType]').setValue(sqlType);
			newWin.down('form').down('[name=sqlType]').fireEvent('select');
			newWin.down('form').down('[name=host]').setValue(record.get('host'));
			newWin.down('form').down('[name=port]').setValue(record.get('port'));
			newWin.down('form').down('[name=dbName]').setValue(record.get('dbName'));
			if(sqlType == 1){
				newWin.down('form').down('[name=username]').setValue(record.get('username'));
			}
			if(sqlType == 2){
				newWin.down('form').down('[name=archName]').setValue(record.get('archName'));
				var authType = record.get('authType');
				newWin.down('form').down('[name=authType]').setValue(authType);
				newWin.down('form').down('[name=authType]').fireEvent('select');
				if(authType == 2){
					newWin.down('form').down('[name=username]').setValue(record.get('username'));
				}
			}			
		}
	}
    
    var displayFields = function(){
    	var value = newWin.down('[name=authType]').getValue();
    	if(value != null && value == 2){
			var target = newWin.down('[name=username]');
			target.setHidden(false);
			target.enable();
			target.clearInvalid();
			target = newWin.down('[name=password]');
			target.setHidden(false);
			target.enable();
			target.clearInvalid();
		}else{
			var target = newWin.down('[name=username]');
			target.setHidden(true);
			target.disable();
			target.clearInvalid();
			target = newWin.down('[name=password]');
			target.setHidden(true);
			target.disable();
			target.clearInvalid();
		}
    }
    
    var grid = Ext.create('Ext.grid.Panel', {
        renderTo: document.body,
        frame:true,
        autoWidth: true,
        viewConfig: {
            forceFit: true
        },
        frame: true,
        title: '数据源列表',
        store: dsstore,
        iconCls: 'icon-title',
        columns: [{
            text: 'ID',
            width: 50,
            sortable: true,
            dataIndex: 'id'
        },  {
            text: '显示名称',
            flex: 1,
            width: 120,
            sortable: true,
            dataIndex: 'displayName'
        }, {
            text: '数据源类型',
            sortable: true,
            dataIndex: 'sqlType',
            renderer: function(v){
            	return dbTypeStore.findRecord('type',v).get('name');
            }
        }, {
            text: '服务器地址',
            width: 120,
            sortable: true,
            dataIndex: 'host'
        }, {
            text: '端口号',
            width: 120,
            sortable: true,
            dataIndex: 'port'
        }, {
            text: '数据库名',
            width: 120,
            sortable: true,
            dataIndex: 'dbName'
        }, {
            text: '架构名',
            width: 120,
            sortable: true,
            dataIndex: 'archName'
        }, {
            text: '认证方式',
            width: 200,
            sortable: true,
            dataIndex: 'authType',
            renderer: function(v){
            	return authTypeStore.findRecord('type',v).get('name');
            }
        }, {
            text: '用户名',
            width: 120,
            sortable: true,
            dataIndex: 'username'
        }, {
        	text: '最后更新时间',
            width: 200,
            sortable: true,
            dataIndex: 'updateTime'
        }, {
            text: '创建时间',
            width: 200,
            sortable: true,
            dataIndex: 'createTime'
        }],
        dockedItems: [{
            xtype: 'toolbar',
            items: [{
                text: '新建数据源',
                itemId: 'grid-ds-new-button',
                iconCls: 'icon-add',
                handler: function(){
                	var position = ((Ext.getBody().getWidth()/2) - 500);
        			newWin.showAt(position < 0 ? 0 : position , 0);
                	newWin.down('form').down('[name=id]').setValue('');
            		resetnewWin();
                }
            }, {
            	text: '编辑',
            	itemId: 'grid-ds-edit-button',
            	iconCls: 'icon-edit',
            	disabled: true,
            	handler: function(){
            		var position = ((Ext.getBody().getWidth()/2) - 500);
        			newWin.showAt(position < 0 ? 0 : position , 0);
            		newWin.down('form').down('[name=id]').setValue(grid.getSelection()[0].get('id'));
            		resetnewWin();
            	}
            },'-', {
                text: '删除',
                itemId: 'grid-ds-delete-button',
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
            										dsstore.reload();
            									} else {
            										Ext.Msg.alert("验证失败", result && result.message ? result.message : '网络超时');
            									}
            								}
            							},
            			    			failure: function(response){
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
            		dsstore.reload();
            	}
            }]
        }],
        listeners: {
        	rowdblclick: function(obj, record){
        		if(grid.getSelection().length > 0){
        			var position = ((Ext.getBody().getWidth()/2) - 500);
        			newWin.showAt(position < 0 ? 0 : position , 0);
            		newWin.down('form').down('[name=id]').setValue(grid.getSelection()[0].get('id'));
            		resetnewWin();
        		}
        	}
        }
    });
    grid.getSelectionModel().on('selectionchange', function(selModel, selections){
    	setgridbutton();
    });
    
    var setgridbutton = function(){
        grid.down('[itemId=grid-ds-edit-button]').setDisabled(grid.getSelection().length === 0);
        grid.down('[itemId=grid-ds-delete-button]').setDisabled(grid.getSelection().length === 0);
    }
    
});