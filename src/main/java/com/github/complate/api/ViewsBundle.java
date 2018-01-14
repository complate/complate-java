package com.github.complate.api;

import java.io.IOException;
import java.io.InputStream;

public interface ViewsBundle {

    InputStream getInputStream() throws IOException;

    String getDescription();
}
