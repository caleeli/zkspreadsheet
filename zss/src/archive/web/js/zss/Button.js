/* Button.js

{{IS_NOTE
	Purpose:
		
	Description:
		
	History:
		Jan 12, 2012 7:07:27 PM , Created by sam
}}IS_NOTE

Copyright (C) 2012 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
}}IS_RIGHT
*/
(function () {

if (zk.ie6_ && !jq.IE6_ALPHAFIX) {
	jq.IE6_ALPHAFIX = '.png';
}
var AU = {au: true},
	AbstractButtonHandler = {
		doMouseDown_: function (evt) {
			var wgt = this._wgt;
			if (wgt) {
				var sheet = wgt.sheetCtrl;
				if (this._pp) {//fake spreadsheet focus
					wgt.focus(false);
				} else if (sheet) {
					sheet._doMousedown(evt);
				}
			}
		},
		doMouseUp_: function (evt) {
			var wgt = this._wgt;
			if (wgt) {
				var sheet = wgt.sheetCtrl;
				if (this._pp) {//fake spreadsheet focus
					wgt.focus(false);
				} else if (sheet) {
					sheet._doMouseup(evt);
				}
			}
		}
	},
	AbstractPopupHandler = {
		doMouseDown_: function (evt) {
			var wgt = this._wgt;
			if (wgt) {//fake focus
				wgt.focus(false);
			}
		},
		doMouseUp_: function (evt) {
			var wgt = this._wgt;
			if (wgt) {//fake focus
				wgt.focus(false);
			}
		}
	};
zss.FontSizeCombobox = zk.$extends(zul.inp.Combobox, {
	$init: function (props, wgt) {
		this.$supers(zss.FontSizeCombobox, '$init', [props]);
		this._wgt = wgt;
	},
	_$action: 'fontSize',
	$define: {
		/**
		 * Represent button's action at server side
		 */
		$action: null
	},
	setDisabled: function (actions) {
		var d = this.isDisabled();
		if (actions.$contains(this.get$action())) {
			if (!d) {
				this.$supers(zss.FontSizeCombobox, 'setDisabled', [true]);
			}
		} else {
			if (d) {
				this.$supers(zss.FontSizeCombobox, 'setDisabled', [false]);
			}
		}
	},
	bind_: function () {
		this.$supers(zss.FontSizeCombobox, 'bind_', arguments);
		this.listen({onSelect: this});
		var wgt = this._wgt;
		if (wgt) {
			var sheet = wgt.sheetCtrl;
			if (sheet) {
				sheet.listen({onCellSelection: this.proxy(this._onCellSelection)});
			}
		}
	},
	unbind_: function () {
		var wgt = this._wgt;
		if (wgt) {
			var sheet = wgt.sheetCtrl;
			if (sheet) {
				sheet.unlisten({onCellSelection: this.proxy(this._onCellSelection)});
			}
		}
		this.unlisten({onSelect: this});
		this.$supers(zss.FontSizeCombobox, 'unbind_', arguments);
	},
	_onCellSelection: function (evt) {
		var d = evt.data,
			c = this._wgt.sheetCtrl.getCell(d.top, d.left);
		if (c) {
			this.setValue(c.getFontSize());
		}
	},
	onSelect: function (evt) {
		var wgt = this._wgt,
			sheet = wgt.sheetCtrl,
			sel = evt.data.reference;
		if (sheet && sel) {
			var s = sheet.getLastSelection(),
				tRow = s.top,
				lCol = s.left,
				bRow = s.bottom,
				rCol = s.right;
			sheet.triggerSelection(tRow, lCol, bRow, rCol);
			wgt.fireToolbarAction('fontSize', 
				{size: sel.getLabel(), tRow: tRow, lCol: lCol, bRow: bRow, rCol: rCol});
		}
	},
	_doBtnClick: function (evt) {
		var chd = this.firstChild;
		if (!chd) {
			var size = ['8', '9', '10', '11', '12', '14', '16', 
			            '18', '20', '22', '24', '26', '28', 
			            '36', '48', '72'];
			for (var i = 0, len = size.length; i < len; i++) {
				this.appendChild(new zul.inp.Comboitem({
					label: size[i],
					sclass: 'zsfontsize-' + size[i]
				}));
			}
		}
		this.$supers(zss.FontSizeCombobox, '_doBtnClick', arguments);
	},
	getSclass: function () {
		return 'zsfontsize'
	}
});
zk.copy(zss.FontSizeCombobox.prototype, AbstractPopupHandler);
	
zss.FontFamilyCombobox = zk.$extends(zul.inp.Combobox, {
	$init: function (props, wgt) {
		this.$supers(zss.FontFamilyCombobox, '$init', [props]);
		this._wgt = wgt;
	},
	_$action: 'fontFamily',
	$define: {
		/**
		 * Represent button's action at server side
		 */
		$action: null
	},
	setDisabled: function (actions) {
		var d = this.isDisabled();
		if (actions.$contains(this.get$action())) {
			if (!d) {
				this.$supers(zss.FontFamilyCombobox, 'setDisabled', [true]);
			}
		} else {
			if (d) {
				this.$supers(zss.FontFamilyCombobox, 'setDisabled', [false]);
			}
		}
	},
	bind_: function () {
		this.$supers(zss.FontFamilyCombobox, 'bind_', arguments);
		this.listen({onSelect: this});
		
		var wgt = this._wgt;
		if (wgt) {
			var sheet = wgt.sheetCtrl;
			if (sheet) {
				sheet.listen({onCellSelection: this.proxy(this._onCellSelection)});
			}
		}
	},
	unbind: function () {
		var wgt = this._wgt;
		if (wgt) {
			var sheet = wgt.sheetCtrl;
			if (sheet) {
				sheet.unlisten({onCellSelection: this.proxy(this._onCellSelection)});
			}
		}
		this.$supers(zss.FontFamilyCombobox, 'unbind', arguments);
	},
	_onCellSelection: function (evt) {
		var d = evt.data,
			c = this._wgt.sheetCtrl.getCell(d.top, d.left);
		if (c) {
			this.setValue(c.getFontName());
		}
	},
	unbind_: function () {
		this.unlisten({onSelect: this});
		this.$supers(zss.FontFamilyCombobox, 'unbind_', arguments);
	},
	onSelect: function (evt) {
		var wgt = this._wgt,
			sheet = wgt.sheetCtrl,
			sel = evt.data.reference;
		if (sheet && sel) {
			var s = sheet.getLastSelection(),
				tRow = s.top,
				lCol = s.left,
				bRow = s.bottom,
				rCol = s.right;
			sheet.triggerSelection(tRow, lCol, bRow, rCol);
			wgt.fireToolbarAction('fontFamily', 
				{name: sel.getLabel(), tRow: tRow, lCol: lCol, bRow: bRow, rCol: rCol});
		}
	},
	_doBtnClick: function (evt) {
		var chd = this.firstChild;
		if (!chd) {
			var prefix = 'zsfontfamily',
				fonts = ['Arial', 'Arial Black', 'Comic Sans MS',
			             'Courier New', 'Georgia', 'Impact', 
			             'Lucida Console', 'Lucida Sans Unicode',
			             'Palatino Linotype', 'Tahoma', 'Times New Roman',
			             'Trebuchet MS', 'Verdana', 'MS Sans Serif', 
			             'MS Serif'];
			for (var i = 0, len = fonts.length; i < len; i++) {
				var fontFamily = fonts[i],
					fs = fontFamily.toLowerCase().split(/\s+/),
					scls = prefix;
				
				for (var j = 0, fl = fs.length; j < fl; j++) {
					scls += ('-' + fs[j]);
				}
				this.appendChild(new zul.inp.Comboitem({
					label: fontFamily,
					sclass: scls
				}));
			}
		}
		this.$supers(zss.FontFamilyCombobox, '_doBtnClick', arguments);
	},
	getSclass: function () {
		return 'zsfontfamily';
	}
});
zk.copy(zss.FontFamilyCombobox.prototype, AbstractPopupHandler);
	
zss.ToolbarbuttonSeparator = zk.$extends(zul.wgt.Toolbarbutton, {
	$o: zk.$void,
	$define: {
		/**
		 * Represent button's action at server side
		 */
		$action: null
	},
	setDisabled: zk.$void,
	domTextStyleAttr_: function () {
		var u = zk.ajaxURI('/web/zss/img/sep.gif', {au: true});
		return zUtl.appendAttr("style", 'background:url(' + u + 
				') repeat-y scroll 0 0 transparent;height: 16px;width:2px;padding:2px 0;');
	},
	getSclass: function () {
		return 'zstbtn-sep';
	}
});

zss.Toolbarbutton = zk.$extends(zul.wgt.Toolbarbutton, {
	$o: zk.$void,
	//_defaultImage: null,
	/**
	 * The menupopup of the Toolbarbutton
	 */
	//_pp: null,
	$init: function (props, wgt) {
		this.$supers(zss.Toolbarbutton, '$init', [props]);
		this._wgt = wgt;
	},
	$define: {
		/**
		 * Represent button's action at server side
		 */
		$action: null,
		clickDisabled: null
	},
	setDisabled: function (actions) {
		if (actions) {
			var d = this.isDisabled();
			if (actions.$contains(this.get$action())) {
				if (!d)
					this.$supers(zss.Toolbarbutton, 'setDisabled', [true]);
			} else if (d) {//clear disabled
				this.$supers(zss.Toolbarbutton, 'setDisabled', [false]);
			}
			
			var pp = this._pp;
			if (pp && pp.setDisabled) {
				pp.setDisabled(actions);
			}
		} else {
			this.$supers(zss.Toolbarbutton, 'setDisabled', arguments);
		}
	},
	setImage: function (v) {
		if (!this._defaultImage) {
			this._defaultImage = v;
		}
		this.$supers(zss.Toolbarbutton, 'setImage', arguments);
	},
	setPopup: function (popup) {
		if (popup.open) {
			this._pp = popup;
		}
		this.$supers(zss.Toolbarbutton, 'setPopup', arguments);
	},
	/**
	 * @param boolean true set UI selected effect, set false to remove
	 * @param zss.Menuitem the menuitem that selected
	 */
	setSelectedEffect: function (seld, menuitem) {
		var pp = this._pp,
			$n = jq(this.$n()),
			src = null;
		if (seld && pp && menuitem) {
			var src = menuitem.getImage();
			this._seldImage = src;
				
			if (src) {
				this.setImage(src);
			}
		} else if (!menuitem) {
			this._seldImage = null;
			
			var src = this._defaultImage;
			if (src) {
				this.setImage(src);
			}
			$n.removeClass(this._getSclass() + '-seld');
		}
		
		if (seld) {
			$n.addClass(this._getSclass() + '-seld');	
		} else {
			$n.removeClass(this._getSclass() + '-seld');
		}
	},
	bind_: function () {
		this.$supers(zss.Toolbarbutton, 'bind_', arguments);
		var cave = this.$n('cave');
		if (cave) { //contains menupopup, expand button width
			var disd = this.isClickDisabled(),
				seld = this._seldImage,
				scls = this._getSclass(),
				$n = jq(this.$n()),
				cnt = cave.parentNode,
				$cv = jq(cave),
				cw = $cv.width() + $cv.zk.sumStyles("lr", jq.paddings),
				w = $n.width() + cw;
			if (w <= 32)//min size
				w = 32;
			this.setWidth(w + 'px');
			if (disd) {
				jq(cnt).addClass(scls + '-clk-disd');
			}
			if (seld) {
				$n.addClass(scls + '-seld');
			}
		}
	},
	doClick_: function (evt) {
		var	cv = this.$n('cave'),
			taget = evt.domTarget;
		if(!this.isDisabled() && cv && jq.isAncestor(cv, taget)) {
			var pp = this._pp;
			if (pp) {
				if (!pp.parent) {//menupopup not render yet
					this.appendChild(pp);
				}
				pp.open(this, null, 'after_start');	
			}
		} else {
			if (!this.isClickDisabled() && !this.isDisabled()) {
				this.fire('onClick');
			}
		}
		//controls fire onClick event, not invoke this.$supers('doClick_')
	},
	doMouseOver_: function (evt) {
		var	cv = this.$n('cave'),
			taget = evt.domTarget;
		if (!this.isDisabled() && cv) {
			if (jq.isAncestor(cv, taget)) {
				jq(cv).addClass(this._getSclass() + '-cave-over');
			}	
		}
		this.$supers(zss.Toolbarbutton, 'doMouseOver_', arguments);
	},
	doMouseOut_: function (evt) {
		var cv = this.$n('cave');
		if (cv) {
			jq(cv).removeClass(this._getSclass() + '-cave-over');
		}
		this.$supers(zss.Toolbarbutton, 'doMouseOut_', arguments);
	},
	domContent_: function () {
		var cnt = this.$supers(zss.Toolbarbutton, 'domContent_', arguments);
		if (this.getPopup()) {
			var uid = this.uuid,
				scls = this._getSclass();
			return '<div id="' + uid + '-real" class="' + scls + '-real">' + 
				cnt + '</div><div id="' + uid + '-cave" class="' + 
				scls +'-cave"><div class="' + scls +'-arrow"></div></div>';
		} else {
			return cnt;
		}
	},
	_getSclass: function () {
		return 'zstbtn';
	},
	getSclass: function () {
		return 'zstbtn-' + this.get$action() + ' ' + this._getSclass();
	}
}, {
	_rmActive: function (wgt) {
		var n = wgt.$n(),
			cv = wgt.$n('cave');
		if (cv) {
			jq(cv).removeClass(wgt._getSclass() + '-cave-over');
		}
		jq(n).removeClass(wgt._getSclass() + '-over');
	}
});
zk.copy(zss.Toolbarbutton.prototype, AbstractButtonHandler);

zss.UploadToolbarbutton = zk.$extends(zss.Toolbarbutton, {
	_upload: 'true',
	$init: function (props, wgt) {
		this.$supers(zss.UploadToolbarbutton, '$init', [props]);
		this._wgt = wgt;
	},
	$define: {
		/**
		 * Represent button's action at server side
		 */
		$action: null
	},
	bind_: function(){
		//jump zul.wgt.Toolbarbutton, override it.
		this.$supers(zul.wgt.Toolbarbutton, 'bind_', arguments);
		
		var upload = this._wgt.getUpload();
		if (upload) {
			var u = upload.getUploader(this.get$action());//server side upload event listener
			if (u) {
				u.parasitize(this);
				u._uplder = new zul.Upload(u, null, this._upload);
			}
		}
	},
	doClick_: function(evt) {
		var wgt = this._wgt,
			sheet = wgt.sheetCtrl;
		if (sheet) {
			var s = sheet.getLastSelection();
			wgt.fireToolbarAction(this.get$action(), {color: '', tRow: s.top, lCol: s.left, bRow: s.bottom, rCol: s.right});
			jq(this.$n()).find('input').trigger('click');
		}
	}
});
zk.copy(zss.UploadToolbarbutton.prototype, AbstractPopupHandler);

zss.CheckableToolbarButton = zk.$extends(zul.wgt.Toolbarbutton, {
	$init: function (props, wgt) {
		this.$supers(zss.CheckableToolbarButton, '$init', [props]);
		this.setWidth('36px');
		this._wgt = wgt;
	},
	$define: {
		/**
		 * Represent button's action at server side
		 */
		$action: null,
		/** Returns whether it is checked.
		 * <p>Default: false.
		 * @return boolean
		 */
		/** Sets whether it is checked.
		 * @param boolean checked
		 */
		checked: function (v) {
			var n = this.$n('real');
			if (n) {
				n.style.backgroundImage = 'url(' + this.getCheckImage() + ')';
			}
		}
	},
	setDisabled: function (actions) {
		var d = this.isDisabled();
		if (actions.$contains(this.get$action())) {
			if (!d)
				this.$supers(zss.CheckableToolbarButton, 'setDisabled', [true]);
		} else if (d) {//clear disabled
			this.$supers(zss.CheckableToolbarButton, 'setDisabled', [false]);
		}
	},
	getCheckImage: function () {
		return zk.ajaxURI('/web/zss/img/' + ((this.isChecked() ? 'ui-check-box' : 'ui-check-box-uncheck')) + (zk.ie6_ ? '.gif' : '.png'), {au: true});
	},
	domContent_: function () {
		return '<div id="' + this.uuid + '-real" class="' + this.getSclass() + '-check" style="background: url(' + 
			this.getCheckImage() +') no-repeat transparent;"></div>' + this.$supers(zul.wgt.Toolbarbutton, 'domContent_', arguments);
	},
	getSclass: function () {
		return 'zschktbtn-' + this.get$action() + ' zschktbtn';
	}
});
zk.copy(zss.CheckableToolbarButton.prototype, AbstractButtonHandler);

zss.ProtectSheetCheckbutton = zk.$extends(zss.CheckableToolbarButton, {
	_$action: 'protectSheet',
	bind_: function () {
		var sheet = this._wgt.sheetCtrl;
		if (sheet) {
			sheet.listen({'onProtectSheet': this.proxy(this.onProtectSheet)});
		}
		this.$supers(zss.ProtectSheetCheckbutton, 'bind_', arguments);
	},
	onProtectSheet: function (evt) {
		this.setChecked(evt.data.protect);
	}
});

zss.DisplayGridlinesCheckbutton = zk.$extends(zss.CheckableToolbarButton, {
	_$action: 'gridlines',
	bind_: function () {
		var sheet = this._wgt.sheetCtrl;
		if (sheet) {
			sheet.listen({'onDisplayGridlines': this.proxy(this.onDisplayGridlines)});
		}
		this.$supers(zss.ProtectSheetCheckbutton, 'bind_', arguments);
	},
	onDisplayGridlines: function (evt) {
		this.setChecked(evt.data.show);
	}
});

if (zk.feature.pe) {
	zk.load('zkex.inp', null, function () { 
		zss.Colorbutton = zk.$extends(zss.Toolbarbutton, {
			widgetName: 'Colorbutton',
			_color: '#000000', /*default color*/
			$init: function (props, wgt) {
				this.$supers(zss.Colorbutton, '$init', [props]);
				
				var thispt = zss.Colorbutton.prototype,
					superpt = zkex.inp.Colorbox.prototype;
				thispt.openPopup = superpt.openPopup;
				thispt._$closePopup = superpt.closePopup;//need customize closePopup
				thispt.openPalette = superpt.openPalette;
				thispt.closePalette = superpt.closePalette;
				thispt.openPicker = superpt.openPicker;
				thispt.closePicker = superpt.closePicker;
				thispt._$onHide = superpt.onHide;//need customize onHide
				thispt._getPosition = superpt._getPosition;
				thispt._syncPopupPosition = superpt._syncPopupPosition;
				thispt._syncShadow = superpt._syncShadow;
				thispt._hideShadow = superpt._hideShadow;
				
				this._wgt = wgt;
				this._currColor = new zkex.inp.Color();
				this._picker = new zkex.inp.PickerPop({_wgt: this});
				var palette = this._palette = new zkex.inp.PalettePop({_wgt: this});
				palette.open = this.proxy(this.openPopup);
					
				this.setPopup(palette);//default open color palette
			},
			$define: {
				/** Sets the image URI.
				 * @param String image the URI of the image
				 */
				/** Returns the image URI.
				 * <p>Default: null.
				 * @return String
				 */
				image: function (v) {
					var n = this.getImageNode();
					if (n) n.src = v || '';
				},
				/**
				 * Sets the color
				 * @param color in #RRGGBB format (hexdecimal).
				 */
				/**
				 * Returns the color
				 * @return string
				 */
				color: function (hex) {
					var c = this.$n('color');
					if (c)
						c.style.backgroundColor = hex;
				}
			},
			closePopup: function () {
				this._$closePopup();
				jq(this.$n()).removeClass('z-toolbarbutton-over');
			},
			onHide: function () {
				this._$onHide();
				jq(this.$n()).removeClass('z-toolbarbutton-over');
			},
			unbind_: function () {
				var paletteBtn = this.$n('palette-btn'),
					pickerBtn = this.$n('picker-btn');
				if (paletteBtn) {
					this.domUnlisten_(paletteBtn, 'onClick', 'openPalette');
				}
				if (pickerBtn) {
					this.domUnlisten_(this.$n('picker-btn'), 'onClick', 'openPicker')
				}
				this.$supers(zss.Colorbutton, 'unbind_', arguments);
			},
			appnedPopup_: function () {
				this.appendChild(this._picker);
				this.appendChild(this._palette);
				
				var paletteBtn = this.$n('palette-btn'),
					pickerBtn = this.$n('picker-btn');
				if (pickerBtn) {
					this.domListen_(pickerBtn, 'onClick', 'openPicker');
				}
				if (paletteBtn) {
					this.domListen_(paletteBtn, 'onClick', 'openPalette');
				}
			},
			onChange: function (hex) {//invoke from Color Palette, not from event
				this.setColor(hex);
				this.fire('onClick');
				var wgt = this._wgt;
				if (wgt) {
					wgt.focus(false);
				}
			},
			insertChildHTML_: function (child, before, desktop) {
				jq(this.$n('pp')).append(child.redrawHTML_()); //color palette and color picker
				child.bind(desktop);
			},
			domContent_: function () {
				var uid = this.uuid,
					cnt = this.$supers(zss.Colorbutton, 'domContent_', arguments),
					color = this.getColor();
				return cnt + '<div id="' + uid + '-color" class="zstbtn-color" style="background:' 
					+ this.getColor() + ';"></div><div id="' + uid + '-pp" style="display:none;" class="z-colorbtn-pp">' +
					'<div id="' + uid + '-palette-btn" class="z-colorbtn-palette-btn"></div><div id="' + 
					uid + '-picker-btn" class="z-colorbtn-picker-btn"></div></div>';//Note. use Colorbox's "z-colorbtn-pp"
			}
		});
	})
} else {//ZK CE version
	zss.Colorbutton = zk.$extends(zss.Toolbarbutton, {
		_open: false,
		_color: '#000000', /*default color*/
		$init: function (props, wgt) {
			this.$supers(zss.Colorbutton, '$init', [props]);
			this._wgt = wgt;
			
			this._colors = ["#000000","#993300","#333300","#003300","#000080",
			              "#003366","#660066","#333333","#800000","#FF8080",
			              "#808000","#008000","#008080","#0000FF","#800080",
			              "#969696","#FF0000","#FF6600","#FFFF66","#99CC00",
			              "#33CCCC","#0066CC","#6666FD","#808080","#FF00FF",
			              "#FF9900","#FFFF00","#00FF00","#00FFFF","#00CCFF",
			              "#CC99FF","#C0C0C0","#FF99CC","#FFCB90","#FFFF99",
			              "#CCFFCC","#CCFFFF","#99CCFF","#9999FF","#FFFFFF"];
		},
		$define: {
			/** Sets the image URI.
			 * @param String image the URI of the image
			 */
			/** Returns the image URI.
			 * <p>Default: null.
			 * @return String
			 */
			image: function (v) {
				var n = this.getImageNode();
				if (n) n.src = v || '';
			},
			/**
			 * Sets the color
			 * @param color in #RRGGBB format (hexdecimal).
			 */
			/**
			 * Returns the color
			 * @return string
			 */
			color: function (hex) {
				var c = this.$n('color');
				if (c)
					c.style.backgroundColor = hex;
			}
		},
		bind_: function () {
			this.$supers('bind_', arguments);
			zWatch.listen({onFloatUp: this});
		},
		unbind_: function () {
			zWatch.unlisten({onFloatUp: this});
			this.$supers(PalettePop, 'unbind_', arguments);
		},
		onFloatUp: function (ctl) {
			if (!zUtl.isAncestor(this, ctl.origin))
				this.closePopup();
		},
		doMouseOver_: _onCell = function (evt) {
			var target = evt.domTarget,
				scls = this.getSclass();
			if (jq(target).parents('.' + scls + '-pp')[0]) {
				jq(target).parents('.' + scls + '-cell')
				[evt.name == 'onMouseOver' ? 'addClass' : 'removeClass'](scls + '-cell-over');
			} else
				jq(this.$n())[evt.name == 'onMouseOver' ? 'addClass' : 'removeClass'](scls + '-over');
		},
		doMouseOut_: _onCell,
		doClick_: function (evt) {
			var t = evt.domTarget,
				p = this.$n('pp'),
				$t = jq(evt.domTarget),
				scls = this.getSclass();

			if (jq.isAncestor(p, t)) {
				if ($t.attr('class').indexOf(scls + '-cell-cnt') >= 0) {
					var hex = $t.children('i').text();
					
					this.setColor(hex);
					this.fire('onClick');
					this.closePopup();
					$t.parent().removeClass(scls + '-cell-over');	
				}
			} else {
				if (this._open) {
					this.closePopup();
				} else {
					this.openPopup();
				}
			}
		},
		openPopup: function () {
			this._open = true;
			var node = this.$n(),
				pp = this.$n("pp");

			pp.style.position = "absolute";
			pp.style.overflow = "auto";
			pp.style.display = "block";
			pp.style.zIndex = "88000";

			jq(pp).zk.makeVParent();
			zk(pp).position(node, this._getPosition());
		},
		_getPosition: function () {
			var parent = this.parent;
			if (!parent) return;
			if (parent.$instanceof(zul.wgt.Toolbar))
				return 'vertical' == parent.getOrient() ? 'end_before' : 'after_start';
			return 'after_start';
		},
		closePopup: function () {
			this._open = false;
			var node = this.$n(),
				pp = this.$n("pp");
			jq(pp).zk.undoVParent();
		},
		domContent_: function () {
			var uid = this.uuid,
				cnt = this.$supers(zss.Colorbutton, 'domContent_', arguments),
				cols = this._colors,
				colSize = cols.length,
				scls = this.getSclass(),
				color = this.getColor();
			cnt = cnt + '<div id="' + uid + '-color" class="' + this.getSclass() + 
				'-color" style="background:' + this.getColor() + ';"></div><div id="' + uid + '-pp" style="display:none;" class="' + scls + '-pp">';
				
			for (var i = 0; i < colSize; i++) {
				cnt += '<div class="' + scls + '-cell"><div style="background: ' + cols[i] + 
					';" class="' + scls + '-cell-cnt"><i style="display: none">' + cols[i] + '</i></div></div>';
			}	
			cnt += '</div>';//Note. use Colorbox's "z-colorbtn-pp"
			return cnt;
		}
	});
}


zss.Menu = zk.$extends(zul.menu.Menu, {
	bind_: function () {
		if (zk.feature.pe && this.getContent()) {//color menu
			this.listen({'onChange': this});
		}
		this.$supers(zss.Menu, 'bind_', arguments);
	},
	unbind_: function () {
		if (zk.feature.pe && this.getContent()) {//color menu
			this.unlisten({'onChange': this});
		}
		this.$supers(zss.Menu, 'unbind_', arguments);
	},
	setDisabled: function (actions) {
		var pp = this.menupopup;
		if (pp && pp.setDisabled) {
			pp.setDisabled(actions);
		}
	},
	getColor: function () {
		return this.color;
	},
	onChange: function (evt) {
		this.color = evt.data.color;
	},
	getSclass: function () {
		return zk.feature.pe && this.getContent() ? 
				'zscolormenu' : 'zsmenu-' + this._sclass;
	}
});

zss.Menuitem = zk.$extends(zul.menu.Menuitem, {
	$init: function (props, wgt) {
		this.$supers(zss.Menuitem, '$init', [props]);
		this._wgt = wgt;
	},
	$define: {
		/**
		 * Represent button's action at server side
		 */
		$action: null
	},
	setDisabled: function (actions) {
		if (jq.isArray(actions)) {
			var d = this.isDisabled();
			if (actions.$contains(this.get$action())) {
				if (!d)
					this.$supers(zss.Menuitem, 'setDisabled', [true]);
			} else if (d) {//clear disabled
				this.$supers(zss.Menuitem, 'setDisabled', [false]);
			}	
		} else {
			this.$supers(zss.Menuitem, 'setDisabled', arguments);
		}
	},
	getSclass: function () {
		return 'zsmenuitem-' + this.get$action();
	}
});
zk.copy(zss.Menuitem.prototype, AbstractPopupHandler);

zss.Menupopup = zk.$extends(zul.menu.Menupopup, {
	$init: function (wgt) {
		this.$supers(zss.Menupopup, '$init', []);
		this._wgt = wgt;
	},
	setDisabled: function (actions) {
		var chd = this.firstChild;
		for (;chd; chd = chd.nextSibling) {
			if (!chd.setDisabled) {//Menuseparator
				continue;
			}
			
			chd.setDisabled(actions);
		}
	},
	open: function () {
		this.$supers(zss.Menupopup, 'open', arguments);
		var wgt = this._wgt;
		if (wgt) {//fake focus
			wgt.focus(false);
		}
	}
}, {
	_rmActive: function (wgt) {
		if (wgt.parent.$instanceof(zul.menu.Menu)) {
			wgt.parent.$class._rmActive(wgt.parent);
		} else if (wgt.parent.$instanceof(zss.Toolbarbutton)) {
			wgt.parent.$class._rmActive(wgt.parent);
		}
	}
});

	function newActionMenuitem(wgt, action, image) {
		return new zss.Menuitem({
			$action: action,
			image: image ? zk.ajaxURI(image, {au: true}) : null,
			label: wgt._labelsCtrl['get' + action.charAt(0).toUpperCase() + action.substr(1)](),
			onClick: function () {
				var sheet = wgt.sheetCtrl;
				if (sheet) {
					var s = sheet.getLastSelection(),
						tRow = s.top,
						lCol = s.left,
						bRow = s.bottom,
						rCol = s.right;
					sheet.triggerSelection(tRow, lCol, bRow, rCol);
					wgt.fireToolbarAction(action, {tRow: tRow, lCol: lCol, bRow: bRow, rCol: rCol});
				}
			}
		});
	}
	
	function newColorActionMenuitem(wgt, colorWidget, action, image) {
		return new zss.Menuitem({
			$action: action,
			image: image ? zk.ajaxURI(image, {au: true}) : null,
			label: wgt._labelsCtrl['get' + action.charAt(0).toUpperCase() + action.substr(1)](),
			onClick: function () {
				var sheet = wgt.sheetCtrl;
				if (sheet) {
					var s = sheet.getLastSelection(),
						color = colorWidget ? colorWidget.getColor() : '';
					wgt.fireToolbarAction(action, {color: color, tRow: s.top, lCol: s.left, bRow: s.bottom, rCol: s.right});
				}
			}
		});
	}
	
zss.StylePanel = zk.$extends(zul.wgt.Popup, {
	$init: function (wgt) {
		this.$supers(zss.StylePanel, '$init', []);
		this._wgt = wgt;
		
		var	self = this,
			tb = new zul.wgt.Toolbar({sclass: 'zsstylepanel-toolbar'}),
			builder = new zss.ButtonBuilder(wgt),
			btns = builder.addAll(['fontFamily', 'fontSize', 'fontBold', 'fontItalic']).build(),
			fontFamily = btns[0],
			fontSize = btns[1],
			b;
		
		wgt.listen({onZSSAction: this.proxy(this._closeStylePanel)});
		fontFamily.setWidth('75px');
		
		fontSize.setWidth('48px');
		while (b = btns.shift()) {
			tb.appendChild(b);
		}
		this.appendChild(tb);
		
		tb = new zul.wgt.Toolbar({sclass: 'zsstylepanel-toolbar'});
		btns = builder.addAll(['fontColor', 'fillColor', 'border', 'verticalAlign', 'horizontalAlign']).build();
		while (b = btns.shift()) {
			tb.appendChild(b);
		}
		this.appendChild(tb);
	},
	_closeStylePanel: function () {
		this.close({sendOnOpen:true});
	},
	//override: spreadsheet's menuitem will trigger onFloatUp, cause StylePanel disapper
	onFloatUp: function (ctl) {
		if (!this.isVisible()) 
			return;
		
		var	c = ctl.origin,
			wgt = this._wgt,//spreadsheet
			sheet = wgt.sheetCtrl;
		if (sheet) {
			var p = sheet.getCellMenupopup();
			if (p && p.isOpen() && zUtl.isAncestor(p, c)) {
				return;
			}
			
			p = sheet.getColumnHeaderMenupopup();
			if (p && p.isOpen() && zUtl.isAncestor(p, c)) {
				return;
			}
			
			p = sheet.getRowHeaderMenupopup();
			if (p && p.isOpen() && zUtl.isAncestor(p, c)) {
				return;
			}
		}
		
		this.$supers(zss.StylePanel, 'onFloatUp', arguments);
	},
	getSclass: function () {
		return 'zsstylepanel';
	}
});
	
zss.MenupopupFactory = zk.$extends(zk.Object, {
	$init: function (wgt) {
		this._wgt = wgt;
	},
	/**
	 * Returns a newly-created paste menupopup
	 */
	paste: function () {
		var wgt = this._wgt,
			p = new zss.Menupopup();
		
		p.appendChild(newActionMenuitem(wgt, 'paste', '/web/zss/img/clipboard-paste.png'));
		p.appendChild(newActionMenuitem(wgt, 'pasteFormula'));
		p.appendChild(newActionMenuitem(wgt, 'pasteValue'));
		p.appendChild(newActionMenuitem(wgt, 'pasteAllExceptBorder'));
		p.appendChild(newActionMenuitem(wgt, 'pasteTranspose'));
		p.appendChild(newActionMenuitem(wgt, 'pasteSpecial'));
		return p;
	},
	/**
	 * Return a newly-created border menupopup
	 * @return zul.menu.Menupopup
	 */
	border: function () {
		var wgt = this._wgt,
			p = new zss.Menupopup(),
			colorMenu = zk.feature.pe ? new zss.Menu({
				$action: 'borderColor',
				label: wgt._labelsCtrl.getBorderColor(),
				content: '#color=#000000'
			}) : null;
			
		if (colorMenu) {//for toolbarbutton to get color
			p.colorMenu = colorMenu;
		}

		p.appendChild(newColorActionMenuitem(wgt, colorMenu, 'borderBottom', '/web/zss/img/border-bottom.png'));
		p.appendChild(newColorActionMenuitem(wgt, colorMenu, 'borderTop', '/web/zss/img/border-top.png'));
		p.appendChild(newColorActionMenuitem(wgt, colorMenu, 'borderLeft', '/web/zss/img/border-left.png'));
		p.appendChild(newColorActionMenuitem(wgt, colorMenu, 'borderRight', '/web/zss/img/border-right.png'));
		p.appendChild(new zul.menu.Menuseparator());
		
		p.appendChild(newColorActionMenuitem(wgt, colorMenu, 'borderNo', '/web/zss/img/border.png'));
		p.appendChild(newColorActionMenuitem(wgt, colorMenu, 'borderAll', '/web/zss/img/border-all.png'));
		p.appendChild(newColorActionMenuitem(wgt, colorMenu, 'borderOutside', '/web/zss/img/border-outside.png'));
		p.appendChild(newColorActionMenuitem(wgt, colorMenu, 'borderInside', '/web/zss/img/border-inside.png'));
		p.appendChild(new zul.menu.Menuseparator());
		
		p.appendChild(newColorActionMenuitem(wgt, colorMenu, 'borderInsideHorizontal', '/web/zss/img/border-horizontal.png'));
		p.appendChild(newColorActionMenuitem(wgt, colorMenu, 'borderInsideVertical', '/web/zss/img/border-vertical.png'));
		
		if (colorMenu) {
			p.appendChild(new zul.menu.Menuseparator());
			p.appendChild(colorMenu);
		}
		return p;
	},
	verticalAlign: function () {
		var wgt = this._wgt,
			p = new zss.Menupopup();
		
		p.appendChild(newActionMenuitem(wgt, 'verticalAlignTop', '/web/zss/img/edit-vertical-alignment-top.png'));
		p.appendChild(newActionMenuitem(wgt, 'verticalAlignMiddle', '/web/zss/img/edit-vertical-alignment-middle.png'));
		p.appendChild(newActionMenuitem(wgt, 'verticalAlignBottom', '/web/zss/img/edit-vertical-alignment.png'));
		return p;
	},
	horizontalAlign: function () {
		var wgt = this._wgt,
			p = new zss.Menupopup();
		
		p.appendChild(newActionMenuitem(wgt, 'horizontalAlignLeft', '/web/zss/img/edit-alignment.png'));
		p.appendChild(newActionMenuitem(wgt, 'horizontalAlignCenter', '/web/zss/img/edit-alignment-center.png'));
		p.appendChild(newActionMenuitem(wgt, 'horizontalAlignRight', '/web/zss/img/edit-alignment-right.png'));
		return p;
	},
	mergeAndCenter: function () {
		var wgt = this._wgt,
			p = new zss.Menupopup();
		
		p.appendChild(newActionMenuitem(wgt, 'mergeAndCenter'));
		p.appendChild(newActionMenuitem(wgt, 'mergeAcross'));
		p.appendChild(newActionMenuitem(wgt, 'mergeCell'));
		p.appendChild(newActionMenuitem(wgt, 'unmergeCell'));
		return p;
	},
	insert: function () {
		var wgt = this._wgt,
			p = new zss.Menupopup(),
			insertCellMenu = new zss.Menu({
				label: wgt._labelsCtrl.getInsertCell(),
				sclass: 'insertCell'
			}),
			insertCellMP = new zss.Menupopup();
		
		insertCellMP.appendChild(newActionMenuitem(wgt, 'shiftCellRight'));
		insertCellMP.appendChild(newActionMenuitem(wgt, 'shiftCellDown'));
		insertCellMenu.appendChild(insertCellMP);
		p.appendChild(insertCellMenu);
		
		p.appendChild(newActionMenuitem(wgt, 'insertSheetRow'));
		p.appendChild(newActionMenuitem(wgt, 'insertSheetColumn'));
		return p;
	},
	del: function () {
		var wgt = this._wgt,
			p = new zss.Menupopup(),
			deleteCellMenu = new zss.Menu({
				label: wgt._labelsCtrl.getDeleteCell(),
				sclass: 'deleteCell'
			}),
			deleteCellMP = new zss.Menupopup();
		deleteCellMP.appendChild(newActionMenuitem(wgt, 'shiftCellLeft'));
		deleteCellMP.appendChild(newActionMenuitem(wgt, 'shiftCellUp'));
		deleteCellMenu.appendChild(deleteCellMP);
		p.appendChild(deleteCellMenu);
		
		p.appendChild(newActionMenuitem(wgt, 'deleteSheetRow'));
		p.appendChild(newActionMenuitem(wgt, 'deleteSheetColumn'));
		return p;
	},
	//TODO
	format: function () {
		
	},
	autoSum: function () {
		var wgt = this._wgt,
			p = new zss.Menupopup();
		
		p.appendChild(newActionMenuitem(wgt, 'autoSum'));
		p.appendChild(newActionMenuitem(wgt, 'average'));
		p.appendChild(newActionMenuitem(wgt, 'countNumber'));
		p.appendChild(newActionMenuitem(wgt, 'max'));
		p.appendChild(newActionMenuitem(wgt, 'min'));
		p.appendChild(newActionMenuitem(wgt, 'moreFunction'));
		return p;
	},
	clear: function () {
		var wgt = this._wgt,
			p = new zss.Menupopup();
		
		p.appendChild(newActionMenuitem(wgt, 'clearContent'));
		p.appendChild(newActionMenuitem(wgt, 'clearStyle'));
		p.appendChild(newActionMenuitem(wgt, 'clearAll'));
		return p;
	},
	sortAndFilter: function () {
		var wgt = this._wgt,
			p = new zss.Menupopup();
		
		p.appendChild(newActionMenuitem(wgt, 'sortAscending', '/web/zss/img/asc.png'));
		p.appendChild(newActionMenuitem(wgt, 'sortDescending', '/web/zss/img/des.png'));
		p.appendChild(newActionMenuitem(wgt, 'customSort'));
		p.appendChild(newActionMenuitem(wgt, 'filter', '/web/zss/img/funnel--pencil.png'));
		p.appendChild(newActionMenuitem(wgt, 'clearFilter', '/web/zss/img/funnel--minus.png'));
		p.appendChild(newActionMenuitem(wgt, 'reapplyFilter', '/web/zss/img/funnel--arrow.png'));
		return p;
	},
	columnChart: function () {
		var wgt = this._wgt,
			p = new zss.Menupopup();
		
		p.appendChild(newActionMenuitem(wgt, 'columnChart'));
		p.appendChild(newActionMenuitem(wgt, 'columnChart3D'));
		return p;
	},
	lineChart: function () {
		var wgt = this._wgt,
			p = new zss.Menupopup();
		
		p.appendChild(newActionMenuitem(wgt, 'lineChart'));
		p.appendChild(newActionMenuitem(wgt, 'lineChart3D'));
		return p;
	},
	pieChart: function () {
		var wgt = this._wgt,
			p = new zss.Menupopup();
		
		p.appendChild(newActionMenuitem(wgt, 'pieChart'));
		p.appendChild(newActionMenuitem(wgt, 'pieChart3D'));
		return p;
	},
	barChart: function () {
		var wgt = this._wgt,
			p = new zss.Menupopup();
		
		p.appendChild(newActionMenuitem(wgt, 'barChart'));
		p.appendChild(newActionMenuitem(wgt, 'barChart3D'));
		return p;
	},
	otherChart: function () {
		var wgt = this._wgt,
			p = new zss.Menupopup();		
		p.appendChild(newActionMenuitem(wgt, 'doughnutChart'));
		return p;
	},
	rowHeader: function () {
		var wgt = this._wgt,
			p = new zss.Menupopup(wgt);
		p.appendChild(newActionMenuitem(wgt, 'cut'));
		p.appendChild(newActionMenuitem(wgt, 'copy'));
		p.appendChild(newActionMenuitem(wgt, 'paste'));
		p.appendChild(newActionMenuitem(wgt, 'pasteSpecial'));
		p.appendChild(new zul.menu.Menuseparator());
		
		p.appendChild(newActionMenuitem(wgt, 'insertSheetRow'));
		p.appendChild(newActionMenuitem(wgt, 'deleteSheetRow'));
		p.appendChild(newActionMenuitem(wgt, 'clearContent'));
		p.appendChild(new zul.menu.Menuseparator());
		
		p.appendChild(newActionMenuitem(wgt, 'formatCell'));
		p.appendChild(newActionMenuitem(wgt, 'rowHeight'));
		p.appendChild(newActionMenuitem(wgt, 'hideRow'));
		p.appendChild(newActionMenuitem(wgt, 'unhideRow'));
		
		return p;
	},
	columnHeader: function () {
		var wgt = this._wgt,
			p = new zss.Menupopup(wgt);
		p.appendChild(newActionMenuitem(wgt, 'cut'));
		p.appendChild(newActionMenuitem(wgt, 'copy'));
		p.appendChild(newActionMenuitem(wgt, 'paste'));
		p.appendChild(newActionMenuitem(wgt, 'pasteSpecial'));
		p.appendChild(new zul.menu.Menuseparator());
		
		p.appendChild(newActionMenuitem(wgt, 'insertSheetColumn'));
		p.appendChild(newActionMenuitem(wgt, 'deleteSheetColumn'));
		p.appendChild(newActionMenuitem(wgt, 'clearContent'));
		p.appendChild(new zul.menu.Menuseparator());
		
		p.appendChild(newActionMenuitem(wgt, 'formatCell'));
		p.appendChild(newActionMenuitem(wgt, 'columnWidth'));
		p.appendChild(newActionMenuitem(wgt, 'hideColumn'));
		p.appendChild(newActionMenuitem(wgt, 'unhideColumn'));
		
		return p;
	},
	cell: function () {
		var wgt = this._wgt,
			p = new zss.Menupopup(wgt),
			insertMenu = new zss.Menu({
				label: wgt._labelsCtrl.getInsert(),
				sclass: 'insert'
			}),
			insertMP = new zss.Menupopup(),
			deleteMenu = new zss.Menu({
				label: wgt._labelsCtrl.getDel(),
				sclass: 'del'
			}),
			deleteMP = new zss.Menupopup(),
			filterMenu = new zss.Menu({
				label: wgt._labelsCtrl.getFilter(),
				sclass: 'filter'
			}),
			filterMP = new zss.Menupopup(),
			sortMenu = new zss.Menu({
				label: wgt._labelsCtrl.getSort(),
				sclass: 'sort'
			}),
			sortMP = new zss.Menupopup();
		p.appendChild(newActionMenuitem(wgt, 'cut'));
		p.appendChild(newActionMenuitem(wgt, 'copy'));
		p.appendChild(newActionMenuitem(wgt, 'paste'));
		p.appendChild(newActionMenuitem(wgt, 'pasteSpecial'));
		p.appendChild(new zul.menu.Menuseparator());
		
		insertMP.appendChild(newActionMenuitem(wgt, 'shiftCellRight'));
		insertMP.appendChild(newActionMenuitem(wgt, 'shiftCellDown'));
		insertMP.appendChild(newActionMenuitem(wgt, 'insertSheetRow'));
		insertMP.appendChild(newActionMenuitem(wgt, 'insertSheetColumn'));
		insertMenu.appendChild(insertMP);
		p.appendChild(insertMenu);
		
		deleteMP.appendChild(newActionMenuitem(wgt, 'shiftCellLeft'));
		deleteMP.appendChild(newActionMenuitem(wgt, 'shiftCellUp'));
		deleteMP.appendChild(newActionMenuitem(wgt, 'deleteSheetRow'));
		deleteMP.appendChild(newActionMenuitem(wgt, 'deleteSheetColumn'));
		deleteMenu.appendChild(deleteMP);
		p.appendChild(deleteMenu);
		
		p.appendChild(newActionMenuitem(wgt, 'clearContent'));
		p.appendChild(new zul.menu.Menuseparator());
		
		filterMP.appendChild(newActionMenuitem(wgt, 'reapplyFilter', '/web/zss/img/funnel--arrow.png'));
		filterMP.appendChild(newActionMenuitem(wgt, 'filter', '/web/zss/img/funnel--pencil.png'));
		filterMenu.appendChild(filterMP);
		p.appendChild(filterMenu);
		
		sortMP.appendChild(newActionMenuitem(wgt, 'sortAscending', '/web/zss/img/asc.png'));
		sortMP.appendChild(newActionMenuitem(wgt, 'sortDescending', '/web/zss/img/des.png'));
		sortMP.appendChild(newActionMenuitem(wgt, 'customSort'));
		sortMenu.appendChild(sortMP);
		p.appendChild(sortMenu);
		p.appendChild(new zul.menu.Menuseparator());
		
		p.appendChild(newActionMenuitem(wgt, 'formatCell'));
		p.appendChild(newActionMenuitem(wgt, 'hyperlink'));
		return p;
	},
	style: function () {
		return new zss.StylePanel(this._wgt);
	}
});

zss.Buttons = zk.$extends(zk.Object, {
}, {//static
	HOME_DEFAULT: ['newBook', 'saveBook', 'exportPDF', 'separator', 
	          'paste', 'cut', 'copy', 'separator',
	          'fontFamily', 'fontSize', 'fontBold', 'fontItalic', 'fontUnderline', 
	          'fontStrike', 'border', 'fontColor', 'fillColor', 'separator',
	          'verticalAlign', 'horizontalAlign', 'wrapText', 'mergeAndCenter', 'separator',
	          'insert', 'del', 'format', 'separator',
	          'autoSum', 'clear', 'sortAndFilter', 'separator',
	          'protectSheet', 'gridlines'
	          ],
	 INSERT_DEFAULT: ['insertPicture', 'separator',
	                  'columnChart', 'lineChart', 'pieChart', 
	                  'barChart', 'areaChart', 'scatterChart', 
	                  'otherChart', 'separator',
	                  'hyperlink'],
	 FORMULA_DEFAULT: ['insertFunction', 'autoSum', 'financial', 
	                   'logical', 'text', 'dateAndTime', 
	                   'lookupAndReference', 'mathAndTrig', 'moreFunction']
});

	function newActionToolbarbutton(wgt, action, image, labelOnly) {
		var val = wgt._labelsCtrl['get' + action.charAt(0).toUpperCase() + action.substr(1)]();
		return new zss.Toolbarbutton({
			$action: action,
			tooltiptext: labelOnly ? null : val,
			label: labelOnly ? val : null,
			image: image ? zk.ajaxURI(image, {au: true}) : null,
			onClick: function () {
				var sheet = wgt.sheetCtrl;
				if (sheet) {
					var s = sheet.getLastSelection(),
						tRow = s.top,
						lCol = s.left,
						bRow = s.bottom,
						rCol = s.right;
					sheet.triggerSelection(tRow, lCol, bRow, rCol);
					wgt.fireToolbarAction(action, {tRow: tRow, lCol: lCol, bRow: bRow, rCol: rCol});
				} else {
					wgt.fireToolbarAction(action, {tRow: -1, lCol: -1, bRow: -1, rCol: -1});
				}
			}
		}, wgt);
	}
	
zss.ButtonBuilder = zk.$extends(zk.Object, {
	/**
	 * @param zss.Spreadsheet
	 */
	$init: function (wgt) {
		this._wgt = wgt;
		this.contents = [];
	},
	/**
	 * @param String
	 */
	add: function (element) {
		this.contents.push(element);
		return this;
	},
	/**
	 * @param String array
	 */
	addAll: function (elements) {
		var cts = this.contents;
		for (var i = 0, len = elements.length; i < len; i++) {
			cts.push(elements[i]);
		}
		return this;
	},
	/**
	 * Returns a newly-created buttons
	 */
	build: function () {
		var btns = [],
			cts = this.contents;
		while (fn = cts.shift()) {
			btns.push(this[fn]());
		}
		return btns;
	},
	/**
	 * @return zss.ToolbarbuttonSeparator
	 */
	separator: function () {
		return new zss.ToolbarbuttonSeparator({
			$action: 'separator'
		});
	},
	/**
	 * Create new book button
	 * @return zss.Toolbarbutton
	 */
	newBook: function () {
		return newActionToolbarbutton(this._wgt, 'newBook', '/web/zss/img/document-medium.png');
	},
	/**
	 * Create save book button 
	 * @return zss.Toolbarbutton
	 */
	saveBook: function () {
		return newActionToolbarbutton(this._wgt, 'saveBook', '/web/zss/img/disk-black.png');
	},
	/**
	 * Create export PDF button
	 * @return zss.Toolbarbutton
	 */
	exportPDF: function () {
		return newActionToolbarbutton(this._wgt, 'exportPDF', '/web/zss/img/document-pdf.png');
	},
	paste: function () {
		var wgt = this._wgt,
			b = newActionToolbarbutton(wgt, 'paste', '/web/zss/img/clipboard-paste.png');
		b.setPopup(new zss.MenupopupFactory(wgt).paste());
		return b;
	},
	cut: function () {
		return newActionToolbarbutton(this._wgt, 'cut', '/web/zss/img/scissors-blue.png');
	},
	copy: function () {
		return newActionToolbarbutton(this._wgt, 'copy', '/web/zss/img/blue-document-copy.png');
	},
	fontFamily: function () {
		return new zss.FontFamilyCombobox({
			$action: 'fontFamily',
			width: '105px'
		}, this._wgt);
	},
	fontSize: function () {
		return new zss.FontSizeCombobox({
			$action: 'fontSize',
			width: '45px'
		}, this._wgt);
	},
	fontBold: function () {
		var wgt = this._wgt,
			b = newActionToolbarbutton(wgt, 'fontBold', '/web/zss/img/edit-bold.png'),
			fn = function (evt) {
				var sheet = wgt.sheetCtrl;
				if (sheet) {
					var d = evt.data,
						c = sheet.getCell(d.top, d.left);
					if (c) {
						b.setSelectedEffect(c.isFontBold());
					}	
				}
			};
		
		b.listen({onBind: function () {
			var sheet = wgt.sheetCtrl;
			if (sheet) {
				sheet.listen({onCellSelection: fn});
			}
		}});
		b.listen({onClick: function () {
			if (!b.isDisabled()) {
				b.setSelectedEffect(true);
			}
		}});
		
		return b;
	},
	fontItalic: function () {
		var wgt = this._wgt,
			b = newActionToolbarbutton(this._wgt, 'fontItalic', '/web/zss/img/edit-italic.png'),
			fn = function (evt) {
				var sheet = wgt.sheetCtrl;
				if (sheet) {
					var d = evt.data,
						c = sheet.getCell(d.top, d.left);
					if (c) {
						b.setSelectedEffect(c.isFontItalic());
					}	
				}
			};
		
		b.listen({onBind: function () {
			var sheet = wgt.sheetCtrl;
			if (sheet) {
				sheet.listen({onCellSelection: fn});
			}
		}});
		b.listen({onClick: function () {
			if (!b.isDisabled()) {
				b.setSelectedEffect(true);
			}
		}});
		
		return b;
	},
	fontUnderline: function () {
		var wgt = this._wgt,
			b = newActionToolbarbutton(this._wgt, 'fontUnderline', '/web/zss/img/edit-underline.png'),
			fn = function (evt) {
				var sheet = wgt.sheetCtrl;
				if (sheet) {
					var d = evt.data,
						c = sheet.getCell(d.top, d.left);
					if (c) {
						b.setSelectedEffect(c.isFontUnderline());
					}
				}
			};
		
		b.listen({onBind: function () {
			var sheet = wgt.sheetCtrl;
			if (sheet) {
				sheet.listen({onCellSelection: fn});
			}
		}});
		b.listen({onClick: function () {
			if (!b.isDisabled()) {
				b.setSelectedEffect(true);
			}
		}});
		return b;
	},
	fontStrike: function () {
		var wgt = this._wgt,
			b = newActionToolbarbutton(this._wgt, 'fontStrike', '/web/zss/img/edit-strike.png'),
			fn = function (evt) {
				var sheet = wgt.sheetCtrl;
				if (sheet) {
					var d = evt.data,
						c = sheet.getCell(d.top, d.left);
					if (c) {
						b.setSelectedEffect(c.isFontStrikeout());
					}
				}
			};
		
		b.listen({onBind: function () {
			var sheet = wgt.sheetCtrl;
			if (sheet) {
				sheet.listen({onCellSelection: fn});
			}
		}});
		b.listen({onClick: function () {
			if (!b.isDisabled()) {
				b.setSelectedEffect(true);
			}
		}});
		return b;
	},
	border: function () {
		var wgt = this._wgt,
			pp = new zss.MenupopupFactory(wgt).border(),
			b = new zss.Toolbarbutton({
				$action: 'border',
				tooltiptext: wgt._labelsCtrl.getBorder(),
				image: zk.ajaxURI('/web/zss/img/border-bottom.png', AU),
				onClick: function () {
					var sht = wgt.sheetCtrl;
					if (sht) {
						var s = sht.getLastSelection(),
							color = pp.colorMenu ? pp.colorMenu.getColor() : '' ;
						wgt.fireToolbarAction('border', {color: color, tRow: s.top, lCol: s.left, bRow: s.bottom, rCol: s.right});	
					}
				}
			}, wgt);
		b.setPopup(pp);
		return b;
	},
	fillColor: function () {
		var wgt = this._wgt;
		return new zss.Colorbutton({
			$action: 'fillColor',
			color: '#FFFFFF',
			tooltiptext: wgt._labelsCtrl.getFillColor(),
			image: zk.ajaxURI('/web/zss/img/paint-can-color.png', AU),
			onClick: function () {
				var sht = wgt.sheetCtrl;
				if (sht) {
					var s = sht.getLastSelection();
					wgt.fireToolbarAction('fillColor', {color: this.getColor(), tRow: s.top, lCol: s.left, bRow: s.bottom, rCol: s.right});	
				}
			}
		}, wgt);
	},
	fontColor: function () {
		var wgt = this._wgt;
		return new zss.Colorbutton({
			$action: 'fontColor',
			color: '#000000',
			tooltiptext: wgt._labelsCtrl.getFontColor(),
			image: zk.ajaxURI('/web/zss/img/edit-color.png', AU),
			onClick: function () {
				var sht = wgt.sheetCtrl;
				if (sht) {
					var s = sht.getLastSelection();
					wgt.fireToolbarAction('fontColor', {color: this.getColor(), tRow: s.top, lCol: s.left, bRow: s.bottom, rCol: s.right});
				}
			}
		}, wgt);
	},
	verticalAlign: function () {
		var wgt = this._wgt,
			b = newActionToolbarbutton(wgt, 'verticalAlign', '/web/zss/img/edit-vertical-alignment-top.png'),
			p = new zss.MenupopupFactory(wgt).verticalAlign(),
			item = p.firstChild,
			fn = function (evt) {
				var sheet = wgt.sheetCtrl;
				if (sheet) {
					var d = evt.data,
						c = sheet.getCell(d.top, d.left);
					if (c) {
						var a = c.getVerticalAlign(),
							item = p.firstChild;
						for (; item; item = item.nextSibling) {
							if (item.get$action() == a) {
								b.setSelectedEffect(true, item);
								break;
							}
						}
					}	
				}
			};
		
		b.listen({onBind: function () {
			var sheet = wgt.sheetCtrl;
			if (sheet) {
				sheet.listen({onCellSelection: fn});
			}
		}});
		for (;item; item = item.nextSibling) {
			item.listen({onClick: function () {
				if (!b.isDisabled()) {
					b.setSelectedEffect(true, item);
				}
			}});
		}
		
		b.setClickDisabled(true);
		b.setPopup(p);
		return b;
	},
	horizontalAlign: function () {
		var wgt = this._wgt,
			b = newActionToolbarbutton(wgt, 'horizontalAlign', '/web/zss/img/edit-alignment.png'),
			p = new zss.MenupopupFactory(wgt).horizontalAlign(),
			item = p.firstChild,
			fn = function (evt) {
				var sheet = wgt.sheetCtrl;
				if (sheet) {
					var d = evt.data,
						c = sheet.getCell(d.top, d.left);
					if (c) {
						var a = c.getHorizontalAlign(),
							item = p.firstChild;
						for (; item; item = item.nextSibling) {
							if (item.get$action() == a) {
								b.setSelectedEffect(true, item);
								break;
							}
						}
					}	
				}
			};
		
		b.listen({onBind: function () {
			var sheet = wgt.sheetCtrl;
			if (sheet) {
				sheet.listen({onCellSelection: fn});
			}
		}});
		for (;item; item = item.nextSibling) {
			item.listen({onClick: function () {
				if (!b.isDisabled()) {
					b.setSelectedEffect(true, item);
				}
			}});
		}
		
		b.setClickDisabled(true);
		b.setPopup(p);
		return b;
	},
	wrapText: function () {
		var wgt = this._wgt,
			b = newActionToolbarbutton(wgt, 'wrapText', '/web/zss/img/edit-wrap.png'),
			fn = function (evt) {
				var sheet = wgt.sheetCtrl;
				if (sheet) {
					var d = evt.data,
						c = sheet.getCell(d.top, d.left);
					if (c) {
						b.setSelectedEffect(c.wrap);
					}	
				}
			};
		b.listen({onBind: function () {
			var sheet = wgt.sheetCtrl;
			if (sheet) {
				sheet.listen({onCellSelection: fn});
			}
		}});
		return b;
	},
	mergeAndCenter: function () {
		var wgt = this._wgt,
			b = newActionToolbarbutton(wgt, 'mergeAndCenter', null, true);
		b.setPopup(new zss.MenupopupFactory(wgt).mergeAndCenter());
		return b;
	},
	insert: function () {
		var wgt = this._wgt,
			b = newActionToolbarbutton(wgt, 'insert', '/web/zss/img/document-insert.png');
		b.setPopup(new zss.MenupopupFactory(wgt).insert());
		b.setClickDisabled(true);
		return b;
	},
	del: function () {
		var wgt = this._wgt,
			b = newActionToolbarbutton(wgt, 'del', '/web/zss/img/document-hf-delete-footer.png');
		b.setPopup(new zss.MenupopupFactory(wgt).del());
		b.setClickDisabled(true);
		return b;
	},
	format: function () {
		//TODO: format
	},
	insertPicture: function () {
		var wgt = this._wgt;
		return new zss.UploadToolbarbutton({
			$action: 'insertPicture',
			tooltiptext: wgt._labelsCtrl.getInsertPicture(),
			image: zk.ajaxURI('/web/zss/img/image.png', AU)
		}, wgt);
	},
	columnChart: function () {
		var wgt = this._wgt,
			b = newActionToolbarbutton(wgt, 'columnChart', null, true);
		b.setClickDisabled(true);
		b.setPopup(new zss.MenupopupFactory(wgt).columnChart());
		return b;
	},
	lineChart: function () {
		var wgt = this._wgt,
			b = newActionToolbarbutton(wgt, 'lineChart', null, true);
		b.setClickDisabled(true);
		b.setPopup(new zss.MenupopupFactory(wgt).lineChart());
		return b;
	},
	pieChart: function () {
		var wgt = this._wgt,
			b = newActionToolbarbutton(wgt, 'pieChart', null, true);
		b.setClickDisabled(true);
		b.setPopup(new zss.MenupopupFactory(wgt).pieChart());
		return b;
	},
	barChart: function () {
		var wgt = this._wgt,
			b = newActionToolbarbutton(wgt, 'barChart', null, true);
		b.setClickDisabled(true);
		b.setPopup(new zss.MenupopupFactory(wgt).barChart());
		return b;
	},
	areaChart: function () {
		var b = newActionToolbarbutton(this._wgt, 'areaChart', null, true);
		return b;
	},
	scatterChart: function () {
		return newActionToolbarbutton(this._wgt, 'scatterChart', null, true);
	},
	otherChart: function () {
		var wgt = this._wgt,
			b = newActionToolbarbutton(wgt, 'otherChart', null, true);
		b.setClickDisabled(true);
		b.setPopup(new zss.MenupopupFactory(wgt).otherChart());
		return b;
	},
	hyperlink: function () {
		return newActionToolbarbutton(this._wgt, 'hyperlink', '/web/zss/img/hyperlink.png');
	},
	clear: function () {
		var wgt = this._wgt,
			b = newActionToolbarbutton(wgt, 'clear', '/web/zss/img/broom.png');
		b.setClickDisabled(true);
		b.setPopup(new zss.MenupopupFactory(wgt).clear());
		return b;
	},
	sortAndFilter: function () {
		var wgt = this._wgt,
			b = newActionToolbarbutton(wgt, 'sortAndFilter', '/web/zss/img/sort-filter.png');
		b.setPopup(new zss.MenupopupFactory(wgt).sortAndFilter());
		b.setClickDisabled(true);
		return b;
	},
	autoSum: function () {
		//TODO:
//		var wgt = this._wgt,
//			b = new zss.Toolbarbutton({
//				$action: 'autoSum',
//				tooltiptext: wgt._labelsCtrl.getAutoSum(),
//				image: zk.ajaxURI('/web/zss/img/sum.png', AU)
//			}, wgt);
//		b.setPopup(new zss.MenupopupFactory(wgt).autoSum());
//		return b;
	},
	insertFunction: function () {
		return newActionToolbarbutton(this._wgt, 'insertFunction', null, true);
	},
	financial: function () {
		return newActionToolbarbutton(this._wgt, 'financial', null, true);
	},
	logical: function () {
		return newActionToolbarbutton(this._wgt, 'logical', null, true);
	},
	text: function () {
		return newActionToolbarbutton(this._wgt, 'text', null, true);
	},
	dateAndTime: function () {
		return newActionToolbarbutton(this._wgt, 'dateAndTime', null, true);
	},
	lookupAndReference: function () {
		return newActionToolbarbutton(this._wgt, 'lookupAndReference', null, true);
	},
	mathAndTrig: function () {
		return newActionToolbarbutton(this._wgt, 'mathAndTrig', null, true);
	},
	moreFunction: function () {
		return newActionToolbarbutton(this._wgt, 'moreFunction', null, true);
	},
	protectSheet: function () {
		var wgt = this._wgt,
			b = new zss.ProtectSheetCheckbutton({
				checked: wgt.isProtect(),
				tooltiptext: wgt._labelsCtrl.getProtectSheet(),
				image: zk.ajaxURI('/web/zss/img/lock.png', AU),
				onClick: function () {
					wgt.fireToolbarAction('protectSheet');
				}
			}, wgt);
		return b;
	},
	gridlines: function () {
		var wgt = this._wgt,
			b = new zss.DisplayGridlinesCheckbutton({
			checked: wgt.isDisplayGridlines(),
			tooltiptext: wgt._labelsCtrl.getGridlines(),
			image: zk.ajaxURI('/web/zss/img/grid.png', AU),
			onClick: function () {
				wgt.fireToolbarAction('gridlines');
			}
		}, wgt);
		return b;
	}
})
})();