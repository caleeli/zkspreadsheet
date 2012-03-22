<%@ page contentType="text/css;charset=UTF-8" %>
<%@ taglib uri="http://www.zkoss.org/dsp/web/core" prefix="c" %>

.zssheet {
	position: relative;
	width: 100px;
	height: 100px;
	background:#FFFFFF;
	overflow:hidden;
	border:1px solid #D8D8D8;
}

.zscave {
	background: #A4C3EB;
}

.zscenter {
	border:0;
	border-top:1px solid #C5C5C5;
}

.zsscroll {
	top: 0px;
	position: absolute;
	z-index: 0;
	overflow:auto;
	width: 100%;
	height:100%;
	left:0px;
	background-color:#FFFFFF;
}

.zsdata {
	left: 0px;
	top: 0px;
	display: block;
	position: relative;
	padding-top: 20px;
	padding-left: 36px;
	overflow: hidden;
	background-color:#FFFFFF;
}
.zsdatapad{
	position: relative;
	left: 0px;
	top: 0px;
	overflow: hidden;
	height:0px;
}
.zsleftpad{
	position: relative;
	left: 0px;
	top: 0px;
	overflow: hidden;
	height:0px;
}

.zsblock {
	left: 0px;
	top: 0px;
	display: block;
	position: relative;
	overflow: visible;
	background-color:#FFFFFF;
}

.zsrow {
	position: relative;
	display: block;
	margin: 0px;
	padding: 0px;
	height: 20px; /*rowHeight*/
	overflow-y: visible;/*don't set hidden, otherwise sometime there will appear h-scrollbar in row*/
	white-space: nowrap;
	z-index: 1;
	<c:if test="${c:browser('ie')}"> 
		font-size:0;
	</c:if>
	<c:if test="${c:isExplorer() && !c:isExplorer7()}">
		position: static;
		overflow-y: hidden;
	</c:if>
}

.zscell {
	display: -moz-inline-box;
	-moz-box-sizing: border-box;
	border-right: 1px solid #D0D7E9;
	border-bottom: 1px solid #D0D7E9;
	padding: 0px 2px 0px 2px; /* cellPadding */
	height: 20px; /*rowHeight*/
	width: 60px; /*colWidth*/
	overflow: hidden;
	vertical-align: top;
	/*font-size: smaller;*/
	/*line-height: 20px;  lineHeight*/
	z-index:10;
	position: relative;
	cursor : default;
	<c:if test="${c:browser('ie')}">
		display: inline-block;
		zoom: 1;
		*display: inline;
	</c:if>
	<c:if test="${c:isSafari()}">
		display: inline-block;
	</c:if>
	<c:if test="${c:isOpera()}">
		display: inline-block;
		vertical-align:text-top;
		/*vertical-align: top;*//* DIFF : important, vertical align will cause opera scrollbar disappear*/
	</c:if>
}

.zscell-over {
	border-right: 1px;
	border-right-style:hidden;
	overflow: visible;
	position: relative;
	<c:if test="${c:browser('ie')}">
		border-right: 0px;
		margin-right: 1px;
	</c:if>
	<c:if test="${c:isSafari()}">
		border-right: 0px;
		margin-right: 1px;
	</c:if>
	<c:if test="${c:isOpera()}">
		border-right: 0px;
		margin-right: 1px;
	</c:if>
}

.zscell-over-b {
	overflow: visible;
	position: relative;
}

.zscelltxt {
	font-family: Calibri;
	font-size: 11px;
	text-align: left;
	width: 49px; /* colWidth - 2*cellPadding - 1 , 1 is border*/
	overflow: hidden;
	<c:if test="${c:browser('ie7-') || c:browser('ie6-')}">
		zoom: 1;
	</c:if>
}

.zscelltxt-real {
	width: 100%;
	<c:if test="${c:browser('ie7-') || c:browser('ie6-')}">
		left: 0;
		position: absolute;
	</c:if>
}

.zstop {
	position: absolute;
	z-index: 2;
	left: 36px; /*leftWidth*/
	top: 0px;
	height: 19px; /*topHeidht -2 , 1 is border*/
	/*line-height: 20px; lineHeight*/
	overflow: hidden;
	border-top: 1px solid #7F9DB9;
	/*border-bottom: 1px solid #7F9DB9;*/
	background:#DAE7F6 none repeat scroll 0%;
}
.zscornertop{
	position: absolute;
	left: 36px; /*leftWidth*/
	top: 0px;
	height: 19px; /*topHeidht -2 , 1 is border*/
	overflow: hidden;
	border-top: 1px solid #7F9DB9;
	background:#DAE7F6 none repeat scroll 0%;
}


.zstopi, .zscornertopi{
	position: relative;
	top: 0px;
	left: 0px;
	height: 100%;
	display: block;
	margin: 0px;
	padding: 0px;
	overflow-y: visible;
	white-space: nowrap;
}

.zstophead {
	
}

.zstopblock {
	left: 0px;
	top: 0px;
	display: block;
	position: relative;
	overflow: visible;
	background-color:#FFFFFF;
	<c:if test="${c:isExplorer7()}">
		top: 1px;
	</c:if>
	<c:if test="${c:isExplorer() && !c:isExplorer7()}">
		top: -1px;
	</c:if>
	<c:if test="${c:isSafari()}">
		top: 1px;
	</c:if>
	<c:if test="${c:isOpera()}">
		top: 1px;
	</c:if>
}

.zstopcell {
	display: -moz-inline-box;
	-moz-box-sizing: border-box;
	border-right: 1px solid #DAE7F6;
	border-bottom: 1px solid #7F9DB9;
	padding: 0px 2px 0px 2px; /*cellPadding*/
	height: 19px; /*topHeigth*/
	width: 60px; /*cellWidth*/
	overflow: hidden;
	vertical-align: top;
	/*line-height: 20px; lineHeigth*/
	border-right: 1px solid #7F9DB9;
	background:#E8EDF4;
	overflow: hidden;
	position:relative;
	cursor : default;
	<c:if test="${c:isGecko()}">
		background-image: -moz-linear-gradient(top, #F9FCFD, #D3DBE9);
	</c:if>
	<c:if test="${c:browser('ie')}">
		display: inline-block;
		zoom: 1;
		*display: inline;
		filter:  progid:DXImageTransform.Microsoft.gradient(startColorStr='#F9FCFD', EndColorStr='#D3DBE9'); /* IE6,IE7 */
		-ms-filter: "progid:DXImageTransform.Microsoft.gradient(startColorStr='#F9FCFD', EndColorStr='#D3DBE9')"; /* IE8 */
	</c:if>
	<c:if test="${c:isSafari()}">
		display: inline-block;
		background-image: -webkit-gradient(linear,left top,left bottom,color-stop(0, #F9FCFD),color-stop(1, #D3DBE9));
	</c:if>
	<c:if test="${c:isOpera()}">
		display: inline-block;
		background-image: -o-linear-gradient(#F9FCFD, #D3DBE9);
	</c:if>
}

.zstop-sel {
	background:#FFD58D;
	<c:if test="${c:isGecko()}">
		background-image: -moz-linear-gradient(top, #F9D99F, #F1C15F);
	</c:if>
	<c:if test="${c:browser('ie')}">
		filter:  progid:DXImageTransform.Microsoft.gradient(startColorStr='#F9D99F', EndColorStr='#F1C15F'); /* IE6,IE7 */
		-ms-filter: "progid:DXImageTransform.Microsoft.gradient(startColorStr='#F9D99F', EndColorStr='#F1C15F')"; /* IE8 */
	</c:if>
	<c:if test="${c:isSafari()}">
		background-image: -webkit-gradient(linear,left top,left bottom,color-stop(0, #F9D99F),color-stop(1, #F1C15F));
	</c:if>
	<c:if test="${c:isOpera()}">
		background-image: -o-linear-gradient(#F9D99F, #F1C15F);
	</c:if>
}

.zstopcelltxt {
	font-size: 11px;
	text-align: center;
	width: 49px;  /* colWidth - 2*cellPadding - 1 , 1 is border*/
}

.zsleft{
	position: absolute;
	z-index: 2;
	top: 20px; /* topHeight */
	left: 0px;
	width: 35px; /* leftWith - 2 , 2 is border */
	overflow: hidden;
	border-left: 1px solid #7F9DB9;
	background:#DAE7F6 none repeat scroll 0%;
}

.zscornerleft  {
	position: absolute;
	z-index: 2;
	top: 20px; /* topHeight */
	left: 0px;
	width: 34px; /* leftWith - 2 , 2 is border */
	overflow: hidden;
	border-left: 1px solid #7F9DB9;
	background:#DAE7F6 none repeat scroll 0%;
}

.zslefti, .zscornerlefti {
	position: relative;
	top: 0px;
	left: 0px;
}


.zsleftblock {
	left: 36px;
	top: 0px;
	display: block;
	position: absolute;
	overflow: visible;
	background-color:#FFFFFF;
}

.zslefthead {
	background-color: #e3ecf7;
}

.zsleftcell {
	border-right: 1px solid #7F9DB9;
	/*line-height: 20px;*/
	height: 19px; /* rowHeight - 1, 1 is border */
	border-bottom: 1px solid #7F9DB9;
	text-align: center;
	overflow: hidden;
	cursor : default;
}
.zsleftcelltxt {
	font-size: 11px;
	height: 19px; /* rowHeight - 1, 1 is border */
	text-align: center;
	<c:if test="${c:browser('ie')}">
		font-size: 10px;
	</c:if>
}

.zsleft-sel {
	background:#ffd58d;
}

.zsscrollinfo{
	z-index:3;
	position: absolute;
	top:0px;
	left:0px;
	width:400px;
	padding-top:1px;
	padding-bottom:1px;
	display:none;
}
.zsscrollinfoinner{
	background-color:#E3ECF7;
	font-family: Tahoma, Arial, Helvetica, sans-serif;
	font-size:small;
	padding-left:5px;
	padding-right:5px;
	border:1px outset #555555;
}

.zsinfo{
	z-index:4;
	position: absolute;
	top:1px;
	left:2px;
	width:400px;
	padding-top:2px;
	padding-bottom:2px;
	display:none;
}
.zsinfoinner{
	background-color:#FFD5AD;
	font-family: Tahoma, Arial, Helvetica, sans-serif;
	font-size:small;
	padding-left:5px;
	padding-right:5px;
	border:1px outset #555555;
}

.zscorner {
	position: absolute;
	z-index: 2;
	width: 36px; /* leftWidth - 2, 2 is border */
	height: 20px; /* topHeight - 2, 2 is border */
	background-color:#DAE7F6;
	overflow: hidden;
	<c:if test="${c:browser('ie')}">
		*font-size: 0;
	</c:if>
}

.zscorneri {
	border: 1px solid #7F9DB9;
	/*background-image: url(${c:encodeURL('~./zss/img/s_hd.gif')});*/
	width:34px;
	height:18px;
	background-color:#A9C4E9;
}

.zscornerblock {
	left: 20px;
	top: 36px;
	display: block;
	position: absolute;
	overflow: hidden;
	background-color:#FFFFFF;
}

.zsfocmark {
	position: absolute;
	z-index: 2;
	display: none;
	border: 3px solid #BBBBBB;
	<c:if test="${c:browser('ie')}">
		font-size: 0;
	</c:if>
}
.zsfocmarki{
	position: absolute;
	width:100%;
	height:100%;
	<c:if test="${c:browser('ie')}">
		font-size: 0;
		filter: alpha(opacity=0);
		background: #FFFFFF;
	</c:if>
}
.zsselect {
	position: absolute;
	z-index: 3;
	display: none;
	cursor: move;
	border: 3px solid #222222;
	<c:if test="${c:browser('ie')}">
		font-size: 0;
	</c:if>
}
.zsselecti{
	position: absolute;
	cursor: default;
	width:100%;
	height:100%;
	<c:if test="${c:browser('ie')}">
		font-size: 0;
		filter: alpha(opacity=0);
		background: #FFFFFF;
	</c:if>
}

.zsselecti-r{
	background-color: #E3ECF7;
	opacity:.5;
	<c:if test="${c:browser('ie')}">
		filter: alpha(opacity=50);
	</c:if>
}

.zsseldot {
	position: absolute;
	width: 5px;
	height: 5px;
	right: -5px;
	bottom: -5px;
	border: 1px solid white;
	background-color: #222222;
	cursor: crosshair;
	<c:if test="${c:browser('ie')}">
		font-size: 0;
	</c:if>
}

.zsselchg {
	position: absolute;
	z-index: 4;
	display: none;
	border: 3px solid #909090;
	
}

.zsselchgi{
	position: absolute;
	width:100%;
	height:100%;
	background-color: #BBAABB;
	opacity:.4;
	<c:if test="${c:browser('ie')}">
		filter: alpha(opacity=40);
		background-color: #99FFAA;
	</c:if>
}

.zshighlight {
	position: absolute;
	z-index: 2;
	display: none;
	border: 3px dotted #909090;
}

.zshighlight2 {
	border-color: #000090;
}

.zshighlighti{
	position: absolute;
	width:100%;
	height:100%;
}

.zsedit {
	font-family: Calibri;
	font-size: 11px;
	position: absolute;
	z-index: 4;
	border: none;
	display: none;
	background-color: white;
	overflow: auto;
	border-bottom:1px #777777 solid;
}

.zsfocus{
	position:absolute;
	width : 0px;
	left : -5px;
}


.zshboun{
	display: -moz-inline-box;
	position:relative;
	height:20px;
	width:0px;
	left:-3px;
	z-index:2;
	vertical-align:top;
	<c:if test="${c:browser('ie')}">
		display: inline-block;
		zoom: 1;
		*display: inline;
	</c:if>
	<c:if test="${c:isSafari()}">
		display: inline-block;
	</c:if>
	<c:if test="${c:isOpera()}">
		display: inline-block;
	</c:if>
}
.zshbouni{
	position: relative;
	height:100%;
	width:7px;
	<c:if test="${c:browser('ie')}">
		*position: absolute;
		display: inline-block;
		zoom: 1;
		*display: inline;
	</c:if>
	<c:if test="${c:isSafari()}">
		display: inline-block;
	</c:if>
	<c:if test="${c:isOpera()}">
		display: inline-block;
	</c:if>
}
.zsvboun{
	position:relative;
	height:0px;
	top:-3px;
	z-index:2;
}
.zsvbouni{
	position: relative;
	height:7px;
	width:100%;
	<c:if test="${c:browser('ie')}">
		*position: absolute;
		font-size: 0;
	</c:if>
}
.zshbouni-over{
	background: #A9C4E9;
	cursor: e-resize;
}
.zsvbouni-over{
	background: #A9C4E9;
	cursor: n-resize;
}
.zshbounw{
	position: relative;
	height:100%;
	width:3px;
	<c:if test="${c:browser('ie')}">
		display: inline-block;
		zoom: 1;
		*display: inline;
		*position: absolute;
		*left:7px;
	</c:if>
	<c:if test="${c:isSafari()}">
		display: inline-block;
	</c:if>
	<c:if test="${c:isOpera()}">
		display: inline-block;
	</c:if>
}
.zshbounw-over{
	background: #A9C4E9;
	cursor: url(${c:encodeURL('~./zss/img/h_resize.cur')}), e-resize;
}
.zsvbounw{
	position: relative;
	height:3px;
	width:100%;
	<c:if test="${c:browser('ie')}">
		font-size: 0;
		*position: absolute;
		*top:7px;
	</c:if>
}
.zsvbounw-over{
	background: #A9C4E9;
	cursor: url(${c:encodeURL('~./zss/img/v_resize.cur')}), n-resize;
}
.zsfztop{
	border-bottom : 1px #5F5FFF;
	border-bottom-style:none;
}

.zsfzleft{
	border-right : 1px #5F5FFF;
	border-right-style:none;
}

.zsfzcorner{
	border-right : 1px #5F5FFF;
	border-bottom : 1px #5F5FFF;
	border-right-style:none;
	border-bottom-style:none;
}
.zssmask{
	position:absolute;
	width:100%;
	height:100%;
	z-index:999;
	background-color : #A9C4E9;
	opacity:.75;
	display:table;
	<c:if test="${c:browser('ie')}">
		filter:alpha(opacity=75);
	</c:if>
}
.zssmask2{
	display:table-cell;
	vertical-align:middle;
}
.zssmasktxt{
	font-size:20px;
	font-style:bold;
	color:white;
}

.zsmergee{
	display:none!important;
}

.zsmergeeu{
	visibility:hidden!important;
}

.zswidgetpanel{
	position:absolute;
	width:0px;
	height:0px;
	top:0px;
	left:0px;
	z-index:10;
	overflow:visible;
}

.zswidget{
	position:absolute;
	top:0px;
	left:0px;
	z-index:10;
	visibility:hidden;/* hidden when loading, javascript will show it after init*/
}

.zswidget-drag {
	background: #FFFFFF;
	border: 2px solid #333333;
	opacity:.5;
	<c:if test="${c:browser('ie')}">
		filter: alpha(opacity=50);
	</c:if>
}

.zswidget-focus {
	border: 1px solid #CFDDDE;
	border-radius: 1px;
}

.zswidget-real, .zswidget-real a:hover{
	cursor: move;
}

.zswidget-focus .zswidget-cave {
	border: 5px solid #E8FDFD;
}

.chartborder img {
	border: 1px solid #868686;
}

.zsbtn {
	height:17px;
	width:17px;
	position: absolute;
	bottom: 2px;
	z-index: 10;
	background-repeat:no-repeat;
	background-size: 100%;
}

.zsdropdown {
	background-image: url(${c:encodeURL('~./zss/img/dropdown.gif')});
}

.zsfilter {
	background-image: url(${c:encodeURL('~./zss/img/filter.gif')});
}

.zspopuppanel {
	position:absolute;
	width:0px;
	height:0px;
	top:0px;
	left:0px;
	overflow:visible;
	z-index:10;
}

.zscellpopup {
	overflow:auto;
	position:absolute;
	top:0px;
	left:0px;
	z-index:10;
	background: #FFFFFF;
	border: 1px solid #868686;
	box-shadow: 1px 1px 2px rgba(0, 0, 0, 0.5);
	height: 155px;
}

.zsdv-popup-cave {
	margin: 0;
	padding: 0;
}

.zsdv-item {
	list-style: none;
	padding: 2px;
	margin: 0;
	font-size: 12px;
}

.zsdv-item a.zsdv-item-text {
	text-decoration: none !important;
	white-space: nowrap;
	color: #000000;
}

.zsdv-item-over, .zsdv-item-over a.zsdv-item-text {
	color: #FFFFFF;
	background: #3399FF;
} 

.zsdvp-popup {
	background: #FFFFE1;
	<c:if test="${c:browser('ie7-') || c:browser('ie6-') || c:isOpera()}">
		width: 200px;
		height: 90px;
		overflow: auto;
	</c:if>
}

.zsdvp-popup-cave {
	padding: 1px 2px;
	background: #FFFFE1;
	display: block;
	overflow: auto;
}

.zsdvp-title {
	font-weight: bold;
	color: #333333;
	padding: 2px 2px 0;
	word-wrap: break-word;
	zoom: 1;
}

.zsdvp-text {
	padding: 0 2px 2px;
	color: #555555;
	word-wrap: break-word;
	zoom: 1;
}

.zsafp-popup, .zsafp-popup-cave {
	height: 235px;
	width: 240px;
}

.zsafp-search {
	border-bottom: 1px solid #D8D8D8;
	width: 100%;
	padding: 2px;
}

.zsafp-search-inp {
	background: #FFFFFF;
	border: none;
	font-size: 12px;
	width: 215px;
}

.zsafp-items {
	overflow-y: auto;
	overflow-x: hidden;
	height: 158px;
	width: 100%;
	border-bottom: 1px solid #D8D8D8;
	position: relative;
}

.zsafp-item {
	padding: 2px 2px 2px 20px;
	position: relative;
	background-position: left center;
	font-size: 13px;
	cursor: pointer;
	<c:if test="${c:browser('ie6-')}">
		padding-left: 2px;
	</c:if>
}

.zsafp-item-over {
	background: #D6F0FD;
}

.zsafp-item-hide {
	display: none;
}

.zsafp-item-checkbox {
	height: 16px;
	width: 16px;
	background-repeat: no-repeat;
	position: absolute;
	left: 3px;
	top: 2px;
	background: url(${c:encodeURL('~./zss/img/ui-check-box-uncheck.png')}) no-repeat transparent;
	<c:if test="${c:browser('ie6-')}">
		background-image: url(${c:encodeURL('~./zss/img/ui-check-box-uncheck.gif')});
	</c:if>
}

<c:if test="${c:browser('ie6-')}">
.zsafp-item-text {
	margin-left: 22px;
}
</c:if>

.zsafp-item-seld .zsafp-item-checkbox {
	background: url(${c:encodeURL('~./zss/img/ui-check-box.png')}) no-repeat transparent;
	<c:if test="${c:browser('ie6-')}">
		background-image: url(${c:encodeURL('~./zss/img/ui-check-box.gif')});
	</c:if>
}

.zsafp-item-seld-mix .zsafp-item-checkbox {
	background: url(${c:encodeURL('~./zss/img/ui-check-box-mix.png')}) no-repeat transparent;
	<c:if test="${c:browser('ie6-')}">
		background-image: url(${c:encodeURL('~./zss/img/ui-check-box-mix.gif')});
	</c:if>
}
	
.zsafp-search-icon {
	background: url(${c:encodeURL('~./zss/img/magnifier-zoom.png')}) no-repeat transparent;
	display: inline-block;
	position: absolute;
	right: 4px;
	top: 4px;
	width: 16px;
	height: 16px;
	<c:if test="${c:browser('ie6-')}">
		background-image: url(${c:encodeURL('~./zss/img/magnifier-zoom.gif')});
	</c:if>
}

.zsafp-btns {
	padding: 10px 12px;
	position: absolute;
	right: 0;
	<c:if test="${c:isExplorer() and not c:browser('ie8')}">
		width: 150px;
		height: 50px;
	</c:if>
}

.zsafp-btn {
	cursor: pointer;
	border-radius: 2px;
	height: 27px;
	width: 50px;
	padding: 0 8px;
	display: inline-block;
	<c:if test="${c:isExplorer() and not c:browser('ie8')}">
		position: absolute;
	</c:if>
	border: 1px solid #C7C7C7;
	margin: 0 8px;
}

.zsafp-btn-disd {
	cursor: default;
	opacity: 0.6;
	<c:if test="${c:browser('ie')}">
		filter: alpha(opacity=60);
	</c:if>
}

.zsafp-ok-btn {
	background: url(${c:encodeURL('~./zss/img/tick.png')}) no-repeat 25px 5px #EFEFEF;
	<c:if test="${c:isExplorer() and not c:browser('ie8')}">
		left: 0;
	</c:if>
	<c:if test="${c:browser('ie6-')}">
		background-image: url(${c:encodeURL('~./zss/img/tick.gif')});
	</c:if>
}

.zsafp-cancel-btn {
	background: url(${c:encodeURL('~./zss/img/cross.png')}) no-repeat 25px 5px #EFEFEF;
	<c:if test="${c:isExplorer() and not c:browser('ie8')}">
		right: 8px;
	</c:if>
	<c:if test="${c:browser('ie6-')}">
		background-image: url(${c:encodeURL('~./zss/img/cross.gif')});
	</c:if>
}

.zsnamebox {
	width: 100%;
	position: relative;
	background: #FFFFFF;
	border: 1px solid #D8D8D8;
	border-top: 0;
	border-left: 0;
	padding-bottom: 1px;
}
.zsnamebox-inp {
	font-size: 14px;
	border: 0;
	height: 14px;
	padding: 5px 4px;
	text-align: center;
}
.zsnamebox-disd .zsnamebox-inp {
	cursor: default;
}
.zsnamebox-icon {
	background: url(${c:encodeURL('~./zss/img/namebox-arrow.png')}) no-repeat;
	position: absolute;
	right: 0;
	top: 0;
	width: 24px;
	height: 24px;
	cursor: pointer;
	<c:if test="${c:browser('ie6-')}">
		background-image: url(${c:encodeURL('~./zss/img/namebox-arrow.gif')});
	</c:if>
}
.zsnamebox-disd .zsnamebox-icon {
	cursor: default;
	opacity: 0.5;
	<c:if test="${c:browser('ie')}">
		filter: alpha(opacity=50);
	</c:if>
}
.zsnamebox-pp {
	position:absolute;
	background: #FFFFFF;
	border: 1px solid #868686;
	box-shadow: 1px 1px 2px rgba(0, 0, 0, 0.5);
	z-index: 20;
}
.zsnamebox-item-over {
	background: #3399FF;
	color: #FFFFFF;
}
.zsformulabar-blank {
	width: 100%;
	height: 100%;
  	background-color: #FEFEFE;
  	background-image: -moz-linear-gradient(top, #FEFEFE, #E4ECF7); /* FF3.6 */
 	background-image: -webkit-gradient(linear,left top,left bottom,color-stop(0, #FEFEFE),color-stop(1, #E4ECF7)); /* Saf4+, Chrome */
            filter:  progid:DXImageTransform.Microsoft.gradient(startColorStr='#FEFEFE', EndColorStr='#E4ECF7'); /* IE6,IE7 */
        -ms-filter: "progid:DXImageTransform.Microsoft.gradient(startColorStr='#FEFEFE', EndColorStr='#E4ECF7')"; /* IE8 */
}

.zsformulabar-okbtn .z-toolbarbutton-cnt, .zsformulabar-cancelbtn .z-toolbarbutton-cnt {
	width: 20px;
	height: 17px;
	font-size: 14px;
	text-align: center;
	padding-top: 0;
	padding-bottom: 4px;
	<c:if test="${c:browser('ie')}">
		padding-top: 1px;
		padding-bottom: 3px;
	</c:if>
	<c:if test="${c:browser('ie6-')}">
		font-family: Lucida Sans Unicode;
	</c:if>
}
.zsformulabar-formulabtn .z-toolbarbutton-cnt {
	font-size: 14px;
	font-style: italic;
	text-align: center;
	padding-top: 0;
	padding-bottom: 4px;
	<c:if test="${c:browser('ie')}">
		padding-top: 1px;
		padding-bottom: 3px;
	</c:if>
}
.zsformulabar-buttons {
	position: absolute;
	top: 0;
	right: -1px;
	padding-left: 4px;
	padding-top: 1px;
	height: 24px;
	border-bottom: 1px solid #D8D8D8;
}
.zsformulabar-west {
	border-right: 0;
}
.zsformulabar-center-cave-colbtn {
	position: absolute;
	right: 0;
	top: 0;
	height: 100%;
	padding-top: 1px;
	border-left: 1px solid #D8D8D8;
	background: #F5F5F5;
}
.zsformulabar-expandbtn-body-over {
	background: url(${c:encodeURL('~./zss/img/toolbarbtn-ctr.gif')}) 0 -500px;
}
.zsformulabar-expandbtn .z-toolbarbutton-cnt {
	width: 20px;
	height: 17px;
	padding: 2px;
	background: url(${c:encodeURL('~./zss/img/arrow-270-small.png')}) no-repeat 4px 2px transparent;
	<c:if test="${c:browser('ie6-')}">
		background-image: url(${c:encodeURL('~./zss/img/arrow-270-small.gif')});
	</c:if>	
}
.zsformulabar-expandbtn-expanded .z-toolbarbutton-cnt {
	background: url(${c:encodeURL('~./zss/img/arrow-090-small.png')}) no-repeat 4px 2px transparent;
	<c:if test="${c:browser('ie6-')}">
		background-image: url(${c:encodeURL('~./zss/img/arrow-090-small.gif')});
	</c:if>		
}
.zsformulabar-editor {
	border: 1px solid #D8D8D8;
	border-top: 0;
	overflow: hidden;
	margin-top: -1px;
}
.zsformulabar-editor-real {
	border: 0;
	font-size: 16px;
	padding: 2px 3px 0 3px;
	font-family: arial,sans-serif;
	position: relative;
	top: 0;
	left: 0;
}

.zssheetselector .z-tab-hl, .zssheetselector .z-tab-hr, .zssheetselector .z-tab-hm {
	background: #F9F9F9;
}

.zssheetselector .z-tab {
	border: 1px solid #D4D4D4;
	border-top: 0;
}

.zssheetselector .z-tab-seld, 
.zssheetselector .z-tab-seld .z-tab-hl, 
.zssheetselector .z-tab-seld .z-tab-hr, 
.zssheetselector .z-tab-seld .z-tab-hm {
	background-color: #FFFFFF;
}

.zssheetselector .z-tabs-left-scroll,
.zssheetselector .z-tabs-right-scroll {
	margin-top: -3px;
}

<c:if test="${c:isGecko()}">
.zssheetselector .z-tabs-cnt {
	-moz-user-select: text;
}
.zssheetselector .z-tab {
	-moz-user-select: text;
}
</c:if>	

.zssheet-editor {
	padding: 1px 0;
}

.zstbtn .z-toolbarbutton-cnt {
	overflow: hidden;
}

.zstbtn-cave {
	position: absolute;
	top: 0;
	right: 0;
	height: 16px;
	padding: 2px 1px;
	width: 10px;
}

.zstbtn-clk-disd .zstbtn-real {
	cursor: default;
}

.zstbtn-clk-disd .zstbtn-cave {
	cursor: pointer;
}

.zstbtn-seld .z-toolbarbutton-cnt {
	background: #FCDE9A;
	padding: 1px;
	border: 1px solid #B7C6C9;
}

.zstbtn-seld .zstbtn-cave {
	padding: 1px;
}

.z-toolbarbutton-over .z-toolbarbutton-cnt {
	padding: 2px;
	border: 0;
}

.z-toolbarbutton-over .zstbtn-cave {
	padding: 2px 1px 2px 0;
	border-left: 1px solid #79B9E0;
}

.zstbtn-cave-over {
	background:#A4D4FD;
	<c:if test="${c:isGecko()}">
		background-image: -moz-linear-gradient(top, #D1EDFF, #A4D4FD);
	</c:if>
	<c:if test="${c:browser('ie')}">
		filter:  progid:DXImageTransform.Microsoft.gradient(startColorStr='#D1EDFF', EndColorStr='#A4D4FD'); /* IE6,IE7 */
		-ms-filter: "progid:DXImageTransform.Microsoft.gradient(startColorStr='#D1EDFF', EndColorStr='#A4D4FD')"; /* IE8 */
	</c:if>
	<c:if test="${c:isSafari()}">
		background-image: -webkit-gradient(linear,left top,left bottom,color-stop(0, #D1EDFF),color-stop(1, #A4D4FD));
	</c:if>
	<c:if test="${c:isOpera()}">
		background-image: -o-linear-gradient(#D1EDFF, #A4D4FD);
	</c:if>
}

.zstbtn-arrow {
	width: 10px;
	height: 16px;
	background: url(${c:encodeURL('~./zss/img/arrow-down.gif')}) no-repeat;
}

.zstbtn-sep {
	border: 0;
	padding: 1px 0;
	cursor: default;
}

.zstbtn-sep .z-toolbarbutton-body {
	border: 0;
	padding: 0 1px;
}

.zsfontfamily-arial .z-comboitem-text {
	font-family:Arial;
	font-size: 18px;
}

.zsfontfamily-arial-black .z-comboitem-text {
	font-family:Arial Black;
	font-size: 18px;
}

.zsfontfamily-comic-sans-ms .z-comboitem-text {
	font-family:Comic Sans MS;
	font-size: 18px;
}

.zsfontfamily-courier-new .z-comboitem-text {
	font-family:courier new;
	font-size: 18px;
}

.zsfontfamily-georgia .z-comboitem-text {
	font-family:Georgia;
	font-size: 18px;
}

.zsfontfamily-impact .z-comboitem-text {
	font-family:Impact;
	font-size: 18px;
}

.zsfontfamily-lucida-console .z-comboitem-text {
	font-family:Lucida Console;
	font-size: 18px;
}

.zsfontfamily-lucida-sans-unicode .z-comboitem-text {
	font-family:Lucida Sans Unicode;
	font-size: 18px;
}

.zsfontfamily-palatino-linotype .z-comboitem-text {
	font-family:Palatino Linotype;
	font-size: 18px;
}

.zsfontfamily-tahoma .z-comboitem-text {
	font-family:Tahoma;
	font-size: 18px;
}

.zsfontfamily-times-new-roman .z-comboitem-text {
	font-family:Times New Roman;
	font-size: 18px;
}

.zsfontfamily-trebuchet-ms .z-comboitem-text {
	font-family:Trebuchet MS;
	font-size: 18px;
}

.zsfontfamily-verdana .z-comboitem-text {
	font-family:Verdana;
	font-size: 18px;
}

.zsfontfamily-ms-sans-serif .z-comboitem-text {
	font-family:MS Sans Serif;
	font-size: 18px;
}

.zsfontfamily-ms-serif .z-comboitem-text {
	font-family:MS Serif;
	font-size: 18px;
}

.zsfontsize .z-comboitem-text {
	font-size:13px;
}

.zsfontfamily,
.zsfontsize {
	vertical-align: middle;
	margin: 0 2px;
}

.zstbtn-color {
	width: 14px;
	height: 3px;
	left: 3px;
	bottom: 2px;
	position: absolute;
}

.zschktbtn img {
	position: absolute;
	right: 3px;
	top: 2px;
}
.zschktbtn-check {
	width: 16px;
	height: 16px;
}

.zscolormenu .z-menu-cnt .z-menu-img{
	margin: 0 16px 0 -4px;
	width: 14px;
}

.zsstylepanel-toolbar {
	background: #F5F5F5;	
}

.zsstylepanel .z-popup-cl .z-popup-cnt {
	padding: 0;
}

.zsstylepanel-toolbar {
	border: 0;
}

<%-- Colorbutton --%>
.zstbtn-pp {
	border: 1px solid #86A4BE;
	background: #FFF;
	position: absolute;
	width: 180px;
	height: 120px;
}
.zstbtn-pp .zstbtn-cell {
	display: inline;
	float: left;
	height: 18px;
	overflow: hidden;
	padding: 2px;
	width: 18px;
}

.zstbtn-pp .zstbtn-cell-over {
	padding: 1px;
	border: 1px solid #666666;
}

.zstbtn-pp .zstbtn-cell-cnt {
	border: 1px solid #808080;
	height: 10px;
	overflow: hidden;
	width: 11px;
	margin: 2px;
	cursor: pointer;
}