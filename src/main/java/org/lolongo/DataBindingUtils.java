package org.lolongo;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataBindingUtils {

    private static Logger logger = LoggerFactory.getLogger(DataBindingUtils.class);

    private DataBindingUtils() {
	// Hide Utility Class Constructor
    }

    public static Collection<Ref<?>> getInputBindings(final Context context, final Function f)
	    throws DataBindingException {
	final Collection<Ref<?>> dataBindings = getDataBindings(f, InputBinding.class);
	if (f instanceof InputDataBindingProvider) {
	    final InputDataBindingProvider dataBindingProvider = (InputDataBindingProvider) f;
	    final Collection<? extends Ref<?>> inputBindings = dataBindingProvider.getInputBindings(context);
	    if (inputBindings != null) {
		dataBindings.addAll(inputBindings);
	    }
	}
	logger.debug("input bindings for {} in {} :", f, context);
	for (final Ref<?> ref : dataBindings) {
	    logger.debug(" - {}", ref);
	}
	return dataBindings;
    }

    public static Collection<Ref<?>> getOutputBindings(final Context context, final Function f)
	    throws DataBindingException {
	final Collection<Ref<?>> dataBindings = getDataBindings(f, OutputBinding.class);
	if (f instanceof OutputDataBindingProvider) {
	    final OutputDataBindingProvider dataBindingProvider = (OutputDataBindingProvider) f;
	    final Collection<? extends Ref<?>> outputBindings = dataBindingProvider.getOutputBindings(context);
	    if (outputBindings != null) {
		dataBindings.addAll(outputBindings);
	    }
	}
	logger.debug("output bindings for {} in {} :", f, context);
	for (final Ref<?> dataRef : dataBindings) {
	    logger.debug(" - {}", dataRef);
	}
	return dataBindings;
    }

    public static Collection<Ref<?>> getDataBindings(Function f, Class<? extends Annotation> bindingAnnotationClass,
	    Class<?> implementationClass) {

	final Collection<Ref<?>> dataBindings = new ArrayList<>();

	final Field[] fields = implementationClass.getDeclaredFields();
	for (final Field field : fields) {
	    if (field.isAnnotationPresent(bindingAnnotationClass)) {
		try {
		    if (field.isAccessible() == false) {
			field.setAccessible(true);
		    }
		    final Object object = field.get(f);
		    if (object != null) {
			dataBindings.add((Ref<?>) object);
		    }
		} catch (final IllegalAccessException e) {
		    assert false;
		}
	    }
	}

	final Class<?> superclass = implementationClass.getSuperclass();
	if (Function.class.isAssignableFrom(superclass)) {
	    dataBindings.addAll(getDataBindings(f, bindingAnnotationClass, superclass));
	}

	return dataBindings;
    }

    public static Collection<Ref<?>> getDataBindings(Function f, Class<? extends Annotation> bindingAnnotationClass) {

	return getDataBindings(f, bindingAnnotationClass, f.getClass());
    }
}
