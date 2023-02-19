package com.capgemini.bedwards.almon.almontest.helpers;

import com.google.common.io.Resources;
import lombok.SneakyThrows;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class TestResourceUtil {
    @SneakyThrows
    public static String readResource(String url, String... prefixes) {
        String prefix = String.join("/", prefixes);
        return Resources.toString(Objects.requireNonNull(TestResourceUtil.class.getResource(String.join("/", prefix, url))), StandardCharsets.UTF_8);
    }
}
