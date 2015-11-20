package org.lolongo.data;

import org.lolongo.Data;
import org.lolongo.Ref;

public class StringData extends Data<String> {

    public StringData(Ref<StringData> ref, String value) {
        super(ref, value);
    }
    public StringData(String value) {
        super(value);
    }
}
