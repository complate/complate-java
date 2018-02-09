var render = (function () {
'use strict';

if(typeof global === "undefined" && typeof window !== "undefined") {
	window.global = window;
}

var BLANKS = [undefined, null, false];
function awaitAll(total, callback) {
	var i = 0;
	return function (_) {
		i++;
		if (i === total) {
			callback();
		}
	};
}
function flatCompact(items) {
	return items.reduce(function (memo, item) {
		return BLANKS.indexOf(item) !== -1 ? memo : memo.concat(item.pop ? flatCompact(item) : item);
	}, []);
}
function noop() {}

var classCallCheck = function (instance, Constructor) {
  if (!(instance instanceof Constructor)) {
    throw new TypeError("Cannot call a class as a function");
  }
};

var createClass = function () {
  function defineProperties(target, props) {
    for (var i = 0; i < props.length; i++) {
      var descriptor = props[i];
      descriptor.enumerable = descriptor.enumerable || false;
      descriptor.configurable = true;
      if ("value" in descriptor) descriptor.writable = true;
      Object.defineProperty(target, descriptor.key, descriptor);
    }
  }

  return function (Constructor, protoProps, staticProps) {
    if (protoProps) defineProperties(Constructor.prototype, protoProps);
    if (staticProps) defineProperties(Constructor, staticProps);
    return Constructor;
  };
}();







































var toArray = function (arr) {
  return Array.isArray(arr) ? arr : Array.from(arr);
};

var toConsumableArray = function (arr) {
  if (Array.isArray(arr)) {
    for (var i = 0, arr2 = Array(arr.length); i < arr.length; i++) arr2[i] = arr[i];

    return arr2;
  } else {
    return Array.from(arr);
  }
};

var VOID_ELEMENTS = {};
["area", "base", "br", "col", "embed", "hr", "img", "input", "keygen", "link", "meta", "param", "source", "track", "wbr"].forEach(function (tag) {
	VOID_ELEMENTS[tag] = true;
});
function generateHTML(tag, params) {
	for (var _len = arguments.length, children = Array(_len > 2 ? _len - 2 : 0), _key = 2; _key < _len; _key++) {
		children[_key - 2] = arguments[_key];
	}
	return function (stream, nonBlocking, callback) {
		stream.write("<" + tag + generateAttributes(params, tag) + ">");
		children = flatCompact(children);
		var total = children.length;
		if (total === 0) {
			closeElement(stream, tag, callback);
		} else {
			var close = awaitAll(total, function (_) {
				closeElement(stream, tag, callback);
			});
			processChildren(stream, children, nonBlocking, close);
		}
	};
}
function HTMLString(str) {
	this.value = str;
}
function htmlEncode(str, attribute) {
	var res = str.replace(/&/g, "&amp;").replace(/</g, "&lt;").replace(/>/g, "&gt;");
	if (attribute) {
		res = res.replace(/"/g, "&quot;").replace(/'/g, "&#x27;");
	}
	return res;
}
function processChildren(stream, children, nonBlocking, callback) {
	var _children = toArray(children),
	    child = _children[0],
	    remainder = _children.slice(1);
	if (child.call) {
		if (child.length !== 1) {
			child(stream, nonBlocking, callback);
		} else {
			var fn = function fn(element) {
				element(stream, nonBlocking, callback);
				if (remainder.length) {
					processChildren(stream, remainder, nonBlocking, callback);
				}
			};
			if (nonBlocking) {
				child(fn);
			} else {
				var invoked = false;
				var _fn = fn;
				fn = function fn() {
					invoked = true;
					return _fn.apply(null, arguments);
				};
				child(fn);
				if (!nonBlocking && !invoked) {
					throw new Error("invalid non-blocking operation detected");
				}
			}
			return;
		}
	} else {
		var content = child instanceof HTMLString ? child.value : htmlEncode(child.toString());
		stream.write(content);
		callback();
	}
	if (remainder.length) {
		processChildren(stream, remainder, nonBlocking, callback);
	}
}
function closeElement(stream, tag, callback) {
	if (!VOID_ELEMENTS[tag]) {
		stream.write("</" + tag + ">");
	}
	stream.flush();
	callback();
}
function generateAttributes(params, tag) {
	if (!params) {
		return "";
	}
	var attribs = Object.keys(params).reduce(function (memo, name) {
		var value = params[name];
		switch (value) {
			case null:
			case undefined:
				break;
			case true:
				memo.push(name);
				break;
			case false:
				break;
			default:
				if (/ |"|'|>|'|\/|=/.test(name)) {
					abort("invalid HTML attribute name: " + repr(name), tag);
				}
				if (typeof value === "number") {
					value = value.toString();
				} else if (!value.substr) {
					abort("invalid value for HTML attribute `" + name + "`: " + (repr(value) + " (expected string)"), tag);
				}
				memo.push(name + "=\"" + htmlEncode(value, true) + "\"");
		}
		return memo;
	}, []);
	return attribs.length === 0 ? "" : " " + attribs.join(" ");
}
function abort(msg, tag) {
	if (tag) {
		msg += " - did you perhaps intend to use `" + tag + "` as a macro?";
	}
	throw new Error(msg);
}
function repr(value) {
	return "`" + JSON.stringify(value) + "`";
}

function createElement(element, params) {
	for (var _len = arguments.length, children = Array(_len > 2 ? _len - 2 : 0), _key = 2; _key < _len; _key++) {
		children[_key - 2] = arguments[_key];
	}
	return element.call ? element.apply(undefined, [params === null ? {} : params].concat(toConsumableArray(flatCompact(children)))) : generateHTML.apply(undefined, [element, params].concat(children));
}
var Renderer$1 = function () {
	function Renderer() {
		var doctype = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : "<!DOCTYPE html>";
		classCallCheck(this, Renderer);
		this._doctype = doctype;
		this._macroRegistry = {};
		this.registerView.bind(this);
		this.renderView.bind(this);
	}
	createClass(Renderer, [{
		key: "registerView",
		value: function registerView(macro) {
			var name = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : macro.name;
			var replace = arguments[2];
			if (!name) {
				throw new Error("missing name for macro: `" + macro + "`");
			}
			var macros = this._macroRegistry;
			if (macros[name] && !replace) {
				throw new Error("invalid macro name: `" + name + "` already registered");
			}
			macros[name] = macro;
			return name;
		}
	}, {
		key: "renderView",
		value: function renderView(view, params, stream) {
			var _ref = arguments.length > 3 && arguments[3] !== undefined ? arguments[3] : {},
			    fragment = _ref.fragment;
			var callback = arguments[4];
			if (!fragment) {
				stream.writeln(this._doctype);
			}
			if (fragment) {
				if (!params) {
					params = {};
				}
				params._layout = false;
			}
			var macro = view && view.substr ? this._macroRegistry[view] : view;
			if (!macro) {
				throw new Error("unknown view macro: `" + view + "` is not registered");
			}
			var element = createElement(macro, params);
			if (callback) {
				element(stream, true, callback);
			} else {
				element(stream, false, noop);
			}
		}
	}]);
	return Renderer;
}();

function DefaultLayout(_ref) {
	var title = _ref.title,
	    stylesheets = _ref.stylesheets,
	    bodyClass = _ref.bodyClass;
	for (var _len = arguments.length, children = Array(_len > 1 ? _len - 1 : 0), _key = 1; _key < _len; _key++) {
		children[_key - 1] = arguments[_key];
	}
	return createElement(
		"html",
		null,
		createElement(
			"head",
			null,
			createElement("meta", { charset: "utf-8" }),
			createElement(
				"title",
				null,
				title
			),
			renderStyleSheets(stylesheets)
		),
		createElement(
			"body",
			{ "class": bodyClass },
			children
		)
	);
}
function renderStyleSheets(items) {
	if (!items || !items.length) {
		return;
	}
	return items.map(function (stylesheet) {
		if (stylesheet.hash) {
			var uri = stylesheet.uri,
			    hash = stylesheet.hash;
		} else {
			uri = stylesheet;
		}
		return createElement("link", { rel: "stylesheet", href: uri,
			integrity: hash, crossorigin: "anonymous" });
	});
}

function SiteIndex(_ref) {
	var title = _ref.title,
	    _layout = _ref._layout;
	var content = createElement(
		"p",
		null,
		"lorem ipsum dolor sit amet"
	);
	return _layout === false ? content : createElement(
		DefaultLayout,
		{ title: title },
		createElement(
			"h1",
			null,
			title
		),
		content
	);
}

function Panel(_ref) {
	var title = _ref.title;
	for (var _len = arguments.length, content = Array(_len > 1 ? _len - 1 : 0), _key = 1; _key < _len; _key++) {
		content[_key - 1] = arguments[_key];
	}
	return createElement(
		"div",
		{ "class": "panel panel-default" },
		createElement(
			"div",
			{ "class": "panel-heading" },
			createElement(
				"h3",
				{ "class": "panel-title" },
				title
			)
		),
		createElement(
			"div",
			{ "class": "panel-body" },
			content
		)
	);
}

function ListGroup(_) {
	for (var _len = arguments.length, items = Array(_len > 1 ? _len - 1 : 0), _key = 1; _key < _len; _key++) {
		items[_key - 1] = arguments[_key];
	}
	return createElement(
		"ul",
		{ "class": "list-group" },
		items.map(function (item) {
			return createElement(
				"li",
				{ "class": "list-group-item" },
				item
			);
		})
	);
}

var STYLESHEETS = [{
	uri: "https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css",
	hash: "sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u"
}];
function BootstrapSample(_ref) {
	var title = _ref.title;
	return createElement(
		DefaultLayout,
		{ title: title
			, stylesheets: STYLESHEETS, bodyClass: "container-fluid" },
		createElement(
			"h1",
			null,
			title
		),
		createElement(
			Panel,
			{ title: "Welcome" },
			createElement(
				"p",
				null,
				"lorem ipsum"
			),
			createElement(
				ListGroup,
				null,
				"foo",
				createElement(
					"em",
					null,
					"bar"
				),
				"baz"
			),
			createElement(
				"p",
				null,
				"dolor sit amet"
			)
		)
	);
}



var views = Object.freeze({
	SiteIndex: SiteIndex,
	BootstrapSample: BootstrapSample
});

var renderer = new Renderer$1("<!DOCTYPE html>");
Object.keys(views).map(function (key) {
	return views[key];
}).forEach(function (view) {
	return renderer.registerView(view);
});
var index = renderer.renderView.bind(renderer);

return index;

}());
