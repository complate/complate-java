function render(stream, tag, params) {
  stream.writeln(firstBinding.run("Hello"));
  stream.writeln(secondBinding.run("Bye"));
  stream.flush();
}
