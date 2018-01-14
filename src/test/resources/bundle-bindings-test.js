function render(stream) {
  stream.writeln(firstBinding.run("Hello"));
  stream.writeln(secondBinding.run("Bye"));
  stream.flush();
}
