Ext.require(['Ext.data.*', 'Ext.grid.*']);

Ext.define('schedule', {
    extend: 'Ext.data.Model',
    fields: [{
        name: 'id',
        type: 'int',
        convert: null
    }, 
    'displayName',
    'actionType',
    'scheduleType',
    'actionDisplayName',
    'msid',
    'sendConditionList',
    'dsid',
    'smid',
    'attachmentAsContent',
    'url',
    'command',
    'commandVariable',
    'sqlSentence',
    'scheduleStatus',
    'cronExpression',
    'delayTime',
    'delayDate',
    'description',
    'status',
    'updateTime',
    'createTime']
});

Ext.define('sendCondition',{
	extend: 'Ext.data.Model',
	fields: [{name: 'id', type: 'int', convert: null},
	    	   {name: 'displayName'},
	    	   {name: 'foundType', type: 'int', convert: null},
	    	   {name: 'handlerId', type: 'int', convert: null},
	    	   {name: 'description'},
	    	   {name: 'operationList'}
	    	   ]
});

Ext.define('sendConditionOperation',{
	extend: 'Ext.data.Model',
	fields: [{name: 'id', type: 'int', convert: null},
	         {name: 'option', type: 'int', convert: null},
	         {name: 'operation', type: 'int', convert: null},
	         {name: 'val', type: 'int', convert: null},
	         {name: 'value'},
	         {name: 'begin'},
	         {name: 'end'},
	         {name: 'caseInsensitive'}
	         ]
});

var setcron;
var closecron;

Ext.onReady(function(){

    var schedulestore = Ext.create('Ext.data.JsonStore', {
        autoLoad: true,
        autoSync: true,
        model: 'schedule',
        proxy: {
            type: 'ajax',
            url: 'list'
        },
        listeners: {
        	endupdate: function(){
        		 setgridbutton();
        	}
        }
    });
    
    var actionStore = Ext.create('Ext.data.JsonStore',{
    	autoLoad: true,
    	fields: ['title', 'val'],
    	proxy: {
    		type: 'ajax',
    		url: 'normalScheduleActionList'
    	}
    });
    
    var dsStore = Ext.create('Ext.data.JsonStore',{
    	autoLoad: true,
    	fields: ['key', 'val'],
    	proxy: {
    		type: 'ajax',
    		url: '/dsList'
    	}
    });
    
    var msStore = Ext.create('Ext.data.JsonStore',{
    	autoLoad: true,
    	fields: ['key', 'val'],
    	proxy: {
    		type: 'ajax',
    		url: '/msList'
    	},
    	listeners: [{
    		endupdate: function(){
    			mseStore.add(Ext.create('Ext.data.Model',{key: '--无--', val: -99}));
    			if(msStore.getCount() > 0){
    				for(var i=0;i<msStore.getCount();i++){
    					var item = msStore.getAt(i);
    					mseStore.add(Ext.create('Ext.data.Model',{key: item.get('key'), val: item.get('val')}));
    				}
    			}
    		}
    	}]
    });
    
    var mseStore = Ext.create('Ext.data.JsonStore',{
    	autoLoad: true,
    	fields: ['key', 'val']
    });
    
    var smStore = Ext.create('Ext.data.JsonStore',{
    	autoLoad: true,
    	fields: ['key', 'val'],
    	proxy: {
    		type: 'ajax',
    		url: '/smList'
    	},
    	listeners: [{
    		endupdate: function(){
    			smeStore.add(Ext.create('Ext.data.Model',{key: '--无--', val: -99}));
    			if(smStore.getCount() > 0){
    				for(var i=0;i< smStore.getCount();i++){
    					var item = smStore.getAt(i);
    					smeStore.add(Ext.create('Ext.data.Model',{key: item.get('key'), val: item.get('val')}));
    				}
    			}
    		}
    	}]
    });
    
    var smeStore = Ext.create('Ext.data.JsonStore',{
    	autoLoad: true,
    	fields: ['key', 'val']
    });
    
    var sendConditionStore = Ext.create('Ext.data.JsonStore',{
    	model: 'sendCondition',
    	listeners: [{
    		clear: function(){
    			sendConditionOperationStore.removeAll();
    		},
    		endupdate: function(){
    			sendConditionOperationStore.removeAll();
    			setNewWinGridOrderButton();
    		}
    	}]
    });
    
    var sendConditionOperationStore = Ext.create('Ext.data.JsonStore',{
    	model: 'sendConditionOperation',
    	listeners: [{
    		endupdate: function(){
        		setNewSendConditionWinGridOrderButton();
            }
    	}]
    });
    
    var optionstore = Ext.create('Ext.data.JsonStore',{
    	autoLoad: true,
    	fields: ['name','code',
    	         {name: 'value', type: 'int', convert: null},
    	         'parentCode',
    	         'description'
    	         ],       
    	proxy: {
    		type: 'ajax',
    		url: 'condition/list'
    	}
    });
    
    var operationstore = Ext.create('Ext.data.JsonStore',{
    	autoLoad: true,
    	fields: ['name','code','value','parentCode','description'],
    	proxy: {
    		type: 'ajax',
    		url: 'condition/operation/list'
    	}
    });
    
    var newOperationWin = Ext.create('Ext.window.Window',{
    	title: '新建过滤条件',
    	minWidth: 200,
    	layout: 'fit',
    	resizable: false,
    	modal: true,
    	closable: false,
    	iconCls: 'icon-new',
    	closeAction: 'hide',
    	onEsc: Ext.emptyFn,
    	animateTarget: 'grid-send-condition-operation-add-button',
    	animateShadow: true,
    	border: false,
    	items: [{
    		xtype: 'form',
    		layout: {
    			type: 'vbox',
    			align: 'stretch'
    		},
    		border: false,
    		bodyPadding: 10,
    		fieldDefaults : {
				msgTarget : 'qtip',
				labelStyle : 'font-weight:bold'
			},
			defaults: {
				padding: '0 10 0 0',
				allowBlank: false,
				selectOnFocus: true,
				msgTarget: 'qtip',
				labelAlign: 'right',
				labelWidth: 150,
			},
			defaultType:'textfield',
			items: [{
            	xtype: 'fieldcontainer',
            	layout: 'vbox',
            	defaultType:'textfield',
            	defaults: {
            		labelAlign: 'top',
            		anchor: '100%',
                	allowBlank: false,
                	padding: '0 10 0 0',
    				selectOnFocus: true,
    				msgTarget: 'qtip',
    				labelWidth: 150,
    				width: 200
            	},
	            items: [{
	            	xtype: 'combobox',
	            	fieldLabel:'条件',
	            	name: 'option',
	            	store: optionstore,
	            	editable: false,
	                queryMode: 'local',
	                displayField: 'name',
	                valueField: 'value',
	                listeners: [{
	                	change: function(){
	                		operationstore.clearFilter();
	                		var optionRecord = optionstore.findRecord('value',newOperationWin.down('form').down('[name=option]').getValue());
                			if(optionRecord){
                				//show operation list
                				operationstore.clearFilter();
                				if(operationstore.find('parentCode',optionRecord.get('code')) > 0){
                					operationstore.filterBy(function(record){
        	                			var parentRecord = optionstore.findRecord('value',newOperationWin.down('form').down('[name=option]').getValue());
        	                			if(parentRecord){
        	                				return record.get('parentCode') == parentRecord.get('code');
        	                			}
        	                			return false;
        	                		});
                					
                					newOperationWin.down('form').down('[name=operation]').setHidden(false);
                					newOperationWin.down('form').down('[name=operation]').enable();
                					if(operationstore.find('value', newOperationWin.down('form').down('[name=operation]').getValue()) <= 0){
                						newOperationWin.down('form').down('[name=operation]').setValue(null);
                					}
                				} else {
                					newOperationWin.down('form').down('[name=operation]').setHidden(true);
                					newOperationWin.down('form').down('[name=operation]').disable();
                					newOperationWin.down('form').down('[name=value]').setHidden(true);
                					newOperationWin.down('form').down('[name=value]').disable();
                					newOperationWin.down('form').down('[name=val]').setHidden(true);
                					newOperationWin.down('form').down('[name=val]').disable();
                					newOperationWin.down('form').down('[itemId=beginContainer]').setHidden(true);
                					newOperationWin.down('form').down('[name=beginDate]').setHidden(true);
                					newOperationWin.down('form').down('[name=beginDate]').disable();
                					newOperationWin.down('form').down('[name=beginTime]').setHidden(true);
                					newOperationWin.down('form').down('[name=beginTime]').disable();
                					newOperationWin.down('form').down('[itemId=endContainer]').setHidden(true);
                					newOperationWin.down('form').down('[name=endDate]').setHidden(true);
                					newOperationWin.down('form').down('[name=endDate]').disable();
                					newOperationWin.down('form').down('[name=endTime]').setHidden(true);
                					newOperationWin.down('form').down('[name=endTime]').disable();
                					newOperationWin.down('form').down('[name=caseInsensitive]').setHidden(true);
                					newOperationWin.down('form').down('[name=caseInsensitive]').disable();
                					newOperationWin.down('form').down('[itemId=optionDescription]').setHidden(true);
                					newOperationWin.down('form').down('[itemId=optionDescription]').setValue('');
                					newOperationWin.down('form').down('[itemId=operationDescription]').setHidden(true);
                					newOperationWin.down('form').down('[itemId=operationDescription]').setValue('');
                				}
                				newOperationWin.down('form').getForm().clearInvalid();
                				//show description if necessary.
                				if(optionRecord.get('description') != null){
                					newOperationWin.down('form').down('[itemId=optionDescription]').setHidden(false);
                					newOperationWin.down('form').down('[itemId=optionDescription]').setValue(optionRecord.get('description'));
                				} else {
                					newOperationWin.down('form').down('[itemId=optionDescription]').setHidden(true);
                					newOperationWin.down('form').down('[itemId=optionDescription]').setValue('');
                				}
                			}
	                	}
	                }]
	            }, {
	            	xtype: 'combobox',
	            	fieldLabel:'操作',
	            	name: 'operation',
	            	store: operationstore,
	            	editable: false,
	                queryMode: 'local',
	                displayField: 'name',
	                valueField: 'value',
	                disabled: true,
	            	hidden: true,
	                listeners: [{
	                	change: function(){
	                		if(newOperationWin.down('form').down('[name=operation]').getValue() == null){
	                			return;
	                		}
	                		var record = operationstore.getAt(operationstore.findBy(function(record){
	                			return record.get('value') == newOperationWin.down('form').down('[name=operation]').getValue();
	                		}));
	                		if(!record){
	                			newOperationWin.down('form').down('[name=value]').setHidden(true);
            					newOperationWin.down('form').down('[name=value]').disable();
            					newOperationWin.down('form').down('[name=val]').setHidden(true);
            					newOperationWin.down('form').down('[name=val]').disable();
            					newOperationWin.down('form').down('[itemId=beginContainer]').setHidden(true);
            					newOperationWin.down('form').down('[name=beginDate]').setHidden(true);
            					newOperationWin.down('form').down('[name=beginDate]').disable();
            					newOperationWin.down('form').down('[name=beginTime]').setHidden(true);
            					newOperationWin.down('form').down('[name=beginTime]').disable();
            					newOperationWin.down('form').down('[itemId=endContainer]').setHidden(true);
            					newOperationWin.down('form').down('[name=endDate]').setHidden(true);
            					newOperationWin.down('form').down('[name=endDate]').disable();
            					newOperationWin.down('form').down('[name=endTime]').setHidden(true);
            					newOperationWin.down('form').down('[name=endTime]').disable();
            					newOperationWin.down('form').down('[name=caseInsensitive]').setHidden(true);
            					newOperationWin.down('form').down('[name=caseInsensitive]').disable();
	                			return;
	                		}
	                		var code = record.get('code');
	                		var target = newOperationWin.down('[name=value]');
	                		if(code == 'S'){
	                			target.enable();
	                			target.setHidden(false);
	                		} else {
	                			target.disable();
	                			target.setHidden(true);
	                		}
	                		target = newOperationWin.down('[name=val]');
	                		if(code == 'D'){
	                			target.enable();
	                			target.setHidden(false);
	                		} else {
	                			target.disable();
	                			target.setHidden(true);
	                		}
	                		target = newOperationWin.down('[itemId=beginContainer]');
	                		target.setHidden(code != 'T' && code != 'Z');
	                		target = newOperationWin.down('[name=beginDate]');
	                		if(code == 'T' || code == 'Z'){
	                			target.enable();
	                			target.setHidden(false);
	                		} else {
	                			target.disable();
	                			target.setHidden(true);
	                		}
	                		target = newOperationWin.down('[name=beginTime]');
	                		if(code == 'T' || code == 'Z'){
	                			target.enable();
	                			target.setHidden(false);
	                		} else {
	                			target.disable();
	                			target.setHidden(true);
	                		}
	                		target = newOperationWin.down('[itemId=endContainer]');
	                		target.setHidden(code != 'Z');
	                		target = newOperationWin.down('[name=endDate]');
	                		if(code == 'Z'){
	                			target.enable();
	                			target.setHidden(false);
	                		} else {
	                			target.disable();
	                			target.setHidden(true);
	                		}
	                		target = newOperationWin.down('[name=endTime]');
	                		if(code == 'Z'){
	                			target.enable();
	                			target.setHidden(false);
	                		} else {
	                			target.disable();
	                			target.setHidden(true);
	                		}
	                		target = newOperationWin.down('[name=caseInsensitive]');
	                		if(code == 'S'){
	                			target.enable();
	                			target.setHidden(false);
	                		} else {
	                			target.disable();
	                			target.setHidden(true);
	                		}
	                		if(record.get('description') != null){
            					newOperationWin.down('form').down('[itemId=operationDescription]').setHidden(false);
            					newOperationWin.down('form').down('[itemId=operationDescription]').setValue(record.get('description'));
            				} else {
            					newOperationWin.down('form').down('[itemId=operationDescription]').setHidden(true);
            					newOperationWin.down('form').down('[itemId=operationDescription]').setValue('');
            				}
	                	}
	                }]
	            }, {
	            	fieldLabel:'值',
	            	name: 'value',
	            	disabled: true,
	            	hidden: true
	            }, {
	            	xtype: 'numberfield',
	            	fieldLabel:'值',
	            	name: 'val',
	            	disabled: true,
	            	hidden: true
	            }, {
	            	xtype:'fieldcontainer',
	            	fieldLabel:'时间',
	            	itemId: 'beginContainer',
	            	width: 400,
	            	hidden: true,
	            	layout: {
	        			type: 'hbox',
	        			align: 'stretch'
	        		},
	            	defaults: {
	            		padding: '0 10 0 0',
	            		msgTarget: 'qtip',
	            		labelAlign: 'right',
	            		labelWidth: 150,
	            		selectOnFocus: true,
	                	anchor: '100%',
	                	allowBlank: false,
	                	disabled: true,
	                	hidden: true
	                },
	            	items: [ {
			            	xtype: 'datefield',
			            	name: 'beginDate',
			            	editable: false,
			            	format: 'Y-m-d'
			            },{
			            	xtype: 'timefield',
			            	name: 'beginTime',
			            	editable: false,
			            	allowBlank: true,
			            	format: 'H:i:s'
			            }]
	            }, {
	            	xtype:'fieldcontainer',
	            	fieldLabel:'结束时间',
	            	itemId: 'endContainer',
	            	hidden: true,
	            	width: 400,
	            	layout: {
	        			type: 'hbox',
	        			align: 'stretch'
	        		},
	            	defaults: {
	            		padding: '0 10 0 0',
	            		msgTarget: 'qtip',
	            		labelAlign: 'right',
	            		labelWidth: 150,
	            		selectOnFocus: true,
	                	anchor: '100%',
	                	allowBlank: false,
	                	disabled: true,
	                	hidden: true
	                },
	            	items: [ {
			            	xtype: 'datefield',
			            	name: 'endDate',
			            	editable: false,
			            	format: 'Y-m-d'
			            },{
			            	xtype: 'timefield',
			            	name: 'endTime',
			            	editable: false,
			            	allowBlank: true,
			            	format: 'H:i:s'
			            }]
	            }, {
	            	xtype: 'checkbox',
	            	fieldLabel:'忽略大小写',
	            	name: 'caseInsensitive',
	            	labelAlign: 'left',
	            	layout: {
	            		padding: '0 10 0 0',
	            	},
	            	labelWidth: 68, 
	            	disabled: true,
	            	hidden: true
	            }]
            },{
            	xtype: 'displayfield',
            	itemId: 'optionDescription',
            	hidden: true,
            	renderer: function(v){
            		return v ? ('<font color=red>*\ </font>' + v) : null;
            	}
            },{
            	xtype: 'displayfield',
            	itemId: 'operationDescription',
            	hidden: true,
            	renderer: function(v){
            		return v ? ('<font color=red>*\ </font>' + v) : null;
            	}
            }],
			buttons:[{
				text: '取消',
				handler: function(){
					newOperationWin.close();
					newOperationWin.record = false;
					newOperationWin.down('form').reset();
				}
			}, {
				text: '确定',
				itemId: 'submitButton',
				handler: function(){ 
					createOrUpdateOperation();
				}
			}]
    	}],
    	listeners: [{
    		beforeshow: function(){
    			optionstore.clearFilter();
    			optionstore.filterBy(function(record){
    				return record.get('parentCode') == (newWin.down('form').down('[name=actionType]').getValue() == 2 ? 'MS' : newWin.down('form').down('[name=actionType]').getValue() == 9 ? 'SM' : null);
    			});
    		},
    		afterrender: function(){
    			newOperationWin.getEl().on({
    				keyup: function(e){
    					if(e.getKey() == Ext.event.Event.ENTER){
    						e.preventDefault();
    						createOrUpdateOperation();
    					}
    				}
    			});
    		}
    	}]
    });
    
    var createOrUpdateOperation = function(){
		if(newOperationWin.down('form').isValid()){
			var flag = newOperationWin.record ? false : true;
			var record = newOperationWin.record ? newOperationWin.record : Ext.create('sendConditionOperation',{});
			record.set('option',newOperationWin.down('form').down('[name=option]').getValue())
			record.set('operation',newOperationWin.down('form').down('[name=operation]').getValue())
			var operationRecord = operationstore.findRecord('value',newOperationWin.down('form').down('[name=operation]').getValue());
			if(operationRecord){
				var code = operationRecord.get('code');
				if(code){
					if(code == 'S'){
						record.set('value',newOperationWin.down('form').down('[name=value]').getValue())
					}
					if(code == 'D'){
						record.set('val',newOperationWin.down('form').down('[name=val]').getValue())
					}
					if(code == 'T'){
						var begin = new Date(0);
						var beginDate = newOperationWin.down('form').down('[name=beginDate]').getValue();
						var beginTime = newOperationWin.down('form').down('[name=beginTime]').getValue();
						if(beginDate){
							begin.setFullYear(beginDate.getFullYear(), beginDate.getMonth(), beginDate.getDate());
							if(beginTime){
								begin.setHours(beginTime.getHours(), beginTime.getMinutes(), beginTime.getSeconds());
							} else {
								begin.setHours(0, 0, 0);
							}
						}
						record.set('begin', begin.getTime());
					}
					if(code == 'Z'){
						
						var begin = new Date(0);
						var beginDate = newOperationWin.down('form').down('[name=beginDate]').getValue();
						var beginTime = newOperationWin.down('form').down('[name=beginTime]').getValue();
						if(beginDate){
							begin.setFullYear(beginDate.getFullYear(), beginDate.getMonth(), beginDate.getDate());
							if(beginTime){
								begin.setHours(beginTime.getHours(), beginTime.getMinutes(), beginTime.getSeconds());
							} else {
								begin.setHours(0, 0, 0);
							}
						}
						
						var end = new Date(0);
						var endDate = newOperationWin.down('form').down('[name=endDate]').getValue();
						var endTime = newOperationWin.down('form').down('[name=endTime]').getValue();
						if(endDate){
							end.setFullYear(endDate.getFullYear(), endDate.getMonth(), endDate.getDate());
							if(endTime){
								end.setHours(endTime.getHours(), endTime.getMinutes(), endTime.getSeconds());
							} else {
								end.setHours(0, 0, 0);
							}
						}
						
						if(begin.getTime() > end.getTime()){
							Ext.toast({
					            html: '结束时间不能早于开始时间。',
					            closable: false,
					            align: 't',
					            slideInDuration: 400,
					        });
							return;
						}
						
						record.set('begin', begin.getTime());
						record.set('end', end.getTime());
					}
				}
			}
			record.set('caseInsensitive',newOperationWin.down('form').down('[name=caseInsensitive]').getValue())
			if(flag){
				sendConditionOperationStore.add(record);
			}
			
			newOperationWin.close();
			newOperationWin.record = false;
			newOperationWin.down('form').reset();
		}
	};
    
    var newSendConditionWin = Ext.create('Ext.window.Window', {
    	title: '新建规则',
    	minWidth: 200,
    	layout: 'fit',
    	resizable: false,
    	modal: true,
    	closable: false,
    	onEsc: Ext.emptyFn,
    	iconCls: 'icon-new',
    	closeAction: 'hide',
    	animateTarget: 'grid-send-condition-add-button',
    	animateShadow: true,
    	border: false,
    	items: [{
    		xtype: 'form',
    		layout: {
    			type: 'vbox'
    		},
    		border: false,
    		bodyPadding: 10,
    		fieldDefaults : {
				msgTarget : 'qtip',
				labelStyle : 'font-weight:bold'
			},
			defaultType:'textfield',
			defaults: {
				padding: '0 10 0 0',
				disabled: true,
            	allowBlank: false,
            	selectOnFocus: true,
				msgTarget: 'qtip',
				labelAlign: 'right',
				labelWidth: 150
			},

            items: [{
            	fieldLabel:'显示名称',
            	disabled: false,
            	name: 'displayName'
            }, {
            	xtype: 'fieldcontainer',
            	fieldLabel:'条件',
            	disabled: false,
            	itemId: 'grid-container',
            	width: 700,
            	layout: {
            		defaultMargins: {top: 0, right: 5, bottom: 0, left: 0}
            	},
            	defaults: {
                	anchor: '100%',
                	allowBlank: false,
                	selectOnFocus: true
                },
                items:[{
	            	xtype: 'grid',
	            	fieldLabel:'过滤条件',
	            	itemId: 'grid-send-condition-operation',
	            	store: sendConditionOperationStore,
	            	allowBlank: true,
	             	editable: false,
	             	loadMask: true,
	                tbar:[
	                    {
	                    	iconCls: 'icon-new',
	                    	text: '添加过滤条件',
	                    	tooltip: '创建过滤条件，然后选择使用哪个目标处理符合条件的任务。',
	                    	id: 'grid-send-condition-operation-add-button',
	                    	handler: function(){
	                    		newOperationWin.record = false;
	                    		newOperationWin.down('form').reset();
	                    		newOperationWin.down('form').down('[name=operation]').setHidden(true);
            					newOperationWin.down('form').down('[name=operation]').disable();
            					newOperationWin.down('form').down('[name=value]').setHidden(true);
            					newOperationWin.down('form').down('[name=value]').disable();
            					newOperationWin.down('form').down('[name=val]').setHidden(true);
            					newOperationWin.down('form').down('[name=val]').disable();
            					newOperationWin.down('form').down('[itemId=beginContainer]').setHidden(true);
            					newOperationWin.down('form').down('[name=beginDate]').setHidden(true);
            					newOperationWin.down('form').down('[name=beginDate]').disable();
            					newOperationWin.down('form').down('[name=beginTime]').setHidden(true);
            					newOperationWin.down('form').down('[name=beginTime]').disable();
            					newOperationWin.down('form').down('[itemId=endContainer]').setHidden(true);
            					newOperationWin.down('form').down('[name=endDate]').setHidden(true);
            					newOperationWin.down('form').down('[name=endDate]').disable();
            					newOperationWin.down('form').down('[name=endTime]').setHidden(true);
            					newOperationWin.down('form').down('[name=endTime]').disable();
            					newOperationWin.down('form').down('[name=caseInsensitive]').setHidden(true);
            					newOperationWin.down('form').down('[name=caseInsensitive]').disable();
            					newOperationWin.down('form').down('[itemId=optionDescription]').setHidden(true);
            					newOperationWin.down('form').down('[itemId=optionDescription]').setValue('');
            					newOperationWin.down('form').down('[itemId=operationDescription]').setHidden(true);
            					newOperationWin.down('form').down('[itemId=operationDescription]').setValue('');
	                    		newOperationWin.down('form').getForm().clearInvalid();
	                    		newOperationWin.show();
	                    	}
	                    },'-',{
                        	iconCls: 'icon-up',
                            text: '上移一行',
                            itemId: 'up',
                            handler: function() {
                            	var selection = newSendConditionWin.down('[itemId=grid-send-condition-operation]').getSelection();
                            	if(selection && selection[0]){
                            		selection = selection[0];
                            	}
                                sendConditionOperationStore.insert(sendConditionOperationStore.indexOf(selection)  - 1, selection);
                            }
                        },
                        {
                        	iconCls: 'icon-down',
                        	text: '下移一行',
                            itemId: 'down',
                            handler: function() {
                            	var selection = newSendConditionWin.down('[itemId=grid-send-condition-operation]').getSelection();
                            	if(selection && selection[0]){
                            		selection = selection[0];
                            	}
                            	sendConditionOperationStore.insert(sendConditionOperationStore.indexOf(selection) + 2, selection);
                            }
                        },
                        {
                        	iconCls: 'icon-up-top',
                        	text: '上移到第一行',
                            itemId: 'up-top',
                            handler: function() {
                            	var selection = newSendConditionWin.down('[itemId=grid-send-condition-operation]').getSelection();
                            	if(selection && selection[0]){
                            		selection = selection[0];
                            	}
                                sendConditionOperationStore.insert(0, selection);
                            }
                        },
                        {
                        	iconCls: 'icon-down-buttom',
                        	text: '下移到最后一行',
                            itemId: 'down-buttom',
                            handler: function() {
                            	var selection = newSendConditionWin.down('[itemId=grid-send-condition-operation]').getSelection();
                            	if(selection && selection[0]){
                            		selection = selection[0];
                            	}
                                sendConditionOperationStore.add(selection);
                            }
                        }
	                    ],
	                columns: [
	                    {text: '名称',  dataIndex: 'option', renderer: function(v){
	                    	if(v){
	                    		var record = optionstore.findRecord('value', v);
	                    		return record ? record.get('name') : '';
	                    	}
	                    	return null;
	                    }},
	                    {text: '操作',  dataIndex: 'operation', flex: 1 , renderer: function(v){
	                    	if(v){
	                    		var record = operationstore.findRecord('value', v);
	                    		return record ? record.get('name') : '';
	                    	}
	                    	return null;
	                    }},
	                    {text: '值', dataIndex: 'value', flex: 2, renderer: function(value, metadata, record){
	                    	if(record){
	                    		var operationRecord = operationstore.findRecord('value',record.get('operation'));
	                    		if(operationRecord){
	                    			var code = operationRecord.get('code');
	                    			if(code){
	                    				if(code == 'S' && record.get('value')){
	                    					return record.get('value');
	                    				}
	                    				if(code == 'D'){
	                    					return record.get('val') ? record.get('val') : 0;
	                    				}
	                    				if(code == 'T' && record.get('begin')){
	                    					var time = new Date(record.get('begin'));
	                    					return Ext.Date.format(time, 'Y-m-d H:i:s');
	                    				}
	                    				if(code == 'Z' && record.get('begin') && record.get('end')){
	                    					var begin = new Date(record.get('begin'));
	                    					var end = new Date(record.get('end'));
	                    					return Ext.Date.format(begin, 'Y-m-d H:i:s') + ' - ' + Ext.Date.format(end, 'Y-m-d H:i:s');
	                    				}
	                    			}
	                    		}
	                    	}
	                    	return null;
	                    }},
	                    {
	                    	xtype: 'actioncolumn',
	                    	menuDisabled: true,
	                    	sortable: false,
	                    	items:[
	                            {
	                            	iconCls: 'icon-condition-edit',
	                                tooltip: '编辑',
	                                handler: function(grid, rowIndex, colIndex) {
	                                    var record = grid.getStore().getAt(rowIndex);
	                                    if(record){
	                                    	newOperationWin.record = record;
	                                    	newOperationWin.down('form').reset();
	                                    	newOperationWin.show();
	                                    	newOperationWin.down('form').down('[name=option]').setValue(record.get('option'));
	                                    	newOperationWin.down('form').down('[name=operation]').setValue(record.get('operation'));
	                                    	var operationRecord = operationstore.findRecord('value',record.get('operation'));
	                                    	if(operationRecord){
	                                    		var code = operationRecord.get('code');
	                                    		if(code){
	                                    			if(code == 'S'){
	                                    				newOperationWin.down('form').down('[name=value]').setValue(record.get('value'));
	                                    			}
	                                    			if(code == 'D'){
	                                    				newOperationWin.down('form').down('[name=val]').setValue(record.get('val'));
	                                    			}
	                                    			if(code == 'T' || code == 'Z'){
	                                    				newOperationWin.down('form').down('[name=beginDate]').setValue(new Date(record.get('begin')));
	                                    				newOperationWin.down('form').down('[name=beginTime]').setValue(new Date(record.get('begin')));
	                                    			}
	                                    			if(code == 'Z'){
	                                    				newOperationWin.down('form').down('[name=endDate]').setValue(new Date(record.get('end')));
	                                    				newOperationWin.down('form').down('[name=endTime]').setValue(new Date(record.get('end')));
	                                    			}
	                                    		}
	                                    	}
	                                    	newOperationWin.down('form').down('[name=caseInsensitive]').setValue(record.get('caseInsensitive'));
	                                    }
	                                }
	                            },
	                            {
	                            	iconCls: 'icon-condition-delete',
	                                tooltip: '删除',
	                                handler: function(grid, rowIndex, colIndex) {
	                                    var record = grid.getStore().getAt(rowIndex);
	                                    sendConditionOperationStore.remove(record);
	                                }
	                            }
	                            ]
	                    },
	                ], 
	                listeners: [{
	                	afterrender: function(){
	                		setNewSendConditionWinGridOrderButton();
		                },
	                	selectionchange: function(){
	                		setNewSendConditionWinGridOrderButton();
	                	},
	                	rowdblclick: function(grid, r, tr, rowIndex, e, eOpts){
                            var record = grid.getStore().getAt(rowIndex);
                            if(record){
                            	newOperationWin.record = record;
                            	newOperationWin.down('form').reset();
                            	newOperationWin.show();
                            	newOperationWin.down('form').down('[name=option]').setValue(record.get('option'));
                            	newOperationWin.down('form').down('[name=operation]').setValue(record.get('operation'));
                            	var operationRecord = operationstore.findRecord('value',record.get('operation'));
                            	if(operationRecord){
                            		var code = operationRecord.get('code');
                            		if(code){
                            			if(code == 'S'){
                            				newOperationWin.down('form').down('[name=value]').setValue(record.get('value'));
                            			}
                            			if(code == 'D'){
                            				newOperationWin.down('form').down('[name=val]').setValue(record.get('val'));
                            			}
                            			if(code == 'T' || code == 'Z'){
                            				newOperationWin.down('form').down('[name=beginDate]').setValue(new Date(record.get('begin')));
                            				newOperationWin.down('form').down('[name=beginTime]').setValue(new Date(record.get('begin')));
                            			}
                            			if(code == 'Z'){
                            				newOperationWin.down('form').down('[name=endDate]').setValue(new Date(record.get('end')));
                            				newOperationWin.down('form').down('[name=endTime]').setValue(new Date(record.get('end')));
                            			}
                            		}
                            	}
                            	newOperationWin.down('form').down('[name=caseInsensitive]').setValue(record.get('caseInsensitive'));
                            }
                        }
	                }]
	            }]
            }, {
            	xtype: 'checkbox',
            	name: 'foundType',
            	disabled: false,
            	fieldLabel: '满足所有',
            	afterSubTpl:'勾选此项表示必须满足所有条件,否则满足任一条件即可',
            	value: true
            },{
            	xtype: 'combo',
            	fieldLabel:'请选择用于处理符合条件的邮件服务器配置信息',
            	name: 'msid',
             	editable: false,
             	hidden: true,
             	disabled: true,
                queryMode: 'local',
                displayField: 'key',
                valueField: 'val',
                store: msStore
            }, {
            	xtype: 'combo',
            	fieldLabel:'请选择用于处理符合条件的短信通道配置信息',
            	name: 'smid',
            	editable: false,
             	hidden: true,
             	disabled: true,
                queryMode: 'local',
                displayField: 'key',
                valueField: 'val',
                store: smStore
            }, {
            	fieldLabel:'描述',
            	xtype: 'textarea',
            	width: 500,
            	disabled: false,
            	allowBlank: true,
            	name: 'description'
            }],
			buttons: [{
				text: '取消',
				handler: function(){
					newSendConditionWin.record = false;
					newSendConditionWin.close();
					newSendConditionWin.down('form').reset();
				}
			},{
				text: '重置',
				handler: function(){
					newSendConditionWin.down('form').reset();
					sendConditionOperationStore.removeAll();
					if(newSendConditionWin.record){
						sendConditionOperationStore.loadData(newSendConditionWin.record.get('operationList'));
           			 	newSendConditionWin.down('form').down('[name=displayName]').setValue(newSendConditionWin.record.get('displayName'));
           			 	var value = newWin.down('[name=actionType]').getValue();
           			 	if(value == 2){
           			 		newSendConditionWin.down('form').down('[name=msid]').setValue(newSendConditionWin.record.get('handlerId'));
           			 	}
           			 	if(value == 9){
           			 		newSendConditionWin.down('form').down('[name=smid]').setValue(newSendConditionWin.record.get('handlerId'));
           			 	}
           			 	newSendConditionWin.down('form').down('[name=foundType]').setValue(newSendConditionWin.record.get('foundType'));
           			 	newSendConditionWin.down('form').down('[name=description]').setValue(newSendConditionWin.record.get('description'));
           			 	return;
					}
				}
			}, {
				text: '确定',
				handler: function(){
					createOrUpdateCondition();
				}
			}
			]
    	}],
    listeners: [{
    	beforeshow: function(){
    		sendConditionOperationStore.removeAll();
    		if(newSendConditionWin.record){
    			sendConditionOperationStore.loadRecords(newSendConditionWin.record.get('operationList'));
    		}
    	},
    	afterrender: function(){
    		newSendConditionWin.getEl().on({
    			keyup: function(e){
    				if(e.getKey() == Ext.event.Event.ENTER){
    					e.preventDefault();
    					createOrUpdateCondition();
    				}
    			}
    		});
    	}
    }]
    });
    
    var setNewSendConditionWinGridOrderButton = function(){
		if(newSendConditionWin){
    		var target = newSendConditionWin.down('form').down('[itemId=grid-send-condition-operation]');
    		var selection = target.getSelection();
    		if(selection.length > 0){
    			selection = selection[0];
    			target.down('[itemId=up]').enable();
				target.down('[itemId=up-top]').enable();
				target.down('[itemId=down]').enable();
				target.down('[itemId=down-buttom]').enable();
    			var index = sendConditionOperationStore.indexOf(selection);
    			if(index == 0){
    				target.down('[itemId=up]').disable();
    				target.down('[itemId=up-top]').disable();
    			}
    			if(index >= (sendConditionOperationStore.count() - 1)){
    				target.down('[itemId=down]').disable();
    				target.down('[itemId=down-buttom]').disable();
    			}
    			return;
    		} 
    		target.down('[itemId=up]').disable();
			target.down('[itemId=up-top]').disable();
			target.down('[itemId=down]').disable();
			target.down('[itemId=down-buttom]').disable();
    	}
	};
	
	var setNewWinGridOrderButton = function(){
		if(newWin){
    		var target = newWin.down('form').down('[itemId=grid-send-condition]');
    		var selection = target.getSelection();
    		if(selection.length > 0){
    			selection = selection[0];
    			target.down('[itemId=up]').enable();
				target.down('[itemId=up-top]').enable();
				target.down('[itemId=down]').enable();
				target.down('[itemId=down-buttom]').enable();
    			var index = sendConditionStore.indexOf(selection);
    			if(index == 0){
    				target.down('[itemId=up]').disable();
    				target.down('[itemId=up-top]').disable();
    			}
    			if(index >= (sendConditionStore.count() - 1)){
    				target.down('[itemId=down]').disable();
    				target.down('[itemId=down-buttom]').disable();
    			}
    			return;
    		} 
    		target.down('[itemId=up]').disable();
			target.down('[itemId=up-top]').disable();
			target.down('[itemId=down]').disable();
			target.down('[itemId=down-buttom]').disable();
    	}
	};
    
    var createOrUpdateCondition = function(){
		if(newSendConditionWin.down('form').isValid()){
			//check condition empty
			if(sendConditionOperationStore.getCount() === 0){
				Ext.Msg.alert("验证失败", '请创建至少一个过滤条件');
				return;
			}
			
			var record = newSendConditionWin.record ? newSendConditionWin.record : Ext.create('sendCondition',{});
			newSendConditionWin.close();
			record.set('displayName', newSendConditionWin.down('form').down('[name=displayName]').getValue());
			record.set('foundType', newSendConditionWin.down('form').down('[name=foundType]').getValue());
			var value = newWin.down('[name=actionType]').getValue();
			record.set('handlerId', value == 2 ? 
					newSendConditionWin.down('form').down('[name=msid]').getValue() : 
						value == 9 ? newSendConditionWin.down('form').down('[name=smid]').getValue() : null);
			record.set('description', newSendConditionWin.down('form').down('[name=description]').getValue());
			var dataList = new Array();
			for(var i = 0; i< sendConditionOperationStore.getCount(); i++){
				var item = sendConditionOperationStore.getAt(i);
				var newRecord = {};
				newRecord.option =  item.get('option');
				newRecord.operation = item.get('operation');
				newRecord.value = item.get('value');
				newRecord.val = item.get('val');
				newRecord.begin = item.get('begin');
				newRecord.end = item.get('end');
				newRecord.caseInsensitive = item.get('caseInsensitive');
				dataList.push(newRecord);
			}
			record.set('operationList', dataList);
			if(!newSendConditionWin.record){
				sendConditionStore.add(record);
			}
			newSendConditionWin.record = false;
			newSendConditionWin.down('form').reset();
			newOperationWin.down('form').reset();
			newOperationWin.record = false;
		}
	};
    
    var newWin = Ext.create('Ext.window.Window',
			{
				title : '新建计划任务',
				minWidth : 200,
				layout : 'fit',
				resizable : false,
				modal : true,
				iconCls: 'icon-new',
				closeAction : 'hide',
				animateTarget: 'grid-add-button',
				animateShadow: true,
				border : false,
				items : [ {
					xtype : 'form',
					layout : {
						type : 'vbox',
						align : 'stretch'
					},
					border : false,
					bodyPadding : 10,
					fieldDefaults : {
						msgTarget : 'qtip',
						labelStyle : 'font-weight:bold'
					},
					items : [{
			            defaultType:'textfield',
			            defaults: {
			            	padding: '0 10 0 0',
			                allowBlank: false,
			                selectOnFocus: true,
			                msgTarget: 'qtip',
			                labelAlign: 'right',
			                labelWidth: 150,
			            },
			            items:[{
			            	xtype: 'hiddenfield',
			            	name: 'id',
			            	allowBlank: true
			            }, {
			             	fieldLabel:'显示名称',
			            	name: 'displayName',
			            	width: 500
			            }, {
			            	xtype: 'combobox',
			            	fieldLabel:'任务类型',
			             	name: 'actionType',
			             	editable: false,
			                queryMode: 'local',
			                store: actionStore,
			                displayField: 'key',
			                valueField: 'val',
			                listeners: [{
			                	select: function(){
			                		var value = newWin.down('[name=actionType]').getValue();
			                		//mail scan
			                		if(value == 2){
			                			var target = newWin.down('[name=dsid]');
			                			target.setHidden(false);
			                			target.enable();
			                			target.clearInvalid();
			                			target = newWin.down('[itemId=grid-container]');
			                			target.setHidden(false);
			                			target = newWin.down('[itemId=grid-send-condition]');
			                			target.setHidden(false);
			                			target = newWin.down('[name=msid]');
			                			target.setHidden(false);
			                			target.enable();
			                			target.clearInvalid();
			                			target = newWin.down('[name=smid]');
			                			target.setHidden(true);
			                			target.disable();
			                			target.clearInvalid();
			                			target = newWin.down('[name=attachmentAsContent]');
			                			target.setHidden(false);
			                			target.enable();
			                			target.clearInvalid();
			                			target = newWin.down('[name=url]');
			                			target.setHidden(true);
			                			target.disable();
			                			target.clearInvalid();
			                			target = newWin.down('[id=vhostcontainer]');
			                			target.setHidden(true);
			                			target = newWin.down('[name=vprotocol]');
			                			target.setHidden(true);
			                			target.disable();
			                			target.clearInvalid();
			                			target = newWin.down('[name=vhost]');
			                			target.setHidden(true);
			                			target.disable();
			                			target.clearInvalid();
			                			target = newWin.down('[name=vport]');
			                			target.setHidden(true);
			                			target.disable();
			                			target.clearInvalid();
			                			target = newWin.down('[name=vappname]');
			                			target.setHidden(true);
			                			target.disable();
			                			target.clearInvalid();
			                			target = newWin.down('[name=vprgname]');
			                			target.setHidden(true);
			                			target.disable();
			                			target.clearInvalid();
			                			target = newWin.down('[name=command]');
			                			target.setHidden(true);
			                			target.disable();
			                			target.clearInvalid();
			                			target = newWin.down('[name=commandVariable]');
			                			target.setHidden(true);
			                			target.disable();
			                			target.clearInvalid();
			                			target = newWin.down('[name=sqlSentence]');
			                			target.setHidden(true);
			                			target.disable();
			                			target.clearInvalid();
			                			
			                			//check the mail list and the ds list
			                			if(dsStore.getCount() === 0){
			                				Ext.Msg.show({
			                						title: '提示', 
			                						message: '创建此计划任务，需要数据源支持，现在还没有相关配置信息，是否现在就去创建？',
			                						buttons: Ext.Msg.OKCANCEL,
			                						icon: Ext.Msg.QUESTION,
			                						buttonText : {
			            								ok : '确定',
			            								cancel : '取消'
			            							},
			                						fn: function(btn){
					                					if(btn === 'ok'){
					                						window.location.href = "/dsm/?_t=" + new Date().toLocaleString();
					                					}
			                						}
			                				});
			                				return;
			                			}
			                			if(msStore.getCount() === 0){
			                				Ext.Msg.show({
			                					title: '提示', 
			                					message: '创建此计划任务，需要邮件发送服务器配置信息，现在还没有相关配置信息，是否现在就去创建？',
			                					buttons: Ext.Msg.OKCANCEL,
			                					icon: Ext.Msg.QUESTION,
			                					buttonText : {
			                						ok : '确定',
			                						cancel : '取消'
			                					},
			                					fn: function(btn){
			                						if(btn === 'ok'){
			                							window.location.href = "/msm/?_t=" + new Date().toLocaleString();
			                						}
			                					}
			                				});
			                			}
			                		}
			                		//MMS plan
			                		if(value == 4){
			                			var target = newWin.down('[name=dsid]');
			                			target.setHidden(true);
			                			target.disable();
			                			target.clearInvalid();
			                			target = newWin.down('[itemId=grid-container]');
			                			target.setHidden(true);
			                			target = newWin.down('[itemId=grid-send-condition]');
			                			target.setHidden(true);
			                			target = newWin.down('[name=msid]');
			                			target.setHidden(true);
			                			target.disable();
			                			target.clearInvalid();
			                			target = newWin.down('[name=smid]');
			                			target.setHidden(true);
			                			target.disable();
			                			target.clearInvalid();
			                			target = newWin.down('[name=attachmentAsContent]');
			                			target.setHidden(true);
			                			target.disable();
			                			target.clearInvalid();
			                			target = newWin.down('[name=url]');
			                			target.setHidden(false);
			                			target.enable();
			                			target.clearInvalid();
			                			target = newWin.down('[id=vhostcontainer]');
			                			target.setHidden(false);
			                			target = newWin.down('[name=vprotocol]');
			                			target.setHidden(false);
			                			target.enable();
			                			target.clearInvalid();
			                			target = newWin.down('[name=vhost]');
			                			target.setHidden(false);
			                			target.enable();
			                			target.clearInvalid();
			                			target = newWin.down('[name=vport]');
			                			target.setHidden(false);
			                			target.enable();
			                			target.clearInvalid();
			                			target = newWin.down('[name=vappname]');
			                			target.setHidden(false);
			                			target.enable();
			                			target.clearInvalid();
			                			target = newWin.down('[name=vprgname]');
			                			target.setHidden(false);
			                			target.enable();
			                			target.clearInvalid();
			                			target = newWin.down('[name=command]');
			                			target.setHidden(true);
			                			target.disable();
			                			target.clearInvalid();
			                			target = newWin.down('[name=commandVariable]');
			                			target.setHidden(true);
			                			target.disable();
			                			target.clearInvalid();
			                			target = newWin.down('[name=sqlSentence]');
			                			target.setHidden(true);
			                			target.disable();
			                			target.clearInvalid();
			                		}
			                		
			                		//window command executor
			                		if(value == 5){
			                			var target = newWin.down('[name=dsid]');
			                			target.setHidden(true);
			                			target.disable();
			                			target.clearInvalid();
			                			target = newWin.down('[itemId=grid-container]');
			                			target.setHidden(true);
			                			target = newWin.down('[itemId=grid-send-condition]');
			                			target.setHidden(true);
			                			target = newWin.down('[name=msid]');
			                			target.setHidden(true);
			                			target.disable();
			                			target.clearInvalid();
			                			target = newWin.down('[name=smid]');
			                			target.setHidden(true);
			                			target.disable();
			                			target.clearInvalid();
			                			target = newWin.down('[name=attachmentAsContent]');
			                			target.setHidden(true);
			                			target.disable();
			                			target.clearInvalid();
			                			target = newWin.down('[name=url]');
			                			target.setHidden(true);
			                			target.disable();
			                			target.clearInvalid();
			                			target = newWin.down('[id=vhostcontainer]');
			                			target.setHidden(true);
			                			target = newWin.down('[name=vprotocol]');
			                			target.setHidden(true);
			                			target.disable();
			                			target.clearInvalid();
			                			target = newWin.down('[name=vhost]');
			                			target.setHidden(true);
			                			target.disable();
			                			target.clearInvalid();
			                			target = newWin.down('[name=vport]');
			                			target.setHidden(true);
			                			target.disable();
			                			target.clearInvalid();
			                			target = newWin.down('[name=vappname]');
			                			target.setHidden(true);
			                			target.disable();
			                			target.clearInvalid();
			                			target = newWin.down('[name=vprgname]');
			                			target.setHidden(true);
			                			target.disable();
			                			target.clearInvalid();
			                			target = newWin.down('[name=command]');
			                			target.setHidden(false);
			                			target.enable();
			                			target.clearInvalid();
			                			target = newWin.down('[name=commandVariable]');
			                			target.setHidden(false);
			                			target.enable();
			                			target.clearInvalid();
			                			target = newWin.down('[name=sqlSentence]');
			                			target.setHidden(true);
			                			target.disable();
			                			target.clearInvalid();
			                		}
			                		
			                		//run db sql action
			                		if(value == 7){
			                			var target = newWin.down('[name=dsid]');
			                			target.setHidden(false);
			                			target.enable();
			                			target.clearInvalid();
			                			target = newWin.down('[itemId=grid-container]');
			                			target.setHidden(true);
			                			target = newWin.down('[itemId=grid-send-condition]');
			                			target.setHidden(true);
			                			target = newWin.down('[name=msid]');
			                			target.setHidden(true);
			                			target.disable();
			                			target.clearInvalid();
			                			target = newWin.down('[name=smid]');
			                			target.setHidden(true);
			                			target.disable();
			                			target.clearInvalid();
			                			target = newWin.down('[name=attachmentAsContent]');
			                			target.setHidden(true);
			                			target.disable();
			                			target.clearInvalid();
			                			target = newWin.down('[name=url]');
			                			target.setHidden(true);
			                			target.disable();
			                			target.clearInvalid();
			                			target = newWin.down('[id=vhostcontainer]');
			                			target.setHidden(true);
			                			target = newWin.down('[name=vprotocol]');
			                			target.setHidden(true);
			                			target.disable();
			                			target.clearInvalid();
			                			target = newWin.down('[name=vhost]');
			                			target.setHidden(true);
			                			target.disable();
			                			target.clearInvalid();
			                			target = newWin.down('[name=vport]');
			                			target.setHidden(true);
			                			target.disable();
			                			target.clearInvalid();
			                			target = newWin.down('[name=vappname]');
			                			target.setHidden(true);
			                			target.disable();
			                			target.clearInvalid();
			                			target = newWin.down('[name=vprgname]');
			                			target.setHidden(true);
			                			target.disable();
			                			target.clearInvalid();
			                			target = newWin.down('[name=command]');
			                			target.setHidden(true);
			                			target.disable();
			                			target.clearInvalid();
			                			target = newWin.down('[name=commandVariable]');
			                			target.setHidden(true);
			                			target.disable();
			                			target.clearInvalid();
			                			target = newWin.down('[name=sqlSentence]');
			                			target.setHidden(false);
			                			target.enable();
			                			target.clearInvalid();
			                			
			                			//check the mail list and the ds list
			                			if(dsStore.getCount() === 0){
			                				Ext.Msg.show({
			                						title: '提示', 
			                						message: '创建此计划任务，需要数据源支持，现在还没有相关配置信息，是否现在就去创建？',
			                						buttons: Ext.Msg.OKCANCEL,
			                						icon: Ext.Msg.QUESTION,
			                						buttonText : {
			            								ok : '确定',
			            								cancel : '取消'
			            							},
			                						fn: function(btn){
					                					if(btn === 'ok'){
					                						window.location.href = "/dsm/?_t=" + new Date().toLocaleString();
					                					}
			                						}
			                				});
			                			}
			                		}
			                		
			                		//short message task scan
			                		if(value == 9){
			                			var target = newWin.down('[name=dsid]');
			                			target.setHidden(false);
			                			target.enable();
			                			target.clearInvalid();
			                			target = newWin.down('[itemId=grid-container]');
			                			target.setHidden(false);
			                			target = newWin.down('[itemId=grid-send-condition]');
			                			target.setHidden(false);
			                			target = newWin.down('[name=msid]');
			                			target.setHidden(true);
			                			target.disable();
			                			target.clearInvalid();
			                			target = newWin.down('[name=smid]');
			                			target.setHidden(false);
			                			target.enable();
			                			target.clearInvalid();
			                			target = newWin.down('[name=attachmentAsContent]');
			                			target.setHidden(true);
			                			target.disable();
			                			target.clearInvalid();
			                			target = newWin.down('[name=url]');
			                			target.setHidden(true);
			                			target.disable();
			                			target.clearInvalid();
			                			target = newWin.down('[id=vhostcontainer]');
			                			target.setHidden(true);
			                			target = newWin.down('[name=vprotocol]');
			                			target.setHidden(true);
			                			target.disable();
			                			target.clearInvalid();
			                			target = newWin.down('[name=vhost]');
			                			target.setHidden(true);
			                			target.disable();
			                			target.clearInvalid();
			                			target = newWin.down('[name=vport]');
			                			target.setHidden(true);
			                			target.disable();
			                			target.clearInvalid();
			                			target = newWin.down('[name=vappname]');
			                			target.setHidden(true);
			                			target.disable();
			                			target.clearInvalid();
			                			target = newWin.down('[name=vprgname]');
			                			target.setHidden(true);
			                			target.disable();
			                			target.clearInvalid();
			                			target = newWin.down('[name=command]');
			                			target.setHidden(true);
			                			target.disable();
			                			target.clearInvalid();
			                			target = newWin.down('[name=commandVariable]');
			                			target.setHidden(true);
			                			target.disable();
			                			target.clearInvalid();
			                			target = newWin.down('[name=sqlSentence]');
			                			target.setHidden(true);
			                			target.disable();
			                			target.clearInvalid();
			                			
			                			//check the mail list and the ds list
			                			if(dsStore.getCount() === 0){
			                				Ext.Msg.show({
			                						title: '提示', 
			                						message: '创建此计划任务，需要数据源支持，现在还没有相关配置信息，是否现在就去创建？',
			                						buttons: Ext.Msg.OKCANCEL,
			                						icon: Ext.Msg.QUESTION,
			                						buttonText : {
			            								ok : '确定',
			            								cancel : '取消'
			            							},
			                						fn: function(btn){
					                					if(btn === 'ok'){
					                						window.location.href = "/dsm/?_t=" + new Date().toLocaleString();
					                					}
			                						}
			                				});
			                				return;
			                			}
			                			if(smStore.getCount() === 0){
			                				Ext.Msg.show({
			                					title: '提示', 
			                					message: '创建此计划任务，需要短信通道支持，现在还没有相关配置信息，是否现在就去创建？',
			                					buttons: Ext.Msg.OKCANCEL,
			                					icon: Ext.Msg.QUESTION,
			                					buttonText : {
			                						ok : '确定',
			                						cancel : '取消'
			                					},
			                					fn: function(btn){
			                						if(btn === 'ok'){
			                							window.location.href = "/smm/?_t=" + new Date().toLocaleString();
			                						}
			                					}
			                				});
			                			}
			                		}
			                	},
			                	change: function(){
			                		sendConditionStore.removeAll();
			                	}
			                }]
			            }, {
			            	xtype: 'combo',
			            	fieldLabel:'请选择目标数据源',
			             	name: 'dsid',
			             	hidden: true,
			                disabled: true,
			             	editable: false,
			                queryMode: 'local',
			                store: dsStore,
			                displayField: 'key',
			                valueField: 'val'
			            }, {
	                    	xtype:'combobox',
	                    	fieldLabel: '设定默认邮件程序',
	                    	name: 'msid',
	                    	hidden: true,
	                    	disabled: true,
	                    	allowBlank: true,
	                		queryMode: 'local',
	                		labelAlign: 'right',
	    	                displayField: 'key',
	    	                valueField: 'val',
	    	                store: mseStore,
	    	                editable: false,
	    	                listeners: [{
	    	                	change: function(){
	    	                		if(newWin.down('form').down('[name=msid]').getValue() <= -99){
	    	                			newWin.down('form').down('[name=msid]').setValue(null);
	    	                		}
	    	                	}
	    	                }]
	                    }, {
	                    	xtype:'combobox',
	                    	fieldLabel: '设定默认短信通道',
	                    	name: 'smid',
	                    	hidden: true,
	                    	disabled: true,
	                    	allowBlank: true,
	                		queryMode: 'local',
	                		labelAlign: 'right',
	    	                displayField: 'key',
	    	                valueField: 'val',
	    	                store: smeStore,
	    	                editable: false,
	    	                listeners: [{
	    	                	change: function(){
	    	                		if(newWin.down('form').down('[name=smid]').getValue() <= -99){
	    	                			newWin.down('form').down('[name=smid]').setValue(null);
	    	                		}
	    	                	}
	    	                }]
	                    }, {
			            	xtype: 'fieldcontainer',
			            	fieldLabel:'高级发送',
			            	itemId: 'grid-container',
			            	width: 700,
			            	hidden: true,
			            	layout: {
			            		defaultMargins: {top: 0, right: 5, bottom: 0, left: 0}
			            	},
			            	defaults: {
			                	anchor: '100%',
			                	hidden: true,
				                disabled: true,
			                	allowBlank: false,
			                	selectOnFocus: true
			                },
				            items:[{
				            	xtype: 'grid',
				            	itemId: 'grid-send-condition',
				            	title: '可选－可按规则使用不同的程序进行任务处理',
				            	store: sendConditionStore,
				            	disabled: false,
				             	hidden: true,
				             	loadMask: true,
				                tbar:[
				                    {
				                    	id: 'grid-send-condition-add-button',
				                    	text: '添加新规则',
				                    	iconCls: 'icon-new',
				                    	tooltip: '创建过滤条件，然后选择使用哪个目标处理符合条件的任务。',
				                    	handler: function(){
				                    		newSendConditionWin.record = false;
				                    		sendConditionOperationStore.removeAll();
				                    		newSendConditionWin.down('form').reset();
				                    		newSendConditionWin.show();
				                    		
				        					var value = newWin.down('form').down('[name=actionType]').getValue();
				        					if(value === 2){
				        						var target = newSendConditionWin.down('[name=msid]');
				        						target.setHidden(false);
				        						target.enable();
				        						target.clearInvalid();
				        						target = newSendConditionWin.down('[name=smid]');
				        						target.setHidden(true);
				        						target.disable();
				        						target.clearInvalid();
				        						return;
				        					}
				        					if(value === 9){
				        						var target = newSendConditionWin.down('[name=msid]');
				        						target.setHidden(true);
				        						target.disable();
				        						target.clearInvalid();
				        						target = newSendConditionWin.down('[name=smid]');
				        						target.setHidden(false);
				        						target.enable();
				        						target.clearInvalid();
				        						return;
				        					}
				        					var target = newSendConditionWin.down('[name=msid]');
				        					target.setHidden(true);
			        						target.disable();
			        						target.clearInvalid();
				        					target = newSendConditionWin.down('[name=smid]');
				        					target.setHidden(true);
			        						target.disable();
			        						target.clearInvalid();
				                    	}
				                    }, '-',{
			                        	iconCls: 'icon-up',
			                            text: '上移一行',
			                            disabled: true,
			                            itemId: 'up',
			                            handler: function() {
			                            	var selection = newWin.down('[itemId=grid-send-condition]').getSelection();
			                            	if(selection && selection[0]){
			                            		selection = selection[0];
			                            	}
			                            	sendConditionStore.insert(sendConditionStore.indexOf(selection) - 1, selection);
			                            }
			                        },
			                        {
			                        	iconCls: 'icon-down',
			                        	text: '下移一行',
			                        	disabled: true,
			                            itemId: 'down',
			                            handler: function() {
			                            	var selection = newWin.down('[itemId=grid-send-condition]').getSelection();
			                            	if(selection && selection[0]){
			                            		selection = selection[0];
			                            	}
			                            	sendConditionStore.insert(sendConditionStore.indexOf(selection)  + 2, selection);
			                            }
			                        },
			                        {
			                        	iconCls: 'icon-up-top',
			                        	text: '上移到第一行',
			                        	disabled: true,
			                            itemId: 'up-top',
			                            handler: function() {
			                            	var selection = newWin.down('[itemId=grid-send-condition]').getSelection();
			                            	if(selection && selection[0]){
			                            		selection = selection[0];
			                            	}
			                            	sendConditionStore.insert(0, selection);
			                            }
			                        },
			                        {
			                        	iconCls: 'icon-down-buttom',
			                        	text: '下移到最后一行',
			                        	disabled: true,
			                            itemId: 'down-buttom',
			                            handler: function() {
			                            	var selection = newWin.down('[itemId=grid-send-condition]').getSelection();
			                            	if(selection && selection[0]){
			                            		selection = selection[0];
			                            	}
			                            	sendConditionStore.add(selection);
			                            }
			                        }
				                    ],
				                columns: [
				                    {header: '名称', flex: 1, dataIndex: 'displayName'},
				                    {header: '目标', flex: 2, dataIndex: 'handlerId', renderer:function(v){
				                    	var actionType = newWin.down('form').down('[name=actionType]').getValue();
				                    	if(actionType == 2){
				                    		return msStore.findRecord('val', v).get('key');
				                    	}
				                    	if(actionType == 9){
				                    		return smStore.findRecord('val', v).get('key');
				                    	}
				                    }},
				                    {
				                    	xtype: 'actioncolumn',
				                    	menuDisabled: true,
				                    	flex: 3,
				                    	sortable: false,
				                    	items:[
				                            {
				                            	iconCls: 'icon-condition-edit',
				                            	tooltip: '编辑',
				                                handler: function(grid, rowIndex, colIndex) {
				                                	newSendConditionWin.down('form').reset();
				                                	newSendConditionWin.show();
				                                    var record = sendConditionStore.getAt(rowIndex);
				                                    sendConditionOperationStore.removeAll();
				                                    sendConditionOperationStore.loadData(record.get('operationList'))
					                    			newSendConditionWin.record = record;
				                                    newSendConditionWin.down('form').down('[name=displayName]').setValue(record.get('displayName'));
					                    			newSendConditionWin.down('form').down('[name=foundType]').setValue(record.get('foundType'));
					                    			newSendConditionWin.down('form').down('[name=description]').setValue(record.get('description'));
					                    			
					             					var value = newWin.down('[name=actionType]').getValue();
					             					if(value === 2){
					             						var target = newSendConditionWin.down('[name=msid]');
					             						target.setHidden(false);
					             						target.enable();
					             						target.clearInvalid();
					             						target.setValue(record.get('handlerId'));
					             						target = newSendConditionWin.down('[name=smid]');
					             						target.setHidden(true);
					                         			target.disable();
					                         			target.clearInvalid();
					             						return;
					             					}
					             					if(value === 9){
					             						var target = newSendConditionWin.down('[name=msid]');
					             						target.setHidden(true);
					                         			target.disable();
					                         			target.clearInvalid();
					             						target = newSendConditionWin.down('[name=smid]');
					             						target.setHidden(false);
					             						target.enable();
					             						target.clearInvalid();
					             						target.setValue(record.get('handlerId'));
					             						return;
					             					}
					             					var target = newSendConditionWin.down('[name=msid]');
					             					target.setHidden(true);
					                     			target.disable();
					                     			target.clearInvalid();
					             					target = newSendConditionWin.down('[name=smid]');
					             					target.setHidden(true);
					                     			target.disable();
					                     			target.clearInvalid();
					             				
				                                }
				                            },
				                            {
				                            	iconCls: 'icon-condition-delete',
				                                tooltip: '删除',
				                                handler: function(grid, rowIndex, colIndex) {
				                                    var record = grid.getStore().getAt(rowIndex);
				                                    sendConditionStore.remove(record);
				                                }
				                            }
				                            ]
				                    },
				                ], 
					             listeners: {
					            	 afterrender: function(){
					            		 setNewWinGridOrderButton(); 
					            	 },
					                 selectionchange: function(){
					                	 setNewWinGridOrderButton();
					                 },
					            	 rowdblclick: function(g, r, tr, rowIndex, e, eOpts){
		                                	newSendConditionWin.down('form').reset();
		                                	newSendConditionWin.show();
		                                    var record = sendConditionStore.getAt(rowIndex);
		                                    sendConditionOperationStore.removeAll();
		                                    sendConditionOperationStore.loadData(record.get('operationList'))
			                    			newSendConditionWin.record = record;
		                                    newSendConditionWin.down('form').down('[name=displayName]').setValue(record.get('displayName'));
			                    			newSendConditionWin.down('form').down('[name=foundType]').setValue(record.get('foundType'));
			                    			newSendConditionWin.down('form').down('[name=description]').setValue(record.get('description'));
			                    			
			             					var value = newWin.down('[name=actionType]').getValue();
			             					if(value === 2){
			             						var target = newSendConditionWin.down('[name=msid]');
			             						target.setHidden(false);
			             						target.enable();
			             						target.clearInvalid();
			             						target.setValue(record.get('handlerId') == null ? target.setValue(grid.getSelection()[0].get('msid')) : record.get('handlerId'));
			             						target = newSendConditionWin.down('[name=smid]');
			             						target.setHidden(true);
			                         			target.disable();
			                         			target.clearInvalid();
			             						return;
			             					}
			             					if(value === 9){
			             						var target = newSendConditionWin.down('[name=msid]');
			             						target.setHidden(true);
			                         			target.disable();
			                         			target.clearInvalid();
			             						target = newSendConditionWin.down('[name=smid]');
			             						target.setHidden(false);
			             						target.enable();
			             						target.clearInvalid();
			             						target.setValue(record.get('handlerId') == null ? target.setValue(grid.getSelection()[0].get('smid')) : record.get('handlerId'));
			             						return;
			             					}
			             					var target = newSendConditionWin.down('[name=msid]');
			             					target.setHidden(true);
			                     			target.disable();
			                     			target.clearInvalid();
			             					target = newSendConditionWin.down('[name=smid]');
			             					target.setHidden(true);
			                     			target.disable();
			                     			target.clearInvalid();
			             				
		                                }
					             }
				            }]
			            }, {
			            	xtype: 'checkbox',
			            	fieldLabel: '如果正文为空，则将附件转换为正文',
			            	checked: true,
			            	name: 'attachmentAsContent',
			            	hidden: false,
			            	disabled: false,
			            	afterSubTpl: '<div><span><font color="red">*</font></span>目前仅支持将HTML，HTM，TXT为后缀的文件转换为正文，其它文件仍然按照附件处理。</div>',
			            }, {
			            	fieldLabel:'MMS URL',
			             	width: 800,
			             	hidden: true,
			                disabled: true,
			                vtype: 'url',
			            	name: 'url',
			            	value: 'http://',
			            	listeners: {
			            		change: function(){
			            			var target = newWin.down('[name=url]');
			            			if(target.isValid()){
			            				var url = purl(target.getValue().toLocaleLowerCase());
			            				newWin.down('[name=vprotocol]').setValue(url.attr('protocol') + '://');
			            				newWin.down('[name=vhost]').setValue(url.attr('host'));
			            				newWin.down('[name=vport]').setValue(url.attr('port') == '' ? 80 : url.attr('port'));
			            				
			            				var startindex = target.getValue().toLocaleLowerCase().indexOf('appname=');
			            				var endindex = target.getValue().indexOf('&', startindex);
			            				newWin.down('[name=vappname]').setValue(decodeURIComponent(target.getValue().substring(startindex < 0 ? target.getValue().length : startindex + 'appname='.length ,endindex < 0 ? target.getValue().length : endindex)));
			            				
			            				startindex = target.getValue().toLocaleLowerCase().indexOf('prgname=');
			            				endindex = target.getValue().indexOf('&', startindex);
			            				newWin.down('[name=vprgname]').setValue(decodeURIComponent(target.getValue().substring(startindex < 0 ? target.getValue().length : startindex + 'prgname='.length ,endindex < 0 ? target.getValue().length : endindex)));
			            				if(newWin.down('[name=vhost]').isValid()
			            						&& newWin.down('[name=vappname]').isValid()
			            						&& newWin.down('[name=vprgname]').isValid()){
			            					urlassemble();
			            				}
			            			}
			            		}
			            	}
			            }, {
			            	xtype:'fieldcontainer',
			            	fieldLabel:'主机信息',
			            	id: 'vhostcontainer',
			            	hidden: true,
			            	layout: {
			            		type: 'hbox',
			            		defaultMargins: {top: 0, right: 5, bottom: 0, left: 0}
			            	},
			            	defaultType:'textfield',
			            	defaults: {
			                	anchor: '100%',
			                	hidden: true,
				                disabled: true,
			                	allowBlank: false,
			                	selectOnFocus: true
			                },
			            	items: [{
			            		xtype: 'combo',
			            		name: 'vprotocol',
			            		queryMode: 'local',
			            		displayField: 'name',
				                valueField: 'value',
				                width: 60,
				                value: 'http://',
				                editable: false,
			            		store: Ext.create('Ext.data.Store', {
			            			fields: ['value', 'name'],
			            			data: [
			            			        {value: 'http://', name: 'HTTP'},
			            			        {value: 'https://', name: 'HTTPS'},
			            			]
			            			}),
			            		listeners: { blur: function(){urlassemble();} } 
					            },{
					            	name: 'vhost',
					            	emptyText: '主机地址',
					            	listeners: { blur: function(){urlassemble();} } 
					            }, {
					            	xtype: 'numberfield',
					            	name: 'vport',
					            	width: 40,
					            	minValue: 0,
					            	msgTarget : 'qtip',
					            	allowBlank: true,
					            	hideTrigger: true,
					                keyNavEnabled: false,
					                mouseWheelEnabled: false,
					            	emptyText: '端口',
					            	listeners: { blur: function(){urlassemble();} } 
					            }]
			            }, {
			            	fieldLabel:'APPNAME',
			            	hidden: true,
			                disabled: true,
			            	name: 'vappname',
			            	listeners: { blur: function(){urlassemble();} } 
			            }, {
			            	fieldLabel:'PRGNAME',
			            	hidden: true,
			                disabled: true,
			            	name: 'vprgname',
			            	listeners: { blur: function(){urlassemble();} } 
			            }, {
			            	fieldLabel:'命令行',
			            	name: 'command',
			            	width: 500,
			            	afterSubTpl: '<div><span><font color="red">*</font></span>请确保服务器上存在该执行程序，并且有权限执行。</div>'
			            }, {
			            	fieldLabel:'命令行参数',
			            	allowBlank: true,
			            	width: 500,
			            	name: 'commandVariable'
			            }, {
			            	fieldLabel:'SQL语句',
			            	xtype: 'textarea',
			            	width: 500,
			            	name: 'sqlSentence',
			            	afterSubTpl: '<div><span><font color="red">*</font></span>请自行确保SQL语句没有语法错误</div>'
			            }, {
			            	fieldLabel:'描述',
			            	xtype: 'textarea',
			            	width: 500,
			            	allowBlank: true,
			            	name: 'description'
			            }, {
			            	xtype: 'fieldset',
			                title: '执行规则',
			                collapsible: false,
			                defaults: {
			                    labelWidth: 120,
			                    anchor: '100%',
			                    labelAlign: 'right',
			                    layout: {
			                        type: 'hbox',
			                        defaultMargins: {top: 0, right: 5, bottom: 0, left: 0}
			                    }
			                },
			            	items: [{
				            	xtype: 'radiogroup',
				            	fieldLabel:'执行规则',
				            	id: 'startflagGroup',
				            	hidden: false,
				            	items:[
				            	       {
				            	    	   id: 'startflag-radio-1', 
				            	    	   boxLabel: '立即执行', 
				            	    	   name: 'startflag', 
				            	    	   inputValue: 0, 
				            	    	   padding: '0 10 0 0',
				            	    	   checked: true, 
				            	    	   listeners: {
				            	    		   change: function(obj){startFlagChangeHandler();}
				            	    	   }
				            	       },
				            	       {
				            	    	   id: 'startflag-radio-2', 
				            	    	   boxLabel: '延时执行', 
				            	    	   name:'startflag',
				            	    	   inputValue: 1,
				            	    	   padding: '0 10 0 0',
				            	    	   listeners: {
				            	    		   change: function(obj){startFlagChangeHandler();}
				            	    	   }
				            	       },
				            	       {
				            	    	   id: 'startflag-radio-3',
				            	    	   boxLabel: '暂不执行',
				            	    	   name: 'startflag',
				            	    	   inputValue: 2,
				            	    	   listeners: {
				            	    		   change: function(obj){startFlagChangeHandler();}
				            	    	   }
				            	       }
				            	]
				            }, {
				            	xtype: 'radiogroup',
				            	fieldLabel:'延时规则',
				            	id: 'delayregularcontainer',
				            	hidden: true,
				            	items:[
				            	       {id: 'radio1', boxLabel: '延时执行', padding: '0 10 0 0', name: 'delay', inputValue: 0, checked: true, listeners:{change: function(obj){
				            	    	   if(newWin.down('[id=radio1]').getValue()){
				            	    		   newWin.down('[id=delaytimecontainer]').setHidden(false);
				            	    		   var target = newWin.down('[name=delayhour]');
				            	    		   target.enable();
				            	    		   target.clearInvalid();
				            	    		   target = newWin.down('[name=delayMinute]');
				            	    		   target.enable();
				            	    		   target.clearInvalid();
				            	    		   
				            	    		   newWin.down('[id=delaydatecontainer]').setHidden(true);
				            	    		   target = newWin.down('[name=delayD]');
				            	    		   target.disable();
				            	    		   target.clearInvalid();
				            	    		   target = newWin.down('[name=delayT]');
				            	    		   target.disable();
				            	    		   target.clearInvalid();
				            	    	   }
				            	       }}},
				            	       {id: 'radio2', boxLabel: '设置开始执行日期', name: 'delay', inputValue: 1, checked: false, listeners:{change: function(obj){
				            	    	   if(newWin.down('[id=radio2]').getValue()){
				            	    		   newWin.down('[id=delaytimecontainer]').setHidden(true);
				            	    		   var target = newWin.down('[name=delayhour]');
				            	    		   target.disable();
				            	    		   target.clearInvalid();
				            	    		   target = newWin.down('[name=delayMinute]');
				            	    		   target.disable();
				            	    		   target.clearInvalid();
				            	    		   
				            	    		   newWin.down('[id=delaydatecontainer]').setHidden(false);
				            	    		   target = newWin.down('[name=delayD]');
				            	    		   target.enable();
				            	    		   target.clearInvalid();
				            	    		   target = newWin.down('[name=delayT]');
				            	    		   target.enable();
				            	    		   target.clearInvalid();
				            	    	   }
				            	       }}}
				            	]
				            }, {
				            	xtype: 'fieldcontainer',
				             	fieldLabel:'延时执行',
				                msgTarget: 'qtip',
				                id: 'delaytimecontainer',
				                hidden: true,
				                defaults: {
				                	anchor: '100%',
				                	disabled: true,
				                	allowBlank: false,
				                    hideLabel: true
				                },
				                items: [{
				                    xtype: 'numberfield',
				                    name: 'delayhour',
				                    minValue : 0,
				                    value: 0,
				                    width: 95,
				                    allowBlank: false
				                }, {
				                    xtype: 'displayfield',
				                    disabled: false,
				                    value: '小时'
				                }, {
				                    xtype: 'numberfield',
				                    name: 'delayMinute',
				                    minValue : 0,
				                    maxValue : 59,
				                    value: 0,
				                    hideTrigger: true,
					                keyNavEnabled: false,
					                mouseWheelEnabled: false,
				                    width: 95,
				                    allowBlank: false
				                }, {
				                    xtype: 'displayfield',
				                    disabled: false,
				                    value: '分钟'
				                 }]
				            },{
				            	xtype:'fieldcontainer',
				            	fieldLabel:'此时间后执行',
				            	id: 'delaydatecontainer',
				            	hidden: true,
				            	defaults: {
				                	anchor: '100%',
				                	disabled: true,
				                	allowBlank: false,
				                },
				            	items: [ {
						            	xtype: 'datefield',
						            	minValue: new Date(),
						            	name: 'delayD',
						            	format: 'Y-m-d'
						            },{
						            	xtype: 'timefield',
						            	format: 'H:i:s',
						            	name: 'delayT'
						            }]
				            },{
			            		xtype: 'fieldcontainer',
			                    fieldLabel: '计划执行规则',
			                    itemId: 'cronExpressionContainer',
			                    msgTarget: 'qtip',
			                    defaults: {
			                    	anchor: '100%',
			                        hideLabel: true
			                    },
				            	items: [{
				            		xtype: 'textfield',
				            		name: 'cronExpression',
				            		emptyText: '请单击右侧按钮进行编辑',
				            		allowBlank: false,
				            		editable: false
				            	}, {
				            		id: 'cron-edit-button',
				            		xtype: 'button',
				            		iconCls: 'icon-edit',
				            		tooltip: '构建计划执行规则',
				            		tooltipType: 'title',
				            		handler: function(){
				            			var cron = '<iframe src="/cron/?cron=' + newWin.down('[name=cronExpression]').getValue() + '" width="100%" height="100%"></iframe>';
				            		    var cronWin = Ext.create('Ext.window.Window',{
				            		    	title: 'cron表达式生成',
				            		    	resizable: false,
				            		    	modal: true,
				            		    	iconCls: 'icon-clock',
				            		    	closeAction: 'hide',
				            		    	animateTarget: 'cron-edit-button',
				            		    	animateShadow: true,
				            		    	items: [{
				            		    		xtype: 'component',
				            		    		width: '961px',
				            		        	height: '450px',
				            		    		html: cron
				            		    	}]
				            		    });
				            			closecron = function(){
				            				cronWin.destroy();
				            			}
				            			setcron = function(cron){
				            				newWin.down('[name=cronExpression]').setValue(cron);
				            				closecron();
				            		    }
				            			cronWin.show();
				            		}
				            	}]
				            }]
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
			        		neworupdateSchedule();
			        	}
			        } ],
			        listeners: {
			        	afterrender: function(){
			        		resetnewWin();
			        		newWin.getEl().on({
			        		    	keyup: function(e){
			        		    		if(e.getKey() == Ext.event.Event.ENTER){
			        		    			e.preventDefault();
			        		    			neworupdateSchedule();
			        		    		}
			        		    	}
			        		    });
			        	}
			        }
				} ]
			});
    
    var neworupdateSchedule = function(){
		var form = newWin.down('form').getForm();
		if(form.isValid()){
			var actionType = newWin.down('[name=actionType]').getValue();

			var submitFunction = function(){
				var mask = new Ext.LoadMask({
					msg    : '请稍等...',
					target : newWin
				});
				mask.show();
				
				var sendConditionList = new Array();
				if(actionType == 2 || actionType == 9){
					if(sendConditionStore.getCount() > 0){
						for(var i = 0; i < sendConditionStore.getCount(); i++){
							var item = sendConditionStore.getAt(i);
							var record = {};
							record.displayName = item.get('displayName');
							record.foundType = item.get('foundType');
							record.handlerId = item.get('handlerId');
							record.description = item.get('description');
							record.operationList = item.get('operationList');
							sendConditionList.push(record);
						}
					}
				}
				
				form.submit({
					url:	'neworupdate',
					method:	'post',
					params: {
						'delayDay': newWin.down('[name=delayD]').getRawValue() + ' ' + newWin.down('[name=delayT]').getRawValue(),
						'sendConditions': Ext.encode(sendConditionList)
					},
					failure: function(form, action){
						mask.destroy();
						Ext.Msg.alert("验证失败", action && action.result && action.result.message ? action.result.message : '网络超时');
					},
					success: function(form, action){
						resetnewWin();
						newWin.hide();
						mask.destroy();
						schedulestore.reload();
					}
				} );
			};
			
			var defaultHandlerId = actionType == 2 ? newWin.down('form').down('[name=msid]').getValue()
					: actionType == 9 ? newWin.down('form').down('[name=smid]').getValue() : null;
			
			//check handlers
			if(actionType == 2 || actionType == 9){
				if(sendConditionStore.getCount() === 0 && defaultHandlerId == null){
					Ext.Msg.alert("验证失败", '至少有一个规则，请设定默认程序，或者设定高级发送，然后再继续。');
					return;
				}
				
				if(sendConditionStore.find('handlerId',null) > -1 || sendConditionStore.find('handlerId','') > -1){
					Ext.Msg.alert("验证失败", '高级发送中有些条件未设定，或者无效，请重新进行设定，然后继续。');
					return;
				}
				
				//check duplicate action for mail scan and short message scan
				var deplicateScheduleIndex = schedulestore.findBy(function(record){
					return record.get('id') != newWin.down('form').down('[name=id]').getValue() 
						&& record.get('actionType') == newWin.down('[name=actionType]').getValue() 
						&& record.get('dsid') == newWin.down('form').down('[name=dsid]').getValue();
				});
				if(deplicateScheduleIndex > -1){
					Ext.Msg.alert("验证失败", '重复计划任务－不能对同一个数据源使用多个邮件或者短信扫描任务，请参考第' + schedulestore.getAt(deplicateScheduleIndex).get("id") + '号计划任务');
					return;
				}
				
				//check default handler
				if(defaultHandlerId == null){
					Ext.Msg.show({
						title: '提示', 
						message: '没有设定默认规则，这可能导致某些任务因不符合所有规则而被忽略掉，是否仍然继续？',
						buttons: Ext.Msg.OKCANCEL,
						icon: Ext.Msg.QUESTION,
						buttonText : {
							ok : '确定',
							cancel : '取消'
						},
						fn: function(btn){
							if(btn == 'ok'){
								submitFunction();
							}
						}
					});
				} else {
					submitFunction();
				}
				return;
			}
			submitFunction();
		}
	};
    
    var urlassemble = function(){
    	var url = '';
    	if(newWin.down('[name=vprotocol]').getValue()){
    		url += newWin.down('[name=vprotocol]').getValue();
    		if(newWin.down('[name=vhost]').getValue().length > 0){
    			newWin.down('[name=vhost]').setValue(Ext.String.trim(newWin.down('[name=vhost]').getValue()));
    			url += newWin.down('[name=vhost]').getValue();
    			if(newWin.down('[name=vport]').getValue() && newWin.down('[name=vport]').getValue() != 80){
    				url += ':';
    				url += newWin.down('[name=vport]').getValue();
    			}
    			if(newWin.down('[name=vappname]').getValue().length > 0){
    				url += '/scripts/mgrqispi.dll?appname=';
    				newWin.down('[name=vappname]').setValue(Ext.String.trim(newWin.down('[name=vappname]').getValue()));
    				url += encodeURIComponent(newWin.down('[name=vappname]').getValue());
    				if(newWin.down('[name=vprgname]').getValue().length > 0){
    					url += '&prgname=';
    					newWin.down('[name=vprgname]').setValue(Ext.String.trim(newWin.down('[name=vprgname]').getValue()));
    					url += encodeURIComponent(newWin.down('[name=vprgname]').getValue());
    				}
    			}
    		}
    	}
    	newWin.down('[name=url]').setValue(url);
    };
    
    var setSendConditionGridButton = function(){
		if(newWin){
    		var target = newWin.down('form').down('[itemId=grid-send-condition]');
    		var selection = target.getSelection();
    		if(selection.length > 0){
    			selection = selection[0];
    			target.down('[itemId=up]').enable();
				target.down('[itemId=up-top]').enable();
				target.down('[itemId=down]').enable();
				target.down('[itemId=down-buttom]').enable();
    			var index = sendConditionOperationStore.indexOf(selection);
    			if(index == 0){
    				target.down('[itemId=up]').disable();
    				target.down('[itemId=up-top]').disable();
    			}
    			if(index >= (sendConditionOperationStore.count() - 1)){
    				target.down('[itemId=down]').disable();
    				target.down('[itemId=down-buttom]').disable();
    			}
    			return;
    		} 
    		target.down('[itemId=up]').disable();
			target.down('[itemId=up-top]').disable();
			target.down('[itemId=down]').disable();
			target.down('[itemId=down-buttom]').disable();
    	}
	};
    
    var startFlagChangeHandler = function(){
		   if(newWin.down('[id=startflag-radio-1]').getValue() || newWin.down('[id=startflag-radio-3]').getValue()){
			   newWin.down('[id=delayregularcontainer]').setHidden(true);
				newWin.down('[id=delaytimecontainer]').setHidden(true);
				newWin.down('[id=delaydatecontainer]').setHidden(true);
				var target = newWin.down('[name=delayhour]');
	    		target.disable();
	    		target.clearInvalid();
	    		target = newWin.down('[name=delayMinute]');
	    		target.disable();
	    		target.clearInvalid();
	    		target = newWin.down('[name=delayD]');
	    		target.disable();
	    		target.clearInvalid();
	    		target = newWin.down('[name=delayT]');
	    		target.disable();
	    		target.clearInvalid();
		   }
		   if(newWin.down('[id=startflag-radio-2]').getValue()){
			   	newWin.down('[id=delayregularcontainer]').setHidden(false);
				newWin.down('[id=radio1]').fireEvent('change');
				newWin.down('[id=radio2]').fireEvent('change');
		   }
		   if(newWin.down('[id=startflag-radio-3]').getValue()){
			   newWin.down('[name=cronExpression]').disable();
			   newWin.down('[itemId=cronExpressionContainer]').setHidden(true);
		   } else {
			   newWin.down('[name=cronExpression]').enable();
			   newWin.down('[itemId=cronExpressionContainer]').setHidden(false);
		   }
    };
    
	var resetnewWin = function(){
		var updateflag = newWin.down('form').down('[name=id]').getValue().length > 0 ? true : false;
		
		newWin.down('[xtype=form]').reset();
		newWin.down('form').down('[name=actionType]').setHidden(false);
		var target = newWin.down('form').down('[name=dsid]');
		target.setHidden(true);
		target.disable();
		target = newWin.down('form').down('[itemId=grid-container]');
		target.setHidden(true);
		target = newWin.down('form').down('[itemId=grid-send-condition]');
		target.setHidden(true);
		target = newWin.down('form').down('[name=msid]');
		target.setHidden(true);
		target.clearInvalid();
		target = newWin.down('form').down('[name=smid]');
		target.setHidden(true);
		target.clearInvalid();
		target = newWin.down('form').down('[name=url]');
		target.setHidden(true);
		target.disable();
		target = newWin.down('[name=attachmentAsContent]');
		target.setHidden(true);
		target.disable();
		target = newWin.down('[id=vhostcontainer]');
		target.setHidden(true);
		target = newWin.down('[name=vprotocol]');
		target.setHidden(true);
		target.disable();
		target = newWin.down('[name=vhost]');
		target.setHidden(true);
		target.disable();
		target = newWin.down('[name=vport]');
		target.setHidden(true);
		target.disable();
		target = newWin.down('[name=vappname]');
		target.setHidden(true);
		target.disable();
		target = newWin.down('[name=vprgname]');
		target.setHidden(true);
		target.disable();
		target = newWin.down('[name=command]');
		target.setHidden(true);
		target.disable();
		target = newWin.down('[name=commandVariable]');
		target.setHidden(true);
		target.disable();
		target = newWin.down('[name=sqlSentence]');
		target.setHidden(true);
		target.disable();
		
		newWin.down('form').down('[name=startflag]').fireEvent('change');
		sendConditionStore.removeAll();
		
		if(updateflag && grid.getSelection().length > 0 && grid.getSelection()[0]){
			var record = grid.getSelection()[0];
			newWin.down('form').down('[name=id]').setValue(record.get('id'));
			newWin.down('form').down('[name=displayName]').setValue(record.get('displayName'));
			var actionType = record.get('actionType');
			newWin.down('form').down('[name=actionType]').setValue(actionType);
			newWin.down('form').down('[name=actionType]').fireEvent('select');
			//mail scan action
			if(actionType == 2){
				newWin.down('form').down('[name=dsid]').setValue(record.get('dsid'));
				target = newWin.down('form').down('[itemId=grid-container]');
				target.setHidden(false);
				target = newWin.down('form').down('[itemId=grid-send-condition]');
				target.setHidden(false);
				sendConditionStore.loadData(record.get('sendConditionList'));
				target = newWin.down('form').down('[name=msid]');
				target.setHidden(false);
				target.setValue(record.get('msid'));
				target = newWin.down('form').down('[name=smid]');
				target.setHidden(true);
				newWin.down('form').down('[name=attachmentAsContent]').setValue(record.get('attachmentAsContent'));
			}
			//MMS action
			if(actionType == 4){
				newWin.down('[name=url]').setValue(record.get('url'));
				if(newWin.down('[name=url]').isValid()){
					newWin.down('[name=url]').fireEvent('change');
				} else {
					newWin.down('[name=url]').setValue('');
					newWin.down('[name=vprgname]').setValue(record.get('url'));
				}
			}
			//run window command action
			if(actionType == 5){
				newWin.down('[name=command]').setValue(record.get('command'));
				newWin.down('[name=commandVariable]').setValue(record.get('commandVariable'));
			}
			//run sql action
			if(actionType == 7){
				newWin.down('form').down('[name=dsid]').setValue(record.get('dsid'));
				newWin.down('[name=sqlSentence]').setValue(record.get('sqlSentence'));
			}
			//short message action
			if(actionType == 9){
				newWin.down('form').down('[name=dsid]').setValue(record.get('dsid'));
				target = newWin.down('form').down('[itemId=grid-container]');
				target.setHidden(false);
				target = newWin.down('form').down('[itemId=grid-send-condition]');
				target.setHidden(false);
				sendConditionStore.loadData(record.get('sendConditionList'));
				target = newWin.down('form').down('[name=msid]');
				target.setHidden(true);
				target = newWin.down('form').down('[name=smid]');
				target.setHidden(false);
				target.setValue(record.get('smid'));
			}
			newWin.down('form').down('[name=actionType]').setHidden(true);
			newWin.down('[name=description]').setValue(record.get('description'));
			newWin.down('[name=cronExpression]').setValue(record.get('cronExpression'));
		}
		
		//set default value.
		if(newWin.down('[name=vprotocol]').getValue().length === 0 || newWin.down('[name=vhost]').getValue().length === 0 || !newWin.down('[name=vport]').getValue() || newWin.down('[name=vappname]').getValue().length === 0){
			Ext.Ajax.request({
				url: 'lastinput',
				success: function(response){
					var result = Ext.decode(response.responseText);
					if(result){
						if(newWin.down('[name=vprotocol]').getValue().length === 0){
							newWin.down('[name=vprotocol]').setValue(result.vprotocol);
						}
						if(newWin.down('[name=vhost]').getValue().length === 0){
							newWin.down('[name=vhost]').setValue(result.vhost);
						}
						if(!newWin.down('[name=vport]').getValue()){
							newWin.down('[name=vport]').setValue(result.vport);
						}
						if(newWin.down('[name=vappname]').getValue().length === 0){
							newWin.down('[name=vappname]').setValue(result.vappname);
						}
						urlassemble();
					}
				}
			});
		}
		
		newWin.down('form').getForm().clearInvalid();
	};
    
    var grid = Ext.create('Ext.grid.Panel', {
        renderTo: document.body,
        frame:true,
        autoWidth: true,
        viewConfig: {
            forceFit: true
        },
        frame: true,
        title: '计划任务列表',
        store: schedulestore,
        iconCls: 'icon-title',
        columns: [{
            text: 'ID',
            width: 50,
            sortable: true,
            dataIndex: 'id'
        },  {
            text: '显示名称',
            flex: 3,
            sortable: true,
            dataIndex: 'displayName'
        }, {
            text: '任务属性',
            sortable: true,
            dataIndex: 'scheduleType',
            renderer: function(v){
	    		return '<div class="schedule-status-container ' + (v == 'S' ? 'schedule-system-icon' :	'schedule-general-icon') + '"><span class="schedule-status-message">' + (v == 'S' ? '系统任务' :	'常规任务') + '<span></div>';
            }
        }, {
            text: '任务类型',
            flex: 2,
            sortable: true,
            dataIndex: 'actionDisplayName'
        }, {
            text: '计划任务执行规则',
            width: 120,
            sortable: true,
            dataIndex: 'cronExpression'
        }, {
        	text: '状态',
        	sortable: true,
        	dataIndex: 'scheduleStatus',
        	renderer: function(v){
        		var message = '';
        		if(v == 'NORMAL'){message = '正常'}
        		if(v == 'UNSCHEDULE'){message = '未执行'}
        		if(v == 'PAUSED'){message = '暂停'}
        		if(v == 'COMPLETE'){message = '已完成'}
        		if(v == 'ERROR'){message = '错误'}
        		if(v == 'BLOCKED'){message = '阻塞'}
        		return '<div class="schedule-status-container schedule-status-images-' + v + '"><span class="schedule-status-message schedule-status-message-' + v + '">' + message + '<span></div>';
        	}
        }, {
            text: '描述',
            flex: 2,
            sortable: true,
            dataIndex: 'description'
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
        tbar: [{
        	id: 'grid-add-button',
            text: '新建计划任务',
            iconCls: 'icon-new',
            handler: function(){
            	var position = ((Ext.getBody().getWidth()/2) - 500);
            	newWin.showAt(position < 0 ? 0 : position , 0);
            	newWin.down('form').down('[name=id]').setValue('');
            	resetnewWin();
            }
        }, {
        	text: '复制',
        	itemId: 'grid-icon-copy-button',
        	tooltip: '通过此功能可以快速复制配置信息几乎完全一致的复杂的计划任务',
            iconCls: 'icon-copy',
            handler: function(){
            	if(dataStoreValidation()){
            		newWin.show();
            		newWin.down('form').down('[name=id]').setValue(grid.getSelection()[0].get('id'));
            		resetnewWin();
            		newWin.down('form').down('[name=id]').setValue('');
            	}
        	}
        }, '-', {
        	text: '编辑',
        	itemId: 'grid-icon-edit-button',
        	iconCls: 'icon-edit',
        	disabled: true,
        	handler: function(){
        		if(dataStoreValidation()){
        			var position = ((Ext.getBody().getWidth()/2) - 500);
        			newWin.showAt(position < 0 ? 0 : position , 0);
        			newWin.down('form').down('[name=id]').setValue(grid.getSelection()[0].get('id'));
        			resetnewWin();
            	}
        	}
        },'-', {
        	text: '启动',
        	itemId: 'grid-icon-start-button',
        	iconCls: 'icon-start',
        	tooltip: '将当前任务加入计划任务列表中，而不是立即执行此任务',
    		tooltipType: 'title',
        	disabled: true,
        	handler: function(){
        		scheduleOperation('run');
        	}
        }, {
        	text: '执行一次',
        	itemId: 'grid-icon-once-button',
        	iconCls: 'icon-once',
        	tooltip: '立即运行当前任务一次，无论其是否在计划之内',
    		tooltipType: 'title',
        	disabled: true,
        	handler: function(){
        		scheduleOperation('runonce', true);
        	}
        }, {
        	text: '暂停',
        	itemId: 'grid-icon-pause-button',
        	iconCls: 'icon-pause',
        	tooltip: '暂停任务后，如系统重新启动，则任务会恢复运行',
    		tooltipType: 'title',
        	disabled: true,
        	handler: function(){
        		scheduleOperation(grid.getSelection()[0].get('scheduleStatus') == 'PAUSED' ? 'resume' : 'pause');
        	}
        }, {
        	text: '停止',
        	itemId: 'grid-icon-stop-button',
        	iconCls: 'icon-stop',
        	tooltip: '停止此任务执行，即使系统重启也不会恢复',
    		tooltipType: 'title',
        	disabled: true,
        	handler: function(){
        		scheduleOperation('stop');
        	}
        }, {
        	text: '重置',
        	itemId: 'grid-icon-reset-button',
        	iconCls: 'icon-reset',
        	tooltip: '如果发现有错误状态的计划任务，可以通过此功能尝试恢复到最初正常执行的状态上',
    		tooltipType: 'title',
        	disabled: true,
        	handler: function(){
        		scheduleOperation('reset');
        	}
        }, '-', {
            text: '删除',
            itemId: 'grid-icon-delete-button',
            iconCls: 'icon-delete',
            tooltip: '删除此任务，会停止任务运行，且不可恢复',
    		tooltipType: 'title',
            disabled: true,
            handler: function(){
            	scheduleOperation('remove');
            }
        },'-',{
        	text: '刷新',
        	iconCls: 'icon-refresh',
        	handler: function(){
        		scheduleFilter();
        	}
        },'-',{
        	boxLabel: '显示系统任务（系统任务无法启动，停止，编辑，运行等操作）',
        	xtype: 'checkbox',
        	itemId: 'grid-show-system-schedule-list',
        	listeners: {
        		change: function(){
        			scheduleFilter();
        		}
        	}
        }, '-','状态:', {
        	boxLabel: '正常任务',
        	xtype: 'checkbox',
        	itemId: 'grid-show-normal-schedule-list',
        	checked: true,
        	listeners: {
        		change: function(){
        			scheduleFilter();
        		}
        	}
        }, {
        	boxLabel: '未执行任务',
        	xtype: 'checkbox',
        	itemId: 'grid-show-unschedule-list',
        	checked: true,
        	listeners: {
        		change: function(){
        			scheduleFilter();
        		}
        	}
        }, {
        	boxLabel: '其它任务',
        	xtype: 'checkbox',
        	itemId: 'grid-show-other-schedule-list',
        	checked: true,
        	listeners: {
        		change: function(){
        			scheduleFilter();
        		}
        	}
        }],
        listeners: {
        	rowdblclick: function(){
        		if(dataStoreValidation()){
        			var position = ((Ext.getBody().getWidth()/2) - 500);
        			newWin.showAt(position < 0 ? 0 : position , 0);
        			newWin.down('form').down('[name=id]').setValue(grid.getSelection()[0].get('id'));
        			resetnewWin();
            	}
        	}
        }
    });
    
    var dataStoreValidation = function(){
		if(grid.getSelection().length > 0 && grid.getSelection()[0].get('scheduleType') != 'S'){
			var actionType = grid.getSelection()[0].get('actionType');
			//mail scan action
			if(actionType == 2){
    			if(dsStore.getCount() === 0){
    				Ext.Msg.show({
    						title: '提示', 
    						message: '创建此计划任务，需要数据源支持，现在还没有相关配置信息，是否现在就去创建？',
    						buttons: Ext.Msg.OKCANCEL,
    						icon: Ext.Msg.QUESTION,
    						buttonText : {
								ok : '确定',
								cancel : '取消'
							},
    						fn: function(btn){
            					if(btn === 'ok'){
            						window.location.href = "/dsm/?_t=" + new Date().toLocaleString();
            					}
    						}
    				});
    				return false;
    			}
    			if(msStore.getCount() === 0){
    				Ext.Msg.show({
    					title: '提示', 
    					message: '创建此计划任务，需要邮件发送服务器支持，现在还没有相关配置信息，是否现在就去创建？',
    					buttons: Ext.Msg.OKCANCEL,
    					icon: Ext.Msg.QUESTION,
    					buttonText : {
    						ok : '确定',
    						cancel : '取消'
    					},
    					fn: function(btn){
    						if(btn === 'ok'){
    							window.location.href = "/msm/?_t=" + new Date().toLocaleString();
    						}
    					}
    				});
    				return false;
    			}
			}
			//sql runner action
			if(actionType == 7){
				if(dsStore.getCount() === 0){
					Ext.Msg.show({
						title: '提示', 
						message: '创建此计划任务，需要数据源支持，现在还没有相关配置信息，是否现在就去创建？',
						buttons: Ext.Msg.OKCANCEL,
						icon: Ext.Msg.QUESTION,
						buttonText : {
							ok : '确定',
							cancel : '取消'
						},
						fn: function(btn){
							if(btn === 'ok'){
								window.location.href = "/dsm/?_t=" + new Date().toLocaleString();
							}
						}
					});
					return false;
				}
			}
			//short message scan action
			if(actionType == 9){
    			if(dsStore.getCount() === 0){
    				Ext.Msg.show({
    						title: '提示', 
    						message: '创建此计划任务，需要数据源支持，现在还没有相关配置信息，是否现在就去创建？',
    						buttons: Ext.Msg.OKCANCEL,
    						icon: Ext.Msg.QUESTION,
    						buttonText : {
								ok : '确定',
								cancel : '取消'
							},
    						fn: function(btn){
            					if(btn === 'ok'){
            						window.location.href = "/dsm/?_t=" + new Date().toLocaleString();
            					}
    						}
    				});
    				return false;
    			}
    			if(smStore.getCount() === 0){
    				Ext.Msg.show({
    					title: '提示', 
    					message: '创建此计划任务，需要短信通道支持，现在还没有相关配置信息，是否现在就去创建？',
    					buttons: Ext.Msg.OKCANCEL,
    					icon: Ext.Msg.QUESTION,
    					buttonText : {
    						ok : '确定',
    						cancel : '取消'
    					},
    					fn: function(btn){
    						if(btn === 'ok'){
    							window.location.href = "/smm/?_t=" + new Date().toLocaleString();
    						}
    					}
    				});
    				return false;
    			}
			}
			return true;
		}
		return false;
	};
    
    var scheduleOperation = function(action, showMessage){
    	if(grid.getSelection().length > 0){
    		var mask = new Ext.LoadMask({
    			msg    : '请稍等...',
    			target : grid
    		});
    		mask.show();
    		Ext.Ajax.request({
    			url: action,
    			params: {
    				id: grid.getSelection()[0].get('id')
    			},
    			success: function(response){
    				var result = Ext.decode(response.responseText);
    				mask.destroy();
    				if(result.success){
    					schedulestore.reload();
    					if(showMessage){
    						Ext.toast({
    							html: '请求已提交，运行结果稍后会体现在系统运行状态中',
    							closable: false,
    							align: 't',
    							slideInDuration: 400,
    							autoCloseDelay: 5000
    						});
    					}
    					return;
    				}
    				Ext.Msg.show({
    					title: '提示', 
    					message: result.message + '是否现在就进行更改？',
    					buttons: Ext.Msg.OKCANCEL,
    					icon: Ext.Msg.QUESTION,
    					buttonText : {
    						ok : '确定',
    						cancel : '取消'
    					},
    					fn: function(btn){
    						if(btn === 'ok'){
    							if(grid.getSelection().length > 0){
    								if(dataStoreValidation()){
    				        			var position = ((Ext.getBody().getWidth()/2) - 500);
    				        			newWin.showAt(position < 0 ? 0 : position , 0);
    				        			newWin.down('form').down('[name=id]').setValue(grid.getSelection()[0].get('id'));
    				        			resetnewWin();
    				            	}
    							}
    						}
    					}
    				});
    			},
    			failure: function(){
    				mask.destroy();
    			}
    		});
    	}
    };
    
    var scheduleFilter = function(){
    	schedulestore.reload({
    		params: {
    			s: grid.down('[itemId=grid-show-system-schedule-list]').getValue(),
    			n: grid.down('[itemId=grid-show-normal-schedule-list]').getValue(),
    			u: grid.down('[itemId=grid-show-unschedule-list]').getValue(),
    			o: grid.down('[itemId=grid-show-other-schedule-list]').getValue()
    		}
    	});
    };
    
    var setgridbutton = function(){
    	var selections = grid.getSelection();
    	if(selections.length === 0 || selections[0].get('scheduleType') == 'S'){
    		grid.down('[itemId=grid-icon-copy-button]').setDisabled(true);
    		grid.down('[itemId=grid-icon-start-button]').setDisabled(true);
    		grid.down('[itemId=grid-icon-pause-button]').setDisabled(true);
    		grid.down('[itemId=grid-icon-stop-button]').setDisabled(true);
    		grid.down('[itemId=grid-icon-delete-button]').setDisabled(true);
    		grid.down('[itemId=grid-icon-edit-button]').setDisabled(true);
    		grid.down('[itemId=grid-icon-once-button]').setDisabled(true);
    		grid.down('[itemId=grid-icon-reset-button]').setDisabled(true);
    	} else {
    		grid.down('[itemId=grid-icon-copy-button]').setDisabled(false);
    		grid.down('[itemId=grid-icon-edit-button]').setDisabled(false);
    		grid.down('[itemId=grid-icon-once-button]').setDisabled(false);
    		grid.down('[itemId=grid-icon-delete-button]').setDisabled(false);
    		grid.down('[itemId=grid-icon-reset-button]').setDisabled(true);
    		var record = selections[0];
    		if(record.get('scheduleStatus') == 'NORMAL'){
    			grid.down('[itemId=grid-icon-start-button]').setDisabled(true);
    			grid.down('[itemId=grid-icon-stop-button]').setDisabled(false);
    			grid.down('[itemId=grid-icon-pause-button]').setDisabled(false);
    		} else {
    			if(record.get('scheduleStatus') == 'PAUSED') {
    				grid.down('[itemId=grid-icon-pause-button]').setDisabled(false);
    				grid.down('[itemId=grid-icon-start-button]').setDisabled(true);
    				grid.down('[itemId=grid-icon-stop-button]').setDisabled(false);
    			} else {
    				grid.down('[itemId=grid-icon-pause-button]').setDisabled(true);
    				grid.down('[itemId=grid-icon-start-button]').setDisabled(false);
    				grid.down('[itemId=grid-icon-stop-button]').setDisabled(true);
    				if(record.get('scheduleStatus') == 'ERROR'){
    					grid.down('[itemId=grid-icon-reset-button]').setDisabled(false);
    				}
    			}
    		}
    		grid.down('[itemId=grid-icon-pause-button]').setText(record.get('scheduleStatus') == 'PAUSED' ? '恢复' : '暂停');
    	}
    };
    
    grid.getSelectionModel().on('selectionchange', function(selModel, selections){
    	 setgridbutton();
    });
    
});