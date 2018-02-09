function render(view, params, stream, options, callback) {
  stream.writeln(global);
  stream.flush();
}
