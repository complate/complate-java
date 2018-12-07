var render=(function(){'use strict';if(typeof global === "undefined" && typeof window !== "undefined") {
	window.global = window;
}

function _classCallCheck(instance, Constructor) {
  if (!(instance instanceof Constructor)) {
    throw new TypeError("Cannot call a class as a function");
  }
}

function _defineProperties(target, props) {
  for (var i = 0; i < props.length; i++) {
    var descriptor = props[i];
    descriptor.enumerable = descriptor.enumerable || false;
    descriptor.configurable = true;
    if ("value" in descriptor) descriptor.writable = true;
    Object.defineProperty(target, descriptor.key, descriptor);
  }
}

function _createClass(Constructor, protoProps, staticProps) {
  if (protoProps) _defineProperties(Constructor.prototype, protoProps);
  if (staticProps) _defineProperties(Constructor, staticProps);
  return Constructor;
}

function _toConsumableArray(arr) {
  return _arrayWithoutHoles(arr) || _iterableToArray(arr) || _nonIterableSpread();
}

function _arrayWithoutHoles(arr) {
  if (Array.isArray(arr)) {
    for (var i = 0, arr2 = new Array(arr.length); i < arr.length; i++) arr2[i] = arr[i];

    return arr2;
  }
}

function _iterableToArray(iter) {
  if (Symbol.iterator in Object(iter) || Object.prototype.toString.call(iter) === "[object Arguments]") return Array.from(iter);
}

function _nonIterableSpread() {
  throw new TypeError("Invalid attempt to spread non-iterable instance");
}var BLANKS = [undefined, null, false];
function simpleLog(type, msg) {
  console.log("[".concat(type, "] ").concat(msg));
}
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
    return BLANKS.indexOf(item) !== -1 ? memo :
    memo.concat(item.pop ? flatCompact(item) : item);
  }, []);
}
function blank(value) {
  return BLANKS.indexOf(value) !== -1;
}
function repr(value) {
  var jsonify = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : true;
  return "`".concat(jsonify ? JSON.stringify(value) : value, "`");
}
function noop() {}var Fragment = {};
var VOID_ELEMENTS = {};
["area", "base", "br", "col", "embed", "hr", "img", "input", "keygen", "link", "meta", "param", "source", "track", "wbr"].forEach(function (tag) {
  VOID_ELEMENTS[tag] = true;
});
function generateHTML(tag, params) {
  for (var _len = arguments.length, children = new Array(_len > 2 ? _len - 2 : 0), _key = 2; _key < _len; _key++) {
    children[_key - 2] = arguments[_key];
  }
  return function (stream, options, callback) {
    var _ref = options || {},
        nonBlocking = _ref.nonBlocking,
        _ref$log = _ref.log,
        log = _ref$log === void 0 ? simpleLog : _ref$log,
        _ref$_idRegistry = _ref._idRegistry,
        _idRegistry = _ref$_idRegistry === void 0 ? {} : _ref$_idRegistry;
    if (tag !== Fragment) {
      var attribs = generateAttributes(params, {
        tag: tag,
        log: log,
        _idRegistry: _idRegistry
      });
      stream.write("<".concat(tag).concat(attribs, ">"));
    }
    children = flatCompact(children);
    var isVoid = VOID_ELEMENTS[tag];
    var closingTag = isVoid || tag === Fragment ? null : tag;
    var total = children.length;
    if (total === 0) {
      closeElement(stream, closingTag, callback);
    } else {
      if (isVoid) {
        log("error", "void elements must not have children: `<".concat(tag, ">`"));
      }
      var close = awaitAll(total, function (_) {
        closeElement(stream, closingTag, callback);
      });
      processChildren(stream, children, 0, {
        tag: tag,
        nonBlocking: nonBlocking,
        log: log,
        _idRegistry: _idRegistry
      }, close);
    }
  };
}
function HTMLString(str) {
  if (blank(str) || !str.substr) {
    throw new Error("invalid ".concat(repr(this.constructor.name, false), ": ").concat(repr(str)));
  }
  this.value = str;
}
function htmlEncode(str, attribute) {
  var res = str.replace(/&/g, "&amp;").replace(/</g, "&lt;").replace(/>/g, "&gt;");
  if (attribute) {
    res = res.replace(/"/g, "&quot;").replace(/'/g, "&#x27;");
  }
  return res;
}
function processChildren(stream, children, startIndex, options, callback) {
  var _loop2 = function _loop2(i) {
    var child = children[i];
    if (!child.call) {
      var content = child instanceof HTMLString ?
      child.value : htmlEncode(child.toString());
      stream.write(content);
      callback();
      return "continue";
    }
    var nonBlocking = options.nonBlocking,
        log = options.log,
        _idRegistry = options._idRegistry;
    var generatorOptions = {
      nonBlocking: nonBlocking,
      log: log,
      _idRegistry: _idRegistry
    };
    if (child.length !== 1) {
      child(stream, generatorOptions, callback);
      return "continue";
    }
    var fn = function fn(element) {
      element(stream, generatorOptions, callback);
      var next = i + 1;
      if (next < children.length) {
        processChildren(stream, children, next, options, callback);
      }
    };
    if (!nonBlocking) {
      var invoked = false;
      var _fn = fn;
      fn = function fn() {
        invoked = true;
        return _fn.apply(null, arguments);
      };
      var _child = child;
      child = function child() {
        var res = _child.apply(null, arguments);
        if (!invoked) {
          var msg = "invalid non-blocking operation detected";
          throw new Error("".concat(msg, ": `").concat(options.tag, "`"));
        }
        return res;
      };
    }
    child(fn);
    return "break";
  };
  _loop: for (var i = startIndex; i < children.length; i++) {
    var _ret = _loop2(i);
    switch (_ret) {
      case "continue":
        continue;
      case "break":
        break _loop;
    }
  }
}
function closeElement(stream, tag, callback) {
  if (tag !== null) {
    stream.write("</".concat(tag, ">"));
  }
  stream.flush();
  callback();
}
function generateAttributes(params, _ref2) {
  var tag = _ref2.tag,
      log = _ref2.log,
      _idRegistry = _ref2._idRegistry;
  if (!params) {
    return "";
  }
  if (_idRegistry && params.id !== undefined) {
    var id = params.id;
    if (_idRegistry[id]) {
      log("error", "duplicate HTML element ID: ".concat(repr(params.id)));
    }
    _idRegistry[id] = true;
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
          reportAttribError("invalid HTML attribute name: ".concat(repr(name)), tag, log);
          break;
        }
        if (typeof value === "number") {
          value = value.toString();
        } else if (!value.substr) {
          reportAttribError("invalid value for HTML attribute `".concat(name, "`: ") + "".concat(repr(value), " (expected string)"), tag, log);
          break;
        }
        memo.push("".concat(name, "=\"").concat(htmlEncode(value, true), "\""));
    }
    return memo;
  }, []);
  return attribs.length === 0 ? "" : " ".concat(attribs.join(" "));
}
function reportAttribError(msg, tag, log) {
  log("error", "".concat(msg, " - did you perhaps intend to use `").concat(tag, "` as a macro?"));
}function createElement(element, params) {
  for (var _len = arguments.length, children = new Array(_len > 2 ? _len - 2 : 0), _key = 2; _key < _len; _key++) {
    children[_key - 2] = arguments[_key];
  }
  return element.call ? element.apply(void 0, [params === null ? {} : params].concat(_toConsumableArray(flatCompact(children)))) : generateHTML.apply(void 0, [element, params].concat(children));
}
var Renderer =
function () {
  function Renderer() {
    var _this = this;
    var _ref = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : {},
        _ref$doctype = _ref.doctype,
        doctype = _ref$doctype === void 0 ? "<!DOCTYPE html>" : _ref$doctype,
        log = _ref.log;
    _classCallCheck(this, Renderer);
    this.doctype = doctype;
    this.log = log;
    this._macroRegistry = {};
    ["registerView", "renderView"].forEach(function (meth) {
      _this[meth] = _this[meth].bind(_this);
    });
  }
  _createClass(Renderer, [{
    key: "registerView",
    value: function registerView(macro) {
      var name = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : macro.name;
      var replace = arguments.length > 2 ? arguments[2] : undefined;
      if (!name) {
        throw new Error("missing name for macro: `".concat(macro, "`"));
      }
      var macros = this._macroRegistry;
      if (macros[name] && !replace) {
        throw new Error("invalid macro name: `".concat(name, "` already registered"));
      }
      macros[name] = macro;
      return name;
    }
  }, {
    key: "renderView",
    value: function renderView(view, params, stream) {
      var _this2 = this;
      var _ref2 = arguments.length > 3 && arguments[3] !== undefined ? arguments[3] : {},
          fragment = _ref2.fragment;
      var callback = arguments.length > 4 ? arguments[4] : undefined;
      if (!fragment) {
        stream.writeln(this.doctype);
      }
      if (fragment) {
        if (!params) {
          params = {};
        }
        params._layout = false;
      }
      var viewName = view && view.substr && view;
      var macro = viewName ? this._macroRegistry[viewName] : view;
      if (!macro) {
        throw new Error("unknown view macro: `".concat(view, "` is not registered"));
      }
      var log = this.log && function (level, message) {
        return _this2.log(level, "<".concat(viewName || macro.name, "> ").concat(message));
      };
      var element = createElement(macro, params);
      if (callback) {
        element(stream, {
          nonBlocking: true,
          log: log
        }, callback);
      } else {
        element(stream, {
          nonBlocking: false,
          log: log
        }, noop);
      }
    }
  }]);
  return Renderer;
}();function DefaultLayout(_ref) {
  var title = _ref.title,
      stylesheets = _ref.stylesheets,
      bodyClass = _ref.bodyClass;
  for (var _len = arguments.length, children = new Array(_len > 1 ? _len - 1 : 0), _key = 1; _key < _len; _key++) {
    children[_key - 1] = arguments[_key];
  }
  return createElement("html", null, createElement("head", null, createElement("meta", {
    charset: "utf-8"
  }), createElement("title", null, title), renderStyleSheets(stylesheets)), createElement("body", {
    class: bodyClass
  }, children));
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
    return createElement("link", {
      rel: "stylesheet",
      href: uri,
      integrity: hash,
      crossorigin: "anonymous"
    });
  });
}function SiteIndex(_ref) {
  var title = _ref.title,
      _layout = _ref._layout;
  var content = createElement("p", null, "lorem ipsum dolor sit amet");
  return _layout === false ? content : createElement(DefaultLayout, {
    title: title
  }, createElement("h1", null, title), content);
}function Panel(_ref) {
  var title = _ref.title;
  for (var _len = arguments.length, content = new Array(_len > 1 ? _len - 1 : 0), _key = 1; _key < _len; _key++) {
    content[_key - 1] = arguments[_key];
  }
  return createElement("div", {
    class: "panel panel-default"
  }, createElement("div", {
    class: "panel-heading"
  }, createElement("h3", {
    class: "panel-title"
  }, title)), createElement("div", {
    class: "panel-body"
  }, content));
}function ListGroup(_) {
  for (var _len = arguments.length, items = new Array(_len > 1 ? _len - 1 : 0), _key = 1; _key < _len; _key++) {
    items[_key - 1] = arguments[_key];
  }
  return createElement("ul", {
    class: "list-group"
  }, items.map(function (item) {
    return createElement("li", {
      class: "list-group-item"
    }, item);
  }));
}var STYLESHEETS = [{
  uri: "https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css",
  hash: "sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u"
}];
function BootstrapSample(_ref) {
  var title = _ref.title;
  return createElement(DefaultLayout, {
    title: title
    ,
    stylesheets: STYLESHEETS,
    bodyClass: "container-fluid"
  }, createElement("h1", null, title), createElement(Panel, {
    title: "Welcome"
  }, createElement("p", null, "lorem ipsum"), createElement(ListGroup, null, "foo", createElement("em", null, "bar"), "baz"), createElement("p", null, "dolor sit amet")));
}var views = /*#__PURE__*/Object.freeze({SiteIndex: SiteIndex,BootstrapSample: BootstrapSample});var renderer = new Renderer();
Object.keys(views).map(function (key) {
  return views[key];
}).forEach(function (view) {
  return renderer.registerView(view);
});
var index = renderer.renderView;return index;}());