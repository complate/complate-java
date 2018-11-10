function render(stream, tag, params) {
  stream.writeln(tag);
  stream.writeln(params.title);
  stream.flush();
}
