package com.github.fzakaria.slf4j.timbre;

import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MarkerIgnoringBase;
import org.slf4j.helpers.MessageFormatter;


public class TimbreLoggerAdapter extends MarkerIgnoringBase {

    private final TimbreLoggerAdapterHelper helper;

    public TimbreLoggerAdapter() {
        this.helper = new TimbreLoggerAdapterHelper();
    }


    @Override
    public boolean isTraceEnabled() {
        return helper.isTraceEnabled();
    }

    @Override
    public void trace(String s) {
        helper.trace(s);
    }

    @Override
    public void trace(String s, Object o) {
        FormattingTuple ft = MessageFormatter.format(s, o);
        helper.trace(ft.getMessage(), ft.getThrowable());
    }

    @Override
    public void trace(String s, Object o, Object o1) {
        FormattingTuple ft = MessageFormatter.format(s, o, o1);
        helper.trace(ft.getMessage(), ft.getThrowable());
    }

    @Override
    public void trace(String s, Object... objects) {
        FormattingTuple ft = MessageFormatter.arrayFormat(s, objects);
        helper.trace(ft.getMessage(), ft.getThrowable());
    }

    @Override
    public void trace(String s, Throwable throwable) {
        helper.trace(s, throwable);
    }

    @Override
    public boolean isDebugEnabled() {
        return helper.isDebugEnabled();
    }

    @Override
    public void debug(String s) {
        helper.debug(s);
    }

    @Override
    public void debug(String s, Object o) {
        FormattingTuple ft = MessageFormatter.format(s, o);
        helper.debug(ft.getMessage(), ft.getThrowable());
    }

    @Override
    public void debug(String s, Object o, Object o1) {
        FormattingTuple ft = MessageFormatter.format(s, o, o1);
        helper.debug(ft.getMessage(), ft.getThrowable());
    }

    @Override
    public void debug(String s, Object... objects) {
        FormattingTuple ft = MessageFormatter.arrayFormat(s, objects);
        helper.debug(ft.getMessage(), ft.getThrowable());
    }

    @Override
    public void debug(String s, Throwable throwable) {
        helper.debug(s, throwable);
    }

    @Override
    public boolean isInfoEnabled() {
        return helper.isInfoEnabled();
    }

    @Override
    public void info(String s) {
        helper.info(s);
    }

    @Override
    public void info(String s, Object o) {
        FormattingTuple ft = MessageFormatter.format(s, o);
        helper.info(ft.getMessage(), ft.getThrowable());
    }

    @Override
    public void info(String s, Object o, Object o1) {
        FormattingTuple ft = MessageFormatter.format(s, o, o1);
        helper.info(ft.getMessage(), ft.getThrowable());
    }

    @Override
    public void info(String s, Object... objects) {
        FormattingTuple ft = MessageFormatter.arrayFormat(s, objects);
        helper.info(ft.getMessage(), ft.getThrowable());
    }

    @Override
    public void info(String s, Throwable throwable) {
        helper.info(s, throwable);
    }

    @Override
    public boolean isWarnEnabled() {
        return helper.isWarnEnabled();
    }

    @Override
    public void warn(String s) {
        helper.warn(s);
    }

    @Override
    public void warn(String s, Object o) {
        FormattingTuple ft = MessageFormatter.format(s, o);
        helper.warn(ft.getMessage(), ft.getThrowable());
    }

    @Override
    public void warn(String s, Object... objects) {
        FormattingTuple ft = MessageFormatter.arrayFormat(s, objects);
        helper.warn(ft.getMessage(), ft.getThrowable());
    }

    @Override
    public void warn(String s, Object o, Object o1) {
        FormattingTuple ft = MessageFormatter.format(s, o, o1);
        helper.warn(ft.getMessage(), ft.getThrowable());
    }

    @Override
    public void warn(String s, Throwable throwable) {
        helper.warn(s, throwable);
    }

    @Override
    public boolean isErrorEnabled() {
        return helper.isErrorEnabled();
    }

    @Override
    public void error(String s) {
        helper.error(s);
    }

    @Override
    public void error(String s, Object o) {
        FormattingTuple ft = MessageFormatter.format(s, o);
        helper.error(ft.getMessage(), ft.getThrowable());
    }

    @Override
    public void error(String s, Object o, Object o1) {
        FormattingTuple ft = MessageFormatter.format(s, o, o1);
        helper.error(ft.getMessage(), ft.getThrowable());
    }

    @Override
    public void error(String s, Object... objects) {
        FormattingTuple ft = MessageFormatter.arrayFormat(s, objects);
        helper.error(ft.getMessage(), ft.getThrowable());
    }

    @Override
    public void error(String s, Throwable throwable) {
        helper.error(s, throwable);
    }
}