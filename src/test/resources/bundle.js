function render(view, params, stream, options, callback) {
  stream.writeln(view);
  stream.writeln(params.title);
  stream.flush();
}
