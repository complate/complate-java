function render(view, params, stream) {
    stream.writeln(view);
    stream.writeln(params.title);
    stream.flush();
}
