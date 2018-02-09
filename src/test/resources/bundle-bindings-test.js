function render(view, params, stream, options, callback) {
  stream.writeln(firstBinding.run("Hello"));
  stream.writeln(secondBinding.run("Bye"));
  stream.flush();
}
