package com.tmap.mit.map_viewer.cd;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

@Getter
@AllArgsConstructor
public enum ManageLocale {
    //KR(Locale.KOREA, StandardCharsets.UTF_8),
    TR(new Locale("ku", "TR"), StandardCharsets.ISO_8859_1);

    private final Locale locale;
    private final Charset encoding;
}
