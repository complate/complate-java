/*
 * Copyright 2019 complate.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
