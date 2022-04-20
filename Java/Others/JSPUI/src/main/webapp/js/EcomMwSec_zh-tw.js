var ctbcpkidTag = document.createElement('object');
ctbcpkidTag.id="CTBCPKIAPI";
ctbcpkidTag.type="application/x-ctbccryptoapi";
ctbcpkidTag.width="0";
ctbcpkidTag.height="0";
document.getElementsByTagName("head")[0].appendChild(ctbcpkidTag);

//

/*!
 * jQuery blockUI plugin
 * Version 2.66.0-2013.10.09
 * Requires jQuery v1.7 or later
 *
 * Examples at: http://malsup.com/jquery/block/
 * Copyright (c) 2007-2013 M. Alsup
 * Dual licensed under the MIT and GPL licenses:
 * http://www.opensource.org/licenses/mit-license.php
 * http://www.gnu.org/licenses/gpl.html
 *
 * Thanks to Amir-Hossein Sobhi for some excellent contributions!
 */

;(function() {
/*jshint eqeqeq:false curly:false latedef:false */
"use strict";

	function setup($) {
		$.fn._fadeIn = $.fn.fadeIn;

		var noOp = $.noop || function() {};

		// this bit is to ensure we don't call setExpression when we shouldn't (with extra muscle to handle
		// confusing userAgent strings on Vista)
		var msie = /MSIE/.test(navigator.userAgent);
		var ie6  = /MSIE 6.0/.test(navigator.userAgent) && ! /MSIE 8.0/.test(navigator.userAgent);
		var mode = document.documentMode || 0;
		var setExpr = $.isFunction( document.createElement('div').style.setExpression );

		// global $ methods for blocking/unblocking the entire page
		$.blockUI   = function(opts) { install(window, opts); };
		$.unblockUI = function(opts) { remove(window, opts); };

		// convenience method for quick growl-like notifications  (http://www.google.com/search?q=growl)
		$.growlUI = function(title, message, timeout, onClose) {
			var $m = $('<div class="growlUI"></div>');
			if (title) $m.append('<h1>'+title+'</h1>');
			if (message) $m.append('<h2>'+message+'</h2>');
			if (timeout === undefined) timeout = 3000;

			// Added by konapun: Set timeout to 30 seconds if this growl is moused over, like normal toast notifications
			var callBlock = function(opts) {
				opts = opts || {};

				$.blockUI({
					message: $m,
					fadeIn : typeof opts.fadeIn  !== 'undefined' ? opts.fadeIn  : 700,
					fadeOut: typeof opts.fadeOut !== 'undefined' ? opts.fadeOut : 1000,
					timeout: typeof opts.timeout !== 'undefined' ? opts.timeout : timeout,
					centerY: false,
					showOverlay: false,
					onUnblock: onClose,
					css: $.blockUI.defaults.growlCSS
				});
			};

			callBlock();
			var nonmousedOpacity = $m.css('opacity');
			$m.mouseover(function() {
				callBlock({
					fadeIn: 0,
					timeout: 30000
				});

				var displayBlock = $('.blockMsg');
				displayBlock.stop(); // cancel fadeout if it has started
				displayBlock.fadeTo(300, 1); // make it easier to read the message by removing transparency
			}).mouseout(function() {
				$('.blockMsg').fadeOut(1000);
			});
			// End konapun additions
		};

		// plugin method for blocking element content
		$.fn.block = function(opts) {
			if ( this[0] === window ) {
				$.blockUI( opts );
				return this;
			}
			var fullOpts = $.extend({}, $.blockUI.defaults, opts || {});
			this.each(function() {
				var $el = $(this);
				if (fullOpts.ignoreIfBlocked && $el.data('blockUI.isBlocked'))
					return;
				$el.unblock({ fadeOut: 0 });
			});

			return this.each(function() {
				if ($.css(this,'position') == 'static') {
					this.style.position = 'relative';
					$(this).data('blockUI.static', true);
				}
				this.style.zoom = 1; // force 'hasLayout' in ie
				install(this, opts);
			});
		};

		// plugin method for unblocking element content
		$.fn.unblock = function(opts) {
			if ( this[0] === window ) {
				$.unblockUI( opts );
				return this;
			}
			return this.each(function() {
				remove(this, opts);
			});
		};

		$.blockUI.version = 2.66; // 2nd generation blocking at no extra cost!

		// override these in your code to change the default behavior and style
		$.blockUI.defaults = {
			// message displayed when blocking (use null for no message)
			//20190103 Larry add gif
			message:  '<img src=".\\gif\\load.gif" /><font size="6">請稍候...</font>',//'<font size="5">請稍候...</font>',

			title: null,		// title string; only used when theme == true
			draggable: true,	// only used when theme == true (requires jquery-ui.js to be loaded)

			theme: false, // set to true to use with jQuery UI themes

			// styles for the message when blocking; if you wish to disable
			// these and use an external stylesheet then do this in your code:
			// $.blockUI.defaults.css = {};border:		'3px solid #aaa',
			css: {
				padding:	0,
				margin:		0,
				width:		'25%',
				top:		'40%',
				left:		'35%',
				textAlign:	'center',
				color:		'#000',
				backgroundColor:'transparent',
				cursor:		'wait'
			},

			// minimal style set used when themes are used
			themedCSS: {
				width:	'30%',
				top:	'40%',
				left:	'35%'
			},

			// styles for the overlay
			overlayCSS:  {
				backgroundColor:	'#ffffff',
				opacity:			0.1,
				cursor:				'wait'
			},

			// style to replace wait cursor before unblocking to correct issue
			// of lingering wait cursor
			cursorReset: 'default',

			// styles applied when using $.growlUI
			growlCSS: {
				width:		'350px',
				top:		'10px',
				left:		'',
				right:		'10px',
				border:		'none',
				padding:	'5px',
				opacity:	0.6,
				cursor:		'default',
				color:		'#fff',
				backgroundColor: '#000',
				'-webkit-border-radius':'10px',
				'-moz-border-radius':	'10px',
				'border-radius':		'10px'
			},

			// IE issues: 'about:blank' fails on HTTPS and javascript:false is s-l-o-w
			// (hat tip to Jorge H. N. de Vasconcelos)
			/*jshint scripturl:true */
			iframeSrc: /^https/i.test(window.location.href || '') ? 'javascript:false' : 'about:blank',

			// force usage of iframe in non-IE browsers (handy for blocking applets)
			forceIframe: false,

			// z-index for the blocking overlay
			baseZ: 1000,

			// set these to true to have the message automatically centered
			centerX: true, // <-- only effects element blocking (page block controlled via css above)
			centerY: true,

			// allow body element to be stetched in ie6; this makes blocking look better
			// on "short" pages.  disable if you wish to prevent changes to the body height
			allowBodyStretch: true,

			// enable if you want key and mouse events to be disabled for content that is blocked
			bindEvents: true,

			// be default blockUI will supress tab navigation from leaving blocking content
			// (if bindEvents is true)
			constrainTabKey: true,

			// fadeIn time in millis; set to 0 to disable fadeIn on block
			fadeIn:  200,

			// fadeOut time in millis; set to 0 to disable fadeOut on unblock
			fadeOut:  400,

			// time in millis to wait before auto-unblocking; set to 0 to disable auto-unblock
			timeout: 0,

			// disable if you don't want to show the overlay
			showOverlay: true,

			// if true, focus will be placed in the first available input field when
			// page blocking
			focusInput: true,

            // elements that can receive focus
            focusableElements: ':input:enabled:visible',

			// suppresses the use of overlay styles on FF/Linux (due to performance issues with opacity)
			// no longer needed in 2012
			// applyPlatformOpacityRules: true,

			// callback method invoked when fadeIn has completed and blocking message is visible
			onBlock: null,

			// callback method invoked when unblocking has completed; the callback is
			// passed the element that has been unblocked (which is the window object for page
			// blocks) and the options that were passed to the unblock call:
			//	onUnblock(element, options)
			onUnblock: null,

			// callback method invoked when the overlay area is clicked.
			// setting this will turn the cursor to a pointer, otherwise cursor defined in overlayCss will be used.
			onOverlayClick: null,

			// don't ask; if you really must know: http://groups.google.com/group/jquery-en/browse_thread/thread/36640a8730503595/2f6a79a77a78e493#2f6a79a77a78e493
			quirksmodeOffsetHack: 4,

			// class name of the message block
			blockMsgClass: 'blockMsg',

			// if it is already blocked, then ignore it (don't unblock and reblock)
			ignoreIfBlocked: false
		};

		// private data and functions follow...

		var pageBlock = null;
		var pageBlockEls = [];

		function install(el, opts) {
			var css, themedCSS;
			var full = (el == window);
			var msg = (opts && opts.message !== undefined ? opts.message : undefined);
			opts = $.extend({}, $.blockUI.defaults, opts || {});

			if (opts.ignoreIfBlocked && $(el).data('blockUI.isBlocked'))
				return;

			opts.overlayCSS = $.extend({}, $.blockUI.defaults.overlayCSS, opts.overlayCSS || {});
			css = $.extend({}, $.blockUI.defaults.css, opts.css || {});
			if (opts.onOverlayClick)
				opts.overlayCSS.cursor = 'pointer';

			themedCSS = $.extend({}, $.blockUI.defaults.themedCSS, opts.themedCSS || {});
			msg = msg === undefined ? opts.message : msg;

			// remove the current block (if there is one)
			if (full && pageBlock)
				remove(window, {fadeOut:0});

			// if an existing element is being used as the blocking content then we capture
			// its current place in the DOM (and current display style) so we can restore
			// it when we unblock
			if (msg && typeof msg != 'string' && (msg.parentNode || msg.jquery)) {
				var node = msg.jquery ? msg[0] : msg;
				var data = {};
				$(el).data('blockUI.history', data);
				data.el = node;
				data.parent = node.parentNode;
				data.display = node.style.display;
				data.position = node.style.position;
				if (data.parent)
					data.parent.removeChild(node);
			}

			$(el).data('blockUI.onUnblock', opts.onUnblock);
			var z = opts.baseZ;

			// blockUI uses 3 layers for blocking, for simplicity they are all used on every platform;
			// layer1 is the iframe layer which is used to supress bleed through of underlying content
			// layer2 is the overlay layer which has opacity and a wait cursor (by default)
			// layer3 is the message content that is displayed while blocking
			var lyr1, lyr2, lyr3, s;
			if (msie || opts.forceIframe)
				lyr1 = $('<iframe class="blockUI" style="z-index:'+ (z++) +';display:none;border:none;margin:0;padding:0;position:absolute;width:100%;height:100%;top:0;left:0" src="'+opts.iframeSrc+'"></iframe>');
			else
				lyr1 = $('<div class="blockUI" style="display:none"></div>');

			if (opts.theme)
				lyr2 = $('<div class="blockUI blockOverlay ui-widget-overlay" style="z-index:'+ (z++) +';display:none"></div>');
			else
				lyr2 = $('<div class="blockUI blockOverlay" style="z-index:'+ (z++) +';display:none;border:none;margin:0;padding:0;width:100%;height:100%;top:0;left:0"></div>');

			if (opts.theme && full) {
				s = '<div class="blockUI ' + opts.blockMsgClass + ' blockPage ui-dialog ui-widget ui-corner-all" style="z-index:'+(z+10)+';display:none;position:fixed">';
				if ( opts.title ) {
					s += '<div class="ui-widget-header ui-dialog-titlebar ui-corner-all blockTitle">'+(opts.title || '&nbsp;')+'</div>';
				}
				s += '<div class="ui-widget-content ui-dialog-content"></div>';
				s += '</div>';
			}
			else if (opts.theme) {
				s = '<div class="blockUI ' + opts.blockMsgClass + ' blockElement ui-dialog ui-widget ui-corner-all" style="z-index:'+(z+10)+';display:none;position:absolute">';
				if ( opts.title ) {
					s += '<div class="ui-widget-header ui-dialog-titlebar ui-corner-all blockTitle">'+(opts.title || '&nbsp;')+'</div>';
				}
				s += '<div class="ui-widget-content ui-dialog-content"></div>';
				s += '</div>';
			}
			else if (full) {
				s = '<div class="blockUI ' + opts.blockMsgClass + ' blockPage" style="z-index:'+(z+10)+';display:none;position:fixed"></div>';
			}
			else {
				s = '<div class="blockUI ' + opts.blockMsgClass + ' blockElement" style="z-index:'+(z+10)+';display:none;position:absolute"></div>';
			}
			lyr3 = $(s);

			// if we have a message, style it
			if (msg) {
				if (opts.theme) {
					lyr3.css(themedCSS);
					lyr3.addClass('ui-widget-content');
				}
				else
					lyr3.css(css);
			}

			// style the overlay
			if (!opts.theme /*&& (!opts.applyPlatformOpacityRules)*/)
				lyr2.css(opts.overlayCSS);
			lyr2.css('position', full ? 'fixed' : 'absolute');

			// make iframe layer transparent in IE
			if (msie || opts.forceIframe)
				lyr1.css('opacity',0.0);

			//$([lyr1[0],lyr2[0],lyr3[0]]).appendTo(full ? 'body' : el);
			var layers = [lyr1,lyr2,lyr3], $par = full ? $('body') : $(el);
			$.each(layers, function() {
				this.appendTo($par);
			});

			if (opts.theme && opts.draggable && $.fn.draggable) {
				lyr3.draggable({
					handle: '.ui-dialog-titlebar',
					cancel: 'li'
				});
			}

			// ie7 must use absolute positioning in quirks mode and to account for activex issues (when scrolling)
			var expr = setExpr && (!$.support.boxModel || $('object,embed', full ? null : el).length > 0);
			if (ie6 || expr) {
				// give body 100% height
				if (full && opts.allowBodyStretch && $.support.boxModel)
					$('html,body').css('height','100%');

				// fix ie6 issue when blocked element has a border width
				if ((ie6 || !$.support.boxModel) && !full) {
					var t = sz(el,'borderTopWidth'), l = sz(el,'borderLeftWidth');
					var fixT = t ? '(0 - '+t+')' : 0;
					var fixL = l ? '(0 - '+l+')' : 0;
				}

				// simulate fixed position
				$.each(layers, function(i,o) {
					var s = o[0].style;
					s.position = 'absolute';
					if (i < 2) {
						if (full)
							s.setExpression('height','Math.max(document.body.scrollHeight, document.body.offsetHeight) - (jQuery.support.boxModel?0:'+opts.quirksmodeOffsetHack+') + "px"');
						else
							s.setExpression('height','this.parentNode.offsetHeight + "px"');
						if (full)
							s.setExpression('width','jQuery.support.boxModel && document.documentElement.clientWidth || document.body.clientWidth + "px"');
						else
							s.setExpression('width','this.parentNode.offsetWidth + "px"');
						if (fixL) s.setExpression('left', fixL);
						if (fixT) s.setExpression('top', fixT);
					}
					else if (opts.centerY) {
						if (full) s.setExpression('top','(document.documentElement.clientHeight || document.body.clientHeight) / 2 - (this.offsetHeight / 2) + (blah = document.documentElement.scrollTop ? document.documentElement.scrollTop : document.body.scrollTop) + "px"');
						s.marginTop = 0;
					}
					else if (!opts.centerY && full) {
						var top = (opts.css && opts.css.top) ? parseInt(opts.css.top, 10) : 0;
						var expression = '((document.documentElement.scrollTop ? document.documentElement.scrollTop : document.body.scrollTop) + '+top+') + "px"';
						s.setExpression('top',expression);
					}
				});
			}

			// show the message
			if (msg) {
				if (opts.theme)
					lyr3.find('.ui-widget-content').append(msg);
				else
					lyr3.append(msg);
				if (msg.jquery || msg.nodeType)
					$(msg).show();
			}

			if ((msie || opts.forceIframe) && opts.showOverlay)
				lyr1.show(); // opacity is zero
			if (opts.fadeIn) {
				var cb = opts.onBlock ? opts.onBlock : noOp;
				var cb1 = (opts.showOverlay && !msg) ? cb : noOp;
				var cb2 = msg ? cb : noOp;
				if (opts.showOverlay)
					lyr2._fadeIn(opts.fadeIn, cb1);
				if (msg)
					lyr3._fadeIn(opts.fadeIn, cb2);
			}
			else {
				if (opts.showOverlay)
					lyr2.show();
				if (msg)
					lyr3.show();
				if (opts.onBlock)
					opts.onBlock();
			}

			// bind key and mouse events
			bind(1, el, opts);

			if (full) {
				pageBlock = lyr3[0];
				pageBlockEls = $(opts.focusableElements,pageBlock);
				if (opts.focusInput)
					setTimeout(focus, 20);
			}
			else
				center(lyr3[0], opts.centerX, opts.centerY);

			if (opts.timeout) {
				// auto-unblock
				var to = setTimeout(function() {
					if (full)
						$.unblockUI(opts);
					else
						$(el).unblock(opts);
				}, opts.timeout);
				$(el).data('blockUI.timeout', to);
			}
		}

		// remove the block
		function remove(el, opts) {
			var count;
			var full = (el == window);
			var $el = $(el);
			var data = $el.data('blockUI.history');
			var to = $el.data('blockUI.timeout');
			if (to) {
				clearTimeout(to);
				$el.removeData('blockUI.timeout');
			}
			opts = $.extend({}, $.blockUI.defaults, opts || {});
			bind(0, el, opts); // unbind events

			if (opts.onUnblock === null) {
				opts.onUnblock = $el.data('blockUI.onUnblock');
				$el.removeData('blockUI.onUnblock');
			}

			var els;
			if (full) // crazy selector to handle odd field errors in ie6/7
				els = $('body').children().filter('.blockUI').add('body > .blockUI');
			else
				els = $el.find('>.blockUI');

			// fix cursor issue
			if ( opts.cursorReset ) {
				if ( els.length > 1 )
					els[1].style.cursor = opts.cursorReset;
				if ( els.length > 2 )
					els[2].style.cursor = opts.cursorReset;
			}

			if (full)
				pageBlock = pageBlockEls = null;

			if (opts.fadeOut) {
				count = els.length;
				els.stop().fadeOut(opts.fadeOut, function() {
					if ( --count === 0)
						reset(els,data,opts,el);
				});
			}
			else
				reset(els, data, opts, el);
		}

		// move blocking element back into the DOM where it started
		function reset(els,data,opts,el) {
			var $el = $(el);
			if ( $el.data('blockUI.isBlocked') )
				return;

			els.each(function(i,o) {
				// remove via DOM calls so we don't lose event handlers
				if (this.parentNode)
					this.parentNode.removeChild(this);
			});

			if (data && data.el) {
				data.el.style.display = data.display;
				data.el.style.position = data.position;
				if (data.parent)
					data.parent.appendChild(data.el);
				$el.removeData('blockUI.history');
			}

			if ($el.data('blockUI.static')) {
				$el.css('position', 'static'); // #22
			}

			if (typeof opts.onUnblock == 'function')
				opts.onUnblock(el,opts);

			// fix issue in Safari 6 where block artifacts remain until reflow
			var body = $(document.body), w = body.width(), cssW = body[0].style.width;
			body.width(w-1).width(w);
			body[0].style.width = cssW;
		}

		// bind/unbind the handler
		function bind(b, el, opts) {
			var full = el == window, $el = $(el);

			// don't bother unbinding if there is nothing to unbind
			if (!b && (full && !pageBlock || !full && !$el.data('blockUI.isBlocked')))
				return;

			$el.data('blockUI.isBlocked', b);

			// don't bind events when overlay is not in use or if bindEvents is false
			if (!full || !opts.bindEvents || (b && !opts.showOverlay))
				return;

			// bind anchors and inputs for mouse and key events
			var events = 'mousedown mouseup keydown keypress keyup touchstart touchend touchmove';
			if (b)
				$(document).bind(events, opts, handler);
			else
				$(document).unbind(events, handler);

		// former impl...
		//		var $e = $('a,:input');
		//		b ? $e.bind(events, opts, handler) : $e.unbind(events, handler);
		}

		// event handler to suppress keyboard/mouse events when blocking
		function handler(e) {
			// allow tab navigation (conditionally)
			if (e.type === 'keydown' && e.keyCode && e.keyCode == 9) {
				if (pageBlock && e.data.constrainTabKey) {
					var els = pageBlockEls;
					var fwd = !e.shiftKey && e.target === els[els.length-1];
					var back = e.shiftKey && e.target === els[0];
					if (fwd || back) {
						setTimeout(function(){focus(back);},10);
						return false;
					}
				}
			}
			var opts = e.data;
			var target = $(e.target);
			if (target.hasClass('blockOverlay') && opts.onOverlayClick)
				opts.onOverlayClick(e);

			// allow events within the message content
			if (target.parents('div.' + opts.blockMsgClass).length > 0)
				return true;

			// allow events for content that is not being blocked
			return target.parents().children().filter('div.blockUI').length === 0;
		}

		function focus(back) {
			if (!pageBlockEls)
				return;
			var e = pageBlockEls[back===true ? pageBlockEls.length-1 : 0];
			if (e)
				e.focus();
		}

		function center(el, x, y) {
			var p = el.parentNode, s = el.style;
			var l = ((p.offsetWidth - el.offsetWidth)/2) - sz(p,'borderLeftWidth');
			var t = ((p.offsetHeight - el.offsetHeight)/2) - sz(p,'borderTopWidth');
			if (x) s.left = l > 0 ? (l+'px') : '0';
			if (y) s.top  = t > 0 ? (t+'px') : '0';
		}

		function sz(el, p) {
			return parseInt($.css(el,p),10)||0;
		}

	}


	/*global define:true */
	if (typeof define === 'function' && define.amd && define.amd.jQuery) {
		define(['jquery'], setup);
	} else {
		setup(jQuery);
	}

})();

//

var JSEncryptExports = {};
(function(exports) {
// Copyright (c) 2005  Tom Wu
// All Rights Reserved.
// See "LICENSE" for details.

// Basic JavaScript BN library - subset useful for RSA encryption.

// Bits per digit
var dbits;

// JavaScript engine analysis
var canary = 0xdeadbeefcafe;
var j_lm = ((canary&0xffffff)==0xefcafe);

// (public) Constructor
function BigInteger(a,b,c) {
  if(a != null)
    if("number" == typeof a) this.fromNumber(a,b,c);
    else if(b == null && "string" != typeof a) this.fromString(a,256);
    else this.fromString(a,b);
}

// return new, unset BigInteger
function nbi() { return new BigInteger(null); }

// am: Compute w_j += (x*this_i), propagate carries,
// c is initial carry, returns final carry.
// c < 3*dvalue, x < 2*dvalue, this_i < dvalue
// We need to select the fastest one that works in this environment.

// am1: use a single mult and divide to get the high bits,
// max digit bits should be 26 because
// max internal value = 2*dvalue^2-2*dvalue (< 2^53)
function am1(i,x,w,j,c,n) {
  while(--n >= 0) {
    var v = x*this[i++]+w[j]+c;
    c = Math.floor(v/0x4000000);
    w[j++] = v&0x3ffffff;
  }
  return c;
}
// am2 avoids a big mult-and-extract completely.
// Max digit bits should be <= 30 because we do bitwise ops
// on values up to 2*hdvalue^2-hdvalue-1 (< 2^31)
function am2(i,x,w,j,c,n) {
  var xl = x&0x7fff, xh = x>>15;
  while(--n >= 0) {
    var l = this[i]&0x7fff;
    var h = this[i++]>>15;
    var m = xh*l+h*xl;
    l = xl*l+((m&0x7fff)<<15)+w[j]+(c&0x3fffffff);
    c = (l>>>30)+(m>>>15)+xh*h+(c>>>30);
    w[j++] = l&0x3fffffff;
  }
  return c;
}
// Alternately, set max digit bits to 28 since some
// browsers slow down when dealing with 32-bit numbers.
function am3(i,x,w,j,c,n) {
  var xl = x&0x3fff, xh = x>>14;
  while(--n >= 0) {
    var l = this[i]&0x3fff;
    var h = this[i++]>>14;
    var m = xh*l+h*xl;
    l = xl*l+((m&0x3fff)<<14)+w[j]+c;
    c = (l>>28)+(m>>14)+xh*h;
    w[j++] = l&0xfffffff;
  }
  return c;
}
if(j_lm && (navigator.appName == "Microsoft Internet Explorer")) {
  BigInteger.prototype.am = am2;
  dbits = 30;
}
else if(j_lm && (navigator.appName != "Netscape")) {
  BigInteger.prototype.am = am1;
  dbits = 26;
}
else { // Mozilla/Netscape seems to prefer am3
  BigInteger.prototype.am = am3;
  dbits = 28;
}

BigInteger.prototype.DB = dbits;
BigInteger.prototype.DM = ((1<<dbits)-1);
BigInteger.prototype.DV = (1<<dbits);

var BI_FP = 52;
BigInteger.prototype.FV = Math.pow(2,BI_FP);
BigInteger.prototype.F1 = BI_FP-dbits;
BigInteger.prototype.F2 = 2*dbits-BI_FP;

// Digit conversions
var BI_RM = "0123456789abcdefghijklmnopqrstuvwxyz";
var BI_RC = new Array();
var rr,vv;
rr = "0".charCodeAt(0);
for(vv = 0; vv <= 9; ++vv) BI_RC[rr++] = vv;
rr = "a".charCodeAt(0);
for(vv = 10; vv < 36; ++vv) BI_RC[rr++] = vv;
rr = "A".charCodeAt(0);
for(vv = 10; vv < 36; ++vv) BI_RC[rr++] = vv;

function int2char(n) { return BI_RM.charAt(n); }
function intAt(s,i) {
  var c = BI_RC[s.charCodeAt(i)];
  return (c==null)?-1:c;
}

// (protected) copy this to r
function bnpCopyTo(r) {
  for(var i = this.t-1; i >= 0; --i) r[i] = this[i];
  r.t = this.t;
  r.s = this.s;
}

// (protected) set from integer value x, -DV <= x < DV
function bnpFromInt(x) {
  this.t = 1;
  this.s = (x<0)?-1:0;
  if(x > 0) this[0] = x;
  else if(x < -1) this[0] = x+this.DV;
  else this.t = 0;
}

// return bigint initialized to value
function nbv(i) { var r = nbi(); r.fromInt(i); return r; }

// (protected) set from string and radix
function bnpFromString(s,b) {
  var k;
  if(b == 16) k = 4;
  else if(b == 8) k = 3;
  else if(b == 256) k = 8; // byte array
  else if(b == 2) k = 1;
  else if(b == 32) k = 5;
  else if(b == 4) k = 2;
  else { this.fromRadix(s,b); return; }
  this.t = 0;
  this.s = 0;
  var i = s.length, mi = false, sh = 0;
  while(--i >= 0) {
    var x = (k==8)?s[i]&0xff:intAt(s,i);
    if(x < 0) {
      if(s.charAt(i) == "-") mi = true;
      continue;
    }
    mi = false;
    if(sh == 0)
      this[this.t++] = x;
    else if(sh+k > this.DB) {
      this[this.t-1] |= (x&((1<<(this.DB-sh))-1))<<sh;
      this[this.t++] = (x>>(this.DB-sh));
    }
    else
      this[this.t-1] |= x<<sh;
    sh += k;
    if(sh >= this.DB) sh -= this.DB;
  }
  if(k == 8 && (s[0]&0x80) != 0) {
    this.s = -1;
    if(sh > 0) this[this.t-1] |= ((1<<(this.DB-sh))-1)<<sh;
  }
  this.clamp();
  if(mi) BigInteger.ZERO.subTo(this,this);
}

// (protected) clamp off excess high words
function bnpClamp() {
  var c = this.s&this.DM;
  while(this.t > 0 && this[this.t-1] == c) --this.t;
}

// (public) return string representation in given radix
function bnToString(b) {
  if(this.s < 0) return "-"+this.negate().toString(b);
  var k;
  if(b == 16) k = 4;
  else if(b == 8) k = 3;
  else if(b == 2) k = 1;
  else if(b == 32) k = 5;
  else if(b == 4) k = 2;
  else return this.toRadix(b);
  var km = (1<<k)-1, d, m = false, r = "", i = this.t;
  var p = this.DB-(i*this.DB)%k;
  if(i-- > 0) {
    if(p < this.DB && (d = this[i]>>p) > 0) { m = true; r = int2char(d); }
    while(i >= 0) {
      if(p < k) {
        d = (this[i]&((1<<p)-1))<<(k-p);
        d |= this[--i]>>(p+=this.DB-k);
      }
      else {
        d = (this[i]>>(p-=k))&km;
        if(p <= 0) { p += this.DB; --i; }
      }
      if(d > 0) m = true;
      if(m) r += int2char(d);
    }
  }
  return m?r:"0";
}

// (public) -this
function bnNegate() { var r = nbi(); BigInteger.ZERO.subTo(this,r); return r; }

// (public) |this|
function bnAbs() { return (this.s<0)?this.negate():this; }

// (public) return + if this > a, - if this < a, 0 if equal
function bnCompareTo(a) {
  var r = this.s-a.s;
  if(r != 0) return r;
  var i = this.t;
  r = i-a.t;
  if(r != 0) return (this.s<0)?-r:r;
  while(--i >= 0) if((r=this[i]-a[i]) != 0) return r;
  return 0;
}

// returns bit length of the integer x
function nbits(x) {
  var r = 1, t;
  if((t=x>>>16) != 0) { x = t; r += 16; }
  if((t=x>>8) != 0) { x = t; r += 8; }
  if((t=x>>4) != 0) { x = t; r += 4; }
  if((t=x>>2) != 0) { x = t; r += 2; }
  if((t=x>>1) != 0) { x = t; r += 1; }
  return r;
}

// (public) return the number of bits in "this"
function bnBitLength() {
  if(this.t <= 0) return 0;
  return this.DB*(this.t-1)+nbits(this[this.t-1]^(this.s&this.DM));
}

// (protected) r = this << n*DB
function bnpDLShiftTo(n,r) {
  var i;
  for(i = this.t-1; i >= 0; --i) r[i+n] = this[i];
  for(i = n-1; i >= 0; --i) r[i] = 0;
  r.t = this.t+n;
  r.s = this.s;
}

// (protected) r = this >> n*DB
function bnpDRShiftTo(n,r) {
  for(var i = n; i < this.t; ++i) r[i-n] = this[i];
  r.t = Math.max(this.t-n,0);
  r.s = this.s;
}

// (protected) r = this << n
function bnpLShiftTo(n,r) {
  var bs = n%this.DB;
  var cbs = this.DB-bs;
  var bm = (1<<cbs)-1;
  var ds = Math.floor(n/this.DB), c = (this.s<<bs)&this.DM, i;
  for(i = this.t-1; i >= 0; --i) {
    r[i+ds+1] = (this[i]>>cbs)|c;
    c = (this[i]&bm)<<bs;
  }
  for(i = ds-1; i >= 0; --i) r[i] = 0;
  r[ds] = c;
  r.t = this.t+ds+1;
  r.s = this.s;
  r.clamp();
}

// (protected) r = this >> n
function bnpRShiftTo(n,r) {
  r.s = this.s;
  var ds = Math.floor(n/this.DB);
  if(ds >= this.t) { r.t = 0; return; }
  var bs = n%this.DB;
  var cbs = this.DB-bs;
  var bm = (1<<bs)-1;
  r[0] = this[ds]>>bs;
  for(var i = ds+1; i < this.t; ++i) {
    r[i-ds-1] |= (this[i]&bm)<<cbs;
    r[i-ds] = this[i]>>bs;
  }
  if(bs > 0) r[this.t-ds-1] |= (this.s&bm)<<cbs;
  r.t = this.t-ds;
  r.clamp();
}

// (protected) r = this - a
function bnpSubTo(a,r) {
  var i = 0, c = 0, m = Math.min(a.t,this.t);
  while(i < m) {
    c += this[i]-a[i];
    r[i++] = c&this.DM;
    c >>= this.DB;
  }
  if(a.t < this.t) {
    c -= a.s;
    while(i < this.t) {
      c += this[i];
      r[i++] = c&this.DM;
      c >>= this.DB;
    }
    c += this.s;
  }
  else {
    c += this.s;
    while(i < a.t) {
      c -= a[i];
      r[i++] = c&this.DM;
      c >>= this.DB;
    }
    c -= a.s;
  }
  r.s = (c<0)?-1:0;
  if(c < -1) r[i++] = this.DV+c;
  else if(c > 0) r[i++] = c;
  r.t = i;
  r.clamp();
}

// (protected) r = this * a, r != this,a (HAC 14.12)
// "this" should be the larger one if appropriate.
function bnpMultiplyTo(a,r) {
  var x = this.abs(), y = a.abs();
  var i = x.t;
  r.t = i+y.t;
  while(--i >= 0) r[i] = 0;
  for(i = 0; i < y.t; ++i) r[i+x.t] = x.am(0,y[i],r,i,0,x.t);
  r.s = 0;
  r.clamp();
  if(this.s != a.s) BigInteger.ZERO.subTo(r,r);
}

// (protected) r = this^2, r != this (HAC 14.16)
function bnpSquareTo(r) {
  var x = this.abs();
  var i = r.t = 2*x.t;
  while(--i >= 0) r[i] = 0;
  for(i = 0; i < x.t-1; ++i) {
    var c = x.am(i,x[i],r,2*i,0,1);
    if((r[i+x.t]+=x.am(i+1,2*x[i],r,2*i+1,c,x.t-i-1)) >= x.DV) {
      r[i+x.t] -= x.DV;
      r[i+x.t+1] = 1;
    }
  }
  if(r.t > 0) r[r.t-1] += x.am(i,x[i],r,2*i,0,1);
  r.s = 0;
  r.clamp();
}

// (protected) divide this by m, quotient and remainder to q, r (HAC 14.20)
// r != q, this != m.  q or r may be null.
function bnpDivRemTo(m,q,r) {
  var pm = m.abs();
  if(pm.t <= 0) return;
  var pt = this.abs();
  if(pt.t < pm.t) {
    if(q != null) q.fromInt(0);
    if(r != null) this.copyTo(r);
    return;
  }
  if(r == null) r = nbi();
  var y = nbi(), ts = this.s, ms = m.s;
  var nsh = this.DB-nbits(pm[pm.t-1]);	// normalize modulus
  if(nsh > 0) { pm.lShiftTo(nsh,y); pt.lShiftTo(nsh,r); }
  else { pm.copyTo(y); pt.copyTo(r); }
  var ys = y.t;
  var y0 = y[ys-1];
  if(y0 == 0) return;
  var yt = y0*(1<<this.F1)+((ys>1)?y[ys-2]>>this.F2:0);
  var d1 = this.FV/yt, d2 = (1<<this.F1)/yt, e = 1<<this.F2;
  var i = r.t, j = i-ys, t = (q==null)?nbi():q;
  y.dlShiftTo(j,t);
  if(r.compareTo(t) >= 0) {
    r[r.t++] = 1;
    r.subTo(t,r);
  }
  BigInteger.ONE.dlShiftTo(ys,t);
  t.subTo(y,y);	// "negative" y so we can replace sub with am later
  while(y.t < ys) y[y.t++] = 0;
  while(--j >= 0) {
    // Estimate quotient digit
    var qd = (r[--i]==y0)?this.DM:Math.floor(r[i]*d1+(r[i-1]+e)*d2);
    if((r[i]+=y.am(0,qd,r,j,0,ys)) < qd) {	// Try it out
      y.dlShiftTo(j,t);
      r.subTo(t,r);
      while(r[i] < --qd) r.subTo(t,r);
    }
  }
  if(q != null) {
    r.drShiftTo(ys,q);
    if(ts != ms) BigInteger.ZERO.subTo(q,q);
  }
  r.t = ys;
  r.clamp();
  if(nsh > 0) r.rShiftTo(nsh,r);	// Denormalize remainder
  if(ts < 0) BigInteger.ZERO.subTo(r,r);
}

// (public) this mod a
function bnMod(a) {
  var r = nbi();
  this.abs().divRemTo(a,null,r);
  if(this.s < 0 && r.compareTo(BigInteger.ZERO) > 0) a.subTo(r,r);
  return r;
}

// Modular reduction using "classic" algorithm
function Classic(m) { this.m = m; }
function cConvert(x) {
  if(x.s < 0 || x.compareTo(this.m) >= 0) return x.mod(this.m);
  else return x;
}
function cRevert(x) { return x; }
function cReduce(x) { x.divRemTo(this.m,null,x); }
function cMulTo(x,y,r) { x.multiplyTo(y,r); this.reduce(r); }
function cSqrTo(x,r) { x.squareTo(r); this.reduce(r); }

Classic.prototype.convert = cConvert;
Classic.prototype.revert = cRevert;
Classic.prototype.reduce = cReduce;
Classic.prototype.mulTo = cMulTo;
Classic.prototype.sqrTo = cSqrTo;

// (protected) return "-1/this % 2^DB"; useful for Mont. reduction
// justification:
//         xy == 1 (mod m)
//         xy =  1+km
//   xy(2-xy) = (1+km)(1-km)
// x[y(2-xy)] = 1-k^2m^2
// x[y(2-xy)] == 1 (mod m^2)
// if y is 1/x mod m, then y(2-xy) is 1/x mod m^2
// should reduce x and y(2-xy) by m^2 at each step to keep size bounded.
// JS multiply "overflows" differently from C/C++, so care is needed here.
function bnpInvDigit() {
  if(this.t < 1) return 0;
  var x = this[0];
  if((x&1) == 0) return 0;
  var y = x&3;		// y == 1/x mod 2^2
  y = (y*(2-(x&0xf)*y))&0xf;	// y == 1/x mod 2^4
  y = (y*(2-(x&0xff)*y))&0xff;	// y == 1/x mod 2^8
  y = (y*(2-(((x&0xffff)*y)&0xffff)))&0xffff;	// y == 1/x mod 2^16
  // last step - calculate inverse mod DV directly;
  // assumes 16 < DB <= 32 and assumes ability to handle 48-bit ints
  y = (y*(2-x*y%this.DV))%this.DV;		// y == 1/x mod 2^dbits
  // we really want the negative inverse, and -DV < y < DV
  return (y>0)?this.DV-y:-y;
}

// Montgomery reduction
function Montgomery(m) {
  this.m = m;
  this.mp = m.invDigit();
  this.mpl = this.mp&0x7fff;
  this.mph = this.mp>>15;
  this.um = (1<<(m.DB-15))-1;
  this.mt2 = 2*m.t;
}

// xR mod m
function montConvert(x) {
  var r = nbi();
  x.abs().dlShiftTo(this.m.t,r);
  r.divRemTo(this.m,null,r);
  if(x.s < 0 && r.compareTo(BigInteger.ZERO) > 0) this.m.subTo(r,r);
  return r;
}

// x/R mod m
function montRevert(x) {
  var r = nbi();
  x.copyTo(r);
  this.reduce(r);
  return r;
}

// x = x/R mod m (HAC 14.32)
function montReduce(x) {
  while(x.t <= this.mt2)	// pad x so am has enough room later
    x[x.t++] = 0;
  for(var i = 0; i < this.m.t; ++i) {
    // faster way of calculating u0 = x[i]*mp mod DV
    var j = x[i]&0x7fff;
    var u0 = (j*this.mpl+(((j*this.mph+(x[i]>>15)*this.mpl)&this.um)<<15))&x.DM;
    // use am to combine the multiply-shift-add into one call
    j = i+this.m.t;
    x[j] += this.m.am(0,u0,x,i,0,this.m.t);
    // propagate carry
    while(x[j] >= x.DV) { x[j] -= x.DV; x[++j]++; }
  }
  x.clamp();
  x.drShiftTo(this.m.t,x);
  if(x.compareTo(this.m) >= 0) x.subTo(this.m,x);
}

// r = "x^2/R mod m"; x != r
function montSqrTo(x,r) { x.squareTo(r); this.reduce(r); }

// r = "xy/R mod m"; x,y != r
function montMulTo(x,y,r) { x.multiplyTo(y,r); this.reduce(r); }

Montgomery.prototype.convert = montConvert;
Montgomery.prototype.revert = montRevert;
Montgomery.prototype.reduce = montReduce;
Montgomery.prototype.mulTo = montMulTo;
Montgomery.prototype.sqrTo = montSqrTo;

// (protected) true iff this is even
function bnpIsEven() { return ((this.t>0)?(this[0]&1):this.s) == 0; }

// (protected) this^e, e < 2^32, doing sqr and mul with "r" (HAC 14.79)
function bnpExp(e,z) {
  if(e > 0xffffffff || e < 1) return BigInteger.ONE;
  var r = nbi(), r2 = nbi(), g = z.convert(this), i = nbits(e)-1;
  g.copyTo(r);
  while(--i >= 0) {
    z.sqrTo(r,r2);
    if((e&(1<<i)) > 0) z.mulTo(r2,g,r);
    else { var t = r; r = r2; r2 = t; }
  }
  return z.revert(r);
}

// (public) this^e % m, 0 <= e < 2^32
function bnModPowInt(e,m) {
  var z;
  if(e < 256 || m.isEven()) z = new Classic(m); else z = new Montgomery(m);
  return this.exp(e,z);
}

// protected
BigInteger.prototype.copyTo = bnpCopyTo;
BigInteger.prototype.fromInt = bnpFromInt;
BigInteger.prototype.fromString = bnpFromString;
BigInteger.prototype.clamp = bnpClamp;
BigInteger.prototype.dlShiftTo = bnpDLShiftTo;
BigInteger.prototype.drShiftTo = bnpDRShiftTo;
BigInteger.prototype.lShiftTo = bnpLShiftTo;
BigInteger.prototype.rShiftTo = bnpRShiftTo;
BigInteger.prototype.subTo = bnpSubTo;
BigInteger.prototype.multiplyTo = bnpMultiplyTo;
BigInteger.prototype.squareTo = bnpSquareTo;
BigInteger.prototype.divRemTo = bnpDivRemTo;
BigInteger.prototype.invDigit = bnpInvDigit;
BigInteger.prototype.isEven = bnpIsEven;
BigInteger.prototype.exp = bnpExp;

// public
BigInteger.prototype.toString = bnToString;
BigInteger.prototype.negate = bnNegate;
BigInteger.prototype.abs = bnAbs;
BigInteger.prototype.compareTo = bnCompareTo;
BigInteger.prototype.bitLength = bnBitLength;
BigInteger.prototype.mod = bnMod;
BigInteger.prototype.modPowInt = bnModPowInt;

// "constants"
BigInteger.ZERO = nbv(0);
BigInteger.ONE = nbv(1);
// Copyright (c) 2005-2009  Tom Wu
// All Rights Reserved.
// See "LICENSE" for details.

// Extended JavaScript BN functions, required for RSA private ops.

// Version 1.1: new BigInteger("0", 10) returns "proper" zero
// Version 1.2: square() API, isProbablePrime fix

// (public)
function bnClone() { var r = nbi(); this.copyTo(r); return r; }

// (public) return value as integer
function bnIntValue() {
  if(this.s < 0) {
    if(this.t == 1) return this[0]-this.DV;
    else if(this.t == 0) return -1;
  }
  else if(this.t == 1) return this[0];
  else if(this.t == 0) return 0;
  // assumes 16 < DB < 32
  return ((this[1]&((1<<(32-this.DB))-1))<<this.DB)|this[0];
}

// (public) return value as byte
function bnByteValue() { return (this.t==0)?this.s:(this[0]<<24)>>24; }

// (public) return value as short (assumes DB>=16)
function bnShortValue() { return (this.t==0)?this.s:(this[0]<<16)>>16; }

// (protected) return x s.t. r^x < DV
function bnpChunkSize(r) { return Math.floor(Math.LN2*this.DB/Math.log(r)); }

// (public) 0 if this == 0, 1 if this > 0
function bnSigNum() {
  if(this.s < 0) return -1;
  else if(this.t <= 0 || (this.t == 1 && this[0] <= 0)) return 0;
  else return 1;
}

// (protected) convert to radix string
function bnpToRadix(b) {
  if(b == null) b = 10;
  if(this.signum() == 0 || b < 2 || b > 36) return "0";
  var cs = this.chunkSize(b);
  var a = Math.pow(b,cs);
  var d = nbv(a), y = nbi(), z = nbi(), r = "";
  this.divRemTo(d,y,z);
  while(y.signum() > 0) {
    r = (a+z.intValue()).toString(b).substr(1) + r;
    y.divRemTo(d,y,z);
  }
  return z.intValue().toString(b) + r;
}

// (protected) convert from radix string
function bnpFromRadix(s,b) {
  this.fromInt(0);
  if(b == null) b = 10;
  var cs = this.chunkSize(b);
  var d = Math.pow(b,cs), mi = false, j = 0, w = 0;
  for(var i = 0; i < s.length; ++i) {
    var x = intAt(s,i);
    if(x < 0) {
      if(s.charAt(i) == "-" && this.signum() == 0) mi = true;
      continue;
    }
    w = b*w+x;
    if(++j >= cs) {
      this.dMultiply(d);
      this.dAddOffset(w,0);
      j = 0;
      w = 0;
    }
  }
  if(j > 0) {
    this.dMultiply(Math.pow(b,j));
    this.dAddOffset(w,0);
  }
  if(mi) BigInteger.ZERO.subTo(this,this);
}

// (protected) alternate constructor
function bnpFromNumber(a,b,c) {
  if("number" == typeof b) {
    // new BigInteger(int,int,RNG)
    if(a < 2) this.fromInt(1);
    else {
      this.fromNumber(a,c);
      if(!this.testBit(a-1))	// force MSB set
        this.bitwiseTo(BigInteger.ONE.shiftLeft(a-1),op_or,this);
      if(this.isEven()) this.dAddOffset(1,0); // force odd
      while(!this.isProbablePrime(b)) {
        this.dAddOffset(2,0);
        if(this.bitLength() > a) this.subTo(BigInteger.ONE.shiftLeft(a-1),this);
      }
    }
  }
  else {
    // new BigInteger(int,RNG)
    var x = new Array(), t = a&7;
    x.length = (a>>3)+1;
    b.nextBytes(x);
    if(t > 0) x[0] &= ((1<<t)-1); else x[0] = 0;
    this.fromString(x,256);
  }
}

// (public) convert to bigendian byte array
function bnToByteArray() {
  var i = this.t, r = new Array();
  r[0] = this.s;
  var p = this.DB-(i*this.DB)%8, d, k = 0;
  if(i-- > 0) {
    if(p < this.DB && (d = this[i]>>p) != (this.s&this.DM)>>p)
      r[k++] = d|(this.s<<(this.DB-p));
    while(i >= 0) {
      if(p < 8) {
        d = (this[i]&((1<<p)-1))<<(8-p);
        d |= this[--i]>>(p+=this.DB-8);
      }
      else {
        d = (this[i]>>(p-=8))&0xff;
        if(p <= 0) { p += this.DB; --i; }
      }
      if((d&0x80) != 0) d |= -256;
      if(k == 0 && (this.s&0x80) != (d&0x80)) ++k;
      if(k > 0 || d != this.s) r[k++] = d;
    }
  }
  return r;
}

function bnEquals(a) { return(this.compareTo(a)==0); }
function bnMin(a) { return(this.compareTo(a)<0)?this:a; }
function bnMax(a) { return(this.compareTo(a)>0)?this:a; }

// (protected) r = this op a (bitwise)
function bnpBitwiseTo(a,op,r) {
  var i, f, m = Math.min(a.t,this.t);
  for(i = 0; i < m; ++i) r[i] = op(this[i],a[i]);
  if(a.t < this.t) {
    f = a.s&this.DM;
    for(i = m; i < this.t; ++i) r[i] = op(this[i],f);
    r.t = this.t;
  }
  else {
    f = this.s&this.DM;
    for(i = m; i < a.t; ++i) r[i] = op(f,a[i]);
    r.t = a.t;
  }
  r.s = op(this.s,a.s);
  r.clamp();
}

// (public) this & a
function op_and(x,y) { return x&y; }
function bnAnd(a) { var r = nbi(); this.bitwiseTo(a,op_and,r); return r; }

// (public) this | a
function op_or(x,y) { return x|y; }
function bnOr(a) { var r = nbi(); this.bitwiseTo(a,op_or,r); return r; }

// (public) this ^ a
function op_xor(x,y) { return x^y; }
function bnXor(a) { var r = nbi(); this.bitwiseTo(a,op_xor,r); return r; }

// (public) this & ~a
function op_andnot(x,y) { return x&~y; }
function bnAndNot(a) { var r = nbi(); this.bitwiseTo(a,op_andnot,r); return r; }

// (public) ~this
function bnNot() {
  var r = nbi();
  for(var i = 0; i < this.t; ++i) r[i] = this.DM&~this[i];
  r.t = this.t;
  r.s = ~this.s;
  return r;
}

// (public) this << n
function bnShiftLeft(n) {
  var r = nbi();
  if(n < 0) this.rShiftTo(-n,r); else this.lShiftTo(n,r);
  return r;
}

// (public) this >> n
function bnShiftRight(n) {
  var r = nbi();
  if(n < 0) this.lShiftTo(-n,r); else this.rShiftTo(n,r);
  return r;
}

// return index of lowest 1-bit in x, x < 2^31
function lbit(x) {
  if(x == 0) return -1;
  var r = 0;
  if((x&0xffff) == 0) { x >>= 16; r += 16; }
  if((x&0xff) == 0) { x >>= 8; r += 8; }
  if((x&0xf) == 0) { x >>= 4; r += 4; }
  if((x&3) == 0) { x >>= 2; r += 2; }
  if((x&1) == 0) ++r;
  return r;
}

// (public) returns index of lowest 1-bit (or -1 if none)
function bnGetLowestSetBit() {
  for(var i = 0; i < this.t; ++i)
    if(this[i] != 0) return i*this.DB+lbit(this[i]);
  if(this.s < 0) return this.t*this.DB;
  return -1;
}

// return number of 1 bits in x
function cbit(x) {
  var r = 0;
  while(x != 0) { x &= x-1; ++r; }
  return r;
}

// (public) return number of set bits
function bnBitCount() {
  var r = 0, x = this.s&this.DM;
  for(var i = 0; i < this.t; ++i) r += cbit(this[i]^x);
  return r;
}

// (public) true iff nth bit is set
function bnTestBit(n) {
  var j = Math.floor(n/this.DB);
  if(j >= this.t) return(this.s!=0);
  return((this[j]&(1<<(n%this.DB)))!=0);
}

// (protected) this op (1<<n)
function bnpChangeBit(n,op) {
  var r = BigInteger.ONE.shiftLeft(n);
  this.bitwiseTo(r,op,r);
  return r;
}

// (public) this | (1<<n)
function bnSetBit(n) { return this.changeBit(n,op_or); }

// (public) this & ~(1<<n)
function bnClearBit(n) { return this.changeBit(n,op_andnot); }

// (public) this ^ (1<<n)
function bnFlipBit(n) { return this.changeBit(n,op_xor); }

// (protected) r = this + a
function bnpAddTo(a,r) {
  var i = 0, c = 0, m = Math.min(a.t,this.t);
  while(i < m) {
    c += this[i]+a[i];
    r[i++] = c&this.DM;
    c >>= this.DB;
  }
  if(a.t < this.t) {
    c += a.s;
    while(i < this.t) {
      c += this[i];
      r[i++] = c&this.DM;
      c >>= this.DB;
    }
    c += this.s;
  }
  else {
    c += this.s;
    while(i < a.t) {
      c += a[i];
      r[i++] = c&this.DM;
      c >>= this.DB;
    }
    c += a.s;
  }
  r.s = (c<0)?-1:0;
  if(c > 0) r[i++] = c;
  else if(c < -1) r[i++] = this.DV+c;
  r.t = i;
  r.clamp();
}

// (public) this + a
function bnAdd(a) { var r = nbi(); this.addTo(a,r); return r; }

// (public) this - a
function bnSubtract(a) { var r = nbi(); this.subTo(a,r); return r; }

// (public) this * a
function bnMultiply(a) { var r = nbi(); this.multiplyTo(a,r); return r; }

// (public) this^2
function bnSquare() { var r = nbi(); this.squareTo(r); return r; }

// (public) this / a
function bnDivide(a) { var r = nbi(); this.divRemTo(a,r,null); return r; }

// (public) this % a
function bnRemainder(a) { var r = nbi(); this.divRemTo(a,null,r); return r; }

// (public) [this/a,this%a]
function bnDivideAndRemainder(a) {
  var q = nbi(), r = nbi();
  this.divRemTo(a,q,r);
  return new Array(q,r);
}

// (protected) this *= n, this >= 0, 1 < n < DV
function bnpDMultiply(n) {
  this[this.t] = this.am(0,n-1,this,0,0,this.t);
  ++this.t;
  this.clamp();
}

// (protected) this += n << w words, this >= 0
function bnpDAddOffset(n,w) {
  if(n == 0) return;
  while(this.t <= w) this[this.t++] = 0;
  this[w] += n;
  while(this[w] >= this.DV) {
    this[w] -= this.DV;
    if(++w >= this.t) this[this.t++] = 0;
    ++this[w];
  }
}

// A "null" reducer
function NullExp() {}
function nNop(x) { return x; }
function nMulTo(x,y,r) { x.multiplyTo(y,r); }
function nSqrTo(x,r) { x.squareTo(r); }

NullExp.prototype.convert = nNop;
NullExp.prototype.revert = nNop;
NullExp.prototype.mulTo = nMulTo;
NullExp.prototype.sqrTo = nSqrTo;

// (public) this^e
function bnPow(e) { return this.exp(e,new NullExp()); }

// (protected) r = lower n words of "this * a", a.t <= n
// "this" should be the larger one if appropriate.
function bnpMultiplyLowerTo(a,n,r) {
  var i = Math.min(this.t+a.t,n);
  r.s = 0; // assumes a,this >= 0
  r.t = i;
  while(i > 0) r[--i] = 0;
  var j;
  for(j = r.t-this.t; i < j; ++i) r[i+this.t] = this.am(0,a[i],r,i,0,this.t);
  for(j = Math.min(a.t,n); i < j; ++i) this.am(0,a[i],r,i,0,n-i);
  r.clamp();
}

// (protected) r = "this * a" without lower n words, n > 0
// "this" should be the larger one if appropriate.
function bnpMultiplyUpperTo(a,n,r) {
  --n;
  var i = r.t = this.t+a.t-n;
  r.s = 0; // assumes a,this >= 0
  while(--i >= 0) r[i] = 0;
  for(i = Math.max(n-this.t,0); i < a.t; ++i)
    r[this.t+i-n] = this.am(n-i,a[i],r,0,0,this.t+i-n);
  r.clamp();
  r.drShiftTo(1,r);
}

// Barrett modular reduction
function Barrett(m) {
  // setup Barrett
  this.r2 = nbi();
  this.q3 = nbi();
  BigInteger.ONE.dlShiftTo(2*m.t,this.r2);
  this.mu = this.r2.divide(m);
  this.m = m;
}

function barrettConvert(x) {
  if(x.s < 0 || x.t > 2*this.m.t) return x.mod(this.m);
  else if(x.compareTo(this.m) < 0) return x;
  else { var r = nbi(); x.copyTo(r); this.reduce(r); return r; }
}

function barrettRevert(x) { return x; }

// x = x mod m (HAC 14.42)
function barrettReduce(x) {
  x.drShiftTo(this.m.t-1,this.r2);
  if(x.t > this.m.t+1) { x.t = this.m.t+1; x.clamp(); }
  this.mu.multiplyUpperTo(this.r2,this.m.t+1,this.q3);
  this.m.multiplyLowerTo(this.q3,this.m.t+1,this.r2);
  while(x.compareTo(this.r2) < 0) x.dAddOffset(1,this.m.t+1);
  x.subTo(this.r2,x);
  while(x.compareTo(this.m) >= 0) x.subTo(this.m,x);
}

// r = x^2 mod m; x != r
function barrettSqrTo(x,r) { x.squareTo(r); this.reduce(r); }

// r = x*y mod m; x,y != r
function barrettMulTo(x,y,r) { x.multiplyTo(y,r); this.reduce(r); }

Barrett.prototype.convert = barrettConvert;
Barrett.prototype.revert = barrettRevert;
Barrett.prototype.reduce = barrettReduce;
Barrett.prototype.mulTo = barrettMulTo;
Barrett.prototype.sqrTo = barrettSqrTo;

// (public) this^e % m (HAC 14.85)
function bnModPow(e,m) {
  var i = e.bitLength(), k, r = nbv(1), z;
  if(i <= 0) return r;
  else if(i < 18) k = 1;
  else if(i < 48) k = 3;
  else if(i < 144) k = 4;
  else if(i < 768) k = 5;
  else k = 6;
  if(i < 8)
    z = new Classic(m);
  else if(m.isEven())
    z = new Barrett(m);
  else
    z = new Montgomery(m);

  // precomputation
  var g = new Array(), n = 3, k1 = k-1, km = (1<<k)-1;
  g[1] = z.convert(this);
  if(k > 1) {
    var g2 = nbi();
    z.sqrTo(g[1],g2);
    while(n <= km) {
      g[n] = nbi();
      z.mulTo(g2,g[n-2],g[n]);
      n += 2;
    }
  }

  var j = e.t-1, w, is1 = true, r2 = nbi(), t;
  i = nbits(e[j])-1;
  while(j >= 0) {
    if(i >= k1) w = (e[j]>>(i-k1))&km;
    else {
      w = (e[j]&((1<<(i+1))-1))<<(k1-i);
      if(j > 0) w |= e[j-1]>>(this.DB+i-k1);
    }

    n = k;
    while((w&1) == 0) { w >>= 1; --n; }
    if((i -= n) < 0) { i += this.DB; --j; }
    if(is1) {	// ret == 1, don't bother squaring or multiplying it
      g[w].copyTo(r);
      is1 = false;
    }
    else {
      while(n > 1) { z.sqrTo(r,r2); z.sqrTo(r2,r); n -= 2; }
      if(n > 0) z.sqrTo(r,r2); else { t = r; r = r2; r2 = t; }
      z.mulTo(r2,g[w],r);
    }

    while(j >= 0 && (e[j]&(1<<i)) == 0) {
      z.sqrTo(r,r2); t = r; r = r2; r2 = t;
      if(--i < 0) { i = this.DB-1; --j; }
    }
  }
  return z.revert(r);
}

// (public) gcd(this,a) (HAC 14.54)
function bnGCD(a) {
  var x = (this.s<0)?this.negate():this.clone();
  var y = (a.s<0)?a.negate():a.clone();
  if(x.compareTo(y) < 0) { var t = x; x = y; y = t; }
  var i = x.getLowestSetBit(), g = y.getLowestSetBit();
  if(g < 0) return x;
  if(i < g) g = i;
  if(g > 0) {
    x.rShiftTo(g,x);
    y.rShiftTo(g,y);
  }
  while(x.signum() > 0) {
    if((i = x.getLowestSetBit()) > 0) x.rShiftTo(i,x);
    if((i = y.getLowestSetBit()) > 0) y.rShiftTo(i,y);
    if(x.compareTo(y) >= 0) {
      x.subTo(y,x);
      x.rShiftTo(1,x);
    }
    else {
      y.subTo(x,y);
      y.rShiftTo(1,y);
    }
  }
  if(g > 0) y.lShiftTo(g,y);
  return y;
}

// (protected) this % n, n < 2^26
function bnpModInt(n) {
  if(n <= 0) return 0;
  var d = this.DV%n, r = (this.s<0)?n-1:0;
  if(this.t > 0)
    if(d == 0) r = this[0]%n;
    else for(var i = this.t-1; i >= 0; --i) r = (d*r+this[i])%n;
  return r;
}

// (public) 1/this % m (HAC 14.61)
function bnModInverse(m) {
  var ac = m.isEven();
  if((this.isEven() && ac) || m.signum() == 0) return BigInteger.ZERO;
  var u = m.clone(), v = this.clone();
  var a = nbv(1), b = nbv(0), c = nbv(0), d = nbv(1);
  while(u.signum() != 0) {
    while(u.isEven()) {
      u.rShiftTo(1,u);
      if(ac) {
        if(!a.isEven() || !b.isEven()) { a.addTo(this,a); b.subTo(m,b); }
        a.rShiftTo(1,a);
      }
      else if(!b.isEven()) b.subTo(m,b);
      b.rShiftTo(1,b);
    }
    while(v.isEven()) {
      v.rShiftTo(1,v);
      if(ac) {
        if(!c.isEven() || !d.isEven()) { c.addTo(this,c); d.subTo(m,d); }
        c.rShiftTo(1,c);
      }
      else if(!d.isEven()) d.subTo(m,d);
      d.rShiftTo(1,d);
    }
    if(u.compareTo(v) >= 0) {
      u.subTo(v,u);
      if(ac) a.subTo(c,a);
      b.subTo(d,b);
    }
    else {
      v.subTo(u,v);
      if(ac) c.subTo(a,c);
      d.subTo(b,d);
    }
  }
  if(v.compareTo(BigInteger.ONE) != 0) return BigInteger.ZERO;
  if(d.compareTo(m) >= 0) return d.subtract(m);
  if(d.signum() < 0) d.addTo(m,d); else return d;
  if(d.signum() < 0) return d.add(m); else return d;
}

var lowprimes = [2,3,5,7,11,13,17,19,23,29,31,37,41,43,47,53,59,61,67,71,73,79,83,89,97,101,103,107,109,113,127,131,137,139,149,151,157,163,167,173,179,181,191,193,197,199,211,223,227,229,233,239,241,251,257,263,269,271,277,281,283,293,307,311,313,317,331,337,347,349,353,359,367,373,379,383,389,397,401,409,419,421,431,433,439,443,449,457,461,463,467,479,487,491,499,503,509,521,523,541,547,557,563,569,571,577,587,593,599,601,607,613,617,619,631,641,643,647,653,659,661,673,677,683,691,701,709,719,727,733,739,743,751,757,761,769,773,787,797,809,811,821,823,827,829,839,853,857,859,863,877,881,883,887,907,911,919,929,937,941,947,953,967,971,977,983,991,997];
var lplim = (1<<26)/lowprimes[lowprimes.length-1];

// (public) test primality with certainty >= 1-.5^t
function bnIsProbablePrime(t) {
  var i, x = this.abs();
  if(x.t == 1 && x[0] <= lowprimes[lowprimes.length-1]) {
    for(i = 0; i < lowprimes.length; ++i)
      if(x[0] == lowprimes[i]) return true;
    return false;
  }
  if(x.isEven()) return false;
  i = 1;
  while(i < lowprimes.length) {
    var m = lowprimes[i], j = i+1;
    while(j < lowprimes.length && m < lplim) m *= lowprimes[j++];
    m = x.modInt(m);
    while(i < j) if(m%lowprimes[i++] == 0) return false;
  }
  return x.millerRabin(t);
}

// (protected) true if probably prime (HAC 4.24, Miller-Rabin)
function bnpMillerRabin(t) {
  var n1 = this.subtract(BigInteger.ONE);
  var k = n1.getLowestSetBit();
  if(k <= 0) return false;
  var r = n1.shiftRight(k);
  t = (t+1)>>1;
  if(t > lowprimes.length) t = lowprimes.length;
  var a = nbi();
  for(var i = 0; i < t; ++i) {
    //Pick bases at random, instead of starting at 2
	//Larry 20190712 Fix Math.random issue
	//a.fromInt(lowprimes[Math.floor(Math.random()*lowprimes.length)]);
	a.fromInt(lowprimes[Math.floor((new Date()).getTime()/10000000000000*lowprimes.length)]);
	
    var y = a.modPow(r,this);
    if(y.compareTo(BigInteger.ONE) != 0 && y.compareTo(n1) != 0) {
      var j = 1;
      while(j++ < k && y.compareTo(n1) != 0) {
        y = y.modPowInt(2,this);
        if(y.compareTo(BigInteger.ONE) == 0) return false;
      }
      if(y.compareTo(n1) != 0) return false;
    }
  }
  return true;
}

// protected
BigInteger.prototype.chunkSize = bnpChunkSize;
BigInteger.prototype.toRadix = bnpToRadix;
BigInteger.prototype.fromRadix = bnpFromRadix;
BigInteger.prototype.fromNumber = bnpFromNumber;
BigInteger.prototype.bitwiseTo = bnpBitwiseTo;
BigInteger.prototype.changeBit = bnpChangeBit;
BigInteger.prototype.addTo = bnpAddTo;
BigInteger.prototype.dMultiply = bnpDMultiply;
BigInteger.prototype.dAddOffset = bnpDAddOffset;
BigInteger.prototype.multiplyLowerTo = bnpMultiplyLowerTo;
BigInteger.prototype.multiplyUpperTo = bnpMultiplyUpperTo;
BigInteger.prototype.modInt = bnpModInt;
BigInteger.prototype.millerRabin = bnpMillerRabin;

// public
BigInteger.prototype.clone = bnClone;
BigInteger.prototype.intValue = bnIntValue;
BigInteger.prototype.byteValue = bnByteValue;
BigInteger.prototype.shortValue = bnShortValue;
BigInteger.prototype.signum = bnSigNum;
BigInteger.prototype.toByteArray = bnToByteArray;
BigInteger.prototype.equals = bnEquals;
BigInteger.prototype.min = bnMin;
BigInteger.prototype.max = bnMax;
BigInteger.prototype.and = bnAnd;
BigInteger.prototype.or = bnOr;
BigInteger.prototype.xor = bnXor;
BigInteger.prototype.andNot = bnAndNot;
BigInteger.prototype.not = bnNot;
BigInteger.prototype.shiftLeft = bnShiftLeft;
BigInteger.prototype.shiftRight = bnShiftRight;
BigInteger.prototype.getLowestSetBit = bnGetLowestSetBit;
BigInteger.prototype.bitCount = bnBitCount;
BigInteger.prototype.testBit = bnTestBit;
BigInteger.prototype.setBit = bnSetBit;
BigInteger.prototype.clearBit = bnClearBit;
BigInteger.prototype.flipBit = bnFlipBit;
BigInteger.prototype.add = bnAdd;
BigInteger.prototype.subtract = bnSubtract;
BigInteger.prototype.multiply = bnMultiply;
BigInteger.prototype.divide = bnDivide;
BigInteger.prototype.remainder = bnRemainder;
BigInteger.prototype.divideAndRemainder = bnDivideAndRemainder;
BigInteger.prototype.modPow = bnModPow;
BigInteger.prototype.modInverse = bnModInverse;
BigInteger.prototype.pow = bnPow;
BigInteger.prototype.gcd = bnGCD;
BigInteger.prototype.isProbablePrime = bnIsProbablePrime;

// JSBN-specific extension
BigInteger.prototype.square = bnSquare;

// BigInteger interfaces not implemented in jsbn:

// BigInteger(int signum, byte[] magnitude)
// double doubleValue()
// float floatValue()
// int hashCode()
// long longValue()
// static BigInteger valueOf(long val)
// prng4.js - uses Arcfour as a PRNG

function Arcfour() {
  this.i = 0;
  this.j = 0;
  this.S = new Array();
}

// Initialize arcfour context from key, an array of ints, each from [0..255]
function ARC4init(key) {
  var i, j, t;
  for(i = 0; i < 256; ++i)
    this.S[i] = i;
  j = 0;
  for(i = 0; i < 256; ++i) {
    j = (j + this.S[i] + key[i % key.length]) & 255;
    t = this.S[i];
    this.S[i] = this.S[j];
    this.S[j] = t;
  }
  this.i = 0;
  this.j = 0;
}

function ARC4next() {
  var t;
  this.i = (this.i + 1) & 255;
  this.j = (this.j + this.S[this.i]) & 255;
  t = this.S[this.i];
  this.S[this.i] = this.S[this.j];
  this.S[this.j] = t;
  return this.S[(t + this.S[this.i]) & 255];
}

Arcfour.prototype.init = ARC4init;
Arcfour.prototype.next = ARC4next;

// Plug in your RNG constructor here
function prng_newstate() {
  return new Arcfour();
}

// Pool size must be a multiple of 4 and greater than 32.
// An array of bytes the size of the pool will be passed to init()
var rng_psize = 256;
// Random number generator - requires a PRNG backend, e.g. prng4.js
var rng_state;
var rng_pool;
var rng_pptr;

// Initialize the pool with junk if needed.
if(rng_pool == null) {
  rng_pool = new Array();
  rng_pptr = 0;
  var t;
  if(window.crypto && window.crypto.getRandomValues) {
    // Extract entropy (2048 bits) from RNG if available
    var z = new Uint32Array(256);
    window.crypto.getRandomValues(z);
    for (t = 0; t < z.length; ++t)
      rng_pool[rng_pptr++] = z[t] & 255;
  } 
  
  // Use mouse events for entropy, if we do not have enough entropy by the time
  // we need it, entropy will be generated by Math.random.
  var onMouseMoveListener = function(ev) {
    this.count = this.count || 0;
    if (this.count >= 256 || rng_pptr >= rng_psize) {
      if (window.removeEventListener)
        window.removeEventListener("mousemove", onMouseMoveListener);
      else if (window.detachEvent)
        window.detachEvent("onmousemove", onMouseMoveListener);
      return;
    }
    this.count += 1;
    var mouseCoordinates = ev.x + ev.y;
    rng_pool[rng_pptr++] = mouseCoordinates & 255;
  };
  if (window.addEventListener)
    window.addEventListener("mousemove", onMouseMoveListener);
  else if (window.attachEvent)
    window.attachEvent("onmousemove", onMouseMoveListener);
  
}

function rng_get_byte() {
  if(rng_state == null) {
    rng_state = prng_newstate();
    // At this point, we may not have collected enough entropy.  If not, fall back to Math.random
    while (rng_pptr < rng_psize) {
      //Larry 20190712 Fix Math.random issue
      //var random = Math.floor(65536 * Math.random());
	  var random = Math.floor(65536 * (new Date()).getTime()/10000000000000);
      rng_pool[rng_pptr++] = random & 255;
    }
    rng_state.init(rng_pool);
    for(rng_pptr = 0; rng_pptr < rng_pool.length; ++rng_pptr)
      rng_pool[rng_pptr] = 0;
    rng_pptr = 0;
  }
  // TODO: allow reseeding after first request
  return rng_state.next();
}

function rng_get_bytes(ba) {
  var i;
  for(i = 0; i < ba.length; ++i) ba[i] = rng_get_byte();
}

function SecureRandom() {}

SecureRandom.prototype.nextBytes = rng_get_bytes;
// Depends on jsbn.js and rng.js

// Version 1.1: support utf-8 encoding in pkcs1pad2

// convert a (hex) string to a bignum object
function parseBigInt(str,r) {
  return new BigInteger(str,r);
}

function linebrk(s,n) {
  var ret = "";
  var i = 0;
  while(i + n < s.length) {
    ret += s.substring(i,i+n) + "\n";
    i += n;
  }
  return ret + s.substring(i,s.length);
}

function byte2Hex(b) {
  if(b < 0x10)
    return "0" + b.toString(16);
  else
    return b.toString(16);
}

// PKCS#1 (type 2, random) pad input string s to n bytes, and return a bigint
function pkcs1pad2(s,n) {
  if(n < s.length + 11) { // TODO: fix for utf-8
    console.error("Message too long for RSA");
    return null;
  }
  var ba = new Array();
  var i = s.length - 1;
  while(i >= 0 && n > 0) {
    var c = s.charCodeAt(i--);
    if(c < 128) { // encode using utf-8
      ba[--n] = c;
    }
    else if((c > 127) && (c < 2048)) {
      ba[--n] = (c & 63) | 128;
      ba[--n] = (c >> 6) | 192;
    }
    else {
      ba[--n] = (c & 63) | 128;
      ba[--n] = ((c >> 6) & 63) | 128;
      ba[--n] = (c >> 12) | 224;
    }
  }
  ba[--n] = 0;
  var rng = new SecureRandom();
  var x = new Array();
  while(n > 2) { // random non-zero pad
    x[0] = 0;
    while(x[0] == 0) rng.nextBytes(x);
    ba[--n] = x[0];
  }
  ba[--n] = 2;
  ba[--n] = 0;
  return new BigInteger(ba);
}

// "empty" RSA key constructor
function RSAKey() {
  this.n = null;
  this.e = 0;
  this.d = null;
  this.p = null;
  this.q = null;
  this.dmp1 = null;
  this.dmq1 = null;
  this.coeff = null;
}

// Set the public key fields N and e from hex strings
function RSASetPublic(N,E) {
  if(N != null && E != null && N.length > 0 && E.length > 0) {
    this.n = parseBigInt(N,16);
    this.e = parseInt(E,16);
  }
  else
    console.error("Invalid RSA public key");
}

// Perform raw public operation on "x": return x^e (mod n)
function RSADoPublic(x) {
  return x.modPowInt(this.e, this.n);
}

// Return the PKCS#1 RSA encryption of "text" as an even-length hex string
function RSAEncrypt(text) {
  var m = pkcs1pad2(text,(this.n.bitLength()+7)>>3);
  if(m == null) return null;
  var c = this.doPublic(m);
  if(c == null) return null;
  var h = c.toString(16);
  if((h.length & 1) == 0) return h; else return "0" + h;
}

// Return the PKCS#1 RSA encryption of "text" as a Base64-encoded string
//function RSAEncryptB64(text) {
//  var h = this.encrypt(text);
//  if(h) return hex2b64(h); else return null;
//}

// protected
RSAKey.prototype.doPublic = RSADoPublic;

// public
RSAKey.prototype.setPublic = RSASetPublic;
RSAKey.prototype.encrypt = RSAEncrypt;
//RSAKey.prototype.encrypt_b64 = RSAEncryptB64;
// Depends on rsa.js and jsbn2.js

// Version 1.1: support utf-8 decoding in pkcs1unpad2

// Undo PKCS#1 (type 2, random) padding and, if valid, return the plaintext
function pkcs1unpad2(d,n) {
  var b = d.toByteArray();
  var i = 0;
  while(i < b.length && b[i] == 0) ++i;
  if(b.length-i != n-1 || b[i] != 2)
    return null;
  ++i;
  while(b[i] != 0)
    if(++i >= b.length) return null;
  var ret = "";
  while(++i < b.length) {
    var c = b[i] & 255;
    if(c < 128) { // utf-8 decode
      ret += String.fromCharCode(c);
    }
    else if((c > 191) && (c < 224)) {
      ret += String.fromCharCode(((c & 31) << 6) | (b[i+1] & 63));
      ++i;
    }
    else {
      ret += String.fromCharCode(((c & 15) << 12) | ((b[i+1] & 63) << 6) | (b[i+2] & 63));
      i += 2;
    }
  }
  return ret;
}

// Set the private key fields N, e, and d from hex strings
function RSASetPrivate(N,E,D) {
  if(N != null && E != null && N.length > 0 && E.length > 0) {
    this.n = parseBigInt(N,16);
    this.e = parseInt(E,16);
    this.d = parseBigInt(D,16);
  }
  else
    console.error("Invalid RSA private key");
}

// Set the private key fields N, e, d and CRT params from hex strings
function RSASetPrivateEx(N,E,D,P,Q,DP,DQ,C) {
  if(N != null && E != null && N.length > 0 && E.length > 0) {
    this.n = parseBigInt(N,16);
    this.e = parseInt(E,16);
    this.d = parseBigInt(D,16);
    this.p = parseBigInt(P,16);
    this.q = parseBigInt(Q,16);
    this.dmp1 = parseBigInt(DP,16);
    this.dmq1 = parseBigInt(DQ,16);
    this.coeff = parseBigInt(C,16);
  }
  else
    console.error("Invalid RSA private key");
}

// Generate a new random private key B bits long, using public expt E
function RSAGenerate(B,E) {
  var rng = new SecureRandom();
  var qs = B>>1;
  this.e = parseInt(E,16);
  var ee = new BigInteger(E,16);
  for(;;) {
    for(;;) {
      this.p = new BigInteger(B-qs,1,rng);
      if(this.p.subtract(BigInteger.ONE).gcd(ee).compareTo(BigInteger.ONE) == 0 && this.p.isProbablePrime(10)) break;
    }
    for(;;) {
      this.q = new BigInteger(qs,1,rng);
      if(this.q.subtract(BigInteger.ONE).gcd(ee).compareTo(BigInteger.ONE) == 0 && this.q.isProbablePrime(10)) break;
    }
    if(this.p.compareTo(this.q) <= 0) {
      var t = this.p;
      this.p = this.q;
      this.q = t;
    }
    var p1 = this.p.subtract(BigInteger.ONE);
    var q1 = this.q.subtract(BigInteger.ONE);
    var phi = p1.multiply(q1);
    if(phi.gcd(ee).compareTo(BigInteger.ONE) == 0) {
      this.n = this.p.multiply(this.q);
      this.d = ee.modInverse(phi);
      this.dmp1 = this.d.mod(p1);
      this.dmq1 = this.d.mod(q1);
      this.coeff = this.q.modInverse(this.p);
      break;
    }
  }
}

// Perform raw private operation on "x": return x^d (mod n)
function RSADoPrivate(x) {
  if(this.p == null || this.q == null)
    return x.modPow(this.d, this.n);

  // TODO: re-calculate any missing CRT params
  var xp = x.mod(this.p).modPow(this.dmp1, this.p);
  var xq = x.mod(this.q).modPow(this.dmq1, this.q);

  while(xp.compareTo(xq) < 0)
    xp = xp.add(this.p);
  return xp.subtract(xq).multiply(this.coeff).mod(this.p).multiply(this.q).add(xq);
}

// Return the PKCS#1 RSA decryption of "ctext".
// "ctext" is an even-length hex string and the output is a plain string.
function RSADecrypt(ctext) {
  var c = parseBigInt(ctext, 16);
  var m = this.doPrivate(c);
  if(m == null) return null;
  return pkcs1unpad2(m, (this.n.bitLength()+7)>>3);
}

// Return the PKCS#1 RSA decryption of "ctext".
// "ctext" is a Base64-encoded string and the output is a plain string.
//function RSAB64Decrypt(ctext) {
//  var h = b64tohex(ctext);
//  if(h) return this.decrypt(h); else return null;
//}

// protected
RSAKey.prototype.doPrivate = RSADoPrivate;

// public
RSAKey.prototype.setPrivate = RSASetPrivate;
RSAKey.prototype.setPrivateEx = RSASetPrivateEx;
RSAKey.prototype.generate = RSAGenerate;
RSAKey.prototype.decrypt = RSADecrypt;
//RSAKey.prototype.b64_decrypt = RSAB64Decrypt;
// Copyright (c) 2011  Kevin M Burns Jr.
// All Rights Reserved.
// See "LICENSE" for details.
//
// Extension to jsbn which adds facilities for asynchronous RSA key generation
// Primarily created to avoid execution timeout on mobile devices
//
// http://www-cs-students.stanford.edu/~tjw/jsbn/
//
// ---

(function(){

// Generate a new random private key B bits long, using public expt E
var RSAGenerateAsync = function (B, E, callback) {
    //var rng = new SeededRandom();
    var rng = new SecureRandom();
    var qs = B >> 1;
    this.e = parseInt(E, 16);
    var ee = new BigInteger(E, 16);
    var rsa = this;
    // These functions have non-descript names because they were originally for(;;) loops.
    // I don't know about cryptography to give them better names than loop1-4.
    var loop1 = function() {
        var loop4 = function() {
            if (rsa.p.compareTo(rsa.q) <= 0) {
                var t = rsa.p;
                rsa.p = rsa.q;
                rsa.q = t;
            }
            var p1 = rsa.p.subtract(BigInteger.ONE);
            var q1 = rsa.q.subtract(BigInteger.ONE);
            var phi = p1.multiply(q1);
            if (phi.gcd(ee).compareTo(BigInteger.ONE) == 0) {
                rsa.n = rsa.p.multiply(rsa.q);
                rsa.d = ee.modInverse(phi);
                rsa.dmp1 = rsa.d.mod(p1);
                rsa.dmq1 = rsa.d.mod(q1);
                rsa.coeff = rsa.q.modInverse(rsa.p);
                setTimeout(function(){callback()},0); // escape
            } else {
                setTimeout(loop1,0);
            }
        };
        var loop3 = function() {
            rsa.q = nbi();
            rsa.q.fromNumberAsync(qs, 1, rng, function(){
                rsa.q.subtract(BigInteger.ONE).gcda(ee, function(r){
                    if (r.compareTo(BigInteger.ONE) == 0 && rsa.q.isProbablePrime(10)) {
                        setTimeout(loop4,0);
                    } else {
                        setTimeout(loop3,0);
                    }
                });
            });
        };
        var loop2 = function() {
            rsa.p = nbi();
            rsa.p.fromNumberAsync(B - qs, 1, rng, function(){
                rsa.p.subtract(BigInteger.ONE).gcda(ee, function(r){
                    if (r.compareTo(BigInteger.ONE) == 0 && rsa.p.isProbablePrime(10)) {
                        setTimeout(loop3,0);
                    } else {
                        setTimeout(loop2,0);
                    }
                });
            });
        };
        setTimeout(loop2,0);
    };
    setTimeout(loop1,0);
};
RSAKey.prototype.generateAsync = RSAGenerateAsync;

// Public API method
var bnGCDAsync = function (a, callback) {
    var x = (this.s < 0) ? this.negate() : this.clone();
    var y = (a.s < 0) ? a.negate() : a.clone();
    if (x.compareTo(y) < 0) {
        var t = x;
        x = y;
        y = t;
    }
    var i = x.getLowestSetBit(),
        g = y.getLowestSetBit();
    if (g < 0) {
        callback(x);
        return;
    }
    if (i < g) g = i;
    if (g > 0) {
        x.rShiftTo(g, x);
        y.rShiftTo(g, y);
    }
    // Workhorse of the algorithm, gets called 200 - 800 times per 512 bit keygen.
    var gcda1 = function() {
        if ((i = x.getLowestSetBit()) > 0){ x.rShiftTo(i, x); }
        if ((i = y.getLowestSetBit()) > 0){ y.rShiftTo(i, y); }
        if (x.compareTo(y) >= 0) {
            x.subTo(y, x);
            x.rShiftTo(1, x);
        } else {
            y.subTo(x, y);
            y.rShiftTo(1, y);
        }
        if(!(x.signum() > 0)) {
            if (g > 0) y.lShiftTo(g, y);
            setTimeout(function(){callback(y)},0); // escape
        } else {
            setTimeout(gcda1,0);
        }
    };
    setTimeout(gcda1,10);
};
BigInteger.prototype.gcda = bnGCDAsync;

// (protected) alternate constructor
var bnpFromNumberAsync = function (a,b,c,callback) {
  if("number" == typeof b) {
    if(a < 2) {
        this.fromInt(1);
    } else {
      this.fromNumber(a,c);
      if(!this.testBit(a-1)){
        this.bitwiseTo(BigInteger.ONE.shiftLeft(a-1),op_or,this);
      }
      if(this.isEven()) {
        this.dAddOffset(1,0);
      }
      var bnp = this;
      var bnpfn1 = function(){
        bnp.dAddOffset(2,0);
        if(bnp.bitLength() > a) bnp.subTo(BigInteger.ONE.shiftLeft(a-1),bnp);
        if(bnp.isProbablePrime(b)) {
            setTimeout(function(){callback()},0); // escape
        } else {
            setTimeout(bnpfn1,0);
        }
      };
      setTimeout(bnpfn1,0);
    }
  } else {
    var x = new Array(), t = a&7;
    x.length = (a>>3)+1;
    b.nextBytes(x);
    if(t > 0) x[0] &= ((1<<t)-1); else x[0] = 0;
    this.fromString(x,256);
  }
};
BigInteger.prototype.fromNumberAsync = bnpFromNumberAsync;

})();var b64map="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
var b64pad="=";

function hex2b64(h) {
  var i;
  var c;
  var ret = "";
  for(i = 0; i+3 <= h.length; i+=3) {
    c = parseInt(h.substring(i,i+3),16);
    ret += b64map.charAt(c >> 6) + b64map.charAt(c & 63);
  }
  if(i+1 == h.length) {
    c = parseInt(h.substring(i,i+1),16);
    ret += b64map.charAt(c << 2);
  }
  else if(i+2 == h.length) {
    c = parseInt(h.substring(i,i+2),16);
    ret += b64map.charAt(c >> 2) + b64map.charAt((c & 3) << 4);
  }
  while((ret.length & 3) > 0) ret += b64pad;
  return ret;
}

// convert a base64 string to hex
function b64tohex(s) {
  var ret = ""
  var i;
  var k = 0; // b64 state, 0-3
  var slop;
  for(i = 0; i < s.length; ++i) {
    if(s.charAt(i) == b64pad) break;
    v = b64map.indexOf(s.charAt(i));
    if(v < 0) continue;
    if(k == 0) {
      ret += int2char(v >> 2);
      slop = v & 3;
      k = 1;
    }
    else if(k == 1) {
      ret += int2char((slop << 2) | (v >> 4));
      slop = v & 0xf;
      k = 2;
    }
    else if(k == 2) {
      ret += int2char(slop);
      ret += int2char(v >> 2);
      slop = v & 3;
      k = 3;
    }
    else {
      ret += int2char((slop << 2) | (v >> 4));
      ret += int2char(v & 0xf);
      k = 0;
    }
  }
  if(k == 1)
    ret += int2char(slop << 2);
  return ret;
}

// convert a base64 string to a byte/number array
function b64toBA(s) {
  //piggyback on b64tohex for now, optimize later
  var h = b64tohex(s);
  var i;
  var a = new Array();
  for(i = 0; 2*i < h.length; ++i) {
    a[i] = parseInt(h.substring(2*i,2*i+2),16);
  }
  return a;
}
/*! asn1-1.0.2.js (c) 2013 Kenji Urushima | kjur.github.com/jsrsasign/license
 */

var JSX = JSX || {};
JSX.env = JSX.env || {};

var L = JSX, OP = Object.prototype, FUNCTION_TOSTRING = '[object Function]',ADD = ["toString", "valueOf"];

JSX.env.parseUA = function(agent) {

    var numberify = function(s) {
        var c = 0;
        return parseFloat(s.replace(/\./g, function() {
            return (c++ == 1) ? '' : '.';
        }));
    },

    nav = navigator,
    o = {
        ie: 0,
        opera: 0,
        gecko: 0,
        webkit: 0,
        chrome: 0,
        mobile: null,
        air: 0,
        ipad: 0,
        iphone: 0,
        ipod: 0,
        ios: null,
        android: 0,
        webos: 0,
        caja: nav && nav.cajaVersion,
        secure: false,
        os: null

    },

    ua = agent || (navigator && navigator.userAgent),
    loc = window && window.location,
    href = loc && loc.href,
    m;

    o.secure = href && (href.toLowerCase().indexOf("https") === 0);

    if (ua) {

        if ((/windows|win32/i).test(ua)) {
            o.os = 'windows';
        } else if ((/macintosh/i).test(ua)) {
            o.os = 'macintosh';
        } else if ((/rhino/i).test(ua)) {
            o.os = 'rhino';
        }
        if ((/KHTML/).test(ua)) {
            o.webkit = 1;
        }
        m = ua.match(/AppleWebKit\/([^\s]*)/);
        if (m && m[1]) {
            o.webkit = numberify(m[1]);
            if (/ Mobile\//.test(ua)) {
                o.mobile = 'Apple'; // iPhone or iPod Touch
                m = ua.match(/OS ([^\s]*)/);
                if (m && m[1]) {
                    m = numberify(m[1].replace('_', '.'));
                }
                o.ios = m;
                o.ipad = o.ipod = o.iphone = 0;
                m = ua.match(/iPad|iPod|iPhone/);
                if (m && m[0]) {
                    o[m[0].toLowerCase()] = o.ios;
                }
            } else {
                m = ua.match(/NokiaN[^\/]*|Android \d\.\d|webOS\/\d\.\d/);
                if (m) {
                    o.mobile = m[0];
                }
                if (/webOS/.test(ua)) {
                    o.mobile = 'WebOS';
                    m = ua.match(/webOS\/([^\s]*);/);
                    if (m && m[1]) {
                        o.webos = numberify(m[1]);
                    }
                }
                if (/ Android/.test(ua)) {
                    o.mobile = 'Android';
                    m = ua.match(/Android ([^\s]*);/);
                    if (m && m[1]) {
                        o.android = numberify(m[1]);
                    }
                }
            }
            m = ua.match(/Chrome\/([^\s]*)/);
            if (m && m[1]) {
                o.chrome = numberify(m[1]); // Chrome
            } else {
                m = ua.match(/AdobeAIR\/([^\s]*)/);
                if (m) {
                    o.air = m[0]; // Adobe AIR 1.0 or better
                }
            }
        }
        if (!o.webkit) {
            m = ua.match(/Opera[\s\/]([^\s]*)/);
            if (m && m[1]) {
                o.opera = numberify(m[1]);
                m = ua.match(/Version\/([^\s]*)/);
                if (m && m[1]) {
                    o.opera = numberify(m[1]); // opera 10+
                }
                m = ua.match(/Opera Mini[^;]*/);
                if (m) {
                    o.mobile = m[0]; // ex: Opera Mini/2.0.4509/1316
                }
            } else { // not opera or webkit
                m = ua.match(/MSIE\s([^;]*)/);
                if (m && m[1]) {
                    o.ie = numberify(m[1]);
                } else { // not opera, webkit, or ie
                    m = ua.match(/Gecko\/([^\s]*)/);
                    if (m) {
                        o.gecko = 1; // Gecko detected, look for revision
                        m = ua.match(/rv:([^\s\)]*)/);
                        if (m && m[1]) {
                            o.gecko = numberify(m[1]);
                        }
                    }
                }
            }
        }
    }
    return o;
};

JSX.env.ua = JSX.env.parseUA();

JSX.isFunction = function(o) {
    return (typeof o === 'function') || OP.toString.apply(o) === FUNCTION_TOSTRING;
};

JSX._IEEnumFix = (JSX.env.ua.ie) ? function(r, s) {
    var i, fname, f;
    for (i=0;i<ADD.length;i=i+1) {

        fname = ADD[i];
        f = s[fname];

        if (L.isFunction(f) && f!=OP[fname]) {
            r[fname]=f;
        }
    }
} : function(){};

JSX.extend = function(subc, superc, overrides) {
    if (!superc||!subc) {
        throw new Error("extend failed, please check that " +
                        "all dependencies are included.");
    }
    var F = function() {}, i;
    F.prototype=superc.prototype;
    subc.prototype=new F();
    subc.prototype.constructor=subc;
    subc.superclass=superc.prototype;
    if (superc.prototype.constructor == OP.constructor) {
        superc.prototype.constructor=superc;
    }

    if (overrides) {
        for (i in overrides) {
            if (L.hasOwnProperty(overrides, i)) {
                subc.prototype[i]=overrides[i];
            }
        }

        L._IEEnumFix(subc.prototype, overrides);
    }
};

/*
 * asn1.js - ASN.1 DER encoder classes
 *
 * Copyright (c) 2013 Kenji Urushima (kenji.urushima@gmail.com)
 *
 * This software is licensed under the terms of the MIT License.
 * http://kjur.github.com/jsrsasign/license
 *
 * The above copyright and license notice shall be 
 * included in all copies or substantial portions of the Software.
 */

/**
 * @fileOverview
 * @name asn1-1.0.js
 * @author Kenji Urushima kenji.urushima@gmail.com
 * @version 1.0.2 (2013-May-30)
 * @since 2.1
 * @license <a href="http://kjur.github.io/jsrsasign/license/">MIT License</a>
 */

/** 
 * kjur's class library name space
 * <p>
 * This name space provides following name spaces:
 * <ul>
 * <li>{@link KJUR.asn1} - ASN.1 primitive hexadecimal encoder</li>
 * <li>{@link KJUR.asn1.x509} - ASN.1 structure for X.509 certificate and CRL</li>
 * <li>{@link KJUR.crypto} - Java Cryptographic Extension(JCE) style MessageDigest/Signature 
 * class and utilities</li>
 * </ul>
 * </p> 
 * NOTE: Please ignore method summary and document of this namespace. This caused by a bug of jsdoc2.
  * @name KJUR
 * @namespace kjur's class library name space
 */
if (typeof KJUR == "undefined" || !KJUR) KJUR = {};

/**
 * kjur's ASN.1 class library name space
 * <p>
 * This is ITU-T X.690 ASN.1 DER encoder class library and
 * class structure and methods is very similar to 
 * org.bouncycastle.asn1 package of 
 * well known BouncyCaslte Cryptography Library.
 *
 * <h4>PROVIDING ASN.1 PRIMITIVES</h4>
 * Here are ASN.1 DER primitive classes.
 * <ul>
 * <li>{@link KJUR.asn1.DERBoolean}</li>
 * <li>{@link KJUR.asn1.DERInteger}</li>
 * <li>{@link KJUR.asn1.DERBitString}</li>
 * <li>{@link KJUR.asn1.DEROctetString}</li>
 * <li>{@link KJUR.asn1.DERNull}</li>
 * <li>{@link KJUR.asn1.DERObjectIdentifier}</li>
 * <li>{@link KJUR.asn1.DERUTF8String}</li>
 * <li>{@link KJUR.asn1.DERNumericString}</li>
 * <li>{@link KJUR.asn1.DERPrintableString}</li>
 * <li>{@link KJUR.asn1.DERTeletexString}</li>
 * <li>{@link KJUR.asn1.DERIA5String}</li>
 * <li>{@link KJUR.asn1.DERUTCTime}</li>
 * <li>{@link KJUR.asn1.DERGeneralizedTime}</li>
 * <li>{@link KJUR.asn1.DERSequence}</li>
 * <li>{@link KJUR.asn1.DERSet}</li>
 * </ul>
 *
 * <h4>OTHER ASN.1 CLASSES</h4>
 * <ul>
 * <li>{@link KJUR.asn1.ASN1Object}</li>
 * <li>{@link KJUR.asn1.DERAbstractString}</li>
 * <li>{@link KJUR.asn1.DERAbstractTime}</li>
 * <li>{@link KJUR.asn1.DERAbstractStructured}</li>
 * <li>{@link KJUR.asn1.DERTaggedObject}</li>
 * </ul>
 * </p>
 * NOTE: Please ignore method summary and document of this namespace. This caused by a bug of jsdoc2.
 * @name KJUR.asn1
 * @namespace
 */
if (typeof KJUR.asn1 == "undefined" || !KJUR.asn1) KJUR.asn1 = {};

/**
 * ASN1 utilities class
 * @name KJUR.asn1.ASN1Util
 * @classs ASN1 utilities class
 * @since asn1 1.0.2
 */
KJUR.asn1.ASN1Util = new function() {
    this.integerToByteHex = function(i) {
	var h = i.toString(16);
	if ((h.length % 2) == 1) h = '0' + h;
	return h;
    };
    this.bigIntToMinTwosComplementsHex = function(bigIntegerValue) {
	var h = bigIntegerValue.toString(16);
	if (h.substr(0, 1) != '-') {
	    if (h.length % 2 == 1) {
		h = '0' + h;
	    } else {
		if (! h.match(/^[0-7]/)) {
		    h = '00' + h;
		}
	    }
	} else {
	    var hPos = h.substr(1);
	    var xorLen = hPos.length;
	    if (xorLen % 2 == 1) {
		xorLen += 1;
	    } else {
		if (! h.match(/^[0-7]/)) {
		    xorLen += 2;
		}
	    }
	    var hMask = '';
	    for (var i = 0; i < xorLen; i++) {
		hMask += 'f';
	    }
	    var biMask = new BigInteger(hMask, 16);
	    var biNeg = biMask.xor(bigIntegerValue).add(BigInteger.ONE);
	    h = biNeg.toString(16).replace(/^-/, '');
	}
	return h;
    };
    /**
     * get PEM string from hexadecimal data and header string
     * @name getPEMStringFromHex
     * @memberOf KJUR.asn1.ASN1Util
     * @function
     * @param {String} dataHex hexadecimal string of PEM body
     * @param {String} pemHeader PEM header string (ex. 'RSA PRIVATE KEY')
     * @return {String} PEM formatted string of input data
     * @description
     * @example
     * var pem  = KJUR.asn1.ASN1Util.getPEMStringFromHex('616161', 'RSA PRIVATE KEY');
     * // value of pem will be:
     * -----BEGIN PRIVATE KEY-----
     * YWFh
     * -----END PRIVATE KEY-----
     */
    this.getPEMStringFromHex = function(dataHex, pemHeader) {
	var dataWA = CryptoJS.enc.Hex.parse(dataHex);
	var dataB64 = CryptoJS.enc.Base64.stringify(dataWA);
	var pemBody = dataB64.replace(/(.{64})/g, "$1\r\n");
        pemBody = pemBody.replace(/\r\n$/, '');
	return "-----BEGIN " + pemHeader + "-----\r\n" + 
               pemBody + 
               "\r\n-----END " + pemHeader + "-----\r\n";
    };
};

// ********************************************************************
//  Abstract ASN.1 Classes
// ********************************************************************

// ********************************************************************

/**
 * base class for ASN.1 DER encoder object
 * @name KJUR.asn1.ASN1Object
 * @class base class for ASN.1 DER encoder object
 * @property {Boolean} isModified flag whether internal data was changed
 * @property {String} hTLV hexadecimal string of ASN.1 TLV
 * @property {String} hT hexadecimal string of ASN.1 TLV tag(T)
 * @property {String} hL hexadecimal string of ASN.1 TLV length(L)
 * @property {String} hV hexadecimal string of ASN.1 TLV value(V)
 * @description
 */
KJUR.asn1.ASN1Object = function() {
    var isModified = true;
    var hTLV = null;
    var hT = '00'
    var hL = '00';
    var hV = '';

    /**
     * get hexadecimal ASN.1 TLV length(L) bytes from TLV value(V)
     * @name getLengthHexFromValue
     * @memberOf KJUR.asn1.ASN1Object
     * @function
     * @return {String} hexadecimal string of ASN.1 TLV length(L)
     */
    this.getLengthHexFromValue = function() {
	if (typeof this.hV == "undefined" || this.hV == null) {
	    throw "this.hV is null or undefined.";
	}
	if (this.hV.length % 2 == 1) {
	    throw "value hex must be even length: n=" + hV.length + ",v=" + this.hV;
	}
	var n = this.hV.length / 2;
	var hN = n.toString(16);
	if (hN.length % 2 == 1) {
	    hN = "0" + hN;
	}
	if (n < 128) {
	    return hN;
	} else {
	    var hNlen = hN.length / 2;
	    if (hNlen > 15) {
		throw "ASN.1 length too long to represent by 8x: n = " + n.toString(16);
	    }
	    var head = 128 + hNlen;
	    return head.toString(16) + hN;
	}
    };

    /**
     * get hexadecimal string of ASN.1 TLV bytes
     * @name getEncodedHex
     * @memberOf KJUR.asn1.ASN1Object
     * @function
     * @return {String} hexadecimal string of ASN.1 TLV
     */
    this.getEncodedHex = function() {
	if (this.hTLV == null || this.isModified) {
	    this.hV = this.getFreshValueHex();
	    this.hL = this.getLengthHexFromValue();
	    this.hTLV = this.hT + this.hL + this.hV;
	    this.isModified = false;
	    //console.error("first time: " + this.hTLV);
	}
	return this.hTLV;
    };

    /**
     * get hexadecimal string of ASN.1 TLV value(V) bytes
     * @name getValueHex
     * @memberOf KJUR.asn1.ASN1Object
     * @function
     * @return {String} hexadecimal string of ASN.1 TLV value(V) bytes
     */
    this.getValueHex = function() {
	this.getEncodedHex();
	return this.hV;
    }

    this.getFreshValueHex = function() {
	return '';
    };
};

// == BEGIN DERAbstractString ================================================
/**
 * base class for ASN.1 DER string classes
 * @name KJUR.asn1.DERAbstractString
 * @class base class for ASN.1 DER string classes
 * @param {Array} params associative array of parameters (ex. {'str': 'aaa'})
 * @property {String} s internal string of value
 * @extends KJUR.asn1.ASN1Object
 * @description
 * <br/>
 * As for argument 'params' for constructor, you can specify one of
 * following properties:
 * <ul>
 * <li>str - specify initial ASN.1 value(V) by a string</li>
 * <li>hex - specify initial ASN.1 value(V) by a hexadecimal string</li>
 * </ul>
 * NOTE: 'params' can be omitted.
 */
KJUR.asn1.DERAbstractString = function(params) {
    KJUR.asn1.DERAbstractString.superclass.constructor.call(this);
    var s = null;
    var hV = null;

    /**
     * get string value of this string object
     * @name getString
     * @memberOf KJUR.asn1.DERAbstractString
     * @function
     * @return {String} string value of this string object
     */
    this.getString = function() {
	return this.s;
    };

    /**
     * set value by a string
     * @name setString
     * @memberOf KJUR.asn1.DERAbstractString
     * @function
     * @param {String} newS value by a string to set
     */
    this.setString = function(newS) {
	this.hTLV = null;
	this.isModified = true;
	this.s = newS;
	this.hV = stohex(this.s);
    };

    /**
     * set value by a hexadecimal string
     * @name setStringHex
     * @memberOf KJUR.asn1.DERAbstractString
     * @function
     * @param {String} newHexString value by a hexadecimal string to set
     */
    this.setStringHex = function(newHexString) {
	this.hTLV = null;
	this.isModified = true;
	this.s = null;
	this.hV = newHexString;
    };

    this.getFreshValueHex = function() {
	return this.hV;
    };

    if (typeof params != "undefined") {
	if (typeof params['str'] != "undefined") {
	    this.setString(params['str']);
	} else if (typeof params['hex'] != "undefined") {
	    this.setStringHex(params['hex']);
	}
    }
};
JSX.extend(KJUR.asn1.DERAbstractString, KJUR.asn1.ASN1Object);
// == END   DERAbstractString ================================================

// == BEGIN DERAbstractTime ==================================================
/**
 * base class for ASN.1 DER Generalized/UTCTime class
 * @name KJUR.asn1.DERAbstractTime
 * @class base class for ASN.1 DER Generalized/UTCTime class
 * @param {Array} params associative array of parameters (ex. {'str': '130430235959Z'})
 * @extends KJUR.asn1.ASN1Object
 * @description
 * @see KJUR.asn1.ASN1Object - superclass
 */
KJUR.asn1.DERAbstractTime = function(params) {
    KJUR.asn1.DERAbstractTime.superclass.constructor.call(this);
    var s = null;
    var date = null;

    // --- PRIVATE METHODS --------------------
    this.localDateToUTC = function(d) {
	utc = d.getTime() + (d.getTimezoneOffset() * 60000);
	var utcDate = new Date(utc);
	return utcDate;
    };

    this.formatDate = function(dateObject, type) {
	var pad = this.zeroPadding;
	var d = this.localDateToUTC(dateObject);
	var year = String(d.getFullYear());
	if (type == 'utc') year = year.substr(2, 2);
	var month = pad(String(d.getMonth() + 1), 2);
	var day = pad(String(d.getDate()), 2);
	var hour = pad(String(d.getHours()), 2);
	var min = pad(String(d.getMinutes()), 2);
	var sec = pad(String(d.getSeconds()), 2);
	return year + month + day + hour + min + sec + 'Z';
    };

    this.zeroPadding = function(s, len) {
	if (s.length >= len) return s;
	return new Array(len - s.length + 1).join('0') + s;
    };

    // --- PUBLIC METHODS --------------------
    /**
     * get string value of this string object
     * @name getString
     * @memberOf KJUR.asn1.DERAbstractTime
     * @function
     * @return {String} string value of this time object
     */
    this.getString = function() {
	return this.s;
    };

    /**
     * set value by a string
     * @name setString
     * @memberOf KJUR.asn1.DERAbstractTime
     * @function
     * @param {String} newS value by a string to set such like "130430235959Z"
     */
    this.setString = function(newS) {
	this.hTLV = null;
	this.isModified = true;
	this.s = newS;
	this.hV = stohex(this.s);
    };

    /**
     * set value by a Date object
     * @name setByDateValue
     * @memberOf KJUR.asn1.DERAbstractTime
     * @function
     * @param {Integer} year year of date (ex. 2013)
     * @param {Integer} month month of date between 1 and 12 (ex. 12)
     * @param {Integer} day day of month
     * @param {Integer} hour hours of date
     * @param {Integer} min minutes of date
     * @param {Integer} sec seconds of date
     */
    this.setByDateValue = function(year, month, day, hour, min, sec) {
	var dateObject = new Date(Date.UTC(year, month - 1, day, hour, min, sec, 0));
	this.setByDate(dateObject);
    };

    this.getFreshValueHex = function() {
	return this.hV;
    };
};
JSX.extend(KJUR.asn1.DERAbstractTime, KJUR.asn1.ASN1Object);
// == END   DERAbstractTime ==================================================

// == BEGIN DERAbstractStructured ============================================
/**
 * base class for ASN.1 DER structured class
 * @name KJUR.asn1.DERAbstractStructured
 * @class base class for ASN.1 DER structured class
 * @property {Array} asn1Array internal array of ASN1Object
 * @extends KJUR.asn1.ASN1Object
 * @description
 * @see KJUR.asn1.ASN1Object - superclass
 */
KJUR.asn1.DERAbstractStructured = function(params) {
    KJUR.asn1.DERAbstractString.superclass.constructor.call(this);
    var asn1Array = null;

    /**
     * set value by array of ASN1Object
     * @name setByASN1ObjectArray
     * @memberOf KJUR.asn1.DERAbstractStructured
     * @function
     * @param {array} asn1ObjectArray array of ASN1Object to set
     */
    this.setByASN1ObjectArray = function(asn1ObjectArray) {
	this.hTLV = null;
	this.isModified = true;
	this.asn1Array = asn1ObjectArray;
    };

    /**
     * append an ASN1Object to internal array
     * @name appendASN1Object
     * @memberOf KJUR.asn1.DERAbstractStructured
     * @function
     * @param {ASN1Object} asn1Object to add
     */
    this.appendASN1Object = function(asn1Object) {
	this.hTLV = null;
	this.isModified = true;
	this.asn1Array.push(asn1Object);
    };

    this.asn1Array = new Array();
    if (typeof params != "undefined") {
	if (typeof params['array'] != "undefined") {
	    this.asn1Array = params['array'];
	}
    }
};
JSX.extend(KJUR.asn1.DERAbstractStructured, KJUR.asn1.ASN1Object);


// ********************************************************************
//  ASN.1 Object Classes
// ********************************************************************

// ********************************************************************
/**
 * class for ASN.1 DER Boolean
 * @name KJUR.asn1.DERBoolean
 * @class class for ASN.1 DER Boolean
 * @extends KJUR.asn1.ASN1Object
 * @description
 * @see KJUR.asn1.ASN1Object - superclass
 */
KJUR.asn1.DERBoolean = function() {
    KJUR.asn1.DERBoolean.superclass.constructor.call(this);
    this.hT = "01";
    this.hTLV = "0101ff";
};
JSX.extend(KJUR.asn1.DERBoolean, KJUR.asn1.ASN1Object);

// ********************************************************************
/**
 * class for ASN.1 DER Integer
 * @name KJUR.asn1.DERInteger
 * @class class for ASN.1 DER Integer
 * @extends KJUR.asn1.ASN1Object
 * @description
 * <br/>
 * As for argument 'params' for constructor, you can specify one of
 * following properties:
 * <ul>
 * <li>int - specify initial ASN.1 value(V) by integer value</li>
 * <li>bigint - specify initial ASN.1 value(V) by BigInteger object</li>
 * <li>hex - specify initial ASN.1 value(V) by a hexadecimal string</li>
 * </ul>
 * NOTE: 'params' can be omitted.
 */
KJUR.asn1.DERInteger = function(params) {
    KJUR.asn1.DERInteger.superclass.constructor.call(this);
    this.hT = "02";

    /**
     * set value by Tom Wu's BigInteger object
     * @name setByBigInteger
     * @memberOf KJUR.asn1.DERInteger
     * @function
     * @param {BigInteger} bigIntegerValue to set
     */
    this.setByBigInteger = function(bigIntegerValue) {
	this.hTLV = null;
	this.isModified = true;
	this.hV = KJUR.asn1.ASN1Util.bigIntToMinTwosComplementsHex(bigIntegerValue);
    };

    /**
     * set value by integer value
     * @name setByInteger
     * @memberOf KJUR.asn1.DERInteger
     * @function
     * @param {Integer} integer value to set
     */
    this.setByInteger = function(intValue) {
	var bi = new BigInteger(String(intValue), 10);
	this.setByBigInteger(bi);
    };

    /**
     * set value by integer value
     * @name setValueHex
     * @memberOf KJUR.asn1.DERInteger
     * @function
     * @param {String} hexadecimal string of integer value
     * @description
     * <br/>
     * NOTE: Value shall be represented by minimum octet length of
     * two's complement representation.
     */
    this.setValueHex = function(newHexString) {
	this.hV = newHexString;
    };

    this.getFreshValueHex = function() {
	return this.hV;
    };

    if (typeof params != "undefined") {
	if (typeof params['bigint'] != "undefined") {
	    this.setByBigInteger(params['bigint']);
	} else if (typeof params['int'] != "undefined") {
	    this.setByInteger(params['int']);
	} else if (typeof params['hex'] != "undefined") {
	    this.setValueHex(params['hex']);
	}
    }
};
JSX.extend(KJUR.asn1.DERInteger, KJUR.asn1.ASN1Object);

// ********************************************************************
/**
 * class for ASN.1 DER encoded BitString primitive
 * @name KJUR.asn1.DERBitString
 * @class class for ASN.1 DER encoded BitString primitive
 * @extends KJUR.asn1.ASN1Object
 * @description 
 * <br/>
 * As for argument 'params' for constructor, you can specify one of
 * following properties:
 * <ul>
 * <li>bin - specify binary string (ex. '10111')</li>
 * <li>array - specify array of boolean (ex. [true,false,true,true])</li>
 * <li>hex - specify hexadecimal string of ASN.1 value(V) including unused bits</li>
 * </ul>
 * NOTE: 'params' can be omitted.
 */
KJUR.asn1.DERBitString = function(params) {
    KJUR.asn1.DERBitString.superclass.constructor.call(this);
    this.hT = "03";

    /**
     * set ASN.1 value(V) by a hexadecimal string including unused bits
     * @name setHexValueIncludingUnusedBits
     * @memberOf KJUR.asn1.DERBitString
     * @function
     * @param {String} newHexStringIncludingUnusedBits
     */
    this.setHexValueIncludingUnusedBits = function(newHexStringIncludingUnusedBits) {
	this.hTLV = null;
	this.isModified = true;
	this.hV = newHexStringIncludingUnusedBits;
    };

    /**
     * set ASN.1 value(V) by unused bit and hexadecimal string of value
     * @name setUnusedBitsAndHexValue
     * @memberOf KJUR.asn1.DERBitString
     * @function
     * @param {Integer} unusedBits
     * @param {String} hValue
     */
    this.setUnusedBitsAndHexValue = function(unusedBits, hValue) {
	if (unusedBits < 0 || 7 < unusedBits) {
	    throw "unused bits shall be from 0 to 7: u = " + unusedBits;
	}
	var hUnusedBits = "0" + unusedBits;
	this.hTLV = null;
	this.isModified = true;
	this.hV = hUnusedBits + hValue;
    };

    /**
     * set ASN.1 DER BitString by binary string
     * @name setByBinaryString
     * @memberOf KJUR.asn1.DERBitString
     * @function
     * @param {String} binaryString binary value string (i.e. '10111')
     * @description
     * Its unused bits will be calculated automatically by length of 
     * 'binaryValue'. <br/>
     * NOTE: Trailing zeros '0' will be ignored.
     */
    this.setByBinaryString = function(binaryString) {
	binaryString = binaryString.replace(/0+$/, '');
	var unusedBits = 8 - binaryString.length % 8;
	if (unusedBits == 8) unusedBits = 0;
	for (var i = 0; i <= unusedBits; i++) {
	    binaryString += '0';
	}
	var h = '';
	for (var i = 0; i < binaryString.length - 1; i += 8) {
	    var b = binaryString.substr(i, 8);
	    var x = parseInt(b, 2).toString(16);
	    if (x.length == 1) x = '0' + x;
	    h += x;  
	}
	this.hTLV = null;
	this.isModified = true;
	this.hV = '0' + unusedBits + h;
    };

    /**
     * set ASN.1 TLV value(V) by an array of boolean
     * @name setByBooleanArray
     * @memberOf KJUR.asn1.DERBitString
     * @function
     * @param {array} booleanArray array of boolean (ex. [true, false, true])
     * @description
     * NOTE: Trailing falses will be ignored.
     */
    this.setByBooleanArray = function(booleanArray) {
	var s = '';
	for (var i = 0; i < booleanArray.length; i++) {
	    if (booleanArray[i] == true) {
		s += '1';
	    } else {
		s += '0';
	    }
	}
	this.setByBinaryString(s);
    };

    /**
     * generate an array of false with specified length
     * @name newFalseArray
     * @memberOf KJUR.asn1.DERBitString
     * @function
     * @param {Integer} nLength length of array to generate
     * @return {array} array of boolean faluse
     * @description
     * This static method may be useful to initialize boolean array.
     */
    this.newFalseArray = function(nLength) {
	var a = new Array(nLength);
	for (var i = 0; i < nLength; i++) {
	    a[i] = false;
	}
	return a;
    };

    this.getFreshValueHex = function() {
	return this.hV;
    };

    if (typeof params != "undefined") {
	if (typeof params['hex'] != "undefined") {
	    this.setHexValueIncludingUnusedBits(params['hex']);
	} else if (typeof params['bin'] != "undefined") {
	    this.setByBinaryString(params['bin']);
	} else if (typeof params['array'] != "undefined") {
	    this.setByBooleanArray(params['array']);
	}
    }
};
JSX.extend(KJUR.asn1.DERBitString, KJUR.asn1.ASN1Object);

// ********************************************************************
/**
 * class for ASN.1 DER OctetString
 * @name KJUR.asn1.DEROctetString
 * @class class for ASN.1 DER OctetString
 * @param {Array} params associative array of parameters (ex. {'str': 'aaa'})
 * @extends KJUR.asn1.DERAbstractString
 * @description
 * @see KJUR.asn1.DERAbstractString - superclass
 */
KJUR.asn1.DEROctetString = function(params) {
    KJUR.asn1.DEROctetString.superclass.constructor.call(this, params);
    this.hT = "04";
};
JSX.extend(KJUR.asn1.DEROctetString, KJUR.asn1.DERAbstractString);

// ********************************************************************
/**
 * class for ASN.1 DER Null
 * @name KJUR.asn1.DERNull
 * @class class for ASN.1 DER Null
 * @extends KJUR.asn1.ASN1Object
 * @description
 * @see KJUR.asn1.ASN1Object - superclass
 */
KJUR.asn1.DERNull = function() {
    KJUR.asn1.DERNull.superclass.constructor.call(this);
    this.hT = "05";
    this.hTLV = "0500";
};
JSX.extend(KJUR.asn1.DERNull, KJUR.asn1.ASN1Object);

// ********************************************************************
/**
 * class for ASN.1 DER ObjectIdentifier
 * @name KJUR.asn1.DERObjectIdentifier
 * @class class for ASN.1 DER ObjectIdentifier
 * @param {Array} params associative array of parameters (ex. {'oid': '2.5.4.5'})
 * @extends KJUR.asn1.ASN1Object
 * @description
 * <br/>
 * As for argument 'params' for constructor, you can specify one of
 * following properties:
 * <ul>
 * <li>oid - specify initial ASN.1 value(V) by a oid string (ex. 2.5.4.13)</li>
 * <li>hex - specify initial ASN.1 value(V) by a hexadecimal string</li>
 * </ul>
 * NOTE: 'params' can be omitted.
 */
KJUR.asn1.DERObjectIdentifier = function(params) {
    var itox = function(i) {
	var h = i.toString(16);
	if (h.length == 1) h = '0' + h;
	return h;
    };
    var roidtox = function(roid) {
	var h = '';
	var bi = new BigInteger(roid, 10);
	var b = bi.toString(2);
	var padLen = 7 - b.length % 7;
	if (padLen == 7) padLen = 0;
	var bPad = '';
	for (var i = 0; i < padLen; i++) bPad += '0';
	b = bPad + b;
	for (var i = 0; i < b.length - 1; i += 7) {
	    var b8 = b.substr(i, 7);
	    if (i != b.length - 7) b8 = '1' + b8;
	    h += itox(parseInt(b8, 2));
	}
	return h;
    }

    KJUR.asn1.DERObjectIdentifier.superclass.constructor.call(this);
    this.hT = "06";

    /**
     * set value by a hexadecimal string
     * @name setValueHex
     * @memberOf KJUR.asn1.DERObjectIdentifier
     * @function
     * @param {String} newHexString hexadecimal value of OID bytes
     */
    this.setValueHex = function(newHexString) {
	this.hTLV = null;
	this.isModified = true;
	this.s = null;
	this.hV = newHexString;
    };

    /**
     * set value by a OID string
     * @name setValueOidString
     * @memberOf KJUR.asn1.DERObjectIdentifier
     * @function
     * @param {String} oidString OID string (ex. 2.5.4.13)
     */
    this.setValueOidString = function(oidString) {
	if (! oidString.match(/^[0-9.]+$/)) {
	    throw "malformed oid string: " + oidString;
	}
	var h = '';
	var a = oidString.split('.');
	var i0 = parseInt(a[0]) * 40 + parseInt(a[1]);
	h += itox(i0);
	a.splice(0, 2);
	for (var i = 0; i < a.length; i++) {
	    h += roidtox(a[i]);
	}
	this.hTLV = null;
	this.isModified = true;
	this.s = null;
	this.hV = h;
    };

    /**
     * set value by a OID name
     * @name setValueName
     * @memberOf KJUR.asn1.DERObjectIdentifier
     * @function
     * @param {String} oidName OID name (ex. 'serverAuth')
     * @since 1.0.1
     * @description
     * OID name shall be defined in 'KJUR.asn1.x509.OID.name2oidList'.
     * Otherwise raise error.
     */
    this.setValueName = function(oidName) {
	if (typeof KJUR.asn1.x509.OID.name2oidList[oidName] != "undefined") {
	    var oid = KJUR.asn1.x509.OID.name2oidList[oidName];
	    this.setValueOidString(oid);
	} else {
	    throw "DERObjectIdentifier oidName undefined: " + oidName;
	}
    };

    this.getFreshValueHex = function() {
	return this.hV;
    };

    if (typeof params != "undefined") {
	if (typeof params['oid'] != "undefined") {
	    this.setValueOidString(params['oid']);
	} else if (typeof params['hex'] != "undefined") {
	    this.setValueHex(params['hex']);
	} else if (typeof params['name'] != "undefined") {
	    this.setValueName(params['name']);
	}
    }
};
JSX.extend(KJUR.asn1.DERObjectIdentifier, KJUR.asn1.ASN1Object);

// ********************************************************************
/**
 * class for ASN.1 DER UTF8String
 * @name KJUR.asn1.DERUTF8String
 * @class class for ASN.1 DER UTF8String
 * @param {Array} params associative array of parameters (ex. {'str': 'aaa'})
 * @extends KJUR.asn1.DERAbstractString
 * @description
 * @see KJUR.asn1.DERAbstractString - superclass
 */
KJUR.asn1.DERUTF8String = function(params) {
    KJUR.asn1.DERUTF8String.superclass.constructor.call(this, params);
    this.hT = "0c";
};
JSX.extend(KJUR.asn1.DERUTF8String, KJUR.asn1.DERAbstractString);

// ********************************************************************
/**
 * class for ASN.1 DER NumericString
 * @name KJUR.asn1.DERNumericString
 * @class class for ASN.1 DER NumericString
 * @param {Array} params associative array of parameters (ex. {'str': 'aaa'})
 * @extends KJUR.asn1.DERAbstractString
 * @description
 * @see KJUR.asn1.DERAbstractString - superclass
 */
KJUR.asn1.DERNumericString = function(params) {
    KJUR.asn1.DERNumericString.superclass.constructor.call(this, params);
    this.hT = "12";
};
JSX.extend(KJUR.asn1.DERNumericString, KJUR.asn1.DERAbstractString);

// ********************************************************************
/**
 * class for ASN.1 DER PrintableString
 * @name KJUR.asn1.DERPrintableString
 * @class class for ASN.1 DER PrintableString
 * @param {Array} params associative array of parameters (ex. {'str': 'aaa'})
 * @extends KJUR.asn1.DERAbstractString
 * @description
 * @see KJUR.asn1.DERAbstractString - superclass
 */
KJUR.asn1.DERPrintableString = function(params) {
    KJUR.asn1.DERPrintableString.superclass.constructor.call(this, params);
    this.hT = "13";
};
JSX.extend(KJUR.asn1.DERPrintableString, KJUR.asn1.DERAbstractString);

// ********************************************************************
/**
 * class for ASN.1 DER TeletexString
 * @name KJUR.asn1.DERTeletexString
 * @class class for ASN.1 DER TeletexString
 * @param {Array} params associative array of parameters (ex. {'str': 'aaa'})
 * @extends KJUR.asn1.DERAbstractString
 * @description
 * @see KJUR.asn1.DERAbstractString - superclass
 */
KJUR.asn1.DERTeletexString = function(params) {
    KJUR.asn1.DERTeletexString.superclass.constructor.call(this, params);
    this.hT = "14";
};
JSX.extend(KJUR.asn1.DERTeletexString, KJUR.asn1.DERAbstractString);

// ********************************************************************
/**
 * class for ASN.1 DER IA5String
 * @name KJUR.asn1.DERIA5String
 * @class class for ASN.1 DER IA5String
 * @param {Array} params associative array of parameters (ex. {'str': 'aaa'})
 * @extends KJUR.asn1.DERAbstractString
 * @description
 * @see KJUR.asn1.DERAbstractString - superclass
 */
KJUR.asn1.DERIA5String = function(params) {
    KJUR.asn1.DERIA5String.superclass.constructor.call(this, params);
    this.hT = "16";
};
JSX.extend(KJUR.asn1.DERIA5String, KJUR.asn1.DERAbstractString);

// ********************************************************************
/**
 * class for ASN.1 DER UTCTime
 * @name KJUR.asn1.DERUTCTime
 * @class class for ASN.1 DER UTCTime
 * @param {Array} params associative array of parameters (ex. {'str': '130430235959Z'})
 * @extends KJUR.asn1.DERAbstractTime
 * @description
 * <br/>
 * As for argument 'params' for constructor, you can specify one of
 * following properties:
 * <ul>
 * <li>str - specify initial ASN.1 value(V) by a string (ex.'130430235959Z')</li>
 * <li>hex - specify initial ASN.1 value(V) by a hexadecimal string</li>
 * <li>date - specify Date object.</li>
 * </ul>
 * NOTE: 'params' can be omitted.
 * <h4>EXAMPLES</h4>
 * @example
 * var d1 = new KJUR.asn1.DERUTCTime();
 * d1.setString('130430125959Z');
 *
 * var d2 = new KJUR.asn1.DERUTCTime({'str': '130430125959Z'});
 *
 * var d3 = new KJUR.asn1.DERUTCTime({'date': new Date(Date.UTC(2015, 0, 31, 0, 0, 0, 0))});
 */
KJUR.asn1.DERUTCTime = function(params) {
    KJUR.asn1.DERUTCTime.superclass.constructor.call(this, params);
    this.hT = "17";

    /**
     * set value by a Date object
     * @name setByDate
     * @memberOf KJUR.asn1.DERUTCTime
     * @function
     * @param {Date} dateObject Date object to set ASN.1 value(V)
     */
    this.setByDate = function(dateObject) {
	this.hTLV = null;
	this.isModified = true;
	this.date = dateObject;
	this.s = this.formatDate(this.date, 'utc');
	this.hV = stohex(this.s);
    };

    if (typeof params != "undefined") {
	if (typeof params['str'] != "undefined") {
	    this.setString(params['str']);
	} else if (typeof params['hex'] != "undefined") {
	    this.setStringHex(params['hex']);
	} else if (typeof params['date'] != "undefined") {
	    this.setByDate(params['date']);
	}
    }
};
JSX.extend(KJUR.asn1.DERUTCTime, KJUR.asn1.DERAbstractTime);

// ********************************************************************
/**
 * class for ASN.1 DER GeneralizedTime
 * @name KJUR.asn1.DERGeneralizedTime
 * @class class for ASN.1 DER GeneralizedTime
 * @param {Array} params associative array of parameters (ex. {'str': '20130430235959Z'})
 * @extends KJUR.asn1.DERAbstractTime
 * @description
 * <br/>
 * As for argument 'params' for constructor, you can specify one of
 * following properties:
 * <ul>
 * <li>str - specify initial ASN.1 value(V) by a string (ex.'20130430235959Z')</li>
 * <li>hex - specify initial ASN.1 value(V) by a hexadecimal string</li>
 * <li>date - specify Date object.</li>
 * </ul>
 * NOTE: 'params' can be omitted.
 */
KJUR.asn1.DERGeneralizedTime = function(params) {
    KJUR.asn1.DERGeneralizedTime.superclass.constructor.call(this, params);
    this.hT = "18";

    /**
     * set value by a Date object
     * @name setByDate
     * @memberOf KJUR.asn1.DERGeneralizedTime
     * @function
     * @param {Date} dateObject Date object to set ASN.1 value(V)
     * @example
     * When you specify UTC time, use 'Date.UTC' method like this:<br/>
     * var o = new DERUTCTime();
     * var date = new Date(Date.UTC(2015, 0, 31, 23, 59, 59, 0)); #2015JAN31 23:59:59
     * o.setByDate(date);
     */
    this.setByDate = function(dateObject) {
	this.hTLV = null;
	this.isModified = true;
	this.date = dateObject;
	this.s = this.formatDate(this.date, 'gen');
	this.hV = stohex(this.s);
    };

    if (typeof params != "undefined") {
	if (typeof params['str'] != "undefined") {
	    this.setString(params['str']);
	} else if (typeof params['hex'] != "undefined") {
	    this.setStringHex(params['hex']);
	} else if (typeof params['date'] != "undefined") {
	    this.setByDate(params['date']);
	}
    }
};
JSX.extend(KJUR.asn1.DERGeneralizedTime, KJUR.asn1.DERAbstractTime);

// ********************************************************************
/**
 * class for ASN.1 DER Sequence
 * @name KJUR.asn1.DERSequence
 * @class class for ASN.1 DER Sequence
 * @extends KJUR.asn1.DERAbstractStructured
 * @description
 * <br/>
 * As for argument 'params' for constructor, you can specify one of
 * following properties:
 * <ul>
 * <li>array - specify array of ASN1Object to set elements of content</li>
 * </ul>
 * NOTE: 'params' can be omitted.
 */
KJUR.asn1.DERSequence = function(params) {
    KJUR.asn1.DERSequence.superclass.constructor.call(this, params);
    this.hT = "30";
    this.getFreshValueHex = function() {
	var h = '';
	for (var i = 0; i < this.asn1Array.length; i++) {
	    var asn1Obj = this.asn1Array[i];
	    h += asn1Obj.getEncodedHex();
	}
	this.hV = h;
	return this.hV;
    };
};
JSX.extend(KJUR.asn1.DERSequence, KJUR.asn1.DERAbstractStructured);

// ********************************************************************
/**
 * class for ASN.1 DER Set
 * @name KJUR.asn1.DERSet
 * @class class for ASN.1 DER Set
 * @extends KJUR.asn1.DERAbstractStructured
 * @description
 * <br/>
 * As for argument 'params' for constructor, you can specify one of
 * following properties:
 * <ul>
 * <li>array - specify array of ASN1Object to set elements of content</li>
 * </ul>
 * NOTE: 'params' can be omitted.
 */
KJUR.asn1.DERSet = function(params) {
    KJUR.asn1.DERSet.superclass.constructor.call(this, params);
    this.hT = "31";
    this.getFreshValueHex = function() {
	var a = new Array();
	for (var i = 0; i < this.asn1Array.length; i++) {
	    var asn1Obj = this.asn1Array[i];
	    a.push(asn1Obj.getEncodedHex());
	}
	a.sort();
	this.hV = a.join('');
	return this.hV;
    };
};
JSX.extend(KJUR.asn1.DERSet, KJUR.asn1.DERAbstractStructured);

// ********************************************************************
/**
 * class for ASN.1 DER TaggedObject
 * @name KJUR.asn1.DERTaggedObject
 * @class class for ASN.1 DER TaggedObject
 * @extends KJUR.asn1.ASN1Object
 * @description
 * <br/>
 * Parameter 'tagNoNex' is ASN.1 tag(T) value for this object.
 * For example, if you find '[1]' tag in a ASN.1 dump, 
 * 'tagNoHex' will be 'a1'.
 * <br/>
 * As for optional argument 'params' for constructor, you can specify *ANY* of
 * following properties:
 * <ul>
 * <li>explicit - specify true if this is explicit tag otherwise false 
 *     (default is 'true').</li>
 * <li>tag - specify tag (default is 'a0' which means [0])</li>
 * <li>obj - specify ASN1Object which is tagged</li>
 * </ul>
 * @example
 * d1 = new KJUR.asn1.DERUTF8String({'str':'a'});
 * d2 = new KJUR.asn1.DERTaggedObject({'obj': d1});
 * hex = d2.getEncodedHex();
 */
KJUR.asn1.DERTaggedObject = function(params) {
    KJUR.asn1.DERTaggedObject.superclass.constructor.call(this);
    this.hT = "a0";
    this.hV = '';
    this.isExplicit = true;
    this.asn1Object = null;

    /**
     * set value by an ASN1Object
     * @name setString
     * @memberOf KJUR.asn1.DERTaggedObject
     * @function
     * @param {Boolean} isExplicitFlag flag for explicit/implicit tag
     * @param {Integer} tagNoHex hexadecimal string of ASN.1 tag
     * @param {ASN1Object} asn1Object ASN.1 to encapsulate
     */
    this.setASN1Object = function(isExplicitFlag, tagNoHex, asn1Object) {
	this.hT = tagNoHex;
	this.isExplicit = isExplicitFlag;
	this.asn1Object = asn1Object;
	if (this.isExplicit) {
	    this.hV = this.asn1Object.getEncodedHex();
	    this.hTLV = null;
	    this.isModified = true;
	} else {
	    this.hV = null;
	    this.hTLV = asn1Object.getEncodedHex();
	    this.hTLV = this.hTLV.replace(/^../, tagNoHex);
	    this.isModified = false;
	}
    };

    this.getFreshValueHex = function() {
	return this.hV;
    };

    if (typeof params != "undefined") {
	if (typeof params['tag'] != "undefined") {
	    this.hT = params['tag'];
	}
	if (typeof params['explicit'] != "undefined") {
	    this.isExplicit = params['explicit'];
	}
	if (typeof params['obj'] != "undefined") {
	    this.asn1Object = params['obj'];
	    this.setASN1Object(this.isExplicit, this.hT, this.asn1Object);
	}
    }
};
JSX.extend(KJUR.asn1.DERTaggedObject, KJUR.asn1.ASN1Object);// Hex JavaScript decoder
// Copyright (c) 2008-2013 Lapo Luchini <lapo@lapo.it>

// Permission to use, copy, modify, and/or distribute this software for any
// purpose with or without fee is hereby granted, provided that the above
// copyright notice and this permission notice appear in all copies.
// 
// THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
// WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
// MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR
// ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
// WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
// ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF
// OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.

/*jshint browser: true, strict: true, immed: true, latedef: true, undef: true, regexdash: false */
(function (undefined) {
"use strict";

var Hex = {},
    decoder;

Hex.decode = function(a) {
    var i;
    if (decoder === undefined) {
        var hex = "0123456789ABCDEF",
            ignore = " \f\n\r\t\u00A0\u2028\u2029";
        decoder = [];
        for (i = 0; i < 16; ++i)
            decoder[hex.charAt(i)] = i;
        hex = hex.toLowerCase();
        for (i = 10; i < 16; ++i)
            decoder[hex.charAt(i)] = i;
        for (i = 0; i < ignore.length; ++i)
            decoder[ignore.charAt(i)] = -1;
    }
    var out = [],
        bits = 0,
        char_count = 0;
    for (i = 0; i < a.length; ++i) {
        var c = a.charAt(i);
        if (c == '=')
            break;
        c = decoder[c];
        if (c == -1)
            continue;
        if (c === undefined)
            throw 'Illegal character at offset ' + i;
        bits |= c;
        if (++char_count >= 2) {
            out[out.length] = bits;
            bits = 0;
            char_count = 0;
        } else {
            bits <<= 4;
        }
    }
    if (char_count)
        throw "Hex encoding incomplete: 4 bits missing";
    return out;
};

// export globals
window.Hex = Hex;
})();// Base64 JavaScript decoder
// Copyright (c) 2008-2013 Lapo Luchini <lapo@lapo.it>

// Permission to use, copy, modify, and/or distribute this software for any
// purpose with or without fee is hereby granted, provided that the above
// copyright notice and this permission notice appear in all copies.
// 
// THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
// WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
// MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR
// ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
// WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
// ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF
// OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.

/*jshint browser: true, strict: true, immed: true, latedef: true, undef: true, regexdash: false */
(function (undefined) {
"use strict";

var Base64 = {},
    decoder;

Base64.decode = function (a) {
    var i;
    if (decoder === undefined) {
        var b64 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/",
            ignore = "= \f\n\r\t\u00A0\u2028\u2029";
        decoder = [];
        for (i = 0; i < 64; ++i)
            decoder[b64.charAt(i)] = i;
        for (i = 0; i < ignore.length; ++i)
            decoder[ignore.charAt(i)] = -1;
    }
    var out = [];
    var bits = 0, char_count = 0;
    for (i = 0; i < a.length; ++i) {
        var c = a.charAt(i);
        if (c == '=')
            break;
        c = decoder[c];
        if (c == -1)
            continue;
        if (c === undefined)
            throw 'Illegal character at offset ' + i;
        bits |= c;
        if (++char_count >= 4) {
            out[out.length] = (bits >> 16);
            out[out.length] = (bits >> 8) & 0xFF;
            out[out.length] = bits & 0xFF;
            bits = 0;
            char_count = 0;
        } else {
            bits <<= 6;
        }
    }
    switch (char_count) {
      case 1:
        throw "Base64 encoding incomplete: at least 2 bits missing";
      case 2:
        out[out.length] = (bits >> 10);
        break;
      case 3:
        out[out.length] = (bits >> 16);
        out[out.length] = (bits >> 8) & 0xFF;
        break;
    }
    return out;
};

Base64.re = /-----BEGIN [^-]+-----([A-Za-z0-9+\/=\s]+)-----END [^-]+-----|begin-base64[^\n]+\n([A-Za-z0-9+\/=\s]+)====/;
Base64.unarmor = function (a) {
    var m = Base64.re.exec(a);
    if (m) {
        if (m[1])
            a = m[1];
        else if (m[2])
            a = m[2];
        else
            throw "RegExp out of sync";
    }
    return Base64.decode(a);
};

// export globals
window.Base64 = Base64;
})();// ASN.1 JavaScript decoder
// Copyright (c) 2008-2013 Lapo Luchini <lapo@lapo.it>

// Permission to use, copy, modify, and/or distribute this software for any
// purpose with or without fee is hereby granted, provided that the above
// copyright notice and this permission notice appear in all copies.
// 
// THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
// WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
// MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR
// ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
// WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
// ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF
// OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.

/*jshint browser: true, strict: true, immed: true, latedef: true, undef: true, regexdash: false */
/*global oids */
(function (undefined) {
"use strict";

var hardLimit = 100,
    ellipsis = "\u2026",
    DOM = {
        tag: function (tagName, className) {
            var t = document.createElement(tagName);
            t.className = className;
            return t;
        },
        text: function (str) {
            return document.createTextNode(str);
        }
    };

function Stream(enc, pos) {
    if (enc instanceof Stream) {
        this.enc = enc.enc;
        this.pos = enc.pos;
    } else {
        this.enc = enc;
        this.pos = pos;
    }
}
Stream.prototype.get = function (pos) {
    if (pos === undefined)
        pos = this.pos++;
    if (pos >= this.enc.length)
        throw 'Requesting byte offset ' + pos + ' on a stream of length ' + this.enc.length;
    return this.enc[pos];
};
Stream.prototype.hexDigits = "0123456789ABCDEF";
Stream.prototype.hexByte = function (b) {
    return this.hexDigits.charAt((b >> 4) & 0xF) + this.hexDigits.charAt(b & 0xF);
};
Stream.prototype.hexDump = function (start, end, raw) {
    var s = "";
    for (var i = start; i < end; ++i) {
        s += this.hexByte(this.get(i));
        if (raw !== true)
            switch (i & 0xF) {
            case 0x7: s += "  "; break;
            case 0xF: s += "\n"; break;
            default:  s += " ";
            }
    }
    return s;
};
Stream.prototype.parseStringISO = function (start, end) {
    var s = "";
    for (var i = start; i < end; ++i)
        s += String.fromCharCode(this.get(i));
    return s;
};
Stream.prototype.parseStringUTF = function (start, end) {
    var s = "";
    for (var i = start; i < end; ) {
        var c = this.get(i++);
        if (c < 128)
            s += String.fromCharCode(c);
        else if ((c > 191) && (c < 224))
            s += String.fromCharCode(((c & 0x1F) << 6) | (this.get(i++) & 0x3F));
        else
            s += String.fromCharCode(((c & 0x0F) << 12) | ((this.get(i++) & 0x3F) << 6) | (this.get(i++) & 0x3F));
    }
    return s;
};
Stream.prototype.parseStringBMP = function (start, end) {
    var str = ""
    for (var i = start; i < end; i += 2) {
        var high_byte = this.get(i);
        var low_byte = this.get(i + 1);
        str += String.fromCharCode( (high_byte << 8) + low_byte );
    }

    return str;
};
Stream.prototype.reTime = /^((?:1[89]|2\d)?\d\d)(0[1-9]|1[0-2])(0[1-9]|[12]\d|3[01])([01]\d|2[0-3])(?:([0-5]\d)(?:([0-5]\d)(?:[.,](\d{1,3}))?)?)?(Z|[-+](?:[0]\d|1[0-2])([0-5]\d)?)?$/;
Stream.prototype.parseTime = function (start, end) {
    var s = this.parseStringISO(start, end),
        m = this.reTime.exec(s);
    if (!m)
        return "Unrecognized time: " + s;
    s = m[1] + "-" + m[2] + "-" + m[3] + " " + m[4];
    if (m[5]) {
        s += ":" + m[5];
        if (m[6]) {
            s += ":" + m[6];
            if (m[7])
                s += "." + m[7];
        }
    }
    if (m[8]) {
        s += " UTC";
        if (m[8] != 'Z') {
            s += m[8];
            if (m[9])
                s += ":" + m[9];
        }
    }
    return s;
};
Stream.prototype.parseInteger = function (start, end) {
    //TODO support negative numbers
    var len = end - start;
    if (len > 4) {
        len <<= 3;
        var s = this.get(start);
        if (s === 0)
            len -= 8;
        else
            while (s < 128) {
                s <<= 1;
                --len;
            }
        return "(" + len + " bit)";
    }
    var n = 0;
    for (var i = start; i < end; ++i)
        n = (n << 8) | this.get(i);
    return n;
};
Stream.prototype.parseBitString = function (start, end) {
    var unusedBit = this.get(start),
        lenBit = ((end - start - 1) << 3) - unusedBit,
        s = "(" + lenBit + " bit)";
    if (lenBit <= 20) {
        var skip = unusedBit;
        s += " ";
        for (var i = end - 1; i > start; --i) {
            var b = this.get(i);
            for (var j = skip; j < 8; ++j)
                s += (b >> j) & 1 ? "1" : "0";
            skip = 0;
        }
    }
    return s;
};
Stream.prototype.parseOctetString = function (start, end) {
    var len = end - start,
        s = "(" + len + " byte) ";
    if (len > hardLimit)
        end = start + hardLimit;
    for (var i = start; i < end; ++i)
        s += this.hexByte(this.get(i)); //TODO: also try Latin1?
    if (len > hardLimit)
        s += ellipsis;
    return s;
};
Stream.prototype.parseOID = function (start, end) {
    var s = '',
        n = 0,
        bits = 0;
    for (var i = start; i < end; ++i) {
        var v = this.get(i);
        n = (n << 7) | (v & 0x7F);
        bits += 7;
        if (!(v & 0x80)) { // finished
            if (s === '') {
                var m = n < 80 ? n < 40 ? 0 : 1 : 2;
                s = m + "." + (n - m * 40);
            } else
                s += "." + ((bits >= 31) ? "bigint" : n);
            n = bits = 0;
        }
    }
    return s;
};

function ASN1(stream, header, length, tag, sub) {
    this.stream = stream;
    this.header = header;
    this.length = length;
    this.tag = tag;
    this.sub = sub;
}
ASN1.prototype.typeName = function () {
    if (this.tag === undefined)
        return "unknown";
    var tagClass = this.tag >> 6,
        tagConstructed = (this.tag >> 5) & 1,
        tagNumber = this.tag & 0x1F;
    switch (tagClass) {
    case 0: // universal
        switch (tagNumber) {
        case 0x00: return "EOC";
        case 0x01: return "BOOLEAN";
        case 0x02: return "INTEGER";
        case 0x03: return "BIT_STRING";
        case 0x04: return "OCTET_STRING";
        case 0x05: return "NULL";
        case 0x06: return "OBJECT_IDENTIFIER";
        case 0x07: return "ObjectDescriptor";
        case 0x08: return "EXTERNAL";
        case 0x09: return "REAL";
        case 0x0A: return "ENUMERATED";
        case 0x0B: return "EMBEDDED_PDV";
        case 0x0C: return "UTF8String";
        case 0x10: return "SEQUENCE";
        case 0x11: return "SET";
        case 0x12: return "NumericString";
        case 0x13: return "PrintableString"; // ASCII subset
        case 0x14: return "TeletexString"; // aka T61String
        case 0x15: return "VideotexString";
        case 0x16: return "IA5String"; // ASCII
        case 0x17: return "UTCTime";
        case 0x18: return "GeneralizedTime";
        case 0x19: return "GraphicString";
        case 0x1A: return "VisibleString"; // ASCII subset
        case 0x1B: return "GeneralString";
        case 0x1C: return "UniversalString";
        case 0x1E: return "BMPString";
        default:   return "Universal_" + tagNumber.toString(16);
        }
    case 1: return "Application_" + tagNumber.toString(16);
    case 2: return "[" + tagNumber + "]"; // Context
    case 3: return "Private_" + tagNumber.toString(16);
    }
};
ASN1.prototype.reSeemsASCII = /^[ -~]+$/;
ASN1.prototype.content = function () {
    if (this.tag === undefined)
        return null;
    var tagClass = this.tag >> 6,
        tagNumber = this.tag & 0x1F,
        content = this.posContent(),
        len = Math.abs(this.length);
    if (tagClass !== 0) { // universal
        if (this.sub !== null)
            return "(" + this.sub.length + " elem)";
        //TODO: TRY TO PARSE ASCII STRING
        var s = this.stream.parseStringISO(content, content + Math.min(len, hardLimit));
        if (this.reSeemsASCII.test(s))
            return s.substring(0, 2 * hardLimit) + ((s.length > 2 * hardLimit) ? ellipsis : "");
        else
            return this.stream.parseOctetString(content, content + len);
    }
    switch (tagNumber) {
    case 0x01: // BOOLEAN
        return (this.stream.get(content) === 0) ? "false" : "true";
    case 0x02: // INTEGER
        return this.stream.parseInteger(content, content + len);
    case 0x03: // BIT_STRING
        return this.sub ? "(" + this.sub.length + " elem)" :
            this.stream.parseBitString(content, content + len);
    case 0x04: // OCTET_STRING
        return this.sub ? "(" + this.sub.length + " elem)" :
            this.stream.parseOctetString(content, content + len);
    //case 0x05: // NULL
    case 0x06: // OBJECT_IDENTIFIER
        return this.stream.parseOID(content, content + len);
    //case 0x07: // ObjectDescriptor
    //case 0x08: // EXTERNAL
    //case 0x09: // REAL
    //case 0x0A: // ENUMERATED
    //case 0x0B: // EMBEDDED_PDV
    case 0x10: // SEQUENCE
    case 0x11: // SET
        return "(" + this.sub.length + " elem)";
    case 0x0C: // UTF8String
        return this.stream.parseStringUTF(content, content + len);
    case 0x12: // NumericString
    case 0x13: // PrintableString
    case 0x14: // TeletexString
    case 0x15: // VideotexString
    case 0x16: // IA5String
    //case 0x19: // GraphicString
    case 0x1A: // VisibleString
    //case 0x1B: // GeneralString
    //case 0x1C: // UniversalString
        return this.stream.parseStringISO(content, content + len);
    case 0x1E: // BMPString
        return this.stream.parseStringBMP(content, content + len);
    case 0x17: // UTCTime
    case 0x18: // GeneralizedTime
        return this.stream.parseTime(content, content + len);
    }
    return null;
};
ASN1.prototype.toString = function () {
    return this.typeName() + "@" + this.stream.pos + "[header:" + this.header + ",length:" + this.length + ",sub:" + ((this.sub === null) ? 'null' : this.sub.length) + "]";
};
ASN1.prototype.print = function (indent) {
    if (indent === undefined) indent = '';
    document.writeln(indent + this);
    if (this.sub !== null) {
        indent += '  ';
        for (var i = 0, max = this.sub.length; i < max; ++i)
            this.sub[i].print(indent);
    }
};
ASN1.prototype.toPrettyString = function (indent) {
    if (indent === undefined) indent = '';
    var s = indent + this.typeName() + " @" + this.stream.pos;
    if (this.length >= 0)
        s += "+";
    s += this.length;
    if (this.tag & 0x20)
        s += " (constructed)";
    else if (((this.tag == 0x03) || (this.tag == 0x04)) && (this.sub !== null))
        s += " (encapsulates)";
    s += "\n";
    if (this.sub !== null) {
        indent += '  ';
        for (var i = 0, max = this.sub.length; i < max; ++i)
            s += this.sub[i].toPrettyString(indent);
    }
    return s;
};
ASN1.prototype.toDOM = function () {
    var node = DOM.tag("div", "node");
    node.asn1 = this;
    var head = DOM.tag("div", "head");
    var s = this.typeName().replace(/_/g, " ");
    head.innerHTML = s;
    var content = this.content();
    if (content !== null) {
        content = String(content).replace(/</g, "&lt;");
        var preview = DOM.tag("span", "preview");
        preview.appendChild(DOM.text(content));
        head.appendChild(preview);
    }
    node.appendChild(head);
    this.node = node;
    this.head = head;
    var value = DOM.tag("div", "value");
    s = "Offset: " + this.stream.pos + "<br/>";
    s += "Length: " + this.header + "+";
    if (this.length >= 0)
        s += this.length;
    else
        s += (-this.length) + " (undefined)";
    if (this.tag & 0x20)
        s += "<br/>(constructed)";
    else if (((this.tag == 0x03) || (this.tag == 0x04)) && (this.sub !== null))
        s += "<br/>(encapsulates)";
    //TODO if (this.tag == 0x03) s += "Unused bits: "
    if (content !== null) {
        s += "<br/>Value:<br/><b>" + content + "</b>";
        if ((typeof oids === 'object') && (this.tag == 0x06)) {
            var oid = oids[content];
            if (oid) {
                if (oid.d) s += "<br/>" + oid.d;
                if (oid.c) s += "<br/>" + oid.c;
                if (oid.w) s += "<br/>(warning!)";
            }
        }
    }
    value.innerHTML = s;
    node.appendChild(value);
    var sub = DOM.tag("div", "sub");
    if (this.sub !== null) {
        for (var i = 0, max = this.sub.length; i < max; ++i)
            sub.appendChild(this.sub[i].toDOM());
    }
    node.appendChild(sub);
    head.onclick = function () {
        node.className = (node.className == "node collapsed") ? "node" : "node collapsed";
    };
    return node;
};
ASN1.prototype.posStart = function () {
    return this.stream.pos;
};
ASN1.prototype.posContent = function () {
    return this.stream.pos + this.header;
};
ASN1.prototype.posEnd = function () {
    return this.stream.pos + this.header + Math.abs(this.length);
};
ASN1.prototype.fakeHover = function (current) {
    this.node.className += " hover";
    if (current)
        this.head.className += " hover";
};
ASN1.prototype.fakeOut = function (current) {
    var re = / ?hover/;
    this.node.className = this.node.className.replace(re, "");
    if (current)
        this.head.className = this.head.className.replace(re, "");
};
ASN1.prototype.toHexDOM_sub = function (node, className, stream, start, end) {
    if (start >= end)
        return;
    var sub = DOM.tag("span", className);
    sub.appendChild(DOM.text(
        stream.hexDump(start, end)));
    node.appendChild(sub);
};
ASN1.prototype.toHexDOM = function (root) {
    var node = DOM.tag("span", "hex");
    if (root === undefined) root = node;
    this.head.hexNode = node;
    this.head.onmouseover = function () { this.hexNode.className = "hexCurrent"; };
    this.head.onmouseout  = function () { this.hexNode.className = "hex"; };
    node.asn1 = this;
    node.onmouseover = function () {
        var current = !root.selected;
        if (current) {
            root.selected = this.asn1;
            this.className = "hexCurrent";
        }
        this.asn1.fakeHover(current);
    };
    node.onmouseout  = function () {
        var current = (root.selected == this.asn1);
        this.asn1.fakeOut(current);
        if (current) {
            root.selected = null;
            this.className = "hex";
        }
    };
    this.toHexDOM_sub(node, "tag", this.stream, this.posStart(), this.posStart() + 1);
    this.toHexDOM_sub(node, (this.length >= 0) ? "dlen" : "ulen", this.stream, this.posStart() + 1, this.posContent());
    if (this.sub === null)
        node.appendChild(DOM.text(
            this.stream.hexDump(this.posContent(), this.posEnd())));
    else if (this.sub.length > 0) {
        var first = this.sub[0];
        var last = this.sub[this.sub.length - 1];
        this.toHexDOM_sub(node, "intro", this.stream, this.posContent(), first.posStart());
        for (var i = 0, max = this.sub.length; i < max; ++i)
            node.appendChild(this.sub[i].toHexDOM(root));
        this.toHexDOM_sub(node, "outro", this.stream, last.posEnd(), this.posEnd());
    }
    return node;
};
ASN1.prototype.toHexString = function (root) {
    return this.stream.hexDump(this.posStart(), this.posEnd(), true);
};
ASN1.decodeLength = function (stream) {
    var buf = stream.get(),
        len = buf & 0x7F;
    if (len == buf)
        return len;
    if (len > 3)
        throw "Length over 24 bits not supported at position " + (stream.pos - 1);
    if (len === 0)
        return -1; // undefined
    buf = 0;
    for (var i = 0; i < len; ++i)
        buf = (buf << 8) | stream.get();
    return buf;
};
ASN1.hasContent = function (tag, len, stream) {
    if (tag & 0x20) // constructed
        return true;
    if ((tag < 0x03) || (tag > 0x04))
        return false;
    var p = new Stream(stream);
    if (tag == 0x03) p.get(); // BitString unused bits, must be in [0, 7]
    var subTag = p.get();
    if ((subTag >> 6) & 0x01) // not (universal or context)
        return false;
    try {
        var subLength = ASN1.decodeLength(p);
        return ((p.pos - stream.pos) + subLength == len);
    } catch (exception) {
        return false;
    }
};
ASN1.decode = function (stream) {
    if (!(stream instanceof Stream))
        stream = new Stream(stream, 0);
    var streamStart = new Stream(stream),
        tag = stream.get(),
        len = ASN1.decodeLength(stream),
        header = stream.pos - streamStart.pos,
        sub = null;
    if (ASN1.hasContent(tag, len, stream)) {
        // it has content, so we decode it
        var start = stream.pos;
        if (tag == 0x03) stream.get(); // skip BitString unused bits, must be in [0, 7]
        sub = [];
        if (len >= 0) {
            // definite length
            var end = start + len;
            while (stream.pos < end)
                sub[sub.length] = ASN1.decode(stream);
            if (stream.pos != end)
                throw "Content size is not correct for container starting at offset " + start;
        } else {
            // undefined length
            try {
                for (;;) {
                    var s = ASN1.decode(stream);
                    if (s.tag === 0)
                        break;
                    sub[sub.length] = s;
                }
                len = start - stream.pos;
            } catch (e) {
                throw "Exception while decoding undefined length content: " + e;
            }
        }
    } else
        stream.pos += len; // skip content
    return new ASN1(streamStart, header, len, tag, sub);
};
ASN1.test = function () {
    var test = [
        { value: [0x27],                   expected: 0x27     },
        { value: [0x81, 0xC9],             expected: 0xC9     },
        { value: [0x83, 0xFE, 0xDC, 0xBA], expected: 0xFEDCBA }
    ];
    for (var i = 0, max = test.length; i < max; ++i) {
        var pos = 0,
            stream = new Stream(test[i].value, 0),
            res = ASN1.decodeLength(stream);
        if (res != test[i].expected)
            document.write("In test[" + i + "] expected " + test[i].expected + " got " + res + "\n");
    }
};

// export globals
window.ASN1 = ASN1;
})();/**
 * Retrieve the hexadecimal value (as a string) of the current ASN.1 element
 * @returns {string}
 * @public
 */
ASN1.prototype.getHexStringValue = function () {
  var hexString = this.toHexString();
  var offset = this.header * 2;
  var length = this.length * 2;
  return hexString.substr(offset, length);
};

/**
 * Method to parse a pem encoded string containing both a public or private key.
 * The method will translate the pem encoded string in a der encoded string and
 * will parse private key and public key parameters. This method accepts public key
 * in the rsaencryption pkcs #1 format (oid: 1.2.840.113549.1.1.1).
 *
 * @todo Check how many rsa formats use the same format of pkcs #1.
 *
 * The format is defined as:
 * PublicKeyInfo ::= SEQUENCE {
 *   algorithm       AlgorithmIdentifier,
 *   PublicKey       BIT STRING
 * }
 * Where AlgorithmIdentifier is:
 * AlgorithmIdentifier ::= SEQUENCE {
 *   algorithm       OBJECT IDENTIFIER,     the OID of the enc algorithm
 *   parameters      ANY DEFINED BY algorithm OPTIONAL (NULL for PKCS #1)
 * }
 * and PublicKey is a SEQUENCE encapsulated in a BIT STRING
 * RSAPublicKey ::= SEQUENCE {
 *   modulus           INTEGER,  -- n
 *   publicExponent    INTEGER   -- e
 * }
 * it's possible to examine the structure of the keys obtained from openssl using
 * an asn.1 dumper as the one used here to parse the components: http://lapo.it/asn1js/
 * @argument {string} pem the pem encoded string, can include the BEGIN/END header/footer
 * @private
 */
RSAKey.prototype.parseKey = function (pem) {
  try {
    var modulus = 0;
    var public_exponent = 0;
    var reHex = /^\s*(?:[0-9A-Fa-f][0-9A-Fa-f]\s*)+$/;
    var der = reHex.test(pem) ? Hex.decode(pem) : Base64.unarmor(pem);
    var asn1 = ASN1.decode(der);

    //Fixes a bug with OpenSSL 1.0+ private keys
    if(asn1.sub.length === 3){
        asn1 = asn1.sub[2].sub[0];
    }
    if (asn1.sub.length === 9) {

      // Parse the private key.
      modulus = asn1.sub[1].getHexStringValue(); //bigint
      this.n = parseBigInt(modulus, 16);

      public_exponent = asn1.sub[2].getHexStringValue(); //int
      this.e = parseInt(public_exponent, 16);

      var private_exponent = asn1.sub[3].getHexStringValue(); //bigint
      this.d = parseBigInt(private_exponent, 16);

      var prime1 = asn1.sub[4].getHexStringValue(); //bigint
      this.p = parseBigInt(prime1, 16);

      var prime2 = asn1.sub[5].getHexStringValue(); //bigint
      this.q = parseBigInt(prime2, 16);

      var exponent1 = asn1.sub[6].getHexStringValue(); //bigint
      this.dmp1 = parseBigInt(exponent1, 16);

      var exponent2 = asn1.sub[7].getHexStringValue(); //bigint
      this.dmq1 = parseBigInt(exponent2, 16);

      var coefficient = asn1.sub[8].getHexStringValue(); //bigint
      this.coeff = parseBigInt(coefficient, 16);

    }
    else if (asn1.sub.length === 2) {

      // Parse the public key.
      var bit_string = asn1.sub[1];
      var sequence = bit_string.sub[0];

      modulus = sequence.sub[0].getHexStringValue();
      this.n = parseBigInt(modulus, 16);
      public_exponent = sequence.sub[1].getHexStringValue();
      this.e = parseInt(public_exponent, 16);

    }
    else {
      return false;
    }
    return true;
  }
  catch (ex) {
    return false;
  }
};

/**
 * Translate rsa parameters in a hex encoded string representing the rsa key.
 *
 * The translation follow the ASN.1 notation :
 * RSAPrivateKey ::= SEQUENCE {
 *   version           Version,
 *   modulus           INTEGER,  -- n
 *   publicExponent    INTEGER,  -- e
 *   privateExponent   INTEGER,  -- d
 *   prime1            INTEGER,  -- p
 *   prime2            INTEGER,  -- q
 *   exponent1         INTEGER,  -- d mod (p1)
 *   exponent2         INTEGER,  -- d mod (q-1)
 *   coefficient       INTEGER,  -- (inverse of q) mod p
 * }
 * @returns {string}  DER Encoded String representing the rsa private key
 * @private
 */
RSAKey.prototype.getPrivateBaseKey = function () {
  var options = {
    'array': [
      new KJUR.asn1.DERInteger({'int': 0}),
      new KJUR.asn1.DERInteger({'bigint': this.n}),
      new KJUR.asn1.DERInteger({'int': this.e}),
      new KJUR.asn1.DERInteger({'bigint': this.d}),
      new KJUR.asn1.DERInteger({'bigint': this.p}),
      new KJUR.asn1.DERInteger({'bigint': this.q}),
      new KJUR.asn1.DERInteger({'bigint': this.dmp1}),
      new KJUR.asn1.DERInteger({'bigint': this.dmq1}),
      new KJUR.asn1.DERInteger({'bigint': this.coeff})
    ]
  };
  var seq = new KJUR.asn1.DERSequence(options);
  return seq.getEncodedHex();
};

/**
 * base64 (pem) encoded version of the DER encoded representation
 * @returns {string} pem encoded representation without header and footer
 * @public
 */
RSAKey.prototype.getPrivateBaseKeyB64 = function () {
  return hex2b64(this.getPrivateBaseKey());
};

/**
 * Translate rsa parameters in a hex encoded string representing the rsa public key.
 * The representation follow the ASN.1 notation :
 * PublicKeyInfo ::= SEQUENCE {
 *   algorithm       AlgorithmIdentifier,
 *   PublicKey       BIT STRING
 * }
 * Where AlgorithmIdentifier is:
 * AlgorithmIdentifier ::= SEQUENCE {
 *   algorithm       OBJECT IDENTIFIER,     the OID of the enc algorithm
 *   parameters      ANY DEFINED BY algorithm OPTIONAL (NULL for PKCS #1)
 * }
 * and PublicKey is a SEQUENCE encapsulated in a BIT STRING
 * RSAPublicKey ::= SEQUENCE {
 *   modulus           INTEGER,  -- n
 *   publicExponent    INTEGER   -- e
 * }
 * @returns {string} DER Encoded String representing the rsa public key
 * @private
 */
RSAKey.prototype.getPublicBaseKey = function () {
  var options = {
    'array': [
      new KJUR.asn1.DERObjectIdentifier({'oid': '1.2.840.113549.1.1.1'}), //RSA Encryption pkcs #1 oid
      new KJUR.asn1.DERNull()
    ]
  };
  var first_sequence = new KJUR.asn1.DERSequence(options);

  options = {
    'array': [
      new KJUR.asn1.DERInteger({'bigint': this.n}),
      new KJUR.asn1.DERInteger({'int': this.e})
    ]
  };
  var second_sequence = new KJUR.asn1.DERSequence(options);

  options = {
    'hex': '00' + second_sequence.getEncodedHex()
  };
  var bit_string = new KJUR.asn1.DERBitString(options);

  options = {
    'array': [
      first_sequence,
      bit_string
    ]
  };
  var seq = new KJUR.asn1.DERSequence(options);
  return seq.getEncodedHex();
};

/**
 * base64 (pem) encoded version of the DER encoded representation
 * @returns {string} pem encoded representation without header and footer
 * @public
 */
RSAKey.prototype.getPublicBaseKeyB64 = function () {
  return hex2b64(this.getPublicBaseKey());
};

/**
 * wrap the string in block of width chars. The default value for rsa keys is 64
 * characters.
 * @param {string} str the pem encoded string without header and footer
 * @param {Number} [width=64] - the length the string has to be wrapped at
 * @returns {string}
 * @private
 */
RSAKey.prototype.wordwrap = function (str, width) {
  width = width || 64;
  if (!str) {
    return str;
  }
  var regex = '(.{1,' + width + '})( +|$\n?)|(.{1,' + width + '})';
  return str.match(RegExp(regex, 'g')).join('\n');
};

/**
 * Retrieve the pem encoded private key
 * @returns {string} the pem encoded private key with header/footer
 * @public
 */
RSAKey.prototype.getPrivateKey = function () {
  var key = "-----BEGIN RSA PRIVATE KEY-----\n";
  key += this.wordwrap(this.getPrivateBaseKeyB64()) + "\n";
  key += "-----END RSA PRIVATE KEY-----";
  return key;
};

/**
 * Retrieve the pem encoded public key
 * @returns {string} the pem encoded public key with header/footer
 * @public
 */
RSAKey.prototype.getPublicKey = function () {
  var key = "-----BEGIN PUBLIC KEY-----\n";
  key += this.wordwrap(this.getPublicBaseKeyB64()) + "\n";
  key += "-----END PUBLIC KEY-----";
  return key;
};

/**
 * Check if the object contains the necessary parameters to populate the rsa modulus
 * and public exponent parameters.
 * @param {Object} [obj={}] - An object that may contain the two public key
 * parameters
 * @returns {boolean} true if the object contains both the modulus and the public exponent
 * properties (n and e)
 * @todo check for types of n and e. N should be a parseable bigInt object, E should
 * be a parseable integer number
 * @private
 */
RSAKey.prototype.hasPublicKeyProperty = function (obj) {
  obj = obj || {};
  return (
    obj.hasOwnProperty('n') &&
    obj.hasOwnProperty('e')
  );
};

/**
 * Check if the object contains ALL the parameters of an RSA key.
 * @param {Object} [obj={}] - An object that may contain nine rsa key
 * parameters
 * @returns {boolean} true if the object contains all the parameters needed
 * @todo check for types of the parameters all the parameters but the public exponent
 * should be parseable bigint objects, the public exponent should be a parseable integer number
 * @private
 */
RSAKey.prototype.hasPrivateKeyProperty = function (obj) {
  obj = obj || {};
  return (
    obj.hasOwnProperty('n') &&
    obj.hasOwnProperty('e') &&
    obj.hasOwnProperty('d') &&
    obj.hasOwnProperty('p') &&
    obj.hasOwnProperty('q') &&
    obj.hasOwnProperty('dmp1') &&
    obj.hasOwnProperty('dmq1') &&
    obj.hasOwnProperty('coeff')
  );
};

/**
 * Parse the properties of obj in the current rsa object. Obj should AT LEAST
 * include the modulus and public exponent (n, e) parameters.
 * @param {Object} obj - the object containing rsa parameters
 * @private
 */
RSAKey.prototype.parsePropertiesFrom = function (obj) {
  this.n = obj.n;
  this.e = obj.e;

  if (obj.hasOwnProperty('d')) {
    this.d = obj.d;
    this.p = obj.p;
    this.q = obj.q;
    this.dmp1 = obj.dmp1;
    this.dmq1 = obj.dmq1;
    this.coeff = obj.coeff;
  }
};

/**
 * Create a new JSEncryptRSAKey that extends Tom Wu's RSA key object.
 * This object is just a decorator for parsing the key parameter
 * @param {string|Object} key - The key in string format, or an object containing
 * the parameters needed to build a RSAKey object.
 * @constructor
 */
var JSEncryptRSAKey = function (key) {
  // Call the super constructor.
  RSAKey.call(this);
  // If a key key was provided.
  if (key) {
    // If this is a string...
    if (typeof key === 'string') {
      this.parseKey(key);
    }
    else if (
      this.hasPrivateKeyProperty(key) ||
      this.hasPublicKeyProperty(key)
    ) {
      // Set the values for the key.
      this.parsePropertiesFrom(key);
    }
  }
};

// Derive from RSAKey.
JSEncryptRSAKey.prototype = new RSAKey();

// Reset the contructor.
JSEncryptRSAKey.prototype.constructor = JSEncryptRSAKey;


/**
 *
 * @param {Object} [options = {}] - An object to customize JSEncrypt behaviour
 * possible parameters are:
 * - default_key_size        {number}  default: 1024 the key size in bit
 * - default_public_exponent {string}  default: '010001' the hexadecimal representation of the public exponent
 * - log                     {boolean} default: false whether log warn/error or not
 * @constructor
 */
var JSEncrypt = function (options) {
  options = options || {};
  this.default_key_size = parseInt(options.default_key_size) || 1024;
  this.default_public_exponent = options.default_public_exponent || '010001'; //65537 default openssl public exponent for rsa key type
  this.log = options.log || false;
  // The private and public key.
  this.key = null;
};

/**
 * Method to set the rsa key parameter (one method is enough to set both the public
 * and the private key, since the private key contains the public key paramenters)
 * Log a warning if logs are enabled
 * @param {Object|string} key the pem encoded string or an object (with or without header/footer)
 * @public
 */
JSEncrypt.prototype.setKey = function (key) {
  if (this.log && this.key) {
    console.warn('A key was already set, overriding existing.');
  }
  this.key = new JSEncryptRSAKey(key);
};

/**
 * Proxy method for setKey, for api compatibility
 * @see setKey
 * @public
 */
JSEncrypt.prototype.setPrivateKey = function (privkey) {
  // Create the key.
  this.setKey(privkey);
};

/**
 * Proxy method for setKey, for api compatibility
 * @see setKey
 * @public
 */
JSEncrypt.prototype.setPublicKey = function (pubkey) {
  // Sets the public key.
  this.setKey(pubkey);
};

/**
 * Proxy method for RSAKey object's decrypt, decrypt the string using the private
 * components of the rsa key object. Note that if the object was not set will be created
 * on the fly (by the getKey method) using the parameters passed in the JSEncrypt constructor
 * @param {string} string base64 encoded crypted string to decrypt
 * @return {string} the decrypted string
 * @public
 */
JSEncrypt.prototype.decrypt = function (string) {
  // Return the decrypted string.
  try {
    return this.getKey().decrypt(b64tohex(string));
  }
  catch (ex) {
    return false;
  }
};

/**
 * Proxy method for RSAKey object's encrypt, encrypt the string using the public
 * components of the rsa key object. Note that if the object was not set will be created
 * on the fly (by the getKey method) using the parameters passed in the JSEncrypt constructor
 * @param {string} string the string to encrypt
 * @return {string} the encrypted string encoded in base64
 * @public
 */
JSEncrypt.prototype.encrypt = function (string) {
  // Return the encrypted string.
  try {
    return hex2b64(this.getKey().encrypt(string));
  }
  catch (ex) {
    return false;
  }
};

/**
 * Getter for the current JSEncryptRSAKey object. If it doesn't exists a new object
 * will be created and returned
 * @param {callback} [cb] the callback to be called if we want the key to be generated
 * in an async fashion
 * @returns {JSEncryptRSAKey} the JSEncryptRSAKey object
 * @public
 */
JSEncrypt.prototype.getKey = function (cb) {
  // Only create new if it does not exist.
  if (!this.key) {
    // Get a new private key.
    this.key = new JSEncryptRSAKey();
    if (cb && {}.toString.call(cb) === '[object Function]') {
      this.key.generateAsync(this.default_key_size, this.default_public_exponent, cb);
      return;
    }
    // Generate the key.
    this.key.generate(this.default_key_size, this.default_public_exponent);
  }
  return this.key;
};

/**
 * Returns the pem encoded representation of the private key
 * If the key doesn't exists a new key will be created
 * @returns {string} pem encoded representation of the private key WITH header and footer
 * @public
 */
JSEncrypt.prototype.getPrivateKey = function () {
  // Return the private representation of this key.
  return this.getKey().getPrivateKey();
};

/**
 * Returns the pem encoded representation of the private key
 * If the key doesn't exists a new key will be created
 * @returns {string} pem encoded representation of the private key WITHOUT header and footer
 * @public
 */
JSEncrypt.prototype.getPrivateKeyB64 = function () {
  // Return the private representation of this key.
  return this.getKey().getPrivateBaseKeyB64();
};


/**
 * Returns the pem encoded representation of the public key
 * If the key doesn't exists a new key will be created
 * @returns {string} pem encoded representation of the public key WITH header and footer
 * @public
 */
JSEncrypt.prototype.getPublicKey = function () {
  // Return the private representation of this key.
  return this.getKey().getPublicKey();
};

/**
 * Returns the pem encoded representation of the public key
 * If the key doesn't exists a new key will be created
 * @returns {string} pem encoded representation of the public key WITHOUT header and footer
 * @public
 */
JSEncrypt.prototype.getPublicKeyB64 = function () {
  // Return the private representation of this key.
  return this.getKey().getPublicBaseKeyB64();
};

exports.JSEncrypt = JSEncrypt;
})(JSEncryptExports);
var JSEncrypt = JSEncryptExports.JSEncrypt;

//

/*
    json2.js
    2015-05-03
    Public Domain.
    NO WARRANTY EXPRESSED OR IMPLIED. USE AT YOUR OWN RISK.
    See http://www.JSON.org/js.html
    This code should be minified before deployment.
    See http://javascript.crockford.com/jsmin.html
    USE YOUR OWN COPY. IT IS EXTREMELY UNWISE TO LOAD CODE FROM SERVERS YOU DO
    NOT CONTROL.
    This file creates a global JSON object containing two methods: stringify
    and parse. This file is provides the ES5 JSON capability to ES3 systems.
    If a project might run on IE8 or earlier, then this file should be included.
    This file does nothing on ES5 systems.
        JSON.stringify(value, replacer, space)
            value       any JavaScript value, usually an object or array.
            replacer    an optional parameter that determines how object
                        values are stringified for objects. It can be a
                        function or an array of strings.
            space       an optional parameter that specifies the indentation
                        of nested structures. If it is omitted, the text will
                        be packed without extra whitespace. If it is a number,
                        it will specify the number of spaces to indent at each
                        level. If it is a string (such as '\t' or '&nbsp;'),
                        it contains the characters used to indent at each level.
            This method produces a JSON text from a JavaScript value.
            When an object value is found, if the object contains a toJSON
            method, its toJSON method will be called and the result will be
            stringified. A toJSON method does not serialize: it returns the
            value represented by the name/value pair that should be serialized,
            or undefined if nothing should be serialized. The toJSON method
            will be passed the key associated with the value, and this will be
            bound to the value
            For example, this would serialize Dates as ISO strings.
                Date.prototype.toJSON = function (key) {
                    function f(n) {
                        // Format integers to have at least two digits.
                        return n < 10 
                            ? '0' + n 
                            : n;
                    }
                    return this.getUTCFullYear()   + '-' +
                         f(this.getUTCMonth() + 1) + '-' +
                         f(this.getUTCDate())      + 'T' +
                         f(this.getUTCHours())     + ':' +
                         f(this.getUTCMinutes())   + ':' +
                         f(this.getUTCSeconds())   + 'Z';
                };
            You can provide an optional replacer method. It will be passed the
            key and value of each member, with this bound to the containing
            object. The value that is returned from your method will be
            serialized. If your method returns undefined, then the member will
            be excluded from the serialization.
            If the replacer parameter is an array of strings, then it will be
            used to select the members to be serialized. It filters the results
            such that only members with keys listed in the replacer array are
            stringified.
            Values that do not have JSON representations, such as undefined or
            functions, will not be serialized. Such values in objects will be
            dropped; in arrays they will be replaced with null. You can use
            a replacer function to replace those with JSON values.
            JSON.stringify(undefined) returns undefined.
            The optional space parameter produces a stringification of the
            value that is filled with line breaks and indentation to make it
            easier to read.
            If the space parameter is a non-empty string, then that string will
            be used for indentation. If the space parameter is a number, then
            the indentation will be that many spaces.
            Example:
            text = JSON.stringify(['e', {pluribus: 'unum'}]);
            // text is '["e",{"pluribus":"unum"}]'
            text = JSON.stringify(['e', {pluribus: 'unum'}], null, '\t');
            // text is '[\n\t"e",\n\t{\n\t\t"pluribus": "unum"\n\t}\n]'
            text = JSON.stringify([new Date()], function (key, value) {
                return this[key] instanceof Date 
                    ? 'Date(' + this[key] + ')' 
                    : value;
            });
            // text is '["Date(---current time---)"]'
        JSON.parse(text, reviver)
            This method parses a JSON text to produce an object or array.
            It can throw a SyntaxError exception.
            The optional reviver parameter is a function that can filter and
            transform the results. It receives each of the keys and values,
            and its return value is used instead of the original value.
            If it returns what it received, then the structure is not modified.
            If it returns undefined then the member is deleted.
            Example:
            // Parse the text. Values that look like ISO date strings will
            // be converted to Date objects.
            myData = JSON.parse(text, function (key, value) {
                var a;
                if (typeof value === 'string') {
                    a =
/^(\d{4})-(\d{2})-(\d{2})T(\d{2}):(\d{2}):(\d{2}(?:\.\d*)?)Z$/.exec(value);
                    if (a) {
                        return new Date(Date.UTC(+a[1], +a[2] - 1, +a[3], +a[4],
                            +a[5], +a[6]));
                    }
                }
                return value;
            });
            myData = JSON.parse('["Date(09/09/2001)"]', function (key, value) {
                var d;
                if (typeof value === 'string' &&
                        value.slice(0, 5) === 'Date(' &&
                        value.slice(-1) === ')') {
                    d = new Date(value.slice(5, -1));
                    if (d) {
                        return d;
                    }
                }
                return value;
            });
    This is a reference implementation. You are free to copy, modify, or
    redistribute.
*/

/*jslint 
    eval, for, this 
*/

/*property
    JSON, apply, call, charCodeAt, getUTCDate, getUTCFullYear, getUTCHours,
    getUTCMinutes, getUTCMonth, getUTCSeconds, hasOwnProperty, join,
    lastIndex, length, parse, prototype, push, replace, slice, stringify,
    test, toJSON, toString, valueOf
*/


// Create a JSON object only if one does not already exist. We create the
// methods in a closure to avoid creating global variables.

if (typeof JSON !== 'object') {
    JSON = {};
}

(function () {
    'use strict';
    
    var rx_one = /^[\],:{}\s]*$/,
        rx_two = /\\(?:["\\\/bfnrt]|u[0-9a-fA-F]{4})/g,
        rx_three = /"[^"\\\n\r]*"|true|false|null|-?\d+(?:\.\d*)?(?:[eE][+\-]?\d+)?/g,
        rx_four = /(?:^|:|,)(?:\s*\[)+/g,
        rx_escapable = /[\\\"\u0000-\u001f\u007f-\u009f\u00ad\u0600-\u0604\u070f\u17b4\u17b5\u200c-\u200f\u2028-\u202f\u2060-\u206f\ufeff\ufff0-\uffff]/g,
        rx_dangerous = /[\u0000\u00ad\u0600-\u0604\u070f\u17b4\u17b5\u200c-\u200f\u2028-\u202f\u2060-\u206f\ufeff\ufff0-\uffff]/g;

    function f(n) {
        // Format integers to have at least two digits.
        return n < 10 
            ? '0' + n 
            : n;
    }
    
    function this_value() {
        return this.valueOf();
    }

    if (typeof Date.prototype.toJSON !== 'function') {

        Date.prototype.toJSON = function () {

            return isFinite(this.valueOf())
                ? this.getUTCFullYear() + '-' +
                        f(this.getUTCMonth() + 1) + '-' +
                        f(this.getUTCDate()) + 'T' +
                        f(this.getUTCHours()) + ':' +
                        f(this.getUTCMinutes()) + ':' +
                        f(this.getUTCSeconds()) + 'Z'
                : null;
        };

        Boolean.prototype.toJSON = this_value;
        Number.prototype.toJSON = this_value;
        String.prototype.toJSON = this_value;
    }

    var gap,
        indent,
        meta,
        rep;


    function quote(string) {

// If the string contains no control characters, no quote characters, and no
// backslash characters, then we can safely slap some quotes around it.
// Otherwise we must also replace the offending characters with safe escape
// sequences.

        rx_escapable.lastIndex = 0;
        return rx_escapable.test(string) 
            ? '"' + string.replace(rx_escapable, function (a) {
                var c = meta[a];
                return typeof c === 'string'
                    ? c
                    : '\\u' + ('0000' + a.charCodeAt(0).toString(16)).slice(-4);
            }) + '"' 
            : '"' + string + '"';
    }


    function str(key, holder) {

// Produce a string from holder[key].

        var i,          // The loop counter.
            k,          // The member key.
            v,          // The member value.
            length,
            mind = gap,
            partial,
            value = holder[key];

// If the value has a toJSON method, call it to obtain a replacement value.

        if (value && typeof value === 'object' &&
                typeof value.toJSON === 'function') {
            value = value.toJSON(key);
        }

// If we were called with a replacer function, then call the replacer to
// obtain a replacement value.

        if (typeof rep === 'function') {
            value = rep.call(holder, key, value);
        }

// What happens next depends on the value's type.

        switch (typeof value) {
        case 'string':
            return quote(value);

        case 'number':

// JSON numbers must be finite. Encode non-finite numbers as null.

            return isFinite(value) 
                ? String(value) 
                : 'null';

        case 'boolean':
        case 'null':

// If the value is a boolean or null, convert it to a string. Note:
// typeof null does not produce 'null'. The case is included here in
// the remote chance that this gets fixed someday.

            return String(value);

// If the type is 'object', we might be dealing with an object or an array or
// null.

        case 'object':

// Due to a specification blunder in ECMAScript, typeof null is 'object',
// so watch out for that case.

            if (!value) {
                return 'null';
            }

// Make an array to hold the partial results of stringifying this object value.

            gap += indent;
            partial = [];

// Is the value an array?

            if (Object.prototype.toString.apply(value) === '[object Array]') {

// The value is an array. Stringify every element. Use null as a placeholder
// for non-JSON values.

                length = value.length;
                for (i = 0; i < length; i += 1) {
                    partial[i] = str(i, value) || 'null';
                }

// Join all of the elements together, separated with commas, and wrap them in
// brackets.

                v = partial.length === 0
                    ? '[]'
                    : gap
                        ? '[\n' + gap + partial.join(',\n' + gap) + '\n' + mind + ']'
                        : '[' + partial.join(',') + ']';
                gap = mind;
                return v;
            }

// If the replacer is an array, use it to select the members to be stringified.

            if (rep && typeof rep === 'object') {
                length = rep.length;
                for (i = 0; i < length; i += 1) {
                    if (typeof rep[i] === 'string') {
                        k = rep[i];
                        v = str(k, value);
                        if (v) {
                            partial.push(quote(k) + (
                                gap 
                                    ? ': ' 
                                    : ':'
                            ) + v);
                        }
                    }
                }
            } else {

// Otherwise, iterate through all of the keys in the object.

                for (k in value) {
                    if (Object.prototype.hasOwnProperty.call(value, k)) {
                        v = str(k, value);
                        if (v) {
                            partial.push(quote(k) + (
                                gap 
                                    ? ': ' 
                                    : ':'
                            ) + v);
                        }
                    }
                }
            }

// Join all of the member texts together, separated with commas,
// and wrap them in braces.

            v = partial.length === 0
                ? '{}'
                : gap
                    ? '{\n' + gap + partial.join(',\n' + gap) + '\n' + mind + '}'
                    : '{' + partial.join(',') + '}';
            gap = mind;
            return v;
        }
    }

// If the JSON object does not yet have a stringify method, give it one.

    if (typeof JSON.stringify !== 'function') {
        meta = {    // table of character substitutions
            '\b': '\\b',
            '\t': '\\t',
            '\n': '\\n',
            '\f': '\\f',
            '\r': '\\r',
            '"': '\\"',
            '\\': '\\\\'
        };
        JSON.stringify = function (value, replacer, space) {

// The stringify method takes a value and an optional replacer, and an optional
// space parameter, and returns a JSON text. The replacer can be a function
// that can replace values, or an array of strings that will select the keys.
// A default replacer method can be provided. Use of the space parameter can
// produce text that is more easily readable.

            var i;
            gap = '';
            indent = '';

// If the space parameter is a number, make an indent string containing that
// many spaces.

            if (typeof space === 'number') {
                for (i = 0; i < space; i += 1) {
                    indent += ' ';
                }

// If the space parameter is a string, it will be used as the indent string.

            } else if (typeof space === 'string') {
                indent = space;
            }

// If there is a replacer, it must be a function or an array.
// Otherwise, throw an error.

            rep = replacer;
            if (replacer && typeof replacer !== 'function' &&
                    (typeof replacer !== 'object' ||
                    typeof replacer.length !== 'number')) {
                throw new Error('JSON.stringify');
            }

// Make a fake root object containing our value under the key of ''.
// Return the result of stringifying the value.

            return str('', {'': value});
        };
    }


// If the JSON object does not yet have a parse method, give it one.

    if (typeof JSON.parse !== 'function') {
        JSON.parse = function (text, reviver) {

// The parse method takes a text and an optional reviver function, and returns
// a JavaScript value if the text is a valid JSON text.

            var j;

            function walk(holder, key) {

// The walk method is used to recursively walk the resulting structure so
// that modifications can be made.

                var k, v, value = holder[key];
                if (value && typeof value === 'object') {
                    for (k in value) {
                        if (Object.prototype.hasOwnProperty.call(value, k)) {
                            v = walk(value, k);
                            if (v !== undefined) {
                                value[k] = v;
                            } else {
                                delete value[k];
                            }
                        }
                    }
                }
                return reviver.call(holder, key, value);
            }


// Parsing happens in four stages. In the first stage, we replace certain
// Unicode characters with escape sequences. JavaScript handles many characters
// incorrectly, either silently deleting them, or treating them as line endings.

            text = String(text);
            rx_dangerous.lastIndex = 0;
            if (rx_dangerous.test(text)) {
                text = text.replace(rx_dangerous, function (a) {
                    return '\\u' +
                            ('0000' + a.charCodeAt(0).toString(16)).slice(-4);
                });
            }

// In the second stage, we run the text against regular expressions that look
// for non-JSON patterns. We are especially concerned with '()' and 'new'
// because they can cause invocation, and '=' because it can cause mutation.
// But just to be safe, we want to reject all unexpected forms.

// We split the second stage into 4 regexp operations in order to work around
// crippling inefficiencies in IE's and Safari's regexp engines. First we
// replace the JSON backslash pairs with '@' (a non-JSON character). Second, we
// replace all simple value tokens with ']' characters. Third, we delete all
// open brackets that follow a colon or comma or that begin the text. Finally,
// we look to see that the remaining characters are only whitespace or ']' or
// ',' or ':' or '{' or '}'. If that is so, then the text is safe for eval.

            if (
                rx_one.test(
                    text
                        .replace(rx_two, '@')
                        .replace(rx_three, ']')
                        .replace(rx_four, '')
                )
            ) {

// In the third stage we use the eval function to compile the text into a
// JavaScript structure. The '{' operator is subject to a syntactic ambiguity
// in JavaScript: it can begin a block or an object literal. We wrap the text
// in parens to eliminate the ambiguity.

                j = eval('(' + text + ')');

// In the optional fourth stage, we recursively walk the new structure, passing
// each name/value pair to a reviver function for possible transformation.

                return typeof reviver === 'function'
                    ? walk({'': j}, '')
                    : j;
            }

// If the text is not JSON parseable, then a SyntaxError is thrown.

            throw new SyntaxError('JSON.parse');
        };
    }
}());

//

/*
CryptoJS_LSR v3.0.2
code.google.com/p/crypto-js
(c) 2009-2012 by Jeff Mott. All rights reserved.
code.google.com/p/crypto-js/wiki/License
*/
var CryptoJS_LSR=CryptoJS_LSR||function(p,h){var i={},l=i.lib={},r=l.Base=function(){function a(){}return{extend:function(e){a.prototype=this;var c=new a;e&&c.mixIn(e);c.$super=this;return c},create:function(){var a=this.extend();a.init.apply(a,arguments);return a},init:function(){},mixIn:function(a){for(var c in a)a.hasOwnProperty(c)&&(this[c]=a[c]);a.hasOwnProperty("toString")&&(this.toString=a.toString)},clone:function(){return this.$super.extend(this)}}}(),o=l.WordArray=r.extend({init:function(a,e){a=
this.words=a||[];this.sigBytes=e!=h?e:4*a.length},toString:function(a){return(a||s).stringify(this)},concat:function(a){var e=this.words,c=a.words,b=this.sigBytes,a=a.sigBytes;this.clamp();if(b%4)for(var d=0;d<a;d++)e[b+d>>>2]|=(c[d>>>2]>>>24-8*(d%4)&255)<<24-8*((b+d)%4);else if(65535<c.length)for(d=0;d<a;d+=4)e[b+d>>>2]=c[d>>>2];else e.push.apply(e,c);this.sigBytes+=a;return this},clamp:function(){var a=this.words,e=this.sigBytes;a[e>>>2]&=4294967295<<32-8*(e%4);a.length=p.ceil(e/4)},clone:function(){var a=
r.clone.call(this);a.words=this.words.slice(0);return a},random:function(a){for(var e=[],c=0;c<a;c+=4)e.push(4294967296*p.random()|0);return o.create(e,a)}}),m=i.enc={},s=m.Hex={stringify:function(a){for(var e=a.words,a=a.sigBytes,c=[],b=0;b<a;b++){var d=e[b>>>2]>>>24-8*(b%4)&255;c.push((d>>>4).toString(16));c.push((d&15).toString(16))}return c.join("")},parse:function(a){for(var e=a.length,c=[],b=0;b<e;b+=2)c[b>>>3]|=parseInt(a.substr(b,2),16)<<24-4*(b%8);return o.create(c,e/2)}},n=m.Latin1={stringify:function(a){for(var e=
a.words,a=a.sigBytes,c=[],b=0;b<a;b++)c.push(String.fromCharCode(e[b>>>2]>>>24-8*(b%4)&255));return c.join("")},parse:function(a){for(var e=a.length,c=[],b=0;b<e;b++)c[b>>>2]|=(a.charCodeAt(b)&255)<<24-8*(b%4);return o.create(c,e)}},k=m.Utf8={stringify:function(a){try{return decodeURIComponent(escape(n.stringify(a)))}catch(e){throw Error("Malformed UTF-8 data");}},parse:function(a){return n.parse(unescape(encodeURIComponent(a)))}},f=l.BufferedBlockAlgorithm=r.extend({reset:function(){this._data=o.create();
this._nDataBytes=0},_append:function(a){"string"==typeof a&&(a=k.parse(a));this._data.concat(a);this._nDataBytes+=a.sigBytes},_process:function(a){var e=this._data,c=e.words,b=e.sigBytes,d=this.blockSize,q=b/(4*d),q=a?p.ceil(q):p.max((q|0)-this._minBufferSize,0),a=q*d,b=p.min(4*a,b);if(a){for(var j=0;j<a;j+=d)this._doProcessBlock(c,j);j=c.splice(0,a);e.sigBytes-=b}return o.create(j,b)},clone:function(){var a=r.clone.call(this);a._data=this._data.clone();return a},_minBufferSize:0});l.Hasher=f.extend({init:function(){this.reset()},
reset:function(){f.reset.call(this);this._doReset()},update:function(a){this._append(a);this._process();return this},finalize:function(a){a&&this._append(a);this._doFinalize();return this._hash},clone:function(){var a=f.clone.call(this);a._hash=this._hash.clone();return a},blockSize:16,_createHelper:function(a){return function(e,c){return a.create(c).finalize(e)}},_createHmacHelper:function(a){return function(e,c){return g.HMAC.create(a,c).finalize(e)}}});var g=i.algo={};return i}(Math);
(function(){var p=CryptoJS_LSR,h=p.lib.WordArray;p.enc.Base64={stringify:function(i){var l=i.words,h=i.sigBytes,o=this._map;i.clamp();for(var i=[],m=0;m<h;m+=3)for(var s=(l[m>>>2]>>>24-8*(m%4)&255)<<16|(l[m+1>>>2]>>>24-8*((m+1)%4)&255)<<8|l[m+2>>>2]>>>24-8*((m+2)%4)&255,n=0;4>n&&m+0.75*n<h;n++)i.push(o.charAt(s>>>6*(3-n)&63));if(l=o.charAt(64))for(;i.length%4;)i.push(l);return i.join("")},parse:function(i){var i=i.replace(/\s/g,""),l=i.length,r=this._map,o=r.charAt(64);o&&(o=i.indexOf(o),-1!=o&&(l=o));
for(var o=[],m=0,s=0;s<l;s++)if(s%4){var n=r.indexOf(i.charAt(s-1))<<2*(s%4),k=r.indexOf(i.charAt(s))>>>6-2*(s%4);o[m>>>2]|=(n|k)<<24-8*(m%4);m++}return h.create(o,m)},_map:"ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/="}})();
(function(p){function h(f,g,a,e,c,b,d){f=f+(g&a|~g&e)+c+d;return(f<<b|f>>>32-b)+g}function i(f,g,a,e,c,b,d){f=f+(g&e|a&~e)+c+d;return(f<<b|f>>>32-b)+g}function l(f,g,a,e,c,b,d){f=f+(g^a^e)+c+d;return(f<<b|f>>>32-b)+g}function r(f,g,a,e,c,b,d){f=f+(a^(g|~e))+c+d;return(f<<b|f>>>32-b)+g}var o=CryptoJS_LSR,m=o.lib,s=m.WordArray,m=m.Hasher,n=o.algo,k=[];(function(){for(var f=0;64>f;f++)k[f]=4294967296*p.abs(p.sin(f+1))|0})();n=n.MD5=m.extend({_doReset:function(){this._hash=s.create([1732584193,4023233417,
2562383102,271733878])},_doProcessBlock:function(f,g){for(var a=0;16>a;a++){var e=g+a,c=f[e];f[e]=(c<<8|c>>>24)&16711935|(c<<24|c>>>8)&4278255360}for(var e=this._hash.words,c=e[0],b=e[1],d=e[2],q=e[3],a=0;64>a;a+=4)16>a?(c=h(c,b,d,q,f[g+a],7,k[a]),q=h(q,c,b,d,f[g+a+1],12,k[a+1]),d=h(d,q,c,b,f[g+a+2],17,k[a+2]),b=h(b,d,q,c,f[g+a+3],22,k[a+3])):32>a?(c=i(c,b,d,q,f[g+(a+1)%16],5,k[a]),q=i(q,c,b,d,f[g+(a+6)%16],9,k[a+1]),d=i(d,q,c,b,f[g+(a+11)%16],14,k[a+2]),b=i(b,d,q,c,f[g+a%16],20,k[a+3])):48>a?(c=
l(c,b,d,q,f[g+(3*a+5)%16],4,k[a]),q=l(q,c,b,d,f[g+(3*a+8)%16],11,k[a+1]),d=l(d,q,c,b,f[g+(3*a+11)%16],16,k[a+2]),b=l(b,d,q,c,f[g+(3*a+14)%16],23,k[a+3])):(c=r(c,b,d,q,f[g+3*a%16],6,k[a]),q=r(q,c,b,d,f[g+(3*a+7)%16],10,k[a+1]),d=r(d,q,c,b,f[g+(3*a+14)%16],15,k[a+2]),b=r(b,d,q,c,f[g+(3*a+5)%16],21,k[a+3]));e[0]=e[0]+c|0;e[1]=e[1]+b|0;e[2]=e[2]+d|0;e[3]=e[3]+q|0},_doFinalize:function(){var f=this._data,g=f.words,a=8*this._nDataBytes,e=8*f.sigBytes;g[e>>>5]|=128<<24-e%32;g[(e+64>>>9<<4)+14]=(a<<8|a>>>
24)&16711935|(a<<24|a>>>8)&4278255360;f.sigBytes=4*(g.length+1);this._process();f=this._hash.words;for(g=0;4>g;g++)a=f[g],f[g]=(a<<8|a>>>24)&16711935|(a<<24|a>>>8)&4278255360}});o.MD5=m._createHelper(n);o.HmacMD5=m._createHmacHelper(n)})(Math);
(function(){var p=CryptoJS_LSR,h=p.lib,i=h.Base,l=h.WordArray,h=p.algo,r=h.EvpKDF=i.extend({cfg:i.extend({keySize:4,hasher:h.MD5,iterations:1}),init:function(i){this.cfg=this.cfg.extend(i)},compute:function(i,m){for(var h=this.cfg,n=h.hasher.create(),k=l.create(),f=k.words,g=h.keySize,h=h.iterations;f.length<g;){a&&n.update(a);var a=n.update(i).finalize(m);n.reset();for(var e=1;e<h;e++)a=n.finalize(a),n.reset();k.concat(a)}k.sigBytes=4*g;return k}});p.EvpKDF=function(i,l,h){return r.create(h).compute(i,
l)}})();
CryptoJS_LSR.lib.Cipher||function(p){var h=CryptoJS_LSR,i=h.lib,l=i.Base,r=i.WordArray,o=i.BufferedBlockAlgorithm,m=h.enc.Base64,s=h.algo.EvpKDF,n=i.Cipher=o.extend({cfg:l.extend(),createEncryptor:function(b,d){return this.create(this._ENC_XFORM_MODE,b,d)},createDecryptor:function(b,d){return this.create(this._DEC_XFORM_MODE,b,d)},init:function(b,d,a){this.cfg=this.cfg.extend(a);this._xformMode=b;this._key=d;this.reset()},reset:function(){o.reset.call(this);this._doReset()},process:function(b){this._append(b);return this._process()},
finalize:function(b){b&&this._append(b);return this._doFinalize()},keySize:4,ivSize:4,_ENC_XFORM_MODE:1,_DEC_XFORM_MODE:2,_createHelper:function(){return function(b){return{encrypt:function(a,q,j){return("string"==typeof q?c:e).encrypt(b,a,q,j)},decrypt:function(a,q,j){return("string"==typeof q?c:e).decrypt(b,a,q,j)}}}}()});i.StreamCipher=n.extend({_doFinalize:function(){return this._process(!0)},blockSize:1});var k=h.mode={},f=i.BlockCipherMode=l.extend({createEncryptor:function(b,a){return this.Encryptor.create(b,
a)},createDecryptor:function(b,a){return this.Decryptor.create(b,a)},init:function(b,a){this._cipher=b;this._iv=a}}),k=k.CBC=function(){function b(b,a,d){var c=this._iv;c?this._iv=p:c=this._prevBlock;for(var e=0;e<d;e++)b[a+e]^=c[e]}var a=f.extend();a.Encryptor=a.extend({processBlock:function(a,d){var c=this._cipher,e=c.blockSize;b.call(this,a,d,e);c.encryptBlock(a,d);this._prevBlock=a.slice(d,d+e)}});a.Decryptor=a.extend({processBlock:function(a,d){var c=this._cipher,e=c.blockSize,f=a.slice(d,d+
e);c.decryptBlock(a,d);b.call(this,a,d,e);this._prevBlock=f}});return a}(),g=(h.pad={}).Pkcs7={pad:function(b,a){for(var c=4*a,c=c-b.sigBytes%c,e=c<<24|c<<16|c<<8|c,f=[],g=0;g<c;g+=4)f.push(e);c=r.create(f,c);b.concat(c)},unpad:function(b){b.sigBytes-=b.words[b.sigBytes-1>>>2]&255}};i.BlockCipher=n.extend({cfg:n.cfg.extend({mode:k,padding:g}),reset:function(){n.reset.call(this);var b=this.cfg,a=b.iv,b=b.mode;if(this._xformMode==this._ENC_XFORM_MODE)var c=b.createEncryptor;else c=b.createDecryptor,
this._minBufferSize=1;this._mode=c.call(b,this,a&&a.words)},_doProcessBlock:function(b,a){this._mode.processBlock(b,a)},_doFinalize:function(){var b=this.cfg.padding;if(this._xformMode==this._ENC_XFORM_MODE){b.pad(this._data,this.blockSize);var a=this._process(!0)}else a=this._process(!0),b.unpad(a);return a},blockSize:4});var a=i.CipherParams=l.extend({init:function(a){this.mixIn(a)},toString:function(a){return(a||this.formatter).stringify(this)}}),k=(h.format={}).OpenSSL={stringify:function(a){var d=
a.ciphertext,a=a.salt,d=(a?r.create([1398893684,1701076831]).concat(a).concat(d):d).toString(m);return d=d.replace(/(.{64})/g,"$1\n")},parse:function(b){var b=m.parse(b),d=b.words;if(1398893684==d[0]&&1701076831==d[1]){var c=r.create(d.slice(2,4));d.splice(0,4);b.sigBytes-=16}return a.create({ciphertext:b,salt:c})}},e=i.SerializableCipher=l.extend({cfg:l.extend({format:k}),encrypt:function(b,d,c,e){var e=this.cfg.extend(e),f=b.createEncryptor(c,e),d=f.finalize(d),f=f.cfg;return a.create({ciphertext:d,
key:c,iv:f.iv,algorithm:b,mode:f.mode,padding:f.padding,blockSize:b.blockSize,formatter:e.format})},decrypt:function(a,c,e,f){f=this.cfg.extend(f);c=this._parse(c,f.format);return a.createDecryptor(e,f).finalize(c.ciphertext)},_parse:function(a,c){return"string"==typeof a?c.parse(a):a}}),h=(h.kdf={}).OpenSSL={compute:function(b,c,e,f){f||(f=r.random(8));b=s.create({keySize:c+e}).compute(b,f);e=r.create(b.words.slice(c),4*e);b.sigBytes=4*c;return a.create({key:b,iv:e,salt:f})}},c=i.PasswordBasedCipher=
e.extend({cfg:e.cfg.extend({kdf:h}),encrypt:function(a,c,f,j){j=this.cfg.extend(j);f=j.kdf.compute(f,a.keySize,a.ivSize);j.iv=f.iv;a=e.encrypt.call(this,a,c,f.key,j);a.mixIn(f);return a},decrypt:function(a,c,f,j){j=this.cfg.extend(j);c=this._parse(c,j.format);f=j.kdf.compute(f,a.keySize,a.ivSize,c.salt);j.iv=f.iv;return e.decrypt.call(this,a,c,f.key,j)}})}();
(function(){var p=CryptoJS_LSR,h=p.lib.BlockCipher,i=p.algo,l=[],r=[],o=[],m=[],s=[],n=[],k=[],f=[],g=[],a=[];(function(){for(var c=[],b=0;256>b;b++)c[b]=128>b?b<<1:b<<1^283;for(var d=0,e=0,b=0;256>b;b++){var j=e^e<<1^e<<2^e<<3^e<<4,j=j>>>8^j&255^99;l[d]=j;r[j]=d;var i=c[d],h=c[i],p=c[h],t=257*c[j]^16843008*j;o[d]=t<<24|t>>>8;m[d]=t<<16|t>>>16;s[d]=t<<8|t>>>24;n[d]=t;t=16843009*p^65537*h^257*i^16843008*d;k[j]=t<<24|t>>>8;f[j]=t<<16|t>>>16;g[j]=t<<8|t>>>24;a[j]=t;d?(d=i^c[c[c[p^i]]],e^=c[c[e]]):d=e=1}})();
var e=[0,1,2,4,8,16,32,64,128,27,54],i=i.AES=h.extend({_doReset:function(){for(var c=this._key,b=c.words,d=c.sigBytes/4,c=4*((this._nRounds=d+6)+1),i=this._keySchedule=[],j=0;j<c;j++)if(j<d)i[j]=b[j];else{var h=i[j-1];j%d?6<d&&4==j%d&&(h=l[h>>>24]<<24|l[h>>>16&255]<<16|l[h>>>8&255]<<8|l[h&255]):(h=h<<8|h>>>24,h=l[h>>>24]<<24|l[h>>>16&255]<<16|l[h>>>8&255]<<8|l[h&255],h^=e[j/d|0]<<24);i[j]=i[j-d]^h}b=this._invKeySchedule=[];for(d=0;d<c;d++)j=c-d,h=d%4?i[j]:i[j-4],b[d]=4>d||4>=j?h:k[l[h>>>24]]^f[l[h>>>
16&255]]^g[l[h>>>8&255]]^a[l[h&255]]},encryptBlock:function(a,b){this._doCryptBlock(a,b,this._keySchedule,o,m,s,n,l)},decryptBlock:function(c,b){var d=c[b+1];c[b+1]=c[b+3];c[b+3]=d;this._doCryptBlock(c,b,this._invKeySchedule,k,f,g,a,r);d=c[b+1];c[b+1]=c[b+3];c[b+3]=d},_doCryptBlock:function(a,b,d,e,f,h,i,g){for(var l=this._nRounds,k=a[b]^d[0],m=a[b+1]^d[1],o=a[b+2]^d[2],n=a[b+3]^d[3],p=4,r=1;r<l;r++)var s=e[k>>>24]^f[m>>>16&255]^h[o>>>8&255]^i[n&255]^d[p++],u=e[m>>>24]^f[o>>>16&255]^h[n>>>8&255]^
i[k&255]^d[p++],v=e[o>>>24]^f[n>>>16&255]^h[k>>>8&255]^i[m&255]^d[p++],n=e[n>>>24]^f[k>>>16&255]^h[m>>>8&255]^i[o&255]^d[p++],k=s,m=u,o=v;s=(g[k>>>24]<<24|g[m>>>16&255]<<16|g[o>>>8&255]<<8|g[n&255])^d[p++];u=(g[m>>>24]<<24|g[o>>>16&255]<<16|g[n>>>8&255]<<8|g[k&255])^d[p++];v=(g[o>>>24]<<24|g[n>>>16&255]<<16|g[k>>>8&255]<<8|g[m&255])^d[p++];n=(g[n>>>24]<<24|g[k>>>16&255]<<16|g[m>>>8&255]<<8|g[o&255])^d[p++];a[b]=s;a[b+1]=u;a[b+2]=v;a[b+3]=n},keySize:8});p.AES=h._createHelper(i)})();

//

var rv = -1;//瀏覽器版本
// keys for encrypt/decrypt
var RSACryptPubkey = "-----BEGIN PUBLIC KEY-----MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA0mNm5YJjWTx8hyhhJ2PfhlmEfmYhT0XSkydgkPQOKklh+YfIhkf1RWzNrFZB3ZwHu8R1vIqG4hDujRNsFsjcmFz8eaW1AkOA5YGIueGYuG3IBsCztCe5NtG4+sOqQbuv6fIzrqkMdqjpqHLkxaTxNFc1ZUPByUVdCW71e3bH012h8cEzJ1cv1QSe7p4BSVnBMYd8ozHoKffOgabbOlMU8pJPwGA6yHg8wpZFudUyRLX4PsKO84ds2uGk3W88CCCWEzMmyE3vM2z49Qz/dWxGHZ6BkpLtgbKQGh7k3sjH3ze6IdbqLvPdcOCY52s25ef3E66qEFvTc82AWQaA1Gy1DQIDAQAB-----END PUBLIC KEY-----";
var strEncryptSessionkey = 'HuykUJW+yVuqngM+YIZUVFgSX77yaVh30oQTS+id+8tvtUHiCVz8sZuLH73gZlvuXslwde0qfOqQcAtyVDR1PS1/UsUzzJR2dN1rk/QgC6iM2+u2q3+HZyZ3S4OcYvzbQTXPQURZX/CP5XlR6yWKBIVscKY/rjaznDQEc37PmOk0wDc5fc7OMj3xC315Hrgi1WkQhJUX5e82oTi60tkBGZ1Lrc9wlj8yPNmDRj2ciLlmmGsQkY01XbDUmsBeII1OpkOzOVNUgm88psF8NbnbPLEsuXzg4067Zq+pkzECMd8EOuOChPn1ahW2/XnOSOL8/GKLxvL9CyCuq1Poqg5sjA==';
var aesKey = '3131313131313131313131313131313131313131313131313131313131313131'; //sessionkey明文, 正式版應該由後台取得
var aesIv = '';
//var localServer_UUID = "3d843c25-a42d-4d4a-992d-475ea9937c9c";
var localServer_UUID = "b2df145f-30e9-4ed3-b56e-7cc2f8fc3b9d";

// local server status
var pluginVersion = '';
var initConnectSuccess = false;
var pluginPorts = ['16006', '16007', '16008', '16009', '16010'];
var pluginPortIndex = 0;


// RA web info

//Larry 20180122
//var RAWEBurl = "http://172.20.10.2:9003";
var RAWEBurl = "http://127.0.0.1:80";

var formName = 'frmmain';
var ServletPathESECURE = RAWEBurl + '/RAWeb/dealTrans.do';

var base64EncodeChars = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/';
var base64DecodeChars = new Array(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, -1, -1, 63,
    52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14,
    15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40,
    41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1, -1, -1, -1);


//if (window.navigator.userAgent.indexOf("Edge") > -1) {
//    pluginPorts = ['16001', '16002', '16003', '16004', '16005', '16001', '16002', '16003', '16004', '16005'];
//}

function getCertServerUrl() {
    return 'https://127.0.0.1:' + pluginPorts[pluginPortIndex] + '/CHTCertTokenServer/';
}

//Larry 20180205
var success = 1;
function getsuccess(){
	return success;
}
var dataret = 0;
function load() {
    //判斷IE
  
	
  var ua = navigator.userAgent;
  if (navigator.appName == 'Microsoft Internet Explorer'){
    var re  = new RegExp("MSIE ([0-9]{1,}[\.0-9]{0,})");
    if (re.exec(ua) != null)
      rv = parseFloat( RegExp.$1 );
      var agentWeb = 'Microsoft webgolds';
  } else if (navigator.appName == 'Netscape'){
    var re  = new RegExp("Trident/.*rv:([0-9]{1,}[\.0-9]{0,})");  //for IE 11
    if (re.exec(ua) != null)
      rv = parseFloat( RegExp.$1 );
  }
  
  //Larry 20180319
  //if(rv<=9&&rv>=8)
  //{
	 /* var obj=document.createElement('object');
obj.setAttribute('id','plugin0');
obj.setAttribute('type','application/x-ctbccryptoapi');
document.body.appendChild(obj);*/
	 // document.write ("<script language='javascript' id='oldJS' src='fbtlEcomMwSecEX_zh-tw.js'></script>");
/*	  var element=document.createElement('script');
element.setAttribute('src', 'fbtlEcomMwSecEX_zh-tw.js');
document.body.appendChild(element);*/

  //}else
  //{
//end判斷IE
	  //console.log("in load");
	  
    doRSAEncrypt();
    initPort({
        onSuccess: function (responseText) {
            try {
                if (responseText) {
                    var data = JSON.parse(responseText);
                    if (data.uuid != localServer_UUID) { //非綁定localServer，因此仍要繼續掃port
                        return -1;
                    }
                    if (data.ret == 0x0) { //網頁連線成功後，自動執行initConnect
                        //alert('Plugin Port:' + pluginPorts[pluginPortIndex]);
                    	success = 0;
                    	$.unblockUI();
                        initConnect();
						
						//Larry 0907
                        return 0;
                    } else {
                    	//console.log("data.ret="+data.ret);
						dataret = data.ret;
                        ShowErrorMessage(data.ret);
                        $.unblockUI();
						
						//Larry 0907
                        return 0;

                    }
                } else {
                    alert('responseText is empty');
                    return -1;
                }
            } catch (err) { //使用IE，如果port被其他程式佔去，可能會回404，因此仍要繼續掃port
            	//修正unblockUI無法正常運行錯誤
            	if(err.message=='$.unblockUI is not a function'){
            		$(".blockUI").fadeOut();
            	}
            	return -1;
            }
            if (responseText) {
                var data = JSON.parse(responseText);
                if (data.ret == 0x0) { //網頁連線成功後，自動執行initConnect
                    initConnect();
                } else {
                    ShowErrorMessage(data.ret);
                }
            } else {
                alert('responseText is empty');
            }
            try{
            	$.unblockUI();
            }catch(err){
            	$(".blockUI").fadeOut();
            }
            
        },
        onError: function (errMsg) {
            alert(errMsg);
            try{
            	$.unblockUI();
            }catch(err){
            	$(".blockUI").fadeOut();
            }
			
			//Larry 0907
			window.location.href="./Envdownload.jsp?lang=0";
        }
    });
    
  //}

}

function initPort(callbacks) {
    callbacks = callbacks || {};
    var onSuccessCallback = callbacks.onSuccess || function () { };
    var onErrorCallback = callbacks.onError || function () { };
    var result;
    $.blockUI({ fadeIn: 0 });
	
	//Larry 0907
    if (pluginPortIndex == pluginPorts.length) {
		//Larry 1018 language寫死
		onErrorCallback("請先安裝新版CTBC網銀交易元件");
		
        
        result = 0;
    }

    sendHttpRequest(getCertServerUrl() + 'checkport', 'POST', '', {
        onSuccess: function (responseText) {
            result = onSuccessCallback(responseText);
            if (result != 0) {
                pluginPortIndex++;
                initPort(callbacks);
            }
        },
        onError: function () {
            pluginPortIndex++;
            initPort(callbacks);
        }
    }, false);
}

//若需更新sessionkey，須再執行一次initConnect
function initConnect() {
    $.blockUI({ fadeIn: 0 });
    var request = {
        func: 'initConnect',
        strInitData: strEncryptSessionkey
    };

    sendHttpRequest(getCertServerUrl(), 'POST', JSON.stringify(request), {
        onSuccess: function (responseText) {
            if (responseText) {
                var data = JSON.parse(responseText);
                if (data.ret == 0x0) { //連線初始化成功
                    initConnectSuccess = true;
                } else {
                    ShowErrorMessage(data.ret);
                }
            }

            $.unblockUI();
        }
    }, false);
}

function doRSAEncrypt(cipherText) {
    var encrypt = new JSEncrypt();
    encrypt.setPublicKey(RSACryptPubkey);
    var encrypted = encrypt.encrypt(aesKey);
    strEncryptSessionkey = encrypted;
}

function doAESEncrypt(plainText) {
    var key = CryptoJS_LSR.enc.Hex.parse(aesKey);
    aesIv = "8be168d2e0071f67ed6f30127f5998d2";
    var iv = CryptoJS_LSR.enc.Hex.parse(aesIv);
    var encrypted = CryptoJS_LSR.AES.encrypt(plainText, key, { iv: iv });
    return encrypted;
}

function doAESDecrypt(cipherText) {
    var key = CryptoJS_LSR.enc.Hex.parse(aesKey);
    aesIv = "8be168d2e0071f67ed6f30127f5998d2";
    var iv = CryptoJS_LSR.enc.Hex.parse(aesIv);
    var decrypted = CryptoJS_LSR.AES.decrypt(cipherText, key, { iv: iv });
    return decrypted.toString(CryptoJS_LSR.enc.Utf8);
}

function sendHttpRequest(url, action, sendStr, callbacks, shouldEncrypt) {
    callbacks = callbacks || {};
    var onSuccessCallback = callbacks.onSuccess || function () { };
    var onErrorCallback = callbacks.onError || function () { };
    var xmlhttp = new XMLHttpRequest();
    try {
        xmlhttp.open(action, url, true);
        //跨瀏覽器元件掃port需要設定timeout
        //避免使用者有個localhost剛剛好用了同個port同個path 過了十分鐘才return response
        if (url.indexOf('checkport') !== -1) {
            xmlhttp.timeout = 5000;
        }
        xmlhttp.onload = function () {
            var responseText = xmlhttp.responseText;
            if (shouldEncrypt) {
                var data = JSON.parse(responseText);
                var nRc = data.ret;
                if (nRc == 0x0) {
                    responseText = doAESDecrypt(data.content);
                    onSuccessCallback(responseText);
                } else {
                    onErrorCallback(data.ret, data);
					
					//Larry 0907
                    //$.unblockUI();
                }
            } else {
                onSuccessCallback(responseText);
            }
        };

        xmlhttp.onerror = function () {
            sendHttpRequestRetry(url, action, sendStr, callbacks, shouldEncrypt);
            //onErrorCallback();
        };

        if (shouldEncrypt) {
            var cipherText = doAESEncrypt(sendStr);
            sendStr = "{\"func\":\"Connected\",\"content\":\"" + cipherText + "\"}";
        }

        xmlhttp.send(sendStr);
    } catch (err) {
        //處理localServer http連線錯誤狀況
        if (!xmlhttp && typeof xmlhttp != "undefined" && xmlhttp != 0) {
            alert("readyState:" + xmlhttp.readyState + ",status:" + xmlhttp.status + ",err:" + err.message);
        } else {
			//Larry 0907
            //console.log("err:" + err.message);
        }

        $.unblockUI();
    }
}

function sendHttpRequestRetry(url, action, sendStr, callbacks, shouldEncrypt) {
    callbacks = callbacks || {};
    var onSuccessCallback = callbacks.onSuccess || function () { };
    var onErrorCallback = callbacks.onError || function () { };
    var xmlhttp = new XMLHttpRequest();
    try {
        xmlhttp.open(action, url, true);
        //跨瀏覽器元件掃port需要設定timeout
        //避免使用者有個localhost剛剛好用了同個port同個path 過了十分鐘才return response
        if (url.indexOf('checkport') !== -1) {
            xmlhttp.timeout = 5000;
        }
        xmlhttp.onload = function () {
            var responseText = xmlhttp.responseText;
            if (shouldEncrypt) {
                var data = JSON.parse(responseText);
                var nRc = data.ret;
                if (nRc == 0x0) {
                    responseText = doAESDecrypt(data.content);
                    onSuccessCallback(responseText);
                } else {
                    onErrorCallback(data.ret, data);
					
					//Larry 0907
                    //$.unblockUI();
                }
            } else {
                onSuccessCallback(responseText);
            }
        };

        xmlhttp.onerror = function () {
            onErrorCallback();
            
			//Larry 0907
            //$.unblockUI();
        };

        if (shouldEncrypt) {
            var cipherText = doAESEncrypt(sendStr);
            sendStr = "{\"func\":\"Connected\",\"content\":\"" + cipherText + "\"}";
        }

        xmlhttp.send(sendStr);
    } catch (err) {
        //處理localServer http連線錯誤狀況
        if (!xmlhttp && typeof xmlhttp != "undefined" && xmlhttp != 0) {
            alert("readyState:" + xmlhttp.readyState + ",status:" + xmlhttp.status + ",err:" + err.message);
        } else {
            alert("err:" + err.message);
        }

        $.unblockUI();
    }
}

function ShowErrorMessage(nRc, data) {
	//Larry 1018 language寫死
    alert(errorCode2Message(nRc, data));
}

function determineLanguage() {
	//Larry 1018 language寫死
    var lang = 0;
       
    return lang;
}

function printLanguage(where, lang) {
    var DEBUG = true;
    if(DEBUG) {
        //console.log("DEBUG - [" + where + ']' + ' / lang = ' + lang);
    }
}

//

var comErrormessage="";
var CTBC_FB = document.getElementById("CTBCPKIAPI");
var ReturnValue = CTBC_FB.version;

//console.log("CTBC_FB.version = "+ReturnValue);

//if(navigator.appName == 'Microsoft Internet Explorer'){
if(typeof ReturnValue != "undefined"){

  	//console.log("errormsg: ActiveXObject");
   
  	comErrormessage = "activex";

}else{ //localserver
	//console.log("errormsg: localserver");
	comErrormessage ="localserver"; 
		
	
}


function errorCode2Message(nRc, data) {

    var UTIL_DEFINED_ERROR = 0x82000000;    /* From unblockSuccess.jsp */

	if(nRc == UTIL_DEFINED_ERROR+0x0100) {
		ret_msg = "${UnlockErr_LOAD_PKCS11LIB_ERRORKey}";
	} else if(nRc == UTIL_DEFINED_ERROR+0x0101) {
		ret_msg = "${UnlockErr_READER_NOT_SELECT_ERRORKey}";
	} else if(nRc == UTIL_DEFINED_ERROR+0x0102) {
		ret_msg = "${UnlockErr_USER_TYPE_ERRORKey}";
    } else if(nRc == UTIL_DEFINED_ERROR+0x0103) {
		ret_msg = "${UnlockErr_OBJECT_NUMBER_ERRORKey}"; 
	} else if(nRc == UTIL_DEFINED_ERROR+0x0104) {
		ret_msg = "${UnlockErr_KEYID_NOT_DEFINED_ERRORKey}"; 
	} else if(nRc == UTIL_DEFINED_ERROR+0x0105) {
		ret_msg = "${UnlockErr_KEYSIZE_NOT_DEFIND_EERRORKey}"; 
	} else if(nRc == UTIL_DEFINED_ERROR+0x0106) {
		ret_msg = "${UnlockErr_SELECT_CARDID_ERRORKey}"; 
	} else if(nRc == UTIL_DEFINED_ERROR+0x0107) {
		ret_msg = "${UnlockErr_UNSUPPROT_PINTYPEKey}"; 
	} else if(nRc == UTIL_DEFINED_ERROR+0x0108) {
		ret_msg = "${UnlockErr_NULL_PINNUMBERKey}"; 
	} else if(nRc == UTIL_DEFINED_ERROR+0x0109) {
		ret_msg = "${UnlockErr_TOKEN_NOT_PRESENTKey}"; 
	} else if(nRc == UTIL_DEFINED_ERROR+0x010A) {
		ret_msg = "${UnlockErr_NOT_BSTRTYPEKey}"; 
	} else if(nRc == UTIL_DEFINED_ERROR+0x010B) {
		ret_msg = "${UnlockErr_DECODE_BASE64_ERRORKey}"; 
	} else if(nRc == UTIL_DEFINED_ERROR+0x010C) {
		ret_msg = "${UnlockErr_TOKEN_UNSUPPORTED_FUNCTIONKey}"; 
	} else if(nRc == UTIL_DEFINED_ERROR+0x010D) {
		ret_msg = "${UnlockErr_EMPTY_CHALLENGE_CDOEKey}";
    } else if(nRc == UTIL_DEFINED_ERROR+0x010E) {
		ret_msg = "${UnlockErr_NOT_VALID_HEXSTRING_FORMATKey}";
    } else if(nRc == UTIL_DEFINED_ERROR+0x010F)  {
		ret_msg = "${UnlockErr_EMPTY_OBJECT_VALUEKey}"; 
    } else if(nRc == UTIL_DEFINED_ERROR+0x0110) {
		ret_msg = "${UnlockErr_EMPTY_USER_PINKey}";
	}
    
    if (nRc == 0x82000001) { /* 0x82開頭為自訂Token的錯誤代碼 */
        ret_msg = "錯誤代碼：0x82000001  說明：CRYPTO_PKCS7_TYPE_ERROR: Crypto TYPE錯誤";
    } else if (nRc == 0x82000002) {
        ret_msg = "錯誤代碼：0x82000002  說明：CRYPTO_PKCS7_GET_SIGNER: Crypto SIGNE錯誤";
    } else if (nRc == 0x82000003) {
        ret_msg = "錯誤代碼：0x82000003  說明：CRYPTO_PKCS7_SIGN_ERROR: Crypto SIGN錯誤";
    } else if (nRc == 0x82000004) {
        ret_msg = "錯誤代碼：0x82000004  說明：CRYPTO_PKCS7_VERIFY_FAIL: Crypto VERIFY錯誤";
    } else if (nRc == 0x82000005) {
        ret_msg = "錯誤代碼：0x82000005  說明：CRYPTO_PKCS7_ENCRYPT_ERROR: Crypto ENCRYPT錯誤";
    } else if (nRc == 0x82000006) {
        ret_msg = "錯誤代碼：0x82000006  說明：CRYPTO_PKCS7_DECRYPT_FAIL: Crypto DECRYPT錯誤";
    } else if (nRc == 0x82000026) {
        ret_msg = "錯誤代碼：0x82000026  說明：憑證解析錯誤(X509_NAME_get_index_by_NID錯誤)";
    } else if (nRc == 0x82000011) {
        ret_msg = "錯誤代碼：0x82000011  說明：時間格式轉換錯誤";
    } else if (nRc == 0x82000021) {
        ret_msg = "錯誤代碼：0x82000021  說明：找不到載具驅動函式庫";
    } else if (nRc == 0x82000022) {
        ret_msg = "錯誤代碼：0x82000022  說明：產生CSR過程中發生X509_REQ_sign錯誤";
    } else if (nRc == 0x82000023) {
        ret_msg = "錯誤代碼：0x82000023  說明：檢驗CSR內容時發現找不到金鑰";
    } else if (nRc == 0x82000024) {
        ret_msg = "錯誤代碼：0x82000024  說明：檢驗CSR內容時發現X509_REQ_verify錯誤";
    } else if (nRc == 0x82000025) {
        ret_msg = "錯誤代碼：0x82000025  說明：憑證內容讀取錯誤(X509_NAME_print_ex錯誤)";
    } else if (nRc == 0x82000026) {
        ret_msg = "錯誤代碼：0x82000026  說明：憑證解析錯誤(X509_NAME_get_index_by_NID錯誤)";
    } else if (nRc == 0x82000031) {
        ret_msg = "錯誤代碼：0x82000031  說明：載具內找不到特定的object";
    } else if (nRc == 0x82000032) {
        ret_msg = "錯誤代碼：0x82000032  說明：載具內找不到特定的CN";
    } else if (nRc == 0x81000001) { /* 0x81開頭為元件所定義的錯誤代碼 */
        ret_msg = "錯誤代碼：0x81000001  說明：使用者取消操作";
    } else if (nRc == 0x81000002) {
        ret_msg = "錯誤代碼：0x81000002  說明：未輸入載具密碼";
    } else if (nRc == 0x81000003) {
        ret_msg = "錯誤代碼：0x81000003  說明：未輸入圖形辨識碼";
    } else if (nRc == 0x81000004) {
        ret_msg = "錯誤代碼：0x81000004  說明：圖形辨識碼輸入錯誤";
    } else if (nRc == 0x81000005) {
        ret_msg = "錯誤代碼：0x81000005  說明：動態鍵盤確定鍵輸入錯誤";
    } else if (nRc == 0x81000006) {
        ret_msg = "錯誤代碼：0x81000006  說明：輸入新密碼與再次輸入新密碼不相符";
    } else if (nRc == 0x81000007) {
        ret_msg = "錯誤代碼：0x81000007  說明：未在指定時間內執行插拔載具動作";
    } else if (nRc == 0x81000008) {
        ret_msg = "錯誤代碼：0x81000008  說明：非合法網域使用";
    } else if (nRc == 0x81000009) {
        ret_msg = "錯誤代碼：0x81000009  說明：找不到載具驅動函式庫";
    } else if (nRc == 0x8100000A) {
        ret_msg = "錯誤代碼：0x8100000A  說明：找不到載具";
    } else if (nRc == 0x8100000B) {
        ret_msg = "錯誤代碼：0x8100000B  說明：憑證不存在或使用者未點選";
    } else if (nRc == 0x8100000C) {
        ret_msg = "錯誤代碼：0x8100000C  說明：載具中不存在任何金鑰";
    } else if (nRc == 0x8100000D) {
        ret_msg = "錯誤代碼：0x8100000D  說明：憑證有錯誤";
    } else if (nRc == 0x8100000E) {
        ret_msg = "錯誤代碼：0x8100000E  說明：找不到和憑證相對應的金鑰";
    } else if (nRc == 0x8100000F) {
        ret_msg = "錯誤代碼：0x8100000F  說明：載具中找不到Object(金鑰和憑證)";
    } else if (nRc == 0x81000010) {
        ret_msg = "錯誤代碼：0x81000010  說明：簽章輸出資料錯誤(長度=0)";
    } else if (nRc == 0x81000011) {
        ret_msg = "錯誤代碼：0x81000011  說明：拒絕重複寫入憑證";
    } else if (nRc == 0x81000012) {
        ret_msg = "錯誤代碼：0x81000012  說明：載具Session開啟失敗";
    } else if (nRc == 0x81000013) {
        ret_msg = "錯誤代碼：0x81000013  說明：讀取載具資訊失敗";
    } else if (nRc == 0x81000014) {
        ret_msg = "錯誤代碼：0x81000014  說明：新PIN碼不為數字組成";
    } else if (nRc == 0x81000015) {
        ret_msg = "錯誤代碼：0x81000015  說明：新PIN碼不為6~12碼";
    }
    else if (nRc == 0x81000017) {
        ret_msg = "錯誤代碼：0x81000017  說明：元件加解密異常";
    }
    else if (nRc == 0x81000018) {
        ret_msg = "錯誤代碼：0x81000018  說明：元件加解密異常";
    }
    else if (nRc == 0x81000019) {
        ret_msg = "錯誤代碼：0x81000019  說明：元件加解密異常";
    }
	// wen add release 1.1.0.2
    else if (nRc == 0x81000020) {
        ret_msg = "錯誤代碼：0x81000020  說明：RawSign簽章錯誤";
    }
    else if (nRc == 0x81000021) {
        ret_msg = "錯誤代碼：0x81000021  說明：RawSign簽章驗證錯誤";
    }
    else if (nRc == 0x81000024) {
        ret_msg = "錯誤代碼：0x81000024  說明：digestInfo資料過大";
    }
    else if (nRc == 0x81000025) {
        ret_msg = "錯誤代碼：0x81000025  說明：P1簽章錯誤";
    }
    else if (nRc == 0x81000026) {
        ret_msg = "錯誤代碼：0x81000026  說明：P1簽章驗證錯誤";
    }
    else if (nRc == 0x81000033) {
        ret_msg = "錯誤代碼：0x81000033  說明：digestInfo資料過大";
    }
    else if (nRc == 0x81000034) {
        ret_msg = "錯誤代碼：0x81000034  說明：P1簽章錯誤";
    }
    else if (nRc == 0x8100001A) {
        ret_msg = "錯誤代碼：0x8100001A  說明：元件加解密異常";
    } else if (nRc == 0x8100001F) {
        ret_msg = "錯誤代碼：0x8100001F  說明：元件記憶體錯誤";
    }
    else if (nRc == 0x81000100) {
        ret_msg = "錯誤代碼：0x81000100  說明：找不到Cilent端IP資訊";
    } else if (nRc == 0x81000101) {
        ret_msg = "錯誤代碼：0x81000101  說明：找不到Cilent端MAC資訊";
    } else if (nRc == 0x81000102) {
        ret_msg = "錯誤代碼：0x81000102  說明：MIME Encode過程發生錯誤";
    } else if (nRc == 0x81000103) {
        ret_msg = "錯誤代碼：0x81000103  說明：MIME Encode attach檔案時發生錯誤";
    } else if (nRc == 0x81000104) {
        ret_msg = "錯誤代碼：0x81000104  說明：註冊檔資料型態錯誤";
    } else if (nRc == 0x81000105) {
        ret_msg = "錯誤代碼：0x81000105  說明：交易相關參數遺失";
    } else if (nRc == 0x00000030) {
        ret_msg = "錯誤代碼：0x00000030  說明：CKR_DEVICE_ERROR: 載具異常";
    } else if (nRc == 0x00000031) {
        ret_msg = "錯誤代碼：0x00000031  說明：CKR_DEVICE_MEMORY: 載具沒有足夠的記憶體空間";
    } else if (nRc == 0x00000032) {
        ret_msg = "錯誤代碼：0x00000032  說明：CKR_DEVICE_REMOVED: 函式執行過程中載具被移除";
    }
    else if (nRc == 0x000000A0) {
        ret_msg = "錯誤代碼：0x000000A0  說明：CKR_PIN_INCORRECT: 密碼驗證失敗";
    } else if (nRc == 0x000000A1) {
        ret_msg = "錯誤代碼：0x000000A1  說明：CKR_PIN_INVALID: 輸入的PIN碼含無效字符，或與密碼強度設定不符";
    } else if (nRc == 0x000000A2) {
        ret_msg = "錯誤代碼：0x000000A2  說明：CKR_PIN_LEN_RANGE: 輸入的PIN碼長度太長或太短";
    } else if (nRc == 0x000000A3) {
        ret_msg = "錯誤代碼：0x000000A3  說明：CKR_PIN_EXPIRED: 載具PIN碼到期";
    } else if (nRc == 0x000000A4) {
        ret_msg = "錯誤代碼：0x000000A4  說明：CKR_PIN_LOCKED: 載具PIN碼鎖定";
    }
    else if (nRc == 0x000000E0) {
        ret_msg = "錯誤代碼：0x000000E0  說明：CKR_TOKEN_NOT_PRESENT: 載具不存在";
    } else if (nRc == 0x000000E1) {
        ret_msg = "錯誤代碼：0x000000E1  說明：CKR_TOKEN_NOT_RECOGNIZED: Cryptoki函式庫或slot無法識別該載具";
    } else if (nRc == 0x000000E2) {
        ret_msg = "錯誤代碼：0x000000E2  說明：CKR_TOKEN_WRITE_PROTECTED: 載具有防止寫入保護，因此無法執行請求的動作";
    } else if (nRc == 0x00000100) {
        ret_msg = "錯誤代碼：0x00000100  說明：CKR_USER_ALREADY_LOGGED_IN: 該使用者已經登入，無法允許重複登入";
    } else if (nRc == 0x00000101) {
        ret_msg = "錯誤代碼：0x00000101  說明：CKR_USER_NOT_LOGGED_IN: 使用者未登入";
    } else if (nRc == 0x00000102) {
        ret_msg = "錯誤代碼：0x00000102  說明：CKR_USER_PIN_NOT_INITIALIZED: PIN碼尚未被初始化";
    }
    else if (nRc < 0x81000000) {
        ret_msg = "錯誤代碼：" + "0x" + parseInt(nRc, 10).toString(16) + "  說明：PKCS#11 錯誤";
    }
    //end region need fox to check
    else if (nRc == 0x81000200) {
        ret_msg = "錯誤代碼：0x81000200  說明：交易時連線錯誤:與主機連線發生問題";
    } else if (nRc == 0x81000201) {
        ret_msg = "錯誤代碼：0x81000201  說明：交易時連線錯誤:接收主機回應訊息逾時";
    } else if (nRc == 0x81000202) {
        ret_msg = "錯誤代碼：0x81000202  說明：交易時連線錯誤:主機回應資料訊息格式不正確";
    } else if (nRc == 0x81000203) {
        ret_msg = "錯誤代碼：0x81000203  說明：交易時連線錯誤:主機無回應訊息";
    } else if (nRc == 0x81000300) {
        ret_msg = "錯誤代碼：0x81000300  說明：本機端時間異常";
    }
    else if (nRc == 0x81000301) {
        ret_msg = "錯誤代碼：0x81000301  說明：元件初始化連線逾時";
    }
    else if (nRc == 0x81000302) {
        ret_msg = "錯誤代碼：0x81000302  說明：元件初始化驗章失敗";
    }
    else if (nRc == 0x81000303) {
        ret_msg = "錯誤代碼：0x81000303  說明：元件解析json失敗";
    }
    else if (nRc == 0x81000304) {
        ret_msg = "錯誤代碼：0x81000304  說明：元件呼叫方法異常";
    }
    else if (nRc == 0x81000305) {
        ret_msg = "錯誤代碼：0x81000305  說明：元件json參數錯誤";
    }
    else if (nRc == 0x81000306) {
        ret_msg = "錯誤代碼：0x81000306  說明：元件json參數錯誤";
    }
    else if (nRc == 0x81000307) {
        ret_msg = "錯誤代碼：0x81000307  說明：元件呼叫方法異常";
    }
    else if (nRc == 0x81000308) {
        ret_msg = "錯誤代碼：0x81000308  說明：UCTTime錯誤";
    }
    else if (nRc == 0x81000309) {
        ret_msg = "錯誤代碼：0x81000309  說明：元件異常";
    }
    else if (nRc == 0x83000001) {
        ret_msg = "錯誤代碼：0x83000001  說明：未授權的網站";
    }
    else if (nRc == 0x83000002) {
        ret_msg = "錯誤代碼：0x83000002  說明：錯誤的連線路徑";
    }
    else if (nRc == 0x83000003) {
        ret_msg = "錯誤代碼：0x83000003  說明：需求指令有誤";
    }
    else if (nRc == 0x81100001) { /* 0x811開頭為元件定義: 上傳資料後，OpMode=EXEC連線回應的RETURNCODE值加上元件自定義值0x81100000，詳細錯誤需查閱property: ERRORCODE */
        
    	//Larry 20180321
    	if(comErrormessage == "activex"){
    		ret_msg = SP11FileUpload_Control().errMsg;
    	}else{
    		ret_msg = data.errMsg;
    	}
    	
    } else if (nRc == 0x81100002) {
        
        //Larry 20180321
    	if(comErrormessage == "activex"){
    		errorCode = getErrorCode(SP11FileUpload_Control().errMsg, 'ERRORCODE');
    	}else{
    		errorCode = getErrorCode(data.errMsg, 'ERRORCODE');
    	}
    	
        if (errorCode == "015") {
            ret_msg = "錯誤代碼：CA-Server 015  說明：驗證客戶資料時發生問題，請確認是否插入正確的憑證";
        } else if (errorCode == "018") {
            
            //Larry 20180321
        	if(comErrormessage == "activex"){
        		ret_msg = ParseErrorMessage(SP11FileUpload_Control().errMsg, 'DETAIL');
        	}else{
        		ret_msg = ParseErrorMessage(data.errMsg, 'DETAIL');
        	}
        } else if (errorCode == "019") {

        	//Larry 20180321
        	if(comErrormessage == "activex"){
        		ret_msg = ParseErrorMessage(SP11FileUpload_Control().errMsg, 'DETAIL');
        	}else{
        		ret_msg = ParseErrorMessage(data.errMsg, 'DETAIL');
        	}
        } else if (errorCode == "026") {

        	//Larry 20180321
        	if(comErrormessage == "activex"){
        		ret_msg = ParseErrorMessage(SP11FileUpload_Control().errMsg, 'DETAIL');
        	}else{
        		ret_msg = ParseErrorMessage(data.errMsg, 'DETAIL');
        	}
        } else {
            ret_msg = "錯誤代碼：CA-Server " + errorCode + "  說明：系統發生錯誤，請聯絡相關人員";
        }
    } else if (nRc == 0x81100003) {
        
        //Larry 20180321
    	if(comErrormessage == "activex"){
    		errorCode = getErrorCode(SP11FileUpload_Control().errMsg, 'ERRORCODE');
    	}else{
    		errorCode = getErrorCode(data.errMsg, 'ERRORCODE');
    	}
        
        ret_msg = "錯誤代碼：CA-Server " + errorCode + "  說明：系統發生錯誤，請聯絡相關人員";
    } else if (nRc == 0x81100004) {
        
    	//Larry 20180321
    	if(comErrormessage == "activex"){
    		ret_msg = ParseErrorMessage(SP11FileUpload_Control().errMsg, 'DETAIL');
    	}else{
    		ret_msg = ParseErrorMessage(data.errMsg, 'DETAIL');
    	}
    } else if (nRc == 0x81100005) {

    	//Larry 20180321
    	if(comErrormessage == "activex"){
    		ret_msg = SP11FileUpload_Control().errMsg;
    	}else{
    		ret_msg = data.errMsg;
    	}
    } else if (nRc == 0x81100006) {

    	//Larry 20180321
    	if(comErrormessage == "activex"){
    		ret_msg = SP11FileUpload_Control().errMsg;
    	}else{
    		ret_msg = data.errMsg;
    	}
    }
    else if ((nRc >= 0x81200001) && (nRc <= 0x812FFFFF)) {
        ret_msg = "錯誤代碼：" + "0x" + parseInt(nRc, 10).toString(16) + "  說明：存取Windows Registry錯誤。錯誤碼：" + "0x" + parseInt(nRc - 0x81200000, 10).toString(16);
    }
    else {
        ret_msg = "0x" + parseInt(nRc, 10).toString(16);
    }
    return ret_msg;
}

function errorCode2Message_CN(nRc, data) {

    var UTIL_DEFINED_ERROR = 0x82000000;    /* From unblockSuccess.jsp */

	if(nRc == UTIL_DEFINED_ERROR+0x0100) {
		ret_msg = "${UnlockErr_LOAD_PKCS11LIB_ERRORKey}";
	} else if(nRc == UTIL_DEFINED_ERROR+0x0101) {
		ret_msg = "${UnlockErr_READER_NOT_SELECT_ERRORKey}";
	} else if(nRc == UTIL_DEFINED_ERROR+0x0102) {
		ret_msg = "${UnlockErr_USER_TYPE_ERRORKey}";
    } else if(nRc == UTIL_DEFINED_ERROR+0x0103) {
		ret_msg = "${UnlockErr_OBJECT_NUMBER_ERRORKey}"; 
	} else if(nRc == UTIL_DEFINED_ERROR+0x0104) {
		ret_msg = "${UnlockErr_KEYID_NOT_DEFINED_ERRORKey}"; 
	} else if(nRc == UTIL_DEFINED_ERROR+0x0105) {
		ret_msg = "${UnlockErr_KEYSIZE_NOT_DEFIND_EERRORKey}"; 
	} else if(nRc == UTIL_DEFINED_ERROR+0x0106) {
		ret_msg = "${UnlockErr_SELECT_CARDID_ERRORKey}"; 
	} else if(nRc == UTIL_DEFINED_ERROR+0x0107) {
		ret_msg = "${UnlockErr_UNSUPPROT_PINTYPEKey}"; 
	} else if(nRc == UTIL_DEFINED_ERROR+0x0108) {
		ret_msg = "${UnlockErr_NULL_PINNUMBERKey}"; 
	} else if(nRc == UTIL_DEFINED_ERROR+0x0109) {
		ret_msg = "${UnlockErr_TOKEN_NOT_PRESENTKey}"; 
	} else if(nRc == UTIL_DEFINED_ERROR+0x010A) {
		ret_msg = "${UnlockErr_NOT_BSTRTYPEKey}"; 
	} else if(nRc == UTIL_DEFINED_ERROR+0x010B) {
		ret_msg = "${UnlockErr_DECODE_BASE64_ERRORKey}"; 
	} else if(nRc == UTIL_DEFINED_ERROR+0x010C) {
		ret_msg = "${UnlockErr_TOKEN_UNSUPPORTED_FUNCTIONKey}"; 
	} else if(nRc == UTIL_DEFINED_ERROR+0x010D) {
		ret_msg = "${UnlockErr_EMPTY_CHALLENGE_CDOEKey}";
    } else if(nRc == UTIL_DEFINED_ERROR+0x010E) {
		ret_msg = "${UnlockErr_NOT_VALID_HEXSTRING_FORMATKey}";
    } else if(nRc == UTIL_DEFINED_ERROR+0x010F)  {
		ret_msg = "${UnlockErr_EMPTY_OBJECT_VALUEKey}"; 
    } else if(nRc == UTIL_DEFINED_ERROR+0x0110) {
		ret_msg = "${UnlockErr_EMPTY_USER_PINKey}";
	}

    if (nRc == 0x82000001) { /* 0x82開頭為自訂Token的錯誤代碼 */
        ret_msg = "错误代码：0x82000001  说明：CRYPTO_PKCS7_TYPE_ERROR: Crypto TYPE错误";
    } else if (nRc == 0x82000002) {
        ret_msg = "错误代码：0x82000002  说明：CRYPTO_PKCS7_GET_SIGNER: Crypto SIGNE错误";
    } else if (nRc == 0x82000003) {
        ret_msg = "错误代码：0x82000003  说明：CRYPTO_PKCS7_SIGN_ERROR: Crypto SIGN错误";
    } else if (nRc == 0x82000004) {
        ret_msg = "错误代码：0x82000004  说明：CRYPTO_PKCS7_VERIFY_FAIL: Crypto VERIFY错误";
    } else if (nRc == 0x82000005) {
        ret_msg = "错误代码：0x82000005  说明：CRYPTO_PKCS7_ENCRYPT_ERROR: Crypto ENCRYPT错误";
    } else if (nRc == 0x82000006) {
        ret_msg = "错误代码：0x82000006  说明：CRYPTO_PKCS7_DECRYPT_FAIL: Crypto DECRYPT错误";
    } else if (nRc == 0x82000026) {
        ret_msg = "错误代码：0x82000026  说明：证书解析错误(X509_NAME_get_index_by_NID错误)";
    } else if (nRc == 0x82000011) {
        ret_msg = "错误代码：0x82000011  说明：时间格式转换错误";
    } else if (nRc == 0x82000021) {
        ret_msg = "错误代码：0x82000021  说明：找不到载具驱动函式库";
    } else if (nRc == 0x82000022) {
        ret_msg = "错误代码：0x82000022  说明：产生CSR过程中发生X509_REQ_sign错误";
    } else if (nRc == 0x82000023) {
        ret_msg = "错误代码：0x82000023  说明：检验CSR内容时发现找不到金钥";
    } else if (nRc == 0x82000024) {
        ret_msg = "错误代码：0x82000024  说明：检验CSR内容时发现X509_REQ_verify错误";
    } else if (nRc == 0x82000025) {
        ret_msg = "错误代码：0x82000025  说明：证书内容读取错误(X509_NAME_print_ex错误)";
    } else if (nRc == 0x82000026) {
        ret_msg = "错误代码：0x82000026  说明：证书解析错误(X509_NAME_get_index_by_NID错误)";
    } else if (nRc == 0x82000031) {
        ret_msg = "错误代码：0x82000031  说明：载具内找不到特定的object";
    } else if (nRc == 0x82000032) {
        ret_msg = "错误代码：0x82000032  说明：载具内找不到特定CN";
    } else if (nRc == 0x81000001) {
        ret_msg = "错误代码：0x81000001  说明：使用者取消操作";
    } else if (nRc == 0x81000002) {
        ret_msg = "错误代码：0x81000002  说明：未输入载具密码";
    } else if (nRc == 0x81000003) {
        ret_msg = "错误代码：0x81000003  说明：未输入图形辨识码";
    } else if (nRc == 0x81000004) {
        ret_msg = "错误代码：0x81000004  说明：图形辨识码输入错误";
    } else if (nRc == 0x81000005) {
        ret_msg = "错误代码：0x81000005  说明：动态键盘确定键输入错误";
    } else if (nRc == 0x81000006) {
        ret_msg = "错误代码：0x81000006  说明：输入新密码与再次输入新密码不相符";
    } else if (nRc == 0x81000007) {
        ret_msg = "错误代码：0x81000007  说明：未在指定时间内执行插拔载具动作";
    } else if (nRc == 0x81000008) {
        ret_msg = "错误代码：0x81000008  说明：非合法网域使用";
    } else if (nRc == 0x81000009) {
        ret_msg = "错误代码：0x81000009  说明：找不到载具驱动函式库";
    } else if (nRc == 0x8100000A) {
        ret_msg = "错误代码：0x8100000A  说明：找不到载具";
    } else if (nRc == 0x8100000B) {
        ret_msg = "错误代码：0x8100000B  说明：证书不存在或使用者未点选";
    } else if (nRc == 0x8100000C) {
        ret_msg = "错误代码：0x8100000C  说明：载具中不存在任何金钥";
    } else if (nRc == 0x8100000D) {
        ret_msg = "错误代码：0x8100000D  说明：证书有错误";
    } else if (nRc == 0x8100000E) {
        ret_msg = "错误代码：0x8100000E  说明：找不到和证书相对应的金钥";
    } else if (nRc == 0x8100000F) {
        ret_msg = "错误代码：0x8100000F  说明：载具中找不到Object(金钥和证书)";
    } else if (nRc == 0x81000010) {
        ret_msg = "错误代码：0x81000010  说明：签名输出资料错误(长度=0)";
    } else if (nRc == 0x81000011) {
        ret_msg = "错误代码：0x81000011  说明：拒绝重复写入证书";
    } else if (nRc == 0x81000012) {
        ret_msg = "错误代码：0x81000012  说明：载具Session开启失败";
    } else if (nRc == 0x81000013) {
        ret_msg = "错误代码：0x81000013  说明：读取载具资讯失败";
    } else if (nRc == 0x81000014) {
        ret_msg = "错误代码：0x81000014  说明：新PIN码不为数字组成";
    } else if (nRc == 0x81000015) {
        ret_msg = "错误代码：0x81000015  说明：新PIN码不为6〜12码";
    }
    else if (nRc == 0x81000017) {
        ret_msg = "错误代码：0x81000017  说明：元件加解密异常";
    }
    else if (nRc == 0x81000018) {
        ret_msg = "错误代码：0x81000018  说明：元件加解密异常";
    }
    else if (nRc == 0x81000019) {
        ret_msg = "错误代码：0x81000019  说明：元件加解密异常";
    }
	// wen add release 1.1.0.2
    else if (nRc == 0x81000020) {
        ret_msg = "錯誤代碼：0x81000020  說明：RawSign签章错误";
    }
    else if (nRc == 0x81000021) {
        ret_msg = "错误代码：0x81000021  說明：RawSign签章验证错误";
    }
    else if (nRc == 0x81000024) {
        ret_msg = "错误代码：0x81000024  說明：digestInfo资料过大";
    }
    else if (nRc == 0x81000025) {
        ret_msg = "错误代码：0x81000025  說明：P1签章错误";
    }
    else if (nRc == 0x81000026) {
        ret_msg = "错误代码：0x81000026  說明：P1签章验证错误";
    }
    else if (nRc == 0x81000033) {
        ret_msg = "错误代码：0x81000033  說明：digestInfo资料过大";
    }
    else if (nRc == 0x81000034) {
        ret_msg = "错误代码：0x81000034  說明：P1签章错误";
    }
    else if (nRc == 0x8100001A) {
        ret_msg = "错误代码：0x8100001A  说明：元件加解密异常";
    } else if (nRc == 0x8100001F) {
        ret_msg = "错误代码：0x8100001F  说明：元件记忆体错误";
    }
    else if (nRc == 0x81000100) {
        ret_msg = "错误代码：0x81000100  说明：找不到Cilent端IP资讯";
    } else if (nRc == 0x81000101) {
        ret_msg = "错误代码：0x81000101  说明：找不到Cilent端MAC资讯";
    } else if (nRc == 0x81000102) {
        ret_msg = "错误代码：0x81000102  说明：MIME Encode过程发生错误";
    } else if (nRc == 0x81000103) {
        ret_msg = "错误代码：0x81000103  说明：MIME Encode attach档案时发生错误";
    } else if (nRc == 0x81000104) {
        ret_msg = "错误代码：0x81000104  说明：注册档资料型态错误";
    } else if (nRc == 0x81000105) {
        ret_msg = "错误代码：0x81000105  说明：交易相关参数遗失";
    } else if (nRc == 0x00000030) {
        ret_msg = "错误代码：0x00000030  说明：CKR_DEVICE_ERROR: 载具异常";
    } else if (nRc == 0x00000031) {
        ret_msg = "错误代码：0x00000031  说明：CKR_DEVICE_MEMORY: 载具没有足够的记忆体空间";
    } else if (nRc == 0x00000032) {
        ret_msg = "错误代码：0x00000032  说明：CKR_DEVICE_REMOVED: 函式执行过程中载具被移除";
    }
    else if (nRc == 0x000000A0) {
        ret_msg = "错误代码：0x000000A0  说明：CKR_PIN_INCORRECT: 密码验证失败";
    } else if (nRc == 0x000000A1) {
        ret_msg = "错误代码：0x000000A1  说明：CKR_PIN_INVALID: 输入的PIN码含无效字符，或与密码强度设定不符";
    } else if (nRc == 0x000000A2) {
        ret_msg = "错误代码：0x000000A2  说明：CKR_PIN_LEN_RANGE: 输入的PIN码长度太长或太短";
    } else if (nRc == 0x000000A3) {
        ret_msg = "错误代码：0x000000A3  说明：CKR_PIN_EXPIRED: 载具PIN码到期";
    } else if (nRc == 0x000000A4) {
        ret_msg = "错误代码：0x000000A4  说明：CKR_PIN_LOCKED: 载具PIN码锁定";
    }
    else if (nRc == 0x000000E0) {
        ret_msg = "错误代码：0x000000E0  说明：CKR_TOKEN_NOT_PRESENT: 载具不存在";
    } else if (nRc == 0x000000E1) {
        ret_msg = "错误代码：0x000000E1  说明：CKR_TOKEN_NOT_RECOGNIZED: Cryptoki函式库或slot无法识别该载具";
    } else if (nRc == 0x000000E2) {
        ret_msg = "错误代码：0x000000E2  说明：CKR_TOKEN_WRITE_PROTECTED: 载具有防止写入保护，因此无法执行请求的动作";
    } else if (nRc == 0x00000100) {
        ret_msg = "错误代码：0x00000100  说明：CKR_USER_ALREADY_LOGGED_IN: 该使用者已经登入，无法允许重复登入";
    } else if (nRc == 0x00000101) {
        ret_msg = "错误代码：0x00000101  说明：CKR_USER_NOT_LOGGED_IN: 使用者未登入";
    } else if (nRc == 0x00000102) {
        ret_msg = "错误代码：0x00000102  说明：CKR_USER_PIN_NOT_INITIALIZED: PIN码尚未被初始化";
    }
    else if (nRc < 0x81000000) {
        ret_msg = "错误代码：" + "0x" + parseInt(nRc, 10).toString(16) + "  说明：PKCS#11 错误";
    }
    else if (nRc == 0x81000200) {
        ret_msg = "错误代码：0x81000200  说明：交易时连线错误:与主机连线发生问题";
    } else if (nRc == 0x81000201) {
        ret_msg = "错误代码：0x81000201  说明：交易时连线错误:接收主机回应讯息逾时";
    } else if (nRc == 0x81000202) {
        ret_msg = "错误代码：0x81000202  说明：交易时连线错误:主机回应资料讯息格式不正确";
    } else if (nRc == 0x81000203) {
        ret_msg = "错误代码：0x81000203  说明：交易时连线错误:主机无回应讯息";
    } else if (nRc == 0x81000300) {
        ret_msg = "错误代码：0x81000300  说明：本机端时间异常";
    }
    else if (nRc == 0x81000301) {
        ret_msg = "错误代码：0x81000301  说明：元件初始化连线逾时";
    }
    else if (nRc == 0x81000302) {
        ret_msg = "错误代码：0x81000302  说明：元件初始化验章失败";
    }
    else if (nRc == 0x81000303) {
        ret_msg = "错误代码：0x81000303  说明：元件解析JSON失败";
    }
    else if (nRc == 0x81000304) {
        ret_msg = "错误代码：0x81000304  说明：元件呼叫方法异常";
    }
    else if (nRc == 0x81000305) {
        ret_msg = "错误代码：0x81000305  说明：元件JSON参数错误";
    }
    else if (nRc == 0x81000306) {
        ret_msg = "错误代码：0x81000306  说明：元件JSON参数错误";
    }
    else if (nRc == 0x81000307) {
        ret_msg = "错误代码：0x81000307  说明：元件呼叫方法异常";
    }
    else if (nRc == 0x81000308) {
        ret_msg = "错误代码：0x81000308  说明：UCTTime错误";
    }
    else if (nRc == 0x81000309) {
        ret_msg = "错误代码：0x81000309  说明：元件异常";
    }
    else if (nRc == 0x83000001) {
        ret_msg = "错误代码：0x83000001  说明：未授权的网站";
    }
    else if (nRc == 0x83000002) {
        ret_msg = "错误代码：0x83000002  说明：错误的连线路径";
    }
    else if (nRc == 0x83000003) {
        ret_msg = "错误代码：0x83000003  说明：需求指令有误";
    }
    else if (nRc == 0x81100001) {/* 0x811開頭為元件定義: 上傳資料後，OpMode=EXEC連線回應的RETURNCODE值加上元件自定義值0x81100000，詳細錯誤需查閱property: ERRORCODE */
        
    	//Larry 20180321
    	//ret_msg = data.errMsg;
    	if(comErrormessage == "activex"){
    		ret_msg = SP11FileUpload_Control().errMsg;
    	}else{
    		ret_msg = data.errMsg;
    	}
    } else if (nRc == 0x81100002) {
        
        //Larry 20180321
    	//ret_msg = ParseErrorMessage(data.errMsg, 'DETAIL');
    	if(comErrormessage == "activex"){
    		errorCode = getErrorCode(SP11FileUpload_Control().errMsg, 'ERRORCODE');
    	}else{
    		errorCode = getErrorCode(data.errMsg, 'ERRORCODE');
    	}
        
        if (errorCode == "015") {
            ret_msg = "错误代码：CA-Server 015  说明：验证客户资料时发生问题，请确认是否插入正确的证书";
        } else if (errorCode == "018") {
        	
        	//Larry 20180321
        	//ret_msg = ParseErrorMessage(data.errMsg, 'DETAIL');
        	if(comErrormessage == "activex"){
        		ret_msg = ParseErrorMessage(SP11FileUpload_Control().errMsg, 'DETAIL');
        	}else{
        		ret_msg = ParseErrorMessage(data.errMsg, 'DETAIL');
        	}
        } else if (errorCode == "019") {
            
        	//Larry 20180321
        	//ret_msg = ParseErrorMessage(data.errMsg, 'DETAIL');
        	if(comErrormessage == "activex"){
        		ret_msg = ParseErrorMessage(SP11FileUpload_Control().errMsg, 'DETAIL');
        	}else{
        		ret_msg = ParseErrorMessage(data.errMsg, 'DETAIL');
        	}
        } else if (errorCode == "026") {
            
        	//Larry 20180321
        	//ret_msg = ParseErrorMessage(data.errMsg, 'DETAIL');
        	if(comErrormessage == "activex"){
        		ret_msg = ParseErrorMessage(SP11FileUpload_Control().errMsg, 'DETAIL');
        	}else{
        		ret_msg = ParseErrorMessage(data.errMsg, 'DETAIL');
        	}
        } else {
            ret_msg = "错误代码：CA-Server " + errorCode + "  说明：系统发生错误，请联络相关人员";
        }
    } else if (nRc == 0x81100003) {
    	
    	//Larry 20180321
    	if(comErrormessage == "activex"){
    		errorCode = getErrorCode(SP11FileUpload_Control().errMsg, 'ERRORCODE');
    	}else{
    		errorCode = getErrorCode(data.errMsg, 'ERRORCODE');
    	}
        
        ret_msg = "错误代码：CA-Server " + errorCode + "  说明：系统发生错误，请联络相关人员";
    } else if (nRc == 0x81100004) {
        
        //Larry 20180321
    	//ret_msg = ParseErrorMessage(data.errMsg, 'DETAIL');
    	if(comErrormessage == "activex"){
    		ret_msg = ParseErrorMessage(SP11FileUpload_Control().errMsg, 'DETAIL');
    	}else{
    		ret_msg = ParseErrorMessage(data.errMsg, 'DETAIL');
    	}
        
    } else if (nRc == 0x81100005) {

    	//Larry 20180321
    	//ret_msg = data.errMsg;
    	if(comErrormessage == "activex"){
    		ret_msg = SP11FileUpload_Control().errMsg;
    	}else{
    		ret_msg = data.errMsg;
    	}
    } else if (nRc == 0x81100006) {
    	
    	//Larry 20180321
    	//ret_msg = data.errMsg;
    	if(comErrormessage == "activex"){
    		ret_msg = SP11FileUpload_Control().errMsg
    	}else{
    		ret_msg = data.errMsg;
    	}
    }
    else if ((nRc >= 0x81200001) && (nRc <= 0x812FFFFF)) {
        ret_msg = "错误代码：" + "0x" + parseInt(nRc, 10).toString(16) + "  说明：存取Windows Registry错误。错误码：" + "0x" + parseInt(nRc - 0x81200000, 10).toString(16);
    } else {
        ret_msg = "0x" + parseInt(nRc, 10).toString(16);
    }
    return ret_msg;
}

function errorCode2Message_ENG(nRc, data) {

    var UTIL_DEFINED_ERROR = 0x82000000;    /* From unblockSuccess.jsp */

	if(nRc == UTIL_DEFINED_ERROR+0x0100) {
		ret_msg = "${UnlockErr_LOAD_PKCS11LIB_ERRORKey}";
	} else if(nRc == UTIL_DEFINED_ERROR+0x0101) {
		ret_msg = "${UnlockErr_READER_NOT_SELECT_ERRORKey}";
	} else if(nRc == UTIL_DEFINED_ERROR+0x0102) {
		ret_msg = "${UnlockErr_USER_TYPE_ERRORKey}";
    } else if(nRc == UTIL_DEFINED_ERROR+0x0103) {
		ret_msg = "${UnlockErr_OBJECT_NUMBER_ERRORKey}"; 
	} else if(nRc == UTIL_DEFINED_ERROR+0x0104) {
		ret_msg = "${UnlockErr_KEYID_NOT_DEFINED_ERRORKey}"; 
	} else if(nRc == UTIL_DEFINED_ERROR+0x0105) {
		ret_msg = "${UnlockErr_KEYSIZE_NOT_DEFIND_EERRORKey}"; 
	} else if(nRc == UTIL_DEFINED_ERROR+0x0106) {
		ret_msg = "${UnlockErr_SELECT_CARDID_ERRORKey}"; 
	} else if(nRc == UTIL_DEFINED_ERROR+0x0107) {
		ret_msg = "${UnlockErr_UNSUPPROT_PINTYPEKey}"; 
	} else if(nRc == UTIL_DEFINED_ERROR+0x0108) {
		ret_msg = "${UnlockErr_NULL_PINNUMBERKey}"; 
	} else if(nRc == UTIL_DEFINED_ERROR+0x0109) {
		ret_msg = "${UnlockErr_TOKEN_NOT_PRESENTKey}"; 
	} else if(nRc == UTIL_DEFINED_ERROR+0x010A) {
		ret_msg = "${UnlockErr_NOT_BSTRTYPEKey}"; 
	} else if(nRc == UTIL_DEFINED_ERROR+0x010B) {
		ret_msg = "${UnlockErr_DECODE_BASE64_ERRORKey}"; 
	} else if(nRc == UTIL_DEFINED_ERROR+0x010C) {
		ret_msg = "${UnlockErr_TOKEN_UNSUPPORTED_FUNCTIONKey}"; 
	} else if(nRc == UTIL_DEFINED_ERROR+0x010D) {
		ret_msg = "${UnlockErr_EMPTY_CHALLENGE_CDOEKey}";
    } else if(nRc == UTIL_DEFINED_ERROR+0x010E) {
		ret_msg = "${UnlockErr_NOT_VALID_HEXSTRING_FORMATKey}";
    } else if(nRc == UTIL_DEFINED_ERROR+0x010F)  {
		ret_msg = "${UnlockErr_EMPTY_OBJECT_VALUEKey}"; 
    } else if(nRc == UTIL_DEFINED_ERROR+0x0110) {
		ret_msg = "${UnlockErr_EMPTY_USER_PINKey}";
	}
    
    if (nRc == 0x82000001) {
        ret_msg = "Error Code:0x82000001  Info:CRYPTO_PKCS7_TYPE_ERROR: Crypto TYPE Error";
    } else if (nRc == 0x82000002) {
        ret_msg = "Error Code:0x82000002  Info:CRYPTO_PKCS7_GET_SIGNER: Crypto SIGNE Error";
    } else if (nRc == 0x82000003) {
        ret_msg = "Error Code:0x82000003  Info:CRYPTO_PKCS7_SIGN_ERROR: Crypto SIGN Error";
    } else if (nRc == 0x82000004) {
        ret_msg = "Error Code:0x82000004  Info:CRYPTO_PKCS7_VERIFY_FAIL: Crypto VERIFY Error";
    } else if (nRc == 0x82000005) {
        ret_msg = "Error Code:0x82000005  Info:CRYPTO_PKCS7_ENCRYPT_ERROR: Crypto ENCRYPT Error";
    } else if (nRc == 0x82000006) {
        ret_msg = "Error Code:0x82000006  Info:CRYPTO_PKCS7_DECRYPT_FAIL: Crypto DECRYPT Error";
    } else if (nRc == 0x82000026) {
        ret_msg = "Error Code:0x82000026  Info:Certificate Analyze Error (X509_NAME_get_index_by_NID Error)";
    } else if (nRc == 0x82000011) {
        ret_msg = "Error Code:0x82000011  Info:Convert Time Format Error";
    } else if (nRc == 0x82000021) {
        ret_msg = "Error Code:0x82000021  Info:Can not Find Token DLL";
    } else if (nRc == 0x82000022) {
        ret_msg = "Error Code:0x82000022  Info:X509_REQ_sign Error When Generating CSR";
    } else if (nRc == 0x82000023) {
        ret_msg = "Error Code:0x82000023  Info:Can not Find Keys When Verifying CSR";
    } else if (nRc == 0x82000024) {
        ret_msg = "Error Code:0x82000024  Info:X509_REQ_verify Error When Verifying CSR";
    } else if (nRc == 0x82000025) {
        ret_msg = "Error Code:0x82000025  Info:Read Cert Error (X509_NAME_print_ex Error)";
    } else if (nRc == 0x82000026) {
        ret_msg = "Error Code:0x82000026  Info:Analyze Cert Error (X509_NAME_get_index_by_NID Error)";
    } else if (nRc == 0x82000031) {
        ret_msg = "Error Code:0x82000031  Info:No Specific Object in the Token";
    } else if (nRc == 0x82000032) {
        ret_msg = "Error Code:0x82000032  Info:Cannot find the specific CN in the Token";
    } else if (nRc == 0x81000001) {
        ret_msg = "Error Code:0x81000001  Info:User has canceled action";
    } else if (nRc == 0x81000002) {
        ret_msg = "Error Code:0x81000002  Info:No Token Password Entered";
    } else if (nRc == 0x81000003) {
        ret_msg = "Error Code:0x81000003  Info:No Captcha Entered";
    } else if (nRc == 0x81000004) {
        ret_msg = "Error Code:0x81000004  Info:Captcha Error";
    } else if (nRc == 0x81000005) {
        ret_msg = "Error Code:0x81000005  Info:Pin Pad Confirm Button Error";
    } else if (nRc == 0x81000006) {
        ret_msg = "Error Code:0x81000006  Info:Content of New PIN Code and confirm PIN Code are not consistent";
    } else if (nRc == 0x81000007) {
        ret_msg = "Error Code:0x81000007  Info:User did not pull device out in specfied time";
    } else if (nRc == 0x81000008) {
        ret_msg = "Error Code:0x81000008  Info:Invalid Domain";
    } else if (nRc == 0x81000009) {
        ret_msg = "Error Code:0x81000009  Info:Can not Find Token DLL";
    } else if (nRc == 0x8100000A) {
        ret_msg = "Error Code:0x8100000A  Info:Token not found";
    } else if (nRc == 0x8100000B) {
        ret_msg = "Error Code:0x8100000B  Info:Can not find certificate or user did not select";
    } else if (nRc == 0x8100000C) {
        ret_msg = "Error Code:0x8100000C  Info:Private Key not found,please check if private key has been deleted";
    } else if (nRc == 0x8100000D) {
        ret_msg = "Error Code:0x8100000D  Info:Certificate Error";
    } else if (nRc == 0x8100000E) {
        ret_msg = "Error Code:0x8100000E  Info:Certificate and Key are not match";
    } else if (nRc == 0x8100000F) {
        ret_msg = "Error Code:0x8100000F  Info:Can not Find Objects(Keys and Certificates)";
    } else if (nRc == 0x81000010) {
        ret_msg = "Error Code:0x81000010  Info:Signature Output Error (Length is 0)";
    } else if (nRc == 0x81000011) {
        ret_msg = "Error Code:0x81000011  Info:Writing duplicate cert denied";
    } else if (nRc == 0x81000012) {
        ret_msg = "Error Code:0x81000012  Info:Token open session failed";
    } else if (nRc == 0x81000013) {
        ret_msg = "Error Code:0x81000013  Info:Read token info failed";
    } else if (nRc == 0x81000014) {
        ret_msg = "Error Code:0x81000014  Info:New PIN code should be composed of numbers";
    } else if (nRc == 0x81000015) {
        ret_msg = "Error Code:0x81000015  Info:The length of new PIN code should be 6-12";
    } else if (nRc == 0x81000017) {
        ret_msg = "Error Code:0x81000017  Info:Local server encrypt/decrypt error";
    } else if (nRc == 0x81000018) {
        ret_msg = "Error Code:0x81000018  Info:Local server encrypt/decrypt error";
    } else if (nRc == 0x81000019) {
        ret_msg = "Error Code:0x81000019  Info:Local server encrypt/decrypt error";
    } 
	// wen add release 1.1.0.2
    else if (nRc == 0x81000020) {
        ret_msg = "Error Code:0x81000020  Info：RawSign sign failed";
    } else if (nRc == 0x81000021) {
        ret_msg = "Error Code:0x81000021  Info：RawSign sign verify failed";
    } else if (nRc == 0x81000024) {
        ret_msg = "Error Code:0x81000024  Info：digestInfo data too big";
    } else if (nRc == 0x81000025) {
        ret_msg = "Error Code:0x81000025  Info：P1 sign failed";
    } else if (nRc == 0x81000026) {
        ret_msg = "Error Code:0x81000026  Info：P1 sign verify failed";
    } else if (nRc == 0x81000033) {
        ret_msg = "Error Code:0x81000033  Info：digestInfo data too big";
    } else if (nRc == 0x81000034) {
        ret_msg = "Error Code:0x81000034  Info：P1 sign failed";
    } else if (nRc == 0x8100001A) {
        ret_msg = "Error Code:0x8100001A  Info:Local server encrypt/decrypt error";
    } else if (nRc == 0x81000100) {
        ret_msg = "Error Code:0x81000100  Info:Can not Find Client IP";
    } else if (nRc == 0x81000101) {
        ret_msg = "Error Code:0x81000101  Info:Can not Find Client MAC";
    } else if (nRc == 0x81000102) {
        ret_msg = "Error Code:0x81000102  Info:MIME Encode Error";
    } else if (nRc == 0x81000103) {
        ret_msg = "Error Code:0x81000103  Info:MIME Encode Attach File Error";
    } else if (nRc == 0x81000104) {
        ret_msg = "Error Code:0x81000104  Info:Registry Type Error";
    } else if (nRc == 0x81000105) {
        ret_msg = "Error Code:0x81000105  Info:Related parameters missing in transaction";
    } else if (nRc == 0x00000030) {
        ret_msg = "Error Code:0x00000030  Info:CKR_DEVICE_ERROR: Some problem has occurred with the token and/or slot";
    } else if (nRc == 0x00000031) {
        ret_msg = "Error Code:0x00000031  Info:CKR_DEVICE_MEMORY: The token does not have sufficient memory to perform the requested function";
    } else if (nRc == 0x00000032) {
        ret_msg = "Error Code:0x00000032  Info:CKR_DEVICE_REMOVED: The token was removed from its slot during the execution of the function";
    } else if (nRc == 0x000000A0) {
        ret_msg = "Error Code:0x000000A0  Info:CKR_PIN_INCORRECT: The specified PIN is incorrect";
    } else if (nRc == 0x000000A1) {
        ret_msg = "Error Code: Info:CKR_PIN_INVALID: The specified PIN has invalid characters in it, or does not meet password policy";
    } else if (nRc == 0x000000A2) {
        ret_msg = "Error Code:0x000000A2  Info:CKR_PIN_LEN_RANGE: The specified PIN is too long or too short";
    } else if (nRc == 0x000000A3) {
        ret_msg = "Error Code:0x000000A3  Info:CKR_PIN_EXPIRED: The specified PIN has expired";
    } else if (nRc == 0x000000A4) {
        ret_msg = "Error Code:0x000000A4  Info:CKR_PIN_LOCKED: The specified PIN is locked and cannot be used";
    } else if (nRc == 0x000000E0) {
        ret_msg = "Error Code:0x000000E0  Info:CKR_TOKEN_NOT_PRESENT: The token was not present in its slot at the time that the function was invoked";
    } else if (nRc == 0x000000E1) {
        ret_msg = "Error Code:0x000000E1  Info:CKR_TOKEN_NOT_RECOGNIZED: The Cryptoki library and/or slot does not recognize the token in the slot";
    } else if (nRc == 0x000000E2) {
        ret_msg = "Error Code:0x000000E2  Info:CKR_TOKEN_WRITE_PROTECTED: The requested action could not be performed because the token is write-protected";
    } else if (nRc == 0x00000100) {
        ret_msg = "Error Code:0x00000100  Info:CKR_USER_ALREADY_LOGGED_IN: User cannot be logged into the session because it is already logged into the session";
    } else if (nRc == 0x00000101) {
        ret_msg = "Error Code:0x00000101  Info:CKR_USER_NOT_LOGGED_IN: The desired action cannot be performed because the appropriate user is not logged in";
    } else if (nRc == 0x00000102) {
        ret_msg = "Error Code:0x00000102  Info:CKR_USER_PIN_NOT_INITIALIZED: Normal user's PIN has not yet been initialized";
    } else if (nRc < 0x81000000) {
        ret_msg = "Error Code:" + "0x" + parseInt(nRc, 10).toString(16) + "  Info:PKCS#11 Error";
    }
    //end of ++Seek 20140417 from chars: p11 error code changed to single error msg again.
    else if (nRc == 0x81000200) {
        ret_msg = "Error Code:0x81000200  Info:Problems connected with the host";
    } else if (nRc == 0x81000201) {
        ret_msg = "Error Code:0x81000201  Info:Host response to the message received timeout";
    } else if (nRc == 0x81000202) {
        ret_msg = "Error Code:0x81000202  Info:Host response to information is incorrect message format";
    } else if (nRc == 0x81000203) {
        ret_msg = "Error Code:0x81000203  Info:Host response to the message received timeout";
    } else if (nRc == 0x81000300) {
        ret_msg = "Error Code:0x81000300  Info:Local time error";
    }
    else if (nRc == 0x81000301) {
        ret_msg = "Error Code:0x81000301  Info:Initial connect to local server timeout";
    }
    else if (nRc == 0x81000302) {
        ret_msg = "Error Code:0x81000302  Info:Local server fail to initial signature";
    }
    else if (nRc == 0x81000303) {
        ret_msg = "Error Code:0x81000303  Info:Local Server fail to parse json";
    }
    else if (nRc == 0x81000304) {
        ret_msg = "Error Code:0x81000304  Info:Error in calling local server method";
    }
    else if (nRc == 0x81000305) {
        ret_msg = "Error Code:0x81000305  Info:Json data error";
    }
    else if (nRc == 0x81000306) {
        ret_msg = "Error Code:0x81000306  Info:Json data error";
    }
    else if (nRc == 0x81000307) {
        ret_msg = "Error Code:0x81000307  Info:Error in calling local server method";
    }
    else if (nRc == 0x81000308) {
        ret_msg = "Error Code:0x81000308  Info:UCTTime error";
    }
    else if (nRc == 0x81000309) {
        ret_msg = "Error Code:0x81000309  Info:Local server error";
    } else if (nRc == 0x83000001) {
        ret_msg = "Error Code:0x83000001  Info:Unauthorized website";
    }
    else if (nRc == 0x83000002) {
        ret_msg = "Error Code:0x83000002  Info:Connection path error";
    }
    else if (nRc == 0x83000003) {
        ret_msg = "Error Code:0x83000003  Info:Request command error";
    } else if (nRc == 0x81100001) {
        
        //Larry 20180321
    	//ret_msg = data.errMsg;
    	if(comErrormessage == "activex"){
    		ret_msg = SP11FileUpload_Control().errMsg;
    	}else{
    		ret_msg = data.errMsg;
    	}
    } else if (nRc == 0x81100002) {
        
        //Larry 20180321
    	if(comErrormessage == "activex"){
    		errorCode = getErrorCode(SP11FileUpload_Control().errMsg, 'ERRORCODE');
    	}else{
    		errorCode = getErrorCode(data.errMsg, 'ERRORCODE');
    	}
        if (errorCode == "015") {
            ret_msg = "Error Code:CA-Server 015  Info:Verify Client Data Error, please make sure you have the right certificate";
        } else if (errorCode == "018") {

            //Larry 20180321
        	if(comErrormessage == "activex"){
        		ret_msg = ParseErrorMessage(SP11FileUpload_Control().errMsg, 'DETAIL');
        	}else{
        		ret_msg = ParseErrorMessage(data.errMsg, 'DETAIL');
        	}
        } else if (errorCode == "019") {

            //Larry 20180321
        	if(comErrormessage == "activex"){
        		ret_msg = ParseErrorMessage(SP11FileUpload_Control().errMsg, 'DETAIL');
        	}else{
        		ret_msg = ParseErrorMessage(data.errMsg, 'DETAIL');
        	}
        } else if (errorCode == "026") {
            
            //Larry 20180321
        	if(comErrormessage == "activex"){
        		ret_msg = ParseErrorMessage(SP11FileUpload_Control().errMsg, 'DETAIL');
        	}else{
        		ret_msg = ParseErrorMessage(data.errMsg, 'DETAIL');
        	}
        } else {
            ret_msg = "Error Code:CA-Server " + errorCode + "  Info:System error, please contact support";
        }
    } else if (nRc == 0x81100003) {
        
        //Larry 20180321
    	if(comErrormessage == "activex"){
    		errorCode = getErrorCode(SP11FileUpload_Control().errMsg, 'ERRORCODE');
    	}else{
    		errorCode = getErrorCode(data.errMsg, 'ERRORCODE');
    	}
        ret_msg = "Error Code:CA-Server " + errorCode + "  Info:System error, please contact support";
    } else if (nRc == 0x81100004) {
        
        //Larry 20180321
    	if(comErrormessage == "activex"){
    		ret_msg = ParseErrorMessage(SP11FileUpload_Control().errMsg, 'DETAIL');
    	}else{
    		ret_msg = ParseErrorMessage(data.errMsg, 'DETAIL');
    	}
    } else if (nRc == 0x81100005) {

    	//Larry 20180321
    	if(comErrormessage == "activex"){
    		ret_msg = SP11FileUpload_Control().errMsg;
    	}else{
    		ret_msg = data.errMsg;
    	}
    } else if (nRc == 0x81100006) {

    	//Larry 20180321
    	if(comErrormessage == "activex"){
    		ret_msg = SP11FileUpload_Control().errMsg;
    	}else{
    		ret_msg = data.errMsg;
    	}
    }
    //Seek 20140214 end
    else if ((nRc >= 0x81200001) && (nRc <= 0x812FFFFF)) {
        ret_msg = "Error Code:" + "0x" + parseInt(nRc, 10).toString(16) + "  Info:Access Windows Registry Error. Code: " + "0x" + parseInt(nRc - 0x81200000, 10).toString(16);
    } else {
        ret_msg = "0x" + parseInt(nRc, 10).toString(16);
    }
    return ret_msg;
}

function getErrorCode(errMsg, name) {
    var vars = errMsg.split("&&");
    var varStr = '';
    for (var i = 0; i < vars.length; i++) {
        var pair = vars[i].split("=");
        if (pair[0] == name) {
            varStr = pair[1];
        }
    }
    return varStr;
}

function ParseErrorMessage(errMsg, name) {
    var vars = errMsg.split("&&");
    var varStr = '';
    for (var i = 0; i < vars.length; i++) {
        var pair = vars[i].split("=");
        if (pair[0] == name) {
            var rep = pair[1].replace(/\\u/g, "||");
            rep = rep.toUpperCase();
            var varArr = rep.split("||");
            for (var j = 1; j < varArr.length; j++) {
                var uni = '"\\u' + varArr[j] + '"';
                if (/^[A-F\d]{4}$/.test(varArr[j])) {
                    varStr = varStr + eval(uni);
                }
            }
            return varStr;
        }
    }
    alert('Query Variable ' + name + ' not found');
}





//Larry 20180809
var urllocation = "https://192.168.40.26:8004";
//var urllocation = "http://127.0.0.1:80";
var strTokenType = "2";

//Larry 20180613

var component="";
checkComponentInstall();


function checkComponentInstall(){
	
	var rv = -1;
	
	var ua = navigator.userAgent;
  	if (navigator.appName == 'Microsoft Internet Explorer'){
	    var re  = new RegExp("MSIE ([0-9]{1,}[\.0-9]{0,})");
	    if (re.exec(ua) != null){
	    	rv = parseFloat( RegExp.$1 );
	      	var agentWeb = 'Microsoft webgolds';
	    }
 	} else if (navigator.appName == 'Netscape'){
	    var re  = new RegExp("Trident/.*rv:([0-9]{1,}[\.0-9]{0,})");  //for IE 11
	    if (re.exec(ua) != null)
	    rv = parseFloat( RegExp.$1 );
  	}
	
	
	//printLanguage('js - dispinfo', determineLanguage(document.getElementById("language").value));
	//if(navigator.appName == 'Microsoft Internet Explorer' && rv==9 || rv==8){
  	
	
	
	var CTBC_FB = document.getElementById("CTBCPKIAPI");
	var ReturnValue = CTBC_FB.version;
	//console.log("CTBC_FB.version = "+ReturnValue);
	
	if(typeof ReturnValue != "undefined"){
		component = "activex"; 
		
	}else{
		//Larry 20180817
		if(rv==8||rv==9){
			component = "activex";
			alert("Please install component");
			
			//Larry 1018 language寫死
			window.location.href="./Envdownload.jsp?lang=0";
			return;
		}

		component = "localserver";
  		load();
		
	}
		
	
}
/*var CTBC_FB = document.getElementById("CTBCPKIAPI");
var ReturnValue = CTBC_FB.version;


if(typeof ReturnValue != "undefined"){
//if(navigator.appName == 'Microsoft Internet Explorer'){

	//console.log("Ecomponent: ActiveXObject");
 
	component = "activex";
	
	if (document.getElementById("CHTCertObj")!=null){
		document.getElementById("CHTCertObj").style.backgroundColor="#CC00FF";
	}

}else{ //localserver
	//console.log("Ecomponent: localserver");
	component = "localserver"; 
		
	if (document.getElementById("CHTCertObj")!=null){
		document.getElementById("CHTCertObj").style.backgroundColor="#CC00FF";
	}
	
}*/

var ServletPathCHT = urllocation+"/RAWeb/dealTrans.do";
//var ObjectVerCHT="1.0.0.1"  //新元件版號 元件換版後該變數要跟著調整 中間以"."號分隔
var ObjectVerCHT="1.0.0.40"  //新元件版號 元件換版後該變數要跟著調整 中間以"."號分隔

/* cht元件下載物件資訊 - End */

var base64EncodeChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
var base64DecodeChars = new Array(
    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, -1, -1, 63,
    52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -1, -1, -1,
    -1,  0,  1,  2,  3,  4,  5,  6,  7,  8,  9, 10, 11, 12, 13, 14,
    15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1,
    -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40,
    41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1, -1, -1, -1);

	
function checkCHT4SP()
{
  var CHTObj;
  try {
	  /*if(!window.ActiveXObject)
		  if(navigator.mimeTypes['application/x-ctbccryptoapi']==undefined)
   			return false;*/
		
    CHTObj = new ActiveXObject("CTBC.ctbccryptoapi");
    if(CHTObj!=null)
    {
		 delete CHTObj;
		 return true;
    }
	return false; 
	
	}catch (e) {
		return false;
	}
}	
	
/* fbtleSecure*/
function plugin0()
{
	if (!checkCHT4SP()){
			alert("請先安裝新版CTBC網銀交易元件");
			//Larry 1018 language寫死
			window.location.href="./Envdownload.jsp?lang=0";
	}else
			return document.getElementById('CTBCPKIAPI');
	
    
}
//Larry 20180309
//SP11FileUpload_Control = plugin0();
if(component == "activex"){
	SP11FileUpload_Control = plugin0;
}


//Larry 20180309 same function name with initlocalserver 
//function load()
function loadactivex()
{
    //0 為英文 1為繁體中文 2為簡體中文
	//Larry 1018 language寫死
    SP11FileUpload_Control().strlanguage= "1";  //chrome第一次load網頁時，在此對property設值無效。因此需在每個script function內重新設值
    //console.log("document.all.language.value="+document.all.language.value);
    
}

//Larry 20180312
function loadtestUpload(){
	if(component == "activex"){
		loadactivex();
	}else{
		load();
	}
}

function showVersion()
{
	//Larry 20180309
	//alert(SP11FileUpload_Control.version);
	if(component == "activex"){
		alert(SP11FileUpload_Control().version);
	}else{
		$.blockUI({ fadeIn: 0 });

		var request = {
			func: 'GetVersion'
		};

		sendHttpRequest(getCertServerUrl(), 'POST', JSON.stringify(request), {
			onSuccess: function (responseText) {
				if (responseText) {
					var data = JSON.parse(responseText);
					if (data.ret == 0x0) {
						var pluginVersion = data.version;
						alert(pluginVersion);
					} else {
						showErrorMessageCHT(data.ret, data);
					}
				}

				$.unblockUI();
			},
			onError: showErrorMessageCHT
		}, true);
	}
}

//Larry 20180105
function CTBC_GetTokenType(tokenValue) {	//取得載具類別
    var nTokenType = "eToken";
    if (tokenValue == 1)
        nTokenType = "iKey";
    else if (tokenValue == 2)
        nTokenType = "eToken";
    return nTokenType;
}
//Larry 20180105
function CTBC_DetectToken()
{
	//Larry 20180309
	//alert(SP11FileUpload_Control.version);
	if(component == "activex"){
		//var strLanguage = "1";			//語系(0:英文 1:繁體中文 2:簡體中文, 未設或其它值為繁體中文)
		
		//Larry 1018 language寫死
		var strLanguage = "1";
		
		var strTokenType = "2";			//載具指定(1:ikey2032, 2:etoken5200, 3:Gemplus, 未設為1)
		var strTokenSerialNo = "";		//載具序號(未設則指定符合strTokenType的第一個slot上的載具)
		//var strPinPad = "0";		//動態鍵盤(0:true/1:false)(請執行簽章做測試)
		//var strPinPad = document.all.PinPad.value;;
				
		if(document.all.PinPad == undefined){
			SP11FileUpload_Control().strPinPad = 1;
		}else{
			if(document.all.PinPad.value == "1" || (document.all.PinPad.value == "false"))
				SP11FileUpload_Control().strPinPad = 1;
			else
				SP11FileUpload_Control().strPinPad = 0;
		}
		
		var strCaptcha = "1";	//是否啟用圖形驗證碼(0:true/1:false)
		var strCaptchaLength = "4";	//圖形驗證碼位數(4/5/6)
		var strCaptchaType = "1";	//圖形驗證碼類型(0:Number and Letter/1:Number only/2:Letter only)
		
		//var strPullDeviceOut = document.all.PullDeviceOut.value; //(0:關閉/1:開啟)
		if(document.all.PullDeviceOut == undefined){
			SP11FileUpload_Control().strPullDeviceOut = 1;
		}else{
			if(document.all.PullDeviceOut.value == "1" || (document.all.PullDeviceOut.value == "false"))
				SP11FileUpload_Control().strPullDeviceOut = 1;
			else
				SP11FileUpload_Control().strPullDeviceOut = 0;
		}
		
		SP11FileUpload_Control().strLanguage = strLanguage;
		SP11FileUpload_Control().strTokenType = strTokenType;
		SP11FileUpload_Control().strTokenSerialNo = strTokenSerialNo;
		//SP11FileUpload_Control().strPinPad = strPinPad;
		SP11FileUpload_Control().strCaptcha = strCaptcha; 
		SP11FileUpload_Control().strCaptchaLength = strCaptchaLength;
		SP11FileUpload_Control().strCaptchaType = strCaptchaType;
		
		
		var nRc = SP11FileUpload_Control().DetectToken();
		if (nRc == 0){
			alert("Token種類為:" + CTBC_GetTokenType(SP11FileUpload_Control().strTokenType) + "\n序號為:" + SP11FileUpload_Control().strTokenSerialNo + "的載具");
		}else{
			alert(nRc);		
		}
	}else{
		$.blockUI({ fadeIn: 0 });

		var request = {
			func: 'DetectToken'
		};
		
		//Larry 1018 language寫死
		var strLanguage = "1";
		
		var strTokenType = "2";			//載具指定(1:ikey2032, 2:etoken5200, 3:Gemplus, 未設為1)
		var strTokenSerialNo = "";		//載具序號(未設則指定符合strTokenType的第一個slot上的載具)
		//var strPinPad = "0";		//動態鍵盤(0:true/1:false)(請執行簽章做測試)
		
		
		
		var strCaptcha = "1";	//是否啟用圖形驗證碼(0:true/1:false)
		var strCaptchaLength = "4";	//圖形驗證碼位數(4/5/6)
		var strCaptchaType = "1";	//圖形驗證碼類型(0:Number and Letter/1:Number only/2:Letter only)
		
		//var strPullDeviceOut = document.all.PullDeviceOut.value; //(0:關閉/1:開啟)
		
		request.strLanguage = strLanguage;
		request.strTokenType = strTokenType;
		/*request.strTokenSerialNo = strTokenSerialNo;
		request.strPinPad = strPinPad;
		request.strCaptcha = strCaptcha; 
		request.strCaptchaLength = strCaptchaLength;
		request.strCaptchaType = strCaptchaType;
		
		request.strPullDeviceOut  = strPullDeviceOut;*/
		
		sendHttpRequest(getCertServerUrl(), 'POST', JSON.stringify(request), {
			onSuccess: function (responseText) {
				if (responseText) {
					var data = JSON.parse(responseText);
					if (data.ret == 0x0) {
						var pluginVersion = data.version;
						alert("Token種類為:" + CTBC_GetTokenType(strTokenType) + "\n序號為:" + data.strTokenSerialNo + "的載具");
					} else {
						//console.log("data.ret="+data.ret);
						showErrorMessageCHT(data.ret, data);
					}
				}

				$.unblockUI();
			},
			onError: showErrorMessageCHT
		}, true);
	}
	
}
//Larry 20180105
function CTBC_P7SignedData()
{
	alert("in CTBC_P7SignedData");
	/*var strLanguage = "1";			//語系(0:英文 1:繁體中文 2:簡體中文, 未設或其它值為繁體中文)
	var strTokenType = "2";			//載具指定(1:ikey2032, 2:etoken5200, 3:Gemplus, 未設為1)
	var strTokenSerialNo = "";		//載具序號(未設則指定符合strTokenType的第一個slot上的載具)
	var strPinPad = "0";		//動態鍵盤(0:true/1:false)(請執行簽章做測試)
	var strCaptcha = "1";	//是否啟用圖形驗證碼(0:true/1:false)
	var strCaptchaLength = "4";	//圖形驗證碼位數(4/5/6)
	var strCaptchaType = "1";	//圖形驗證碼類型(0:Number and Letter/1:Number only/2:Letter only)
	var strPullDeviceOut = "0";	//是否啟用載具插拔驗證(0:關閉)
	var strDigestAlgorithm = "0";  //(0:SHA1 / 1:SHA256 ,未設或其它值依憑證判斷)*/
	
	/*SP11FileUpload_Control().strLanguage = strLanguage;
	SP11FileUpload_Control().strPullDeviceOut = strPullDeviceOut;
	SP11FileUpload_Control().strTokenType = strTokenType;
	SP11FileUpload_Control().strDigestAlgorithm = strDigestAlgorithm;
	
	SP11FileUpload_Control().strTokenSerialNo = strTokenSerialNo;
	SP11FileUpload_Control().strPinPad = strPinPad;
	SP11FileUpload_Control().strCaptcha = strCaptcha; 
	SP11FileUpload_Control().strCaptchaLength = strCaptchaLength;
	SP11FileUpload_Control().strCaptchaType = strCaptchaType;*/
	
	/*SP11FileUpload_Control().strLanguage = strLanguage;
	SP11FileUpload_Control().strPullDeviceOut = strPullDeviceOut;
	SP11FileUpload_Control().strTokenType = strTokenType;
	SP11FileUpload_Control().strTokenSerialNo = strTokenSerialNo;
	
	SP11FileUpload_Control().strLanguage = "1";
	SP11FileUpload_Control().strPullDeviceOut = "0"; 
	SP11FileUpload_Control().strInAccountID = "000000000000";
	SP11FileUpload_Control().strOutAccountID = "111111111111";    
	SP11FileUpload_Control().strTradeAmount = "10000"; 
	
	SP11FileUpload_Control().strSubject = "";
	SP11FileUpload_Control().strIssuerName = "";
	SP11FileUpload_Control().strSerialNum = "";
	SP11FileUpload_Control().strNotAfter = "0";
	SP11FileUpload_Control().strKeyUsage = "";	
	SP11FileUpload_Control().strDigestAlgorithm = "0";	
	
	//var result = parseInt(SP11FileUpload_Control().P7SignedData(document.getElementById("strSTB").value), 10);
	var testresult = -1;
	testresult = SP11FileUpload_Control().P7SignedData();

	if (testresult == 0) {
		alert(SP11FileUpload_Control().strB64P7SignData);
	} else {	
		alert(result);
	}*/
	
	//Larry 1018 language寫死
	var strLanguage = "1";			//語系(0:英文 1:繁體中文 2:簡體中文, 未設或其它值為繁體中文)
	
	//var strTokenType = "2";			//載具指定(1:ikey2032, 2:etoken5200, 3:Gemplus, 未設為1)
	//var strTokenSerialNo = "";		//載具序號(未設則指定符合strTokenType的第一個slot上的載具)
	var strPinPad = "0";		//動態鍵盤(0:true/1:false)(請執行簽章做測試)
	var strPullDeviceOut = "0";	//是否啟用載具插拔驗證(0:關閉)
	var strSTB = "StringToBeSigned";
	
	SP11FileUpload_Control.strLanguage = strLanguage;
	SP11FileUpload_Control.strPullDeviceOut = strPinPad; 
	SP11FileUpload_Control.strPullDeviceOut = strPullDeviceOut; 
	
	SP11FileUpload_Control.strInAccountID = "000000000000";
	SP11FileUpload_Control.strOutAccountID = "111111111111";    
	SP11FileUpload_Control.strTradeAmount = "10000"; 

	SP11FileUpload_Control.strSubject = "";
	SP11FileUpload_Control.strIssuerName = "";
	SP11FileUpload_Control.strSerialNum = "";
	SP11FileUpload_Control.strNotAfter = "0";
	SP11FileUpload_Control.strKeyUsage = "";	
	SP11FileUpload_Control.strDigestAlgorithm = "0";		
	
	var result = parseInt(SP11FileUpload_Control.P7SignedData(strSTB), 10);
	//alert("0x"+result.toString(16));	
	if (result != 0) {
		alert(result);
	} else {	
		alert(SP11FileUpload_Control.strB64P7SignData);
	}
	
	
}


//--fbtleSecure
function utf16to8(str) {
    var out, i, len, c;

    out = "";
    len = str.length;
    for(i = 0; i < len; i++) {
 c = str.charCodeAt(i);
 if ((c >= 0x0001) && (c <= 0x007F)) {
     out += str.charAt(i);
 } else if (c > 0x07FF) {
     out += String.fromCharCode(0xE0 | ((c >> 12) & 0x0F));
     out += String.fromCharCode(0x80 | ((c >>  6) & 0x3F));
     out += String.fromCharCode(0x80 | ((c >>  0) & 0x3F));
 } else {
     out += String.fromCharCode(0xC0 | ((c >>  6) & 0x1F));
     out += String.fromCharCode(0x80 | ((c >>  0) & 0x3F));
 }
    }
    return out;
}

function base64encode(str) {
    var out, i, len;
    var c1, c2, c3;
 if (str == null) str = "";
  str = utf16to8(str);
 
 len = str.length;
    i = 0;
    out = "";
    while(i < len) {
 c1 = str.charCodeAt(i++) & 0xff;
 if(i == len)
 {
     out += base64EncodeChars.charAt(c1 >> 2);
     out += base64EncodeChars.charAt((c1 & 0x3) << 4);
     out += "==";
     break;
 }
 c2 = str.charCodeAt(i++);
 if(i == len)
 {
     out += base64EncodeChars.charAt(c1 >> 2);
     out += base64EncodeChars.charAt(((c1 & 0x3)<< 4) | ((c2 & 0xF0) >> 4));
     out += base64EncodeChars.charAt((c2 & 0xF) << 2);
     out += "=";
     break;
 }
 c3 = str.charCodeAt(i++);
 out += base64EncodeChars.charAt(c1 >> 2);
 out += base64EncodeChars.charAt(((c1 & 0x3)<< 4) | ((c2 & 0xF0) >> 4));
 out += base64EncodeChars.charAt(((c2 & 0xF) << 2) | ((c3 & 0xC0) >>6));
 out += base64EncodeChars.charAt(c3 & 0x3F);
    }
    //document.all['txt2'].value = out;
    return out;
}

//++Seek 20140318 change ui lang dynamically
function SetUIwordingCHT(componentponent){
	switch (componentponent.strLanguage){
		case 0: //en
		case "0":
		{
			componentponent.strStaticPINWinText = "Enter PIN Code"; //"輸入PIN視窗";
			componentponent.strStaticChangePINWinText = "Change Password"; // "輸入Change PIN dlg視窗";  	
			componentponent.strStaticEnterPIN = "Please Enter Token PIN Code"; // "输入PIN";
			componentponent.strStaticCaptcha = "Captcha"; // "Captcha";
			componentponent.strStaticOldPIN = "Old Password"; // "Old PIN";
			componentponent.strStaticNewPIN = "New Password"; // "New PIN";
			componentponent.strStaticREenterPIN = "Confirm Password"; // "REenter PIN";
			//++Seek 20150812
			componentponent.strStaticChangePINFormatErrorDigit = "Only numbers are allowed in new password";
			componentponent.strStaticChangePINFormatErrorLength = "New password length should be 6 to 12";
			//end of ++Seek 20150812
			componentponent.strStaticCert = "Selected Cert CN"; // "Cert CN";	
			componentponent.strStaticBtnCancel = "Cancel";
			componentponent.strStaticChangePINDefault = "Please do not enter default password";
			componentponent.strStaticChangePINMismatch = "Content of New PIN Code and confirm PIN Code are not consistent";
			componentponent.strStaticPINLock = "Token has been locked";
			componentponent.strStaticLoginWinText = "Certificate management";
			componentponent.strStaticPINError = "Password Error";
			componentponent.strStaticLoginFail = "Password Error";
			componentponent.strStaticFileWinText = "File List";
			componentponent.strStaticFilePath = "File Path";
			componentponent.strStaticFileAdd = "Add";
			componentponent.strStaticFileClear = "Clear";
			componentponent.strStaticFileUpload = "Upload";
			componentponent.strStaticFileLimitWinText = "Upload file number limit";
			componentponent.strStaticFileNumLimit = "Max upload file number";
			//componentponent.strStaticFileProgressWinText = "Chinatrust";
			componentponent.strStaticFileProgressWinText = "Data Processing";
			componentponent.strStaticSec = "Sec";
			componentponent.strStaticRePlug = "Please pull out and plug in the certificate device";
			componentponent.strStaticCertWinText = "Please choose a certificate";
			componentponent.strStaticCertSub = "Subject";
			componentponent.strStaticCertNotAfter = "Expire";
			componentponent.strStaticProgressWinText = "Data Processing";
			componentponent.strStaticBtnOK = "Confirm";
			componentponent.strStaticBtnClear = "Correct";
		}
		break;
		case 1: //tw
		case "1":
		{
			componentponent.strStaticPINWinText = "輸入密碼"; //"輸入PIN視窗";
			componentponent.strStaticChangePINWinText = "變更密碼"; // "輸入Change PIN dlg視窗";  	
			componentponent.strStaticEnterPIN = "請輸入憑證載具密碼"; // "输入PIN";
			componentponent.strStaticCaptcha = "圖形驗證碼"; // "Captcha";
			componentponent.strStaticOldPIN = "舊密碼"; // "Old PIN";
			componentponent.strStaticNewPIN = "新密碼"; // "New PIN";
			componentponent.strStaticREenterPIN = "確認新密碼"; // "REenter PIN";
			//++Seek 20150812
			componentponent.strStaticChangePINFormatErrorDigit = "新密碼限用數字";
			componentponent.strStaticChangePINFormatErrorLength = "新密碼限長度為6~12碼";
			//end of ++Seek 20150812
			componentponent.strStaticCert = "已選取Cert CN"; // "Cert CN";
			componentponent.strStaticLoginFail = "密碼錯誤";
			
			componentponent.strStaticBtnCancel = "取消";
			componentponent.strStaticChangePINDefault = "請勿輸入預設密碼";
			componentponent.strStaticChangePINMismatch = "新密碼和確認新密碼不一致";
			componentponent.strStaticPINLock = "已鎖碼";
			componentponent.strStaticLoginWinText = "憑證管理";
			componentponent.strStaticPINError = "密碼錯誤";
			componentponent.strStaticFileWinText = "檔案列表";
			componentponent.strStaticFilePath = "檔案路徑";
			componentponent.strStaticFileAdd = "加入";
			componentponent.strStaticFileClear = "清除";
			componentponent.strStaticFileUpload = "上傳";
			componentponent.strStaticFileLimitWinText = "上傳檔案限制";
			componentponent.strStaticFileNumLimit = "最多上傳檔案數量";
			//componentponent.strStaticFileProgressWinText = "中國信託";
			componentponent.strStaticFileProgressWinText = "資料處理中";
			componentponent.strStaticSec = "秒";
			componentponent.strStaticRePlug = "請拔插憑證載具";
			componentponent.strStaticCertWinText = "請選擇一張憑證";
			componentponent.strStaticCertSub = "主旨";
			componentponent.strStaticCertNotAfter = "到期日";
			componentponent.strStaticProgressWinText = "資料處理中";
			componentponent.strStaticBtnOK = "確認";
			componentponent.strStaticBtnClear = "更正";
		}
		break;
		case 2: //cn
		case "2":
		{
			componentponent.strStaticPINWinText = "输入密码"; //"輸入PIN視窗";
			componentponent.strStaticChangePINWinText = "变更密码"; // "輸入Change PIN dlg視窗";  	
			componentponent.strStaticEnterPIN = "请输入证书载具密码"; // "输入PIN";
			componentponent.strStaticCaptcha = "图形验证码"; // "Captcha";
			componentponent.strStaticOldPIN = "旧密码"; // "Old PIN";
			componentponent.strStaticNewPIN = "新密码"; // "New PIN";
			componentponent.strStaticREenterPIN = "确认新密码"; // "REenter PIN";
			//++Seek 20150812
			componentponent.strStaticChangePINFormatErrorDigit = "新密码限用数字";
			componentponent.strStaticChangePINFormatErrorLength = "新密码限长度为6~12码";
			//end of ++Seek 20150812
			componentponent.strStaticCert = "已选取Cert CN"; // "Cert CN";
			componentponent.strStaticLoginFail = "密码错误";
			
			componentponent.strStaticBtnCancel = "取消";
			componentponent.strStaticChangePINDefault = "请勿输入预设密码";
			componentponent.strStaticChangePINMismatch = "新密码和确认新密码不一致";
			componentponent.strStaticPINLock = "已锁码";
			componentponent.strStaticLoginWinText = "证书管理";
			componentponent.strStaticPINError = "密码错误";
			componentponent.strStaticFileWinText = "档案列表";
			componentponent.strStaticFilePath = "档案路径";
			componentponent.strStaticFileAdd = "加入";
			componentponent.strStaticFileClear = "清除";
			componentponent.strStaticFileUpload = "上传";
			componentponent.strStaticFileLimitWinText = "上传档案限制";
			componentponent.strStaticFileNumLimit = "最多上传档案数量";
			//componentponent.strStaticFileProgressWinText = "中国信托";
			componentponent.strStaticFileProgressWinText = "资料处理中";
			componentponent.strStaticSec = "秒";
			componentponent.strStaticRePlug = "请拔插证书载具";
			componentponent.strStaticCertWinText = "请选择一张证书";
			componentponent.strStaticCertSub = "主旨";
			componentponent.strStaticCertNotAfter = "到期日";
			componentponent.strStaticProgressWinText = "资料处理中";
			componentponent.strStaticBtnOK = "确认";
			componentponent.strStaticBtnClear = "更正";
		}
		break;
	}
}
//end of Seek 20140318 change ui lang dynamically


//Larry 20180313
function showErrorMessageCHT(nRc, data) {//localserver
	//Larry 1018 language寫死
	alert(errorCode2Message(nRc, data));
	
	$.unblockUI();
}


//Larry 20180313
function SecureData(updateTransResultCallback, strTradeAlert) {
	
	if(component == "activex"){
		return SecureDataCHT(updateTransResultCallback);
	}else{
		SecureDataCHT(updateTransResultCallback, strTradeAlert, sentMIMECallback);
		return "new";
	}
	
}
/* 同時支援新舊元件 - End */


/* cht元件所使用的顯示錯誤訊息函式(新增) - End */
/* 呼叫CHT元件進行交易的函式(新增) - Start */
function SecureDataCHT(updateTransResultCallback, strTradeAlert, sentMIMECallback) {
	if(component == "activex"){
		//Larry 1018 language寫死
		SP11FileUpload_Control().strLanguage = "1";//0 en, 1  cht,  2 cn
		
		SP11FileUpload_Control().strUrl = ServletPathCHT;
	SP11FileUpload_Control().strIssureName = "";
	SP11FileUpload_Control().strKeyUsage = "192";
	SP11FileUpload_Control().strSubject = "OU=8220901-CTCB";

	
   
	//掃 所有的form data
	var isUpload ="false";
	var submission_string = "";
	var nRc = 0;
	var strTxCode = "nothing", strFunction = "nothing", strBizId = "nothing", strSESSIONID = "nothing", strVERIFYUSER = "nothing", strCOUNTRYCODE = "nothing", strCUSTOMERID = "nothing", strUSERID = "nothing", strCAID = "nothing", strP1 = "nothing", strP2 = "nothing", strP3 = "nothing", strP4 = "nothing", strP5 = "nothing";
	var strSessionID = "nothing";
	for (var form_loop = 0; form_loop < document.forms.length; form_loop++) {
		if (formName != document.forms [form_loop].name) {
			continue;
		}
		for (var elems = 0; elems < document.forms[form_loop].length; elems++) {
			if (document.forms[form_loop].elements[elems].name != "" && document.forms[form_loop].elements[elems].name != "szAToBCipher") {
				if (document.forms[form_loop].elements[elems].type == "radio" || document.forms[form_loop].elements[elems].type == "checkbox") {
					if (document.forms[form_loop].elements[elems].checked) {
						document.forms[form_loop].elements[elems].checked = true;
						submission_string += document.forms[form_loop].elements[elems].name + "=" + base64encode(document.forms[form_loop].elements[elems].value) + "&";
						//submission_string += document.forms[form_loop].elements[elems].name + "=" + (document.forms[form_loop].elements[elems].value) + "&";
					} else {
						document.forms[form_loop].elements[elems].checked = false;
					}
				} else {
					if (document.forms[form_loop].elements[elems].type == "file") {									                         					
						//submission_string += "|S;e;P;a;R;a;T;e|!!" + document.forms[form_loop].elements[elems].name + "//" + "=" + document.forms[form_loop].elements[elems].value + "&";
						submission_string += "|S;e;P;a;R;a;T;e|!!" + document.forms[form_loop].elements[elems].name + "//" + "=" + base64encode(document.forms[form_loop].elements[elems].value) + "&";
						//var filename = document.forms[form_loop].elements[elems].name;
						//if (filename == "FILE1")
						//	filename = "FILE1_CHT"
						//submission_string += "|S;e;P;a;R;a;T;e|!!" + filename + "//" + "=" + base64encode(document.forms[form_loop].elements[elems].value) + "&";
						isUpload = "true";
					} else {				
						submission_string += document.forms[form_loop].elements[elems].name + "=" + base64encode(document.forms[form_loop].elements[elems].value) + "&";
					}
				}
			}
		}
	}
    
	//組MIME Data
	strSessionID = "TxCode=" + strTxCode + "&&Function=" + strFunction + "&&BizID=" + strBizId + "&&SESSIONID=" + strSESSIONID;
	strSessionID = strSessionID + "&&VERIFYUSER=" + strVERIFYUSER + "&&COUNTRYCODE=" + strCOUNTRYCODE + "&&CUSTOMERID=" + strCUSTOMERID;
	strSessionID = strSessionID + "&&CAID=" + strCAID + "&&P1=" + strP1 + "&&P2=" + strP2 + "&&P3=" + strP3 + "&&P4=" + strP4 + "&&P5=" + strP5 + "&&";
	SP11FileUpload_Control().strSessionID = strSessionID;
	//document.all.SignRowData.value = submission_string;

	var APURL = "";
	//mod by seek 20140411 if strtype = 0 clear file1 
	//mod by seek 20140318 modify choose file upload method
	if(document.all.IsUploadFile == undefined){
		if(isUpload == "true"){
			SP11FileUpload_Control().strType = 0;
		}else{
			SP11FileUpload_Control().strType = 1;
		}
	}else{
		if((document.all.IsUploadFile.value == "1") || (document.all.IsUploadFile.value == "false"))
			SP11FileUpload_Control().strType = 1;
		else
			SP11FileUpload_Control().strType = 0;
	}

	//end of mod by seek 20140318 modify choose file upload method
	//end of mod by seek 20140411 if strtype = 0 clear file1 
	
	SP11FileUpload_Control().strNotAfter = "0";
//	SP11FileUpload_Control().strCheckNotAfter = "0"; //TRUE
	SP11FileUpload_Control().strIssureName = "";

	if(document.all.PullDeviceOut == undefined){
		SP11FileUpload_Control().strPullDeviceOut = 1;
	}else{
		if(document.all.PullDeviceOut.value == "1" || (document.all.PullDeviceOut.value == "false"))
			SP11FileUpload_Control().strPullDeviceOut = 1;
		else
			SP11FileUpload_Control().strPullDeviceOut = 0;
	}
		
	if(document.all.PinPad == undefined){
		SP11FileUpload_Control().strPinPad = 1;
	}else{
		if(document.all.PinPad.value == "1" || (document.all.PinPad.value == "false"))
			SP11FileUpload_Control().strPinPad = 1;
		else
			SP11FileUpload_Control().strPinPad = 0;
	}
	
	
	//end of seek 20140421 from chars: if not 1 and false, all set to true(have pinpad)
	//SP11FileUpload_Control().strKeyUsage = "128&&192";
	SP11FileUpload_Control().strKeyUsage = "192";
	SP11FileUpload_Control().strCertExpireBefore="30";//憑證到期前ㄧ個月預警
	//mod by seek 20140218 no captcha
	//SP11FileUpload_Control().strCaptcha=document.all.strCaptcha.value; //動態圖形驗證碼
	//SP11FileUpload_Control().strCaptchaLength=document.all.strCaptchaLength.value;	//動態圖形驗證碼長度
	//SP11FileUpload_Control().strCaptchaType=document.all.strCaptchaType.value;	//圖形驗證碼類型
	SP11FileUpload_Control().strCaptcha = 1; //動態圖形驗證碼 //0: enable; 1:disable
	SP11FileUpload_Control().strCaptchaLength = 4;	//動態圖形驗證碼長度
	SP11FileUpload_Control().strCaptchaType = 1;	//圖形驗證碼類型
	//end of mod by seek 20140218 no captcha
	//SP11FileUpload_Control().strFileMaxNum=document.all.AllowUploadFileNum.value;	//max upload file

	//assign wording
	SetUIwordingCHT(SP11FileUpload_Control());
	
			  	
	  	//larry 20180105  	
		nRc = SP11FileUpload_Control().UploadP7SignedData(submission_string);
		//nRc = SP11FileUpload_Control().P7SignedData(submission_string);

		if (nRc != 0) {
			//Larry 20180313
			showErrorMessageCHT(nRc);
			//document.all.SignResult.value = SP11FileUpload_Control().strB64P7SignData;
			return false;
		} else {
			if(SP11FileUpload_Control().strCertExpireBefore!=""&&SP11FileUpload_Control().strExpireTime!=""){
				
				//Larry 1018 language寫死
		        window.open(urllocation+"/RAWeb/RenewalNotice1.jsp?deadline="+ SP11FileUpload_Control().strExpireTime,"","Height=210px, Width=380px, toolbar=no, menubar=no, scrollbars=no, resizable=no, location=no, status=no");
		  	}
			alert(ParseErrorMessage(SP11FileUpload_Control().errMsg, 'DETAIL'));
			//document.all.SignResult.value = SP11FileUpload_Control().strB64P7SignData;		
			//document.all.SignResult.value = SP11FileUpload_Control().szgenerateCipherData;
			return true;
		}
		for (var form_loop = 0; form_loop < document.forms.length; form_loop++) {
			for (var elems = 0; elems < document.forms[form_loop].length - 3; elems++) {
				if (document.forms[form_loop].elements[elems].name != "szAToBCipher") {
					document.forms[form_loop].elements[elems].disabled = true;
				}
			}
		}
	}else{
		$.blockUI({ fadeIn: 0 });
		
		/**
			Sent file request to localserver
		**/
		//var file = $('#FILE1').get(0).files[0];
		//console.log(document.getElementById("UploadFileName"));
		
		if(document.getElementById("UploadFileName") == undefined)
		{
			sentMIMECallback(strTradeAlert, "");
		}else{
			var file = document.getElementById("UploadFileName").files[0];
			var reader = new FileReader();
			reader.readAsDataURL(file);
			// Callback when readAsDataURL finish
			reader.addEventListener("load", function () {		
				var request = {
					func: 'FileUpload'
				};
				
				
				var fileResult=reader.result.split(",")[1];
				request.selectedFile=fileResult;
				
				request.fileName = btoa(encodeURIComponent(file.name));
			
				sendHttpRequest(getCertServerUrl(), 'POST', JSON.stringify(request), {
					onSuccess: function (responseText) {
						if (responseText) {
							//console.log("responseText : " + responseText);
							var data = JSON.parse(responseText);
							var nRc = data.ret;
							if (nRc == 0) {
								if(data.result == "true") {
									// Combine MIME with filePath and sent to localserver
									sentMIMECallback(strTradeAlert, data.path);
								}
							} else {
							 var result = parseInt(nRc, 10);
								showErrorMessage(result);
							}
						}
						//$.unblockUI();
					},
					
					onError: showErrorMessageCHT
					
				}, true);
			}, false);
		}
	}
}

// LocalServer Callback

function sentMIMECallback(strTradeAlert, filePath) {
	var request = {
		func: 'UploadP7SignedData'
	};
	request.strEnableLog = 1;
		
	//Larry 1018 language寫死
	request.strLanguage = "1";
	
	request.strUrl = ServletPathCHT;
	request.strIssureName = "";
	request.strKeyUsage = "192";  //簽章憑證指定128&&192
	request.strSubject = "OU=8220901-CTCB"; //"OU=BCI Banking" "OU=BCI Banking, OU=FXML"
	request.strNotAfter = "0";
	request.strCertExpireBefore = "30";//憑證到期前ㄧ個月預警
	request.strTokenType = 2;
	request.strTradeAlert = strTradeAlert;

	//request.strType = Number(document.all.IsUploadFile.value);

	if(document.all.PullDeviceOut == undefined){
		request.strPullDeviceOut = 1;
	}else{
		if(document.all.PullDeviceOut.value == "1" || (document.all.PullDeviceOut.value == "false"))
			request.strPullDeviceOut = 1;
		else
			request.strPullDeviceOut = 0;
	}
	
	if(document.all.PinPad == undefined){
		request.strPinPad = 1;
	}else{
		if(document.all.PinPad.value == "1" || (document.all.PinPad.value == "false"))
			request.strPinPad = 1;
		else
			request.strPinPad = 0;
	}
	
	
	request.strCaptcha = 1;
	request.strCaptchaLength = 4;
	request.strCaptchaType = 1;
	//request.strCaptcha=Number(document.all.strCaptcha.value); //1;動態圖形驗證碼
	//request.strCaptchaLength=Number(document.all.strCaptchaLength.value);	//4;動態圖形驗證碼長度
	//request.strCaptchaType=Number(document.all.strCaptchaType.value);	//1;圖形驗證碼類型
	//request.strFileMaxNum=Number(document.all.AllowUploadFileNum.value);	//1;max upload file

	//Larry 20180312
	var isUpload = "false";
	//掃 所有的form data
	//掃 所有的form data
	var submission_string = "";
	var nRc = 0;
	var strTxCode = "nothing", strFunction = "nothing", strBizId = "nothing", strSESSIONID = "nothing", strVERIFYUSER = "nothing", strCOUNTRYCODE = "nothing", strCUSTOMERID = "nothing", strUSERID = "nothing", strCAID = "nothing", strP1 = "nothing", strP2 = "nothing", strP3 = "nothing", strP4 = "nothing", strP5 = "nothing";
	var strSessionID = "nothing";
	for (var form_loop = 0; form_loop < document.forms.length; form_loop++) {
		if (formName != document.forms [form_loop].name) {
			continue;
		}
		
		for (var elems = 0; elems < document.forms[form_loop].length; elems++) {
			if (document.forms[form_loop].elements[elems].name != "" && document.forms[form_loop].elements[elems].name != "szAToBCipher") {
				if (document.forms[form_loop].elements[elems].type == "radio" || document.forms[form_loop].elements[elems].type == "checkbox") {
					if (document.forms[form_loop].elements[elems].checked) {
						document.forms[form_loop].elements[elems].checked = true;
						submission_string += document.forms[form_loop].elements[elems].name + "=" + base64encode(document.forms[form_loop].elements[elems].value) + "&";
						//submission_string += document.forms[form_loop].elements[elems].name + "=" + (document.forms[form_loop].elements[elems].value) + "&";
					} else {
						document.forms[form_loop].elements[elems].checked = false;
					}
				} else {
					if (document.forms[form_loop].elements[elems].type == "file") {		
						//submission_string += "|S;e;P;a;R;a;T;e|!!" + document.forms[form_loop].elements[elems].name + "//" + "=" + document.forms[form_loop].elements[elems].value + "&";
;
						submission_string += "|S;e;P;a;R;a;T;e|!!" + document.forms[form_loop].elements[elems].name + "//" + "=" + base64encode(filePath) + "&";
						/** 新增加 filepath 
						base64encode(document.forms[form_loop].elements[elems].value) + "&";
						**/
						
						
						//var filename = document.forms[form_loop].elements[elems].name;
						//if (filename == "FILE1")
						//	filename = "FILE1_CHT"
						//submission_string += "|S;e;P;a;R;a;T;e|!!" + filename + "//" + "=" + base64encode(document.forms[form_loop].elements[elems].value) + "&";
						if(filePath == "")
							isUpload = "false";
						else
							isUpload = "true";
					} else {				
						submission_string += document.forms[form_loop].elements[elems].name + "=" + base64encode(document.forms[form_loop].elements[elems].value) + "&";
					}
				}
			}
		}
	}
	request.submission_string = submission_string;
	
	
	//組MIME Data
	strSessionID = "TxCode=" + strTxCode + "&&Function=" + strFunction + "&&BizID=" + strBizId + "&&SESSIONID=" + strSESSIONID;
	strSessionID = strSessionID + "&&VERIFYUSER=" + strVERIFYUSER + "&&COUNTRYCODE=" + strCOUNTRYCODE + "&&CUSTOMERID=" + strCUSTOMERID;
	strSessionID = strSessionID + "&&CAID=" + strCAID + "&&P1=" + strP1 + "&&P2=" + strP2 + "&&P3=" + strP3 + "&&P4=" + strP4 + "&&P5=" + strP5 + "&&";
	request.strSessionID = strSessionID;

	
	//沒有IsUploadFile欄位即預設檔案簽章
	if(document.all.IsUploadFile == undefined){
		if(isUpload == "true"){
			request.strType = 0;
		}else{
			request.strType = 1;
		}
	}else{
		if(document.all.IsUploadFile.value == "1" || (document.all.IsUploadFile.value == "false"))
			request.strType = 1;
		else
			request.strType = 0;
	}
	
	var type = (request.strType == 1) ? 1 : 2;  //(單筆:1, 整批:2)
	


	//assign wording
	SetUIwordingCHT(request);
	
	sendHttpRequest(getCertServerUrl(), 'POST', JSON.stringify(request), {
		onSuccess: function (responseText) {
			if (responseText) {
				var data = JSON.parse(responseText);
				console.log(responseText);
				if (data.ret == 0x0) {
					if (data.strCertExpireBefore != "" && data.strExpireTime != "") {
						
						//Larry 1018 language寫死
						window.open(urllocation + "/RAWeb/RenewalNotice1.jsp?deadline=" + data.strExpireTime, "", "Height=210px, Width=380px, toolbar=no, menubar=no, scrollbars=no, resizable=no, location=no, status=no");
					}
					alert(ParseErrorMessage(data.errMsg, 'DETAIL'));
					//console.log(data.errMsg);
					updateTransResult(true); 

					$.unblockUI();
					return true;
				} else {
					//console.log(data.ret);
					showErrorMessageCHT(data.ret, data);

					updateTransResult(false); 

					$.unblockUI();
					return false;
				}
			}

			$.unblockUI();
		},
		onError: showErrorMessageCHT
	}, true);
}


/* 呼叫CHT進行交易的函式(新增) - End */

function getErrorCode(errMsg, name){
  var vars = errMsg.split("&&");
  var varStr = '';
  for (var i=0; i < vars.length; i++) {
     var pair = vars[i].split("=");
     if ( pair[0] == name ) {
     	varStr=pair[1];
     }	
  }	
  return varStr;
}

function ParseErrorMessage(errMsg, name){
  var vars = errMsg.split("&&");
  var varStr = '';
  for (var i=0; i < vars.length; i++) {
    var pair = vars[i].split("=");
    if ( pair[0] == name ) {
      var rep = pair[1].replace(/\\u/g, "||");
      //alert("rep --> " + rep.toUpperCase() + "---");
      rep = rep.toUpperCase();
      var varArr = rep.split("||");
      for (var j=1; j < varArr.length; j++) {
          var uni = '"\\u' + varArr[j]+'"';
          //alert(varArr[j]  );
          if (/^[A-F\d]{4}$/.test(varArr[j])){
            varStr = varStr + eval(uni);
          }
          //alert("varStr = " + varStr);
      }      
      return varStr;
    }
  }
  alert('Query Variable ' + name  + ' not found');
}

