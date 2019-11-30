Ext.require(['Ext.data.*', 'Ext.grid.*','Ext.layout.*']);

Ext.define('schedule', {
    extend: 'Ext.data.Model',
    fields: [{
        name: 'id',
        type: 'int'
    }, 
    'displayName',
    'actionType',
    'scheduleType',
    'actionDisplayName',
    'executeCount',
    'successCount',
    'failedCount',
    'scheduleStatus',
    'description',
    'status']
    
});

//date add function
Date.prototype.DateAdd = function(strInterval, Number) {   
    var dtTmp = this;  
    switch (strInterval) {   
        case 's' :return new Date(Date.parse(dtTmp) + (1000 * Number));  
        case 'n' :return new Date(Date.parse(dtTmp) + (60000 * Number));  
        case 'h' :return new Date(Date.parse(dtTmp) + (3600000 * Number));  
        case 'd' :return new Date(Date.parse(dtTmp) + (86400000 * Number));  
        case 'w' :return new Date(Date.parse(dtTmp) + ((86400000 * 7) * Number));  
        case 'q' :return new Date(dtTmp.getFullYear(), (dtTmp.getMonth()) + Number*3, dtTmp.getDate(), dtTmp.getHours(), dtTmp.getMinutes(), dtTmp.getSeconds());  
        case 'm' :return new Date(dtTmp.getFullYear(), (dtTmp.getMonth()) + Number, dtTmp.getDate(), dtTmp.getHours(), dtTmp.getMinutes(), dtTmp.getSeconds());  
        case 'y' :return new Date((dtTmp.getFullYear() + Number), dtTmp.getMonth(), dtTmp.getDate(), dtTmp.getHours(), dtTmp.getMinutes(), dtTmp.getSeconds());  
    }  
};

Ext.onReady(function(){
	
	var timeStore = Ext.create('Ext.data.Store', {
		fields: ['value', 'name'],
		data: [
		       	{value: '0', name: '最近一小时'},
		        {value: '1', name: '最近一天内'},
		        {value: '2', name: '最近一周内'},
		        {value: '3', name: '最近一月内'},
		        {value: '4', name: '最近三月内'},
		        {value: '5', name: '最近半年内'},
		        {value: '6', name: '最近一年内'},
		        {value: '7', name: '所有'},
		        {value: '8', name: '自定义时间'},
		]
	});
	
	var schedulestore = Ext.create('Ext.data.JsonStore', {
        autoSync: true,
        model: 'schedule',
        proxy: {
            type: 'ajax',
            url: 'scheduleList'
        },
        listeners: {
        	endUpdate : function(){
        		recordReload();
        	}
        }
    });
	
	var recordStore = Ext.create('Ext.data.JsonStore', {
		fields: ['id', 'scheduleid','status','executeTime','runtimeCount','message'],
        autoSync: true,
		proxy: {
			type: 'ajax',
            url: 'recordlist'
		},
		listeners: {
			endUpdate: function(){
				panel.down('[itemId=recordGrid]').down('[itemId=record-refresh-button]').enable();
			}
		}
	});
	
	var mailstore = Ext.create('Ext.data.JsonStore', {
		model : 'Ext.data.Model',
		autoSync: true,
		fields : ['id', 'seq', 'from', 'to', 'status', 'subject', 'message', 'returnCode', 'content', 'attachment', 'formatSendTime', 'formatCreateTime' ],
		proxy : {
			type : 'ajax',
			url : 'mailLogList'
		}
	});
	
	var smstore = Ext.create('Ext.data.JsonStore', {
		model : 'Ext.data.Model',
		autoSync: true,
		fields : [ 'id', 'seq', 'from', 'to', 'status',  'subject', 'message', 'formatSendTime', 'formatCreateTime' ],
		proxy : {
			type : 'ajax',
			url : 'smLogList'
		}
	});
	
	var refreshstatusinfo = function(){
		Ext.Ajax.request({
			url : 'statusinfo',
			success : function(response, opts) {
				var result = Ext.decode(response.responseText);
				panel.down('[itemId=taskCount]').setValue(result.taskCount);
				panel.down('[itemId=executeCount]').setValue(result.executeCount);
				panel.down('[itemId=successCount]').setValue(result.successCount);
				panel.down('[itemId=failedCount]').setValue(result.failedCount);
				if(result.taskCount === 0 && result.executeCount == 0){
        			var mask = new Ext.LoadMask({
        			    msg    : '请稍等...',
        			    target : panel
        			});
        			mask.show();
        			Ext.Msg.show({
						title: '提示', 
						message: '系统中不存在运行中的计划任务和相关记录，是否现在就去创建一个计划任务？',
						buttons: Ext.Msg.OKCANCEL,
						icon: Ext.Msg.QUESTION,
						buttonText : {
							ok : '确定',
							cancel : '取消'
						},
						fn: function(btn){
        					if(btn === 'ok'){
        						window.location.href = "/schedule/?_t=" + new Date().toLocaleString();
        					} else {
        						mask.destroy();
        					}
						}
        			});
				}
			}
		});
	}
	
	var panel = Ext.create('Ext.panel.Panel',{
		renderTo: document.body,
		title: '系统运行状态(仅显示非系统任务运行统计)',
		autoWidth: true,
        viewConfig: {
            forceFit: true
        },
        frame: true,
        scrollable: true,
        defaults: {
            border: true
        },
        layout: {
            type: 'vbox'
        },
        items: [
            {
            	xtype: 'panel',
            	width: '99%',
    			defaults : {
    				labelAlign : 'right',
    				padding: '10 0 0 0',
    				labelWidth: 110
    			},
    			layout: {
    	            type: 'table',
    	            columns: 5,
    	            tableAttrs: {
    	                style: {
    	                    width: '100%',
    	                    height: '100%'
    	                }
    	            }
    	        },
    	        defaultType : 'displayfield',
    			items : [ {
    				itemId : 'taskCount',
    				fieldLabel : '计划中的总任务数',
    			}, {
    				itemId : 'executeCount',
    				fieldLabel : '任务执行总次数',
    			}, {
    				itemId : 'successCount',
    				fieldLabel : '任务执行成功数量',
    			}, {
    				itemId : 'failedCount',
    				fieldLabel : '任务执行失败数量',
    			}, {
    				xtype: 'button',
    				text: '刷新',
    				handler: function(){refreshstatusinfo();}
    			}],
    			listeners: [{
    				afterrender: function(){
    					refreshstatusinfo();
    				}
    			}]
            }, {
            	xtype: 'grid',
            	itemId: 'scheduleGrid',
            	frame:true,
            	width: '100%',
            	height: 200,
                viewConfig: {
                    forceFit: true
                },
                frame: true,
                store: schedulestore,
                tbar: [{
            		xtype: 'combo',
            		itemId: 'timescope',
            		labelWidth: 80,
            		fieldLabel: '设定时间范围:',
            		store: timeStore,
            		queryMode: 'local',
            		labelAlign: 'right',
	                displayField: 'name',
	                valueField: 'value',
	                value: 7,
	                editable: false,
	                listeners: {
	                	select: function(){
	                		var flag = panel.down('[itemId=timescope]').getValue();
	                		scheduleFilter();
	                		if(flag == 8){
	                			panel.down('[name=fromDate]').show();
	                			panel.down('[name=fromTime]').show();
	                			panel.down('[name=toDate]').show();
	                			panel.down('[name=toTime]').show();
	                		} else {
	                			panel.down('[name=fromDate]').hide();
	                			panel.down('[name=fromTime]').hide();
	                			panel.down('[name=toDate]').hide();
	                			panel.down('[name=toTime]').hide();
	                		}
	                	}
	                }
            	}, '-', {
	            	xtype: 'datefield',
	            	fieldLabel: '开始时间',
	            	labelAlign: 'right',
	            	labelWidth: 70,
	            	maxValue: new Date(),
	            	editable: false,
	            	hidden: true,
	            	name: 'fromDate',
	            	format: 'Y-m-d',
	            	listeners: {
	            		change: function(){
	            			scheduleFilter();
	            		}
	            	}
	            }, {
	            	xtype: 'timefield',
	            	editable: false,
	            	hidden: true,
	            	name: 'fromTime',
	            	value: '00:00:00',
	            	format: 'H:i:s',
	            	listeners: {
	            		change: function(){
	            			scheduleFilter();
	            		}
	            	}
	            }, {
	            	xtype: 'datefield',
	            	fieldLabel: '结束时间',
	            	labelAlign: 'right',
	            	labelWidth: 70,
	            	maxValue: new Date(),
	            	value: new Date(),
	            	editable: false,
	            	hidden: true,
	            	name: 'toDate',
	            	format: 'Y-m-d',
	            	listeners: {
	            		change: function(){
	            			scheduleFilter();
	            		}
	            	}
	            }, {
	            	xtype: 'timefield',
	            	editable: false,
	            	hidden: true,
	            	value: new Date(),
	            	name: 'toTime',
	            	format: 'H:i:s',
	            	listeners: {
	            		change: function(){
	            			scheduleFilter();
	            		}
	            	}
	            }, '-', {
	            	text: '刷新',
	            	iconCls: 'icon-refresh',
	            	handler: function(){
	            		scheduleFilter();
	            	}
	            }, '-', {
                	xtype: 'checkbox',
                	itemId: 'showNormalflag',
                	boxLabel: '仅显示正常的计划任务',
	            	listeners: {
	            		change: function(){
	            			scheduleFilter();
	            		}
	            	}
            	}, {
            		xtype: 'checkbox',
                	itemId: 'showSystemflag',
                	boxLabel: '显示系统计划任务',
	            	listeners: {
	            		change: function(){
	            			scheduleFilter();
	            		}
	            	}
            	}],
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
                    text: '任务类型',
                    flex: 2,
                    sortable: true,
                    dataIndex: 'actionDisplayName'
                }, {
                	text: '执行总次数',
                	sortable: true,
                	dataIndex: 'executeCount'
                }, {
                	text: '执行成功次数',
                	sortable: true,
                	dataIndex: 'successCount'
                }, {
                	text: '执行失败次数',
                	sortable: true,
                	dataIndex: 'failedCount',
                	renderer: function(v){
                		return '<div><font color="' + (v > 0 ? 'red' : 'green') + '">' + v + '</font></div>';
                	}
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
                }],
                listeners: {
                	afterrender: function(){
                		setTimeout(function(){
                			panel.down('[itemId=timescope]').fireEvent('select');
                		},1);
                	}
                }
            }, {
            	xtype: 'tabpanel',
            	itemId: 'recordListpanel',
            	width: '100%',
            	items: [{
            		title: '执行记录(最近1000条)',
                	xtype: 'grid',
                	itemId: 'recordGrid',
                	store: recordStore,
                	frame:true,
                	width: '100%',
                	height: 500,
                    viewConfig: {
                        forceFit: true
                    },
                    frame: true,
                    tbar: [{
                    	xtype: 'checkbox',
                    	itemId: 'showflag',
                    	boxLabel: '默认显示失败的任务执行记录，勾选此项显示所有执行信息',
    	            	listeners: {
    	            		change: function(){
    	            			recordReload();
    	            		}
    	            	}
                	}, '-', {
		            	text: '刷新',
		            	itemId: 'record-refresh-button',
		            	iconCls: 'icon-refresh',
		            	disabled: true,
		            	handler: function(){
		            		recordReload();
		            	}
                	}],
                    columns: [{
                        text: 'ID',
                        width: 50,
                        sortable: true,
                        dataIndex: 'id'
                    },  {
                        text: '执行结果',
                        sortable: true,
                        dataIndex: 'status',
                        renderer: function(v){
        					return '<div><font color="' + (v == 'Y' ? 'green' : 'red') + '">' + (v == 'Y' ? '成功' : '失败') + '</font></div>';
            			}
                    }, {
                        text: '耗时（毫秒）',
                        sortable: true,
                        dataIndex: 'runtimeCount'
                    }, {
                    	text: '消息',
                    	flex: 1,
                    	sortable: true,
                    	dataIndex: 'message'
                    }, {
                    	text: '执行时间',
                    	width: 150,
                    	sortable: true,
                    	dataIndex: 'executeTime'
                    }],
                    plugins: [{
                    	ptype: 'rowexpander',
                    	selectRowOnExpand: true,
                    	rowBodyTpl: new Ext.XTemplate(
                    			'<p><b>ID:</b> {id}</p>',
                    			'<p><b>执行结果:</b> {status:this.statusFormat}</p>',
                    			'<p><b>耗时:</b> {runtimeCount} 毫秒</p>',
                    			'<p><b>消息:</b> {message}</p>',
                    			'<p><b>执行时间:</b> {executeTime}</p>',
                    			{
                    				statusFormat: function(v){
                    					return '<div><font color="' + (v == 'Y' ? 'green' : 'red') + '">' + (v == 'Y' ? '成功' : '失败') + '</font></div>';
                        			}
                    			}
                    			)
                    }]
                }, {
                	title: '邮件发送记录(最近1000条)',
                	xtype: 'grid',
                	itemId: 'mailGrid',
                	disabled: true,
                	store: mailstore,
                	frame:true,
                	width: '100%',
                	height: 500,
                    viewConfig: {
                        forceFit: true
                    },
                    tbar: [{
                    	xtype: 'checkbox',
                    	itemId: 'showMailflag',
                    	boxLabel: '默认显示失败的任务执行记录，勾选此项显示所有执行信息',
    	            	listeners: {
    	            		change: function(){
    	            			mailGridReload();
    	            		}
    	            	}
                	}, {
		            	text: '刷新',
		            	itemId: 'mail-refresh-button',
		            	iconCls: 'icon-refresh',
		            	handler: function(){
		            		mailGridReload();
		            	}
                	}, '-', {
                    	xtype: 'displayfield',
                    	itemId: 'mailCountLabel',
                    	fieldLabel: '总计发送数量',
                    	labelWidth: 90,
                    	height: 18,
                    	value: 0
                	}, '-', {
                		xtype: 'displayfield',
                    	itemId: 'mailSuccessCountLabel',
                    	fieldLabel: '发送成功数量',
                    	labelWidth: 80,
                    	height: 18,
                    	value: 0
                	}, '-', {
                		xtype: 'displayfield',
                    	itemId: 'mailFailedCountLabel',
                    	fieldLabel: '发送失败数量',
                    	labelWidth: 80,
                    	height: 18,
                    	value: 0
                	}],
        			columns : [ {
        				text : "ID",
        				width : 120,
        				dataIndex : 'id'
        			}, {
        				text : "编号",
        				width : 120,
        				dataIndex : 'seq'
        			}, {
        				text : "发件人",
        				flex : 1,
        				dataIndex : 'from'
        			}, {
        				text : "收件人",
        				flex : 1,
        				dataIndex : 'to'
        			}, {
        				text: '状态',
        				dataIndex: 'status',
        				renderer: function(v){
        					return '<div><font color="' + (v == 0 ? 'green' : v == -2 ? 'gray' : v == 1 ? 'blue' : 'red') + '">' + (v == 0 ? '成功' : v == -2 ? '无效' : v == 1 ? '排队中' : '失败') + '</font></div>';
        				}
        			}, {
        				text : "标题",
        				flex : 1,
        				dataIndex : 'subject'
        			}, {
        				text : "消息",
        				flex : 2,
        				dataIndex : 'message'
        			}, {
        				text : "发送时间",
        				width : 125,
        				dataIndex : 'formatSendTime'
        			}, {
        				text : '创建时间',
        				width : 125,
        				dataIndex : 'formatCreateTime'
        			} ],
        			plugins: [{
                    	ptype: 'rowexpander',
                    	selectRowOnExpand: true,
                    	rowBodyTpl: new Ext.XTemplate(
                    			'<p><b>ID:</b> {id}</p>',
                    			'<p><b>编号:</b> {seq}</p>',
                    			'<p><b>发件人:</b> {from}</p>',
                    			'<p><b>收件人:</b> {to}</p>',
                    			'<p><b>状态:</b> {status:this.statusFormat}</p>',
                    			'<p><b>标题:</b> {subject}</p>',
                    			'<p><b>附件路径:</b> {attachment}</p>',
                    			'<p><b>消息:</b> {message}</p>',
                    			'<p><b>执行时间:</b> {formatSendTime}</p>',
                    			'<p><b>任务创建时间:</b> {formatCreateTime}</p>',
                    			{
                    				statusFormat: function(v){
                    					return '<div><font color="' + (v == 0 ? 'green' : v == -2 ? 'gray' : v == 1 ? 'blue' : 'red') + '">' + (v == 0 ? '成功' : v == -2 ? '无效' : v == 1 ? '排队中' : '失败') + '</font></div>';
                        			}
                    			}
                    			)
                    }]
                }, {
                	title: '短信发送记录(最近1000条)',
                	xtype: 'grid',
                	itemId: 'smGrid',
                	disabled: true,
                	store: smstore,
                	frame:true,
                	width: '100%',
                	height: 500,
                    viewConfig: {
                        forceFit: true
                    },
                    tbar: [{
                    	xtype: 'checkbox',
                    	itemId: 'showSmflag',
                    	boxLabel: '默认显示失败的任务执行记录，勾选此项显示所有执行信息',
    	            	listeners: {
    	            		change: function(){
    	            			smGridReload();
    	            		}
    	            	}
                	}, {
		            	text: '刷新',
		            	itemId: 'sm-refresh-button',
		            	iconCls: 'icon-refresh',
		            	handler: function(){
		            		smGridReload();
		            	}
                	}, '-', {
                    	xtype: 'displayfield',
                    	itemId: 'smCountLabel',
                    	fieldLabel: '总计发送数量',
                    	labelWidth: 90,
                    	height: 18,
                    	value: 0
                	}, '-', {
                		xtype: 'displayfield',
                    	itemId: 'smSuccessCountLabel',
                    	fieldLabel: '发送成功数量',
                    	labelWidth: 80,
                    	height: 18,
                    	value: 0
                	}, '-', {
                		xtype: 'displayfield',
                    	itemId: 'smFailedCountLabel',
                    	fieldLabel: '发送失败数量',
                    	labelWidth: 80,
                    	height: 18,
                    	value: 0
                	}],
        			columns : [ {
        				text : "ID",
        				width : 120,
        				dataIndex : 'id'
        			}, {
        				text : "编号",
        				width : 120,
        				dataIndex : 'seq'
        			}, {
        				text : "发送号码",
        				flex : 1,
        				dataIndex : 'from'
        			}, {
        				text : "接收手机号",
        				flex : 1,
        				dataIndex : 'to'
        			}, {
        				text: '状态',
        				dataIndex: 'status',
        				renderer: function(v){
        					return '<div><font color="' + (v == 0 ? 'green' : v == -2 ? 'gray' : 'red') + '">' + (v == 0 ? '成功' : v == -2 ? '无效' : '失败') + '</font></div>';
        				}
        			}, {
        				text : "消息",
        				flex : 2,
        				dataIndex : 'message'
        			}, {
        				text : "发送时间",
        				width : 125,
        				dataIndex : 'formatSendTime'
        			}, {
        				text : '创建时间',
        				width : 125,
        				dataIndex : 'formatCreateTime'
        			} ],
        			plugins: [{
                    	ptype: 'rowexpander',
                    	rowBodyTpl: new Ext.XTemplate(
                    			'<p><b>ID:</b> {id}</p>',
                    			'<p><b>编号:</b> {seq}</p>',
                    			'<p><b>发送号码:</b> {from}</p>',
                    			'<p><b>接收手机号:</b> {to}</p>',
                    			'<p><b>状态:</b> {status:this.statusFormat}</p>',
                    			'<p><b>返回状态码:</b> {returnCode}',
                    			'<p><b>发送内容:</b> {content}</p>',
                    			'<p><b>消息:</b> {message}</p>',
                    			'<p><b>执行时间:</b> {formatSendTime}</p>',
                    			'<p><b>任务创建时间:</b> {formatCreateTime}</p>',
                    			{
                    				statusFormat: function(v){
                    					return '<font color="' + (v == 0 ? 'green' : v == -2 ? 'gray' : 'red') + '">' + (v == 0 ? '成功' : v == -2 ? '无效' : '失败') + '</font>';
                    				}
                    			}
                    			)
                    }]
                }]
            }
        ]
	});
	
	var getTimescope = function(){

		var flag = panel.down('[itemId=timescope]').getValue();
		var from = new Date();
		var to = new Date();
		to.setMilliseconds(0);
		
		if(flag == 8){
			var fromDate = panel.down('[name=fromDate]').getValue();
			var fromTime = panel.down('[name=fromTime]').getValue();
			if(fromDate){
				from.setFullYear(fromDate.getFullYear(), fromDate.getMonth(), fromDate.getDate());
				if(fromTime){
					from.setHours(fromTime.getHours(), fromTime.getMinutes(), fromTime.getSeconds());
				}
			}
			
			var toDate = panel.down('[name=toDate]').getValue();
			var toTime = panel.down('[name=toTime]').getValue();
			if(toDate){
				to.setFullYear(toDate.getFullYear(), toDate.getMonth(), toDate.getDate());
				if(toTime){
					to.setHours(toTime.getHours(), toTime.getMinutes(), toTime.getSeconds());
				}
			}
		} else {
			if(flag == 0){
				from = from.DateAdd('h', -1);
			}
			if(flag == 1){
				from = from.DateAdd('d', -1);
			}
			if(flag == 2){
				from = from.DateAdd('w', -1);
			}
			if(flag == 3){
				from = from.DateAdd('m', -1);
			}
			if(flag == 4){
				from = from.DateAdd('w', -3);
			}
			if(flag == 5){
				from = from.DateAdd('w', -6);
			}
			if(flag == 6){
				from = from.DateAdd('y', -1);
			}
			if(flag == 7){
				from = null;
			}
		}
		return {from: from == null ? null : from.getTime(), to : to.getTime()};
	}
	
	var scheduleFilter = function(){
		var params = getTimescope();
		if(params.from > params.to){
			Ext.toast({
	            html: '结束时间不能早于开始时间。',
	            closable: false,
	            align: 't',
	            slideInDuration: 400,
	        });
			return;
		}
		
		params.showNormalflag = panel.down('[itemId=showNormalflag]').getValue();
		params.showSystemflag = panel.down('[itemId=showSystemflag]').getValue();
		
		schedulestore.reload({
			params: params
		});
	}
	
	panel.down('[itemId=scheduleGrid]').getSelectionModel().on('selectionchange', function(selModel, selections){
		if(selections.length > 0){
			recordReload();
		}
	});
	
	var recordReload = function(){
		var selections = panel.down('[itemId=scheduleGrid]').getSelection();
		if(selections.length > 0){
			var record = selections[0];
			var params = getTimescope();
			params.id = record.get('id');
			params.flag = panel.down('[itemId=showflag]').getValue();
			
			recordStore.reload({
				params: params
			});
			
			//set mail log if it is mail scan
			if(record.get('actionType') == 2){
				panel.down('[itemId=mailGrid]').enable();
				if(panel.down('[itemId=recordListpanel]').getLayout().getActiveItem().isDisabled){
					panel.down('[itemId=recordListpanel]').getLayout().setActiveItem(0);
				}
				panel.down('[itemId=smGrid]').disable();
				
				if(panel.down('[itemId=recordListpanel]').down('[itemId=mailGrid]') &&
						panel.down('[itemId=recordListpanel]').down('[itemId=mailGrid]').down('[itemId=mail-refresh-button]') &&
						panel.down('[itemId=recordListpanel]').down('[itemId=mailGrid]').down('[itemId=mail-refresh-button]').getEl() &&
						panel.down('[itemId=recordListpanel]').down('[itemId=mailGrid]').down('[itemId=mail-refresh-button]').getEl().down('.x-mask')){
					panel.down('[itemId=recordListpanel]').down('[itemId=mailGrid]').down('[itemId=mail-refresh-button]').getEl().down('.x-mask').destroy();
				}
				
				mailGridReload();
				return;
			}
			
			//set short message log if is short message scan
			if(record.get('actionType') == 9){
				panel.down('[itemId=mailGrid]').disable();
				panel.down('[itemId=smGrid]').enable();
				if(panel.down('[itemId=recordListpanel]').getLayout().getActiveItem().isDisabled){
					panel.down('[itemId=recordListpanel]').getLayout().setActiveItem(0);
				}
				
				if(panel.down('[itemId=recordListpanel]').down('[itemId=smGrid]') &&
						panel.down('[itemId=recordListpanel]').down('[itemId=smGrid]').down('[itemId=sm-refresh-button]') &&
						panel.down('[itemId=recordListpanel]').down('[itemId=smGrid]').down('[itemId=sm-refresh-button]').getEl() &&
						panel.down('[itemId=recordListpanel]').down('[itemId=smGrid]').down('[itemId=sm-refresh-button]').getEl().down('.x-mask')){
					panel.down('[itemId=recordListpanel]').down('[itemId=smGrid]').down('[itemId=sm-refresh-button]').getEl().down('.x-mask').destroy();
				}
				
				smGridReload();
				return;
			}
			
			//batch job action
			if(record.get('actionType') == 3){
				panel.down('[itemId=mailGrid]').enable();
				panel.down('[itemId=smGrid]').enable();
				
				if(panel.down('[itemId=recordListpanel]').down('[itemId=mailGrid]') &&
						panel.down('[itemId=recordListpanel]').down('[itemId=mailGrid]').down('[itemId=mail-refresh-button]') &&
						panel.down('[itemId=recordListpanel]').down('[itemId=mailGrid]').down('[itemId=mail-refresh-button]').getEl() &&
						panel.down('[itemId=recordListpanel]').down('[itemId=mailGrid]').down('[itemId=mail-refresh-button]').getEl().down('.x-mask')){
					panel.down('[itemId=recordListpanel]').down('[itemId=mailGrid]').down('[itemId=mail-refresh-button]').getEl().down('.x-mask').destroy();
				}
				
				if(panel.down('[itemId=recordListpanel]').down('[itemId=smGrid]') &&
						panel.down('[itemId=recordListpanel]').down('[itemId=smGrid]').down('[itemId=sm-refresh-button]') &&
						panel.down('[itemId=recordListpanel]').down('[itemId=smGrid]').down('[itemId=sm-refresh-button]').getEl() &&
						panel.down('[itemId=recordListpanel]').down('[itemId=smGrid]').down('[itemId=sm-refresh-button]').getEl().down('.x-mask')){
					panel.down('[itemId=recordListpanel]').down('[itemId=smGrid]').down('[itemId=sm-refresh-button]').getEl().down('.x-mask').destroy();
				}
				
				mailGridReload();
				smGridReload();
				return;
			}
			
			panel.down('[itemId=mailGrid]').disable();
			panel.down('[itemId=smGrid]').disable();
			panel.down('[itemId=recordListpanel]').getLayout().setActiveItem(0);
			mailstore.removeAll();
			smstore.removeAll();
		}
	}
	
	var mailGridReload = function(){
		var selections = panel.down('[itemId=scheduleGrid]').getSelection();
		if(selections.length > 0){
			var record = selections[0];
			if(record.get('actionType') == 2 || record.get('actionType') == 3){
				var params = getTimescope();
				params.id = record.get('id');
				params.flag = panel.down('[itemId=showMailflag]').getValue();
				Ext.Ajax.request({
					url: 'mailLogList',
					params: params,
					method: 'get',
					success: function(response){
						var result = Ext.decode(response.responseText);
						if(result.success){
							mailstore.loadData(result.mailLogList);
							panel.down('[itemId=mailGrid]').down('[itemId=mailCountLabel]').setValue(result.totalCount);
							panel.down('[itemId=mailGrid]').down('[itemId=mailSuccessCountLabel]').setValue(result.successCount);
							panel.down('[itemId=mailGrid]').down('[itemId=mailFailedCountLabel]').setValue(result.failedCount);
						}
					}
				});
			}
		}
	}
	
	var smGridReload = function(){
		var selections = panel.down('[itemId=scheduleGrid]').getSelection();
		if(selections.length > 0){
			var record = selections[0];
			if(record.get('actionType') == 9 || record.get('actionType') == 3){
				var params = getTimescope();
				params.id = record.get('id');
				params.flag = panel.down('[itemId=showSmflag]').getValue();
				Ext.Ajax.request({
					url: 'smLogList',
					params: params,
					method: 'get',
					success: function(response){
						var result = Ext.decode(response.responseText);
						if(result.success){
							smstore.loadData(result.smLogList);
							panel.down('[itemId=smGrid]').down('[itemId=smCountLabel]').setValue(result.totalCount);
							panel.down('[itemId=smGrid]').down('[itemId=smSuccessCountLabel]').setValue(result.successCount);
							panel.down('[itemId=smGrid]').down('[itemId=smFailedCountLabel]').setValue(result.failedCount);
						}
					}
				});
			}
		}
	}
	
});