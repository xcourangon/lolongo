package org.lolongo.matcher;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.lolongo.Ref;
import org.lolongo.RefException;

public class RefExceptionMatcher extends BaseMatcher<RefException> {

    private Ref<?> ref;

    public RefExceptionMatcher(Ref<?> ref) {
        this.ref = ref;
    }

    @Override
    public boolean matches(Object item) {
        return ((RefException) item).getRef().equals(ref);
    }

    @Override
    public void describeTo(Description description) {
        description.appendText(ref.toString());
    }

    @Override
    public void describeMismatch(Object item, Description description) {
        description.appendText("was ").appendValue(((RefException) item).getRef());
    }
}
