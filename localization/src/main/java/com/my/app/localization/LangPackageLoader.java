package com.my.app.localization;

import java.util.List;

public interface LangPackageLoader {
    List<Object> provideLanguagesForComponent(String component);
}
