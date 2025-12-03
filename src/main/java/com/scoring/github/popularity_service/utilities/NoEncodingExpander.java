package com.scoring.github.popularity_service.utilities;

import feign.Param;

public class NoEncodingExpander implements Param.Expander {
    @Override
    public String expand(Object value) {
        return value.toString(); // return raw string, no encoding
    }
}
