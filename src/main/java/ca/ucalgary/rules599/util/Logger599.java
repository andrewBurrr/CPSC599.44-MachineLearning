package ca.ucalgary.rules599.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.LoggingEvent;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;

public class Logger599 {

    private Logger log;

    public Logger599(String name) {
        log = LoggerFactory.getLogger(name);
    }

    private FormattingTuple formatMsg(String corrID, String format, Object message) {
        return  MessageFormatter.format(corrID + " " + format, message);
    }

    private FormattingTuple formatMsg(String corrID, String format, Object... arguments) {
        return MessageFormatter.arrayFormat(corrID + " " + format, arguments);
    }

    public void trace(String msg) {
        log.trace( msg);
        return;
    }

    public void trace(String corrID,String format, Object param1) {
        FormattingTuple tp = formatMsg(corrID, format, param1);
        log.trace(tp.getMessage(),tp.getThrowable());
    }

    public void trace(String corrID,String format, Object param1, Object param2) {
        FormattingTuple tp = formatMsg(corrID, format, param1,param2);
        log.trace(tp.getMessage(),tp.getThrowable());

    }

    public void trace(String corrID,String format, Object... argArray) {
        FormattingTuple tp = formatMsg(corrID, format, argArray);
        log.trace(tp.getMessage(),tp.getThrowable());

    }

    public void trace(String corrID,String msg, Throwable t) {
        log.trace(new StringBuilder(corrID).append(" ").append(msg).toString(), t);
        return;
    }

    public void debug(String msg) {
        log.debug(" {}",msg);
    }

    public void debug(String corrID,String format, Object param1)
    {
        FormattingTuple tp = formatMsg(corrID, format, param1);
        log.debug(tp.getMessage(),tp.getThrowable());
    }

    public void debug(String corrID,String format, Object param1, Object param2) {
        FormattingTuple tp = formatMsg(corrID, format, param1,param2);
        log.debug(tp.getMessage(),tp.getThrowable());

    }

    public void debug(String corrID,String format, Object... argArray) {
        FormattingTuple tp = formatMsg(corrID, format, argArray);
        log.debug(tp.getMessage(),tp.getThrowable());
    }


    public void debug(String corrID,String msg, Throwable t) {
        log.debug(corrID+msg,t);
    }


    public void info(String msg) {
        log.info(" {}",msg);
    }


    public void info(String corrID,String format, Object arg)
    {
        FormattingTuple tp = formatMsg(corrID, format, arg);
        log.info(tp.getMessage(),tp.getThrowable());
    }

    public void info(String corrID,String format, Object arg1, Object arg2) {
        FormattingTuple tp = formatMsg(corrID, format, arg1,arg2);
        log.info(tp.getMessage(),tp.getThrowable());
    }

    public void info(String corrID,String format, Object... argArray)
    {
        FormattingTuple tp = formatMsg(corrID, format, argArray);
        log.info(tp.getMessage(),tp.getThrowable());
    }


    public void info(String corrID,String msg, Throwable t) {
        log.info(new StringBuilder(corrID).append(" ").append(msg).toString(), t);
    }


    public void warn(String msg) {
        log.warn(" {}",msg);
    }

    public void warn(String corrID,String format, Object arg)
    {
        FormattingTuple tp = formatMsg(corrID, format, arg);
        log.warn(tp.getMessage(),tp.getThrowable());
    }


    public void warn(String corrID,String format, Object arg1, Object arg2) {
        FormattingTuple tp = formatMsg(corrID, format, arg1,arg2);
        log.warn(tp.getMessage(),tp.getThrowable());
    }


    public void warn(String corrID,String format, Object... argArray)
    {
        FormattingTuple tp = formatMsg(corrID, format, argArray);
        log.warn(tp.getMessage(),tp.getThrowable());
    }


    public void warn(String corrID,String msg, Throwable t) {
        log.warn(new StringBuilder(corrID).append(" ").append(msg).toString(), t);
    }


    public void error(String msg) {
        log.error("{}",msg);
    }


    public void error(String corrID,String format, Object arg) {
        FormattingTuple tp = formatMsg(corrID, format, arg);
        log.error(tp.getMessage(),tp.getThrowable());
    }


    public void error(String corrID,String format, Object arg1, Object arg2) {
        FormattingTuple tp = formatMsg(corrID, format, arg1,arg2);
        log.error(tp.getMessage(),tp.getThrowable());

    }

    public void error(String corrID,String format, Object... argArray) {
        FormattingTuple tp = formatMsg(corrID, format, argArray);
        log.error(tp.getMessage(),tp.getThrowable());
    }

    public void error(String corrID,String msg, Throwable t) {
        log.error(new StringBuilder(corrID).append(" ").append(msg).toString(), t);
    }

    public void entering(String msg) {
        log.info("Entering {}",msg);
    }
    public void exiting(String msg) {
        log.info("Exiting  {}",msg);
    }

    public void log(LoggingEvent event) {
        FormattingTuple tp = MessageFormatter.arrayFormat(event.getMessage(), event.getArgumentArray(), event.getThrowable());
        log.info(tp.getMessage(), event.getThrowable());
    }


}