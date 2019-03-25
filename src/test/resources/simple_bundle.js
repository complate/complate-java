function render(view, params, stream) {
    if (view === 'list') {
        stream.write("Arguments: " + params.a + ", " + params.b + ", " + params.c);
    } else if (view === 'global') {
        stream.write(global);
    } else if (view === 'console') {
        stream.writeln(console);
        stream.writeln(console.log);
        stream.write(console.error);
    } else if (view === 'bindings') {
        stream.write(functionBinding.greet(constantBinding));
    } else {
        stream.write("View not found: " + view);
    }
}
