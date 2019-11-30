var pop;
//菜单类
function popMenu(menuid,name,overcolor, outcolor, bdcolor) {
	this.id = menuid;
	this.name = name;
	this.overColor = overcolor;//鼠标over背景色
	this.outColor = outcolor;//鼠标out背景色
	this.bdColor = bdcolor;//边框色
	this.itemNames = new Array();
	this.itemUrls = new Array();
	this.addItem = f_addItem;
	this.draw = f_draw;
	return this;
}
//增加菜单项
function f_addItem(name,url) {
	this.itemNames[this.itemNames.length] = name;
	this.itemUrls[this.itemUrls.length] = url;
	return this;
}

//绘制菜单
function f_draw() {

		var temp = this;
		document.write("<DIV id=" + temp.id + " style=\"DISPLAY: none\">");
		document.write("<TABLE cellSpacing=0 cellPadding=0 width=\"100%\" align=center border=0><TBODY>");
		document.write("<TR><TD onmouseover=\"style.borderColor ='"+this.bdColor+"';style.backgroundColor ='"+this.overColor+"';\" style=\"FONT-WEIGHT: normal; FONT-SIZE: 12px; LIST-STYLE-IMAGE:  COLOR: #084d96; LINE-HEIGHT: 24px; BACKGROUND-COLOR: #ebf3fb;\" onmouseout=\"style.borderColor='';style.backgroundColor ='"+this.outColor+"';\" vAlign=center align=middle height=24>" + temp.name + "</TD></TR>");
		for (var j = 0; j < temp.itemNames.length; j++) {
			document.write("<TR><TD background=util/images/popdiv.gif height=4></TD></TR><TR><TD onmouseover=\"style.borderColor ='#666666';style.backgroundColor ='#ffffff';\" style=\"FONT-WEIGHT: normal; FONT-SIZE: 12px; LIST-STYLE-IMAGE: url(util/images/popicon.gif); COLOR: #084d96; LINE-HEIGHT: 24px; BACKGROUND-COLOR: #ebf3fb\" onclick='parent.parent.location.href=\"" + temp.itemUrls[j] + "\"' onmouseout=\"style.borderColor='';style.backgroundColor ='#EBF3FB';\" align=left nowrap><LI>" + temp.itemNames[j] + "</LI></TD></TR>");
		}
		document.write("</TBODY></TABLE></DIV>");
	}

//显示菜单
function showMenu(id, num,width) {
    hidePop();
	var obj = event.srcElement;   //捕获触发 expand 函数的对象
	//根据对象在它所在行的 cells 的索引号捕获到相应的下拉菜单组 
	var w = width;// obj.offsetWidth + 2;   // popup 窗口的宽度，等于对象的宽度加 2
	//popup 窗口的高度
	var h = (num + 1) * 24 + 4 * num; 
	// popup 窗口的X坐标值
	var x =  event.x - event.offsetX + obj.offsetLeft - (width-obj.offsetWidth)/2; 
	// popup 窗口的Y坐标值 
	var y = document.body.scrollTop + event.y - event.offsetY;
	pop = window.createPopup();
	var ctrl = eval(id);
	pop.document.body.innerHTML = ctrl.innerHTML;
	pop.show(x, y, w, h, document.body);
	var oPopBody = pop.document.body;
	oPopBody.style.backgroundColor = "lightyellow";
	oPopBody.style.border = "solid black 1px";
	oPopBody.style.cursor = "pointer";
	event.returnValue = false;
	event.cancelBubble = true;
	return false;
}
//隐藏菜单
function hidePop() {
	if (pop != null) {
		pop.hide();
	}
}

// 刷新主框架中的页面
function refreshPage() {
	if (parent.mainFrame)
	{
		parent.mainFrame.location.reload();
		return;
	}
	parent.location.reload();
}

// 隐藏topFrame框架
function hideTopFrame() {
	var toptab = document.getElementById("toptable");
	toptab.style.display = "none";
	var header = document.getElementById("header");
	if (header)
	{
		header.style.height = "25";
	}
	var objFrame = parent.document.getElementById("frametop");
	if (objFrame)
	{
		objFrame.rows = "25,*";
	}
}
// 显示topFrame框架
function showTopFrame() {
	var toptab = document.getElementById("toptable");
	toptab.style.display = "block";
	var header = document.getElementById("header");
	if (header)
	{
		header.style.height = "100";
	}
	var objFrame = parent.document.getElementById("frametop");
	if (objFrame)
	{
		objFrame.rows = "100,*";
	}
}

// 隐藏leftFrame框架
function hideLeftFrame() {
	var objFrame = parent.document.getElementById("framecontent");
	if (objFrame)
	{
		objFrame.cols = "0,*";
	}
}
// 显示leftFrame框架
function showLeftFrame() {
	var objFrame = parent.document.getElementById("framecontent");
	if (objFrame)
	{
		objFrame.cols = "159,*";
	}
}

var PIXELGIF="util/blank.gif";// 这个是个头1*1像素的透明gif图片，请自行建立并放在你网页的相同目录下

var arVersion = navigator.appVersion.split("MSIE");
var version = parseFloat(arVersion[1]);
var pngxp=/\.png$/i;
var AlphaPNGfix= "progid:DXImageTransform.Microsoft.AlphaImageLoader";

function fixPNG(img,width,height) {
	if(!document.all) {
		return;
	}
	if (version < 5.5 || version >= 7) {
		return;
	}
	if(img && img.src && pngxp.test(img.src)) {
		var imgName = img.src;
		var imgID = (img.id) ? "id='" + img.id + "' " : "";
		var imgClass = (img.className) ? "class='" + img.className + "' " : "";
		var imgTitle = (img.title) ? "title='" + img.title + "' " : "title='" + img.alt + "' ";
		var imgStyle = "display:inline-block;" + img.style.cssText;
		if (img.align == "left") imgStyle = "float:left;" + imgStyle;
		if (img.align == "right") imgStyle = "float:right;" + imgStyle;
		if (img.parentElement.href) imgStyle = "cursor:pointer;" + imgStyle;
		var strNewHTML = "<span " + imgID + imgClass + imgTitle
			+ " style=\"" + "width:" + width + "; height:" + height + ";" + imgStyle + ";"
			+ "filter:" +AlphaPNGfix
			+ "(src='" + img.src + "',enabled='true', sizingMethod='scale');\"></span>";
		if (img.useMap) {
			strNewHTML += "<img style=\"position:relative; left:-" + width + ";"
						+ "height:" + height + ";width:" + width +"\" "
						+ "src=\"" + PIXELGIF + "\" usemap=\"" + img.useMap 
						+ "\" border=\"" + img.border + "\">";
		}
		img.outerHTML = strNewHTML;
	} 
}

// window.document.attachEvent("onmouseover", hidePop);

function layoutObject(){
    var self = this;
	var bKeyDown = false;
	var currentleft;
	var currentright;

   	this.mouseDown = function (e) {
	    var event = e||event;
		var el = event.target || event.srcElement;
		bKeyDown = true;
		if (el.setCapture) {
			el.setCapture();
		}else {
			window.captureEvents(Event.MOUSEMOVE|Event.MOUSEUP);
		}
	};

	this.mouseMove = function (e) {
		 if (!bKeyDown) return;
		
		 var event = e||event;
		 var x=event.clientX ? event.clientX : event.pageX;
		 // document.getElementById("header").innerHTML = x;
		 if (x<=2)
			 self.currentleft.style.width = x + "px";
		 else
			 self.currentleft.style.width= (x-2) + "px";
		 if (document.documentElement.clientWidth - self.currentleft.clientWidth - 5>0)
		     self.currentright.style.width = (document.documentElement.clientWidth - self.currentleft.clientWidth - 5) + "px";
		 else
			 self.currentright.style.width = 0;
		 self.updateSize();
    };
   this.mouseUp = function (e) {
	    var event = e||event;
		var el = event.target || event.srcElement;
		bKeyDown = false;
		if (el.releaseCapture) {
			self.currentsplit.releaseCapture();
		}else {
			window.captureEvents(Event.MOUSEMOVE|Event.MOUSEUP);
		}
	};
   this.togglerTo = function(){
		
   };
   this._addEvent = function (o, t, f) {
		if (o.addEventListener) o.addEventListener(t, f, false);
		else if (o.attachEvent) o.attachEvent('on'+ t, f);
		else o['on'+ t] = f;
	};

	this._removeEvent = function (o, t, f) {
		if (o.removeEventListener) o.removeEventListener(t, f, false);
		else if (o.detachEvent) o.detachEvent('on'+ t, f);
		else o['on'+ t] = null;
	};
	this.resize = function(){
		if (document.documentElement.clientWidth - self.currentleft.clientWidth - 5>0)
			self.currentright.style.width = (document.documentElement.clientWidth - self.currentleft.clientWidth - 5) + "px";
		else 
			self.currentright.style.width = 0;
		self.updateSize();
	};
	this.init=function(m,leftWidth){
	   var obj = document.getElementById(m);
	   this._addEvent( obj,"dblclick",this.togglerTo,false);
	   this._addEvent( obj,"mousedown",this.mouseDown,false);
	   this._addEvent( obj,"mousemove",this.mouseMove,false);
	   this._addEvent( obj,"mouseup",this.mouseUp,false);
	   this._addEvent(window,"resize",this.resize,false);
	   self.currentleft =  obj.previousSibling.tagName=="DIV" ? obj.previousSibling : obj.previousSibling.previousSibling;
	   self.currentright = obj.nextSibling.tagName=="DIV" ? obj.nextSibling : obj.nextSibling.nextSibling;
	   self.currentsplit = obj;
	   self.currentleft.style.width = leftWidth;
	   self.currentright.style.width = (document.documentElement.clientWidth - parseInt(leftWidth) - 5) + "px";
	   self.updateSize();
	};
	this.updateSize = function() {
		var obj = document.getElementById("header");
		var height = document.documentElement.clientHeight - obj.clientHeight;
		document.getElementById("main").style.height = height + "px";
		document.getElementById("x-separator").style.height = height + "px";
		self.currentleft.style.height = height + "px";
		self.currentright.style.height = height + "px";
		obj = self.getIFrameName(self.currentright);
		if (self.currentright.style.width)
			obj.style.width = parseInt(self.currentright.style.width);
		else
			obj.style.width = parseInt(self.currentright.clientWidth);
		if (self.currentright.style.height)
			obj.style.height = parseInt(self.currentright.style.height);
		else
			obj.style.height = parseInt(self.currentright.clientHeight);
		obj = self.getIFrameName(self.currentleft);
		if (self.currentleft.style.width)
			obj.style.width = parseInt(self.currentleft.style.width);
		else
			obj.style.width = parseInt(self.currentleft.clientWidth);
		if (self.currentleft.style.height)
			obj.style.height = parseInt(self.currentleft.style.height);
		else
			obj.style.height = parseInt(self.currentleft.clientHeight);
		// alert(self.currentleft.style.width + "->" + self.currentright.style.width + "->" + document.documentElement.clientWidth);

	};
	this.getIFrameName = function(obj) {
		var e = document.getElementsByTagName('iframe');
		if (!e) return null;
		return e[0];
	};
}
// 下拉菜单
function appMenu() {
	var menu = document.getElementById("appmenu");
	if (!menu) return;
	// 取LI
	var objs = menu.getElementsByTagName("li");
	for (var i=0;i<objs.length ;i++ )
	{

		// alert(objs[i].innerHTML);
		objs[i].style.width = "120px";
		objs[i].style.textAlign = "center";
	}
}

// JavaScript Document

// DropDownMenu by Miha Hribar
// http://hribar.info

function addLoadEvent(func) {
    var oldonload = window.onload;
    if (typeof window.onload != 'function') {
        window.onload = func;
    } else {
        window.onload = function() {
            oldonload();
            func();
        }
    }
}

function prepareMenu() {
  	if (!document.getElementsByTagName) return false;
  	if (!document.getElementById) return false;
  	
  	if (!document.getElementById("appmenu")) return false;
  	var menu = document.getElementById("appmenu");
	var sub;
	var len;
  	var root_li = menu.getElementsByTagName("li");
  	for (var i = 0; i < root_li.length; i++) {
		if (root_li[i].parentNode.id=="appmenu") {
			sub = root_li[i].getElementsByTagName("a");
			len = sub[0].innerHTML.length;
			root_li[i].style.width = (20 + len * 16) + "px";
			var subli = root_li[i].getElementsByTagName("ul");
			if (subli.length>0)
				sub[0].className = "top-has-child";
			else {
				sub[0].onclick = forward;
			}
		} else {
			var subli = root_li[i].getElementsByTagName("ul");
			sub = root_li[i].getElementsByTagName("a");
			if (subli.length>0) {
				sub[0].className = "item-has-child";
			} else {
				sub[0].onclick = forward;
			}
		}
  	    var li = root_li[i];
  	    // search for children
  	    var child_ul = li.getElementsByTagName("ul");
  	    if (child_ul.length >= 1) {
  	        // we have children - append hover function to the parent
  	        li.onmouseover = function () {
  	            if (!this.getElementsByTagName("ul")) return false;
  	            var ul = this.getElementsByTagName("ul");
  	            ul[0].style.display = "block";
  	            return true;
  	        }
  	        li.onmouseout = function () {
  	            if (!this.getElementsByTagName("ul")) return false;
  	            var ul = this.getElementsByTagName("ul");
  	            ul[0].style.display = "none";
  	            return true;
  	        }
  	    }
  	}
  	
  	return true;
}

function forward() {
//	var mid = this.parentNode.id;
//	var nPos;
//	if (mid.indexOf("HOME-")==0) {
//		nPos = mid.indexOf("-");
//		top.document.location.href = mid.substring(nPos+1) + ".jsp";
//		return;
//	}
//	if (mid=="OU") {
//		top.document.location.href = "ou";
//		return;
//	}
//	if (mid=="PORTAL") {
//		top.document.location.href = "portal";
//		return;
//	}
//	if (mid=="EFORM") {
//		top.document.location.href = "eform";
//		return;
//	}
//	if (mid=="WORKFLOW") {
//		top.document.location.href = "workflow";
//		return;
//	}
//	if (mid=="WEBSITE") {
//		top.document.location.href = "website/EnterSiteAction.do?actionType=query";
//		return;
//	}
//	var leftFrame = document.getElementById("outlook");
//	if (leftFrame) {
//		loadOutlookMenuXML(mid);
//		return;
//	}
//	top.document.location.href = "appportal.jsp?sysid=" + mid;
}

function popMenuObject() {
	var self = this;
	var m_styletop = "20px";
	var m_styleleft_show = "1px";
	var timer = null;
	var objBtn;
	var objMenu;

	//显示菜单
	this.showMenu = function() {
		if (self.objMenu.style.display=="none") {
			self.objMenu.style.display="block";
			self.objMenu.style.left=m_styleleft_show;
			self.objMenu.style.top=m_styletop;
		}
		else 
		{
			self.objMenu.style.display="none";
		}
	}

	//隐藏菜单
	this.hiddenMenu = function () {
		setTimeout(self.removeMenu, 300);
	}
	// 隐藏菜单
	this.removeMenu = function () {
		self.objMenu.style.display = "none";
	}
	//初始化菜单按钮
	this.init = function (btn,ul)	{
		self.objBtn = document.getElementById(btn);
		if (!self.objBtn) return;
		self.objMenu = document.getElementById(ul);
		if (!self.objMenu) return;

		self.objMenu.style.display = "none";
		this._addEvent( self.objBtn,"click",this.showMenu,false);
		this._addEvent( self.objBtn,"blur",this.hiddenMenu,false);
		// this._addEvent( self.objMenu,"click",this.clickItem,false);
		this._addEvent( self.objMenu,"mouseover",this.drop_mouseover,false);
		this._addEvent( self.objMenu,"mouseout",this.drop_mouseout,false);
	}
	//当鼠标移动到对象上时清楚时间对象
	this.drop_mouseover = function() {
		if (self.timer)
		    window.clearTimeout(self.timer);
	}
	//当鼠标移开后隐藏菜单
	this.drop_mouseout = function()	{
		self.timer = setTimeout(self.hiddenMenu, 100);
	};
    this._addEvent = function (o, t, f) {
		if (o.addEventListener) o.addEventListener(t, f, false);
		else if (o.attachEvent) o.attachEvent('on'+ t, f);
		else o['on'+ t] = f;
	};
}
// 显示/隐藏LOGO区
function displayLogoArea(obj) {
	var tb = document.getElementById("tbl_eflow_top");
	var v = tb.style.display;
	if (v && v=="none")	{
		tb.style.display = "block";
		obj.className = "imghiddenopt";
	} else {
		tb.style.display = "none";
		obj.className = "imgshowopt";
	}
	if (typeof(onMainResize)=="function") {
		onMainResize();
		onMainResize();
	}
}

// 初始化弹出菜单
function prepareMyMenu() {
	var obj = new popMenuObject();
	obj.init("popmenu","mymenu");
}
addLoadEvent(prepareMenu);
addLoadEvent(prepareMyMenu);